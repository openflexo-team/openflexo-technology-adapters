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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.diana.geom.DianaPoint;
import org.openflexo.diana.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.model.ModelEntity;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test Diagram model
 * 
 * @author sylvain, vincent
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDiagram extends OpenflexoTestCase {

	/**
	 * Test the diagram factory
	 */
	@Test
	@TestOrder(1)
	public void testDiagramFactory() {

		try {
			DiagramFactory factory = new DiagramFactory(null, null);

			ModelEntity<Diagram> diagramEntity = factory.getModelContext().getModelEntity(Diagram.class);
			ModelEntity<DiagramShape> shapeEntity = factory.getModelContext().getModelEntity(DiagramShape.class);
			ModelEntity<DiagramConnector> connectorEntity = factory.getModelContext().getModelEntity(DiagramConnector.class);

			assertNotNull(diagramEntity);
			assertNotNull(shapeEntity);
			assertNotNull(connectorEntity);

		} catch (ModelDefinitionException e) {
			e.printStackTrace();
			fail();
		}
	}

	/**
	 * Test the diagram factory
	 */
	@Test
	@TestOrder(2)
	public void testInstanciateDiagram() throws Exception {

		DiagramFactory factory = new DiagramFactory(null, null);

		Diagram diagram = factory.newInstance(Diagram.class);
		assertTrue(diagram instanceof Diagram);

		DiagramShape shape1 = factory.makeNewShape("Shape1", ShapeType.RECTANGLE, new DianaPoint(100, 100), diagram);
		shape1.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.RED));
		shape1.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.BLUE));
		assertTrue(shape1 instanceof DiagramShape);
		DiagramShape shape2 = factory.makeNewShape("Shape2", ShapeType.RECTANGLE, new DianaPoint(200, 100), diagram);
		shape2.getGraphicalRepresentation().setForeground(factory.makeForegroundStyle(Color.BLUE));
		shape2.getGraphicalRepresentation().setBackground(factory.makeColoredBackground(Color.WHITE));
		assertTrue(shape2 instanceof DiagramShape);

		DiagramConnector connector1 = factory.makeNewConnector("Connector", shape1, shape2, diagram);
		assertTrue(connector1 instanceof DiagramConnector);

	}

}
