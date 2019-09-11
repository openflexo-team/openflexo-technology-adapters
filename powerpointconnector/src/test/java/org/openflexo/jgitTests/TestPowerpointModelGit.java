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
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.AddUseDeclaration;
import org.openflexo.foundation.fml.rm.CompilationUnitResource;
import org.openflexo.foundation.fml.rm.CompilationUnitResourceFactory;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.GitIODelegate;
import org.openflexo.foundation.resource.GitResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.test.OpenFlexoTestCaseWithGit;
import org.openflexo.pamela.ModelContextLibrary;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.pamela.factory.ModelFactory;
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

		powerpointAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(PowerpointTechnologyAdapter.class);
		assertNotNull(powerpointAdapter);

		for (FlexoResourceCenter<?> rc : testApplicationContext.getResourceCenterService().getResourceCenters()) {
			System.out.println("rc = " + rc + " of " + rc.getClass());
		}

		for (FlexoResourceCenter<?> rc : testApplicationContext.getResourceCenterService().getResourceCenters()) {
			if (powerpointAdapter.getPowerpointSlideShowRepository(rc) != null) {
				modelRepository = powerpointAdapter.getPowerpointSlideShowRepository(rc);
			}
			if (rc instanceof GitResourceCenter) {
				gitResourceCenter = (GitResourceCenter) rc;
			}
		}
		gitResourceCenterDirectory = gitResourceCenter.getRootDirectory();

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
	 * @throws FlexoException
	 * @throws ResourceLoadingCancelledException
	 */
	@Test
	@TestOrder(2)
	public void testCreatePowerpointViewpoint()
			throws IOException, ModelDefinitionException, ResourceLoadingCancelledException, FlexoException {
		logger.info("testCreatePowerpointViewpoint()");

		System.out.println("resourceCenterDirectory=" + gitResourceCenterDirectory);

		FMLTechnologyAdapter fmlTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(FMLTechnologyAdapter.class);
		CompilationUnitResourceFactory factory = fmlTechnologyAdapter.getCompilationUnitResourceFactory();

		CompilationUnitResource compilationUnitResource = factory.makeTopLevelCompilationUnitResource(VIEWPOINT_NAME, VIEWPOINT_URI,
				fmlTechnologyAdapter.getGlobalRepository(gitResourceCenter).getRootFolder(), true);
		VirtualModel newViewPoint = compilationUnitResource.getLoadedResourceData().getVirtualModel();

		assertNotNull(testApplicationContext.getVirtualModelLibrary()
				.getVirtualModel("http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint.fml"));

		CompilationUnitResource newCompilationUnitResource = factory.makeContainedCompilationUnitResource(VIRTUAL_MODEL_NAME, compilationUnitResource, true);
		VirtualModel newVirtualModel = newCompilationUnitResource.getLoadedResourceData().getVirtualModel();

		FlexoConcept newFlexoConcept = newVirtualModel.getFMLModelFactory().newFlexoConcept();
		newVirtualModel.addToFlexoConcepts(newFlexoConcept);
		if (powerpointAdapter.getAvailableModelSlotTypes() != null) {
			for (Class<? extends ModelSlot<?>> msType : powerpointAdapter.getAvailableModelSlotTypes()) {
				AddUseDeclaration useDeclarationAction = AddUseDeclaration.actionType.makeNewAction(newVirtualModel, null, _editor);
				useDeclarationAction.setModelSlotClass(msType);
				useDeclarationAction.doAction();
				ModelSlot<?> modelSlot = powerpointAdapter.makeModelSlot(msType, newVirtualModel);
				modelSlot.setName("powerpointBasicModelSlot");
				assertNotNull(modelSlot);
				newVirtualModel.addToModelSlots(modelSlot);
			}
		}
		newViewPoint.getResource().save();
		newVirtualModel.getResource().save();

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
	public void retrieveFileInGitRepository() throws NoWorkTreeException, IOException, ModelDefinitionException {
		try (Repository gitRepository = gitResourceCenter.getGitRepository()) {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(GitIODelegate.class, PowerpointSlideshowResource.class));
			Collection<FlexoResource<?>> ressources = gitResourceCenter.getAllResources();
			for (FlexoResource<?> flexoResource : ressources) {
				// flexoResource.setFlexoIODelegate(FlexoIOGitDelegateImpl.makeFlexoIOGitDelegate(flexoResource.getName(),
				// factory,
				// gitRepository.getWorkTree(), gitRepository));
				flexoResource.setIODelegate(gitResourceCenter.getGitIODelegateFactory().makeNewInstance(flexoResource));
				GitIODelegate gitDelegate = (GitIODelegate) flexoResource.getIODelegate();
				try {
					gitDelegate.save(flexoResource);
				} catch (NotImplementedException e) {
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
				}
				else {
					logger.warning("Null commitId !!! Please investigate");
				}
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
		modelRes = factory.makeResource(pptFile, gitResourceCenter, true);
		System.out.println("apres: " + modelRes.getIODelegate().getSerializationArtefact());

		// modelRes =
		// PowerpointSlideshowResourceImpl.makePowerpointSlideshowResource(pptFile.getAbsolutePath(),
		// pptFile,
		// powerpointAdapter.getTechnologyContextManager(), resourceCenter);

		modelRes.save();

		System.out.println("Serialization artefact = " + modelRes.getIODelegate().getSerializationArtefact());
		System.out.println("pptFile = " + pptFile);

		assertTrue(pptFile.exists());

		modelRes.delete();
		pptFile.delete();
	}

	private VirtualModel loadViewPoint(String viewPointURI) {

		log("Testing ViewPoint loading: " + viewPointURI);
		// System.out.println("resourceCenter=" + resourceCenter);
		// System.out.println("resourceCenter.getViewPointRepository()=" +
		// resourceCenter.getViewPointRepository());
		CompilationUnitResource viewPointResource = testApplicationContext.getVirtualModelLibrary().getCompilationUnitResource(viewPointURI);
		assertNotNull(viewPointResource);
		VirtualModel viewPoint = viewPointResource.getCompilationUnit().getVirtualModel();
		assertTrue(viewPointResource.isLoaded());
		return viewPoint;
	}
}
