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

package org.openflexo.technologyadapter.excel.model;

import java.io.FileNotFoundException;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.pamela.StringConverterLibrary.Converter;
import org.openflexo.pamela.factory.ModelFactory;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;

public class ExcelCellRangeConverter extends Converter<ExcelCellRange> {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
			.getLogger(ExcelCellRangeConverter.class.getPackage().getName());

	private FlexoServiceManager serviceManager;

	private ExcelCellRangeConverter() {
		super(ExcelCellRange.class);
	}

	public ExcelCellRangeConverter(FlexoServiceManager serviceManager) {
		this();
		this.serviceManager = serviceManager;
	}

	@Override
	public ExcelCellRange convertFromString(String value, ModelFactory factory) {

		System.out.println("Lookup ExcelCellRange for " + value);

		int separatorIndex = value.lastIndexOf("$");

		if (separatorIndex > -1) {

			String documentURI = value.substring(0, separatorIndex);
			String rangeId = value.substring(separatorIndex + 1);

			System.out.println("documentURI: " + documentURI);
			System.out.println("rangeId: " + rangeId);
			System.out.println("serviceManager: " + serviceManager);

			// System.out.println("factory: " + factory);

			FlexoResource<ExcelWorkbook> documentResource = null;

			if (serviceManager != null) {
				activateExcelTechnologyAdapter();
				documentResource = serviceManager.getResourceManager().getResource(documentURI, null, ExcelWorkbook.class);
				/*System.out.println("Searching " + documentURI + " found: " + documentResource);
				if (documentResource == null) {
					logger.warning("Cannot find document resource " + documentURI);
					for (FlexoResource<?> r : serviceManager.getResourceManager().getRegisteredResources()) {
						System.out.println("> " + r.getURI());
					}
				}*/
			}
			if (factory instanceof FMLModelFactory) {
				serviceManager = ((FMLModelFactory) factory).getServiceManager();
				activateExcelTechnologyAdapter();
				documentResource = ((FMLModelFactory) factory).getServiceManager().getResourceManager().getResource(documentURI, null,
						ExcelWorkbook.class);
				System.out.println("on cherche encore " + documentURI + " et on trouve: " + documentResource);
			}

			System.out.println("documentResource=" + documentResource);

			if (documentResource != null) {
				ExcelWorkbook document;
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

				BasicExcelModelConverter converter = document.getConverter();
				return (ExcelCellRange) converter.fromSerializationIdentifier(rangeId);

				/*ExcelSheet sheet = document.getExcelSheetByName(sheetId);
				System.out.println("sheet=" + sheet);*/

				/*
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
				}*/
			}

			else {
				logger.warning("Could not find documentResource identified by " + documentURI);
			}
		}
		return null;
	}

	@Override
	public String convertToString(ExcelCellRange cellRange) {
		StringBuffer sb = new StringBuffer();
		sb.append(cellRange.getExcelWorkbook().getResource().getURI());
		BasicExcelModelConverter converter = cellRange.getExcelWorkbook().getConverter();
		sb.append("$" + converter.toSerializationIdentifier(cellRange));
		return sb.toString();
	}

	/**
	 * Activate the Excel technology adapter in order to retrieve excel resources. Wait until the Excel technology adapter is activated.
	 */
	private void activateExcelTechnologyAdapter() {
		serviceManager.activateTechnologyAdapter(
				serviceManager.getTechnologyAdapterService().getTechnologyAdapter(ExcelTechnologyAdapter.class), true);
	}
}
