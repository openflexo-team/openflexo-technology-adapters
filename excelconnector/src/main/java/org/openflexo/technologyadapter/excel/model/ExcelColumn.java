package org.openflexo.technologyadapter.excel.model;

import org.apache.poi.ss.util.CellReference;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;

public class ExcelColumn extends ExcelObject {
	
	private int colNumber;
	
	public static String getColumnLetters(int colIndex){
		return CellReference.convertNumToColString(colIndex);
	}
	
	public static int getColumnIndex(String columnLetter){
		return CellReference.convertColStringToIndex(columnLetter.toUpperCase());
	}

	public ExcelColumn(int colNumber, ExcelTechnologyAdapter adapter) {
		super(adapter);
		this.colNumber = colNumber;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Column"+colNumber;
	}

	public int getColNumber() {
		return colNumber;
	}

	public void setColNumber(int colNumber) {
		this.colNumber = colNumber;
	}

}
