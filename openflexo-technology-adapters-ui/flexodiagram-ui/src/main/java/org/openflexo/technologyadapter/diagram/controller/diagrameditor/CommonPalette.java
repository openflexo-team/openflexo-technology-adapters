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
import java.awt.Font;
import java.util.logging.Logger;

import org.openflexo.fge.BackgroundImageBackgroundStyle;
import org.openflexo.fge.BackgroundStyle.BackgroundStyleType;
import org.openflexo.fge.Drawing.ContainerNode;
import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.FGEConstants;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.control.PaletteElement;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.Rectangle;
import org.openflexo.fge.shapes.ShapeSpecification;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

public class CommonPalette extends AbstractDiagramPalette {

	@SuppressWarnings("unused")
	private static final Logger logger = FlexoLogger.getLogger(CommonPalette.class.getPackage().getName());

	private static final int GRID_WIDTH = 50;
	private static final int GRID_HEIGHT = 40;
	public static final Font DEFAULT_TEXT_FONT = new Font("SansSerif", Font.PLAIN, 7);
	public static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 11);
	private static final Resource DEFAULT_IMAGE = ResourceLocator.locateResource("Icons/Diagram.png");

	// private final DiagramEditor editor;
	public CommonPalette(DiagramEditor editor) {
		super(editor, 200, 200, "default");

		ShapeSpecification[] ssp = new ShapeSpecification[15];

		ssp[0] = FACTORY.makeShape(ShapeType.RECTANGLE);
		ssp[1] = FACTORY.makeShape(ShapeType.RECTANGLE);
		((Rectangle) ssp[1]).setIsRounded(true);
		((Rectangle) ssp[1]).setArcSize(20);
		ssp[2] = FACTORY.makeShape(ShapeType.SQUARE);
		ssp[3] = FACTORY.makeShape(ShapeType.PARALLELOGRAM);
		ssp[4] = FACTORY.makeShape(ShapeType.RECTANGULAROCTOGON);
		ssp[5] = FACTORY.makeShape(ShapeType.OVAL);
		ssp[6] = FACTORY.makeShape(ShapeType.CIRCLE);
		ssp[7] = FACTORY.makeShape(ShapeType.LOSANGE);
		ssp[8] = FACTORY.makeShape(ShapeType.POLYGON);
		ssp[9] = FACTORY.makeShape(ShapeType.TRIANGLE);
		ssp[10] = FACTORY.makeShape(ShapeType.STAR);
		ssp[11] = FACTORY.makeShape(ShapeType.ARC);
		ssp[12] = FACTORY.makeShape(ShapeType.PLUS);
		ssp[13] = FACTORY.makeShape(ShapeType.CHEVRON);
		ssp[14] = FACTORY.makeShape(ShapeType.RECTANGLE);

		int px = 0;
		int py = 0;
		for (ShapeSpecification sspi : ssp) {
			if (sspi == ssp[14]) {
				addElement(makeImagePaletteElement(sspi, px, py, DEFAULT_IMAGE));
			}
			addElement(makePaletteElement(sspi, px, py));
			px = px + 1;
			if (px == 4) {
				px = 0;
				py++;
			}
		}

	}

	private void computePaletteElementPosition(ShapeSpecification shapeSpecification, int px, int py, ShapeGraphicalRepresentation gr) {
		if (shapeSpecification.getShapeType() == ShapeType.SQUARE || shapeSpecification.getShapeType() == ShapeType.CIRCLE) {
			gr.setX(px * GRID_WIDTH + 15);
			gr.setY(py * GRID_HEIGHT + 10);
			gr.setWidth(30);
			gr.setHeight(30);
		}
		else {
			gr.setX(px * GRID_WIDTH + 10);
			gr.setY(py * GRID_HEIGHT + 10);
			gr.setWidth(40);
			gr.setHeight(30);
		}
	}

	private PaletteElement makePaletteElement(ShapeSpecification shapeSpecification, int px, int py) {
		final ShapeGraphicalRepresentation gr = FACTORY.makeShapeGraphicalRepresentation(shapeSpecification);
		FACTORY.applyDefaultProperties(gr);
		// if (gr.getDimensionConstraints() == DimensionConstraints.CONSTRAINED_DIMENSIONS) {
		computePaletteElementPosition(shapeSpecification, px, py, gr);
		// gr.setText(st.name());
		gr.setTextStyle(FACTORY.makeTextStyle(Color.DARK_GRAY, DEFAULT_TEXT_FONT));
		gr.setIsFloatingLabel(false);
		gr.setForeground(FACTORY.makeForegroundStyle(Color.BLACK));
		gr.setBackground(FACTORY.makeColoredBackground(FGEConstants.DEFAULT_BACKGROUND_COLOR));
		gr.setIsVisible(true);
		gr.setAllowToLeaveBounds(false);

		return makePaletteElement(gr, true, true, true, true, false);

	}

	private PaletteElement makeImagePaletteElement(ShapeSpecification shapeSpecification, int px, int py, Resource image) {
		final ShapeGraphicalRepresentation gr = FACTORY.makeShapeGraphicalRepresentation(shapeSpecification);
		computePaletteElementPosition(shapeSpecification, px, py, gr);
		gr.setBackgroundType(BackgroundStyleType.IMAGE);
		gr.getForeground().setNoStroke(true);
		gr.getShadowStyle().setDrawShadow(false);
		((BackgroundImageBackgroundStyle) gr.getBackground()).setFitToShape(true);
		((BackgroundImageBackgroundStyle) gr.getBackground()).setImageResource(image);
		gr.setIsVisible(true);
		gr.setAllowToLeaveBounds(false);
		return makePaletteElement(gr, false, false, false, false, true);
	}

	private PaletteElement makePaletteElement(final ShapeGraphicalRepresentation gr, final boolean applyCurrentForeground,
			final boolean applyCurrentBackground, final boolean applyCurrentTextStyle, final boolean applyCurrentShadowStyle,
			final boolean isImage) {
		@SuppressWarnings("serial")
		PaletteElement returned = new PaletteElement() {
			@Override
			public boolean acceptDragging(DrawingTreeNode<?, ?> target) {
				return getEditor() != null && target instanceof ContainerNode
						&& (target.getDrawable() instanceof Diagram || target.getDrawable() instanceof DiagramShape);
			}

			@Override
			public boolean elementDragged(DrawingTreeNode<?, ?> target, FGEPoint dropLocation) {

				// if (true)
				return handleBasicGraphicalRepresentationDrop(target, getGraphicalRepresentation(), dropLocation, applyCurrentForeground,
						applyCurrentBackground, applyCurrentTextStyle, applyCurrentShadowStyle, isImage, true);

				/*if (getEditor() == null) {
					return false;
				}
				
				DiagramContainerElement<?> container = (DiagramContainerElement<?>) target.getDrawable();
				
				logger.info("dragging " + this + " in " + container);
				
				// getController().addNewShape(new Shape(getGraphicalRepresentation().getShapeType(), dropLocation,
				// getController().getDrawing()),container);
				
				CompoundEdit edit = getEditor().getUndoManager().startRecording("Dragging new Element");
				
				ShapeGraphicalRepresentation shapeGR = getEditor().getFactory().makeShapeGraphicalRepresentation(
						getGraphicalRepresentation());
				
				shapeGR.setIsReadOnly(false);
				shapeGR.setIsFocusable(true);
				shapeGR.setIsSelectable(true);
				shapeGR.setLocationConstraints(LocationConstraints.FREELY_MOVABLE);
				
				if (shapeGR.getShapeSpecification().getShapeType() == ShapeType.SQUARE
						|| shapeGR.getShapeSpecification().getShapeType() == ShapeType.CIRCLE) {
					shapeGR.setWidth(40);
					shapeGR.setHeight(40);
				} else {
					shapeGR.setWidth(50);
					shapeGR.setHeight(40);
				}
				if (applyCurrentForeground) {
					shapeGR.setForeground(getEditor().getInspectedForegroundStyle().cloneStyle());
				}
				if (applyCurrentBackground) {
					shapeGR.setBackground(getEditor().getInspectedBackgroundStyle().cloneStyle());
				}
				if (applyCurrentTextStyle) {
					shapeGR.setTextStyle(getEditor().getInspectedTextStyle().cloneStyle());
				}
				if (applyCurrentShadowStyle) {
					shapeGR.setShadowStyle(getEditor().getInspectedShadowStyle().cloneStyle());
				}
				
				shapeGR.setX(dropLocation.x);
				shapeGR.setY(dropLocation.y);
				// shapeGR.setAllowToLeaveBounds(true);
				
				System.out.println("OK, create AddShape");
				System.out.println("location=" + shapeGR.getLocation());
				System.out.println("size=" + shapeGR.getSize());
				
				if (isImage) {
					FIBComponent fibComponent = FIBLibrary.instance().retrieveFIBComponent(DiagramCst.IMPORT_IMAGE_FILE_DIALOG_FIB);
					FIBDialog dialog = FIBDialog.instanciateAndShowDialog(fibComponent, shapeGR, FlexoFrame.getActiveFrame(), true,
							new FlexoFIBController(fibComponent, getEditor().getFlexoController()));
				}
				
				AddShape action = AddShape.actionType.makeNewAction(container, null, getEditor().getFlexoController().getEditor());
				action.setGraphicalRepresentation(shapeGR);
				action.setNewShapeName(shapeGR.getText());
				if (action.getNewShapeName() == null) {
					action.setNewShapeName(FlexoLocalization.localizedForKey("shape"));
				}
				
				// action.nameSetToNull = true;
				// action.setNewShapeName(FlexoLocalization.localizedForKey("unnamed"));
				
				action.doAction();
				
				DiagramShape newShape = action.getNewShape();
				
				System.out.println("Apres la creation:");
				System.out.println("location=" + newShape.getGraphicalRepresentation().getLocation());
				System.out.println("size=" + newShape.getGraphicalRepresentation().getSize());
				
				getEditor().getUndoManager().stopRecording(edit);
				
				getEditor().setCurrentTool(EditorTool.SelectionTool);
				getEditor().setSelectedObject(getEditor().getDrawing().getDrawingTreeNode(newShape));
				
				return action.hasActionExecutionSucceeded();*/
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public ShapeGraphicalRepresentation getGraphicalRepresentation() {
				return gr;
			}

			@Override
			public void delete(Object... context) {
				gr.delete(context);
			}

			/*public DrawingPalette getPalette() {
				return CommonPalette.this;
			}*/
		};
		// gr.setDrawable(returned);
		return returned;
	}

}
