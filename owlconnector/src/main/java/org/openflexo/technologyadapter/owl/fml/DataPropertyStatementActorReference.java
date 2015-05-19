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

import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.DataPropertyStatement;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;
import org.openflexo.technologyadapter.owl.model.OWLOntology;

/**
 * Implements {@link ActorReference} for {@link DataPropertyStatement} as modelling elements.<br>
 * We need to serialize here the three values identifying related triplet: subject, property and value
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(DataPropertyStatementActorReference.DataPropertyStatementActorReferenceImpl.class)
@XMLElement
@FML("DataPropertyStatementActorReference")
public interface DataPropertyStatementActorReference extends ActorReference<DataPropertyStatement> {

	@PropertyIdentifier(type = String.class)
	public static final String SUBJECT_URI_KEY = "subjectURI";
	@PropertyIdentifier(type = String.class)
	public static final String DATA_PROPERTY_URI_KEY = "dataPropertyURI";
	@PropertyIdentifier(type = String.class)
	public static final String VALUE_KEY = "value";

	@Getter(value = SUBJECT_URI_KEY)
	@XMLAttribute
	public String getSubjectURI();

	@Setter(SUBJECT_URI_KEY)
	public void setSubjectURI(String objectURI);

	@Getter(value = DATA_PROPERTY_URI_KEY)
	@XMLAttribute
	public String getDataPropertyURI();

	@Setter(DATA_PROPERTY_URI_KEY)
	public void setDataPropertyURI(String dataPropertyURI);

	@Getter(value = VALUE_KEY)
	@XMLAttribute
	public String getValue();

	@Setter(VALUE_KEY)
	public void setValue(String value);

	public static abstract class DataPropertyStatementActorReferenceImpl extends ActorReferenceImpl<DataPropertyStatement> implements
			DataPropertyStatementActorReference {

		static final Logger logger = FlexoLogger.getLogger(DataPropertyStatementActorReferenceImpl.class.getPackage().toString());

		private DataPropertyStatement statement;
		private String subjectURI;
		private String dataPropertyURI;
		private String value;

		/**
		 * Default constructor
		 */
		public DataPropertyStatementActorReferenceImpl() {
			super();
		}

		/*public DataPropertyStatementActorReference(DataPropertyStatement o, DataPropertyStatementRole aPatternRole,
				FlexoConceptInstance epi) {
			super(epi.getProject());
			setFlexoConceptInstance(epi);
			setPatternRole(aPatternRole);
			statement = o;
			subjectURI = o.getSubject().getURI();
			value = o.getLiteral().toString();
			dataPropertyURI = o.getProperty().getURI();
		}

		// Constructor used during deserialization
		public DataPropertyStatementActorReference(FlexoProject project) {
			super(project);
		}*/

		@Override
		public void setModellingElement(DataPropertyStatement statement) {
			this.statement = statement;
			if (statement != null && getModelSlotInstance() != null) {
				subjectURI = statement.getSubject().getURI();
				value = statement.getLiteral().toString();
				dataPropertyURI = statement.getProperty().getURI();
			}
		}

		@Override
		public DataPropertyStatement getModellingElement() {
			if (statement == null) {
				OWLOntology ontology = (OWLOntology) getModelSlotInstance().getAccessedResourceData();
				if (ontology != null) {
					OWLConcept<?> subject = ontology.getOntologyObject(subjectURI);
					OWLDataProperty property = ontology.getDataProperty(dataPropertyURI);
					if (subject != null && property != null) {
						// TODO: also handle value here
						statement = subject.getDataPropertyStatement(property);
					}
				} else {
					logger.warning("Could not access to ontology referenced by " + getModelSlotInstance());
				}
			}
			if (statement == null) {
				logger.warning("Could not retrieve statement" + subjectURI + " " + dataPropertyURI + " " + value);
			}
			return statement;
		}

		@Override
		public String getSubjectURI() {
			return subjectURI;
		}

		@Override
		public void setSubjectURI(String subjectURI) {
			this.subjectURI = subjectURI;
		}

		@Override
		public String getDataPropertyURI() {
			return dataPropertyURI;
		}

		@Override
		public void setDataPropertyURI(String dataPropertyURI) {
			this.dataPropertyURI = dataPropertyURI;
		}

		@Override
		public String getValue() {
			return value;
		}

		@Override
		public void setValue(String value) {
			this.value = value;
		}
	}
}
