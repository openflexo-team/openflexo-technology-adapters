package org.openflexo.technologyadapter.owl.viewpoint;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
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
import org.openflexo.technologyadapter.owl.model.DataPropertyStatement;
import org.openflexo.technologyadapter.owl.model.StatementWithProperty;

@ModelEntity
@ImplementationClass(DataPropertyStatementPatternRole.DataPropertyStatementPatternRoleImpl.class)
@XMLElement
public interface DataPropertyStatementPatternRole extends StatementPatternRole<DataPropertyStatement> {

	@PropertyIdentifier(type = String.class)
	public static final String DATA_PROPERTY_URI_KEY = "dataPropertyURI";

	@Getter(value = DATA_PROPERTY_URI_KEY)
	@XMLAttribute(xmlTag = "dataProperty")
	public String _getDataPropertyURI();

	@Setter(DATA_PROPERTY_URI_KEY)
	public void _setDataPropertyURI(String dataPropertyURI);

	public IFlexoOntologyDataProperty getDataProperty();

	public void setDataProperty(IFlexoOntologyStructuralProperty p);

	public static abstract class DataPropertyStatementPatternRoleImpl extends StatementPatternRoleImpl<DataPropertyStatement> implements
			DataPropertyStatementPatternRole {

		static final Logger logger = FlexoLogger.getLogger(DataPropertyStatementPatternRole.class.getPackage().toString());

		public DataPropertyStatementPatternRoleImpl() {
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
		public IFlexoOntologyDataProperty getDataProperty() {
			return getVirtualModel().getOntologyDataProperty(_getDataPropertyURI());
		}

		@Override
		public void setDataProperty(IFlexoOntologyStructuralProperty p) {
			_setDataPropertyURI(p != null ? p.getURI() : null);
		}

		@Override
		public DataPropertyStatementActorReference makeActorReference(DataPropertyStatement object, FlexoConceptInstance epi) {
			VirtualModelInstanceModelFactory factory = epi.getFactory();
			DataPropertyStatementActorReference returned = factory.newInstance(DataPropertyStatementActorReference.class);
			returned.setPatternRole(this);
			returned.setFlexoConceptInstance(epi);
			returned.setModellingElement(object);
			return returned;
		}

	}

	public static class DataPropertyStatementPatternRoleMustDefineAValidProperty extends
			ValidationRule<DataPropertyStatementPatternRoleMustDefineAValidProperty, DataPropertyStatementPatternRole> {
		public DataPropertyStatementPatternRoleMustDefineAValidProperty() {
			super(DataPropertyStatementPatternRole.class, "pattern_role_must_define_a_valid_data_property");
		}

		@Override
		public ValidationIssue<DataPropertyStatementPatternRoleMustDefineAValidProperty, DataPropertyStatementPatternRole> applyValidation(
				DataPropertyStatementPatternRole patternRole) {
			if (patternRole.getDataProperty() == null) {
				return new ValidationError<DataPropertyStatementPatternRoleMustDefineAValidProperty, DataPropertyStatementPatternRole>(
						this, patternRole, "pattern_role_does_not_define_any_valid_data_property");
			}
			return null;
		}
	}

}
