/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * Copyright (c) 2012, THALES SYSTEMES AEROPORTES - All Rights Reserved
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateFlexoConcept;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rm.ViewPointResourceFactory;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rm.VirtualModelResourceFactory;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext.ReturnException;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateViewInFolder;
import org.openflexo.foundation.fml.rt.action.CreationSchemeAction;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.DefaultModelSlotInstanceConfigurationOption;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.test.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.emf.EMFModelSlot;
import org.openflexo.technologyadapter.emf.EMFModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.fml.editionaction.RemoveEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelRepository;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;
import org.openflexo.technologyadapter.emf.rm.EMFModelResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test Class for EMF Model Edition.
 * 
 * @author gbesancon, xtof
 * 
 */
@RunWith(OrderedRunner.class)
public class TestEMFModelEdition extends OpenflexoProjectAtRunTimeTestCase {
	protected static final Logger logger = Logger.getLogger(TestUMLModelEdition.class.getPackage().getName());

	public static final String VIEWPOINT_NAME = "TestViewPoint";
	public static final String VIEWPOINT_URI = "http://openflexo.org/test/TestViewPoint";
	public static final String VIRTUAL_MODEL_NAME = "TestVirtualModel";

	static FlexoEditor editor;
	static EMFTechnologyAdapter technologicalAdapter;
	static ViewPoint newViewPoint;
	static VirtualModel newVirtualModel;
	static EMFModelSlot newModelSlot = null;
	static EMFModelResource emfModelResource = null;
	static EMFMetaModelResource emfMetaModelResource = null;
	private static FlexoProject project;
	private static View newView;
	private static VirtualModelInstance newVirtualModelInstance;
	private static FlexoConcept flexoConcept;
	private static CreateFlexoBehaviour creationEditionScheme;
	private static CreationScheme creationScheme;
	private static CreationSchemeAction creationSchemeCreationAction;

	private static DirectoryResourceCenter newResourceCenter;
	private static FlexoResourceCenter<?> emfResourceCenter;

	/**
	 * Test the VP creation
	 * 
	 * @throws ModelDefinitionException
	 * @throws SaveResourceException
	 * @throws IOException
	 */
	@Test
	@TestOrder(1)
	public void testCreateViewPoint() throws SaveResourceException, ModelDefinitionException, IOException {
		instanciateTestServiceManager(EMFTechnologyAdapter.class);

		newResourceCenter = makeNewDirectoryResourceCenter();

		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(EMFTechnologyAdapter.class);

		FMLTechnologyAdapter fmlTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(FMLTechnologyAdapter.class);
		ViewPointResourceFactory factory = fmlTechnologyAdapter.getVirtualModelResourceFactory();

		ViewPointResource newViewPointResource = factory.makeViewPointResource(VIEWPOINT_NAME, VIEWPOINT_URI,
				fmlTechnologyAdapter.getGlobalRepository(newResourceCenter).getRootFolder(),
				fmlTechnologyAdapter.getTechnologyContextManager(), true);
		newViewPoint = newViewPointResource.getLoadedResourceData();

		// newViewPoint = ViewPointImpl.newViewPoint("TestViewPoint",
		// "http://openflexo.org/test/TestViewPoint",
		// resourceCenter.getDirectory(),
		// serviceManager.getViewPointLibrary(), resourceCenter);
		// assertTrue(((ViewPointResource)
		// newViewPoint.getResource()).getDirectory().exists());
		// assertTrue(((ViewPointResource)
		// newViewPoint.getResource()).getFile().exists());
		assertTrue(((ViewPointResource) newViewPoint.getResource()).getDirectory() != null);
		assertTrue(((ViewPointResource) newViewPoint.getResource()).getIODelegate().exists());
	}

