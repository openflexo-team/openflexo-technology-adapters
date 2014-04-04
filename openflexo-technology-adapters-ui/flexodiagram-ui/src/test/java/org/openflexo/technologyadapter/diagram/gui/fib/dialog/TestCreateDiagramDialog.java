package org.openflexo.technologyadapter.diagram.gui.fib.dialog;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fib.testutils.GraphicalContextDelegate;
import org.openflexo.fib.utils.OpenflexoFIBTestCaseWithProjectAtRunTime;
import org.openflexo.fib.view.widget.DefaultFIBCustomComponent;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.rm.Resource;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.model.action.CreateDiagram;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test CreateDiagramDialog fib
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestCreateDiagramDialog extends OpenflexoFIBTestCaseWithProjectAtRunTime {

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

		fibResource = DiagramCst.CREATE_DIAGRAM_DIALOG_FIB;
		assertTrue(fibResource != null);
	}

	@Test
	@TestOrder(2)
	public void testValidateWidget() {

		validateFIB(fibResource);
	}

	@Test
	@TestOrder(3)
	public void createProject() {

		editor = createProject("TestCreateDiagramDialog");

	}

	@Test
	@TestOrder(4)
	public void testInstanciateDialog() {

		FlexoProject project = editor.getProject();
		CreateDiagram createDiagram = CreateDiagram.actionType.makeNewAction(project.getRootFolder(), null, editor);
		DefaultFIBCustomComponent<CreateDiagram> widget = instanciateFIB(fibResource, createDiagram, CreateDiagram.class);

		gcDelegate.addTab("CreateDiagram", widget.getController());
	}

	public static void initGUI() {
		gcDelegate = new GraphicalContextDelegate(TestCreateDiagramDialog.class.getSimpleName());
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
	}

}
