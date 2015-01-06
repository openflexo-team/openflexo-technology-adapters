/*
 * (c) Copyright 2013 Openflexo
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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.DeletionScheme;
import org.openflexo.foundation.fml.FlexoBehaviour;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceParameter;
import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.IndividualParameter;
import org.openflexo.foundation.fml.IndividualRole;
import org.openflexo.foundation.fml.URIParameter;
import org.openflexo.foundation.fml.editionaction.AddIndividual;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.editionaction.DeleteAction;
import org.openflexo.foundation.fml.inspector.FlexoConceptInspector;
import org.openflexo.foundation.fml.rt.FMLRTModelSlot;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.editionaction.AddFlexoConceptInstance;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.binding.DiagramBehaviourBindingModel;
import org.openflexo.technologyadapter.diagram.fml.binding.DropSchemeBindingModel;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

/**
 * This class represents an abstraction for a declare shape in flexo concept action among several kind of shapes.</br>
 * 
 * 
 * @author Vincent
 * 
 * @param <T1>
 */

public class DeclareShapeInFlexoConcept extends DeclareInFlexoConcept<DeclareShapeInFlexoConcept, DiagramShape> {

	private static final Logger logger = Logger.getLogger(DeclareShapeInFlexoConcept.class.getPackage().getName());

