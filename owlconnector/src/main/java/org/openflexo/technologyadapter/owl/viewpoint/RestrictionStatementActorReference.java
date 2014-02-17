package org.openflexo.technologyadapter.owl.viewpoint;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.view.ActorReference;
import org.openflexo.foundation.view.EditionPatternInstance;
import org.openflexo.technologyadapter.owl.model.OWLRestriction;

public class RestrictionStatementActorReference extends ActorReference<OWLRestriction> {

	public OWLRestriction restriction;
	public String objectURI;
	public String propertyURI;

	public RestrictionStatementActorReference(OWLRestriction o, OWLRestriction aPatternRole, EditionPatternInstance epi) {
		super(epi.getProject());
		setEditionPatternInstance(epi);
		// setPatternRole(aPatternRole);
		restriction = o;
		// subjectURI = o.getSubject().getURI();
		// parentURI = o.getParent().getURI();
	}

	// Constructor used during deserialization
	public RestrictionStatementActorReference(FlexoProject project) {
		super(project);
	}

	@Override
	public OWLRestriction retrieveObject() {
		return null;
	}
}