/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBindingParameter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;

public class CreateFMLDiagramPaletteElementBinding
		extends FlexoAction<CreateFMLDiagramPaletteElementBinding, TypedDiagramModelSlot, FMLObject> {

	private static final Logger logger = Logger.getLogger(CreateFMLDiagramPaletteElementBinding.class.getPackage().getName());

	public static FlexoActionType<CreateFMLDiagramPaletteElementBinding, TypedDiagramModelSlot, FMLObject> actionType = new FlexoActionType<CreateFMLDiagramPaletteElementBinding, TypedDiagramModelSlot, FMLObject>(
			"create_new_palette_binding", FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFMLDiagramPaletteElementBinding makeNewAction(TypedDiagramModelSlot focusedObject, Vector<FMLObject> globalSelection,
				FlexoEditor editor) {
			return new CreateFMLDiagramPaletteElementBinding(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(TypedDiagramModelSlot object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(TypedDiagramModelSlot object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFMLDiagramPaletteElementBinding.actionType, TypedDiagramModelSlot.class);
	}

	private String description;

	CreateFMLDiagramPaletteElementBinding(TypedDiagramModelSlot focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException, SaveResourceException {
		logger.info("Add palette element binding to typed diagram modelslot");
		FMLDiagramPaletteElementBinding newBinding = getFocusedObject().getFMLModelFactory()
				.newInstance(FMLDiagramPaletteElementBinding.class);
		newBinding.setDiagramModelSlot(getFocusedObject());
		newBinding.setPaletteElement(diagramPaletteElement);
		newBinding.setBoundFlexoConcept(flexoConcept);
		newBinding.setDropScheme(dropScheme);

		getFocusedObject().addToPaletteElementBindings(newBinding);
	}

	private DropScheme dropScheme;

	private FlexoConcept flexoConcept;

	private DiagramPaletteElement diagramPaletteElement;

	private List<FMLDiagramPaletteElementBindingParameter> paletteElementBindingParameter;

	private DiagramPalette diagramPalette;

	private final String nameValidityMessage = EMPTY_NAME;

	private static final String NAME_IS_VALID = "name_is_valid";
	private static final String DUPLICATED_NAME = "this_name_is_already_used_please_choose_an_other_one";
	private static final String EMPTY_NAME = "empty_name";

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
		if (getFocusedObject().getMetaModelResource() != null) {
			return getFocusedObject().getMetaModelResource().getMetaModelData().getPalettes();
		}
		return null;
	}

	public List<DiagramPaletteElement> allAvailableDiagramPaletteElements() {
		if (diagramPalette != null) {
			return diagramPalette.getElements();
		}
		return null;
	}

	public List<FMLDiagramPaletteElementBindingParameter> getPaletteElementBindingParameter() {
		return paletteElementBindingParameter;
	}

	public void setPaletteElementBindingParameter(List<FMLDiagramPaletteElementBindingParameter> paletteElementBindingParameter) {
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
