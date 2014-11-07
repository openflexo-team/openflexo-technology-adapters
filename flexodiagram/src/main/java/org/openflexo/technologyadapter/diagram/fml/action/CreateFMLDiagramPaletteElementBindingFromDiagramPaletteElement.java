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

import java.awt.Image;
import java.security.InvalidParameterException;
import java.util.ArrayList;
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
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.rm.VirtualModelResource;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBindingParameter;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.toolbox.StringUtils;

public class CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement extends FlexoAction<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement, DiagramPaletteElement, DiagramPaletteElement> {

	private static final Logger logger = Logger.getLogger(CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement.class.getPackage().getName());

	public static FlexoActionType<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement, DiagramPaletteElement, DiagramPaletteElement> actionType = new FlexoActionType<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement, DiagramPaletteElement, DiagramPaletteElement>(
			"create_new_palette_binding", FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement makeNewAction(DiagramPaletteElement focusedObject, Vector<DiagramPaletteElement> globalSelection,
				FlexoEditor editor) {
			return new CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramPaletteElement object, Vector<DiagramPaletteElement> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(DiagramPaletteElement object, Vector<DiagramPaletteElement> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement.actionType, DiagramPaletteElement.class);
	}

	private String description;

	CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement(DiagramPaletteElement focusedObject, Vector<DiagramPaletteElement> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws NotImplementedException, InvalidParameterException, SaveResourceException {
		logger.info("Add palette element binding to typed diagram modelslot");
		FMLDiagramPaletteElementBinding newBinding = getTypedDiagramModelSlot().getVirtualModelFactory().newInstance(
				FMLDiagramPaletteElementBinding.class);
		
		getTypedDiagramModelSlot().addToPaletteElementBindings(newBinding);
		newBinding.setPaletteElement(getFocusedObject());
		newBinding.setFlexoConcept(flexoConcept);
		if(createDropScheme){
			dropScheme = getVirtualModel().getVirtualModelFactory().newInstance(DropScheme.class);
			dropScheme.setName(getDropSchemeName());
			dropScheme.setFlexoConcept(getFlexoConcept());
			flexoConcept.addToFlexoBehaviours(dropScheme);
		}
		newBinding.setDropScheme(dropScheme);
	}

	private String name;
	private DropScheme dropScheme;
	private String dropSchemeName = "newDropScheme";
	private FlexoConcept flexoConcept;
	private List<FMLDiagramPaletteElementBindingParameter> paletteElementBindingParameter;
	private DiagramPalette diagramPalette;
	private VirtualModelResource virtualModelResource;
	private VirtualModel virtualModel;
	private List<DropScheme> dropSchemes;
	private List<FlexoConcept> flexoConcepts;
	private Image image;
	private boolean createDropScheme;
	
	private String errorMessage = EMPTY_NAME;

	private static final String NAME_IS_VALID = FlexoLocalization.localizedForKey("name_is_valid");
	private static final String EMPTY_NAME = FlexoLocalization.localizedForKey("empty_name");
	private static final String FLEXO_CONCEPT_IS_EMPTY = FlexoLocalization.localizedForKey("flexo_concept_is_empty");
	private static final String DROP_SCHEME_IS_EMPTY = FlexoLocalization.localizedForKey("drop_scheme_is_empty");

	public boolean isNameValid() {
		if (StringUtils.isEmpty(getName())) {
			setErrorMessage(EMPTY_NAME);
			return false;
		} else {
			setErrorMessage(NAME_IS_VALID);
			return true;
		}
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
	
	public List<FlexoConcept> getFlexoConcepts() {
		return flexoConcepts;
	}

	public void setFlexoConcepts(List<FlexoConcept> flexoConcepts) {
		this.flexoConcepts = flexoConcepts;
	}

	public void setFlexoConcept(FlexoConcept flexoConcept) {
		this.flexoConcept = flexoConcept;
		computeDropSchemes();
	}
	
	public DropScheme getDropScheme() {
		return dropScheme;
	}

	public void setDropScheme(DropScheme dropScheme) {
		this.dropScheme = dropScheme;
	}

	public List<FMLDiagramPaletteElementBindingParameter> getPaletteElementBindingParameter() {
		return paletteElementBindingParameter;
	}

	public void setPaletteElementBindingParameter(
			List<FMLDiagramPaletteElementBindingParameter> paletteElementBindingParameter) {
		this.paletteElementBindingParameter = paletteElementBindingParameter;
	}
	
	public DiagramPalette getDiagramPalette() {
		return diagramPalette;
	}

	public void setDiagramPalette(DiagramPalette diagramPalette) {
		this.diagramPalette = diagramPalette;
	}

	public TypedDiagramModelSlot getTypedDiagramModelSlot() {
		if(getVirtualModel()!=null){
			for(TypedDiagramModelSlot ms : getVirtualModel().getModelSlots(TypedDiagramModelSlot.class)){
				if(ms.getDiagramSpecification().equalsObject(getDiagramSpecification()))
					return ms;
			}
		}
		return null;
	}

	public DiagramSpecification getDiagramSpecification() {
		return getFocusedObject().getDiagramSpecification();
	}
	
	public VirtualModelResource getVirtualModelResource() {
		return virtualModelResource;
	}

	public void setVirtualModelResource(VirtualModelResource virtualModelResource) {
		this.virtualModelResource = virtualModelResource;
		if(virtualModelResource!=null){
			setVirtualModel(virtualModelResource.getVirtualModel());
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public VirtualModel getVirtualModel() {
		return virtualModel;
	}

	public void setVirtualModel(VirtualModel virtualModel) {
		this.virtualModel = virtualModel;
		computeFlexoConcepts();
	}
	
	public List<DropScheme> getDropSchemes() {
		return dropSchemes;
	}

	public void setDropSchemes(List<DropScheme> dropSchemes) {
		this.dropSchemes = dropSchemes;
	}

	private void computeDropSchemes(){
		if(dropSchemes==null){
			dropSchemes = new ArrayList<DropScheme>();
		}
		dropSchemes.clear();
		if(getFlexoConcept()!=null){
			dropSchemes.addAll(getFlexoConcept().getFlexoBehaviours(DropScheme.class));
		}
	}
	
	private void computeFlexoConcepts(){
		if(flexoConcepts==null){
			flexoConcepts = new ArrayList<FlexoConcept>();
		}
		flexoConcepts.clear();
		if(getVirtualModel()!=null){
			for(FlexoConcept fc :getVirtualModel().getFlexoConcepts()){
				if(fc.getFlexoBehaviours(DropScheme.class)!=null){
					flexoConcepts.add(fc);
				}
			}
		}
		//getPropertyChangeSupport().firePropertyChange("flexoConcepts", null, flexoConcepts);
	}

	@Override
	public boolean isValid() {
		if (!isNameValid()) {
			return false;
		} else if(getDropScheme()==null && !createDropScheme) {
			setErrorMessage(DROP_SCHEME_IS_EMPTY);
			return false;
		} else if(getFlexoConcept()==null) {
			setErrorMessage(FLEXO_CONCEPT_IS_EMPTY);
			return false;
		}
		return true;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public boolean isCreateDropScheme() {
		return createDropScheme;
	}

	public void setCreateDropScheme(boolean createDropScheme) {
		this.createDropScheme = createDropScheme;
	}

	public String getDropSchemeName() {
		return dropSchemeName;
	}

	public void setDropSchemeName(String dropSchemeName) {
		this.dropSchemeName = dropSchemeName;
	}

	
}