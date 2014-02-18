package org.openflexo.technologyadapter.owl.viewpoint;

import java.util.logging.Level;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.view.ActorReference;
import org.openflexo.foundation.view.EditionPatternInstance;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLObject;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.ObjectPropertyStatement;
import org.openflexo.technologyadapter.owl.viewpoint.ObjectPropertyStatementPatternRole.ObjectPropertyStatementPatternRoleImpl;

public class ObjectPropertyStatementActorReference extends ActorReference<ObjectPropertyStatement> {

	public ObjectPropertyStatement statement;
	public String subjectURI;
	public String objectPropertyURI;
	public String objectURI;

	public ObjectPropertyStatementActorReference(ObjectPropertyStatement o, ObjectPropertyStatementPatternRole aPatternRole,
			EditionPatternInstance epi) {
		super(epi.getProject());
		setEditionPatternInstance(epi);
		setPatternRole(aPatternRole);
		statement = o;
		subjectURI = o.getSubject().getURI();
		objectURI = o.getStatementObject().getURI();
		objectPropertyURI = o.getProperty().getURI();
	}

	// Constructor used during deserialization
	public ObjectPropertyStatementActorReference(FlexoProject project) {
		super(project);
	}

	@Override
	public ObjectPropertyStatement getModellingElement() {
		if (statement == null) {
			OWLObject subject = (OWLObject) getProject().getOntologyObject(subjectURI);
			if (subject == null) {
				if (ObjectPropertyStatementPatternRoleImpl.logger.isLoggable(Level.WARNING)) {
					ObjectPropertyStatementPatternRoleImpl.logger.warning("Could not find subject with URI " + subjectURI);
				}
				return null;
			}
			if (subject instanceof OWLConcept == false) {
				if (ObjectPropertyStatementPatternRoleImpl.logger.isLoggable(Level.WARNING)) {
					ObjectPropertyStatementPatternRoleImpl.logger
							.warning("Statements aren't supported by non-owl ontologies, subject's URI: " + subjectURI);
				}
				return null;
			}

			OWLConcept<?> object = (OWLConcept<?>) getProject().getOntologyObject(objectURI);
			if (object == null) {
				if (ObjectPropertyStatementPatternRoleImpl.logger.isLoggable(Level.WARNING)) {
					ObjectPropertyStatementPatternRoleImpl.logger.warning("Could not find object with URI " + objectURI);
				}
				return null;
			}

			OWLObjectProperty property = (OWLObjectProperty) getProject().getObject(objectPropertyURI);
			if (property != null) {
				// FIXED HUGE ISSUE HERE, with incorrect deserialization of statements
				statement = ((OWLConcept<?>) subject).getObjectPropertyStatement(property, object);
				// logger.info("Found statement: "+statement);
			}
		}
		if (statement == null) {
			ObjectPropertyStatementPatternRoleImpl.logger.warning("Could not retrieve object " + objectURI);
		}
		return statement;
	}
}