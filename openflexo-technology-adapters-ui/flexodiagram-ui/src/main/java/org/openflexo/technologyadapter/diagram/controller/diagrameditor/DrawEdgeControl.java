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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.openflexo.diana.ConnectorGraphicalRepresentation;
import org.openflexo.diana.Drawing.DrawingTreeNode;
import org.openflexo.diana.Drawing.ShapeNode;
import org.openflexo.diana.connectors.ConnectorSpecification.ConnectorType;
import org.openflexo.diana.control.AbstractDianaEditor;
import org.openflexo.diana.control.MouseControlContext;
import org.openflexo.diana.control.actions.MouseDragControlActionImpl;
import org.openflexo.diana.control.actions.MouseDragControlImpl;
import org.openflexo.diana.swing.control.JMouseControlContext;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoUndoManager.FlexoActionCompoundEdit;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.pamela.factory.EditingContext;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.AddConnector;
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
		ShapeNode<?> fromShape = null;
		ShapeNode<?> toShape = null;
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
				fromShape = (ShapeNode<?>) node;
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

					if (controller instanceof FMLControlledDiagramEditor) {
						handleFMLControlledEdge(controller, context);
					}
					else {
						performAddDefaultConnector(controller);
					}

					// System.out.println("Add ConnectorSpecification contextualMenuInvoker="+contextualMenuInvoker+"
					// point="+contextualMenuClickedPoint);
					/*CompoundEdit drawEdge = editingContext.getUndoManager().startRecording("Draw edge");
					DiagramConnector newConnector = factory.makeNewConnector("edge", fromShape.getDrawable(), toShape.getDrawable(),
							controller.getDrawing().getModel());
					DrawingTreeNode<?, ?> fatherNode = DianaUtils.getFirstCommonAncestor(fromShape, toShape);
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
			FlexoActionCompoundEdit drawEdgeEdit = (FlexoActionCompoundEdit) editingContext.getUndoManager().startRecording("Draw edge");

			if (fromShape == null) {
				logger.warning("Cannot add connector for null start node");
				return;
			}
			if (toShape == null) {
				logger.warning("Cannot add connector for null end node");
				return;
			}

			DiagramShape startShape = controller.getShapeForShapeNode(fromShape);
			DiagramShape endShape = controller.getShapeForShapeNode(toShape);

			// System.out.println("startShape = " + startShape);
			// System.out.println("endShape = " + endShape);
			// System.out.println("controller = " + controller);

			AddConnector action = AddConnector.actionType.makeNewAction(startShape, null, controller.getFlexoController().getEditor());
			action.setToShape(endShape);
			action.setAutomaticallyCreateConnector(true);
			ConnectorGraphicalRepresentation connectorGR = factory.makeConnectorGraphicalRepresentation();
			connectorGR.setConnectorType(ConnectorType.LINE);
			connectorGR.setIsSelectable(true);
			connectorGR.setIsFocusable(true);
			connectorGR.setIsReadOnly(false);
			connectorGR.setForeground(controller.getInspectedForegroundStyle().cloneStyle());
			connectorGR.setTextStyle(controller.getInspectedTextStyle().cloneStyle());

			action.setGraphicalRepresentation(connectorGR);

			action.setCompoundEdit(drawEdgeEdit);
			action.doAction();

			// System.out.println("Added new connector !");
			// editingContext.getUndoManager().stopRecording(drawEdgeEdit);
			controller.setSelectedObject(controller.getDrawing().getDrawingTreeNode(action.getNewConnector()));

			drawEdge = false;
			fromShape = null;
			toShape = null;
			controller.getDrawingView().setDrawEdgeAction(null);

		}

		/*	private void performAddConnector(DiagramEditor controller, ConnectorGraphicalRepresentation connectorGR, String text) {
				CompoundEdit drawEdgeEdit = editingContext.getUndoManager().startRecording("Draw edge");
		
				DiagramShape startShape = controller.getShapeForShapeNode(fromShape);
				DiagramShape endShape = controller.getShapeForShapeNode(toShape);
		
				AddConnector action = AddConnector.actionType.makeNewAction(startShape, null, controller.getFlexoController().getEditor());
				action.setToShape(endShape);
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
		
			}*/

		private void handleFMLControlledEdge(final DiagramEditor controller, MouseControlContext context) {
			// TODO: Choose one of 2 versions
			VirtualModel virtualModel = ((FMLControlledDiagramEditor) controller).getVirtualModelInstance().getVirtualModel();

			DiagramShape startShape = controller.getShapeForShapeNode(fromShape);
			DiagramShape endShape = controller.getShapeForShapeNode(toShape);

			/* we have to find if both starts and end are FlexoConcept, else, draw a standard connector */

			FlexoObject startObject = ((FMLControlledDiagramEditor) controller).getDrawableForDrawingTreeNode(fromShape);
			FlexoObject endObject = ((FMLControlledDiagramEditor) controller).getDrawableForDrawingTreeNode(toShape);

			if (startObject instanceof FlexoConceptInstance && endObject instanceof FlexoConceptInstance && virtualModel != null) {

				FlexoConceptInstance startFlexoConceptInstance = (FlexoConceptInstance) startObject;
				FlexoConceptInstance endFlexoConceptInstance = (FlexoConceptInstance) endObject;

				List<FlexoConcept> availableFlexoConcepts = virtualModel.getFlexoConcepts();
				List<LinkScheme> availableConnectors = new ArrayList<>();
				for (FlexoConcept flexoConcept : availableFlexoConcepts) {
					if (flexoConcept.getFlexoBehaviours(LinkScheme.class) != null) {
						for (LinkScheme linkScheme : flexoConcept.getFlexoBehaviours(LinkScheme.class)) {
							if (linkScheme.isValidTarget(startFlexoConceptInstance.getFlexoConcept(),
									endFlexoConceptInstance.getFlexoConcept())) {
								availableConnectors.add(linkScheme);
							}
						}
					}
				}
				if (availableConnectors.size() > 0) {
					JPopupMenu popup = new JPopupMenu();
					for (final LinkScheme linkScheme : availableConnectors) {
						JMenuItem menuItem = new JMenuItem(linkScheme.getDeclaringVirtualModel().getLocalizedDictionary()
								.localizedForKey(linkScheme.getLabel() != null ? linkScheme.getLabel() : linkScheme.getName()));
						menuItem.addActionListener(
								new DrawingEdgeActionListener((FMLControlledDiagramEditor) controller, startShape, endShape, linkScheme));
						popup.add(menuItem);
					}
					JMenuItem menuItem = new JMenuItem(startObject.getLocales().localizedForKey("graphical_connector_only"));
					menuItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							performAddDefaultConnector(controller);
						}
					});
					popup.add(menuItem);
					popup.show(controller.getDrawingView(), context.getPoint().x, context.getPoint().y);
				}
				else {
					performAddDefaultConnector(controller);
				}
			}
			else {
				performAddDefaultConnector(controller);
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
				}
				else {
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
				Point from = controller.getDrawing().getRoot().convertRemoteNormalizedPointToLocalViewCoordinates(
						fromShape.getShape().getShape().getCenter(), fromShape, controller.getScale());
				Point to = currentDraggingLocationInDrawingView;
				if (toShape != null) {
					to = controller.getDrawing().getRoot().convertRemoteNormalizedPointToLocalViewCoordinates(
							toShape.getShape().getShape().getCenter(), toShape, controller.getScale());
					g.setColor(Color.BLUE);
				}
				else {
					g.setColor(Color.RED);
				}
				g.drawLine(from.x, from.y, to.x, to.y);
			}
		}

		public class DrawingEdgeActionListener implements ActionListener {

			private final DiagramShape sourceShape;
			private final DiagramShape targetShape;
			private final FMLControlledDiagramEditor controller;
			private final LinkScheme linkScheme;

			DrawingEdgeActionListener(FMLControlledDiagramEditor controller, DiagramShape sourceShape, DiagramShape targetShape,
					LinkScheme linkScheme) {
				this.sourceShape = sourceShape;
				this.targetShape = targetShape;
				this.controller = controller;
				this.linkScheme = linkScheme;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				LinkSchemeAction action = new LinkSchemeAction(linkScheme, controller.getVirtualModelInstance(), null,
						controller.getFlexoController().getEditor());
				action.setFromShape(sourceShape);
				action.setToShape(targetShape);
				action.doAction();
			}

		}

	}

}
