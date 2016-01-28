/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Powerpointconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.powerpoint;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModel.VirtualModelImpl;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideShowRepository;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResource;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResourceImpl;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestPowerpointModel extends OpenflexoProjectAtRunTimeTestCase {

	protected static final Logger logger = Logger.getLogger(TestPowerpointModel.class.getPackage().getName());

	private static FlexoServiceManager testApplicationContext;
	private static PowerpointTechnologyAdapter powerpointAdapter;
	private static PowerpointSlideShowRepository modelRepository;
	private static File resourceCenterDirectory;

	/**
	 * Instantiate test resource center
	 */
	@Test
	@TestOrder(1)
	public void test0InstantiateResourceCenter() {

		log("test0InstantiateResourceCenter()");
		testApplicationContext = instanciateTestServiceManager();
		assertNotNull(testApplicationContext);
		resourceCenterDirectory = resourceCenter.getDirectory();
		powerpointAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(PowerpointTechnologyAdapter.class);
		assertNotNull(powerpointAdapter);
		for (FlexoResourceCenter rc : testApplicationContext.getResourceCenterService().getResourceCenters()) {
			if (rc.getRepository(PowerpointSlideShowRepository.class, powerpointAdapter) != null) {
				modelRepository = (PowerpointSlideShowRepository) rc.getRepository(PowerpointSlideShowRepository.class, powerpointAdapter);
			}
		}
		assertNotNull(modelRepository);
		assertTrue(modelRepository.getSize() > 0);
		logger.info("Found " + modelRepository.getSize() + " powerpoint files");
	}

	/*
	@Before
	public void setUp() {
		 resourceCenterDirectory = new FileResource(
				new File("src/test/resources").getAbsolutePath());
		
		testApplicationContext = instanciateTestServiceManager();
		FlexoResourceCenter resourceCenter = DirectoryResourceCenter.instanciateNewDirectoryResourceCenter(resourceCenterDirectory);
		testApplicationContext.getResourceCenterService().addToResourceCenters(resourceCenter);
		powerpointAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(
				PowerpointTechnologyAdapter.class);
		
		modelRepository = (PowerpointSlideShowRepository) resourceCenter.getRepository(PowerpointSlideShowRepository.class, powerpointAdapter);
	}*/

	/**
	 * Instanciate powerpoint viewpoint
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(2)
	public void testCreatePowerpointViewpoint() throws IOException {
		logger.info("testCreatePowerpointViewpoint()");

		System.out.println("resourceCenterDirectory=" + resourceCenterDirectory);

		ViewPoint newViewPoint = ViewPointImpl.newViewPoint("TestPPTViewPoint",
				"http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint", resourceCenterDirectory,
				testApplicationContext.getViewPointLibrary(), resourceCenter);

		assertNotNull(
				testApplicationContext.getViewPointLibrary().getViewPoint("http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint"));

		VirtualModel newVirtualModel = null;
		try {
			newVirtualModel = VirtualModelImpl.newVirtualModel("TestPPTVirtualModel", newViewPoint);
			FlexoConcept newFlexoConcept = newVirtualModel.getFMLModelFactory().newFlexoConcept();
			newVirtualModel.addToFlexoConcepts(newFlexoConcept);
			if (powerpointAdapter.getAvailableModelSlotTypes() != null) {
				for (Class msType : powerpointAdapter.getAvailableModelSlotTypes()) {
					ModelSlot modelSlot = powerpointAdapter.makeModelSlot(msType, newVirtualModel);
					modelSlot.setName("powerpointBasicModelSlot");
					assertNotNull(modelSlot);
					newVirtualModel.addToModelSlots(modelSlot);
				}
			}
			newViewPoint.getResource().save(null);
			newVirtualModel.getResource().save(null);
		} catch (SaveResourceException e) {
			fail(e.getMessage());
		}

		assertNotNull(newVirtualModel);

	}

	@Test
	@TestOrder(3)
	public void testLoadFiles() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		logger.info("testLoadFiles()");
		assertNotNull(modelRepository);
		assertTrue(modelRepository.getSize() > 0);
		for (PowerpointSlideshowResource pssResource : modelRepository.getAllResources()) {
			logger.info("Load file " + pssResource.getFlexoIODelegate().toString());
			assertNotNull(pssResource);
			assertFalse(pssResource.isLoaded());
			pssResource.loadResourceData(null);
			assertTrue(pssResource.isLoaded());
			assertNotNull(pssResource.getLoadedResourceData());
		}
	}

	@Test
	@TestOrder(4)
	public void testCreateNewFile() throws Exception {
		logger.info("testCreateNewFile()");
		assertNotNull(modelRepository);

		PowerpointSlideshowResource modelRes;

		File generationDir = new File(resourceCenterDirectory, "GenPowerpoint");
		generationDir.mkdirs();
		assertTrue(generationDir.exists());

		File pptFile = new File(generationDir, "generated_File.ppt");
		modelRes = PowerpointSlideshowResourceImpl.makePowerpointSlideshowResource(pptFile.getAbsolutePath(), pptFile,
				powerpointAdapter.getTechnologyContextManager(), resourceCenter);
		modelRes.save(null);
		assertTrue(pptFile.exists());
		modelRes.delete();
		pptFile.delete();
	}

	/**
	 * Load powerpoint viewpoint
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(5)
	public void testLoadPowerpointViewpoints() throws IOException {
		ViewPoint test1ViewPoint = loadViewPoint("http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint");
		assertNotNull(test1ViewPoint);

		System.out.println("dir=" + ((ViewPointResource) test1ViewPoint.getResource()).getDirectory());
		System.out.println("VMs=" + test1ViewPoint.getVirtualModels());

		VirtualModel vm = test1ViewPoint.getVirtualModelNamed("TestPPTVirtualModel");
		assertNotNull(vm);
		assertNotNull(vm.getModelSlots(BasicPowerpointModelSlot.class));
		assertTrue(vm.getModelSlots(BasicPowerpointModelSlot.class).size() > 0);
		BasicPowerpointModelSlot basicPowerpointModelslot = (BasicPowerpointModelSlot) vm.getModelSlot("powerpointBasicModelSlot");
		assertNotNull(basicPowerpointModelslot);

	}

	private ViewPoint loadViewPoint(String viewPointURI) {

		log("Testing ViewPoint loading: " + viewPointURI);
		System.out.println("resourceCenter=" + resourceCenter);
		System.out.println("resourceCenter.getViewPointRepository()=" + resourceCenter.getViewPointRepository());
		ViewPointResource viewPointResource = testApplicationContext.getViewPointLibrary().getViewPointResource(viewPointURI);
		assertNotNull(viewPointResource);
		ViewPoint viewPoint = viewPointResource.getViewPoint();
		assertTrue(viewPointResource.isLoaded());
		return viewPoint;
	}
}
