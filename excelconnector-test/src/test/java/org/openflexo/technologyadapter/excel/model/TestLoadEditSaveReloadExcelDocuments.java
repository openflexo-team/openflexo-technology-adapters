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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.excel.AbstractTestExcel;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.toolbox.FileUtils;

@RunWith(OrderedRunner.class)
// TODO: run this test locally
@Ignore
public class TestLoadEditSaveReloadExcelDocuments extends AbstractTestExcel {
	protected static final Logger logger = Logger.getLogger(TestLoadEditSaveReloadExcelDocuments.class.getPackage().getName());

	private static ExcelWorkbookResource workbook1Resource;

	private static DirectoryResourceCenter directoryRC;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(ExcelTechnologyAdapter.class);

		directoryRC = makeNewDirectoryResourceCenter();

		ExcelWorkbookResource initialResource = workbook1Resource = getExcelResource("Workbook1.xlsx");

		System.out.println("Was: " + initialResource);

		try {
			FileUtils.copyResourceToDir(initialResource.getIODelegate().getSerializationArtefactAsResource().getContainer(),
					directoryRC.getRootDirectory());
		} catch (IOException e) {
			e.printStackTrace();
		}
		directoryRC.setDefaultBaseURI("CopiedResourceCenter");
		directoryRC.performDirectoryWatchingNow();

		System.out.println("Created DirectoryRC: " + directoryRC.getRootDirectory());
	}

	@Test
	@TestOrder(4)
	public void testWorkbook1Loading() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		workbook1Resource = getExcelResource("Workbook1.xlsx", directoryRC);
		ExcelWorkbook workbook1 = workbook1Resource.loadResourceData();
		System.out.println("Workbook1.xlsx:\n" + workbook1);

		System.out.println("Now: " + workbook1Resource);

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
	public void testWorkbook1Saving() throws FlexoException {

		ExcelWorkbook workbook1 = workbook1Resource.getExcelWorkbook();

		assertEquals(1, workbook1.getExcelSheets().size());
		ExcelSheet sheet1 = workbook1.getExcelSheets().get(0);
		assertEquals(3, sheet1.getExcelRows().size());
		ExcelRow row1 = sheet1.getExcelRows().get(0);
		ExcelCell cell13 = row1.getExcelCellAt(2);
		cell13.setCellStringValue("D");
		System.out.println("Saving to " + workbook1Resource.getIODelegate().getSerializationArtefact());
		if (workbook1Resource.getIODelegate().getSerializationArtefact() instanceof File) {
			workbook1Resource.save();
		}
	}

	@Test
	@TestOrder(6)
	public void testWorkbook1Reloading() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		if (!(workbook1Resource.getIODelegate().getSerializationArtefact() instanceof File)) {
			return;
		}

		System.out.println("Unload " + workbook1Resource.getIODelegate().getSerializationArtefact());
		workbook1Resource.unloadResourceData(false);

		System.out.println("Reload " + workbook1Resource.getIODelegate().getSerializationArtefact());
		ExcelWorkbook workbook1 = workbook1Resource.loadResourceData();

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
		assertEquals("D", cell13.getCellValueAsString());

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

		// Put it back to C, otherwise it might fail in future tests
		cell13.setCellStringValue("C");
		System.out.println("Saving to " + workbook1Resource.getIODelegate().getSerializationArtefact());
		workbook1Resource.save();

	}

}
