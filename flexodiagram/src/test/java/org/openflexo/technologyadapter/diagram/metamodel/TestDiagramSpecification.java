package org.openflexo.technologyadapter.diagram.metamodel;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.model.ModelEntity;
import org.openflexo.model.exceptions.ModelDefinitionException;
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

	/**
	 * Test the diagram specification factory
	 */
	@Test
	@TestOrder(1)
	public void testDiagramSpecificationFactory() {

		try {
			DiagramSpecificationFactory factory = new DiagramSpecificationFactory();

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

		DiagramSpecificationFactory factory = new DiagramSpecificationFactory();

		DiagramSpecification diagramSpecification = factory.makeNewDiagramSpecification();
		assertTrue(diagramSpecification instanceof DiagramSpecification);

	}
	
	/**
	 * Test the palette factory
	 */
	@Test
	@TestOrder(3)
	public void testInstanciatePalette() throws Exception {

		DiagramPaletteFactory factory = new DiagramPaletteFactory();

		DiagramPalette diagramPalette = factory.makeNewDiagramPalette();
		assertNotNull(diagramPalette);
		assertTrue(diagramPalette instanceof DiagramPalette);
	
	}

}
