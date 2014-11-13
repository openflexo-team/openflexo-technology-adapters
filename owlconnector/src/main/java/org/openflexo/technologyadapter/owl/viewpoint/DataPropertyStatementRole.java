package org.openflexo.technologyadapter.owl.viewpoint;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.DefineValidationRule;
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
import org.openflexo.technologyadapter.owl.OWLModelSlot.OWLModelSlotImpl;
import org.openflexo.technologyadapter.owl.model.DataPropertyStatement;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;
import org.openflexo.technologyadapter.owl.model.StatementWithProperty;

@ModelEntity
@ImplementationClass(DataPropertyStatementRole.DataPropertyStatementRoleImpl.class)
@XMLElement
public interface DataPropertyStatementRole extends StatementRole<DataPropertyStatement> {

	@PropertyIdentifier(type = String.class)
	public static final String DATA_PROPERTY_URI_KEY = "dataPropertyURI";

	@Getter(value = DATA_PROPERTY_URI_KEY)
	@XMLAttribute(xmlTag = "dataProperty")
	public String _getDataPropertyURI();

	@Setter(DATA_PROPERTY_URI_KEY)
	public void _setDataPropertyURI(String dataPropertyURI);

	public OWLDataProperty getDataProperty();

	public void setDataProperty(OWLDataProperty p);

	public static abstract class DataPropertyStatementRoleImpl extends StatementRoleImpl<DataPropertyStatement> implements
			DataPropertyStatementRole {

		static final Logger logger = FlexoLogger.getLogger(DataPropertyStatementRole.class.getPackage().toString());

		public DataPropertyStatementRoleImpl() {
			super();
		}

		@Override
		public Type getType() {
			if (getDataProperty() == null) {
				return DataPropertyStatement.class;
			}
			return StatementWithProperty.getStatementWithProperty(getDataProperty());
		}

		@Override
		public String getPreciseType() {
			if (getDataProperty() != null) {
				return getDataProperty().getName();
			}
			return "";
		}

		private String dataPropertyURI;

		@Override
		public String _getDataPropertyURI() {
			return dataPropertyURI;
		}

		@Override
		public void _setDataPropertyURI(String dataPropertyURI) {
			this.dataPropertyURI = dataPropertyURI;
		}

		@Override
		public OWLDataProperty getDataProperty() {
			if (getVirtualModel() != null) {
				return (OWLDataProperty) getVirtualModel().getOntologyDataProperty(_getDataPropertyURI());
			}
			return null;
		}

		@Override
		public void setDataProperty(OWLDataProperty p) {
			_setDataPropertyURI(p != null ? p.getURI() : null);
		}

		@Override
		public DataPropertyStatementActorReference makeActorReference(DataPropertyStatement object, FlexoConceptInstance epi) {
			org.openflexo.model.factory.ModelFactory factory = OWLModelSlotImpl.getModelFactory();
			DataPropertyStatementActorReference returned = factory.newInstance(DataPropertyStatementActorReference.class);
			returned.setFlexoRole(this);
			returned.setFlexoConceptInstance(epi);
			returned.setModellingElement(object);
			return returned;
		}

	}

	@DefineValidationRule
	public static class DataPropertyStatementPatternRoleMustDefineAValidProperty extends
			ValidationRule<DataPropertyStatementPatternRoleMustDefineAValidProperty, DataPropertyStatementRole> {
		public DataPropertyStatementPatternRoleMustDefineAValidProperty() {
			super(DataPropertyStatementRole.class, "pattern_role_must_define_a_valid_data_property");
		}

		@Override
		public ValidationIssue<DataPropertyStatementPatternRoleMustDefineAValidProperty, DataPropertyStatementRole> applyValidation(
				DataPropertyStatementRole patternRole) {
			if (patternRole.getDataProperty() == null) {
				return new ValidationError<DataPropertyStatementPatternRoleMustDefineAValidProperty, DataPropertyStatementRole>(this,
						patternRole, "pattern_role_does_not_define_any_valid_data_property");
			}
			return null;
		}
	}

}
