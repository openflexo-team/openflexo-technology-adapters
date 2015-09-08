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
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.StringTokenizer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.doc.FlexoDocumentFragment.FragmentConsistencyException;
import org.openflexo.foundation.doc.TextSelection;
import org.openflexo.foundation.doc.fml.TextBinding;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.FlexoBehaviourParameter;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceParameter;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.GetSetProperty;
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
import org.openflexo.foundation.fml.action.CreateGetSetProperty;
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
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
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
import org.openflexo.technologyadapter.docx.fml.editionaction.AddDocXFragment.LocationSemantics;
import org.openflexo.technologyadapter.docx.fml.editionaction.ApplyTextBindings;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXDocument;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXTable;
import org.openflexo.technologyadapter.docx.fml.editionaction.ReinjectTextBindings;
import org.openflexo.technologyadapter.docx.fml.editionaction.SelectGeneratedDocXFragment;
import org.openflexo.technologyadapter.docx.fml.editionaction.SelectGeneratedDocXTable;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXElement;
import org.openflexo.technologyadapter.docx.model.DocXFragment;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;
import org.openflexo.technologyadapter.docx.model.DocXRun;
import org.openflexo.technologyadapter.docx.model.DocXTable;
import org.openflexo.technologyadapter.docx.model.DocXTableCell;
import org.openflexo.technologyadapter.docx.model.DocXUtils;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.toolbox.StringUtils;

import junit.framework.AssertionFailedError;

