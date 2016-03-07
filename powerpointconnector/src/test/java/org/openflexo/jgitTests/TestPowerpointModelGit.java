package org.openflexo.jgitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.junit.Ignore;
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
import org.openflexo.gitUtils.GitFile;
import org.openflexo.gitUtils.GitIODelegateFactory;
import org.openflexo.gitUtils.SerializationArtefactFile;
import org.openflexo.gitUtils.SerializationArtefactKind;
import org.openflexo.gitUtils.Workspace;
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
	private static GitResourceCenter emptyGitResourceCenter;
	private static List<GitResourceCenter> gitResourceCenters = new ArrayList<>();
	private static Workspace workspace = new Workspace();

	/**
	 * Instantiate test resource center
	 * 
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
	public void test0InstantiateResourceCenter()
			throws NoHeadException, NoMessageException, UnmergedPathsException, ConcurrentRefUpdateException,
			WrongRepositoryStateException, AbortedByHookException, GitAPIException, IllegalStateException, IOException {

		log("test0InstantiateResourceCenter()");
		testApplicationContext = instanciateTestServiceManager(false);
		assertNotNull(testApplicationContext);
		powerpointAdapter = testApplicationContext.getTechnologyAdapterService()
				.getTechnologyAdapter(PowerpointTechnologyAdapter.class);
		assertNotNull(powerpointAdapter);
		for (FlexoResourceCenter rc : testApplicationContext.getResourceCenterService().getResourceCenters()) {
			if (rc.getRepository(PowerpointSlideShowRepository.class, powerpointAdapter) != null) {
				if (!((GitResourceCenter) rc).getAllResources().isEmpty()) {
					modelRepository = (PowerpointSlideShowRepository) rc
							.getRepository(PowerpointSlideShowRepository.class, powerpointAdapter);
				}
			}
			if (rc instanceof GitResourceCenter) {
				// Put the resource center with some resources in the field
				// gitResourceCenter
				if (!((GitResourceCenter) rc).getAllResources().isEmpty()) {
					gitResourceCenter = (GitResourceCenter) rc;
				}
				// And put the empty one in the emptyGitResourceCenter
				else {
					emptyGitResourceCenter = (GitResourceCenter) rc;
				}
				gitResourceCenters.add((GitResourceCenter) rc);
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
	 * Instanciate viewpoint
	 * 
	 * @throws IOException
	 * @throws SaveResourceException 
	 */
	@Test
	@TestOrder(2)
	public void testCreatePowerpointViewpoint() throws IOException, SaveResourceException {
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
		gitResourceCenter.registerResource(newViewPoint.getViewPointResource());
		// Serialize the viewpoint resource in the git repo
		newViewPoint.getViewPointResource().save(null);
		VirtualModel newVirtualModel = null;
		try {
			newVirtualModel = VirtualModelImpl.newGitVirtualModel("TestPPTVirtualModel", newViewPoint);
			newVirtualModel.getResource().save(null);
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
			//newViewPoint.getResource().save(null);
			//newVirtualModel.getResource().save(null);
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
	public void retrieveFileInGitRepository() throws NoWorkTreeException, IOException, ModelDefinitionException, SaveResourceException {
		Collection<FlexoResource<?>> ressources = gitResourceCenter.getAllResources();
		for (FlexoResource<?> flexoResource : ressources) {
			if (flexoResource instanceof PowerpointSlideshowResource) {
				
//				flexoResource.setFlexoIODelegate(gitResourceCenter.getDelegateFactory()
//						.makeIODelegateNewInstance(flexoResource, new SerializationArtefactFile()));
				
				flexoResource.save(null);
				
				FlexoIOGitDelegate gitDelegate = (FlexoIOGitDelegate) flexoResource.getFlexoIODelegate();
				System.out.println("Ressource " + flexoResource.getName() + " saved");
				assertTrue(gitDelegate.getGitCommitIds().size() == 1);
			}

		}
	}

	@Test
	@TestOrder(5)
	public void testSaveSeveralVersions() throws IOException, SaveResourceException {
		Repository gitRepository = gitResourceCenter.getGitRepository();
		Collection<FlexoResource<?>> ressources = gitResourceCenter.getAllResources();

		FlexoResource<?> resource = null;

		for (FlexoResource<?> flexoResource : ressources) {
			if (flexoResource.getName().equals("TestPPT1.ppt")) {
				resource = flexoResource;
			}
		}
		System.out.println("Ressource chosen : " + resource.getName());
		FlexoIOGitDelegate gitDelegate = (FlexoIOGitDelegate) resource.getFlexoIODelegate();
		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(gitDelegate.getFile()));
		stream.write("balbaaba".getBytes());
		stream.flush();
		resource.save(null);
		assertTrue(resource.getVersion().equals(new FlexoVersion("0.2RC0")));
		FileTreeIterator iter = new FileTreeIterator(gitRepository);
		File versionFile = new File(((FlexoIOGitDelegate)resource.getFlexoIODelegate()).getFile().getAbsolutePath()+".version");
		stream.close();
		BufferedReader reader = new BufferedReader(new FileReader(versionFile));
		int numberVersion = 0;
		while (reader.readLine() != null) {
			numberVersion++;
		}
		assertTrue(numberVersion == 2);
		reader.close();
	}

	

	@Test
	@TestOrder(6)
	public void testCreateNewPowerpoint() throws Exception {
		logger.info("testCreateNewFile()");
		assertNotNull(modelRepository);

		PowerpointSlideshowResource modelRes;

		modelRes = createPowerpointResource("generated_File.ppt", gitResourceCenter);
		FileTreeIterator fileTree = new FileTreeIterator(gitResourceCenter.getGitRepository());
		FlexoIOGitDelegate delegate = (FlexoIOGitDelegate) modelRes.getFlexoIODelegate();
		while (!fileTree.getEntryFile().getName().equals(delegate.getFile().getName())) {
			fileTree.next(1);
		}
		System.out.println("New Entry file retrieved: " + fileTree.getEntryFile().getName() + " "
				+ fileTree.getEntryObjectId().getName());
		// Ask sylvain why perform super delete provokes an exception

		System.out.println("Version of model Res : " + modelRes.getVersion().toString());
		assertEquals(modelRes.getVersion().toString(), "0.1");

		// modelRes.delete();
		// pptFile.delete();

	}
	
	
	@Test
	@TestOrder(7)
	public void retrieveFlexoResourceFromGit() throws CorruptObjectException {

		PowerpointTechnologyAdapter adapter = gitResourceCenter.getTechnologyAdapterService()
				.getTechnologyAdapter(PowerpointTechnologyAdapter.class);

		Collection<FlexoResource<?>> ressources = gitResourceCenter.getAllResources();

		FlexoResource<?> resource = null;

		for (FlexoResource<?> flexoResource : ressources) {
			if (flexoResource.getName().equals("generated_File.ppt")) {
				resource = flexoResource;
			}
		}
		File fileToLoadResource = null;
		FlexoIOGitDelegate gitDelegate = (FlexoIOGitDelegate) resource.getFlexoIODelegate();
		FileTreeIterator fileTree = new FileTreeIterator(gitResourceCenter.getGitRepository());
		// Git put the same id if the content is the same so we need to
		// check the name of the entry
		while (!fileTree.getEntryFile().getName() .equals(gitDelegate.getFile().getName())) {
			fileTree.next(1);
		}
		fileToLoadResource = fileTree.getEntryFile();

		PowerpointSlideshowResource loadResource = adapter.retrieveSlideshowResource(fileToLoadResource,
				gitResourceCenter);
		assertNotNull(loadResource);
	}

	@Test
	@TestOrder(8)
	public void testVersionning() throws IOException, SaveResourceException {
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
		FileTreeIterator fileTree = new FileTreeIterator(gitResourceCenter.getGitRepository());

		while (!fileTree.getEntryFile().getName().equals(gitDelegate.getFile().getName())) {
			fileTree.next(1);
		}
		fileToVersion = fileTree.getEntryFile();
		assertNotNull(fileToVersion);

		FileOutputStream stream = new FileOutputStream(fileToVersion);
		stream.write(6777);
		stream.close();
		resourceToVersion.save(null);
		System.out.println(resourceToVersion.getVersion().toString());
		assertTrue(resourceToVersion.getVersion().isGreaterThan(firstVersion));

		FileOutputStream stream2 = new FileOutputStream(fileToVersion);
		stream2.write(123456);
		stream2.close();
		resourceToVersion.save(null);
		System.out.println(resourceToVersion.getVersion().toString());
		assertTrue(resourceToVersion.getVersion().isGreaterThan(firstVersion));
	}

	@Test
	@TestOrder(9)
	public void matchDifferentsResourceVersion() throws RefAlreadyExistsException, RefNotFoundException,
			InvalidRefNameException, CheckoutConflictException, GitAPIException, MissingObjectException, IOException, SaveResourceException {
		List<FlexoResource<?>> resources = new ArrayList<>();

		// Prepare
		// Create 3 powerpoint resources
		for (int i = 0; i < 3; i++) {
			resources.add(createPowerpointResource("NewPPTGenerated" + i + ".ppt", gitResourceCenter));
			File file = new File(gitResourceCenter.getGitRepository().getWorkTree(), "NewPPTGenerated" + i + ".ppt");
			PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
			writer.println("Some content in the ppt" + "NewPPTGenerated" + i + " version 0.1");
			writer.flush();
			writer.close();
		}
		// Save the first version
		for (FlexoResource<?> flexoResource : resources) {
			flexoResource.save(null);
		}

		// Write a second Version
		for (FlexoResource<?> flexoResource : resources) {
			File file = new File(gitResourceCenter.getGitRepository().getWorkTree(), flexoResource.getName());
			PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
			writer.println("Some second content in the ppt" + flexoResource.getName() + " version 0.2");
			writer.flush();
			writer.close();
			flexoResource.save(null);
		}

		// Write a third Version
		for (FlexoResource<?> flexoResource : resources) {
			File file = new File(gitResourceCenter.getGitRepository().getWorkTree(), flexoResource.getName());
			PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
			writer.println("Some third content in the ppt" + flexoResource.getName() + " version 0.3");
			writer.flush();
			writer.close();
			flexoResource.save(null);
		}
		Map<FlexoResource<?>, FlexoVersion> versionsToMatch = new HashMap<>();
		for (int i = 0; i < resources.size(); i++) {
			FlexoVersion version = null;
			version = new FlexoVersion("0." + (i + 2) + "RC0");
			versionsToMatch.put(resources.get(i), version);
		}
		// Synchronize Working Tree with the versions of the files we want to
		// checkout
		gitResourceCenter.checkoutSeveralVersions(versionsToMatch, "AnotherWorkspace");

		FileTreeIterator iter = new FileTreeIterator(gitResourceCenter.getGitRepository());
		List<ObjectId> listOfIds = new LinkedList<>();
		iterateOnTheWorkTree(iter, "NewPPTGenerated", listOfIds);
		int i = 1;
		for (ObjectId objectId : listOfIds) {
			ObjectLoader loader = gitResourceCenter.getGitRepository().open(objectId);
			String str = new String(loader.getBytes(), StandardCharsets.UTF_8);
			// Check that it is the good version of the file in the working tree
			assertTrue(str.contains("version 0." + i));
			i++;
			loader.copyTo(System.out);
		}
		for (FlexoResource<?> resource : gitResourceCenter.getAllResources()) {
			if (resource.getVersion() != null) {
				System.out.println(
						"Version after checkout " + resource.getName() + " " + resource.getVersion().toString());
			}
		}
	}

	@Test
	@TestOrder(10)
	public void reloadEnvironmentFromGitRepository() throws NoHeadException, GitAPIException, IOException {
		Repository repositoryToLoad = gitResourceCenter.getGitRepository();
		gitResourceCenter.getAllResources().clear();
		assertEquals(gitResourceCenter.getAllResources().size(), 0);
		gitResourceCenter.loadFlexoEnvironment(repositoryToLoad);
		Collection<FlexoResource<?>> resources = gitResourceCenter.getAllResources();
		// Check
		for (FlexoResource<?> flexoResource : resources) {
			System.out.println("Resource instantiated : " + flexoResource.getName());
			System.out.println("Version : " + flexoResource.getVersion().toString());
			// Check that we have the good number of versions in the io delegate
			assertTrue(((FlexoIOGitDelegate) flexoResource.getFlexoIODelegate()).getGitCommitIds().keySet()
					.size() == flexoResource.getVersion().minor);
		}
		// Check that the versions are registred in the resources we have
		// instanciated
		Map<FlexoResource<?>, FlexoVersion> versionsToMatch = new HashMap<>();
		for (FlexoResource<?> flexoResource : resources) {
			if (flexoResource.getName().contains("NewPPTGenerated") && !flexoResource.getName().contains("version")) {
				versionsToMatch.put(flexoResource, new FlexoVersion("0.3RC0"));
			}
		}
		// Synchronize Working Tree with the versions of the files we want to
		// checkout
		gitResourceCenter.checkoutSeveralVersions(versionsToMatch, "AnotherWorkspace");

		FileTreeIterator iter = new FileTreeIterator(gitResourceCenter.getGitRepository());
		List<ObjectId> listOfIds = new LinkedList<>();
		iterateOnTheWorkTree(iter, "NewPPTGenerated", listOfIds);
		for (ObjectId objectId : listOfIds) {
			ObjectLoader loader = gitResourceCenter.getGitRepository().open(objectId);
			String str = new String(loader.getBytes(), StandardCharsets.UTF_8);
			// Check that it is the good version of the file in the working tree
			assertTrue(str.contains("version 0.2"));
			loader.copyTo(System.out);
		}
	}

	@Test
	@TestOrder(11)
	public void createWorkspaceWithOneRepo()
			throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException,
			MissingObjectException, IncorrectObjectTypeException, GitAPIException, IOException {
		Workspace workspace = new Workspace();
		workspace.setGitResourceCenterList(gitResourceCenters);

		Map<String, FlexoVersion> resourcesToWorkOn = new HashMap<>();
		resourcesToWorkOn.put("NewPPTGenerated0.ppt", new FlexoVersion("0.2RC0"));
		resourcesToWorkOn.put("NewPPTGenerated1.ppt", new FlexoVersion("0.3RC0"));
		resourcesToWorkOn.put("NewPPTGenerated2.ppt", new FlexoVersion("0.4RC0"));

		workspace.checkoutResources(resourcesToWorkOn);

		// Check
		assertTrue(workspace.getResourcesOnWorking().size() == 3);
		// Check that git repository has synchronized his working tree
		FileTreeIterator iter = new FileTreeIterator(gitResourceCenter.getGitRepository());
		List<ObjectId> listOfIds = new LinkedList<>();
		iterateOnTheWorkTree(iter, "NewPPTGenerated", listOfIds);
		for (ObjectId objectId : listOfIds) {
			ObjectLoader loader = gitResourceCenter.getGitRepository().open(objectId);
			String str = new String(loader.getBytes(), StandardCharsets.UTF_8);
			// Check that it is the good version of the file in the working tree
			if (str.contains("NewPPTGenerated0")) {
				assertTrue(str.contains("version 0.1"));
			} else if (str.contains("NewPPTGenerated1")) {
				assertTrue(str.contains("version 0.2"));
			} else if (str.contains("NewPPTGenerated2")) {
				assertTrue(str.contains("version 0.3"));
			}
			loader.copyTo(System.out);
		}

	}

	@Test
	@TestOrder(12)
	public void workspaceWithTwoRepos()
			throws InvalidRemoteException, TransportException, GitAPIException, IOException, SaveResourceException {
		// Prepare
		File source = new File(gitResourceCenter.getGitRepository().getWorkTree(), "NewPPTGenerated2.ppt");
		File target = new File(emptyGitResourceCenter.getGitRepository().getWorkTree(), "NewPPTGenerated2.ppt");
		Files.copy(source.toPath(), target.toPath());
		FlexoResource<?> resource = gitResourceCenter.retrieveResource("file:" + source.getAbsolutePath(), null);
		if(resource != null){
			resource.setResourceCenter(emptyGitResourceCenter);			
		}
		//Same hack as for the DocX Resource.... Why the retrieve resource method register something???
		emptyGitResourceCenter.getAllResources().clear();
		////////////////////
		assertTrue(emptyGitResourceCenter.getAllResources().size()==0);

		emptyGitResourceCenter.registerResource(resource);
		Collection<FlexoResource<?>> resources = emptyGitResourceCenter.getAllResources();
		for (FlexoResource<?> flexoResource : resources) {
			System.out.println("URI RESOURCE " + flexoResource.getURI());
		}
		assertTrue(resource != null);
		System.out.println("VERSION FOUND" + resource.getVersion().toString());
		// Create some content
		createPowerpointResource("NewPPTGenerated4.ppt", emptyGitResourceCenter);
		// Update some existing content
		for (FlexoResource<?> flexoResource : emptyGitResourceCenter.getAllResources()) {
			if (flexoResource.getName().equals("NewPPTGenerated2.ppt")) {
				File file = new File(emptyGitResourceCenter.getGitRepository().getWorkTree(), flexoResource.getName());
				PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
				writer.append("\n" + flexoResource.getName() + " version 0.4");
				writer.flush();
				writer.close();
				
				FlexoIOGitDelegate gitDelegatePPT2= (FlexoIOGitDelegate) flexoResource.getFlexoIODelegate();
				SerializationArtefactFile fileSerialization = new SerializationArtefactFile();
				fileSerialization.setAbsolutePath(file.getAbsolutePath());
				// Must decide how to determine that the resource has been copied without a version file...
				gitDelegatePPT2.setResourceVersionFile(null);
				//////////////////////////////////////////////
				gitDelegatePPT2.setSerializationArtefactKind(fileSerialization);
				gitDelegatePPT2.setFile(target);
				// THERE IS A BUG WITH THE CONTENT OF THE PPT, it cannot READ the entire HEADER, I ASSUME IT COMES FROM THE RANDOM BYTES
				// USED TO MODIFY THE CONTENT WHEREAS A TRUE PPT CONTENT IS EXPECTED....
				flexoResource.save(null);
			}
		}
		Workspace aWorkspace = new Workspace();
		aWorkspace.setGitResourceCenterList(gitResourceCenters);

		Map<String, FlexoVersion> resourcesToWorkOn = new HashMap<>();
		resourcesToWorkOn.put("NewPPTGenerated2.ppt", new FlexoVersion("0.5RC0"));
		resourcesToWorkOn.put("NewPPTGenerated1.ppt", new FlexoVersion("0.4RC0"));
		resourcesToWorkOn.put("NewPPTGenerated4.ppt", new FlexoVersion("0.1"));
		aWorkspace.checkoutResources(resourcesToWorkOn);
		// Check
		assertTrue(aWorkspace.getResourcesOnWorking().size() == 3);
		// Check if the version of the resource is the good one
		for (FlexoResource<?> flexoResource : workspace.getResourcesOnWorking().keySet()) {
			if (flexoResource.getName().equals("NewPPTGenerated1.ppt")) {
				assertTrue(flexoResource.getVersion().equals(new FlexoVersion("0.4RC0")));
			} else if (flexoResource.getName().equals("NewPPTGenerated2.ppt")) {
				assertTrue(flexoResource.getVersion().equals(new FlexoVersion("0.5RC0")));
			} else if (flexoResource.getName().equals("NewPPTGenerated4.ppt")) {
				assertTrue(flexoResource.getVersion().equals(new FlexoVersion("0.1")));
			}
		}

		// Check if the repos are synchronized with the workspace
		FileTreeIterator iter = new FileTreeIterator(gitResourceCenter.getGitRepository());
		List<ObjectId> listOfIds = new LinkedList<>();
		iterateOnTheWorkTree(iter, "NewPPTGenerated", listOfIds);
		for (ObjectId objectId : listOfIds) {
			ObjectLoader loader = gitResourceCenter.getGitRepository().open(objectId);
			String str = new String(loader.getBytes(), StandardCharsets.UTF_8);
			// Check that it is the good version of the file in the working tree
			if (str.contains("NewPPTGenerated0")) {
				assertTrue(str.contains("version 0.1"));
			} else if (str.contains("NewPPTGenerated1")) {
				// Check that the repository has changed
				assertTrue(str.contains("version 0.3"));
			} else if (str.contains("NewPPTGenerated2")) {
				assertTrue(str.contains("version 0.3"));
			}
			loader.copyTo(System.out);
		}

		FileTreeIterator iter2 = new FileTreeIterator(emptyGitResourceCenter.getGitRepository());
		List<ObjectId> listOfIds2 = new LinkedList<>();
		iterateOnTheWorkTree(iter2, "NewPPTGenerated", listOfIds2);
		for (ObjectId objectId : listOfIds2) {
			ObjectLoader loader = emptyGitResourceCenter.getGitRepository().open(objectId);
			String str = new String(loader.getBytes(), StandardCharsets.UTF_8);
			// Check that it is the good version of the file in the working tree
			if (str.contains("NewPPTGenerated4")) {
				assertTrue(str.isEmpty());
			} else if (str.contains("NewPPTGenerated2")) {
				assertTrue(str.contains("version 0.4"));
			}
			loader.copyTo(System.out);
		}

		// For the next test
		workspace = aWorkspace;
	}

	@Test
	@TestOrder(13)
	public void saveWorkspace() throws NoWorkTreeException, GitAPIException {
		workspace.commitWorkspace(workspace.getResourcesOnWorking());
		for (GitResourceCenter resourceCenter : workspace.getGitResourceCenterList()) {
			Git git = new Git(resourceCenter.getGitRepository());
			Set<String> uncommittedchange = git.status().call().getUncommittedChanges();
			for (String string : uncommittedchange) {
				assertTrue(!string.contains("NewPPTGenerated"));
			}
			git.close();
		}

	}

	@Ignore
	@Test
	@TestOrder(14)
	public void initializeResourceCenter() throws IOException {
		Repository repositoryToLoad = gitResourceCenter.getGitRepository();
		gitResourceCenter.getAllResources().clear();
		assertEquals(gitResourceCenter.getAllResources().size(), 0);
		gitResourceCenter.initializeResources(repositoryToLoad);

	}

	
	@Test
	@TestOrder(15)
	public void putVersionsInCache(){
		gitResourceCenter.putVersionInCache("NewPPTGenerated1.ppt", new FlexoVersion("0.3RC0"));
		assertTrue(gitResourceCenter.getCache().size()==1);
		List<GitFile> cache = gitResourceCenter.getCache();
		assertTrue(cache.get(0).getVersion().equals(new FlexoVersion("0.3RC0")));
		assertTrue(cache.get(0).getFileInRepository().getName().equals("NewPPTGenerated1.ppt"));
		
		gitResourceCenter.putVersionInCache("NewPPTGenerated1.ppt", new FlexoVersion("0.4RC0"));
		assertTrue(gitResourceCenter.getCache().size()==2);
		assertTrue(cache.get(1).getVersion().equals(new FlexoVersion("0.4RC0")));
		assertTrue(cache.get(1).getFileInRepository().getName().equals("NewPPTGenerated1.ppt"));
	}
	
	public PowerpointSlideshowResource createPowerpointResource(String name, GitResourceCenter gitResourceCenter) throws SaveResourceException {
		logger.info("creating powerpoint resource...");
		assertNotNull(modelRepository);

		PowerpointSlideshowResource modelRes;

		File pptFile = new File(gitResourceCenter.getGitRepository().getWorkTree(), name);
		modelRes = PowerpointSlideshowResourceImpl.makePowerpointSlideshowResource(pptFile.getAbsolutePath(), pptFile,
				powerpointAdapter.getTechnologyContextManager(), gitResourceCenter);
		modelRes.save(null);
		// Register the new resource in the gitResourceCenter
		gitResourceCenter.registerResource(modelRes);
		return modelRes;
	}

	private void iterateOnTheWorkTree(FileTreeIterator fileTree, String path, List<ObjectId> list)
			throws CorruptObjectException {
		while (!fileTree.eof()) {

			if (!fileTree.getEntryFile().getName().contains(path)
					|| fileTree.getEntryFile().getName().endsWith(".version")) {
				System.out.println(fileTree.getEntryPathString());
				fileTree.next(1);
			} else {
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
