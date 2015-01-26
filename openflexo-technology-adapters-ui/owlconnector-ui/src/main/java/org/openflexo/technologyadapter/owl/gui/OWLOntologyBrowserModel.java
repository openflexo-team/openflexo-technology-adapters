/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

		if (object instanceof OWLOntology) {
			if ((object == ((OWLOntology) object).getOntologyLibrary().getRDFOntology()
					|| object == ((OWLOntology) object).getOntologyLibrary().getRDFSOntology() || object == ((OWLOntology) object)
					.getOntologyLibrary().getOWLOntology()) && object != getContext()) {
				return getShowOWLAndRDFConcepts();
			}
			return true;
		}

		if (!getShowOWLAndRDFConcepts() && StringUtils.isNotEmpty(object.getURI()) && object instanceof IFlexoOntologyConcept
				&& ((IFlexoOntologyConcept<OWLTechnologyAdapter>) object).getOntology() != getContext()) {
			if (object.getURI().startsWith(RDFURIDefinitions.RDF_ONTOLOGY_URI)
					|| object.getURI().startsWith(RDFSURIDefinitions.RDFS_ONTOLOGY_URI)
					|| object.getURI().startsWith(OWL2URIDefinitions.OWL_ONTOLOGY_URI)) {
				return false;
			}
		}

		if (object instanceof IFlexoOntologyStructuralProperty && getDomain() != null) {
			IFlexoOntologyStructuralProperty<OWLTechnologyAdapter> p = (IFlexoOntologyStructuralProperty<OWLTechnologyAdapter>) object;
			if (p instanceof OWLProperty) {
				if ((((OWLProperty) p).getDomainList().size() > 0)) {
					OWLProperty owlProperty = (OWLProperty) p;
					boolean hasASuperClass = false;
					for (OWLObject d : owlProperty.getDomainList()) {
						if (((OWLClass) d).isSuperClassOf(getDomain())) {
							hasASuperClass = true;
						}

					}
					if (!hasASuperClass) {
						return false;
					}
				}
			}
		}

		// If we haven't returned false yet, object is still candidate for being visible
		// Now the super class takes the responsability

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

		// Remove Thing references if list is non trivially the Thing singleton
		for (IFlexoOntologyClass<OWLTechnologyAdapter> c2 : new ArrayList<IFlexoOntologyClass<OWLTechnologyAdapter>>(
				potentialStorageClasses)) {
			if (c2 == null || (c2.isRootConcept() && potentialStorageClasses.size() > 1)) {
				potentialStorageClasses.remove(c2);
			}
		}

		return potentialStorageClasses;

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
