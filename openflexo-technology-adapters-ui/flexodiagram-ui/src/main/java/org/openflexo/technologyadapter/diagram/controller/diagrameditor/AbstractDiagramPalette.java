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

import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.LocationConstraints;
import org.openflexo.fge.control.DianaInteractiveEditor.EditorTool;
import org.openflexo.fge.control.DrawingPalette;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.fib.FIBLibrary;
import org.openflexo.fib.controller.FIBDialog;
import org.openflexo.fib.model.FIBComponent;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.undo.CompoundEdit;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.AddShape;
import org.openflexo.view.FlexoFrame;
import org.openflexo.view.controller.FlexoFIBController;

public abstract class AbstractDiagramPalette extends DrawingPalette {

	@SuppressWarnings("unused")
	private static final Logger logger = FlexoLogger.getLogger(AbstractDiagramPalette.class.getPackage().getName());

	private DiagramEditor editor;

	public AbstractDiagramPalette(DiagramEditor editor, int width, int height, String title) {
		super(width, height, title);
		this.editor = editor;
	}

	public DiagramEditor getEditor() {
		return editor;
	}

	@Override
	public void delete() {
		editor = null;
		super.delete();
	}

	public boolean handleBasicGraphicalRepresentationDrop(DrawingTreeNode<?, ?> target, ShapeGraphicalRepresentation gr,
			FGEPoint dropLocation, boolean applyCurrentForeground, boolean applyCurrentBackground, boolean applyCurrentTextStyle,
			boolean applyCurrentShadowStyle, boolean isImage, boolean resize) {

		if (getEditor() == null) {
			return false;
		}

		DiagramContainerElement<?> container = (DiagramContainerElement<?>) target.getDrawable();

		logger.info("dragging " + this + " in " + container);

		// getController().addNewShape(new Shape(getGraphicalRepresentation().getShapeType(), dropLocation,
		// getController().getDrawing()),container);

		CompoundEdit edit = getEditor().getFactory().getUndoManager().startRecording("Dragging new Element");

		ShapeGraphicalRepresentation shapeGR = getEditor().getFactory().makeShapeGraphicalRepresentation(gr);

		shapeGR.setIsReadOnly(false);
		shapeGR.setIsFocusable(true);
		shapeGR.setIsSelectable(true);
		shapeGR.setLocationConstraints(LocationConstraints.FREELY_MOVABLE);

		if (resize) {
			if (shapeGR.getShapeSpecification().getShapeType() == ShapeType.SQUARE
					|| shapeGR.getShapeSpecification().getShapeType() == ShapeType.CIRCLE) {
				shapeGR.setWidth(40);
				shapeGR.setHeight(40);
			} else {
				shapeGR.setWidth(50);
				shapeGR.setHeight(40);
			}
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

		AddShape action = AddShape.actionType.makeNewAction(container, null, editor.getFlexoController().getEditor());
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

		getEditor().getFactory().getUndoManager().stopRecording(edit);

		getEditor().setCurrentTool(EditorTool.SelectionTool);
		getEditor().setSelectedObject(getEditor().getDrawing().getDrawingTreeNode(newShape));

		return action.hasActionExecutionSucceeded();
	}

}
