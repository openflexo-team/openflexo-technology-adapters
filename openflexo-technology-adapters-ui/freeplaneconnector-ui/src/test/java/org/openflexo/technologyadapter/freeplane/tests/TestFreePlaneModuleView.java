/**
 * 
 * Copyright (c) 2014-2016, Openflexo
 * 
 * This file is part of Freeplane, a component of the software infrastructure 
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
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openflexo.OpenflexoTestCaseWithGUI;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.controller.FreeplaneAdapterController;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.impl.FreeplaneMapImpl;
import org.openflexo.technologyadapter.freeplane.view.AbstractFreeplaneModuleView;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;

/**
 * Created by eloubout on 01/09/14.
 */
public class TestFreePlaneModuleView extends OpenflexoTestCaseWithGUI {

	private static final Logger LOGGER = Logger.getLogger(TestFreePlaneModuleView.class.getPackage().getName());

	private static FreeplaneTechnologyAdapter fpTA;

	private static FlexoServiceManager applicationContext;

	private ModelFactory factory;

	// private FlexoController controller;
	// private FlexoPerspective perspective;

	@BeforeClass
	public static void setupBeforeClass() {
		applicationContext = instanciateTestServiceManager();
		fpTA = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(FreeplaneTechnologyAdapter.class);
		
		// Looks for the first FileSystemBasedResourceCenter
				for (FlexoResourceCenter rc : applicationContext.getResourceCenterService().getResourceCenters() ){
					if (rc instanceof DirectoryResourceCenter && !rc.getResourceCenterEntry().isSystemEntry()){
						resourceCenter = (DirectoryResourceCenter) rc;
						break;
					}
				}
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
		// this.initializeAFlexoController();

	}

	@Override
	public void tearDown() {
		this.factory = null;
		// this.controller = null;
		// this.perspective = null;
	}

	/*private void initializeAFlexoController() {
		try {
			final FlexoModule module = new VPMModule((ApplicationContext) applicationContext);
			module.initModule();
			controller = module.getController();
			this.perspective = controller.getCurrentPerspective();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "controller init fail", e);
			Assert.fail("Error while initializing FlexoController");
		}
	}*/

	// @Test
	public void emptyTest() {

	}

	@Test
	public void testInitModuleView() throws InvocationTargetException, InterruptedException {
		final MapModel loadedMap = FreeplaneBasicAdapter.getInstance().loadMapFromFile(
				ResourceLocator.retrieveResourceAsFile(ResourceLocator.locateResource("TestResourceCenter/FPTest.mm")));
		final FreeplaneMapImpl map = (FreeplaneMapImpl) this.factory.newInstance(IFreeplaneMap.class);
		map.setTechnologyAdapter(fpTA);
		map.setMapModel(loadedMap);
		FreeplaneAdapterController freeplaneAdapterController = new FreeplaneAdapterController();

		// ModuleView moduleView = freeplaneAdapterController.createModuleViewForObject(map, controller, perspective);
		ModuleView moduleView = freeplaneAdapterController.createModuleViewForObject(map, null, null);
		Assert.assertTrue(moduleView instanceof AbstractFreeplaneModuleView);

		ModuleView emptyView = freeplaneAdapterController.createModuleViewForObject(null, null, null);
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
