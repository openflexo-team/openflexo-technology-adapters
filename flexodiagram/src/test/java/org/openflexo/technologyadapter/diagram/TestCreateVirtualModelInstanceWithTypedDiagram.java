/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.diagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModelRepository;
import org.openflexo.foundation.fml.action.CreateContainedVirtualModel;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateModelSlot;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rm.VirtualModelResourceFactory;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResource;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.test.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.fml.editionaction.CreateDiagram;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * This unit test is intented to test FMLRTVirtualModelInstance using a {@link TypedDiagramModelSlot}
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestCreateVirtualModelInstanceWithTypedDiagram extends OpenflexoProjectAtRunTimeTestCase {

	public static final String VIEWPOINT_NAME = "TestViewPoint";
	public static final String VIEWPOINT_URI = "http://openflexo.org/test/TestViewPoint";
	public static final String VIRTUAL_MODEL_NAME = "TestVirtualModel";

	private static VirtualModelResource newViewPointResource;
	private static VirtualModel newViewPoint;
	private static VirtualModel newVirtualModel;
	private static FlexoEditor editor;
	private static FlexoProject project;
	private static FMLRTVirtualModelInstance newView;
	private static FMLRTVirtualModelInstance newVirtualModelInstance;

	private static DirectoryResourceCenter newResourceCenter;

	/**
	 * Instantiate a ViewPoint with a VirtualModel
	 * 
	 * @throws SaveResourceException
	 * @throws ModelDefinitionException
	 * @throws IOException
	 */
	@Test
	@TestOrder(1)
	public void testCreateViewPoint() throws SaveResourceException, ModelDefinitionException, IOException {
		instanciateTestServiceManager(DiagramTechnologyAdapter.class);

		FMLTechnologyAdapter fmlTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(FMLTechnologyAdapter.class);
		VirtualModelResourceFactory factory = fmlTechnologyAdapter.getVirtualModelResourceFactory();

		newResourceCenter = makeNewDirectoryResourceCenter(serviceManager);

		newViewPointResource = factory.makeTopLevelVirtualModelResource(VIEWPOINT_NAME, VIEWPOINT_URI,
				fmlTechnologyAdapter.getGlobalRepository(newResourceCenter).getRootFolder(),
				fmlTechnologyAdapter.getTechnologyContextManager(), true);
		newViewPoint = newViewPointResource.getLoadedResourceData();

		// newViewPoint = ViewPointImpl.newViewPoint("TestViewPoint",
		// "http://openflexo.org/test/TestViewPoint",
		// resourceCenter.getDirectory(),
		// serviceManager.getViewPointLibrary(), resourceCenter);
		assertNotNull(newViewPoint);
		assertNotNull(newViewPoint.getResource());
		// assertTrue(((ViewPointResource)
		// newViewPoint.getResource()).getDirectory().exists());
		// assertTrue(((ViewPointResource)
		// newViewPoint.getResource()).getFile().exists());
		assertTrue(((VirtualModelResource) newViewPoint.getResource()).getDirectory() != null);
		assertTrue(((VirtualModelResource) newViewPoint.getResource()).getIODelegate().exists());
		CreateContainedVirtualModel action = CreateContainedVirtualModel.actionType.makeNewAction(newViewPoint, null, editor);
		action.setNewVirtualModelName("TestVirtualModel");
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
	}

	/**
	 * Instantiate a ViewPoint with a VirtualModel
	 * 
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(2)
	public void testCreateVirtualModel() throws SaveResourceException {
		CreateContainedVirtualModel action = CreateContainedVirtualModel.actionType.makeNewAction(newViewPoint, null, editor);
		action.setNewVirtualModelName(VIRTUAL_MODEL_NAME);
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		newVirtualModel = action.getNewVirtualModel();
		// newVirtualModel =
		// VirtualModelImpl.newVirtualModel("TestVirtualModel", newViewPoint);
		// assertTrue(((VirtualModelResource)
		// newVirtualModel.getResource()).getDirectory().exists());
		// assertTrue(((VirtualModelResource)
		// newVirtualModel.getResource()).getFile().exists());
		assertTrue(ResourceLocator.retrieveResourceAsFile(((VirtualModelResource) newVirtualModel.getResource()).getDirectory()).exists());
		assertTrue(((VirtualModelResource) newVirtualModel.getResource()).getIODelegate().exists());

		// Now we create the diagram model slot
		CreateModelSlot createMS = CreateModelSlot.actionType.makeNewAction(newVirtualModel, null, editor);
		createMS.setTechnologyAdapter(serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class));
		createMS.setModelSlotClass(TypedDiagramModelSlot.class);
		createMS.setModelSlotName("diagram");
		createMS.doAction();

		assertTrue(createMS.hasActionExecutionSucceeded());

		// VirtualModel should have only one TypedDiagramModelSlot
		assertEquals(1, newVirtualModel.getModelSlots(TypedDiagramModelSlot.class).size());

		TypedDiagramModelSlot diagramModelSlot = newVirtualModel.getModelSlots(TypedDiagramModelSlot.class).get(0);
		assertNotNull(diagramModelSlot);

		// We create a CreationScheme
		CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewAction(newVirtualModel, null, editor);
		createCreationScheme.setFlexoBehaviourName("create");
		createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
		createCreationScheme.doAction();
		assertTrue(createCreationScheme.hasActionExecutionSucceeded());
		CreationScheme creationScheme = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();

		CreateEditionAction createDiagramAction = CreateEditionAction.actionType.makeNewAction(creationScheme.getControlGraph(), null,
				_editor);
		createDiagramAction.setEditionActionClass(CreateDiagram.class);
		createDiagramAction.setAssignation(new DataBinding<>("diagram"));
		createDiagramAction.doAction();
		assertTrue(createDiagramAction.hasActionExecutionSucceeded());

		CreateDiagram createDiagram = (CreateDiagram) createDiagramAction.getBaseEditionAction();
		createDiagram.setDiagramName(new DataBinding<>("'TestDiagram'"));
		createDiagram.setResourceCenter(new DataBinding<>("this.resourceCenter"));

		System.out.println("FML: " + newVirtualModel.getFMLRepresentation());
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

	/**
	 * Instantiate in project a View conform to the ViewPoint
	 */
	@Test
	@TestOrder(4)
	public void testCreateView() {
		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType
				.makeNewAction(project.getVirtualModelInstanceRepository().getRootFolder(), null, editor);
		action.setNewVirtualModelInstanceName("MyView");
		action.setNewVirtualModelInstanceTitle("Test creation of a new view");
		action.setVirtualModel(newViewPoint);
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		newView = action.getNewVirtualModelInstance();
		assertNotNull(newView);
		assertNotNull(newView.getResource());
		try {
			newView.getResource().save(null);
		} catch (SaveResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// assertTrue(((ViewResource)
		// newView.getResource()).getDirectory().exists());
		// assertTrue(((ViewResource)
		// newView.getResource()).getFile().exists());
		assertTrue(
				ResourceLocator.retrieveResourceAsFile(((FMLRTVirtualModelInstanceResource) newView.getResource()).getDirectory()) != null);
		assertTrue(((FMLRTVirtualModelInstanceResource) newView.getResource()).getIODelegate().exists());
	}

	/**
	 * Instantiate in project a FMLRTVirtualModelInstance conform to the VirtualModel
	 */
	@Test
	@TestOrder(5)
	public void testCreateVirtualModelInstance() {
		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType.makeNewAction(newView, null, editor);
		action.setNewVirtualModelInstanceName("MyVirtualModelInstance");
		action.setNewVirtualModelInstanceTitle("Test creation of a new FMLRTVirtualModelInstance");
		action.setVirtualModel(newVirtualModel);
		action.setCreationScheme(newVirtualModel.getCreationSchemes().get(0));

		/*TypedDiagramModelSlot diagramModelSlot = newVirtualModel.getModelSlots(TypedDiagramModelSlot.class).get(0);
		assertNotNull(diagramModelSlot);
		TypedDiagramModelSlotInstanceConfiguration diagramModelSlotInstanceConfiguration = (TypedDiagramModelSlotInstanceConfiguration) action
				.getModelSlotInstanceConfiguration(diagramModelSlot);
		diagramModelSlotInstanceConfiguration.setOption(DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel);
		assertTrue(diagramModelSlotInstanceConfiguration.isValidConfiguration());*/

		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		newVirtualModelInstance = action.getNewVirtualModelInstance();
		assertNotNull(newVirtualModelInstance);
		assertNotNull(newVirtualModelInstance.getResource());
		assertTrue(
				ResourceLocator.retrieveResourceAsFile(((FMLRTVirtualModelInstanceResource) newView.getResource()).getDirectory()) != null);
		assertTrue(((FMLRTVirtualModelInstanceResource) newView.getResource()).getIODelegate().exists());

		assertEquals(1, newVirtualModelInstance.getModelSlotInstances().size());

		TypeAwareModelSlotInstance diagramMSInstance = (TypeAwareModelSlotInstance) newVirtualModelInstance
				.getModelSlotInstance(newVirtualModel.getModelSlots(TypedDiagramModelSlot.class).get(0));
		assertNotNull(diagramMSInstance);

	}

	/**
	 * Reload project, check that everything is still ok
	 * 
	 * @throws FileNotFoundException
	 * @throws ResourceLoadingCancelledException
	 * @throws FlexoException
	 */
	@Test
	@TestOrder(6)
	public void testReloadProject() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		instanciateTestServiceManager(DiagramTechnologyAdapter.class);

		serviceManager.getResourceCenterService()
				.addToResourceCenters(newResourceCenter = new DirectoryResourceCenter(newResourceCenter.getDirectory(),
						serviceManager.getResourceCenterService()));
		newResourceCenter.performDirectoryWatchingNow();

		/*
		 * FlexoResourceCenter<?> resourceCenter =
		 * serviceManager.getResourceCenterService()
		 * .getFlexoResourceCenter("http://openflexo.org/diagram-test");
		 * 
		 * File directory =
		 * ResourceLocator.retrieveResourceAsFile(newViewPointResource.
		 * getDirectory()); File newDirectory = new
		 * File(((FileSystemBasedResourceCenter) resourceCenter).getDirectory(),
		 * directory.getName()); newDirectory.mkdirs();
		 * 
		 * try { FileUtils.copyContentDirToDir(directory, newDirectory); // We
		 * wait here for the thread monitoring ResourceCenters to detect // new
		 * files ((FileSystemBasedResourceCenter)
		 * resourceCenter).performDirectoryWatchingNow(); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

		editor = reloadProject(project.getDirectory());
		project = editor.getProject();
		assertNotNull(editor);
		assertNotNull(project);
		FMLRTVirtualModelInstanceResource newViewResource = project.getVirtualModelInstanceRepository()
				.getVirtualModelInstance(newView.getURI());
		assertNotNull(newViewResource);

		System.out.println("view in " + newViewResource.getIODelegate().getSerializationArtefact());

		VirtualModelRepository<?> vpRep = newResourceCenter.getVirtualModelRepository();
		for (VirtualModelResource r : vpRep.getAllResources()) {
			System.out.println("> " + r.getURI());
		}

		assertNull(newViewResource.getLoadedResourceData());

		newViewResource.loadResourceData(null);
		assertNotNull(newViewResource.getLoadedResourceData());
		newView = newViewResource.getLoadedResourceData();
	}

}
