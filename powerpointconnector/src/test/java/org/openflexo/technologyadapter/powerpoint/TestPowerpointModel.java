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

import org.junit.Before;
import org.junit.Test;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenflexoRunTimeTestCase;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.VirtualModel.VirtualModelImpl;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideShowRepository;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResource;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResourceImpl;
import org.openflexo.toolbox.FileResource;


public class TestPowerpointModel extends OpenflexoRunTimeTestCase {

	protected static final Logger logger = Logger.getLogger(TestPowerpointModel.class.getPackage().getName());

	private static FlexoServiceManager testApplicationContext;
	private static PowerpointTechnologyAdapter powerpointAdapter;
	private static PowerpointSlideShowRepository modelRepository;
	private static File resourceCenterDirectory;

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
    }
	
	/**
	 * Instanciate powerpoint model slots
	 * 
	 * @throws IOException
	 */
	@Test
	public void testLoadPowerpointModelSlots() throws IOException {
		logger.info("testLoadPowerpointModelSlots()");
		assertNotNull(modelRepository);
		assertNotNull(powerpointAdapter);
	
		ViewPoint newViewPoint = ViewPointImpl.newViewPoint("TestViewPoint",
				"http://openflexo.org/test/TestViewPoint",
				resourceCenterDirectory,
				testApplicationContext.getViewPointLibrary());
		try {
			VirtualModel newVirtualModel = VirtualModelImpl.newVirtualModel("TestVirtualModel", newViewPoint);
			if(powerpointAdapter.getAvailableModelSlotTypes()!=null){
				for(Class msType : powerpointAdapter.getAvailableModelSlotTypes()){
					ModelSlot modelSlot = powerpointAdapter.makeModelSlot(msType, newVirtualModel);
					assertNotNull(modelSlot);
				}
			}
		} catch (SaveResourceException e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Instanciate test ResourceCenter
	 * 
	 * @throws IOException
	 */
	@Test
	public void testLoadPowerpointResourceCenter() throws IOException {
		logger.info("testLoadPowerpointResourceCenter()");
		assertNotNull(modelRepository);
		assertTrue(modelRepository.getSize()>0);
		logger.info("Found "+modelRepository.getSize()+" powerpoint files");
	}

	@Test
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
	public void testCreateNewFile() throws Exception {
		logger.info("testCreateNewFile()");
		assertNotNull(modelRepository);
		
		PowerpointSlideshowResource modelRes;
		String fileName = "src/test/resources/GenPowerpoint/generated_File.ppt";
		
		int oldModelRepositorySize=modelRepository.getSize();

		File pptFile = new File(fileName);

		modelRes = PowerpointSlideshowResourceImpl.makePowerpointSlideshowResource(pptFile.getAbsolutePath(),pptFile, powerpointAdapter.getTechnologyContextManager());
			
		modelRes.save(null);
		
		assertEquals(oldModelRepositorySize+1,modelRepository.getSize());
		modelRes.delete();
		pptFile.delete();
	}
}
