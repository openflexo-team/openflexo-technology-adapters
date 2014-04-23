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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.Drawing.ShapeNode;
import org.openflexo.fge.FGEUtils;
import org.openflexo.fge.connectors.ConnectorSpecification.ConnectorType;
import org.openflexo.fge.control.AbstractDianaEditor;
import org.openflexo.fge.control.MouseControlContext;
import org.openflexo.fge.control.actions.MouseDragControlActionImpl;
import org.openflexo.fge.control.actions.MouseDragControlImpl;
import org.openflexo.fge.swing.control.JMouseControlContext;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.editionaction.EditionAction;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.model.factory.EditingContext;
import org.openflexo.model.undo.CompoundEdit;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.AddConnector;
import org.openflexo.technologyadapter.diagram.model.action.DropSchemeAction;
import org.openflexo.technologyadapter.diagram.model.action.LinkSchemeAction;

public class DrawEdgeControl extends MouseDragControlImpl<DiagramEditor> {

	private static final Logger logger = Logger.getLogger(DrawEdgeControl.class.getPackage().getName());

	protected Point currentDraggingLocationInDrawingView = null;
	protected boolean drawEdge = false;

	public DrawEdgeControl(DiagramFactory factory) {
		super("Draw edge", MouseButton.LEFT, new DrawEdgeAction(factory), false, true, false, false, factory.getEditingContext()); // CTRL-DRAG
	}

	protected static class DrawEdgeAction extends MouseDragControlActionImpl<DiagramEditor> {

		Point currentDraggingLocationInDrawingView = null;
		boolean drawEdge = false;
		ShapeNode<DiagramShape> fromShape = null;
		ShapeNode<DiagramShape> toShape = null;
		private final DiagramFactory factory;
		private final EditingContext editingContext;
		
		public DrawEdgeAction(DiagramFactory factory) {
			this.factory = factory;
			this.editingContext = factory.getEditingContext();
		}

		@Override
		public boolean handleMousePressed(DrawingTreeNode<?, ?> node, DiagramEditor controller, MouseControlContext context) {
			if (node instanceof ShapeNode) {
				drawEdge = true;
				fromShape = (ShapeNode<DiagramShape>) node;
				controller.getDrawingView().setDrawEdgeAction(this);
				return true;
			}
			return false;
		}

		@Override
		public boolean handleMouseReleased(DrawingTreeNode<?, ?> node, final DiagramEditor controller, MouseControlContext context,
				boolean isSignificativeDrag) {
			if (drawEdge) {
				if (fromShape != null && toShape != null) {

					// VINCENT: I comment this because I tried on huge viewpoints with many link schemes, and this is not easy to use.
					// Most of the case what we want to reuse is the shape of connector pattern roles, so, I change the code to display only
					// the
					// shapes
					// of the connector pattern roles available for this virtual model

					logger.warning("Please implement DrawEdge when diagram is FML-managed");
					
					if (controller instanceof FMLControlledDiagramEditor) {
						handleFMLControlledEdge(controller,context);
					} else {
						performAddDefaultConnector(controller);
					}
					
					// System.out.println("Add ConnectorSpecification contextualMenuInvoker="+contextualMenuInvoker+" point="+contextualMenuClickedPoint);
					/*CompoundEdit drawEdge = editingContext.getUndoManager().startRecording("Draw edge");
					DiagramConnector newConnector = factory.makeNewConnector("edge", fromShape.getDrawable(), toShape.getDrawable(),
							controller.getDrawing().getModel());
					DrawingTreeNode<?, ?> fatherNode = FGEUtils.getFirstCommonAncestor(fromShape, toShape);
					((DiagramContainerElement<?>) fatherNode.getDrawable()).addToConnectors(newConnector);
					System.out.println("Add new connector !");
					editingContext.getUndoManager().stopRecording(drawEdge);
					controller.setSelectedObject(controller.getDrawing().getDrawingTreeNode(newConnector));*/
				}
				drawEdge = false;
				fromShape = null;
				toShape = null;
				controller.getDrawingView().setDrawEdgeAction(null);
				return true;
			}
			return false;

		}

