/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.powerpoint;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideShowRepository;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestPowerpointEditionActions extends OpenflexoProjectAtRunTimeTestCase {

	protected static final Logger logger = Logger.getLogger(TestPowerpointEditionActions.class.getPackage().getName());

	private static FlexoServiceManager testApplicationContext;
	private static PowerpointTechnologyAdapter powerpointAdapter;
	private static FlexoResourceCenter resourceCenter;
	private static PowerpointSlideShowRepository modelRepository;
	private static String baseDirName;
	private static PowerpointSlideshowResource modelRes;
	private static BasicPowerpointModelSlot modelSlot;
	private static ViewPoint newViewPoint;
	private static VirtualModel newVirtualModel;

	/*@Before
	public void setUp() {
		File resourceCenterDirectory = new FileResource(
				new File("src/test/resources").getAbsolutePath());
		
		testApplicationContext = instanciateTestServiceManager();
		resourceCenter = DirectoryResourceCenter.instanciateNewDirectoryResourceCenter(resourceCenterDirectory);
		testApplicationContext.getResourceCenterService().addToResourceCenters(resourceCenter);
		powerpointAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(
				PowerpointTechnologyAdapter.class);
		
		
		FMLTechnologyAdapter testAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(
				FMLTechnologyAdapter.class);
		
		modelRepository = (PowerpointSlideShowRepository) resourceCenter.getRepository(PowerpointSlideShowRepository.class, powerpointAdapter);
		File pptFile = new File("src/test/resources/GenPowerpoint/generated_File2.ppt");
		modelRes = PowerpointSlideshowResourceImpl.makePowerpointSlideshowResource(pptFile.getAbsolutePath(),pptFile, powerpointAdapter.getTechnologyContextManager());

		newViewPoint = ViewPointImpl.newViewPoint("TestViewPoint",
				"http://openflexo.org/test/TestViewPoint",
				resourceCenterDirectory,
				testApplicationContext.getViewPointLibrary());
		try {
			newVirtualModel = VirtualModelImpl.newVirtualModel(
					"TestVirtualModel", newViewPoint);
			modelSlot = powerpointAdapter.makeModelSlot(BasicPowerpointModelSlot.class, newVirtualModel);
			modelRes.save(null);
		} catch (SaveResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	/**
	 * Instantiate test resource center
	 */
	@Test
	@TestOrder(1)
	public void test0InstantiateResourceCenter() {

		log("test0InstantiateResourceCenter()");
		testApplicationContext = instanciateTestServiceManager();
		assertNotNull(testApplicationContext);
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

	/**
	 * Add a powerpoint slide
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(2)
	public void testAddPowerpointSlide() throws IOException {
		logger.info("testAddPowerpointSlide()");
		// AddPowerpointShape addPowerpointShape = modelSlot.createAction(AddPowerpointShape.class);

		// assertNotNull(modelRes);

	}

	/**
	 * Add a powerpoint shape
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(3)
	public void testAddPowerpointShape() throws IOException {
		logger.info("testAddPowerpointShape()");
		// assertNotNull(modelRes);

	}

	/**
	 * Select a powerpoint shape
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(4)
	public void testSelectPowerpointShape() throws IOException {
		logger.info("testSelectPowerpointShape()");
		// assertNotNull(modelRes);
	}

	/**
	 * Select a powerpoint slide
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(5)
	public void testSelectPowerpointSlide() throws IOException {
		logger.info("testSelectPowerpointSlide()");
		// assertNotNull(modelRes);
	}
}
