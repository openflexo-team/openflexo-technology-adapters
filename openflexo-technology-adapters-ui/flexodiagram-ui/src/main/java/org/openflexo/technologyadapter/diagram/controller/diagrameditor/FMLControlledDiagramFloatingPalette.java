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
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.openflexo.fge.BackgroundStyle;
import org.openflexo.fge.ColorGradientBackgroundStyle.ColorGradientDirection;
import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.Drawing.RootNode;
import org.openflexo.fge.Drawing.ShapeNode;
import org.openflexo.fge.FGECoreUtils;
import org.openflexo.fge.FGEUtils;
import org.openflexo.fge.ForegroundStyle;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.control.DianaEditor;
import org.openflexo.fge.cp.ControlArea;
import org.openflexo.fge.geom.FGEGeometricObject.Filling;
import org.openflexo.fge.geom.FGEGeometricObject.SimplifiedCardinalDirection;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.geom.FGERectangle;
import org.openflexo.fge.geom.FGERoundRectangle;
import org.openflexo.fge.geom.FGEShape;
import org.openflexo.fge.graphics.FGEGraphics;
import org.openflexo.fge.swing.paint.FGEPaintManager;
import org.openflexo.fge.swing.view.JShapeView;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramShape.DropAndLinkScheme;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.AddConnector;
import org.openflexo.technologyadapter.diagram.model.action.DropSchemeAction;
import org.openflexo.technologyadapter.diagram.model.action.LinkSchemeAction;

/**
 * Represents a floating palette associated to a {@link FMLControlledDiagramShape} and a cardinal orientation.<br>
 * From a technical point of view, this floating palette is a {@link ControlArea} allowing to trigger LinkScheme(s) and a combination of a
 * DropScheme with a LinkScheme
 * 
 * @author sylvain
 *
 */
public class FMLControlledDiagramFloatingPalette extends ControlArea<FGERoundRectangle> implements PropertyChangeListener {

	private static final Logger logger = Logger.getLogger(FMLControlledDiagramFloatingPalette.class.getPackage().getName());

	protected static final Color OK = new Color(0, 191, 0);

	private enum Mode {
		CREATE_SHAPE_AND_LINK, LINK_ONLY;
	}

	private FGERoundRectangle roleRect;
	private FGERectangle edgeRect;

	/** The vertical space between two elements of the palette */
	private static final int SPACING = 5;
	/** The height of an element of the palette */
	private static final int ELEMENTS_HEIGHT = 8;
	/** The width of an element of the palette */
	private static final int ELEMENTS_WIDTH = 12;

	private static final ForegroundStyle NONE = FGECoreUtils.TOOLS_FACTORY.makeNoneForegroundStyle();
	private static final BackgroundStyle DEFAULT = FGECoreUtils.TOOLS_FACTORY.makeColoredBackground(Color.WHITE);
	private static final ForegroundStyle NODE_FOREGROUND = FGECoreUtils.TOOLS_FACTORY.makeForegroundStyle(Color.RED, 1.0f);
	private static final ForegroundStyle EDGE_FOREGROUND = FGECoreUtils.TOOLS_FACTORY.makeForegroundStyle(FGEUtils.NICE_BROWN, 1.0f);
	private static final BackgroundStyle NODE_BACKGROUND = FGECoreUtils.TOOLS_FACTORY.makeColorGradientBackground(Color.ORANGE,
			Color.WHITE, ColorGradientDirection.SOUTH_EAST_NORTH_WEST);

	static {
		DEFAULT.setUseTransparency(true);
		DEFAULT.setTransparencyLevel(0.3f);
		NODE_BACKGROUND.setUseTransparency(true);
		NODE_BACKGROUND.setTransparencyLevel(0.7f);
	}

	private final SimplifiedCardinalDirection orientation;

	protected Point currentDraggingLocationInDrawingView = null;
	protected boolean drawEdge = false;
	protected boolean isDnd = false;

	protected ShapeNode<?> to = null;
	protected DrawingTreeNode<?, ?> focusedNode;
	private FMLControlledDiagramEditor controller;
	private FGEPoint normalizedStartPoint;

	private Rectangle previousRectangle;
	private Mode mode;

