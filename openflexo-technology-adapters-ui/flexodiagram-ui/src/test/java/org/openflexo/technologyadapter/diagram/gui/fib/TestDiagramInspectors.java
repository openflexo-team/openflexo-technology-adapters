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
	public void testConnectorOverridingGraphicalRepresentationInspector() {
		validateFIB("Inspectors/Diagram/ConnectorOverridingGraphicalRepresentation.inspector");
	}

	@Test
	public void testConnectorRoleInspector() {
		validateFIB("Inspectors/Diagram/ConnectorRole.inspector");
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
	public void testDiagramPatternRoleInspector() {
		validateFIB("Inspectors/Diagram/DiagramPatternRole.inspector");
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
	public void testExampleDiagramInspector() {
		validateFIB("Inspectors/Diagram/ExampleDiagram.inspector");
	}

	@Test
	public void testExampleDiagramConnectorInspector() {
		validateFIB("Inspectors/Diagram/ExampleDiagramConnector.inspector");
	}

	@Test
	public void testExampleDiagramObjectInspector() {
		validateFIB("Inspectors/Diagram/ExampleDiagramObject.inspector");
	}

	@Test
	public void testExampleDiagramShapeInspector() {
		validateFIB("Inspectors/Diagram/ExampleDiagramShape.inspector");
	}

	@Test
	public void testGraphicalElementPatternRoleInspector() {
		validateFIB("Inspectors/Diagram/GraphicalElementPatternRole.inspector");
	}

	@Test
	public void testLinkSchemeInspector() {
		validateFIB("Inspectors/Diagram/LinkScheme.inspector");
	}

	@Test
	public void testShapeOverridingGraphicalRepresentationInspector() {
		validateFIB("Inspectors/Diagram/ShapeOverridingGraphicalRepresentation.inspector");
	}

	@Test
	public void testShapePatternRoleInspector() {
		validateFIB("Inspectors/Diagram/ShapePatternRole.inspector");
	}

}
