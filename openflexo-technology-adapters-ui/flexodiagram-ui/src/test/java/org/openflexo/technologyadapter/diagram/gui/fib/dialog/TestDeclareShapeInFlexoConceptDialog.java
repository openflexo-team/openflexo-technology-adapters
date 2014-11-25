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
import org.openflexo.technologyadapter.diagram.fml.action.DeclareShapeInFlexoConcept;
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
public class TestDeclareShapeInFlexoConceptDialog extends OpenflexoFIBTestCaseWithProjectAtRunTime {

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

		fibResource = DiagramCst.DECLARE_SHAPE_IN_FLEXO_CONCEPT_DIALOG_FIB;
		assertTrue(fibResource != null);
	}

	@Test
	@TestOrder(2)
	public void testValidateWidget() throws InterruptedException {

		validateFIB(fibResource);
	}

	private static DiagramShape shape1, shape2;

	@Test
	@TestOrder(3)
	public void createProjectAndDiagram() {

		editor = createProject("TestDeclareShapeInFlexoConceptDialog");

		CreateDiagram createDiagram = CreateDiagram.actionType.makeNewAction(editor.getProject().getRootFolder(), null, editor);
		createDiagram.setDiagramName("TestNewDiagram");
		createDiagram.setDiagramTitle("A nice title for a new diagram");
		createDiagram.doAction();
		assertTrue(createDiagram.hasActionExecutionSucceeded());

		Diagram diagram = createDiagram.getNewDiagram();
		assertNotNull(diagram);

		DiagramFactory factory = ((DiagramResource) diagram.getResource()).getFactory();

		shape1 = factory.makeNewShape("Shape1", ShapeType.RECTANGLE, new FGEPoint(100, 100), diagram);
		shape1.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.RED));
		shape1.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.BLUE));
		assertNotNull(shape1);
		shape2 = factory.makeNewShape("Shape2", ShapeType.RECTANGLE, new FGEPoint(200, 100), diagram);
		shape2.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.BLUE));
		shape2.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.WHITE));
		assertNotNull(shape2);
		DiagramConnector connector = factory.makeNewConnector("Connector", shape1, shape2, diagram);
		assertNotNull(connector);

	}

	@Test
	@TestOrder(4)
	public void testInstanciateDialog() {

		FlexoProject project = editor.getProject();
		DeclareShapeInFlexoConcept declareShapeAction = DeclareShapeInFlexoConcept.actionType.makeNewAction(shape1, null, editor);
		FIBJPanel<DeclareShapeInFlexoConcept> widget = instanciateFIB(fibResource, declareShapeAction, DeclareShapeInFlexoConcept.class);

		gcDelegate.addTab("DeclareShapeInFlexoConcept", widget.getController());
	}

	public static void initGUI() {
		gcDelegate = new GraphicalContextDelegate(TestDeclareShapeInFlexoConceptDialog.class.getSimpleName());
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