/**
 * Test the creation and some manipulations of a {@link VirtualModel} with {@link FMLControlledDocumentVirtualModelNature}<br>
 * We create here a {@link VirtualModel} storing a library containing books.<br>
 * We then generate a document where the whole library is described.<br>
 * We test then some manipulations on the model, generated document, and template.
 * 
 * Those tests complete those which are defined in {@link TestLibrary}
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestLibrary2 extends OpenflexoProjectAtRunTimeTestCase {

	private final String VIEWPOINT_NAME = "TestLibraryViewPoint2";
	private final String VIEWPOINT_URI = "http://openflexo.org/test/TestLibraryViewPoint2";

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

	public static GetSetProperty<?> allBooksProperty;

	public static DocXFragmentRole introductionFragmentRole;
	public static DocXFragmentRole booksDescriptionFragmentRole;
	public static DocXTableRole bookListingTableRole;
	public static DocXFragmentRole conclusionFragmentRole;
	public static FlexoConcept bookDescriptionSection;
	public static CreationScheme bookDescriptionSectionCreationScheme;
	public static ActionScheme bookDescriptionSectionUpdateScheme;
	public static ActionScheme bookDescriptionSectionReinjectScheme;

	public static ActionScheme generateDocumentActionScheme;
	public static ActionScheme updateDocumentActionScheme;
	public static ActionScheme reinjectFromDocumentActionScheme;

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

		templateResource = getDocument("ExampleLibrary2.docx");

		assertNotNull(templateDocument = templateResource.getResourceData(null));

		System.out.println(templateDocument.debugStructuredContents());

		assertEquals(50, templateDocument.getElements().size());

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
	@TestOrder(3)
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
	@TestOrder(4)
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
	 * VirtualModel LibraryVirtualModel type=VirtualModel uri="http://openflexo.org/test/TestLibraryViewPoint/LibraryVirtualModel" {
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
	@TestOrder(5)
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

		CreateGetSetProperty createAllBooksProperty = CreateGetSetProperty.actionType.makeNewAction(libraryVirtualModel, null, editor);
		createAllBooksProperty.setPropertyName("books");

		SelectFlexoConceptInstance selectBooks = bookConcept.getFMLModelFactory().newSelectFlexoConceptInstance();
		selectBooks.setVirtualModelInstance(new DataBinding<VirtualModelInstance>("virtualModelInstance"));
		selectBooks.setFlexoConceptType(bookConcept);
		createAllBooksProperty.setGetControlGraph(bookConcept.getFMLModelFactory().newReturnStatement(selectBooks));

		createAllBooksProperty.doAction();
		assertTrue(createAllBooksProperty.hasActionExecutionSucceeded());

		assertTrue(selectBooks.getVirtualModelInstance().isValid());

		allBooksProperty = createAllBooksProperty.getNewFlexoProperty();
		assertNotNull(allBooksProperty);

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
	 * Following is the verbatim of DocumentVirtualModel
	 * 
	 * <code>
	VirtualModel DocumentVirtualModel type=VirtualModel uri="http://openflexo.org/test/TestLibraryViewPoint2/DocumentVirtualModel" {
	 
	  ModelSlot library as FML::FMLRTModelSlot conformTo http://openflexo.org/test/TestLibraryViewPoint2/LibraryVirtualModel required=true readOnly=false;  
	  ModelSlot document as DOCX::DocXModelSlot conformTo http://openflexo.org/test/TestResourceCenter/ExampleLibrary2.docx required=true readOnly=false;  
	
	  FlexoRole introductionSection as DOCX::DocXFragmentRole conformTo DocXFragment(http://openflexo.org/test/TestResourceCenter/ExampleLibrary2.docx:707AC6F7:3D899DD8);
	  FlexoRole booksDescriptionSection as DOCX::DocXFragmentRole conformTo DocXFragment(http://openflexo.org/test/TestResourceCenter/ExampleLibrary2.docx:78BBD0DD:5052C24D);
	  FlexoRole bookListingTable as DOCX::DocXTableRole conformTo DocXTable;
	  FlexoRole conclusionSection as DOCX::DocXFragmentRole conformTo DocXFragment(http://openflexo.org/test/TestResourceCenter/ExampleLibrary2.docx:1A787643:2B774AAC);
	
	  FML::ActionScheme generateDocument() {  
	    document.DOCX::GenerateDocXDocument()    
	    introductionSection = document.DOCX::SelectGeneratedDocXFragment();    
	    booksDescriptionSection = document.DOCX::SelectGeneratedDocXFragment();    
	    conclusionSection = document.DOCX::SelectGeneratedDocXFragment();  
	  }  
	
	  FML::ActionScheme updateDocument() {  
	    for (book : FML::SelectFlexoConceptInstance from library as Book) {    
	      FML::MatchFlexoConceptInstance as BookDescriptionSection match (book=book;section=;) using BookDescriptionSection:createBookDescriptionSection(book)    
	    }    
	    for (bookSection : FML::SelectFlexoConceptInstance from library as BookDescriptionSection) {    
	      bookSection.updateBookDescriptionSection()    
	    }    
	    bookListingTable.DOCX::GenerateDocXTable()  
	  }  
	
	  FML::ActionScheme reinjectFromDocument() {  
	    for (bookSection : FML::SelectFlexoConceptInstance from library as BookDescriptionSection) {    
	      bookSection.reinjectDataFromBookDescriptionSection()    
	    }  
	  }  
	
	  FlexoConcept BookDescriptionSection {  
	
	    FlexoRole book as FlexoConceptInstance conformTo Book;  
	    FlexoRole section as DOCX::DocXFragmentRole conformTo DocXFragment(http://openflexo.org/test/TestResourceCenter/ExampleLibrary2.docx:2936B416:395C1CE1);  
	
	    FML::CreationScheme createBookDescriptionSection(FlexoConceptInstanceType<> aBook) {    
	      book = parameters.aBook;      
	      section = document.DOCX::AddDocXFragment();      
	      section.DOCX::ApplyTextBindings()    
	    }    
	
	    FML::ActionScheme updateBookDescriptionSection() {    
	      section.DOCX::ApplyTextBindings()    
	    }    
	
	    FML::ActionScheme reinjectDataFromBookDescriptionSection() {    
	      section.DOCX::ReinjectTextBindings()    
	    }    
	
	  }  
	
	}
	</code>
	 * 
	 * @throws FragmentConsistencyException
	 */
	@Test
	@TestOrder(6)
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
		// createIntroductionSectionRole.setModelSlot(docXModelSlot);
		createIntroductionSectionRole.setFlexoRoleClass(DocXFragmentRole.class);
		assertEquals(docXModelSlot, createIntroductionSectionRole.getModelSlot());
		createIntroductionSectionRole.doAction();
		assertTrue(createIntroductionSectionRole.hasActionExecutionSucceeded());
		introductionFragmentRole = (DocXFragmentRole) createIntroductionSectionRole.getNewFlexoRole();
		DocXParagraph startParagraph1 = (DocXParagraph) templateDocument.getElements().get(7);
		DocXParagraph endParagraph1 = (DocXParagraph) templateDocument.getElements().get(16);
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
		// createBooksDescriptionSectionRole.setModelSlot(docXModelSlot);
		createBooksDescriptionSectionRole.setFlexoRoleClass(DocXFragmentRole.class);
		assertEquals(docXModelSlot, createBooksDescriptionSectionRole.getModelSlot());
		createBooksDescriptionSectionRole.doAction();
		assertTrue(createBooksDescriptionSectionRole.hasActionExecutionSucceeded());
		booksDescriptionFragmentRole = (DocXFragmentRole) createBooksDescriptionSectionRole.getNewFlexoRole();
		DocXParagraph startParagraph2 = (DocXParagraph) templateDocument.getElements().get(17);
		DocXParagraph endParagraph2 = (DocXParagraph) templateDocument.getElements().get(32);
		System.out.println("BooksDescription:");
		System.out.println("start=" + startParagraph2.getIdentifier() + " " + startParagraph2.getRawText());
		System.out.println("end=" + endParagraph2.getIdentifier() + endParagraph2.getRawText());

		DocXFragment booksDescriptionFragment = (DocXFragment) templateResource.getFactory().makeFragment(startParagraph2, endParagraph2);
		booksDescriptionFragmentRole.setFragment(booksDescriptionFragment);
		assertEquals(booksDescriptionFragmentRole.getFragment(), booksDescriptionFragment);

		// We create a role pointing to the book listing table
		CreateTechnologyRole createTableRole = CreateTechnologyRole.actionType.makeNewAction(documentVirtualModel, null, editor);
		createTableRole.setRoleName("bookListingTable");
		createTableRole.setFlexoRoleClass(DocXTableRole.class);
		assertEquals(docXModelSlot, createTableRole.getModelSlot());
		createTableRole.doAction();
		assertTrue(createTableRole.hasActionExecutionSucceeded());
		bookListingTableRole = (DocXTableRole) createTableRole.getNewFlexoRole();
		DocXTable bookListingTable = (DocXTable) templateDocument.getElements().get(21);
		System.out.println("bookListingTable=" + DocXUtils.debugStructuredContents(bookListingTable, 2));
		bookListingTableRole.setTable(bookListingTable);
		assertEquals(bookListingTableRole.getTable(), bookListingTable);

		bookListingTableRole.setStartIterationIndex(1);
		bookListingTableRole.setEndIterationIndex(2);
		bookListingTableRole.setIteration(new DataBinding("library.books"));

		assertTrue(bookListingTableRole.getIteration().isValid());

		// Title
		// TextSelection<DocXDocument, DocXTechnologyAdapter> titleSelection = bookDescriptionFragment.makeTextSelection(titleParagraph, 0,
		// 1);
		// assertEquals("Les misérables", titleSelection.getRawText());
		// TextBinding<DocXDocument, DocXTechnologyAdapter> titleBinding = sectionRole.makeTextBinding(titleSelection,
		// new DataBinding<String>("book.title"));
		// assertTrue(titleBinding.getValue().isValid());

		// We create a role pointing to the third section (conclusion section)
		CreateTechnologyRole createConclusionSectionRole = CreateTechnologyRole.actionType.makeNewAction(documentVirtualModel, null,
				editor);
		createConclusionSectionRole.setRoleName("conclusionSection");
		// createConclusionSectionRole.setModelSlot(docXModelSlot);
		createConclusionSectionRole.setFlexoRoleClass(DocXFragmentRole.class);
		assertEquals(docXModelSlot, createConclusionSectionRole.getModelSlot());
		createConclusionSectionRole.doAction();
		assertTrue(createConclusionSectionRole.hasActionExecutionSucceeded());
		conclusionFragmentRole = (DocXFragmentRole) createConclusionSectionRole.getNewFlexoRole();
		DocXParagraph startParagraph3 = (DocXParagraph) templateDocument.getElements().get(33);
		DocXParagraph endParagraph3 = (DocXParagraph) templateDocument.getElements().get(49);
		System.out.println("Conclusion:");
		System.out.println("start=" + startParagraph3.getRawText());
		System.out.println("end=" + endParagraph3.getRawText());

		DocXFragment conclusionFragment = (DocXFragment) templateResource.getFactory().makeFragment(startParagraph3, endParagraph3);
		conclusionFragmentRole.setFragment(conclusionFragment);
		assertEquals(conclusionFragmentRole.getFragment(), conclusionFragment);

		bookDescriptionSection = createBookDescriptionSection();

		generateDocumentActionScheme = createGenerateDocument();
		updateDocumentActionScheme = createUpdateDocument();
		reinjectFromDocumentActionScheme = createReinjectFromDocument();

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
		// createSectionRole.setModelSlot(docXModelSlot);
		createSectionRole.setFlexoRoleClass(DocXFragmentRole.class);
		assertEquals(docXModelSlot, createSectionRole.getModelSlot());
		createSectionRole.doAction();
		assertTrue(createSectionRole.hasActionExecutionSucceeded());
		DocXFragmentRole sectionRole = (DocXFragmentRole) createSectionRole.getNewFlexoRole();

		DocXParagraph titleParagraph = (DocXParagraph) templateDocument.getElements().get(23);
		DocXTable bookDescriptionTable = (DocXTable) templateDocument.getElements().get(25);
		DocXParagraph descriptionParagraph = (DocXParagraph) templateDocument.getElements().get(27);
		DocXParagraph descriptionParagraph2 = (DocXParagraph) templateDocument.getElements().get(28);

		System.out.println("titleParagraph=" + titleParagraph.getRawText());
		System.out.println("bookDescriptionTable=" + bookDescriptionTable);
		System.out.println("descriptionParagraph=" + descriptionParagraph.getRawText());
		System.out.println("descriptionParagraph2=" + descriptionParagraph2.getRawText());

		DocXFragment bookDescriptionFragment = (DocXFragment) templateResource.getFactory().makeFragment(titleParagraph,
				descriptionParagraph2);
		sectionRole.setFragment(bookDescriptionFragment);
		assertEquals(sectionRole.getFragment(), bookDescriptionFragment);
		assertEquals(docXModelSlot, sectionRole.getModelSlot());

		for (DocXElement element : bookDescriptionFragment.getElements()) {
			System.out.println(DocXUtils.debugStructuredContents(element, 2));
		}

		// Here is the structuration of original fragment (bookDescriptionFragment):

		// > { (Les )(misérables) }
		// > { }
		// > [Auteur] [Victor Hugo]
		// ..[Edition] [Dunod]
		// ..[Type] [Roman]
		// > { }
		// > { (Les Misérables est un roman de Victor Hugo paru en 1862 (la première partie est publiée le 30 mars à Bruxelles par les
		// Éditions Lacroix, )(Verboeckhoven)( et Cie, et le 3 avril de la même année à Paris1).) }
		// > { (Dans ce roman, un des plus emblématiques de la littérature française, Victor Hugo décrit la vie de misérables dans Paris et
		// la France provinciale du )(xixe)( siècle et s'attache plus particulièrement aux pas du bagnard Jean )(Valjean)(.) }

		DocXTableCell authorCell = (DocXTableCell) bookDescriptionTable.getCell(0, 1);
		DocXParagraph authorParagraph = (DocXParagraph) authorCell.getParagraphs().get(0);

		DocXTableCell editionCell = (DocXTableCell) bookDescriptionTable.getCell(1, 1);
		DocXParagraph editionParagraph = (DocXParagraph) editionCell.getParagraphs().get(0);

		DocXTableCell typeCell = (DocXTableCell) bookDescriptionTable.getCell(2, 1);
		DocXParagraph typeParagraph = (DocXParagraph) typeCell.getParagraphs().get(0);

		assertEquals(2, titleParagraph.getRuns().size());
		assertEquals(1, authorParagraph.getRuns().size());
		assertEquals(2, editionParagraph.getRuns().size());
		assertEquals(1, typeParagraph.getRuns().size());
		assertEquals(3, descriptionParagraph.getRuns().size());
		assertEquals(5, descriptionParagraph2.getRuns().size());

		// Title
		TextSelection<DocXDocument, DocXTechnologyAdapter> titleSelection = bookDescriptionFragment.makeTextSelection(titleParagraph, 0, 1);
		assertEquals("Les misérables", titleSelection.getRawText());
		TextBinding<DocXDocument, DocXTechnologyAdapter> titleBinding = sectionRole.makeTextBinding(titleSelection,
				new DataBinding<String>("book.title"));
		assertTrue(titleBinding.getValue().isValid());

		// Author
		TextSelection<DocXDocument, DocXTechnologyAdapter> authorSelection = bookDescriptionFragment.makeTextSelection(authorParagraph, 0,
				0);
		assertEquals("Victor Hugo", authorSelection.getRawText());
		TextBinding<DocXDocument, DocXTechnologyAdapter> authorBinding = sectionRole.makeTextBinding(authorSelection,
				new DataBinding<String>("book.author"));
		assertTrue(authorBinding.getValue().isValid());

		// Edition
		TextSelection<DocXDocument, DocXTechnologyAdapter> editionSelection = bookDescriptionFragment.makeTextSelection(editionParagraph, 0,
				1);
		assertEquals("Editions Dunod", editionSelection.getRawText());
		TextBinding<DocXDocument, DocXTechnologyAdapter> editionBinding = sectionRole.makeTextBinding(editionSelection,
				new DataBinding<String>("book.edition"));
		assertTrue(editionBinding.getValue().isValid());

		// Type
		TextSelection<DocXDocument, DocXTechnologyAdapter> typeSelection = bookDescriptionFragment.makeTextSelection(typeParagraph, 0, 0);
		assertEquals("Roman", typeSelection.getRawText());
		TextBinding<DocXDocument, DocXTechnologyAdapter> typeBinding = sectionRole.makeTextBinding(typeSelection,
				new DataBinding<String>("book.type"));
		assertTrue(typeBinding.getValue().isValid());

		// Description
		TextSelection<DocXDocument, DocXTechnologyAdapter> descriptionSelection = bookDescriptionFragment
				.makeTextSelection(descriptionParagraph, descriptionParagraph2);
		TextBinding<DocXDocument, DocXTechnologyAdapter> descriptionBinding = sectionRole.makeTextBinding(descriptionSelection,
				new DataBinding<String>("book.description"), true);
		assertTrue(descriptionBinding.getValue().isValid());

		// Create bookDescriptionSectionCreationScheme
		CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewAction(bookDescriptionSection, null, editor);
		createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
		createCreationScheme.setFlexoBehaviourName("createBookDescriptionSection");
		createCreationScheme.doAction();
		bookDescriptionSectionCreationScheme = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();

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
		createFragment.setLocationSemantics(LocationSemantics.InsertBeforeLastChild);
		createFragment.setLocation(new DataBinding<DocXParagraph>("booksDescriptionSection.startElement"));
		assertTrue(createFragment.getLocation().isValid());

		CreateEditionAction applyTextBindingsAction = CreateEditionAction.actionType
				.makeNewAction(bookDescriptionSectionCreationScheme.getControlGraph(), null, editor);
		applyTextBindingsAction.setFlexoRole(sectionRole);
		applyTextBindingsAction.setEditionActionClass(ApplyTextBindings.class);
		applyTextBindingsAction.doAction();
		assertTrue(applyTextBindingsAction.hasActionExecutionSucceeded());
		ApplyTextBindings applyTextBindings = (ApplyTextBindings) applyTextBindingsAction.getNewEditionAction();

		assertNotNull(applyTextBindings);

		assertTrue(bookDescriptionSection.getCreationSchemes().contains(bookDescriptionSectionCreationScheme));

		// Create bookDescriptionSectionUpdateScheme
		CreateFlexoBehaviour createUpdateScheme = CreateFlexoBehaviour.actionType.makeNewAction(bookDescriptionSection, null, editor);
		createUpdateScheme.setFlexoBehaviourClass(ActionScheme.class);
		createUpdateScheme.setFlexoBehaviourName("updateBookDescriptionSection");
		createUpdateScheme.doAction();
		bookDescriptionSectionUpdateScheme = (ActionScheme) createUpdateScheme.getNewFlexoBehaviour();

		CreateEditionAction applyTextBindingsAction2 = CreateEditionAction.actionType
				.makeNewAction(bookDescriptionSectionUpdateScheme.getControlGraph(), null, editor);
		applyTextBindingsAction2.setFlexoRole(sectionRole);
		applyTextBindingsAction2.setEditionActionClass(ApplyTextBindings.class);
		applyTextBindingsAction2.doAction();
		assertTrue(applyTextBindingsAction2.hasActionExecutionSucceeded());
		ApplyTextBindings applyTextBindings2 = (ApplyTextBindings) applyTextBindingsAction.getNewEditionAction();
		assertNotNull(applyTextBindings2);

		assertTrue(bookDescriptionSection.getActionSchemes().contains(bookDescriptionSectionUpdateScheme));

		// Create bookDescriptionSectionUpdateScheme
		CreateFlexoBehaviour createReinjectScheme = CreateFlexoBehaviour.actionType.makeNewAction(bookDescriptionSection, null, editor);
		createReinjectScheme.setFlexoBehaviourClass(ActionScheme.class);
		createReinjectScheme.setFlexoBehaviourName("reinjectDataFromBookDescriptionSection");
		createReinjectScheme.doAction();
		bookDescriptionSectionReinjectScheme = (ActionScheme) createReinjectScheme.getNewFlexoBehaviour();

		CreateEditionAction reinjectTextBindingsAction = CreateEditionAction.actionType
				.makeNewAction(bookDescriptionSectionReinjectScheme.getControlGraph(), null, editor);
		reinjectTextBindingsAction.setFlexoRole(sectionRole);
		reinjectTextBindingsAction.setEditionActionClass(ReinjectTextBindings.class);
		reinjectTextBindingsAction.doAction();
		assertTrue(reinjectTextBindingsAction.hasActionExecutionSucceeded());
		ReinjectTextBindings reinjectAction = (ReinjectTextBindings) reinjectTextBindingsAction.getNewEditionAction();
		assertNotNull(reinjectAction);

		assertTrue(bookDescriptionSection.getActionSchemes().contains(bookDescriptionSectionReinjectScheme));

		return bookDescriptionSection;
	}

	private ActionScheme createGenerateDocument() {

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

		CreateEditionAction createSelectIntroductionSection = CreateEditionAction.actionType
				.makeNewAction(generateDocumentActionScheme.getControlGraph(), null, editor);
		createSelectIntroductionSection.setEditionActionClass(SelectGeneratedDocXFragment.class);
		createSelectIntroductionSection.setAssignation(new DataBinding<Object>("introductionSection"));
		createSelectIntroductionSection.doAction();
		AssignationAction<?> action1 = (AssignationAction<?>) createSelectIntroductionSection.getNewEditionAction();
		assertTrue(action1.getAssignation().isValid());

		CreateEditionAction createSelectBooksDescriptionSection = CreateEditionAction.actionType
				.makeNewAction(generateDocumentActionScheme.getControlGraph(), null, editor);
		createSelectBooksDescriptionSection.setEditionActionClass(SelectGeneratedDocXFragment.class);
		createSelectBooksDescriptionSection.setAssignation(new DataBinding<Object>("booksDescriptionSection"));
		createSelectBooksDescriptionSection.doAction();
		AssignationAction<?> action2 = (AssignationAction<?>) createSelectBooksDescriptionSection.getNewEditionAction();
		assertTrue(action2.getAssignation().isValid());

		CreateEditionAction createSelectConclusionSection = CreateEditionAction.actionType
				.makeNewAction(generateDocumentActionScheme.getControlGraph(), null, editor);
		createSelectConclusionSection.setEditionActionClass(SelectGeneratedDocXFragment.class);
		createSelectConclusionSection.setAssignation(new DataBinding<Object>("conclusionSection"));
		createSelectConclusionSection.doAction();
		AssignationAction<?> action3 = (AssignationAction<?>) createSelectConclusionSection.getNewEditionAction();
		assertTrue(action3.getAssignation().isValid());

		CreateEditionAction createSelectTable = CreateEditionAction.actionType.makeNewAction(generateDocumentActionScheme.getControlGraph(),
				null, editor);
		createSelectTable.setEditionActionClass(SelectGeneratedDocXTable.class);
		createSelectTable.setAssignation(new DataBinding<Object>("bookListingTable"));
		createSelectTable.doAction();
		AssignationAction<?> action4 = (AssignationAction<?>) createSelectTable.getNewEditionAction();
		assertTrue(action4.getAssignation().isValid());

		return generateDocumentActionScheme;

	}

	private ActionScheme createUpdateDocument() {

		// We programmatically implement this code:
		// ActionScheme updateDocument() {
		// ..for (book : SelectFlexoConceptInstance from library as Book) {
		// ....MatchFlexoConceptInstance as BookDescriptionSection match (book=book;section=;) using
		// BookDescriptionSection:createBookDescriptionSection(book)
		// ..}
		// }

		// We create an ActionScheme allowing to update docXDocument
		CreateFlexoBehaviour createActionScheme = CreateFlexoBehaviour.actionType.makeNewAction(documentVirtualModel, null, editor);
		createActionScheme.setFlexoBehaviourName("updateDocument");
		createActionScheme.setFlexoBehaviourClass(ActionScheme.class);
		createActionScheme.doAction();
		assertTrue(createActionScheme.hasActionExecutionSucceeded());
		ActionScheme updateDocumentActionScheme = (ActionScheme) createActionScheme.getNewFlexoBehaviour();

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

		CreateEditionAction createSelectFetchRequestIterationAction2 = CreateEditionAction.actionType
				.makeNewAction(updateDocumentActionScheme.getControlGraph(), null, editor);
		// createSelectFetchRequestIterationAction.actionChoice = CreateEditionActionChoice.ControlAction;
		createSelectFetchRequestIterationAction2.setEditionActionClass(IterationAction.class);
		createSelectFetchRequestIterationAction2.doAction();
		IterationAction fetchRequestIteration2 = (IterationAction) createSelectFetchRequestIterationAction2.getNewEditionAction();
		fetchRequestIteration2.setIteratorName("bookSection");

		SelectFlexoConceptInstance selectFlexoConceptInstance2 = fetchRequestIteration.getFMLModelFactory().newSelectFlexoConceptInstance();
		selectFlexoConceptInstance2.setVirtualModelInstance(new DataBinding<VirtualModelInstance>("virtualModelInstance"));
		selectFlexoConceptInstance2.setFlexoConceptType(bookDescriptionSection);
		fetchRequestIteration2.setIterationAction(selectFlexoConceptInstance2);

		CreateEditionAction createUpdateAction = CreateEditionAction.actionType.makeNewAction(fetchRequestIteration2.getControlGraph(),
				null, editor);
		createUpdateAction.setEditionActionClass(ExpressionAction.class);
		createUpdateAction.doAction();
		ExpressionAction<?> updateExpression = (ExpressionAction<?>) createUpdateAction.getNewEditionAction();
		updateExpression.setExpression(new DataBinding("bookSection.updateBookDescriptionSection()"));
		assertTrue(updateExpression.getExpression().isValid());

		CreateEditionAction generateTableAction = CreateEditionAction.actionType.makeNewAction(updateDocumentActionScheme.getControlGraph(),
				null, editor);
		generateTableAction.setFlexoRole(bookListingTableRole);
		generateTableAction.setEditionActionClass(GenerateDocXTable.class);
		generateTableAction.doAction();
		assertTrue(generateTableAction.hasActionExecutionSucceeded());
		GenerateDocXTable generateTable = (GenerateDocXTable) generateTableAction.getNewEditionAction();
		assertNotNull(generateTable);

		return updateDocumentActionScheme;
	}

	private ActionScheme createReinjectFromDocument() {

		// We create an ActionScheme allowing to update docXDocument
		CreateFlexoBehaviour createActionScheme = CreateFlexoBehaviour.actionType.makeNewAction(documentVirtualModel, null, editor);
		createActionScheme.setFlexoBehaviourName("reinjectFromDocument");
		createActionScheme.setFlexoBehaviourClass(ActionScheme.class);
		createActionScheme.doAction();
		assertTrue(createActionScheme.hasActionExecutionSucceeded());
		ActionScheme reinjectDocumentActionScheme = (ActionScheme) createActionScheme.getNewFlexoBehaviour();

		CreateEditionAction createSelectFetchRequestIterationAction = CreateEditionAction.actionType
				.makeNewAction(reinjectDocumentActionScheme.getControlGraph(), null, editor);
		// createSelectFetchRequestIterationAction.actionChoice = CreateEditionActionChoice.ControlAction;
		createSelectFetchRequestIterationAction.setEditionActionClass(IterationAction.class);
		createSelectFetchRequestIterationAction.doAction();
		IterationAction fetchRequestIteration = (IterationAction) createSelectFetchRequestIterationAction.getNewEditionAction();
		fetchRequestIteration.setIteratorName("bookSection");

		SelectFlexoConceptInstance selectFlexoConceptInstance = fetchRequestIteration.getFMLModelFactory().newSelectFlexoConceptInstance();
		selectFlexoConceptInstance.setVirtualModelInstance(new DataBinding<VirtualModelInstance>("virtualModelInstance"));
		selectFlexoConceptInstance.setFlexoConceptType(bookDescriptionSection);
		fetchRequestIteration.setIterationAction(selectFlexoConceptInstance);

		CreateEditionAction createReinjectAction = CreateEditionAction.actionType.makeNewAction(fetchRequestIteration.getControlGraph(),
				null, editor);
		createReinjectAction.setEditionActionClass(ExpressionAction.class);
		createReinjectAction.doAction();
		ExpressionAction<?> reinjectExpression = (ExpressionAction<?>) createReinjectAction.getNewEditionAction();
		reinjectExpression.setExpression(new DataBinding("bookSection.reinjectDataFromBookDescriptionSection()"));
		assertTrue(reinjectExpression.getExpression().isValid());

		return reinjectDocumentActionScheme;
	}

	/**
	 * Instantiate in project a View conform to the ViewPoint
	 */
	@Test
	@TestOrder(7)
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

	public static final String LES_MISERABLES_DESCRIPTION = "Les Misérables est un roman de Victor Hugo paru en 1862 (la première partie est publiée le 30 mars à Bruxelles par les Éditions Lacroix, Verboeckhoven et Cie, et le 3 avril de la même année à Paris1). Dans ce roman, un des plus emblématiques de la littérature française, Victor Hugo décrit la vie de misérables dans Paris et la France provinciale du xixe siècle et s'attache plus particulièrement aux pas du bagnard Jean Valjean.";
	public static final String GERMINAL_DESCRIPTION = "Germinal est un roman d'Émile Zola publié en 1885.\nIl s'agit du treizième roman de la série des Rougon-Macquart.\nÉcrit d'avril 1884 à janvier 1885, le roman paraît d'abord en feuilleton entre novembre 1884 et février 1885 dans le Gil Blas. Il connaît sa première édition en mars 1885. Depuis il a été publié dans plus d'une centaine de pays.";
	public static final String LA_CHARTREUSE_DE_PARME_DESCRIPTION = "La Chartreuse de Parme est un roman publié par Stendhal.\nCette œuvre majeure, qui lui valut la célébrité, fut publiée en deux volumes en mars 1839, puis refondue en 1841, soit peu avant la mort de Stendhal, à la suite d'un article fameux de Balzac et prenant de fait un tour plus « balzacien » : aujourd’hui, c’est le texte stendhalien d’origine que l’on lit encore.";
	public static final String LA_CHARTREUSE_DE_PARME_DESCRIPTION_ADDENDUM = "L’œuvre sera, jusqu’au début du XXe siècle, relativement inconnue en dehors de quelques cercles d’esthètes, de critiques littéraires, ou de personnalités visionnaires (Nietzsche), ce que Stendhal semblait appeler de ses vœux, dédicaçant son roman To the Happy Few.";
	public static final String LE_ROUGE_ET_LE_NOIR_DESCRIPTION = "Le Rouge et le Noir, sous-titré Chronique du XIXe siècle, deuxième sous-titré Chronique de 1830 est un roman écrit par Stendhal, publié pour la première fois à Paris chez Levasseur en novembre 1830, bien que l'édition originale1 mentionne la date de 1831.\nC'est le deuxième roman de Stendhal, après Armance. Il est cité par William Somerset Maugham en 1954, dans son essai : Ten Novels and Their Authors parmi les dix plus grands romans.";
	public static final String LE_ROUGE_ET_LE_NOIR_DESCRIPTION_ADDENDUM = "Le roman est divisé en deux parties : la première partie retrace le parcours de Julien Sorel en province à Verrières puis à Besançon et plus précisément son entrée chez les Rênal, de même que son séjour dans un séminaire ; la seconde partie porte sur la vie du héros à Paris comme secrétaire du marquis de La Mole.";

	/**
	 * Instantiate in project a VirtualModelInstance conform to the VirtualModel
	 * 
	 * @throws SaveResourceException
	 * @throws InvocationTargetException
	 * @throws NullReferenceException
	 * @throws TypeMismatchException
	 * @throws AssertionFailedError
	 */
	@Test
	@TestOrder(8)
	public void testInstantiateLibrary()
			throws SaveResourceException, AssertionFailedError, TypeMismatchException, NullReferenceException, InvocationTargetException {

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
		createBook1.setParameterValue(descriptionParam, LES_MISERABLES_DESCRIPTION);
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
		createBook2.setParameterValue(descriptionParam, GERMINAL_DESCRIPTION);
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
		createBook3.setParameterValue(descriptionParam, LA_CHARTREUSE_DE_PARME_DESCRIPTION);
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

		DataBinding booksAccess = new DataBinding("books", libraryVMI.getVirtualModel(), Object.class, BindingDefinitionType.GET);

		assertTrue(booksAccess.isValid());
		assertSameList((List) booksAccess.getBindingValue(libraryVMI), book1, book2, book3);

	}

	/**
	 * Instantiate in project a VirtualModelInstance conform to the VirtualModel
	 */
	@Test
	@TestOrder(9)
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
	 * @throws FragmentConsistencyException
	 */
	@Test
	@TestOrder(10)
	public void testGenerateDocument() throws SaveResourceException, FragmentConsistencyException {

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

		assertEquals(44, generatedDocument.getElements().size());

		DocXParagraph introTitle = (DocXParagraph) generatedDocument.getElements().get(7);
		DocXParagraph introLastPar = (DocXParagraph) generatedDocument.getElements().get(16);
		assertEquals(generatedDocument.getFragment(introTitle, introLastPar), documentVMI.getFlexoActor("introductionSection"));

		DocXParagraph booksTitle = (DocXParagraph) generatedDocument.getElements().get(17);
		DocXParagraph booksLastPar = (DocXParagraph) generatedDocument.getElements().get(26);

		/*DocXFragment booksDescriptionSection = (DocXFragment) documentVMI.getFlexoActor("booksDescriptionSection");
		System.out.println("start=" + booksDescriptionSection.getStartElement().getIdentifier());
		System.out.println("end=" + booksDescriptionSection.getEndElement().getIdentifier());*/

		assertEquals(generatedDocument.getFragment(booksTitle, booksLastPar), documentVMI.getFlexoActor("booksDescriptionSection"));

		DocXParagraph conclusionTitle = (DocXParagraph) generatedDocument.getElements().get(27);
		DocXParagraph conclusionLastPar = (DocXParagraph) generatedDocument.getElements().get(43);
		assertEquals(generatedDocument.getFragment(conclusionTitle, conclusionLastPar), documentVMI.getFlexoActor("conclusionSection"));

	}

	/**
	 * Try to update the document from data
	 * 
	 * @throws SaveResourceException
	 * @throws FragmentConsistencyException
	 */
	@Test
	@TestOrder(11)
	public void testUpdateDocument() throws SaveResourceException, FragmentConsistencyException {

		log("testUpdateDocument()");

		DocXParagraph booksTitle = (DocXParagraph) generatedDocument.getElements().get(17);
		DocXParagraph booksLastPar = (DocXParagraph) generatedDocument.getElements().get(26);
		System.out.println("booksTitle=" + booksTitle.getIdentifier() + " : " + booksTitle.getRawText());
		System.out.println("booksLastPar=" + booksLastPar.getIdentifier() + " : " + booksLastPar.getRawText());
		assertEquals(generatedDocument.getFragment(booksTitle, booksLastPar), documentVMI.getFlexoActor("booksDescriptionSection"));

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

		assertEquals(62, generatedDocument.getElements().size());

		assertEquals(3, documentVMI.getFlexoConceptInstances().size());

		System.out.println("Template resource = " + docXModelSlot.getTemplateResource());
		System.out.println("generatedDocument = " + generatedDocument.getResource());

		// Here is the structuration of original fragment (bookDescriptionFragment):

		// > [2936B416/23] { (Les )(misérables) }
		// > [0BFF1745/24] { }
		// > [Table4A82BC28/25][{ (Aut)(eur) } ] [{ (Victor Hugo) } ]
		// [{ (Edition) } ] [{ (Editions )(Dunod) } ]
		// [{ (Type) } ] [{ (Roman) } ]
		// > [05FCD490/26] { }
		// > [289CEE3E/27] { (Les Misérables est un roman de Victor Hugo paru en 1862 (la première partie est publiée le 30 mars à Bruxelles
		// par les Éditions Lacroix, )(Verboeckhoven)( et Cie, et le 3 avril de la même année à Paris1).) }
		// > [395C1CE1/28] { (Dans ce roman, un des plus emblématiques de la littérature française, Victor Hugo décrit la vie de misérables
		// dans Paris et la France provinciale du )(xixe)( siècle et s'attache plus particulièrement aux pas du bagnard Jean )(Valjean)(.) }
		// > [096F50C5/29] { }
		// > [0E7A4C44/30] { }
		// > [631A77B5/31] { }
		// > [5052C24D/32] { }

		// Les misérables

		DocXParagraph titleParagraph3 = (DocXParagraph) generatedDocument.getElements().get(26);
		DocXTable table3 = (DocXTable) generatedDocument.getElements().get(28);
		DocXParagraph authorParagraph3 = (DocXParagraph) table3.getCell(0, 1).getParagraphs().get(0);
		DocXParagraph editionParagraph3 = (DocXParagraph) table3.getCell(1, 1).getParagraphs().get(0);
		DocXParagraph typeParagraph3 = (DocXParagraph) table3.getCell(2, 1).getParagraphs().get(0);
		DocXParagraph descriptionParagraph3 = (DocXParagraph) generatedDocument.getElements().get(30);
		DocXFragment lmFragment = generatedDocument.getFragment(titleParagraph3, descriptionParagraph3);

		// > [39F6DE6E/26] { (Les misérables) }
		// > [2E0630CF/27] { }
		// > [Table4D37D9A1/28][{ (Aut)(eur) } ] [{ (Victor Hugo) } ]
		// [{ (Edition) } ] [{ (Dunod) } ]
		// [{ (Type) } ] [{ (Roman) } ]
		// > [62183C48/29] { }
		// > [2FA6B49E/30] { (Les Misérables est un roman de Victor Hugo paru en 1862 (la première partie est publiée le 30 mars à Bruxelles
		// par les Éditions Lacroix, Verboeckhoven et Cie, et le 3 avril de la même année à Paris1). Dans ce roman, un des plus
		// emblématiques de la littérature française, Victor Hugo décrit la vie de misérables dans Paris et la France provinciale du xixe
		// siècle et s'attache plus particulièrement aux pas du bagnard Jean Valjean.) }

		assertEquals(1, titleParagraph3.getRuns().size());
		assertEquals("Les misérables", titleParagraph3.getRuns().get(0).getText());

		assertEquals(1, authorParagraph3.getRuns().size());
		assertEquals("Victor Hugo", authorParagraph3.getRuns().get(0).getText());

		assertEquals(1, editionParagraph3.getRuns().size());
		assertEquals("Dunod", editionParagraph3.getRuns().get(0).getText());

		assertEquals(1, typeParagraph3.getRuns().size());
		assertEquals("Roman", typeParagraph3.getRuns().get(0).getText());

		assertEquals(1, descriptionParagraph3.getRuns().size());
		assertEquals(LES_MISERABLES_DESCRIPTION, descriptionParagraph3.getRuns().get(0).getText());

		// Germinal

		DocXParagraph titleParagraph2 = (DocXParagraph) generatedDocument.getElements().get(31);
		DocXTable table2 = (DocXTable) generatedDocument.getElements().get(33);
		DocXParagraph authorParagraph2 = (DocXParagraph) table2.getCell(0, 1).getParagraphs().get(0);
		DocXParagraph editionParagraph2 = (DocXParagraph) table2.getCell(1, 1).getParagraphs().get(0);
		DocXParagraph typeParagraph2 = (DocXParagraph) table2.getCell(2, 1).getParagraphs().get(0);
		DocXParagraph descriptionParagraph2 = (DocXParagraph) generatedDocument.getElements().get(35);
		DocXParagraph descriptionParagraph2bis = (DocXParagraph) generatedDocument.getElements().get(36);
		DocXParagraph descriptionParagraph2ter = (DocXParagraph) generatedDocument.getElements().get(37);
		DocXFragment gFragment = generatedDocument.getFragment(titleParagraph2, descriptionParagraph2);

		// > [3827E78E/31] { (Germinal) }
		// > [4DD64F05/32] { }
		// > [Table498B6EF9/33][{ (Aut)(eur) } ] [{ (Emile Zola) } ]
		// [{ (Edition) } ] [{ (Gil Blas) } ]
		// [{ (Type) } ] [{ (Roman) } ]
		// > [3E079748/34] { }
		// > [53105B73/35] { (Germinal est un roman d'Émile Zola publié en 1885.) }
		// > [78FB1D5A/36] { (Il s'agit du treizième roman de la série des Rougon-Macquart.) }
		// > [44C3460D/37] { (Écrit d'avril 1884 à janvier 1885, le roman paraît d'abord en feuilleton entre novembre 1884 et février 1885
		// dans le Gil Blas. Il connaît sa première édition en mars 1885. Depuis il a été publié dans plus d'une centaine de pays.) }

		assertEquals(1, titleParagraph2.getRuns().size());
		assertEquals("Germinal", titleParagraph2.getRuns().get(0).getText());

		assertEquals(1, authorParagraph2.getRuns().size());
		assertEquals("Emile Zola", authorParagraph2.getRuns().get(0).getText());

		assertEquals(1, editionParagraph2.getRuns().size());
		assertEquals("Gil Blas", editionParagraph2.getRuns().get(0).getText());

		assertEquals(1, typeParagraph2.getRuns().size());
		assertEquals("Roman", typeParagraph2.getRuns().get(0).getText());

		assertEquals(1, descriptionParagraph2.getRuns().size());
		StringTokenizer st2 = new StringTokenizer(GERMINAL_DESCRIPTION, StringUtils.LINE_SEPARATOR);
		String description2Line1 = st2.nextToken();
		String description2Line2 = st2.nextToken();
		String description2Line3 = st2.nextToken();

		assertEquals(description2Line1, descriptionParagraph2.getRuns().get(0).getText());
		assertEquals(description2Line2, descriptionParagraph2bis.getRuns().get(0).getText());
		assertEquals(description2Line3, descriptionParagraph2ter.getRuns().get(0).getText());

		// La chartreuse de Parme

		DocXParagraph titleParagraph1 = (DocXParagraph) generatedDocument.getElements().get(38);
		DocXTable table1 = (DocXTable) generatedDocument.getElements().get(40);
		DocXParagraph authorParagraph1 = (DocXParagraph) table1.getCell(0, 1).getParagraphs().get(0);
		DocXParagraph editionParagraph1 = (DocXParagraph) table1.getCell(1, 1).getParagraphs().get(0);
		DocXParagraph typeParagraph1 = (DocXParagraph) table1.getCell(2, 1).getParagraphs().get(0);
		DocXParagraph descriptionParagraph1 = (DocXParagraph) generatedDocument.getElements().get(42);
		DocXParagraph descriptionParagraph1bis = (DocXParagraph) generatedDocument.getElements().get(43);
		DocXFragment cpFragment = generatedDocument.getFragment(titleParagraph1, descriptionParagraph1bis);

		// Now the fragment should be this:

		// > [79521E07/38] { (La chartreuse de Parme) }
		// > [4C9B8A7D/39] { }
		// > [Table6C1DCF68/40][{ (Aut)(eur) } ] [{ (Stendhal) } ]
		// [{ (Edition) } ] [{ (J. Hetzel, 1846) } ]
		// [{ (Type) } ] [{ (Roman) } ]
		// > [533033C3/41] { }
		// > [3E1623C6/42] { (La Chartreuse de Parme est un roman publié par Stendhal.) }
		// > [31DCF254/43] { (Cette œuvre majeure, qui lui valut la célébrité, fut publiée en deux volumes en mars 1839, puis refondue en
		// 1841, soit peu avant la mort de Stendhal, à la suite d'un article fameux de Balzac et prenant de fait un tour plus « balzacien »
		// : aujourd’hui, c’est le texte stendhalien d’origine que l’on lit encore.) }
		// > [4A46B077/44] { }

		assertEquals(1, titleParagraph1.getRuns().size());
		assertEquals("La chartreuse de Parme", titleParagraph1.getRuns().get(0).getText());

		assertEquals(1, authorParagraph1.getRuns().size());
		assertEquals("Stendhal", authorParagraph1.getRuns().get(0).getText());

		assertEquals(1, editionParagraph1.getRuns().size());
		assertEquals("J. Hetzel, 1846", editionParagraph1.getRuns().get(0).getText());

		assertEquals(1, typeParagraph1.getRuns().size());
		assertEquals("Roman", typeParagraph1.getRuns().get(0).getText());

		assertEquals(1, descriptionParagraph1.getRuns().size());
		StringTokenizer st = new StringTokenizer(LA_CHARTREUSE_DE_PARME_DESCRIPTION, StringUtils.LINE_SEPARATOR);
		String description1Line1 = st.nextToken();
		String description1Line2 = st.nextToken();

		assertEquals(description1Line1, descriptionParagraph1.getRuns().get(0).getText());
		assertEquals(description1Line2, descriptionParagraph1bis.getRuns().get(0).getText());

		/*StringBuffer sb = new StringBuffer();
		for (DocXElement element : lmFragment.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph para = (DocXParagraph) element;
				for (FlexoRun run : para.getRuns()) {
					sb.append("[" + run.getText() + "]");
				}
				sb.append("\n");
			}
		}
		
		System.out.println(sb.toString());*/

		assertEquals(62, generatedDocument.getElements().size());

	}

	/**
	 * Reload project<br>
	 * Check that the two {@link VirtualModelInstance} are correct and that generated document is correct
	 */
	@Test
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

		// TAKE CARE TO RELOAD all static fields as they are still pointing on old references
		assertNotNull(libraryVirtualModel = newView.getViewPoint().getVirtualModelNamed("LibraryVirtualModel"));
		assertNotNull(documentVirtualModel = newView.getViewPoint().getVirtualModelNamed("DocumentVirtualModel"));
		assertNotNull(bookConcept = libraryVirtualModel.getFlexoConcept("Book"));
		assertNotNull(allBooksProperty = (GetSetProperty<?>) libraryVirtualModel.getAccessibleProperty("allBooks"));
		assertNotNull(bookCreationScheme = bookConcept.getCreationSchemes().get(0));
		assertNotNull(titleParam = bookCreationScheme.getParameter("aTitle"));
		assertNotNull(authorParam = bookCreationScheme.getParameter("anAuthor"));
		assertNotNull(editionParam = bookCreationScheme.getParameter("anEdition"));
		assertNotNull(typeParam = bookCreationScheme.getParameter("aType"));
		assertNotNull(descriptionParam = bookCreationScheme.getParameter("aDescription"));
		assertNotNull(generateDocumentActionScheme = (ActionScheme) documentVirtualModel.getFlexoBehaviour("generateDocument"));
		assertNotNull(updateDocumentActionScheme = (ActionScheme) documentVirtualModel.getFlexoBehaviour("updateDocument"));
		assertNotNull(reinjectFromDocumentActionScheme = (ActionScheme) documentVirtualModel.getFlexoBehaviour("reinjectFromDocument"));
		assertNotNull(bookDescriptionSection = documentVirtualModel.getFlexoConcept("BookDescriptionSection"));
		assertNotNull(bookDescriptionSectionCreationScheme = (CreationScheme) bookDescriptionSection
				.getFlexoBehaviour("createBookDescriptionSection"));
		assertNotNull(bookDescriptionSectionUpdateScheme = (ActionScheme) bookDescriptionSection
				.getFlexoBehaviour("updateBookDescriptionSection"));
		assertNotNull(bookDescriptionSectionReinjectScheme = (ActionScheme) bookDescriptionSection
				.getFlexoBehaviour("reinjectDataFromBookDescriptionSection"));
		assertNotNull(introductionFragmentRole = (DocXFragmentRole) documentVirtualModel.getAccessibleProperty("introductionSection"));
		assertNotNull(
				booksDescriptionFragmentRole = (DocXFragmentRole) documentVirtualModel.getAccessibleProperty("booksDescriptionSection"));
		assertNotNull(bookListingTableRole = (DocXTableRole) documentVirtualModel.getAccessibleProperty("bookListingTable"));
		assertNotNull(conclusionFragmentRole = (DocXFragmentRole) documentVirtualModel.getAccessibleProperty("conclusionSection"));

		assertEquals(2, newViewResource.getVirtualModelInstanceResources().size());

		assertEquals(1, newViewResource.getVirtualModelInstanceResources(libraryVirtualModel).size());
		VirtualModelInstanceResource libraryVmiResource = newViewResource.getVirtualModelInstanceResources(libraryVirtualModel).get(0);
		assertNotNull(libraryVmiResource);
		assertNull(libraryVmiResource.getLoadedResourceData());
		libraryVmiResource.loadResourceData(null);
		assertNotNull(libraryVMI = libraryVmiResource.getVirtualModelInstance());
		assertEquals(3, libraryVMI.getFlexoConceptInstances().size());

		for (FlexoConceptInstance fci : libraryVMI.getFlexoConceptInstances()) {
			System.out.println("fci = " + fci);
		}

		assertEquals(1, newViewResource.getVirtualModelInstanceResources(documentVirtualModel).size());
		VirtualModelInstanceResource documentVmiResource = newViewResource.getVirtualModelInstanceResources(documentVirtualModel).get(0);
		assertNotNull(documentVmiResource);
		assertNull(documentVmiResource.getLoadedResourceData());
		documentVmiResource.loadResourceData(null);
		assertNotNull(documentVMI = documentVmiResource.getVirtualModelInstance());
		assertEquals(3, documentVMI.getFlexoConceptInstances().size());

		assertTrue(documentVMI.getVirtualModel().hasNature(FMLControlledDocumentVirtualModelNature.INSTANCE));
		assertTrue(documentVMI.hasNature(FMLControlledDocumentVirtualModelInstanceNature.INSTANCE));
		// TODO: fix this
		// Testing nature of documentVMI will cause resolution of model slot instances, and call to setResource

		for (FlexoConceptInstance fci : documentVMI.getFlexoConceptInstances()) {
			System.out.println("fci = " + fci);
		}

		ModelSlotInstance<DocXModelSlot, DocXDocument> msInstance = FMLControlledDocumentVirtualModelInstanceNature
				.getModelSlotInstance(documentVMI);

		assertNotNull(msInstance);
		assertNotNull(msInstance.getAccessedResourceData());

		generatedDocument = msInstance.getAccessedResourceData();

		assertNotSame(generatedDocumentBeforeReload, generatedDocument);

		assertEquals(62, generatedDocument.getElements().size());

		assertFalse(libraryVMI.isModified());
		assertFalse(documentVMI.isModified());
		assertFalse(generatedDocument.isModified());

	}

	/**
	 * Try to add a new book to the library
	 * 
	 * @throws FragmentConsistencyException
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(13)
	public void testAddNewBook() throws FragmentConsistencyException, SaveResourceException {

		log("testAddNewBook()");

		assertFalse(libraryVMI.isModified());
		assertFalse(documentVMI.isModified());
		assertFalse(generatedDocument.isModified());

		System.out.println("AVANT LE ADD: Generated document:\n" + generatedDocument.debugStructuredContents());

		// Creation of new book
		CreationSchemeAction createNewBook = CreationSchemeAction.actionType.makeNewAction(libraryVMI, null, editor);
		createNewBook.setCreationScheme(bookCreationScheme);
		createNewBook.setParameterValue(titleParam, "Le rouge et le noir");
		createNewBook.setParameterValue(authorParam, "Stendhal");
		createNewBook.setParameterValue(editionParam, "Levasseur, 1830");
		createNewBook.setParameterValue(typeParam, "Roman");
		createNewBook.setParameterValue(descriptionParam, LE_ROUGE_ET_LE_NOIR_DESCRIPTION);
		assertNotNull(createNewBook);
		createNewBook.doAction();
		assertTrue(createNewBook.hasActionExecutionSucceeded());
		FlexoConceptInstance newBook = createNewBook.getFlexoConceptInstance();
		assertNotNull(newBook);
		assertEquals(bookConcept, newBook.getFlexoConcept());

		for (FlexoConceptInstance fci : libraryVMI.getFlexoConceptInstances()) {
			System.out.println("fci = " + fci);
		}
		assertEquals(4, libraryVMI.getFlexoConceptInstances().size());

		assertTrue(libraryVMI.isModified());
		assertFalse(documentVMI.isModified());
		assertFalse(generatedDocument.isModified());

		System.out.println("Applying updateDocumentActionScheme: ");

		System.out.println(updateDocumentActionScheme.getFMLRepresentation());

		// Launch updateDocument actions
		ActionSchemeActionType actionType = new ActionSchemeActionType(updateDocumentActionScheme, documentVMI);
		ActionSchemeAction actionSchemeCreationAction = actionType.makeNewAction(documentVMI, null, editor);
		assertNotNull(actionSchemeCreationAction);
		actionSchemeCreationAction.doAction();
		assertTrue(actionSchemeCreationAction.hasActionExecutionSucceeded());

		for (FlexoConceptInstance fci : documentVMI.getFlexoConceptInstances()) {
			System.out.println("fci = " + fci);
		}
		assertEquals(4, documentVMI.getFlexoConceptInstances().size());

		assertTrue(libraryVMI.isModified());
		assertTrue(documentVMI.isModified());
		assertTrue(generatedDocument.isModified());

		libraryVMI.getResource().save(null);
		documentVMI.getResource().save(null);
		generatedDocument.getResource().save(null);

		assertFalse(libraryVMI.isModified());
		assertFalse(documentVMI.isModified());
		assertFalse(generatedDocument.isModified());

		System.out.println("Generated document:\n" + generatedDocument.debugStructuredContents());

		assertEquals(68, generatedDocument.getElements().size());

		DocXParagraph titleParagraph4 = (DocXParagraph) generatedDocument.getElements().get(44);
		DocXTable table4 = (DocXTable) generatedDocument.getElements().get(46);
		DocXParagraph authorParagraph4 = (DocXParagraph) table4.getCell(0, 1).getParagraphs().get(0);
		DocXParagraph editionParagraph4 = (DocXParagraph) table4.getCell(1, 1).getParagraphs().get(0);
		DocXParagraph typeParagraph4 = (DocXParagraph) table4.getCell(2, 1).getParagraphs().get(0);
		DocXParagraph descriptionParagraph4 = (DocXParagraph) generatedDocument.getElements().get(48);
		DocXParagraph descriptionParagraph4bis = (DocXParagraph) generatedDocument.getElements().get(49);

		System.out.println("titleParagraph4=" + titleParagraph4.getRawText());
		System.out.println("descriptionParagraph4bis=" + descriptionParagraph4bis.getRawText());

		DocXFragment lrnFragment = generatedDocument.getFragment(titleParagraph4, descriptionParagraph4bis);

		/*StringBuffer sb = new StringBuffer();
		for (DocXElement element : lrnFragment.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph para = (DocXParagraph) element;
				for (FlexoRun run : para.getRuns()) {
					sb.append("[" + run.getText() + "]");
				}
				sb.append("\n");
			}
		}
		
		System.out.println(sb.toString());*/

		// [Le rouge et le noir]
		// [Author][: ][Stendhal]
		// [Edition][: ][Levasseur, 1830]
		// [Type][: ][Roman]
		// [Le Rouge et le Noir, sous-titré Chronique du XIXe siècle ...]
		// [C'est le deuxième roman de Stendhal, après Armance. Il ...]

		assertEquals(1, titleParagraph4.getRuns().size());
		assertEquals("Le rouge et le noir", titleParagraph4.getRuns().get(0).getText());

		assertEquals(1, authorParagraph4.getRuns().size());
		assertEquals("Stendhal", authorParagraph4.getRuns().get(0).getText());

		assertEquals(1, editionParagraph4.getRuns().size());
		assertEquals("Levasseur, 1830", editionParagraph4.getRuns().get(0).getText());

		assertEquals(1, typeParagraph4.getRuns().size());
		assertEquals("Roman", typeParagraph4.getRuns().get(0).getText());

		assertEquals(1, descriptionParagraph4.getRuns().size());
		StringTokenizer st = new StringTokenizer(LE_ROUGE_ET_LE_NOIR_DESCRIPTION, StringUtils.LINE_SEPARATOR);
		String description4Line1 = st.nextToken();
		String description4Line2 = st.nextToken();
		assertEquals(description4Line1, descriptionParagraph4.getRuns().get(0).getText());
		assertEquals(description4Line2, descriptionParagraph4bis.getRuns().get(0).getText());

	}

	/**
	 * Try to add a new book to the library
	 * 
	 * @throws FragmentConsistencyException
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(14)
	public void testModifySomeDataAndUpdateDocument() throws FragmentConsistencyException, SaveResourceException {

		log("testModifySomeDataAndUpdateDocument()");

		assertFalse(libraryVMI.isModified());
		assertFalse(documentVMI.isModified());
		assertFalse(generatedDocument.isModified());

		assertEquals(4, libraryVMI.getFlexoConceptInstances().size());
		FlexoConceptInstance book1 = libraryVMI.getFlexoConceptInstances().get(0);
		FlexoConceptInstance book2 = libraryVMI.getFlexoConceptInstances().get(1);
		FlexoConceptInstance book3 = libraryVMI.getFlexoConceptInstances().get(2);
		FlexoConceptInstance book4 = libraryVMI.getFlexoConceptInstances().get(3);

		book4.setFlexoActor("Le Rouge et le Noir, Chronique du XIXe siècle",
				(FlexoRole<String>) book4.getFlexoConcept().getAccessibleRole("title"));
		book4.setFlexoActor("Stendhal aka Henri Beyle", (FlexoRole<String>) book4.getFlexoConcept().getAccessibleRole("author"));
		book4.setFlexoActor("Levasseur", (FlexoRole<String>) book4.getFlexoConcept().getAccessibleRole("edition"));
		book4.setFlexoActor("Roman historique", (FlexoRole<String>) book4.getFlexoConcept().getAccessibleRole("type"));
		book4.setFlexoActor(LE_ROUGE_ET_LE_NOIR_DESCRIPTION + "\n" + LE_ROUGE_ET_LE_NOIR_DESCRIPTION_ADDENDUM,
				(FlexoRole<String>) book4.getFlexoConcept().getAccessibleRole("description"));

		assertTrue(libraryVMI.isModified());
		assertFalse(documentVMI.isModified());
		assertFalse(generatedDocument.isModified());

		System.out.println("Applying updateDocumentActionScheme: ");

		System.out.println(updateDocumentActionScheme.getFMLRepresentation());

		// Launch updateDocument actions
		ActionSchemeActionType actionType = new ActionSchemeActionType(updateDocumentActionScheme, documentVMI);
		ActionSchemeAction actionSchemeCreationAction = actionType.makeNewAction(documentVMI, null, editor);
		assertNotNull(actionSchemeCreationAction);
		actionSchemeCreationAction.doAction();
		assertTrue(actionSchemeCreationAction.hasActionExecutionSucceeded());

		for (FlexoConceptInstance fci : documentVMI.getFlexoConceptInstances()) {
			System.out.println("fci = " + fci);
		}
		assertEquals(4, documentVMI.getFlexoConceptInstances().size());

		System.out.println("Generated document:\n" + generatedDocument.debugStructuredContents());

		DocXParagraph titleParagraph4 = (DocXParagraph) generatedDocument.getElements().get(44);
		DocXTable table4 = (DocXTable) generatedDocument.getElements().get(46);
		DocXParagraph authorParagraph4 = (DocXParagraph) table4.getCell(0, 1).getParagraphs().get(0);
		DocXParagraph editionParagraph4 = (DocXParagraph) table4.getCell(1, 1).getParagraphs().get(0);
		DocXParagraph typeParagraph4 = (DocXParagraph) table4.getCell(2, 1).getParagraphs().get(0);
		DocXParagraph descriptionParagraph4 = (DocXParagraph) generatedDocument.getElements().get(48);
		DocXParagraph descriptionParagraph4bis = (DocXParagraph) generatedDocument.getElements().get(49);
		DocXParagraph descriptionParagraph4ter = (DocXParagraph) generatedDocument.getElements().get(50);

		assertEquals(1, titleParagraph4.getRuns().size());
		assertEquals("Le Rouge et le Noir, Chronique du XIXe siècle", titleParagraph4.getRuns().get(0).getText());

		assertEquals(1, authorParagraph4.getRuns().size());
		assertEquals("Stendhal aka Henri Beyle", authorParagraph4.getRuns().get(0).getText());

		assertEquals(1, editionParagraph4.getRuns().size());
		assertEquals("Levasseur", editionParagraph4.getRuns().get(0).getText());

		assertEquals(1, typeParagraph4.getRuns().size());
		assertEquals("Roman historique", typeParagraph4.getRuns().get(0).getText());

		assertEquals(1, descriptionParagraph4.getRuns().size());
		assertEquals(1, descriptionParagraph4bis.getRuns().size());
		assertEquals(1, descriptionParagraph4ter.getRuns().size());
		StringTokenizer st = new StringTokenizer(LE_ROUGE_ET_LE_NOIR_DESCRIPTION, StringUtils.LINE_SEPARATOR);
		String description4Line1 = st.nextToken();
		String description4Line2 = st.nextToken();
		String description4Line3 = LE_ROUGE_ET_LE_NOIR_DESCRIPTION_ADDENDUM;
		assertEquals(description4Line1, descriptionParagraph4.getRuns().get(0).getText());
		assertEquals(description4Line2, descriptionParagraph4bis.getRuns().get(0).getText());
		assertEquals(description4Line3, descriptionParagraph4ter.getRuns().get(0).getText());

		assertTrue(libraryVMI.isModified());
		assertTrue(documentVMI.isModified()); // DocumentVMI has been modified, because FragmentActorReference has been modified
		assertTrue(generatedDocument.isModified());

		libraryVMI.getResource().save(null);
		documentVMI.getResource().save(null);
		generatedDocument.getResource().save(null);

		assertFalse(libraryVMI.isModified());
		assertFalse(documentVMI.isModified());
		assertFalse(generatedDocument.isModified());

	}

	/**
	 * Try to modify generated document without modifiying the structure, and reinject it to the model<br>
	 * Check that reinjection works
	 * 
	 * @throws FragmentConsistencyException
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(15)
	public void testModifyDocumentAndReinjectData() throws FragmentConsistencyException, SaveResourceException {

		log("testModifyDocumentAndReinjectData()");

		// La chartreuse de Parme

		DocXParagraph titleParagraph1 = (DocXParagraph) generatedDocument.getElements().get(38);
		DocXTable table1 = (DocXTable) generatedDocument.getElements().get(40);
		DocXParagraph authorParagraph1 = (DocXParagraph) table1.getCell(0, 1).getParagraphs().get(0);
		DocXParagraph editionParagraph1 = (DocXParagraph) table1.getCell(1, 1).getParagraphs().get(0);
		DocXParagraph typeParagraph1 = (DocXParagraph) table1.getCell(2, 1).getParagraphs().get(0);
		DocXParagraph descriptionParagraph1 = (DocXParagraph) generatedDocument.getElements().get(42);
		DocXParagraph descriptionParagraph1bis = (DocXParagraph) generatedDocument.getElements().get(43);
		DocXFragment cpFragment = generatedDocument.getFragment(titleParagraph1, descriptionParagraph1bis);

		// [La chartreuse de Parme]
		// [Author][: ][Stendhal]
		// [Edition][: ][J. Hetzel, 1846]
		// [Type][: ][Roman]
		// [La Chartreuse de Parme est ...]

		titleParagraph1.getRuns().get(0).setText("La Chartreuse de Parme"); // Added a maj
		authorParagraph1.getRuns().get(0).setText("Stendhal (Henri Beyle)"); // Added original name of author
		editionParagraph1.getRuns().get(0).setText("Éditions Rencontre, Lausanne, 1967"); // Change for a newer edition
		typeParagraph1.getRuns().get(0).setText("Roman historique"); // Change for another type

		System.out.println("Generated document:\n" + generatedDocument.debugStructuredContents());

		System.out.println("Applying reinjectFromDocumentActionScheme: ");

		System.out.println(reinjectFromDocumentActionScheme.getFMLRepresentation());

		// Launch updateDocument actions
		ActionSchemeActionType actionType = new ActionSchemeActionType(reinjectFromDocumentActionScheme, documentVMI);
		ActionSchemeAction actionSchemeCreationAction = actionType.makeNewAction(documentVMI, null, editor);
		assertNotNull(actionSchemeCreationAction);
		actionSchemeCreationAction.doAction();
		assertTrue(actionSchemeCreationAction.hasActionExecutionSucceeded());

		for (FlexoConceptInstance fci : documentVMI.getFlexoConceptInstances()) {
			System.out.println("fci = " + fci);
		}
		assertEquals(4, documentVMI.getFlexoConceptInstances().size());

		assertEquals(4, libraryVMI.getFlexoConceptInstances().size());
		FlexoConceptInstance book3 = libraryVMI.getFlexoConceptInstances().get(2);
		assertEquals("La Chartreuse de Parme", book3.getFlexoActor("title"));
		assertEquals("Stendhal (Henri Beyle)", book3.getFlexoActor("author"));
		assertEquals("Éditions Rencontre, Lausanne, 1967", book3.getFlexoActor("edition"));
		assertEquals("Roman historique", book3.getFlexoActor("type"));

		assertTrue(libraryVMI.isModified());
		assertTrue(documentVMI.isModified()); // FragmentActorReference were modified
		assertTrue(generatedDocument.isModified());

		generatedDocument.getResource().save(null);
		documentVMI.getResource().save(null);
		libraryVMI.getResource().save(null);

		assertFalse(libraryVMI.isModified());
		assertFalse(documentVMI.isModified());
		assertFalse(generatedDocument.isModified());

	}

	/**
	 * Try to modify generated document by modifiying the structure, and reinject it to the model<br>
	 * We modify title and check that reinjection works
	 * 
	 * @throws FragmentConsistencyException
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(16)
	public void testModifyDocumentAndReinjectData2() throws FragmentConsistencyException, SaveResourceException {

		log("testModifyDocumentAndReinjectData2()");

		// La chartreuse de Parme

		DocXParagraph titleParagraph1 = (DocXParagraph) generatedDocument.getElements().get(38);
		DocXTable table1 = (DocXTable) generatedDocument.getElements().get(40);
		DocXParagraph authorParagraph1 = (DocXParagraph) table1.getCell(0, 1).getParagraphs().get(0);
		DocXParagraph editionParagraph1 = (DocXParagraph) table1.getCell(1, 1).getParagraphs().get(0);
		DocXParagraph typeParagraph1 = (DocXParagraph) table1.getCell(2, 1).getParagraphs().get(0);
		DocXParagraph descriptionParagraph1 = (DocXParagraph) generatedDocument.getElements().get(42);
		DocXParagraph descriptionParagraph1bis = (DocXParagraph) generatedDocument.getElements().get(43);
		DocXFragment cpFragment = generatedDocument.getFragment(titleParagraph1, descriptionParagraph1bis);

		// [La chartreuse de Parme]
		// [Author][: ][Stendhal]
		// [Edition][: ][J. Hetzel, 1846]
		// [Type][: ][Roman]
		// [La Chartreuse de Parme est ...]

		DocXRun currentSingleRun = (DocXRun) titleParagraph1.getRuns().get(0);

		DocXRun run1 = (DocXRun) currentSingleRun.cloneObject();
		DocXRun run2 = (DocXRun) currentSingleRun.cloneObject();
		DocXRun run3 = (DocXRun) currentSingleRun.cloneObject();
		DocXRun run4 = (DocXRun) currentSingleRun.cloneObject();

		run1.setText("La");
		run2.setText(" chartreuse ");
		run3.setText("de ");
		run4.setText("Parme");

		titleParagraph1.removeFromRuns(currentSingleRun);
		titleParagraph1.addToRuns(run1);
		titleParagraph1.addToRuns(run2);
		titleParagraph1.addToRuns(run3);
		titleParagraph1.addToRuns(run4);

		System.out.println("Generated document:\n" + generatedDocument.debugStructuredContents());

		System.out.println("Applying reinjectFromDocumentActionScheme: ");

		System.out.println(reinjectFromDocumentActionScheme.getFMLRepresentation());

		// Launch updateDocument actions
		ActionSchemeActionType actionType = new ActionSchemeActionType(reinjectFromDocumentActionScheme, documentVMI);
		ActionSchemeAction actionSchemeCreationAction = actionType.makeNewAction(documentVMI, null, editor);
		assertNotNull(actionSchemeCreationAction);
		actionSchemeCreationAction.doAction();
		assertTrue(actionSchemeCreationAction.hasActionExecutionSucceeded());

		for (FlexoConceptInstance fci : documentVMI.getFlexoConceptInstances()) {
			System.out.println("fci = " + fci);
		}
		assertEquals(4, documentVMI.getFlexoConceptInstances().size());

		assertEquals(4, libraryVMI.getFlexoConceptInstances().size());
		FlexoConceptInstance book3 = libraryVMI.getFlexoConceptInstances().get(2);
		assertEquals("La chartreuse de Parme", book3.getFlexoActor("title"));
		assertEquals("Stendhal (Henri Beyle)", book3.getFlexoActor("author"));
		assertEquals("Éditions Rencontre, Lausanne, 1967", book3.getFlexoActor("edition"));
		assertEquals("Roman historique", book3.getFlexoActor("type"));
		assertEquals(LA_CHARTREUSE_DE_PARME_DESCRIPTION, book3.getFlexoActor("description"));

		assertTrue(libraryVMI.isModified());
		assertFalse(documentVMI.isModified());
		assertTrue(generatedDocument.isModified());

		generatedDocument.getResource().save(null);
		libraryVMI.getResource().save(null);

		assertFalse(libraryVMI.isModified());
		assertFalse(documentVMI.isModified());
		assertFalse(generatedDocument.isModified());

	}

	/**
	 * Try to modify generated document by modifiying the structure, and reinject it to the model<br>
	 * We modify description and check that reinjection works
	 * 
	 * @throws FragmentConsistencyException
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(17)
	public void testModifyDocumentAndReinjectData3() throws FragmentConsistencyException, SaveResourceException {

		log("testModifyDocumentAndReinjectData3()");

		// La chartreuse de Parme

		DocXParagraph titleParagraph1 = (DocXParagraph) generatedDocument.getElements().get(38);
		DocXTable table1 = (DocXTable) generatedDocument.getElements().get(40);
		DocXParagraph authorParagraph1 = (DocXParagraph) table1.getCell(0, 1).getParagraphs().get(0);
		DocXParagraph editionParagraph1 = (DocXParagraph) table1.getCell(1, 1).getParagraphs().get(0);
		DocXParagraph typeParagraph1 = (DocXParagraph) table1.getCell(2, 1).getParagraphs().get(0);
		DocXParagraph descriptionParagraph1 = (DocXParagraph) generatedDocument.getElements().get(42);
		DocXParagraph descriptionParagraph1bis = (DocXParagraph) generatedDocument.getElements().get(43);
		DocXFragment cpFragment = generatedDocument.getFragment(titleParagraph1, descriptionParagraph1bis);

		// [La chartreuse de Parme]
		// [Author][: ][Stendhal]
		// [Edition][: ][J. Hetzel, 1846]
		// [Type][: ][Roman]
		// [La Chartreuse de Parme est ...]

		DocXParagraph descriptionParagraph1ter = (DocXParagraph) descriptionParagraph1bis.cloneObject();
		descriptionParagraph1ter.setBaseIdentifier(null);
		descriptionParagraph1ter.getRuns().get(0).setText(LA_CHARTREUSE_DE_PARME_DESCRIPTION_ADDENDUM);

		generatedDocument.insertElementAtIndex(descriptionParagraph1ter,
				generatedDocument.getElements().indexOf(descriptionParagraph1bis) + 1);

		/*DocXRun currentSingleRun = (DocXRun) titleParagraph1.getRuns().get(0);
		
		DocXRun run1 = (DocXRun) currentSingleRun.cloneObject();
		DocXRun run2 = (DocXRun) currentSingleRun.cloneObject();
		DocXRun run3 = (DocXRun) currentSingleRun.cloneObject();
		DocXRun run4 = (DocXRun) currentSingleRun.cloneObject();
		
		run1.setText("La");
		run2.setText(" chartreuse ");
		run3.setText("de ");
		run4.setText("Parme");
		
		titleParagraph1.removeFromRuns(currentSingleRun);
		titleParagraph1.addToRuns(run1);
		titleParagraph1.addToRuns(run2);
		titleParagraph1.addToRuns(run3);
		titleParagraph1.addToRuns(run4);*/

		/*titleParagraph1.getRuns().get(0).setText("La Chartreuse de Parme"); // Added a maj
		authorParagraph1.getRuns().get(2).setText("Stendhal (Henri Beyle)"); // Added original name of author
		editionParagraph1.getRuns().get(2).setText("Éditions Rencontre, Lausanne, 1967"); // Change for a newer edition
		typeParagraph1.getRuns().get(2).setText("Roman historique"); // Change for another type
		descriptionParagraph1.getRuns().get(0).setText(LA_CHARTREUSE_DE_PARME_DESCRIPTION + LA_CHARTREUSE_DE_PARME_DESCRIPTION_ADDENDUM);
		 */

		System.out.println("Generated document:\n" + generatedDocument.debugStructuredContents());

		System.out.println("Applying reinjectFromDocumentActionScheme: ");

		System.out.println(reinjectFromDocumentActionScheme.getFMLRepresentation());

		// Launch updateDocument actions
		ActionSchemeActionType actionType = new ActionSchemeActionType(reinjectFromDocumentActionScheme, documentVMI);
		ActionSchemeAction actionSchemeCreationAction = actionType.makeNewAction(documentVMI, null, editor);
		assertNotNull(actionSchemeCreationAction);
		actionSchemeCreationAction.doAction();
		assertTrue(actionSchemeCreationAction.hasActionExecutionSucceeded());

		for (FlexoConceptInstance fci : documentVMI.getFlexoConceptInstances()) {
			System.out.println("fci = " + fci);
		}
		assertEquals(4, documentVMI.getFlexoConceptInstances().size());

		assertEquals(4, libraryVMI.getFlexoConceptInstances().size());
		FlexoConceptInstance book3 = libraryVMI.getFlexoConceptInstances().get(2);
		assertEquals("La chartreuse de Parme", book3.getFlexoActor("title"));
		assertEquals("Stendhal (Henri Beyle)", book3.getFlexoActor("author"));
		assertEquals("Éditions Rencontre, Lausanne, 1967", book3.getFlexoActor("edition"));
		assertEquals("Roman historique", book3.getFlexoActor("type"));
		assertEquals(LA_CHARTREUSE_DE_PARME_DESCRIPTION + "\n" + LA_CHARTREUSE_DE_PARME_DESCRIPTION_ADDENDUM,
				book3.getFlexoActor("description"));

		assertTrue(libraryVMI.isModified());
		assertTrue(documentVMI.isModified()); // Modified because FragmentActorReference was modified (extra paragraph added)
		assertTrue(generatedDocument.isModified());

		generatedDocument.getResource().save(null);
		documentVMI.getResource().save(null);
		libraryVMI.getResource().save(null);

		assertFalse(libraryVMI.isModified());
		assertFalse(documentVMI.isModified());
		assertFalse(generatedDocument.isModified());

	}
}
