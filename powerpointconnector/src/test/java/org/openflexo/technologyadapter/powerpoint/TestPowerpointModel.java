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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.AddUseDeclaration;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.foundation.fml.rm.CompilationUnitResourceFactory;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.test.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideShowRepository;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResource;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResourceFactory;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestPowerpointModel extends OpenflexoProjectAtRunTimeTestCase {

	private static final String VIEWPOINT_NAME = "TestPPTViewPoint";
	private static final String VIEWPOINT_URI = "http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint.fml";
	private static final String VIRTUAL_MODEL_NAME = "TestPPTVirtualModel";

	protected static final Logger logger = Logger.getLogger(TestPowerpointModel.class.getPackage().getName());

	private static FlexoServiceManager testApplicationContext;
	private static PowerpointTechnologyAdapter powerpointAdapter;
	private static PowerpointSlideShowRepository<?> modelRepository;
	private static File resourceCenterDirectory;

	private static DirectoryResourceCenter resourceCenter;

	/**
	 * Instantiate test resource center
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(1)
	public void test0InstantiateResourceCenter() throws IOException {

		log("test0InstantiateResourceCenter()");
		testApplicationContext = instanciateTestServiceManager(PowerpointTechnologyAdapter.class);
		assertNotNull(testApplicationContext);

		resourceCenter = makeNewDirectoryResourceCenter();

		resourceCenterDirectory = resourceCenter.getRootDirectory();
		powerpointAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(PowerpointTechnologyAdapter.class);
		assertNotNull(powerpointAdapter);
		/*
		 * for (FlexoResourceCenter rc :
		 * testApplicationContext.getResourceCenterService().getResourceCenters(
		 * )) { if (powerpointAdapter.getPowerpointSlideShowRepository(rc) !=
		 * null) { modelRepository =
		 * powerpointAdapter.getPowerpointSlideShowRepository(rc); }
		 * System.out.println(">>> rc=" + rc + " modelRepository=" +
		 * modelRepository); }
		 */
		modelRepository = powerpointAdapter.getPowerpointSlideShowRepository(
				testApplicationContext.getResourceCenterService().getFlexoResourceCenter("http://openflexo.org/ppt-test"));
		assertNotNull(modelRepository);
		assertTrue(modelRepository.getSize() > 0);
		logger.info("Found " + modelRepository.getSize() + " powerpoint files");
	}

	/*
	 * @Before public void setUp() { resourceCenterDirectory = new FileResource(
	 * new File("src/test/resources").getAbsolutePath());
	 * 
	 * testApplicationContext = instanciateTestServiceManager();
	 * FlexoResourceCenter resourceCenter =
	 * DirectoryResourceCenter.instanciateNewDirectoryResourceCenter(
	 * resourceCenterDirectory);
	 * testApplicationContext.getResourceCenterService().addToResourceCenters(
	 * resourceCenter); powerpointAdapter =
	 * testApplicationContext.getTechnologyAdapterService().
	 * getTechnologyAdapter( PowerpointTechnologyAdapter.class);
	 * 
	 * modelRepository = (PowerpointSlideShowRepository)
	 * resourceCenter.getRepository(PowerpointSlideShowRepository.class,
	 * powerpointAdapter); }
	 */

	/**
	 * Instanciate powerpoint viewpoint
	 * 
	 * @throws IOException
	 * @throws ModelDefinitionException
	 * @throws FlexoException
	 * @throws ResourceLoadingCancelledException
	 */
	@Test
	@TestOrder(2)
	public void testCreatePowerpointViewpoint()
			throws IOException, ModelDefinitionException, ResourceLoadingCancelledException, FlexoException {
		logger.info("testCreatePowerpointViewpoint()");

		System.out.println("resourceCenterDirectory=" + resourceCenterDirectory);

		FMLTechnologyAdapter fmlTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(FMLTechnologyAdapter.class);
		CompilationUnitResourceFactory factory = fmlTechnologyAdapter.getCompilationUnitResourceFactory();

		CompilationUnitResource compilationUnitResource = factory.makeTopLevelCompilationUnitResource(VIEWPOINT_NAME, VIEWPOINT_URI,
				fmlTechnologyAdapter.getGlobalRepository(resourceCenter).getRootFolder(), true);
		VirtualModel newViewPoint = compilationUnitResource.getLoadedResourceData().getVirtualModel();

		// ViewPoint newViewPoint =
		// ViewPointImpl.newViewPoint("TestPPTViewPoint",
		// "http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint",
		// resourceCenterDirectory,
		// testApplicationContext.getViewPointLibrary(), resourceCenter);

		assertNotNull(testApplicationContext.getVirtualModelLibrary().getVirtualModel(VIEWPOINT_URI));

		CompilationUnitResource newCompilationUnitResource = factory.makeContainedCompilationUnitResource(VIRTUAL_MODEL_NAME, compilationUnitResource, true);
		VirtualModel newVirtualModel = newCompilationUnitResource.getLoadedResourceData().getVirtualModel();

		// VirtualModel newVirtualModel = null;
		// try {
		// newVirtualModel =
		// VirtualModelImpl.newVirtualModel("TestPPTVirtualModel",
		// newViewPoint);
		FlexoConcept newFlexoConcept = newVirtualModel.getFMLModelFactory().newFlexoConcept();
		newVirtualModel.addToFlexoConcepts(newFlexoConcept);
		if (powerpointAdapter.getAvailableModelSlotTypes() != null) {
			for (Class<? extends ModelSlot<?>> msType : powerpointAdapter.getAvailableModelSlotTypes()) {
				AddUseDeclaration useDeclarationAction = AddUseDeclaration.actionType.makeNewAction(newVirtualModel, null, _editor);
				useDeclarationAction.setModelSlotClass(msType);
				useDeclarationAction.doAction();
				ModelSlot modelSlot = powerpointAdapter.makeModelSlot(msType, newVirtualModel);
				modelSlot.setName("powerpointBasicModelSlot");
				assertNotNull(modelSlot);
				newVirtualModel.addToModelSlots(modelSlot);
			}
		}
		newViewPoint.getResource().save();
		newVirtualModel.getResource().save();
		/*
		 * } catch (SaveResourceException e) { fail(e.getMessage()); }
		 */

		assertNotNull(newVirtualModel);

	}

	@Test
	@TestOrder(3)
	public void testLoadFiles() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		logger.info("testLoadFiles()");
		assertNotNull(modelRepository);
		assertTrue(modelRepository.getSize() > 0);
		for (PowerpointSlideshowResource pssResource : modelRepository.getAllResources()) {
			logger.info("Load file " + pssResource.getIODelegate().toString());
			assertNotNull(pssResource);
			assertFalse(pssResource.isLoaded());
			pssResource.loadResourceData();
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

		PowerpointTechnologyAdapter pptTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(PowerpointTechnologyAdapter.class);
		PowerpointSlideshowResourceFactory factory = pptTechnologyAdapter.getPowerpointSlideshowResourceFactory();

		modelRes = factory.makeResource(pptFile, resourceCenter, true);
		// modelRes =
		// PowerpointSlideshowResourceImpl.makePowerpointSlideshowResource(pptFile.getAbsolutePath(),
		// pptFile,
		// powerpointAdapter.getTechnologyContextManager(), resourceCenter);
		modelRes.save();
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
		VirtualModel test1ViewPoint = loadViewPoint("http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint.fml");
		assertNotNull(test1ViewPoint);

		System.out.println("dir=" + ((CompilationUnitResource) test1ViewPoint.getResource()).getDirectory());
		System.out.println("VMs=" + test1ViewPoint.getVirtualModels());

		VirtualModel vm = test1ViewPoint.getVirtualModelNamed("TestPPTVirtualModel");
		assertNotNull(vm);
		assertNotNull(vm.getModelSlots(BasicPowerpointModelSlot.class));
		assertTrue(vm.getModelSlots(BasicPowerpointModelSlot.class).size() > 0);
		BasicPowerpointModelSlot basicPowerpointModelslot = (BasicPowerpointModelSlot) vm.getModelSlot("powerpointBasicModelSlot");
		assertNotNull(basicPowerpointModelslot);

	}

	private VirtualModel loadViewPoint(String viewPointURI) {

		log("Testing ViewPoint loading: " + viewPointURI);
		System.out.println("resourceCenter=" + resourceCenter);
		System.out.println("resourceCenter.getViewPointRepository()=" + resourceCenter.getVirtualModelRepository());
		CompilationUnitResource viewPointResource = testApplicationContext.getVirtualModelLibrary().getCompilationUnitResource(viewPointURI);
		assertNotNull(viewPointResource);
		VirtualModel viewPoint = viewPointResource.getCompilationUnit().getVirtualModel();
		assertTrue(viewPointResource.isLoaded());
		return viewPoint;
	}
}
