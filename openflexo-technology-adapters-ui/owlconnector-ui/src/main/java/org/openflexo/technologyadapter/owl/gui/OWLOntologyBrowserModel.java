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
package org.openflexo.technologyadapter.owl.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.components.widget.OntologyBrowserModel;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyObject;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.technologyadapter.owl.model.OWL2URIDefinitions;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLConcept;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;
import org.openflexo.technologyadapter.owl.model.OWLObject;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLProperty;
import org.openflexo.technologyadapter.owl.model.OWLRestriction;
import org.openflexo.technologyadapter.owl.model.RDFSURIDefinitions;
import org.openflexo.technologyadapter.owl.model.RDFURIDefinitions;
import org.openflexo.toolbox.StringUtils;

/**
 * Model supporting browsing through models or metamodels conform to {@link FlexoOntology} API<br>
 * 
 * Developers note: this model is shared by many widgets. Please modify it with caution.
 * 
 * @see FIBOWLClassSelector
 * @see FIBOWLIndividualSelector
 * @see FIBOWLPropertySelector
 * 
 * @author sguerin
 */
public class OWLOntologyBrowserModel extends OntologyBrowserModel<OWLTechnologyAdapter> {

	static final Logger logger = Logger.getLogger(OWLOntologyBrowserModel.class.getPackage().getName());

	private boolean showOWLAndRDFConcepts = false;

	public OWLOntologyBrowserModel(OWLOntology context) {
		super(context);
	}

	@Override
	protected List<OWLClass> getFirstDisplayableParents(IFlexoOntologyClass<OWLTechnologyAdapter> c) {
		return (List<OWLClass>) super.getFirstDisplayableParents(c);
	}

	/**
	 * OWLObjectProperty which declare a Literal range should be stored as DataProperty
	 */
	@Override
	protected boolean isDisplayableAsDataProperty(IFlexoOntologyStructuralProperty p) {
		if (p instanceof OWLObjectProperty && ((OWLObjectProperty) p).isLiteralRange()) {
			return true;
		}
		return p instanceof OWLDataProperty;
	}

	/**
	 * OWLObjectProperty which declare a Literal range should not be stored as ObjectProperty
	 */
	@Override
	protected boolean isDisplayableAsObjectProperty(IFlexoOntologyStructuralProperty p) {
		if (p instanceof OWLObjectProperty && ((OWLObjectProperty) p).isLiteralRange()) {
			return false;
		}
		return super.isDisplayableAsObjectProperty(p);
	}

	@Override
	public boolean isDisplayable(IFlexoOntologyObject<OWLTechnologyAdapter> object) {

		boolean debug = false;

		if (object.getName().equals("altLabel")) {
			System.out.println("isDisplayable for " + object + " ???");
			debug = true;
		}

		if (object instanceof OWLOntology) {
			if ((object == ((OWLOntology) object).getOntologyLibrary().getRDFOntology()
					|| object == ((OWLOntology) object).getOntologyLibrary().getRDFSOntology() || object == ((OWLOntology) object)
					.getOntologyLibrary().getOWLOntology()) && object != getContext()) {
				return getShowOWLAndRDFConcepts();
			}
			return true;
		}

		if (debug) {
			System.out.println("hop1");
		}

		if (!getShowOWLAndRDFConcepts() && StringUtils.isNotEmpty(object.getURI()) && object instanceof IFlexoOntologyConcept
				&& ((IFlexoOntologyConcept<OWLTechnologyAdapter>) object).getOntology() != getContext()) {
			if (object.getURI().startsWith(RDFURIDefinitions.RDF_ONTOLOGY_URI)
					|| object.getURI().startsWith(RDFSURIDefinitions.RDFS_ONTOLOGY_URI)
					|| object.getURI().startsWith(OWL2URIDefinitions.OWL_ONTOLOGY_URI)) {
				return false;
			}
		}

		if (debug) {
			System.out.println("hop2, domain = " + getDomain());
		}

		if (object instanceof IFlexoOntologyStructuralProperty && getDomain() != null) {
			IFlexoOntologyStructuralProperty<OWLTechnologyAdapter> p = (IFlexoOntologyStructuralProperty<OWLTechnologyAdapter>) object;
			if (p instanceof OWLProperty) {
				if (debug) {
					System.out.println("Pour la pte " + p);
				}
				if ((((OWLProperty) p).getDomainList().size() > 0)) {
					OWLProperty owlProperty = (OWLProperty) p;
					boolean hasASuperClass = false;
					for (OWLObject d : owlProperty.getDomainList()) {
						if (debug) {
							System.out.println("Pour le domaine " + d);
						}
						// List<OWLClass> dList = getFirstDisplayableParents((OWLClass) d);
						// for (OWLClass visibleClass : dList) {
						/*if (debug) {
							System.out.println("Plutot que " + d + " on regarde avec " + d);
						}*/

						if (((OWLClass) d).isSuperClassOf(getDomain())) {
							hasASuperClass = true;
						} else {
							if (debug) {
								System.out.println("desole, " + d + " n'est pas super class de " + getDomain());
							}
						}

						// }

						/*if (((OWLClass) d).isSuperClassOf(getDomain())) {
							hasASuperClass = true;
						} else {
							System.out.println("desole, " + d + " n'est pas super class de " + getDomain());
						}*/
						/*if (getDomain().isSuperClassOf((OWLClass) d)) {
							hasASuperClass = true;
						} else {
							System.out.println("desole, " + getDomain() + " n'est pas super class de " + d);
						}*/
					}
					if (!hasASuperClass) {
						return false;
					}
				} else {
					/*if (!getDomain().isRootConcept()) {
						return false;
					}*/
				}
			}
		}

		if (debug) {
			System.out.println("hop3, c'est le super qui decide pour " + object + ", et qui decide " + super.isDisplayable(object));
		}

		return super.isDisplayable(object);
	}

