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
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateFlexoConcept;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateViewInFolder;
import org.openflexo.foundation.fml.rt.action.CreationSchemeAction;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration.DefaultModelSlotInstanceConfigurationOption;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.technologyadapter.emf.EMFModelSlot;
import org.openflexo.technologyadapter.emf.EMFModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.UMLEMFModelSlot;
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
public class TestUMLModelEdition extends OpenflexoProjectAtRunTimeTestCase {
	protected static final Logger logger = Logger.getLogger(TestEMFModelEdition.class.getPackage().getName());

	static FlexoEditor editor;
	static EMFTechnologyAdapter technologicalAdapter;
	static ViewPoint newViewPoint;
	static VirtualModel newVirtualModel;
	static EMFModelSlot newModelSlot = null;
	static EMFModelResource umlModelResource = null;
	static EMFMetaModelResource umlMetaModelResource = null;
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
		instanciateTestServiceManager(EMFTechnologyAdapter.class);

		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(EMFTechnologyAdapter.class);

		System.out.println("ResourceCenter= " + resourceCenter);
		newViewPoint = ViewPointImpl.newViewPoint("TestViewPoint", "http://openflexo.org/test/TestUMLViewPoint",
				resourceCenter.getDirectory(), serviceManager.getViewPointLibrary(), resourceCenter);
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

		umlMetaModelResource = technologicalAdapter.getTechnologyContextManager()
				.getMetaModelResourceByURI(EMFTechnologyAdapter.UML_MM_URI);

		assertNotNull(umlMetaModelResource);

		newVirtualModel = VirtualModelImpl.newVirtualModel("TestVirtualModel", newViewPoint);
		assertTrue(((ViewPointResource) newViewPoint.getResource()).getDirectory() != null);
		assertTrue(((ViewPointResource) newViewPoint.getResource()).getFlexoIODelegate().exists());
		newModelSlot = technologicalAdapter.makeModelSlot(UMLEMFModelSlot.class, newVirtualModel);
		newModelSlot.setMetaModelResource(umlMetaModelResource);
		assertNotNull(newModelSlot);
		newVirtualModel.addToModelSlots(newModelSlot);
		assertTrue(newVirtualModel.getModelSlots(UMLEMFModelSlot.class).size() == 1);
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

			assertNotNull(umlMetaModelResource);

			try {
				RepositoryFolder<FlexoResource<?>> modelFolder = project.createNewFolder("Models");
				umlModelResource = technologicalAdapter.createNewEMFModel(new File(modelFolder.getFile(), "coucou.uml"), "myURI",
						umlMetaModelResource, resourceCenter);
			} catch (Exception e) {
				e.printStackTrace();
			}

			assertNotNull(umlModelResource);

			umlModelResource.save(null);

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
		modelSlotInstanceConfiguration.setModelResource(umlModelResource);
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

		/**
		 * NamedElement e = null; Profile profile = null; profile.getAllProfileApplications(); e.getApplicableStereotypes();
		 **/

	}

	@Test
	@TestOrder(7)
	public void testEditEMFModelinVMI() {

		try {

			creationScheme = (CreationScheme) creationEditionScheme.getNewFlexoBehaviour();
			assertNotNull(creationScheme);

			creationSchemeCreationAction = CreationSchemeAction.actionType.makeNewAction(newVirtualModelInstance, null, editor);
			creationSchemeCreationAction.setCreationScheme(creationScheme);
			assertNotNull(creationSchemeCreationAction);

			/* TODO some stuff TO DO HERE */

			umlModelResource.save(null);

		} catch (FlexoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
