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

import org.openflexo.antar.binding.BindingEvaluationContext;
import org.openflexo.antar.binding.BindingPathElement;
import org.openflexo.antar.binding.SimplePathElement;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.ontology.IndividualOfClass;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;
import org.openflexo.technologyadapter.owl.model.OWLIndividual;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.OWLProperty;

/**
 * Implements a path element representing an OWL property applied on {@link IndividualOfClass}
 * 
 * @author sylvain
 *
 */
public abstract class PropertyStatementPathElement extends SimplePathElement {

	private static final Logger logger = Logger.getLogger(PropertyStatementPathElement.class.getPackage().getName());

	private final OWLProperty property;

	public static PropertyStatementPathElement makePropertyStatementPathElement(BindingPathElement aParent, OWLProperty property) {
		if (property instanceof OWLDataProperty) {
			return new DataPropertyStatementPathElement(aParent, (OWLDataProperty) property);
		} else if (property instanceof OWLObjectProperty) {
			if (((OWLObjectProperty) property).isLiteralRange()) {
				return new ObjectPropertyStatementAccessingLiteralPathElement(aParent, (OWLObjectProperty) property);
			} else {
				return new ObjectPropertyStatementAccessingObjectPathElement(aParent, (OWLObjectProperty) property);
			}
		} else {
			logger.warning("unexpected property " + property);
			return null;
		}
	}

	private PropertyStatementPathElement(BindingPathElement parent, OWLProperty property) {
		super(parent, property.getName(), OWLIndividual.class);
		this.property = property;
	}

	public OWLProperty getProperty() {
		return property;
	}

	@Override
	public String getLabel() {
		return getPropertyName();
	}

	@Override
	public String getTooltipText(Type resultingType) {
		return property.getDisplayableDescription();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + getLabel() + "[" + property + "]";
	}

	public static class DataPropertyStatementPathElement extends PropertyStatementPathElement {

		private DataPropertyStatementPathElement(BindingPathElement parent, OWLDataProperty property) {
			super(parent, property);
		}

		@Override
		public OWLDataProperty getProperty() {
			return (OWLDataProperty) super.getProperty();
		}

		@Override
		public Type getType() {
			if (getProperty() != null && getProperty().getDataType() != null) {
				return getProperty().getDataType().getAccessedType();
			}
			return Object.class;
		}

		@Override
		public Object getBindingValue(Object target, BindingEvaluationContext context) throws TypeMismatchException, NullReferenceException {
			if (target instanceof OWLIndividual) {
				// System.out.println("Property " + getPropertyName() + " for individual " + target + " return "
				// + ((OWLIndividual) target).getPropertyValue(getProperty()));
				return ((OWLIndividual) target).getPropertyValue(getProperty());
			}
			logger.warning("Please implement me, target=" + target + " context=" + context);
			return null;
		}

		@Override
		public void setBindingValue(Object value, Object target, BindingEvaluationContext context) throws TypeMismatchException,
				NullReferenceException {
			if (target instanceof OWLIndividual) {
				// System.out.println("Property " + getPropertyName() + " for individual " + target + " sets value " + value);
				((OWLIndividual) target).setPropertyValue(getProperty(), value);
				return;
			}
			logger.warning("Please implement me, target=" + target + " context=" + context);
		}
	}

	public static class ObjectPropertyStatementAccessingObjectPathElement extends PropertyStatementPathElement {

		private ObjectPropertyStatementAccessingObjectPathElement(BindingPathElement parent, OWLObjectProperty property) {
			super(parent, property);
		}

		@Override
		public OWLObjectProperty getProperty() {
			return (OWLObjectProperty) super.getProperty();
		}

		@Override
		public Type getType() {
			if (getProperty().getRange() instanceof OWLClass) {
				return IndividualOfClass.getIndividualOfClass(getProperty().getRange());
			}
			return OWLIndividual.class;
		}

		@Override
		public Object getBindingValue(Object target, BindingEvaluationContext context) throws TypeMismatchException, NullReferenceException {
			if (target instanceof OWLIndividual) {
				return ((OWLIndividual) target).getPropertyValue(getProperty());
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

	public static class ObjectPropertyStatementAccessingLiteralPathElement extends PropertyStatementPathElement {

		private ObjectPropertyStatementAccessingLiteralPathElement(BindingPathElement parent, OWLObjectProperty property) {
			super(parent, property);
		}

		@Override
		public Type getType() {
			return Object.class;
		}

		@Override
		public Object getBindingValue(Object target, BindingEvaluationContext context) throws TypeMismatchException, NullReferenceException {
			if (target instanceof OWLIndividual) {
				return ((OWLIndividual) target).getPropertyValue(getProperty());
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
}
