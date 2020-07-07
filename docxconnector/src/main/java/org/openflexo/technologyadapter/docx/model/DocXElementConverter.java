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
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.pamela.factory.ModelFactory;
import org.openflexo.pamela.model.StringConverterLibrary.Converter;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;

public class DocXElementConverter extends Converter<DocXElement> {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
			.getLogger(DocXElementConverter.class.getPackage().getName());

	public DocXElementConverter() {
		super(DocXElement.class);
	}

	@Override
	public DocXElement convertFromString(String value, ModelFactory factory) {
		int separatorIndex = value.lastIndexOf(":");

		if (separatorIndex > -1) {

			String documentURI = value.substring(0, separatorIndex);
			String elementId = value.substring(separatorIndex + 1);

			// System.out.println("documentURI: " + documentURI);
			// System.out.println("elementId: " + elementId);

			// System.out.println("factory: " + factory);

			if (factory instanceof FMLModelFactory) {
				// System.out.println("serviceManager: " + ((FMLModelFactory) factory).getServiceManager());
				FlexoResource<DocXDocument> documentResource = ((FMLModelFactory) factory).getServiceManager().getResourceManager()
						.getResource(documentURI, null, DocXDocument.class);
				// System.out.println("document=" + documentResource);

				if (documentResource != null) {
					DocXDocument document;
					try {
						document = documentResource.getResourceData();
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
					FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element = document.getElementWithIdentifier(elementId);
					System.out.println("element = " + element);
					return (DocXElement) element;

				}
			}
		}
		return null;
	}

	@Override
	public String convertToString(DocXElement element) {
		return element.getFlexoDocument().getURI() + ":" + element.getIdentifier();
	}
}
