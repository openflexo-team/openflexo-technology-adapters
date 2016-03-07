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

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelStyleManager.CellStyleFeature;

/**
 * Represents an Excel row, implemented as a wrapper of a POI row
 * 
 * @author vincent, sylvain
 * 
 */
public class ExcelRow extends ExcelObject implements ExcelStyleObject {

	private static final Logger logger = Logger.getLogger(ExcelRow.class.getPackage().getName());

	private Row row;
	private final ExcelSheet excelSheet;
	private final List<ExcelCell> excelCells;

	public Row getRow() {
		return row;
	}

	public ExcelRow(Row row, ExcelSheet excelSheet, ExcelTechnologyAdapter adapter) {
		super(adapter);
		this.row = row;
		this.excelSheet = excelSheet;
		excelCells = new ArrayList<ExcelCell>();
	}

	protected void createRowWhenNonExistant() {
		if (row == null) {
			row = excelSheet.getSheet().createRow(getRowNum());
		}
	}

	public ExcelSheet getExcelSheet() {
		return excelSheet;
	}

	public List<ExcelCell> getExcelCells() {
		return excelCells;
	}

	public void addToExcelCells(ExcelCell newExcelCell) {
		this.excelCells.add(newExcelCell);
		getExcelSheet().getWorkbook().addToAccessibleExcelObjects(newExcelCell);
	}

	public void removeFromExcelCells(ExcelCell deletedExcelCell) {
		this.excelCells.remove(deletedExcelCell);
		getExcelSheet().getWorkbook().removeFromAccessibleExcelObjects(deletedExcelCell);
	}

	@Override
	public String getName() {
		return "row." + getRowNum();
	}

	public int getRowIndex() {
		if (row != null) {
			return row.getRowNum();
		}
		return getExcelSheet().getExcelRows().indexOf(this);
	}

	public int getRowNum() {
		return getRowIndex();
	}

	public ExcelCell getExcelCell(int columnIndex) {
		return getCellAt(columnIndex);
	}

	public ExcelCell getCellAt(int columnIndex) {
		if (columnIndex < 0) {
			return null;
		}
		// Append missing cells
		while (getExcelCells().size() <= columnIndex) {
			// No need to create the cell now, but the ExcelCell should be instantiated
			addToExcelCells(new ExcelCell(/*row.createCell(getExcelCells().size())*/null, this, getTechnologyAdapter()));
		}
		return getExcelCells().get(columnIndex);
	}

	public ExcelCell getCellAtExcelColumn(ExcelColumn column) {
		if (column.getColNumber() < 0) {
			return null;
		}
		// Append missing cells
		while (getExcelCells().size() <= column.getColNumber()) {
			// No need to create the cell now, but the ExcelCell should be instantiated
			addToExcelCells(new ExcelCell(/*row.createCell(getExcelCells().size())*/null, this, getTechnologyAdapter()));
		}
		return getExcelCells().get(column.getColNumber());
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
			CellStyle newStyle = getExcelSheet().getWorkbook().getStyleManager().udapteCellStyle(cellStyle, value, oldStyle);
			// Set the style of this cell to the new style
			setRowStyle(newStyle);
		}
		return;
	}

	@Override
	public String getUri() {
		return getExcelSheet().getUri() + "/" + getName();
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean delete(Object... context) {
		try {
			for (ExcelCell e : excelCells) {
				e.delete(context);
			}
			getExcelSheet().getSheet().removeRow(this.getRow());
			row = null;
		} catch (Exception e) {
			logger.warning("Unable to remove Excel Row");
			return false;
		}
		return true;

	}

}
