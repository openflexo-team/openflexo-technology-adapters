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

import org.openflexo.fge.BackgroundStyle;
import org.openflexo.fge.ColorGradientBackgroundStyle.ColorGradientDirection;
import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.Drawing.ShapeNode;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.FGECoreUtils;
import org.openflexo.fge.FGEUtils;
import org.openflexo.fge.ForegroundStyle;
import org.openflexo.fge.control.AbstractDianaEditor;
import org.openflexo.fge.control.DianaEditor;
import org.openflexo.fge.cp.ControlArea;
import org.openflexo.fge.geom.FGEArc;
import org.openflexo.fge.geom.FGEArc.ArcType;
import org.openflexo.fge.geom.FGECircle;
import org.openflexo.fge.geom.FGEDimension;
import org.openflexo.fge.geom.FGEGeometricObject.Filling;
import org.openflexo.fge.geom.FGEGeometricObject.SimplifiedCardinalDirection;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.geom.FGERoundRectangle;
import org.openflexo.fge.geom.FGEShape;
import org.openflexo.fge.geom.area.FGEArea;
import org.openflexo.fge.graphics.FGEGraphics;
import org.openflexo.fge.swing.graphics.JFGEGraphics;
import org.openflexo.fge.swing.paint.FGEPaintManager;
import org.openflexo.fge.swing.view.JDrawingView;
import org.openflexo.fge.swing.view.JShapeView;
import org.openflexo.selection.SelectionManagingDianaEditor;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

public class CircularFloatingPalette extends ControlArea<FGEArea> implements PropertyChangeListener {

	private static final Logger logger = Logger.getLogger(CircularFloatingPalette.class.getPackage().getName());

	private final ShapeNode<DiagramElement<?>> shapeNode;
	private final DiagramElement<?> target;

	private static final BackgroundStyle DEFAULT = FGECoreUtils.TOOLS_FACTORY.makeColoredBackground(Color.WHITE);
	private static final ForegroundStyle NODE_FOREGROUND = FGECoreUtils.TOOLS_FACTORY.makeForegroundStyle(Color.BLACK, 1.0f);
	private static final BackgroundStyle NODE_BACKGROUND = FGECoreUtils.TOOLS_FACTORY.makeColorGradientBackground(Color.DARK_GRAY,
			Color.WHITE, ColorGradientDirection.SOUTH_EAST_NORTH_WEST);

	static {
		DEFAULT.setUseTransparency(true);
		DEFAULT.setTransparencyLevel(0.3f);
		NODE_BACKGROUND.setUseTransparency(true);
		NODE_BACKGROUND.setTransparencyLevel(0.7f);
	}

	public CircularFloatingPalette(ShapeNode<DiagramElement<?>> shapeNode, DiagramElement<?> target, SimplifiedCardinalDirection orientation) {
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
	private FGEPoint normalizedStartPoint;

	private Rectangle previousRectangle;

	public void paint(Graphics g, AbstractDianaEditor<?, ?, ?> controller) {
		if (drawEdge && currentDraggingLocationInDrawingView != null) {
			FGEShape<?> fgeShape = shapeNode.getFGEShapeOutline();
			DrawingGraphicalRepresentation drawingGR = controller.getDrawing().getRoot().getGraphicalRepresentation();
			double scale = controller.getScale();
			FGEPoint nearestOnOutline = fgeShape.getNearestPoint(controller.getDrawing().getRoot()
					.convertLocalViewCoordinatesToRemoteNormalizedPoint(currentDraggingLocationInDrawingView, shapeNode, scale));
			/*nodeGR.convertLocalNormalizedPointToRemoteViewCoordinates(this.normalizedStartPoint, controller.getDrawingGraphicalRepresentation(), controller.getScale())*/
			Point fromPoint = shapeNode.convertLocalNormalizedPointToRemoteViewCoordinates(nearestOnOutline, controller.getDrawing()
					.getRoot(), scale);
			Point toPoint = currentDraggingLocationInDrawingView;

			g.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);
			int x, y, w, h;
			if (fromPoint.x >= toPoint.x) {
				x = toPoint.x;
				w = fromPoint.x - toPoint.x;
			} else {
				x = fromPoint.x;
				w = toPoint.x - fromPoint.x;
			}
			if (fromPoint.y >= toPoint.y) {
				y = toPoint.y;
				h = fromPoint.y - toPoint.y;
			} else {
				y = fromPoint.y;
				h = toPoint.y - fromPoint.y;
			}
			previousRectangle = new Rectangle(x, y, w, h);
		}
	}

