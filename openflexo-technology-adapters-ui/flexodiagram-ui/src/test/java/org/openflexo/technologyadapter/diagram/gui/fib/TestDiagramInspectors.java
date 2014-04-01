package org.openflexo.technologyadapter.diagram.gui.fib;

import org.junit.Test;
import org.openflexo.fib.utils.GenericFIBInspectorTestCase;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.ResourceLocator;

public class TestDiagramInspectors extends GenericFIBInspectorTestCase {

	/*
	 * Use this method to print all
	 * Then copy-paste 
	 */

	public static void main(String[] args) {
		System.out.println(generateInspectorTestCaseClass(
				((FileResourceImpl) ResourceLocator.locateResource("Inspectors/Diagram")).getFile(), "Inspectors/Diagram/"));
	}

	@Test
	public void testConnectorRoleInspector() {
		validateFIB("Inspectors/Diagram/ConnectorRole.inspector");
	}

	@Test
	public void testDiagramInspector() {
		validateFIB("Inspectors/Diagram/Diagram.inspector");
	}

	@Test
	public void testDiagramModelSlotInspector() {
		validateFIB("Inspectors/Diagram/DiagramModelSlot.inspector");
	}

	@Test
	public void testDiagramPaletteInspector() {
		validateFIB("Inspectors/Diagram/DiagramPalette.inspector");
	}

	@Test
	public void testDiagramPaletteElementInspector() {
		validateFIB("Inspectors/Diagram/DiagramPaletteElement.inspector");
	}

	@Test
	public void testDiagramRoleInspector() {
		validateFIB("Inspectors/Diagram/DiagramRole.inspector");
	}

	@Test
	public void testDiagramSpecificationInspector() {
		validateFIB("Inspectors/Diagram/DiagramSpecification.inspector");
	}

	@Test
	public void testDiagramSpecificationResourceInspector() {
		validateFIB("Inspectors/Diagram/DiagramSpecificationResource.inspector");
	}

	@Test
	public void testDropSchemeInspector() {
		validateFIB("Inspectors/Diagram/DropScheme.inspector");
	}

	@Test
	public void testFMLDiagramPaletteElementBindingInspector() {
		validateFIB("Inspectors/Diagram/FMLDiagramPaletteElementBinding.inspector");
	}

	@Test
	public void testGraphicalElementRoleInspector() {
		validateFIB("Inspectors/Diagram/GraphicalElementRole.inspector");
	}

	@Test
	public void testLinkSchemeInspector() {
		validateFIB("Inspectors/Diagram/LinkScheme.inspector");
	}

	@Test
	public void testShapeRoleInspector() {
		validateFIB("Inspectors/Diagram/ShapeRole.inspector");
	}

}
