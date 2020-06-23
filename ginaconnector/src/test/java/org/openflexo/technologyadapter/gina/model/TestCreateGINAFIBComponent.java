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

package org.openflexo.technologyadapter.gina.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.action.AddRepositoryFolder;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.model.action.CreateGINAFIBComponent;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResource;
import org.openflexo.technologyadapter.gina.rm.GINAResourceRepository;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test basic diagram manipulations
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestCreateGINAFIBComponent extends OpenflexoTestCase {

	public static GINATechnologyAdapter technologicalAdapter;
	public static FlexoServiceManager applicationContext;
	public static FlexoResourceCenter<?> resourceCenter;
	public static GINAResourceRepository<?> repository;
	public static RepositoryFolder<GINAFIBComponentResource, ?> componentFolder;

	public static FlexoEditor editor;

	private static GINAFIBComponentResource componentResource;

	/**
	 * Initialize
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(1)
	public void testInitialize() throws IOException {

		log("testInitialize()");

		applicationContext = instanciateTestServiceManager(GINATechnologyAdapter.class);

		technologicalAdapter = applicationContext.getTechnologyAdapterService()
				.getTechnologyAdapter(GINATechnologyAdapter.class);

		DirectoryResourceCenter resourceCenter = makeNewDirectoryResourceCenter(applicationContext);
		assertNotNull(resourceCenter);

		System.out.println("Found resource center: " + resourceCenter);

		repository = technologicalAdapter.getGINAResourceRepository(resourceCenter);

		assertNotNull(repository);

		assertNotNull(applicationContext);
		assertNotNull(technologicalAdapter);
		assertNotNull(resourceCenter);
		assertNotNull(repository);

		editor = new FlexoTestEditor(null, applicationContext);

	}

	/**
	 * Test component folder
	 */
	@Test
	@TestOrder(2)
	public void testComponentRepositoryFolder() {

		log("testComponentRepositoryFolder()");

		AddRepositoryFolder addRepositoryFolder = AddRepositoryFolder.actionType
				.makeNewAction(repository.getRootFolder(), null, editor);
		addRepositoryFolder.setNewFolderName("NewFolder");
		addRepositoryFolder.doAction();
		assertTrue(addRepositoryFolder.hasActionExecutionSucceeded());
		componentFolder = addRepositoryFolder.getNewFolder();
		assertTrue(((File) componentFolder.getSerializationArtefact()).exists());
	}

	/**
	 * Test create component
	 */
	@Test
	@TestOrder(3)
	public void testCreateNewComponent() {

		log("testCreateNewComponent()");

		CreateGINAFIBComponent createComponent = CreateGINAFIBComponent.actionType.makeNewAction(componentFolder, null,
				editor);
		createComponent.setComponentName("TestComponent.fib");
		createComponent.doAction();
		assertTrue(createComponent.hasActionExecutionSucceeded());
		componentResource = createComponent.getNewComponentResource();

		assertTrue(componentResource.getIODelegate() instanceof FileIODelegate);

		assertTrue(((FileIODelegate) componentResource.getIODelegate()).getFile().exists());

	}

	/**
	 * Test diagram edition
	 */
	/*
	 * @Test
	 * 
	 * @TestOrder(3) public void testEditDiagram() {
	 * 
	 * log("testEditDiagram()");
	 * 
	 * try { // Edit diagram DiagramFactory factory =
	 * diagramResource.getFactory(); Diagram diagram =
	 * diagramResource.getDiagram();
	 * 
	 * DiagramShape shape1 = factory.makeNewShape("Shape1a",
	 * ShapeType.RECTANGLE, new DianaPoint(100, 100), diagram);
	 * shape1.getGraphicalRepresentation().setForeground(factory.
	 * makeForegroundStyle(Color.RED));
	 * shape1.getGraphicalRepresentation().setBackground(factory.
	 * makeColoredBackground(Color.BLUE)); DiagramShape shape2 =
	 * factory.makeNewShape("Shape2a", ShapeType.RECTANGLE, new DianaPoint(200,
	 * 100), diagram);
	 * shape2.getGraphicalRepresentation().setForeground(factory.
	 * makeForegroundStyle(Color.BLUE));
	 * shape2.getGraphicalRepresentation().setBackground(factory.
	 * makeColoredBackground(Color.WHITE)); DiagramConnector connector1 =
	 * factory.makeNewConnector("Connector", shape1, shape2, diagram);
	 * diagram.addToShapes(shape1); diagram.addToShapes(shape2);
	 * diagram.addToConnectors(connector1);
	 * 
	 * // Testing management of FlexoID assertEquals(1, diagram.getFlexoID());
	 * assertEquals(2, shape1.getFlexoID()); assertEquals(3,
	 * shape2.getFlexoID()); assertEquals(4, connector1.getFlexoID());
	 * 
	 * assertEquals(4, diagramResource.getLastID());
	 * 
	 * diagramResource.save(null);
	 * 
	 * } catch (SaveResourceException e) { fail(e.getMessage()); }
	 * 
	 * }
	 */

	/**
	 * Reload the diagram
	 */
	/*
	 * @Test
	 * 
	 * @TestOrder(4) public void testReloadDiagram() {
	 * 
	 * log("testReloadDiagram()");
	 * 
	 * DiagramResource reloadedResource =
	 * DiagramResourceImpl.retrieveDiagramResource( ((FileFlexoIODelegate)
	 * (diagramResource.getFlexoIODelegate())).getFile(), resourceCenter,
	 * applicationContext); assertNotNull(reloadedResource);
	 * assertNotSame(diagramResource, reloadedResource);
	 * assertEquals(diagramResource.getURI(), reloadedResource.getURI());
	 * 
	 * assertEquals(2, reloadedResource.getDiagram().getShapes().size());
	 * assertEquals(1, reloadedResource.getDiagram().getConnectors().size());
	 * 
	 * assertEquals(4, reloadedResource.getLastID());
	 * 
	 * DiagramShape shape1 = reloadedResource.getDiagram().getShapes().get(0);
	 * DiagramShape shape2 = reloadedResource.getDiagram().getShapes().get(1);
	 * DiagramConnector connector1 =
	 * reloadedResource.getDiagram().getConnectors().get(0);
	 * 
	 * // Testing management of FlexoID assertEquals(1,
	 * reloadedResource.getDiagram().getFlexoID()); assertEquals(2,
	 * shape1.getFlexoID()); assertEquals(3, shape2.getFlexoID());
	 * assertEquals(4, connector1.getFlexoID());
	 * 
	 * // Edit diagram DiagramFactory factory = reloadedResource.getFactory();
	 * Diagram diagram = reloadedResource.getDiagram();
	 * 
	 * DiagramShape shape3 = factory.makeNewShape("Shape3a",
	 * ShapeType.RECTANGLE, new DianaPoint(100, 100), diagram);
	 * shape1.getGraphicalRepresentation().setForeground(factory.
	 * makeForegroundStyle(Color.RED));
	 * shape1.getGraphicalRepresentation().setBackground(factory.
	 * makeColoredBackground(Color.BLUE));
	 * 
	 * DiagramConnector connector2 = factory.makeNewConnector("Connector",
	 * shape1, shape3, diagram); assertEquals(5, shape3.getFlexoID());
	 * assertEquals(6, connector2.getFlexoID());
	 * 
	 * }
	 */

}
