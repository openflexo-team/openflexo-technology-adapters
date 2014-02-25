package org.openflexo.technologyadapter.owl.viewpoint;

import java.util.logging.Logger;

import org.openflexo.foundation.view.ActorReference;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.SubClassStatement;

public interface SubClassStatementActorReference extends ActorReference<SubClassStatement> {

	@PropertyIdentifier(type = String.class)
	public static final String SUBJECT_URI_KEY = "subjectURI";
	@PropertyIdentifier(type = String.class)
	public static final String PARENT_URI_KEY = "parentURI";

	@Getter(value = SUBJECT_URI_KEY)
	@XMLAttribute
	public String getSubjectURI();

	@Setter(SUBJECT_URI_KEY)
	public void setSubjectURI(String objectURI);

	@Getter(value = PARENT_URI_KEY)
	@XMLAttribute
	public String getParentURI();

	@Setter(PARENT_URI_KEY)
	public void setParentURI(String parentURI);

	public abstract static class SubClassStatementActorReferenceImpl extends ActorReferenceImpl<SubClassStatement> implements
			SubClassStatementActorReference {

		static final Logger logger = FlexoLogger.getLogger(SubClassStatementActorReferenceImpl.class.getPackage().toString());

		private SubClassStatement statement;
		private String subjectURI;
		private String parentURI;

		/*public SubClassStatementActorReference(SubClassStatement o, SubClassStatementRole aPatternRole, FlexoConceptInstance epi) {
			super(epi.getProject());
			setFlexoConceptInstance(epi);
			setPatternRole(aPatternRole);
			statement = o;
			subjectURI = o.getSubject().getURI();
			parentURI = o.getParent().getURI();
		}*/

		// Constructor used during deserialization
		/*public SubClassStatementActorReference(FlexoProject project) {
			super(project);
		}*/

		@Override
		public void setModellingElement(SubClassStatement statement) {
			this.statement = statement;
			if (statement != null && getModelSlotInstance() != null) {
				subjectURI = statement.getSubject().getURI();
				parentURI = statement.getParent().getURI();
			}
		}

		@Override
		public SubClassStatement getModellingElement() {
			if (statement == null) {
				OWLOntology ontology = (OWLOntology) getModelSlotInstance().getAccessedResourceData();
				if (ontology != null) {
					OWLConcept<?> subject = ontology.getOntologyObject(subjectURI);
					OWLConcept<?> parent = ontology.getOntologyObject(parentURI);
					if (subject != null && parent != null) {
						// TODO: also handle value here
						statement = ((OWLConcept<?>) subject).getSubClassStatement(parent);
					}
				} else {
					logger.warning("Could not access to ontology referenced by " + getModelSlotInstance());
				}
			}
			if (statement == null) {
				logger.warning("Could not retrieve sub-class statement" + subjectURI + " " + parentURI);
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
		public String getParentURI() {
			return parentURI;
		}

		@Override
		public void setParentURI(String parentURI) {
			this.parentURI = parentURI;
		}
	}
}