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

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.openflexo.connie.annotations.NotificationUnsafe;
import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.CloningStrategy;
import org.openflexo.pamela.annotations.Embedded;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Reindexer;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;
import org.openflexo.pamela.annotations.Getter.Cardinality;

/**
 * Represents an Excel sheet, implemented as a wrapper of a POI sheet
 * 
 * @author vincent, sylvain
 * 
 */
@ModelEntity
@ImplementationClass(value = ExcelSheet.ExcelSheetImpl.class)
@XMLElement
public interface ExcelSheet extends ExcelObject {

	@PropertyIdentifier(type = ExcelWorkbook.class)
	public static final String EXCEL_WORKBOOK_KEY = "excelWorkbook";
	@PropertyIdentifier(type = Sheet.class)
	public static final String SHEET_KEY = "sheet";
	@PropertyIdentifier(type = ExcelRow.class, cardinality = Cardinality.LIST)
	public static final String EXCEL_ROWS_KEY = "excelRows";

	/**
	 * Return name of the sheet
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Return {@link ExcelWorkbook} where this {@link ExcelSheet} is defined
	 * 
	 * @return
	 */
	@Getter(value = EXCEL_WORKBOOK_KEY)
	public ExcelWorkbook getExcelWorkbook();

	/**
	 * Sets {@link ExcelWorkbook} where this {@link ExcelSheet} is defined
	 * 
	 * @param workbook
	 */
	@Setter(EXCEL_WORKBOOK_KEY)
	public void setExcelWorkbook(ExcelWorkbook workbook);

	/**
	 * Return sheet wrapped by this {@link ExcelSheet}
	 * 
	 * @return
	 */
	@Getter(value = SHEET_KEY, ignoreType = true)
	public Sheet getSheet();

	/**
	 * Sets sheet wrapped by this {@link ExcelSheet}
	 * 
	 * @param sheet
	 */
	@Setter(SHEET_KEY)
	public void setSheet(Sheet sheet);

	/**
	 * Return all {@link ExcelRow} defined in this {@link ExcelSheet}
	 * 
	 * @return
	 */
	@Getter(value = EXCEL_ROWS_KEY, cardinality = Cardinality.LIST, inverse = ExcelRow.EXCEL_SHEET_KEY)
	@XMLElement
	@Embedded
	@CloningStrategy(StrategyType.CLONE)
	public List<ExcelRow> getExcelRows();

	@Setter(EXCEL_ROWS_KEY)
	public void setExcelRows(List<ExcelRow> excelRows);

	@Adder(EXCEL_ROWS_KEY)
	@PastingPoint
	public void addToExcelRows(ExcelRow anExcelRow);

	@Remover(EXCEL_ROWS_KEY)
	public void removeFromExcelRows(ExcelRow anExcelRow);

	@Reindexer(EXCEL_ROWS_KEY)
	public void moveExcelRowToIndex(ExcelRow anExcelRow, int index);

	public FormulaEvaluator getEvaluator();

	@NotificationUnsafe
	public ExcelRow getRowAt(int row);

	public ExcelCell getCellAt(int row, int column);

	public Object getCellValue(int row, int column);

	public void setCellValue(int row, int column, String value);

	public Object getCellValue(String column, String row);

	public void setCellValue(String column, String row, String value);

	public ExcelCell getCellFromName(String name);

	public Object getCellValueFromName(String name);

	public void setCellValueFromName(String name, String value);

	public void setCellValue(ExcelCell cell, String value);

	/**
	 * Insert new row in this sheet<br>
	 * First shift all existing rows after supplied insertion point to the bottom
	 * 
	 * @param rowIndex
	 * @return
	 */
	public ExcelRow insertRowAt(int rowIndex);

	/**
	 * Remove row identified by its index in this sheet<br>
	 * First shift all existing rows after supplied deletion point to the top
	 * 
	 * @param rowIndex
	 * @return row beeing deleted
	 */
	public ExcelRow removeRowAt(int rowIndex);

	/**
	 * Append new row in this sheet at the last position<br>
	 * 
	 * @return
	 */
	public ExcelRow createNewRow();

	public int getMaxColNumber();

