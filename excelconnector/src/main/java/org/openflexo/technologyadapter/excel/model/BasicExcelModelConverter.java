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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.openflexo.technologyadapter.excel.model.BasicExcelModelConverter.SheetReference.RowReference;
import org.openflexo.technologyadapter.excel.model.BasicExcelModelConverter.SheetReference.RowReference.CellReference;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.toolbox.StringUtils;

/**
 * Internal object attached to a {@link ExcelWorkbook} and allowing to map POI objects to {@link ExcelObject} wrapping them
 * 
 * @author sylvain
 *
 */
public class BasicExcelModelConverter {

	private static final Logger logger = Logger.getLogger(BasicExcelModelConverter.class.getPackage().getName());

	/**
	 * Excel Objects. We have to build here a map for each sheet because equals() method of HSSFRow does not check sheet<br>
	 * (two rows at same index in multiple sheet respond to equals())
	 */
	// protected final Map<ExcelSheet, Map<Object, ExcelObject>> excelObjectsForSheet = new HashMap<>();
	// protected final Map<Object, ExcelObject> excelObjects = new HashMap<>();

	private ExcelWorkbookResource excelWorkbookResource;

	/**
	 * Instantiate a {@link BasicExcelModelConverter} for a given {@link ExcelWorkbookResource}
	 */
	public BasicExcelModelConverter(ExcelWorkbookResource excelWorkbookResource) {
		this.excelWorkbookResource = excelWorkbookResource;
	}

	public ExcelModelFactory getFactory() {
		return excelWorkbookResource.getFactory();
	}

	private ExcelWorkbook excelWorkbook;
	private Map<String, SheetReference> references = new HashMap<>();

	public class SheetReference {

		class ColumnReference {
			private ExcelColumn excelColumn;

			public void delete() {
			}
		}

		class RowReference {
			class CellReference {
				private ExcelCell excelCell;

				public CellReference(ExcelCell excelCell) {
					this.excelCell = excelCell;
				}

				public void delete() {
					excelCell = null;
				}

			}

			private final ExcelRow excelRow;
			private final List<CellReference> cells = new ArrayList<>();;

			public RowReference(ExcelRow excelRow) {
				this.excelRow = excelRow;
			}

			public void delete() {
				for (CellReference cr : cells) {
					cr.delete();
				}
				cells.clear();
			}

			private boolean isConverted = false;

			public void ensureConversion() {
				if (!isConverted) {
					convert();
				}
			}

			private CellReference getExcelCellReference(int colIndex) {
				ensureConversion();
				return cells.get(colIndex);
			}

			ExcelCell getExcelCell(int colIndex) {
				return getExcelCellReference(colIndex).excelCell;
			}

			ExcelCell getExcelCell(Cell cell) {
				return getExcelCell(cell.getColumnIndex());
			}

			public ExcelCell newCell(int columnIndex) {
				Cell cell = excelRow.getRow().createCell(columnIndex);
				ExcelCell excelCell = makeExcelCell(cell);
				excelRow.addToExcelCells(excelCell);
				CellReference cellReference = new CellReference(excelCell);
				cells.add(cellReference);
				return excelCell;
			}

			private ExcelCell makeExcelCell(Cell cell) {
				ExcelCell excelCell = getFactory().makeExcelCell();
				excelCell.setCell(cell);
				return excelCell;
			}

			private void convert() {
				// System.out
				// .println("Je convertis les donnees de la row " + excelRow.getRow().getRowNum() + " sheet " + excelSheet.getName());
				int lastCell = -1;
				for (Cell cell : excelRow.getRow()) {
					// System.out.println("Adding cell " + cell.getColumnIndex() + " value=" + cell.getStringCellValue());
					while (cell.getColumnIndex() > lastCell + 1) {
						// Missing cell
						// System.out.println("Adding a missing cell");
						// This cell is not bound to any excel cell !
						ExcelCell excelCell = getFactory().makeExcelCell();
						excelRow.addToExcelCells(excelCell);
						CellReference cellReference = new CellReference(excelCell);
						cells.add(cellReference);
						lastCell++;
					}
					ExcelCell excelCell = makeExcelCell(cell);
					excelRow.addToExcelCells(excelCell);
					CellReference cellReference = new CellReference(excelCell);
					cells.add(cellReference);
					lastCell = excelCell.getColumnIndex();
				}
				// System.out.println("Created a row with " + excelRow.getExcelCells().size() + " cells");
				/*int i = 0;
				for (ExcelCell cell : excelRow.getExcelCells()) {
					// System.out.println("Index " + i + ": Cell with " + cell.getCell()
					// + (cell.getCell() != null ? " index=" + cell.getCell().getColumnIndex() : "n/a"));
					i++;
				
				}*/
				isConverted = true;
			}

