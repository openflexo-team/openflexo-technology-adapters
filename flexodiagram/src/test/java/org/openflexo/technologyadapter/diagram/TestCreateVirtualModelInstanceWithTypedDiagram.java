package org.openflexo.technologyadapter.diagram;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.view.TypeAwareModelSlotInstance;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.view.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.view.action.CreateView;
import org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration.DefaultModelSlotInstanceConfigurationOption;
import org.openflexo.foundation.view.rm.ViewResource;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.action.CreateModelSlot;
import org.openflexo.foundation.viewpoint.action.CreateVirtualModel;
import org.openflexo.foundation.viewpoint.rm.ViewPointResource;
import org.openflexo.foundation.viewpoint.rm.VirtualModelResource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * This unit test is intented to test VirtualModelInstance using a {@link TypedDiagramModelSlot}
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestCreateVirtualModelInstanceWithTypedDiagram extends OpenflexoProjectAtRunTimeTestCase {

	private static ViewPoint newViewPoint;
	private static VirtualModel newVirtualModel;
	private static FlexoEditor editor;
	private static FlexoProject project;
	private static View newView;
	private static VirtualModelInstance newVirtualModelInstance;

	/**
	 * Instantiate a ViewPoint with a VirtualModel
	 * 
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(1)
	public void testCreateViewPoint() throws SaveResourceException {
		instanciateTestServiceManager();
		System.out.println("ResourceCenter= " + resourceCenter);
		newViewPoint = ViewPointImpl.newViewPoint("TestViewPoint", "http://openflexo.org/test/TestViewPoint",
				resourceCenter.getDirectory(), serviceManager.getViewPointLibrary());
		assertNotNull(newViewPoint);
		assertNotNull(newViewPoint.getResource());
		//assertTrue(((ViewPointResource) newViewPoint.getResource()).getDirectory().exists());
		//assertTrue(((ViewPointResource) newViewPoint.getResource()).getFile().exists());
		assertTrue(((ViewPointResource) newViewPoint.getResource()).getDirectory()!=null);
		assertTrue(((ViewPointResource) newViewPoint.getResource()).getFlexoIODelegate().exists());
		CreateVirtualModel action = CreateVirtualModel.actionType.makeNewAction(newViewPoint, null, editor);
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
		CreateVirtualModel action = CreateVirtualModel.actionType.makeNewAction(newViewPoint, null, editor);
		action.setNewVirtualModelName("TestVirtualModel");
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		newVirtualModel = action.getNewVirtualModel();
		// newVirtualModel = VirtualModelImpl.newVirtualModel("TestVirtualModel", newViewPoint);
		//assertTrue(((VirtualModelResource) newVirtualModel.getResource()).getDirectory().exists());
		//assertTrue(((VirtualModelResource) newVirtualModel.getResource()).getFile().exists());
		assertTrue(ResourceLocator.retrieveResourceAsFile(((VirtualModelResource) newVirtualModel.getResource()).getDirectory()).exists());
		assertTrue(((VirtualModelResource) newVirtualModel.getResource()).getFlexoIODelegate().exists());

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

	/**
	 * Instantiate in project a View conform to the ViewPoint
	 */
	@Test
	@TestOrder(4)
	public void testCreateView() {
		CreateView action = CreateView.actionType.makeNewAction(project.getViewLibrary().getRootFolder(), null, editor);
		action.setNewViewName("MyView");
		action.setNewViewTitle("Test creation of a new view");
		action.setViewpointResource((ViewPointResource) newViewPoint.getResource());
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		newView = action.getNewView();
		assertNotNull(newView);
		assertNotNull(newView.getResource());
		try {
			newView.getResource().save(null);
		} catch (SaveResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//assertTrue(((ViewResource) newView.getResource()).getDirectory().exists());
		//assertTrue(((ViewResource) newView.getResource()).getFile().exists());
		assertTrue(ResourceLocator.retrieveResourceAsFile(((ViewResource) newView.getResource()).getDirectory())!=null);
		assertTrue(((ViewResource) newView.getResource()).getFlexoIODelegate().exists());
	}

	/**
	 * Instantiate in project a VirtualModelInstance conform to the VirtualModel
	 */
	@Test
	@TestOrder(5)
	public void testCreateVirtualModelInstance() {
		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType.makeNewAction(newView, null, editor);
		action.setNewVirtualModelInstanceName("MyVirtualModelInstance");
		action.setNewVirtualModelInstanceTitle("Test creation of a new VirtualModelInstance");
		action.setVirtualModel(newVirtualModel);

		TypedDiagramModelSlot diagramModelSlot = newVirtualModel.getModelSlots(TypedDiagramModelSlot.class).get(0);
		assertNotNull(diagramModelSlot);
		TypedDiagramModelSlotInstanceConfiguration diagramModelSlotInstanceConfiguration = (TypedDiagramModelSlotInstanceConfiguration) action
				.getModelSlotInstanceConfiguration(diagramModelSlot);
		diagramModelSlotInstanceConfiguration.setOption(DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel);
		/*File modelFile1 = new File(((FileSystemBasedResourceCenter) resourceCenter).getRootDirectory(), "EMF/Model/city1/my.city1");
		System.out.println("Searching " + modelFile1.getAbsolutePath());
		assertTrue(modelFile1.exists());
		System.out.println("Searching " + modelFile1.toURI().toString());
		FlexoModelResource<?, ?, ?> modelResource1 = project.getServiceManager().getInformationSpace()
				.getModelWithURI(modelFile1.toURI().toString());
		assertNotNull(modelResource1);
		emfModelSlotConfiguration1.setModelResource(modelResource1);*/
		assertTrue(diagramModelSlotInstanceConfiguration.isValidConfiguration());

		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		newVirtualModelInstance = action.getNewVirtualModelInstance();
		assertNotNull(newVirtualModelInstance);
		assertNotNull(newVirtualModelInstance.getResource());
		//assertTrue(((ViewResource) newView.getResource()).getDirectory().exists());
		//assertTrue(((ViewResource) newView.getResource()).getFile().exists());
		assertTrue(ResourceLocator.retrieveResourceAsFile(((ViewResource) newView.getResource()).getDirectory())!=null);
		assertTrue(((ViewResource) newView.getResource()).getFlexoIODelegate().exists());

		assertEquals(1, newVirtualModelInstance.getModelSlotInstances().size());

		/*VirtualModelModelSlotInstance reflexiveMSInstance = (VirtualModelModelSlotInstance) newVirtualModelInstance
				.getModelSlotInstance(newVirtualModel.getReflexiveModelSlot());
		assertNotNull(reflexiveMSInstance);
		assertEquals(newVirtualModelInstance, reflexiveMSInstance.getAccessedResourceData());*/

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

		instanciateTestServiceManager();
		editor = reloadProject(project.getDirectory());
		project = editor.getProject();
		assertNotNull(editor);
		assertNotNull(project);
		ViewResource newViewResource = project.getViewLibrary().getView(newView.getURI());
		assertNotNull(newViewResource);
		assertNull(newViewResource.getLoadedResourceData());
		newViewResource.loadResourceData(null);
		assertNotNull(newViewResource.getLoadedResourceData());
		newView = newViewResource.getLoadedResourceData();
	}

}
