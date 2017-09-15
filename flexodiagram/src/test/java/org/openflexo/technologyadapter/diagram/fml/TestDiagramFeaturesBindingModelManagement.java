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

package org.openflexo.technologyadapter.diagram.fml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModelLibrary;
import org.openflexo.foundation.fml.action.AddUseDeclaration;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateTechnologyRole;
import org.openflexo.foundation.fml.binding.FlexoBehaviourBindingModel;
import org.openflexo.foundation.fml.binding.FlexoConceptBindingModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rm.VirtualModelResourceFactory;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.JarResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.model.exceptions.ModelDefinitionException;
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

/**
 * Test the BindingModel management of some diagram-specific features
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDiagramFeaturesBindingModelManagement extends DiagramTestCase {

	private final String DIAGRAM_SPECIFICATION_NAME = "myDiagramSpecification";
	private final String DIAGRAM_SPECIFICATION_URI = "http://myDiagramSpecification";
	private final String PALETTE_NAME = "myDiagramSpecificationPalette";
	private final String PALETTE_ELEMENT_NAME = "myPaletteElement";

	private final String VIEWPOINT_NAME = "TestViewPoint";
	private final String VIEWPOINT_URI = "http://openflexo.org/test/TestResourceCenter/TestViewPoint.fml";
	public static final String VIRTUAL_MODEL_NAME = "TestVirtualModel";

	public static DiagramSpecificationResource diagramSpecificationResource;
	public static DiagramPaletteResource paletteResource;

	private static DiagramSpecification diagramSpecification;

	public static DiagramPalette palette;
	public static DiagramPaletteElement paletteElement;

	public static VirtualModel viewPoint;
	public static VirtualModelResource viewPointResource;
	public static TypedDiagramModelSlot typedDiagramModelSlot;
	public static VirtualModel virtualModel;
	public static FlexoConcept flexoConcept;
	public static FlexoConcept flexoConcept2;
	public static DropScheme dropScheme;
	public static LinkScheme linkScheme;

	private static Diagram exampleDiagram;

	/**
	 * Test Create diagram specification resource
	 */
	@Test
	@TestOrder(2)
	public void testCreateDiagramSpecification() {

		log("testCreateDiagramSpecification()");

		DiagramSpecificationRepository<?> repository = technologicalAdapter.getDiagramSpecificationRepository(newResourceCenter);
		assertNotNull(repository);

		CreateDiagramSpecification action = CreateDiagramSpecification.actionType.makeNewAction(repository.getRootFolder(), null, editor);
		action.setNewDiagramSpecificationName(DIAGRAM_SPECIFICATION_NAME);
		action.setNewDiagramSpecificationURI(DIAGRAM_SPECIFICATION_URI);
		action.setMakeDefaultExampleDiagram(true);

		action.doAction();

		assertTrue(action.hasActionExecutionSucceeded());

		diagramSpecificationResource = action.getNewDiagramSpecification().getResource();

		assertNotNull(diagramSpecificationResource);
		assertTrue(diagramSpecificationResource.getIODelegate().exists());

		assertNotNull(exampleDiagram = action.getNewDiagramSpecification().getExampleDiagrams().get(0));

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
		assertTrue(paletteResource.getIODelegate().exists());
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
	 * 
	 * @throws ModelDefinitionException
	 * @throws SaveResourceException
	 * @throws IOException
	 */
	@Test
	@TestOrder(4)
	public void testCreateViewPoint() throws SaveResourceException, ModelDefinitionException, IOException {

		log("testCreateViewPoint()");

		FMLTechnologyAdapter fmlTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(FMLTechnologyAdapter.class);
		VirtualModelResourceFactory factory = fmlTechnologyAdapter.getVirtualModelResourceFactory();

		viewPointResource = factory.makeTopLevelVirtualModelResource(VIEWPOINT_NAME, VIEWPOINT_URI,
				fmlTechnologyAdapter.getGlobalRepository(newResourceCenter).getRootFolder(),
				fmlTechnologyAdapter.getTechnologyContextManager(), true);
		viewPoint = viewPointResource.getLoadedResourceData();
		// viewPoint = ViewPointImpl.newViewPoint(VIEWPOINT_NAME, VIEWPOINT_URI,
		// ((FileSystemBasedResourceCenter) resourceCenter).getDirectory(),
		// serviceManager.getViewPointLibrary(), resourceCenter);
		// viewPointResource = (ViewPointResource) viewPoint.getResource();
		assertTrue(ResourceLocator.retrieveResourceAsFile(viewPointResource.getDirectory()).exists());
		assertTrue(viewPointResource.getIODelegate().exists());
	}

	/**
	 * Test the VirtualModel creation, check BindingModel management
	 * 
	 * @throws ModelDefinitionException
	 */
	@Test
	@TestOrder(5)
	public void testCreateVirtualModel() throws SaveResourceException, ModelDefinitionException {

		log("testCreateVirtualModel()");

		FMLTechnologyAdapter fmlTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(FMLTechnologyAdapter.class);
		VirtualModelResourceFactory vmFactory = fmlTechnologyAdapter.getVirtualModelResourceFactory();
		VirtualModelResource newVMResource = vmFactory.makeContainedVirtualModelResource(VIRTUAL_MODEL_NAME,
				viewPoint.getVirtualModelResource(), fmlTechnologyAdapter.getTechnologyContextManager(), true);
		virtualModel = newVMResource.getLoadedResourceData();
		// virtualModel = VirtualModelImpl.newVirtualModel("TestVirtualModel",
		// viewPoint);
		assertTrue(ResourceLocator.retrieveResourceAsFile(((VirtualModelResource) virtualModel.getResource()).getDirectory()).exists());
		assertTrue(((VirtualModelResource) virtualModel.getResource()).getIODelegate().exists());

		AddUseDeclaration useDeclarationAction = AddUseDeclaration.actionType.makeNewAction(virtualModel, null, editor);
		useDeclarationAction.setModelSlotClass(TypedDiagramModelSlot.class);
		useDeclarationAction.doAction();

		typedDiagramModelSlot = technologicalAdapter.makeModelSlot(TypedDiagramModelSlot.class, virtualModel);
		typedDiagramModelSlot.setMetaModelResource(diagramSpecificationResource);
		typedDiagramModelSlot.setName("diagram");
		assertNotNull(typedDiagramModelSlot);

		virtualModel.addToModelSlots(typedDiagramModelSlot);
		assertTrue(virtualModel.getModelSlots(TypedDiagramModelSlot.class).size() == 1);

		flexoConcept = virtualModel.getFMLModelFactory().newInstance(FlexoConcept.class);
		flexoConcept.setName("Concept");
		virtualModel.addToFlexoConcepts(flexoConcept);

		CreateTechnologyRole createShapeRole = CreateTechnologyRole.actionType.makeNewAction(flexoConcept, null, editor);
		createShapeRole.setRoleName("shape");
		createShapeRole.setFlexoRoleClass(ShapeRole.class);
		createShapeRole.doAction();
		assertTrue(createShapeRole.hasActionExecutionSucceeded());

		ShapeRole role = (ShapeRole) createShapeRole.getNewFlexoRole();

		DiagramShape newShape = createShapeInDiagram(exampleDiagram, "TestShape");
		role.bindTo(newShape);

		virtualModel.getResource().save(null);

		System.out.println(virtualModel.getFMLModelFactory().stringRepresentation(virtualModel));

		assertTrue(virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE));
		assertEquals(typedDiagramModelSlot, FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(virtualModel));

		assertNotNull(virtualModel.getBindingModel());

		assertEquals(3, virtualModel.getBindingModel().getBindingVariablesCount());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(virtualModel.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertTrue(((DiagramType) virtualModel.getBindingModel().bindingVariableNamed("diagram").getType())
				.getDiagramSpecification() == diagramSpecificationResource.getDiagramSpecification());

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
		// createAddShape.actionChoice =
		// CreateEditionActionChoice.ModelSlotSpecificAction;
		createAddShape.setModelSlot(typedDiagramModelSlot);
		createAddShape.setEditionActionClass(AddShape.class);
		createAddShape.doAction();
		assertTrue(createAddShape.hasActionExecutionSucceeded());

		FMLDiagramPaletteElementBinding newBinding = virtualModel.getFMLModelFactory().newInstance(FMLDiagramPaletteElementBinding.class);
		newBinding.setPaletteElement(paletteElement);
		newBinding.setBoundFlexoConcept(flexoConcept);
		newBinding.setDropScheme(dropScheme);

		typedDiagramModelSlot.addToPaletteElementBindings(newBinding);

		virtualModel.getResource().save(null);

		System.out.println(virtualModel.getFMLModelFactory().stringRepresentation(virtualModel));

		assertEquals(6, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("shape"));
		assertEquals(DiagramShape.class, dropScheme.getBindingModel().bindingVariableNamed("shape").getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(dropScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertTrue(((DiagramType) dropScheme.getBindingModel().bindingVariableNamed("diagram").getType())
				.getDiagramSpecification() == diagramSpecificationResource.getDiagramSpecification());

		assertTrue(dropScheme.isTopTarget());
		dropScheme.setTopTarget(true);
		assertTrue(dropScheme.isTopTarget());

		dropScheme.setTopTarget(false);
		assertFalse(dropScheme.isTopTarget());

		dropScheme.setTargetFlexoConcept(flexoConcept);
		assertFalse(dropScheme.isTopTarget());
		assertEquals(7, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET).getType());

		flexoConcept2 = virtualModel.getFMLModelFactory().newInstance(FlexoConcept.class);
		flexoConcept2.setName("Concept2");
		virtualModel.addToFlexoConcepts(flexoConcept2);
		dropScheme.setTargetFlexoConcept(flexoConcept2);
		assertFalse(dropScheme.isTopTarget());
		assertEquals(7, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept2),
				dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET).getType());

		dropScheme.setTopTarget(true);
		assertEquals(6, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));

		dropScheme.setTargetFlexoConcept(flexoConcept);
		assertFalse(dropScheme.isTopTarget());
		assertEquals(7, dropScheme.getBindingModel().getBindingVariablesCount());
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
		// createAddConnector.actionChoice =
		// CreateEditionActionChoice.ModelSlotSpecificAction;
		createAddConnector.setModelSlot(typedDiagramModelSlot);
		createAddConnector.setEditionActionClass(AddConnector.class);
		createAddConnector.doAction();
		assertTrue(createAddConnector.hasActionExecutionSucceeded());

		virtualModel.getResource().save(null);

		assertEquals(8, linkScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed("shape"));
		assertEquals(DiagramShape.class, linkScheme.getBindingModel().bindingVariableNamed("shape").getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, linkScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(linkScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertTrue(((DiagramType) linkScheme.getBindingModel().bindingVariableNamed("diagram").getType())
				.getDiagramSpecification() == diagramSpecificationResource.getDiagramSpecification());

		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.FROM_TARGET));
		assertEquals(DiagramShape.class, linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.FROM_TARGET).getType());

		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.TO_TARGET));
		assertEquals(DiagramShape.class, linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.TO_TARGET).getType());

		linkScheme.setFromTargetFlexoConcept(flexoConcept);
		assertEquals(8, linkScheme.getBindingModel().getBindingVariablesCount());
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.FROM_TARGET).getType());

		linkScheme.setToTargetFlexoConcept(flexoConcept2);
		assertEquals(8, linkScheme.getBindingModel().getBindingVariablesCount());
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept2),
				linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.TO_TARGET).getType());

		virtualModel.getResource().save(null);

	}

	/**
	 * Reload the DiagramSpecification and VirtualModel
	 */
	@Test
	@TestOrder(8)
	public void testReloadDiagramSpecificationAndVirtualModel() {

		log("testReloadDiagramSpecification()");

		serviceManager = instanciateTestServiceManager(DiagramTechnologyAdapter.class);

		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);

		serviceManager.getResourceCenterService()
				.addToResourceCenters(newResourceCenter = new DirectoryResourceCenter(newResourceCenter.getDirectory(),
						serviceManager.getResourceCenterService()));
		newResourceCenter.performDirectoryWatchingNow();

		diagramTestResourceCenter = serviceManager.getResourceCenterService().getFlexoResourceCenter("http://openflexo.org/diagram-test");

		assertNotNull(diagramTestResourceCenter);

		DiagramSpecificationRepository<?> repository = technologicalAdapter.getDiagramSpecificationRepository(newResourceCenter);
		assertNotNull(repository);

		DiagramSpecificationResource retrievedDSResource = repository.getResource(DIAGRAM_SPECIFICATION_URI);
		assertNotNull(retrievedDSResource);
		assertEquals(1, retrievedDSResource.getDiagramSpecification().getPalettes().size());

		VirtualModelResource retrievedVPResource = serviceManager.getVirtualModelLibrary().getVirtualModelResource(VIEWPOINT_URI);
		assertNotNull(retrievedVPResource);

		assertEquals(1, retrievedVPResource.getContainedVirtualModelResources().size());
		VirtualModelResource retrievedVMResource = retrievedVPResource.getContainedVirtualModelResources().get(0);

		assertTrue(FMLControlledDiagramVirtualModelNature.INSTANCE.hasNature(retrievedVMResource.getVirtualModel()));

		TypedDiagramModelSlot retrievedDiagramMS = FMLControlledDiagramVirtualModelNature
				.getTypedDiagramModelSlot(retrievedVMResource.getVirtualModel());
		assertNotNull(retrievedDiagramMS);
		assertEquals(1, retrievedDiagramMS.getPaletteElementBindings().size());

		FMLDiagramPaletteElementBinding retrievedBinding = retrievedDiagramMS.getPaletteElementBindings().get(0);
		assertNotNull(retrievedBinding.getPaletteElement());
		assertNotNull(retrievedBinding.getDropScheme());

		viewPoint = retrievedVPResource.getVirtualModel();
		assertNotNull(viewPoint);

		virtualModel = viewPoint.getVirtualModelNamed("TestVirtualModel");

		assertNotNull(virtualModel.getBindingModel());
		assertEquals(3, virtualModel.getBindingModel().getBindingVariablesCount());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(virtualModel.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertTrue(((DiagramType) virtualModel.getBindingModel().bindingVariableNamed("diagram").getType())
				.getDiagramSpecification() == retrievedDSResource.getDiagramSpecification());

		flexoConcept = virtualModel.getFlexoConcept("Concept");

		dropScheme = (DropScheme) flexoConcept.getFlexoBehaviour("drop");

		assertEquals(7, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("shape"));
		assertEquals(DiagramShape.class, dropScheme.getBindingModel().bindingVariableNamed("shape").getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(dropScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertTrue(((DiagramType) dropScheme.getBindingModel().bindingVariableNamed("diagram").getType())
				.getDiagramSpecification() == retrievedDSResource.getDiagramSpecification());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET).getType());

	}

	/**
	 * Retrieve the ViewPoint
	 * 
	 * @throws FlexoException
	 * @throws ResourceLoadingCancelledException
	 * @throws FileNotFoundException
	 */
	@Test
	@TestOrder(9)
	public void testLoadViewPointAndVirtualModel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		instanciateTestServiceManager(DiagramTechnologyAdapter.class);
		VirtualModelLibrary vpLib = serviceManager.getVirtualModelLibrary();
		assertNotNull(vpLib);
		viewPoint = vpLib.getVirtualModel("http://openflexo.org/test/TestResourceCenter/TestControlledDiagramViewPoint.fml");
		assertNotNull(viewPoint);

		virtualModel = viewPoint.getVirtualModelNamed("TestVirtualModel");

		assertNotNull(virtualModel);
		assertTrue(virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE));

		assertNotNull(virtualModel.getBindingModel());
		assertEquals(3, virtualModel.getBindingModel().getBindingVariablesCount());
		assertNotNull(virtualModel.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(virtualModel.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);

		flexoConcept = virtualModel.getFlexoConcepts().get(0);
		assertNotNull(flexoConcept);

		dropScheme = (DropScheme) flexoConcept.getFlexoBehaviours().get(0);
		assertNotNull(dropScheme);

	}

	/**
	 * Retrieve the ViewPoint
	 * 
	 * @throws FlexoException
	 * @throws ResourceLoadingCancelledException
	 * @throws FileNotFoundException
	 */
	@Test
	@TestOrder(10)
	public void testLoadViewPointAndVirtualModelFromJar() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		instanciateTestServiceManager(DiagramTechnologyAdapter.class);
		JarResourceCenter.addNamedJarFromClassPath(getFlexoServiceManager().getResourceCenterService(), "testdiagram_vp-1.1");
		VirtualModelLibrary vpLib = serviceManager.getVirtualModelLibrary();
		assertNotNull(vpLib);
		viewPoint = vpLib.getVirtualModel("http://openflexo.org/test/TestResourceCenter/TestControlledDiagramViewPoint2.fml");
		assertNotNull(viewPoint);

		virtualModel = viewPoint.getVirtualModelNamed("TestVirtualModel");
		assertNotNull(virtualModel);
		assertTrue(virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE));

		assertNotNull(virtualModel.getBindingModel());
		assertEquals(3, virtualModel.getBindingModel().getBindingVariablesCount());
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

		assertEquals(6, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("shape"));
		assertEquals(DiagramShape.class, dropScheme.getBindingModel().bindingVariableNamed("shape").getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(dropScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));

		FlexoConcept FCLink = virtualModel.getFlexoConcept("TestLink");
		assertNotNull(FCLink);
		linkScheme = (LinkScheme) FCLink.getFlexoBehaviour("link");
		assertNotNull(linkScheme);

		assertEquals(8, linkScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(FCLink),
				linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY).getType());

		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, linkScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(linkScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);

		System.out.println("BM=" + linkScheme.getBindingModel());

		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.FROM_TARGET));
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

		assertEquals(6, dropScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(flexoConcept),
				dropScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("shape"));
		assertEquals(DiagramShape.class, dropScheme.getBindingModel().bindingVariableNamed("shape").getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, dropScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(dropScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(dropScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);
		assertNull(dropScheme.getBindingModel().bindingVariableNamed(DropSchemeBindingModel.TARGET));

		FlexoConcept FCLink = virtualModel.getFlexoConcept("TestLink");
		assertNotNull(FCLink);
		linkScheme = (LinkScheme) FCLink.getFlexoBehaviour("link");
		assertNotNull(linkScheme);

		assertEquals(8, linkScheme.getBindingModel().getBindingVariablesCount());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY));
		assertEquals(FlexoConceptInstanceType.getFlexoConceptInstanceType(FCLink),
				linkScheme.getBindingModel().bindingVariableNamed(FlexoConceptBindingModel.THIS_PROPERTY).getType());

		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(FlexoBehaviourBindingModel.PARAMETERS_PROPERTY));
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL));
		assertEquals(Diagram.class, linkScheme.getBindingModel().bindingVariableNamed(DiagramBehaviourBindingModel.TOP_LEVEL).getType());
		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed("diagram"));
		assertTrue(linkScheme.getBindingModel().bindingVariableNamed("diagram").getType() instanceof DiagramType);

		System.out.println("BM=" + linkScheme.getBindingModel());

		assertNotNull(linkScheme.getBindingModel().bindingVariableNamed(LinkSchemeBindingModel.FROM_TARGET));
	}
}
