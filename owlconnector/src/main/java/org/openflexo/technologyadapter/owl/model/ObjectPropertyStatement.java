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

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.openflexo.foundation.ontology.IFlexoOntologyObjectPropertyValue;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;

public class ObjectPropertyStatement extends PropertyStatement implements IFlexoOntologyObjectPropertyValue<OWLTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(ObjectPropertyStatement.class.getPackage().getName());

	private final OWLObjectProperty property;
	private OWLConcept<?> statementObject;
	private Literal literal;

	public ObjectPropertyStatement(OWLConcept<?> subject, Statement s, OWLTechnologyAdapter adapter) {
		super(subject, s, adapter);
		property = getOntology().getObjectProperty(s.getPredicate().getURI());
		if (s.getObject() instanceof Resource) {
			statementObject = getOntology().retrieveOntologyObject((Resource) s.getObject());
			/*if (statementObject instanceof IFlexoOntology) {
				System.out.println("OK, i have my ontology with resource "+s.getObject().getClass().getName());
				System.out.println("resource: "+s.getObject());
				System.out.println("uri: "+((Resource)s.getObject()).getURI());
			}*/
			if (statementObject == null) {
				logger.warning("Ontology: " + getOntology() + " cannot retrieve " + s.getObject() + " for statement " + s);
			}
		}
		else if (s.getObject() instanceof Literal) {
			literal = (Literal) s.getObject();
		}
		else {
			logger.warning("ObjectPropertyStatement: object is not a Resource nor a Litteral !");
		}
	}

	@Override
	public OWLObjectProperty getPredicate() {
		return (OWLObjectProperty) super.getPredicate();
	}

	@Override
	public Literal getLiteral() {
		return literal;
	}

	@Override
	public OWLObjectProperty getProperty() {
		return property;
	}

	public OWLConcept<?> getStatementObject() {
		return statementObject;
	}

	/**
	 * Creates a new Statement equals to this one with a new Object value
	 * 
	 * @param anObject
	 */
	public final void setStatementObject(OWLConcept<?> anObject) {
		// Take care to this point: this object will disappear and be replaced by a new one
		// during updateOntologyStatements() !!!!!

		// logger.info("Change object from="+getStatementObject().getURI()+" to="+anObject.getURI());
		getSubject().removePropertyStatement(this);
		getSubject().getOntResource().addProperty(getProperty().getOntProperty(), anObject.getResource());
		getSubject().updateOntologyStatements();
		statementObject = anObject;
	}

	@Override
	public String toString() {
		if (hasLitteralValue()) {
			return (isAnnotationProperty() ? "(A) " : "") + getSubject().getName() + " " + getProperty().getName() + "=\"" + getLiteral()
					+ "\" language=" + getLanguage();
		}
		else {
			return (isAnnotationProperty() ? "(A) " : "") + getSubject().getName() + " " + getProperty().getName() + " "
					+ (getStatementObject() != null ? getStatementObject().getName() : getStatementObject());
		}
	}

	@Override
	public boolean isAnnotationProperty() {
		return getProperty().isAnnotationProperty();
	}

	@Override
	public OWLObjectProperty getObjectProperty() {
		return getProperty();
	}

	@Override
	public List<? extends OWLConcept<?>> getValues() {
		return Collections.singletonList(getStatementObject());
	}

}
