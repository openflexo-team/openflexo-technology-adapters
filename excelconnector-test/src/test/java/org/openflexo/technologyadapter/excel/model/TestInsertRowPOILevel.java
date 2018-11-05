/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexo-foundation, a component of the software infrastructure 
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

import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.technologyadapter.excel.AbstractTestExcel;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * This unit test is intented to test basic workbook manipulation at POI level
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestInsertRowPOILevel extends AbstractTestExcel {

	private static ExcelWorkbookResource personListingResource;
	private static ExcelWorkbook personListingWB;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(ExcelTechnologyAdapter.class);
	}

	@Test
	@TestOrder(2)
	public void loadWorkbook() throws Exception {
		log("loadWorkbook");
		personListingResource = getExcelResource("PersonListing.xlsx");
		System.out.println("Found resource " + personListingResource.getURI());
		personListingWB = personListingResource.getExcelWorkbook();
	}

	@Test
	@TestOrder(6)
	public void testInsertRow() throws IOException {
		try (Workbook wb = personListingWB.getWorkbook()) {
			Sheet sheet = wb.getSheetAt(0);

			System.out.println("Found sheet: " + sheet);
			System.out.println("first/last: " + sheet.getFirstRowNum() + " / " + sheet.getLastRowNum());

			assertEquals(0, sheet.getFirstRowNum());
			assertEquals(6, sheet.getLastRowNum());

			for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				StringBuffer sb = new StringBuffer();
				sb.append("[ROW-" + row.getRowNum() + "]");
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					Cell cell = row.getCell(j);
					sb.append(" " + cell);
				}
				System.out.println(sb.toString());
			}

			sheet.shiftRows(2, 6, 1);
			Row newRow = sheet.createRow(2);

			Cell newCell1 = newRow.createCell(0);
			Cell newCell2 = newRow.createCell(1);
			Cell newCell3 = newRow.createCell(2);
			Cell newCell4 = newRow.createCell(3);

			assertEquals(0, sheet.getFirstRowNum());
			assertEquals(7, sheet.getLastRowNum());

			System.out.println("first/last: " + sheet.getFirstRowNum() + " / " + sheet.getLastRowNum());

			for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				StringBuffer sb = new StringBuffer();
				sb.append("[ROW-" + row.getRowNum() + "]");
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					Cell cell = row.getCell(j);
					sb.append(" " + cell);
				}
				System.out.println(sb.toString());
			}
		}
	}
}
