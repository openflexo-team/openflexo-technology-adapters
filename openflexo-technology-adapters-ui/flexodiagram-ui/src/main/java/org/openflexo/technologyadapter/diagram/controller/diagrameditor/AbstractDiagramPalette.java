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

import org.openflexo.fge.Drawing.ContainerNode;
import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.LocationConstraints;
import org.openflexo.fge.control.DianaInteractiveEditor.EditorTool;
import org.openflexo.fge.control.DrawingPalette;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.action.FlexoUndoManager.FlexoActionCompoundEdit;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.swing.utils.JFIBDialog;
import org.openflexo.gina.swing.view.SwingViewFactory;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.model.Diagram;
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

	/**
	 * Return boolean if this palette accept drop for an element in supplied target<br>
	 * Default behaviour is to return true if target represent the diagram (top level drop) or a container shape<br>
	 * This method should be overriden when required<br>
	 * 
	 * @param target
	 * @return
	 */
	public boolean shouldAcceptDrop(DrawingTreeNode<?, ?> target) {
		return getEditor() != null && target instanceof ContainerNode
				&& (target.getDrawable() instanceof Diagram || target.getDrawable() instanceof DiagramShape);
	}

	/**
	 * Handle drop of an element in a supplied target, given some parameters<br>
	 * This method should be overriden when required
	 * 
	 * @param target
	 * @param gr
	 * @param dropLocation
	 * @param applyCurrentForeground
	 * @param applyCurrentBackground
	 * @param applyCurrentTextStyle
	 * @param applyCurrentShadowStyle
	 * @param isImage
	 * @param resize
	 * @return
	 */
	public boolean handleBasicGraphicalRepresentationDrop(DrawingTreeNode<?, ?> target, ShapeGraphicalRepresentation gr,
			FGEPoint dropLocation, boolean applyCurrentForeground, boolean applyCurrentBackground, boolean applyCurrentTextStyle,
			boolean applyCurrentShadowStyle, boolean isImage, boolean resize) {

		if (getEditor() == null) {
			return false;
		}

		DiagramContainerElement<?> container = (DiagramContainerElement<?>) target.getDrawable();

		logger.info("drop " + this + " in " + container);

		// getController().addNewShape(new Shape(getGraphicalRepresentation().getShapeType(), dropLocation,
		// getController().getDrawing()),container);

		FlexoActionCompoundEdit edit = (FlexoActionCompoundEdit) getEditor().getUndoManager().startRecording("Dragging new Element");

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
			}
			else {
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

		if (isImage) {
			FIBComponent fibComponent = getEditor().getFIBLibrary().retrieveFIBComponent(DiagramCst.IMPORT_IMAGE_FILE_DIALOG_FIB);
			JFIBDialog dialog = JFIBDialog.instanciateAndShowDialog(fibComponent, shapeGR, FlexoFrame.getActiveFrame(), true,
					new FlexoFIBController(fibComponent, SwingViewFactory.INSTANCE, getEditor().getFlexoController()));
		}

		AddShape action = AddShape.actionType.makeNewAction(container, null, editor.getFlexoController().getEditor());
		action.setGraphicalRepresentation(shapeGR);
		action.setNewShapeName(shapeGR.getText());
		if (action.getNewShapeName() == null) {
			action.setNewShapeName(action.getLocales().localizedForKey("shape"));
		}

		// action.nameSetToNull = true;
		// action.setNewShapeName(FlexoLocalization.localizedForKey("unnamed"));

		action.setCompoundEdit(edit);
		action.doAction();

		DiagramShape newShape = action.getNewShape();

		// getEditor().getUndoManager().stopRecording(edit);

		getEditor().setCurrentTool(EditorTool.SelectionTool);
		getEditor().setSelectedObject(getEditor().getDrawing().getDrawingTreeNode(newShape));

		return action.hasActionExecutionSucceeded();
	}
}
