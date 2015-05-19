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

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.ontology.IFlexoOntologyDataPropertyValue;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Statement;

public class DataPropertyStatement extends PropertyStatement implements IFlexoOntologyDataPropertyValue<OWLTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(DataPropertyStatement.class.getPackage().getName());

	private final OWLDataProperty property;
	private Literal literal;

	public DataPropertyStatement(OWLConcept<?> subject, Statement s, OWLTechnologyAdapter adapter) {
		super(subject, s, adapter);
		property = getOntology().getDataProperty(s.getPredicate().getURI());
		if (s.getObject() instanceof Literal) {
			literal = (Literal) s.getObject();
		} else {
			logger.warning("DataPropertyStatement: object is not a Literal !");
		}
	}

	@Override
	public OWLDataProperty getProperty() {
		return property;
	}

	public OWLDataType getDataType() {
		if (getProperty() != null) {
			return property.getDataType();
		}
		return null;
	}

	@Override
	public Literal getLiteral() {
		return literal;
	}

	@Override
	public String toString() {
		return (isAnnotationProperty() ? "(A) " : "") + getSubject().getName() + " " + getProperty().getName() + "=\"" + getLiteral()
				+ "\" language=" + getLanguage();
	}

	@Override
	public boolean isAnnotationProperty() {
		return getProperty().isAnnotationProperty();
	}

	@Override
	public OWLDataProperty getPredicate() {
		return (OWLDataProperty) super.getPredicate();
	}

	public Object getValue() {
		if (getDataType() != null && getLiteral() != null) {
			return getDataType().valueFromLiteral(getLiteral());
		}
		return null;
	}

	/**
	 * Creates a new Statement equals to this one with a new value
	 * 
	 * @param anObject
	 */
	public final void setValue(Object aValue) {
		// Take care to this point: this object will disappear and be replaced by a new one
		// during updateOntologyStatements() !!!!!

		// logger.info("Change object from="+getStatementObject().getURI()+" to="+anObject.getURI());
		getSubject().removePropertyStatement(this);
		getSubject().getOntResource().addLiteral(getProperty().getOntProperty(), aValue);
		getSubject().updateOntologyStatements();
	}

	@Override
	public OWLDataProperty getDataProperty() {
		return getProperty();
	}

	@Override
	public List<Object> getValues() {
		return Collections.singletonList(getValue());
	}
}
