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

import java.lang.reflect.Type;

import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.fml.VirtualModelInstanceType;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.FMLRTModelSlot;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.editionaction.AddFlexoConceptInstance;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

/**
 * Encodes a {@link FlexoConcept} creation strategy, using a {@link DiagramConnector}<br>
 * We create a new {@link FlexoConcept} implementing the mapping between some diagram elements and a single {@link FlexoConceptInstance}
 * 
 * @author sylvain
 *
 */
public class MapConnectorToFlexoConceptlnstanceStrategy extends FlexoConceptFromConnectorCreationStrategy {

	private static final String NO_CONCEPT_DEFINED = "please_select_a_valid_flexo_concept";
	private static final String NO_ROLE_NAME = "please_enter_a_valid_role_name";

	private FlexoConcept typeConcept;
	private String flexoConceptInstanceRoleName;
	private FlexoConceptInstanceRole flexoConceptInstanceRole;

	public MapConnectorToFlexoConceptlnstanceStrategy(DeclareConnectorInFlexoConcept transformationAction) {
		super(transformationAction);
	}

	public String getFlexoConceptInstanceRoleName() {
		if (flexoConceptInstanceRoleName == null) {
			if (getTypeConcept() != null) {
				return JavaUtils.getVariableName(getTypeConcept().getName());
			}
			return "concept";
		}
		return flexoConceptInstanceRoleName;
	}

	public void setFlexoConceptInstanceRoleName(String flexoConceptInstanceRoleName) {
		if ((flexoConceptInstanceRoleName == null && this.flexoConceptInstanceRoleName != null)
				|| (flexoConceptInstanceRoleName != null && !flexoConceptInstanceRoleName.equals(this.flexoConceptInstanceRoleName))) {
			String oldValue = this.flexoConceptInstanceRoleName;
			this.flexoConceptInstanceRoleName = flexoConceptInstanceRoleName;
			getPropertyChangeSupport().firePropertyChange("flexoConceptInstanceRoleName", oldValue, flexoConceptInstanceRoleName);
		}
	}

	private DataBinding<FMLRTVirtualModelInstance> virtualModelInstance;

	public DataBinding<FMLRTVirtualModelInstance> getVirtualModelInstance() {
		if (virtualModelInstance == null) {
			virtualModelInstance = new DataBinding<>(this, FMLRTVirtualModelInstance.class, DataBinding.BindingDefinitionType.GET);
			virtualModelInstance.setBindingName("virtualModelInstance");
		}
		return virtualModelInstance;
	}

	public void setVirtualModelInstance(DataBinding<FMLRTVirtualModelInstance> aVirtualModelInstance) {
		if (aVirtualModelInstance != null) {
			aVirtualModelInstance.setOwner(this);
			aVirtualModelInstance.setBindingName("virtualModelInstance");
			aVirtualModelInstance.setDeclaredType(FMLRTVirtualModelInstance.class);
			aVirtualModelInstance.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
		}
		this.virtualModelInstance = aVirtualModelInstance;
	}

	public VirtualModel getVirtualModelType() {
		if (getVirtualModelInstance().isSet() && getVirtualModelInstance().isValid()) {
			Type type = getVirtualModelInstance().getAnalyzedType();
			if (type instanceof VirtualModelInstanceType) {
				return ((VirtualModelInstanceType) type).getVirtualModel();
			}
		}
		return null;
	}

	public FlexoConcept getTypeConcept() {
		return typeConcept;
	}

	public void setTypeConcept(FlexoConcept typeConcept) {
		if ((typeConcept == null && this.typeConcept != null) || (typeConcept != null && !typeConcept.equals(this.typeConcept))) {
			FlexoConcept oldValue = this.typeConcept;
			this.typeConcept = typeConcept;
			getPropertyChangeSupport().firePropertyChange("typeConcept", oldValue, typeConcept);
		}
	}

	/**
	 * Return flag indicating if currently selected {@link ModelSlot} is a {@link FMLRTModelSlot}
	 * 
	 * @return
	 */
	@Deprecated
	public boolean isVirtualModelModelSlot() {
		return (getTransformationAction() != null && getTransformationAction().isVirtualModelModelSlot());
	}

	@Override
	public boolean isValid() {

		if (!super.isValid()) {
			return false;
		}
		if (getTypeConcept() == null) {
			setIssueMessage(getLocales().localizedForKey(NO_CONCEPT_DEFINED), IssueMessageType.ERROR);
			return false;
		}
		if (StringUtils.isEmpty(getFlexoConceptInstanceRoleName())) {
			setIssueMessage(getLocales().localizedForKey(NO_ROLE_NAME), IssueMessageType.ERROR);
			return false;
		}
		return true;
	}

	@Override
	public FlexoConcept performStrategy() {
		FlexoConcept newFlexoConcept = super.performStrategy();

		FMLModelFactory factory = getTransformationAction().getFactory();
		flexoConceptInstanceRole = factory.newInstance(FlexoConceptInstanceRole.class);
		flexoConceptInstanceRole.setRoleName(getFlexoConceptInstanceRoleName());
		flexoConceptInstanceRole
				.setVirtualModelInstance(new DataBinding<VirtualModelInstance<?, ?>>(getVirtualModelInstance().toString()));
		flexoConceptInstanceRole.setFlexoConceptType(getTypeConcept());
		newFlexoConcept.addToFlexoProperties(flexoConceptInstanceRole);

		return getNewFlexoConcept();
	}

	@Override
	protected void initializeLinkScheme(LinkScheme newLinkScheme) {
		super.initializeLinkScheme(newLinkScheme);

		// AddFlexoConceptInstance action
		AddFlexoConceptInstance<?> newAddFCI = getTransformationAction().getFactory().newInstance(AddFlexoConceptInstance.class);
		newAddFCI.setReceiver(new DataBinding(getVirtualModelInstance().toString()));
		newAddFCI.setFlexoConceptType(getTypeConcept());
		if (getTypeConcept() != null && getTypeConcept().getCreationSchemes().size() > 0) {
			newAddFCI.setCreationScheme(getTypeConcept().getCreationSchemes().get(0));
		}

		AssignationAction<FlexoConceptInstance> assignationAction = getTransformationAction().getFactory().newAssignationAction(newAddFCI);
		assignationAction.setAssignation(new DataBinding<Object>(getFlexoConceptInstanceRoleName()));

		newLinkScheme.getControlGraph().sequentiallyAppend(assignationAction);

	}

	public FlexoConceptInstanceRole getFlexoConceptInstanceRole() {
		return flexoConceptInstanceRole;
	}

	@Override
	public String getPresentationName() {
		return "map_connector_to_flexo_concept_instance";
	}

	@Override
	public String getDescriptionKey() {
		return "<html>build_a_new_concept_representing_a_flexo_concept_instance</html>";
	}

	@Override
	public void notifiedBindingChanged(DataBinding<?> dataBinding) {
		super.notifiedBindingChanged(dataBinding);
		if (dataBinding == virtualModelInstance) {
			getPropertyChangeSupport().firePropertyChange("virtualModelInstance", null, getVirtualModelInstance());
			getPropertyChangeSupport().firePropertyChange("virtualModelType", null, getVirtualModelType());
		}
	}

}
