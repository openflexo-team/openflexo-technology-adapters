/**
 * 
 * Copyright (c) 2014, Openflexo
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
	public void testDiagramInspector() {
		validateFIB("Inspectors/Diagram/Diagram.inspector");
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
	public void testAddConnectorInspector() {
		validateFIB("Inspectors/Diagram/EditionAction/AddConnector.inspector");
	}

	@Test
	public void testAddDiagramInspector() {
		validateFIB("Inspectors/Diagram/EditionAction/AddDiagram.inspector");
	}

	@Test
	public void testAddShapeInspector() {
		validateFIB("Inspectors/Diagram/EditionAction/AddShape.inspector");
	}

	@Test
	public void testGraphicalActionInspector() {
		validateFIB("Inspectors/Diagram/EditionAction/GraphicalAction.inspector");
	}

	@Test
	public void testDropSchemeInspector() {
		validateFIB("Inspectors/Diagram/FlexoBehaviour/DropScheme.inspector");
	}

	@Test
	public void testLinkSchemeInspector() {
		validateFIB("Inspectors/Diagram/FlexoBehaviour/LinkScheme.inspector");
	}

	@Test
	public void testConnectorRoleInspector() {
		validateFIB("Inspectors/Diagram/FlexoRole/ConnectorRole.inspector");
	}

	@Test
	public void testDiagramRoleInspector() {
		validateFIB("Inspectors/Diagram/FlexoRole/DiagramRole.inspector");
	}

	@Test
	public void testGraphicalElementRoleInspector() {
		validateFIB("Inspectors/Diagram/FlexoRole/GraphicalElementRole.inspector");
	}

	@Test
	public void testShapeRoleInspector() {
		validateFIB("Inspectors/Diagram/FlexoRole/ShapeRole.inspector");
	}

	@Test
	public void testDiagramModelSlotInspector() {
		validateFIB("Inspectors/Diagram/ModelSlot/DiagramModelSlot.inspector");
	}

	@Test
	public void testTypedDiagramModelSlotInspector() {
		validateFIB("Inspectors/Diagram/ModelSlot/TypedDiagramModelSlot.inspector");
	}

	@Test
	public void testDiagramPaletteInspector() {
		validateFIB("Inspectors/Diagram/Palette/DiagramPalette.inspector");
	}

	@Test
	public void testDiagramPaletteElementInspector() {
		validateFIB("Inspectors/Diagram/Palette/DiagramPaletteElement.inspector");
	}

	@Test
	public void testFMLDiagramPaletteElementBindingInspector() {
		validateFIB("Inspectors/Diagram/Palette/FMLDiagramPaletteElementBinding.inspector");
	}

}
