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
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.model.ExcelCell.ExcelCellImpl.CellType;
import org.openflexo.technologyadapter.excel.model.ExcelStyleManager.CellStyleFeature;

/**
 * Represents an Excel cell, implemented as a wrapper of a POI Cell
 * 
 * @author vincent, sylvain
 * 
 */
@ModelEntity
@ImplementationClass(value = ExcelCell.ExcelCellImpl.class)
@XMLElement
public interface ExcelCell extends ExcelObject, ExcelStyleObject {

	@PropertyIdentifier(type = ExcelRow.class)
	public static final String EXCEL_ROW_KEY = "excelRow";
	@PropertyIdentifier(type = Cell.class)
	public static final String CELL_KEY = "cell";

	/**
	 * Return {@link ExcelRow} where this {@link ExcelCell} is defined
	 * 
	 * @return
	 */
	@Getter(value = EXCEL_ROW_KEY)
	public ExcelRow getExcelRow();

	/**
	 * Sets {@link ExcelRow} where this {@link ExcelCell} is defined
	 * 
	 * @param workbook
	 */
	@Setter(EXCEL_ROW_KEY)
	public void setExcelRow(ExcelRow row);

	/**
	 * Return cell wrapped by this {@link ExcelCell}
	 * 
	 * @return
	 */
	@Getter(value = CELL_KEY, ignoreType = true)
	public Cell getCell();

	/**
	 * Sets cell wrapped by this {@link ExcelCell}
	 * 
	 * @param sheet
	 */
	@Setter(CELL_KEY)
	public void setCell(Cell cell);

	/**
	 * Return {@link ExcelSheet} where this cell is defined
	 * 
	 * @return
	 */
	public ExcelSheet getExcelSheet();

	/**
	 * Return identifier of the cell under the form <code>B7</code>
	 * 
	 * @return
	 */
	public String getIdentifier();

	/**
	 * Return index of the column this cell belongs to
	 * 
	 * @return
	 */
	public int getColumnIndex();

	/**
	 * Return index of the row this cell belongs to
	 * 
	 * @return
	 */
	public int getRowIndex();

	/**
	 * Return the cell located at the left of this cell.<br>
	 * If cell is merged, return first non-merged cell located at the left of this cell
	 * 
	 * @return
	 */
	public ExcelCell getPreviousCell();

	/**
	 * Return the cell located at the right of this cell.<br>
	 * If cell is merged, return first non-merged cell located at the right of this cell
	 * 
	 * @return
	 */
	public ExcelCell getNextCell();

	/**
	 * Return type of this cell
	 * 
	 * @return
	 */
	public CellType getCellType();

	public void setCellType(CellType cellType);

	/**
	 * Return the value to be displayed in a generic viewer
	 * 
	 * @return
	 */
	public String getDisplayValue();

	/**
	 * Return the value (set or computed) associated with this cell
	 * 
	 * @return
	 */
	public Object getCellValue();

	public String getCellValueAsString();

	public void setCellValueAsString(String cellValueAsString);

	public void setCellValue(Object value);

	public void setCellStringValue(String value);

	public void setCellNumericValue(Number value);

	public void setCellBooleanValue(boolean value);

	public void setCellDateValue(Date value);

	/**
	 * Return the specification of this cell
	 * 
	 * @return
	 */
	public String getDisplayCellSpecification();

	/**
	 * Return a String identifying this cell (eg. (0,0) will return "A1")
	 * 
	 * @return
	 */
	public String getCellIdentifier();

	public boolean hasTopBorder();

	public boolean hasLeftBorder();

	public boolean hasRightBorder();

	public boolean hasBottomBorder();

	public void setCellStyle(CellStyle style);

	public CellStyle getCellStyle();

	/**
	 * Merge cell with supplied range
	 * 
	 * @param cellRange
	 */
	public void merge(CellRangeAddress cellRange);

	/**
	 * Indicated if this cell is merged
	 * 
	 * @return
	 */
	public boolean isMerged();

	/**
	 * Return the list of cells with which this cell has been merged
	 * 
	 * @return
	 */
	public List<ExcelCell> getMergedCells();

	/**
	 * Return the top left merged cell
	 * 
	 * @return
	 */
	public ExcelCell getTopLeftMergedCell();