	/**
	 * Test the VirtualModel creation
	 * 
	 * @throws ModelDefinitionException
	 */
	@Test
	@TestOrder(2)
	public void testCreateVirtualModel() throws SaveResourceException, ModelDefinitionException {

		emfResourceCenter = serviceManager.getResourceCenterService().getFlexoResourceCenter("http://openflexo.org/emf-test");
		assertNotNull(emfResourceCenter);

		EMFMetaModelRepository<?> emfMetaModelRepository = technologicalAdapter.getEMFMetaModelRepository(emfResourceCenter);

		emfMetaModelResource = emfMetaModelRepository.getResource("http://www.thalesgroup.com/parameters/1.0");

		assertNotNull(emfMetaModelResource);

		FMLTechnologyAdapter fmlTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(FMLTechnologyAdapter.class);
		VirtualModelResourceFactory factory = fmlTechnologyAdapter.getVirtualModelResourceFactory().getVirtualModelResourceFactory();
		VirtualModelResource newVMResource = factory.makeVirtualModelResource(VIRTUAL_MODEL_NAME, newViewPoint.getViewPointResource(),
				fmlTechnologyAdapter.getTechnologyContextManager(), true);
		newVirtualModel = newVMResource.getLoadedResourceData();

		// newVirtualModel =
		// VirtualModelImpl.newVirtualModel("TestVirtualModel", newViewPoint);
		// assertTrue(((VirtualModelResource)
		// newVirtualModel.getResource()).getDirectory().exists());
		// assertTrue(((VirtualModelResource)
		// newVirtualModel.getResource()).getFile().exists());
		assertTrue(((ViewPointResource) newViewPoint.getResource()).getDirectory() != null);
		assertTrue(((ViewPointResource) newViewPoint.getResource()).getIODelegate().exists());
		newModelSlot = technologicalAdapter.makeModelSlot(EMFModelSlot.class, newVirtualModel);
		newModelSlot.setMetaModelResource(emfMetaModelResource);
		assertNotNull(newModelSlot);
		newVirtualModel.addToModelSlots(newModelSlot);
		assertTrue(newVirtualModel.getModelSlots(EMFModelSlot.class).size() == 1);
	}

	@Test
	@TestOrder(3)
	public void testCreateProject() {
		editor = createProject("TestProject");
		project = editor.getProject();
		System.out.println("Created project " + project.getProjectDirectory());
		assertTrue(project.getProjectDirectory().exists());
		assertTrue(project.getProjectDataResource().getIODelegate().exists());
	}

	@Test
	@TestOrder(4)
	public void testCreateEMFModel() throws ModelDefinitionException {
		try {

			assertNotNull(emfMetaModelResource);

			RepositoryFolder<FlexoResource<?>, File> modelFolder = project.createNewFolder("Models");
			File serializationArtefact = new File(modelFolder.getSerializationArtefact(), "coucou.emf");
			emfModelResource = technologicalAdapter.getEMFModelResourceFactory().makeEMFModelResource(serializationArtefact,
					emfMetaModelResource, newResourceCenter, technologicalAdapter.getTechnologyContextManager(), "coucou.emf", "myURI",
					true);

			/*
			 * try { RepositoryFolder<FlexoResource<?>,?> modelFolder =
			 * project.createNewFolder("Models"); emfModelResource =
			 * technologicalAdapter.createNewEMFModel(new
			 * File(modelFolder.getFile(), "coucou.emf"), "myURI",
			 * emfMetaModelResource, resourceCenter); } catch (Exception e) {
			 * e.printStackTrace(); }
			 */

			assertNotNull(emfModelResource);

			emfModelResource.save(null);

		} catch (SaveResourceException e) {
			e.printStackTrace();
		}
	}

