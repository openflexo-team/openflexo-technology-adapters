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

import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.URIParameter;
import org.openflexo.foundation.fml.editionaction.AssignableAction;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.fml.IndividualRole;
import org.openflexo.foundation.ontology.fml.editionaction.AddIndividual;
import org.openflexo.foundation.ontology.technologyadapter.FlexoOntologyModelSlot;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.JavaUtils;

/**
 * Encodes a {@link FlexoConcept} creation strategy, using a {@link DiagramShape}<br>
 * We create a new {@link FlexoConcept} implementing the mapping between some diagram elements and a single individual
 * 
 * @author sylvain
 *
 */
public class MapConnectorToIndividualStrategy extends FlexoConceptFromConnectorCreationStrategy {

	private static final String NO_FLEXO_ONTOLOGY_MODEL_SLOT_DEFINED = "please_select_a_valid_model_slot";
	private static final String NO_CONCEPT_DEFINED = "no_concept_defined_as_type_of_individual";

	private IFlexoOntologyClass<?> concept;
	private String individualFlexoRoleName;
	private IndividualRole<?> individualFlexoRole;

	public MapConnectorToIndividualStrategy(DeclareConnectorInFlexoConcept transformationAction) {
		super(transformationAction);
	}

	public String getIndividualFlexoRoleName() {
		if (individualFlexoRoleName == null && concept != null) {
			return JavaUtils.getVariableName(concept.getName());
		}
		return individualFlexoRoleName;
	}

	public void setIndividualFlexoRoleName(String individualFlexoRoleName) {
		if (!individualFlexoRoleName.equals(getIndividualFlexoRoleName())) {
			String oldValue = getIndividualFlexoRoleName();
			this.individualFlexoRoleName = individualFlexoRoleName;
			getPropertyChangeSupport().firePropertyChange("individualFlexoRoleName", oldValue, individualFlexoRoleName);
		}
	}

	public IFlexoOntologyClass<?> getConcept() {
		return concept;
	}

	public void setConcept(IFlexoOntologyClass<?> concept) {
		if ((concept == null && this.concept != null) || (concept != null && !concept.equals(this.concept))) {
			IFlexoOntologyClass<?> oldValue = this.concept;
			this.concept = concept;
			getPropertyChangeSupport().firePropertyChange("concept", oldValue, concept);
		}
	}

	/**
	 * Return flag indicating if currently selected {@link ModelSlot} is a {@link FlexoOntologyModelSlot}
	 * 
	 * @return
	 */
	public boolean isFlexoOntologyModelSlot() {
		return (getTransformationAction() != null
				&& getTransformationAction().getInformationSourceModelSlot() instanceof FlexoOntologyModelSlot);
	}

	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}
		if (!isFlexoOntologyModelSlot()) {
			setIssueMessage(getLocales().localizedForKey(NO_FLEXO_ONTOLOGY_MODEL_SLOT_DEFINED), IssueMessageType.ERROR);
			return false;
		}
		if (getConcept() == null) {
			setIssueMessage(getLocales().localizedForKey(NO_CONCEPT_DEFINED), IssueMessageType.ERROR);
			return false;
		}
		return true;
	}

	@Override
	public FlexoConcept performStrategy() {

		if (isFlexoOntologyModelSlot()) {
			FlexoOntologyModelSlot<?, ?, ?> flexoOntologyModelSlot = (FlexoOntologyModelSlot<?, ?, ?>) getTransformationAction()
					.getInformationSourceModelSlot();
			individualFlexoRole = flexoOntologyModelSlot.makeIndividualRole(getConcept());
			individualFlexoRole.setRoleName(getIndividualFlexoRoleName());
			individualFlexoRole.setOntologicType(getConcept());
		}

		FlexoConcept newFlexoConcept = super.performStrategy();

		newFlexoConcept.addToFlexoProperties(individualFlexoRole);

		return getNewFlexoConcept();
	}

	public IndividualRole<?> getIndividualFlexoRole() {
		return individualFlexoRole;
	}

	@Override
	protected void initializeLinkScheme(LinkScheme newLinkScheme) {
		if (isFlexoOntologyModelSlot()) {
			FlexoOntologyModelSlot<?, ?, ?> flexoOntologyModelSlot = (FlexoOntologyModelSlot<?, ?, ?>) getTransformationAction()
					.getInformationSourceModelSlot();
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

			URIParameter uriParameter = getTransformationAction().getFactory().newURIParameter();
			uriParameter.setName("uri");
			uriParameter.setLabel("uri");
			/*if (mainPropertyDescriptor != null) {
				uriParameter.setBaseURI(new DataBinding<String>(mainPropertyDescriptor.property.getName()));
			}*/
			newLinkScheme.addToParameters(uriParameter);

			// Declare pattern property
			/*for (IndividualRole r : otherRoles) {
				DeclareFlexoRole action = new DeclareFlexoRole(builder);
				action.setAssignation(new DataBinding<Object>(r.getRoleName()));
				action.setObject(new DataBinding<Object>("parameters." + r.getName()));
				newDropScheme.addToActions(action);
			}*/

			// Add individual action
			AddIndividual<?, ?> newAddIndividual = flexoOntologyModelSlot.makeAddIndividualAction(individualFlexoRole, newLinkScheme);

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

			AssignationAction<?> newAssignationAction = getTransformationAction().getFactory().newAssignationAction();
			newAssignationAction.setAssignableAction((AssignableAction) newAddIndividual);
			newAssignationAction.setAssignation(new DataBinding(individualFlexoRole.getRoleName()));

			newLinkScheme.getControlGraph().sequentiallyAppend(newAssignationAction);

		}

		super.initializeLinkScheme(newLinkScheme);

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

}
