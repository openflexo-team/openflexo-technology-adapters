package org.openflexo.jgitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenFlexoTestCaseWithGit;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModel.VirtualModelImpl;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.resource.FlexoIOGitDelegate;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.GitResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.gitUtils.GitIODelegateFactory;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.powerpoint.PowerpointTechnologyAdapter;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideShowRepository;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResource;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResourceImpl;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.toolbox.FlexoVersion;

@RunWith(OrderedRunner.class)
public class TestPowerpointModelGit extends OpenFlexoTestCaseWithGit {

	protected static final Logger logger = Logger.getLogger(TestPowerpointModelGit.class.getPackage().getName());

	private static FlexoServiceManager testApplicationContext;
	private static PowerpointTechnologyAdapter powerpointAdapter;
	private static PowerpointSlideShowRepository modelRepository;

	private static GitResourceCenter gitResourceCenter;
	private static Repository emptyGitRepository;
	
	/**
	 * Instantiate test resource center
	 * @throws GitAPIException 
	 * @throws AbortedByHookException 
	 * @throws WrongRepositoryStateException 
	 * @throws ConcurrentRefUpdateException 
	 * @throws UnmergedPathsException 
	 * @throws NoMessageException 
	 * @throws NoHeadException 
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@Test
	@TestOrder(1)
	public void test0InstantiateResourceCenter() throws NoHeadException, NoMessageException, UnmergedPathsException, ConcurrentRefUpdateException, WrongRepositoryStateException, AbortedByHookException, GitAPIException, IllegalStateException, IOException {

		log("test0InstantiateResourceCenter()");
		testApplicationContext = instanciateTestServiceManager(false);
		assertNotNull(testApplicationContext);
		powerpointAdapter = testApplicationContext.getTechnologyAdapterService()
				.getTechnologyAdapter(PowerpointTechnologyAdapter.class);
		assertNotNull(powerpointAdapter);
		for (FlexoResourceCenter rc : testApplicationContext.getResourceCenterService().getResourceCenters()) {
			if (rc.getRepository(PowerpointSlideShowRepository.class, powerpointAdapter) != null) {
				modelRepository = (PowerpointSlideShowRepository) rc.getRepository(PowerpointSlideShowRepository.class,
						powerpointAdapter);
			}
			if (rc instanceof GitResourceCenter) {
				gitResourceCenter = (GitResourceCenter) rc;
			}
		}

		assertNotNull(modelRepository);
		assertTrue(modelRepository.getSize() > 0);
		assertTrue(gitResourceCenter.getDelegateFactory() instanceof GitIODelegateFactory);
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
	 */
	@Test
	@TestOrder(2)
	public void testCreatePowerpointViewpoint() throws IOException {
		logger.info("testCreatePowerpointViewpoint()");

		/*
		 * Create a new Ressource and put it in the GitRessourceCenter
		 */
		ViewPoint newViewPoint = ViewPointImpl.newGitViewPoint("TestPPTViewPoint",
				"http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint",
				gitResourceCenter.getGitRepository().getWorkTree(), gitResourceCenter.getGitRepository(),
				testApplicationContext.getViewPointLibrary(), gitResourceCenter);
		assertNotNull(testApplicationContext.getViewPointLibrary()
				.getViewPoint("http://openflexo.org/test/TestResourceCenter/TestPPTViewPoint"));

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
	public void retrieveFileInGitRepository() throws NoWorkTreeException, IOException, ModelDefinitionException {
		Repository gitRepository = gitResourceCenter.getGitRepository();
		Collection<FlexoResource<?>> ressources = gitResourceCenter.getAllResources();
		for (FlexoResource<?> flexoResource : ressources) {
			flexoResource
					.setFlexoIODelegate(gitResourceCenter.getDelegateFactory().makeNewInstance(flexoResource));
			FlexoIOGitDelegate gitDelegate = (FlexoIOGitDelegate) flexoResource.getFlexoIODelegate();
			gitDelegate.createAndSaveIO(flexoResource);
			System.out.println("Ressource " + flexoResource.getName() + " saved first time");
			LinkedList<ObjectId> listIds = (LinkedList<ObjectId>) gitDelegate.getGitObjectIds();

			FileTreeIterator fileTree = new FileTreeIterator(gitRepository);

			for (ObjectId objectId : listIds) {
				while (!fileTree.getEntryObjectId().equals(objectId)) {
					fileTree.next(1);
				}
				assertEquals(fileTree.getEntryObjectId(), objectId);
				// System.out.println("Resource name : "+
				// flexoResource.getName() + " id : "+ objectId.getName());
			}

		}
	}

	@Test
	@TestOrder(5)
	public void testSaveSeveralVersions() throws IOException {
		Repository gitRepository = gitResourceCenter.getGitRepository();
		Collection<FlexoResource<?>> ressources = gitResourceCenter.getAllResources();

		FlexoResource<?> resource = (FlexoResource<?>) ressources.toArray()[0];
		System.out.println("Ressource chosen : " + resource.getName());
		FlexoIOGitDelegate gitDelegate = (FlexoIOGitDelegate) resource.getFlexoIODelegate();
		LinkedList<ObjectId> listIds = (LinkedList<ObjectId>) gitDelegate.getGitObjectIds();
		FileTreeIterator fileTree = new FileTreeIterator(gitRepository);
		for (ObjectId objectId : listIds) {
			System.out.println("Add Id : " + objectId.getName());
			// Git put the same id if the content is the same so we need to
			// check the name of the entry
			while (!fileTree.getEntryObjectId().equals(objectId)
					|| !fileTree.getEntryFile().getName().contains(resource.getName())) {
				fileTree.next(1);
			}
			System.out.println("Current Entry file retrieved: " + fileTree.getEntryFile().getName() + " "
					+ fileTree.getEntryObjectId().getName());
			FileOutputStream stream = new FileOutputStream(fileTree.getEntryFile());
			System.out.println("Writing in the file : " + fileTree.getEntryFile().getName());
			stream.write(6777);
		}
		System.out.println("Saving modified resource : " + resource.getName());
		gitDelegate.save(resource);
		assertFalse(listIds.getFirst().equals(listIds.getLast()));

	}

	@Test
	@TestOrder(6)
	public void retrieveFlexoResourceFromGit() throws CorruptObjectException {

		PowerpointTechnologyAdapter adapter = gitResourceCenter.getTechnologyAdapterService()
				.getTechnologyAdapter(PowerpointTechnologyAdapter.class);

		Collection<FlexoResource<?>> ressources = gitResourceCenter.getAllResources();

		FlexoResource<?> resource = (FlexoResource<?>) ressources.toArray()[1];
		File fileToLoadResource = null;
		FlexoIOGitDelegate gitDelegate = (FlexoIOGitDelegate) resource.getFlexoIODelegate();
		LinkedList<ObjectId> listIds = (LinkedList<ObjectId>) gitDelegate.getGitObjectIds();
		FileTreeIterator fileTree = new FileTreeIterator(gitResourceCenter.getGitRepository());
		for (ObjectId objectId : listIds) {
			// Git put the same id if the content is the same so we need to
			// check the name of the entry
			while (!fileTree.getEntryObjectId().equals(objectId)
					|| !fileTree.getEntryFile().getName().contains(resource.getName())) {
				fileTree.next(1);
			}
			fileToLoadResource = fileTree.getEntryFile();
		}

		PowerpointSlideshowResource loadResource = adapter.retrieveSlideshowResource(fileToLoadResource,
				gitResourceCenter);
		assertNotNull(resource);
	}

	@Test
	@TestOrder(7)
	public void testCreateNewPowerpoint() throws Exception {
		logger.info("testCreateNewFile()");
		assertNotNull(modelRepository);

		PowerpointSlideshowResource modelRes;

		modelRes = createPowerpointResource("generated_File.ppt");
		FlexoIOGitDelegate gitDelegate = (FlexoIOGitDelegate) modelRes.getFlexoIODelegate();
		LinkedList<ObjectId> listIds = (LinkedList<ObjectId>) gitDelegate.getGitObjectIds();
		FileTreeIterator fileTree = new FileTreeIterator(gitResourceCenter.getGitRepository());
		for (ObjectId objectId : listIds) {
			// Git put the same id if the content is the same so we need to
			// check the name of the entry
			while (!fileTree.getEntryObjectId().equals(objectId)
					|| !fileTree.getEntryFile().getName().contains(modelRes.getName())) {
				fileTree.next(1);
			}
			System.out.println("New Entry file retrieved: " + fileTree.getEntryFile().getName() + " "
					+ fileTree.getEntryObjectId().getName());
			assertEquals(fileTree.getEntryObjectId(), objectId);
		}
		// Ask sylvain why perform super delete provokes an exception

		System.out.println("Version of model Res : " + modelRes.getVersion().toString());
		assertEquals(modelRes.getVersion().toString(), "0.1");

		// modelRes.delete();
		// pptFile.delete();

	}

	@Test
	@TestOrder(8)
	public void testVersionning() throws IOException {
		Collection<FlexoResource<?>> ressources = gitResourceCenter.getAllResources();
		FlexoResource<?> resourceToVersion = null;
		File fileToVersion = null;
		FlexoVersion firstVersion = null;
		for (FlexoResource<?> flexoResource : ressources) {
			if (flexoResource.getName().contains("generated_File.ppt")) {
				resourceToVersion = flexoResource;
				firstVersion = resourceToVersion.getVersion();
			}
		}

		FlexoIOGitDelegate gitDelegate = (FlexoIOGitDelegate) resourceToVersion.getFlexoIODelegate();
		LinkedList<ObjectId> listIds = (LinkedList<ObjectId>) gitDelegate.getGitObjectIds();
		FileTreeIterator fileTree = new FileTreeIterator(gitResourceCenter.getGitRepository());
		for (ObjectId objectId : listIds) {
			// Git put the same id if the content is the same so we need to
			// check the name of the entry
			while (!fileTree.getEntryObjectId().equals(objectId)
					|| !fileTree.getEntryFile().getName().contains(resourceToVersion.getName())) {
				fileTree.next(1);
			}
			fileToVersion = fileTree.getEntryFile();
			assertNotNull(fileToVersion);
		}
		FileOutputStream stream = new FileOutputStream(fileToVersion);
		stream.write(6777);

		resourceToVersion.getFlexoIODelegate().save(resourceToVersion);
		System.out.println(resourceToVersion.getVersion().toString());
		assertTrue(resourceToVersion.getVersion().isGreaterThan(firstVersion));

		FileOutputStream stream2 = new FileOutputStream(fileToVersion);
		stream2.write(123456);

		resourceToVersion.getFlexoIODelegate().save(resourceToVersion);
		System.out.println(resourceToVersion.getVersion().toString());
		assertTrue(resourceToVersion.getVersion().isGreaterThan(firstVersion));
	}

	@Test
	@TestOrder(9)
	public void matchDifferentsResourceVersion() throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException, MissingObjectException, IOException {
		List<FlexoResource<?>> resources = new ArrayList<>();

		// Prepare
		Git git = new Git(gitResourceCenter.getGitRepository());
		// Create 3 powerpoint resources
		for (int i = 0; i < 3; i++) {
			resources.add(createPowerpointResource("NewPPTGenerated" + i+".ppt"));
			File file = new File(gitResourceCenter.getGitRepository().getWorkTree(), "NewPPTGenerated" + i+".ppt");
			PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
			writer.println("Some content in the ppt" + i + " version 0.1");
			writer.flush();
		}
		// Save the first version
		for (FlexoResource<?> flexoResource : resources) {
			((FlexoIOGitDelegate) flexoResource.getFlexoIODelegate()).save(flexoResource);
		}

		// Write a second Version
		for (FlexoResource<?> flexoResource : resources) {
			File file = new File(gitResourceCenter.getGitRepository().getWorkTree(), flexoResource.getName());
			PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
			writer.println("Some second content in the ppt" + flexoResource.getName() + " version 0.2");
			writer.flush();
			((FlexoIOGitDelegate) flexoResource.getFlexoIODelegate()).save(flexoResource);
		}

		// Write a third Version
		for (FlexoResource<?> flexoResource : resources) {
			File file = new File(gitResourceCenter.getGitRepository().getWorkTree(), flexoResource.getName());
			PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
			writer.println("Some third content in the ppt" + flexoResource.getName() + " version 0.3");
			writer.flush();
			((FlexoIOGitDelegate) flexoResource.getFlexoIODelegate()).save(flexoResource);
		}
		Map<FlexoResource<?>,FlexoVersion> versionsToMatch = new HashMap<>();
		for(int i=0;i<resources.size();i++){
			FlexoVersion version = null;
			version = new FlexoVersion("0."+(i+2)+"RC0");
			versionsToMatch.put(resources.get(i), version);
		}
		// Synchronize Working Tree with the versions of the files we want to checkout
		gitResourceCenter.checkoutSeveralVersions(versionsToMatch, "AnotherWorkspace");
		
		FileTreeIterator iter = new FileTreeIterator(gitResourceCenter.getGitRepository());
		List<ObjectId> listOfIds = new LinkedList<>();
		iterateOnTheWorkTree(iter, "NewPPTGenerated", listOfIds);
		int i=1;
		for (ObjectId objectId : listOfIds) {
			ObjectLoader loader = gitResourceCenter.getGitRepository().open(objectId);
			String str = new String(loader.getBytes(),StandardCharsets.UTF_8);
			// Check that it is the good version of the file in the working tree
			assertTrue(str.contains("version 0."+i));
			i++;
			loader.copyTo(System.out);
		}
	}

	@Test
	@TestOrder(10)
	public void reloadEnvironmentFromGitRepository() throws NoHeadException, GitAPIException, IOException{
		Repository repositoryToLoad = gitResourceCenter.getGitRepository();
		gitResourceCenter.setGitRepository(emptyGitRepository);
		gitResourceCenter.getAllResources().clear();
		assertEquals(gitResourceCenter.getAllResources().size(),0);
		gitResourceCenter.loadFlexoEnvironment(repositoryToLoad);
		Collection<FlexoResource<?>> resources = gitResourceCenter.getAllResources();
		//Check
		for (FlexoResource<?> flexoResource : resources) {
			System.out.println("Resource instantiated : "+ flexoResource.getName());
			System.out.println("Version : "+ flexoResource.getVersion().toString());
			//Check that we have the good number of versions in the io delegate
			assertTrue(((FlexoIOGitDelegate)flexoResource.getFlexoIODelegate()).getGitCommitIds().keySet().size()==flexoResource.getVersion().minor);
		}
	}
	
	public PowerpointSlideshowResource createPowerpointResource(String name) {
		logger.info("creating powerpoint resource...");
		assertNotNull(modelRepository);

		PowerpointSlideshowResource modelRes;

		File pptFile = new File(gitResourceCenter.getGitRepository().getWorkTree(), name);
		modelRes = PowerpointSlideshowResourceImpl.makePowerpointSlideshowResource(pptFile.getAbsolutePath(), pptFile,
				powerpointAdapter.getTechnologyContextManager(), gitResourceCenter);
		((FlexoIOGitDelegate) modelRes.getFlexoIODelegate()).createAndSaveIO(modelRes);

		// Register the new resource in the gitResourceCenter
		gitResourceCenter.registerResource(modelRes);
		return modelRes;
	}

	private void iterateOnTheWorkTree(FileTreeIterator fileTree, String path,List<ObjectId> list) throws CorruptObjectException {
		while(!fileTree.eof()){
			
			if (!fileTree.getEntryFile().getName().contains(path)) {
				System.out.println(fileTree.getEntryPathString());
				fileTree.next(1);
			}
			else {
				System.out.println(fileTree.getEntryPathString());
				list.add(fileTree.getEntryObjectId());
				fileTree.next(1);
			}
		}
		
	}
	
	/**
	 * Load powerpoint viewpoint
	 * 
	 * @throws IOException
	 */

	private ViewPoint loadViewPoint(String viewPointURI) {

		log("Testing ViewPoint loading: " + viewPointURI);
		System.out.println("resourceCenter=" + resourceCenter);
		System.out.println("resourceCenter.getViewPointRepository()=" + resourceCenter.getViewPointRepository());
		ViewPointResource viewPointResource = testApplicationContext.getViewPointLibrary()
				.getViewPointResource(viewPointURI);
		assertNotNull(viewPointResource);
		ViewPoint viewPoint = viewPointResource.getViewPoint();
		assertTrue(viewPointResource.isLoaded());
		return viewPoint;
	}
}
