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


import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenflexoRunTimeTestCase;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.viewpoint.VirtualModel.VirtualModelImpl;
import org.openflexo.foundation.viewpoint.VirtualModelTechnologyAdapter;
import org.openflexo.technologyadapter.powerpoint.BasicPowerpointModelSlot;
import org.openflexo.technologyadapter.powerpoint.BasicPowerpointModelSlot.BasicPowerpointModelSlotImpl;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideShowRepository;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResource;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResourceImpl;
import org.openflexo.technologyadapter.powerpoint.viewpoint.editionaction.AddPowerpointShape;
import org.openflexo.technologyadapter.powerpoint.viewpoint.editionaction.AddPowerpointShape.AddPowerpointShapeImpl;
import org.openflexo.toolbox.FileResource;


public class TestPowerpointEditionActions extends OpenflexoRunTimeTestCase {

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
	
	@Before
    public void setUp() {
		File resourceCenterDirectory = new FileResource(
				new File("src/test/resources").getAbsolutePath());
		
		testApplicationContext = instanciateTestServiceManager();
		resourceCenter = DirectoryResourceCenter.instanciateNewDirectoryResourceCenter(resourceCenterDirectory);
		testApplicationContext.getResourceCenterService().addToResourceCenters(resourceCenter);
		powerpointAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(
				PowerpointTechnologyAdapter.class);
		
		
		VirtualModelTechnologyAdapter testAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(
				VirtualModelTechnologyAdapter.class);
		
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
	}
	
	/**
	 * Add a powerpoint slide
	 * 
	 * @throws IOException
	 */
	@Test
	public void testAddPowerpointSlide() throws IOException {
		logger.info("testAddPowerpointSlide()");
		AddPowerpointShape addPowerpointShape = modelSlot.createAction(AddPowerpointShape.class);
		
	
		//assertNotNull(modelRes);
		
	}
	
	/**
	 * Add a powerpoint shape
	 * 
	 * @throws IOException
	 */
	@Test
	public void testAddPowerpointShape() throws IOException {
		logger.info("testAddPowerpointShape()");
		//assertNotNull(modelRes);
		
		
	}
	
	/**
	 * Select a powerpoint shape
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSelectPowerpointShape() throws IOException {
		logger.info("testSelectPowerpointShape()");
		//assertNotNull(modelRes);
	}
	
	/**
	 * Select a powerpoint slide
	 * 
	 * @throws IOException
	 */
	@Test
	public void testSelectPowerpointSlide() throws IOException {
		logger.info("testSelectPowerpointSlide()");
		//assertNotNull(modelRes);
	}
}
