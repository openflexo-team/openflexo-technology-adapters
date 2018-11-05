/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
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

package org.openflexo.technologyadapter.owl.model;

import java.util.logging.Logger;

import org.apache.jena.ontology.OntProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;

public class OWLDataProperty extends OWLProperty
		implements IFlexoOntologyDataProperty<OWLTechnologyAdapter>, Comparable<IFlexoOntologyDataProperty<OWLTechnologyAdapter>> {

	static final Logger logger = Logger.getLogger(IFlexoOntologyDataProperty.class.getPackage().getName());

	protected OWLDataProperty(OntProperty aDataProperty, OWLOntology ontology, OWLTechnologyAdapter adapter) {
		super(aDataProperty, ontology, adapter);
	}

	@Override
	public boolean delete(Object... context) {
		getFlexoOntology().removeDataProperty(this);
		getOntResource().remove();
		getFlexoOntology().updateConceptsAndProperties();
		super.delete(context);
		deleteObservers();
		return true;
	}

	@Override
	public int compareTo(IFlexoOntologyDataProperty<OWLTechnologyAdapter> o) {
		return COMPARATOR.compare(this, o);
	}

	public OWLDataType getDataType() {
		return getRange();
	}

	@Override
	public OWLDataType getRange() {
		if (getRangeStatement() != null) {
			return getRangeStatement().getDataType();
		}
		return null;
	}

	@Override
	public String getDisplayableDescription() {
		return "<html>Datatype property <b>" + getName() + "</b><br>" + "<i>" + getURI() + "</i><br>" + "Domain: "
				+ (getDomain() != null ? getDomain().getURI() : "?") + "<br>" + "Range: "
				+ (getRange() != null ? getRange().toString() : "?") + "<br>" + "</html>";
	}

	@Override
	public String getHTMLDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("Datatype property <b>" + getName() + "</b><br>");
		sb.append("<i>" + getURI() + "</i><br>");
		sb.append("<b>Asserted in:</b> " + getOntology().getURI() + "<br>");
		sb.append("<b>Domain:</b> " + (getDomain() != null ? getDomain().getURI() : "?") + "<br>");
		sb.append("<b>Datatype:</b> " + (getDataType() != null ? getDataType().toString() : "?") + "<br>");
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
		OWLClass DATA_PROPERTY_CONCEPT = getOntology().getClass(OWL_DATA_PROPERTY_URI);
		// DATA_PROPERTY_CONCEPT is generally non null but can be null when reading RDFS for example
		if (DATA_PROPERTY_CONCEPT != null) {
			propertiesTakingMySelfAsRange.addAll(DATA_PROPERTY_CONCEPT.getPropertiesTakingMySelfAsRange());
			propertiesTakingMySelfAsDomain.addAll(DATA_PROPERTY_CONCEPT.getPropertiesTakingMySelfAsDomain());
		}
	}

}
