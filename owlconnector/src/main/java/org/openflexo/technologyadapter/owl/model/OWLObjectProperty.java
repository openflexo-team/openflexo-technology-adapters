/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.owl.model;

import java.util.logging.Logger;

import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;

import com.hp.hpl.jena.ontology.OntProperty;

public class OWLObjectProperty extends OWLProperty implements IFlexoOntologyObjectProperty<OWLTechnologyAdapter>,
		Comparable<IFlexoOntologyObjectProperty<OWLTechnologyAdapter>> {

	static final Logger logger = Logger.getLogger(IFlexoOntologyObjectProperty.class.getPackage().getName());

	protected OWLObjectProperty(OntProperty anObjectProperty, OWLOntology ontology, OWLTechnologyAdapter adapter) {
		super(anObjectProperty, ontology, adapter);
	}

	@Override
	public boolean delete() {
		getFlexoOntology().removeObjectProperty(this);
		getOntResource().remove();
		getFlexoOntology().updateConceptsAndProperties();
		super.delete();
		deleteObservers();
		return true;
	}

	@Override
	public OntProperty getOntProperty() {
		return super.getOntProperty();
	}

	@Override
	public int compareTo(IFlexoOntologyObjectProperty<OWLTechnologyAdapter> o) {
		return COMPARATOR.compare(this, o);
	}

	@Override
	public OWLClass getRange() {
		return (OWLClass) super.getRange();
	}

	@Override
	public String getDisplayableDescription() {
		return "<html>Object property <b>" + getName() + "</b><br>" + "<i>" + getURI() + "</i><br>" + "Domain: "
				+ (getDomain() != null ? getDomain().getURI() : "?") + "<br>" + "Range: "
				+ (getRange() != null ? getRange().getURI() : "?") + "<br>" + "</html>";
	}

	public boolean isLiteralRange() {
		return getRange() == getOntology().getOntologyObject(RDFSURIDefinitions.RDFS_LITERAL_URI);
	}

	@Override
	public String getHTMLDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("Object property <b>" + getName() + "</b><br>");
		sb.append("<i>" + getURI() + "</i><br>");
		sb.append("<b>Asserted in:</b> " + getOntology().getURI() + "<br>");
		sb.append("<b>Domain:</b> " + (getDomain() != null ? getDomain().getURI() : "?") + "<br>");
		sb.append("<b>Range:</b> " + (getRange() != null ? getRange().getURI() : "?") + "<br>");
		if (redefinesOriginalDefinition()) {
			sb.append("<b>Redefines:</b> " + getOriginalDefinition() + "<br>");
		}
		sb.append("</html>");
		return sb.toString();
	}

	@Override
	protected void recursivelySearchRangeAndDomains() {
		super.recursivelySearchRangeAndDomains();
		for (OWLProperty aProperty : getSuperProperties()) {
			propertiesTakingMySelfAsRange.addAll(aProperty.getPropertiesTakingMySelfAsRange());
			propertiesTakingMySelfAsDomain.addAll(aProperty.getPropertiesTakingMySelfAsDomain());
		}
		OWLClass OBJECT_PROPERTY_CONCEPT = getOntology().getClass(OWL_OBJECT_PROPERTY_URI);
		// OBJECT_PROPERTY_CONCEPT is generally non null but can be null when reading RDFS for example
		if (OBJECT_PROPERTY_CONCEPT != null) {
			propertiesTakingMySelfAsRange.addAll(OBJECT_PROPERTY_CONCEPT.getPropertiesTakingMySelfAsRange());
			propertiesTakingMySelfAsDomain.addAll(OBJECT_PROPERTY_CONCEPT.getPropertiesTakingMySelfAsDomain());
		}
	}

}
