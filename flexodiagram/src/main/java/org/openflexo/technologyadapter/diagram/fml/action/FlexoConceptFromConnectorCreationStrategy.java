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

import org.openflexo.connie.DataBinding;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.binding.LinkSchemeBindingModel;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.StringUtils;

/**
 * This abstract class is the base class for a {@link FlexoConcept} creation strategy, using a {@link DiagramShape}
 * 
 * @author sylvain
 *
 */
public abstract class FlexoConceptFromConnectorCreationStrategy
		extends FlexoConceptFromDiagramElementCreationStrategy<DeclareConnectorInFlexoConcept> {

	private static final String DEFAULT_ROLE_NAME = "connector";

	private static final String LINK_SCHEME_NAME_UNDEFINED = "linkScheme_name_is_not_defined";
	private static final String FROM_CONCEPT_UNDEFINED = "origin_concept_is_not_defined";
	private static final String TO_CONCEPT_UNDEFINED = "destination_concept_is_not_defined";

	private String linkSchemeName;
	private FlexoConcept fromFlexoConcept;
	private FlexoConcept toFlexoConcept;

	private LinkScheme newLinkScheme;

	public FlexoConceptFromConnectorCreationStrategy(DeclareConnectorInFlexoConcept transformationAction) {
		super(transformationAction);
	}

	@Override
	public String getDefaultRoleName() {
		return DEFAULT_ROLE_NAME;
	}

	@Override
	public void normalizeGraphicalRepresentation(GraphicalRepresentation gr) {
		if (gr instanceof ConnectorGraphicalRepresentation) {
			// ConnectorGraphicalRepresentation connectorGR = (ConnectorGraphicalRepresentation) gr;
			// Anything to do ???
		}
	}

	public String getLinkSchemeName() {
		if (linkSchemeName == null) {
			return "link" + (getFromFlexoConcept() != null ? fromFlexoConcept.getName() : "") + "To"
					+ (toFlexoConcept != null ? toFlexoConcept.getName() : "");
		}
		return linkSchemeName;
	}

	public void setLinkSchemeName(String linkSchemeName) {
		this.linkSchemeName = linkSchemeName;
	}

	// Hack to keep the right edition patterns in link from/target drop downs
	// This should be removed.
	private List<FlexoConcept> flexoConceptsFromList;

	private List<FlexoConcept> flexoConceptsToList;

	public List<FlexoConcept> getFlexoConceptsFrom() {
		if (flexoConceptsFromList == null) {
			flexoConceptsFromList = new ArrayList<FlexoConcept>();
		}
		if (getTransformationAction().getVirtualModel() != null) {
			flexoConceptsFromList.clear();
			flexoConceptsFromList.addAll(getTransformationAction().getVirtualModel().getFlexoConcepts());
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
		if (getTransformationAction().getVirtualModel() != null) {
			flexoConceptsToList.clear();
			flexoConceptsToList.addAll(getTransformationAction().getVirtualModel().getFlexoConcepts());
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

	public FlexoConcept getFromFlexoConcept() {
		return fromFlexoConcept;
	}

	public void setFromFlexoConcept(FlexoConcept fromFlexoConcept) {
		if ((fromFlexoConcept == null && this.fromFlexoConcept != null)
				|| (fromFlexoConcept != null && !fromFlexoConcept.equals(this.fromFlexoConcept))) {
			FlexoConcept oldValue = this.fromFlexoConcept;
			this.fromFlexoConcept = fromFlexoConcept;
			getPropertyChangeSupport().firePropertyChange("fromFlexoConcept", oldValue, fromFlexoConcept);
		}
	}

	public FlexoConcept getToFlexoConcept() {
		return toFlexoConcept;
	}

	public void setToFlexoConcept(FlexoConcept toFlexoConcept) {
		if ((toFlexoConcept == null && this.toFlexoConcept != null)
				|| (toFlexoConcept != null && !toFlexoConcept.equals(this.toFlexoConcept))) {
			FlexoConcept oldValue = this.toFlexoConcept;
			this.toFlexoConcept = toFlexoConcept;
			getPropertyChangeSupport().firePropertyChange("toFlexoConcept", oldValue, toFlexoConcept);
		}
	}

	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}
		if (StringUtils.isEmpty(getLinkSchemeName())) {
			setIssueMessage(getLocales().localizedForKey(LINK_SCHEME_NAME_UNDEFINED), IssueMessageType.ERROR);
			return false;
		}
		if (getFromFlexoConcept() == null) {
			setIssueMessage(getLocales().localizedForKey(FROM_CONCEPT_UNDEFINED), IssueMessageType.ERROR);
			return false;
		}
		if (getToFlexoConcept() == null) {
			setIssueMessage(getLocales().localizedForKey(TO_CONCEPT_UNDEFINED), IssueMessageType.ERROR);
			return false;
		}
		return true;
	}

	@Override
	public FlexoConcept performStrategy() {
		FlexoConcept newFlexoConcept = super.performStrategy();

		newLinkScheme = getTransformationAction().getFactory().newInstance(LinkScheme.class);
		newLinkScheme.setName(getLinkSchemeName());
		newLinkScheme.setControlGraph(getTransformationAction().getFactory().newEmptyControlGraph());
		newLinkScheme.setFromTargetFlexoConcept(fromFlexoConcept);
		newLinkScheme.setToTargetFlexoConcept(toFlexoConcept);

		// Add new link scheme
		newFlexoConcept.addToFlexoBehaviours(newLinkScheme);

		initializeLinkScheme(newLinkScheme);

		return newFlexoConcept;
	}

	protected void initializeLinkScheme(LinkScheme newLinkScheme) {

		if (primaryRepresentationRole instanceof ConnectorRole) {

			ConnectorRole newConnectorRole = (ConnectorRole) primaryRepresentationRole;

			// Add connector action
			AddConnector newAddConnector = getTransformationAction().getFactory().newInstance(AddConnector.class);
			newAddConnector.getReceiver().setUnparsedBinding(getTransformationAction().getDiagramModelSlot().getModelSlotName());
			// newAddConnector.setModelSlot(getTransformationAction().getDiagramModelSlot());
			AssignationAction<DiagramConnector> assignationAction = getTransformationAction().getFactory()
					.newAssignationAction(newAddConnector);
			assignationAction.setAssignation(new DataBinding<Object>(newConnectorRole.getRoleName()));
			ShapeRole fromPatternRole = null;
			ShapeRole toPatternRole = null;
			if (fromFlexoConcept.getDeclaredProperties(ShapeRole.class).size() > 0) {
				fromPatternRole = fromFlexoConcept.getDeclaredProperties(ShapeRole.class).get(0);
			}
			if (toFlexoConcept.getDeclaredProperties(ShapeRole.class).size() > 0) {
				toPatternRole = toFlexoConcept.getDeclaredProperties(ShapeRole.class).get(0);
			}

			newAddConnector
					.setFromShape(new DataBinding<DiagramShape>(LinkSchemeBindingModel.FROM_TARGET + "." + fromPatternRole.getRoleName()));
			newAddConnector.setToShape(new DataBinding<DiagramShape>(LinkSchemeBindingModel.TO_TARGET + "." + toPatternRole.getRoleName()));

			newLinkScheme.getControlGraph().sequentiallyAppend(assignationAction);
		}

	}

	@Override
	public void initializeBehaviours() {
		/*if (getTransformationAction().getVirtualModel() != null && getTransformationAction().getVirtualModel().getFlexoConcepts() != null
				&& !getTransformationAction().getVirtualModel().getFlexoConcepts().isEmpty()) {
			FlexoBehaviourConfiguration linkFlexoBehaviour = new FlexoBehaviourConfiguration(FlexoBehaviourChoice.LINK);
			getFlexoBehaviours().add(linkFlexoBehaviour);
			((LinkScheme) linkFlexoBehaviour.getFlexoBehaviour())
					.setToTargetFlexoConcept(getTransformationAction().getVirtualModel().getFlexoConcepts().get(0));
			((LinkScheme) linkFlexoBehaviour.getFlexoBehaviour())
					.setFromTargetFlexoConcept(getTransformationAction().getVirtualModel().getFlexoConcepts().get(0));
		}*/
	}

	/*private FlexoBehaviour createDropFlexoBehaviourActions(FlexoBehaviourConfiguration editionSchemeConfiguration) {
		// Create new drop scheme
		DropScheme editionScheme = (DropScheme) editionSchemeConfiguration.getFlexoBehaviour();
	
		// Parameters
		if (patternChoice == NewFlexoConceptChoices.MAP_SINGLE_INDIVIDUAL) {
			if (isTypeAwareModelSlot()) {
				TypeAwareModelSlot<?, ?> flexoOntologyModelSlot = (TypeAwareModelSlot<?, ?>) getInformationSourceModelSlot();
	
				if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DROP_AND_SELECT) {
					IndividualParameter individualParameter = getFactory().newIndividualParameter();
					individualParameter.setName(individualFlexoRole.getName());
					individualParameter.setLabel(individualFlexoRole.getName());
					individualParameter.setInformationSourceModelSlot(individualFlexoRole.getInformationSourceModelSlot());
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
				FMLRTModelSlot virtualModelModelSlot = (FMLRTModelSlot) getInformationSourceModelSlot();
	
				if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DROP_AND_SELECT) {
					FlexoConceptInstanceParameter flexoConceptInstanceParameter = getFactory().newFlexoConceptInstanceParameter();
					flexoConceptInstanceParameter.setName(flexoConceptInstanceRole.getName());
					flexoConceptInstanceParameter.setLabel(flexoConceptInstanceRole.getName());
					flexoConceptInstanceParameter.setModelSlot((FMLRTModelSlot) flexoConceptInstanceRole.getModelSlot());
					// editionPatternInstanceParameter.setFlexoConceptType(editionPatternFlexoRole.getFlexoConceptType());
					editionScheme.addToParameters(flexoConceptInstanceParameter);
					// Add individual action
					AssignationAction<?> declareFlexoRole = getFactory()
							.newAssignationAction(new DataBinding<Object>("parameters." + flexoConceptInstanceParameter.getName()));
					declareFlexoRole.setAssignation(new DataBinding<Object>(flexoConceptInstanceRole.getName()));
					editionScheme.addToActions(declareFlexoRole);
				}
				if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DROP_AND_CREATE) {
					// Add individual action
					AddFlexoConceptInstance newAddFlexoConceptInstance = virtualModelModelSlot
							.makeEditionAction(AddFlexoConceptInstance.class);
					newAddFlexoConceptInstance.setFlexoConceptType(flexoConceptInstanceRole.getFlexoConceptType());
					AssignationAction<FlexoConceptInstance> newAddFlexoConceptInstanceAssignation = getFactory()
							.newAssignationAction(newAddFlexoConceptInstance);
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
				AssignationAction<DiagramShape> assignationAction = getFactory()
						.newAssignationAction(newAddShape = getFactory().newInstance(AddShape.class));
				assignationAction.setAssignation(new DataBinding<Object>(graphicalElementFlexoRole.getRoleName()));
				editionScheme.addToActions(assignationAction);
				if (mainFlexoRole) {
					if (editionScheme.isTopTarget()) {
						newAddShape.setContainer(new DataBinding<DiagramContainerElement<?>>(DiagramBehaviourBindingModel.TOP_LEVEL));
					} else {
						ShapeRole containerRole = getVirtualModel().getFlexoConcept(editionScheme._getTarget())
								.getDeclaredProperties(ShapeRole.class).get(0);
						newAddShape.setContainer(new DataBinding<DiagramContainerElement<?>>(
								DropSchemeBindingModel.TARGET + "." + containerRole.getRoleName()));
					}
				} else {
					newAddShape.setContainer(new DataBinding<DiagramContainerElement<?>>(grFlexoRole.getParentShapeRole().getRoleName()));
				}
				mainFlexoRole = false;
			}
		}
		return editionScheme;
	}*/

}
