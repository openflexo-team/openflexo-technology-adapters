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
import static org.junit.Assert.assertSame;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.excel.AbstractTestExcel;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestExcelCellRange extends AbstractTestExcel {
	protected static final Logger logger = Logger.getLogger(TestExcelCellRange.class.getPackage().getName());

	private static ExcelWorkbookResource workbook1Resource;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(ExcelTechnologyAdapter.class);
	}

	@Test
	@TestOrder(2)
	public void testLoadExcelDocument() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		workbook1Resource = getExcelResource("Workbook1.xlsx");
		ExcelWorkbook workbook1 = workbook1Resource.loadResourceData(null);
		System.out.println("Workbook1.xlsx:\n" + workbook1);

		assertEquals(1, workbook1.getExcelSheets().size());
	}

	@Test
	@TestOrder(3)
	public void testExcelCellConverter() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		BasicExcelModelConverter converter = workbook1Resource.getConverter();
		ExcelSheet sheet1 = workbook1Resource.getExcelWorkbook().getExcelSheets().get(0);

		ExcelCell A1 = sheet1.getCellAt(0, 0);
		assertEquals("A1", A1.getIdentifier());
		assertEquals("Feuil1/cell[A1]", converter.toSerializationIdentifier(A1));

		ExcelCell J8 = sheet1.getCellAt(7, 9);
		assertEquals("J8", J8.getIdentifier());
		assertEquals("Feuil1/cell[J8]", converter.toSerializationIdentifier(J8));
		String J8Id = converter.toSerializationIdentifier(J8);

		ExcelCell J8bis = (ExcelCell) converter.fromSerializationIdentifier(J8Id);
		assertSame(J8, J8bis);

		ExcelCell AA29 = sheet1.getCellAt(28, 26);
		assertEquals("AA29", AA29.getIdentifier());
		assertEquals("Feuil1/cell[AA29]", converter.toSerializationIdentifier(AA29));
		String AA29Id = converter.toSerializationIdentifier(AA29);

		ExcelRow row = sheet1.getRowAt(28);
		ExcelCell AA29bis = row.getExcelCellAt(26);
		assertSame(AA29, AA29bis);

		assertSame(AA29, converter.fromSerializationIdentifier(AA29Id));

	}

	@Test
	@TestOrder(4)
	public void testExcelCellRangeConverter() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		ExcelSheet sheet1 = workbook1Resource.getExcelWorkbook().getExcelSheets().get(0);

		ExcelCell A2 = sheet1.getCellAt(1, 0);
		ExcelCell E7 = sheet1.getCellAt(6, 4);
		ExcelCellRange cellRange = workbook1Resource.getFactory().makeExcelCellRange(A2, E7);

		ExcelCellRangeConverter converter = new ExcelCellRangeConverter(getFlexoServiceManager());
		System.out.println("cellRange=" + converter.convertToString(cellRange));
		assertEquals("http://www.openflexo.org/test/excel/Excel/Workbook1.xlsx$Feuil1/range[A2:E7]", converter.convertToString(cellRange));

		ExcelCellRange cellRange2 = converter
				.convertFromString("http://www.openflexo.org/test/excel/Excel/Workbook1.xlsx$Feuil1/range[A2:E7]", null);

		System.out.println("cellRange2=" + cellRange2);

		assertEquals(cellRange, cellRange2);

	}

}
