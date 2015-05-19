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

package org.openflexo.technologyadapter.diagram.fml.action;

import java.security.InvalidParameterException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteFactory;

public class CreateDiagramPaletteElement extends FlexoAction<CreateDiagramPaletteElement, DiagramPalette, FMLObject> {

	private static final Logger logger = Logger.getLogger(CreateDiagramPaletteElement.class.getPackage().getName());

	public static FlexoActionType<CreateDiagramPaletteElement, DiagramPalette, FMLObject> actionType = new FlexoActionType<CreateDiagramPaletteElement, DiagramPalette, FMLObject>(
			"add_new_palette_element", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateDiagramPaletteElement makeNewAction(DiagramPalette focusedObject, Vector<FMLObject> globalSelection,
				FlexoEditor editor) {
			return new CreateDiagramPaletteElement(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramPalette object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(DiagramPalette object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateDiagramPaletteElement.actionType, DiagramPalette.class);
	}

	private String _newElementName;
	private DiagramPaletteElement _newElement;
	private Object _graphicalRepresentation;

	CreateDiagramPaletteElement(DiagramPalette focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	public DiagramPaletteFactory getDiagramPaletteFactory() {
		return getFocusedObject().getFactory();
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException {
		_newElement = getFocusedObject().addPaletteElement(getNewElementName(), getGraphicalRepresentation());

	}

	public DiagramPaletteElement getNewElement() {
		return _newElement;
	}

	public String getNewElementName() {
		return _newElementName;
	}

	public void setNewElementName(String newElementName) {
		_newElementName = newElementName;
	}

	public Object getGraphicalRepresentation() {
		return _graphicalRepresentation;
	}

	public void setGraphicalRepresentation(Object graphicalRepresentation) {
		_graphicalRepresentation = graphicalRepresentation;
	}

}
