/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.controller.diagrameditor;

import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
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
		returned.addToMouseClickControls(new DiagramEditor.ShowContextualMenuControl(factory.getEditingContext()));
		if (ToolBox.getPLATFORM() != ToolBox.MACOS) {
			returned.addToMouseClickControls(new DiagramEditor.ShowContextualMenuControl(factory.getEditingContext(), true));
		}
		return returned;
	}

	protected ShapeGraphicalRepresentation retrieveGraphicalRepresentation(DiagramShape shape, DiagramFactory factory) {

		if (shape != null) {
			ShapeGraphicalRepresentation returned = null;
			if (shape.getGraphicalRepresentation() != null) {
				shape.getGraphicalRepresentation().setFactory(factory);
				returned = shape.getGraphicalRepresentation();
			} else {
				returned = factory.makeShapeGraphicalRepresentation();
				shape.setGraphicalRepresentation(returned);
			}
			returned.addToMouseClickControls(new DiagramEditor.ShowContextualMenuControl(factory.getEditingContext()));
			if (ToolBox.getPLATFORM() != ToolBox.MACOS) {
				returned.addToMouseClickControls(new DiagramEditor.ShowContextualMenuControl(factory.getEditingContext(), true));
			}
			returned.addToMouseDragControls(new DrawEdgeControl(factory));
			return returned;
		}
		return null;
	}

	protected ConnectorGraphicalRepresentation retrieveGraphicalRepresentation(DiagramConnector connector, DiagramFactory factory) {
		if (connector != null) {
			ConnectorGraphicalRepresentation returned = null;
			if (connector.getGraphicalRepresentation() != null) {
				connector.getGraphicalRepresentation().setFactory(factory);
				returned = connector.getGraphicalRepresentation();
			} else {
				returned = factory.makeConnectorGraphicalRepresentation();
				connector.setGraphicalRepresentation(returned);
			}
			returned.addToMouseClickControls(new DiagramEditor.ShowContextualMenuControl(factory.getEditingContext()));
			if (ToolBox.getPLATFORM() != ToolBox.MACOS) {
				returned.addToMouseClickControls(new DiagramEditor.ShowContextualMenuControl(factory.getEditingContext(), true));
			}
			return returned;
		}
		return null;
	}

}
