package org.openflexo.technologyadapter.owl.viewpoint;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.foundation.validation.ValidationError;
import org.openflexo.foundation.validation.ValidationIssue;
import org.openflexo.foundation.validation.ValidationRule;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstanceModelFactory;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.ObjectPropertyStatement;
import org.openflexo.technologyadapter.owl.model.StatementWithProperty;

@ModelEntity
@ImplementationClass(ObjectPropertyStatementPatternRole.ObjectPropertyStatementPatternRoleImpl.class)
@XMLElement
public interface ObjectPropertyStatementPatternRole extends StatementPatternRole<ObjectPropertyStatement> {

	@PropertyIdentifier(type = String.class)
	public static final String OBJECT_PROPERTY_URI_KEY = "objectPropertyURI";

	@Getter(value = OBJECT_PROPERTY_URI_KEY)
	@XMLAttribute(xmlTag = "objectProperty")
	public String _getObjectPropertyURI();

	@Setter(OBJECT_PROPERTY_URI_KEY)
	public void _setObjectPropertyURI(String objectPropertyURI);

	public IFlexoOntologyStructuralProperty getObjectProperty();

	public void setObjectProperty(IFlexoOntologyStructuralProperty p);

	public static abstract class ObjectPropertyStatementPatternRoleImpl extends StatementPatternRoleImpl<ObjectPropertyStatement> implements
			ObjectPropertyStatementPatternRole {

		static final Logger logger = FlexoLogger.getLogger(ObjectPropertyStatementPatternRole.class.getPackage().toString());

		public ObjectPropertyStatementPatternRoleImpl() {
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
		public IFlexoOntologyStructuralProperty getObjectProperty() {
			return getVirtualModel().getOntologyObjectProperty(_getObjectPropertyURI());
		}

		@Override
		public void setObjectProperty(IFlexoOntologyStructuralProperty p) {
			_setObjectPropertyURI(p != null ? p.getURI() : null);
		}

		@Override
		public ObjectPropertyStatementActorReference makeActorReference(ObjectPropertyStatement object, FlexoConceptInstance epi) {
			VirtualModelInstanceModelFactory factory = epi.getFactory();
			ObjectPropertyStatementActorReference returned = factory.newInstance(ObjectPropertyStatementActorReference.class);
			returned.setPatternRole(this);
			returned.setFlexoConceptInstance(epi);
			returned.setModellingElement(object);
			return returned;
		}

	}

	public static class ObjectPropertyStatementPatternRoleMustDefineAValidProperty extends
			ValidationRule<ObjectPropertyStatementPatternRoleMustDefineAValidProperty, ObjectPropertyStatementPatternRole> {
		public ObjectPropertyStatementPatternRoleMustDefineAValidProperty() {
			super(ObjectPropertyStatementPatternRole.class, "pattern_role_must_define_a_valid_object_property");
		}

		@Override
		public ValidationIssue<ObjectPropertyStatementPatternRoleMustDefineAValidProperty, ObjectPropertyStatementPatternRole> applyValidation(
				ObjectPropertyStatementPatternRole patternRole) {
			if (patternRole.getObjectProperty() == null) {
				return new ValidationError<ObjectPropertyStatementPatternRoleMustDefineAValidProperty, ObjectPropertyStatementPatternRole>(
						this, patternRole, "pattern_role_does_not_define_any_valid_object_property");
			}
			return null;
		}
	}

}
