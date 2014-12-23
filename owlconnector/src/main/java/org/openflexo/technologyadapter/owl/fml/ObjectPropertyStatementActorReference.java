package org.openflexo.technologyadapter.owl.fml;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.rt.ActorReference;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.ObjectPropertyStatement;

/**
 * Implements {@link ActorReference} for {@link ObjectPropertyStatement} as modelling elements.<br>
 * We need to serialize here the three values identifying related triplet: subject, property and value
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(ObjectPropertyStatementActorReference.ObjectPropertyStatementActorReferenceImpl.class)
@XMLElement
public interface ObjectPropertyStatementActorReference extends ActorReference<ObjectPropertyStatement> {

	@PropertyIdentifier(type = String.class)
	public static final String SUBJECT_URI_KEY = "subjectURI";
	@PropertyIdentifier(type = String.class)
	public static final String OBJECT_PROPERTY_URI_KEY = "objectPropertyURI";
	@PropertyIdentifier(type = String.class)
	public static final String OBJECT_URI_KEY = "objectURI";

	@Getter(value = SUBJECT_URI_KEY)
	@XMLAttribute
	public String getSubjectURI();

	@Setter(SUBJECT_URI_KEY)
	public void setSubjectURI(String objectURI);

	@Getter(value = OBJECT_PROPERTY_URI_KEY)
	@XMLAttribute
	public String getObjectPropertyURI();

	@Setter(OBJECT_PROPERTY_URI_KEY)
	public void setObjectPropertyURI(String objectPropertyURI);

	@Getter(value = OBJECT_URI_KEY)
	@XMLAttribute
	public String getObjectURI();

	@Setter(OBJECT_URI_KEY)
	public void setObjectURI(String objectURI);

	public static abstract class ObjectPropertyStatementActorReferenceImpl extends ActorReferenceImpl<ObjectPropertyStatement> implements
			ObjectPropertyStatementActorReference {

		static final Logger logger = FlexoLogger.getLogger(ObjectPropertyStatementActorReferenceImpl.class.getPackage().toString());

		private ObjectPropertyStatement statement;
		private String subjectURI;
		private String objectPropertyURI;
		private String objectURI;

		/**
		 * Default constructor
		 */
		public ObjectPropertyStatementActorReferenceImpl() {
			super();
		}

		/*public ObjectPropertyStatementActorReference(ObjectPropertyStatement o, ObjectPropertyStatementRole aPatternRole,
				FlexoConceptInstance epi) {
			super(epi.getProject());
			setFlexoConceptInstance(epi);
			setPatternRole(aPatternRole);
			statement = o;
			subjectURI = o.getSubject().getURI();
			objectURI = o.getStatementObject().getURI();
			objectPropertyURI = o.getProperty().getURI();
		}

		// Constructor used during deserialization
		public ObjectPropertyStatementActorReference(FlexoProject project) {
			super(project);
		}*/

		@Override
		public void setModellingElement(ObjectPropertyStatement statement) {
			this.statement = statement;
			if (statement != null && getModelSlotInstance() != null) {
				subjectURI = statement.getSubject().getURI();
				objectURI = statement.getStatementObject().getURI();
				objectPropertyURI = statement.getProperty().getURI();
			}
		}

		@Override
		public ObjectPropertyStatement getModellingElement() {
			if (statement == null) {
				OWLOntology ontology = (OWLOntology) getModelSlotInstance().getAccessedResourceData();
				if (ontology != null) {
					OWLConcept<?> subject = ontology.getOntologyObject(subjectURI);
					OWLObjectProperty property = ontology.getObjectProperty(objectPropertyURI);
					OWLConcept<?> object = ontology.getOntologyObject(objectURI);
					if (subject != null && property != null && object != null) {
						// TODO: also handle value here
						statement = subject.getObjectPropertyStatement(property, object);
					}
				} else {
					logger.warning("Could not access to ontology referenced by " + getModelSlotInstance());
				}
			}
			if (statement == null) {
				logger.warning("Could not retrieve statement" + subjectURI + " " + objectPropertyURI + " " + objectURI);
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
		public String getObjectPropertyURI() {
			return objectPropertyURI;
		}

		@Override
		public void setObjectPropertyURI(String objectPropertyURI) {
			this.objectPropertyURI = objectPropertyURI;
		}

		@Override
		public String getObjectURI() {
			return objectURI;
		}

		@Override
		public void setObjectURI(String objectURI) {
			this.objectURI = objectURI;
		}
	}
}