package org.openflexo.technologyadapter.owl.viewpoint;

import java.util.logging.Level;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.ontology.IFlexoOntologyObject;
import org.openflexo.foundation.view.ActorReference;
import org.openflexo.foundation.view.EditionPatternInstance;
import org.openflexo.technologyadapter.owl.model.DataPropertyStatement;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;
import org.openflexo.technologyadapter.owl.viewpoint.DataPropertyStatementPatternRole.DataPropertyStatementPatternRoleImpl;

public class DataPropertyStatementActorReference extends ActorReference<DataPropertyStatement> {

	public DataPropertyStatement statement;
	public String subjectURI;
	public String dataPropertyURI;
	public String value;

	public DataPropertyStatementActorReference(DataPropertyStatement o, DataPropertyStatementPatternRole aPatternRole,
			EditionPatternInstance epi) {
		super(epi.getProject());
		setEditionPatternInstance(epi);
		setPatternRole(aPatternRole);
		statement = o;
		subjectURI = o.getSubject().getURI();
		value = o.getLiteral().toString();
		dataPropertyURI = o.getProperty().getURI();
	}

	// Constructor used during deserialization
	public DataPropertyStatementActorReference(FlexoProject project) {
		super(project);
	}

	@Override
	public DataPropertyStatement retrieveObject() {
		if (statement == null) {
			IFlexoOntologyObject subject = getProject().getOntologyObject(subjectURI);
			if (subject instanceof OWLConcept == false) {
				if (DataPropertyStatementPatternRoleImpl.logger.isLoggable(Level.WARNING)) {
					DataPropertyStatementPatternRoleImpl.logger
							.warning("Statements aren't supported by non-owl ontologies, subject's URI: " + subjectURI);
				}
				return null;
			}
			OWLDataProperty property = (OWLDataProperty) getProject().getObject(dataPropertyURI);
			if (property != null) {
				statement = ((OWLConcept<?>) subject).getDataPropertyStatement(property);
				// logger.info("Found statement: "+statement);
			}
		}
		if (statement == null) {
			DataPropertyStatementPatternRoleImpl.logger.warning("Could not retrieve object " + value);
		}
		return statement;
	}
}