			/*private ExcelRow makeExcelRow(Row row) {
				ExcelRow excelRow = getFactory().makeExcelRow();
				excelRow.setRow(row);
				int lastCell = -1;
				for (Cell cell : row) {
					// System.out.println("Adding cell " + cell.getColumnIndex() + " value=" + cell.getStringCellValue());
					while (cell.getColumnIndex() > lastCell + 1) {
						// Missing cell
						// System.out.println("Adding a missing cell");
						// This cell is not bound to any excel cell !
						ExcelCell excelCell = getFactory().makeExcelCell();
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
			}*/

		}

		private final ExcelSheet excelSheet;
		private final List<RowReference> rows = new ArrayList<>();
		private final List<ColumnReference> columns = new ArrayList<>();

		public SheetReference(ExcelSheet excelSheet) {
			this.excelSheet = excelSheet;
		}

		public void delete() {
			for (RowReference rr : rows) {
				rr.delete();
			}
			for (ColumnReference cr : columns) {
				cr.delete();
			}
			rows.clear();
			columns.clear();
		}

		private boolean isConverted = false;

		public void ensureConversion() {
			if (!isConverted) {
				convert();
			}
		}

		private RowReference getExcelRowReference(int rowIndex) {
			ensureConversion();
			return rows.get(rowIndex);
		}

		ExcelRow getExcelRow(int rowIndex) {
			return getExcelRowReference(rowIndex).excelRow;
		}

		ExcelRow getExcelRow(Row row) {
			return getExcelRow(row.getRowNum());
		}

		private void convert() {
			// System.out.println("Je convertis les donnees de la sheet " + excelSheet.getName());
			int lastRow = -1;
			Map<Integer, ExcelRow> newInsertedRows = new LinkedHashMap<>();
			for (Row row : excelSheet.getSheet()) {
				while (row.getRowNum() > lastRow + 1) {
					// Missing row
					// System.out.println("on marque comme missing car " + row.getRowNum() + " > " + lastRow + " + 1");
					ExcelRow excelRow = getFactory().makeExcelRow();
					excelSheet.addToExcelRows(excelRow);
					RowReference rowReference = new RowReference(excelRow);
					rows.add(rowReference);
					// lastRow = excelRow.getRowIndex();
					lastRow++;
					newInsertedRows.put(lastRow, excelRow);
					// System.out.println("will insert at =" + lastRow);
					// System.out.println("lastRow =" + lastRow);
				}
				lastRow = row.getRowNum();
				// System.out.println("< lastRow=" + lastRow);

				ExcelRow excelRow = makeExcelRow(row);
				excelSheet.addToExcelRows(excelRow);
				RowReference rowReference = new RowReference(excelRow);
				rows.add(rowReference);

				// System.out.println("je passe de " + lastRow + " a " + excelRow.getRowIndex());
				lastRow = excelRow.getRowIndex();

			}
			// int insertedIndex = 0;
			for (Integer insertedIndex : newInsertedRows.keySet()) {
				// System.out.println("-----> Insert row at index " + insertedIndex);
				ExcelRow insertedRow = newInsertedRows.get(insertedIndex);
				// System.out.println("AH y est on cree la row pour " + insertedIndex);
				insertedRow.createRowWhenNonExistant(insertedIndex);
			}

			/*for (ExcelRow insertedRow : newInsertedRows) {
				// insertedRow.createRowWhenNonExistant(lastRow + 1);
				insertedIndex++;
				System.out.println("du coup on est a insertedIndex=" + insertedIndex);
				// lastRow = insertedRow.getRowIndex();
			}
			
			System.out.println("lastRow=" + lastRow);
			System.out.println("insertedIndex=" + insertedIndex);*/

			/*for (Row row : excelSheet.getSheet()) {
				ExcelRow excelRow = makeExcelRow(row);
				excelSheet.addToExcelRows(excelRow);
				RowReference rowReference = new RowReference(excelRow);
				rows.add(rowReference);
				lastRow = excelRow.getRowIndex();
			}*/

			isConverted = true;

			for (int i = 0; i < excelSheet.getSheet().getNumMergedRegions(); i++) {
				CellRangeAddress cellRange = excelSheet.getSheet().getMergedRegion(i);
				for (int row = cellRange.getFirstRow(); row <= cellRange.getLastRow(); row++) {
					for (int col = cellRange.getFirstColumn(); col <= cellRange.getLastColumn(); col++) {
						excelSheet.getCellAt(row, col).merge(cellRange);
					}
				}
			}
		}