	/**
	 * Create a new Flexo Action Type
	 */
	public static FlexoActionType<DeclareShapeInFlexoConcept, DiagramShape, DiagramElement<?>> actionType = new FlexoActionType<DeclareShapeInFlexoConcept, DiagramShape, DiagramElement<?>>(
			"declare_in_flexo_concept", FlexoActionType.defaultGroup, FlexoActionType.NORMAL_ACTION_TYPE) {

		@Override
		public DeclareShapeInFlexoConcept makeNewAction(DiagramShape focusedObject, Vector<DiagramElement<?>> globalSelection,
				FlexoEditor editor) {
			return new DeclareShapeInFlexoConcept(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramShape shape, Vector<DiagramElement<?>> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(DiagramShape shape, Vector<DiagramElement<?>> globalSelection) {
			// TODO : implement the rights for modifying the viewpoint
			// ex: if(shape.getDiagramSpec.isEditable) ...

			return shape != null /*&& shape.getDiagramSpecification() != null*/;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(DeclareShapeInFlexoConcept.actionType, DiagramShape.class);
	}

	public static enum NewFlexoConceptChoices {
		MAP_SINGLE_INDIVIDUAL, MAP_SINGLE_FLEXO_CONCEPT, BLANK_FLEXO_CONCEPT
	}

	private static final String FLEXO_ROLE_IS_NULL = FlexoLocalization.localizedForKey("flexo_role_is_null");
	private static final String FLEXO_CONCEPT_IS_NULL = FlexoLocalization.localizedForKey("flexo_concept_is_null");
	private static final String FLEXO_CONCEPT_NAME_IS_NULL = FlexoLocalization.localizedForKey("flexo_concept_name_is_null");
	private static final String FOCUSED_OBJECT_IS_NULL = FlexoLocalization.localizedForKey("focused_object_is_null");
	private static final String INDIVIDUAL_FLEXO_ROLE_NAME_IS_NULL = FlexoLocalization
			.localizedForKey("individual_flexo_role_name_is_null");
	private static final String CONCEPT_IS_NULL = FlexoLocalization.localizedForKey("concept_is_null");
	private static final String NO_SELECTED_ENTRY = FlexoLocalization.localizedForKey("no_selected_entry");
	private static final String A_SCHEME_NAME_IS_NOT_VALID = FlexoLocalization.localizedForKey("a_scheme_name_is_not_valid");
	private static final String VIRTUAL_MODEL_FLEXO_ROLE_NAME_IS_NULL = FlexoLocalization
			.localizedForKey("virtual_model_flexo_role_name_is_null");
	private static final String VIRTUAL_MODEL_CONCEPT_IS_NULL = FlexoLocalization.localizedForKey("virtual_model_concept_is_null");

	public NewFlexoConceptChoices patternChoice = NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL;

	private String flexoConceptName;
	private IFlexoOntologyClass concept;
	private String individualFlexoRoleName;
	private FlexoConceptInstanceRole flexoConceptInstanceRole;
	private String virtualModelFlexoRoleName;
	private FlexoConcept newFlexoConcept;
	private LinkedHashMap<DrawingObjectEntry, GraphicalElementRole> newGraphicalElementRoles;
	// public DiagramPalette palette;

	private boolean isTopLevel = true;
	// public boolean isPushedToPalette = false;
	private FlexoConcept containerFlexoConcept;
	private FlexoConcept virtualModelConcept;
	private String dropSchemeName;

	private IndividualRole<?> individualFlexoRole;

	private DropScheme dropScheme;

	DeclareShapeInFlexoConcept(DiagramShape focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	private String errorMessage;

	public String getErrorMessage() {
		isValid();
		return errorMessage;
	}

	@Override
	public boolean isValid() {
		if (getFocusedObject() == null) {
			errorMessage = FOCUSED_OBJECT_IS_NULL;
			return false;
		}
		switch (primaryChoice) {
		case CHOOSE_EXISTING_FLEXO_CONCEPT: {
			if (getFlexoRole() == null) {
				errorMessage = FLEXO_ROLE_IS_NULL;
			}
			if (getFlexoConcept() == null) {
				errorMessage = FLEXO_CONCEPT_IS_NULL;
			}
			return getFlexoConcept() != null && getFlexoRole() != null;
		}
		case CREATES_FLEXO_CONCEPT:
			switch (patternChoice) {
			case MAP_SINGLE_INDIVIDUAL:
				if (StringUtils.isEmpty(getFlexoConceptName())) {
					errorMessage = FLEXO_CONCEPT_NAME_IS_NULL;
				}
				if (StringUtils.isEmpty(getIndividualFlexoRoleName())) {
					errorMessage = INDIVIDUAL_FLEXO_ROLE_NAME_IS_NULL;
				}
				if (concept == null) {
					errorMessage = CONCEPT_IS_NULL;
				}
				if (getSelectedEntriesCount() == 0) {
					errorMessage = NO_SELECTED_ENTRY;
				}
				/*if (!editionSchemesNamedAreValid()) {
					errorMessage = A_SCHEME_NAME_IS_NOT_VALID;
				}*/
				return StringUtils.isNotEmpty(getFlexoConceptName()) && concept != null
						&& StringUtils.isNotEmpty(getIndividualFlexoRoleName()) && getSelectedEntriesCount() > 0;
				// && editionSchemesNamedAreValid();

			case MAP_SINGLE_FLEXO_CONCEPT:
				if (StringUtils.isEmpty(getFlexoConceptName())) {
					errorMessage = FLEXO_CONCEPT_NAME_IS_NULL;
				}
				if (StringUtils.isEmpty(getVirtualModelFlexoRoleName())) {
					errorMessage = VIRTUAL_MODEL_FLEXO_ROLE_NAME_IS_NULL;
				}
				if (virtualModelConcept == null) {
					errorMessage = VIRTUAL_MODEL_CONCEPT_IS_NULL;
				}
				if (getSelectedEntriesCount() == 0) {
					errorMessage = NO_SELECTED_ENTRY;
				}
				/*if (!editionSchemesNamedAreValid()) {
					errorMessage = A_SCHEME_NAME_IS_NOT_VALID;
				}*/
				return StringUtils.isNotEmpty(getFlexoConceptName()) && virtualModelConcept != null
						&& StringUtils.isNotEmpty(getVirtualModelFlexoRoleName()) && getSelectedEntriesCount() > 0;
				// && editionSchemesNamedAreValid();

			case BLANK_FLEXO_CONCEPT:
				if (StringUtils.isEmpty(getFlexoConceptName())) {
					errorMessage = FLEXO_CONCEPT_NAME_IS_NULL;
				}
				if (getSelectedEntriesCount() == 0) {
					errorMessage = NO_SELECTED_ENTRY;
				}
				/*if (!editionSchemesNamedAreValid()) {
					errorMessage = A_SCHEME_NAME_IS_NOT_VALID;
				}*/
				return StringUtils.isNotEmpty(getFlexoConceptName()) && getSelectedEntriesCount() > 0; // && editionSchemesNamedAreValid();
			default:
				break;
			}
		default:
			return false;
		}
	}

	private ShapeRole flexoRole;

	@Override
	public ShapeRole getFlexoRole() {
		return flexoRole;
	}

	public void setFlexoRole(ShapeRole flexoRole) {
		this.flexoRole = flexoRole;
	}

	@Override
	public List<ShapeRole> getAvailableFlexoRoles() {
		if (getFlexoConcept() != null) {
			return getFlexoConcept().getFlexoRoles(ShapeRole.class);
		}
		return null;
	}

	@Override
	public void resetFlexoRole() {
		this.flexoRole = null;
	}

	public IFlexoOntologyClass getConcept() {
		return concept;
	}

	public void setConcept(IFlexoOntologyClass concept) {
		this.concept = concept;
	}

	public FlexoConcept getVirtualModelConcept() {
		return virtualModelConcept;
	}

	public void setVirtualModelConcept(FlexoConcept virtualModelConcept) {
		this.virtualModelConcept = virtualModelConcept;
	}

	public String getDropSchemeName() {
		if (StringUtils.isEmpty(dropSchemeName)) {
			return "drop" + (StringUtils.isEmpty(getFlexoConceptName()) ? "" : getFlexoConceptName())
					+ (isTopLevel ? "AtTopLevel" : containerFlexoConcept != null ? "In" + containerFlexoConcept.getName() : "");
		}
		return dropSchemeName;
	}

	public void setDropSchemeName(String dropSchemeName) {
		this.dropSchemeName = dropSchemeName;
	}

	@Override
	public FlexoConcept getFlexoConcept() {
		if (primaryChoice == DeclareInFlexoConceptChoices.CREATES_FLEXO_CONCEPT) {
			return newFlexoConcept;
		}
		return super.getFlexoConcept();
	}

	public String getFlexoConceptName() {
		if (isTypeAwareModelSlot()) {
			if (StringUtils.isEmpty(flexoConceptName) && concept != null) {
				return concept.getName();
			}
		}
		if (isVirtualModelModelSlot()) {
			if (StringUtils.isEmpty(flexoConceptName) && virtualModelConcept != null) {
				return virtualModelConcept.getName();
			}
		}

		return flexoConceptName;
	}

	public void setFlexoConceptName(String flexoConceptName) {
		this.flexoConceptName = flexoConceptName;
	}

	public String getIndividualFlexoRoleName() {
		if (StringUtils.isEmpty(individualFlexoRoleName) && concept != null) {
			return JavaUtils.getVariableName(concept.getName());
		}
		return individualFlexoRoleName;
	}

	public void setIndividualFlexoRoleName(String individualFlexoRoleName) {
		this.individualFlexoRoleName = individualFlexoRoleName;
	}

	// public Vector<PropertyEntry> propertyEntries = new Vector<PropertyEntry>();

	/*
	public void selectNoneProperties() {
		for (PropertyEntry e : propertyEntries) {
			e.selectEntry = false;
		}
	}
	 */

	/*public class PropertyEntry {

		public IFlexoOntologyStructuralProperty property;
		public String label;
		public boolean selectEntry = false;

		public PropertyEntry(IFlexoOntologyStructuralProperty property) {
			this.property = property;
			if (StringUtils.isNotEmpty(property.getDescription())) {
				label = property.getDescription();
			} else {
				label = property.getName() + "_of_" + getIndividualFlexoRoleName();
			}
		}

		public String getRange() {
			return property.getRange().getName();
		}
	}*/

	/*private PropertyEntry selectBestEntryForURIBaseName() {
		Vector<PropertyEntry> candidates = new Vector<PropertyEntry>();
		for (PropertyEntry e : propertyEntries) {
			if (e.selectEntry && e.property instanceof IFlexoOntologyDataProperty
					&& ((IFlexoOntologyDataProperty) e.property).getRange().getBuiltInDataType() == BuiltInDataType.String) {
				candidates.add(e);
			}
		}
		if (candidates.size() > 0) {
			return candidates.firstElement();
		}
		return null;
	}*/

	/*public PropertyEntry createPropertyEntry() {
		PropertyEntry newPropertyEntry = new PropertyEntry(null);
		propertyEntries.add(newPropertyEntry);
		return newPropertyEntry;
	}

	public PropertyEntry deletePropertyEntry(PropertyEntry aPropertyEntry) {
		propertyEntries.remove(aPropertyEntry);
		return aPropertyEntry;
	}

	public void selectAllProperties() {
		for (PropertyEntry e : propertyEntries) {
			e.selectEntry = true;
		}

	 */

	public String getVirtualModelFlexoRoleName() {
		if (StringUtils.isEmpty(virtualModelFlexoRoleName) && virtualModelConcept != null) {
			return JavaUtils.getVariableName(virtualModelConcept.getName());
		}
		return virtualModelFlexoRoleName;
	}

	public void setVirtualModelFlexoRoleName(String virtualModelFlexoRoleName) {
		this.virtualModelFlexoRoleName = virtualModelFlexoRoleName;
	}

	public boolean isTopLevel() {
		return isTopLevel;
	}

	public void setTopLevel(boolean isTopLevel) {
		this.isTopLevel = isTopLevel;
	}

	public FlexoConcept getContainerFlexoConcept() {
		return containerFlexoConcept;
	}

	public void setContainerFlexoConcept(FlexoConcept containerFlexoConcept) {
		this.containerFlexoConcept = containerFlexoConcept;
	}

	@Override
	protected void doAction(Object context) {
		logger.info("Declare shape in flexo concept");
		if (isValid()) {
			switch (primaryChoice) {
			case CHOOSE_EXISTING_FLEXO_CONCEPT:
				if (getFlexoRole() != null) {
					getFlexoRole().updateGraphicalRepresentation(getFocusedObject().getGraphicalRepresentation());
				}
				break;
			case CREATES_FLEXO_CONCEPT:
				// DiagramShape diagramShape = getFocusedObject();

				/*VirtualModel.VirtualModelBuilder builder = new VirtualModel.VirtualModelBuilder(getFocusedObject()
						.getDiagramSpecification().getViewPointLibrary(), getVirtualModel().getViewPoint(),
						getVirtualModel().getResource());*/

				switch (patternChoice) {
				case MAP_SINGLE_INDIVIDUAL:
				case MAP_SINGLE_FLEXO_CONCEPT:
				case BLANK_FLEXO_CONCEPT:

					// Create new flexo concept
					newFlexoConcept = getVirtualModel().getVirtualModelFactory().newFlexoConcept();
					newFlexoConcept.setName(getFlexoConceptName());

					// And add the newly created flexo concept
					getVirtualModel().addToFlexoConcepts(newFlexoConcept);

					// Find best URI base candidate
					// PropertyEntry mainPropertyDescriptor = selectBestEntryForURIBaseName();

					// Create pattern role, if it is an ontology then we create an individual, otherwise if it is a virtual model we create
					// an flexo concept instance
					individualFlexoRole = null;
					FlexoConceptInstanceRole flexoConceptFlexoRole = null;
					if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
						if (isTypeAwareModelSlot()) {
							TypeAwareModelSlot<?, ?> flexoOntologyModelSlot = (TypeAwareModelSlot<?, ?>) getModelSlot();
							individualFlexoRole = flexoOntologyModelSlot.makeIndividualRole(getConcept());
							individualFlexoRole.setRoleName(getIndividualFlexoRoleName());
							individualFlexoRole.setOntologicType(getConcept());
							newFlexoConcept.addToFlexoRoles(individualFlexoRole);
							// newFlexoConcept.setPrimaryConceptRole(individualFlexoRole);
						}
					}
					if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_FLEXO_CONCEPT) {
						if (isVirtualModelModelSlot()) {
							FMLRTModelSlot virtualModelModelSlot = (FMLRTModelSlot) getModelSlot();
							flexoConceptFlexoRole = virtualModelModelSlot.makeFlexoConceptInstanceRole(getVirtualModelConcept());
							flexoConceptFlexoRole.setRoleName(getVirtualModelFlexoRoleName());
							newFlexoConcept.addToFlexoRoles(flexoConceptFlexoRole);
						}
					}

					// Create graphical elements pattern role

					newGraphicalElementRoles = new LinkedHashMap<DrawingObjectEntry, GraphicalElementRole>();

					GraphicalElementRole primaryRepresentationRole = null;
					for (DrawingObjectEntry entry : drawingObjectEntries) {
						if (entry.getSelectThis()) {
							if (entry.graphicalObject instanceof DiagramShape) {
								DiagramShape grShape = (DiagramShape) entry.graphicalObject;
								ShapeRole newShapeRole = getFactory().newInstance(ShapeRole.class);
								newShapeRole.setRoleName(entry.flexoRoleName);
								newShapeRole.setModelSlot(getTypedDiagramModelSlot());
								/*if (mainPropertyDescriptor != null && entry.isMainEntry()) {
									newShapeFlexoRole.setLabel(new DataBinding<String>(getIndividualFlexoRoleName() + "."
											+ mainPropertyDescriptor.property.getName()));
								} else {*/
								newShapeRole.setReadOnlyLabel(true);
								if (StringUtils.isNotEmpty(entry.graphicalObject.getName())) {
									newShapeRole.setLabel(new DataBinding<String>("\"" + entry.graphicalObject.getName() + "\""));
								}
								// }
								newShapeRole.setExampleLabel(grShape.getGraphicalRepresentation().getText());
								// We clone here the GR (fixed unfocusable GR bug)
								newShapeRole.setGraphicalRepresentation((ShapeGraphicalRepresentation) grShape.getGraphicalRepresentation()
										.clone());
								// Forces GR to be displayed in view
								newShapeRole.getGraphicalRepresentation().setAllowToLeaveBounds(false);
								newFlexoConcept.addToFlexoRoles(newShapeRole);
								if (entry.getParentEntry() != null) {
									newShapeRole.setParentShapeRole((ShapeRole) newGraphicalElementRoles.get(entry.getParentEntry()));
								}
								if (entry.isMainEntry()) {
									primaryRepresentationRole = newShapeRole;
								}
								newGraphicalElementRoles.put(entry, newShapeRole);
							}
							if (entry.graphicalObject instanceof DiagramConnector) {
								DiagramConnector grConnector = (DiagramConnector) entry.graphicalObject;
								ConnectorRole newConnectorRole = getFactory().newInstance(ConnectorRole.class);
								newConnectorRole.setRoleName(entry.flexoRoleName);
								newConnectorRole.setReadOnlyLabel(true);
								if (StringUtils.isNotEmpty(entry.graphicalObject.getName())) {
									newConnectorRole.setLabel(new DataBinding<String>("\"" + entry.graphicalObject.getName() + "\""));
								}
								newConnectorRole.setExampleLabel(grConnector.getGraphicalRepresentation().getText());
								newConnectorRole.setGraphicalRepresentation((ConnectorGraphicalRepresentation) grConnector
										.getGraphicalRepresentation().clone());
								newFlexoConcept.addToFlexoRoles(newConnectorRole);
								// Set the source/target
								// newConnectorFlexoRole.setEndShapeFlexoRole(endShapeFlexoRole);
								if (entry.isMainEntry()) {
									primaryRepresentationRole = newConnectorRole;
								}
								newGraphicalElementRoles.put(entry, newConnectorRole);
							}

						}
					}
					// newFlexoConcept.setPrimaryRepresentationRole(primaryRepresentationRole);

					/*	if (isPushedToPalette) {
							DiagramPaletteElement _newPaletteElement = palette.addPaletteElement(newFlexoConcept.getName(),
									getFocusedObject().getGraphicalRepresentation());
							_newPaletteElement.setFlexoConcept(newFlexoConcept);
						}*/

					// Create other individual roles
					/*Vector<IndividualRole> otherRoles = new Vector<IndividualRole>();
					if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
						for (PropertyEntry e : propertyEntries) {
							if (e.selectEntry) {
								if (e.property instanceof IFlexoOntologyObjectProperty) {
									IFlexoOntologyConcept range = ((IFlexoOntologyObjectProperty) e.property).getRange();
									if (range instanceof IFlexoOntologyClass) {
										IndividualRole newFlexoRole = null; // new IndividualRole(builder);
										newFlexoRole.setFlexoRoleName(e.property.getName());
										newFlexoRole.setOntologicType((IFlexoOntologyClass) range);
										newFlexoConcept.addToFlexoRoles(newFlexoRole);
										otherRoles.add(newFlexoRole);
									}
								}
							}
						}
					}*/

					// Create new drop scheme
					DropScheme newDropScheme = getFactory().newInstance(DropScheme.class);
					newDropScheme.setName(getDropSchemeName());

					// Add new drop scheme
					newFlexoConcept.addToFlexoBehaviours(newDropScheme);

					newDropScheme.setTopTarget(isTopLevel);
					if (!isTopLevel) {
						newDropScheme.setTargetFlexoConcept(containerFlexoConcept);
					}

					// Parameters
					if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
						if (isTypeAwareModelSlot()) {
							TypeAwareModelSlot<?, ?> flexoOntologyModelSlot = (TypeAwareModelSlot<?, ?>) getModelSlot();
							// Vector<PropertyEntry> candidates = new Vector<PropertyEntry>();
							/*for (PropertyEntry e : propertyEntries) {
								if (e != null && e.selectEntry) {
									FlexoBehaviourParameter newParameter = null;
									if (e.property instanceof IFlexoOntologyDataProperty) {
										switch (((IFlexoOntologyDataProperty) e.property).getRange().getBuiltInDataType()) {
										case Boolean:
											newParameter = new CheckboxParameter(builder);
											newParameter.setName(e.property.getName());
											newParameter.setLabel(e.label);
											break;
										case Byte:
										case Integer:
										case Long:
										case Short:
											newParameter = new IntegerParameter(builder);
											newParameter.setName(e.property.getName());
											newParameter.setLabel(e.label);
											break;
										case Double:
										case Float:
											newParameter = new FloatParameter(builder);
											newParameter.setName(e.property.getName());
											newParameter.setLabel(e.label);
											break;
										case String:
											newParameter = new TextFieldParameter(builder);
											newParameter.setName(e.property.getName());
											newParameter.setLabel(e.label);
											break;
										default:
											break;
										}
									} else if (e.property instanceof IFlexoOntologyObjectProperty) {
										IFlexoOntologyConcept range = ((IFlexoOntologyObjectProperty) e.property).getRange();
										if (range instanceof IFlexoOntologyClass) {
											newParameter = new IndividualParameter(builder);
											newParameter.setName(e.property.getName());
											newParameter.setLabel(e.label);
											((IndividualParameter) newParameter).setConcept((IFlexoOntologyClass) range);
										}
									}
									if (newParameter != null) {
										newDropScheme.addToParameters(newParameter);
									}
								}
							}*/

							URIParameter uriParameter = getFactory().newURIParameter();
							uriParameter.setName("uri");
							uriParameter.setLabel("uri");
							/*if (mainPropertyDescriptor != null) {
								uriParameter.setBaseURI(new DataBinding<String>(mainPropertyDescriptor.property.getName()));
							}*/
							newDropScheme.addToParameters(uriParameter);

							// Declare pattern role
							/*for (IndividualRole r : otherRoles) {
								DeclareFlexoRole action = new DeclareFlexoRole(builder);
								action.setAssignation(new DataBinding<Object>(r.getRoleName()));
								action.setObject(new DataBinding<Object>("parameters." + r.getName()));
								newDropScheme.addToActions(action);
							}*/

							// Add individual action
							AddIndividual<?, ?> newAddIndividual = flexoOntologyModelSlot.makeAddIndividualAction(individualFlexoRole,
									newDropScheme);

							/*AddIndividual newAddIndividual = new AddIndividual(builder);
							newAddIndividual.setAssignation(new ViewPointDataBinding(individualFlexoRole.getRoleName()));
							newAddIndividual.setIndividualName(new ViewPointDataBinding("parameters.uri"));
							for (PropertyEntry e : propertyEntries) {
								if (e.selectEntry) {
									if (e.property instanceof IFlexoOntologyObjectProperty) {
										IFlexoOntologyConcept range = ((IFlexoOntologyObjectProperty) e.property).getRange();
										if (range instanceof IFlexoOntologyClass) {
											ObjectPropertyAssertion propertyAssertion = new ObjectPropertyAssertion(builder);
											propertyAssertion.setOntologyProperty(e.property);
											propertyAssertion.setObject(new ViewPointDataBinding("parameters." + e.property.getName()));
											newAddIndividual.addToObjectAssertions(propertyAssertion);
										}
									} else if (e.property instanceof IFlexoOntologyDataProperty) {
										DataPropertyAssertion propertyAssertion = new DataPropertyAssertion(builder);
										propertyAssertion.setOntologyProperty(e.property);
										propertyAssertion.setValue(new ViewPointDataBinding("parameters." + e.property.getName()));
										newAddIndividual.addToDataAssertions(propertyAssertion);
									}
								}
							}*/
							newDropScheme.addToActions(newAddIndividual);
						}
					}

					// Parameters for flexo concepts creation action
					/*if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_FLEXO_CONCEPT) {
						if (isVirtualModelModelSlot()) {
							FMLRTModelSlot<?, ?> virtualModelModelSlot = (FMLRTModelSlot<?, ?>) getModelSlot();

							// Add individual action
							EditionAction newAddFlexoConcept = virtualModelModelSlot.makeAddFlexoConceptInstanceEditionAction(
									flexoConceptFlexoRole, newDropScheme);

							newDropScheme.addToActions(newAddFlexoConcept);
						}
					}*/

					// Add shape/connector actions
					boolean mainFlexoRole = true;
					for (GraphicalElementRole graphicalElementRole : newGraphicalElementRoles.values()) {
						if (graphicalElementRole instanceof ShapeRole) {
							ShapeRole grFlexoRole = (ShapeRole) graphicalElementRole;
							// Add shape action
							AddShape newAddShape;
							AssignationAction<DiagramShape> assignationAction = getFactory().newAssignationAction(
									newAddShape = getFactory().newInstance(AddShape.class));
							newDropScheme.addToActions(assignationAction);
							assignationAction.setAssignation(new DataBinding<Object>(graphicalElementRole.getRoleName()));
							if (mainFlexoRole) {
								if (isTopLevel) {
									newAddShape.setContainer(new DataBinding<DiagramContainerElement<?>>(
											DiagramBehaviourBindingModel.TOP_LEVEL));
								} /*else {
									newAddShape.setContainer(new DataBinding<DiagramElement<?>>(DiagramFlexoBehaviour.TARGET + "."
											+ containerFlexoConcept.getPrimaryRepresentationRole().getRoleName()));
									}*/
							} else {
								newAddShape.setContainer(new DataBinding<DiagramContainerElement<?>>(grFlexoRole.getParentShapeRole()
										.getRoleName()));
							}
							mainFlexoRole = false;
						}
					}

					// Add inspector
					FlexoConceptInspector inspector = newFlexoConcept.getInspector();
					inspector.setInspectorTitle(getFlexoConceptName());
					if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
						/*for (PropertyEntry e : propertyEntries) {
							if (e.selectEntry) {
								if (e.property instanceof IFlexoOntologyObjectProperty) {
									IFlexoOntologyConcept range = ((IFlexoOntologyObjectProperty) e.property).getRange();
									if (range instanceof IFlexoOntologyClass) {
										InspectorEntry newInspectorEntry = null;
										newInspectorEntry = new TextFieldInspectorEntry(builder);
										newInspectorEntry.setName(e.property.getName());
										newInspectorEntry.setLabel(e.label);
										newInspectorEntry.setIsReadOnly(true);
										newInspectorEntry.setData(new DataBinding<Object>(e.property.getName() + ".uriName"));
										inspector.addToEntries(newInspectorEntry);
									}
								} else if (e.property instanceof IFlexoOntologyDataProperty) {
									InspectorEntry newInspectorEntry = null;
									switch (((IFlexoOntologyDataProperty) e.property).getRange().getBuiltInDataType()) {
									case Boolean:
										newInspectorEntry = new CheckboxInspectorEntry(builder);
										break;
									case Byte:
									case Integer:
									case Long:
									case Short:
										newInspectorEntry = new IntegerInspectorEntry(builder);
										break;
									case Double:
									case Float:
										newInspectorEntry = new FloatInspectorEntry(builder);
										break;
									case String:
										newInspectorEntry = new TextFieldInspectorEntry(builder);
										break;
									default:
										logger.warning("Not handled: " + ((IFlexoOntologyDataProperty) e.property).getRange());
									}
									if (newInspectorEntry != null) {
										newInspectorEntry.setName(e.property.getName());
										newInspectorEntry.setLabel(e.label);
										newInspectorEntry.setData(new DataBinding<Object>(getIndividualFlexoRoleName() + "."
												+ e.property.getName()));
										inspector.addToEntries(newInspectorEntry);
									}
								}
							}
						}*/
					}

				default:
					break;
				}
			default:
				logger.warning("Pattern not implemented");
			}
		} else {
			logger.warning("Focused role is null !");
		}
	}

