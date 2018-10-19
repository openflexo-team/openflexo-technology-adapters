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

package org.openflexo.technologyadapter.diagram.controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.openflexo.diana.BackgroundStyle;
import org.openflexo.diana.DianaCoreUtils;
import org.openflexo.diana.DianaUtils;
import org.openflexo.diana.ForegroundStyle;
import org.openflexo.diana.ColorGradientBackgroundStyle.ColorGradientDirection;
import org.openflexo.diana.Drawing.DrawingTreeNode;
import org.openflexo.diana.Drawing.ShapeNode;
import org.openflexo.diana.control.AbstractDianaEditor;
import org.openflexo.diana.control.DianaEditor;
import org.openflexo.diana.cp.ControlArea;
import org.openflexo.diana.geom.DianaArc;
import org.openflexo.diana.geom.DianaCircle;
import org.openflexo.diana.geom.DianaDimension;
import org.openflexo.diana.geom.DianaPoint;
import org.openflexo.diana.geom.DianaRoundRectangle;
import org.openflexo.diana.geom.DianaShape;
import org.openflexo.diana.geom.DianaArc.ArcType;
import org.openflexo.diana.geom.DianaGeometricObject.Filling;
import org.openflexo.diana.geom.DianaGeometricObject.SimplifiedCardinalDirection;
import org.openflexo.diana.geom.area.DianaArea;
import org.openflexo.diana.graphics.DianaGraphics;
import org.openflexo.diana.swing.graphics.JDianaGraphics;
import org.openflexo.diana.swing.paint.DianaPaintManager;
import org.openflexo.diana.swing.view.JDrawingView;
import org.openflexo.diana.swing.view.JShapeView;
import org.openflexo.selection.SelectionManagingDianaEditor;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

public class CircularFloatingPalette extends ControlArea<DianaArea> implements PropertyChangeListener {

	private static final Logger logger = Logger.getLogger(CircularFloatingPalette.class.getPackage().getName());

	private final ShapeNode<DiagramElement<?>> shapeNode;
	private final DiagramElement<?> target;

	private static final BackgroundStyle DEFAULT = DianaCoreUtils.TOOLS_FACTORY.makeColoredBackground(Color.WHITE);
	private static final ForegroundStyle NODE_FOREGROUND = DianaCoreUtils.TOOLS_FACTORY.makeForegroundStyle(Color.BLACK, 1.0f);
	private static final BackgroundStyle NODE_BACKGROUND = DianaCoreUtils.TOOLS_FACTORY.makeColorGradientBackground(Color.DARK_GRAY,
			Color.WHITE, ColorGradientDirection.NORTH_WEST_SOUTH_EAST);

	static {
		DEFAULT.setUseTransparency(true);
		DEFAULT.setTransparencyLevel(0.3f);
		NODE_BACKGROUND.setUseTransparency(true);
		NODE_BACKGROUND.setTransparencyLevel(0.7f);
	}

	public CircularFloatingPalette(ShapeNode<DiagramElement<?>> shapeNode, DiagramElement<?> target,
			SimplifiedCardinalDirection orientation) {
		super(shapeNode, makeRoundRect(shapeNode));
		this.shapeNode = shapeNode;
		this.target = target;
		shapeNode.getPropertyChangeSupport().addPropertyChangeListener(this);
		updateElements(shapeNode);
	}

	@Override
	public boolean isDraggable() {
		return shapeNode.getDrawing().isEditable();
	}

	protected Point currentDraggingLocationInDrawingView = null;
	protected boolean drawEdge = false;
	protected boolean isDnd = false;
	protected ShapeNode<?> to = null;
	protected DrawingTreeNode<?, ?> focusedNode;
	private SelectionManagingDianaEditor<?> controller;
	private DianaPoint normalizedStartPoint;

	private Rectangle previousRectangle;

