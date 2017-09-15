/*
 * Copyright (c) 2013-2017, Openflexo
 *
 * This file is part of Flexo-foundation, a component of the software infrastructure
 * developed at Openflexo.
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
 *           Additional permission under GNU GPL version 3 section 7
 *           If you modify this Program, or any covered work, by linking or
 *           combining it with software containing parts covered by the terms
 *           of EPL 1.0, the licensors of this Program grant you additional permission
 *           to convey the resulting work.
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.shapes.Rectangle;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationRepository;

/**
 * Provides testing environment in JDBC context
 *
 */
public abstract class DiagramTestCase extends OpenflexoTestCase {

	public static DiagramTechnologyAdapter technologicalAdapter;
	public static FlexoEditor editor;

	protected static FlexoResourceCenter<?> diagramTestResourceCenter;
	protected static DirectoryResourceCenter newResourceCenter;

	@AfterClass
	public static void tearDownClass() {
		deleteTestResourceCenters();
		unloadServiceManager();
	}

	@BeforeClass
	public static void setupClass() throws ModelDefinitionException, IOException {
		serviceManager = instanciateTestServiceManager(DiagramTechnologyAdapter.class);

		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);

		diagramTestResourceCenter = serviceManager.getResourceCenterService().getFlexoResourceCenter("http://openflexo.org/diagram-test");

		newResourceCenter = makeNewDirectoryResourceCenter(serviceManager);

		assertNotNull(diagramTestResourceCenter);

		DiagramSpecificationRepository<?> repository = technologicalAdapter.getDiagramSpecificationRepository(diagramTestResourceCenter);
		assertNotNull(repository);

		editor = new FlexoTestEditor(null, serviceManager);

		assertNotNull(serviceManager);
		assertNotNull(technologicalAdapter);
	}

	public DiagramShape createShapeInDiagram(Diagram diagram, String name) {
		org.openflexo.technologyadapter.diagram.model.action.AddShape addShapeAction = org.openflexo.technologyadapter.diagram.model.action.AddShape.actionType
				.makeNewAction(diagram, null, editor);
		addShapeAction.setNewShapeName(name);
		DiagramFactory factory = ((DiagramResource) diagram.getResource()).getFactory();
		ShapeGraphicalRepresentation shapeGR = factory.newInstance(ShapeGraphicalRepresentation.class);
		Rectangle rectangleShape = factory.newInstance(Rectangle.class);
		shapeGR.setShapeSpecification(rectangleShape);
		addShapeAction.setGraphicalRepresentation(shapeGR);
		addShapeAction.doAction();
		assertTrue(addShapeAction.hasActionExecutionSucceeded());
		return addShapeAction.getNewShape();
	}

}