	private void createSchemeActions(FlexoBehaviourConfiguration editionSchemeConfiguration) {
		FlexoBehaviour editionScheme = null;

		// Delete shapes as well as model
		if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DELETE_GR_AND_MODEL) {
			editionScheme = createDeleteFlexoBehaviourActions(editionSchemeConfiguration, false);
		}

		// Delete only shapes
		if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DELETE_GR_ONLY) {
			editionScheme = createDeleteFlexoBehaviourActions(editionSchemeConfiguration, true);
		}

		// Drop
		if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DROP_AND_SELECT
				|| editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DROP_AND_CREATE) {
			editionScheme = createDropFlexoBehaviourActions(editionSchemeConfiguration);
		}

		newFlexoConcept.addToFlexoBehaviours(editionScheme);
	}

	private FlexoBehaviour createDeleteFlexoBehaviourActions(FlexoBehaviourConfiguration editionSchemeConfiguration, boolean shapeOnly) {

		DeletionScheme editionScheme = (DeletionScheme) editionSchemeConfiguration.getFlexoBehaviour();

		Vector<FlexoRole> rolesToDelete = new Vector<FlexoRole>();
		if (shapeOnly) {
			for (FlexoRole pr : newFlexoConcept.getFlexoRoles()) {
				if (pr instanceof GraphicalElementRole) {
					rolesToDelete.add(pr);
				}
			}
		} else {
			for (FlexoRole pr : newFlexoConcept.getFlexoRoles()) {
				rolesToDelete.add(pr);
			}
		}

		Collections.sort(rolesToDelete, new Comparator<FlexoRole>() {
			@Override
			public int compare(FlexoRole o1, FlexoRole o2) {
				if (o1 instanceof ShapeRole && o2 instanceof ConnectorRole) {
					return 1;
				} else if (o1 instanceof ConnectorRole && o2 instanceof ShapeRole) {
					return -1;
				}

				if (o1 instanceof ShapeRole) {
					if (o2 instanceof ShapeRole) {
						if (((ShapeRole) o1).isContainedIn((ShapeRole) o2)) {
							return -1;
						}
						if (((ShapeRole) o2).isContainedIn((ShapeRole) o1)) {
							return 1;
						}
						return 0;
					}
				}
				return 0;
			}

		});
		for (FlexoRole pr : rolesToDelete) {
			DeleteAction a = getFactory().newDeleteAction();
			a.setObject(new DataBinding<Object>(pr.getRoleName()));
			editionScheme.addToActions(a);
		}
		return editionScheme;
	}

	private FlexoBehaviour createDropFlexoBehaviourActions(FlexoBehaviourConfiguration editionSchemeConfiguration) {
		// Create new drop scheme
		DropScheme editionScheme = (DropScheme) editionSchemeConfiguration.getFlexoBehaviour();

		// Parameters
		if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
			if (isTypeAwareModelSlot()) {
				TypeAwareModelSlot<?, ?> flexoOntologyModelSlot = (TypeAwareModelSlot<?, ?>) getModelSlot();

				if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DROP_AND_SELECT) {
					IndividualParameter individualParameter = getFactory().newIndividualParameter();
					individualParameter.setName(individualFlexoRole.getName());
					individualParameter.setLabel(individualFlexoRole.getName());
					individualParameter.setModelSlot(individualFlexoRole.getModelSlot());
					individualParameter.setConcept(individualFlexoRole.getOntologicType());
					editionScheme.addToParameters(individualParameter);
					// Add individual action
					AssignationAction<?> declareFlexoRole = getFactory().newAssignationAction(
							getFactory().newExpressionAction(new DataBinding<Object>("parameters." + individualParameter.getName())));
					declareFlexoRole.setAssignation(new DataBinding<Object>(individualFlexoRole.getName()));
					editionScheme.addToActions(declareFlexoRole);
				}
				if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DROP_AND_CREATE) {
					URIParameter uriParameter = getFactory().newURIParameter();
					uriParameter.setName("uri");
					uriParameter.setLabel("uri");
					editionScheme.addToParameters(uriParameter);
					// Add individual action
					AddIndividual<?, ?> newAddIndividual = flexoOntologyModelSlot.makeAddIndividualAction(individualFlexoRole,
							editionScheme);
					editionScheme.addToActions(newAddIndividual);
				}
			}
		}

		if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_FLEXO_CONCEPT) {
			if (isVirtualModelModelSlot()) {
				FMLRTModelSlot virtualModelModelSlot = (FMLRTModelSlot) getModelSlot();

				if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DROP_AND_SELECT) {
					FlexoConceptInstanceParameter flexoConceptInstanceParameter = getFactory().newFlexoConceptInstanceParameter();
					flexoConceptInstanceParameter.setName(flexoConceptInstanceRole.getName());
					flexoConceptInstanceParameter.setLabel(flexoConceptInstanceRole.getName());
					flexoConceptInstanceParameter.setModelSlot((FMLRTModelSlot) flexoConceptInstanceRole.getModelSlot());
					// editionPatternInstanceParameter.setFlexoConceptType(editionPatternFlexoRole.getFlexoConceptType());
					editionScheme.addToParameters(flexoConceptInstanceParameter);
					// Add individual action
					AssignationAction<?> declareFlexoRole = getFactory().newAssignationAction(
							new DataBinding<Object>("parameters." + flexoConceptInstanceParameter.getName()));
					declareFlexoRole.setAssignation(new DataBinding<Object>(flexoConceptInstanceRole.getName()));
					editionScheme.addToActions(declareFlexoRole);
				}
				if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DROP_AND_CREATE) {
					// Add individual action
					AddFlexoConceptInstance newAddFlexoConceptInstance = virtualModelModelSlot
							.makeEditionAction(AddFlexoConceptInstance.class);
					newAddFlexoConceptInstance.setFlexoConceptType(flexoConceptInstanceRole.getFlexoConceptType());
					AssignationAction<FlexoConceptInstance> newAddFlexoConceptInstanceAssignation = getFactory().newAssignationAction(
							newAddFlexoConceptInstance);
					newAddFlexoConceptInstanceAssignation.setAssignation(new DataBinding<Object>(flexoConceptInstanceRole.getName()));
					editionScheme.addToActions(newAddFlexoConceptInstance);
				}
			}
		}

		// Add shape/connector actions
		boolean mainFlexoRole = true;
		for (GraphicalElementRole<?, ?> graphicalElementFlexoRole : newGraphicalElementRoles.values()) {
			if (graphicalElementFlexoRole instanceof ShapeRole) {
				ShapeRole grFlexoRole = (ShapeRole) graphicalElementFlexoRole;
				// Add shape action
				AddShape newAddShape;
				AssignationAction<DiagramShape> assignationAction = getFactory().newAssignationAction(
						newAddShape = getFactory().newInstance(AddShape.class));
				assignationAction.setAssignation(new DataBinding<Object>(graphicalElementFlexoRole.getRoleName()));
				editionScheme.addToActions(assignationAction);
				if (mainFlexoRole) {
					if (editionScheme.isTopTarget()) {
						newAddShape.setContainer(new DataBinding<DiagramContainerElement<?>>(DiagramBehaviourBindingModel.TOP_LEVEL));
					} else {
						ShapeRole containerRole = getVirtualModel().getFlexoConcept(editionScheme._getTarget())
								.getFlexoRoles(ShapeRole.class).get(0);
						newAddShape.setContainer(new DataBinding<DiagramContainerElement<?>>(DropSchemeBindingModel.TARGET + "."
								+ containerRole.getRoleName()));
					}
				} else {
					newAddShape.setContainer(new DataBinding<DiagramContainerElement<?>>(grFlexoRole.getParentShapeRole().getRoleName()));
				}
				mainFlexoRole = false;
			}
		}
		return editionScheme;
	}

	/*public DropScheme getDropScheme(FlexoBehaviourConfiguration conf) {
		if (conf != null) {
			if (conf.getFlexoBehaviour() instanceof DropScheme) {
				selectedDropScheme = (DropScheme) conf.getFlexoBehaviour();
				return selectedDropScheme;
			}
		}
		return null;
	}*/

	/*public void addFlexoBehaviourConfigurationDropAndCreate() {
		FlexoBehaviourConfiguration editionSchemeConfiguration = new FlexoBehaviourConfiguration(FlexoBehaviourChoice.DROP_AND_CREATE);
		getFlexoBehaviours().add(editionSchemeConfiguration);
	}

	public void addFlexoBehaviourConfigurationDropAndSelect() {
		FlexoBehaviourConfiguration editionSchemeConfiguration = new FlexoBehaviourConfiguration(FlexoBehaviourChoice.DROP_AND_SELECT);
		getFlexoBehaviours().add(editionSchemeConfiguration);
	}*/

	@Override
	public void initializeBehaviours() {
		/*	FlexoBehaviourConfiguration dropFlexoBehaviour = new FlexoBehaviourConfiguration(FlexoBehaviourChoice.DROP_AND_CREATE);
			if (dropFlexoBehaviour.getFlexoBehaviour() != null) {
				dropFlexoBehaviour.getFlexoBehaviour().setName("drop");
			}
			getFlexoBehaviours().add(dropFlexoBehaviour);*/
	}

	/*
		private List<FlexoConcept> editionPatternsDropList;

		public List<FlexoConcept> getFlexoConceptsDrop() {
			if (editionPatternsDropList == null) {
				editionPatternsDropList = new ArrayList<FlexoConcept>();
				if(getVirtualModel()!=null){
					editionPatternsDropList.addAll(getVirtualModel().getFlexoConcepts());
				}
			}
			if (selectedDropScheme != null && getVirtualModel().getFlexoConcept(selectedDropScheme._getTarget()) != null) {
				FlexoConcept currentFromEp = getVirtualModel().getFlexoConcept(selectedDropScheme._getTarget());
				FlexoConcept firstEp = editionPatternsDropList.get(0);
				if (!currentFromEp.equals(firstEp)) {
					int lastIndex = editionPatternsDropList.indexOf(currentFromEp);
					;
					editionPatternsDropList.set(0, currentFromEp);
					editionPatternsDropList.set(lastIndex, firstEp);
				}
			}
			return editionPatternsDropList;
		}*/

	public DropScheme getDropScheme() {
		return dropScheme;
	}

	public void setDropScheme(DropScheme dropScheme) {
		this.dropScheme = dropScheme;
	}

}
