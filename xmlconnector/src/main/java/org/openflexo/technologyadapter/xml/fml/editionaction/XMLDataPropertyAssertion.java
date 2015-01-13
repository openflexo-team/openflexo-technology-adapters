/*
 * (c) Copyright 2014 - Openflexo
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
package org.openflexo.technologyadapter.xml.fml.editionaction;

import java.lang.reflect.InvocationTargetException;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.binding.DataBinding.BindingDefinitionType;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.fml.editionaction.AbstractAssertion;
import org.openflexo.foundation.fml.editionaction.AddIndividual;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.validation.ValidationError;
import org.openflexo.model.validation.ValidationIssue;
import org.openflexo.model.validation.ValidationRule;
import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;

@ModelEntity
@ImplementationClass(XMLDataPropertyAssertion.XMLDataPropertyAssertionImpl.class)
@XMLElement
public interface XMLDataPropertyAssertion extends AbstractAssertion {

	@PropertyIdentifier(type = AddXMLIndividual.class)
	public static final String ACTION_KEY = "action";

	@PropertyIdentifier(type = String.class)
	public static final String DATA_PROPERTY_NAME_KEY = "dataPropertyNAME";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String VALUE_KEY = "value";

	@Override
	@Getter(value = ACTION_KEY, inverse = AddXMLIndividual.DATA_ASSERTIONS_KEY)
	public AddIndividual<?, ?> getAction();

	@Override
	@Setter(ACTION_KEY)
	public void setAction(AddIndividual<?, ?> action);

	@Getter(value = DATA_PROPERTY_NAME_KEY)
	@XMLAttribute
	public String _getDataPropertyName();

	@Setter(DATA_PROPERTY_NAME_KEY)
	public void _setDataPropertyName(String dataPropertyURI);

	@Getter(value = VALUE_KEY)
	@XMLAttribute
	public DataBinding<?> getValue();

	@Setter(VALUE_KEY)
	public void setValue(DataBinding<?> value);

	public XMLDataProperty getDataProperty();

	public void setDataProperty(XMLDataProperty p);

	public Object getValue(FlexoBehaviourAction action);

	public java.lang.reflect.Type getType();

	public static abstract class XMLDataPropertyAssertionImpl extends AbstractAssertionImpl implements XMLDataPropertyAssertion {

		private String dataPropertyName;
		private DataBinding<?> value;

		public XMLDataPropertyAssertionImpl() {
			super();
		}

		@Override
		public String _getDataPropertyName() {
			return dataPropertyName;
		}

		@Override
		public void _setDataPropertyName(String dataPropertyURI) {
			this.dataPropertyName = dataPropertyURI;
		}

		@Override
		public XMLDataProperty getDataProperty() {
			AddXMLIndividual act = (AddXMLIndividual) getAction();
			String pname = _getDataPropertyName();
			if (act != null && pname != null) {
				String typeURI = act.getTypeURI();
				XMLMetaModel mm = act.getMetamodel();
				if (mm != null) {
					XMLComplexType t = ((XMLComplexType) mm.getTypeFromURI(typeURI));
					if (t != null)
						return (XMLDataProperty) t.getPropertyByName(pname);
				}
			}
			return null;
		}

		@Override
		public void setDataProperty(XMLDataProperty p) {
			_setDataPropertyName(p != null ? p.getName() : null);
		}

		@Override
		public Object getValue(FlexoBehaviourAction action) {
			try {
				return getValue().getBindingValue(action);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public java.lang.reflect.Type getType() {
			if (getDataProperty() instanceof IFlexoOntologyDataProperty) {
				if (((IFlexoOntologyDataProperty) getDataProperty()).getRange() != null) {
					return ((IFlexoOntologyDataProperty) getDataProperty()).getRange().getAccessedType();
				}
			}
			return Object.class;
		};

		@Override
		public DataBinding<?> getValue() {
			if (value == null) {
				value = new DataBinding<Object>(this, getType(), BindingDefinitionType.GET);
				value.setBindingName("value");
			}
			return value;
		}

		@Override
		public void setValue(DataBinding<?> value) {
			if (value != null) {
				value.setOwner(this);
				value.setBindingName("value");
				value.setDeclaredType(getType());
				value.setBindingDefinitionType(BindingDefinitionType.GET);
			}
			this.value = value;
		}

	}

	public static class DataPropertyAssertionMustDefineAnOntologyProperty extends
			ValidationRule<DataPropertyAssertionMustDefineAnOntologyProperty, XMLDataPropertyAssertion> {
		public DataPropertyAssertionMustDefineAnOntologyProperty() {
			super(XMLDataPropertyAssertion.class, "data_property_assertion_must_define_an_ontology_property");
		}

		@Override
		public ValidationIssue<DataPropertyAssertionMustDefineAnOntologyProperty, XMLDataPropertyAssertion> applyValidation(
				XMLDataPropertyAssertion assertion) {
			if (assertion.getDataProperty() == null) {
				return new ValidationError<DataPropertyAssertionMustDefineAnOntologyProperty, XMLDataPropertyAssertion>(this, assertion,
						"data_property_assertion_does_not_define_an_ontology_property");
			}
			return null;
		}

	}

	public static class ValueBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<XMLDataPropertyAssertion> {
		public ValueBindingIsRequiredAndMustBeValid() {
			super("'value'_binding_is_required_and_must_be_valid", XMLDataPropertyAssertion.class);
		}

		@Override
		public DataBinding<?> getBinding(XMLDataPropertyAssertion object) {
			return object.getValue();
		}

	}

}
