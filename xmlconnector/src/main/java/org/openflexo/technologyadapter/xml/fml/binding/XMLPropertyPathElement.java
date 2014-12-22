/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2012-2014 Openflexo
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
import java.util.logging.Logger;

import org.openflexo.antar.binding.BindingEvaluationContext;
import org.openflexo.antar.binding.BindingPathElement;
import org.openflexo.antar.binding.SimplePathElement;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;


public class XMLPropertyPathElement extends SimplePathElement {

	private final XMLProperty property;

	private static final Logger logger = Logger.getLogger(XMLPropertyPathElement.class.getPackage().getName());

	public XMLPropertyPathElement(BindingPathElement parent, XMLProperty property) {
		super(parent, property.getName(), property.getType());
		this.property = property;
	}

	public XMLProperty getDataProperty() {
		return property;
	}

	@Override
	public Type getType() {

		return property.getType();
		
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
	public Object getBindingValue(Object target, BindingEvaluationContext context) throws TypeMismatchException, NullReferenceException {
		Object xsdAnswer = ((XMLIndividual) target).getPropertyValue(getPropertyName());
		return xsdAnswer;
	}

	@Override
	public void setBindingValue(Object value, Object target, BindingEvaluationContext context) throws TypeMismatchException,
			NullReferenceException {
		XMLProperty prop = ((XMLIndividual) target).getType().getPropertyByName(getPropertyName());
		((XMLIndividual) target).addPropertyValue(prop, value);
	}
}
