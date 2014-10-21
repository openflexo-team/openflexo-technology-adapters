package org.openflexo.technologyadapter.diagram.metamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramSpecification;
import org.openflexo.technologyadapter.diagram.fml.action.CreateExampleDiagram;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.toolbox.FileUtils;

/**
 * Test DiagramSpecification features using {@link FlexoAction} primitives
 * 
 * @author vincent,sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDiagramSpecification extends OpenflexoTestCase {

	private final String diagramSpecificationName = "myDiagramSpecification";
	private final String diagramSpecificationURI = "http://myDiagramSpecification";
	private final String paletteName = "myDiagramSpecificationPalette";

	public static DiagramTechnologyAdapter technologicalAdapter;
	public static FlexoServiceManager applicationContext;
	public static FlexoResourceCenter<?> resourceCenter;
	public static DiagramSpecificationRepository repository;
	public static FlexoEditor editor;

	public static DiagramSpecificationResource diagramSpecificationResource;
	public static DiagramPaletteResource paletteResource;
	public static DiagramResource exampleDiagramResource;
	public static TypedDiagramModelSlot typedDiagramModelSlot;
	public static VirtualModel newVirtualModel;

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
		action.setNewDiagramSpecificationName(diagramSpecificationName);
		action.setNewDiagramSpecificationURI(diagramSpecificationURI);

		action.doAction();

		assertTrue(action.hasActionExecutionSucceeded());

		diagramSpecificationResource = action.getNewDiagramSpecification().getResource();

		assertNotNull(diagramSpecificationResource);
		assertTrue(diagramSpecificationResource.getFlexoIODelegate().exists());

	}

	/**
	 * Test palettes
	 */
	@Test
	@TestOrder(3)
	public void testPalette() {

		log("testPalette()");

		CreateDiagramPalette action = CreateDiagramPalette.actionType.makeNewAction(diagramSpecificationResource.getDiagramSpecification(),
				null, editor);
		action.setNewPaletteName(paletteName);

		action.doAction();

		assertTrue(action.hasActionExecutionSucceeded());

		paletteResource = action.getNewPalette().getResource();

		assertNotNull(paletteResource);
		assertTrue(paletteResource.getFlexoIODelegate().exists());
		assertTrue(diagramSpecificationResource.getDiagramPaletteResources().contains(paletteResource));

		assertEquals(1, diagramSpecificationResource.getDiagramSpecification().getPalettes().size());

	}

	/**
	 * Test example diagrams
	 * 
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(5)
	public void testExampleDiagrams() throws SaveResourceException {

		log("testExampleDiagrams()");

		CreateExampleDiagram action = CreateExampleDiagram.actionType.makeNewAction(diagramSpecificationResource.getDiagramSpecification(),
				null, editor);
		action.setNewDiagramName("exampleDiagram1");
		action.setNewDiagramTitle("This is an example diagram");

		action.doAction();

		assertTrue(action.hasActionExecutionSucceeded());

		exampleDiagramResource = (DiagramResource) action.getNewDiagram().getResource();

		assertNotNull(exampleDiagramResource);
		assertTrue(exampleDiagramResource.getFlexoIODelegate().exists());
		assertTrue(diagramSpecificationResource.getExampleDiagramResources().contains(exampleDiagramResource));

		assertEquals(1, diagramSpecificationResource.getDiagramSpecification().getExampleDiagrams().size());

		// Edit example diagram
		DiagramFactory factory = exampleDiagramResource.getFactory();
		Diagram diagram = exampleDiagramResource.getDiagram();

		DiagramShape shape1 = factory.makeNewShape("Shape1a", ShapeType.RECTANGLE, new FGEPoint(100, 100), diagram);
		shape1.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.RED));
		shape1.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.BLUE));
		DiagramShape shape2 = factory.makeNewShape("Shape2a", ShapeType.RECTANGLE, new FGEPoint(200, 100), diagram);
		shape2.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.BLUE));
		shape2.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.WHITE));
		DiagramConnector connector1 = factory.makeNewConnector("Connector", shape1, shape2, diagram);
		diagram.addToShapes(shape1);
		diagram.addToShapes(shape2);
		diagram.addToConnectors(connector1);

		exampleDiagramResource.save(null);

	}

	/**
	 * Reload the DiagramSpecification, tests that uri and name are persistent
	 */
	@Test
	@TestOrder(6)
	public void testReloadDiagramSpecification() {

		log("testReloadDiagramSpecification()");

		applicationContext = instanciateTestServiceManager();

		technologicalAdapter = applicationContext.getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
		resourceCenter = applicationContext.getResourceCenterService().getResourceCenters().get(0);
		repository = resourceCenter.getRepository(DiagramSpecificationRepository.class, technologicalAdapter);

		File newDirectory = new File(((FileSystemBasedResourceCenter) resourceCenter).getDirectory(), ResourceLocator.retrieveResourceAsFile(diagramSpecificationResource
				.getDirectory()).getName());
		newDirectory.mkdirs();

		try {
			FileUtils.copyContentDirToDir(ResourceLocator.retrieveResourceAsFile(diagramSpecificationResource.getDirectory()), newDirectory);
			// We wait here for the thread monitoring ResourceCenters to detect new files
			Thread.sleep(3000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DiagramSpecificationResource retrievedResource = repository.getResource(diagramSpecificationURI);
		assertNotNull(retrievedResource);
		assertEquals(diagramSpecificationURI, retrievedResource.getURI());

		assertEquals(2, retrievedResource.getContents().size());
		assertEquals(1, retrievedResource.getDiagramSpecification().getPalettes().size());
		assertEquals(1, retrievedResource.getDiagramSpecification().getExampleDiagrams().size());

		System.out.println("Palettes= " + retrievedResource.getDiagramSpecification().getPalettes());
		System.out.println("Example diagrams= " + retrievedResource.getDiagramSpecification().getExampleDiagrams());

	}

}
