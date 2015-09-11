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

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.doc.FlexoDocumentFragment.FragmentConsistencyException;
import org.openflexo.foundation.doc.fml.FragmentActorReference;
import org.openflexo.foundation.doc.fml.FragmentActorReference.ElementReference;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModel.VirtualModelImpl;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateTechnologyRole;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionType;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateView;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.DefaultModelSlotInstanceConfigurationOption;
import org.openflexo.foundation.fml.rt.rm.ViewResource;
import org.openflexo.foundation.fml.rt.rm.VirtualModelInstanceResource;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.docx.DocXModelSlot;
import org.openflexo.technologyadapter.docx.DocXModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.fml.editionaction.AddDocXFragment;
import org.openflexo.technologyadapter.docx.fml.editionaction.AddDocXFragment.LocationSemantics;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFragment;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the creation and some manipulations of a {@link VirtualModel} with {@link FMLControlledDocumentVirtualModelNature}<br>
 * We basically test here the generation of a plain document from a template, and the adding of a fragment at the end of the document
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestControlledDocumentVirtualModel extends OpenflexoProjectAtRunTimeTestCase {

	private final String VIEWPOINT_NAME = "TestViewPointControlledDocument";
	private final String VIEWPOINT_URI = "http://openflexo.org/test/TestViewPointControlledDocument";

	public static DocXTechnologyAdapter technologicalAdapter;
	public static DocXDocumentRepository repository;
	public static FlexoEditor editor;
	public static FlexoProject project;
	public static View newView;
	public static VirtualModelInstance newVirtualModelInstance;

	public static DocXDocumentResource templateResource;
	public static DocXDocument templateDocument;
	public static DocXDocument generatedDocument;

	public static ViewPoint viewPoint;
	public static ViewPointResource viewPointResource;
	public static DocXModelSlot docXModelSlot;
	public static DocXFragmentRole fragmentRole;
	public static VirtualModel virtualModel;
	public static ActionScheme actionScheme;

	/**
	 * Initialize an environment with DocX technology adapter, perform some checks
	 */
	@Test
	@TestOrder(1)
	public void testInitialize() {

		log("testInitialize()");

		serviceManager = instanciateTestServiceManager();

		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class);
		repository = resourceCenter.getRepository(DocXDocumentRepository.class, technologicalAdapter);
		editor = new FlexoTestEditor(null, serviceManager);

		assertNotNull(serviceManager);
		assertNotNull(technologicalAdapter);
		assertNotNull(resourceCenter);
		assertNotNull(repository);

		System.out.println("URI" + resourceCenter.getDefaultBaseURI());
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

		templateResource = getDocument("StructuredDocument.docx");

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
	private DocXDocumentResource getDocument(String documentName)
			throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		for (FlexoResource<?> r : resourceCenter.getAllResources()) {
			System.out.println("Resource " + r + " uri=" + r.getURI());
		}

		String documentURI = resourceCenter.getDefaultBaseURI() + File.separator + documentName;
		System.out.println("Searching " + documentURI);

		DocXDocumentResource documentResource = (DocXDocumentResource) serviceManager.getResourceManager().getResource(documentURI, null,
				DocXDocument.class);
		assertNotNull(documentResource);

		DocXDocument document = documentResource.getResourceData(null);
		assertNotNull(document);
		assertNotNull(document.getWordprocessingMLPackage());

		return documentResource;
	}

	/**
	 * Create a brand new project
	 */
	@Test
	@TestOrder(4)
	public void testCreateProject() {
		editor = createProject("TestProject");
		project = editor.getProject();
		System.out.println("Created project " + project.getProjectDirectory());
		assertTrue(project.getProjectDirectory().exists());
		assertTrue(project.getProjectDataResource().getFlexoIODelegate().exists());
	}

	/**
	 * Creates a new empty ViewPoint in the project
	 */
	@Test
	@TestOrder(5)
	public void testCreateViewPoint() {

		log("testCreateViewPoint()");

		viewPoint = ViewPointImpl.newViewPoint(VIEWPOINT_NAME, VIEWPOINT_URI, project.getDirectory(), serviceManager.getViewPointLibrary(),
				resourceCenter);
		viewPointResource = (ViewPointResource) viewPoint.getResource();
		// assertTrue(viewPointResource.getDirectory().exists());
		assertTrue(viewPointResource.getDirectory() != null);
		assertTrue(viewPointResource.getFlexoIODelegate().exists());
	}

	/**
	 * Test the VirtualModel creation<br>
	 * We create here a VirtualModel with a unique {@link DocXModelSlot}, configured with template docx Then we define a fragment role on
	 * the {@link VirtualModel}<br>
	 * We also define an {@link ActionScheme} on the {@link VirtualModel}, which generate the docx document from the template and then add a
	 * fragment to the end of document
	 * 
	 * @throws FragmentConsistencyException
	 */
	@Test
	@TestOrder(6)
	public void testCreateVirtualModel() throws SaveResourceException, FragmentConsistencyException {

		log("testCreateVirtualModel()");

		virtualModel = VirtualModelImpl.newVirtualModel("TestVirtualModel", viewPoint);
		assertTrue(ResourceLocator.retrieveResourceAsFile(((VirtualModelResource) virtualModel.getResource()).getDirectory()).exists());
		assertTrue(((VirtualModelResource) virtualModel.getResource()).getFlexoIODelegate().exists());

		docXModelSlot = technologicalAdapter.makeModelSlot(DocXModelSlot.class, virtualModel);
		docXModelSlot.setTemplateResource(templateResource);
		docXModelSlot.setName("document");
		assertNotNull(docXModelSlot);

		virtualModel.addToModelSlots(docXModelSlot);
		assertTrue(virtualModel.getModelSlots(DocXModelSlot.class).size() == 1);

		// FlexoConcept flexoConcept = virtualModel.getFMLModelFactory().newInstance(FlexoConcept.class);
		// virtualModel.addToFlexoConcepts(flexoConcept);

		CreateTechnologyRole createFragmentRole = CreateTechnologyRole.actionType.makeNewAction(virtualModel, null, editor);
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

		CreateFlexoBehaviour createActionScheme = CreateFlexoBehaviour.actionType.makeNewAction(virtualModel, null, editor);
		createActionScheme.setFlexoBehaviourName("generate");
		createActionScheme.setFlexoBehaviourClass(ActionScheme.class);
		createActionScheme.doAction();
		assertTrue(createActionScheme.hasActionExecutionSucceeded());
		actionScheme = (ActionScheme) createActionScheme.getNewFlexoBehaviour();

		CreateEditionAction createGenerateDocXDocumentAction = CreateEditionAction.actionType.makeNewAction(actionScheme.getControlGraph(),
				null, editor);
		// createAddShape.actionChoice = CreateEditionActionChoice.ModelSlotSpecificAction;
		createGenerateDocXDocumentAction.setModelSlot(docXModelSlot);
		createGenerateDocXDocumentAction.setEditionActionClass(GenerateDocXDocument.class);
		createGenerateDocXDocumentAction.doAction();
		assertTrue(createGenerateDocXDocumentAction.hasActionExecutionSucceeded());

		CreateEditionAction createFragmentAction = CreateEditionAction.actionType.makeNewAction(actionScheme.getControlGraph(), null,
				editor);
		// createAddShape.actionChoice = CreateEditionActionChoice.ModelSlotSpecificAction;
		createFragmentAction.setModelSlot(docXModelSlot);
		createFragmentAction.setEditionActionClass(AddDocXFragment.class);
		createFragmentAction.setAssignation(new DataBinding<Object>(fragmentRole.getRoleName()));
		createFragmentAction.doAction();
		assertTrue(createFragmentAction.hasActionExecutionSucceeded());

		AddDocXFragment createFragment = (AddDocXFragment) ((AssignationAction) createFragmentAction.getNewEditionAction())
				.getAssignableAction();

		createFragment.setLocationSemantics(LocationSemantics.EndOfDocument);

		assertEquals(fragment, createFragment.getFragment());

		virtualModel.getResource().save(null);

		System.out.println(virtualModel.getFMLModelFactory().stringRepresentation(virtualModel));

		assertTrue(virtualModel.hasNature(FMLControlledDocumentVirtualModelNature.INSTANCE));
		assertEquals(docXModelSlot, FMLControlledDocumentVirtualModelNature.getDocumentModelSlot(virtualModel));

	}

	/**
	 * Instantiate in project a View conform to the ViewPoint
	 */
	@Test
	@TestOrder(7)
	public void testCreateView() {
		CreateView action = CreateView.actionType.makeNewAction(project.getViewLibrary().getRootFolder(), null, editor);
		action.setNewViewName("MyView");
		action.setNewViewTitle("Test creation of a new view");
		action.setViewpointResource((ViewPointResource) viewPoint.getResource());
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		newView = action.getNewView();
		assertNotNull(newView);
		assertNotNull(newView.getResource());
		assertTrue(ResourceLocator.retrieveResourceAsFile(((ViewResource) newView.getResource()).getDirectory()).exists());
		assertTrue(((ViewResource) newView.getResource()).getFlexoIODelegate().exists());
	}

	/**
	 * Instantiate in project a VirtualModelInstance conform to the VirtualModel
	 */
	@Test
	@TestOrder(8)
	public void testCreateVirtualModelInstance() {

		log("testCreateVirtualModelInstance()");

		assertEquals(1, virtualModel.getModelSlots().size());

		assertEquals(1, virtualModel.getModelSlots(DocXModelSlot.class).size());
		DocXModelSlot ms = virtualModel.getModelSlots(DocXModelSlot.class).get(0);
		assertNotNull(ms);

		assertTrue(virtualModel.getModelSlots().contains(ms));

		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType.makeNewAction(newView, null, editor);
		action.setNewVirtualModelInstanceName("MyVirtualModelInstance");
		action.setNewVirtualModelInstanceTitle("Test creation of a new VirtualModelInstance for document generation");
		action.setVirtualModel(virtualModel);

		DocXModelSlotInstanceConfiguration docXModelSlotInstanceConfiguration = (DocXModelSlotInstanceConfiguration) action
				.getModelSlotInstanceConfiguration(ms);
		assertNotNull(docXModelSlotInstanceConfiguration);
		docXModelSlotInstanceConfiguration.setOption(DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewResource);
		docXModelSlotInstanceConfiguration.setRelativePath("DocX");
		docXModelSlotInstanceConfiguration.setFilename("GeneratedDocument.docx");
		docXModelSlotInstanceConfiguration.setResourceUri("GeneratedDocument.docx");

		assertTrue(docXModelSlotInstanceConfiguration.isValidConfiguration());

		action.doAction();

		if (!action.hasActionExecutionSucceeded()) {
			fail(action.getThrownException().getMessage());
		}

		assertTrue(action.hasActionExecutionSucceeded());
		newVirtualModelInstance = action.getNewVirtualModelInstance();
		assertNotNull(newVirtualModelInstance);
		assertNotNull(newVirtualModelInstance.getResource());
		assertTrue(ResourceLocator.retrieveResourceAsFile(((ViewResource) newView.getResource()).getDirectory()).exists());
		assertTrue(((ViewResource) newView.getResource()).getFlexoIODelegate().exists());
		assertEquals(1, newVirtualModelInstance.getModelSlotInstances().size());

		FreeModelSlotInstance<DocXDocument, DocXModelSlot> docXMSInstance = (FreeModelSlotInstance<DocXDocument, DocXModelSlot>) newVirtualModelInstance
				.getModelSlotInstances().get(0);
		assertNotNull(docXMSInstance);
		// Only resource was created, resource data remains null here
		assertNull(docXMSInstance.getAccessedResourceData());
		assertNotNull(docXMSInstance.getResource());

		// The VMI does not have the FMLControlledDocumentVirtualModelInstanceNature yet, because document still null
		assertFalse(newVirtualModelInstance.hasNature(FMLControlledDocumentVirtualModelInstanceNature.INSTANCE));

		assertNotNull(FMLControlledDocumentVirtualModelInstanceNature.getModelSlotInstance(newVirtualModelInstance));
		assertNotNull(FMLControlledDocumentVirtualModelInstanceNature.getModelSlotInstance(newVirtualModelInstance).getModelSlot());

		// assertFalse(generatedDocument.isModified());
		assertFalse(newVirtualModelInstance.isModified());

	}

	/**
	 * Try to generate the document in VirtualModelInstance
	 * 
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(9)
	public void testGenerateDocument() throws SaveResourceException {

		log("testGenerateDocument()");

		VirtualModelInstanceResource vmiRes = (VirtualModelInstanceResource) newVirtualModelInstance.getResource();

		assertFalse(templateResource.isModified());
		assertFalse(newVirtualModelInstance.isModified());

		System.out.println(vmiRes.getFactory().stringRepresentation(vmiRes.getLoadedResourceData()));

		ActionSchemeActionType actionType = new ActionSchemeActionType(actionScheme, newVirtualModelInstance);
		ActionSchemeAction actionSchemeCreationAction = actionType.makeNewAction(newVirtualModelInstance, null, editor);
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

		// The VMI has now the FMLControlledDocumentVirtualModelInstanceNature yet, because document not null anymore
		assertTrue(newVirtualModelInstance.hasNature(FMLControlledDocumentVirtualModelInstanceNature.INSTANCE));

		assertNotNull(FMLControlledDocumentVirtualModelInstanceNature.getModelSlotInstance(newVirtualModelInstance));
		assertNotNull(FMLControlledDocumentVirtualModelInstanceNature.getModelSlotInstance(newVirtualModelInstance).getModelSlot());

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

		// OK, generatedFragment is supposed to be generated from template fragment
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

		instanciateTestServiceManager();

		System.out.println("Project dir = " + project.getDirectory());

		editor = reloadProject(project.getDirectory());
		project = editor.getProject();
		assertNotNull(editor);
		assertNotNull(project);

		assertEquals(3, project.getAllResources().size());
		// System.out.println("All resources=" + project.getAllResources());
		assertNotNull(project.getResource(newView.getURI()));

		ViewResource newViewResource = project.getViewLibrary().getView(newView.getURI());
		assertNotNull(newViewResource);
		assertNull(newViewResource.getLoadedResourceData());
		newViewResource.loadResourceData(null);
		assertNotNull(newView = newViewResource.getView());

		assertEquals(1, newViewResource.getVirtualModelInstanceResources().size());
		VirtualModelInstanceResource vmiResource = newViewResource.getVirtualModelInstanceResources().get(0);
		assertNotNull(vmiResource);
		assertNull(vmiResource.getLoadedResourceData());
		vmiResource.loadResourceData(null);
		assertNotNull(newVirtualModelInstance = vmiResource.getVirtualModelInstance());

		assertTrue(newVirtualModelInstance.getVirtualModel().hasNature(FMLControlledDocumentVirtualModelNature.INSTANCE));

		assertTrue(newVirtualModelInstance.hasNature(FMLControlledDocumentVirtualModelInstanceNature.INSTANCE));

		ModelSlotInstance<DocXModelSlot, DocXDocument> msInstance = FMLControlledDocumentVirtualModelInstanceNature
				.getModelSlotInstance(newVirtualModelInstance);

		assertNotNull(msInstance);
		assertNotNull(msInstance.getAccessedResourceData());

		generatedDocument = msInstance.getAccessedResourceData();

		assertNotSame(generatedDocumentBeforeReload, generatedDocument);

		assertEquals(13, generatedDocument.getElements().size());

		DocXFragment templatefragment = fragmentRole.getFragment();
		DocXFragment generatedFragment = newVirtualModelInstance.getFlexoActor(fragmentRole);
		FragmentActorReference<DocXFragment> actorReference = (FragmentActorReference<DocXFragment>) newVirtualModelInstance
				.getActorReference(fragmentRole);

		System.out.println("Template fragment = " + templatefragment);
		System.out.println("Generated fragment = " + generatedFragment);
		System.out.println("ActorReference = " + actorReference);

		// OK, generatedFragment is supposed to be generated from template fragment
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
