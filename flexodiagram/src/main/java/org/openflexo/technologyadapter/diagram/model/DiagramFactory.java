/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.diagram.model;

import java.util.logging.Logger;

import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.FGEModelFactoryImpl;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.connectors.ConnectorSpecification.ConnectorType;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.FlexoModelFactory;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.resource.PamelaResource;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.EditingContext;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;

/**
 * Diagram factory<br>
 * Only one instance of this class should be used
 * 
 * @author sylvain
 * 
 */
public class DiagramFactory extends FGEModelFactoryImpl implements FlexoModelFactory {

	private static final Logger logger = Logger.getLogger(DiagramFactory.class.getPackage().getName());

	public DiagramFactory(EditingContext editingContext) throws ModelDefinitionException {
		super(Diagram.class, DiagramShape.class, DiagramConnector.class);
		setEditingContext(editingContext);
	}

	public Diagram makeNewDiagram() {
		return newInstance(Diagram.class);
	}

	public Diagram makeNewDiagram(DiagramSpecification diagramSpecification) {
		Diagram returned = newInstance(Diagram.class);
		returned.setDiagramSpecification(diagramSpecification);
		return returned;
	}

	public DiagramShape makeNewShape(String name, ShapeGraphicalRepresentation gr, DiagramContainerElement<?> container) {
		if (gr == null) {
			gr = makeShapeGraphicalRepresentation(ShapeType.RECTANGLE);
		}
		DiagramShape returned = newInstance(DiagramShape.class);
		returned.setGraphicalRepresentation(gr);
		returned.setName(name);
		container.addToShapes(returned);
		return returned;
	}

	public DiagramShape makeNewShape(String name, ShapeType shapeType, FGEPoint fgePoint, DiagramContainerElement<?> container) {
		ShapeGraphicalRepresentation gr = makeShapeGraphicalRepresentation(shapeType);
		if (fgePoint != null) {
			gr.setX(fgePoint.getX());
			gr.setY(fgePoint.getY());
		}
		DiagramShape returned = newInstance(DiagramShape.class);
		returned.setGraphicalRepresentation(gr);
		returned.setName(name);
		container.addToShapes(returned);
		return returned;
	}

	public DiagramShape makeNewShape(String name, DiagramContainerElement<?> container) {
		return makeNewShape(name, ShapeType.RECTANGLE, null, container);
	}

	public DiagramConnector makeNewConnector(String name, DiagramShape startShape, DiagramShape endShape,
			DiagramContainerElement<?> container) {
		ConnectorGraphicalRepresentation gr = makeConnectorGraphicalRepresentation(ConnectorType.LINE);
		DiagramConnector returned = newInstance(DiagramConnector.class);
		returned.setGraphicalRepresentation(gr);
		returned.setName(name);
		returned.setStartShape(startShape);
		returned.setEndShape(endShape);
		container.addToConnectors(returned);
		return returned;
	}

	private PamelaResource<?, ?> resourceBeeingDeserialized = null;

	@Override
	public synchronized void startDeserializing(PamelaResource<?, ?> resource) throws ConcurrentDeserializationException {
		if (resourceBeeingDeserialized == null) {
			resourceBeeingDeserialized = resource;
		} else {
			throw new ConcurrentDeserializationException(resource);
		}
	}

	@Override
	public synchronized void stopDeserializing(PamelaResource<?, ?> resource) {
		if (resourceBeeingDeserialized == resource) {
			resourceBeeingDeserialized = null;
		}
	}

	@Override
	public <I> void objectHasBeenDeserialized(I newlyCreatedObject, Class<I> implementedInterface) {
		super.objectHasBeenDeserialized(newlyCreatedObject, implementedInterface);
		if (newlyCreatedObject instanceof FlexoObject) {
			if (resourceBeeingDeserialized != null) {
				resourceBeeingDeserialized.setLastID(((FlexoObject) newlyCreatedObject).getFlexoID());
			} else {
				logger.warning("Could not access resource beeing deserialized");
			}
		}
	}
}
