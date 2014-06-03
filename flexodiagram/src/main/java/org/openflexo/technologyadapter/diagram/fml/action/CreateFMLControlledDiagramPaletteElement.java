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
package org.openflexo.technologyadapter.diagram.fml.action;

import java.security.InvalidParameterException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;

public class CreateFMLControlledDiagramPaletteElement extends
		FlexoAction<CreateFMLControlledDiagramPaletteElement, VirtualModel, ViewPointObject> {

	private static final Logger logger = Logger.getLogger(CreateFMLControlledDiagramPaletteElement.class.getPackage().getName());

	public static FlexoActionType<CreateFMLControlledDiagramPaletteElement, VirtualModel, ViewPointObject> actionType = new FlexoActionType<CreateFMLControlledDiagramPaletteElement, VirtualModel, ViewPointObject>(
			"add_new_palette_element", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFMLControlledDiagramPaletteElement makeNewAction(VirtualModel focusedObject, Vector<ViewPointObject> globalSelection,
				FlexoEditor editor) {
			return new CreateFMLControlledDiagramPaletteElement(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(VirtualModel object, Vector<ViewPointObject> globalSelection) {
			return object.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE);
		}

		@Override
		public boolean isEnabledForSelection(VirtualModel object, Vector<ViewPointObject> globalSelection) {
			return object.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFMLControlledDiagramPaletteElement.actionType, VirtualModel.class);
	}

	private String newElementName;
	private DiagramPaletteElement newElement;
	private ShapeGraphicalRepresentation graphicalRepresentation;
	private FlexoConcept concept;
	private DropScheme dropScheme;
	private DiagramPalette palette;

	CreateFMLControlledDiagramPaletteElement(VirtualModel focusedObject, Vector<ViewPointObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException {

		if (getPalette().getResource() != null) {
			newElement = getPalette().getResource().getFactory().makeDiagramPaletteElement();
			newElement.setName(getNewElementName());
			newElement.setGraphicalRepresentation(getGraphicalRepresentation());

			TypedDiagramModelSlot ms = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(getFocusedObject());
			CreateFMLDiagramPaletteElementBinding createBinding = CreateFMLDiagramPaletteElementBinding.actionType.makeNewEmbeddedAction(
					ms, null, this);
			createBinding.setDiagramPalette(getPalette());
			createBinding.setDiagramPaletteElement(getNewElement());
			createBinding.setFlexoConcept(getConcept());
			createBinding.setDropScheme(getDropScheme());

			createBinding.doAction();

			getPalette().addToElements(newElement);
		}

	}

	public DiagramPaletteElement getNewElement() {
		return newElement;
	}

	private String computeNewName() {
		String newName = getConcept().getName();
		while (getPalette().getPaletteElement(newName) != null) {
			newName = newName + "_new";
		}
		return newName;
	}

	public String getNewElementName() {
		if (newElementName == null && getConcept().getName() != null) {
			newElementName = computeNewName();
		}
		return newElementName;
	}

	public void setNewElementName(String newElementName) {
		this.newElementName = newElementName;
	}

	public ShapeGraphicalRepresentation getGraphicalRepresentation() {
		return graphicalRepresentation;
	}

	public void setGraphicalRepresentation(ShapeGraphicalRepresentation graphicalRepresentation) {
		this.graphicalRepresentation = graphicalRepresentation;
	}

	public FlexoConcept getConcept() {
		return concept;
	}

	public void setConcept(FlexoConcept concept) {
		this.concept = concept;
	}

	public DropScheme getDropScheme() {
		return dropScheme;
	}

	public void setDropScheme(DropScheme dropScheme) {
		this.dropScheme = dropScheme;
	}

	public DiagramPalette getPalette() {
		return palette;
	}

	public void setPalette(DiagramPalette palette) {
		this.palette = palette;
	}

}