		private void performAddDefaultConnector(DiagramEditor controller) {
			CompoundEdit drawEdgeEdit = editingContext.getUndoManager().startRecording("Draw edge");
			AddConnector action = AddConnector.actionType.makeNewAction(fromShape.getDrawable(), null, controller.getFlexoController()
					.getEditor());
			action.setToShape(toShape.getDrawable());
			action.setAutomaticallyCreateConnector(true);
			ConnectorGraphicalRepresentation connectorGR = factory.makeConnectorGraphicalRepresentation();
			connectorGR.setConnectorType(ConnectorType.LINE);
			connectorGR.setIsSelectable(true);
			connectorGR.setIsFocusable(true);
			connectorGR.setIsReadOnly(false);
			connectorGR.setForeground(controller.getInspectedForegroundStyle().cloneStyle());
			connectorGR.setTextStyle(controller.getInspectedTextStyle().cloneStyle());

			action.setGraphicalRepresentation(connectorGR);

			action.doAction();

			System.out.println("Added new connector !");
			editingContext.getUndoManager().stopRecording(drawEdgeEdit);
			controller.setSelectedObject(controller.getDrawing().getDrawingTreeNode(action.getNewConnector()));

			drawEdge = false;
			fromShape = null;
			toShape = null;
			controller.getDrawingView().setDrawEdgeAction(null);
			
		}

		private void performAddConnector(DiagramEditor controller, ConnectorGraphicalRepresentation connectorGR, String text) {
			CompoundEdit drawEdgeEdit = editingContext.getUndoManager().startRecording("Draw edge");
			AddConnector action = AddConnector.actionType.makeNewAction(fromShape.getDrawable(), null, controller.getFlexoController()
					.getEditor());
			action.setToShape(toShape.getDrawable());
			action.setGraphicalRepresentation(connectorGR);
			action.setNewConnectorName(text);
			action.doAction();

			System.out.println("Added new connector !");
			editingContext.getUndoManager().stopRecording(drawEdgeEdit);
			controller.setSelectedObject(controller.getDrawing().getDrawingTreeNode(action.getNewConnector()));

			drawEdge = false;
			fromShape = null;
			toShape = null;
			controller.getDrawingView().setDrawEdgeAction(null);

		}
		