	/**
	 * Return the top right merged cell
	 * 
	 * @return
	 */
	public ExcelCell getTopRightMergedCell();

	/**
	 * Return the bottom left merged cell
	 * 
	 * @return
	 */
	public ExcelCell getBottomLeftMergedCell();

	/**
	 * Return the bottom right merged cell
	 * 
	 * @return
	 */
	public ExcelCell getBottomRightMergedCell();

	/**
	 * Return the merged cell located at the top and at same column
	 * 
	 * @return
	 */
	public ExcelCell getTopMergedCell();

	/**
	 * Return the merged cell located at the bottom and at same column
	 * 
	 * @return
	 */
	public ExcelCell getBottomMergedCell();

	/**
	 * Return the merged cell located at the left and at same row
	 * 
	 * @return
	 */
	public ExcelCell getLeftMergedCell();

	/**
	 * Return the merged cell located at the right and at same row
	 * 
	 * @return
	 */
	public ExcelCell getRightMergedCell();

	/**
	 * Return the cell located at the top of this cell.<br>
	 * If cell is merged, return first non-merged cell located at the top of this cell
	 * 
	 * @return
	 */
	public ExcelCell getUpperCell();

	/**
	 * Return the cell located at the bottom of this cell.<br>
	 * If cell is merged, return first non-merged cell located at the bottom of this cell
	 * 
	 * @return
	 */
	public ExcelCell getLowerCell();

	/**
	 * Default base implementation for {@link ExcelCell}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class ExcelCellImpl extends ExcelObjectImpl implements ExcelCell {
		static final Logger logger = Logger.getLogger(ExcelCell.class.getPackage().getName());

		// private Cell cell;
		// private ExcelRow excelRow;

		private CellRangeAddress cellRange = null;

		public enum CellType {
			Blank, Numeric, String, NumericFormula, StringFormula, Boolean, Error, Empty, Unknown
		}

		/*@Override
		public Cell getCell() {
			return cell;
		}*/

		/*public ExcelCell(Cell cell, ExcelRow excelRow, ExcelTechnologyAdapter adapter) {
			super(adapter);
			this.cell = cell;
			this.excelRow = excelRow;
		}*/

		/*@Override
		public ExcelRow getExcelRow() {
			return excelRow;
		}*/

		@Override
		public ExcelWorkbook getResourceData() {
			return getExcelSheet().getExcelWorkbook();
		}

		@Override
		public ExcelSheet getExcelSheet() {
			return getExcelRow().getExcelSheet();
		}

		@Override
		public int getColumnIndex() {
			if (getCell() != null) {
				return getCell().getColumnIndex();
			}
			else {
				return getExcelRow().getExcelCells().indexOf(this);
			}
		}

		@Override
		public int getRowIndex() {
			if (getCell() != null) {
				return getCell().getRowIndex();
			}
			else {
				return getExcelRow().getRowIndex();
			}
		}

		/*@Override
		public String getName() {
		
			if (cell != null) {
				return "cell." + "row" + cell.getRowIndex() + "." + "col_" + cell.getColumnIndex();
			}
			else {
				// This happens when the cell is empty (without any value)
				// TODO: there is a risk for major issues here
				return "EmpyCell";
			}
		}*/

		@Override
		public void merge(CellRangeAddress cellRange) {
			this.cellRange = cellRange;
		}

		/**
		 * Indicated if this cell is merged
		 * 
		 * @return
		 */
		@Override
		public boolean isMerged() {
			return cellRange != null;
		}

		/**
		 * Return the list of cells with which this cell has been merged
		 * 
		 * @return
		 */
		@Override
		public List<ExcelCell> getMergedCells() {
			if (isMerged()) {
				List<ExcelCell> returned = new ArrayList<ExcelCell>();
				for (int row = cellRange.getFirstRow(); row <= cellRange.getLastRow(); row++) {
					for (int col = cellRange.getFirstColumn(); col <= cellRange.getLastColumn(); col++) {
						returned.add(getExcelSheet().getCellAt(row, col));
					}
				}
				return returned;
			}
			else {
				return null;
			}
		}

