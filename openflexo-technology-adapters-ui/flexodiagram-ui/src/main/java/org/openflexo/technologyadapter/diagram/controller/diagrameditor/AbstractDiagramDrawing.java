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
package org.openflexo.technologyadapter.diagram.controller.diagrameditor;

import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.FGEModelFactory;
import org.openflexo.fge.GRBinding.ConnectorGRBinding;
import org.openflexo.fge.GRBinding.DrawingGRBinding;
import org.openflexo.fge.GRBinding.ShapeGRBinding;
import org.openflexo.fge.GRProvider.ConnectorGRProvider;
import org.openflexo.fge.GRProvider.DrawingGRProvider;
import org.openflexo.fge.GRProvider.ShapeGRProvider;
import org.openflexo.fge.GRStructureVisitor;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.impl.DrawingImpl;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.toolbox.ToolBox;

/**
 * This is the abstraction of a drawing representing a {@link Diagram}<br>
 * Note that this class is abstract. There are two implementations. One for a {@link FreeDiagramEditor}, see {@link FreeDiagramDrawing}, and
 * one for {@link FMLControlledDiagramEditor}, see {@link FMLControlledDiagramDrawing}
 * 
 * @author sylvain
 * 
 */
public abstract class AbstractDiagramDrawing extends DrawingImpl<Diagram> implements DiagramRepresentationConstants {

	private static final Logger logger = Logger.getLogger(AbstractDiagramDrawing.class.getPackage().getName());

	public AbstractDiagramDrawing(Diagram model, boolean readOnly) {
		super(model, ((DiagramResource) model.getResource()).getFactory(), PersistenceMode.UniqueGraphicalRepresentations);
		setEditable(!readOnly);
	}

	@Override
	public void init() {

		final DrawingGRBinding<Diagram> drawingBinding = bindDrawing(Diagram.class, "drawing", new DrawingGRProvider<Diagram>() {
			@Override
			public DrawingGraphicalRepresentation provideGR(Diagram drawable, FGEModelFactory factory) {
				return retrieveGraphicalRepresentation(drawable, (DiagramFactory) factory);
			}
		});
		final ShapeGRBinding<DiagramShape> shapeBinding = bindShape(DiagramShape.class, "shape", new ShapeGRProvider<DiagramShape>() {
			@Override
			public ShapeGraphicalRepresentation provideGR(DiagramShape drawable, FGEModelFactory factory) {
				return retrieveGraphicalRepresentation(drawable, (DiagramFactory) factory);
			}
		});
		final ConnectorGRBinding<DiagramConnector> connectorBinding = bindConnector(DiagramConnector.class, "connector", shapeBinding,
				shapeBinding, new ConnectorGRProvider<DiagramConnector>() {
					@Override
					public ConnectorGraphicalRepresentation provideGR(DiagramConnector drawable, FGEModelFactory factory) {
						return retrieveGraphicalRepresentation(drawable, (DiagramFactory) factory);
					}
				});

		drawingBinding.addToWalkers(new GRStructureVisitor<Diagram>() {

			@Override
			public void visit(Diagram diagram) {
				for (DiagramShape shape : diagram.getShapes()) {
					drawShape(shapeBinding, shape, diagram);
				}
				for (DiagramConnector connector : diagram.getConnectors()) {
					drawConnector(connectorBinding, connector, connector.getStartShape(), connector.getEndShape(), diagram);
				}
			}
		});

		shapeBinding.addToWalkers(new GRStructureVisitor<DiagramShape>() {
			@Override
			public void visit(DiagramShape aShape) {
				for (DiagramShape shape : aShape.getShapes()) {
					drawShape(shapeBinding, shape, aShape);
				}
				for (DiagramConnector connector : aShape.getConnectors()) {
					drawConnector(connectorBinding, connector, connector.getStartShape(), connector.getEndShape(), aShape);
				}
			}
		});

		shapeBinding.setDynamicPropertyValue(GraphicalRepresentation.TEXT, new DataBinding<String>("drawable.name"), true);
		connectorBinding.setDynamicPropertyValue(GraphicalRepresentation.TEXT, new DataBinding<String>("drawable.name"), true);

	}

	@Override
	public void delete() {
		super.delete();
	}

	public Diagram getDiagram() {
		return getModel();
	}

	protected DrawingGraphicalRepresentation retrieveGraphicalRepresentation(Diagram diagram, DiagramFactory factory) {
		DrawingGraphicalRepresentation returned = null;
		if (diagram.getGraphicalRepresentation() != null) {
			diagram.getGraphicalRepresentation().setFactory(factory);
			returned = diagram.getGraphicalRepresentation();
		} else {
			returned = factory.makeDrawingGraphicalRepresentation();
			diagram.setGraphicalRepresentation(returned);
		}
		returned.addToMouseClickControls(new DiagramEditor.ShowContextualMenuControl(factory));
		if (ToolBox.getPLATFORM() != ToolBox.MACOS) {
			returned.addToMouseClickControls(new DiagramEditor.ShowContextualMenuControl(factory, true));
		}
		return returned;
	}

	protected ShapeGraphicalRepresentation retrieveGraphicalRepresentation(DiagramShape shape, DiagramFactory factory) {
		ShapeGraphicalRepresentation returned = null;
		if (shape.getGraphicalRepresentation() != null) {
			shape.getGraphicalRepresentation().setFactory(factory);
			returned = shape.getGraphicalRepresentation();
		} else {
			returned = factory.makeShapeGraphicalRepresentation();
			shape.setGraphicalRepresentation(returned);
		}
		returned.addToMouseClickControls(new DiagramEditor.ShowContextualMenuControl(factory));
		if (ToolBox.getPLATFORM() != ToolBox.MACOS) {
			returned.addToMouseClickControls(new DiagramEditor.ShowContextualMenuControl(factory, true));
		}
		returned.addToMouseDragControls(new DrawEdgeControl(factory));

		return returned;
	}

	protected ConnectorGraphicalRepresentation retrieveGraphicalRepresentation(DiagramConnector connector, DiagramFactory factory) {
		ConnectorGraphicalRepresentation returned = null;
		if (connector.getGraphicalRepresentation() != null) {
			connector.getGraphicalRepresentation().setFactory(factory);
			returned = connector.getGraphicalRepresentation();
		} else {
			returned = factory.makeConnectorGraphicalRepresentation();
			connector.setGraphicalRepresentation(returned);
		}
		returned.addToMouseClickControls(new DiagramEditor.ShowContextualMenuControl(factory));
		if (ToolBox.getPLATFORM() != ToolBox.MACOS) {
			returned.addToMouseClickControls(new DiagramEditor.ShowContextualMenuControl(factory, true));
		}
		return returned;
	}

}
