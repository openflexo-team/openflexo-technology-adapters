/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Owlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.owl.fml.binding;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.connie.BindingEvaluationContext;
import org.openflexo.connie.binding.IBindingPathElement;
import org.openflexo.connie.binding.SimplePathElement;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.owl.model.DataPropertyStatement;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;
import org.openflexo.technologyadapter.owl.model.StatementWithProperty;

/**
 * Implements 'value' path element applied on {@link StatementWithProperty} for any property statement using an {@link OWLDataProperty}
 * 
 * @author sylvain
 *
 */
public class StatementValuePathElement extends SimplePathElement {

	private static final Logger logger = Logger.getLogger(StatementValuePathElement.class.getPackage().getName());

	public static final String VALUE = "value";

	private OWLDataProperty property = null;

	public StatementValuePathElement(IBindingPathElement parent) {
		super(parent, VALUE, null); // Type is dynamically retrieved
		if (parent.getType() instanceof StatementWithProperty) {
			property = (OWLDataProperty) ((StatementWithProperty) parent.getType()).getProperty();
		}
		else {
			logger.warning("Unexpected type: " + parent.getType());
		}
	}

	public LocalizedDelegate getLocales() {
		if (property != null) {
			return property.getLocales();
		}
		return FlexoLocalization.getMainLocalizer();
	}

	@Override
	public Type getType() {
		if (property != null) {
			if (property.getDataType() != null) {
				return property.getDataType().getAccessedType();
			}
		}
		return Object.class;
	}

	@Override
	public String getLabel() {
		return getPropertyName();
	}

	@Override
	public String getTooltipText(Type resultingType) {
		return getLocales().localizedForKey("owl_property_value");
	}

	@Override
	public Object getBindingValue(Object target, BindingEvaluationContext context) throws TypeMismatchException, NullReferenceException {
		if (target instanceof DataPropertyStatement) {
			return ((DataPropertyStatement) target).getValue();
		}
		logger.warning("Please implement me, target=" + target + " context=" + context);
		return null;
	}

	@Override
	public void setBindingValue(Object value, Object target, BindingEvaluationContext context)
			throws TypeMismatchException, NullReferenceException {
		logger.warning("Please implement me, target=" + target + " context=" + context);
	}

}
