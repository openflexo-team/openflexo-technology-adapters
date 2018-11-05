/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
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

import org.apache.jena.ontology.MinCardinalityRestriction;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.Restriction;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;

public class MinCardinalityRestrictionClass extends OWLRestriction {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(MinCardinalityRestrictionClass.class.getPackage().getName());

	private final Restriction restriction;
	private OWLClass object;
	private int minCardinality = -1;
	private OWLDataType dataRange;

	@Deprecated
	protected MinCardinalityRestrictionClass(Restriction aRestriction, OWLOntology ontology, OWLTechnologyAdapter adapter) {
		super(aRestriction, ontology, adapter);
		this.restriction = aRestriction;
		retrieveRestrictionInformations();
	}

	protected MinCardinalityRestrictionClass(MinCardinalityRestriction aRestriction, OWLOntology ontology, OWLTechnologyAdapter adapter) {
		super(aRestriction, ontology, adapter);
		this.restriction = aRestriction;
		retrieveRestrictionInformations();
	}

	@Override
	protected void retrieveRestrictionInformations() {
		super.retrieveRestrictionInformations();

		if (restriction instanceof MinCardinalityRestriction) {
			minCardinality = ((MinCardinalityRestriction) restriction).getMinCardinality();
		}

		String OWL = getFlexoOntology().getOntModel().getNsPrefixURI("owl");
		Property ON_CLASS_PROPERTY = ResourceFactory.createProperty(OWL + ON_CLASS);
		Property ON_DATA_RANGE_PROPERTY = ResourceFactory.createProperty(OWL + ON_DATA_RANGE);
		Property MIN_QUALIFIED_CARDINALITY_PROPERTY = ResourceFactory.createProperty(OWL + MIN_QUALIFIED_CARDINALITY);

		Statement onClassStmt = restriction.getProperty(ON_CLASS_PROPERTY);
		Statement onDataRangeStmt = restriction.getProperty(ON_DATA_RANGE_PROPERTY);
		Statement cardinalityStmt = restriction.getProperty(MIN_QUALIFIED_CARDINALITY_PROPERTY);

		if (onClassStmt != null) {
			RDFNode onClassStmtValue = onClassStmt.getObject();
			if (onClassStmtValue != null && onClassStmtValue.canAs(OntClass.class)) {
				object = getOntology().retrieveOntologyClass(onClassStmtValue.as(OntClass.class));
			}
		}

		if (onDataRangeStmt != null) {
			RDFNode onDataRangeStmtValue = onDataRangeStmt.getObject();
			dataRange = getTechnologyAdapter().getOntologyLibrary().getDataType(((Resource) onDataRangeStmtValue).getURI());
		}

		if (cardinalityStmt != null) {
			RDFNode cardinalityStmtValue = cardinalityStmt.getObject();
			if (cardinalityStmtValue.isLiteral() && cardinalityStmtValue.canAs(Literal.class)) {
				Literal literal = cardinalityStmtValue.as(Literal.class);
				minCardinality = literal.getInt();
			}
		}

	}

	@Override
	public Restriction getOntResource() {
		return restriction;
	}

	@Override
	public String getDisplayableDescription() {
		return (getProperty() != null ? getProperty().getName() : "null") + " min " + minCardinality + " "
				+ (getObject() != null ? getObject().getName() : getDataRange());
	}

	@Override
	public OWLClass getObject() {
		return object;
	}

	public int getMinCardinality() {
		return minCardinality;
	}

	@Override
	public OWLDataType getDataRange() {
		return dataRange;
	}

	@Override
	public Integer getLowerBound() {
		return getMinCardinality();
	}

	@Override
	public Integer getUpperBound() {
		return null;
	}

}