		private void handleFMLControlledEdge(final DiagramEditor controller,MouseControlContext context){
			// TODO: Choose one of 2 versions
			VirtualModel virtualModel = ((FMLControlledDiagramEditor)controller).getVirtualModelInstance().getVirtualModel();
			
			if (virtualModel != null) {
				List<FlexoConcept> availableFlexoConcepts = virtualModel.getFlexoConcepts();
				List<LinkScheme> availableConnectors = new ArrayList<LinkScheme>();
				for (FlexoConcept flexoConcept : availableFlexoConcepts) {
					if (flexoConcept.getFlexoBehaviours(LinkScheme.class) != null) {
						availableConnectors.addAll(flexoConcept.getFlexoBehaviours(LinkScheme.class));
					}
				}
				if (availableConnectors.size() > 0) {
					JPopupMenu popup = new JPopupMenu();
					for (final LinkScheme linkScheme : availableConnectors) {
						JMenuItem menuItem = new JMenuItem(FlexoLocalization.localizedForKey(linkScheme.getLabel() != null ? linkScheme
								.getLabel() : linkScheme.getName()));
						menuItem.addActionListener(new DrawingEdgeActionListener((FMLControlledDiagramEditor) controller, fromShape.getDrawable(), toShape.getDrawable(), linkScheme));
						popup.add(menuItem);
					}
					JMenuItem menuItem = new JMenuItem(FlexoLocalization.localizedForKey("graphical_connector_only"));
					menuItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							performAddDefaultConnector(controller);
						}
					});
					popup.add(menuItem);
					popup.show(controller.getDrawingView(), context.getPoint().x, context.getPoint().y);
				} else {
					performAddDefaultConnector(controller);
				}
			}
			
			/*if (virtualModel != null) {
				List<FlexoConcept> availableFlexoConcepts = virtualModel.getFlexoConcepts();
				List<ConnectorRole> aivalableConnectorFlexoRoles = new ArrayList<ConnectorRole>();
				for (FlexoConcept flexoConcept : availableFlexoConcepts) {
					if (flexoConcept.getFlexoRoles(ConnectorRole.class) != null) {
						aivalableConnectorFlexoRoles.addAll(flexoConcept.getFlexoRoles(ConnectorRole.class));
					}
				}

				if (aivalableConnectorFlexoRoles.size() > 0) {
					JPopupMenu popup = new JPopupMenu();
					for (final ConnectorRole connectorPatternFlexoRole : aivalableConnectorFlexoRoles) {
						JMenuItem menuItem = new JMenuItem(FlexoLocalization.localizedForKey(connectorPatternFlexoRole
								.getFlexoConcept().getName()));
						menuItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String text="";
								if(connectorPatternFlexoRole.getLabel()!=null){
									text = connectorPatternFlexoRole.getLabel().getExpression().toString();// Value(null);
								}
								performAddConnector((DiagramEditor)controller, connectorPatternFlexoRole.getGraphicalRepresentation(), text);
								return;
							}
						});
						popup.add(menuItem);
					}
					JMenuItem menuItem = new JMenuItem(FlexoLocalization.localizedForKey("graphical_connector_only"));
					menuItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							performAddDefaultConnector(controller);
						}
					});
					popup.add(menuItem);
					popup.show(controller.getDrawingView(), context.getPoint().x, context.getPoint().y);
				} 
			}*/

		}

		@Override
		public boolean handleMouseDragged(DrawingTreeNode<?, ?> node, DiagramEditor controller, MouseControlContext context) {
			if (drawEdge) {
				MouseEvent event = ((JMouseControlContext) context).getMouseEvent();
				DrawingTreeNode<?, ?> dtn = controller.getDrawingView().getFocusRetriever().getFocusedObject(event);
				if (dtn instanceof ShapeNode && dtn != fromShape && !fromShape.getAncestors().contains(dtn)) {
					toShape = (ShapeNode<DiagramShape>) dtn;
				} else {
					toShape = null;
				}
				currentDraggingLocationInDrawingView = getPointInDrawingView(controller, context);
				controller.getDrawingView().getPaintManager().repaint(controller.getDrawingView());
				return true;
			}
			return false;
		}

		public void paint(Graphics g, AbstractDianaEditor controller) {
			if (drawEdge && currentDraggingLocationInDrawingView != null) {
				Point from = controller
						.getDrawing()
						.getRoot()
						.convertRemoteNormalizedPointToLocalViewCoordinates(fromShape.getShape().getShape().getCenter(), fromShape,
								controller.getScale());
				Point to = currentDraggingLocationInDrawingView;
				if (toShape != null) {
					to = controller
							.getDrawing()
							.getRoot()
							.convertRemoteNormalizedPointToLocalViewCoordinates(toShape.getShape().getShape().getCenter(), toShape,
									controller.getScale());
					g.setColor(Color.BLUE);
				} else {
					g.setColor(Color.RED);
				}
				g.drawLine(from.x, from.y, to.x, to.y);
			}
		}
		
		public class DrawingEdgeActionListener implements ActionListener{

			private DiagramShape sourceShape;
			private DiagramShape targetShape;
			private FMLControlledDiagramEditor controller;
			private LinkScheme linkScheme;
			
			DrawingEdgeActionListener(FMLControlledDiagramEditor controller,DiagramShape sourceShape,DiagramShape targetShape, LinkScheme linkScheme){
				this.sourceShape = sourceShape;
				this.targetShape = targetShape;
				this.controller = controller;
				this.linkScheme = linkScheme;
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				LinkSchemeAction action = LinkSchemeAction.actionType.makeNewAction(controller.getVirtualModelInstance(),
						null, controller.getFlexoController().getEditor());
				action.setLinkScheme(linkScheme);
				action.setFromShape(sourceShape);
				action.setToShape(targetShape);
				action.doAction();
			}

		}

	}
	
	
}