	/**
	 * Default base implementation for {@link ExcelSheet}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class ExcelSheetImpl extends ExcelObjectImpl implements ExcelSheet {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(ExcelSheetImpl.class.getPackage().getName());

		private String CELL_NAME_REGEX = "([A-Z]+)(\\d+)";
		private FormulaEvaluator evaluator;

		public ExcelSheetImpl() {
		}

		@Override
		public ExcelWorkbook getResourceData() {
			return getExcelWorkbook();
		}

		@Override
		public void setExcelWorkbook(ExcelWorkbook workbook) {
			performSuperSetter(EXCEL_WORKBOOK_KEY, workbook);
			if (workbook != null) {
				evaluator = workbook.getWorkbook().getCreationHelper().createFormulaEvaluator();
			}
		}

		@Override
		public FormulaEvaluator getEvaluator() {
			return evaluator;
		}

		@Override
		public String getName() {
			return getSheet().getSheetName();
		}

		@Override
		public int getMaxColNumber() {
			int returned = 0;
			for (ExcelRow row : getExcelRows()) {
				if (row.getRow() != null && row.getRow().getLastCellNum() > returned) {
					returned = row.getRow().getLastCellNum();
				}
			}
			return returned;
		}

		private boolean isConverting = false;

		private void ensureConversion() {
			if (isConverting || isDeleted()) {
				return;
			}
			if (getExcelWorkbook() != null) {
				try {
					isConverting = true;
					getExcelWorkbook().getConverter().getSheetReference(getSheet()).ensureConversion();
				} finally {
					isConverting = false;
				}
			}
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<ExcelRow> getExcelRows() {
			ensureConversion();
			return (List<ExcelRow>) performSuperGetter(EXCEL_ROWS_KEY);
		}

		@Override
		public ExcelRow getRowAt(int row) {
			if (row < 0) {
				return null;
			}

			// Append missing rows
			while (getExcelRows().size() <= row) {
				insertRowAt(getExcelRows().size());
			}

			return getExcelRows().get(row);
		}

		@Override
		public ExcelCell getCellAt(int row, int column) {
			if (row < 0) {
				return null;
			}
			if (column < 0) {
				return null;
			}
			return getRowAt(row).getExcelCellAt(column);
		}

		@Override
		public Object getCellValue(int row, int column) {
			ExcelCell cell = getCellAt(row, column);
			return cell.getCellValue();
		}

		@Override
		public void setCellValue(int row, int column, String value) {
			ExcelCell cell = getCellAt(row, column);
			cell.setCellValue(value);
		}

		@Override
		public Object getCellValue(String column, String row) {
			ExcelCell cell = getCellAt(Integer.parseInt(row) - 1, ExcelColumn.getColumnIndex(column));
			return cell.getCellValue();
		}

		@Override
		public void setCellValue(String column, String row, String value) {
			ExcelCell cell = getCellAt(Integer.parseInt(row) - 1, ExcelColumn.getColumnIndex(column));
			cell.setCellValue(value);
		}

		@Override
		public ExcelCell getCellFromName(String name) {
			Pattern id = Pattern.compile(CELL_NAME_REGEX);
			Matcher makeMatchId = id.matcher(name);
			makeMatchId.find();
			String col = makeMatchId.group(1);
			String row = makeMatchId.group(2);
			return getCellAt(Integer.parseInt(row) - 1, ExcelColumn.getColumnIndex(col));
		}

		@Override
		public Object getCellValueFromName(String name) {
			return getCellFromName(name).getCellValue();
		}

		@Override
		public void setCellValueFromName(String name, String value) {
			getCellFromName(name).setCellValue(value);
		}

		@Override
		public void setCellValue(ExcelCell cell, String value) {
			cell.setCellValue(value);
		}

		/**
		 * Insert new row in this sheet<br>
		 * First shift all existing rows after supplied insertion point to the bottom
		 * 
		 * @param rowIndex
		 * @return
		 */
		@Override
		public ExcelRow insertRowAt(int rowIndex) {
			BasicExcelModelConverter converter = getResourceData().getConverter();
			ExcelRow returned = converter.getSheetReference(getSheet()).createOrInsertNewRow(rowIndex);
			return returned;
		}

		/**
		 * Append new row in this sheet at the last position<br>
		 * 
		 * @return
		 */
		@Override
		public ExcelRow createNewRow() {
			return insertRowAt(getExcelRows().size());
		}

		/**
		 * Remove row identified by its index in this sheet<br>
		 * First shift all existing rows after supplied deletion point to the top
		 * 
		 * @param rowIndex
		 * @return row beeing deleted
		 */
		@Override
		public ExcelRow removeRowAt(int rowIndex) {
			BasicExcelModelConverter converter = getResourceData().getConverter();
			ExcelRow returned = converter.getSheetReference(getSheet()).removeRowAt(rowIndex);
			return returned;
		}

	}
}