	/**
	 * Compute a list of preferred location for an ontology property to be displayed.<br>
	 * If searchedOntology is not null, restrict returned list to classes declared in supplied ontology
	 * 
	 * @param p
	 * @param searchedOntology
	 * @return
	 */
	@Override
	protected List<IFlexoOntologyClass<OWLTechnologyAdapter>> getPreferredStorageLocations(
			IFlexoOntologyStructuralProperty<OWLTechnologyAdapter> p, IFlexoOntology<OWLTechnologyAdapter> searchedOntology) {
		List<IFlexoOntologyClass<OWLTechnologyAdapter>> potentialStorageClasses = super.getPreferredStorageLocations(p, searchedOntology);

		/*if (p.getName().equals("altLabel")) {
			System.out.println("Je suis dans OWLOntologyBrowser, j'ai pour le moment: ");
			for (IFlexoOntologyClass<OWLTechnologyAdapter> preferredLocation : potentialStorageClasses) {
				System.out.println(" > " + preferredLocation);
			}
		}*/

		if (p instanceof OWLProperty && ((OWLProperty) p).getDomainList().size() > 0) {
			OWLProperty owlProperty = (OWLProperty) p;
			for (OWLObject d : owlProperty.getDomainList()) {
				OWLClass domainClass = (OWLClass) (searchedOntology != null ? searchedOntology : getContext()).getClass(d.getURI());
				if (domainClass != null && (searchedOntology == null || domainClass.getFlexoOntology() == searchedOntology)) {
					if (getDomain() == null || domainClass.isSuperClassOf(getDomain())) {
						if (!potentialStorageClasses.contains(domainClass) && isDisplayable(domainClass)) {
							potentialStorageClasses.add(domainClass);
						}
					} else {
						// Do not add it, it does not concern this
					}
				}
			}
		}

		for (IFlexoOntologyClass<OWLTechnologyAdapter> c : getContext().getAccessibleClasses()) {
			if (c.isNamedClass()) {
				for (IFlexoOntologyClass<OWLTechnologyAdapter> superClass : c.getSuperClasses()) {
					if (superClass instanceof OWLRestriction && ((OWLRestriction) superClass).getProperty().equalsToConcept(p)) {
						if (searchedOntology == null || c.getOntology() == searchedOntology) {
							if (!potentialStorageClasses.contains(c) && isDisplayable(c)) {
								potentialStorageClasses.add(c);
							}
						}
					}
				}
			}
		}

		/*if (p.getName().equals("altLabel")) {
			System.out.println("2 - Je suis dans OWLOntologyBrowser, j'ai pour le moment: ");
			for (IFlexoOntologyClass<OWLTechnologyAdapter> preferredLocation : potentialStorageClasses) {
				System.out.println(" > " + preferredLocation);
			}
		}*/

		// Remove Thing references if list is non trivially the Thing singleton
		for (IFlexoOntologyClass<OWLTechnologyAdapter> c2 : new ArrayList<IFlexoOntologyClass<OWLTechnologyAdapter>>(
				potentialStorageClasses)) {
			if (c2 == null || (c2.isRootConcept() && potentialStorageClasses.size() > 1)) {
				potentialStorageClasses.remove(c2);
			}
		}

		/*if (p.getName().equals("altLabel")) {
			System.out.println("3 - Je suis dans OWLOntologyBrowser, j'ai pour le moment: ");
			for (IFlexoOntologyClass<OWLTechnologyAdapter> preferredLocation : potentialStorageClasses) {
				System.out.println(" > " + preferredLocation);
			}
		}*/

		return potentialStorageClasses;

		/*if (potentialStorageClasses.size() > 0) {
			return potentialStorageClasses.get(0);
		}*/

		/*if (p.getStorageLocations().size() > 0) {
			return p.getStorageLocations().get(0);
		}*/
		// return null;
	}

	/**
	 * Remove originals from redefined classes<br>
	 * Special case: original Thing definition is kept and redefinitions are excluded
	 * 
	 * @param list
	 */
	@Override
	protected void removeOriginalFromRedefinedObjects(List<? extends IFlexoOntologyObject<OWLTechnologyAdapter>> list) {

		for (IFlexoOntologyObject<OWLTechnologyAdapter> c : new ArrayList<IFlexoOntologyObject<OWLTechnologyAdapter>>(list)) {
			if (c instanceof OWLConcept<?> && ((OWLConcept<?>) c).redefinesOriginalDefinition()) {
				list.remove(((OWLConcept<?>) c).getOriginalDefinition());
			}
			if (c instanceof OWLClass && ((OWLClass) c).isRootConcept() && ((OWLClass) c).getOntology() != getContext()
					&& list.contains(getContext().getRootConcept())) {
				list.remove(c);
			}
		}
	}

	public boolean getShowOWLAndRDFConcepts() {
		return showOWLAndRDFConcepts;
	}

	public void setShowOWLAndRDFConcepts(boolean showOWLAndRDFConcepts) {
		this.showOWLAndRDFConcepts = showOWLAndRDFConcepts;
		if (autoUpdate) {
			recomputeStructure();
		}
	}

}
