/*
 * (c) Copyright 2014-2015 Openflexo
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
package org.openflexo.technologyadapter.diagram.fml.binding;

import java.beans.PropertyChangeEvent;

import org.openflexo.antar.binding.BindingModel;
import org.openflexo.antar.binding.BindingVariable;
import org.openflexo.foundation.viewpoint.FlexoConceptInstanceType;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;

/**
 * This is the {@link BindingModel} exposed by a {@link DropScheme}<br>
 * 
 * @author sylvain
 * 
 */
public class DropSchemeBindingModel extends DiagramBehaviourBindingModel {

	public static final String TARGET = "target";

	private BindingVariable targetBindingVariable;

	public DropSchemeBindingModel(DropScheme dropScheme) {
		super(dropScheme);

		if (dropScheme.getTargetFlexoConcept() != null) {
			targetBindingVariable = new BindingVariable(DropSchemeBindingModel.TARGET,
					FlexoConceptInstanceType.getFlexoConceptInstanceType(dropScheme.getTargetFlexoConcept()));
			addToBindingVariables(targetBindingVariable);
		}
	}

	@Override
	public DropScheme getFlexoBehaviour() {
		return (DropScheme) super.getFlexoBehaviour();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (evt.getSource() == getFlexoBehaviour()) {
			if (evt.getPropertyName().equals(DropScheme.TARGET_FLEXO_CONCEPT_KEY)
					|| evt.getPropertyName().equals(DropScheme.FLEXO_CONCEPT_KEY)) {
				// The DropScheme changes it's target's FlexoConcept
				if (targetBindingVariable == null) {
					if (getFlexoBehaviour().getTargetFlexoConcept() != null) {
						targetBindingVariable = new BindingVariable(DropSchemeBindingModel.TARGET,
								FlexoConceptInstanceType.getFlexoConceptInstanceType(getFlexoBehaviour().getTargetFlexoConcept()));
						addToBindingVariables(targetBindingVariable);
					}
				} else {
					if (getFlexoBehaviour().getTargetFlexoConcept() != null) {
						targetBindingVariable.setType(FlexoConceptInstanceType.getFlexoConceptInstanceType(getFlexoBehaviour()
								.getTargetFlexoConcept()));
					} else {
						removeFromBindingVariables(targetBindingVariable);
						targetBindingVariable = null;
					}
				}
			}
		}
	}

	public BindingVariable getTargetBindingVariable() {
		return targetBindingVariable;
	}
}
