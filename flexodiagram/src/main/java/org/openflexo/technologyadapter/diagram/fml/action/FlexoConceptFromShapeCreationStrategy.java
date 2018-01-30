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

import java.util.List;

import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.binding.DiagramBehaviourBindingModel;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.StringUtils;

/**
 * This abstract class is the base class for a {@link FlexoConcept} creation strategy, using a {@link DiagramShape}
 * 
 * @author sylvain
 *
 */
public abstract class FlexoConceptFromShapeCreationStrategy
		extends FlexoConceptFromDiagramElementCreationStrategy<DeclareShapeInFlexoConcept> {

	private static final String DEFAULT_ROLE_NAME = "shape";

	private static final String DROP_SCHEME_NAME_UNDEFINED = "dropScheme_name_is_not_defined";
	private static final String CONTAINER_CONCEPT_UNDEFINED = "please_select_a_container_concept";

	private boolean isTopLevel = true;
	private String dropSchemeName;
	private FlexoConcept containerFlexoConcept;

	private DropScheme newDropScheme;

	public FlexoConceptFromShapeCreationStrategy(DeclareShapeInFlexoConcept transformationAction) {
		super(transformationAction);
	}

	@Override
	public String getDefaultRoleName() {
		return DEFAULT_ROLE_NAME;
	}

	public boolean isTopLevel() {
		return isTopLevel;
	}

	public void setTopLevel(boolean isTopLevel) {
		if (isTopLevel != this.isTopLevel) {
			this.isTopLevel = isTopLevel;
			getPropertyChangeSupport().firePropertyChange("isTopLevel", !isTopLevel, isTopLevel);
		}
	}

	/*@Override
	public void normalizeGraphicalRepresentation(GraphicalRepresentation gr) {
		if (gr instanceof ShapeGraphicalRepresentation) {
			ShapeGraphicalRepresentation shapeGR = (ShapeGraphicalRepresentation) gr;
			// Center shape in preview
			shapeGR.setX((250 - shapeGR.getWidth()) / 2);
			shapeGR.setY((200 - shapeGR.getHeight()) / 2);
			// Forces GR to be displayed in view
			shapeGR.setAllowToLeaveBounds(false);
		}
	}*/

	public String getDropSchemeName() {
		if (StringUtils.isEmpty(dropSchemeName)) {
			return "drop" + (StringUtils.isEmpty(getFlexoConceptName()) ? "" : getFlexoConceptName())
					+ (isTopLevel ? "AtTopLevel" : containerFlexoConcept != null ? "In" + containerFlexoConcept.getName() : "");
		}
		return dropSchemeName;
	}

	public void setDropSchemeName(String dropSchemeName) {
		if ((dropSchemeName == null && this.dropSchemeName != null)
				|| (dropSchemeName != null && !dropSchemeName.equals(this.dropSchemeName))) {
			String oldValue = this.dropSchemeName;
			this.dropSchemeName = dropSchemeName;
			getPropertyChangeSupport().firePropertyChange("dropSchemeName", oldValue, dropSchemeName);
		}
	}

	public FlexoConcept getContainerFlexoConcept() {
		return containerFlexoConcept;
	}

	public void setContainerFlexoConcept(FlexoConcept containerFlexoConcept) {
		if ((containerFlexoConcept == null && this.containerFlexoConcept != null)
				|| (containerFlexoConcept != null && !containerFlexoConcept.equals(this.containerFlexoConcept))) {
			FlexoConcept oldValue = this.containerFlexoConcept;
			this.containerFlexoConcept = containerFlexoConcept;
			getPropertyChangeSupport().firePropertyChange("containerFlexoConcept", oldValue, containerFlexoConcept);
		}
	}

	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}
		if (StringUtils.isEmpty(getDropSchemeName())) {
			setIssueMessage(getLocales().localizedForKey(DROP_SCHEME_NAME_UNDEFINED), IssueMessageType.ERROR);
			return false;
		}
		if (!isTopLevel() && getContainerFlexoConcept() == null) {
			setIssueMessage(getLocales().localizedForKey(CONTAINER_CONCEPT_UNDEFINED), IssueMessageType.ERROR);
			return false;
		}
		return true;
	}

	@Override
	public FlexoConcept performStrategy() {
		FlexoConcept newFlexoConcept = super.performStrategy();

		// Create new drop scheme
		newDropScheme = getTransformationAction().getFactory().newInstance(DropScheme.class);
		newDropScheme.setName(getDropSchemeName());
		newDropScheme.setControlGraph(getTransformationAction().getFactory().newEmptyControlGraph());

		// Add new drop scheme
		newFlexoConcept.addToFlexoBehaviours(newDropScheme);

		newDropScheme.setTopTarget(isTopLevel());
		if (!isTopLevel()) {
			newDropScheme.setTargetFlexoConcept(getContainerFlexoConcept());
		}

		initializeDropScheme(newDropScheme);

		/*	if (isPushedToPalette) {
				DiagramPaletteElement _newPaletteElement = palette.addPaletteElement(newFlexoConcept.getName(),
						getFocusedObject().getGraphicalRepresentation());
				_newPaletteElement.setFlexoConcept(newFlexoConcept);
			}*/

		return newFlexoConcept;
	}

	protected void initializeDropScheme(DropScheme newDropScheme) {
		// Add shape/connector actions
		boolean mainFlexoRole = true;
		for (GraphicalElementRole<?, ?> graphicalElementRole : newGraphicalElementRoles.values()) {
			if (graphicalElementRole instanceof ShapeRole) {
				ShapeRole grFlexoRole = (ShapeRole) graphicalElementRole;
				// Add shape action
				AddShape newAddShape = getTransformationAction().getFactory().newInstance(AddShape.class);
				newAddShape.getReceiver().setUnparsedBinding(getTransformationAction().getDiagramModelSlot().getModelSlotName());
				// newAddShape.setModelSlot(getTransformationAction().getDiagramModelSlot());
				AssignationAction<DiagramShape> assignationAction = getTransformationAction().getFactory()
						.newAssignationAction(newAddShape);
				assignationAction.setAssignation(new DataBinding<>(graphicalElementRole.getRoleName()));

				newDropScheme.getControlGraph().sequentiallyAppend(assignationAction);

				List<TypedDiagramModelSlot> msList = graphicalElementRole.getFlexoConcept()
						.getAccessibleProperties(TypedDiagramModelSlot.class);
				if (msList.size() > 0) {
					newAddShape.setReceiver(new DataBinding<>(msList.get(0).getName()));
				}

				if (mainFlexoRole) {
					if (isTopLevel()) {
						newAddShape.setContainer(new DataBinding<DiagramContainerElement<?>>(DiagramBehaviourBindingModel.TOP_LEVEL));
					}
				}
				else {
					newAddShape.setContainer(new DataBinding<DiagramContainerElement<?>>(grFlexoRole.getParentShapeRole().getRoleName()));
				}
				mainFlexoRole = false;
			}
		}

	}

	@Override
	public void initializeBehaviours() {
	}

	public DropScheme getNewDropScheme() {
		return newDropScheme;
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