	@Test
	@TestOrder(5)
	public void testCreateVMI() {

		CreateViewInFolder viewAction = CreateViewInFolder.actionType.makeNewAction(project.getViewLibrary().getRootFolder(), null, editor);
		viewAction.setNewViewName("MyView");
		viewAction.setNewViewTitle("Test creation of a new view");
		viewAction.setViewpointResource((ViewPointResource) newViewPoint.getResource());
		viewAction.doAction();
		assertTrue(viewAction.hasActionExecutionSucceeded());
		newView = viewAction.getNewView();

		CreateBasicVirtualModelInstance vmiAction = CreateBasicVirtualModelInstance.actionType.makeNewAction(newView, null, editor);
		vmiAction.setNewVirtualModelInstanceName("MyVMI");
		vmiAction.setVirtualModel(newVirtualModel);
		vmiAction.setNewVirtualModelInstanceTitle("My Virtual Model Instance");

		EMFModelSlotInstanceConfiguration modelSlotInstanceConfiguration = (EMFModelSlotInstanceConfiguration) vmiAction
				.getModelSlotInstanceConfiguration(newModelSlot);
		assertNotNull(modelSlotInstanceConfiguration);
		modelSlotInstanceConfiguration.setOption(DefaultModelSlotInstanceConfigurationOption.SelectExistingModel);
		modelSlotInstanceConfiguration.setModelResource(emfModelResource);
		assertTrue(modelSlotInstanceConfiguration.isValidConfiguration());

		logger.info("Creating a new VirtualModelInstance");
		vmiAction.doAction();
		newVirtualModelInstance = vmiAction.getNewVirtualModelInstance();

	}

	@Test
	@TestOrder(6)
	public void testCreateFlexoConceptC() throws SaveResourceException {

		CreateFlexoConcept addEP = CreateFlexoConcept.actionType.makeNewAction(newVirtualModel, null, editor);
		addEP.setNewFlexoConceptName("EMFFlexoConcept");
		addEP.doAction();

		flexoConcept = addEP.getNewFlexoConcept();

		System.out.println("FlexoConcept = " + flexoConcept);
		assertNotNull(flexoConcept);

		creationEditionScheme = CreateFlexoBehaviour.actionType.makeNewAction(flexoConcept, null, editor);
		creationEditionScheme.setFlexoBehaviourClass(CreationScheme.class);
		creationEditionScheme.setFlexoBehaviourName("DynamicCreation");
		assertNotNull(creationEditionScheme);
		creationEditionScheme.doAction();

		((VirtualModelResource) newVirtualModel.getResource()).save(null);

		System.out.println("Saved: " + ((VirtualModelResource) newVirtualModel.getResource()).getIODelegate().toString());

	}

	@Test
	@TestOrder(7)
	public void testEdithEMFModelinVMI() {

		try {

			creationScheme = (CreationScheme) creationEditionScheme.getNewFlexoBehaviour();
			assertNotNull(creationScheme);

			creationSchemeCreationAction = CreationSchemeAction.actionType.makeNewAction(newVirtualModelInstance, null, editor);
			creationSchemeCreationAction.setCreationScheme(creationScheme);
			assertNotNull(creationSchemeCreationAction);

			/*
			 * EMFObjectIndividual intParameter =
			 * createIntParameter(emfModelResource, emfMetaModelResource,
			 * "IntParameter Name", Integer.valueOf(12),
			 * newVirtualModel.getFMLModelFactory());
			 * assertNotNull(intParameter);
			 */
			/*
			 * EMFObjectIndividual doubleParameter =
			 * createDoubleParameter(emfModelResource, emfMetaModelResource,
			 * "DoubleParameter Name", Double.valueOf(42.12),
			 * newVirtualModel.getFMLModelFactory());
			 * assertNotNull(doubleParameter);
			 */

			// TODO : this does not work yet

			/*
			 * EMFObjectIndividual boolParameter =
			 * createBoolParameter(emfModelResource, emfMetaModelResource,
			 * "BoolParameter Name", true,
			 * newVirtualModel.getFMLModelFactory());
			 * assertNotNull(boolParameter);
			 * 
			 * EMFObjectIndividual stringParameter =
			 * createStringParameter(emfModelResource, emfMetaModelResource,
			 * "StringParameter Name", "StringParameter Value",
			 * newVirtualModel.getFMLModelFactory());
			 * assertNotNull(stringParameter);
			 * 
			 * EMFObjectIndividual parameterSet =
			 * createParameterSet(emfModelResource, emfMetaModelResource,
			 * "ParameterSet Name", Arrays.asList(intParameter, doubleParameter,
			 * boolParameter, stringParameter),
			 * newVirtualModel.getFMLModelFactory());
			 * assertNotNull(parameterSet);
			 */

			emfModelResource.save(null);

		} catch (FlexoException e) {
			e.printStackTrace();
		}

	}

