/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf.fml.binding;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.connie.binding.IBindingPathElement;
import org.openflexo.connie.binding.SimplePathElement;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.connie.type.ParameterizedTypeImpl;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeAssociation;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeDataProperty;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividual;

public class AttributeDataPropertyFeatureAssociationPathElement extends SimplePathElement {

	private EMFAttributeDataProperty dataProperty;
	private EMFAttributeAssociation association;

	private static final Logger logger = Logger.getLogger(AttributeDataPropertyFeatureAssociationPathElement.class.getPackage().getName());

	public AttributeDataPropertyFeatureAssociationPathElement(IBindingPathElement parent, EMFAttributeAssociation association,
			EMFAttributeDataProperty property) {
		super(parent, property.getName(), property.getRange().getAccessedType());
		this.association = association;
		dataProperty = property;
	}

	public EMFAttributeDataProperty getDataProperty() {
		return dataProperty;
	}

	@Override
	public Type getType() {
		if (association.getUpperBound() == null || (association.getUpperBound() >= 0 && association.getUpperBound() <= 1)) {
			// Single cardinality
			if (getDataProperty() != null && getDataProperty().getRange() != null) {
				return getDataProperty().getRange().getAccessedType();
			}
			return Object.class;
		}
		else {
			if (getDataProperty() != null && getDataProperty().getRange() != null) {
				return new ParameterizedTypeImpl(List.class, getDataProperty().getRange().getAccessedType());
			}
			return new ParameterizedTypeImpl(List.class, Object.class);
		}
	}

	@Override
	public String getLabel() {
		return getPropertyName();
	}

	@Override
	public String getTooltipText(Type resultingType) {
		return "DataAttribute " + dataProperty.getDisplayableDescription();
	}

	@Override
	public Object getBindingValue(Object target, BindingEvaluationContext context) throws TypeMismatchException, NullReferenceException {
		EMFModel model = ((EMFObjectIndividual) target).getFlexoOntology();
		Object emfAnswer = ((EMFObjectIndividual) target).getObject().eGet(dataProperty.getObject());
		// System.out.println("AttributeDataPropertyFeatureAssociationPathElement, returning " + emfAnswer + " of " + (emfAnswer != null ?
		// emfAnswer.getClass() : null));
		return emfAnswer;
	}

	@Override
	public void setBindingValue(Object value, Object target, BindingEvaluationContext context)
			throws TypeMismatchException, NullReferenceException {
		((EMFObjectIndividual) target).getObject().eSet(dataProperty.getObject(), value);
	}
}
