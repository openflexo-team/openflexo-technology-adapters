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

package org.openflexo.technologyadapter.excel.tests.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookRepository;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
@Ignore
public class TestRequestExcelWorkbook extends OpenflexoProjectAtRunTimeTestCase {
	protected static final Logger logger = Logger.getLogger(TestRequestExcelWorkbook.class.getPackage().getName());

	private static FlexoEditor editor;
	private static FlexoProject project;
	private static ExcelWorkbookResource workbook;
	private static String baseUrl;
	private static ExcelWorkbookRepository excelWorkbookRepository;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(ExcelTechnologyAdapter.class);
	}

	@Test
	@TestOrder(2)
	public void testCreateProject() {
		editor = createProject("TestProject");
		project = editor.getProject();
		System.out.println("Created project " + project.getProjectDirectory());
		assertTrue(project.getProjectDirectory().exists());
		assertTrue(project.getProjectDataResource().getIODelegate().exists());

		FlexoResourceCenter<?> resourceCenter = serviceManager.getResourceCenterService()
				.getFlexoResourceCenter("http://openflexo.org/excel-test");
		assertNotNull(resourceCenter);

		baseUrl = resourceCenter.getDefaultBaseURI();
		ExcelTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(ExcelTechnologyAdapter.class);

		ExcelWorkbookRepository<?> excelWorkbookRepository = technologicalAdapter.getExcelWorkbookRepository(resourceCenter);
		assertNotNull(excelWorkbookRepository);
		workbook = excelWorkbookRepository.getResource(baseUrl + "/TestResourceCenter/Excel/Workbook3.xlsx");
		assertNotNull(workbook);
	}

	@Test
	@TestOrder(3)
	public void testOperationsOnExcelWorkbook() {
		try {
			ExcelWorkbook excelWorkbook = workbook.loadResourceData(null);
			assertNotNull(excelWorkbook);

			// Workbook operations
			ExcelSheet excelSheet1a = excelWorkbook.getExcelSheetAtPosition(0);
			assertNotNull(excelSheet1a);
			ExcelSheet excelSheet1b = excelWorkbook.getExcelSheetByName("Sheet1");
			assertNotNull(excelSheet1b);
			assertEquals(excelSheet1a, excelSheet1b);

			// Sheet operations
			ExcelCell excelCell1a = excelSheet1a.getCellAt(2, 1);
			assertNotNull(excelCell1a);
			assertEquals(excelCell1a.getCellValueAsString(), "A");
			ExcelCell excelCell1b = excelSheet1a.getCellFromName("B3");
			assertNotNull(excelCell1b);
			assertEquals(excelCell1b, excelCell1a);

			excelSheet1a.setCellValueFromName("B3", "OK");
			assertEquals(excelCell1a.getCellValueAsString(), "OK");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
		} catch (FlexoException e) {
			e.printStackTrace();
		}
	}
}
