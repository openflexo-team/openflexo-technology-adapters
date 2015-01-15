package org.openflexo.technologyadapter.emf.gui.fib;

import org.openflexo.fib.utils.GenericFIBInspectorTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestEMFInspectors extends GenericFIBInspectorTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */

	public static void main(String[] args) {
		System.out.println(generateInspectorTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Inspectors/EMF")).getFile(),
				"Inspectors/EMF/"));
	}

	public void testEMFMetaModelInspector() {
		validateFIB("Inspectors/EMF/EMFMetaModel.inspector");
	}

	public void testEMFMetaModelResourceInspector() {
		validateFIB("Inspectors/EMF/EMFMetaModelResource.inspector");
	}

	public void testEMFModelInspector() {
		validateFIB("Inspectors/EMF/EMFModel.inspector");
	}

	public void testEMFModelResourceInspector() {
		validateFIB("Inspectors/EMF/EMFModelResource.inspector");
	}

}
