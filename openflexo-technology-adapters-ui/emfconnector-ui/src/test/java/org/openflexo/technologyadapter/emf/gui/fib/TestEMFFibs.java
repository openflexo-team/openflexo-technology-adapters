package org.openflexo.technologyadapter.emf.gui.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestEMFFibs extends GenericFIBTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */
	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib")).getFile(), "Fib/"));
	}

	@Test
	public void testAddEMFObjectIndividual() {
		validateFIB("Fib/AddEMFObjectIndividual.fib");
	}

	@Test
	public void testAddEMFObjectIndividualAttributeDataPropertyValuePanel() {
		validateFIB("Fib/AddEMFObjectIndividualAttributeDataPropertyValuePanel.fib");
	}

	@Test
	public void testAddEMFObjectIndividualAttributeObjectPropertyValuePanel() {
		validateFIB("Fib/AddEMFObjectIndividualAttributeObjectPropertyValuePanel.fib");
	}

}
