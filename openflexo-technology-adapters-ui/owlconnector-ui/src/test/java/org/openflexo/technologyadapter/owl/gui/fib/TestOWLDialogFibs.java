package org.openflexo.technologyadapter.owl.gui.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestOWLDialogFibs extends GenericFIBTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */
	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib/Dialog")).getFile(),
				"Fib/Dialog/"));
	}

	@Test
	public void testCreateDataPropertyDialog() {
		validateFIB("Fib/Dialog/CreateDataPropertyDialog.fib");
	}

	@Test
	public void testCreateObjectPropertyDialog() {
		validateFIB("Fib/Dialog/CreateObjectPropertyDialog.fib");
	}

	@Test
	public void testCreateOntologyClassDialog() {
		validateFIB("Fib/Dialog/CreateOntologyClassDialog.fib");
	}

	@Test
	public void testCreateOntologyIndividualDialog() {
		validateFIB("Fib/Dialog/CreateOntologyIndividualDialog.fib");
	}

	@Test
	public void testDeleteOntologyObjectsDialog() {
		validateFIB("Fib/Dialog/DeleteOntologyObjectsDialog.fib");
	}

}
