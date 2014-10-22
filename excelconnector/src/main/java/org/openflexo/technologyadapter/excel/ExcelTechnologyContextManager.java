/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.excel;

import java.util.HashMap;
import java.util.Map;

import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;

public class ExcelTechnologyContextManager extends TechnologyContextManager<ExcelTechnologyAdapter> {

	/** Stores all known excel workbook where key is the URI of ExcelWorbook */
	protected Map<String, ExcelWorkbookResource> excelWorkbooks = new HashMap<String, ExcelWorkbookResource>();
	
	public ExcelTechnologyContextManager(ExcelTechnologyAdapter adapter, FlexoResourceCenterService resourceCenterService) {
		super(adapter, resourceCenterService);
	}

	@Override
	public ExcelTechnologyAdapter getTechnologyAdapter() {
		return (ExcelTechnologyAdapter) super.getTechnologyAdapter();
	}
	
	public ExcelWorkbookResource getExcelWorkbookResource(Object excelWorkbook) {
		return excelWorkbooks.get(excelWorkbook);
	}
	
	public void registerExcelWorkbook(ExcelWorkbookResource newExcelWorkbookResource) {
		registerResource(newExcelWorkbookResource);
		excelWorkbooks.put(newExcelWorkbookResource.getURI(), newExcelWorkbookResource);
	}

}