	public FMLControlledDiagramFloatingPalette(ShapeNode<FMLControlledDiagramShape> node, SimplifiedCardinalDirection orientation) {
		super(node, makeRoundRect(node, orientation));
		this.orientation = orientation;
		node.getPropertyChangeSupport().addPropertyChangeListener(this);

		updateElements(orientation);
	}

	@Override
	public ShapeNode<FMLControlledDiagramShape> getNode() {
		return (ShapeNode<FMLControlledDiagramShape>) super.getNode();
	}

	@Override
	public boolean isDraggable() {
		return getNode().getDrawing().isEditable();
	}

	public void paint(Graphics g, DiagramEditor controller) {
		if (drawEdge && currentDraggingLocationInDrawingView != null) {
			FGEShape<?> fgeShape = getNode().getShape().getOutline();
			RootNode<Diagram> rootNode = controller.getDrawing().getRoot();
			// DrawingGraphicalRepresentation<?> drawingGR = controller.getDrawingGraphicalRepresentation();
			double scale = controller.getScale();
			FGEPoint nearestOnOutline = fgeShape.getNearestPoint(rootNode.convertLocalViewCoordinatesToRemoteNormalizedPoint(
					currentDraggingLocationInDrawingView, getNode(), scale));
			/*nodeGR.convertLocalNormalizedPointToRemoteViewCoordinates(this.normalizedStartPoint, controller.getDrawingGraphicalRepresentation(), controller.getScale())*/
			Point fromPoint = getNode().convertLocalNormalizedPointToRemoteViewCoordinates(nearestOnOutline, rootNode, scale);
			Point toPoint = currentDraggingLocationInDrawingView;

			if (mode == Mode.LINK_ONLY) {
				if (to != null && isDnd) {
					// toPoint = drawingGR.convertRemoteNormalizedPointToLocalViewCoordinates(to.getShape().getShape().getCenter(), to,
					// scale);
					g.setColor(OK);
				} else {
					g.setColor(Color.RED);
				}
				g.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);

			} else {
				if (isDnd) {
					g.setColor(OK);
				} else {
					g.setColor(Color.RED);
				}
				Rectangle rect = new Rectangle(toPoint.x - 10, toPoint.y - 10, 20, 20);
				g.drawRect(rect.x, rect.y, rect.width, rect.height);
				FGERectangle r = new FGERectangle(rect);
				FGEPoint outlineToPoint = r.nearestOutlinePoint(new FGEPoint(fromPoint.x, fromPoint.y));
				g.drawLine(fromPoint.x, fromPoint.y, (int) outlineToPoint.x, (int) outlineToPoint.y);
			}
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
			previousRectangle = new Rectangle(x - 11, y - 11, w + 22, h + 22);
		}
	}

	@Override
	public void startDragging(DianaEditor<?> controller, FGEPoint startPoint) {
		mode = null;
		if (roleRect.contains(startPoint)) {
			mode = Mode.CREATE_SHAPE_AND_LINK;
		} else if (edgeRect.contains(startPoint)) {
			mode = Mode.LINK_ONLY;
		}
		if (mode != null) {
			drawEdge = true;
			normalizedStartPoint = startPoint;
			this.controller = (FMLControlledDiagramEditor) controller;
			this.controller.getDrawingView().setFloatingPalette(this);
		} else {
			drawEdge = false;
		}
	}

	@Override
	public boolean dragToPoint(FGEPoint newRelativePoint, FGEPoint pointRelativeToInitialConfiguration, FGEPoint newAbsolutePoint,
			FGEPoint initialPoint, MouseEvent event) {
		if (drawEdge) {
			DiagramView drawingView = controller.getDrawingView();
			FGEPaintManager paintManager = drawingView.getPaintManager();
			// Attempt to repaint relevant zone only
			Rectangle oldBounds = previousRectangle;
			if (oldBounds != null) {
				oldBounds.x -= 1;
				oldBounds.y -= 1;
				oldBounds.width += 2;
				oldBounds.height += 2;
			}
			focusedNode = controller.getDrawingView().getFocusRetriever().getFocusedObject(event);
			if (focusedNode instanceof ShapeNode && focusedNode != getNode()) {
				to = (ShapeNode<?>) focusedNode;
			} else {
				to = null;
			}

			currentDraggingLocationInDrawingView = SwingUtilities.convertPoint((Component) event.getSource(), event.getPoint(),
					controller.getDrawingView());
			if (!isDnd) {
				isDnd = getNode().convertLocalNormalizedPointToRemoteViewCoordinates(normalizedStartPoint,
						getNode().getDrawing().getRoot(), controller.getScale()).distance(currentDraggingLocationInDrawingView) > 5;
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
	private Rectangle getBoundsToRepaint(DiagramView drawingView) {
		JShapeView<?> fromView = drawingView.shapeViewForNode(getNode());
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
				DrawingTreeNode<?, ?> targetNode = getNode().getParentNode();
				if (focused == null) {
					focused = getNode().getDrawing().getRoot();
				}
				SimplifiedCardinalDirection direction = FGEPoint.getSimplifiedOrientation(
						new FGEPoint(getNode().convertLocalNormalizedPointToRemoteViewCoordinates(this.normalizedStartPoint,
								getNode().getDrawing().getRoot(), controller.getScale())), new FGEPoint(
								currentDraggingLocationInDrawingView));
				Point dropPoint = currentDraggingLocationInDrawingView;
				if (dropPoint.x < 0) {
					dropPoint.x = 0;
				}
				if (dropPoint.y < 0) {
					dropPoint.y = 0;
				}

				Point p = FGEUtils.convertPoint(getNode().getDrawing().getRoot(), dropPoint, focused, controller.getScale());
				FGEPoint dropLocation = new FGEPoint(p.x / controller.getScale(), p.y / controller.getScale());
				// ShapeNode<?> to = null;

				switch (mode) {
				case CREATE_SHAPE_AND_LINK:
					askAndApplyDropAndLinkScheme(dropLocation, focused);
					break;
				case LINK_ONLY:
					if (to != null) {
						// to = this.to.getDrawable();
						askAndApplyLinkScheme(dropLocation, to);
					}
					break;
				default:
					logger.warning("Not implemented !!!");
					break;
				}
				if (to == null) {
					return;
				}

			} finally {
				resetVariables();
				DiagramView diagramView = ((DiagramEditor) controller).getDrawingView();
				diagramView.resetFloatingPalette();
				FGEPaintManager paintManager = diagramView.getPaintManager();
				paintManager.invalidate(diagramView.getDrawing().getRoot());
				paintManager.repaint(diagramView.getDrawing().getRoot());
			}
		} else {
			resetVariables();
		}
		super.stopDragging(controller, focusedNode);
	}

	private void askAndApplyDropAndLinkScheme(final FGEPoint dropLocation, DrawingTreeNode<?, ?> focused) {

		FlexoConceptInstance parentFlexoConceptInstance = null;
		ShapeRole parentShapeRole = null;

		if (focused.getDrawable() instanceof FMLControlledDiagramElement) {
			parentFlexoConceptInstance = ((FMLControlledDiagramElement<?, ?>) focused.getDrawable()).getFlexoConceptInstance();
		}
		if (focused.getDrawable() instanceof FMLControlledDiagramShape) {
			parentShapeRole = (ShapeRole) ((FMLControlledDiagramShape) focused.getDrawable()).getRole();
		}

		if (parentFlexoConceptInstance == null || parentShapeRole == null) {
			return;
		}

		/*if (focused.getDrawable() instanceof FMLControlledDiagramShape) {
			container = ((FMLControlledDiagramShape) focused.getDrawable()).getDiagramElement();
			containerConcept = ((FMLControlledDiagramShape) focused.getDrawable()).getFlexoConceptInstance().getFlexoConcept();
		} else if (focused.getDrawable() instanceof Diagram) {
			container = (Diagram) focused.getDrawable();
			containerConcept = null;
		}

		if (container == null) {
			return;
		}*/

		List<DropAndLinkScheme> allDropAndLinkSchemes = getNode().getDrawable().getAvailableDropAndLinkSchemes(
				parentFlexoConceptInstance.getFlexoConcept());

		if (allDropAndLinkSchemes.size() == 0) {
			return;
		}

		else if (allDropAndLinkSchemes.size() == 1) {
			applyDropAndLinkScheme(allDropAndLinkSchemes.get(0), dropLocation, parentFlexoConceptInstance, parentShapeRole);
		}

		else {
			JPopupMenu popup = new JPopupMenu();
			for (final DropAndLinkScheme dropAndLinkScheme : allDropAndLinkSchemes) {
				LocalizedDelegate localizedDictionary = dropAndLinkScheme.linkScheme.getViewPoint().getLocalizedDictionary();
				String linkLabel = dropAndLinkScheme.linkScheme.getLabel() != null ? dropAndLinkScheme.linkScheme.getLabel()
						: dropAndLinkScheme.linkScheme.getName();
				String localizedLinkLabel = localizedDictionary.getLocalizedForKeyAndLanguage(linkLabel,
						FlexoLocalization.getCurrentLanguage());
				if (localizedLinkLabel == null) {
					localizedLinkLabel = FlexoLocalization.localizedForKey(linkLabel);
				}
				String dropLabel = dropAndLinkScheme.dropScheme.getFlexoConcept().getName();
				String localizedDropLabel = localizedDictionary.getLocalizedForKeyAndLanguage(dropLabel,
						FlexoLocalization.getCurrentLanguage());
				if (localizedDropLabel == null) {
					localizedDropLabel = FlexoLocalization.localizedForKey(dropLabel);
				}
				String withNew = FlexoLocalization.localizedForKey("with_new");
				JMenuItem menuItem = new JMenuItem(localizedLinkLabel + " " + withNew + " " + localizedDropLabel);
				// final DiagramContainerElement<?> finalContainer = container;
				final FlexoConceptInstance finalParentFlexoConceptInstance = parentFlexoConceptInstance;
				final ShapeRole finalParentShapeRole = parentShapeRole;

				menuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						applyDropAndLinkScheme(dropAndLinkScheme, dropLocation, finalParentFlexoConceptInstance, finalParentShapeRole);
					}
				});
				menuItem.setToolTipText(dropAndLinkScheme.dropScheme.getDescription());
				popup.add(menuItem);
			}
			popup.show((Component) controller.getDrawingView().viewForNode(getNode().getParentNode()), (int) dropLocation.x,
					(int) dropLocation.y);

		}
	}

	private void askAndApplyLinkScheme(final FGEPoint dropLocation, final ShapeNode<?> to) {

		if (to.getDrawable() instanceof FMLControlledDiagramShape) {
			final FMLControlledDiagramShape toShape = (FMLControlledDiagramShape) to.getDrawable();
			List<LinkScheme> availableLinkSchemes = getNode().getDrawable().getAvailableLinkSchemes(
					toShape.getFlexoConceptInstance().getFlexoConcept());

			if (availableLinkSchemes.size() == 1) {
				LinkSchemeAction action = LinkSchemeAction.actionType.makeNewAction(getNode().getDrawable().getFlexoConceptInstance()
						.getVirtualModelInstance(), null, controller.getFlexoController().getEditor());
				action.setLinkScheme(availableLinkSchemes.get(0));
				action.setFromShape(getNode().getDrawable().getDiagramElement());
				action.setToShape(toShape.getDiagramElement());
				action.escapeParameterRetrievingWhenValid = true;
				action.doAction();
			} else if (availableLinkSchemes.size() > 1) {
				JPopupMenu popup = new JPopupMenu();
				for (final LinkScheme linkScheme : availableLinkSchemes) {
					// final CalcPaletteConnector connector = availableConnectors.get(linkScheme);
					// System.out.println("Available: "+paletteConnector.getEditionPattern().getName());
					LocalizedDelegate localizedDictionary = linkScheme.getViewPoint().getLocalizedDictionary();
					String label = linkScheme.getLabel() != null ? linkScheme.getLabel() : linkScheme.getName();
					String localized = localizedDictionary.getLocalizedForKeyAndLanguage(label, FlexoLocalization.getCurrentLanguage());
					if (localized == null) {
						localized = FlexoLocalization.localizedForKey(label);
					}
					JMenuItem menuItem = new JMenuItem(localized);
					menuItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							// System.out.println("Action "+paletteConnector.getEditionPattern().getName());
							LinkSchemeAction action = LinkSchemeAction.actionType
									.makeNewAction(getNode().getDrawable().getFlexoConceptInstance().getVirtualModelInstance(), null,
											controller.getFlexoController().getEditor());
							action.setLinkScheme(linkScheme);
							action.setFromShape(getNode().getDrawable().getDiagramElement());
							action.setToShape(toShape.getDiagramElement());
							action.escapeParameterRetrievingWhenValid = true;
							action.doAction();
						}
					});
					menuItem.setToolTipText(linkScheme.getDescription());
					popup.add(menuItem);
				}
				popup.show((Component) controller.getDrawingView().viewForNode(getNode().getParentNode()), (int) dropLocation.x,
						(int) dropLocation.y);
			}
		} else if (to.getDrawable() instanceof DiagramShape) {
			// Graphical connector only
			AddConnector action = AddConnector.actionType.makeNewAction(getNode().getDrawable().getDiagramElement(), null, controller
					.getFlexoController().getEditor());
			action.setToShape((DiagramShape) to.getDrawable());
			action.doAction();
		}

	}

	protected void applyDropAndLinkScheme(final DropAndLinkScheme dropAndLinkScheme, final FGEPoint dropLocation,
	/*DiagramContainerElement<?> container*/FlexoConceptInstance parentFlexoConceptInstance, ShapeRole parentShapeRole) {
		applyDropAndLinkScheme(dropAndLinkScheme.dropScheme, dropAndLinkScheme.linkScheme, dropLocation, parentFlexoConceptInstance,
				parentShapeRole);
	}

	protected void applyDropAndLinkScheme(DropScheme dropScheme, LinkScheme linkScheme, FGEPoint dropLocation,
	/*DiagramContainerElement<?> container*/FlexoConceptInstance parentFlexoConceptInstance, ShapeRole parentShapeRole) {

		logger.info("applyDropAndLinkScheme dropScheme=" + dropScheme + " linkScheme=" + linkScheme + " parentFlexoConceptInstance="
				+ parentFlexoConceptInstance + " parentShapeRole=" + parentShapeRole);

		FMLControlledDiagramShape newShape = createNewShape(dropLocation, parentFlexoConceptInstance, parentShapeRole, dropScheme);

		if (newShape != null) {
			createNewConnector(getNode().getDrawable(), newShape, linkScheme);
			controller.setSelectedObject(controller.getDrawing().getDrawingTreeNode(newShape));
		}
	}

	private void resetVariables() {
		drawEdge = false;
		isDnd = false;
		to = null;
		currentDraggingLocationInDrawingView = null;
	}

	private FMLControlledDiagramShape createNewShape(FGEPoint dropLocation, /*DiagramContainerElement<?> container*/
			FlexoConceptInstance parentFlexoConceptInstance, ShapeRole parentShapeRole, DropScheme dropScheme) {

		DropSchemeAction dropSchemeAction = DropSchemeAction.actionType.makeNewAction(getNode().getDrawable().getFlexoConceptInstance()
				.getVirtualModelInstance(), null, controller.getFlexoController().getEditor());
		dropSchemeAction.setDropScheme(dropScheme);
		// dropSchemeAction.setParent(container);
		dropSchemeAction.setParentInformations(parentFlexoConceptInstance, parentShapeRole);
		dropSchemeAction.setDropLocation(dropLocation);
		dropSchemeAction.escapeParameterRetrievingWhenValid = true;
		dropSchemeAction.doAction();

		return controller.getDrawing().getFederatedShape(dropSchemeAction.getPrimaryShape());

	}

	private FMLControlledDiagramConnector createNewConnector(FMLControlledDiagramShape from, FMLControlledDiagramShape to,
			LinkScheme linkScheme) {

		LinkSchemeAction action = LinkSchemeAction.actionType.makeNewAction(getNode().getDrawable().getFlexoConceptInstance()
				.getVirtualModelInstance(), null, controller.getFlexoController().getEditor());
		action.setLinkScheme(linkScheme);
		action.setFromShape(getNode().getDrawable().getDiagramElement());
		action.setToShape(to.getDiagramElement());
		action.escapeParameterRetrievingWhenValid = true;
		action.doAction();

		return controller.getDrawing().getFederatedConnector(action.getNewConnector());
	}

	@Override
	public Rectangle paint(FGEGraphics drawingGraphics) {
		// System.out.println("Focused:"+nodeGR.getIsFocused());

		if (getNode().getIsSelected() && !getNode().getIsFocused()) {
			return null;
		}
		if (/*nodeGR.getIsSelected() ||*/getNode().isResizing() || getNode().isMoving()) {
			return null;
		}
		if (!getNode().getDrawing().isEditable()) {
			return null;
		}
		AffineTransform at = FGEUtils.convertNormalizedCoordinatesAT(getNode(), getNode().getDrawing().getRoot());

		// Graphics2D oldGraphics = drawingGraphics.cloneGraphics();

		drawingGraphics.setDefaultForeground(NONE);
		drawingGraphics.setDefaultBackground(DEFAULT);
		FGERoundRectangle paletteRect = (FGERoundRectangle) getArea().transform(at);
		FGERoundRectangle nodeRect = (FGERoundRectangle) this.roleRect.transform(at);
		FGERectangle edgeRect = (FGERectangle) this.edgeRect.transform(at);
		double arrowSize = 4/** drawingGraphics.getScale() */
		;

		paletteRect.paint(drawingGraphics);

		// 1. Node
		drawingGraphics.setDefaultForeground(NODE_FOREGROUND);
		drawingGraphics.setDefaultBackground(NODE_BACKGROUND);
		nodeRect.paint(drawingGraphics);

		// 2. Edge
		drawingGraphics.setDefaultForeground(EDGE_FOREGROUND);
		// drawingGraphics.setDefaultBackground(EDGE_BACKGROUND);
		drawingGraphics.useDefaultForegroundStyle();
		// drawingGraphics.useDefaultBackgroundStyle();
		FGEPoint eastPt, westPt, northPt, southPt;
		switch (orientation) {
		case EAST:
			eastPt = edgeRect.getEastPt();
			westPt = edgeRect.getWestPt();
			drawingGraphics.drawLine(westPt.x, westPt.y, eastPt.x - arrowSize, eastPt.y);
			drawingGraphics.drawLine(eastPt.x - arrowSize, edgeRect.y + 1, eastPt.x - arrowSize, edgeRect.y + ELEMENTS_HEIGHT - 1);
			drawingGraphics.drawLine(eastPt.x - arrowSize, edgeRect.y + 1, eastPt.x, eastPt.y);
			drawingGraphics.drawLine(eastPt.x - arrowSize, edgeRect.y + ELEMENTS_HEIGHT - 1, eastPt.x, eastPt.y);
			break;
		case WEST:
			eastPt = edgeRect.getEastPt();
			westPt = edgeRect.getWestPt();
			drawingGraphics.drawLine(eastPt.x, eastPt.y, edgeRect.x + arrowSize, eastPt.y);
			drawingGraphics.drawLine(edgeRect.x + arrowSize, edgeRect.y + 1, edgeRect.x + arrowSize, edgeRect.y + ELEMENTS_HEIGHT - 1);
			drawingGraphics.drawLine(edgeRect.x + arrowSize, edgeRect.y + 1, westPt.x, westPt.y);
			drawingGraphics.drawLine(edgeRect.x + arrowSize, edgeRect.y + ELEMENTS_HEIGHT - 1, westPt.x, westPt.y);
			break;
		case NORTH:
			northPt = edgeRect.getNorthPt();
			southPt = edgeRect.getSouthPt();
			drawingGraphics.drawLine(southPt.x, southPt.y, southPt.x, edgeRect.y + arrowSize);
			drawingGraphics.drawLine(edgeRect.x + 2, edgeRect.y + arrowSize, edgeRect.x + ELEMENTS_WIDTH - 2, edgeRect.y + arrowSize);
			drawingGraphics.drawLine(edgeRect.x + 2, edgeRect.y + arrowSize, northPt.x, northPt.y);
			drawingGraphics.drawLine(edgeRect.x + ELEMENTS_WIDTH - 2, edgeRect.y + arrowSize, northPt.x, northPt.y);
			break;
		case SOUTH:
			northPt = edgeRect.getNorthPt();
			southPt = edgeRect.getSouthPt();
			drawingGraphics.drawLine(northPt.x, northPt.y, northPt.x, southPt.y - arrowSize);
			drawingGraphics.drawLine(edgeRect.x + 2, southPt.y - arrowSize, edgeRect.x + ELEMENTS_WIDTH - 2, southPt.y - arrowSize);
			drawingGraphics.drawLine(edgeRect.x + 2, southPt.y - arrowSize, southPt.x, southPt.y);
			drawingGraphics.drawLine(edgeRect.x + ELEMENTS_WIDTH - 2, southPt.y - arrowSize, southPt.x, southPt.y);
			break;

		default:
			break;
		}

		// drawingGraphics.releaseClonedGraphics(oldGraphics);
		Rectangle returned = getNode().getDrawing().getRoot()
				.convertNormalizedRectangleToViewCoordinates(paletteRect.getBoundingBox(), drawingGraphics.getScale());
		returned.x = returned.x - 20;
		returned.y = returned.y - 20;
		returned.width = returned.width + 40;
		returned.height = returned.height + 40;
		return returned;
	}

	@Override
	public boolean isClickable() {
		return false;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(ShapeGraphicalRepresentation.WIDTH_KEY)
				|| evt.getPropertyName().equals(ShapeGraphicalRepresentation.HEIGHT_KEY)) {
			updateElements(orientation);
		}
	}

	private int PALETTE_WIDTH = 16;
	private int PALETTE_HEIGHT = 2 * ELEMENTS_HEIGHT + 3 * SPACING;

	private void updateElements(SimplifiedCardinalDirection orientation) {
		setArea(makeRoundRect(getNode(), orientation));
		AffineTransform at = AffineTransform.getScaleInstance(1 / getNode().getWidth(), 1 / getNode().getHeight());

		if (orientation == SimplifiedCardinalDirection.EAST || orientation == SimplifiedCardinalDirection.WEST) {
			PALETTE_WIDTH = ELEMENTS_WIDTH + 4;
			PALETTE_HEIGHT = 2 * ELEMENTS_HEIGHT + 3 * SPACING;
		} else if (orientation == SimplifiedCardinalDirection.NORTH || orientation == SimplifiedCardinalDirection.SOUTH) {
			PALETTE_WIDTH = 2 * ELEMENTS_WIDTH + 3 * SPACING;
			PALETTE_HEIGHT = ELEMENTS_HEIGHT + 4;
		}

		switch (orientation) {
		case EAST:
			roleRect = (FGERoundRectangle) new FGERoundRectangle(getNode().getWidth() + SPACING + (PALETTE_WIDTH - ELEMENTS_WIDTH) / 2
					+ 0.5, (getNode().getHeight() - PALETTE_HEIGHT) / 2 + SPACING, ELEMENTS_WIDTH, ELEMENTS_HEIGHT, 2, 2, Filling.FILLED)
					.transform(at);
			edgeRect = (FGERectangle) new FGERectangle(getNode().getWidth() + SPACING + (PALETTE_WIDTH - ELEMENTS_WIDTH) / 2, (getNode()
					.getHeight() - PALETTE_HEIGHT) / 2 + SPACING + (SPACING + ELEMENTS_HEIGHT), ELEMENTS_WIDTH, ELEMENTS_HEIGHT,
					Filling.FILLED).transform(at);
			break;
		case WEST:
			roleRect = (FGERoundRectangle) new FGERoundRectangle(-SPACING - ELEMENTS_WIDTH, (getNode().getHeight() - PALETTE_HEIGHT) / 2
					+ SPACING, ELEMENTS_WIDTH, ELEMENTS_HEIGHT, 2, 2, Filling.FILLED).transform(at);
			edgeRect = (FGERectangle) new FGERectangle(-SPACING - ELEMENTS_WIDTH, (getNode().getHeight() - PALETTE_HEIGHT) / 2 + SPACING
					+ (SPACING + ELEMENTS_HEIGHT), ELEMENTS_WIDTH, ELEMENTS_HEIGHT, Filling.FILLED).transform(at);
			break;
		case NORTH:
			roleRect = (FGERoundRectangle) new FGERoundRectangle((getNode().getWidth() - PALETTE_WIDTH) / 2 + SPACING, -SPACING
					- ELEMENTS_HEIGHT, ELEMENTS_WIDTH, ELEMENTS_HEIGHT, 2, 2, Filling.FILLED).transform(at);
			edgeRect = (FGERectangle) new FGERectangle((getNode().getWidth() - PALETTE_WIDTH) / 2 + SPACING + (SPACING + ELEMENTS_WIDTH),
					-SPACING - ELEMENTS_HEIGHT, ELEMENTS_WIDTH, ELEMENTS_HEIGHT, Filling.FILLED).transform(at);
			break;
		case SOUTH:
			roleRect = (FGERoundRectangle) new FGERoundRectangle((getNode().getWidth() - PALETTE_WIDTH) / 2 + SPACING, getNode()
					.getHeight() + SPACING + (PALETTE_HEIGHT - ELEMENTS_HEIGHT) / 2 + 0.5, ELEMENTS_WIDTH, ELEMENTS_HEIGHT, 2, 2,
					Filling.FILLED).transform(at);
			edgeRect = (FGERectangle) new FGERectangle((getNode().getWidth() - PALETTE_WIDTH) / 2 + SPACING + (SPACING + ELEMENTS_WIDTH),
					getNode().getHeight() + SPACING + (PALETTE_HEIGHT - ELEMENTS_HEIGHT) / 2 + 0.5, ELEMENTS_WIDTH, ELEMENTS_HEIGHT,
					Filling.FILLED).transform(at);
			break;

		default:
			break;
		}

	}

	private static FGERoundRectangle makeRoundRect(ShapeNode<FMLControlledDiagramShape> node, SimplifiedCardinalDirection orientation) {
		double x, y, width, height;
		int PALETTE_WIDTH = 0, PALETTE_HEIGHT = 0;
		ShapeGraphicalRepresentation shapeGR = node.getGraphicalRepresentation();

		if (orientation == SimplifiedCardinalDirection.EAST || orientation == SimplifiedCardinalDirection.WEST) {
			PALETTE_WIDTH = ELEMENTS_WIDTH + 4;
			PALETTE_HEIGHT = 2 * ELEMENTS_HEIGHT + 3 * SPACING;
		} else if (orientation == SimplifiedCardinalDirection.NORTH || orientation == SimplifiedCardinalDirection.SOUTH) {
			PALETTE_WIDTH = 2 * ELEMENTS_WIDTH + 3 * SPACING;
			PALETTE_HEIGHT = ELEMENTS_HEIGHT + 4;
		}

		switch (orientation) {
		case EAST:
			x = shapeGR.getWidth() + SPACING;
			y = (shapeGR.getHeight() - PALETTE_HEIGHT) / 2;
			width = PALETTE_WIDTH;
			height = PALETTE_HEIGHT;
			return new FGERoundRectangle(x / shapeGR.getWidth(), y / shapeGR.getHeight(), width / shapeGR.getWidth(), height
					/ shapeGR.getHeight(), 13.0 / shapeGR.getWidth(), 13.0 / shapeGR.getHeight(), Filling.FILLED);
		case WEST:
			x = -SPACING - ELEMENTS_WIDTH;
			y = (shapeGR.getHeight() - PALETTE_HEIGHT) / 2;
			width = PALETTE_WIDTH;
			height = PALETTE_HEIGHT;
			return new FGERoundRectangle(x / shapeGR.getWidth(), y / shapeGR.getHeight(), width / shapeGR.getWidth(), height
					/ shapeGR.getHeight(), 13.0 / shapeGR.getWidth(), 13.0 / shapeGR.getHeight(), Filling.FILLED);
		case NORTH:
			x = (shapeGR.getWidth() - PALETTE_WIDTH) / 2;
			y = -SPACING - ELEMENTS_HEIGHT;
			width = PALETTE_WIDTH;
			height = PALETTE_HEIGHT;
			return new FGERoundRectangle(x / shapeGR.getWidth(), y / shapeGR.getHeight(), width / shapeGR.getWidth(), height
					/ shapeGR.getHeight(), 13.0 / shapeGR.getWidth(), 13.0 / shapeGR.getHeight(), Filling.FILLED);
		case SOUTH:
			x = (shapeGR.getWidth() - PALETTE_WIDTH) / 2;
			y = shapeGR.getHeight() + SPACING;
			width = PALETTE_WIDTH;
			height = PALETTE_HEIGHT;
			return new FGERoundRectangle(x / shapeGR.getWidth(), y / shapeGR.getHeight(), width / shapeGR.getWidth(), height
					/ shapeGR.getHeight(), 13.0 / shapeGR.getWidth(), 13.0 / shapeGR.getHeight(), Filling.FILLED);
		default:
			return null;
		}
	}

}
