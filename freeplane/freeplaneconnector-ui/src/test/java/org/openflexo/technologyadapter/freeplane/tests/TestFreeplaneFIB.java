package org.openflexo.technologyadapter.freeplane.tests;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestFreeplaneFIB extends GenericFIBTestCase {

	/**
	 * Print tent class in console, copy paste to have the full class. Maybe use
	 * parameterized runner instead.
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib")).getFile(), "Fib/"));
	}

	@Test
	public void testAddChildNodeFIB() {
		this.validateFIB("Fib/AddChildNodePanel.fib");
	}
}
