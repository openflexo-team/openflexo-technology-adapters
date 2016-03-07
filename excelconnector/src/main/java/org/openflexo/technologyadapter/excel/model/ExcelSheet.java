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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;

/**
 * Represents an Excel sheet, implemented as a wrapper of a POI sheet
 * 
 * @author vincent, sylvain
 * 
 */
public class ExcelSheet extends ExcelObject {
	private Sheet sheet;
	private ExcelWorkbook workbook;
	private List<ExcelRow> excelRows;
	private String CELL_NAME_REGEX = "(\\w+)(\\d+)";
	private FormulaEvaluator evaluator;

	public Sheet getSheet() {
		return sheet;
	}

	public ExcelSheet(Sheet sheet, ExcelWorkbook workbook, ExcelTechnologyAdapter adapter) {
		super(adapter);
		this.sheet = sheet;
		this.workbook = workbook;
		excelRows = new ArrayList<ExcelRow>();
		evaluator = workbook.getWorkbook().getCreationHelper().createFormulaEvaluator();
	}

	public FormulaEvaluator getEvaluator() {
		return evaluator;
	}

	@Override
	public String getName() {
		return sheet.getSheetName();
	}

	public ExcelWorkbook getWorkbook() {
		return workbook;
	}

	public List<ExcelRow> getExcelRows() {
		return excelRows;
	}

	public void addToExcelRows(ExcelRow newExcelRow) {
		this.excelRows.add(newExcelRow);
		getWorkbook().addToAccessibleExcelObjects(newExcelRow);
	}

	public void insertToExcelRows(ExcelRow newExcelRow, int index) {
		this.excelRows.add(index, newExcelRow);
		getWorkbook().addToAccessibleExcelObjects(newExcelRow);
	}

	public void removeFromExcelRows(ExcelRow deletedExcelRow) {
		this.excelRows.remove(deletedExcelRow);
		getWorkbook().removeFromAccessibleExcelObjects(deletedExcelRow);
	}

	public int getMaxColNumber() {
		int returned = 0;
		for (ExcelRow row : getExcelRows()) {
			if (row.getRow() != null && row.getRow().getLastCellNum() > returned) {
				returned = row.getRow().getLastCellNum();
			}
		}
		return returned;
	}

	public ExcelRow getRowAt(int row) {
		if (row < 0) {
			return null;
		}
		// Append missing rows
		while (getExcelRows().size() <= row) {
			addToExcelRows(new ExcelRow(null, this, getTechnologyAdapter()));
		}
		return getExcelRows().get(row);
	}

	public ExcelCell getCellAt(int row, int column) {
		if (row < 0) {
			return null;
		}
		if (column < 0) {
			return null;
		}
		return getRowAt(row).getCellAt(column);
	}

	public Object getCellValue(int row, int column) {
		ExcelCell cell = getCellAt(row, column);
		return cell.getCellValue();
	}

	public void setCellValue(int row, int column, String value) {
		ExcelCell cell = getCellAt(row, column);
		cell.setCellValue(value);
	}

	public Object getCellValue(String column, String row) {
		ExcelCell cell = getCellAt(Integer.parseInt(row) - 1, ExcelColumn.getColumnIndex(column));
		return cell.getCellValue();
	}

	public void setCellValue(String column, String row, String value) {
		ExcelCell cell = getCellAt(Integer.parseInt(row) - 1, ExcelColumn.getColumnIndex(column));
		cell.setCellValue(value);
	}

	public ExcelCell getCellFromName(String name) {
		Pattern id = Pattern.compile(CELL_NAME_REGEX);
		Matcher makeMatchId = id.matcher(name);
		makeMatchId.find();
		String col = makeMatchId.group(1);
		String row = makeMatchId.group(2);
		return getCellAt(Integer.parseInt(row) - 1, ExcelColumn.getColumnIndex(col));
	}

	public Object getCellValueFromName(String name) {
		return getCellFromName(name).getCellValue();
	}

	public void setCellValueFromName(String name, String value) {
		getCellFromName(name).setCellValue(value);
	}

	@Override
	public String getUri() {
		return getWorkbook().getUri() + "/" + getName();
	}

	@Override
	public String toString() {
		return getName();
	}
}
