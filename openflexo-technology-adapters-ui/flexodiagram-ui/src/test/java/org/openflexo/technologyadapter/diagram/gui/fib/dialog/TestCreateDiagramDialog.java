package org.openflexo.technologyadapter.diagram.gui.fib.dialog;

import static org.junit.Assert.assertTrue;

import java.io.File;

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
import org.openflexo.technologyadapter.diagram.model.action.CreateDiagram;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.toolbox.ResourceLocator;

/**
 * Test StandardFlexoConceptView fib
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestCreateDiagramDialog extends OpenflexoFIBTestCaseWithProjectAtRunTime {

	private static GraphicalContextDelegate gcDelegate;

	private static File fibFile;

	static FlexoEditor editor;

	@BeforeClass
	public static void setupClass() {
		instanciateTestServiceManager();
		initGUI();
	}

	@Test
	@TestOrder(1)
	public void testLoadWidget() {

		fibFile = ResourceLocator.locateFile("Fib/Dialog/CreateDiagramDialog.fib");
		assertTrue(fibFile.exists());
	}

	@Test
	@TestOrder(2)
	public void testValidateWidget() {

		validateFIB(fibFile);
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
		DefaultFIBCustomComponent<CreateDiagram> widget = instanciateFIB(fibFile, createDiagram, CreateDiagram.class);

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
