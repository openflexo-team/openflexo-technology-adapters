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

import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.fml.rt.FMLRTModelSlot;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.JavaUtils;

/**
 * Encodes a {@link FlexoConcept} creation strategy, using a {@link DiagramShape}<br>
 * We create a new {@link FlexoConcept} implementing the mapping between some diagram elements and a single individual
 * 
 * @author sylvain
 *
 */
public class MapConnectorToFlexoConceptlnstanceStrategy extends FlexoConceptFromConnectorCreationStrategy {

	private static final String NO_FML_RT_MODEL_SLOT_DEFINED = "please_select_a_valid_model_slot";
	private static final String NO_CONCEPT_DEFINED = "no_concept_defined_as_type_of_flexo_concept_instance";

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
	public boolean isVirtualModelModelSlot() {
		return (getTransformationAction() != null && getTransformationAction().isVirtualModelModelSlot());
	}

	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}
		if (!isVirtualModelModelSlot()) {
			setIssueMessage(getLocales().localizedForKey(NO_FML_RT_MODEL_SLOT_DEFINED), IssueMessageType.ERROR);
			return false;
		}
		if (getTypeConcept() == null) {
			setIssueMessage(getLocales().localizedForKey(NO_CONCEPT_DEFINED), IssueMessageType.ERROR);
			return false;
		}
		return true;
	}

	@Override
	public FlexoConcept performStrategy() {
		FlexoConcept newFlexoConcept = super.performStrategy();

		if (isVirtualModelModelSlot()) {

			FMLRTModelSlot virtualModelModelSlot = (FMLRTModelSlot) getTransformationAction().getInformationSourceModelSlot();
			flexoConceptInstanceRole = virtualModelModelSlot.makeFlexoConceptInstanceRole(getTypeConcept());
			flexoConceptInstanceRole.setRoleName(getFlexoConceptInstanceRoleName());
			newFlexoConcept.addToFlexoProperties(flexoConceptInstanceRole);
		}

		return getNewFlexoConcept();
	}

	public FlexoConceptInstanceRole getFlexoConceptInstanceRole() {
		return flexoConceptInstanceRole;
	}
}
