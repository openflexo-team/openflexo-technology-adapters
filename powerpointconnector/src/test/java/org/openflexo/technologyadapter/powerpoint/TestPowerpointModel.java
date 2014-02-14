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

import static org.junit.Assert.assertEquals;
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
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.VirtualModel.VirtualModelImpl;
import org.openflexo.foundation.viewpoint.rm.ViewPointResource;
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
		powerpointAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(
				PowerpointTechnologyAdapter.class);
		assertNotNull(powerpointAdapter);
		for(FlexoResourceCenter rc : testApplicationContext.getResourceCenterService().getResourceCenters()){
			if(rc.getRepository(PowerpointSlideShowRepository.class, powerpointAdapter)!=null){
				modelRepository = (PowerpointSlideShowRepository) rc.getRepository(PowerpointSlideShowRepository.class, powerpointAdapter);
			}
		}
		assertNotNull(modelRepository);
		assertTrue(modelRepository.getSize()>0);
		logger.info("Found "+modelRepository.getSize()+" powerpoint files");
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
		
	
		ViewPoint newViewPoint = ViewPointImpl.newViewPoint("TestPPTViewPoint",
				"http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint",
				resourceCenterDirectory,
				testApplicationContext.getViewPointLibrary());
		
		
		VirtualModel newVirtualModel = null;
		try {
			newVirtualModel = VirtualModelImpl.newVirtualModel("TestPPTVirtualModel", newViewPoint);
			FlexoConcept newFlexoConcept = newVirtualModel.getVirtualModelFactory().newFlexoConcept();
			if(powerpointAdapter.getAvailableModelSlotTypes()!=null){
				for(Class msType : powerpointAdapter.getAvailableModelSlotTypes()){
					ModelSlot modelSlot = powerpointAdapter.makeModelSlot(msType, newVirtualModel);
					assertNotNull(modelSlot);
				}
			}
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
		assertTrue(modelRepository.getSize()>0);
		for(PowerpointSlideshowResource pssResource : modelRepository.getAllResources()){
			logger.info("Load file "+pssResource.getName());
			assertNotNull(pssResource);
			assertFalse(pssResource.isLoaded());
			assertNotNull(pssResource.loadResourceData(null));
			assertTrue(pssResource.isLoaded());
		}
	}

	@Test
	@TestOrder(4)
	public void testCreateNewFile() throws Exception {
		logger.info("testCreateNewFile()");
		assertNotNull(modelRepository);
		
		PowerpointSlideshowResource modelRes;
		String fileName = "src/test/resources/TestResourceCenter/GenPowerpoint/generated_File.ppt";
		
		int oldModelRepositorySize=modelRepository.getSize();

		File pptFile = new File(fileName);
		modelRes = PowerpointSlideshowResourceImpl.makePowerpointSlideshowResource(pptFile.getAbsolutePath(),pptFile, powerpointAdapter.getTechnologyContextManager());
		modelRes.save(null);
		
		assertEquals(oldModelRepositorySize+1,modelRepository.getSize());
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
		ViewPoint test1ViewPoint = loadViewPoint("http://openflexo.org/test/TestViewPoint");
		assertNotNull(test1ViewPoint);
		VirtualModel vm = test1ViewPoint.getVirtualModelNamed("TestVirtualModel");
		assertNotNull(vm);
		assertNotNull(vm.getModelSlots(BasicPowerpointModelSlot.class));
		assertTrue(vm.getModelSlots(BasicPowerpointModelSlot.class).size()>0);
		BasicPowerpointModelSlot basicPowerpointModelslot = (BasicPowerpointModelSlot) vm.getModelSlot("powerpointBasicModelSlot");
		assertNotNull(basicPowerpointModelslot);
		

	}
	
	private ViewPoint loadViewPoint(String viewPointURI) {
		log("Testing ViewPoint loading: " + viewPointURI);
		System.out.println("resourceCenter=" + resourceCenter);
		System.out.println("resourceCenter.getViewPointRepository()=" + resourceCenter.getViewPointRepository());
		ViewPointResource viewPointResource = resourceCenter.getViewPointRepository().getResource(viewPointURI);
		assertNotNull(viewPointResource);
		assertFalse(viewPointResource.isLoaded());
		ViewPoint viewPoint = viewPointResource.getViewPoint();
		assertTrue(viewPointResource.isLoaded());
		return viewPoint;
	}
}