		/**
		 * Return the top left merged cell
		 * 
		 * @return
		 */
		@Override
		public ExcelCell getTopLeftMergedCell() {
			if (isMerged()) {
				return getExcelSheet().getCellAt(cellRange.getFirstRow(), cellRange.getFirstColumn());
			}
			return null;
		}

		/**
		 * Return the top right merged cell
		 * 
		 * @return
		 */
		@Override
		public ExcelCell getTopRightMergedCell() {
			if (isMerged()) {
				return getExcelSheet().getCellAt(cellRange.getFirstRow(), cellRange.getLastColumn());
			}
			return null;
		}

		/**
		 * Return the bottom left merged cell
		 * 
		 * @return
		 */
		@Override
		public ExcelCell getBottomLeftMergedCell() {
			if (isMerged()) {
				return getExcelSheet().getCellAt(cellRange.getLastRow(), cellRange.getFirstColumn());
			}
			return null;
		}

		/**
		 * Return the bottom right merged cell
		 * 
		 * @return
		 */
		@Override
		public ExcelCell getBottomRightMergedCell() {
			if (isMerged()) {
				return getExcelSheet().getCellAt(cellRange.getLastRow(), cellRange.getLastColumn());
			}
			return null;
		}

		/**
		 * Return the merged cell located at the top and at same column
		 * 
		 * @return
		 */
		@Override
		public ExcelCell getTopMergedCell() {
			if (isMerged()) {
				return getExcelSheet().getCellAt(cellRange.getFirstRow(), getColumnIndex());
			}
			return null;
		}

		/**
		 * Return the merged cell located at the bottom and at same column
		 * 
		 * @return
		 */
		@Override
		public ExcelCell getBottomMergedCell() {
			if (isMerged()) {
				return getExcelSheet().getCellAt(cellRange.getLastRow(), getColumnIndex());
			}
			return null;
		}

		/**
		 * Return the merged cell located at the left and at same row
		 * 
		 * @return
		 */
		@Override
		public ExcelCell getLeftMergedCell() {
			if (isMerged()) {
				return getExcelSheet().getCellAt(getRowIndex(), cellRange.getFirstColumn());
			}
			return null;
		}

		/**
		 * Return the merged cell located at the right and at same row
		 * 
		 * @return
		 */
		@Override
		public ExcelCell getRightMergedCell() {
			if (isMerged()) {
				return getExcelSheet().getCellAt(getRowIndex(), cellRange.getLastColumn());
			}
			return null;
		}

		/**
		 * Return the cell located at the top of this cell.<br>
		 * If cell is merged, return first non-merged cell located at the top of this cell
		 * 
		 * @return
		 */
		@Override
		public ExcelCell getUpperCell() {
			if (isMerged()) {
				return getExcelSheet().getCellAt(cellRange.getFirstRow() - 1, getColumnIndex());
			}
			return getExcelSheet().getCellAt(getRowIndex() - 1, getColumnIndex());
		}

		/**
		 * Return the cell located at the bottom of this cell.<br>
		 * If cell is merged, return first non-merged cell located at the bottom of this cell
		 * 
		 * @return
		 */
		@Override
		public ExcelCell getLowerCell() {
			if (isMerged()) {
				return getExcelSheet().getCellAt(cellRange.getLastRow() + 1, getColumnIndex());
			}
			return getExcelSheet().getCellAt(getRowIndex() + 1, getColumnIndex());
		}

		/**
		 * Return the cell located at the left of this cell.<br>
		 * If cell is merged, return first non-merged cell located at the left of this cell
		 * 
		 * @return
		 */
		@Override
		public ExcelCell getPreviousCell() {
			if (isMerged()) {
				return getExcelSheet().getCellAt(getRowIndex(), cellRange.getFirstColumn() - 1);
			}
			return getExcelSheet().getCellAt(getRowIndex(), getColumnIndex() - 1);
		}

		/**
		 * Return the cell located at the right of this cell.<br>
		 * If cell is merged, return first non-merged cell located at the right of this cell
		 * 
		 * @return
		 */
		@Override
		public ExcelCell getNextCell() {
			if (isMerged()) {
				return getExcelSheet().getCellAt(getRowIndex(), cellRange.getLastColumn() + 1);
			}
			return getExcelSheet().getCellAt(getRowIndex(), getColumnIndex() + 1);
		}

