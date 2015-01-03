/** Copyright (c) 2013, THALES SYSTEMES AEROPORTES - All Rights Reserved
 * Author : Gilles Besançon
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or 
 * combining it with eclipse EMF (or a modified version of that library), 
 * containing parts covered by the terms of EPL 1.0, the licensors of this 
 * Program grant you additional permission to convey the resulting work.
 *
 * Contributors :
 *
 */
package org.openflexo.technologyadapter.emf.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.antar.binding.DataBinding;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModel.VirtualModelImpl;
import org.openflexo.foundation.fml.VirtualModelModelFactory;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateEditionAction.CreateEditionActionChoice;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateFlexoConcept;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateView;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreationSchemeAction;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.DefaultModelSlotInstanceConfigurationOption;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.technologyadapter.emf.EMFModelSlot;
import org.openflexo.technologyadapter.emf.EMFModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividualAttributeDataPropertyValue;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividualAttributeObjectPropertyValue;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividualReferenceObjectPropertyValue;
import org.openflexo.technologyadapter.emf.fml.editionaction.RemoveEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.fml.editionaction.RemoveEMFObjectIndividualAttributeDataPropertyValue;
import org.openflexo.technologyadapter.emf.fml.editionaction.RemoveEMFObjectIndividualAttributeObjectPropertyValue;
import org.openflexo.technologyadapter.emf.fml.editionaction.RemoveEMFObjectIndividualReferenceObjectPropertyValue;
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
	protected static final Logger logger = Logger.getLogger(TestEMFModelEdition.class.getPackage().getName());

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

	/**
	 * Test the VP creation
	 */
	@Test
	@TestOrder(1)
	public void testCreateViewPoint() {
		instanciateTestServiceManager();

		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(EMFTechnologyAdapter.class);

		System.out.println("ResourceCenter= " + resourceCenter);
		newViewPoint = ViewPointImpl.newViewPoint("TestViewPoint", "http://openflexo.org/test/TestViewPoint",
				resourceCenter.getDirectory(), serviceManager.getViewPointLibrary());
		// assertTrue(((ViewPointResource) newViewPoint.getResource()).getDirectory().exists());
		// assertTrue(((ViewPointResource) newViewPoint.getResource()).getFile().exists());
		assertTrue(((ViewPointResource) newViewPoint.getResource()).getDirectory() != null);
		assertTrue(((ViewPointResource) newViewPoint.getResource()).getFlexoIODelegate().exists());
	}

	/**
	 * Test the VirtualModel creation
	 */
	@Test
	@TestOrder(2)
	public void testCreateVirtualModel() throws SaveResourceException {

		EMFMetaModelRepository emfMetaModelRepository = resourceCenter.getRepository(EMFMetaModelRepository.class, technologicalAdapter);

		emfMetaModelResource = emfMetaModelRepository.getResource("http://www.thalesgroup.com/parameters/1.0");

		assertNotNull(emfMetaModelResource);

		newVirtualModel = VirtualModelImpl.newVirtualModel("TestVirtualModel", newViewPoint);
		// assertTrue(((VirtualModelResource) newVirtualModel.getResource()).getDirectory().exists());
		// assertTrue(((VirtualModelResource) newVirtualModel.getResource()).getFile().exists());
		assertTrue(((ViewPointResource) newViewPoint.getResource()).getDirectory() != null);
		assertTrue(((ViewPointResource) newViewPoint.getResource()).getFlexoIODelegate().exists());
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
		assertTrue(project.getProjectDataResource().getFlexoIODelegate().exists());
	}

	@Test
	@TestOrder(4)
	public void testCreateEMFModel() {
		try {

			assertNotNull(emfMetaModelResource);

			try {
				RepositoryFolder<FlexoResource<?>> modelFolder = project.createNewFolder("Models");
				emfModelResource = technologicalAdapter.createNewEMFModel(new File(modelFolder.getFile(), "coucou.emf"), "myURI",
						emfMetaModelResource);
			} catch (Exception e) {
				e.printStackTrace();
			}

			assertNotNull(emfModelResource);

			emfModelResource.save(null);

		} catch (SaveResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlexoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@TestOrder(5)
	public void testCreateVMI() {

		CreateView viewAction = CreateView.actionType.makeNewAction(project.getViewLibrary().getRootFolder(), null, editor);
		viewAction.setNewViewName("MyView");
		viewAction.setNewViewTitle("Test creation of a new view");
		viewAction.setViewpointResource((ViewPointResource) newViewPoint.getResource());
		viewAction.doAction();
		assertTrue(viewAction.hasActionExecutionSucceeded());
		newView = viewAction.getNewView();

		CreateVirtualModelInstance<?> vmiAction = CreateBasicVirtualModelInstance.actionType.makeNewAction(newView, null, editor);
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

		System.out.println("Saved: " + ((VirtualModelResource) newVirtualModel.getResource()).getFlexoIODelegate().toString());

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

			EMFObjectIndividual intParameter = createIntParameter(emfModelResource, emfMetaModelResource, "IntParameter Name",
					Integer.valueOf(12), newVirtualModel.getVirtualModelFactory());
			assertNotNull(intParameter);

			EMFObjectIndividual doubleParameter = createDoubleParameter(emfModelResource, emfMetaModelResource, "DoubleParameter Name",
					Double.valueOf(42.12), newVirtualModel.getVirtualModelFactory());
			assertNotNull(doubleParameter);

			// TODO : this does not work yet

			/*
			EMFObjectIndividual boolParameter = createBoolParameter(emfModelResource, emfMetaModelResource, "BoolParameter Name", true,
					newVirtualModel.getVirtualModelFactory());
			assertNotNull(boolParameter);

			EMFObjectIndividual stringParameter = createStringParameter(emfModelResource, emfMetaModelResource, "StringParameter Name",
					"StringParameter Value", newVirtualModel.getVirtualModelFactory());
			assertNotNull(stringParameter);

			EMFObjectIndividual parameterSet = createParameterSet(emfModelResource, emfMetaModelResource, "ParameterSet Name",
					Arrays.asList(intParameter, doubleParameter, boolParameter, stringParameter), newVirtualModel.getVirtualModelFactory());
			assertNotNull(parameterSet);
			 */

			emfModelResource.save(null);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlexoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected EMFObjectIndividual addEMFObjectIndividual(EMFModelResource emfModelResource, String classURI,
			VirtualModelModelFactory factory) {

		EMFObjectIndividual result = null;

		CreateEditionAction createEditionAction1 = CreateEditionAction.actionType.makeNewAction(creationScheme.getControlGraph(), null,
				editor);
		createEditionAction1.actionChoice = CreateEditionActionChoice.ModelSlotSpecificAction;
		createEditionAction1.setModelSlotSpecificActionClass(AddEMFObjectIndividual.class);
		createEditionAction1.setModelSlot(newModelSlot);
		createEditionAction1.doAction();

		AddEMFObjectIndividual addObject = (AddEMFObjectIndividual) createEditionAction1.getNewEditionAction();

		try {
			addObject.setOntologyClass(emfMetaModelResource.getResourceData(null).getClass(classURI));
			// addObject.setEMFClassURI(classURI);
			result = addObject.performAction(creationSchemeCreationAction);
			addObject.finalizePerformAction(creationSchemeCreationAction, result);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlexoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	protected EMFObjectIndividual removeEMFObjectIndividual(EMFModelResource emfModelResource, EMFObjectIndividual objectIndividual,
			VirtualModelModelFactory factory) {
		EMFObjectIndividual result = null;
		RemoveEMFObjectIndividual removeObject = factory.newInstance(RemoveEMFObjectIndividual.class);
		removeObject.setModelSlot(newModelSlot);
		// removeObject.setEMFModelResource(emfModelResource);
		// removeObject.setObjectIndividual(objectIndividual);
		removeObject.performAction(creationSchemeCreationAction);
		result = objectIndividual;
		removeObject.finalizePerformAction(creationSchemeCreationAction, null);
		return result;
	}

	protected EMFObjectIndividualAttributeDataPropertyValue addEMFObjectIndividualAttributeDataPropertyValue(
			EMFMetaModelResource emfMetaModelResource, EMFObjectIndividual objectIndividual, String propertyURI, Object value,
			VirtualModelModelFactory factory) throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		EMFObjectIndividualAttributeDataPropertyValue result = null;

		CreateEditionAction createEditionAction1 = CreateEditionAction.actionType.makeNewAction(creationScheme.getControlGraph(), null,
				editor);
		createEditionAction1.actionChoice = CreateEditionActionChoice.ModelSlotSpecificAction;
		createEditionAction1.setModelSlotSpecificActionClass(AddEMFObjectIndividualAttributeDataPropertyValue.class);
		createEditionAction1.setModelSlot(newModelSlot);
		createEditionAction1.doAction();

		AddEMFObjectIndividualAttributeDataPropertyValue addAttribute = (AddEMFObjectIndividualAttributeDataPropertyValue) createEditionAction1
				.getNewEditionAction();

		addAttribute.setModelSlot(newModelSlot);
		// addAttribute.set .setObjectIndividual(objectIndividual);
		addAttribute.setDataProperty(emfMetaModelResource.getResourceData(null).getDataProperty(propertyURI));
		addAttribute.setSubject(new DataBinding("this"));
		addAttribute.setValue(new DataBinding("\"" + value + "\""));
		result = addAttribute.performAction(creationSchemeCreationAction);
		addAttribute.finalizePerformAction(creationSchemeCreationAction, result);
		return result;
	}

	protected EMFObjectIndividualAttributeDataPropertyValue removeEMFObjectIndividualAttributeDataPropertyValue(
			EMFMetaModelResource emfMetaModelResource, EMFObjectIndividual objectIndividual, String propertyURI, Object value,
			VirtualModelModelFactory factory) throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		EMFObjectIndividualAttributeDataPropertyValue result = null;
		RemoveEMFObjectIndividualAttributeDataPropertyValue removeProperty = factory
				.newInstance(RemoveEMFObjectIndividualAttributeDataPropertyValue.class);
		removeProperty.setModelSlot(newModelSlot);
		// addName.setObjectIndividual(objectIndividual);
		// addName.setAttributeDataProperty((EMFAttributeDataProperty)
		// emfMetaModelResource.getResourceData(null).getDataProperty(propertyURI));
		// addName.setValue(value);
		result = removeProperty.performAction(creationSchemeCreationAction);
		removeProperty.finalizePerformAction(creationSchemeCreationAction, result);
		return result;
	}

	protected EMFObjectIndividualAttributeObjectPropertyValue addEMFObjectIndividualAttributeObjectPropertyValue(
			EMFMetaModelResource emfMetaModelResource, EMFObjectIndividual objectIndividual, String propertyURI, Object value,
			VirtualModelModelFactory factory) throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		EMFObjectIndividualAttributeObjectPropertyValue result = null;
		AddEMFObjectIndividualAttributeObjectPropertyValue addPropValue = factory
				.newInstance(AddEMFObjectIndividualAttributeObjectPropertyValue.class);
		addPropValue.setModelSlot(newModelSlot);
		// addName.setObjectIndividual(objectIndividual);
		// addName.setAttributeObjectProperty((EMFAttributeObjectProperty) emfMetaModelResource.getResourceData(null).getDataProperty(
		// propertyURI));
		// addName.setValue(value);
		result = addPropValue.performAction(creationSchemeCreationAction);
		addPropValue.finalizePerformAction(creationSchemeCreationAction, result);
		return result;
	}

	protected EMFObjectIndividualAttributeObjectPropertyValue removeEMFObjectIndividualAttributeObjectPropertyValue(
			EMFMetaModelResource emfMetaModelResource, EMFObjectIndividual objectIndividual, String propertyURI, Object value,
			VirtualModelModelFactory factory) throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		EMFObjectIndividualAttributeObjectPropertyValue result = null;
		RemoveEMFObjectIndividualAttributeObjectPropertyValue removePropVal = factory
				.newInstance(RemoveEMFObjectIndividualAttributeObjectPropertyValue.class);

		removePropVal.setModelSlot(newModelSlot);
		// addName.setObjectIndividual(objectIndividual);
		// addName.setAttributeObjectProperty((EMFAttributeObjectProperty) emfMetaModelResource.getResourceData(null).getDataProperty(
		// propertyURI));
		// addName.setValue(value);
		result = removePropVal.performAction(creationSchemeCreationAction);
		removePropVal.finalizePerformAction(creationSchemeCreationAction, result);
		return result;
	}

	protected EMFObjectIndividualReferenceObjectPropertyValue addEMFObjectIndividualReferenceObjectPropertyValue(
			EMFMetaModelResource emfMetaModelResource, EMFObjectIndividual objectIndividual, String propertyURI, Object value,
			VirtualModelModelFactory factory) throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		EMFObjectIndividualReferenceObjectPropertyValue result = null;
		AddEMFObjectIndividualReferenceObjectPropertyValue addRefPropVal = factory
				.newInstance(AddEMFObjectIndividualReferenceObjectPropertyValue.class);
		addRefPropVal.setModelSlot(newModelSlot);
		// addName.setObjectIndividual(objectIndividual);
		// addName.setReferenceObjectProperty((EMFReferenceObjectProperty) emfMetaModelResource.getResourceData(null).getObjectProperty(
		// propertyURI));
		// addName.setValue(value);
		result = addRefPropVal.performAction(creationSchemeCreationAction);
		addRefPropVal.finalizePerformAction(creationSchemeCreationAction, result);
		return result;
	}

	protected EMFObjectIndividualReferenceObjectPropertyValue removeEMFObjectIndividualReferenceObjectPropertyValue(
			EMFMetaModelResource emfMetaModelResource, EMFObjectIndividual objectIndividual, String propertyURI, Object value,
			VirtualModelModelFactory factory) throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		EMFObjectIndividualReferenceObjectPropertyValue result = null;
		RemoveEMFObjectIndividualReferenceObjectPropertyValue removeRefPropVal = factory
				.newInstance(RemoveEMFObjectIndividualReferenceObjectPropertyValue.class);

		removeRefPropVal.setModelSlot(newModelSlot);
		// addName.setObjectIndividual(objectIndividual);
		// addName.setReferenceObjectProperty((EMFReferenceObjectProperty) emfMetaModelResource.getResourceData(null).getObjectProperty(
		// propertyURI));
		// addName.setValue(value);
		result = removeRefPropVal.performAction(creationSchemeCreationAction);
		removeRefPropVal.finalizePerformAction(creationSchemeCreationAction, result);
		return result;
	}

	protected EMFObjectIndividual createParameterSet(EMFModelResource emfModelResource, EMFMetaModelResource emfMetaModelResource,
			String name, List<EMFObjectIndividual> ownedParameters, VirtualModelModelFactory factory) throws FileNotFoundException,
			ResourceLoadingCancelledException, FlexoException {
		EMFObjectIndividual result = null;
		// ParameterSet object
		result = addEMFObjectIndividual(emfModelResource, "http://www.thalesgroup.com/parameters/1.0/ParameterSet", factory);
		// Name parameter
		addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource, result,
				"http://www.thalesgroup.com/parameters/1.0/ParameterSet/name", name, factory);
		// OwnedParameters parameter
		for (EMFObjectIndividual ownedParameter : ownedParameters) {
			addEMFObjectIndividualReferenceObjectPropertyValue(emfMetaModelResource, result,
					"http://www.thalesgroup.com/parameters/1.0/ParameterSet/ownedParameters", ownedParameter.getObject(), factory);
		}
		return result;
	}

	protected EMFObjectIndividual createIntParameter(EMFModelResource emfModelResource, EMFMetaModelResource emfMetaModelResource,
			String name, Integer value, VirtualModelModelFactory factory) throws FileNotFoundException, ResourceLoadingCancelledException,
			FlexoException {
		EMFObjectIndividual result = null;
		// IntParameter object
		result = addEMFObjectIndividual(emfModelResource, "http://www.thalesgroup.com/parameters/1.0/IntParameterValue", factory);

		if (result != null) {
			result.addToPropertyValue(
					emfMetaModelResource.getResourceData(null).getDataProperty(
							"http://www.thalesgroup.com/parameters/1.0/GenericParameter/name"), name);
			result.addToPropertyValue(
					emfMetaModelResource.getResourceData(null).getDataProperty(
							"http://www.thalesgroup.com/parameters/1.0/IntParameterValue/value"), value);

			// TODO : this does not work yet
			// Name parameter
			// addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource, result,
			// "http://www.thalesgroup.com/parameters/1.0/GenericParameter/name", name, factory);
			// Value parameter.
			// addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource, result,
			// "http://www.thalesgroup.com/parameters/1.0/IntParameterValue/value", value, factory);
		} else {
			logger.warning("Unable to create a individual of type: http://www.thalesgroup.com/parameters/1.0/IntParameterValue");
		}
		return result;
	}

	protected EMFObjectIndividual createDoubleParameter(EMFModelResource emfModelResource, EMFMetaModelResource emfMetaModelResource,
			String name, Double value, VirtualModelModelFactory factory) throws FileNotFoundException, ResourceLoadingCancelledException,
			FlexoException {
		EMFObjectIndividual result = null;
		// DoubleParameter object
		result = addEMFObjectIndividual(emfModelResource, "http://www.thalesgroup.com/parameters/1.0/DoubleParameterValue", factory);

		if (result != null) {

			result.addToPropertyValue(
					emfMetaModelResource.getResourceData(null).getDataProperty(
							"http://www.thalesgroup.com/parameters/1.0/GenericParameter/name"), name);
			result.addToPropertyValue(
					emfMetaModelResource.getResourceData(null).getDataProperty(
							"http://www.thalesgroup.com/parameters/1.0/DoubleParameterValue/value"), value);

			// TODO : this does not work yet
			//
			// Name parameter
			// addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource, result,
			// "http://www.thalesgroup.com/parameters/1.0/GenericParameter/name", name, factory);
			// Value parameter.
			// addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource, result,
			// "http://www.thalesgroup.com/parameters/1.0/DoubleParameterValue/value", value, factory);

		} else {
			logger.warning("Unable to create a individual of type: http://www.thalesgroup.com/parameters/1.0/IntParameterValue");
		}
		return result;
	}

	protected EMFObjectIndividual createBoolParameter(EMFModelResource emfModelResource, EMFMetaModelResource emfMetaModelResource,
			String name, Boolean value, VirtualModelModelFactory factory) throws FileNotFoundException, ResourceLoadingCancelledException,
			FlexoException {
		EMFObjectIndividual result = null;
		// BoolParameter object
		result = addEMFObjectIndividual(emfModelResource, "http://www.thalesgroup.com/parameters/1.0/BoolParameterValue", factory);
		// Name parameter
		addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource, result,
				"http://www.thalesgroup.com/parameters/1.0/GenericParameter/name", name, factory);
		// Value parameter.
		addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource, result,
				"http://www.thalesgroup.com/parameters/1.0/BoolParameterValue/value", value, factory);
		return result;
	}

	protected EMFObjectIndividual createStringParameter(EMFModelResource emfModelResource, EMFMetaModelResource emfMetaModelResource,
			String name, String value, VirtualModelModelFactory factory) throws FileNotFoundException, ResourceLoadingCancelledException,
			FlexoException {
		EMFObjectIndividual result = null;
		// StringParameter object
		result = addEMFObjectIndividual(emfModelResource, "http://www.thalesgroup.com/parameters/1.0/StringParameterValue", factory);
		// Name parameter
		addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource, result,
				"http://www.thalesgroup.com/parameters/1.0/GenericParameter/name", name, factory);
		// Value parameter.
		addEMFObjectIndividualAttributeDataPropertyValue(emfMetaModelResource, result,
				"http://www.thalesgroup.com/parameters/1.0/StringParameterValue/value", value, factory);
		return result;
	}
}