	public void paint(Graphics g, AbstractDianaEditor<?, ?, ?> controller) {
		if (drawEdge && currentDraggingLocationInDrawingView != null) {
			DianaShape<?> dianaShape = shapeNode.getDianaShapeOutline();
			// Unused DrawingGraphicalRepresentation drawingGR = controller.getDrawing().getRoot().getGraphicalRepresentation();
			double scale = controller.getScale();
			DianaPoint nearestOnOutline = dianaShape.getNearestPoint(controller.getDrawing().getRoot()
					.convertLocalViewCoordinatesToRemoteNormalizedPoint(currentDraggingLocationInDrawingView, shapeNode, scale));
			/*nodeGR.convertLocalNormalizedPointToRemoteViewCoordinates(this.normalizedStartPoint, controller.getDrawingGraphicalRepresentation(), controller.getScale())*/
			Point fromPoint = shapeNode.convertLocalNormalizedPointToRemoteViewCoordinates(nearestOnOutline,
					controller.getDrawing().getRoot(), scale);
			Point toPoint = currentDraggingLocationInDrawingView;

			g.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);
			int x, y, w, h;
			if (fromPoint.x >= toPoint.x) {
				x = toPoint.x;
				w = fromPoint.x - toPoint.x;
			}
			else {
				x = fromPoint.x;
				w = toPoint.x - fromPoint.x;
			}
			if (fromPoint.y >= toPoint.y) {
				y = toPoint.y;
				h = fromPoint.y - toPoint.y;
			}
			else {
				y = fromPoint.y;
				h = toPoint.y - fromPoint.y;
			}
			previousRectangle = new Rectangle(x, y, w, h);
		}
	}

	@Override
	public void startDragging(DianaEditor<?> controller, DianaPoint startPoint) {

	}

	@Override
	public boolean dragToPoint(DianaPoint newRelativePoint, DianaPoint pointRelativeToInitialConfiguration, DianaPoint newAbsolutePoint,
			DianaPoint initialPoint, MouseEvent event) {
		if (drawEdge) {
			JDrawingView<?> drawingView = controller.getDrawingView();
			DianaPaintManager paintManager = drawingView.getPaintManager();
			// Attempt to repaint relevant zone only
			Rectangle oldBounds = previousRectangle;
			if (oldBounds != null) {
				oldBounds.x -= 1;
				oldBounds.y -= 1;
				oldBounds.width += 2;
				oldBounds.height += 2;
			}
			focusedNode = drawingView.getFocusRetriever().getFocusedObject(event);
			if (focusedNode instanceof ShapeNode && focusedNode != shapeNode) {
				to = (ShapeNode<?>) focusedNode;
			}
			else {
				to = null;
			}

			currentDraggingLocationInDrawingView = SwingUtilities.convertPoint((Component) event.getSource(), event.getPoint(),
					drawingView);
			if (!isDnd) {
				isDnd = shapeNode.convertLocalNormalizedPointToRemoteViewCoordinates(normalizedStartPoint,
						controller.getDrawing().getRoot(), controller.getScale()).distance(currentDraggingLocationInDrawingView) > 5;
			}

			// Attempt to repaint relevant zone only
			Rectangle newBounds = getBoundsToRepaint(drawingView);
			Rectangle boundsToRepaint;
			if (oldBounds != null) {
				boundsToRepaint = oldBounds.union(newBounds);
			}
			else {
				boundsToRepaint = newBounds;
			}
			paintManager.repaint(drawingView, boundsToRepaint);

			// Alternative @brutal zone
			// paintManager.repaint(drawingView);

			return true;
		}
		return false;
	}

	// Attempt to repaint relevant zone only
	private Rectangle getBoundsToRepaint(JDrawingView<?> drawingView) {
		JShapeView<?> fromView = drawingView.shapeViewForNode(shapeNode);
		Rectangle fromViewBounds = SwingUtilities.convertRectangle(fromView, fromView.getBounds(), drawingView);
		Rectangle boundsToRepaint = fromViewBounds;

		if (to != null) {
			JShapeView<?> toView = drawingView.shapeViewForNode(to);
			Rectangle toViewBounds = SwingUtilities.convertRectangle(toView, toView.getBounds(), drawingView);
			boundsToRepaint = fromViewBounds.union(toViewBounds);
		}

		if (currentDraggingLocationInDrawingView != null) {
			Rectangle lastLocationBounds = new Rectangle(currentDraggingLocationInDrawingView);
			boundsToRepaint = fromViewBounds.union(lastLocationBounds);
		}

		// logger.fine("boundsToRepaint="+boundsToRepaint);

		return boundsToRepaint;
	}

	@Override
	public void stopDragging(DianaEditor<?> controller, DrawingTreeNode<?, ?> focused) {
		if (drawEdge && currentDraggingLocationInDrawingView != null && isDnd) {
			try {
				DrawingTreeNode<?, ?> targetNode = controller.getDrawing().getDrawingTreeNode(target);

				if (targetNode == null) {
					targetNode = controller.getDrawing().getRoot();
				}
				if (focusedNode == null) {
					focusedNode = controller.getDrawing().getRoot();
				}
				// Unused SimplifiedCardinalDirection direction = DianaPoint.getSimplifiedOrientation(
				// Unused new DianaPoint(shapeNode.convertLocalNormalizedPointToRemoteViewCoordinates(this.normalizedStartPoint,
				// Unused controller.getDrawing().getRoot(), controller.getScale())),
				// Unused new DianaPoint(currentDraggingLocationInDrawingView));
				Point dropPoint = currentDraggingLocationInDrawingView;
				if (dropPoint.x < 0) {
					dropPoint.x = 0;
				}
				if (dropPoint.y < 0) {
					dropPoint.y = 0;
				}

				// Unused Point p = DianaUtils.convertPoint(controller.getDrawing().getRoot(), dropPoint, focusedNode, controller.getScale());
				// Unused DianaPoint dropLocation = new DianaPoint(p.x / controller.getScale(), p.y / controller.getScale());
			} finally {
				// ((DiagramView) controller.getDrawingView()).resetFloatingPalette();
				JDrawingView<?> drawingView = this.controller.getDrawingView();
				DianaPaintManager paintManager = drawingView.getPaintManager();
				paintManager.invalidate(drawingView.getDrawing().getRoot());
				paintManager.repaint(drawingView.getDrawing().getRoot());
			}
		}
		else {
		}
		super.stopDragging(controller, focusedNode);
	}

	private static DiagramShape createNewShape(DianaPoint dropLocation, DiagramElement<?> container, DropScheme dropScheme) {

		return null;

		// TODO: reimplement this

		/*DropSchemeAction dropSchemeAction = DropSchemeAction.actionType.makeNewAction(container, null, controller.getSelectionManager()
				.getController().getEditor());
		dropSchemeAction.setDropScheme(dropScheme);
		dropSchemeAction.escapeParameterRetrievingWhenValid = true;
		dropSchemeAction.doAction();
		
		if (dropSchemeAction.getPrimaryShape() != null) {
		
			DrawingTreeNode<?, ?> targetGR = controller.getDrawing().getDrawingTreeNode(target);
		
			ShapeNode<?> shapeNode = controller.getDrawing().getShapeNode(dropSchemeAction.getPrimaryShape());
			ShapeGraphicalRepresentation gr = shapeNode.getGraphicalRepresentation();
		
			double xOffset = 0;
			double yOffset = 0;
			if (gr != null) {
				if (gr.getBorder() != null) {
					xOffset -= gr.getBorder().getLeft();
					yOffset -= gr.getBorder().getTop();
				}
				xOffset -= gr.getWidth() / 2;
				yOffset -= gr.getHeight() / 2;
				gr.setX(dropLocation.x + xOffset);
				gr.setY(dropLocation.y + yOffset);
			}
		
		}
		return dropSchemeAction.getPrimaryShape();*/
	}

	@Override
	public Rectangle paint(DianaGraphics drawingGraphics) {
		// System.out.println("Focused:"+nodeGR.getIsFocused());
		if (shapeNode.getIsSelected() && !shapeNode.getIsFocused()) {
			return null;
		}
		if (/*nodeGR.getIsSelected() ||*/shapeNode.isResizing() || shapeNode.isMoving()) {
			return null;
		}
		if (!shapeNode.getDrawing().isEditable()) {
			return null;
		}
		AffineTransform at = DianaUtils.convertNormalizedCoordinatesAT(shapeNode, drawingGraphics.getDrawingTreeNode());

		Graphics2D oldGraphics = ((JDianaGraphics) drawingGraphics).cloneGraphics();

		// Unused DianaRoundRectangle paletteRect = (DianaRoundRectangle)
		getArea().transform(at);
		DianaArea nodeRect = makeBaseRoundedArc(shapeNode, 1, 5);

		// paletteRect.paint(drawingGraphics);

		// 1. Node
		drawingGraphics.setDefaultForeground(NODE_FOREGROUND);
		drawingGraphics.setDefaultBackground(NODE_BACKGROUND);
		nodeRect.paint(drawingGraphics);

		((JDianaGraphics) drawingGraphics).releaseClonedGraphics(oldGraphics);
		return drawingGraphics.getDrawingTreeNode().convertNormalizedRectangleToViewCoordinates(nodeRect.getEmbeddingBounds(),
				drawingGraphics.getScale());

	}

	@Override
	public boolean isClickable() {
		return false;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == shapeNode) {
			updateElements(shapeNode);
		}
	}

	private void updateElements(ShapeNode<DiagramElement<?>> shapeNode) {
		setArea(makeRoundRect(shapeNode));
		// Unused DianaArea roleRect =
		makeBaseRoundedArc(shapeNode, 1, 5);
	}

	private static DianaArea makeBaseRoundedArc(ShapeNode<DiagramElement<?>> shapeNode, double id, double arcNumber) {

		DianaCircle centerShape = new DianaCircle(new DianaPoint(shapeNode.getX() + 50, shapeNode.getY() + 50), shapeNode.getWidth() / 3,
				Filling.FILLED);
		DianaArc arc = new DianaArc(centerShape.getCenter(), new DianaDimension(centerShape.getHeight(), centerShape.getWidth()), id,
				360 / arcNumber - 5, ArcType.PIE);
		DianaArea area = arc.substract(centerShape, true);
		return area;
	}

	private static DianaRoundRectangle makeRoundRect(ShapeNode<DiagramElement<?>> shapeNode) {

		return new DianaRoundRectangle(shapeNode.getX(), shapeNode.getY(), shapeNode.getWidth() + 50, shapeNode.getWidth() + 50, 13.0, 13.0,
				Filling.FILLED);

	}
}