		/**
		 * Return type of this cell
		 * 
		 * @return
		 */
		@Override
		public CellType getCellType() {
			if (getCell() == null) {
				return CellType.Empty;
			}
			switch (getCell().getCellType()) {
				case Cell.CELL_TYPE_BLANK:
					return CellType.Blank;
				case Cell.CELL_TYPE_NUMERIC:
					return CellType.Numeric;
				case Cell.CELL_TYPE_STRING:
					return CellType.String;
				case Cell.CELL_TYPE_FORMULA:
					try {
						getCell().getNumericCellValue();
						return CellType.NumericFormula;
					} catch (IllegalStateException e1) {
						try {
							getCell().getStringCellValue();
							return CellType.StringFormula;
						} catch (IllegalStateException e2) {
							return CellType.Unknown;
						}
					}
				case Cell.CELL_TYPE_BOOLEAN:
					return CellType.Boolean;
				case Cell.CELL_TYPE_ERROR:
					return CellType.Error;
				default:
					return CellType.Unknown;
			}
		}

		@Override
		public void setCellType(CellType cellType) {
			createCellWhenNonExistant();
			switch (cellType) {
				case Blank:
					getCell().setCellType(Cell.CELL_TYPE_BLANK);
					break;
				case Boolean:
					getCell().setCellType(Cell.CELL_TYPE_BOOLEAN);
					break;
				case Error:
					getCell().setCellType(Cell.CELL_TYPE_ERROR);
					break;
				case Numeric:
					getCell().setCellType(Cell.CELL_TYPE_NUMERIC);
					break;
				case NumericFormula:
				case StringFormula:
					getCell().setCellType(Cell.CELL_TYPE_FORMULA);
					break;
				case String:
					getCell().setCellType(Cell.CELL_TYPE_STRING);
					break;
				default:
					break;
			}
		}

		private static final DataFormatter FORMATTER = new DataFormatter();

		/**
		 * Return the value to be displayed in a generic viewer
		 * 
		 * @return
		 */
		@Override
		public String getDisplayValue() {
			try {
				return FORMATTER.formatCellValue(getCell(), getExcelSheet().getEvaluator());
			} catch (RuntimeException e) {
				return "!ERROR: " + e.getMessage();
			}
		}

		/**
		 * Return the value (set or computed) associated with this cell
		 * 
		 * @return
		 */
		@Override
		public Object getCellValue() {
			switch (getCellType()) {
				case Blank:
					return null;
				case Boolean:
					return getCell().getBooleanCellValue();
				case Numeric:
					if (DateUtil.isCellDateFormatted(getCell())) {
						return getCell().getDateCellValue();
					}
					return getCell().getNumericCellValue();
				case NumericFormula:
					if (DateUtil.isCellDateFormatted(getCell())) {
						return getCell().getDateCellValue();
					}
					return getCell().getNumericCellValue();
				case String:
					/*if (cell.getStringCellValue().contains("\n")) {
						logger.warning("Excel Cell " + this.getCellIdentifier() + " contains line return.");
					}*/
					return getCell().getStringCellValue();
				case StringFormula:
					return getCell().getStringCellValue();
				case Empty:
					// System.out.println("EMPTY Cell at ind: " + this.getColumnIndex() + " ROW: " + this.getRowIndex() + " SHEET: "
					// + this.getExcelSheet().getName());
					return null;
				case Error:
					return getCell().getErrorCellValue();
				case Unknown:
					return "???";
				default:
					return "????";
			}
		};

		@Override
		public String getCellValueAsString() {
			switch (getCellType()) {
				case Blank:
					return null;
				case Boolean:
					return Boolean.toString(getCell().getBooleanCellValue());
				case Numeric:
					if (DateUtil.isCellDateFormatted(getCell())) {
						return getCell().getDateCellValue().toString();
					}
					return Double.toString(getCell().getNumericCellValue());
				case NumericFormula:
					if (DateUtil.isCellDateFormatted(getCell())) {
						return getCell().getDateCellValue().toString();
					}
					return Double.toString(getCell().getNumericCellValue());
				case String:
					return getCell().getStringCellValue();
				case StringFormula:
					return getCell().getStringCellValue();
				case Empty:
					return null;
				case Error:
					return Byte.toString(getCell().getErrorCellValue());
				case Unknown:
					return "???";
				default:
					return "????";
			}
		}

