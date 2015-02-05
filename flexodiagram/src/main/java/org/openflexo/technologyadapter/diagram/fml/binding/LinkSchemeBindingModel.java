/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.diagram.fml.binding;

import java.beans.PropertyChangeEvent;

import org.openflexo.connie.BindingModel;
import org.openflexo.connie.BindingVariable;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
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
		if (evt.getPropertyName().equals(BindingModel.BASE_BINDING_MODEL_PROPERTY)) {
			// This is called when the BindingModel hierarchy change
			// We track here the fact that the FlexoBehaviour belongs to a FlexoConcept in a VirtualModel
			// This containment is necessary to retrieve FlexoConcept from its URI
			// Calling those methods will fire necessary notifications and will notify binding variable type change (see below)
			getFlexoBehaviour().getFromTargetFlexoConcept();
			getFlexoBehaviour().getToTargetFlexoConcept();
		}
		if (evt.getSource() == getFlexoBehaviour()) {
			if (evt.getPropertyName().equals(LinkScheme.FROM_TARGET_FLEXO_CONCEPT_KEY) && fromTargetBindingVariable != null) {
				// The LinkScheme changes it's FROM target's FlexoConcept
				fromTargetBindingVariable.setType(getFlexoBehaviour().getFromTargetFlexoConcept() != null ? FlexoConceptInstanceType
						.getFlexoConceptInstanceType(getFlexoBehaviour().getFromTargetFlexoConcept()) : DiagramShape.class);
			} else if (evt.getPropertyName().equals(LinkScheme.TO_TARGET_FLEXO_CONCEPT_KEY) && toTargetBindingVariable != null) {
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
