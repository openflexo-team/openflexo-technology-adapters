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

package org.openflexo.technologyadapter.diagram.metamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
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

/**
 * Test DiagramSpecification features using low level primitives
 * 
 * @author vincent,sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDiagramSpecificationResource extends OpenflexoTestCase {

	private final String diagramSpecificationName = "myDiagramSpecification";
	private final String diagramSpecificationURI = "http://myDiagramSpecification";
	private final String paletteName = "myDiagramSpecificationPalette";

	public static DiagramTechnologyAdapter technologicalAdapter;
	public static FlexoServiceManager applicationContext;
	// public static DiagramSpecificationRepository<?> repository;

	public static DiagramSpecificationResource diagramSpecificationResource;
	public static DiagramPaletteResource paletteResource;
	public static DiagramResource exampleDiagramResource;
	public static TypedDiagramModelSlot typedDiagramModelSlot;
	public static VirtualModel newVirtualModel;

	public static FileSystemBasedResourceCenter newResourceCenter;

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

		assertNotNull(applicationContext);
		assertNotNull(technologicalAdapter);
		assertNotNull(newResourceCenter);
	}

	/**
	 * Test Create diagram specification resource
	 * 
	 * @throws ModelDefinitionException
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(2)
	public void testCreateDiagramSpecificationResource() throws ModelDefinitionException, SaveResourceException {

		log("testCreateDiagramSpecificationResource()");

		DiagramTechnologyAdapter diagramTA = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);

		DiagramSpecificationRepository<?> repository = technologicalAdapter
				.getDiagramSpecificationRepository(newResourceCenter);
		assertNotNull(repository);

		diagramSpecificationResource = diagramTA.getDiagramSpecificationResourceFactory()
				.makeDiagramSpecificationResourceResource(diagramSpecificationName, diagramSpecificationURI,
						repository.getRootFolder(), diagramTA.getTechnologyContextManager(), true);

		diagramSpecificationResource.save(null);
		assertTrue(diagramSpecificationResource.getFlexoIODelegate().exists());

		DiagramSpecificationResource retrievedResource = repository.getResource(diagramSpecificationURI);
		assertNotNull(retrievedResource);

		assertSame(retrievedResource, diagramSpecificationResource);
		assertEquals(diagramSpecificationURI, retrievedResource.getURI());

	}

	/**
	 * Test palettes
	 * 
	 * @throws ModelDefinitionException
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(4)
	public void testPalette() throws SaveResourceException, ModelDefinitionException {

		log("testPalette()");

		DiagramTechnologyAdapter diagramTA = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);

		paletteResource = diagramTA.getDiagramSpecificationResourceFactory().getPaletteResourceFactory()
				.makeDiagramPaletteResource(paletteName, diagramSpecificationResource,
						diagramTA.getTechnologyContextManager(), true);

		// paletteResource =
		// DiagramPaletteResourceImpl.makeDiagramPaletteResource(diagramSpecificationResource,
		// paletteName,
		// applicationContext);

		DiagramPaletteElement diagramPaletteElement = paletteResource.getFactory().makeDiagramPaletteElement();
		ShapeGraphicalRepresentation shapeGR = paletteResource.getFactory().makeShapeGraphicalRepresentation();
		diagramPaletteElement.setGraphicalRepresentation(shapeGR);
		paletteResource.getDiagramPalette().addToElements(diagramPaletteElement);

		paletteResource.save(null);
		assertTrue(paletteResource.getFlexoIODelegate().exists());
		diagramSpecificationResource.save(null);
		assertTrue(diagramSpecificationResource.getDiagramPaletteResources().contains(paletteResource));

		assertEquals(1, diagramSpecificationResource.getDiagramSpecification().getPalettes().size());

	}

	/**
	 * Test example diagrams
	 * 
	 * @throws ModelDefinitionException
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(5)
	public void testExampleDiagrams() throws SaveResourceException, ModelDefinitionException {

		log("testExampleDiagrams()");

		DiagramTechnologyAdapter diagramTA = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);

		exampleDiagramResource = diagramTA.getDiagramSpecificationResourceFactory().getExampleDiagramsResourceFactory()
				.makeExampleDiagramResource("exampleDiagram1", diagramSpecificationResource,
						diagramTA.getTechnologyContextManager(), true);

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

		diagramSpecificationResource.addToContents(exampleDiagramResource);
		diagramSpecificationResource.getDiagramSpecification().addToExampleDiagrams(diagram);
		diagramSpecificationResource.save(null);
		exampleDiagramResource.save(null);

	}

	/**
	 * Reload the DiagramSpecification, tests that uri and name are persistent
	 */
	@Test
	@TestOrder(6)
	public void testReloadDiagramSpecification() {

		log("testReloadDiagramSpecification()");

		applicationContext = instanciateTestServiceManager(DiagramTechnologyAdapter.class);

		technologicalAdapter = applicationContext.getTechnologyAdapterService()
				.getTechnologyAdapter(DiagramTechnologyAdapter.class);

		applicationContext.getResourceCenterService()
				.addToResourceCenters(newResourceCenter = new DirectoryResourceCenter(testResourceCenterDirectory,
						applicationContext.getResourceCenterService()));
		newResourceCenter.performDirectoryWatchingNow();

		DiagramSpecificationRepository<?> repository = technologicalAdapter
				.getDiagramSpecificationRepository(newResourceCenter);
		assertNotNull(repository);

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
