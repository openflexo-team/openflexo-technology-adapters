/*
 * (c) Copyright 2012-2013 AgileBirds
 * (c) Copyright 2014- Openflexo
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

package org.openflexo.technologyadapter.owl.fml.binding;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.antar.binding.BindingEvaluationContext;
import org.openflexo.antar.binding.BindingPathElement;
import org.openflexo.antar.binding.SimplePathElement;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.ontology.IFlexoOntologyIndividual;
import org.openflexo.foundation.ontology.IndividualOfClass;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLProperty;
import org.openflexo.technologyadapter.owl.model.PropertyStatement;
import org.openflexo.technologyadapter.owl.model.StatementWithProperty;

/**
 * Implements 'object' path element applied on {@link StatementWithProperty}
 * 
 * @author sylvain
 *
 */
public class StatementSubjectPathElement extends SimplePathElement {

	private static final Logger logger = Logger.getLogger(StatementSubjectPathElement.class.getPackage().getName());

	public static final String SUBJECT = "subject";

	private OWLProperty property = null;

	public StatementSubjectPathElement(BindingPathElement parent) {
		super(parent, SUBJECT, null); // Type is dynamically retrieved
		if (parent.getType() instanceof StatementWithProperty) {
			property = ((StatementWithProperty) parent.getType()).getProperty();
		} else {
			logger.warning("Unexpected type: " + parent.getType());
		}
	}

	@Override
	public Type getType() {
		if (property != null) {
			if (property.getDomain() instanceof OWLClass) {
				return IndividualOfClass.getIndividualOfClass((OWLClass) property.getDomain());
			}
		}
		return IFlexoOntologyIndividual.class;
	}

	@Override
	public String getLabel() {
		return getPropertyName();
	}

	@Override
	public String getTooltipText(Type resultingType) {
		return FlexoLocalization.localizedForKey("owl_property_subject");
	}

	@Override
	public Object getBindingValue(Object target, BindingEvaluationContext context) throws TypeMismatchException, NullReferenceException {
		if (target instanceof PropertyStatement) {
			return ((PropertyStatement) target).getSubject();
		}
		logger.warning("Please implement me, target=" + target + " context=" + context);
		return null;
	}

	@Override
	public void setBindingValue(Object value, Object target, BindingEvaluationContext context) throws TypeMismatchException,
			NullReferenceException {
		logger.warning("Please implement me, target=" + target + " context=" + context);
	}

}
