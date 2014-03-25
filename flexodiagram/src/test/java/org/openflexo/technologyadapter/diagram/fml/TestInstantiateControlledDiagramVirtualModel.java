package org.openflexo.technologyadapter.diagram.fml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.view.TypeAwareModelSlotInstance;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.view.VirtualModelModelSlotInstance;
import org.openflexo.foundation.view.action.CreateView;
import org.openflexo.foundation.view.action.CreateVirtualModelInstance;
import org.openflexo.foundation.view.action.ModelSlotInstanceConfiguration.DefaultModelSlotInstanceConfigurationOption;
import org.openflexo.foundation.view.rm.ViewResource;
import org.openflexo.foundation.view.rm.VirtualModelInstanceResource;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.ViewPointLibrary;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.rm.ViewPointResource;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.action.DropSchemeAction;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the instantiation of a VirtualModel whose instances have {@link ControlledDiagramNature}
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestInstantiateControlledDiagramVirtualModel extends OpenflexoProjectAtRunTimeTestCase {

	private static FlexoEditor editor;
	private static FlexoProject project;

	private static ViewPoint viewPoint;
	private static VirtualModel virtualModel;
	private static FlexoConcept flexoConcept;
	private static DropScheme dropScheme;
	private static DiagramSpecification diagramSpecification;
	private static DiagramPalette palette;
	private static DiagramPaletteElement paletteElement;

	private static View newView;
	private static VirtualModelInstance newVirtualModelInstance;
	private static Diagram diagram;

	/**
	 * Retrieve the ViewPoint
	 */
	@Test
	@TestOrder(1)
	public void testLoadViewPoint() {
		instanciateTestServiceManager();
		ViewPointLibrary vpLib = serviceManager.getViewPointLibrary();
		assertNotNull(vpLib);
		viewPoint = vpLib.getViewPoint("http://openflexo.org/test/TestControlledDiagramViewPoint");
		assertNotNull(viewPoint);

		virtualModel = viewPoint.getVirtualModelNamed("TestVirtualModel");
		assertNotNull(virtualModel);
		assertTrue(virtualModel.hasNature(ControlledDiagramNature.INSTANCE));

		flexoConcept = virtualModel.getFlexoConcepts().get(0);
		assertNotNull(flexoConcept);

		dropScheme = (DropScheme) flexoConcept.getFlexoBehaviours().get(0);
		assertNotNull(dropScheme);

		DiagramTechnologyAdapter diagramTA = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(
				DiagramTechnologyAdapter.class);
		DiagramSpecificationRepository repository = resourceCenter.getRepository(DiagramSpecificationRepository.class, diagramTA);
		DiagramSpecificationResource diagramSpecificationResource = repository
				.getResource("http://openflexo.org/test/TestDiagramSpecification");
		assertNotNull(diagramSpecificationResource);
		assertNotNull(diagramSpecification = diagramSpecificationResource.getDiagramSpecification());
		assertEquals(1, diagramSpecificationResource.getDiagramSpecification().getPalettes().size());
		assertNotNull(palette = diagramSpecificationResource.getDiagramSpecification().getPalettes().get(0));
		assertEquals(1, palette.getElements().size());
		assertNotNull(paletteElement = palette.getElements().get(0));

	}

	@Test
	@TestOrder(2)
	public void testCreateProject() {
		editor = createProject("TestProject");
		project = editor.getProject();
		System.out.println("Created project " + project.getProjectDirectory());
		assertTrue(project.getProjectDirectory().exists());
		assertTrue(project.getProjectDataResource().getFile().exists());
	}

	/**
	 * Instantiate in project a View conform to the ViewPoint
	 */
	@Test
	@TestOrder(3)
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
		assertTrue(((ViewResource) newView.getResource()).getDirectory().exists());
		assertTrue(((ViewResource) newView.getResource()).getFile().exists());
	}

	/**
	 * Instantiate in project a VirtualModelInstance conform to the VirtualModel
	 */
	@Test
	@TestOrder(4)
	public void testCreateVirtualModelInstance() {

		log("testCreateVirtualModelInstance()");

		assertEquals(1, virtualModel.getModelSlots(TypedDiagramModelSlot.class).size());
		TypedDiagramModelSlot ms = virtualModel.getModelSlots(TypedDiagramModelSlot.class).get(0);
		assertNotNull(ms);

		CreateVirtualModelInstance action = CreateVirtualModelInstance.actionType.makeNewAction(newView, null, editor);
		action.setNewVirtualModelInstanceName("MyVirtualModelInstance");
		action.setNewVirtualModelInstanceTitle("Test creation of a new VirtualModelInstance");
		action.setVirtualModel(virtualModel);

		TypedDiagramModelSlotInstanceConfiguration diagramModelSlotInstanceConfiguration = (TypedDiagramModelSlotInstanceConfiguration) action
				.getModelSlotInstanceConfiguration(ms);
		assertNotNull(diagramModelSlotInstanceConfiguration);
		diagramModelSlotInstanceConfiguration.setOption(DefaultModelSlotInstanceConfigurationOption.CreatePrivateNewModel);
		assertTrue(diagramModelSlotInstanceConfiguration.isValidConfiguration());

		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		newVirtualModelInstance = action.getNewVirtualModelInstance();
		assertNotNull(newVirtualModelInstance);
		assertNotNull(newVirtualModelInstance.getResource());
		assertTrue(((ViewResource) newView.getResource()).getDirectory().exists());
		assertTrue(((ViewResource) newView.getResource()).getFile().exists());
		assertEquals(2, newVirtualModelInstance.getModelSlotInstances().size());

		VirtualModelModelSlotInstance reflexiveMSInstance = (VirtualModelModelSlotInstance) newVirtualModelInstance.getModelSlotInstances()
				.get(0);
		assertNotNull(reflexiveMSInstance);
		assertEquals(newVirtualModelInstance, reflexiveMSInstance.getAccessedResourceData());

		TypeAwareModelSlotInstance<Diagram, DiagramSpecification, TypedDiagramModelSlot> diagramMSInstance = (TypeAwareModelSlotInstance<Diagram, DiagramSpecification, TypedDiagramModelSlot>) newVirtualModelInstance
				.getModelSlotInstances().get(1);
		assertNotNull(diagramMSInstance);
		assertNotNull(diagram = diagramMSInstance.getAccessedResourceData());
		assertNotNull(diagramMSInstance.getResource());
		assertTrue(((DiagramResource) diagramMSInstance.getResource()).getFile().exists());

		assertTrue(newVirtualModelInstance.hasNature(ControlledDiagramInstanceNature.INSTANCE));

		assertFalse(diagram.isModified());
		assertFalse(newVirtualModelInstance.isModified());

	}

	/**
	 * Try to populate VirtualModelInstance
	 * 
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(5)
	public void testPopulateVirtualModelInstance() throws SaveResourceException {

		log("testPopulateVirtualModelInstance()");

		VirtualModelInstanceResource vmiRes = (VirtualModelInstanceResource) newVirtualModelInstance.getResource();

		assertFalse(diagram.isModified());
		assertFalse(newVirtualModelInstance.isModified());

		System.out.println(vmiRes.getFactory().stringRepresentation(vmiRes.getLoadedResourceData()));

		DropSchemeAction action = DropSchemeAction.actionType.makeNewAction(newVirtualModelInstance, null, editor);
		action.setDropScheme(dropScheme);
		action.setParent(diagram);
		action.setPaletteElement(paletteElement);
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
		assertTrue(((VirtualModelInstanceResource) newVirtualModelInstance.getResource()).getFile().exists());
		assertFalse(newVirtualModelInstance.isModified());

		diagram.getResource().save(null);
		assertTrue(((DiagramResource) diagram.getResource()).getFile().exists());
		assertFalse(diagram.isModified());

	}

}
