package org.openflexo.technologyadapter.freeplane.tests;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

import org.fest.swing.fixture.FrameFixture;
import org.freeplane.features.map.MapModel;
import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.junit.*;
import org.openflexo.ApplicationContext;
import org.openflexo.OpenflexoTestCaseWithGUI;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.module.FlexoModule;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.controller.FreeplaneAdapterController;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.impl.FreeplaneMapImpl;
import org.openflexo.technologyadapter.freeplane.view.AbstractFreeplaneModuleView;
import org.openflexo.technologyadapter.freeplane.view.FreeplaneModuleView;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.vpm.VPMModule;

/**
 * Created by eloubout on 01/09/14.
 */
public class TestFreePlaneModuleView extends OpenflexoTestCaseWithGUI {

	private static final Logger LOGGER = Logger.getLogger(TestFreePlaneModuleView.class.getPackage().getName());

	private static FreeplaneTechnologyAdapter fpTA;

	private static FlexoServiceManager applicationContext;

	private ModelFactory factory;
	private FlexoController controller;
	private FlexoPerspective perspective;

	@BeforeClass
	public static void sotupBeforeClass() {
		applicationContext = instanciateTestServiceManager();
		fpTA = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(FreeplaneTechnologyAdapter.class);
		resourceCenter = (DirectoryResourceCenter) applicationContext.getResourceCenterService().getResourceCenters().get(0);
		Assume.assumeNotNull(applicationContext, fpTA, resourceCenter);
	}

	@Before
	public void setup() {
		try {
			this.factory = new ModelFactory(IFreeplaneMap.class);
		} catch (final ModelDefinitionException e) {
			final String msg = "Error while initializing Freeplane model resource";
			LOGGER.log(Level.SEVERE, msg, e);
		}
		FreeplaneBasicAdapter.getInstance();
		this.initializeAFlexoController();

	}

	@Override
	public void tearDown() {
		this.factory = null;
		this.controller = null;
		this.perspective = null;
	}

	private void initializeAFlexoController() {
		try {
			final FlexoModule module = new VPMModule((ApplicationContext) applicationContext);
			module.initModule();
			controller = module.getController();
			this.perspective = controller.getCurrentPerspective();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "controller init fail", e);
			Assert.fail("Error while initializing FlexoController");
		}
	}

	//@Test
	public void emptyTest() {

	}

	@Test
	public void testInitModuleView() throws InvocationTargetException, InterruptedException {
		final MapModel loadedMap = FreeplaneBasicAdapter.getInstance().loadMapFromFile(ResourceLocator.retrieveResourceAsFile(
				ResourceLocator.locateResource("TestResourceCenter/FPTest.mm")));
		final FreeplaneMapImpl map = (FreeplaneMapImpl) this.factory.newInstance(IFreeplaneMap.class);
		map.setTechnologyAdapter(fpTA);
		map.setMapModel(loadedMap);
		FreeplaneAdapterController freeplaneAdapterController = new FreeplaneAdapterController();

		ModuleView moduleView = freeplaneAdapterController.createModuleViewForObject(map, controller, perspective);
		Assert.assertTrue(moduleView instanceof AbstractFreeplaneModuleView);

		ModuleView emptyView = freeplaneAdapterController.createModuleViewForObject(null, controller, perspective);
		Assert.assertTrue(emptyView instanceof EmptyPanel);

		final JFrame frame = new JFrame("Freeplane Module View Test Frame");
		frame.setLayout(new BorderLayout());
		frame.setSize(new Dimension(1024, 768));
		frame.getContentPane().add((Container) moduleView);
		FrameFixture fixture = new FrameFixture(frame);
		fixture.show();
		Thread.sleep(15000);
	}
}
