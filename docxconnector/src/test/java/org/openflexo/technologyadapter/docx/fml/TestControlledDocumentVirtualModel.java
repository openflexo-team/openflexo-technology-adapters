/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.fml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.doc.FlexoDocFragment.FragmentConsistencyException;
import org.openflexo.foundation.doc.fml.FragmentActorReference;
import org.openflexo.foundation.doc.fml.FragmentActorReference.ElementReference;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateTechnologyRole;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rm.VirtualModelResourceFactory;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionType;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.DefaultModelSlotInstanceConfigurationOption;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResource;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.technologyadapter.docx.DocXModelSlot;
import org.openflexo.technologyadapter.docx.DocXModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.fml.editionaction.AddDocXFragment;
import org.openflexo.technologyadapter.docx.fml.editionaction.AddDocXFragment.LocationSemantics;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFragment;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;
import org.openflexo.technologyadapter.docx.model.IdentifierManagementStrategy;
import org.openflexo.technologyadapter.docx.nature.FMLControlledDocXVirtualModelInstanceNature;
import org.openflexo.technologyadapter.docx.nature.FMLControlledDocXVirtualModelNature;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the creation and some manipulations of a {@link VirtualModel} with {@link FMLControlledDocXVirtualModelNature}<br>
 * We basically test here the generation of a plain document from a template, and the adding of a fragment at the end of the document
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestControlledDocumentVirtualModel extends AbstractTestDocX {

	private final String VIEWPOINT_NAME = "TestViewPointControlledDocument";
	private final String VIEWPOINT_URI = "http://openflexo.org/test/TestResourceCenter/TestViewPointControlledDocument.fml";
	public static final String VIRTUAL_MODEL_NAME = "TestVirtualModel";

	public static DocXTechnologyAdapter technologicalAdapter;
	public static DocXDocumentRepository repository;
	public static FMLRTVirtualModelInstance newView;
	public static FMLRTVirtualModelInstance newVirtualModelInstance;

	public static DocXDocumentResource templateResource;
	public static DocXDocument templateDocument;
	public static DocXDocument generatedDocument;

	public static VirtualModel viewPoint;
	public static VirtualModelResource viewPointResource;
	public static DocXModelSlot docXModelSlot;
	public static DocXFragmentRole fragmentRole;
	public static VirtualModel virtualModel;
	public static ActionScheme actionScheme;

	private static FlexoResourceCenter<?> docXResourceCenter;
	private static DirectoryResourceCenter newResourceCenter;

	@AfterClass
	public static void tearDownClass() {

		technologicalAdapter = null;
		repository = null;
		newView = null;
		newVirtualModelInstance = null;

		templateResource = null;
		templateDocument = null;
		generatedDocument = null;

		viewPoint = null;
		viewPointResource = null;
		docXModelSlot = null;
		fragmentRole = null;
		virtualModel = null;
		actionScheme = null;

		deleteProject();
		deleteTestResourceCenters();
		unloadServiceManager();
	}

	/**
	 * Initialize an environment with DocX technology adapter, perform some checks
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(1)
	public void testInitialize() throws IOException {

		log("testInitialize()");

		instanciateTestServiceManagerForDocX(IdentifierManagementStrategy.ParaId);

		docXResourceCenter = serviceManager.getResourceCenterService().getFlexoResourceCenter("http://openflexo.org/docx-test");
		assertNotNull(docXResourceCenter);

		newResourceCenter = makeNewDirectoryResourceCenter();
		assertNotNull(newResourceCenter);

		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class);

		repository = technologicalAdapter.getDocXDocumentRepository(docXResourceCenter);

		_editor = new FlexoTestEditor(null, serviceManager);

		assertNotNull(serviceManager);
		assertNotNull(technologicalAdapter);
		assertNotNull(repository);

	}

	/**
	 * Test docx template loading
	 * 
	 * @throws FlexoException
	 * @throws ResourceLoadingCancelledException
	 * @throws FileNotFoundException
	 */
	@Test
	@TestOrder(2)
	public void testLoadTemplate() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("testLoadTemplate()");

		templateResource = getDocumentResource("StructuredDocument.docx");

		assertNotNull(templateDocument = templateResource.getResourceData(null));

		assertEquals(13, templateDocument.getElements().size());

	}

	/**
	 * Internal method used to retrieve in test resource center a docx resource identified by document name<br>
	 * Also assume this resource will be loaded
	 * 
	 * @param documentName
	 * @return
	 * @throws FileNotFoundException
	 * @throws ResourceLoadingCancelledException
	 * @throws FlexoException
	 */
	/*
	 * @Override private DocXDocumentResource getDocument(String documentName)
	 * throws FileNotFoundException, ResourceLoadingCancelledException,
	 * FlexoException {
	 * 
	 * for (FlexoResource<?> r : resourceCenter.getAllResources()) {
	 * System.out.println("Resource " + r + " uri=" + r.getURI()); }
	 * 
	 * String documentURI = resourceCenter.getDefaultBaseURI() + File.separator
	 * + documentName; System.out.println("Searching " + documentURI);
	 * 
	 * DocXDocumentResource documentResource = (DocXDocumentResource)
	 * serviceManager.getResourceManager().getResource(documentURI, null,
	 * DocXDocument.class); assertNotNull(documentResource);
	 * 
	 * DocXDocument document = documentResource.getResourceData(null);
	 * assertNotNull(document);
	 * assertNotNull(document.getWordprocessingMLPackage());
	 * 
	 * return documentResource; }
	 */

	/**
	 * Create a brand new _project
	 */
	@Test
	@TestOrder(4)
	public void testCreateProject() {
		_editor = createProject("TestProject");
		_project = _editor.getProject();
		System.out.println("Created _project " + _project.getProjectDirectory());
		assertTrue(_project.getProjectDirectory().exists());
		assertTrue(_project.getProjectDataResource().getIODelegate().exists());
	}

	/**
	 * Creates a new empty ViewPoint in the _project
	 * 
	 * @throws ModelDefinitionException
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(5)
	public void testCreateViewPoint() throws SaveResourceException, ModelDefinitionException {

		log("testCreateViewPoint()");

		FMLTechnologyAdapter fmlTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(FMLTechnologyAdapter.class);
		VirtualModelResourceFactory factory = fmlTechnologyAdapter.getVirtualModelResourceFactory();

		viewPointResource = factory.makeTopLevelVirtualModelResource(VIEWPOINT_NAME, VIEWPOINT_URI,
				fmlTechnologyAdapter.getGlobalRepository(newResourceCenter).getRootFolder(),
				fmlTechnologyAdapter.getTechnologyContextManager(), true);
		viewPoint = viewPointResource.getLoadedResourceData();

		// viewPoint = ViewPointImpl.newViewPoint(VIEWPOINT_NAME, VIEWPOINT_URI,
		// _project.getDirectory(),
		// serviceManager.getViewPointLibrary(),
		// resourceCenter);
		// viewPointResource = (ViewPointResource) viewPoint.getResource();
		// assertTrue(viewPointResource.getDirectory().exists());
		assertTrue(viewPointResource.getDirectory() != null);
		assertTrue(viewPointResource.getIODelegate().exists());

	}

	/**
	 * Test the VirtualModel creation<br>
	 * We create here a VirtualModel with a unique {@link DocXModelSlot}, configured with template docx Then we define a fragment role on
	 * the {@link VirtualModel}<br>
	 * We also define an {@link ActionScheme} on the {@link VirtualModel}, which generate the docx document from the template and then add a
	 * fragment to the end of document
	 * 
	 * @throws FragmentConsistencyException
	 * @throws ModelDefinitionException
	 */
	@Test
	@TestOrder(6)
	public void testCreateVirtualModel() throws SaveResourceException, FragmentConsistencyException, ModelDefinitionException {

		log("testCreateVirtualModel()");

		FMLTechnologyAdapter fmlTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(FMLTechnologyAdapter.class);
		VirtualModelResourceFactory factory = fmlTechnologyAdapter.getVirtualModelResourceFactory();
		VirtualModelResource newVMResource = factory.makeContainedVirtualModelResource(VIRTUAL_MODEL_NAME,
				viewPoint.getVirtualModelResource(), fmlTechnologyAdapter.getTechnologyContextManager(), true);
		virtualModel = newVMResource.getLoadedResourceData();

		// virtualModel = VirtualModelImpl.newVirtualModel("TestVirtualModel",
		// viewPoint);
		assertTrue(ResourceLocator.retrieveResourceAsFile(((VirtualModelResource) virtualModel.getResource()).getDirectory()).exists());
		assertTrue(((VirtualModelResource) virtualModel.getResource()).getIODelegate().exists());

		docXModelSlot = technologicalAdapter.makeModelSlot(DocXModelSlot.class, virtualModel);
		docXModelSlot.setTemplateResource(templateResource);
		docXModelSlot.setName("document");
		assertNotNull(docXModelSlot);

		virtualModel.addToModelSlots(docXModelSlot);
		assertTrue(virtualModel.getModelSlots(DocXModelSlot.class).size() == 1);

		// FlexoConcept flexoConcept =
		// virtualModel.getFMLModelFactory().newInstance(FlexoConcept.class);
		// virtualModel.addToFlexoConcepts(flexoConcept);

		CreateTechnologyRole createFragmentRole = CreateTechnologyRole.actionType.makeNewAction(virtualModel, null, _editor);
		createFragmentRole.setRoleName("fragment");
		createFragmentRole.setFlexoRoleClass(DocXFragmentRole.class);
		createFragmentRole.doAction();
		assertTrue(createFragmentRole.hasActionExecutionSucceeded());

		fragmentRole = (DocXFragmentRole) createFragmentRole.getNewFlexoRole();
		// FMLModelFactory factory = virtualModel.getFMLModelFactory();

		DocXParagraph startParagraph = (DocXParagraph) templateDocument.getElements().get(7);
		DocXParagraph endParagraph = (DocXParagraph) templateDocument.getElements().get(11);

		System.out.println("start=" + startParagraph.getRawText());
		System.out.println("end=" + endParagraph.getRawText());

		DocXFragment fragment = (DocXFragment) templateResource.getFactory().makeFragment(startParagraph, endParagraph);
		fragmentRole.setFragment(fragment);

		assertEquals(fragmentRole.getFragment(), fragment);

		CreateFlexoBehaviour createActionScheme = CreateFlexoBehaviour.actionType.makeNewAction(virtualModel, null, _editor);
		createActionScheme.setFlexoBehaviourName("generate");
		createActionScheme.setFlexoBehaviourClass(ActionScheme.class);
		createActionScheme.doAction();
		assertTrue(createActionScheme.hasActionExecutionSucceeded());
		actionScheme = (ActionScheme) createActionScheme.getNewFlexoBehaviour();

		CreateEditionAction createGenerateDocXDocumentAction = CreateEditionAction.actionType.makeNewAction(actionScheme.getControlGraph(),
				null, _editor);
		// createAddShape.actionChoice =
		// CreateEditionActionChoice.ModelSlotSpecificAction;
		createGenerateDocXDocumentAction.setModelSlot(docXModelSlot);
		createGenerateDocXDocumentAction.setEditionActionClass(GenerateDocXDocument.class);
		createGenerateDocXDocumentAction.doAction();
		assertTrue(createGenerateDocXDocumentAction.hasActionExecutionSucceeded());

		CreateEditionAction createFragmentAction = CreateEditionAction.actionType.makeNewAction(actionScheme.getControlGraph(), null,
				_editor);
		// createAddShape.actionChoice =
		// CreateEditionActionChoice.ModelSlotSpecificAction;
		createFragmentAction.setModelSlot(docXModelSlot);
		createFragmentAction.setEditionActionClass(AddDocXFragment.class);
		createFragmentAction.setAssignation(new DataBinding<>(fragmentRole.getRoleName()));
		createFragmentAction.doAction();
		assertTrue(createFragmentAction.hasActionExecutionSucceeded());

		AddDocXFragment createFragment = (AddDocXFragment) ((AssignationAction) createFragmentAction.getNewEditionAction())
				.getAssignableAction();

		createFragment.setLocationSemantics(LocationSemantics.EndOfDocument);

		assertEquals(fragment, createFragment.getFragment());

		virtualModel.getResource().save(null);

		System.out.println(virtualModel.getFMLModelFactory().stringRepresentation(virtualModel));

		assertTrue(virtualModel.hasNature(FMLControlledDocXVirtualModelNature.INSTANCE));
		assertEquals(docXModelSlot, FMLControlledDocXVirtualModelNature.getDocumentModelSlot(virtualModel));

		System.out.println("VirtualModel ***************************");
		System.out.println(virtualModel.getFMLRepresentation());
	}

	/**
	 * Instantiate in _project a View conform to the ViewPoint
	 */
	@Test
	@TestOrder(7)
	public void testCreateView() {
		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType
				.makeNewAction(_project.getVirtualModelInstanceRepository().getRootFolder(), null, _editor);
		action.setNewVirtualModelInstanceName("MyView");
		action.setNewVirtualModelInstanceTitle("Test creation of a new view");
		action.setVirtualModel(viewPoint);
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		newView = action.getNewVirtualModelInstance();
		assertNotNull(newView);
		assertNotNull(newView.getResource());
		assertTrue(ResourceLocator.retrieveResourceAsFile(((FMLRTVirtualModelInstanceResource) newView.getResource()).getDirectory())
				.exists());
		assertTrue(((FMLRTVirtualModelInstanceResource) newView.getResource()).getIODelegate().exists());
	}

	/**
	 * Instantiate in _project a FMLRTVirtualModelInstance conform to the VirtualModel
	 * 
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(8)
	public void testCreateVirtualModelInstance() throws SaveResourceException {

		log("testCreateVirtualModelInstance()");

		assertEquals(1, virtualModel.getModelSlots().size());

		assertEquals(1, virtualModel.getModelSlots(DocXModelSlot.class).size());
		DocXModelSlot ms = virtualModel.getModelSlots(DocXModelSlot.class).get(0);
		assertNotNull(ms);

		assertTrue(virtualModel.getModelSlots().contains(ms));

		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType.makeNewAction(newView, null, _editor);
		action.setNewVirtualModelInstanceName("MyVirtualModelInstance");
		action.setNewVirtualModelInstanceTitle("Test creation of a new FMLRTVirtualModelInstance for document generation");
		action.setVirtualModel(virtualModel);

		DocXModelSlotInstanceConfiguration docXModelSlotInstanceConfiguration = (DocXModelSlotInstanceConfiguration) action
				.getModelSlotInstanceConfiguration(ms);
		assertNotNull(docXModelSlotInstanceConfiguration);
		docXModelSlotInstanceConfiguration.setOption(DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewResource);
		docXModelSlotInstanceConfiguration.setRelativePath("DocX");
		docXModelSlotInstanceConfiguration.setFilename("GeneratedDocument.docx");
		// docXModelSlotInstanceConfiguration.setResourceUri("GeneratedDocument.docx");

		assertTrue(docXModelSlotInstanceConfiguration.isValidConfiguration());

		action.doAction();

		if (!action.hasActionExecutionSucceeded()) {
			fail(action.getThrownException().getMessage());
		}

		assertTrue(action.hasActionExecutionSucceeded());
		newVirtualModelInstance = action.getNewVirtualModelInstance();
		assertNotNull(newVirtualModelInstance);
		assertNotNull(newVirtualModelInstance.getResource());
		assertTrue(ResourceLocator.retrieveResourceAsFile(((FMLRTVirtualModelInstanceResource) newView.getResource()).getDirectory())
				.exists());
		assertTrue(((FMLRTVirtualModelInstanceResource) newView.getResource()).getIODelegate().exists());
		assertEquals(1, newVirtualModelInstance.getModelSlotInstances().size());

		FreeModelSlotInstance<DocXDocument, DocXModelSlot> docXMSInstance = (FreeModelSlotInstance<DocXDocument, DocXModelSlot>) newVirtualModelInstance
				.getModelSlotInstances().get(0);
		assertNotNull(docXMSInstance);

		System.out.println("FML: " + newVirtualModelInstance.getVirtualModel().getFMLRepresentation());

		newVirtualModelInstance.getResource().save(null);

		System.out.println("docXMSInstance=" + docXMSInstance);
		System.out.println("docXMSInstance.getAccessedResourceData()=" + docXMSInstance.getAccessedResourceData());

		assertNotNull(docXMSInstance.getAccessedResourceData());
		assertNotNull(docXMSInstance.getResource());

		// The VMI should have the FMLControlledDocXVirtualModelInstanceNature
		assertTrue(newVirtualModelInstance.hasNature(FMLControlledDocXVirtualModelInstanceNature.INSTANCE));

		assertNotNull(FMLControlledDocXVirtualModelInstanceNature.getModelSlotInstance(newVirtualModelInstance));
		assertNotNull(FMLControlledDocXVirtualModelInstanceNature.getModelSlotInstance(newVirtualModelInstance).getModelSlot());

		// assertFalse(generatedDocument.isModified());
		assertFalse(newVirtualModelInstance.isModified());

	}

	/**
	 * Try to generate the document in FMLRTVirtualModelInstance
	 * 
	 * @throws FlexoException
	 * @throws ResourceLoadingCancelledException
	 * @throws FileNotFoundException
	 */
	@Test
	@TestOrder(9)
	public void testGenerateDocument() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("testGenerateDocument()");

		FMLRTVirtualModelInstanceResource vmiRes = (FMLRTVirtualModelInstanceResource) newVirtualModelInstance.getResource();

		System.out.println("Template:\n" + templateResource.getResourceData(null).debugStructuredContents());

		// assertFalse(templateResource.isModified());
		assertFalse(newVirtualModelInstance.isModified());

		System.out.println(vmiRes.getFactory().stringRepresentation(vmiRes.getLoadedResourceData()));

		System.out.println("Executing FML:");
		System.out.println(actionScheme.getFMLRepresentation());

		ActionSchemeActionType actionType = new ActionSchemeActionType(actionScheme, newVirtualModelInstance);
		ActionSchemeAction actionSchemeCreationAction = actionType.makeNewAction(newVirtualModelInstance, null, _editor);
		assertNotNull(actionSchemeCreationAction);
		actionSchemeCreationAction.doAction();
		assertTrue(actionSchemeCreationAction.hasActionExecutionSucceeded());

		System.out.println(vmiRes.getFactory().stringRepresentation(vmiRes.getLoadedResourceData()));

		FreeModelSlotInstance<DocXDocument, DocXModelSlot> docXMSInstance = (FreeModelSlotInstance<DocXDocument, DocXModelSlot>) newVirtualModelInstance
				.getModelSlotInstances().get(0);
		assertNotNull(docXMSInstance);

		assertNotNull(docXMSInstance.getResource());
		// Resource data is the generated document now, and is not null
		assertNotNull(generatedDocument = docXMSInstance.getAccessedResourceData());

		// The VMI has now the FMLControlledDocXVirtualModelInstanceNature yet,
		// because document not null anymore
		assertTrue(newVirtualModelInstance.hasNature(FMLControlledDocXVirtualModelInstanceNature.INSTANCE));

		assertNotNull(FMLControlledDocXVirtualModelInstanceNature.getModelSlotInstance(newVirtualModelInstance));
		assertNotNull(FMLControlledDocXVirtualModelInstanceNature.getModelSlotInstance(newVirtualModelInstance).getModelSlot());

		newVirtualModelInstance.getResource().save(null);
		newView.getResource().save(null);

		assertTrue(generatedDocument.isModified());
		// assertFalse(newVirtualModelInstance.isModified());

		System.out.println("Generated document:\n" + generatedDocument.debugStructuredContents());

		generatedDocument.getResource().save(null);
		assertFalse(generatedDocument.isModified());

		assertEquals(13, generatedDocument.getElements().size());

		DocXFragment templatefragment = fragmentRole.getFragment();
		DocXFragment generatedFragment = newVirtualModelInstance.getFlexoActor(fragmentRole);
		FragmentActorReference<DocXFragment> actorReference = (FragmentActorReference<DocXFragment>) newVirtualModelInstance
				.getActorReference(fragmentRole);

		System.out.println("Template fragment = " + templatefragment);
		System.out.println("Generated fragment = " + generatedFragment);
		System.out.println("ActorReference = " + actorReference);

		// OK, generatedFragment is supposed to be generated from template
		// fragment
		// Perform some checks

		assertEquals(templatefragment.getElements().size(), generatedFragment.getElements().size());
		for (int i = 0; i < templatefragment.getElements().size(); i++) {
			DocXParagraph p1 = (DocXParagraph) templatefragment.getElements().get(i);
			DocXParagraph p2 = (DocXParagraph) generatedFragment.getElements().get(i);
			assertEquals(p1.getRawText(), p2.getRawText());
			assertFalse(p1.getIdentifier().equals(p2.getIdentifier()));
			assertEquals(p2.getBaseIdentifier(), p1.getIdentifier());
			ElementReference er = actorReference.getElementReferences().get(i);
			assertEquals(p1.getIdentifier(), er.getTemplateElementId());
			assertEquals(p2.getIdentifier(), er.getElementId());
		}

	}

	/**
	 */
	@Test
	@TestOrder(10)
	public void testReloadProject() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("testReloadProject()");

		DocXDocument generatedDocumentBeforeReload = generatedDocument;
		assertNotNull(generatedDocumentBeforeReload);

		instanciateTestServiceManagerForDocX(IdentifierManagementStrategy.ParaId);

		serviceManager.getResourceCenterService().addToResourceCenters(
				newResourceCenter = new DirectoryResourceCenter(testResourceCenterDirectory, serviceManager.getResourceCenterService()));
		newResourceCenter.performDirectoryWatchingNow();

		System.out.println("Project dir = " + _project.getDirectory());

		_editor = reloadProject(_project.getDirectory());

		/*
		 * System.out.println("Registered resources:"); for (FlexoResource<?> r
		 * : serviceManager.getResourceManager().getRegisteredResources()) {
		 * System.out.println(" > " + r.getURI()); }
		 */

		_project = _editor.getProject();
		assertNotNull(_editor);
		assertNotNull(_project);

		assertEquals(3, _project.getAllResources().size());
		// System.out.println("All resources=" + _project.getAllResources());
		assertNotNull(_project.getResource(newView.getURI()));

		System.out.println("viewPoint.getURI()=" + viewPoint.getURI());
		System.out.println("viewPointResource.getURI()=" + viewPointResource.getURI());

		FMLRTVirtualModelInstanceResource newViewResource = _project.getVirtualModelInstanceRepository()
				.getVirtualModelInstance(newView.getURI());
		assertNotNull(newViewResource);
		assertNull(newViewResource.getLoadedResourceData());

		System.out.println("newViewResource.getURI()=" + newViewResource.getURI());
		System.out.println("newViewResource.getViewPointResource().getURI()=" + newViewResource.getVirtualModelResource().getURI());

		newViewResource.loadResourceData(null);
		assertNotNull(newView = newViewResource.getVirtualModelInstance());

		assertEquals(1, newViewResource.getVirtualModelInstanceResources().size());
		FMLRTVirtualModelInstanceResource vmiResource = newViewResource.getVirtualModelInstanceResources().get(0);
		assertNotNull(vmiResource);
		assertNull(vmiResource.getLoadedResourceData());
		vmiResource.loadResourceData(null);
		assertNotNull(newVirtualModelInstance = vmiResource.getVirtualModelInstance());

		assertTrue(newVirtualModelInstance.getVirtualModel().hasNature(FMLControlledDocXVirtualModelNature.INSTANCE));

		assertTrue(newVirtualModelInstance.hasNature(FMLControlledDocXVirtualModelInstanceNature.INSTANCE));

		ModelSlotInstance<DocXModelSlot, DocXDocument> msInstance = FMLControlledDocXVirtualModelInstanceNature
				.getModelSlotInstance(newVirtualModelInstance);

		assertNotNull(msInstance);
		assertNotNull(msInstance.getAccessedResourceData());

		generatedDocument = msInstance.getAccessedResourceData();

		assertNotSame(generatedDocumentBeforeReload, generatedDocument);

		assertEquals(13, generatedDocument.getElements().size());

		fragmentRole = (DocXFragmentRole) newVirtualModelInstance.getVirtualModel().getAccessibleRole("fragment");
		DocXFragment templatefragment = fragmentRole.getFragment();
		DocXFragment generatedFragment = newVirtualModelInstance.getFlexoActor(fragmentRole);
		FragmentActorReference<DocXFragment> actorReference = (FragmentActorReference<DocXFragment>) newVirtualModelInstance
				.getActorReference(fragmentRole);

		System.out.println("Template fragment = " + templatefragment);
		System.out.println("Generated fragment = " + generatedFragment);
		System.out.println("ActorReference = " + actorReference);

		assertNotNull(templatefragment);
		assertNotNull(generatedFragment);
		assertNotNull(actorReference);

		// OK, generatedFragment is supposed to be generated from template
		// fragment
		// Perform some checks

		assertEquals(templatefragment.getElements().size(), generatedFragment.getElements().size());
		for (int i = 0; i < templatefragment.getElements().size(); i++) {
			DocXParagraph p1 = (DocXParagraph) templatefragment.getElements().get(i);
			DocXParagraph p2 = (DocXParagraph) generatedFragment.getElements().get(i);
			assertEquals(p1.getRawText(), p2.getRawText());
			assertFalse(p1.getIdentifier().equals(p2.getIdentifier()));
			assertEquals(p2.getBaseIdentifier(), p1.getIdentifier());
			ElementReference er = actorReference.getElementReferences().get(i);
			assertEquals(p1.getIdentifier(), er.getTemplateElementId());
			assertEquals(p2.getIdentifier(), er.getElementId());
		}
	}
}
