/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Owlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.owl.fml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.connie.Bindable;
import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.connie.BindingModel;
import org.openflexo.connie.BindingVariable;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.binding.SimplePathElement;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.DefaultFlexoEditor;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
import org.openflexo.foundation.fml.PrimitiveRole;
import org.openflexo.foundation.fml.PrimitiveRole.PrimitiveType;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.fml.ViewType;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModel.VirtualModelImpl;
import org.openflexo.foundation.fml.VirtualModelInstanceType;
import org.openflexo.foundation.fml.action.CreateFlexoConcept;
import org.openflexo.foundation.fml.action.CreatePrimitiveRole;
import org.openflexo.foundation.fml.action.CreateTechnologyRole;
import org.openflexo.foundation.fml.binding.FlexoConceptBindingModel;
import org.openflexo.foundation.fml.binding.FlexoPropertyBindingVariable;
import org.openflexo.foundation.fml.binding.ViewPointBindingModel;
import org.openflexo.foundation.fml.binding.VirtualModelBindingModel;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.ontology.fml.action.CreateIndividualRole;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.owl.OWLModelSlot;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * This unit test is intented to test {@link BindingModel} management along FML model
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestOWLBindingModelManagement extends OpenflexoProjectAtRunTimeTestCase {

	static FlexoEditor editor;
	static ViewPoint viewPoint;
	static VirtualModel virtualModel1;

	static FlexoConcept flexoConceptA;

	static FlexoProject project;
	static View newView;
	static VirtualModelInstance vmi1;
	static VirtualModelInstance vmi2;
	static VirtualModelInstance vmi3;
	static FlexoConceptInstance fci;
	static private boolean renameWasNotified;
	static private boolean typeChangedWasNotified;
	static private OWLModelSlot newModelSlot;
	static private OWLTechnologyAdapter technologicalAdapter;

	static private String OWL_MODEL_SLOT_NAME = "owlmodel";

	public static final String BASIC_ONTOLOGY_URI = "http://www.agilebirds.com/openflexo/ViewPoints/BasicOntology.owl";

	static private OWLClass basicConcept;

	static private OWLObjectProperty hasObjectProperty;

	/**
	 * Init
	 */
	@Test
	@TestOrder(1)
	public void init() {
		instanciateTestServiceManager();

		editor = new DefaultFlexoEditor(null, serviceManager);
		assertNotNull(editor);

		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(OWLTechnologyAdapter.class);

	}

	/**
	 * Test {@link ViewPoint} creation, check {@link BindingModel}
	 */
	@Test
	@TestOrder(2)
	public void testCreateViewPoint() {
		viewPoint = ViewPointImpl.newViewPoint("TestViewPoint", "http://openflexo.org/test/TestViewPoint", resourceCenter.getDirectory(),
				serviceManager.getViewPointLibrary(), resourceCenter);
		assertTrue(ResourceLocator.retrieveResourceAsFile(((ViewPointResource) viewPoint.getResource()).getDirectory()).exists());
		assertTrue(((ViewPointResource) viewPoint.getResource()).getFlexoIODelegate().exists());

		assertNotNull(viewPoint.getBindingModel());
		assertEquals(3, viewPoint.getBindingModel().getBindingVariablesCount());
		assertNotNull(viewPoint.getBindingModel().bindingVariableNamed(ViewPointBindingModel.PROJECT_PROPERTY));
		assertNotNull(viewPoint.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(viewPoint.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));

	}

	/**
	 * Test {@link VirtualModel} creation, check {@link BindingModel}
	 */
	@Test
	@TestOrder(3)
	public void testCreateVirtualModel() throws SaveResourceException {

		virtualModel1 = VirtualModelImpl.newVirtualModel("VM1", viewPoint);
		assertTrue(ResourceLocator.retrieveResourceAsFile(((VirtualModelResource) virtualModel1.getResource()).getDirectory()).exists());
		assertTrue(((VirtualModelResource) virtualModel1.getResource()).getFlexoIODelegate().exists());

		assertNotNull(virtualModel1.getBindingModel());
		assertEquals(5, virtualModel1.getBindingModel().getBindingVariablesCount());
		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(ViewPointBindingModel.PROJECT_PROPERTY));
		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				virtualModel1.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getVirtualModelInstanceType(virtualModel1), virtualModel1.getBindingModel()
				.bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());

		// We disconnect VirtualModel from ViewPoint, and we check BindingModel evolution
		viewPoint.removeFromVirtualModels(virtualModel1);
		System.out.println("VirtualModel BindingModel = " + virtualModel1.getBindingModel());
		assertNotNull(virtualModel1.getBindingModel());
		assertEquals(2, virtualModel1.getBindingModel().getBindingVariablesCount());
		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		// assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		// assertEquals(View.class, virtualModel1.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		// assertEquals(VirtualModelInstanceType.getVirtualModelInstanceType(virtualModel1), virtualModel1.getBindingModel()
		// .bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());

		// We reconnect VirtualModel again, and we check BindingModel evolution
		viewPoint.addToVirtualModels(virtualModel1);
		System.out.println("VirtualModel BindingModel = " + virtualModel1.getBindingModel());
		assertEquals(5, virtualModel1.getBindingModel().getBindingVariablesCount());
		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(ViewPointBindingModel.PROJECT_PROPERTY));
		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				virtualModel1.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getVirtualModelInstanceType(virtualModel1), virtualModel1.getBindingModel()
				.bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());

	}

	/**
	 * Test {@link OWLModelSlot} creation, check {@link BindingModel}
	 */
	@Test
	@TestOrder(4)
	public void testCreateOWLModelSlot() throws SaveResourceException {

		newModelSlot = technologicalAdapter.makeModelSlot(OWLModelSlot.class, virtualModel1);
		newModelSlot.setName(OWL_MODEL_SLOT_NAME);

		newModelSlot.setMetaModelURI(BASIC_ONTOLOGY_URI);

		newModelSlot.setIsReadOnly(false);

		assertNotNull(newModelSlot);
		// TODO: ask Sylvain why we need this... when you provide a virtualModel container
		virtualModel1.addToModelSlots(newModelSlot);

		assertNotNull(virtualModel1.getModelSlot(OWL_MODEL_SLOT_NAME));

		assertNotNull(virtualModel1.getBindingModel().bindingVariableNamed(OWL_MODEL_SLOT_NAME));

		basicConcept = newModelSlot.getMetaModelResource().getMetaModelData().getClass(BASIC_ONTOLOGY_URI + "#" + "Concept");

		assertNotNull(basicConcept);

		hasObjectProperty = newModelSlot.getMetaModelResource().getMetaModelData().getObjectProperty(BASIC_ONTOLOGY_URI + "#" + "has");

		assertNotNull(hasObjectProperty);
	}

	/**
	 * Test FlexoConcept creation, check BindingModel
	 */
	@Test
	@TestOrder(5)
	public void testCreateFlexoConceptA() throws SaveResourceException {

		CreateFlexoConcept addFlexoConcept = CreateFlexoConcept.actionType.makeNewAction(virtualModel1, null, editor);
		addFlexoConcept.setNewFlexoConceptName("FlexoConceptA");
		addFlexoConcept.doAction();

		flexoConceptA = addFlexoConcept.getNewFlexoConcept();

		System.out.println("FlexoConcept A = " + flexoConceptA);
		assertNotNull(flexoConceptA);

		((VirtualModelResource) virtualModel1.getResource()).save(null);

		System.out.println("Saved: " + ((VirtualModelResource) virtualModel1.getResource()).getFlexoIODelegate().toString());

		System.out.println("FlexoConcept BindingModel = " + flexoConceptA.getBindingModel());

		assertEquals(8, flexoConceptA.getBindingModel().getBindingVariablesCount());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(ViewPointBindingModel.PROJECT_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				flexoConceptA.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getVirtualModelInstanceType(virtualModel1), flexoConceptA.getBindingModel()
				.bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConceptA), flexoConceptA.getBindingModel()
				.bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY).getType());

		// Disconnect FlexoConcept
		virtualModel1.removeFromFlexoConcepts(flexoConceptA);

		assertEquals(2, flexoConceptA.getBindingModel().getBindingVariablesCount());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY));

		// Reconnect FlexoConcept
		virtualModel1.addToFlexoConcepts(flexoConceptA);

		assertEquals(8, flexoConceptA.getBindingModel().getBindingVariablesCount());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(ViewPointBindingModel.PROJECT_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				flexoConceptA.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getVirtualModelInstanceType(virtualModel1), flexoConceptA.getBindingModel()
				.bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConceptA), flexoConceptA.getBindingModel()
				.bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY).getType());

	}

	@Test
	@TestOrder(6)
	public void testOWLIndividualRoleBindingModelManagement() throws SaveResourceException {

		CreatePrimitiveRole createPR1 = CreatePrimitiveRole.actionType.makeNewAction(flexoConceptA, null, editor);
		createPR1.setRoleName("aStringInA");
		createPR1.setPrimitiveType(PrimitiveType.String);
		createPR1.doAction();

		CreatePrimitiveRole createPR2 = CreatePrimitiveRole.actionType.makeNewAction(flexoConceptA, null, editor);
		createPR2.setRoleName("aBooleanInA");
		createPR2.setPrimitiveType(PrimitiveType.Boolean);
		createPR2.doAction();

		CreatePrimitiveRole createPR3 = CreatePrimitiveRole.actionType.makeNewAction(flexoConceptA, null, editor);
		createPR3.setRoleName("anIntegerInA");
		createPR3.setPrimitiveType(PrimitiveType.Integer);
		createPR3.doAction();

		CreateIndividualRole createPR4 = CreateIndividualRole.actionType.makeNewAction(flexoConceptA, null, editor);
		createPR4.setRoleName("anIndividual");
		createPR4.setFlexoRoleClass(OWLIndividualRole.class);
		createPR4.setIndividualType(basicConcept);
		createPR4.doAction();

		CreateTechnologyRole createPR5 = CreateTechnologyRole.actionType.makeNewAction(flexoConceptA, null, editor);
		createPR5.setRoleName("anObjectProperty");
		createPR5.setFlexoRoleClass(OWLObjectPropertyRole.class);
		createPR5.doAction();
		OWLObjectPropertyRole aRole = (OWLObjectPropertyRole) createPR5.getNewFlexoRole();
		aRole.setParentProperty(hasObjectProperty);

		CreateTechnologyRole createPR6 = CreateTechnologyRole.actionType.makeNewAction(flexoConceptA, null, editor);
		createPR6.setRoleName("anObjectPropertyStatement");
		createPR6.setFlexoRoleClass(ObjectPropertyStatementRole.class);
		createPR6.doAction();
		ObjectPropertyStatementRole bRole = (ObjectPropertyStatementRole) createPR6.getNewFlexoRole();
		bRole.setObjectProperty(hasObjectProperty);

		assertEquals(6, flexoConceptA.getFlexoProperties().size());
		assertTrue(flexoConceptA.getFlexoProperties().contains(createPR1.getNewFlexoRole()));
		assertTrue(flexoConceptA.getFlexoProperties().contains(createPR2.getNewFlexoRole()));
		assertTrue(flexoConceptA.getFlexoProperties().contains(createPR3.getNewFlexoRole()));
		assertTrue(flexoConceptA.getFlexoProperties().contains(createPR4.getNewFlexoRole()));
		assertTrue(flexoConceptA.getFlexoProperties().contains(createPR5.getNewFlexoRole()));

		System.out.println("FlexoConcept BindingModel = " + flexoConceptA.getBindingModel());
		System.out.println("OWLIndividualRole BindingModel = " + flexoConceptA.getAccessibleProperty("anIndividual").getBindingModel());

		FlexoPropertyBindingVariable bv = (FlexoPropertyBindingVariable) flexoConceptA.getBindingModel().bindingVariableNamed(
				"anIndividual");

		List<? extends SimplePathElement> listSPE = flexoConceptA.getBindingFactory().getAccessibleSimplePathElements(bv);

		System.out.println("** Accessible Elements for Individual");

		boolean found = false;
		for (SimplePathElement spe : listSPE) {
			System.out.println("   -- " + spe.getPropertyName() + " [ " + spe.getType().toString() + "]");
			if ("uriName".equals(spe.getPropertyName())) {
				found = true;
			}
		}

		bv = (FlexoPropertyBindingVariable) flexoConceptA.getBindingModel().bindingVariableNamed("anObjectProperty");
		listSPE = flexoConceptA.getBindingFactory().getAccessibleSimplePathElements(bv);

		System.out.println("\n** Accessible Elements for ObjectProperty");

		found = false;
		for (SimplePathElement spe : listSPE) {
			System.out.println("   -- " + spe.getPropertyName() + " [ " + spe.getType().toString() + "]");
			if ("domain".equals(spe.getPropertyName())) {
				found = true;
			}
		}

		bv = (FlexoPropertyBindingVariable) flexoConceptA.getBindingModel().bindingVariableNamed("anObjectPropertyStatement");
		listSPE = flexoConceptA.getBindingFactory().getAccessibleSimplePathElements(bv);

		System.out.println("\n** Accessible Elements for ObjectPropertyStatement");

		found = false;
		for (SimplePathElement spe : listSPE) {
			System.out.println("   -- " + spe.getPropertyName() + " [ " + spe.getType().toString() + "]");
		}

		assertEquals(14, flexoConceptA.getBindingModel().getBindingVariablesCount());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(ViewPointBindingModel.PROJECT_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				flexoConceptA.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getFlexoConceptInstanceType(virtualModel1), flexoConceptA.getBindingModel()
				.bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConceptA), flexoConceptA.getBindingModel()
				.bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY).getType());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed("aStringInA"));
		assertEquals(String.class, flexoConceptA.getBindingModel().bindingVariableNamed("aStringInA").getType());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed("aBooleanInA"));
		assertEquals(Boolean.class, flexoConceptA.getBindingModel().bindingVariableNamed("aBooleanInA").getType());
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed("anIntegerInA"));
		assertEquals(Integer.class, flexoConceptA.getBindingModel().bindingVariableNamed("anIntegerInA").getType());

		PrimitiveRole aStringInA = (PrimitiveRole) flexoConceptA.getAccessibleProperty("aStringInA");
		assertNotNull(aStringInA);

		bv = (FlexoPropertyBindingVariable) flexoConceptA.getBindingModel().bindingVariableNamed("aStringInA");
		assertNotNull(bv);

		// Attempt to change name
		renameWasNotified = false;
		bv.getPropertyChangeSupport().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(BindingVariable.VARIABLE_NAME_PROPERTY)) {
					renameWasNotified = true;
				}
			}
		});

		// Attempt to change type
		aStringInA.setName("aRenamedStringInA");
		if (!renameWasNotified) {
			fail("FlexoRole renaming was not notified");
		}

		typeChangedWasNotified = false;
		bv.getPropertyChangeSupport().addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(BindingVariable.TYPE_PROPERTY)) {
					typeChangedWasNotified = true;
				}
			}
		});

		aStringInA.setPrimitiveType(PrimitiveType.Float);
		if (!typeChangedWasNotified) {
			fail("FlexoRole type changing was not notified");
		}

		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed("aRenamedStringInA"));
		assertEquals(Float.class, flexoConceptA.getBindingModel().bindingVariableNamed("aRenamedStringInA").getType());

		// Back to initial values
		aStringInA.setName("aStringInA");
		aStringInA.setPrimitiveType(PrimitiveType.String);
		assertNotNull(flexoConceptA.getBindingModel().bindingVariableNamed("aStringInA"));
		assertEquals(String.class, flexoConceptA.getBindingModel().bindingVariableNamed("aStringInA").getType());

		System.out.println("FlexoConcept BindingModel = " + flexoConceptA.getBindingModel());
	}

	private void checkBindingVariableAccess(String variableName, Bindable owner, BindingEvaluationContext beContext, Object expectedValue) {
		BindingVariable bv = owner.getBindingModel().bindingVariableNamed(variableName);
		assertNotNull(bv);
		DataBinding<Object> db = new DataBinding<Object>(bv.getVariableName(), owner, bv.getType(), BindingDefinitionType.GET);
		assertTrue(db.isValid());
		try {
			assertEquals(expectedValue, db.getBindingValue(beContext));
		} catch (TypeMismatchException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (NullReferenceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	private void checkBinding(String binding, Bindable owner, BindingEvaluationContext beContext, Object expectedValue) {
		DataBinding<Object> db = new DataBinding<Object>(binding, owner, Object.class, BindingDefinitionType.GET);
		assertTrue(db.isValid());
		try {
			assertEquals(expectedValue, db.getBindingValue(beContext));
		} catch (TypeMismatchException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (NullReferenceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
