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
import static org.junit.Assert.assertSame;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.technologyadapter.excel.AbstractTestExcel;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * This unit test is intented to test basic workbook manipulation at model level
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestInsertRowModelLevel extends AbstractTestExcel {

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

	/**
	 * Insert a row in the middle of the data area, and perform some checks
	 * 
	 * @throws TypeMismatchException
	 * @throws NullReferenceException
	 * @throws InvocationTargetException
	 * @throws InvalidBindingException
	 */
	@Test
	@TestOrder(6)
	public void testInsertRow() throws TypeMismatchException, NullReferenceException, InvocationTargetException, InvalidBindingException {
		ExcelSheet sheet = personListingWB.getExcelSheetAtPosition(0);

		System.out.println("Found sheet: " + sheet);
		System.out.println("rows: " + sheet.getExcelRows().size());

		assertEquals(7, sheet.getExcelRows().size());

		for (int i = 0; i < sheet.getExcelRows().size(); i++) {
			ExcelRow row = sheet.getRowAt(i);
			assertSame(row, sheet.getExcelRows().get(i));
			assertEquals(5, row.getExcelCells().size());
			assertEquals(i, row.getRowIndex());
			StringBuffer sb = new StringBuffer();
			sb.append("[ROW-" + row.getRowIndex() + "]");
			for (int j = 0; j < row.getExcelCells().size(); j++) {
				ExcelCell cell = row.getExcelCellAt(j);
				assertSame(cell, row.getExcelCells().get(j));
				assertSame(cell, sheet.getCellAt(i, j));
				sb.append(" " + cell.getCellValue());
			}
			System.out.println(sb.toString());
		}

		assertEquals("Jean Dupont", sheet.getCellAt(1, 1).getCellValue());
		assertEquals("Bernardette Dupont", sheet.getCellAt(2, 1).getCellValue());
		assertEquals("Jules Dupont", sheet.getCellAt(3, 1).getCellValue());
		assertEquals("Nina Dupont", sheet.getCellAt(4, 1).getCellValue());
		assertEquals("Gérard Menvusat", sheet.getCellAt(5, 1).getCellValue());
		assertEquals("Alain Terrieur", sheet.getCellAt(6, 1).getCellValue());

		System.out.println("Now insert new row");

		ExcelRow newRow = sheet.insertRowAt(2);
		ExcelCell newCell1 = newRow.createCellAt(0);
		ExcelCell newCell2 = newRow.createCellAt(1);
		ExcelCell newCell3 = newRow.createCellAt(2);
		ExcelCell newCell4 = newRow.createCellAt(3);
		ExcelCell newCell5 = newRow.createCellAt(4);
		newCell1.setCellStringValue("MR");
		newCell2.setCellStringValue("John McLane");
		newCell4.setCellNumericValue(52);
		newCell5.setCellStringValue("New-york");

		assertEquals(8, sheet.getExcelRows().size());

		for (int i = 0; i < sheet.getExcelRows().size(); i++) {
			ExcelRow row = sheet.getRowAt(i);
			assertSame(row, sheet.getExcelRows().get(i));
			assertEquals(5, row.getExcelCells().size());
			assertEquals(i, row.getRowIndex());
			StringBuffer sb = new StringBuffer();
			sb.append("[ROW-" + row.getRowIndex() + "]");
			for (int j = 0; j < row.getExcelCells().size(); j++) {
				ExcelCell cell = row.getExcelCellAt(j);
				assertSame(cell, row.getExcelCells().get(j));
				assertSame(cell, sheet.getCellAt(i, j));
				sb.append(" " + cell.getCellValue());
			}
			System.out.println(sb.toString());
		}

		assertEquals("Jean Dupont", sheet.getCellAt(1, 1).getCellValue());
		assertEquals("John McLane", sheet.getCellAt(2, 1).getCellValue());
		assertEquals("Bernardette Dupont", sheet.getCellAt(3, 1).getCellValue());
		assertEquals("Jules Dupont", sheet.getCellAt(4, 1).getCellValue());
		assertEquals("Nina Dupont", sheet.getCellAt(5, 1).getCellValue());
		assertEquals("Gérard Menvusat", sheet.getCellAt(6, 1).getCellValue());
		assertEquals("Alain Terrieur", sheet.getCellAt(7, 1).getCellValue());

	}

	/**
	 * Insert a row at the end of the data area, and perform some checks
	 * 
	 * @throws TypeMismatchException
	 * @throws NullReferenceException
	 * @throws InvocationTargetException
	 * @throws InvalidBindingException
	 */
	@Test
	@TestOrder(7)
	public void testAppendRow() throws TypeMismatchException, NullReferenceException, InvocationTargetException, InvalidBindingException {
		ExcelSheet sheet = personListingWB.getExcelSheetAtPosition(0);

		System.out.println("Found sheet: " + sheet);
		System.out.println("rows: " + sheet.getExcelRows().size());

		System.out.println("Now append new row");

		ExcelRow newRow = sheet.createNewRow();
		ExcelCell newCell1 = newRow.createCellAt(0);
		ExcelCell newCell2 = newRow.createCellAt(1);
		ExcelCell newCell3 = newRow.createCellAt(2);
		ExcelCell newCell4 = newRow.createCellAt(3);
		ExcelCell newCell5 = newRow.createCellAt(4);
		newCell1.setCellStringValue("MS");
		newCell2.setCellStringValue("Mary Robinson");
		newCell4.setCellNumericValue(76);
		newCell5.setCellStringValue("Boston");

		assertEquals(9, sheet.getExcelRows().size());

		for (int i = 0; i < sheet.getExcelRows().size(); i++) {
			ExcelRow row = sheet.getRowAt(i);
			assertSame(row, sheet.getExcelRows().get(i));
			assertEquals(5, row.getExcelCells().size());
			assertEquals(i, row.getRowIndex());
			StringBuffer sb = new StringBuffer();
			sb.append("[ROW-" + row.getRowIndex() + "]");
			for (int j = 0; j < row.getExcelCells().size(); j++) {
				ExcelCell cell = row.getExcelCellAt(j);
				assertSame(cell, row.getExcelCells().get(j));
				assertSame(cell, sheet.getCellAt(i, j));
				sb.append(" " + cell.getCellValue());
			}
			System.out.println(sb.toString());
		}

		assertEquals("Jean Dupont", sheet.getCellAt(1, 1).getCellValue());
		assertEquals("John McLane", sheet.getCellAt(2, 1).getCellValue());
		assertEquals("Bernardette Dupont", sheet.getCellAt(3, 1).getCellValue());
		assertEquals("Jules Dupont", sheet.getCellAt(4, 1).getCellValue());
		assertEquals("Nina Dupont", sheet.getCellAt(5, 1).getCellValue());
		assertEquals("Gérard Menvusat", sheet.getCellAt(6, 1).getCellValue());
		assertEquals("Alain Terrieur", sheet.getCellAt(7, 1).getCellValue());
		assertEquals("Mary Robinson", sheet.getCellAt(8, 1).getCellValue());

	}

	/**
	 * Insert a row at the end of the data area, and perform some checks
	 * 
	 * @throws TypeMismatchException
	 * @throws NullReferenceException
	 * @throws InvocationTargetException
	 * @throws InvalidBindingException
	 */
	@Test
	@TestOrder(8)
	public void testInsertRowDoNotFollowOrder()
			throws TypeMismatchException, NullReferenceException, InvocationTargetException, InvalidBindingException {
		ExcelSheet sheet = personListingWB.getExcelSheetAtPosition(0);

		System.out.println("Found sheet: " + sheet);
		System.out.println("rows: " + sheet.getExcelRows().size());

		System.out.println("Now insert new row");

		ExcelRow newRow = sheet.insertRowAt(4);
		// Strange creation order
		ExcelCell newCell4 = newRow.createCellAt(3);
		ExcelCell newCell5 = newRow.createCellAt(4);
		ExcelCell newCell3 = newRow.createCellAt(2);
		ExcelCell newCell1 = newRow.createCellAt(0);
		ExcelCell newCell2 = newRow.createCellAt(1);
		newCell1.setCellStringValue("MS");
		newCell2.setCellStringValue("Patty Smith");
		newCell3.setCellStringValue("Singer");
		newCell4.setCellNumericValue(71);
		newCell5.setCellStringValue("Chicago");

		assertEquals(10, sheet.getExcelRows().size());

		for (int i = 0; i < sheet.getExcelRows().size(); i++) {
			ExcelRow row = sheet.getRowAt(i);
			assertSame(row, sheet.getExcelRows().get(i));
			assertEquals(5, row.getExcelCells().size());
			assertEquals(i, row.getRowIndex());
			StringBuffer sb = new StringBuffer();
			sb.append("[ROW-" + row.getRowIndex() + "]");
			for (int j = 0; j < row.getExcelCells().size(); j++) {
				ExcelCell cell = row.getExcelCellAt(j);
				assertSame(cell, row.getExcelCells().get(j));
				assertSame(cell, sheet.getCellAt(i, j));
				sb.append(" " + cell.getCellValue());
			}
			System.out.println(sb.toString());
		}

		assertEquals("Jean Dupont", sheet.getCellAt(1, 1).getCellValue());
		assertEquals("John McLane", sheet.getCellAt(2, 1).getCellValue());
		assertEquals("Bernardette Dupont", sheet.getCellAt(3, 1).getCellValue());
		assertEquals("Patty Smith", sheet.getCellAt(4, 1).getCellValue());
		assertEquals("Jules Dupont", sheet.getCellAt(5, 1).getCellValue());
		assertEquals("Nina Dupont", sheet.getCellAt(6, 1).getCellValue());
		assertEquals("Gérard Menvusat", sheet.getCellAt(7, 1).getCellValue());
		assertEquals("Alain Terrieur", sheet.getCellAt(8, 1).getCellValue());
		assertEquals("Mary Robinson", sheet.getCellAt(9, 1).getCellValue());

	}

}
