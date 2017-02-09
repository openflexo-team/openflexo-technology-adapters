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

package org.openflexo.technologyadapter.diagram.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.rm.DiagramRepository;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
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
	public static DirectoryResourceCenter newResourceCenter;

	private static DiagramResource diagramResource;

	/**
	 * Initialize
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(1)
	public void testInitialize() throws IOException {

		log("testInitialize()");

		applicationContext = instanciateTestServiceManager(DiagramTechnologyAdapter.class);

		technologicalAdapter = applicationContext.getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);

		newResourceCenter = makeNewDirectoryResourceCenter(applicationContext);
		assertNotNull(newResourceCenter);

		DiagramRepository<?> repository = technologicalAdapter.getDiagramRepository(newResourceCenter);
		assertNotNull(repository);

		assertNotNull(applicationContext);
		assertNotNull(technologicalAdapter);
	}

	private static final String EXAMPLE_DIAGRAM_URI = "http://myExampleDiagram";

	/**
	 * Test example diagrams
	 * 
	 * @throws ModelDefinitionException
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(2)
	public void testCreateDiagram() throws SaveResourceException, ModelDefinitionException {

		log("testCreateDiagram()");

		DiagramTechnologyAdapter diagramTA = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);

		DiagramRepository<?> repository = technologicalAdapter.getDiagramRepository(newResourceCenter);
		assertNotNull(repository);

		diagramResource = diagramTA.getDiagramResourceFactory().makeDiagramResource("exampleDiagram1",
				EXAMPLE_DIAGRAM_URI, null, repository.getRootFolder(), diagramTA.getTechnologyContextManager(), true);

		assertNotNull(diagramResource);

		diagramResource.save(null);

		assertTrue(diagramResource.getFlexoIODelegate().exists());

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

			// Testing management of FlexoID
			assertEquals(1, diagram.getFlexoID());
			assertEquals(2, shape1.getFlexoID());
			assertEquals(3, shape2.getFlexoID());
			assertEquals(4, connector1.getFlexoID());

			assertEquals(4, diagramResource.getLastID());

			diagramResource.save(null);

		} catch (SaveResourceException e) {
			fail(e.getMessage());
		}

	}

	/**
	 * Reload the diagram
	 * 
	 * @throws ModelDefinitionException
	 * @throws IOException
	 */
	@Test
	@TestOrder(4)
	public void testReloadDiagram() throws ModelDefinitionException, IOException {

		log("testReloadDiagram()");

		applicationContext = instanciateTestServiceManager(DiagramTechnologyAdapter.class);

		technologicalAdapter = applicationContext.getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);

		applicationContext.getResourceCenterService()
				.addToResourceCenters(newResourceCenter = new DirectoryResourceCenter(testResourceCenterDirectory,
						applicationContext.getResourceCenterService()));
		newResourceCenter.performDirectoryWatchingNow();

		DiagramRepository<?> repository = technologicalAdapter.getDiagramRepository(newResourceCenter);
		assertNotNull(repository);

		DiagramResource reloadedResource = repository.getResource(EXAMPLE_DIAGRAM_URI);
		assertNotNull(reloadedResource);
		assertNotSame(diagramResource, reloadedResource);
		assertEquals(diagramResource.getURI(), reloadedResource.getURI());

		assertEquals(2, reloadedResource.getDiagram().getShapes().size());
		assertEquals(1, reloadedResource.getDiagram().getConnectors().size());

		assertEquals(4, reloadedResource.getLastID());

		DiagramShape shape1 = reloadedResource.getDiagram().getShapes().get(0);
		DiagramShape shape2 = reloadedResource.getDiagram().getShapes().get(1);
		DiagramConnector connector1 = reloadedResource.getDiagram().getConnectors().get(0);

		// Testing management of FlexoID
		assertEquals(1, reloadedResource.getDiagram().getFlexoID());
		assertEquals(2, shape1.getFlexoID());
		assertEquals(3, shape2.getFlexoID());
		assertEquals(4, connector1.getFlexoID());

		// Edit diagram
		DiagramFactory factory = reloadedResource.getFactory();
		Diagram diagram = reloadedResource.getDiagram();

		DiagramShape shape3 = factory.makeNewShape("Shape3a", ShapeType.RECTANGLE, new FGEPoint(100, 100), diagram);
		shape1.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.RED));
		shape1.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.BLUE));

		DiagramConnector connector2 = factory.makeNewConnector("Connector", shape1, shape3, diagram);
		assertEquals(5, shape3.getFlexoID());
		assertEquals(6, connector2.getFlexoID());

	}

}
