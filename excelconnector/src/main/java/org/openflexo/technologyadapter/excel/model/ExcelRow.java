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

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PastingPoint;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.model.ExcelStyleManager.CellStyleFeature;

/**
 * Represents an Excel row, implemented as a wrapper of a POI row
 * 
 * @author vincent, sylvain
 * 
 */
@ModelEntity
@ImplementationClass(value = ExcelRow.ExcelRowImpl.class)
@XMLElement
public interface ExcelRow extends ExcelObject, ExcelStyleObject {

	@PropertyIdentifier(type = ExcelSheet.class)
	public static final String EXCEL_SHEET_KEY = "excelSheet";
	@PropertyIdentifier(type = Row.class)
	public static final String ROW_KEY = "row";
	@PropertyIdentifier(type = ExcelCell.class, cardinality = Cardinality.LIST)
	public static final String EXCEL_CELLS_KEY = "excelCells";

	/**
	 * Return {@link ExcelSheet} where this {@link ExcelRow} is defined
	 * 
	 * @return
	 */
	@Getter(value = EXCEL_SHEET_KEY)
	public ExcelSheet getExcelSheet();

	/**
	 * Sets {@link ExcelSheet} where this {@link ExcelRow} is defined
	 * 
	 * @param workbook
	 */
	@Setter(EXCEL_SHEET_KEY)
	public void setExcelSheet(ExcelSheet sheet);

	/**
	 * Return row wrapped by this {@link ExcelRow}
	 * 
	 * @return
	 */
	@Getter(value = ROW_KEY, ignoreType = true)
	public Row getRow();

	/**
	 * Sets row wrapped by this {@link ExcelRow}
	 * 
	 * @param sheet
	 */
	@Setter(ROW_KEY)
	public void setRow(Row row);

	/**
	 * Return all {@link ExcelRow} defined in this {@link ExcelSheet}
	 * 
	 * @return
	 */
	@Getter(value = EXCEL_CELLS_KEY, cardinality = Cardinality.LIST, inverse = ExcelCell.EXCEL_ROW_KEY)
	@XMLElement
	@Embedded
	@CloningStrategy(StrategyType.CLONE)
	public List<ExcelCell> getExcelCells();

	@Setter(EXCEL_CELLS_KEY)
	public void setExcelCells(List<ExcelCell> excelCells);

	@Adder(EXCEL_CELLS_KEY)
	@PastingPoint
	public void addToExcelCells(ExcelCell anExcelCell);

	@Remover(EXCEL_CELLS_KEY)
	public void removeFromExcelRows(ExcelCell anExcelCell);

	public int getRowIndex();

	public ExcelCell getExcelCellAt(int columnIndex);

	public void createRowWhenNonExistant(int rowIndex);

	public ExcelCell createCellAt(int columnIndex);

	/**
	 * Default base implementation for {@link ExcelRow}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class ExcelRowImpl extends ExcelObjectImpl implements ExcelRow {

		private static final Logger logger = Logger.getLogger(ExcelRow.class.getPackage().getName());

		// private Row row;
		// private final ExcelSheet excelSheet;
		// private final List<ExcelCell> excelCells;

		public ExcelRowImpl() {
		}

		@Override
		public ExcelWorkbook getResourceData() {
			return getExcelSheet().getExcelWorkbook();
		}

		private boolean isConverting = false;

		private void ensureConversion() {
			if (isConverting) {
				return;
			}
			try {
				isConverting = true;
				getResourceData().getConverter().getRowReference(getRow()).ensureConversion();
			} finally {
				isConverting = false;
			}
		}

		@Override
		public List<ExcelCell> getExcelCells() {
			ensureConversion();
			return (List<ExcelCell>) performSuperGetter(EXCEL_CELLS_KEY);
		}

		/*@Override
		public Row getRow() {
			return row;
		}*/

		/*public ExcelRow(Row row, ExcelSheet excelSheet, ExcelTechnologyAdapter adapter) {
			super(adapter);
			this.row = row;
			this.excelSheet = excelSheet;
			excelCells = new ArrayList<ExcelCell>();
		}*/

		@Override
		public void createRowWhenNonExistant(int rowIndex) {
			if (getRow() == null) {
				setRow(getExcelSheet().getSheet().createRow(rowIndex));
			}
		}

		/*@Override
		public ExcelSheet getExcelSheet() {
			return excelSheet;
		}
		
		@Override
		public List<ExcelCell> getExcelCells() {
			return excelCells;
		}
		
		@Override
		public void addToExcelCells(ExcelCell newExcelCell) {
			this.excelCells.add(newExcelCell);
			getExcelSheet().getWorkbook().addToAccessibleExcelObjects(newExcelCell);
		}
		
		public void removeFromExcelCells(ExcelCell deletedExcelCell) {
			this.excelCells.remove(deletedExcelCell);
			getExcelSheet().getWorkbook().removeFromAccessibleExcelObjects(deletedExcelCell);
		}*/

		/*@Override
		public String getName() {
			return "row." + getRowNum();
		}*/

		@Override
		public int getRowIndex() {
			if (getRow() != null) {
				return getRow().getRowNum();
			}
			return getExcelSheet().getExcelRows().indexOf(this);
		}

		@Override
		public ExcelCell getExcelCellAt(int columnIndex) {
			if (columnIndex < 0) {
				return null;
			}
			// Append missing cells
			while (getExcelCells().size() <= columnIndex) {
				// No need to create the cell now, but the ExcelCell should be instantiated
				ExcelCell newCell = getFactory().makeExcelCell();
				addToExcelCells(newCell);
			}
			return getExcelCells().get(columnIndex);
		}

		public ExcelCell getCellAtExcelColumn(ExcelColumn column) {
			if (column.getColumnIndex() < 0) {
				return null;
			}
			// Append missing cells
			while (getExcelCells().size() <= column.getColumnIndex()) {
				// No need to create the cell now, but the ExcelCell should be instantiated
				addToExcelCells(getFactory().makeExcelCell());
			}
			return getExcelCells().get(column.getColumnIndex());
		}

		public void setRowStyle(CellStyle style) {
			if (getRow() != null) {
				getRow().setRowStyle(style);
				// Ensure that this is really done for all the cells
				for (ExcelCell cell : getExcelCells()) {
					cell.setCellStyle(style);
				}
			}
		}

		public CellStyle getRowStyle() {
			if (getRow() != null) {
				return getRow().getRowStyle();
			}
			return null;
		}

		@Override
		public void setStyle(CellStyleFeature cellStyle, Object value) {
			if (getRow() != null && cellStyle != null) {
				// First get the old style
				CellStyle oldStyle = getRowStyle();
				// Create a new style
				CellStyle newStyle = getExcelSheet().getExcelWorkbook().getStyleManager().udapteCellStyle(cellStyle, value, oldStyle);
				// Set the style of this cell to the new style
				setRowStyle(newStyle);
			}
			return;
		}

		@Override
		public boolean delete(Object... context) {
			try {
				for (ExcelCell e : getExcelCells()) {
					e.delete(context);
				}
				getExcelSheet().getSheet().removeRow(this.getRow());
				setRow(null);
			} catch (Exception e) {
				logger.warning("Unable to remove Excel Row");
				return false;
			}
			return true;

		}

		@Override
		public ExcelCell createCellAt(int columnIndex) {
			BasicExcelModelConverter converter = getResourceData().getConverter();
			return converter.getRowReference(getRow()).newCell(columnIndex);
		}

	}
}
