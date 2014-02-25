package org.openflexo.technologyadapter.owl.viewpoint;

import java.util.logging.Logger;

import org.openflexo.foundation.view.ActorReference;
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