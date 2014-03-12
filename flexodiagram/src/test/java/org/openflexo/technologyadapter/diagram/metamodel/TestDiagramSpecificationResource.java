package org.openflexo.technologyadapter.diagram.metamodel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.foundation.TestFlexoServiceManager;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramPaletteResourceImpl;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResourceImpl;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.toolbox.FileResource;

/**
 * Test DiagramSpecification resource 
 * @author vincent
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDiagramSpecificationResource extends OpenflexoTestCase{

	private final String diagramFileName = "myDiagramSpecification";
	private final String resourcesFolder = "src/test/resources";
	private final String diagramSpecificationURI = "http://myDiagramSpecification";
	private final String paletteName = "myDiagramSpecificationPalette";
	
	public static DiagramSpecificationResource diagramSpecificationResource;
	public static DiagramPaletteResource paletteResource;
	public static FlexoResourceCenter<?> resourceCenter;
	public static DiagramTechnologyAdapter technologicalAdapter;
	public static TestFlexoServiceManager applicationContext;
	public static DiagramSpecificationRepository repository;
	
	/**
	 * Initialize
	 */
	@Test
	@TestOrder(1)
	public void testInitialize() {

		log("testInitialize()");

		applicationContext = new TestFlexoServiceManager(new FileResource(new File(resourcesFolder).getAbsolutePath()));
		technologicalAdapter = applicationContext.getTechnologyAdapterService().getTechnologyAdapter(
				DiagramTechnologyAdapter.class);
		resourceCenter = applicationContext.getResourceCenterService().getResourceCenters().get(0);
		repository = resourceCenter.getRepository(DiagramSpecificationRepository.class, technologicalAdapter);
		
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
	public void testCreateDiagramSpecificationResource() {
		
		log("testCreateDiagramSpecificationResource()");

		try {
			File diagramRep = new File(repository.getDirectory() + "/"+diagramFileName);
			File diagramFile = new File(diagramRep+ "/"+diagramFileName+DiagramSpecificationResource.DIAGRAM_SPECIFICATION_SUFFIX);
			diagramSpecificationResource = DiagramSpecificationResourceImpl.makeDiagramSpecificationResource(diagramSpecificationURI, diagramRep, 
					diagramFile, applicationContext);
			
			diagramSpecificationResource.save(null);
			assertTrue(diagramSpecificationResource.getFile().exists());
			
		} catch (SaveResourceException e) {
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Test Load diagram specification resource
	 */
	@Test
	@TestOrder(3)
	public void testLoadDiagramSpecificationResource() {
		
		log("testLoadDiagramSpecificationResource()");
		
		DiagramSpecificationResource resource = DiagramSpecificationResourceImpl.retrieveDiagramSpecificationResource(new File(resourcesFolder+"/"+diagramFileName), applicationContext);
		assertNotNull(resource);
	}
	
	/**
	 * Test Create Palette resource
	 */
	@Test
	@TestOrder(4)
	public void testUpdateDiagramSpecificationResourceWithPaletteResource() {
		
		log("testUpdateDiagramSpecificationResourceWithPaletteResource()");
		
		try {
			paletteResource = DiagramPaletteResourceImpl.makeDiagramPaletteResource(diagramSpecificationResource, paletteName, applicationContext);
			paletteResource.save(null);
			assertTrue(paletteResource.getFile().exists());
			diagramSpecificationResource.save(null);
			assertTrue(diagramSpecificationResource.getDiagramPaletteResources().contains(paletteResource));
		} catch (SaveResourceException e) {
			fail(e.getMessage());
		}
		
	}
	
	/**
	 * Test update diagram specification resource with diagram resource
	 */
	@Test
	@TestOrder(5)
	public void testUpdateDiagramSpecificationResourceWithDiagramResource() {
		try {
			// Create an example diagram
			DiagramFactory factory = new DiagramFactory();
			Diagram diagram = factory.newInstance(Diagram.class);
			DiagramShape shape1 = factory.makeNewShape("Shape1", ShapeType.RECTANGLE, new FGEPoint(100, 100), diagram);
			shape1.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.RED));
			shape1.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.BLUE));
			DiagramShape shape2 = factory.makeNewShape("Shape2", ShapeType.RECTANGLE, new FGEPoint(200, 100), diagram);
			shape2.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.BLUE));
			shape2.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.WHITE));
			DiagramConnector connector1 = factory.makeNewConnector("Connector", shape1, shape2, diagram);
			
			// Add the palette and the example diagram
			diagramSpecificationResource.getDiagramSpecification().addToExampleDiagrams(diagram);
			diagramSpecificationResource.getDiagramSpecification().addToPalettes(paletteResource.getDiagramPalette());
			diagramSpecificationResource.save(null);
			
		} catch (SaveResourceException e) {
			fail(e.getMessage());
		} catch (ModelDefinitionException e) {
			fail(e.getMessage());
		}
		
	}
	
	
	
}