		@Override
		public void setCellValueAsString(String cellValueAsString) {
			if ((cellValueAsString == null && getCellValueAsString() != null)
					|| (cellValueAsString != null && !cellValueAsString.equals(getCellValueAsString()))) {
				String oldValue = getCellValueAsString();
				createCellWhenNonExistant();
				// System.out.println(
				// "*********** Setting new cell value: " + cellValueAsString + " at row=" + getRowIndex() + " col=" + getColumnIndex());
				getCell().setCellValue(cellValueAsString);
				getExcelSheet().getEvaluator().clearAllCachedResultValues();
				getPropertyChangeSupport().firePropertyChange("cellValueAsString", oldValue, cellValueAsString);
				getExcelSheet().getExcelWorkbook().setIsModified();
			}
		}

		private void setCellFormula(String formula) {
			if (formula.startsWith("=")) {
				formula = formula.substring(formula.indexOf("=") + 1);
			}
			try {
				getCell().setCellFormula(formula);
			} catch (IllegalArgumentException e) {
				logger.warning("Cannot parse forumla: " + formula);
			}
			getExcelSheet().getEvaluator().clearAllCachedResultValues();
		}

		private CellValue evaluateFormula() {
			return getExcelSheet().getEvaluator().evaluate(getCell());
		}

		protected void createCellWhenNonExistant() {
			if (getCell() == null) {
				getExcelRow().createRowWhenNonExistant(getExcelRow().getRowIndex());
				setCell(getExcelRow().getRow().createCell(getColumnIndex()));
			}
		}

		@Override
		public void setCellValue(Object value) {

			createCellWhenNonExistant();

			if (value == null) {
				getCell().setCellValue((String) null);
				return;
			}
			if (value instanceof String) {
				String valueString = (String) value;
				if (valueString.startsWith("=")) {
					setCellFormula(valueString);
					setCellValue(evaluateFormula().formatAsString());
					return;
				}
				if (valueString.equalsIgnoreCase("true")) {
					getCell().setCellValue(true);
					getExcelSheet().getEvaluator().clearAllCachedResultValues();
					return;
				}
				else if (valueString.equalsIgnoreCase("false")) {
					getCell().setCellValue(false);
					getExcelSheet().getEvaluator().clearAllCachedResultValues();
					return;
				}
				try {
					double doubleValue = Double.parseDouble(valueString);
					getCell().setCellValue(doubleValue);
					getExcelSheet().getEvaluator().clearAllCachedResultValues();
					return;
				} catch (NumberFormatException e) {
					getCell().setCellValue(valueString);
					getExcelSheet().getEvaluator().clearAllCachedResultValues();
					return;
				}
			}
			else if (value instanceof Integer) {
				getCell().setCellValue((Integer) value);
				getExcelSheet().getEvaluator().clearAllCachedResultValues();
				return;
			}
			else if (value instanceof Double) {
				getCell().setCellValue((Double) value);
				getExcelSheet().getEvaluator().clearAllCachedResultValues();
				return;
			}
			else if (value instanceof Float) {
				getCell().setCellValue((Float) value);
				getExcelSheet().getEvaluator().clearAllCachedResultValues();
				return;
			}
			else if (value instanceof Long) {
				getCell().setCellValue((Long) value);
				getExcelSheet().getEvaluator().clearAllCachedResultValues();
				return;
			}
			else if (value instanceof Boolean) {
				getCell().setCellValue((Boolean) value);
				getExcelSheet().getEvaluator().clearAllCachedResultValues();
				return;
			}

		}

		@Override
		public void setCellStringValue(String value) {
			setCellValueAsString(value);
		}

		@Override
		public void setCellNumericValue(Number value) {
			createCellWhenNonExistant();
			getCell().setCellValue(value.doubleValue());
		}

		@Override
		public void setCellBooleanValue(boolean value) {
			createCellWhenNonExistant();
			getCell().setCellValue(value);
		}

