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

package org.openflexo.technologyadapter.diagram.gui.fib.dialog;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.fib.swing.FIBJPanel;
import org.openflexo.fib.testutils.GraphicalContextDelegate;
import org.openflexo.fib.utils.OpenflexoFIBTestCaseWithProjectAtRunTime;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.rm.Resource;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.fml.action.DeclareConnectorInFlexoConcept;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.CreateDiagram;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test CreateDiagramDialog fib
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDeclareConnectorInFlexoConceptDialog extends OpenflexoFIBTestCaseWithProjectAtRunTime {

	private static GraphicalContextDelegate gcDelegate;

	private static Resource fibResource;

	static FlexoEditor editor;

	@BeforeClass
	public static void setupClass() {
		instanciateTestServiceManager();
		initGUI();
	}

	@Test
	@TestOrder(1)
	public void testLoadWidget() {

		fibResource = DiagramCst.DECLARE_CONNECTOR_IN_FLEXO_CONCEPT_DIALOG_FIB;
		assertTrue(fibResource != null);
	}

	@Test
	@TestOrder(2)
	public void testValidateWidget() throws InterruptedException {

		validateFIB(fibResource);
	}

	private static DiagramConnector connector;

	@Test
	@TestOrder(3)
	public void createProjectAndDiagram() {

		editor = createProject("TestDeclareConnectorInFlexoConceptDialog");

		CreateDiagram createDiagram = CreateDiagram.actionType.makeNewAction(editor.getProject().getRootFolder(), null, editor);
		createDiagram.setDiagramName("TestNewDiagram");
		createDiagram.setDiagramTitle("A nice title for a new diagram");
		createDiagram.doAction();
		assertTrue(createDiagram.hasActionExecutionSucceeded());

		Diagram diagram = createDiagram.getNewDiagram();
		assertNotNull(diagram);

		DiagramFactory factory = ((DiagramResource) diagram.getResource()).getFactory();

		DiagramShape shape1 = factory.makeNewShape("Shape1", ShapeType.RECTANGLE, new FGEPoint(100, 100), diagram);
		shape1.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.RED));
		shape1.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.BLUE));
		assertNotNull(shape1);
		DiagramShape shape2 = factory.makeNewShape("Shape2", ShapeType.RECTANGLE, new FGEPoint(200, 100), diagram);
		shape2.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.BLUE));
		shape2.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.WHITE));
		assertNotNull(shape2);
		connector = factory.makeNewConnector("Connector", shape1, shape2, diagram);
		assertNotNull(connector);

	}

	@Test
	@TestOrder(4)
	public void testInstanciateDialog() {

		FlexoProject project = editor.getProject();
		DeclareConnectorInFlexoConcept declareConnectorAction = DeclareConnectorInFlexoConcept.actionType.makeNewAction(connector, null,
				editor);
		FIBJPanel<DeclareConnectorInFlexoConcept> widget = instanciateFIB(fibResource, declareConnectorAction,
				DeclareConnectorInFlexoConcept.class);

		gcDelegate.addTab("DeclareConnectorInFlexoConcept", widget.getController());
	}

	public static void initGUI() {
		gcDelegate = new GraphicalContextDelegate(TestDeclareConnectorInFlexoConceptDialog.class.getSimpleName());
	}

	@AfterClass
	public static void waitGUI() {
		gcDelegate.waitGUI();
	}

	@Before
	public void setUp() {
		gcDelegate.setUp();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		gcDelegate.tearDown();

		super.tearDown();

		if (editor != null) {
			File PRJDirectory = editor.getProject().getDirectory();
			PRJDirectory.deleteOnExit();
		}

	}

}
