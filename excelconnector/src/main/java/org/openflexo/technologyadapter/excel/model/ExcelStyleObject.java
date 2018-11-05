package org.openflexo.technologyadapter.excel.model;

import org.openflexo.technologyadapter.excel.model.ExcelStyleManager.CellStyleFeature;

public interface ExcelStyleObject {
	
	public void setStyle(CellStyleFeature cellStyle, Object value);
	
}
