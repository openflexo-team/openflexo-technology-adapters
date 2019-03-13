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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.diana.Drawing.ContainerNode;
import org.openflexo.diana.Drawing.DrawingTreeNode;
import org.openflexo.diana.Drawing.RootNode;
import org.openflexo.diana.Drawing.ShapeNode;
import org.openflexo.diana.control.AbstractDianaEditor;
import org.openflexo.diana.control.MouseControlContext;
import org.openflexo.diana.control.actions.MouseDragControlActionImpl;
import org.openflexo.diana.control.actions.MouseDragControlImpl;
import org.openflexo.diana.geom.DianaPoint;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.pamela.factory.EditingContext;
import org.openflexo.technologyadapter.diagram.fml.DrawRectangleScheme;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.action.DrawRectangleSchemeAction;

public class DrawRectangleControl extends MouseDragControlImpl<DiagramEditor> {

	private static final Logger logger = Logger.getLogger(DrawRectangleControl.class.getPackage().getName());

	protected Point currentDraggingLocationInDrawingView = null;
	protected boolean drawRectangle = false;

	public DrawRectangleControl(DiagramFactory factory) {
		super("Draw rectangle", MouseButton.LEFT, new DrawRectangleAction(factory), false, false, false, true, factory.getEditingContext()); // SHIFT-DRAG
	}

	protected static class DrawRectangleAction extends MouseDragControlActionImpl<DiagramEditor> {

		Point currentDraggingLocationInDrawingView = null;
		Point startPointInDrawingView = null;
		Point endPointInDrawingView = null;
		boolean drawRectangle = false;
		private final DiagramFactory factory;
		private final EditingContext editingContext;

		private DrawRectangleScheme drawRectangleScheme;
		private DiagramContainerElement<?> containerElement;
		private FlexoConceptInstance containerFlexoConceptInstance;

		public DrawRectangleAction(DiagramFactory factory) {
			this.factory = factory;
			this.editingContext = factory.getEditingContext();
		}

		@Override
		public boolean handleMousePressed(DrawingTreeNode<?, ?> node, DiagramEditor controller, MouseControlContext context) {

			if (node instanceof ContainerNode) {
				System.out.println("Hop, c'est parti");
				List<DrawRectangleScheme> availableRectangleSchemes = getAvailableRectangleSchemes((ContainerNode<?, ?>) node, controller);
				if (availableRectangleSchemes.size() == 0) {
					return false;
				}
				else if (availableRectangleSchemes.size() == 1) {
					drawRectangleScheme = availableRectangleSchemes.get(0);
				}
				else if (availableRectangleSchemes.size() > 1) {
					logger.warning("Multiple applicable DrawRectangleSchemes, taking first one");
					drawRectangleScheme = availableRectangleSchemes.get(0);
				}

				// System.out.println("node: " + node);
				// System.out.println("drawable: " + node.getDrawable());

				if (node.getDrawable() instanceof FMLControlledDiagramElement) {
					containerElement = (DiagramContainerElement<?>) ((FMLControlledDiagramElement<?, ?>) node.getDrawable())
							.getDiagramElement();
					containerFlexoConceptInstance = ((FMLControlledDiagramElement<?, ?>) node.getDrawable()).getFlexoConceptInstance();
				}

				if (node.getDrawable() instanceof Diagram) {
					containerElement = (Diagram) node.getDrawable();
				}

				drawRectangle = true;
				startPointInDrawingView = getPointInDrawingView(controller, context);
				controller.getDrawingView().setDrawRectangleAction(this);
				return true;
			}
			return false;
		}

