/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.technologyadapter.owl.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.fib.annotation.FIBPanel;
import org.openflexo.foundation.fml.FMLRepresentationContext;
import org.openflexo.foundation.fml.FMLRepresentationContext.FMLRepresentationOutput;
import org.openflexo.foundation.fml.FlexoProperty;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.editionaction.SetDataPropertyValueAction;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.foundation.ontology.IndividualOfClass;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.validation.FixProposal;
import org.openflexo.model.validation.ValidationError;
import org.openflexo.model.validation.ValidationIssue;
import org.openflexo.model.validation.ValidationRule;
import org.openflexo.technologyadapter.owl.fml.DataPropertyStatementRole;
import org.openflexo.technologyadapter.owl.model.DataPropertyStatement;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;
import org.openflexo.technologyadapter.owl.model.StatementWithProperty;
import org.openflexo.toolbox.StringUtils;

@FIBPanel("Fib/AddDataPropertyStatementPanel.fib")
@ModelEntity
@ImplementationClass(AddDataPropertyStatement.AddDataPropertyStatementImpl.class)
@XMLElement
@FML("AddDataPropertyStatement")
public interface AddDataPropertyStatement extends AddStatement<DataPropertyStatement>, SetDataPropertyValueAction<DataPropertyStatement> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String VALUE_KEY = "value";
	@PropertyIdentifier(type = String.class)
	public static final String DATA_PROPERTY_URI_KEY = "dataPropertyURI";

	@Override
	@Getter(value = VALUE_KEY)
	@XMLAttribute
	public DataBinding<?> getValue();

	@Override
	@Setter(VALUE_KEY)
	public void setValue(DataBinding<?> value);

	@Getter(value = DATA_PROPERTY_URI_KEY)
	@XMLAttribute
	public String _getDataPropertyURI();

	@Setter(DATA_PROPERTY_URI_KEY)
	public void _setDataPropertyURI(String dataPropertyURI);

	@Override
	public IFlexoOntologyStructuralProperty getProperty();

	@Override
	public void setProperty(IFlexoOntologyStructuralProperty aProperty);

	public static abstract class AddDataPropertyStatementImpl extends AddStatementImpl<DataPropertyStatement> implements
			AddDataPropertyStatement {

		private static final Logger logger = Logger.getLogger(AddDataPropertyStatement.class.getPackage().getName());

		private String dataPropertyURI = null;
		private DataBinding<?> value;

		public AddDataPropertyStatementImpl() {
			super();
		}

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			FMLRepresentationOutput out = new FMLRepresentationOutput(context);
			out.append((getSubject() != null ? getSubject().toString() : "null") + "."
					+ (getDataProperty() != null ? getDataProperty().getName() : "null") + " = " + getValue().toString() + ";", context);
			return out.toString();
		}

		@Override
		public Type getSubjectType() {
			if (getDataProperty() != null && getDataProperty().getDomain() instanceof IFlexoOntologyClass) {
				return IndividualOfClass.getIndividualOfClass((IFlexoOntologyClass) getDataProperty().getDomain());
			}
			return super.getSubjectType();
		}

		/*@Override
		public List<DataPropertyStatementRole> getAvailablePatternRoles() {
			return getFlexoConcept().getPatternRoles(DataPropertyStatementRole.class);
		}*/

		@Override
		public DataPropertyStatementRole getAssignedFlexoProperty() {
			FlexoProperty<?> superFlexoRole = super.getAssignedFlexoProperty();
			if (superFlexoRole instanceof DataPropertyStatementRole) {
				return (DataPropertyStatementRole) superFlexoRole;
			} else if (superFlexoRole != null) {
				// logger.warning("Unexpected pattern property of type " + superPatternRole.getClass().getSimpleName());
				return null;
			}
			return null;
		}

		@Override
		public IFlexoOntologyStructuralProperty getProperty() {
			return getDataProperty();
		}

		@Override
		public void setProperty(IFlexoOntologyStructuralProperty aProperty) {
			setDataProperty((OWLDataProperty) aProperty);
		}

		@Override
		public OWLDataProperty getDataProperty() {
			if (getOwningVirtualModel() != null && StringUtils.isNotEmpty(dataPropertyURI)) {
				return (OWLDataProperty) getOwningVirtualModel().getOntologyDataProperty(dataPropertyURI);
			} else {
				if (getAssignedFlexoProperty() != null) {
					return getAssignedFlexoProperty().getDataProperty();
				}
			}
			return null;
		}

		@Override
		public void setDataProperty(IFlexoOntologyDataProperty ontologyProperty) {
			if (ontologyProperty != null) {
				if (getAssignedFlexoProperty() != null) {
					if (getAssignedFlexoProperty().getDataProperty().isSuperConceptOf(ontologyProperty)) {
						dataPropertyURI = ontologyProperty.getURI();
					} else {
						getAssignedFlexoProperty().setDataProperty((OWLDataProperty) ontologyProperty);
					}
				} else {
					dataPropertyURI = ontologyProperty.getURI();
				}
			} else {
				dataPropertyURI = null;
			}
		}

		@Override
		public String _getDataPropertyURI() {
			if (getDataProperty() != null) {
				if (getAssignedFlexoProperty() != null && getAssignedFlexoProperty().getDataProperty() == getDataProperty()) {
					// No need to store an overriding type, just use default provided by pattern property
					return null;
				}
				return getDataProperty().getURI();
			}
			return dataPropertyURI;
		}

		@Override
		public void _setDataPropertyURI(String dataPropertyURI) {
			this.dataPropertyURI = dataPropertyURI;
		}

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

		public Type getType() {
			if (getDataProperty() != null) {
				return getDataProperty().getRange().getAccessedType();
			}
			return Object.class;
		};

		@Override
		public DataBinding<?> getValue() {
			if (value == null) {
				value = new DataBinding<Object>(this, getType(), BindingDefinitionType.GET) {
					@Override
					public Type getDeclaredType() {
						return getType();
					}
				};
				value.setBindingName("value");
			}
			return value;
		}

		@Override
		public void setValue(DataBinding<?> value) {
			if (value != null) {
				value = new DataBinding<Object>(value.toString(), this, getType(), BindingDefinitionType.GET) {
					@Override
					public Type getDeclaredType() {
						return getType();
					}
				};
				value.setBindingName("value");
			}
			this.value = value;
		}

		@Override
		public Type getAssignableType() {
			if (getDataProperty() == null) {
				return DataPropertyStatement.class;
			}
			return StatementWithProperty.getStatementWithProperty(getDataProperty());
		}

		@Override
		public String getStringRepresentation() {
			if (getSubject() == null || getDataProperty() == null || getValue() == null) {
				return "Add DataPropertyStatement";
			}
			return getSubject() + " " + (getDataProperty() != null ? getDataProperty().getName() : "null") + " " + getValue();
		}

		@Override
		public DataPropertyStatement execute(FlexoBehaviourAction<?, ?, ?> action) {
			OWLDataProperty property = getDataProperty();
			OWLConcept<?> subject = getPropertySubject(action);
			Object value = getValue(action);
			if (property == null) {
				return null;
			}
			if (subject == null) {
				return null;
			}
			if (value == null) {
				return null;
			}
			return subject.addDataPropertyStatement(property, value);
		}

	}

	public static class AddDataPropertyStatementActionMustDefineADataProperty extends
			ValidationRule<AddDataPropertyStatementActionMustDefineADataProperty, AddDataPropertyStatement> {
		public AddDataPropertyStatementActionMustDefineADataProperty() {
			super(AddDataPropertyStatement.class, "add_data_property_statement_action_must_define_a_data_property");
		}

		@Override
		public ValidationIssue<AddDataPropertyStatementActionMustDefineADataProperty, AddDataPropertyStatement> applyValidation(
				AddDataPropertyStatement action) {
			if (action.getDataProperty() == null && action.getOwner() instanceof AssignationAction) {
				Vector<FixProposal<AddDataPropertyStatementActionMustDefineADataProperty, AddDataPropertyStatement>> v = new Vector<FixProposal<AddDataPropertyStatementActionMustDefineADataProperty, AddDataPropertyStatement>>();
				for (DataPropertyStatementRole pr : action.getFlexoConcept().getFlexoProperties(DataPropertyStatementRole.class)) {
					v.add(new SetsFlexoRole(pr));
				}
				return new ValidationError<AddDataPropertyStatementActionMustDefineADataProperty, AddDataPropertyStatement>(this, action,
						"add_data_property_statement_action_does_not_define_an_data_property", v);
			}
			return null;
		}

		protected static class SetsFlexoRole extends
				FixProposal<AddDataPropertyStatementActionMustDefineADataProperty, AddDataPropertyStatement> {

			private final DataPropertyStatementRole flexoRole;

			public SetsFlexoRole(DataPropertyStatementRole flexoRole) {
				super("assign_action_to_flexo_role_($flexoRole.flexoRoleName)");
				this.flexoRole = flexoRole;
			}

			public DataPropertyStatementRole getFlexoRole() {
				return flexoRole;
			}

			@Override
			protected void fixAction() {
				AddDataPropertyStatement action = getValidable();
				((AssignationAction) action.getOwner()).setAssignation(new DataBinding<Object>(flexoRole.getRoleName()));
			}

		}
	}

	public static class ValueIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddDataPropertyStatement> {
		public ValueIsRequiredAndMustBeValid() {
			super("'value'_binding_is_required_and_must_be_valid", AddDataPropertyStatement.class);
		}

		@Override
		public DataBinding<?> getBinding(AddDataPropertyStatement object) {
			return object.getValue();
		}

	}

}
