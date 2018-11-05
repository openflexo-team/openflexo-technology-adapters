/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.view;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.technologyadapter.excel.AbstractTestExcelUI;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelCellRange;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.widget.FIBCellRangeSelector;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the structural and behavioural features of FIBDocXFragmentSelector
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestFIBCellRangeSelector extends AbstractTestExcelUI {

	private static FIBCellRangeSelector selector;

	@Test
	@TestOrder(1)
	public void testSelectorWithNoRange() {

		ExcelWorkbook workbook = getWorkbook("PersonListing.xlsx");
		assertNotNull(workbook);

		selector = new FIBCellRangeSelector(null);
		selector.setServiceManager(serviceManager);
		selector.setExcelWorkbook(workbook);
		selector.getCustomPanel();

		assertNotNull(selector.getSelectorPanel().getController());

		gcDelegate.addTab("PersonListing.xlsx-1", selector.getSelectorPanel().getController());
	}

	@Test
	@TestOrder(2)
	public void testSelectorWithRangeOnSheet1() {

		ExcelWorkbook workbook = getWorkbook("PersonListing.xlsx");
		assertNotNull(workbook);

		ExcelSheet sheet1 = workbook.getExcelSheetAtPosition(0);
		ExcelCell b2 = sheet1.getCellAt(1, 1);
		ExcelCell d7 = sheet1.getCellAt(6, 3);
		ExcelCellRange range = workbook.getFactory().makeExcelCellRange(b2, d7);

		selector = new FIBCellRangeSelector(range);
		selector.setServiceManager(serviceManager);
		selector.setExcelWorkbook(workbook);
		selector.getCustomPanel();

		assertNotNull(selector.getSelectorPanel().getController());

		gcDelegate.addTab("PersonListing.xlsx-2", selector.getSelectorPanel().getController());
	}

	@Test
	@TestOrder(3)
	public void testSelectorWithRangeOnSheet2() {

		ExcelWorkbook workbook = getWorkbook("PersonListing.xlsx");
		assertNotNull(workbook);

		ExcelSheet sheet2 = workbook.getExcelSheetAtPosition(1);
		ExcelCell b2 = sheet2.getCellAt(1, 1);
		ExcelCell e2 = sheet2.getCellAt(1, 3);
		ExcelCellRange range = workbook.getFactory().makeExcelCellRange(b2, e2);

		selector = new FIBCellRangeSelector(range);
		selector.setServiceManager(serviceManager);
		selector.setExcelWorkbook(workbook);
		selector.getCustomPanel();

		assertNotNull(selector.getSelectorPanel().getController());

		gcDelegate.addTab("PersonListing.xlsx-3", selector.getSelectorPanel().getController());
	}

}
