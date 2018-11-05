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

package org.openflexo.technologyadapter.diagram.controller.paletteeditor;

import java.util.logging.Logger;

import org.openflexo.fge.Drawing.ContainerNode;
import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.PaletteElementSpecification;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.control.DianaInteractiveEditor.EditorTool;
import org.openflexo.fge.control.PaletteElement;
import org.openflexo.fge.control.PaletteModel;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.undo.CompoundEdit;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.CommonPalette;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;

public class DiagramPalettePalette extends PaletteModel {

	@SuppressWarnings("unused")
	private static final Logger logger = FlexoLogger.getLogger(CommonPalette.class.getPackage().getName());

	/*private static final int GRID_WIDTH = 50;
	private static final int GRID_HEIGHT = 40;
	public static final Font DEFAULT_TEXT_FONT = new Font("SansSerif", Font.PLAIN, 7);
	public static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 11);*/

	private final DiagramPaletteEditor editor;

	public DiagramPalettePalette(DiagramPaletteEditor editor) {
		super("default", 200, 200, 40, 30, 10, 10);
		// super(200, 200, "default");

		this.editor = editor;

		/*ShapeSpecification[] ssp = new ShapeSpecification[13];
		
		ssp[0] = FACTORY.makeShape(ShapeType.RECTANGLE);
		ssp[1] = FACTORY.makeShape(ShapeType.RECTANGLE);
		((Rectangle) ssp[1]).setIsRounded(true);
		((Rectangle) ssp[1]).setArcSize(20);
		ssp[2] = FACTORY.makeShape(ShapeType.SQUARE);
		ssp[3] = FACTORY.makeShape(ShapeType.RECTANGULAROCTOGON);
		ssp[4] = FACTORY.makeShape(ShapeType.OVAL);
		ssp[5] = FACTORY.makeShape(ShapeType.CIRCLE);
		ssp[6] = FACTORY.makeShape(ShapeType.LOSANGE);
		ssp[7] = FACTORY.makeShape(ShapeType.POLYGON);
		ssp[8] = FACTORY.makeShape(ShapeType.TRIANGLE);
		ssp[9] = FACTORY.makeShape(ShapeType.STAR);
		ssp[10] = FACTORY.makeShape(ShapeType.ARC);
		ssp[11] = FACTORY.makeShape(ShapeType.PLUS);
		ssp[12] = FACTORY.makeShape(ShapeType.CHEVRON);
		
		int px = 0;
		int py = 0;
		for (ShapeSpecification sspi : ssp) {
			addElement(makePaletteElement(sspi, px, py));
			px = px + 1;
			if (px == 4) {
				px = 0;
				py++;
			}
		}*/

	}

	public DiagramPaletteEditor getEditor() {
		return editor;
	}

	/*private PaletteElement makePaletteElement(ShapeSpecification shapeSpecification, int px, int py) {
		final ShapeGraphicalRepresentation gr = FACTORY.makeShapeGraphicalRepresentation(shapeSpecification);
		FACTORY.applyDefaultProperties(gr);
		// if (gr.getDimensionConstraints() == DimensionConstraints.CONSTRAINED_DIMENSIONS) {
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
		// gr.setText(st.name());
		gr.setTextStyle(FACTORY.makeTextStyle(Color.DARK_GRAY, DEFAULT_TEXT_FONT));
		gr.setIsFloatingLabel(false);
		gr.setForeground(FACTORY.makeForegroundStyle(Color.BLACK));
		gr.setBackground(FACTORY.makeColoredBackground(FGEConstants.DEFAULT_BACKGROUND_COLOR));
		gr.setIsVisible(true);
		gr.setAllowToLeaveBounds(false);
	
		return makePaletteElement(gr, true, true, true, true);
	
	}*/

