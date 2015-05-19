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

import org.apache.poi.ss.usermodel.Workbook;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.io.BasicExcelModelConverter;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;

public class ExcelWorkbook extends ExcelObject implements ResourceData<ExcelWorkbook> {

	private Workbook workbook;
	private ExcelWorkbookResource resource;
	private List<ExcelSheet> excelSheets;
	private BasicExcelModelConverter converter;
	private ArrayList<ExcelObject> accessibleExcelObjects;

	public Workbook getWorkbook() {
		return workbook;
	}

	public ExcelWorkbook(Workbook workbook, ExcelTechnologyAdapter adapter) {
		super(adapter);
		this.workbook = workbook;
		excelSheets = new ArrayList<ExcelSheet>();
	}

	public ExcelWorkbook(ExcelTechnologyAdapter adapter) {
		super(adapter);
		excelSheets = new ArrayList<ExcelSheet>();
	}

	public ExcelWorkbook(Workbook workbook, BasicExcelModelConverter converter, ExcelTechnologyAdapter adapter) {
		super(adapter);
		this.workbook = workbook;
		this.converter = converter;
		excelSheets = new ArrayList<ExcelSheet>();
	}

	public BasicExcelModelConverter getConverter() {
		return converter;
	}

	@Override
	public FlexoResource<ExcelWorkbook> getResource() {
		return resource;
	}

	@Override
	public void setResource(FlexoResource<ExcelWorkbook> resource) {
		this.resource = (ExcelWorkbookResource) resource;
	}

	@Override
	public String getName() {
		return getResource().getName();
	}

	public List<ExcelSheet> getExcelSheets() {
		return excelSheets;
	}

	public void addToExcelSheets(ExcelSheet newExcelSheet) {
		this.excelSheets.add(newExcelSheet);
		addToAccessibleExcelObjects(newExcelSheet);
	}

	public void removeFromExcelSheets(ExcelSheet deletedExcelSheet) {
		this.excelSheets.remove(deletedExcelSheet);
		removeFromAccessibleExcelObjects(deletedExcelSheet);
	}

	public List<ExcelObject> getAccessibleExcelObjects() {
		if(accessibleExcelObjects==null){
			accessibleExcelObjects = new ArrayList<ExcelObject>();
		}
		return accessibleExcelObjects;
	}
	
	public void addToAccessibleExcelObjects(ExcelObject excelObject){
		getAccessibleExcelObjects().add(excelObject);
	}
	
	public void removeFromAccessibleExcelObjects(ExcelObject excelObject){
		getAccessibleExcelObjects().remove(excelObject);
	}

	@Override
	public String getUri() {
		return getName();
	}

}
