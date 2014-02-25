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

import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.FlexoConceptInstancePatternRole;
import org.openflexo.foundation.viewpoint.IndividualPatternRole;
import org.openflexo.foundation.viewpoint.URIParameter;
import org.openflexo.foundation.viewpoint.VirtualModelModelSlot;
import org.openflexo.foundation.viewpoint.editionaction.AddIndividual;
import org.openflexo.foundation.viewpoint.inspector.FlexoConceptInspector;
import org.openflexo.technologyadapter.diagram.fml.ConnectorPatternRole;
import org.openflexo.technologyadapter.diagram.fml.DiagramEditionScheme;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementPatternRole;
import org.openflexo.technologyadapter.diagram.fml.ShapePatternRole;
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
			"declare_in_flexo_concept", FlexoActionType.editGroup, FlexoActionType.NORMAL_ACTION_TYPE) {

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

	public NewFlexoConceptChoices patternChoice = NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL;

	private String flexoConceptName;
	private IFlexoOntologyClass concept;
	private String individualPatternRoleName;
	private String virtualModelPatternRoleName;
	private FlexoConcept newFlexoConcept;
	private LinkedHashMap<DrawingObjectEntry, GraphicalElementPatternRole> newGraphicalElementPatternRoles;
	// public DiagramPalette palette;

	private boolean isTopLevel = true;
	// public boolean isPushedToPalette = false;
	private FlexoConcept containerFlexoConcept;
	private FlexoConcept virtualModelConcept;
	private String dropSchemeName;

	DeclareShapeInFlexoConcept(DiagramShape focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public boolean isValid() {
		if (getFocusedObject() == null) {
			return false;
		}
		switch (primaryChoice) {
		case CHOOSE_EXISTING_FLEXO_CONCEPT:
			return getFlexoConcept() != null && getPatternRole() != null;
		case CREATES_FLEXO_CONCEPT:
			switch (patternChoice) {
			case MAP_SINGLE_INDIVIDUAL:
				return StringUtils.isNotEmpty(getFlexoConceptName()) && concept != null
						&& StringUtils.isNotEmpty(getIndividualPatternRoleName()) && getSelectedEntriesCount() > 0
						&& (isTopLevel || containerFlexoConcept != null) && StringUtils.isNotEmpty(getDropSchemeName());
			case MAP_SINGLE_FLEXO_CONCEPT:
				return StringUtils.isNotEmpty(getFlexoConceptName()) && virtualModelConcept != null
						&& StringUtils.isNotEmpty(getVirtualModelPatternRoleName()) && getSelectedEntriesCount() > 0
						&& (isTopLevel || containerFlexoConcept != null) && StringUtils.isNotEmpty(getDropSchemeName());
			case BLANK_FLEXO_CONCEPT:
				return StringUtils.isNotEmpty(getFlexoConceptName()) && getSelectedEntriesCount() > 0
						&& (isTopLevel || containerFlexoConcept != null) && StringUtils.isNotEmpty(getDropSchemeName());
			default:
				break;
			}
		default:
			return false;
		}
	}

	private ShapePatternRole patternRole;

	@Override
	public ShapePatternRole getPatternRole() {
		return patternRole;
	}

	public void setPatternRole(ShapePatternRole patternRole) {
		this.patternRole = patternRole;
	}

	@Override
	public void resetPatternRole() {
		this.patternRole = null;
	}

	public IFlexoOntologyClass getConcept() {
		return concept;
	}

	public void setConcept(IFlexoOntologyClass concept) {
		this.concept = concept;
		/*propertyEntries.clear();
		IFlexoOntology ownerOntology = concept.getOntology();
		for (IFlexoOntologyFeature p : concept.getPropertiesTakingMySelfAsDomain()) {
			if (p.getOntology() == ownerOntology && p instanceof IFlexoOntologyStructuralProperty) {
				PropertyEntry newEntry = new PropertyEntry((IFlexoOntologyStructuralProperty) p);
				propertyEntries.add(newEntry);
			}
		}*/
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

	public String getIndividualPatternRoleName() {
		if (StringUtils.isEmpty(individualPatternRoleName) && concept != null) {
			return JavaUtils.getVariableName(concept.getName());
		}
		return individualPatternRoleName;
	}

	public void setIndividualPatternRoleName(String individualPatternRoleName) {
		this.individualPatternRoleName = individualPatternRoleName;
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
				label = property.getName() + "_of_" + getIndividualPatternRoleName();
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

	public String getVirtualModelPatternRoleName() {
		if (StringUtils.isEmpty(virtualModelPatternRoleName) && virtualModelConcept != null) {
			return JavaUtils.getVariableName(virtualModelConcept.getName());
		}
		return virtualModelPatternRoleName;
	}

	public void setVirtualModelPatternRoleName(String virtualModelPatternRoleName) {
		this.virtualModelPatternRoleName = virtualModelPatternRoleName;
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
				if (getPatternRole() != null) {
					getPatternRole().updateGraphicalRepresentation(getFocusedObject().getGraphicalRepresentation());
				}
				break;
			case CREATES_FLEXO_CONCEPT:
				// DiagramShape diagramShape = getFocusedObject();

				/*VirtualModel.VirtualModelBuilder builder = new VirtualModel.VirtualModelBuilder(getFocusedObject()
						.getDiagramSpecification().getViewPointLibrary(), getFocusedObject().getDiagramSpecification().getViewPoint(),
						getFocusedObject().getDiagramSpecification().getResource());*/

				switch (patternChoice) {
				case MAP_SINGLE_INDIVIDUAL:
				case MAP_SINGLE_FLEXO_CONCEPT:
				case BLANK_FLEXO_CONCEPT:

					// Create new flexo concept
					newFlexoConcept = getFactory().newFlexoConcept();
					newFlexoConcept.setName(getFlexoConceptName());

					// And add the newly created flexo concept
					getDiagramModelSlot().getVirtualModel().addToFlexoConcepts(newFlexoConcept);

					// Find best URI base candidate
					// PropertyEntry mainPropertyDescriptor = selectBestEntryForURIBaseName();

					// Create pattern role, if it is an ontology then we create an individual, otherwise if it is a virtual model we create
					// an flexo concept instance
					IndividualPatternRole<?> individualPatternRole = null;
					FlexoConceptInstancePatternRole flexoConceptPatternRole = null;
					if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
						if (isTypeAwareModelSlot()) {
							TypeAwareModelSlot<?, ?> flexoOntologyModelSlot = (TypeAwareModelSlot<?, ?>) getModelSlot();
							individualPatternRole = flexoOntologyModelSlot.makeIndividualPatternRole(getConcept());
							individualPatternRole.setPatternRoleName(getIndividualPatternRoleName());
							individualPatternRole.setOntologicType(getConcept());
							newFlexoConcept.addToPatternRoles(individualPatternRole);
							// newFlexoConcept.setPrimaryConceptRole(individualPatternRole);
						}
					}
					if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_FLEXO_CONCEPT) {
						if (isVirtualModelModelSlot()) {
							VirtualModelModelSlot virtualModelModelSlot = (VirtualModelModelSlot) getModelSlot();
							flexoConceptPatternRole = virtualModelModelSlot
									.makeFlexoConceptInstancePatternRole(getVirtualModelConcept());
							flexoConceptPatternRole.setPatternRoleName(getVirtualModelPatternRoleName());
							newFlexoConcept.addToPatternRoles(flexoConceptPatternRole);
						}
					}

					// Create graphical elements pattern role

					newGraphicalElementPatternRoles = new LinkedHashMap<DrawingObjectEntry, GraphicalElementPatternRole>();

					GraphicalElementPatternRole primaryRepresentationRole = null;
					for (DrawingObjectEntry entry : drawingObjectEntries) {
						if (entry.getSelectThis()) {
							if (entry.graphicalObject instanceof DiagramShape) {
								DiagramShape grShape = (DiagramShape) entry.graphicalObject;
								ShapePatternRole newShapePatternRole = getFactory().newInstance(ShapePatternRole.class);
								newShapePatternRole.setPatternRoleName(entry.patternRoleName);
								/*if (mainPropertyDescriptor != null && entry.isMainEntry()) {
									newShapePatternRole.setLabel(new DataBinding<String>(getIndividualPatternRoleName() + "."
											+ mainPropertyDescriptor.property.getName()));
								} else {*/
								newShapePatternRole.setReadOnlyLabel(true);
								if (StringUtils.isNotEmpty(entry.graphicalObject.getName())) {
									newShapePatternRole.setLabel(new DataBinding<String>("\"" + entry.graphicalObject.getName() + "\""));
								}
								// }
								newShapePatternRole.setExampleLabel(grShape.getGraphicalRepresentation().getText());
								// We clone here the GR (fixed unfocusable GR bug)
								newShapePatternRole.setGraphicalRepresentation((ShapeGraphicalRepresentation) grShape
										.getGraphicalRepresentation().clone());
								// Forces GR to be displayed in view
								newShapePatternRole.getGraphicalRepresentation().setAllowToLeaveBounds(false);
								newFlexoConcept.addToPatternRoles(newShapePatternRole);
								if (entry.getParentEntry() != null) {
									newShapePatternRole.setParentShapePatternRole((ShapePatternRole) newGraphicalElementPatternRoles
											.get(entry.getParentEntry()));
								}
								if (entry.isMainEntry()) {
									primaryRepresentationRole = newShapePatternRole;
								}
								newGraphicalElementPatternRoles.put(entry, newShapePatternRole);
							}
							if (entry.graphicalObject instanceof DiagramConnector) {
								DiagramConnector grConnector = (DiagramConnector) entry.graphicalObject;
								ConnectorPatternRole newConnectorPatternRole = getFactory().newInstance(ConnectorPatternRole.class);
								newConnectorPatternRole.setPatternRoleName(entry.patternRoleName);
								newConnectorPatternRole.setReadOnlyLabel(true);
								if (StringUtils.isNotEmpty(entry.graphicalObject.getName())) {
									newConnectorPatternRole
											.setLabel(new DataBinding<String>("\"" + entry.graphicalObject.getName() + "\""));
								}
								newConnectorPatternRole.setExampleLabel(grConnector.getGraphicalRepresentation().getText());
								newConnectorPatternRole.setGraphicalRepresentation((ConnectorGraphicalRepresentation) grConnector
										.getGraphicalRepresentation().clone());
								newFlexoConcept.addToPatternRoles(newConnectorPatternRole);
								// Set the source/target
								// newConnectorPatternRole.setEndShapePatternRole(endShapePatternRole);
								if (entry.isMainEntry()) {
									primaryRepresentationRole = newConnectorPatternRole;
								}
								newGraphicalElementPatternRoles.put(entry, newConnectorPatternRole);
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
					/*Vector<IndividualPatternRole> otherRoles = new Vector<IndividualPatternRole>();
					if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
						for (PropertyEntry e : propertyEntries) {
							if (e.selectEntry) {
								if (e.property instanceof IFlexoOntologyObjectProperty) {
									IFlexoOntologyConcept range = ((IFlexoOntologyObjectProperty) e.property).getRange();
									if (range instanceof IFlexoOntologyClass) {
										IndividualPatternRole newPatternRole = null; // new IndividualPatternRole(builder);
										newPatternRole.setPatternRoleName(e.property.getName());
										newPatternRole.setOntologicType((IFlexoOntologyClass) range);
										newFlexoConcept.addToPatternRoles(newPatternRole);
										otherRoles.add(newPatternRole);
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
							/*for (IndividualPatternRole r : otherRoles) {
								DeclarePatternRole action = new DeclarePatternRole(builder);
								action.setAssignation(new DataBinding<Object>(r.getPatternRoleName()));
								action.setObject(new DataBinding<Object>("parameters." + r.getName()));
								newDropScheme.addToActions(action);
							}*/

							// Add individual action
							AddIndividual<?, ?> newAddIndividual = flexoOntologyModelSlot.makeAddIndividualAction(individualPatternRole,
									newDropScheme);

							/*AddIndividual newAddIndividual = new AddIndividual(builder);
							newAddIndividual.setAssignation(new ViewPointDataBinding(individualPatternRole.getPatternRoleName()));
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
							VirtualModelModelSlot<?, ?> virtualModelModelSlot = (VirtualModelModelSlot<?, ?>) getModelSlot();

							// Add individual action
							EditionAction newAddFlexoConcept = virtualModelModelSlot.makeAddFlexoConceptInstanceEditionAction(
									flexoConceptPatternRole, newDropScheme);

							newDropScheme.addToActions(newAddFlexoConcept);
						}
					}*/

					// Add shape/connector actions
					boolean mainPatternRole = true;
					for (GraphicalElementPatternRole graphicalElementPatternRole : newGraphicalElementPatternRoles.values()) {
						if (graphicalElementPatternRole instanceof ShapePatternRole) {
							ShapePatternRole grPatternRole = (ShapePatternRole) graphicalElementPatternRole;
							// Add shape action
							AddShape newAddShape = getFactory().newInstance(AddShape.class);
							newDropScheme.addToActions(newAddShape);
							newAddShape.setAssignation(new DataBinding<Object>(graphicalElementPatternRole.getPatternRoleName()));
							if (mainPatternRole) {
								if (isTopLevel) {
									newAddShape.setContainer(new DataBinding<DiagramContainerElement<?>>(DiagramEditionScheme.TOP_LEVEL));
								} /*else {
									newAddShape.setContainer(new DataBinding<DiagramElement<?>>(DiagramEditionScheme.TARGET + "."
											+ containerFlexoConcept.getPrimaryRepresentationRole().getPatternRoleName()));
									}*/
							} else {
								newAddShape.setContainer(new DataBinding<DiagramContainerElement<?>>(grPatternRole
										.getParentShapePatternRole().getPatternRoleName()));
							}
							mainPatternRole = false;
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
										newInspectorEntry.setData(new DataBinding<Object>(getIndividualPatternRoleName() + "."
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

	/*public class PropertyEntry {

		public IFlexoOntologyStructuralProperty property;
		public String label;
		public boolean selectEntry = false;

		public PropertyEntry(IFlexoOntologyStructuralProperty property) {
			this.property = property;
			if (StringUtils.isNotEmpty(property.getDescription())) {
				label = property.getDescription();
			} else {
				label = property.getName() + "_of_" + getIndividualPatternRoleName();
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
	}

	public void selectNoneProperties() {
		for (PropertyEntry e : propertyEntries) {
			e.selectEntry = false;
		}
	}
	 */

}
