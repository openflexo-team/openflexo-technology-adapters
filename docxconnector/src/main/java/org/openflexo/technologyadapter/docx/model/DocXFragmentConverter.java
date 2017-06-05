/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Pamela-core, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.docx.model;

import java.io.FileNotFoundException;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.doc.FlexoDocFragment.FragmentConsistencyException;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.task.FlexoTask;
import org.openflexo.model.StringConverterLibrary.Converter;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;

public class DocXFragmentConverter extends Converter<DocXFragment> {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
			.getLogger(DocXFragmentConverter.class.getPackage().getName());

	private FlexoServiceManager serviceManager;

	public DocXFragmentConverter() {
		super(DocXFragment.class);
	}

	public DocXFragmentConverter(FlexoServiceManager serviceManager) {
		this();
		this.serviceManager = serviceManager;
	}

	@Override
	public DocXFragment convertFromString(String value, ModelFactory factory) {
		int endSeparatorIndex = value.lastIndexOf(":");
		int startSeparatorIndex = value.substring(0, endSeparatorIndex).lastIndexOf(":");

		if (endSeparatorIndex > -1 && startSeparatorIndex > -1) {

			String documentURI = value.substring(0, startSeparatorIndex);
			String startElementId = value.substring(startSeparatorIndex + 1, endSeparatorIndex);
			String endElementId = value.substring(endSeparatorIndex + 1);

			// System.out.println("documentURI: " + documentURI);
			// System.out.println("startElementId: " + startElementId);
			// System.out.println("endElementId: " + endElementId);

			// System.out.println("factory: " + factory);

			FlexoResource<DocXDocument> documentResource = null;

			if (serviceManager != null) {
				activateDocXTechnologyAdapter();
				documentResource = serviceManager.getResourceManager().getResource(documentURI, null, DocXDocument.class);

			}
			if (factory instanceof FMLModelFactory) {
				serviceManager = ((FMLModelFactory) factory).getServiceManager();
				activateDocXTechnologyAdapter();
				documentResource = ((FMLModelFactory) factory).getServiceManager().getResourceManager().getResource(documentURI, null,
						DocXDocument.class);
			}

			if (documentResource != null) {
				DocXDocument document;
				try {
					document = documentResource.getResourceData(null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				} catch (ResourceLoadingCancelledException e) {
					e.printStackTrace();
					return null;
				} catch (FlexoException e) {
					e.printStackTrace();
					return null;
				}
				FlexoDocElement<DocXDocument, DocXTechnologyAdapter> startElement = document.getElementWithIdentifier(startElementId);
				FlexoDocElement<DocXDocument, DocXTechnologyAdapter> endElement = document.getElementWithIdentifier(endElementId);

				// System.out.println("startElement = " + startElement);
				// System.out.println("endElement = " + endElement);

				if (startElement == null) {
					logger.warning("Could not find FlexoDocElement identified by " + startElementId);
					// System.out.println("Strategy" + document.getFactory().getIDStrategy());
					// System.out.println(document.debugStructuredContents());
					return null;
				}
				if (endElement == null) {
					logger.warning("Could not find FlexoDocElement identified by " + endElementId);
					return null;
				}

				try {
					return document.getFragment(startElement, endElement);
				} catch (FragmentConsistencyException e) {
					e.printStackTrace();
					return null;
				}
			}

			else {
				logger.warning("Could not find documentResource identified by " + documentURI);
			}
		}
		return null;
	}

	@Override
	public String convertToString(DocXFragment fragment) {
		StringBuffer sb = new StringBuffer();
		sb.append(fragment.getFlexoDocument().getURI());
		sb.append(":" + fragment.getStartElement().getIdentifier());
		sb.append(":" + fragment.getEndElement().getIdentifier());
		return sb.toString();
	}

	/**
	 * Activate the Docx technology adapter in order to retrieve docx resources. Wait until the docx technology adapter is activated.
	 */
	private void activateDocXTechnologyAdapter() {
		FlexoTask activateDocX = serviceManager
				.activateTechnologyAdapter(serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class));
		if (activateDocX != null) {
			serviceManager.getTaskManager().waitTask(activateDocX);
		}
	}
}
