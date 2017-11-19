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

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelRow;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookRepository;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestLoadExcelDocuments extends AbstractTestExcel {
	protected static final Logger logger = Logger.getLogger(TestLoadExcelDocuments.class.getPackage().getName());

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(ExcelTechnologyAdapter.class);
	}

	@Test
	@TestOrder(3)
	public void testExcelLoading() {
		ExcelTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(ExcelTechnologyAdapter.class);

		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			ExcelWorkbookRepository<?> workbookRepository = technologicalAdapter.getExcelWorkbookRepository(resourceCenter);
			assertNotNull(workbookRepository);
			Collection<ExcelWorkbookResource> documents = workbookRepository.getAllResources();
			for (ExcelWorkbookResource docResource : documents) {
				try {
					docResource.loadResourceData(null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ResourceLoadingCancelledException e) {
					e.printStackTrace();
				} catch (FlexoException e) {
					e.printStackTrace();
				}
				assertNotNull(docResource.getLoadedResourceData());
				System.out.println("URI of document: " + docResource.getURI());
				for (ExcelSheet excelSheet : docResource.getLoadedResourceData().getExcelSheets()) {
					System.out.println("Sheet " + excelSheet.getName() + " rows:" + excelSheet.getExcelRows().size());
				}
			}
		}
	}

	@Test
	@TestOrder(4)
	public void testWorkbook1Loading() {

		ExcelWorkbook workbook1 = getWorkbook("Workbook1.xlsx");
		System.out.println("Workbook1.xlsx:\n" + workbook1);

		assertEquals(1, workbook1.getExcelSheets().size());
		ExcelSheet sheet1 = workbook1.getExcelSheets().get(0);

		assertEquals(3, sheet1.getExcelRows().size());
		ExcelRow row1 = sheet1.getExcelRows().get(0);
		ExcelRow row2 = sheet1.getExcelRows().get(1);
		ExcelRow row3 = sheet1.getExcelRows().get(2);

		assertEquals(3, row1.getExcelCells().size());
		ExcelCell cell11 = row1.getExcelCellAt(0);
		ExcelCell cell12 = row1.getExcelCellAt(1);
		ExcelCell cell13 = row1.getExcelCellAt(2);
		assertEquals("A", cell11.getCellValueAsString());
		assertEquals("B", cell12.getCellValueAsString());
		assertEquals("C", cell13.getCellValueAsString());

		assertEquals(3, row2.getExcelCells().size());
		ExcelCell cell21 = row2.getExcelCellAt(0);
		ExcelCell cell22 = row2.getExcelCellAt(1);
		ExcelCell cell23 = row2.getExcelCellAt(2);
		assertEquals("D", cell21.getCellValueAsString());
		assertEquals("E", cell22.getCellValueAsString());
		assertEquals("F", cell23.getCellValueAsString());

		assertEquals(5, row3.getExcelCells().size());
		ExcelCell cell31 = row3.getExcelCellAt(0);
		ExcelCell cell32 = row3.getExcelCellAt(1);
		ExcelCell cell33 = row3.getExcelCellAt(2);
		ExcelCell cell34 = row3.getExcelCellAt(3);
		ExcelCell cell35 = row3.getExcelCellAt(4);
		assertEquals(1.0, cell31.getCellValue());
		assertEquals(2.0, cell32.getCellValue());
		assertEquals(3.0, cell33.getCellValue());
		assertEquals(4.0, cell34.getCellValue());
		assertEquals(5.0, cell35.getCellValue());

	}

	@Test
	@TestOrder(5)
	public void testWorkbook2Loading() {

		ExcelWorkbook workbook2 = getWorkbook("Workbook2.xlsx");
		System.out.println("Workbook2.xlsx:\n" + workbook2);

		assertEquals(1, workbook2.getExcelSheets().size());
		ExcelSheet sheet1 = workbook2.getExcelSheets().get(0);

		assertEquals(4, sheet1.getExcelRows().size());
		ExcelRow row1 = sheet1.getExcelRows().get(0);
		ExcelRow row2 = sheet1.getExcelRows().get(1);
		ExcelRow row3 = sheet1.getExcelRows().get(2);
		ExcelRow row4 = sheet1.getExcelRows().get(3);

		assertEquals(0, row1.getExcelCells().size());
		assertEquals(0, row2.getExcelCells().size());

		assertEquals(5, row3.getExcelCells().size());
		ExcelCell cell31 = row3.getExcelCellAt(0);
		ExcelCell cell32 = row3.getExcelCellAt(1);
		ExcelCell cell33 = row3.getExcelCellAt(2);
		ExcelCell cell34 = row3.getExcelCellAt(3);
		ExcelCell cell35 = row3.getExcelCellAt(4);

		System.out.println(cell31.getCellValue() + " | " + cell32.getCellValue() + " | " + cell33.getCellValue() + " | "
				+ cell34.getCellValue() + " | " + cell35.getCellValue());

		assertEquals(null, cell31.getCellValue());
		assertEquals("A", cell32.getCellValue());
		assertEquals(null, cell33.getCellValue());
		assertEquals("B", cell34.getCellValue());
		assertEquals("C", cell35.getCellValue());

		assertEquals(8, row4.getExcelCells().size());
		ExcelCell cell41 = row4.getExcelCellAt(0);
		ExcelCell cell42 = row4.getExcelCellAt(1);
		ExcelCell cell43 = row4.getExcelCellAt(2);
		ExcelCell cell44 = row4.getExcelCellAt(3);
		ExcelCell cell45 = row4.getExcelCellAt(4);
		ExcelCell cell46 = row4.getExcelCellAt(5);
		ExcelCell cell47 = row4.getExcelCellAt(6);
		ExcelCell cell48 = row4.getExcelCellAt(7);

		System.out.println(cell41.getCellValue() + " | " + cell42.getCellValue() + " | " + cell43.getCellValue() + " | "
				+ cell44.getCellValue() + " | " + cell45.getCellValue() + " | " + cell46.getCellValue() + " | " + cell47.getCellValue()
				+ " | " + cell48.getCellValue());

		assertEquals(null, cell41.getCellValue());
		assertEquals(null, cell42.getCellValue());
		assertEquals("D", cell43.getCellValue());
		assertEquals(null, cell44.getCellValue());
		assertEquals("E", cell45.getCellValue());
		assertEquals(null, cell46.getCellValue());
		assertEquals(null, cell47.getCellValue());
		assertEquals("F", cell48.getCellValue());

	}
}