		@Override
		public boolean handleMouseReleased(DrawingTreeNode<?, ?> node, final DiagramEditor controller, MouseControlContext context,
				boolean isSignificativeDrag) {
			if (drawRectangle) {

				endPointInDrawingView = getPointInDrawingView(controller, context);

				// System.out.println("Building rectangle from " + startPointInDrawingView + " to " + endPointInDrawingView);

				DrawRectangleSchemeAction action = new DrawRectangleSchemeAction(drawRectangleScheme,
						((FMLControlledDiagramEditor) controller).getVirtualModelInstance(), null,
						((FMLControlledDiagramEditor) controller).getFlexoController().getEditor());
				action.setTargetConceptInstance(containerFlexoConceptInstance);
				if (drawRectangleScheme.getSelectObjects() && node instanceof ContainerNode) {
					List<DrawingTreeNode<?, ?>> nodeSelection = buildCurrentSelection((ContainerNode<?, ?>) node, controller);
					List<FlexoConceptInstance> selection = new ArrayList<>();
					// System.out.println("nodeSelection = " + nodeSelection);
					for (DrawingTreeNode<?, ?> dtn : nodeSelection) {
						if (dtn.getDrawable() instanceof FMLControlledDiagramElement) {
							// System.out.println(" Hop > " + ((FMLControlledDiagramElement) dtn.getDrawable()).getFlexoConceptInstance());
							selection.add(((FMLControlledDiagramElement) dtn.getDrawable()).getFlexoConceptInstance());
						}
					}
					action.setSelection(selection);
				}
				action.setFromLocation(new DianaPoint(startPointInDrawingView.getX(), startPointInDrawingView.getY()));
				action.setToLocation(new DianaPoint(endPointInDrawingView.getX(), endPointInDrawingView.getY()));
				action.doAction();

				// The new shape has well be added to the diagram, and the drawing (which listen to the diagram) has well received the event
				// The drawing is now up-to-date... but there is something wrong if we are in FML-controlled mode.
				// Since the shape has been added BEFORE the FlexoConceptInstance has been set, the drawing only knows about the
				// DiagamShape,
				// and not about an FMLControlledDiagramShape. That's why we need to notify again the new diagram element's parent, to be
				// sure that the Drawing can discover that the new shape is FML-controlled

				if (containerElement != null) {
					containerElement.getPropertyChangeSupport().firePropertyChange(DiagramElement.INVALIDATE, null, containerElement);
				}

				controller.getDrawingView().setDrawRectangleAction(null);
				drawRectangle = false;
				startPointInDrawingView = null;
				endPointInDrawingView = null;
				currentDraggingLocationInDrawingView = null;
				controller.getDrawingView().getPaintManager().repaint(controller.getDrawingView());

				return action.hasActionExecutionSucceeded();

			}
			return false;

		}

		@Override
		public boolean handleMouseDragged(DrawingTreeNode<?, ?> node, DiagramEditor controller, MouseControlContext context) {
			if (drawRectangle) {

				endPointInDrawingView = getPointInDrawingView(controller, context);

				/*MouseEvent event = ((JMouseControlContext) context).getMouseEvent();
				DrawingTreeNode<?, ?> dtn = controller.getDrawingView().getFocusRetriever().getFocusedObject(event);
				if (dtn instanceof ShapeNode && dtn != fromShape && !fromShape.getAncestors().contains(dtn)) {
					toShape = (ShapeNode<DiagramShape>) dtn;
				}
				else {
					toShape = null;
				}*/
				currentDraggingLocationInDrawingView = getPointInDrawingView(controller, context);

				if (drawRectangleScheme.getSelectObjects()) {
					List<DrawingTreeNode<?, ?>> newFocusSelection;
					if (node instanceof ContainerNode) {
						newFocusSelection = buildCurrentSelection((ContainerNode<?, ?>) node, controller);
					}
					else {
						newFocusSelection = Collections.emptyList();
					}
					controller.setFocusedObjects(newFocusSelection);
				}

				if (controller.getDrawingView() == null) {
					return false;
				}
				if (controller.getDelegate() != null) {
					controller.getDelegate().repaintAll();
				}

				// controller.getDrawingView().getPaintManager().repaint(controller.getDrawingView());
				return true;
			}
			return false;
		}

