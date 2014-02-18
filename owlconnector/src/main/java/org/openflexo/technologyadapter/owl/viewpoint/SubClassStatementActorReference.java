package org.openflexo.technologyadapter.owl.viewpoint;

import java.util.logging.Level;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.view.ActorReference;
import org.openflexo.foundation.view.EditionPatternInstance;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLObject;
import org.openflexo.technologyadapter.owl.model.SubClassStatement;

public class SubClassStatementActorReference extends ActorReference<SubClassStatement> {

	public SubClassStatement statement;
	public String subjectURI;
	public String parentURI;

	public SubClassStatementActorReference(SubClassStatement o, SubClassStatementPatternRole aPatternRole,
			EditionPatternInstance epi) {
		super(epi.getProject());
		setEditionPatternInstance(epi);
		setPatternRole(aPatternRole);
		statement = o;
		subjectURI = o.getSubject().getURI();
		parentURI = o.getParent().getURI();
	}

	// Constructor used during deserialization
	public SubClassStatementActorReference(FlexoProject project) {
		super(project);
	}

	@Override
	public SubClassStatement getModellingElement() {
		if (statement == null) {
			OWLObject subject = (OWLObject) getProject().getOntologyObject(subjectURI);
			if (subject instanceof OWLConcept == false) {
				if (SubClassStatementPatternRole.SubClassStatementPatternRoleImpl.logger.isLoggable(Level.WARNING)) {
					SubClassStatementPatternRole.SubClassStatementPatternRoleImpl.logger.warning("Statements aren't supported by non-owl ontologies, subject's URI: " + subjectURI);
				}
				return null;
			}
			OWLConcept<?> parent = (OWLConcept<?>) getProject().getOntologyObject(parentURI);
			if (subject != null && parent != null) {
				statement = ((OWLConcept<?>) subject).getSubClassStatement(parent);
			}
			SubClassStatementPatternRole.SubClassStatementPatternRoleImpl.logger.info("Found statement: " + statement);
		}
		if (statement == null) {
			SubClassStatementPatternRole.SubClassStatementPatternRoleImpl.logger.warning("Could not retrieve object " + parentURI);
		}
		return statement;
	}
}