	@Override
	public void startDragging(DianaEditor<?> controller, FGEPoint startPoint) {

	}

	@Override
	public boolean dragToPoint(FGEPoint newRelativePoint, FGEPoint pointRelativeToInitialConfiguration, FGEPoint newAbsolutePoint,
			FGEPoint initialPoint, MouseEvent event) {
		if (drawEdge) {
			JDrawingView<?> drawingView = controller.getDrawingView();
			FGEPaintManager paintManager = drawingView.getPaintManager();
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
			} else {
				to = null;
			}

			currentDraggingLocationInDrawingView = SwingUtilities
					.convertPoint((Component) event.getSource(), event.getPoint(), drawingView);
			if (!isDnd) {
				isDnd = shapeNode.convertLocalNormalizedPointToRemoteViewCoordinates(normalizedStartPoint,
						controller.getDrawing().getRoot(), controller.getScale()).distance(currentDraggingLocationInDrawingView) > 5;
			}

			// Attempt to repaint relevant zone only
			Rectangle newBounds = getBoundsToRepaint(drawingView);
			Rectangle boundsToRepaint;
			if (oldBounds != null) {
				boundsToRepaint = oldBounds.union(newBounds);
			} else {
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
				SimplifiedCardinalDirection direction = FGEPoint.getSimplifiedOrientation(
						new FGEPoint(shapeNode.convertLocalNormalizedPointToRemoteViewCoordinates(this.normalizedStartPoint, controller
								.getDrawing().getRoot(), controller.getScale())), new FGEPoint(currentDraggingLocationInDrawingView));
				Point dropPoint = currentDraggingLocationInDrawingView;
				if (dropPoint.x < 0) {
					dropPoint.x = 0;
				}
				if (dropPoint.y < 0) {
					dropPoint.y = 0;
				}

				Point p = FGEUtils.convertPoint(controller.getDrawing().getRoot(), dropPoint, focusedNode, controller.getScale());
				FGEPoint dropLocation = new FGEPoint(p.x / controller.getScale(), p.y / controller.getScale());
				DiagramShape to = null;

			} finally {
				// ((DiagramView) controller.getDrawingView()).resetFloatingPalette();
				JDrawingView<?> drawingView = this.controller.getDrawingView();
				FGEPaintManager paintManager = drawingView.getPaintManager();
				paintManager.invalidate(drawingView.getDrawing().getRoot());
				paintManager.repaint(drawingView.getDrawing().getRoot());
			}
		} else {
		}
		super.stopDragging(controller, focusedNode);
	}

	private DiagramShape createNewShape(FGEPoint dropLocation, DiagramElement<?> container, DropScheme dropScheme) {

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
	public Rectangle paint(FGEGraphics drawingGraphics) {
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
		AffineTransform at = FGEUtils.convertNormalizedCoordinatesAT(shapeNode, drawingGraphics.getDrawingTreeNode());

		Graphics2D oldGraphics = ((JFGEGraphics) drawingGraphics).cloneGraphics();

		FGERoundRectangle paletteRect = (FGERoundRectangle) getArea().transform(at);
		FGEArea nodeRect = makeBaseRoundedArc(shapeNode, 1, 5);

		// paletteRect.paint(drawingGraphics);

		// 1. Node
		drawingGraphics.setDefaultForeground(NODE_FOREGROUND);
		drawingGraphics.setDefaultBackground(NODE_BACKGROUND);
		nodeRect.paint(drawingGraphics);

		((JFGEGraphics) drawingGraphics).releaseClonedGraphics(oldGraphics);
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
		FGEArea roleRect = makeBaseRoundedArc(shapeNode, 1, 5);
	}

	private static FGEArea makeBaseRoundedArc(ShapeNode<DiagramElement<?>> shapeNode, double id, double arcNumber) {

		FGECircle centerShape = new FGECircle(new FGEPoint(shapeNode.getX() + 50, shapeNode.getY() + 50), shapeNode.getWidth() / 3,
				Filling.FILLED);
		FGEArc arc = new FGEArc(centerShape.getCenter(), new FGEDimension(centerShape.getHeight(), centerShape.getWidth()), id,
				360 / arcNumber - 5, ArcType.PIE);
		FGEArea area = arc.substract(centerShape, true);
		return area;
	}

	private static FGERoundRectangle makeRoundRect(ShapeNode<DiagramElement<?>> shapeNode) {

		return new FGERoundRectangle(shapeNode.getX(), shapeNode.getY(), shapeNode.getWidth() + 50, shapeNode.getWidth() + 50, 13.0, 13.0,
				Filling.FILLED);

	}
}
