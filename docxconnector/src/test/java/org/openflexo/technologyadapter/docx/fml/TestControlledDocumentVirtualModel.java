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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.doc.FlexoDocumentFragment.FragmentConsistencyException;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModel.VirtualModelImpl;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateFlexoRole;
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
import org.openflexo.technologyadapter.docx.fml.action.GenerateDocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFragment;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the creation of a VirtualModel whose instances have {@link FMLControlledDocumentVirtualModelNature}
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestControlledDocumentVirtualModel extends OpenflexoProjectAtRunTimeTestCase {

	/*private final String DIAGRAM_SPECIFICATION_NAME = "myDiagramSpecification";
	private final String DIAGRAM_SPECIFICATION_URI = "http://myDiagramSpecification";
	private final String PALETTE_NAME = "myDiagramSpecificationPalette";
	private final String PALETTE_ELEMENT_NAME = "myPaletteElement";*/

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
	public static VirtualModel virtualModel;
	public static ActionScheme actionScheme;

	/**
	 * Initialize
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
	 * Test Create diagram specification resource
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

	}

	private DocXDocumentResource getDocument(String documentName) throws FileNotFoundException, ResourceLoadingCancelledException,
			FlexoException {

		for (FlexoResource<?> r : resourceCenter.getAllResources()) {
			System.out.println("Resource " + r + " uri=" + r.getURI());
		}

		String documentURI = resourceCenter.getDefaultBaseURI() + File.separator + documentName;
		System.out.println("Searching " + documentURI);

		DocXDocumentResource documentResource = (DocXDocumentResource) serviceManager.getInformationSpace().getResource(documentURI, null,
				DocXDocument.class);
		assertNotNull(documentResource);

		DocXDocument document = documentResource.getResourceData(null);
		assertNotNull(document);
		assertNotNull(document.getWordprocessingMLPackage());

		return documentResource;
	}

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
	 * Test the VP creation, in the project
	 */
	@Test
	@TestOrder(5)
	public void testCreateViewPoint() {

		log("testCreateViewPoint()");

		viewPoint = ViewPointImpl.newViewPoint(VIEWPOINT_NAME, VIEWPOINT_URI, project.getDirectory(), serviceManager.getViewPointLibrary());
		viewPointResource = (ViewPointResource) viewPoint.getResource();
		// assertTrue(viewPointResource.getDirectory().exists());
		assertTrue(viewPointResource.getDirectory() != null);
		assertTrue(viewPointResource.getFlexoIODelegate().exists());
	}

	/**
	 * Test the VirtualModel creation
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

		FlexoConcept flexoConcept = virtualModel.getFMLModelFactory().newInstance(FlexoConcept.class);
		virtualModel.addToFlexoConcepts(flexoConcept);

		CreateFlexoRole createFragmentRole = CreateFlexoRole.actionType.makeNewAction(flexoConcept, null, editor);
		createFragmentRole.setRoleName("fragment");
		createFragmentRole.setFlexoRoleClass(DocXFragmentRole.class);
		createFragmentRole.doAction();
		assertTrue(createFragmentRole.hasActionExecutionSucceeded());

		DocXFragmentRole fragmentRole = (DocXFragmentRole) createFragmentRole.getNewFlexoRole();
		FMLModelFactory factory = flexoConcept.getFMLModelFactory();

		DocXParagraph startParagraph = (DocXParagraph) templateDocument.getElements().get(7);
		DocXParagraph endParagraph = (DocXParagraph) templateDocument.getElements().get(11);

		System.out.println("start=" + startParagraph.getRawText());
		System.out.println("end=" + endParagraph.getRawText());

		DocXFragment fragment = (DocXFragment) templateResource.getFactory().makeFragment(startParagraph, endParagraph);
		fragmentRole.setFragment(fragment);

		CreateFlexoBehaviour createActionScheme = CreateFlexoBehaviour.actionType.makeNewAction(flexoConcept, null, editor);
		createActionScheme.setFlexoBehaviourName("generate");
		createActionScheme.setFlexoBehaviourClass(ActionScheme.class);
		createActionScheme.doAction();
		assertTrue(createActionScheme.hasActionExecutionSucceeded());
		actionScheme = (ActionScheme) createActionScheme.getNewFlexoBehaviour();

		CreateEditionAction createGenerateDocXDocument = CreateEditionAction.actionType.makeNewAction(actionScheme.getControlGraph(), null,
				editor);
		// createAddShape.actionChoice = CreateEditionActionChoice.ModelSlotSpecificAction;
		createGenerateDocXDocument.setModelSlot(docXModelSlot);
		createGenerateDocXDocument.setEditionActionClass(GenerateDocXDocument.class);
		createGenerateDocXDocument.doAction();
		assertTrue(createGenerateDocXDocument.hasActionExecutionSucceeded());

		virtualModel.getResource().save(null);

		System.out.println(virtualModel.getFMLModelFactory().stringRepresentation(virtualModel));

		assertTrue(virtualModel.hasNature(FMLControlledDocumentVirtualModelNature.INSTANCE));
		assertEquals(docXModelSlot, FMLControlledDocumentVirtualModelNature.getDocumentModelSlot(virtualModel));

		System.exit(-1);

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

		// assertFalse(generatedDocument.isModified());
		// assertFalse(newVirtualModelInstance.isModified());

		/*System.out.println("Unsaved resources=" + serviceManager.getResourceManager().getUnsavedResources());
		
		assertEquals(2, serviceManager.getResourceManager().getUnsavedResources().size());
		assertTrue(serviceManager.getResourceManager().getUnsavedResources().contains(newVirtualModelInstance.getResource()));
		assertTrue(serviceManager.getResourceManager().getUnsavedResources().contains(generatedDocument.getResource()));
		
		newVirtualModelInstance.getResource().save(null);
		assertTrue(((VirtualModelInstanceResource) newVirtualModelInstance.getResource()).getFlexoIODelegate().exists());
		assertFalse(newVirtualModelInstance.isModified());
		
		generatedDocument.getResource().save(null);
		assertFalse(generatedDocument.isModified());
		
		assertEquals(0, serviceManager.getResourceManager().getUnsavedResources().size());*/
	}

	/*@Test
	@TestOrder(9)
	public void testPopulateVirtualModelInstance() throws SaveResourceException {
	
		log("testPopulateVirtualModelInstance()");
	
		VirtualModelInstanceResource vmiRes = (VirtualModelInstanceResource) newVirtualModelInstance.getResource();
	
		assertFalse(diagram.isModified());
		assertFalse(newVirtualModelInstance.isModified());
	
		System.out.println(vmiRes.getFactory().stringRepresentation(vmiRes.getLoadedResourceData()));
	
		DropSchemeAction action = DropSchemeAction.actionType.makeNewAction(newVirtualModelInstance, null, editor);
		action.setDropScheme(dropScheme);
		// action.setParent(diagram);
		// action.setPaletteElement(paletteElement);
		action.setDropLocation(new FGEPoint(100, 100));
	
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
	
		System.out.println(vmiRes.getFactory().stringRepresentation(vmiRes.getLoadedResourceData()));
	
		assertTrue(diagram.isModified());
		assertTrue(newVirtualModelInstance.isModified());
	
		System.out.println("Unsaved resources=" + serviceManager.getResourceManager().getUnsavedResources());
	
		assertTrue(diagram.isModified());
		assertTrue(newVirtualModelInstance.isModified());
	
		assertEquals(2, serviceManager.getResourceManager().getUnsavedResources().size());
		assertTrue(serviceManager.getResourceManager().getUnsavedResources().contains(newVirtualModelInstance.getResource()));
		assertTrue(serviceManager.getResourceManager().getUnsavedResources().contains(diagram.getResource()));
	
		newVirtualModelInstance.getResource().save(null);
		assertTrue(((VirtualModelInstanceResource) newVirtualModelInstance.getResource()).getFlexoIODelegate().exists());
		assertFalse(newVirtualModelInstance.isModified());
	
		diagram.getResource().save(null);
		assertTrue(((DiagramResource) diagram.getResource()).getFlexoIODelegate().exists());
		assertFalse(diagram.isModified());
	
		assertEquals(0, serviceManager.getResourceManager().getUnsavedResources().size());
	}*/

	/**
	 */
	@Test
	@TestOrder(10)
	public void testReloadProject() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("testReloadProject()");

		instanciateTestServiceManager();
		editor = reloadProject(project.getDirectory());
		project = editor.getProject();
		assertNotNull(editor);
		assertNotNull(project);

		assertEquals(3, project.getAllResources().size());
		System.out.println("All resources=" + project.getAllResources());
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

		/*assertEquals(1, newVirtualModelInstance.getFlexoConceptInstances().size());
		FlexoConceptInstance fci = newVirtualModelInstance.getFlexoConceptInstances().get(0);
		assertNotNull(fci);*/

		assertTrue(newVirtualModelInstance.hasNature(FMLControlledDocumentVirtualModelInstanceNature.INSTANCE));

		ModelSlotInstance<DocXModelSlot, DocXDocument> msInstance = FMLControlledDocumentVirtualModelInstanceNature
				.getModelSlotInstance(newVirtualModelInstance);

		assertNotNull(msInstance);
		assertNotNull(msInstance.getAccessedResourceData());

		// assertEquals(1, fci.getActors().size());

		/*ModelObjectActorReference<DiagramShape> actorReference = (ModelObjectActorReference<DiagramShape>) fci.getActors().get(0);
		assertNotNull(actorReference);
		assertNotNull(actorReference.getModellingElement());*/

	}
}
