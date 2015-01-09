package org.openflexo.technologyadapter.diagram.fml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.shapes.Rectangle;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPoint.ViewPointImpl;
import org.openflexo.foundation.fml.ViewPointLibrary;
import org.openflexo.foundation.fml.ViewType;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModel.VirtualModelImpl;
import org.openflexo.foundation.fml.VirtualModelInstanceType;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateEditionAction.CreateEditionActionChoice;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateFlexoRole;
import org.openflexo.foundation.fml.binding.FlexoBehaviourBindingModel;
import org.openflexo.foundation.fml.binding.FlexoConceptBindingModel;
import org.openflexo.foundation.fml.binding.ViewPointBindingModel;
import org.openflexo.foundation.fml.binding.VirtualModelBindingModel;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.JarResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramSpecification;
import org.openflexo.technologyadapter.diagram.fml.binding.DiagramBehaviourBindingModel;
import org.openflexo.technologyadapter.diagram.fml.binding.DropSchemeBindingModel;
import org.openflexo.technologyadapter.diagram.fml.binding.LinkSchemeBindingModel;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddConnector;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.DiagramType;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.toolbox.FileUtils;

/**
 * Test the BindingModel management of some diagram-specific features
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDiagramFeaturesBindingModelManagement extends OpenflexoTestCase {

	private final String DIAGRAM_SPECIFICATION_NAME = "myDiagramSpecification";
	private final String DIAGRAM_SPECIFICATION_URI = "http://myDiagramSpecification";
	private final String PALETTE_NAME = "myDiagramSpecificationPalette";
	private final String PALETTE_ELEMENT_NAME = "myPaletteElement";

	private final String VIEWPOINT_NAME = "TestViewPoint";
	private final String VIEWPOINT_URI = "http://openflexo.org/test/TestViewPoint";

	public static DiagramTechnologyAdapter technologicalAdapter;
	public static FlexoServiceManager applicationContext;
	public static FlexoResourceCenter<?> resourceCenter;
	public static DiagramSpecificationRepository repository;
	public static FlexoEditor editor;

	public static DiagramSpecificationResource diagramSpecificationResource;
	public static DiagramPaletteResource paletteResource;

	private static DiagramSpecification diagramSpecification;

	public static DiagramPalette palette;
	public static DiagramPaletteElement paletteElement;

	public static ViewPoint viewPoint;
	public static ViewPointResource viewPointResource;
	public static TypedDiagramModelSlot typedDiagramModelSlot;
	public static VirtualModel virtualModel;
	public static FlexoConcept flexoConcept;
	public static FlexoConcept flexoConcept2;
	public static DropScheme dropScheme;
	public static LinkScheme linkScheme;

	/**
	 * Initialize
	 */
	@Test
	@TestOrder(1)
	public void testInitialize() {

		log("testInitialize()");

		applicationContext = instanciateTestServiceManager();

		technologicalAdapter = applicationContext.getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
		resourceCenter = applicationContext.getResourceCenterService().getResourceCenters().get(0);
		repository = resourceCenter.getRepository(DiagramSpecificationRepository.class, technologicalAdapter);
		editor = new FlexoTestEditor(null, applicationContext);

		assertNotNull(applicationContext);
		assertNotNull(technologicalAdapter);
		assertNotNull(resourceCenter);
		assertNotNull(repository);
	}

	/**
	 * Test Create diagram specification resource
	 */
	@Test
	@TestOrder(2)
	public void testCreateDiagramSpecification() {

		log("testCreateDiagramSpecification()");

		CreateDiagramSpecification action = CreateDiagramSpecification.actionType.makeNewAction(repository.getRootFolder(), null, editor);
		action.setNewDiagramSpecificationName(DIAGRAM_SPECIFICATION_NAME);
		action.setNewDiagramSpecificationURI(DIAGRAM_SPECIFICATION_URI);

		action.doAction();

		assertTrue(action.hasActionExecutionSucceeded());

		diagramSpecificationResource = action.getNewDiagramSpecification().getResource();

		assertNotNull(diagramSpecificationResource);
		assertTrue(diagramSpecificationResource.getFlexoIODelegate().exists());

	}

	/**
	 * Test palettes
	 * 
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(3)
	public void testCreatePalette() throws SaveResourceException {

		log("testCreatePalette()");

		CreateDiagramPalette action = CreateDiagramPalette.actionType.makeNewAction(diagramSpecificationResource.getDiagramSpecification(),
				null, editor);
		action.setNewPaletteName(PALETTE_NAME);

		action.doAction();

		assertTrue(action.hasActionExecutionSucceeded());

		paletteResource = action.getNewPalette().getResource();

		assertNotNull(paletteResource);
		assertTrue(paletteResource.getFlexoIODelegate().exists());
		assertTrue(diagramSpecificationResource.getDiagramPaletteResources().contains(paletteResource));

		assertEquals(1, diagramSpecificationResource.getDiagramSpecification().getPalettes().size());

		// Add palette element
		DiagramPalette palette = paletteResource.getDiagramPalette();

		paletteElement = paletteResource.getFactory().makeDiagramPaletteElement();
		paletteElement.setName(PALETTE_ELEMENT_NAME);
		ShapeGraphicalRepresentation shapeGR = paletteResource.getFactory().makeShapeGraphicalRepresentation();
		shapeGR.setShapeSpecification(paletteResource.getFactory().makeShape(ShapeType.RECTANGLE));
		shapeGR.setForeground(paletteResource.getFactory().makeForegroundStyle(Color.RED));
		shapeGR.setBackground(paletteResource.getFactory().makeColoredBackground(Color.BLUE));
		shapeGR.setX(10);
		shapeGR.setY(10);
		shapeGR.setWidth(100);
		shapeGR.setHeight(80);
		paletteElement.setGraphicalRepresentation(shapeGR);
		paletteResource.getDiagramPalette().addToElements(paletteElement);

		paletteResource.save(null);

	}

	/**
	 * Test the VP creation
	 */
	@Test
	@TestOrder(4)
	public void testCreateViewPoint() {

		log("testCreateViewPoint()");

		viewPoint = ViewPointImpl.newViewPoint(VIEWPOINT_NAME, VIEWPOINT_URI,
				((FileSystemBasedResourceCenter) resourceCenter).getDirectory(), serviceManager.getViewPointLibrary());
		viewPointResource = (ViewPointResource) viewPoint.getResource();
		assertTrue(ResourceLocator.retrieveResourceAsFile(viewPointResource.getDirectory()).exists());
		assertTrue(viewPointResource.getFlexoIODelegate().exists());
	}

	/**
	 * Test the VirtualModel creation, check BindingModel management
	 */
	@Test
	@TestOrder(5)
	public void testCreateVirtualModel() throws SaveResourceException {

		log("testCreateVirtualModel()");

		virtualModel = VirtualModelImpl.newVirtualModel("TestVirtualModel", viewPoint);
		assertTrue(ResourceLocator.retrieveResourceAsFile(((VirtualModelResource) virtualModel.getResource()).getDirectory()).exists());
		assertTrue(((VirtualModelResource) virtualModel.getResource()).getFlexoIODelegate().exists());

		typedDiagramModelSlot = technologicalAdapter.makeModelSlot(TypedDiagramModelSlot.class, virtualModel);
		typedDiagramModelSlot.setMetaModelResource(diagramSpecificationResource);
		typedDiagramModelSlot.setName("diagram");
		assertNotNull(typedDiagramModelSlot);

		virtualModel.addToModelSlots(typedDiagramModelSlot);
		assertTrue(virtualModel.getModelSlots(TypedDiagramModelSlot.class).size() == 1);

		flexoConcept = virtualModel.getVirtualModelFactory().newInstance(FlexoConcept.class);
		flexoConcept.setName("Concept");
		virtualModel.addToFlexoConcepts(flexoConcept);

		CreateFlexoRole createShapeRole = CreateFlexoRole.actionType.makeNewAction(flexoConcept, null, editor);
		createShapeRole.setRoleName("shape");
		createShapeRole.setFlexoRoleClass(ShapeRole.class);
		createShapeRole.doAction();
		assertTrue(createShapeRole.hasActionExecutionSucceeded());

		ShapeRole role = (ShapeRole) createShapeRole.getNewFlexoRole();
		FMLModelFactory factory = flexoConcept.getVirtualModelFactory();
		ShapeGraphicalRepresentation shapeGR = factory.newInstance(ShapeGraphicalRepresentation.class);
		Rectangle rectangleShape = factory.newInstance(Rectangle.class);
		shapeGR.setShapeSpecification(rectangleShape);
		role.setGraphicalRepresentation(shapeGR);

		virtualModel.getResource().save(null);

		System.out.println(virtualModel.getVirtualModelFactory().stringRepresentation(virtualModel));

		assertTrue(virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE));
		assertEquals(typedDiagramModelSlot, FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(virtualModel));

		assertNotNull(virtualModel.getBindingModel());
		assertEquals(5, virtualModel.getBindingModel().getBindingVariablesCount());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				virtualModel.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getVirtualModelInstanceType(virtualModel), virtualModel.getBindingModel()
				.bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(virtualModel.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertTrue(((DiagramType) virtualModel.getBindingModel().bindingVariableNamed("diagram").getType()).getDiagramSpecification() == diagramSpecificationResource
				.getDiagramSpecification());

	}

	/**
	 * Test the DropScheme creation, check BindingModel management
	 */
	@Test
	@TestOrder(6)
	public void testCreateDropScheme() throws SaveResourceException {

		log("testCreateDropScheme()");

		CreateFlexoBehaviour createDropScheme = CreateFlexoBehaviour.actionType.makeNewAction(flexoConcept, null, editor);
		createDropScheme.setFlexoBehaviourName("drop");
		createDropScheme.setFlexoBehaviourClass(DropScheme.class);
		createDropScheme.doAction();
		assertTrue(createDropScheme.hasActionExecutionSucceeded());
		dropScheme = (DropScheme) createDropScheme.getNewFlexoBehaviour();

		CreateEditionAction createAddShape = CreateEditionAction.actionType.makeNewAction(dropScheme.getControlGraph(), null, editor);
		createAddShape.actionChoice = CreateEditionActionChoice.ModelSlotSpecificAction;
		createAddShape.setModelSlot(typedDiagramModelSlot);
		createAddShape.setModelSlotSpecificActionClass(AddShape.class);
		createAddShape.doAction();
		assertTrue(createAddShape.hasActionExecutionSucceeded());

		FMLDiagramPaletteElementBinding newBinding = virtualModel.getVirtualModelFactory().newInstance(
				FMLDiagramPaletteElementBinding.class);
		newBinding.setPaletteElement(paletteElement);
		newBinding.setBoundFlexoConcept(flexoConcept);
		newBinding.setDropScheme(dropScheme);

		typedDiagramModelSlot.addToPaletteElementBindings(newBinding);

		virtualModel.getResource().save(null);

		System.out.println(virtualModel.getVirtualModelFactory().stringRepresentation(virtualModel));

		assertEquals(11, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				dropScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getFlexoConceptInstanceType(virtualModel),
				dropScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("shape"));
		assertEquals(DiagramShape.class, dropScheme.getBindingModel().bindingVariableNamed("shape").getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_DEFINITION_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(dropScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertTrue(((DiagramType) dropScheme.getBindingModel().bindingVariableNamed("diagram").getType()).getDiagramSpecification() == diagramSpecificationResource
				.getDiagramSpecification());

		assertTrue(dropScheme.isTopTarget());
		dropScheme.setTopTarget(true);
		assertTrue(dropScheme.isTopTarget());

		dropScheme.setTopTarget(false);
		assertFalse(dropScheme.isTopTarget());

		dropScheme.setTargetFlexoConcept(flexoConcept);
		assertFalse(dropScheme.isTopTarget());
		assertEquals(12, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET).getType());

		flexoConcept2 = virtualModel.getVirtualModelFactory().newInstance(FlexoConcept.class);
		flexoConcept2.setName("Concept2");
		virtualModel.addToFlexoConcepts(flexoConcept2);
		dropScheme.setTargetFlexoConcept(flexoConcept2);
		assertFalse(dropScheme.isTopTarget());
		assertEquals(12, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept2), dropScheme.getBindingModel()
				.bindingVariableNamed(DropSchemeBindingModel.TARGET).getType());

		dropScheme.setTopTarget(true);
		assertEquals(11, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));

		dropScheme.setTargetFlexoConcept(flexoConcept);
		assertFalse(dropScheme.isTopTarget());
		assertEquals(12, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET).getType());

	}

	/**
	 * Test the DropScheme creation, check BindingModel management
	 */
	@Test
	@TestOrder(7)
	public void testCreateLinkScheme() throws SaveResourceException {

		log("testCreateLinkScheme()");

		CreateFlexoBehaviour createLinkScheme = CreateFlexoBehaviour.actionType.makeNewAction(flexoConcept, null, editor);
		createLinkScheme.setFlexoBehaviourName("link");
		createLinkScheme.setFlexoBehaviourClass(LinkScheme.class);
		createLinkScheme.doAction();
		assertTrue(createLinkScheme.hasActionExecutionSucceeded());
		linkScheme = (LinkScheme) createLinkScheme.getNewFlexoBehaviour();

		CreateEditionAction createAddConnector = CreateEditionAction.actionType.makeNewAction(linkScheme.getControlGraph(), null, editor);
		createAddConnector.actionChoice = CreateEditionActionChoice.ModelSlotSpecificAction;
		createAddConnector.setModelSlot(typedDiagramModelSlot);
		createAddConnector.setModelSlotSpecificActionClass(AddConnector.class);
		createAddConnector.doAction();
		assertTrue(createAddConnector.hasActionExecutionSucceeded());

		virtualModel.getResource().save(null);

		assertEquals(13, linkScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				linkScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getFlexoConceptInstanceType(virtualModel),
				linkScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed("shape"));
		assertEquals(DiagramShape.class, linkScheme.getBindingModel().bindingVariableNamed("shape").getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_DEFINITION_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, linkScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(linkScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertTrue(((DiagramType) linkScheme.getBindingModel().bindingVariableNamed("diagram").getType()).getDiagramSpecification() == diagramSpecificationResource
				.getDiagramSpecification());

		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.FROM_TARGET));
		assertEquals(DiagramShape.class, linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.FROM_TARGET).getType());

		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.TO_TARGET));
		assertEquals(DiagramShape.class, linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.TO_TARGET).getType());

		linkScheme.setFromTargetFlexoConcept(flexoConcept);
		assertEquals(13, linkScheme.getBindingModel().getBindingVariablesCount());
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.FROM_TARGET).getType());

		linkScheme.setToTargetFlexoConcept(flexoConcept2);
		assertEquals(13, linkScheme.getBindingModel().getBindingVariablesCount());
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept2), linkScheme.getBindingModel()
				.bindingVariableNamed(LinkSchemeBindingModel.TO_TARGET).getType());

		virtualModel.getResource().save(null);

	}

	/**
	 * Reload the DiagramSpecification and VirtualModel
	 */
	@Test
	@TestOrder(8)
	public void testReloadDiagramSpecificationAndVirtualModel() {

		log("testReloadDiagramSpecification()");

		applicationContext = instanciateTestServiceManager();

		technologicalAdapter = applicationContext.getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
		resourceCenter = applicationContext.getResourceCenterService().getResourceCenters().get(0);
		repository = resourceCenter.getRepository(DiagramSpecificationRepository.class, technologicalAdapter);

		File newDirectory = new File(((FileSystemBasedResourceCenter) resourceCenter).getDirectory(), "CopyFromPreviousRC");
		newDirectory.mkdirs();

		try {
			File dsDir = new File(newDirectory, ResourceLocator.retrieveResourceAsFile(diagramSpecificationResource.getDirectory())
					.getName());
			dsDir.mkdirs();
			FileUtils.copyContentDirToDir(ResourceLocator.retrieveResourceAsFile(diagramSpecificationResource.getDirectory()), dsDir);
			File vpDir = new File(newDirectory, ResourceLocator.retrieveResourceAsFile(viewPointResource.getDirectory()).getName());
			vpDir.mkdirs();
			FileUtils.copyContentDirToDir(ResourceLocator.retrieveResourceAsFile(viewPointResource.getDirectory()), vpDir);
			// We wait here for the thread monitoring ResourceCenters to detect new files
			Thread.sleep(3000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DiagramSpecificationResource retrievedDSResource = repository.getResource(DIAGRAM_SPECIFICATION_URI);
		assertNotNull(retrievedDSResource);
		assertEquals(1, retrievedDSResource.getDiagramSpecification().getPalettes().size());

		ViewPointResource retrievedVPResource = serviceManager.getViewPointLibrary().getViewPointResource(VIEWPOINT_URI);
		assertNotNull(retrievedVPResource);

		assertEquals(1, retrievedVPResource.getVirtualModelResources().size());
		VirtualModelResource retrievedVMResource = retrievedVPResource.getVirtualModelResources().get(0);

		assertTrue(FMLControlledDiagramVirtualModelNature.INSTANCE.hasNature(retrievedVMResource.getVirtualModel()));

		TypedDiagramModelSlot retrievedDiagramMS = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(retrievedVMResource
				.getVirtualModel());
		assertNotNull(retrievedDiagramMS);
		assertEquals(1, retrievedDiagramMS.getPaletteElementBindings().size());

		FMLDiagramPaletteElementBinding retrievedBinding = retrievedDiagramMS.getPaletteElementBindings().get(0);
		assertNotNull(retrievedBinding.getPaletteElement());
		assertNotNull(retrievedBinding.getDropScheme());

		viewPoint = retrievedVPResource.getViewPoint();
		assertNotNull(viewPoint);

		virtualModel = viewPoint.getVirtualModelNamed("TestVirtualModel");

		assertNotNull(virtualModel.getBindingModel());
		assertEquals(5, virtualModel.getBindingModel().getBindingVariablesCount());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				virtualModel.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getVirtualModelInstanceType(virtualModel), virtualModel.getBindingModel()
				.bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(virtualModel.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertTrue(((DiagramType) virtualModel.getBindingModel().bindingVariableNamed("diagram").getType()).getDiagramSpecification() == retrievedDSResource
				.getDiagramSpecification());

		flexoConcept = virtualModel.getFlexoConcept("Concept");

		dropScheme = (DropScheme) flexoConcept.getFlexoBehaviour("drop");

		assertEquals(13, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				dropScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getFlexoConceptInstanceType(virtualModel),
				dropScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("shape"));
		assertEquals(DiagramShape.class, dropScheme.getBindingModel().bindingVariableNamed("shape").getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_DEFINITION_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(dropScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertTrue(((DiagramType) dropScheme.getBindingModel().bindingVariableNamed("diagram").getType()).getDiagramSpecification() == retrievedDSResource
				.getDiagramSpecification());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET).getType());

	}

	/**
	 * Retrieve the ViewPoint
	 */
	@Test
	@TestOrder(9)
	public void testLoadViewPointAndVirtualModel() {
		instanciateTestServiceManager();
		ViewPointLibrary vpLib = serviceManager.getViewPointLibrary();
		assertNotNull(vpLib);
		viewPoint = vpLib.getViewPoint("http://openflexo.org/test/TestControlledDiagramViewPoint");
		assertNotNull(viewPoint);

		virtualModel = viewPoint.getVirtualModelNamed("TestVirtualModel");
		assertNotNull(virtualModel);
		assertTrue(virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE));

		assertNotNull(virtualModel.getBindingModel());
		assertEquals(5, virtualModel.getBindingModel().getBindingVariablesCount());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				virtualModel.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getVirtualModelInstanceType(virtualModel), virtualModel.getBindingModel()
				.bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(virtualModel.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);

		flexoConcept = virtualModel.getFlexoConcepts().get(0);
		assertNotNull(flexoConcept);

		dropScheme = (DropScheme) flexoConcept.getFlexoBehaviours().get(0);
		assertNotNull(dropScheme);

	}

	/**
	 * Retrieve the ViewPoint
	 */
	@Test
	@TestOrder(10)
	public void testLoadViewPointAndVirtualModelFromJar() {
		instanciateTestServiceManager();
		JarResourceCenter
				.addNamedJarFromClassPathResourceCenters(getFlexoServiceManager().getResourceCenterService(), "testdiagram_vp-1.1");
		ViewPointLibrary vpLib = serviceManager.getViewPointLibrary();
		assertNotNull(vpLib);
		viewPoint = vpLib.getViewPoint("http://openflexo.org/test/TestControlledDiagramViewPoint2");
		assertNotNull(viewPoint);

		virtualModel = viewPoint.getVirtualModelNamed("TestVirtualModel");
		assertNotNull(virtualModel);
		assertTrue(virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE));

		assertNotNull(virtualModel.getBindingModel());
		assertEquals(5, virtualModel.getBindingModel().getBindingVariablesCount());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				virtualModel.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getVirtualModelInstanceType(virtualModel), virtualModel.getBindingModel()
				.bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(virtualModel.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);

		flexoConcept = virtualModel.getFlexoConcepts().get(0);
		assertNotNull(flexoConcept);

		dropScheme = (DropScheme) flexoConcept.getFlexoBehaviours().get(0);
		assertNotNull(dropScheme);

	}

	/**
	 * Reload the DiagramSpecification and VirtualModel
	 */
	@Test
	@TestOrder(11)
	public void testSchemeFromExistingVirtualModel() {

		log("testLoadExistingVirtualModel()");

		flexoConcept = virtualModel.getFlexoConcept("TestConcept");

		dropScheme = (DropScheme) flexoConcept.getFlexoBehaviour("drop");

		assertEquals(11, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				dropScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getFlexoConceptInstanceType(virtualModel),
				dropScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("shape"));
		assertEquals(DiagramShape.class, dropScheme.getBindingModel().bindingVariableNamed("shape").getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_DEFINITION_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(dropScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));

		FlexoConcept FCLink = virtualModel.getFlexoConcept("TestLink");
		assertNotNull(FCLink);
		linkScheme = (LinkScheme) FCLink.getFlexoBehaviour("link");
		assertNotNull(linkScheme);

		assertEquals(13, linkScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				linkScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getFlexoConceptInstanceType(virtualModel),
				linkScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(FCLink),
				linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY).getType());

		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_DEFINITION_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, linkScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(linkScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);

		System.out.println("BM=" + linkScheme.getBindingModel());

		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.FROM_TARGET));
		/*		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
						linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.FROM_TARGET).getType());

				assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.TO_TARGET));
				assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept2), linkScheme.getBindingModel()
						.bindingVariableNamed(LinkSchemeBindingModel.TO_TARGET).getType());
		*/
	}

	/**
	 * Reload the DiagramSpecification and VirtualModel
	 */
	@Test
	@TestOrder(12)
	public void testSchemeFromExistingVirtualModelFromJar() {

		log("testLoadExistingVirtualModel()");

		flexoConcept = virtualModel.getFlexoConcept("TestConcept");

		dropScheme = (DropScheme) flexoConcept.getFlexoBehaviour("drop");

		assertEquals(11, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				dropScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getFlexoConceptInstanceType(virtualModel),
				dropScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("shape"));
		assertEquals(DiagramShape.class, dropScheme.getBindingModel().bindingVariableNamed("shape").getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_DEFINITION_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(dropScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));

		FlexoConcept FCLink = virtualModel.getFlexoConcept("TestLink");
		assertNotNull(FCLink);
		linkScheme = (LinkScheme) FCLink.getFlexoBehaviour("link");
		assertNotNull(linkScheme);

		assertEquals(13, linkScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY));
		assertEquals(ViewType.getViewType(viewPoint),
				linkScheme.getBindingModel().bindingVariableNamed(ViewPointBindingModel.VIEW_PROPERTY).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.REFLEXIVE_ACCESS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY));
		assertEquals(VirtualModelInstanceType.getFlexoConceptInstanceType(virtualModel),
				linkScheme.getBindingModel().bindingVariableNamed(VirtualModelBindingModel.VIRTUAL_MODEL_INSTANCE_PROPERTY).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(FCLink),
				linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.FLEXO_CONCEPT_INSTANCE_PROPERTY).getType());

		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_DEFINITION_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, linkScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(linkScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);

		System.out.println("BM=" + linkScheme.getBindingModel());

		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.FROM_TARGET));
		/*		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
						linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.FROM_TARGET).getType());

				assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.TO_TARGET));
				assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept2), linkScheme.getBindingModel()
						.bindingVariableNamed(LinkSchemeBindingModel.TO_TARGET).getType());
		*/
	}
}
