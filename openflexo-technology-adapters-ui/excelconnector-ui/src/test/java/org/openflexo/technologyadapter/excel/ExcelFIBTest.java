package org.openflexo.technologyadapter.excel;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class ExcelFIBTest extends GenericFIBTestCase {

	public static void main(final String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib")).getFile(), "Fib/"));
	}

	@Test
	public void testExcelSheetPanel() {
		this.validateFIB("Fib/ExcelSheetPanel.fib");
	}

	@Test
	public void testSelectExcelSheetPanel() {
		this.validateFIB("Fib/SelectExcelSheetPanel.fib");
	}

	@Test
	public void testCellStyleActionPanel() {
		this.validateFIB("Fib/CellStyleActionPanel.fib");
	}

	@Test
	public void testAddExcelCellPanel() {
		this.validateFIB("Fib/AddExcelCellPanel.fib");
	}

	@Test
	public void testSelectExcelRowPanel() {
		this.validateFIB("Fib/SelectExcelRowPanel.fib");
	}

	@Test
	public void testSelectExcelCellPanel() {
		this.validateFIB("Fib/SelectExcelCellPanel.fib");
	}

	@Test
	public void testAddExcelSheetPanel() {
		this.validateFIB("Fib/AddExcelSheetPanel.fib");
	}

	@Test
	public void testAddExcelRowPanel() {
		this.validateFIB("Fib/AddExcelRowPanel.fib");
	}

}
