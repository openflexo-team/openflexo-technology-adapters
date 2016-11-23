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
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelStyleManager.CellStyleFeature;

/**
 * Represents an Excel cell, implemented as a wrapper of a POI Cell
 * 
 * @author vincent, sylvain
 * 
 */
public class ExcelCell extends ExcelObject implements ExcelStyleObject {

	static final Logger logger = Logger.getLogger(ExcelCell.class.getPackage().getName());

	private Cell cell;
	private final ExcelRow excelRow;

	private CellRangeAddress cellRange = null;

	public enum CellType {
		Blank, Numeric, String, NumericFormula, StringFormula, Boolean, Error, Empty, Unknown
	}

	public Cell getCell() {
		return cell;
	}

	public ExcelCell(Cell cell, ExcelRow excelRow, ExcelTechnologyAdapter adapter) {
		super(adapter);
		this.cell = cell;
		this.excelRow = excelRow;
	}

	public ExcelRow getExcelRow() {
		return excelRow;
	}

	public ExcelSheet getExcelSheet() {
		return getExcelRow().getExcelSheet();
	}

	public int getColumnIndex() {
		if (cell != null) {
			return cell.getColumnIndex();
		}
		else {
			return getExcelRow().getExcelCells().indexOf(this);
		}
	}

	public int getRowIndex() {
		if (cell != null) {
			return cell.getRowIndex();
		}
		else {
			return getExcelRow().getRowIndex();
		}
	}

	@Override
	public String getName() {

		if (cell != null) {
			return "cell." + "row" + cell.getRowIndex() + "." + "col_" + cell.getColumnIndex();
		}
		else {
			// This happens when the cell is empty (without any value)
			// TODO: there is a risk for major issues here
			return "EmpyCell";
		}
	}

	public void merge(CellRangeAddress cellRange) {
		this.cellRange = cellRange;
	}

	/**
	 * Indicated if this cell is merged
	 * 
	 * @return
	 */
	public boolean isMerged() {
		return cellRange != null;
	}

