/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2013-2015 Openflexo
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
import org.openflexo.model.validation.ValidationError;
import org.openflexo.model.validation.ValidationIssue;
import org.openflexo.model.validation.ValidationRule;
import org.openflexo.technologyadapter.owl.OWLModelSlot.OWLModelSlotImpl;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.ObjectPropertyStatement;
import org.openflexo.technologyadapter.owl.model.StatementWithProperty;

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

	public static abstract class ObjectPropertyStatementRoleImpl extends StatementRoleImpl<ObjectPropertyStatement> implements
			ObjectPropertyStatementRole {

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
		public String getPreciseType() {
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
			if (getOwningVirtualModel() != null) {
				return (OWLObjectProperty) getOwningVirtualModel().getOntologyObjectProperty(_getObjectPropertyURI());
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
	public static class ObjectPropertyStatementPatternRoleMustDefineAValidProperty extends
			ValidationRule<ObjectPropertyStatementPatternRoleMustDefineAValidProperty, ObjectPropertyStatementRole> {
		public ObjectPropertyStatementPatternRoleMustDefineAValidProperty() {
			super(ObjectPropertyStatementRole.class, "pattern_role_must_define_a_valid_object_property");
		}

		@Override
		public ValidationIssue<ObjectPropertyStatementPatternRoleMustDefineAValidProperty, ObjectPropertyStatementRole> applyValidation(
				ObjectPropertyStatementRole patternRole) {
			if (patternRole.getObjectProperty() == null) {
				return new ValidationError<ObjectPropertyStatementPatternRoleMustDefineAValidProperty, ObjectPropertyStatementRole>(this,
						patternRole, "pattern_role_does_not_define_any_valid_object_property");
			}
			return null;
		}
	}

}
