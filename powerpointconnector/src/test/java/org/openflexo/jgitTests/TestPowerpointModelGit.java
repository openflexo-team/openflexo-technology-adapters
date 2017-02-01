package org.openflexo.jgitTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rm.ViewPointResourceFactory;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.resource.FlexoIOGitDelegate;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.GitResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.test.OpenFlexoTestCaseWithGit;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.powerpoint.PowerpointTechnologyAdapter;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideShowRepository;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResource;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResourceFactory;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestPowerpointModelGit extends OpenFlexoTestCaseWithGit {

	protected static final Logger logger = Logger.getLogger(TestPowerpointModelGit.class.getPackage().getName());

	private static final String VIEWPOINT_NAME = "TestPPTViewPoint";
	private static final String VIEWPOINT_URI = "http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint";
	private static final String VIRTUAL_MODEL_NAME = "TestPPTVirtualModel";

	private static FlexoServiceManager testApplicationContext;
	private static PowerpointTechnologyAdapter powerpointAdapter;
	private static PowerpointSlideShowRepository<?> modelRepository;

	private static File gitResourceCenterDirectory;

	private static GitResourceCenter gitResourceCenter;

	/**
	 * Instantiate test resource center
	 */
	@Test
	@TestOrder(1)
	public void test0InstantiateResourceCenter() {

		log("test0InstantiateResourceCenter()");
		testApplicationContext = instanciateTestServiceManager(PowerpointTechnologyAdapter.class);
		assertNotNull(testApplicationContext);

		powerpointAdapter = testApplicationContext.getTechnologyAdapterService()
				.getTechnologyAdapter(PowerpointTechnologyAdapter.class);
		assertNotNull(powerpointAdapter);

		for (FlexoResourceCenter rc : testApplicationContext.getResourceCenterService().getResourceCenters()) {
			System.out.println("rc = " + rc + " of " + rc.getClass());
		}

		for (FlexoResourceCenter rc : testApplicationContext.getResourceCenterService().getResourceCenters()) {
			if (powerpointAdapter.getPowerpointSlideShowRepository(rc) != null) {
				modelRepository = powerpointAdapter.getPowerpointSlideShowRepository(rc);
			}
			if (rc instanceof GitResourceCenter) {
				gitResourceCenter = (GitResourceCenter) rc;
			}
		}
		gitResourceCenterDirectory = gitResourceCenter.getDirectory();

		assertNotNull(modelRepository);
		assertTrue(modelRepository.getSize() > 0);
		Collection<PowerpointSlideshowResource> resources = modelRepository.getAllResources();
		for (PowerpointSlideshowResource powerpointSlideshowResource : resources) {
			System.out.println("Ressource " + powerpointSlideshowResource.getName());
		}

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
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(2)
	public void testCreatePowerpointViewpoint() throws IOException, SaveResourceException, ModelDefinitionException {
		logger.info("testCreatePowerpointViewpoint()");

		System.out.println("resourceCenterDirectory=" + gitResourceCenterDirectory);

		FMLTechnologyAdapter fmlTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(FMLTechnologyAdapter.class);
		ViewPointResourceFactory factory = fmlTechnologyAdapter.getViewPointResourceFactory();

		ViewPointResource viewPointResource = factory.makeViewPointResource(VIEWPOINT_NAME, VIEWPOINT_URI,
				fmlTechnologyAdapter.getGlobalRepository(gitResourceCenter).getRootFolder(),
				fmlTechnologyAdapter.getTechnologyContextManager(), true);
		ViewPoint newViewPoint = viewPointResource.getLoadedResourceData();

		/*
		 * Create a new Ressource and put it in the GitRessourceCenter
		 */
		/*
		 * ViewPoint newViewPoint =
		 * ViewPointImpl.newGitViewPoint("TestPPTViewPoint",
		 * "http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint",
		 * gitResourceCenter.getGitRepository().getWorkTree(),
		 * gitResourceCenter.getGitRepository(),
		 * testApplicationContext.getViewPointLibrary(), gitResourceCenter);
		 */
		assertNotNull(testApplicationContext.getViewPointLibrary()
				.getViewPoint("http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint"));

		VirtualModelResource newVMResource = factory.getVirtualModelResourceFactory().makeVirtualModelResource(
				VIRTUAL_MODEL_NAME, viewPointResource, fmlTechnologyAdapter.getTechnologyContextManager(), true);
		VirtualModel newVirtualModel = newVMResource.getLoadedResourceData();

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
	public void retrieveFileInGitRepository() throws NoWorkTreeException, IOException, ModelDefinitionException {
		Repository gitRepository = gitResourceCenter.getGitRepository();
		ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FlexoIOGitDelegate.class,
				PowerpointSlideshowResource.class));
		Collection<FlexoResource<?>> ressources = gitResourceCenter.getAllResources();
		for (FlexoResource<?> flexoResource : ressources) {
			// flexoResource.setFlexoIODelegate(FlexoIOGitDelegateImpl.makeFlexoIOGitDelegate(flexoResource.getName(),
			// factory,
			// gitRepository.getWorkTree(), gitRepository));
			flexoResource
					.setFlexoIODelegate(gitResourceCenter.getGitIODelegateFactory().makeNewInstance(flexoResource));
			FlexoIOGitDelegate gitDelegate = (FlexoIOGitDelegate) flexoResource.getFlexoIODelegate();
			try {
				gitDelegate.save(flexoResource);
			} catch (NotImplementedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ObjectId commitId = gitDelegate.getGitObjectId();
			if (commitId != null) {
				FileTreeIterator fileTree = new FileTreeIterator(gitRepository);
				System.out.println("Commit Id : " + commitId.getName());
				while (!fileTree.getEntryObjectId().equals(commitId)) {
					System.out.println("Current Entry : " + fileTree.getEntryObjectId().getName());
					fileTree.next(1);
				}
				System.out.println("File retrieved :" + fileTree.getEntryFile().getName());
			} else {
				logger.warning("Null commitId !!! Please investigate");
			}
		}
		// ViewPoint viewPointToRetrieve =
		// testApplicationContext.getViewPointLibrary()
		// .getViewPoint("http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint");
		// FlexoIOGitDelegate gitDelegate = (FlexoIOGitDelegate)
		// viewPointToRetrieve.getResource().getFlexoIODelegate();
		// ObjectId commitId = gitDelegate.getGitObjectId();

	}

	@Test
	@TestOrder(5)
	public void testCreateNewFile() throws Exception {
		logger.info("testCreateNewFile()");
		assertNotNull(modelRepository);

		PowerpointSlideshowResource modelRes;

		File generationDir = new File(gitResourceCenterDirectory, "GenPowerpoint");
		generationDir.mkdirs();
		assertTrue(generationDir.exists());

		System.out.println("resourceCenterDirectory=" + gitResourceCenterDirectory);
		System.out.println("generationDir=" + generationDir);

		File pptFile = new File(generationDir, "generated_File.ppt");

		PowerpointTechnologyAdapter pptTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(PowerpointTechnologyAdapter.class);
		PowerpointSlideshowResourceFactory factory = pptTechnologyAdapter.getPowerpointSlideshowResourceFactory();

		System.out.println("avant la resource pptFile = " + pptFile);
		modelRes = factory.makeResource(pptFile, gitResourceCenter, pptTechnologyAdapter.getTechnologyContextManager(),
				true);
		System.out.println("apres: " + modelRes.getFlexoIODelegate().getSerializationArtefact());

		// modelRes =
		// PowerpointSlideshowResourceImpl.makePowerpointSlideshowResource(pptFile.getAbsolutePath(),
		// pptFile,
		// powerpointAdapter.getTechnologyContextManager(), resourceCenter);

		modelRes.save(null);

		System.out.println("Serialization artefact = " + modelRes.getFlexoIODelegate().getSerializationArtefact());
		System.out.println("pptFile = " + pptFile);

		assertTrue(pptFile.exists());

		modelRes.delete();
		pptFile.delete();
	}

	/**
	 * Load powerpoint viewpoint
	 * 
	 * @throws IOException
	 */
	/*
	 * @Test
	 * 
	 * @TestOrder(6) public void testLoadPowerpointViewpoints() throws
	 * IOException { ViewPoint test1ViewPoint = loadViewPoint(
	 * "http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint");
	 * assertNotNull(test1ViewPoint);
	 * 
	 * System.out.println("dir=" + ((ViewPointResource)
	 * test1ViewPoint.getResource()).getDirectory()); System.out.println("VMs="
	 * + test1ViewPoint.getVirtualModels());
	 * 
	 * VirtualModel vm =
	 * test1ViewPoint.getVirtualModelNamed("TestPPTVirtualModel");
	 * assertNotNull(vm);
	 * assertNotNull(vm.getModelSlots(BasicPowerpointModelSlot.class));
	 * assertTrue(vm.getModelSlots(BasicPowerpointModelSlot.class).size() > 0);
	 * BasicPowerpointModelSlot basicPowerpointModelslot =
	 * (BasicPowerpointModelSlot) vm.getModelSlot("powerpointBasicModelSlot");
	 * assertNotNull(basicPowerpointModelslot);
	 * 
	 * }
	 */

	private ViewPoint loadViewPoint(String viewPointURI) {

		log("Testing ViewPoint loading: " + viewPointURI);
		// System.out.println("resourceCenter=" + resourceCenter);
		// System.out.println("resourceCenter.getViewPointRepository()=" +
		// resourceCenter.getViewPointRepository());
		ViewPointResource viewPointResource = testApplicationContext.getViewPointLibrary()
				.getViewPointResource(viewPointURI);
		assertNotNull(viewPointResource);
		ViewPoint viewPoint = viewPointResource.getViewPoint();
		assertTrue(viewPointResource.isLoaded());
		return viewPoint;
	}
}
