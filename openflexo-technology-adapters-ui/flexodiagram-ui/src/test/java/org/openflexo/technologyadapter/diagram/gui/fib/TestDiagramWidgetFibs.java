package org.openflexo.technologyadapter.diagram.gui.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestDiagramWidgetFibs extends GenericFIBTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */
	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib/Widget")).getFile(),
				"Fib/Widget/"));
	}

	@Test
	public void testFIBDiagramBrowser() {
		validateFIB("Fib/Widget/FIBDiagramBrowser.fib");
	}

	@Test
	public void testFIBDiagramPaletteBrowser() {
		validateFIB("Fib/Widget/FIBDiagramPaletteBrowser.fib");
	}

}
