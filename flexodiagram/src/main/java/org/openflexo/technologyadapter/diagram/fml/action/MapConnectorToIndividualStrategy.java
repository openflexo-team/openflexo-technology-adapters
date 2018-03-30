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

import java.io.FileNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.binding.IBindingPathElement;
import org.openflexo.connie.expr.BindingValue;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.FlexoBehaviourParameter;
import org.openflexo.foundation.fml.FlexoBehaviourParameter.WidgetType;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.binding.ModelSlotBindingVariable;
import org.openflexo.foundation.fml.binding.VirtualModelModelSlotPathElement;
import org.openflexo.foundation.fml.editionaction.AssignableAction;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.fml.IndividualRole;
import org.openflexo.foundation.ontology.fml.editionaction.AddIndividual;
import org.openflexo.foundation.ontology.technologyadapter.FlexoOntologyModelSlot;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import org.openflexo.foundation.technologyadapter.FlexoModel;
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

	private static final String NO_CONCEPT_DEFINED = "please_select_a_valid_concept_as_type_of_mapped_instance";
	private static final String NO_ROLE_NAME = "please_enter_a_valid_role_name";

	private IFlexoOntologyClass<?> concept;
	private String individualFlexoRoleName;
	private IndividualRole<?> individualFlexoRole;
	private DataBinding<FlexoModel> model;

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

	public DataBinding<FlexoModel> getModel() {
		if (model == null) {
			model = new DataBinding<>(this, FlexoModel.class, DataBinding.BindingDefinitionType.GET);
			model.setBindingName("model");
		}
		return model;
	}

	public void setModel(DataBinding<FlexoModel> aModel) {
		if (aModel != null) {
			aModel.setOwner(this);
			aModel.setBindingName("model");
			aModel.setDeclaredType(FlexoModel.class);
			aModel.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
		}
		this.model = aModel;
	}

	public FlexoOntologyModelSlot<?, ?, ?> getFlexoOntologyModelSlot() {
		if (getModel().isSet() && getModel().isValid()) {
			if (getModel().isBindingValue()) {
				BindingValue bv = (BindingValue) getModel().getExpression();
				IBindingPathElement bpe = bv.getLastBindingPathElement();
				ModelSlot<?> ms = null;
				if (bpe instanceof ModelSlotBindingVariable) {
					ms = ((ModelSlotBindingVariable) bpe).getModelSlot();
				}
				else if (bpe instanceof VirtualModelModelSlotPathElement) {
					ms = ((VirtualModelModelSlotPathElement<?>) bpe).getModelSlot();
				}
				if (ms instanceof FlexoOntologyModelSlot) {
					return (FlexoOntologyModelSlot<?, ?, ?>) ms;
				}
			}
		}
		return null;
	}

	public FlexoMetaModel<?> getMetaModel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		if (getFlexoOntologyModelSlot() != null) {
			return getFlexoOntologyModelSlot().getMetaModelResource().getResourceData();
		}
		return null;
	}

	public IFlexoOntology<?> getMetaModelOntology() {
		FlexoMetaModel<?> metaMododel;
		try {
			metaMododel = getMetaModel();
			if (metaMododel instanceof IFlexoOntology) {
				return (IFlexoOntology<?>) metaMododel;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlexoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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

	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}
		if (getConcept() == null) {
			setIssueMessage(getLocales().localizedForKey(NO_CONCEPT_DEFINED), IssueMessageType.ERROR);
			return false;
		}
		if (StringUtils.isEmpty(getIndividualFlexoRoleName())) {
			setIssueMessage(getLocales().localizedForKey(NO_ROLE_NAME), IssueMessageType.ERROR);
			return false;
		}
		return true;
	}

	@Override
	public FlexoConcept performStrategy() {

		FlexoConcept newFlexoConcept = super.performStrategy();

		FlexoOntologyModelSlot<?, ?, ?> flexoOntologyModelSlot = getFlexoOntologyModelSlot();

		if (flexoOntologyModelSlot != null) {
			individualFlexoRole = flexoOntologyModelSlot.makeIndividualRole(getConcept());
			individualFlexoRole.setRoleName(getIndividualFlexoRoleName());
			individualFlexoRole.setOntologicType(getConcept());
			newFlexoConcept.addToFlexoProperties(individualFlexoRole);
		}

		return newFlexoConcept;
	}

	public IndividualRole<?> getIndividualFlexoRole() {
		return individualFlexoRole;
	}

	@Override
	protected void initializeLinkScheme(LinkScheme newLinkScheme) {

		FlexoOntologyModelSlot<?, ?, ?> flexoOntologyModelSlot = getFlexoOntologyModelSlot();

		if (flexoOntologyModelSlot != null) {
			FlexoBehaviourParameter uriParameter = getTransformationAction().getFactory().newParameter(newLinkScheme);
			uriParameter.setWidget(WidgetType.TEXT_FIELD);
			uriParameter.setName("uri");
			newLinkScheme.addToParameters(uriParameter);

			// Add individual action
			AddIndividual<?, ?, ?, ?> newAddIndividual = flexoOntologyModelSlot.makeAddIndividualAction(individualFlexoRole, newLinkScheme);
			newAddIndividual.setOntologyClass(getConcept());

			AssignationAction<?> newAssignationAction = getTransformationAction().getFactory().newAssignationAction();
			newAssignationAction.setAssignableAction((AssignableAction) newAddIndividual);
			newAssignationAction.setAssignation(new DataBinding<>(getIndividualFlexoRoleName()));

			newLinkScheme.getControlGraph().sequentiallyAppend(newAssignationAction);

		}

		super.initializeLinkScheme(newLinkScheme);

	}

	@Override
	public String getPresentationName() {
		return "map_connector_to_individual";
	}

	@Override
	public String getDescriptionKey() {
		return "<html>build_a_new_concept_representing_an_individual</html>";
	}

	@Override
	public void notifiedBindingChanged(DataBinding<?> dataBinding) {
		super.notifiedBindingChanged(dataBinding);
		if (dataBinding == model) {
			getPropertyChangeSupport().firePropertyChange("model", null, getModel());
			getPropertyChangeSupport().firePropertyChange("metaModelOntology", null, getMetaModelOntology());
		}
	}

}
