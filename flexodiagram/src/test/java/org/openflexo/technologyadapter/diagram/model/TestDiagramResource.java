package org.openflexo.technologyadapter.diagram.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.rm.DiagramRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramResourceImpl;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test basic diagram manipulations
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDiagramResource extends OpenflexoTestCase {

	public static DiagramTechnologyAdapter technologicalAdapter;
	public static FlexoServiceManager applicationContext;
	public static FlexoResourceCenter<?> resourceCenter;
	public static DiagramRepository repository;

	private static DiagramResource diagramResource;

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
		repository = resourceCenter.getRepository(DiagramRepository.class, technologicalAdapter);

		assertNotNull(applicationContext);
		assertNotNull(technologicalAdapter);
		assertNotNull(resourceCenter);
		assertNotNull(repository);
	}

	/**
	 * Test example diagrams
	 */
	@Test
	@TestOrder(2)
	public void testCreateDiagram() {

		log("testCreateDiagram()");

		try {
			File diagramFile = new File(repository.getDirectory(), "myDiagram.diagram");
			diagramResource = DiagramResourceImpl.makeDiagramResource("exampleDiagram1", "http://myExampleDiagram", diagramFile,
					applicationContext);

			assertNotNull(diagramResource);

			diagramResource.save(null);

			assertTrue(diagramResource.getFile().exists());

		} catch (SaveResourceException e) {
			fail(e.getMessage());
		}

	}

	/**
	 * Test diagram edition
	 */
	@Test
	@TestOrder(3)
	public void testEditDiagram() {

		log("testEditDiagram()");

		try {
			// Edit diagram
			DiagramFactory factory = diagramResource.getFactory();
			Diagram diagram = diagramResource.getDiagram();

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

			diagramResource.save(null);

		} catch (SaveResourceException e) {
			fail(e.getMessage());
		}

	}

	/**
	 * Reload the diagram
	 */
	@Test
	@TestOrder(4)
	public void testLoadDiagram() {

		log("testLoadDiagram()");

		DiagramResource reloadedResource = DiagramResourceImpl.retrieveDiagramResource(diagramResource.getFile(), applicationContext);
		assertNotNull(reloadedResource);
		assertEquals(diagramResource.getURI(), reloadedResource.getURI());

		assertEquals(2, reloadedResource.getDiagram().getShapes().size());
		assertEquals(1, reloadedResource.getDiagram().getConnectors().size());

	}

}
