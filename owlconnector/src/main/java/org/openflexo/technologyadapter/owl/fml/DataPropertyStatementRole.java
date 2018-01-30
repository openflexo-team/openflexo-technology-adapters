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
import org.openflexo.technologyadapter.owl.nature.OWLOntologyVirtualModelNature;

@ModelEntity
@ImplementationClass(DataPropertyStatementRole.DataPropertyStatementRoleImpl.class)
@XMLElement
@FML("DataPropertyStatementRole")
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

	public static abstract class DataPropertyStatementRoleImpl extends StatementRoleImpl<DataPropertyStatement>
			implements DataPropertyStatementRole {

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
		public String getTypeDescription() {
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
			if (OWLOntologyVirtualModelNature.INSTANCE.hasNature(getOwningVirtualModel())) {
				return OWLOntologyVirtualModelNature.getOWLDataProperty(_getDataPropertyURI(), getOwningVirtualModel());
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
	public static class DataPropertyStatementPatternRoleMustDefineAValidProperty
			extends ValidationRule<DataPropertyStatementPatternRoleMustDefineAValidProperty, DataPropertyStatementRole> {
		public DataPropertyStatementPatternRoleMustDefineAValidProperty() {
			super(DataPropertyStatementRole.class, "pattern_role_must_define_a_valid_data_property");
		}

		@Override
		public ValidationIssue<DataPropertyStatementPatternRoleMustDefineAValidProperty, DataPropertyStatementRole> applyValidation(
				DataPropertyStatementRole patternRole) {
			if (patternRole.getDataProperty() == null) {
				return new ValidationError<>(this,
						patternRole, "pattern_role_does_not_define_any_valid_data_property");
			}
			return null;
		}
	}

}