		private ExcelRow makeExcelRow(Row row) {
			ExcelRow excelRow = getFactory().makeExcelRow();
			excelRow.setRow(row);
			return excelRow;
		}

		public ExcelRow newRow(int rowIndex) {
			Row row = excelSheet.getSheet().createRow(rowIndex);
			ExcelRow excelRow = makeExcelRow(row);
			excelSheet.addToExcelRows(excelRow);
			RowReference rowReference = new RowReference(excelRow);
			rows.add(rowReference);
			return excelRow;
		}

	}

	/**
	 * <ul>
	 * <li>{@link ExcelWorkbook}: <code>_workbook_</code></li>
	 * <li>{@link ExcelSheet}: <code>sheet_name</code></li>
	 * <li>{@link ExcelRow}: <code>sheet_name/row[3]</code></li>
	 * <li>{@link ExcelColumn}: <code>sheet_name/col[4]</code></li>
	 * <li>{@link ExcelCell}: <code>sheet_name/cell[A5]</code></li>
	 * <li>{@link ExcelCellRange}: <code>sheet_name/range[A5:T9]</code></li>
	 * </ul>
	 * 
	 * @param object
	 * @return
	 */
	public String toSerializationIdentifier(ExcelObject object) {
		if (object instanceof ExcelWorkbook) {
			return "_workbook_";
		}
		else if (object instanceof ExcelSheet) {
			return ((ExcelSheet) object).getName();
		}
		else if (object instanceof ExcelRow) {
			ExcelRow excelRow = (ExcelRow) object;
			return excelRow.getExcelSheet().getName() + "/row[" + excelRow.getRowIndex() + "]";
		}
		else if (object instanceof ExcelColumn) {
			ExcelColumn excelColumn = (ExcelColumn) object;
			return excelColumn.getExcelSheet().getName() + "/col[" + excelColumn.getColumnIndex() + "]";
		}
		else if (object instanceof ExcelCell) {
			ExcelCell excelCell = (ExcelCell) object;
			return excelCell.getExcelSheet().getName() + "/cell[" + excelCell.getIdentifier() + "]";
		}
		else if (object instanceof ExcelCellRange) {
			ExcelCellRange excelCellRange = (ExcelCellRange) object;
			return excelCellRange.getExcelSheet().getName() + "/range[" + excelCellRange.getTopLeftCell().getIdentifier() + ":"
					+ excelCellRange.getBottomRightCell().getIdentifier() + "]";
		}
		return null;
	}

	public ExcelObject fromSerializationIdentifier(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		if (id.equals("_workbook_")) {
			return excelWorkbook;
		}
		if (id.contains("/")) {
			// This is an object in a sheet
			String sheetName = id.substring(0, id.indexOf("/"));
			String objectId = id.substring(id.indexOf("/") + 1);
			SheetReference sheetRef = references.get(sheetName);
			if (sheetRef != null) {
				ExcelSheet sheet = sheetRef.excelSheet;
				if (objectId.startsWith("row[")) {
					// This is a row
					String indexAsString = objectId.substring(4, objectId.length() - 1);
					int rowIndex = Integer.parseInt(indexAsString);
					return sheetRef.rows.get(rowIndex).excelRow;
				}
				else if (objectId.startsWith("col[")) {
					// This is a column
					String indexAsString = objectId.substring(4, objectId.length() - 1);
					int colIndex = Integer.parseInt(indexAsString);
					return sheetRef.columns.get(colIndex).excelColumn;
				}
				else if (objectId.startsWith("cell[")) {
					// This is a cell
					String cellId = objectId.substring(5, objectId.length() - 1);
					return getCell(cellId, sheet);
				}
				else if (objectId.startsWith("range[")) {
					// This is a range
					String rangeId = objectId.substring(6, objectId.length() - 1);
					if (rangeId.contains(":")) {
						String topLeftCellId = rangeId.substring(0, rangeId.indexOf(":"));
						ExcelCell topLeftCell = getCell(topLeftCellId, sheet);
						String bottomRightCellId = rangeId.substring(rangeId.indexOf(":") + 1);
						ExcelCell bottomRightCell = getCell(bottomRightCellId, sheet);
						return sheet.getExcelWorkbook().getFactory().makeExcelCellRange(topLeftCell, bottomRightCell);
					}
					logger.warning("Cannot lookup range " + rangeId);
					return null;
				}
				logger.warning("Could not find object with id " + objectId);
				return null;
			}
			else {
				logger.warning("Could not find sheet " + sheetName);
				return null;
			}
		}
		else {
			// This is a sheet
			SheetReference sheetRef = references.get(id);
			if (sheetRef != null) {
				return sheetRef.excelSheet;
			}
			logger.warning("Could not find sheet " + id);
			return null;
		}
	}

