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

import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;

/**
 * Represents an excel cells range, as a rectangular selection of cells in a sheet
 * 
 * @author vincent, sylvain
 * 
 */
@ModelEntity
@ImplementationClass(value = ExcelCellRange.ExcelCellRangeImpl.class)
@XMLElement
public interface ExcelCellRange extends ExcelObject {

	@PropertyIdentifier(type = ExcelSheet.class)
	public static final String EXCEL_SHEET_KEY = "excelSheet";
	@PropertyIdentifier(type = ExcelCell.class)
	public static final String TOP_LEFT_CELL_KEY = "topLeftCell";
	@PropertyIdentifier(type = ExcelCell.class)
	public static final String BOTTOM_RIGHT_CELL_KEY = "bottomRightCell";

	/**
	 * Return {@link ExcelWorkbook} where this {@link ExcelCellRange} is defined
	 * 
	 * @return
	 */
	public ExcelWorkbook getExcelWorkbook();

	/**
	 * Return {@link ExcelWorkbook} where this {@link ExcelCellRange} is defined
	 * 
	 * @return
	 */
	@Getter(value = EXCEL_SHEET_KEY)
	public ExcelSheet getExcelSheet();

	/**
	 * Sets {@link ExcelSheet} where this {@link ExcelCellRange} is defined
	 * 
	 * @param sheet
	 */
	@Setter(EXCEL_SHEET_KEY)
	public void setExcelSheet(ExcelSheet sheet);

	/**
	 * Return {@link ExcelCell} marking the top-left corner of the rectangle selection
	 * 
	 * @return
	 */
	@Getter(value = TOP_LEFT_CELL_KEY)
	public ExcelCell getTopLeftCell();

	/**
	 * Sets {@link ExcelCell} marking the top-left corner of the rectangle selection
	 * 
	 * @param topLeftCell
	 */
	@Setter(TOP_LEFT_CELL_KEY)
	public void setTopLeftCell(ExcelCell topLeftCell);

	/**
	 * Return {@link ExcelCell} marking the bottom-right corner of the rectangle selection
	 * 
	 * @return
	 */
	@Getter(value = BOTTOM_RIGHT_CELL_KEY)
	public ExcelCell getBottomRightCell();

	/**
	 * Sets {@link ExcelCell} marking the bottom-right corner of the rectangle selection
	 * 
	 * @param topLeftCell
	 */
	@Setter(BOTTOM_RIGHT_CELL_KEY)
	public void setBottomRighCell(ExcelCell topLeftCell);

	/**
	 * Return identifier of the cell under the form <code>B7:T9</code>
	 * 
	 * @return
	 */
	public String getIdentifier();

	public boolean isSingleCell();

	/**
	 * Default base implementation for {@link ExcelCellRange}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class ExcelCellRangeImpl extends ExcelObjectImpl implements ExcelCellRange {

		@Override
		public ExcelWorkbook getExcelWorkbook() {
			return getExcelSheet().getExcelWorkbook();
		}

		@Override
		public ExcelSheet getExcelSheet() {
			ExcelSheet returned = (ExcelSheet) performSuperGetter(EXCEL_SHEET_KEY);
			if (returned == null && getTopLeftCell() != null) {
				return getTopLeftCell().getExcelSheet();
			}
			return returned;
		}

		@Override
		public ExcelWorkbook getResourceData() {
			return getExcelSheet().getExcelWorkbook();
		}

		/**
		 * Return identifier of the cell under the form <code>B7:T9</code>
		 * 
		 * @return
		 */
		@Override
		public String getIdentifier() {
			return getTopLeftCell().getIdentifier() + ":" + getBottomRightCell().getIdentifier();
		}

		@Override
		public boolean isSingleCell() {
			return getTopLeftCell().getRowIndex() == getBottomRightCell().getRowIndex()
					&& getTopLeftCell().getColumnIndex() == getBottomRightCell().getColumnIndex();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ExcelCellRange other = (ExcelCellRange) obj;

			if (getExcelSheet() == null) {
				if (other.getExcelSheet() != null)
					return false;
			}
			else if (!getExcelSheet().equals(other.getExcelSheet()))
				return false;

			if (getBottomRightCell() == null) {
				if (other.getBottomRightCell() != null)
					return false;
			}
			else if (!getBottomRightCell().equals(other.getBottomRightCell()))
				return false;

			if (getTopLeftCell() == null) {
				if (other.getTopLeftCell() != null)
					return false;
			}
			else if (!getTopLeftCell().equals(other.getTopLeftCell()))
				return false;
			return true;
		}
	}
}
