/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.model.action;

import java.security.InvalidParameterException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

public class AddShape extends FlexoAction<AddShape, DiagramContainerElement<?>, DiagramElement<?>> {

	private static final Logger logger = Logger.getLogger(AddShape.class.getPackage().getName());

	public static FlexoActionType<AddShape, DiagramContainerElement<?>, DiagramElement<?>> actionType = new FlexoActionType<AddShape, DiagramContainerElement<?>, DiagramElement<?>>(
			"add_new_shape", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public AddShape makeNewAction(DiagramContainerElement<?> focusedObject, Vector<DiagramElement<?>> globalSelection,
				FlexoEditor editor) {
			return new AddShape(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramContainerElement<?> object, Vector<DiagramElement<?>> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(DiagramContainerElement<?> object, Vector<DiagramElement<?>> globalSelection) {
			return object instanceof Diagram || object instanceof DiagramShape;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(AddShape.actionType, Diagram.class);
		FlexoObjectImpl.addActionForClass(AddShape.actionType, DiagramShape.class);
	}

	private DiagramShape newShape;
	private String newShapeName;
	private DiagramContainerElement<?> parent;
	private ShapeGraphicalRepresentation graphicalRepresentation;
	private boolean nameSetToNull = false;

	AddShape(DiagramContainerElement<?> focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class).getLocales();
		}
		return super.getLocales();
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException {
		logger.info("Add shape");

		if (getParent() == null) {
			throw new InvalidParameterException("folder is undefined");
		}
		if (getNewShapeName() == null && !isNameSetToNull()) {
			throw new InvalidParameterException("shape name is undefined");
		}

		// System.out.println("getFocusedObject()=" + getFocusedObject());
		// System.out.println("getFocusedObject().getDiagram()=" + getFocusedObject().getDiagram());
		// System.out.println("getFocusedObject().getDiagram().getDiagramFactory()=" + getFocusedObject().getDiagram().getDiagramFactory());

		// System.out.println("GR=" + getGraphicalRepresentation());

		newShape = getFocusedObject().getDiagram().getDiagramFactory().makeNewShape(getNewShapeName(), getGraphicalRepresentation(),
				getParent());

		if (getGraphicalRepresentation() != null) {
			newShape.setGraphicalRepresentation(getGraphicalRepresentation());
		}

		getParent().addToShapes(newShape);

		logger.info("Added shape " + newShape + " under " + getParent());

		// System.out.println("newShape=" + newShape);
		// System.out.println("diagram=" + newShape.getDiagram());
	}

	public DiagramShape getNewShape() {
		return newShape;
	}

	public DiagramContainerElement<?> getParent() {
		if (parent == null) {
			parent = getFocusedObject();
		}
		return parent;
	}

	public void setParent(DiagramContainerElement<?> parent) {
		this.parent = parent;
	}

	public String getNewShapeName() {
		return newShapeName;
	}

	public void setNewShapeName(String newShapeName) {
		this.newShapeName = newShapeName;
	}

	public ShapeGraphicalRepresentation getGraphicalRepresentation() {
		return graphicalRepresentation;
	}

	public void setGraphicalRepresentation(ShapeGraphicalRepresentation graphicalRepresentation) {
		this.graphicalRepresentation = graphicalRepresentation;
	}

	public boolean isNameSetToNull() {
		return nameSetToNull;
	}

	public void setNameSetToNull(boolean nameSetToNull) {
		this.nameSetToNull = nameSetToNull;
	}

}
