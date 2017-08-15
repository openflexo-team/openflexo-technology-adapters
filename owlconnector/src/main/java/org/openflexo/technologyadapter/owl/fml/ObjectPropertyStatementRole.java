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

package org.openflexo.technologyadapter.owl.fml;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.DefineValidationRule;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.validation.ValidationIssue;
import org.openflexo.model.validation.ValidationRule;
import org.openflexo.model.validation.ValidationWarning;
import org.openflexo.technologyadapter.owl.OWLModelSlot.OWLModelSlotImpl;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.ObjectPropertyStatement;
import org.openflexo.technologyadapter.owl.model.StatementWithProperty;
import org.openflexo.technologyadapter.owl.nature.OWLOntologyVirtualModelNature;

@ModelEntity
@Imports(@Import(ObjectPropertyStatementActorReference.class))
@ImplementationClass(ObjectPropertyStatementRole.ObjectPropertyStatementRoleImpl.class)
@XMLElement
@FML("ObjectPropertyStatementRole")
public interface ObjectPropertyStatementRole extends StatementRole<ObjectPropertyStatement> {

	@PropertyIdentifier(type = String.class)
	public static final String OBJECT_PROPERTY_URI_KEY = "objectPropertyURI";

	@Getter(value = OBJECT_PROPERTY_URI_KEY)
	@XMLAttribute(xmlTag = "objectProperty")
	public String _getObjectPropertyURI();

	@Setter(OBJECT_PROPERTY_URI_KEY)
	public void _setObjectPropertyURI(String objectPropertyURI);

	public OWLObjectProperty getObjectProperty();

	public void setObjectProperty(OWLObjectProperty p);

	public static abstract class ObjectPropertyStatementRoleImpl extends StatementRoleImpl<ObjectPropertyStatement>
			implements ObjectPropertyStatementRole {

		static final Logger logger = FlexoLogger.getLogger(ObjectPropertyStatementRole.class.getPackage().toString());

		public ObjectPropertyStatementRoleImpl() {
			super();
		}

		@Override
		public Type getType() {
			if (getObjectProperty() == null) {
				return ObjectPropertyStatement.class;
			}
			return StatementWithProperty.getStatementWithProperty(getObjectProperty());
		}

		@Override
		public String getTypeDescription() {
			if (getObjectProperty() != null) {
				return getObjectProperty().getName();
			}
			return "";
		}

		private String objectPropertyURI;

		@Override
		public String _getObjectPropertyURI() {
			return objectPropertyURI;
		}

		@Override
		public void _setObjectPropertyURI(String objectPropertyURI) {
			this.objectPropertyURI = objectPropertyURI;
		}

		@Override
		public OWLObjectProperty getObjectProperty() {
			if (OWLOntologyVirtualModelNature.INSTANCE.hasNature(getOwningVirtualModel())) {
				return OWLOntologyVirtualModelNature.getOWLObjectProperty(_getObjectPropertyURI(), getOwningVirtualModel());
			}
			return null;
		}

		@Override
		public void setObjectProperty(OWLObjectProperty p) {
			_setObjectPropertyURI(p != null ? p.getURI() : null);
		}

		@Override
		public ObjectPropertyStatementActorReference makeActorReference(ObjectPropertyStatement object, FlexoConceptInstance epi) {
			org.openflexo.model.factory.ModelFactory factory = OWLModelSlotImpl.getModelFactory();
			ObjectPropertyStatementActorReference returned = factory.newInstance(ObjectPropertyStatementActorReference.class);
			returned.setFlexoRole(this);
			returned.setFlexoConceptInstance(epi);
			returned.setModellingElement(object);
			return returned;
		}

	}

	@DefineValidationRule
	public static class ObjectPropertyStatementRoleMustDefineAValidProperty
			extends ValidationRule<ObjectPropertyStatementRoleMustDefineAValidProperty, ObjectPropertyStatementRole> {
		public ObjectPropertyStatementRoleMustDefineAValidProperty() {
			super(ObjectPropertyStatementRole.class, "statement_role_must_define_a_valid_object_property");
		}

		@Override
		public ValidationIssue<ObjectPropertyStatementRoleMustDefineAValidProperty, ObjectPropertyStatementRole> applyValidation(
				ObjectPropertyStatementRole patternRole) {
			if (patternRole.getObjectProperty() == null) {
				return new ValidationWarning<ObjectPropertyStatementRoleMustDefineAValidProperty, ObjectPropertyStatementRole>(this,
						patternRole, "statement_role_does_not_define_any_valid_object_property");
			}
			return null;
		}
	}

}