		private List<DrawingTreeNode<?, ?>> buildCurrentSelection(ContainerNode<?, ?> node, AbstractDianaEditor<?, ?, ?> controller) {
			if (getRectangleSelection() == null) {
				return null;
			}
			List<DrawingTreeNode<?, ?>> returned = new Vector<>();
			for (DrawingTreeNode<?, ?> child : node.getChildNodes()) {
				if (child.getGraphicalRepresentation().getIsVisible()) {
					FlexoObject childObject = ((FMLControlledDiagramEditor) controller).getDrawableForDrawingTreeNode(child);
					if (childObject instanceof FlexoConceptInstance) {
						boolean takeIt = (drawRectangleScheme.getChildrenFlexoConcept() == null);
						if (drawRectangleScheme.getChildrenFlexoConcept()
								.isAssignableFrom(((FlexoConceptInstance) childObject).getFlexoConcept())) {
							takeIt = true;
						}
						if (takeIt && child.isContainedInSelection(getRectangleSelection(), controller.getScale())) {
							returned.add(child);
						}
					}
					if (child instanceof ContainerNode) {
						returned.addAll(buildCurrentSelection((ContainerNode<?, ?>) child, controller));
					}

				}
			}
			return returned;
		}

		/**
		 * Return current rectangle selection
		 * 
		 * @return Rectangle object as current selection
		 */
		private Rectangle getRectangleSelection() {
			if (startPointInDrawingView != null && currentDraggingLocationInDrawingView != null) {
				Point origin = new Point();
				Dimension dim = new Dimension();
				if (startPointInDrawingView.x <= currentDraggingLocationInDrawingView.x) {
					origin.x = startPointInDrawingView.x;
					dim.width = currentDraggingLocationInDrawingView.x - startPointInDrawingView.x;
				}
				else {
					origin.x = currentDraggingLocationInDrawingView.x;
					dim.width = startPointInDrawingView.x - currentDraggingLocationInDrawingView.x;
				}
				if (startPointInDrawingView.y <= currentDraggingLocationInDrawingView.y) {
					origin.y = startPointInDrawingView.y;
					dim.height = currentDraggingLocationInDrawingView.y - startPointInDrawingView.y;
				}
				else {
					origin.y = currentDraggingLocationInDrawingView.y;
					dim.height = startPointInDrawingView.y - currentDraggingLocationInDrawingView.y;
				}
				return new Rectangle(origin, dim);
			}
			return null;
		}

		public void paint(Graphics g, AbstractDianaEditor controller) {
			if (drawRectangle && currentDraggingLocationInDrawingView != null) {
				Rectangle selection = getRectangleSelection();
				Graphics2D g2d = (Graphics2D) g;
				g.setColor(Color.RED);
				g.drawRect(selection.x, selection.y, selection.width, selection.height);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
				g.setColor(Color.YELLOW);
				g.fillRect(selection.x, selection.y, selection.width, selection.height);
			}
		}

		private List<DrawRectangleScheme> getAvailableRectangleSchemes(ContainerNode<?, ?> container, final DiagramEditor controller) {

			VirtualModel virtualModel = ((FMLControlledDiagramEditor) controller).getVirtualModelInstance().getVirtualModel();
			// System.out.println("virtual model = " + virtualModel);

			if (virtualModel == null) {
				return null;
			}

			List<DrawRectangleScheme> returned = new ArrayList<>();

			for (FlexoConcept concept : virtualModel.getFlexoConcepts()) {
				for (DrawRectangleScheme drScheme : concept.getAccessibleFlexoBehaviours(DrawRectangleScheme.class)) {
					if (container instanceof RootNode && drScheme.getTopTarget()) {
						returned.add(drScheme);
					}
					else if (container instanceof ShapeNode) {
						FlexoObject targetObject = ((FMLControlledDiagramEditor) controller).getDrawableForDrawingTreeNode(container);
						if (targetObject instanceof FlexoConceptInstance && virtualModel != null) {
							FlexoConceptInstance targetFlexoConceptInstance = (FlexoConceptInstance) targetObject;
							if (drScheme.isValidTarget(targetFlexoConceptInstance.getFlexoConcept())) {
								returned.add(drScheme);
							}
						}
					}
				}
			}

			return returned;
		}

	}

}
