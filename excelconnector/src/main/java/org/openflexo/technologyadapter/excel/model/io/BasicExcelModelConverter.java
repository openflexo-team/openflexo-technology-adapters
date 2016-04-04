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

package org.openflexo.technologyadapter.excel.model.io;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelObject;
import org.openflexo.technologyadapter.excel.model.ExcelRow;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;

public class BasicExcelModelConverter {

	private static final Logger logger = Logger.getLogger(BasicExcelModelConverter.class.getPackage().getName());

	/**
	 * Excel Objects. We have to build here a map for each sheet because equals() method of HSSFRow does not check sheet<br>
	 * (two rows at same index in multiple sheet respond to equals())
	 */
	protected final Map<ExcelSheet, Map<Object, ExcelObject>> excelObjectsForSheet = new HashMap<>();
	protected final Map<Object, ExcelObject> excelObjects = new HashMap<>();

	/**
	 * Constructor.
	 */
	public BasicExcelModelConverter() {
	}

	/**
	 * Convert a Workbook into an Excel Workbook
	 */
	public ExcelWorkbook convertExcelWorkbook(Workbook workbook, ExcelTechnologyAdapter technologyAdapter) {
		ExcelWorkbook excelWorkbook = new ExcelWorkbook(workbook, this, technologyAdapter);
		excelObjects.put(workbook, excelWorkbook);
		for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
			Sheet sheet = workbook.getSheetAt(index);
			ExcelSheet excelSheet = convertExcelSheetToSheet(sheet, excelWorkbook, technologyAdapter);
			excelWorkbook.addToExcelSheets(excelSheet);
		}
		return excelWorkbook;
	}

	/**
	 * Convert a Sheet into an Excel Sheet
	 */
	public ExcelSheet convertExcelSheetToSheet(Sheet sheet, ExcelWorkbook workbook, ExcelTechnologyAdapter technologyAdapter) {
		ExcelSheet excelSheet = null;
		if (excelObjects.get(sheet) == null) {
			excelSheet = new ExcelSheet(sheet, workbook, technologyAdapter);
			excelObjects.put(sheet, excelSheet);
			excelObjectsForSheet.put(excelSheet, new HashMap<Object, ExcelObject>());
			int lastRow = -1;
			for (Row row : sheet) {
				while (row.getRowNum() > lastRow + 1) {
					// Missing row
					ExcelRow excelRow = new ExcelRow(null, excelSheet, technologyAdapter);
					excelSheet.addToExcelRows(excelRow);
					lastRow++;
				}
				ExcelRow excelRow = convertExcelRowToRow(row, excelSheet, technologyAdapter);
				excelSheet.addToExcelRows(excelRow);
				lastRow = excelRow.getRowNum();
			}
			for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
				CellRangeAddress cellRange = sheet.getMergedRegion(i);
				for (int row = cellRange.getFirstRow(); row <= cellRange.getLastRow(); row++) {
					for (int col = cellRange.getFirstColumn(); col <= cellRange.getLastColumn(); col++) {
						excelSheet.getCellAt(row, col).merge(cellRange);
					}
				}
			}
		}
		else {
			excelSheet = (ExcelSheet) excelObjects.get(sheet);
		}
		return excelSheet;
	}

	/**
	 * Convert a Row into an Excel Row
	 */
	public ExcelRow convertExcelRowToRow(Row row, ExcelSheet excelSheet, ExcelTechnologyAdapter technologyAdapter) {
		ExcelRow excelRow;
		Map<Object, ExcelObject> map = excelObjectsForSheet.get(excelSheet);
		if (map.get(row) == null) {
			// System.out.println("Build row " + row.getRowNum() + " for sheet " + excelSheet.getName());
			excelRow = new ExcelRow(row, excelSheet, technologyAdapter);
			map.put(row, excelRow);
			int lastCell = -1;
			for (Cell cell : row) {
				// System.out.println("Adding cell " + cell.getColumnIndex() + " value=" + cell.getStringCellValue());
				while (cell.getColumnIndex() > lastCell + 1) {
					// Missing cell
					// System.out.println("Adding a missing cell");
					// This cell is not bound to any excel cell !
					ExcelCell excelCell = new ExcelCell(null/*row.createCell(lastCell + 1)*/, excelRow, technologyAdapter);
					excelRow.addToExcelCells(excelCell);
					lastCell++;
				}
				ExcelCell excelCell = convertExcelCellToCell(cell, excelRow, technologyAdapter);
				excelRow.addToExcelCells(excelCell);
				lastCell = excelCell.getColumnIndex();
			}
			// System.out.println("Created a row with " + excelRow.getExcelCells().size() + " cells");
			int i = 0;
			for (ExcelCell cell : excelRow.getExcelCells()) {
				// System.out.println("Index " + i + ": Cell with " + cell.getCell()
				// + (cell.getCell() != null ? " index=" + cell.getCell().getColumnIndex() : "n/a"));
				i++;

			}
		}
		else {
			excelRow = (ExcelRow) map.get(row);
			// System.out.println(" C'est égal? (" + row.getClass().getCanonicalName() + ")" + (excelRow.getRow().equals(row)) + " -- "
			// + (excelRow.getRow() == row));
		}

		return excelRow;
	}

	/**
	 * Convert a Cell into an Excel Cell
	 */
	public ExcelCell convertExcelCellToCell(Cell cell, ExcelRow excelRow, ExcelTechnologyAdapter technologyAdapter) {
		ExcelCell excelCell = null;
		Map<Object, ExcelObject> map = excelObjectsForSheet.get(excelRow.getExcelSheet());
		if (map.get(cell) == null) {
			excelCell = new ExcelCell(cell, excelRow, technologyAdapter);
			map.put(cell, excelCell);
		}
		else {
			excelCell = (ExcelCell) map.get(cell);
		}
		return excelCell;
	}

	/**
	 * Getter of excel objects.
	 * 
	 * @return the individuals value
	 */
	/*public Map<Object, ExcelObject> getExcelObjects() {
		return excelObjects;
	}*/

}
