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
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * This is the {@link BindingModel} exposed by a {@link LinkScheme}<br>
 * 
 * @author sylvain
 * 
 */
public class LinkSchemeBindingModel extends DiagramBehaviourBindingModel {

	private final BindingVariable fromTargetBindingVariable;
	private final BindingVariable toTargetBindingVariable;
	public static final String FROM_TARGET = "fromTarget";
	public static final String TO_TARGET = "toTarget";

	public LinkSchemeBindingModel(final LinkScheme linkScheme) {
		super(linkScheme);

		fromTargetBindingVariable = new BindingVariable(LinkSchemeBindingModel.FROM_TARGET,
				linkScheme.getFromTargetFlexoConcept() != null ? FlexoConceptInstanceType.getFlexoConceptInstanceType(linkScheme
						.getFromTargetFlexoConcept()) : DiagramShape.class);
		toTargetBindingVariable = new BindingVariable(LinkSchemeBindingModel.TO_TARGET,
				linkScheme.getToTargetFlexoConcept() != null ? FlexoConceptInstanceType.getFlexoConceptInstanceType(linkScheme
						.getToTargetFlexoConcept()) : DiagramShape.class);
		addToBindingVariables(fromTargetBindingVariable);
		addToBindingVariables(toTargetBindingVariable);
	}

	@Override
	public LinkScheme getFlexoBehaviour() {
		return (LinkScheme) super.getFlexoBehaviour();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (evt.getSource() == getFlexoBehaviour()) {
			if (evt.getPropertyName().equals(LinkScheme.FROM_TARGET_FLEXO_CONCEPT_KEY)) {
				// The LinkScheme changes it's FROM target's FlexoConcept
				fromTargetBindingVariable.setType(getFlexoBehaviour().getFromTargetFlexoConcept() != null ? FlexoConceptInstanceType
						.getFlexoConceptInstanceType(getFlexoBehaviour().getFromTargetFlexoConcept()) : DiagramShape.class);
			}
			else if (evt.getPropertyName().equals(LinkScheme.TO_TARGET_FLEXO_CONCEPT_KEY)) {
				// The LinkScheme changes it's TO target's FlexoConcept
				toTargetBindingVariable.setType(getFlexoBehaviour().getToTargetFlexoConcept() != null ? FlexoConceptInstanceType
						.getFlexoConceptInstanceType(getFlexoBehaviour().getToTargetFlexoConcept()) : DiagramShape.class);
			}
		}
	}

	public BindingVariable getFromTargetBindingVariable() {
		return fromTargetBindingVariable;
	}

	public BindingVariable getToTargetBindingVariable() {
		return toTargetBindingVariable;
	}
}