	protected EMFObjectIndividual addEMFObjectIndividual(EMFModelResource emfModelResource, String classURI, FMLModelFactory factory) {

		EMFObjectIndividual result = null;

		CreateEditionAction createEditionAction1 = CreateEditionAction.actionType.makeNewAction(creationScheme.getControlGraph(), null,
				editor);
		// createEditionAction1.actionChoice =
		// CreateEditionActionChoice.ModelSlotSpecificAction;
		createEditionAction1.setEditionActionClass(AddEMFObjectIndividual.class);
		createEditionAction1.setModelSlot(newModelSlot);
		createEditionAction1.doAction();

		AddEMFObjectIndividual addObject = (AddEMFObjectIndividual) createEditionAction1.getNewEditionAction();

		try {
			addObject.setOntologyClass(emfMetaModelResource.getResourceData(null).getClass(classURI));
			// addObject.setEMFClassURI(classURI);
			result = addObject.execute(creationSchemeCreationAction);
			// addObject.finalizePerformAction(creationSchemeCreationAction,
			// result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
		} catch (FlexoException e) {
			e.printStackTrace();
		} catch (ReturnException e) {
			e.printStackTrace();
		}
		return result;
	}

	protected EMFObjectIndividual removeEMFObjectIndividual(EMFModelResource emfModelResource, EMFObjectIndividual objectIndividual,
			FMLModelFactory factory) throws FlexoException {
		EMFObjectIndividual result = null;
		RemoveEMFObjectIndividual removeObject = factory.newInstance(RemoveEMFObjectIndividual.class);
		// removeObject.setModelSlot(newModelSlot);
		// removeObject.setEMFModelResource(emfModelResource);
		// removeObject.setObjectIndividual(objectIndividual);
		try {
			removeObject.execute(creationSchemeCreationAction);
		} catch (ReturnException e) {
			e.printStackTrace();
		}
		result = objectIndividual;
		// removeObject.finalizePerformAction(creationSchemeCreationAction,
		// null);
		return result;
	}
	/*
	 * 
	 * protected EMFObjectIndividualAttributeDataPropertyValue
	 * addEMFObjectIndividualAttributeDataPropertyValue( EMFMetaModelResource
	 * emfMetaModelResource, EMFObjectIndividual objectIndividual, String
	 * propertyURI, Object value, FMLModelFactory factory) throws
	 * FileNotFoundException, ResourceLoadingCancelledException, FlexoException
	 * { EMFObjectIndividualAttributeDataPropertyValue result = null;
	 * 
	 * CreateEditionAction createEditionAction1 =
	 * CreateEditionAction.actionType.makeNewAction(creationScheme.
	 * getControlGraph(), null, editor); // createEditionAction1.actionChoice =
	 * CreateEditionActionChoice.ModelSlotSpecificAction;
	 * createEditionAction1.setEditionActionClass(
	 * AddEMFObjectIndividualAttributeDataPropertyValue.class);
	 * createEditionAction1.setModelSlot(newModelSlot);
	 * createEditionAction1.doAction();
	 * 
	 * AddEMFObjectIndividualAttributeDataPropertyValue addAttribute =
	 * (AddEMFObjectIndividualAttributeDataPropertyValue) createEditionAction1
	 * .getNewEditionAction();
	 * 
	 * addAttribute.setModelSlot(newModelSlot); // addAttribute.set
	 * .setObjectIndividual(objectIndividual);
	 * addAttribute.setDataProperty(emfMetaModelResource.getResourceData(null).
	 * getDataProperty(propertyURI)); addAttribute.setSubject(new
	 * DataBinding("this")); addAttribute.setValue(new DataBinding("\"" + value
	 * + "\"")); result = addAttribute.execute(creationSchemeCreationAction); //
	 * addAttribute.finalizePerformAction(creationSchemeCreationAction, result);
	 * return result; }
	 * 
	 * protected EMFObjectIndividualAttributeDataPropertyValue
	 * removeEMFObjectIndividualAttributeDataPropertyValue( EMFMetaModelResource
	 * emfMetaModelResource, EMFObjectIndividual objectIndividual, String
	 * propertyURI, Object value, FMLModelFactory factory) throws
	 * FileNotFoundException, ResourceLoadingCancelledException, FlexoException
	 * { EMFObjectIndividualAttributeDataPropertyValue result = null;
	 * RemoveEMFObjectIndividualAttributeDataPropertyValue removeProperty =
	 * factory
	 * .newInstance(RemoveEMFObjectIndividualAttributeDataPropertyValue.class);
	 * removeProperty.setModelSlot(newModelSlot); //
	 * addName.setObjectIndividual(objectIndividual); //
	 * addName.setAttributeDataProperty((EMFAttributeDataProperty) //
	 * emfMetaModelResource.getResourceData(null).getDataProperty(propertyURI));
	 * // addName.setValue(value); result =
	 * removeProperty.execute(creationSchemeCreationAction); //
	 * removeProperty.finalizePerformAction(creationSchemeCreationAction,
	 * result); return result; }
	 * 
	 * protected EMFObjectIndividualAttributeObjectPropertyValue
	 * addEMFObjectIndividualAttributeObjectPropertyValue( EMFMetaModelResource
	 * emfMetaModelResource, EMFObjectIndividual objectIndividual, String
	 * propertyURI, Object value, FMLModelFactory factory) throws
	 * FileNotFoundException, ResourceLoadingCancelledException, FlexoException
	 * { EMFObjectIndividualAttributeObjectPropertyValue result = null;
	 * AddEMFObjectIndividualAttributeObjectPropertyValue addPropValue = factory
	 * .newInstance(AddEMFObjectIndividualAttributeObjectPropertyValue.class);
	 * addPropValue.setModelSlot(newModelSlot); //
	 * addName.setObjectIndividual(objectIndividual); //
	 * addName.setAttributeObjectProperty((EMFAttributeObjectProperty)
	 * emfMetaModelResource.getResourceData(null).getDataProperty( //
	 * propertyURI)); // addName.setValue(value); result =
	 * addPropValue.execute(creationSchemeCreationAction); //
	 * addPropValue.finalizePerformAction(creationSchemeCreationAction, result);
	 * return result; }
	 * 
	 * protected EMFObjectIndividualAttributeObjectPropertyValue
	 * removeEMFObjectIndividualAttributeObjectPropertyValue(
	 * EMFMetaModelResource emfMetaModelResource, EMFObjectIndividual
	 * objectIndividual, String propertyURI, Object value, FMLModelFactory
	 * factory) throws FileNotFoundException, ResourceLoadingCancelledException,
	 * FlexoException { EMFObjectIndividualAttributeObjectPropertyValue result =
	 * null; RemoveEMFObjectIndividualAttributeObjectPropertyValue removePropVal
	 * = factory
	 * .newInstance(RemoveEMFObjectIndividualAttributeObjectPropertyValue.class)
	 * ;
	 * 
	 * removePropVal.setModelSlot(newModelSlot); //
	 * addName.setObjectIndividual(objectIndividual); //
	 * addName.setAttributeObjectProperty((EMFAttributeObjectProperty)
	 * emfMetaModelResource.getResourceData(null).getDataProperty( //
	 * propertyURI)); // addName.setValue(value); result =
	 * removePropVal.execute(creationSchemeCreationAction); //
	 * removePropVal.finalizePerformAction(creationSchemeCreationAction,
	 * result); return result; }
	 * 
	 * protected EMFObjectIndividualReferenceObjectPropertyValue
	 * addEMFObjectIndividualReferenceObjectPropertyValue( EMFMetaModelResource
	 * emfMetaModelResource, EMFObjectIndividual objectIndividual, String
	 * propertyURI, Object value, FMLModelFactory factory) throws
	 * FileNotFoundException, ResourceLoadingCancelledException, FlexoException
	 * { EMFObjectIndividualReferenceObjectPropertyValue result = null;
	 * AddEMFObjectIndividualReferenceObjectPropertyValue addRefPropVal =
	 * factory
	 * .newInstance(AddEMFObjectIndividualReferenceObjectPropertyValue.class);
	 * addRefPropVal.setModelSlot(newModelSlot); //
	 * addName.setObjectIndividual(objectIndividual); //
	 * addName.setReferenceObjectProperty((EMFReferenceObjectProperty)
	 * emfMetaModelResource.getResourceData(null).getObjectProperty( //
	 * propertyURI)); // addName.setValue(value); result =
	 * addRefPropVal.execute(creationSchemeCreationAction); //
	 * addRefPropVal.finalizePerformAction(creationSchemeCreationAction,
	 * result); return result; }
	 * 
	 * protected EMFObjectIndividualReferenceObjectPropertyValue
	 * removeEMFObjectIndividualReferenceObjectPropertyValue(
	 * EMFMetaModelResource emfMetaModelResource, EMFObjectIndividual
	 * objectIndividual, String propertyURI, Object value, FMLModelFactory
	 * factory) throws FileNotFoundException, ResourceLoadingCancelledException,
	 * FlexoException { EMFObjectIndividualReferenceObjectPropertyValue result =
	 * null; RemoveEMFObjectIndividualReferenceObjectPropertyValue
	 * removeRefPropVal = factory
	 * .newInstance(RemoveEMFObjectIndividualReferenceObjectPropertyValue.class)
	 * ;
	 * 
	 * removeRefPropVal.setModelSlot(newModelSlot); //
	 * addName.setObjectIndividual(objectIndividual); //
	 * addName.setReferenceObjectProperty((EMFReferenceObjectProperty)
	 * emfMetaModelResource.getResourceData(null).getObjectProperty( //
	 * propertyURI)); // addName.setValue(value); result =
	 * removeRefPropVal.execute(creationSchemeCreationAction); //
	 * removeRefPropVal.finalizePerformAction(creationSchemeCreationAction,
	 * result); return result; }
	 * 
	 * protected EMFObjectIndividual createParameterSet(EMFModelResource
	 * emfModelResource, EMFMetaModelResource emfMetaModelResource, String name,
	 * List<EMFObjectIndividual> ownedParameters, FMLModelFactory factory)
	 * throws FileNotFoundException, ResourceLoadingCancelledException,
	 * FlexoException { EMFObjectIndividual result = null; // ParameterSet
	 * object result = addEMFObjectIndividual(emfModelResource,
	 * "http://www.thalesgroup.com/parameters/1.0/ParameterSet", factory); //
	 * Name parameter
	 * addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource,
	 * result, "http://www.thalesgroup.com/parameters/1.0/ParameterSet/name",
	 * name, factory); // OwnedParameters parameter for (EMFObjectIndividual
	 * ownedParameter : ownedParameters) {
	 * addEMFObjectIndividualReferenceObjectPropertyValue(emfMetaModelResource,
	 * result,
	 * "http://www.thalesgroup.com/parameters/1.0/ParameterSet/ownedParameters",
	 * ownedParameter.getObject(), factory); } return result; }
	 * 
	 * protected EMFObjectIndividual createIntParameter(EMFModelResource
	 * emfModelResource, EMFMetaModelResource emfMetaModelResource, String name,
	 * Integer value, FMLModelFactory factory) throws FileNotFoundException,
	 * ResourceLoadingCancelledException, FlexoException { EMFObjectIndividual
	 * result = null; // IntParameter object result =
	 * addEMFObjectIndividual(emfModelResource,
	 * "http://www.thalesgroup.com/parameters/1.0/IntParameterValue", factory);
	 * 
	 * if (result != null) { result.addToPropertyValue(
	 * emfMetaModelResource.getResourceData(null).getDataProperty(
	 * "http://www.thalesgroup.com/parameters/1.0/GenericParameter/name"),
	 * name); result.addToPropertyValue(
	 * emfMetaModelResource.getResourceData(null).getDataProperty(
	 * "http://www.thalesgroup.com/parameters/1.0/IntParameterValue/value"),
	 * value);
	 * 
	 * // TODO : this does not work yet // Name parameter //
	 * addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource,
	 * result, //
	 * "http://www.thalesgroup.com/parameters/1.0/GenericParameter/name", name,
	 * factory); // Value parameter. //
	 * addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource,
	 * result, //
	 * "http://www.thalesgroup.com/parameters/1.0/IntParameterValue/value",
	 * value, factory); } else { logger.
	 * warning("Unable to create a individual of type: http://www.thalesgroup.com/parameters/1.0/IntParameterValue"
	 * ); } return result; }
	 * 
	 * protected EMFObjectIndividual createDoubleParameter(EMFModelResource
	 * emfModelResource, EMFMetaModelResource emfMetaModelResource, String name,
	 * Double value, FMLModelFactory factory) throws FileNotFoundException,
	 * ResourceLoadingCancelledException, FlexoException { EMFObjectIndividual
	 * result = null; // DoubleParameter object result =
	 * addEMFObjectIndividual(emfModelResource,
	 * "http://www.thalesgroup.com/parameters/1.0/DoubleParameterValue",
	 * factory);
	 * 
	 * if (result != null) {
	 * 
	 * result.addToPropertyValue(
	 * emfMetaModelResource.getResourceData(null).getDataProperty(
	 * "http://www.thalesgroup.com/parameters/1.0/GenericParameter/name"),
	 * name); result.addToPropertyValue(
	 * emfMetaModelResource.getResourceData(null).getDataProperty(
	 * "http://www.thalesgroup.com/parameters/1.0/DoubleParameterValue/value"),
	 * value);
	 * 
	 * // TODO : this does not work yet // // Name parameter //
	 * addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource,
	 * result, //
	 * "http://www.thalesgroup.com/parameters/1.0/GenericParameter/name", name,
	 * factory); // Value parameter. //
	 * addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource,
	 * result, //
	 * "http://www.thalesgroup.com/parameters/1.0/DoubleParameterValue/value",
	 * value, factory);
	 * 
	 * } else { logger.
	 * warning("Unable to create a individual of type: http://www.thalesgroup.com/parameters/1.0/IntParameterValue"
	 * ); } return result; }
	 * 
	 * protected EMFObjectIndividual createBoolParameter(EMFModelResource
	 * emfModelResource, EMFMetaModelResource emfMetaModelResource, String name,
	 * Boolean value, FMLModelFactory factory) throws FileNotFoundException,
	 * ResourceLoadingCancelledException, FlexoException { EMFObjectIndividual
	 * result = null; // BoolParameter object result =
	 * addEMFObjectIndividual(emfModelResource,
	 * "http://www.thalesgroup.com/parameters/1.0/BoolParameterValue", factory);
	 * // Name parameter
	 * addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource,
	 * result,
	 * "http://www.thalesgroup.com/parameters/1.0/GenericParameter/name", name,
	 * factory); // Value parameter.
	 * addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource,
	 * result,
	 * "http://www.thalesgroup.com/parameters/1.0/BoolParameterValue/value",
	 * value, factory); return result; }
	 * 
	 * protected EMFObjectIndividual createStringParameter(EMFModelResource
	 * emfModelResource, EMFMetaModelResource emfMetaModelResource, String name,
	 * String value, FMLModelFactory factory) throws FileNotFoundException,
	 * ResourceLoadingCancelledException, FlexoException { EMFObjectIndividual
	 * result = null; // StringParameter object result =
	 * addEMFObjectIndividual(emfModelResource,
	 * "http://www.thalesgroup.com/parameters/1.0/StringParameterValue",
	 * factory); // Name parameter
	 * addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource,
	 * result,
	 * "http://www.thalesgroup.com/parameters/1.0/GenericParameter/name", name,
	 * factory); // Value parameter.
	 * addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource,
	 * result,
	 * "http://www.thalesgroup.com/parameters/1.0/StringParameterValue/value",
	 * value, factory); return result; }
	 */
}
