package org.openflexo.technologyadapter.diagram.metamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.model.ModelEntity;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.DiagramSpecificationFactory;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test Diagram Specification model
 * 
 * @author vincent
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDiagramSpecification extends OpenflexoTestCase{

	public static DiagramSpecification diagramSpecification;
	public static DiagramPalette diagramPalette;
	public static DiagramSpecificationFactory factory;
	
	/**
	 * Test the diagram specification factory
	 */
	@Test
	@TestOrder(1)
	public void testDiagramSpecificationFactory() {

		log("testDiagramSpecificationFactory()");
		
		try {
			factory = new DiagramSpecificationFactory();

			ModelEntity<DiagramSpecification> diagramSpecificationEntity = factory.getModelContext().getModelEntity(DiagramSpecification.class);
			ModelEntity<DiagramPalette> paletteEntity = factory.getModelContext().getModelEntity(DiagramPalette.class);
			ModelEntity<DiagramPaletteElement> paletteElementEntity = factory.getModelContext().getModelEntity(DiagramPaletteElement.class);

			assertNotNull(diagramSpecificationEntity);
			assertNotNull(paletteEntity);
			assertNotNull(paletteElementEntity);

		} catch (ModelDefinitionException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Test the diagram specification factory
	 */
	@Test
	@TestOrder(2)
	public void testInstanciateDiagramSpecfication() throws Exception {
		
		log("testInstanciateDiagramSpecfication()");
		
		diagramSpecification = factory.makeNewDiagramSpecification();
		assertTrue(diagramSpecification instanceof DiagramSpecification);
	}
	
	/**
	 * Test the palette factory
	 */
	@Test
	@TestOrder(3)
	public void testInstanciatePalette() throws Exception {
		
		log("testInstanciatePalette()");
		
		DiagramPaletteFactory factory = new DiagramPaletteFactory();
		DiagramPalette diagramPalette = factory.makeNewDiagramPalette();
		assertNotNull(diagramPalette);
		assertTrue(diagramPalette instanceof DiagramPalette);
	}
	
	/**
	 * Test fill the Diagram Specification
	 */
	@Test
	@TestOrder(4)
	public void testFillDiagramSpecification() throws Exception {
		
		log("testFillDiagramSpecification()");
		
		// Create an example diagram
		DiagramFactory factory = new DiagramFactory();
		Diagram diagram = factory.newInstance(Diagram.class);
		assertTrue(diagram instanceof Diagram);
		DiagramShape shape1 = factory.makeNewShape("Shape1", ShapeType.RECTANGLE, new FGEPoint(100, 100), diagram);
		shape1.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.RED));
		shape1.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.BLUE));
		assertTrue(shape1 instanceof DiagramShape);
		DiagramShape shape2 = factory.makeNewShape("Shape2", ShapeType.RECTANGLE, new FGEPoint(200, 100), diagram);
		shape2.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.BLUE));
		shape2.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.WHITE));
		assertTrue(shape2 instanceof DiagramShape);
		DiagramConnector connector1 = factory.makeNewConnector("Connector", shape1, shape2, diagram);
		assertTrue(connector1 instanceof DiagramConnector);
		
		// Add the palette and the example diagram
		diagramSpecification.addToExampleDiagrams(diagram);
		diagramSpecification.addToPalettes(diagramPalette);
		
		assertTrue(diagramSpecification.getExampleDiagrams().size()>0);
		assertTrue(diagramSpecification.getPalettes().size()>0);
		
	}

}
