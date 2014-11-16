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
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBindingParameter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;

public class CreateFMLDiagramPaletteElementBinding extends FlexoAction<CreateFMLDiagramPaletteElementBinding, TypedDiagramModelSlot, ViewPointObject> {

	private static final Logger logger = Logger.getLogger(CreateFMLDiagramPaletteElementBinding.class.getPackage().getName());

	public static FlexoActionType<CreateFMLDiagramPaletteElementBinding, TypedDiagramModelSlot, ViewPointObject> actionType = new FlexoActionType<CreateFMLDiagramPaletteElementBinding, TypedDiagramModelSlot, ViewPointObject>(
			"create_new_palette_binding", FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFMLDiagramPaletteElementBinding makeNewAction(TypedDiagramModelSlot focusedObject, Vector<ViewPointObject> globalSelection,
				FlexoEditor editor) {
			return new CreateFMLDiagramPaletteElementBinding(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(TypedDiagramModelSlot object, Vector<ViewPointObject> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(TypedDiagramModelSlot object, Vector<ViewPointObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFMLDiagramPaletteElementBinding.actionType, TypedDiagramModelSlot.class);
	}

	private String description;

	CreateFMLDiagramPaletteElementBinding(TypedDiagramModelSlot focusedObject, Vector<ViewPointObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException, SaveResourceException {
		logger.info("Add palette element binding to typed diagram modelslot");
		FMLDiagramPaletteElementBinding newBinding = getFocusedObject().getVirtualModelFactory().newInstance(
				FMLDiagramPaletteElementBinding.class);
		newBinding.setPaletteElement(diagramPaletteElement);
		newBinding.setFlexoConcept(flexoConcept);
		newBinding.setDropScheme(dropScheme);

		getFocusedObject().addToPaletteElementBindings(newBinding);
	}

	private DropScheme dropScheme;
	
	private FlexoConcept flexoConcept;
	
	private DiagramPaletteElement diagramPaletteElement;
	
	private List<FMLDiagramPaletteElementBindingParameter> paletteElementBindingParameter;
	
	private DiagramPalette diagramPalette;
	
	private String nameValidityMessage = EMPTY_NAME;

	private static final String NAME_IS_VALID = FlexoLocalization.localizedForKey("name_is_valid");
	private static final String DUPLICATED_NAME = FlexoLocalization.localizedForKey("this_name_is_already_used_please_choose_an_other_one");
	private static final String EMPTY_NAME = FlexoLocalization.localizedForKey("empty_name");

	public String getNameValidityMessage() {
		return nameValidityMessage;
	}

	public boolean isNameValid() {
		return true;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public FlexoConcept getFlexoConcept() {
		return flexoConcept;
	}

	public void setFlexoConcept(FlexoConcept flexoConcept) {
		this.flexoConcept = flexoConcept;
	}
	
	public DropScheme getDropScheme() {
		return dropScheme;
	}

	public void setDropScheme(DropScheme dropScheme) {
		this.dropScheme = dropScheme;
	}

	public List<DropScheme> allAvailableDropSchemes() {
		if (getFlexoConcept() != null) {
			return getFlexoConcept().getFlexoBehaviours(DropScheme.class);
		}
		return null;
	}
	
	public List<DiagramPalette> allAvailableDiagramPalette() {
		if(getFocusedObject().getMetaModelResource()!=null){
			return getFocusedObject().getMetaModelResource().getMetaModelData().getPalettes();
		}
		return null;
	}
	
	public List<DiagramPaletteElement> allAvailableDiagramPaletteElements() {
		if(diagramPalette!=null){
			return diagramPalette.getElements();
		}
		return null;
	}

	public List<FMLDiagramPaletteElementBindingParameter> getPaletteElementBindingParameter() {
		return paletteElementBindingParameter;
	}

	public void setPaletteElementBindingParameter(
			List<FMLDiagramPaletteElementBindingParameter> paletteElementBindingParameter) {
		this.paletteElementBindingParameter = paletteElementBindingParameter;
	}

	public DiagramPaletteElement getDiagramPaletteElement() {
		return diagramPaletteElement;
	}

	public void setDiagramPaletteElement(DiagramPaletteElement diagramPaletteElement) {
		this.diagramPaletteElement = diagramPaletteElement;
	}

	public DiagramPalette getDiagramPalette() {
		return diagramPalette;
	}

	public void setDiagramPalette(DiagramPalette diagramPalette) {
		this.diagramPalette = diagramPalette;
	}


}