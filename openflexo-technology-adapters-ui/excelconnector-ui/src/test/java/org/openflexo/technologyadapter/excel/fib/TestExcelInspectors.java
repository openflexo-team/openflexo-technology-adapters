/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.technologyadapter.excel.fib;

import org.junit.Test;
import org.openflexo.gina.test.GenericFIBInspectorTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestExcelInspectors extends GenericFIBInspectorTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */

	public static void main(String[] args) {
		System.out.println(generateInspectorTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Inspectors/Excel")).getFile(),
				"Inspectors/Excel/"));
	}

	@Test
	public void testBasicExcelModelSlotInspector() {
		validateFIB("Inspectors/Excel/BasicExcelModelSlot.inspector");
	}

	@Test
	public void testAddExcelCellInspector() {
		validateFIB("Inspectors/Excel/EditionAction/AddExcelCell.inspector");
	}

	@Test
	public void testAddExcelRowInspector() {
		validateFIB("Inspectors/Excel/EditionAction/AddExcelRow.inspector");
	}

	@Test
	public void testAddExcelSheetInspector() {
		validateFIB("Inspectors/Excel/EditionAction/AddExcelSheet.inspector");
	}

	@Test
	public void testCellStyleActionInspector() {
		validateFIB("Inspectors/Excel/EditionAction/CellStyleAction.inspector");
	}

	@Test
	public void testCreateExcelResourceInspector() {
		validateFIB("Inspectors/Excel/EditionAction/CreateExcelResource.inspector");
	}

	@Test
	public void testSelectExcelCellInspector() {
		validateFIB("Inspectors/Excel/EditionAction/SelectExcelCell.inspector");
	}

	@Test
	public void testSelectExcelRowInspector() {
		validateFIB("Inspectors/Excel/EditionAction/SelectExcelRow.inspector");
	}

	@Test
	public void testSelectExcelSheetInspector() {
		validateFIB("Inspectors/Excel/EditionAction/SelectExcelSheet.inspector");
	}

	@Test
	public void testExcelCellInspector() {
		validateFIB("Inspectors/Excel/ExcelCell.inspector");
	}

	@Test
	public void testExcelSheetInspector() {
		validateFIB("Inspectors/Excel/ExcelSheet.inspector");
	}

	@Test
	public void testExcelWorkbookInspector() {
		validateFIB("Inspectors/Excel/ExcelWorkbook.inspector");
	}

}
