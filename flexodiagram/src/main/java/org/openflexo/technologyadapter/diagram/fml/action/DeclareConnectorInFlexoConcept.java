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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.fml.IndividualRole;
import org.openflexo.foundation.fml.URIParameter;
import org.openflexo.foundation.fml.editionaction.AddIndividual;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.editionaction.ExpressionAction;
import org.openflexo.foundation.fml.inspector.FlexoConceptInspector;
import org.openflexo.foundation.fml.rt.FMLRTModelSlot;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.binding.LinkSchemeBindingModel;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
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
public class DeclareConnectorInFlexoConcept extends DeclareInFlexoConcept<DeclareConnectorInFlexoConcept, DiagramConnector> {

	private static final Logger logger = Logger.getLogger(DeclareConnectorInFlexoConcept.class.getPackage().getName());

	public static FlexoActionType<DeclareConnectorInFlexoConcept, DiagramConnector, DiagramElement<?>> actionType = new FlexoActionType<DeclareConnectorInFlexoConcept, DiagramConnector, DiagramElement<?>>(
			"declare_in_flexo_concept", FlexoActionType.defaultGroup, FlexoActionType.NORMAL_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public DeclareConnectorInFlexoConcept makeNewAction(DiagramConnector focusedObject, Vector<DiagramElement<?>> globalSelection,
				FlexoEditor editor) {
			return new DeclareConnectorInFlexoConcept(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(DiagramConnector connector, Vector<DiagramElement<?>> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(DiagramConnector connector, Vector<DiagramElement<?>> globalSelection) {
			return connector != null /*&& connector.getDiagramSpecification().getFlexoConcepts().size() > 0*/;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(DeclareConnectorInFlexoConcept.actionType, DiagramConnector.class);
	}

	public static enum NewFlexoConceptChoices {
		MAP_SINGLE_INDIVIDUAL, MAP_OBJECT_PROPERTY, MAP_SINGLE_FLEXO_CONCEPT, BLANK_FLEXO_CONCEPT
	}

	private static final String PATTERN_ROLE_IS_NULL = FlexoLocalization.localizedForKey("pattern_role_is_null");
	private static final String EDITION_PATTERN_IS_NULL = FlexoLocalization.localizedForKey("edition_pattern_is_null");
	private static final String EDITION_PATTERN_NAME_IS_NULL = FlexoLocalization.localizedForKey("edition_pattern_name_is_null");
	private static final String FOCUSED_OBJECT_IS_NULL = FlexoLocalization.localizedForKey("focused_object_is_null");
	private static final String INDIVIDUAL_PATTERN_ROLE_NAME_IS_NULL = FlexoLocalization
			.localizedForKey("individual_pattern_role_name_is_null");
	private static final String CONCEPT_IS_NULL = FlexoLocalization.localizedForKey("concept_is_null");
	private static final String CONNECTOR_PATTERN_ROLE_NAME_IS_NULL = FlexoLocalization
			.localizedForKey("connector_pattern_role_name_is_null");
	private static final String A_SCHEME_NAME_IS_NOT_VALID = FlexoLocalization.localizedForKey("a_scheme_name_is_not_valid");
	private static final String VIRTUAL_MODEL_PATTERN_ROLE_NAME_IS_NULL = FlexoLocalization
			.localizedForKey("virtual_model_pattern_role_name_is_null");
	private static final String VIRTUAL_MODEL_CONCEPT_IS_NULL = FlexoLocalization.localizedForKey("virtual_model_concept_is_null");

	public NewFlexoConceptChoices patternChoice = NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL;

	private String flexoConceptName;
	private IFlexoOntologyClass concept;
	private IFlexoOntologyObjectProperty objectProperty;
	private String individualPatternRoleName;
	private String connectorPatternRoleName;
	private String objectPropertyStatementPatternRoleName;
	private String virtualModelPatternRoleName;

	public FlexoConcept fromFlexoConcept;
	public FlexoConcept toFlexoConcept;

	private String linkSchemeName;

	private FlexoConcept newFlexoConcept;
	private FlexoConcept virtualModelConcept;
	private ConnectorRole newConnectorRole;

	private List<IndividualRole> otherRoles;
	private IndividualRole individualRole;
	private FlexoConceptInstanceRole flexoConceptPatternRole;

	// private LinkScheme selectedLinkScheme;

	private String errorMessage;

	DeclareConnectorInFlexoConcept(DiagramConnector focusedObject, Vector<DiagramElement<?>> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) {
		logger.info("Declare connector in flexo concept");
		if (isValid()) {
			switch (primaryChoice) {
			case CHOOSE_EXISTING_FLEXO_CONCEPT:
				if (getFlexoRole() != null) {
					System.out.println("Connector representation updated");
					// getPatternRole().setGraphicalRepresentation(getFocusedObject().getGraphicalRepresentation());
					getFlexoRole().updateGraphicalRepresentation(getFocusedObject().getGraphicalRepresentation());
				}
				break;
			case CREATES_FLEXO_CONCEPT:

				/*VirtualModel.VirtualModelBuilder builder = new VirtualModel.VirtualModelBuilder(getFocusedObject()
						.getDiagramSpecification().getViewPointLibrary(), getFocusedObject().getDiagramSpecification().getViewPoint(),
						getFocusedObject().getDiagramSpecification().getResource());*/

				// Create new flexo concept
				newFlexoConcept = getFactory().newFlexoConcept();
				newFlexoConcept.setName(getFlexoConceptName());

				// And add the newly created flexo concept
				getVirtualModel().addToFlexoConcepts(newFlexoConcept);

				// Find best URI base candidate
				// PropertyEntry mainPropertyDescriptor = selectBestEntryForURIBaseName();

				// Create individual pattern property if required
				individualRole = null;
				if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
					if (isTypeAwareModelSlot()) {
						TypeAwareModelSlot ontologyModelSlot = (TypeAwareModelSlot) getModelSlot();
						individualRole = ontologyModelSlot.makeIndividualRole(getConcept());
						individualRole.setRoleName(getIndividualPatternRoleName());
						individualRole.setOntologicType(getConcept());
						newFlexoConcept.addToFlexoProperties(individualRole);
						// newFlexoConcept.setPrimaryConceptRole(individualPatternRole);
					}
				}

				// Create an flexo concept pattern property if required
				flexoConceptPatternRole = null;
				if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_FLEXO_CONCEPT) {
					if (isVirtualModelModelSlot()) {
						FMLRTModelSlot virtualModelModelSlot = (FMLRTModelSlot) getModelSlot();
						flexoConceptPatternRole = virtualModelModelSlot.makeFlexoConceptInstanceRole(getVirtualModelConcept());
						flexoConceptPatternRole.setRoleName(getVirtualModelPatternRoleName());
						newFlexoConcept.addToFlexoProperties(flexoConceptPatternRole);
					}
				}

				// Create individual pattern property if required
				/*ObjectPropertyStatementPatternRole objectPropertyStatementPatternRole = null;
				if (patternChoice == NewFlexoConceptChoices.MAP_OBJECT_PROPERTY) {
					objectPropertyStatementPatternRole = new ObjectPropertyStatementPatternRole(builder);
					objectPropertyStatementPatternRole.setPatternRoleName(getObjectPropertyStatementPatternRoleName());
					objectPropertyStatementPatternRole.setObjectProperty(getObjectProperty());
					newFlexoConcept.addToPatternRoles(objectPropertyStatementPatternRole);
					newFlexoConcept.setPrimaryConceptRole(objectPropertyStatementPatternRole);
				}*/

				// Create connector pattern property
				newConnectorRole = getFactory().newInstance(ConnectorRole.class);
				newConnectorRole.setRoleName(getConnectorPatternRoleName());
				/*if (mainPropertyDescriptor != null) {
					newConnectorRole.setLabel(new DataBinding<String>(getIndividualPatternRoleName() + "."
							+ mainPropertyDescriptor.property.getName()));
				} else {*/
				newConnectorRole.setReadOnlyLabel(true);
				newConnectorRole.setLabel(new DataBinding<String>("\"label\""));
				newConnectorRole.setExampleLabel(getFocusedObject().getGraphicalRepresentation().getText());
				// }
				// We clone here the GR (fixed unfocusable GR bug)
				newConnectorRole.setGraphicalRepresentation((ConnectorGraphicalRepresentation) getFocusedObject()
						.getGraphicalRepresentation().clone());
				newFlexoConcept.addToFlexoProperties(newConnectorRole);
				// newFlexoConcept.setPrimaryRepresentationRole(newConnectorRole);

				// Create other individual roles
				otherRoles = new ArrayList<IndividualRole>();
				/*if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
					for (PropertyEntry e : propertyEntries) {
						if (e.selectEntry) {
							if (e.property instanceof IFlexoOntologyObjectProperty) {
								IFlexoOntologyConcept range = ((IFlexoOntologyObjectProperty) e.property).getRange();
								if (range instanceof IFlexoOntologyClass) {
									IndividualRole newPatternRole = null; // new IndividualRole(builder);
									newPatternRole.setPatternRoleName(e.property.getName());
									newPatternRole.setOntologicType((IFlexoOntologyClass) range);
									newFlexoConcept.addToPatternRoles(newPatternRole);
									otherRoles.add(newPatternRole);
								}
							}
						}
					}
				}*/

				// Create new link scheme
				LinkScheme newLinkScheme = getFactory().newInstance(LinkScheme.class);
				newLinkScheme.setName(getLinkSchemeName());
				newLinkScheme.setFromTargetFlexoConcept(fromFlexoConcept);
				newLinkScheme.setToTargetFlexoConcept(toFlexoConcept);

				// Parameters
				if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
					if (isTypeAwareModelSlot()) {
						TypeAwareModelSlot<?, ?> typeAwareModelSlot = (TypeAwareModelSlot<?, ?>) getModelSlot();
						/*Vector<PropertyEntry> candidates = new Vector<PropertyEntry>();
						for (PropertyEntry e : propertyEntries) {
							if (e.selectEntry) {
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
									newLinkScheme.addToParameters(newParameter);
								}
							}
						}*/

						URIParameter uriParameter = getFactory().newURIParameter();
						uriParameter.setName("uri");
						uriParameter.setLabel("uri");
						/*if (mainPropertyDescriptor != null) {
							uriParameter.setBaseURI(new DataBinding<String>(mainPropertyDescriptor.property.getName()));
						}*/
						newLinkScheme.addToParameters(uriParameter);

						// Declare pattern property
						for (IndividualRole r : otherRoles) {
							AssignationAction<?> action = getFactory().newAssignationAction();
							action.setAssignation(new DataBinding<Object>(r.getRoleName()));
							ExpressionAction expressionAction = getFactory().newExpressionAction();
							expressionAction.setExpression(new DataBinding<Object>("parameters." + r.getName()));
							action.setAssignableAction(expressionAction);
							newLinkScheme.addToActions(action);
						}

						// Add individual action
						if (individualRole != null) {
							AddIndividual newAddIndividual = typeAwareModelSlot.makeAddIndividualAction(individualRole, newLinkScheme);
							newLinkScheme.addToActions(newAddIndividual);
						}

						// Add individual action
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
						}
						newLinkScheme.addToActions(newAddIndividual);
						*/
					}
				}

				// Add connector action
				AddConnector newAddConnector;
				AssignationAction<DiagramConnector> assignationAction = getFactory().newAssignationAction(
						newAddConnector = getFactory().newInstance(AddConnector.class));
				// AddConnector newAddConnector = getFactory().newInstance(AddConnector.class);
				assignationAction.setAssignation(new DataBinding<Object>(newConnectorRole.getRoleName()));
				ShapeRole fromPatternRole = null;
				ShapeRole toPatternRole = null;
				if (fromFlexoConcept.getDeclaredProperties(ShapeRole.class).size() > 0) {
					fromPatternRole = fromFlexoConcept.getDeclaredProperties(ShapeRole.class).get(0);
				}
				if (fromFlexoConcept.getDeclaredProperties(ShapeRole.class).size() > 0) {
					toPatternRole = toFlexoConcept.getDeclaredProperties(ShapeRole.class).get(0);
				}

				newAddConnector.setFromShape(new DataBinding<DiagramShape>(LinkSchemeBindingModel.FROM_TARGET + "."
						+ fromPatternRole.getRoleName()));
				newAddConnector.setToShape(new DataBinding<DiagramShape>(LinkSchemeBindingModel.TO_TARGET + "."
						+ toPatternRole.getRoleName()));

				newLinkScheme.addToActions(assignationAction);

				// Add new drop scheme
				newFlexoConcept.addToFlexoBehaviours(newLinkScheme);

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
				logger.warning("Pattern not implemented");
			}
		} else {
			logger.warning("Focused property is null !");
		}
	}

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
		case CHOOSE_EXISTING_FLEXO_CONCEPT:
			if (getFlexoRole() == null) {
				errorMessage = PATTERN_ROLE_IS_NULL;
			}
			if (getFlexoConcept() == null) {
				errorMessage = EDITION_PATTERN_IS_NULL;
			}
			return getFlexoConcept() != null && getFlexoRole() != null;
		case CREATES_FLEXO_CONCEPT:
			switch (patternChoice) {
			case MAP_SINGLE_INDIVIDUAL:
				if (StringUtils.isEmpty(getFlexoConceptName())) {
					errorMessage = EDITION_PATTERN_NAME_IS_NULL;
				}
				if (StringUtils.isEmpty(getIndividualPatternRoleName())) {
					errorMessage = INDIVIDUAL_PATTERN_ROLE_NAME_IS_NULL;
				}
				if (concept == null) {
					errorMessage = CONCEPT_IS_NULL;
				}
				if (StringUtils.isEmpty(getConnectorPatternRoleName())) {
					errorMessage = CONNECTOR_PATTERN_ROLE_NAME_IS_NULL;
				}
				if (!StringUtils.isNotEmpty(getLinkSchemeName())) {
					errorMessage = A_SCHEME_NAME_IS_NOT_VALID;
				}
				return StringUtils.isNotEmpty(getFlexoConceptName()) && concept != null
						&& StringUtils.isNotEmpty(getIndividualPatternRoleName()) && StringUtils.isNotEmpty(getConnectorPatternRoleName())
						&& StringUtils.isNotEmpty(getLinkSchemeName());
			case MAP_OBJECT_PROPERTY:
				return StringUtils.isNotEmpty(getFlexoConceptName()) && objectProperty != null
						&& StringUtils.isNotEmpty(getObjectPropertyStatementPatternRoleName())
						&& StringUtils.isNotEmpty(getConnectorPatternRoleName()) && StringUtils.isNotEmpty(getLinkSchemeName());
			case MAP_SINGLE_FLEXO_CONCEPT:
				if (StringUtils.isEmpty(getFlexoConceptName())) {
					errorMessage = EDITION_PATTERN_NAME_IS_NULL;
				}
				if (StringUtils.isEmpty(getVirtualModelPatternRoleName())) {
					errorMessage = VIRTUAL_MODEL_PATTERN_ROLE_NAME_IS_NULL;
				}
				if (virtualModelConcept == null) {
					errorMessage = VIRTUAL_MODEL_CONCEPT_IS_NULL;
				}
				if (StringUtils.isEmpty(getConnectorPatternRoleName())) {
					errorMessage = CONNECTOR_PATTERN_ROLE_NAME_IS_NULL;
				}
				if (!StringUtils.isNotEmpty(getLinkSchemeName())) {
					errorMessage = A_SCHEME_NAME_IS_NOT_VALID;
				}
				return StringUtils.isNotEmpty(getFlexoConceptName()) && virtualModelConcept != null
						&& StringUtils.isNotEmpty(getVirtualModelPatternRoleName()) && getSelectedEntriesCount() > 0
						&& StringUtils.isNotEmpty(getLinkSchemeName());
			case BLANK_FLEXO_CONCEPT:
				if (StringUtils.isEmpty(getFlexoConceptName())) {
					errorMessage = EDITION_PATTERN_NAME_IS_NULL;
				}
				if (StringUtils.isEmpty(getConnectorPatternRoleName())) {
					errorMessage = CONNECTOR_PATTERN_ROLE_NAME_IS_NULL;
				}
				if (!StringUtils.isNotEmpty(getLinkSchemeName())) {
					errorMessage = A_SCHEME_NAME_IS_NOT_VALID;
				}
				return StringUtils.isNotEmpty(getFlexoConceptName()) && StringUtils.isNotEmpty(getConnectorPatternRoleName())
						&& StringUtils.isNotEmpty(getLinkSchemeName());
			default:
				break;
			}
		default:
			return false;
		}
	}

	private ConnectorRole patternRole;

	@Override
	public ConnectorRole getFlexoRole() {
		if (primaryChoice == DeclareInFlexoConceptChoices.CREATES_FLEXO_CONCEPT) {
			return newConnectorRole;
		}
		return patternRole;
	}

	public void setPatternRole(ConnectorRole patternRole) {
		this.patternRole = patternRole;
	}

	@Override
	public List<ConnectorRole> getAvailableFlexoRoles() {
		if (getFlexoConcept() != null) {
			return getFlexoConcept().getDeclaredProperties(ConnectorRole.class);
		}
		return null;
	}

	@Override
	public void resetFlexoRole() {
		this.patternRole = null;
	}

	public IFlexoOntologyClass getConcept() {
		return concept;
	}

	public void setConcept(IFlexoOntologyClass concept) {
		this.concept = concept;
		// propertyEntries.clear();
		// IFlexoOntology ownerOntology = concept.getOntology();
		/*for (IFlexoOntologyFeature p : concept.getPropertiesTakingMySelfAsDomain()) {
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

	public IFlexoOntologyObjectProperty getObjectProperty() {
		return objectProperty;
	}

	public void setObjectProperty(IFlexoOntologyObjectProperty property) {
		this.objectProperty = property;
	}

	public String getFlexoConceptName() {
		if (isTypeAwareModelSlot()) {
			if (StringUtils.isEmpty(flexoConceptName) && concept != null) {
				return concept.getName();
			}
			if (StringUtils.isEmpty(flexoConceptName) && objectProperty != null) {
				return objectProperty.getName();
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

	public String getVirtualModelPatternRoleName() {
		if (StringUtils.isEmpty(virtualModelPatternRoleName) && virtualModelConcept != null) {
			return JavaUtils.getVariableName(virtualModelConcept.getName());
		}
		return virtualModelPatternRoleName;
	}

	public void setVirtualModelPatternRoleName(String virtualModelPatternRoleName) {
		this.virtualModelPatternRoleName = virtualModelPatternRoleName;
	}

	public String getObjectPropertyStatementPatternRoleName() {
		if (StringUtils.isEmpty(objectPropertyStatementPatternRoleName) && objectProperty != null) {
			return JavaUtils.getVariableName(objectProperty.getName()) + "Statement";
		}
		return objectPropertyStatementPatternRoleName;
	}

	public void setObjectPropertyStatementPatternRoleName(String objectPropertyStatementPatternRoleName) {
		this.objectPropertyStatementPatternRoleName = objectPropertyStatementPatternRoleName;
	}

	public String getConnectorPatternRoleName() {
		if (StringUtils.isEmpty(connectorPatternRoleName)) {
			return "connector";
		}
		return connectorPatternRoleName;
	}

	public void setConnectorPatternRoleName(String connectorPatternRoleName) {
		this.connectorPatternRoleName = connectorPatternRoleName;
	}

	public String getLinkSchemeName() {
		if (StringUtils.isEmpty(linkSchemeName) && fromFlexoConcept != null && toFlexoConcept != null) {
			return "link" + (fromFlexoConcept != null ? fromFlexoConcept.getName() : "") + "To"
					+ (toFlexoConcept != null ? toFlexoConcept.getName() : "");
		}
		return linkSchemeName;
	}

	public void setLinkSchemeName(String linkSchemeName) {
		this.linkSchemeName = linkSchemeName;
	}

	@Override
	public FlexoConcept getFlexoConcept() {
		if (primaryChoice == DeclareInFlexoConceptChoices.CREATES_FLEXO_CONCEPT) {
			return newFlexoConcept;
		}
		return super.getFlexoConcept();
	};

	/*private void createSchemeActions(FlexoBehaviourConfiguration configuration) {
		FlexoBehaviour FlexoBehaviour = null;

		// Create new link scheme
		if (configuration.getType() == FlexoBehaviourChoice.LINK) {
			FlexoBehaviour = createLinkSchemeEditionActions(configuration);
		}

		// Delete shapes as well as model
		if (configuration.getType() == FlexoBehaviourChoice.DELETE_GR_AND_MODEL) {
			FlexoBehaviour = createDeleteFlexoBehaviourActions(configuration, false);
		}

		// Delete only shapes
		if (configuration.getType() == FlexoBehaviourChoice.DELETE_GR_ONLY) {
			FlexoBehaviour = createDeleteFlexoBehaviourActions(configuration, true);
		}
		newFlexoConcept.addToFlexoBehaviours(FlexoBehaviour);
	}*/

	/*private FlexoBehaviour createDeleteFlexoBehaviourActions(FlexoBehaviourConfiguration configuration, boolean shapeOnly) {

		DeletionScheme FlexoBehaviour = (DeletionScheme) configuration.getFlexoBehaviour();

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
			FlexoBehaviour.addToActions(a);
		}
		return FlexoBehaviour;
	}*/

	/*private FlexoBehaviour createLinkSchemeEditionActions(FlexoBehaviourConfiguration FlexoBehaviourConfiguration) {
		LinkScheme FlexoBehaviour = (LinkScheme) FlexoBehaviourConfiguration.getFlexoBehaviour();

		// Parameters
		if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
			if (isTypeAwareModelSlot()) {
				TypeAwareModelSlot<?, ?> typeAwareModelSlot = (TypeAwareModelSlot<?, ?>) getModelSlot();

				URIParameter uriParameter = getFactory().newURIParameter();
				uriParameter.setName("uri");
				uriParameter.setLabel("uri");

				FlexoBehaviour.addToParameters(uriParameter);

				// Declare pattern property
				for (IndividualRole r : otherRoles) {
					DeclareFlexoRole action = getFactory().newDeclareFlexoRole();
					action.setAssignation(new DataBinding<Object>(r.getRoleName()));
					action.setObject(new DataBinding<Object>("parameters." + r.getName()));
					FlexoBehaviour.addToActions(action);
				}

				// Add individual action
				if (individualRole != null) {
					AddIndividual newAddIndividual = typeAwareModelSlot.makeAddIndividualAction(individualRole, (FlexoBehaviour));
					FlexoBehaviour.addToActions(newAddIndividual);
				}
			}
		}

		if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_FLEXO_CONCEPT) {
			if (isVirtualModelModelSlot()) {
				FMLRTModelSlot virtualModelModelSlot = (FMLRTModelSlot) getModelSlot();

				// Add individual action
				AddFlexoConceptInstance newAddEditionPatternInstance = virtualModelModelSlot
						.makeEditionAction(AddFlexoConceptInstance.class);
				newAddEditionPatternInstance.setAssignation(new DataBinding<Object>(flexoConceptPatternRole.getName()));
				newAddEditionPatternInstance.setFlexoConceptType(flexoConceptPatternRole.getFlexoConceptType());
				FlexoBehaviour.addToActions(newAddEditionPatternInstance);
			}
		}

		// Add connector action
		AddConnector newAddConnector = getFactory().newInstance(AddConnector.class);
		newAddConnector.setAssignation(new DataBinding<Object>(newConnectorRole.getRoleName()));
		ShapeRole fromShapeRole = getVirtualModel().getFlexoConcept(FlexoBehaviour._getFromTarget()).getFlexoRoles(ShapeRole.class).get(0);
		ShapeRole toShapeRole = getVirtualModel().getFlexoConcept(FlexoBehaviour._getToTarget()).getFlexoRoles(ShapeRole.class).get(0);
		newAddConnector.setFromShape(new DataBinding<DiagramShape>(DiagramFlexoBehaviour.FROM_TARGET + "." + fromShapeRole.getRoleName()));
		newAddConnector.setToShape(new DataBinding<DiagramShape>(DiagramFlexoBehaviour.TO_TARGET + "." + toShapeRole.getRoleName()));

		FlexoBehaviour.addToActions(newAddConnector);
		return FlexoBehaviour;
	}*/

	/*public LinkScheme getLinkScheme(FlexoBehaviourConfiguration conf) {
		if (conf != null) {
			if (conf.getFlexoBehaviour() instanceof LinkScheme) {
				selectedLinkScheme = (LinkScheme) conf.getFlexoBehaviour();
				return selectedLinkScheme;
			}
		}
		return null;
	}

	public void addFlexoBehaviourConfigurationLink() {
		FlexoBehaviourConfiguration FlexoBehaviourConfiguration = new FlexoBehaviourConfiguration(FlexoBehaviourChoice.LINK);
		getFlexoBehaviours().add(FlexoBehaviourConfiguration);
	}
	*/
	@Override
	public void initializeBehaviours() {
		if (getVirtualModel() != null && getVirtualModel().getFlexoConcepts() != null && !getVirtualModel().getFlexoConcepts().isEmpty()) {
			FlexoBehaviourConfiguration linkFlexoBehaviour = new FlexoBehaviourConfiguration(FlexoBehaviourChoice.LINK);
			getFlexoBehaviours().add(linkFlexoBehaviour);
			((LinkScheme) linkFlexoBehaviour.getFlexoBehaviour()).setToTargetFlexoConcept(getVirtualModel().getFlexoConcepts().get(0));
			((LinkScheme) linkFlexoBehaviour.getFlexoBehaviour()).setFromTargetFlexoConcept(getVirtualModel().getFlexoConcepts().get(0));
		}
	}

	// Hack to keep the right edition patterns in link from/target drop downs
	// This should be removed.
	private List<FlexoConcept> flexoConceptsFromList;

	private List<FlexoConcept> flexoConceptsToList;

	public List<FlexoConcept> getFlexoConceptsFrom() {
		if (flexoConceptsFromList == null) {
			flexoConceptsFromList = new ArrayList<FlexoConcept>();
		}
		if (getVirtualModel() != null) {
			flexoConceptsFromList.clear();
			flexoConceptsFromList.addAll(getVirtualModel().getFlexoConcepts());
		}
		/*if (selectedLinkScheme != null && 
				getVirtualModel()!=null &&
				getVirtualModel().getFlexoConcept(selectedLinkScheme._getFromTarget()) != null) {
			FlexoConcept currentFromEp = getVirtualModel().getFlexoConcept(selectedLinkScheme._getFromTarget());
			FlexoConcept firstEp = flexoConceptsFromList.get(0);
			if (!currentFromEp.equals(firstEp)) {
				int lastIndex = flexoConceptsFromList.indexOf(currentFromEp);
				;
				flexoConceptsFromList.set(0, currentFromEp);
				flexoConceptsFromList.set(lastIndex, firstEp);
			}
		}*/
		return flexoConceptsFromList;
	}

	public List<FlexoConcept> getFlexoConceptsTo() {
		if (flexoConceptsToList == null) {
			flexoConceptsToList = new ArrayList<FlexoConcept>();
		}
		if (getVirtualModel() != null) {
			flexoConceptsToList.clear();
			flexoConceptsToList.addAll(getVirtualModel().getFlexoConcepts());
		}
		/*if (selectedLinkScheme != null && getVirtualModel()!=null && getVirtualModel().getFlexoConcept(selectedLinkScheme._getToTarget()) != null) {
			FlexoConcept currentToEp = getVirtualModel().getFlexoConcept(selectedLinkScheme._getToTarget());
			FlexoConcept firstEp = flexoConceptsToList.get(0);
			if (!currentToEp.equals(firstEp)) {
				int lastIndex = flexoConceptsToList.indexOf(currentToEp);
				flexoConceptsToList.set(0, currentToEp);
				flexoConceptsToList.set(lastIndex, firstEp);
			}
		}*/
		return flexoConceptsToList;
	}
}
