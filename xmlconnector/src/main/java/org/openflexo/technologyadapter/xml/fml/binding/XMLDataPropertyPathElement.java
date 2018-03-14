/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.xml.fml.binding;

import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.connie.binding.IBindingPathElement;
import org.openflexo.connie.binding.SimplePathElement;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.connie.type.ParameterizedTypeImpl;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.model.XMLDataPropertyValue;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;

public class XMLDataPropertyPathElement extends SimplePathElement {

	private final XMLDataProperty property;

	private static final Logger logger = Logger.getLogger(XMLDataPropertyPathElement.class.getPackage().getName());

	public XMLDataPropertyPathElement(IBindingPathElement parent, XMLDataProperty property) {
		super(parent, property.getName(), property.getType());
		this.property = property;
	}

	public XMLDataProperty getDataProperty() {
		return property;
	}

	@Override
	public Type getType() {
		if (property != null) {
			if (property.getUpperBound() == null || (property.getUpperBound() >= 0 && property.getUpperBound() <= 1)) {
				// Single cardinality
				if (property.getType() != null) {
					return property.getType();
				}
				return Object.class;
			}
			if (property != null && property.getType() != null) {
				return new ParameterizedTypeImpl(List.class, property.getType());
			}
			return new ParameterizedTypeImpl(List.class, Object.class);
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
	public Object getBindingValue(Object target, BindingEvaluationContext context) throws TypeMismatchException, NullReferenceException {
		XMLDataPropertyValue xsdAnswer = (XMLDataPropertyValue) ((XMLIndividual) target).getPropertyValue(getDataProperty());

		if (xsdAnswer != null) {
			return xsdAnswer.getValue();
		}
		return null;
	}

	@Override
	public void setBindingValue(Object value, Object target, BindingEvaluationContext context)
			throws TypeMismatchException, NullReferenceException {
		XMLProperty prop = ((XMLIndividual) target).getType().getPropertyByName(getPropertyName());
		((XMLIndividual) target).addPropertyValue(prop, value);
	}
}
