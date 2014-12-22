/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2012-2013 Openflexo
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

package org.openflexo.technologyadapter.xml.fml.binding;

import java.lang.reflect.Type;
import java.util.List;

import org.openflexo.antar.binding.BindingEvaluationContext;
import org.openflexo.antar.binding.BindingPathElement;
import org.openflexo.antar.binding.ParameterizedTypeImpl;
import org.openflexo.antar.binding.SimplePathElement;
import org.openflexo.antar.expr.InvocationTargetTransformException;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.technologyadapter.xml.metamodel.XMLObjectPropertyImpl;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;

/**
 * @author xtof
 *
 */
public class XMLObjectPropertyPathElement extends SimplePathElement {

	private final XMLObjectPropertyImpl property;

	public XMLObjectPropertyPathElement(BindingPathElement parent, XMLObjectPropertyImpl property) {
		super(parent, property.getName(), property.getType());
		this.property = property;
	}

	@Override
	public Type getType() {
		if (property  != null) {
			if (property.getUpperBound() == null || (property.getUpperBound() >= 0 && property.getUpperBound() <= 1)) {
				// Single cardinality
				if (property.getType() != null) {
					return property.getType();
				}
				return Object.class;
			} else {
				if (property.getType() != null) {
					return new ParameterizedTypeImpl(List.class, XMLIndividual.class);
				}
				return new ParameterizedTypeImpl(List.class, Object.class);
			}
		}
		return null;

	}

	@Override
	public String getLabel() {
		return getPropertyName();
	}


	@Override
	public String getTooltipText(Type resultingType) {
		return "DataAttribute " + property.getDisplayableDescription();
	}

	@Override
	public Object getBindingValue(Object target,
			BindingEvaluationContext context) throws TypeMismatchException,
			NullReferenceException, InvocationTargetTransformException {
		if (property != null){
			// FIXME => check that this actually works with ObjectProperties
			return ((XMLIndividual) target).getPropertyValue(property);
		}
		return null;
	}

	@Override
	public void setBindingValue(Object value, Object target,
			BindingEvaluationContext context) throws TypeMismatchException,
			NullReferenceException {

		XMLProperty prop = ((XMLIndividual) target).getType().getPropertyByName(getPropertyName());
		((XMLIndividual) target).addPropertyValue(prop, value);
	}

}
