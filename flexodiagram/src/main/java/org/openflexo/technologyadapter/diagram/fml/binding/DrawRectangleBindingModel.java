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
import java.lang.reflect.Type;
import java.util.List;

import org.openflexo.connie.BindingModel;
import org.openflexo.connie.BindingVariable;
import org.openflexo.connie.type.ParameterizedTypeImpl;
import org.openflexo.diana.geom.DianaPoint;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
import org.openflexo.technologyadapter.diagram.fml.DrawRectangleScheme;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;

/**
 * This is the {@link BindingModel} exposed by a {@link DropScheme}<br>
 * 
 * @author sylvain
 * 
 */
public class DrawRectangleBindingModel extends DiagramBehaviourBindingModel {

	public static final String TARGET = "target";
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String SELECTION = "selection";

	private BindingVariable targetBindingVariable;
	private BindingVariable fromBindingVariable;
	private BindingVariable toBindingVariable;
	private BindingVariable selectionBindingVariable;

	public DrawRectangleBindingModel(DrawRectangleScheme drawRectangleScheme) {
		super(drawRectangleScheme);

		fromBindingVariable = new BindingVariable(DrawRectangleBindingModel.FROM, DianaPoint.class);
		addToBindingVariables(fromBindingVariable);
		toBindingVariable = new BindingVariable(DrawRectangleBindingModel.TO, DianaPoint.class);
		addToBindingVariables(toBindingVariable);

		if (drawRectangleScheme.getTargetFlexoConcept() != null) {
			targetBindingVariable = new BindingVariable(DrawRectangleBindingModel.TARGET,
					FlexoConceptInstanceType.getFlexoConceptInstanceType(drawRectangleScheme.getTargetFlexoConcept()));
			addToBindingVariables(targetBindingVariable);
		}

		if (drawRectangleScheme.getSelectObjects()) {
			Type selectionType;
			if (drawRectangleScheme.getChildrenFlexoConcept() != null) {
				selectionType = new ParameterizedTypeImpl(List.class,
						FlexoConceptInstanceType.getFlexoConceptInstanceType(drawRectangleScheme.getChildrenFlexoConcept()));
			}
			else {
				selectionType = new ParameterizedTypeImpl(List.class, FlexoConceptInstanceType.UNDEFINED_FLEXO_CONCEPT_INSTANCE_TYPE);
			}
			selectionBindingVariable = new BindingVariable(DrawRectangleBindingModel.SELECTION, selectionType);
			addToBindingVariables(selectionBindingVariable);
		}

	}

	@Override
	public DrawRectangleScheme getFlexoBehaviour() {
		return (DrawRectangleScheme) super.getFlexoBehaviour();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		if (evt.getPropertyName().equals(BindingModel.BASE_BINDING_MODEL_PROPERTY)) {
			// This is called when the BindingModel hierarchy change
			// We track here the fact that the FlexoBehaviour belongs to a FlexoConcept in a VirtualModel
			// This containment is necessary to retrieve FlexoConcept from its URI
			// Calling those methods will fire necessary notifications and will notify binding variable type change (see below)
			getFlexoBehaviour().getTargetFlexoConcept();
		}
		if (evt.getSource() == getFlexoBehaviour()) {
			if (evt.getPropertyName().equals(DrawRectangleScheme.TARGET_FLEXO_CONCEPT_KEY)
					|| evt.getPropertyName().equals(DrawRectangleScheme.FLEXO_CONCEPT_KEY)
					|| evt.getPropertyName().equals(DrawRectangleScheme.TOP_TARGET_KEY)) {
				// The DropScheme changes it's target's FlexoConcept
				if (targetBindingVariable == null) {
					if (getFlexoBehaviour().getTargetFlexoConcept() != null) {
						targetBindingVariable = new BindingVariable(DrawRectangleBindingModel.TARGET,
								FlexoConceptInstanceType.getFlexoConceptInstanceType(getFlexoBehaviour().getTargetFlexoConcept()));
						addToBindingVariables(targetBindingVariable);
					}
				}
				else {
					if (getFlexoBehaviour().getTargetFlexoConcept() != null) {
						targetBindingVariable
								.setType(FlexoConceptInstanceType.getFlexoConceptInstanceType(getFlexoBehaviour().getTargetFlexoConcept()));
					}
					else {
						removeFromBindingVariables(targetBindingVariable);
						targetBindingVariable = null;
					}
				}
			}
		}
		if (evt.getSource() == getFlexoBehaviour()) {
			if (evt.getPropertyName().equals(DrawRectangleScheme.SELECT_OBJECTS_KEY)
					|| evt.getPropertyName().equals(DrawRectangleScheme.CHILDREN_KEY)
					|| evt.getPropertyName().equals(DrawRectangleScheme.CHILDREN_FLEXO_CONCEPT_KEY)) {

				Type selectionType;
				if (getFlexoBehaviour().getChildrenFlexoConcept() != null) {
					selectionType = new ParameterizedTypeImpl(List.class,
							FlexoConceptInstanceType.getFlexoConceptInstanceType(getFlexoBehaviour().getChildrenFlexoConcept()));
				}
				else {
					selectionType = new ParameterizedTypeImpl(List.class, FlexoConceptInstanceType.UNDEFINED_FLEXO_CONCEPT_INSTANCE_TYPE);
				}

				// The DrawRectangleScheme changes it's children FlexoConcept
				if (selectionBindingVariable == null) {
					if (getFlexoBehaviour().getChildrenFlexoConcept() != null) {
						selectionBindingVariable = new BindingVariable(DrawRectangleBindingModel.SELECTION, selectionType);
						addToBindingVariables(selectionBindingVariable);
					}
				}
				else {
					if (getFlexoBehaviour().getSelectObjects()) {
						selectionBindingVariable.setType(selectionType);
					}
					else {
						removeFromBindingVariables(selectionBindingVariable);
						selectionBindingVariable = null;
					}
				}
			}
		}
	}

	public BindingVariable getTargetBindingVariable() {
		return targetBindingVariable;
	}

	public BindingVariable getSelectionBindingVariable() {
		return selectionBindingVariable;
	}

	public BindingVariable getFromBindingVariable() {
		return fromBindingVariable;
	}

	public BindingVariable getToBindingVariable() {
		return toBindingVariable;
	}
}