		@Override
		public void setCellDateValue(Date value) {
			createCellWhenNonExistant();
			CellStyle cellStyle = getExcelSheet().getExcelWorkbook().getWorkbook().createCellStyle();
			CreationHelper createHelper = getExcelSheet().getExcelWorkbook().getWorkbook().getCreationHelper();
			cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
			getCell().setCellStyle(cellStyle);
			getCell().setCellValue(value);
		}

		/**
		 * Return the specification of this cell
		 * 
		 * @return
		 */
		@Override
		public String getDisplayCellSpecification() {
			try {
				if (getCellType() == CellType.NumericFormula || getCellType() == CellType.StringFormula) {
					return "=" + FORMATTER.formatCellValue(getCell());
				}
				return FORMATTER.formatCellValue(getCell());
			} catch (RuntimeException e) {
				return "!ERROR: " + e.getMessage();
			}
		};

		/**
		 * Return a String identifying this cell (eg. (0,0) will return "A1")
		 * 
		 * @return
		 */
		@Override
		public String getCellIdentifier() {
			return CellReference.convertNumToColString(getColumnIndex());
		}

		/**
		 * Return string representation for this cell (debug)
		 */
		@Override
		public String toString() {
			return "[" + getCellIdentifier() + "]/" + getCellType().name() + "/" + (isMerged() ? "MergedWith:" + "["
					+ getTopLeftMergedCell().getCellIdentifier() + ":" + getBottomRightMergedCell().getCellIdentifier() + "]" + "/" : "")
					+ getDisplayValue();
		}

		@Override
		public boolean hasTopBorder() {
			if ((getCell() != null && getCell().getCellStyle().getBorderTop() != CellStyle.BORDER_NONE)) {
				return true;
			}
			if (isMerged()) {
				return getTopMergedCell().getCell() != null
						&& getTopMergedCell().getCell().getCellStyle().getBorderTop() != CellStyle.BORDER_NONE;
			}
			return false;
		}

		@Override
		public boolean hasLeftBorder() {
			if (getCell() != null && getCell().getCellStyle().getBorderLeft() != CellStyle.BORDER_NONE) {
				return true;
			}
			if (isMerged()) {
				return getLeftMergedCell().getCell() != null
						&& getLeftMergedCell().getCell().getCellStyle().getBorderLeft() != CellStyle.BORDER_NONE;
			}
			return false;
		}

		@Override
		public boolean hasRightBorder() {
			if (getCell() != null && getCell().getCellStyle().getBorderRight() != CellStyle.BORDER_NONE) {
				return true;
			}
			if (isMerged()) {
				return getRightMergedCell().getCell() != null
						&& getRightMergedCell().getCell().getCellStyle().getBorderRight() != CellStyle.BORDER_NONE;
			}
			return false;
		}

		@Override
		public boolean hasBottomBorder() {
			if (getCell() != null && getCell().getCellStyle().getBorderBottom() != CellStyle.BORDER_NONE) {
				return true;
			}
			if (isMerged()) {
				return getBottomMergedCell().getCell() != null
						&& getBottomMergedCell().getCell().getCellStyle().getBorderBottom() != CellStyle.BORDER_NONE;
			}
			return false;
		}

		@Override
		public void setCellStyle(CellStyle style) {
			if (getCell() != null) {
				getCell().setCellStyle(style);
			}
		}

		@Override
		public CellStyle getCellStyle() {
			if (getCell() != null) {
				return getCell().getCellStyle();
			}
			return null;
		}

		@Override
		public void setStyle(CellStyleFeature cellStyle, Object value) {
			if (getCell() != null && cellStyle != null) {
				// First get the old style
				CellStyle oldStyle = getCellStyle();
				// Create a new style
				CellStyle newStyle = getExcelSheet().getExcelWorkbook().getStyleManager().udapteCellStyle(cellStyle, value, oldStyle);
				// Set the style of this cell to the new style
				getCell().setCellStyle(newStyle);
			}
			return;
		}

		@Override
		public boolean delete(Object... context) {
			try {
				this.getExcelRow().getRow().removeCell(getCell());
				setCell(null);
			} catch (Exception e) {
				logger.warning("Unable to remove Excel Cell");
				return false;
			}
			return true;

		}

		@Override
		public String getIdentifier() {
			return ExcelColumn.getColumnLetters(getColumnIndex()) + getRowIndex();
		}

	}
}
