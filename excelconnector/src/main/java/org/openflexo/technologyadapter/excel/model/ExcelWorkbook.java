/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.io.BasicExcelModelConverter;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;

public class ExcelWorkbook extends ExcelObject implements ResourceData<ExcelWorkbook> {

	private static final Logger logger = Logger.getLogger(ExcelWorkbook.class.getPackage().getName());

	private Workbook workbook;
	private ExcelWorkbookResource resource;
	private List<ExcelSheet> excelSheets;
	private BasicExcelModelConverter converter;
	private ArrayList<ExcelObject> accessibleExcelObjects;
	private final ExcelStyleManager styleManager;

	public Workbook getWorkbook() {
		return workbook;
	}

	public ExcelWorkbook(Workbook workbook, ExcelTechnologyAdapter adapter) {
		super(adapter);
		this.workbook = workbook;
		excelSheets = new ArrayList<ExcelSheet>();
		styleManager = new ExcelStyleManager(this);
	}

	public ExcelWorkbook(ExcelTechnologyAdapter adapter) {
		super(adapter);
		excelSheets = new ArrayList<ExcelSheet>();
		styleManager = new ExcelStyleManager(this);
	}

	public ExcelWorkbook(Workbook workbook, BasicExcelModelConverter converter, ExcelTechnologyAdapter adapter) {
		super(adapter);
		this.workbook = workbook;
		this.converter = converter;
		excelSheets = new ArrayList<ExcelSheet>();
		styleManager = new ExcelStyleManager(this);
	}

	public BasicExcelModelConverter getConverter() {
		return converter;
	}

	@Override
	public ExcelWorkbookResource getResource() {
		return resource;
	}

	@Override
	public void setResource(FlexoResource<ExcelWorkbook> resource) {
		this.resource = (ExcelWorkbookResource) resource;
	}

	@Override
	public String getName() {
		return getResource().getName();
	}

	public List<ExcelSheet> getExcelSheets() {
		return excelSheets;
	}

	public void addToExcelSheets(ExcelSheet newExcelSheet) {
		this.excelSheets.add(newExcelSheet);
		addToAccessibleExcelObjects(newExcelSheet);
	}

	/**
	 * Get an Excel Sheet within a workbook using its name.
	 * 
	 * @param name
	 * @return
	 */
	public ExcelSheet getExcelSheetByName(String name) {
		Sheet sheet = workbook.getSheet(name);
		return getExcelSheetFromSheet(sheet);
	}

	/**
	 * Get an Excel Sheet using its position in a workbook in the list of sheets
	 * 
	 * @param place
	 * @return
	 */
	public ExcelSheet getExcelSheetAtPosition(int position) {
		Sheet sheet = workbook.getSheetAt(position);
		return getExcelSheetFromSheet(sheet);
	}

	/**
	 * Remove an excel sheet from a workbook
	 * 
	 * @param deletedExcelSheet
	 */
	public void removeFromExcelSheets(ExcelSheet deletedExcelSheet) {
		this.excelSheets.remove(deletedExcelSheet);
		removeFromAccessibleExcelObjects(deletedExcelSheet);
	}

	public List<ExcelObject> getAccessibleExcelObjects() {
		if (accessibleExcelObjects == null) {
			accessibleExcelObjects = new ArrayList<ExcelObject>();
		}
		return accessibleExcelObjects;
	}

	public void addToAccessibleExcelObjects(ExcelObject excelObject) {
		getAccessibleExcelObjects().add(excelObject);
	}

	public void removeFromAccessibleExcelObjects(ExcelObject excelObject) {
		getAccessibleExcelObjects().remove(excelObject);
	}

	/**
	 * Get an ExcelSheet(Technology adapter abstraction) from a Sheet(Poi abstraction).
	 * 
	 * @param sheet
	 * @return
	 */
	public ExcelSheet getExcelSheetFromSheet(Sheet sheet) {
		for (ExcelSheet excelSheet : getExcelSheets()) {
			if (excelSheet.getSheet().equals(sheet)) {
				return excelSheet;
			}
		}
		logger.warning("No converted sheet found for " + sheet.getSheetName());
		return null;
	}

	@Override
	public String getUri() {
		return getName();
	}

	public ExcelStyleManager getStyleManager() {
		return styleManager;
	}
}
