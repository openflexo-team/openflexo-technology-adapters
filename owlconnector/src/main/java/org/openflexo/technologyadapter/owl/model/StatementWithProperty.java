package org.openflexo.technologyadapter.owl.model;

import java.lang.reflect.Type;

import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.foundation.viewpoint.TechnologySpecificCustomType;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;

public class StatementWithProperty implements TechnologySpecificCustomType<OWLTechnologyAdapter> {

	public static StatementWithProperty getStatementWithProperty(OWLProperty aProperty) {
		if (aProperty == null) {
			return null;
		}
		return aProperty.getTechnologyAdapter().getTechnologyContextManager().getStatementWithProperty(aProperty);
	}

	private final OWLProperty property;

	public StatementWithProperty(OWLProperty aProperty) {
		this.property = aProperty;
	}

	public OWLProperty getProperty() {
		return property;
	}

	@Override
	public Class<?> getBaseClass() {
		if (property != null) {
			return property.getClass();
		}
		return IFlexoOntologyStructuralProperty.class;
	}

	@Override
	public boolean isTypeAssignableFrom(Type aType, boolean permissive) {
		// System.out.println("isTypeAssignableFrom " + aType + " (i am a " + this + ")");
		if (aType instanceof StatementWithProperty) {
			return property.isSuperConceptOf(((StatementWithProperty) aType).getProperty());
		}
		return false;
	}

	@Override
	public String simpleRepresentation() {
		return "Statement:" + property.getName();
	}

	@Override
	public String fullQualifiedRepresentation() {
		return simpleRepresentation();
	}

	@Override
	public String toString() {
		return simpleRepresentation();
	}

	@Override
	public OWLTechnologyAdapter getTechnologyAdapter() {
		if (getProperty() != null) {
			return getProperty().getTechnologyAdapter();
		}
		return null;
	}

}
