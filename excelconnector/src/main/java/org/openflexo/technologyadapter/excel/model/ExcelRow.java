package org.openflexo.technologyadapter.excel.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Row;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;

/**
 * Represents an Excel row, implemented as a wrapper of a POI row
 * 
 * @author vincent, sylvain
 * 
 */
public class ExcelRow extends ExcelObject {

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
			addToExcelCells(new ExcelCell(null, this, getTechnologyAdapter()));
		}
		return getExcelCells().get(columnIndex);
	}

	public ExcelCell getCellAtExcelColumn(ExcelColumn column) {
		if (column.getColNumber() < 0) {
			return null;
		}
		// Append missing cells
		while (getExcelCells().size() <= column.getColNumber()) {
			addToExcelCells(new ExcelCell(null, this, getTechnologyAdapter()));
		}
		return getExcelCells().get(column.getColNumber());
	}

	@Override
	public String getUri() {
		return getExcelSheet().getUri() + "/" + getName();
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
