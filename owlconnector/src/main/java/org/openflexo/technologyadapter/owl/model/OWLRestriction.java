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

import org.openflexo.foundation.ontology.IFlexoOntologyFeatureAssociation;
import org.openflexo.foundation.ontology.IFlexoOntologyObject;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Restriction;

public abstract class OWLRestriction extends OWLClass implements IFlexoOntologyFeatureAssociation<OWLTechnologyAdapter> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(OWLRestriction.class.getPackage().getName());

	static final String ON_CLASS = "onClass";
	static final String ON_DATA_RANGE = "onDataRange";
	static final String QUALIFIED_CARDINALITY = "qualifiedCardinality";
	static final String MIN_QUALIFIED_CARDINALITY = "minQualifiedCardinality";
	static final String MAX_QUALIFIED_CARDINALITY = "maxQualifiedCardinality";

	public static enum RestrictionType {
		Some, // SomValuesFromRestriction
		Only, // AllValuesFromRestriction
		HasValue, // HasValueRestriction
		Exact, // CardinalityRestriction
		Min, // MinCardinalityRestriction
		Max // MaxCardinalityRestriction
	}

	private final Restriction restriction;
	private OWLProperty property;
	private OWLClass domain;

	protected OWLRestriction(Restriction aRestriction, OWLOntology ontology, OWLTechnologyAdapter adapter) {
		super(aRestriction, ontology, adapter);
		this.restriction = aRestriction;
	}

	@Override
	protected void init() {
		super.init();
		retrieveRestrictionInformations();
	}

	@Override
	protected void update(OntClass anOntClass) {
		super.update(anOntClass);
		retrieveRestrictionInformations();
	}

	private final boolean addedToReferencingRestriction = false;

	protected void retrieveRestrictionInformations() {
		property = getOntology().retrieveOntologyProperty(restriction.getOnProperty());
		if (!addedToReferencingRestriction) {
			property.addToReferencingRestriction(this);
		}
	}

	public OWLProperty getProperty() {
		return property;
	}

	@Override
	public abstract Restriction getOntResource();

	@Override
	public abstract String getDisplayableDescription();

	@Override
	public String getName() {
		return getDisplayableDescription();
	}

	public abstract OWLConcept<?> getObject();

	public abstract OWLDataType getDataRange();

	@Override
	public boolean isNamedClass() {
		return false;
	}

	@Override
	public OWLClass getDomain() {
		return domain;
	}

	public void setDomain(OWLClass domain) {
		this.domain = domain;
	}

	@Override
	public OWLProperty getFeature() {
		return getProperty();
	}

	@Override
	public IFlexoOntologyObject<OWLTechnologyAdapter> getRange() {
		if (getFeature() instanceof IFlexoOntologyStructuralProperty) {
			return getFeature().getRange();
		}
		return null;
	}

}
