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
import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.doc.FlexoDocumentFragment.FragmentConsistencyException;
import org.openflexo.foundation.doc.FlexoRun;
import org.openflexo.foundation.doc.TextSelection;
import org.openflexo.foundation.doc.fml.TextBinding;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.FlexoBehaviourParameter;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceParameter;
import org.openflexo.foundation.fml.PrimitiveRole.PrimitiveType;
import org.openflexo.foundation.fml.TextFieldParameter;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModel.VirtualModelImpl;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviourParameter;
import org.openflexo.foundation.fml.action.CreateFlexoConcept;
import org.openflexo.foundation.fml.action.CreateFlexoConceptInstanceRole;
import org.openflexo.foundation.fml.action.CreateModelSlot;
import org.openflexo.foundation.fml.action.CreatePrimitiveRole;
import org.openflexo.foundation.fml.action.CreateTechnologyRole;
import org.openflexo.foundation.fml.controlgraph.IterationAction;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.editionaction.ExpressionAction;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FMLRTModelSlot;
import org.openflexo.foundation.fml.rt.FMLRTModelSlotInstanceConfiguration;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionType;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateView;
import org.openflexo.foundation.fml.rt.action.CreationSchemeAction;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.DefaultModelSlotInstanceConfigurationOption;
import org.openflexo.foundation.fml.rt.editionaction.CreateFlexoConceptInstanceParameter;
import org.openflexo.foundation.fml.rt.editionaction.MatchFlexoConceptInstance;
import org.openflexo.foundation.fml.rt.editionaction.MatchingCriteria;
import org.openflexo.foundation.fml.rt.editionaction.SelectFlexoConceptInstance;
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
import org.openflexo.technologyadapter.docx.fml.editionaction.ApplyTextBindings;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXDocument;
import org.openflexo.technologyadapter.docx.fml.editionaction.AddDocXFragment.LocationSemantics;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXElement;
import org.openflexo.technologyadapter.docx.model.DocXFragment;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the creation and some manipulations of a {@link VirtualModel} with {@link FMLControlledDocumentVirtualModelNature}<br>
 * We create here a {@link VirtualModel} storing a library containing books.<br>
 * We then generate a document where the whole library is described.<br>
 * We test then some manipulations on the model, generated document, and template.
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestLibrary extends OpenflexoProjectAtRunTimeTestCase {

	private final String VIEWPOINT_NAME = "TestLibraryViewPoint";
	private final String VIEWPOINT_URI = "http://openflexo.org/test/TestLibraryViewPoint";

	public static DocXTechnologyAdapter technologicalAdapter;
	public static DocXDocumentRepository repository;
	public static FlexoEditor editor;
	public static FlexoProject project;
	public static View newView;
	public static VirtualModelInstance libraryVMI;
	public static VirtualModelInstance documentVMI;

	public static DocXDocumentResource templateResource;
	public static DocXDocument templateDocument;
	public static DocXDocument generatedDocument;

	public static ViewPoint viewPoint;
	public static ViewPointResource viewPointResource;

	public static VirtualModel libraryVirtualModel;
	public static FlexoConcept bookConcept;
	public static CreationScheme bookCreationScheme;
	public static FlexoBehaviourParameter titleParam, authorParam, editionParam, typeParam, descriptionParam;

	public static VirtualModel documentVirtualModel;
	public static FMLRTModelSlot libraryModelSlot;
	public static DocXModelSlot docXModelSlot;
	public static DocXFragmentRole introductionFragmentRole;
	public static DocXFragmentRole booksDescriptionFragmentRole;
	public static DocXFragmentRole conclusionFragmentRole;
	public static FlexoConcept bookDescriptionSection;
	public static ActionScheme generateDocumentActionScheme;
	public static ActionScheme updateDocumentActionScheme;

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

		templateResource = getDocument("ExampleLibrary.docx");

		assertNotNull(templateDocument = templateResource.getResourceData(null));

		assertEquals(14, templateDocument.getElements().size());

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

		DocXDocumentResource documentResource = (DocXDocumentResource) serviceManager.getInformationSpace().getResource(documentURI, null,
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

		viewPoint = ViewPointImpl.newViewPoint(VIEWPOINT_NAME, VIEWPOINT_URI, project.getDirectory(), serviceManager.getViewPointLibrary());
		viewPointResource = (ViewPointResource) viewPoint.getResource();
		// assertTrue(viewPointResource.getDirectory().exists());
		assertTrue(viewPointResource.getDirectory() != null);
		assertTrue(viewPointResource.getFlexoIODelegate().exists());
	}

	/**
	 * Create Library VirtualModel<br>
	 * We create here a VirtualModel without model slot, but with Book FlexoConcept
	 * 
	 * <code>
	 * VirtualModel LibraryVirtualModel uri="http://openflexo.org/test/TestLibraryViewPoint/LibraryVirtualModel" {
	 * 
	 *   FlexoConcept Book {  
	 *     FlexoRole title as String cardinality=ZeroOne;  
	 *     FlexoRole author as String cardinality=ZeroOne;  
	 *     FlexoRole edition as String cardinality=ZeroOne;  
	 *     FlexoRole type as String cardinality=ZeroOne;  
	 *     FlexoRole description as String cardinality=ZeroOne;  
	 * 
	 *     CreationScheme creationScheme(String aTitle, String anAuthor, String anEdition, String aType, String aDescription) {    
	 *       title = parameters.aTitle;      
	 *       author = parameters.anAuthor;      
	 *       edition = parameters.anEdition;      
	 *       type = parameters.aType;      
	 *       description = parameters.aDescription;    
	 *     }    
	 *   }  
	 * }
	 * </code>
	 */
	@Test
	@TestOrder(6)
	public void testCreateLibraryVirtualModel() throws SaveResourceException, FragmentConsistencyException {

		log("testCreateLibraryVirtualModel()");

		libraryVirtualModel = VirtualModelImpl.newVirtualModel("LibraryVirtualModel", viewPoint);
		assertTrue(
				ResourceLocator.retrieveResourceAsFile(((VirtualModelResource) libraryVirtualModel.getResource()).getDirectory()).exists());
		assertTrue(((VirtualModelResource) libraryVirtualModel.getResource()).getFlexoIODelegate().exists());

		CreateFlexoConcept createConceptAction = CreateFlexoConcept.actionType.makeNewAction(libraryVirtualModel, null, editor);
		createConceptAction.setNewFlexoConceptName("Book");
		createConceptAction.doAction();
		assertTrue(createConceptAction.hasActionExecutionSucceeded());
		bookConcept = createConceptAction.getNewFlexoConcept();

		CreatePrimitiveRole createTitleRole = CreatePrimitiveRole.actionType.makeNewAction(bookConcept, null, editor);
		createTitleRole.setRoleName("title");
		createTitleRole.setPrimitiveType(PrimitiveType.String);
		createTitleRole.doAction();
		assertTrue(createTitleRole.hasActionExecutionSucceeded());

		CreatePrimitiveRole createAuthorRole = CreatePrimitiveRole.actionType.makeNewAction(bookConcept, null, editor);
		createAuthorRole.setRoleName("author");
		createAuthorRole.setPrimitiveType(PrimitiveType.String);
		createAuthorRole.doAction();
		assertTrue(createAuthorRole.hasActionExecutionSucceeded());

		CreatePrimitiveRole createEditionRole = CreatePrimitiveRole.actionType.makeNewAction(bookConcept, null, editor);
		createEditionRole.setRoleName("edition");
		createEditionRole.setPrimitiveType(PrimitiveType.String);
		createEditionRole.doAction();
		assertTrue(createEditionRole.hasActionExecutionSucceeded());

		CreatePrimitiveRole createTypeRole = CreatePrimitiveRole.actionType.makeNewAction(bookConcept, null, editor);
		createTypeRole.setRoleName("type");
		createTypeRole.setPrimitiveType(PrimitiveType.String);
		createTypeRole.doAction();
		assertTrue(createTypeRole.hasActionExecutionSucceeded());

		CreatePrimitiveRole createDescriptionRole = CreatePrimitiveRole.actionType.makeNewAction(bookConcept, null, editor);
		createDescriptionRole.setRoleName("description");
		createDescriptionRole.setPrimitiveType(PrimitiveType.String);
		createDescriptionRole.doAction();
		assertTrue(createDescriptionRole.hasActionExecutionSucceeded());

		CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewAction(bookConcept, null, editor);
		createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
		createCreationScheme.setFlexoBehaviourName("creationScheme");
		createCreationScheme.doAction();
		bookCreationScheme = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();

		CreateFlexoBehaviourParameter createParameter1 = CreateFlexoBehaviourParameter.actionType.makeNewAction(bookCreationScheme, null,
				editor);
		createParameter1.setFlexoBehaviourParameterClass(TextFieldParameter.class);
		createParameter1.setParameterName("aTitle");
		createParameter1.doAction();
		titleParam = createParameter1.getNewParameter();

		CreateFlexoBehaviourParameter createParameter2 = CreateFlexoBehaviourParameter.actionType.makeNewAction(bookCreationScheme, null,
				editor);
		createParameter2.setFlexoBehaviourParameterClass(TextFieldParameter.class);
		createParameter2.setParameterName("anAuthor");
		createParameter2.doAction();
		authorParam = createParameter2.getNewParameter();

		CreateFlexoBehaviourParameter createParameter3 = CreateFlexoBehaviourParameter.actionType.makeNewAction(bookCreationScheme, null,
				editor);
		createParameter3.setFlexoBehaviourParameterClass(TextFieldParameter.class);
		createParameter3.setParameterName("anEdition");
		createParameter3.doAction();
		editionParam = createParameter3.getNewParameter();

		CreateFlexoBehaviourParameter createParameter4 = CreateFlexoBehaviourParameter.actionType.makeNewAction(bookCreationScheme, null,
				editor);
		createParameter4.setFlexoBehaviourParameterClass(TextFieldParameter.class);
		createParameter4.setParameterName("aType");
		createParameter4.doAction();
		typeParam = createParameter4.getNewParameter();

		CreateFlexoBehaviourParameter createParameter5 = CreateFlexoBehaviourParameter.actionType.makeNewAction(bookCreationScheme, null,
				editor);
		createParameter5.setFlexoBehaviourParameterClass(TextFieldParameter.class);
		createParameter5.setParameterName("aDescription");
		createParameter5.doAction();
		descriptionParam = createParameter5.getNewParameter();

		CreateEditionAction createEditionAction1 = CreateEditionAction.actionType.makeNewAction(bookCreationScheme.getControlGraph(), null,
				editor);
		createEditionAction1.setEditionActionClass(ExpressionAction.class);
		createEditionAction1.setAssignation(new DataBinding<Object>("title"));
		createEditionAction1.doAction();
		AssignationAction<?> action1 = (AssignationAction<?>) createEditionAction1.getNewEditionAction();
		((ExpressionAction) action1.getAssignableAction()).setExpression(new DataBinding<Object>("parameters.aTitle"));
		action1.setName("action1");
		assertTrue(action1.getAssignation().isValid());
		assertTrue(((ExpressionAction) action1.getAssignableAction()).getExpression().isValid());

		CreateEditionAction createEditionAction2 = CreateEditionAction.actionType.makeNewAction(bookCreationScheme.getControlGraph(), null,
				editor);
		createEditionAction2.setEditionActionClass(ExpressionAction.class);
		createEditionAction2.setAssignation(new DataBinding<Object>("author"));
		createEditionAction2.doAction();
		AssignationAction<?> action2 = (AssignationAction<?>) createEditionAction2.getNewEditionAction();
		((ExpressionAction) action2.getAssignableAction()).setExpression(new DataBinding<Object>("parameters.anAuthor"));
		action2.setName("action2");
		assertTrue(action2.getAssignation().isValid());
		assertTrue(((ExpressionAction) action2.getAssignableAction()).getExpression().isValid());

		CreateEditionAction createEditionAction3 = CreateEditionAction.actionType.makeNewAction(bookCreationScheme.getControlGraph(), null,
				editor);
		createEditionAction3.setEditionActionClass(ExpressionAction.class);
		createEditionAction3.setAssignation(new DataBinding<Object>("edition"));
		createEditionAction3.doAction();
		AssignationAction<?> action3 = (AssignationAction<?>) createEditionAction3.getNewEditionAction();
		((ExpressionAction) action3.getAssignableAction()).setExpression(new DataBinding<Object>("parameters.anEdition"));
		action3.setName("action3");
		assertTrue(action3.getAssignation().isValid());
		assertTrue(((ExpressionAction) action3.getAssignableAction()).getExpression().isValid());

		CreateEditionAction createEditionAction4 = CreateEditionAction.actionType.makeNewAction(bookCreationScheme.getControlGraph(), null,
				editor);
		createEditionAction4.setEditionActionClass(ExpressionAction.class);
		createEditionAction4.setAssignation(new DataBinding<Object>("type"));
		createEditionAction4.doAction();
		AssignationAction<?> action4 = (AssignationAction<?>) createEditionAction4.getNewEditionAction();
		((ExpressionAction) action4.getAssignableAction()).setExpression(new DataBinding<Object>("parameters.aType"));
		action4.setName("action4");
		assertTrue(action4.getAssignation().isValid());
		assertTrue(((ExpressionAction) action4.getAssignableAction()).getExpression().isValid());

		CreateEditionAction createEditionAction5 = CreateEditionAction.actionType.makeNewAction(bookCreationScheme.getControlGraph(), null,
				editor);
		createEditionAction5.setEditionActionClass(ExpressionAction.class);
		createEditionAction5.setAssignation(new DataBinding<Object>("description"));
		createEditionAction5.doAction();
		AssignationAction<?> action5 = (AssignationAction<?>) createEditionAction5.getNewEditionAction();
		((ExpressionAction) action5.getAssignableAction()).setExpression(new DataBinding<Object>("parameters.aDescription"));
		action5.setName("action5");
		assertTrue(action5.getAssignation().isValid());
		assertTrue(((ExpressionAction) action5.getAssignableAction()).getExpression().isValid());

		assertTrue(bookConcept.getCreationSchemes().contains(bookCreationScheme));

		libraryVirtualModel.getResource().save(null);

		System.out.println(libraryVirtualModel.getFMLModelFactory().stringRepresentation(libraryVirtualModel));

		System.out.println("FML:");
		System.out.println(libraryVirtualModel.getFMLRepresentation());
	}

	/**
	 * Create VirtualModel handling document generation features<br>
	 * We create here a VirtualModel with two model slots:
	 * <ul>
	 * <li>A {@link FMLModelSlot}, pointing to the Library virtual model</li>
	 * <li>A {@link DocXModelSlot}, configured with template docx</li> Then we define some fragment roles on the {@link VirtualModel},
	 * pointing to the three sections of the document<br>
	 * We also define an {@link ActionScheme} on the {@link VirtualModel}, which generate the docx document from the template and then add a
	 * fragment to the end of document
	 * 
	 * @throws FragmentConsistencyException
	 */
	@Test
	@TestOrder(7)
	public void testCreateDocumentVirtualModel() throws SaveResourceException, FragmentConsistencyException {

		log("testCreateDocumentVirtualModel()");

		documentVirtualModel = VirtualModelImpl.newVirtualModel("DocumentVirtualModel", viewPoint);
		assertTrue(ResourceLocator.retrieveResourceAsFile(((VirtualModelResource) documentVirtualModel.getResource()).getDirectory())
				.exists());
		assertTrue(((VirtualModelResource) documentVirtualModel.getResource()).getFlexoIODelegate().exists());

		// Now we create the library model slot
		CreateModelSlot createLibraryModelSlot = CreateModelSlot.actionType.makeNewAction(documentVirtualModel, null, editor);
		createLibraryModelSlot
				.setTechnologyAdapter(serviceManager.getTechnologyAdapterService().getTechnologyAdapter(FMLTechnologyAdapter.class));
		createLibraryModelSlot.setModelSlotClass(FMLRTModelSlot.class);
		createLibraryModelSlot.setModelSlotName("library");
		createLibraryModelSlot.setVmRes((VirtualModelResource) libraryVirtualModel.getResource());
		createLibraryModelSlot.doAction();
		assertTrue(createLibraryModelSlot.hasActionExecutionSucceeded());
		assertNotNull(libraryModelSlot = (FMLRTModelSlot) createLibraryModelSlot.getNewModelSlot());

		// Then we create the docx model slot
		CreateModelSlot createDocumentModelSlot = CreateModelSlot.actionType.makeNewAction(documentVirtualModel, null, editor);
		createDocumentModelSlot.setTechnologyAdapter(technologicalAdapter);
		createDocumentModelSlot.setModelSlotClass(DocXModelSlot.class);
		createDocumentModelSlot.setModelSlotName("document");
		createDocumentModelSlot.doAction();
		assertTrue(createDocumentModelSlot.hasActionExecutionSucceeded());
		assertNotNull(docXModelSlot = (DocXModelSlot) createDocumentModelSlot.getNewModelSlot());
		docXModelSlot.setTemplateResource(templateResource);

		assertTrue(documentVirtualModel.getModelSlots().size() == 2);

		// We create a role pointing to the first section (introduction section)
		CreateTechnologyRole createIntroductionSectionRole = CreateTechnologyRole.actionType.makeNewAction(documentVirtualModel, null,
				editor);
		createIntroductionSectionRole.setRoleName("introductionSection");
		createIntroductionSectionRole.setFlexoRoleClass(DocXFragmentRole.class);
		createIntroductionSectionRole.doAction();
		assertTrue(createIntroductionSectionRole.hasActionExecutionSucceeded());
		introductionFragmentRole = (DocXFragmentRole) createIntroductionSectionRole.getNewFlexoRole();
		DocXParagraph startParagraph1 = (DocXParagraph) templateDocument.getElements().get(2);
		DocXParagraph endParagraph1 = (DocXParagraph) templateDocument.getElements().get(3);
		System.out.println("Introduction:");
		System.out.println("start=" + startParagraph1.getRawText());
		System.out.println("end=" + endParagraph1.getRawText());
		DocXFragment introductionFragment = (DocXFragment) templateResource.getFactory().makeFragment(startParagraph1, endParagraph1);
		introductionFragmentRole.setFragment(introductionFragment);
		assertEquals(introductionFragmentRole.getFragment(), introductionFragment);

		// We create a role pointing to the second section (books description section)
		CreateTechnologyRole createBooksDescriptionSectionRole = CreateTechnologyRole.actionType.makeNewAction(documentVirtualModel, null,
				editor);
		createBooksDescriptionSectionRole.setRoleName("booksDescriptionSection");
		createBooksDescriptionSectionRole.setFlexoRoleClass(DocXFragmentRole.class);
		createBooksDescriptionSectionRole.doAction();
		assertTrue(createBooksDescriptionSectionRole.hasActionExecutionSucceeded());
		booksDescriptionFragmentRole = (DocXFragmentRole) createBooksDescriptionSectionRole.getNewFlexoRole();
		DocXParagraph startParagraph2 = (DocXParagraph) templateDocument.getElements().get(4);
		DocXParagraph endParagraph2 = (DocXParagraph) templateDocument.getElements().get(10);
		System.out.println("BooksDescription:");
		System.out.println("start=" + startParagraph2.getRawText());
		System.out.println("end=" + endParagraph2.getRawText());
		DocXFragment booksDescriptionFragment = (DocXFragment) templateResource.getFactory().makeFragment(startParagraph2, endParagraph2);
		booksDescriptionFragmentRole.setFragment(booksDescriptionFragment);
		assertEquals(booksDescriptionFragmentRole.getFragment(), booksDescriptionFragment);

		// We create a role pointing to the third section (conclusion section)
		CreateTechnologyRole createConclusionSectionRole = CreateTechnologyRole.actionType.makeNewAction(documentVirtualModel, null,
				editor);
		createConclusionSectionRole.setRoleName("conclusionSection");
		createConclusionSectionRole.setFlexoRoleClass(DocXFragmentRole.class);
		createConclusionSectionRole.doAction();
		assertTrue(createConclusionSectionRole.hasActionExecutionSucceeded());
		conclusionFragmentRole = (DocXFragmentRole) createConclusionSectionRole.getNewFlexoRole();
		DocXParagraph startParagraph3 = (DocXParagraph) templateDocument.getElements().get(12);
		DocXParagraph endParagraph3 = (DocXParagraph) templateDocument.getElements().get(13);
		System.out.println("Conclusion:");
		System.out.println("start=" + startParagraph3.getRawText());
		System.out.println("end=" + endParagraph3.getRawText());
		DocXFragment conclusionFragment = (DocXFragment) templateResource.getFactory().makeFragment(startParagraph3, endParagraph3);
		conclusionFragmentRole.setFragment(conclusionFragment);
		assertEquals(conclusionFragmentRole.getFragment(), conclusionFragment);

		bookDescriptionSection = createBookDescriptionSection();

		generateDocumentActionScheme = createGenerateDocument();
		updateDocumentActionScheme = createUpdateDocument();

		documentVirtualModel.getResource().save(null);

		System.out.println(documentVirtualModel.getFMLModelFactory().stringRepresentation(documentVirtualModel));

		System.out.println("FML:");
		System.out.println(documentVirtualModel.getFMLRepresentation());

		assertTrue(documentVirtualModel.hasNature(FMLControlledDocumentVirtualModelNature.INSTANCE));
		assertEquals(docXModelSlot, FMLControlledDocumentVirtualModelNature.getDocumentModelSlot(documentVirtualModel));

	}

	private FlexoConcept createBookDescriptionSection() throws FragmentConsistencyException {

		FlexoConcept bookDescriptionSection;

		CreateFlexoConcept createConceptAction = CreateFlexoConcept.actionType.makeNewAction(documentVirtualModel, null, editor);
		createConceptAction.setNewFlexoConceptName("BookDescriptionSection");
		createConceptAction.doAction();
		assertTrue(createConceptAction.hasActionExecutionSucceeded());
		bookDescriptionSection = createConceptAction.getNewFlexoConcept();

		// We create a role pointing to a book in the library virtual model
		CreateFlexoConceptInstanceRole createBookRole = CreateFlexoConceptInstanceRole.actionType.makeNewAction(bookDescriptionSection,
				null, editor);
		createBookRole.setRoleName("book");
		createBookRole.setFlexoConceptInstanceType(bookConcept);
		createBookRole.doAction();
		assertTrue(createBookRole.hasActionExecutionSucceeded());
		assertEquals(libraryModelSlot, createBookRole.getModelSlot());
		assertEquals(libraryModelSlot, createBookRole.getNewFlexoRole().getModelSlot());

		// We create a role pointing to the right fragment
		CreateTechnologyRole createSectionRole = CreateTechnologyRole.actionType.makeNewAction(bookDescriptionSection, null, editor);
		createSectionRole.setRoleName("section");
		createSectionRole.setFlexoRoleClass(DocXFragmentRole.class);
		createSectionRole.doAction();
		assertTrue(createSectionRole.hasActionExecutionSucceeded());
		DocXFragmentRole sectionRole = (DocXFragmentRole) createSectionRole.getNewFlexoRole();
		DocXParagraph titleParagraph = (DocXParagraph) templateDocument.getElements().get(6);
		DocXParagraph authorParagraph = (DocXParagraph) templateDocument.getElements().get(7);
		DocXParagraph editionParagraph = (DocXParagraph) templateDocument.getElements().get(8);
		DocXParagraph typeParagraph = (DocXParagraph) templateDocument.getElements().get(9);
		DocXParagraph descriptionParagraph = (DocXParagraph) templateDocument.getElements().get(10);

		DocXFragment bookDescriptionFragment = (DocXFragment) templateResource.getFactory().makeFragment(titleParagraph,
				descriptionParagraph);
		sectionRole.setFragment(bookDescriptionFragment);
		assertEquals(sectionRole.getFragment(), bookDescriptionFragment);
		assertEquals(docXModelSlot, sectionRole.getModelSlot());

		StringBuffer sb = new StringBuffer();
		for (DocXElement element : bookDescriptionFragment.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph para = (DocXParagraph) element;
				for (FlexoRun run : para.getRuns()) {
					sb.append("[" + run.getText() + "]");
				}
				sb.append("\n");
			}
		}

		System.out.println(sb.toString());

		// Here is the structuration of original fragment (bookDescriptionFragment):

		// [Les ][misérables]
		// [Author][: Victor Hugo]
		// [Edition][: ][Dunod]
		// [Type][: Roman]
		// [Les Misérables est un roman de Victor Hugo paru en 1862...][Verboeckhoven][ et Cie...][...]....

		assertEquals(2, titleParagraph.getRuns().size());
		assertEquals(2, authorParagraph.getRuns().size());
		assertEquals(3, editionParagraph.getRuns().size());
		assertEquals(2, typeParagraph.getRuns().size());
		assertEquals(10, descriptionParagraph.getRuns().size());

		TextSelection<DocXDocument, DocXTechnologyAdapter> titleSelection = bookDescriptionFragment.makeTextSelection(titleParagraph, 0, 1);
		assertEquals("Les misérables", titleSelection.getRawText());

		TextBinding titleBinding = sectionRole.makeTextBinding(titleSelection, new DataBinding<String>("book.title"));
		assertTrue(titleBinding.getValue().isValid());

		// System.out.println("BM=" + sectionRole.getBindingModel());
		// System.out.println("BM=" + binding1.getBindingModel());

		CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewAction(bookDescriptionSection, null, editor);
		createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
		createCreationScheme.setFlexoBehaviourName("createBookDescriptionSection");
		createCreationScheme.doAction();
		CreationScheme bookDescriptionSectionCreationScheme = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();

		CreateFlexoBehaviourParameter createParameter = CreateFlexoBehaviourParameter.actionType
				.makeNewAction(bookDescriptionSectionCreationScheme, null, editor);
		createParameter.setFlexoBehaviourParameterClass(FlexoConceptInstanceParameter.class);
		createParameter.setParameterName("aBook");
		createParameter.doAction();
		FlexoBehaviourParameter bookParam = createParameter.getNewParameter();

		CreateEditionAction createEditionAction = CreateEditionAction.actionType
				.makeNewAction(bookDescriptionSectionCreationScheme.getControlGraph(), null, editor);
		createEditionAction.setEditionActionClass(ExpressionAction.class);
		createEditionAction.setAssignation(new DataBinding<Object>("book"));
		createEditionAction.doAction();
		AssignationAction<?> action = (AssignationAction<?>) createEditionAction.getNewEditionAction();
		((ExpressionAction) action.getAssignableAction()).setExpression(new DataBinding<Object>("parameters.aBook"));
		action.setName("action");
		assertTrue(action.getAssignation().isValid());
		assertTrue(((ExpressionAction) action.getAssignableAction()).getExpression().isValid());

		CreateEditionAction createFragmentAction = CreateEditionAction.actionType
				.makeNewAction(bookDescriptionSectionCreationScheme.getControlGraph(), null, editor);
		createFragmentAction.setModelSlot(docXModelSlot);
		createFragmentAction.setEditionActionClass(AddDocXFragment.class);
		createFragmentAction.setAssignation(new DataBinding<Object>(sectionRole.getRoleName()));
		createFragmentAction.doAction();
		assertTrue(createFragmentAction.hasActionExecutionSucceeded());
		AddDocXFragment createFragment = (AddDocXFragment) ((AssignationAction) createFragmentAction.getNewEditionAction())
				.getAssignableAction();
		createFragment.setLocationSemantics(LocationSemantics.EndOfDocument);

		CreateEditionAction applyTextBindingsAction = CreateEditionAction.actionType
				.makeNewAction(bookDescriptionSectionCreationScheme.getControlGraph(), null, editor);
		applyTextBindingsAction.setFlexoRole(sectionRole);
		applyTextBindingsAction.setEditionActionClass(ApplyTextBindings.class);
		applyTextBindingsAction.doAction();
		assertTrue(applyTextBindingsAction.hasActionExecutionSucceeded());
		ApplyTextBindings applyTextBindings = (ApplyTextBindings) applyTextBindingsAction.getNewEditionAction();

		assertNotNull(applyTextBindings);

		assertTrue(bookDescriptionSection.getCreationSchemes().contains(bookDescriptionSectionCreationScheme));

		return bookDescriptionSection;
	}

	public ActionScheme createGenerateDocument() {

		// We create an ActionScheme allowing to generate docXDocument
		CreateFlexoBehaviour createActionScheme = CreateFlexoBehaviour.actionType.makeNewAction(documentVirtualModel, null, editor);
		createActionScheme.setFlexoBehaviourName("generateDocument");
		createActionScheme.setFlexoBehaviourClass(ActionScheme.class);
		createActionScheme.doAction();
		assertTrue(createActionScheme.hasActionExecutionSucceeded());
		ActionScheme generateDocumentActionScheme = (ActionScheme) createActionScheme.getNewFlexoBehaviour();

		CreateEditionAction createGenerateDocXDocumentAction = CreateEditionAction.actionType
				.makeNewAction(generateDocumentActionScheme.getControlGraph(), null, editor);
		createGenerateDocXDocumentAction.setModelSlot(docXModelSlot);
		createGenerateDocXDocumentAction.setEditionActionClass(GenerateDocXDocument.class);
		createGenerateDocXDocumentAction.doAction();
		assertTrue(createGenerateDocXDocumentAction.hasActionExecutionSucceeded());

		return generateDocumentActionScheme;

	}

	public ActionScheme createUpdateDocument() {

		// We programmatically implement this code:
		// ActionScheme testFetchRequestIteration(String aString, Boolean aBoolean) {
		// ... for (item in SelectFlexoConceptInstance as FlexoConceptA where
		// ......(selected.aBooleanInA = parameters.aBoolean; selected.aStringInA = parameters.aString)) {
		// .........name = item.aStringInA;
		// .........item.aStringInA = (name + "foo");
		// ......}
		// ...}
		// }

		// We create an ActionScheme allowing to update docXDocument
		CreateFlexoBehaviour createActionScheme2 = CreateFlexoBehaviour.actionType.makeNewAction(documentVirtualModel, null, editor);
		createActionScheme2.setFlexoBehaviourName("updateDocument");
		createActionScheme2.setFlexoBehaviourClass(ActionScheme.class);
		createActionScheme2.doAction();
		assertTrue(createActionScheme2.hasActionExecutionSucceeded());
		ActionScheme updateDocumentActionScheme = (ActionScheme) createActionScheme2.getNewFlexoBehaviour();

		CreateEditionAction createSelectFetchRequestIterationAction = CreateEditionAction.actionType
				.makeNewAction(updateDocumentActionScheme.getControlGraph(), null, editor);
		// createSelectFetchRequestIterationAction.actionChoice = CreateEditionActionChoice.ControlAction;
		createSelectFetchRequestIterationAction.setEditionActionClass(IterationAction.class);
		createSelectFetchRequestIterationAction.doAction();
		IterationAction fetchRequestIteration = (IterationAction) createSelectFetchRequestIterationAction.getNewEditionAction();
		fetchRequestIteration.setIteratorName("book");

		SelectFlexoConceptInstance selectFlexoConceptInstance = fetchRequestIteration.getFMLModelFactory().newSelectFlexoConceptInstance();
		selectFlexoConceptInstance.setVirtualModelInstance(new DataBinding<VirtualModelInstance>("library"));
		selectFlexoConceptInstance.setFlexoConceptType(bookConcept);
		fetchRequestIteration.setIterationAction(selectFlexoConceptInstance);

		/*FetchRequestCondition condition1 = selectFlexoConceptInstance.createCondition();
		condition1.setCondition(new DataBinding<Boolean>("selected.aBooleanInA = parameters.aBoolean"));
		
		FetchRequestCondition condition2 = selectFlexoConceptInstance.createCondition();
		condition2.setCondition(new DataBinding<Boolean>("selected.aStringInA = parameters.aString"));
		 */

		CreateEditionAction createMatchFlexoConceptInstanceAction = CreateEditionAction.actionType
				.makeNewAction(fetchRequestIteration.getControlGraph(), null, editor);
		// createMatchFlexoConceptInstanceAction.actionChoice = CreateEditionActionChoice.BuiltInAction;
		createMatchFlexoConceptInstanceAction.setEditionActionClass(MatchFlexoConceptInstance.class);
		createMatchFlexoConceptInstanceAction.doAction();
		MatchFlexoConceptInstance matchFlexoConceptInstance = (MatchFlexoConceptInstance) createMatchFlexoConceptInstanceAction
				.getNewEditionAction();
		matchFlexoConceptInstance.setFlexoConceptType(bookDescriptionSection);
		matchFlexoConceptInstance.setVirtualModelInstance(new DataBinding<VirtualModelInstance>("virtualModelInstance"));

		matchFlexoConceptInstance.setCreationScheme(bookDescriptionSection.getCreationSchemes().get(0));

		// We check here that matching criterias were updated
		assertEquals(2, matchFlexoConceptInstance.getMatchingCriterias().size());

		MatchingCriteria bookCriteria = matchFlexoConceptInstance.getMatchingCriteria(bookDescriptionSection.getAccessibleProperty("book"));
		MatchingCriteria sectionCriteria = matchFlexoConceptInstance
				.getMatchingCriteria(bookDescriptionSection.getAccessibleProperty("section"));

		assertNotNull(bookCriteria);
		assertNotNull(sectionCriteria);

		bookCriteria.setValue(new DataBinding<Object>("book"));
		assertTrue(bookCriteria.getValue().isValid());

		// We check here that creation parameters were updated
		assertEquals(1, matchFlexoConceptInstance.getParameters().size());

		CreateFlexoConceptInstanceParameter bookParam = matchFlexoConceptInstance
				.getParameter(bookDescriptionSection.getCreationSchemes().get(0).getParameters().get(0));
		assertNotNull(bookParam);
		bookParam.setValue(new DataBinding<Object>("book"));
		assertTrue(bookParam.getValue().isValid());

		/*CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewAction(flexoConceptA, null, editor);
		createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
		createCreationScheme.setFlexoBehaviourName("creationScheme2");
		createCreationScheme.doAction();
		CreationScheme creationScheme = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();
		
		CreateFlexoBehaviourParameter createStringParameter2 = CreateFlexoBehaviourParameter.actionType.makeNewAction(creationScheme, null,
				editor);
		createStringParameter2.setFlexoBehaviourParameterClass(TextFieldParameter.class);
		createStringParameter2.setParameterName("aStringParameter");
		createStringParameter2.doAction();
		FlexoBehaviourParameter creationSchemeParam1 = createStringParameter2.getNewParameter();
		assertNotNull(creationSchemeParam1);
		assertTrue(creationScheme.getParameters().contains(creationSchemeParam1));
		
		CreateFlexoBehaviourParameter createBooleanParameter2 = CreateFlexoBehaviourParameter.actionType.makeNewAction(creationScheme,
				null, editor);
		createBooleanParameter2.setFlexoBehaviourParameterClass(CheckboxParameter.class);
		createBooleanParameter2.setParameterName("aBooleanParameter");
		createBooleanParameter2.doAction();
		FlexoBehaviourParameter creationSchemeParam2 = createBooleanParameter2.getNewParameter();
		assertNotNull(creationSchemeParam2);
		assertTrue(creationScheme.getParameters().contains(creationSchemeParam2));
		
		CreateEditionAction createEditionAction1 = CreateEditionAction.actionType.makeNewAction(creationScheme.getControlGraph(), null,
				editor);
		// createEditionAction1.actionChoice = CreateEditionActionChoice.BuiltInAction;
		createEditionAction1.setEditionActionClass(ExpressionAction.class);
		createEditionAction1.setAssignation(new DataBinding<Object>("aStringInA"));
		createEditionAction1.doAction();
		AssignationAction<?> action1 = (AssignationAction<?>) createEditionAction1.getNewEditionAction();
		((ExpressionAction) action1.getAssignableAction()).setExpression(new DataBinding<Object>("parameters.aStringParameter"));
		
		assertTrue(action1.getAssignation().isValid());
		assertTrue(((ExpressionAction) action1.getAssignableAction()).getExpression().isValid());
		
		CreateEditionAction createEditionAction2 = CreateEditionAction.actionType.makeNewAction(creationScheme.getControlGraph(), null,
				editor);
		// createEditionAction2.actionChoice = CreateEditionActionChoice.BuiltInAction;
		createEditionAction2.setEditionActionClass(ExpressionAction.class);
		createEditionAction2.setAssignation(new DataBinding<Object>("aBooleanInA"));
		createEditionAction2.doAction();
		AssignationAction<?> action2 = (AssignationAction<?>) createEditionAction2.getNewEditionAction();
		((ExpressionAction) action2.getAssignableAction()).setExpression(new DataBinding<Object>("parameters.aBooleanParameter"));
		
		assertTrue(action2.getAssignation().isValid());
		assertTrue(((ExpressionAction) action2.getAssignableAction()).getExpression().isValid());
		
		assertNotNull(actionScheme);
		System.out.println("FML=" + actionScheme.getFMLRepresentation());
		
		matchFlexoConceptInstance.setCreationScheme(creationScheme);
		
		// We check here that matching criterias were updated
		assertEquals(4, matchFlexoConceptInstance.getMatchingCriterias().size());
		
		MatchingCriteria criteria1 = matchFlexoConceptInstance.getMatchingCriteria(flexoConceptA.getAccessibleProperty("aStringInA"));
		MatchingCriteria criteria2 = matchFlexoConceptInstance.getMatchingCriteria(flexoConceptA.getAccessibleProperty("aBooleanInA"));
		MatchingCriteria criteria3 = matchFlexoConceptInstance.getMatchingCriteria(flexoConceptA.getAccessibleProperty("anIntegerInA"));
		MatchingCriteria criteria4 = matchFlexoConceptInstance
				.getMatchingCriteria(flexoConceptA.getAccessibleProperty("anOtherBooleanInA"));
		
		assertNotNull(criteria1);
		assertNotNull(criteria2);
		assertNotNull(criteria3);
		assertNotNull(criteria4);
		
		criteria1.setValue(new DataBinding<Object>("item.aStringInA"));
		
		System.out.println("FML=" + actionScheme.getFMLRepresentation());
		
		assertTrue(criteria1.getValue().isValid());
		
		MatchingCriteria criteria1bis = matchFlexoConceptInstance.getMatchingCriteria(flexoConceptA.getAccessibleProperty("aStringInA"));
		assertSame(criteria1, criteria1bis);
		
		// We add a property
		// We check here that matching criterias were updated: an other criteria should appear
		
		AbstractCreateFlexoRole createRole = AbstractCreateFlexoRole.actionType.makeNewAction(flexoConceptA, null, editor);
		createRole.setRoleName("anOtherIntegerInA");
		createRole.setFlexoRoleClass(PrimitiveRole.class);
		createRole.setPrimitiveType(PrimitiveType.Integer);
		createRole.doAction();
		FlexoRole newRole = createRole.getNewFlexoRole();
		
		assertEquals(5, matchFlexoConceptInstance.getMatchingCriterias().size());
		assertNotNull(matchFlexoConceptInstance.getMatchingCriteria(newRole));
		
		// We remove the property
		// We check here that matching criterias were updated: the criteria should disappear
		flexoConceptA.removeFromFlexoProperties(newRole);
		
		assertEquals(4, matchFlexoConceptInstance.getMatchingCriterias().size());
		
		// We check here that create parameters were updated
		
		assertEquals(2, matchFlexoConceptInstance.getParameters().size());
		
		CreateFlexoConceptInstanceParameter createFCIParam1 = matchFlexoConceptInstance.getParameter(creationSchemeParam1);
		CreateFlexoConceptInstanceParameter createFCIParam2 = matchFlexoConceptInstance.getParameter(creationSchemeParam2);
		assertNotNull(createFCIParam1);
		assertNotNull(createFCIParam2);
		
		createFCIParam1.setValue(new DataBinding<Object>("item.aStringInA"));
		createFCIParam2.setValue(new DataBinding<Object>("true"));
		assertTrue(createFCIParam1.getValue().isValid());
		assertTrue(createFCIParam2.getValue().isValid());
		
		// WE change creation scheme, parameters should disappear
		matchFlexoConceptInstance.setCreationScheme(null);
		
		assertEquals(0, matchFlexoConceptInstance.getParameters().size());
		
		// We set again the creation scheme, parameters should come back
		matchFlexoConceptInstance.setCreationScheme(creationScheme);
		assertEquals(2, matchFlexoConceptInstance.getParameters().size());
		createFCIParam1 = matchFlexoConceptInstance.getParameter(creationSchemeParam1);
		createFCIParam2 = matchFlexoConceptInstance.getParameter(creationSchemeParam2);
		createFCIParam1.setValue(new DataBinding<Object>("item.aStringInA"));
		createFCIParam2.setValue(new DataBinding<Object>("true"));
		assertTrue(createFCIParam1.getValue().isValid());
		assertTrue(createFCIParam2.getValue().isValid());
		
		// We try to add a parameter
		CreateFlexoBehaviourParameter createBooleanParameter3 = CreateFlexoBehaviourParameter.actionType.makeNewAction(creationScheme,
				null, editor);
		createBooleanParameter3.setFlexoBehaviourParameterClass(CheckboxParameter.class);
		createBooleanParameter3.setParameterName("anOtherBooleanParameter");
		createBooleanParameter3.doAction();
		FlexoBehaviourParameter creationSchemeParam3 = createBooleanParameter3.getNewParameter();
		assertNotNull(creationSchemeParam3);
		assertTrue(creationScheme.getParameters().contains(creationSchemeParam3));
		assertEquals(3, matchFlexoConceptInstance.getParameters().size());
		
		// We remove it
		creationScheme.removeFromParameters(creationSchemeParam3);
		assertEquals(2, matchFlexoConceptInstance.getParameters().size());
		
		assertEquals(12, fetchRequestIteration.getBindingModel().getBindingVariablesCount());
		
		assertEquals(13, condition1.getBindingModel().getBindingVariablesCount());
		
		assertEquals(13, createFCIParam1.getBindingModel().getBindingVariablesCount());
		
		System.out.println("FML: " + actionScheme.getFMLRepresentation());*/

		return updateDocumentActionScheme;
	}

	/**
	 * Instantiate in project a View conform to the ViewPoint
	 */
	@Test
	@TestOrder(8)
	public void testCreateView() {
		CreateView action = CreateView.actionType.makeNewAction(project.getViewLibrary().getRootFolder(), null, editor);
		action.setNewViewName("MyLibraryView");
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
	 * 
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(9)
	public void testInstantiateLibrary() throws SaveResourceException {

		log("testInstantiateLibrary()");

		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType.makeNewAction(newView, null, editor);
		action.setNewVirtualModelInstanceName("MyLibrary");
		action.setNewVirtualModelInstanceTitle("Creation of my library");
		action.setVirtualModel(libraryVirtualModel);

		action.doAction();

		if (!action.hasActionExecutionSucceeded()) {
			fail(action.getThrownException().getMessage());
		}

		assertTrue(action.hasActionExecutionSucceeded());
		libraryVMI = action.getNewVirtualModelInstance();
		assertNotNull(libraryVMI);
		assertNotNull(libraryVMI.getResource());
		assertTrue(ResourceLocator.retrieveResourceAsFile(((ViewResource) newView.getResource()).getDirectory()).exists());
		assertTrue(((ViewResource) newView.getResource()).getFlexoIODelegate().exists());

		// Creation of book1
		CreationSchemeAction createBook1 = CreationSchemeAction.actionType.makeNewAction(libraryVMI, null, editor);
		createBook1.setCreationScheme(bookCreationScheme);
		createBook1.setParameterValue(titleParam, "Les misérables");
		createBook1.setParameterValue(authorParam, "Victor Hugo");
		createBook1.setParameterValue(editionParam, "Dunod");
		createBook1.setParameterValue(typeParam, "Roman");
		createBook1.setParameterValue(descriptionParam,
				"Les Misérables est un roman de Victor Hugo paru en 1862 (la première partie est publiée le 30 mars à Bruxelles par les Éditions Lacroix, Verboeckhoven et Cie, et le 3 avril de la même année à Paris1). Dans ce roman, un des plus emblématiques de la littérature française, Victor Hugo décrit la vie de misérables dans Paris et la France provinciale du xixe siècle et s'attache plus particulièrement aux pas du bagnard Jean Valjean.");
		assertNotNull(createBook1);
		createBook1.doAction();
		assertTrue(createBook1.hasActionExecutionSucceeded());
		FlexoConceptInstance book1 = createBook1.getFlexoConceptInstance();
		assertNotNull(book1);
		assertEquals(bookConcept, book1.getFlexoConcept());

		// Creation of book2
		CreationSchemeAction createBook2 = CreationSchemeAction.actionType.makeNewAction(libraryVMI, null, editor);
		createBook2.setCreationScheme(bookCreationScheme);
		createBook2.setParameterValue(titleParam, "Germinal");
		createBook2.setParameterValue(authorParam, "Emile Zola");
		createBook2.setParameterValue(editionParam, "Gil Blas");
		createBook2.setParameterValue(typeParam, "Roman");
		createBook2.setParameterValue(descriptionParam,
				"Germinal est un roman d'Émile Zola publié en 1885. Il s'agit du treizième roman de la série des Rougon-Macquart. Écrit d'avril 1884 à janvier 1885, le roman paraît d'abord en feuilleton entre novembre 1884 et février 1885 dans le Gil Blas. Il connaît sa première édition en mars 1885. Depuis il a été publié dans plus d'une centaine de pays.");
		assertNotNull(createBook2);
		createBook2.doAction();
		assertTrue(createBook2.hasActionExecutionSucceeded());
		FlexoConceptInstance book2 = createBook2.getFlexoConceptInstance();
		assertNotNull(book2);
		assertEquals(bookConcept, book2.getFlexoConcept());

		// Creation of book3
		CreationSchemeAction createBook3 = CreationSchemeAction.actionType.makeNewAction(libraryVMI, null, editor);
		createBook3.setCreationScheme(bookCreationScheme);
		createBook3.setParameterValue(titleParam, "La chartreuse de Parme");
		createBook3.setParameterValue(authorParam, "Stendhal");
		createBook3.setParameterValue(editionParam, "J. Hetzel, 1846");
		createBook3.setParameterValue(typeParam, "Roman");
		createBook3.setParameterValue(descriptionParam,
				"La Chartreuse de Parme est un roman publié par Stendhal. Cette œuvre majeure, qui lui valut la célébrité, fut publiée en deux volumes en mars 1839, puis refondue en 1841, soit peu avant la mort de Stendhal, à la suite d'un article fameux de Balzac et prenant de fait un tour plus « balzacien » : aujourd’hui, c’est le texte stendhalien d’origine que l’on lit encore.");
		assertNotNull(createBook3);
		createBook3.doAction();
		assertTrue(createBook3.hasActionExecutionSucceeded());
		FlexoConceptInstance book3 = createBook3.getFlexoConceptInstance();
		assertNotNull(book3);
		assertEquals(bookConcept, book3.getFlexoConcept());

		assertTrue(libraryVMI.isModified());

		libraryVMI.getResource().save(null);

		assertFalse(libraryVMI.isModified());

		VirtualModelInstanceResource vmiRes = (VirtualModelInstanceResource) libraryVMI.getResource();
		System.out.println(vmiRes.getFactory().stringRepresentation(vmiRes.getLoadedResourceData()));

	}

	/**
	 * Instantiate in project a VirtualModelInstance conform to the VirtualModel
	 */
	@Test
	@TestOrder(10)
	public void testInstanciateDocumentVMI() {

		log("testInstanciateDocumentVMI()");

		assertEquals(2, documentVirtualModel.getModelSlots().size());

		assertEquals(1, documentVirtualModel.getModelSlots(FMLRTModelSlot.class).size());
		FMLRTModelSlot libraryModelSlot = documentVirtualModel.getModelSlots(FMLRTModelSlot.class).get(0);
		assertNotNull(libraryModelSlot);

		assertEquals(1, documentVirtualModel.getModelSlots(DocXModelSlot.class).size());
		DocXModelSlot docXModelSlot = documentVirtualModel.getModelSlots(DocXModelSlot.class).get(0);
		assertNotNull(docXModelSlot);

		assertTrue(documentVirtualModel.getModelSlots().contains(libraryModelSlot));
		assertTrue(documentVirtualModel.getModelSlots().contains(docXModelSlot));

		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType.makeNewAction(newView, null, editor);
		action.setNewVirtualModelInstanceName("GeneratedDocumentVMI");
		action.setNewVirtualModelInstanceTitle("Test creation of a new VirtualModelInstance for document generation");
		action.setVirtualModel(documentVirtualModel);

		FMLRTModelSlotInstanceConfiguration libraryModelSlotInstanceConfiguration = (FMLRTModelSlotInstanceConfiguration) action
				.getModelSlotInstanceConfiguration(libraryModelSlot);
		assertNotNull(libraryModelSlotInstanceConfiguration);
		libraryModelSlotInstanceConfiguration.setOption(DefaultModelSlotInstanceConfigurationOption.SelectExistingVirtualModel);
		libraryModelSlotInstanceConfiguration
				.setAddressedVirtualModelInstanceResource((VirtualModelInstanceResource) libraryVMI.getResource());
		assertTrue(libraryModelSlotInstanceConfiguration.isValidConfiguration());

		DocXModelSlotInstanceConfiguration docXModelSlotInstanceConfiguration = (DocXModelSlotInstanceConfiguration) action
				.getModelSlotInstanceConfiguration(docXModelSlot);
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
		documentVMI = action.getNewVirtualModelInstance();
		assertNotNull(documentVMI);
		assertNotNull(documentVMI.getResource());
		assertTrue(ResourceLocator.retrieveResourceAsFile(((ViewResource) newView.getResource()).getDirectory()).exists());
		assertTrue(((ViewResource) newView.getResource()).getFlexoIODelegate().exists());
		assertEquals(2, documentVMI.getModelSlotInstances().size());

		FreeModelSlotInstance<DocXDocument, DocXModelSlot> docXMSInstance = (FreeModelSlotInstance<DocXDocument, DocXModelSlot>) documentVMI
				.getModelSlotInstances().get(1);
		assertNotNull(docXMSInstance);
		// Only resource was created, resource data remains null here
		assertNull(docXMSInstance.getAccessedResourceData());
		assertNotNull(docXMSInstance.getResource());

		// The VMI does not have the FMLControlledDocumentVirtualModelInstanceNature yet, because document still null
		assertFalse(documentVMI.hasNature(FMLControlledDocumentVirtualModelInstanceNature.INSTANCE));

		assertNotNull(FMLControlledDocumentVirtualModelInstanceNature.getModelSlotInstance(documentVMI));
		assertNotNull(FMLControlledDocumentVirtualModelInstanceNature.getModelSlotInstance(documentVMI).getModelSlot());

		// assertFalse(generatedDocument.isModified());
		assertFalse(documentVMI.isModified());

	}

	/**
	 * Try to generate the document in documentVMI
	 * 
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(11)
	public void testGenerateDocument() throws SaveResourceException {

		log("testGenerateDocument()");

		VirtualModelInstanceResource vmiRes = (VirtualModelInstanceResource) documentVMI.getResource();

		assertFalse(templateResource.isModified());
		// assertFalse(libraryVMI.isModified());

		System.out.println(vmiRes.getFactory().stringRepresentation(vmiRes.getLoadedResourceData()));

		ActionSchemeActionType actionType = new ActionSchemeActionType(generateDocumentActionScheme, documentVMI);
		ActionSchemeAction actionSchemeCreationAction = actionType.makeNewAction(documentVMI, null, editor);
		assertNotNull(actionSchemeCreationAction);
		actionSchemeCreationAction.doAction();
		assertTrue(actionSchemeCreationAction.hasActionExecutionSucceeded());

		System.out.println(vmiRes.getFactory().stringRepresentation(vmiRes.getLoadedResourceData()));

		FreeModelSlotInstance<DocXDocument, DocXModelSlot> docXMSInstance = (FreeModelSlotInstance<DocXDocument, DocXModelSlot>) documentVMI
				.getModelSlotInstances().get(1);
		assertNotNull(docXMSInstance);

		assertNotNull(docXMSInstance.getResource());
		// Resource data is the generated document now, and is not null
		assertNotNull(generatedDocument = docXMSInstance.getAccessedResourceData());

		// The VMI has now the FMLControlledDocumentVirtualModelInstanceNature yet, because document not null anymore
		assertTrue(documentVMI.hasNature(FMLControlledDocumentVirtualModelInstanceNature.INSTANCE));

		assertNotNull(FMLControlledDocumentVirtualModelInstanceNature.getModelSlotInstance(documentVMI));
		assertNotNull(FMLControlledDocumentVirtualModelInstanceNature.getModelSlotInstance(documentVMI).getModelSlot());

		documentVMI.getResource().save(null);
		newView.getResource().save(null);

		// assertTrue(generatedDocument.isModified());
		// assertFalse(newVirtualModelInstance.isModified());

		System.out.println("Generated document:\n" + generatedDocument.debugStructuredContents());

		generatedDocument.getResource().save(null);
		assertFalse(generatedDocument.isModified());

		assertEquals(14, generatedDocument.getElements().size());

	}

	/**
	 * Try to update the document from data
	 * 
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(12)
	public void testUpdateDocument() throws SaveResourceException {

		log("testUpdateDocument()");

		VirtualModelInstanceResource vmiRes = (VirtualModelInstanceResource) documentVMI.getResource();

		assertFalse(templateResource.isModified());

		ActionSchemeActionType actionType = new ActionSchemeActionType(updateDocumentActionScheme, documentVMI);
		ActionSchemeAction actionSchemeCreationAction = actionType.makeNewAction(documentVMI, null, editor);
		assertNotNull(actionSchemeCreationAction);
		actionSchemeCreationAction.doAction();
		assertTrue(actionSchemeCreationAction.hasActionExecutionSucceeded());

		System.out.println(vmiRes.getFactory().stringRepresentation(vmiRes.getLoadedResourceData()));

		FreeModelSlotInstance<DocXDocument, DocXModelSlot> docXMSInstance = (FreeModelSlotInstance<DocXDocument, DocXModelSlot>) documentVMI
				.getModelSlotInstances().get(1);
		assertNotNull(docXMSInstance);

		assertNotNull(docXMSInstance.getResource());
		// Resource data is the generated document now, and is not null
		assertNotNull(generatedDocument = docXMSInstance.getAccessedResourceData());

		// The VMI has the FMLControlledDocumentVirtualModelInstanceNature yet, because document not null anymore
		assertTrue(documentVMI.hasNature(FMLControlledDocumentVirtualModelInstanceNature.INSTANCE));

		assertNotNull(FMLControlledDocumentVirtualModelInstanceNature.getModelSlotInstance(documentVMI));
		assertNotNull(FMLControlledDocumentVirtualModelInstanceNature.getModelSlotInstance(documentVMI).getModelSlot());

		documentVMI.getResource().save(null);
		newView.getResource().save(null);

		// assertTrue(generatedDocument.isModified());
		// assertFalse(newVirtualModelInstance.isModified());

		System.out.println("Generated document:\n" + generatedDocument.debugStructuredContents());

		generatedDocument.getResource().save(null);
		assertFalse(generatedDocument.isModified());

		assertEquals(29, generatedDocument.getElements().size());

		assertEquals(3, documentVMI.getFlexoConceptInstances().size());

		System.out.println("Template resource = " + docXModelSlot.getTemplateResource());
		System.out.println("generatedDocument = " + generatedDocument.getResource());

	}

	/**
	 */
	/*@Test
	@TestOrder(12)
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
		assertNotNull(libraryVMI = vmiResource.getVirtualModelInstance());
	
		assertTrue(libraryVMI.getVirtualModel().hasNature(FMLControlledDocumentVirtualModelNature.INSTANCE));
	
		assertTrue(libraryVMI.hasNature(FMLControlledDocumentVirtualModelInstanceNature.INSTANCE));
	
		ModelSlotInstance<DocXModelSlot, DocXDocument> msInstance = FMLControlledDocumentVirtualModelInstanceNature
				.getModelSlotInstance(libraryVMI);
	
		assertNotNull(msInstance);
		assertNotNull(msInstance.getAccessedResourceData());
	
		generatedDocument = msInstance.getAccessedResourceData();
	
		assertNotSame(generatedDocumentBeforeReload, generatedDocument);
	
		assertEquals(18, generatedDocument.getElements().size());
	
	}*/
}