	/**
	 * Return the list of cells with which this cell has been merged
	 * 
	 * @return
	 */
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
	public CellType getCellType() {
		if (cell == null) {
			return CellType.Empty;
		}
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				return CellType.Blank;
			case Cell.CELL_TYPE_NUMERIC:
				return CellType.Numeric;
			case Cell.CELL_TYPE_STRING:
				return CellType.String;
			case Cell.CELL_TYPE_FORMULA:
				try {
					cell.getNumericCellValue();
					return CellType.NumericFormula;
				} catch (IllegalStateException e1) {
					try {
						cell.getStringCellValue();
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

	public void setCellType(CellType cellType) {
		createCellWhenNonExistant();
		switch (cellType) {
			case Blank:
				cell.setCellType(Cell.CELL_TYPE_BLANK);
				break;
			case Boolean:
				cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
				break;
			case Error:
				cell.setCellType(Cell.CELL_TYPE_ERROR);
				break;
			case Numeric:
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				break;
			case NumericFormula:
			case StringFormula:
				cell.setCellType(Cell.CELL_TYPE_FORMULA);
				break;
			case String:
				cell.setCellType(Cell.CELL_TYPE_STRING);
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
	public String getDisplayValue() {
		try {
			return FORMATTER.formatCellValue(cell, getExcelSheet().getEvaluator());
		} catch (RuntimeException e) {
			return "!ERROR: " + e.getMessage();
		}
	}

	/**
	 * Return the value (set or computed) associated with this cell
	 * 
	 * @return
	 */
	public Object getCellValue() {
		switch (getCellType()) {
			case Blank:
				return null;
			case Boolean:
				return cell.getBooleanCellValue();
			case Numeric:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue();
				}
				return cell.getNumericCellValue();
			case NumericFormula:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue();
				}
				return cell.getNumericCellValue();
			case String:
				/*if (cell.getStringCellValue().contains("\n")) {
					logger.warning("Excel Cell " + this.getCellIdentifier() + " contains line return.");
				}*/
				return cell.getStringCellValue();
			case StringFormula:
				return cell.getStringCellValue();
			case Empty:
				// System.out.println("EMPTY Cell at ind: " + this.getColumnIndex() + " ROW: " + this.getRowIndex() + " SHEET: "
				// + this.getExcelSheet().getName());
				return null;
			case Error:
				return cell.getErrorCellValue();
			case Unknown:
				return "???";
			default:
				return "????";
		}
	};

	public String getCellValueAsString() {
		switch (getCellType()) {
			case Blank:
				return null;
			case Boolean:
				return Boolean.toString(cell.getBooleanCellValue());
			case Numeric:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue().toString();
				}
				return Double.toString(cell.getNumericCellValue());
			case NumericFormula:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue().toString();
				}
				return Double.toString(cell.getNumericCellValue());
			case String:
				return cell.getStringCellValue();
			case StringFormula:
				return cell.getStringCellValue();
			case Empty:
				return null;
			case Error:
				return Byte.toString(cell.getErrorCellValue());
			case Unknown:
				return "???";
			default:
				return "????";
		}
	}

	public void setCellValueAsString(String cellValueAsString) {
		if ((cellValueAsString == null && getCellValueAsString() != null)
				|| (cellValueAsString != null && !cellValueAsString.equals(getCellValueAsString()))) {
			String oldValue = getCellValueAsString();
			createCellWhenNonExistant();
			// System.out.println(
			// "*********** Setting new cell value: " + cellValueAsString + " at row=" + getRowIndex() + " col=" + getColumnIndex());
			cell.setCellValue(cellValueAsString);
			getExcelSheet().getEvaluator().clearAllCachedResultValues();
			getPropertyChangeSupport().firePropertyChange("cellValueAsString", oldValue, cellValueAsString);
			getExcelSheet().getWorkbook().setIsModified();
		}
	}

	private void setCellFormula(String formula) {
		if (formula.startsWith("=")) {
			formula = formula.substring(formula.indexOf("=") + 1);
		}
		try {
			cell.setCellFormula(formula);
		} catch (IllegalArgumentException e) {
			logger.warning("Cannot parse forumla: " + formula);
		}
		getExcelSheet().getEvaluator().clearAllCachedResultValues();
	}

	private CellValue evaluateFormula() {
		return getExcelSheet().getEvaluator().evaluate(cell);
	}

	protected void createCellWhenNonExistant() {
		if (cell == null) {
			getExcelRow().createRowWhenNonExistant();
			cell = getExcelRow().getRow().createCell(getColumnIndex());
		}
	}

	public void setCellValue(Object value) {

		createCellWhenNonExistant();

		if (value == null) {
			cell.setCellValue((String) null);
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
				cell.setCellValue(true);
				getExcelSheet().getEvaluator().clearAllCachedResultValues();
				return;
			}
			else if (valueString.equalsIgnoreCase("false")) {
				cell.setCellValue(false);
				getExcelSheet().getEvaluator().clearAllCachedResultValues();
				return;
			}
			try {
				double doubleValue = Double.parseDouble(valueString);
				cell.setCellValue(doubleValue);
				getExcelSheet().getEvaluator().clearAllCachedResultValues();
				return;
			} catch (NumberFormatException e) {
				cell.setCellValue(valueString);
				getExcelSheet().getEvaluator().clearAllCachedResultValues();
				return;
			}
		}
		else if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
			getExcelSheet().getEvaluator().clearAllCachedResultValues();
			return;
		}
		else if (value instanceof Double) {
			cell.setCellValue((Double) value);
			getExcelSheet().getEvaluator().clearAllCachedResultValues();
			return;
		}
		else if (value instanceof Float) {
			cell.setCellValue((Float) value);
			getExcelSheet().getEvaluator().clearAllCachedResultValues();
			return;
		}
		else if (value instanceof Long) {
			cell.setCellValue((Long) value);
			getExcelSheet().getEvaluator().clearAllCachedResultValues();
			return;
		}
		else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
			getExcelSheet().getEvaluator().clearAllCachedResultValues();
			return;
		}

	}

	public void setCellStringValue(String value) {
		setCellValueAsString(value);
	}

	public void setCellNumericValue(Number value) {
		createCellWhenNonExistant();
		cell.setCellValue(value.doubleValue());
	}

	public void setCellBooleanValue(boolean value) {
		createCellWhenNonExistant();
		cell.setCellValue(value);
	}

	public void setCellDateValue(Date value) {
		createCellWhenNonExistant();
		CellStyle cellStyle = getExcelSheet().getWorkbook().getWorkbook().createCellStyle();
		CreationHelper createHelper = getExcelSheet().getWorkbook().getWorkbook().getCreationHelper();
		cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("m/d/yy h:mm"));
		cell.setCellStyle(cellStyle);
		cell.setCellValue(value);
	}

	/**
	 * Return the specification of this cell
	 * 
	 * @return
	 */
	public String getDisplayCellSpecification() {
		try {
			if (getCellType() == CellType.NumericFormula || getCellType() == CellType.StringFormula) {
				return "=" + FORMATTER.formatCellValue(cell);
			}
			return FORMATTER.formatCellValue(cell);
		} catch (RuntimeException e) {
			return "!ERROR: " + e.getMessage();
		}
	};

	/**
	 * Return a String identifying this cell (eg. (0,0) will return "A1")
	 * 
	 * @return
	 */
	public String getCellIdentifier() {
		return CellReference.convertNumToColString(getColumnIndex());
	}

	/**
	 * Return string representation for this cell (debug)
	 */
	@Override
	public String toString() {
		return "["
				+ getCellIdentifier()
				+ "]/"
				+ getCellType().name()
				+ "/"
				+ (isMerged() ? "MergedWith:" + "[" + getTopLeftMergedCell().getCellIdentifier() + ":"
						+ getBottomRightMergedCell().getCellIdentifier() + "]" + "/" : "") + getDisplayValue();
	}

	public boolean hasTopBorder() {
		if ((cell != null && cell.getCellStyle().getBorderTop() != CellStyle.BORDER_NONE)) {
			return true;
		}
		if (isMerged()) {
			return getTopMergedCell().cell != null && getTopMergedCell().cell.getCellStyle().getBorderTop() != CellStyle.BORDER_NONE;
		}
		return false;
	}

	public boolean hasLeftBorder() {
		if (cell != null && cell.getCellStyle().getBorderLeft() != CellStyle.BORDER_NONE) {
			return true;
		}
		if (isMerged()) {
			return getLeftMergedCell().cell != null && getLeftMergedCell().cell.getCellStyle().getBorderLeft() != CellStyle.BORDER_NONE;
		}
		return false;
	}

	public boolean hasRightBorder() {
		if (cell != null && cell.getCellStyle().getBorderRight() != CellStyle.BORDER_NONE) {
			return true;
		}
		if (isMerged()) {
			return getRightMergedCell().cell != null && getRightMergedCell().cell.getCellStyle().getBorderRight() != CellStyle.BORDER_NONE;
		}
		return false;
	}

	public boolean hasBottomBorder() {
		if (cell != null && cell.getCellStyle().getBorderBottom() != CellStyle.BORDER_NONE) {
			return true;
		}
		if (isMerged()) {
			return getBottomMergedCell().cell != null
					&& getBottomMergedCell().cell.getCellStyle().getBorderBottom() != CellStyle.BORDER_NONE;
		}
		return false;
	}

	public void setCellStyle(CellStyle style) {
		if (getCell() != null) {
			getCell().setCellStyle(style);
		}
	}

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
			CellStyle newStyle = getExcelSheet().getWorkbook().getStyleManager().udapteCellStyle(cellStyle, value, oldStyle);
			// Set the style of this cell to the new style
			getCell().setCellStyle(newStyle);
		}
		return;
	}

	@Override
	public String getUri() {
		return getExcelRow().getUri() + "/" + getName();
	}

	@Override
	public boolean delete(Object... context) {
		try {
			this.getExcelRow().getRow().removeCell(getCell());
			cell = null;
		} catch (Exception e) {
			logger.warning("Unable to remove Excel Cell");
			return false;
		}
		return true;

	}

}
