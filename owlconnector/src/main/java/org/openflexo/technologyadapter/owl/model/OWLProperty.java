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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.jena.ontology.ConversionException;
import org.apache.jena.ontology.OntProperty;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyFeatureAssociation;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;

public abstract class OWLProperty extends OWLConcept<OntProperty> implements IFlexoOntologyStructuralProperty<OWLTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(IFlexoOntologyStructuralProperty.class.getPackage().getName());

	private OntProperty ontProperty;

	private DomainStatement domainStatement;
	private RangeStatement rangeStatement;
	private final List<DomainStatement> domainStatementList;
	private final List<RangeStatement> rangeStatementList;
	private List<OWLConcept<?>> domainList;
	private List<OWLConcept<?>> rangeList;

	private boolean superDomainStatementWereAppened = false;
	private boolean superRangeStatementWereAppened = false;

	private final Vector<OWLProperty> superProperties;

	private final List<OWLRestriction> referencingRestrictions;

	protected OWLProperty(OntProperty anOntProperty, OWLOntology ontology, OWLTechnologyAdapter adapter) {
		super(anOntProperty, ontology, adapter);
		ontProperty = anOntProperty;
		superProperties = new Vector<OWLProperty>();
		domainStatementList = new ArrayList<DomainStatement>();
		rangeStatementList = new ArrayList<RangeStatement>();
		domainList = null;
		rangeList = null;
		referencingRestrictions = new ArrayList<OWLRestriction>();
	}

	/**
	 * Init this IFlexoOntologyStructuralProperty, given base OntProperty
	 */
	protected void init() {
		updateOntologyStatements(ontProperty);
		updateSuperProperties(ontProperty);
	}

	/**
	 * Update this IFlexoOntologyStructuralProperty, given base OntProperty
	 */
	@Override
	protected void update() {
		updateOntologyStatements(ontProperty);
		updateSuperProperties(ontProperty);
	}

	/**
	 * Update this IFlexoOntologyStructuralProperty given a new OntProperty which is assumed to extends base OntProperty
	 * 
	 * @param anOntProperty
	 */
	protected void update(OntProperty anOntProperty) {
		updateOntologyStatements(anOntProperty);
		updateSuperProperties(anOntProperty);
	}

	@Override
	public void setName(String aName) {
		ontProperty = renameURI(aName, ontProperty, OntProperty.class);
	}

	@Override
	protected void _setOntResource(OntProperty r) {
		ontProperty = r;
	}

	public static final Comparator<IFlexoOntologyStructuralProperty<OWLTechnologyAdapter>> COMPARATOR = new Comparator<IFlexoOntologyStructuralProperty<OWLTechnologyAdapter>>() {
		@Override
		public int compare(IFlexoOntologyStructuralProperty<OWLTechnologyAdapter> o1,
				IFlexoOntologyStructuralProperty<OWLTechnologyAdapter> o2) {
			return Collator.getInstance().compare(o1.getName(), o2.getName());
		}
	};

	public OntProperty getOntProperty() {
		return ontProperty;
	}

	@Override
	public OntProperty getOntResource() {
		return getOntProperty();
	}

	private void updateSuperProperties(OntProperty anOntProperty) {
		// superClasses.clear();
		try {
			Iterator<? extends OntProperty> it = anOntProperty.listSuperProperties(true);
			while (it.hasNext()) {
				OntProperty father = it.next();
				OWLProperty fatherProp = getOntology().getProperty(father.getURI());
				if (fatherProp != null) {
					if (!superProperties.contains(fatherProp)) {
						superProperties.add(fatherProp);
					}
				}
			}
		} catch (ConversionException e) {
			logger.warning("Unexpected " + e.getMessage() + " while processing " + getURI());
			// Petit hack en attendant de mieux comprendre le probleme
			if (getURI().equals("http://www.w3.org/2004/02/skos/core#altLabel")
					|| getURI().equals("http://www.w3.org/2004/02/skos/core#prefLabel")
					|| getURI().equals("http://www.w3.org/2004/02/skos/core#hiddenLabel")) {
				OWLProperty label = getOntology().getProperty("http://www.w3.org/2000/01/rdf-schema#label");
				if (!superProperties.contains(label)) {
					superProperties.add(label);
				}
			}
		}
	}

	@Override
	public Vector<OWLProperty> getSuperProperties() {
		return superProperties;
	}

	/**
	 * Return a vector of Ontology property, as a subset of getSubProperties(), which correspond to all properties necessary to see all
	 * properties belonging to supplied context, which is an ontology
	 * 
	 * @param context
	 * @return
	 */
	@Override
	public final List<OWLProperty> getSubProperties(IFlexoOntology<OWLTechnologyAdapter> context) {
		if (context instanceof OWLOntology) {
			List<OWLProperty> returned = new Vector<OWLProperty>();
			for (OWLDataProperty p : ((OWLOntology) context).getAccessibleDataProperties()) {
				if (p.isSubConceptOf(this)) {
					if (!returned.contains(p)) {
						returned.add(p);
					}
				}
			}
			for (OWLObjectProperty p : ((OWLOntology) context).getAccessibleObjectProperties()) {
				if (p.isSubConceptOf(this)) {
					if (!returned.contains(p)) {
						returned.add(p);
					}
				}
			}
			return returned;
		}

		return null;
	}

	@Override
	public final boolean isSuperConceptOf(IFlexoOntologyConcept<OWLTechnologyAdapter> concept) {
		if (concept instanceof OWLProperty) {
			OWLProperty ontologyDataProperty = (OWLProperty) concept;
			return ontologyDataProperty.getOntProperty().hasSuperProperty(getOntProperty(), false);
		}
		return false;
	}

	@Override
	public boolean isAnnotationProperty() {
		return getOntResource().isAnnotationProperty();// isAnnotationProperty;
	}

	@Override
	public void updateOntologyStatements(OntProperty anOntResource) {
		super.updateOntologyStatements(anOntResource);
		superDomainStatementWereAppened = false;
		superRangeStatementWereAppened = false;
		domainStatementList.clear();
		rangeStatementList.clear();
		domainList = null;
		rangeList = null;
		for (OWLStatement s : getSemanticStatements()) {
			if (s instanceof DomainStatement) {
				domainStatement = (DomainStatement) s;
				domainStatementList.add(domainStatement);
			}
			if (s instanceof RangeStatement) {
				rangeStatement = (RangeStatement) s;
				rangeStatementList.add(rangeStatement);
			}
		}
	}

	/**
	 * Return domain statement, asserting there is only one domain statement
	 * 
	 * @return
	 */
	public DomainStatement getDomainStatement() {
		if (domainStatement == null) {
			for (OWLProperty p : getSuperProperties()) {
				DomainStatement d = p.getDomainStatement();
				if (d != null) {
					return d;
				}
			}
			return null;
		}
		return domainStatement;
	}

	/**
	 * Return range statement, asserting there is only one range statement
	 * 
	 * @return
	 */
	public RangeStatement getRangeStatement() {
		if (rangeStatement == null) {
			for (OWLProperty p : getSuperProperties()) {
				RangeStatement r = p.getRangeStatement();
				if (r != null) {
					return r;
				}
			}
			return null;
		}
		return rangeStatement;
	}

	/**
	 * Return domain as ontology object, asserting there is only one domain statement
	 * 
	 * @return
	 */
	@Override
	public IFlexoOntologyConcept<OWLTechnologyAdapter> getDomain() {
		if (getDomainStatement() == null) {
			for (OWLProperty p : getSuperProperties()) {
				IFlexoOntologyConcept<OWLTechnologyAdapter> o = p.getDomain();
				if (o != null) {
					return o;
				}
			}
			return null;
		}
		return getDomainStatement().getDomain();
	}

	/**
	 * Return range as ontology object, asserting there is only one range statement
	 * 
	 * @return
	 */
	@Override
	public OWLObject getRange() {
		if (getRangeStatement() == null) {
			return null;
		}
		return getRangeStatement().getRange();
	}

	/**
	 * Return list of DomainStatement
	 * 
	 * @return
	 */
	public List<DomainStatement> getDomainStatementList() {
		if (!superDomainStatementWereAppened) {
			for (OWLProperty p : getSuperProperties()) {
				domainStatementList.addAll(p.getDomainStatementList());
			}
			superDomainStatementWereAppened = true;
		}
		return domainStatementList;
	}

	public List<RangeStatement> getRangeStatementList() {
		if (!superRangeStatementWereAppened) {
			for (OWLProperty p : getSuperProperties()) {
				rangeStatementList.addAll(p.getRangeStatementList());
			}
			superRangeStatementWereAppened = true;
		}
		return rangeStatementList;
	}

	public List<OWLConcept<?>> getDomainList() {
		if (domainList == null) {
			domainList = new ArrayList<OWLConcept<?>>();
			for (DomainStatement s : getDomainStatementList()) {
				if (s.getDomain() != null) {
					domainList.add(s.getDomain());
				}
			}
		}
		return domainList;
	}

	public List<OWLConcept<?>> getRangeList() {
		if (rangeList == null) {
			rangeList = new ArrayList<OWLConcept<?>>();
			for (RangeStatement s : getRangeStatementList()) {
				if (s.getRange() != null) {
					rangeList.add(s.getRange());
				}
			}
		}
		return rangeList;
	}

	public List<OWLRestriction> getReferencingRestrictions() {
		return referencingRestrictions;
	}

	public void addToReferencingRestriction(OWLRestriction aRestriction) {
		referencingRestrictions.add(aRestriction);
	}

	public void removeFromReferencingRestriction(OWLRestriction aRestriction) {
		referencingRestrictions.remove(aRestriction);
	}

	@Override
	public List<OWLRestriction> getReferencingFeatureAssociations() {
		return getReferencingRestrictions();
	}

	@Override
	public List<? extends IFlexoOntologyFeatureAssociation<OWLTechnologyAdapter>> getStructuralFeatureAssociations() {
		// No feature associations for this kind of concept
		return Collections.emptyList();
	}

	/*@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + getURI() + " originalDefinition=" + getOriginalDefinition();
	}*/

	@Override
	public String toString() {
		return getClass().getSimpleName() + Integer.toHexString(hashCode()) + ":" + getURI() + " (originalDefinition="
				+ getOriginalDefinition() + ") in " + getOntology();
	}

}
