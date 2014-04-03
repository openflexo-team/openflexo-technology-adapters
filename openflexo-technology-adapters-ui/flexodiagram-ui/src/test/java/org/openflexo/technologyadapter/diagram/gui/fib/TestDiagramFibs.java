package org.openflexo.technologyadapter.diagram.gui.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestDiagramFibs extends GenericFIBTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */
	public static void main(String[] args) {
		System.out.println(generateFIBTestCaseClass(((FileResourceImpl) ResourceLocator.locateResource("Fib")).getFile(), "Fib/"));
	}

	@Test
	public void testAddConnectorPanel() {
		validateFIB("Fib/AddConnectorPanel.fib");
	}

	@Test
	public void testAddDiagramPanel() {
		validateFIB("Fib/AddDiagramPanel.fib");
	}

	@Test
	public void testAddShapePanel() {
		validateFIB("Fib/AddShapePanel.fib");
	}

	@Test
	public void testDiagramFlexoConceptView() {
		validateFIB("Fib/DiagramFlexoConceptView.fib");
	}

	@Test
	public void testDiagramSpecificationView() {
		validateFIB("Fib/DiagramSpecificationView.fib");
	}

	@Test
	public void testDropSchemePanel() {
		validateFIB("Fib/DropSchemePanel.fib");
	}

	@Test
	public void testGraphicalActionPanel() {
		validateFIB("Fib/GraphicalActionPanel.fib");
	}

	@Test
	public void testLinkSchemePanel() {
		validateFIB("Fib/LinkSchemePanel.fib");
	}

}