	/**
	 * Return cell under the form 'A2'
	 * 
	 * @param id
	 * @param sheet
	 * @return
	 */
	private ExcelCell getCell(String id, ExcelSheet sheet) {
		String colAsString = "";
		String rowAsString = "";
		id = id.toUpperCase();
		for (int i = 0; i < id.length(); i++) {
			char c = id.charAt(i);
			if (c >= 'A' && c <= 'Z') {
				colAsString = colAsString + c;
			}
			else if (c >= '0' && c <= '9') {
				rowAsString = rowAsString + c;
			}
		}
		int col = ExcelColumn.getColumnIndex(colAsString);
		int row = Integer.parseInt(rowAsString) - 1;
		return sheet.getCellAt(row, col);
	}

	/**
	 * Convert a Workbook into an Excel Workbook
	 */
	public ExcelWorkbook convertExcelWorkbook(Workbook workbook) {
		excelWorkbook = getFactory().makeExcelWorkbook();
		excelWorkbook.setWorkbook(workbook);
		for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
			Sheet sheet = workbook.getSheetAt(index);
			getExcelSheet(sheet);
		}
		return excelWorkbook;
	}

	public SheetReference getSheetReference(Sheet sheet) {
		SheetReference sheetReference = references.get(sheet.getSheetName());
		if (sheetReference == null) {
			ExcelSheet excelSheet = getFactory().makeExcelSheet();
			excelSheet.setSheet(sheet);
			excelWorkbook.addToExcelSheets(excelSheet);
			sheetReference = new SheetReference(excelSheet);
			references.put(sheet.getSheetName(), sheetReference);
		}
		return sheetReference;
	}

	public ExcelSheet getExcelSheet(Sheet sheet) {
		return getSheetReference(sheet).excelSheet;
	}

	public RowReference getRowReference(Row row) {
		SheetReference sheetReference = getSheetReference(row.getSheet());
		return sheetReference.getExcelRowReference(row.getRowNum());
	}

	public ExcelRow getExcelRow(Row row) {
		return getRowReference(row).excelRow;
	}

	public CellReference getCellReference(Cell cell) {
		RowReference rowReference = getRowReference(cell.getRow());
		return rowReference.getExcelCellReference(cell.getColumnIndex());
	}

	public ExcelCell getExcelCell(Cell cell) {
		return getCellReference(cell).excelCell;
	}

	public ExcelSheet newSheet(String name, boolean override) {
		Sheet sheet = retrieveOrCreateSheet(name, override);
		return getExcelSheet(sheet);
	}

	// Create an Excel Sheet or get the existing one.
	private Sheet retrieveOrCreateSheet(String name, boolean override) {
		Workbook wb = excelWorkbook.getWorkbook();
		Sheet sheet = null;
		// A sheet with this name already exists
		if (wb.getSheet(name) != null) {
			if (override) {
				// Override it
				wb.removeSheetAt(wb.getSheetIndex(name));
				sheet = wb.createSheet(name);
				logger.info("Override excel sheet with the name " + name);
			}
			else {
				// Retrieve the existing one
				sheet = wb.getSheet(name);
				logger.warning("An excel sheet already exists with this name " + name + " , retrieve existing sheet");
			}
		}
		else {
			// Create a new one
			sheet = wb.createSheet(name);
			logger.info("Create a new excel sheet with the name " + name);
		}
		return sheet;
	}

	public void delete() {
		for (SheetReference sr : references.values()) {
			sr.delete();
		}
		references.clear();
		references = null;
		excelWorkbook = null;
	}

}