	@Override
	protected PaletteElement buildPaletteElement(PaletteElementSpecification paletteElement) {
		@SuppressWarnings("serial")
		PaletteElement returned = new PaletteElement() {
			@Override
			public boolean acceptDragging(DrawingTreeNode<?, ?> target) {
				return getEditor() != null && target instanceof ContainerNode && (target.getDrawable() instanceof DiagramPalette);
			}

			@Override
			public boolean elementDragged(DrawingTreeNode<?, ?> target, FGEPoint dropLocation) {

				if (getEditor() == null) {
					return false;
				}

				DiagramPalette container = (DiagramPalette) target.getDrawable();

				logger.info("dragging " + this + " in " + container);

				// getController().addNewShape(new Shape(getGraphicalRepresentation().getShapeType(), dropLocation,
				// getController().getDrawing()),container);

				CompoundEdit edit = getEditor().getUndoManager().startRecording("Dragging new element in palette");

				ShapeGraphicalRepresentation shapeGR = getEditor().getFactory().makeNewShapeGR(getGraphicalRepresentation());
				if (shapeGR.getShapeSpecification().getShapeType() == ShapeType.SQUARE
						|| shapeGR.getShapeSpecification().getShapeType() == ShapeType.CIRCLE) {
					shapeGR.setWidth(40);
					shapeGR.setHeight(40);
				}
				else {
					shapeGR.setWidth(50);
					shapeGR.setHeight(40);
				}
				if (paletteElement.getApplyCurrentForeground()) {
					shapeGR.setForeground(getEditor().getInspectedForegroundStyle().cloneStyle());
				}
				if (paletteElement.getApplyCurrentBackground()) {
					shapeGR.setBackground(getEditor().getInspectedBackgroundStyle().cloneStyle());
				}
				if (paletteElement.getApplyCurrentTextStyle()) {
					shapeGR.setTextStyle(getEditor().getInspectedTextStyle().cloneStyle());
				}
				if (paletteElement.getApplyCurrentShadowStyle()) {
					shapeGR.setShadowStyle(getEditor().getInspectedShadowStyle().cloneStyle());
				}

				shapeGR.setX(dropLocation.x);
				shapeGR.setY(dropLocation.y);

				CreateDiagramPaletteElement action = CreateDiagramPaletteElement.actionType.makeNewAction(container, null,
						editor.getFlexoController().getEditor());
				action.setGraphicalRepresentation(shapeGR);

				action.doAction();

				DiagramPaletteElement newElement = action.getNewElement();

				getEditor().getUndoManager().stopRecording(edit);

				getEditor().setCurrentTool(EditorTool.SelectionTool);
				getEditor().setSelectedObject(getEditor().getDrawing().getDrawingTreeNode(newElement));

				return action.hasActionExecutionSucceeded();
			}

			@Override
			public String getName() {
				return paletteElement.getName();
			}

			@Override
			public ShapeGraphicalRepresentation getGraphicalRepresentation() {
				return paletteElement.getGraphicalRepresentation();
			}

			@Override
			public void delete(Object... context) {
				paletteElement.delete();
			}

			/*public PaletteModel getPalette() {
				return CommonPalette.this;
			}*/
		};
		// gr.setDrawable(returned);
		return returned;
	}
}

/*
 * 	private PaletteElement makePaletteElement(final PaletteElementGraphicalRepresentation gr, final boolean applyForegroundStyle,
			final boolean applyBackgroundStyle, final boolean applyShadowStyle, final boolean applyTextStyle) {
		PaletteElement returned = new PaletteElement() {
			@Override
			public boolean acceptDragging(GraphicalRepresentation target) {
				return target.getDrawable() instanceof DiagramPalette;
			}

			@Override
			public boolean elementDragged(GraphicalRepresentation containerGR, FGEPoint dropLocation) {
				if (containerGR.getDrawable() instanceof DiagramPalette) {

					DiagramPalette container = (DiagramPalette) containerGR.getDrawable();

					ShapeGraphicalRepresentation shapeGR = getGraphicalRepresentation().clone();
					shapeGR.setIsSelectable(true);
					shapeGR.setIsFocusable(true);
					shapeGR.setIsReadOnly(false);
					shapeGR.setLocationConstraints(LocationConstraints.FREELY_MOVABLE);
					shapeGR.setLocation(dropLocation);
					shapeGR.setLayer(containerGR.getLayer() + 1);
					shapeGR.setAllowToLeaveBounds(true);

					CreateDiagramPaletteElement action = CreateDiagramPaletteElement.actionType.makeNewAction(container, null, _editor);
					action.setGraphicalRepresentation(shapeGR);

					action.doAction();

					// container.addPaletteElement(shapeGR.getText(), shapeGR);

					return true;
				}

				return false;
			}

			@Override
			public PaletteElementGraphicalRepresentation getGraphicalRepresentation() {
				return gr;
			}

			@Override
			public PaletteModel getPalette() {
				return DiagramPalettePalette.this;
			}

		};
		gr.setDrawable(returned);
		return returned;
	}

*/
