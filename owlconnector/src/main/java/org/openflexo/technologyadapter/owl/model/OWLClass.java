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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.ontology.ConversionException;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.foundation.ontology.OntologyUtils;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.toolbox.StringUtils;

public class OWLClass extends OWLConcept<OntClass>
		implements IFlexoOntologyClass<OWLTechnologyAdapter>, Comparable<IFlexoOntologyClass<OWLTechnologyAdapter>> {

	private static final Logger logger = Logger.getLogger(IFlexoOntologyClass.class.getPackage().getName());

	private OntClass ontClass;

	private final Vector<OWLClass> superClasses;

	private final List<OWLRestriction> restrictions = new ArrayList<OWLRestriction>();

	protected OWLClass(OntClass anOntClass, OWLOntology ontology, OWLTechnologyAdapter adapter) {
		super(anOntClass, ontology, adapter);
		superClasses = new Vector<OWLClass>();
		ontClass = anOntClass;
	}

	/**
	 * Init this IFlexoOntologyClass, given base OntClass
	 */
	protected void init() {
		updateOntologyStatements(ontClass);
		updateSuperClasses(ontClass);
	}

	@Override
	public boolean delete(Object... context) {
		getFlexoOntology().removeClass(this);
		getOntResource().remove();
		getFlexoOntology().updateConceptsAndProperties();
		super.delete(context);
		deleteObservers();
		return true;
	}

	/**
	 * Update this IFlexoOntologyClass, given base OntClass
	 */
	@Override
	protected void update() {
		update(ontClass);
	}

	/**
	 * Update this IFlexoOntologyClass, given base OntClass which is assumed to extends base OntClass
	 * 
	 * @param anOntClass
	 */
	protected void update(OntClass anOntClass) {
		updateOntologyStatements(anOntClass);
		updateSuperClasses(anOntClass);
		ontClass = anOntClass;
	}

	@Override
	public void setName(String aName) {
		renameURI(aName, ontClass, OntClass.class);
	}

	@Override
	protected void _setOntResource(OntClass r) {
		ontClass = r;
	}

	@Override
	public OWLClass getOriginalDefinition() {
		return (OWLClass) super.getOriginalDefinition();
	}

	private void appendToSuperClasses(OWLClass superClass) {
		if (getURI().equals(OWL2URIDefinitions.OWL_THING_URI)) {
			return;
		}
		if (superClass == this) {
			return;
		}
		// NPE Protection
		if (superClass != null) {

			if (superClass.redefinesOriginalDefinition()) {
				if (superClasses.contains(superClass.getOriginalDefinition())) {
					superClasses.remove(superClass.getOriginalDefinition());
				}
			}
			if (!superClasses.contains(superClass)) {
				superClasses.add(superClass);
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("Add " + superClass.getName() + " as a super class of " + getName());
				}
			}
		}
	}

	private void updateSuperClasses(OntClass anOntClass) {
		superClasses.clear();
		if (redefinesOriginalDefinition()) {
			for (OWLClass c : getOriginalDefinition().getSuperClasses()) {
				if (!c.isRootConcept()) {
					appendToSuperClasses(c);
				}
			}
		}

		Iterator it = anOntClass.listSuperClasses(true);
		while (it.hasNext()) {
			OntClass father = (OntClass) it.next();
			OWLClass fatherClass = getOntology().retrieveOntologyClass(father);// getOntologyLibrary().getClass(father.getURI());
			if (fatherClass != null) {
				appendToSuperClasses(fatherClass);
			}
		}

		// If this class is equivalent to the intersection of some other classes, then all those operand classes are super classes of this
		// class
		if (getEquivalentClass() instanceof OWLIntersectionClass) {
			for (OWLClass operand : ((OWLIntersectionClass) getEquivalentClass()).getOperands()) {
				appendToSuperClasses(operand);
			}
		}

		// If computed ontology is either not RDF, nor RDFS, nor OWL
		// add OWL Thing as parent
		if (getFlexoOntology() != getOntologyLibrary().getRDFOntology() && getFlexoOntology() != getOntologyLibrary().getRDFSOntology()) {
			if (isNamedClass() && !isRootConcept()) {
				OWLClass THING_CLASS = getOntology().getRootClass();
				appendToSuperClasses(THING_CLASS);
			}
		}
	}

	public static final Comparator<IFlexoOntologyClass<OWLTechnologyAdapter>> COMPARATOR = new Comparator<IFlexoOntologyClass<OWLTechnologyAdapter>>() {
		@Override
		public int compare(IFlexoOntologyClass<OWLTechnologyAdapter> o1, IFlexoOntologyClass<OWLTechnologyAdapter> o2) {
			return Collator.getInstance().compare(o1.getName(), o2.getName());
		}
	};

	@Override
	public int compareTo(IFlexoOntologyClass<OWLTechnologyAdapter> o) {
		return COMPARATOR.compare(this, o);
	}

	@Override
	public OntClass getOntResource() {
		return ontClass;
	}

	private static boolean isSuperClassOf(OntClass parentClass, OntClass subClass) {
		if (parentClass == null) {
			return false;
		}
		if (subClass == null) {
			return false;
		}
		if (parentClass.equals(subClass)) {
			return true;
		}
		Iterator it = subClass.listSuperClasses();
		while (it.hasNext()) {
			OntClass p = (OntClass) it.next();
			if (p.equals(parentClass)) {
				return true;
			}
			if (isSuperClassOf(parentClass, p)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isSuperClassOf(OntClass parentClass, Individual individual) {
		if (parentClass == null) {
			return false;
		}
		if (individual == null) {
			return false;
		}
		Iterator it = individual.listOntClasses(false);
		while (it.hasNext()) {
			try {
				OntClass p = (OntClass) it.next();
				if (p.equals(parentClass)) {
					return true;
				}
				if (isSuperClassOf(parentClass, p)) {
					return true;
				}
			} catch (ConversionException e) {
				logger.warning("Unexpected " + e);
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean isSuperConceptOf(IFlexoOntologyConcept<OWLTechnologyAdapter> concept) {
		if (OWL2URIDefinitions.OWL_THING_URI.equals(getURI())) {
			return true;
		}
		if (concept instanceof OWLIndividual) {
			OWLIndividual ontologyIndividual = (OWLIndividual) concept;
			// Doesn't work, i dont know why
			// return ontologyIndividual.getIndividual().hasOntClass(ontClass);
			return isSuperClassOf(ontClass, ontologyIndividual.getIndividual());
		}
		if (concept instanceof OWLClass) {
			OWLClass ontologyClass = (OWLClass) concept;
			// Doesn't work, i dont know why
			// return ontologyClass.getOntResource().hasSuperClass(ontClass);
			// return isSuperClassOf(ontClass, ontologyClass.getOntResource());
			return isSuperClassOf(ontologyClass);
		}
		return false;
	}

	@Override
	public boolean isSuperClassOf(IFlexoOntologyClass<OWLTechnologyAdapter> aClass) {

		if (aClass == this) {
			return true;
		}
		if (equalsToConcept(aClass)) {
			return true;
		}
		if (OWL2URIDefinitions.OWL_THING_URI.equals(getURI())) {
			return true;
		}
		// We assert here that all OWL classes inherits from Resource
		if (RDFSURIDefinitions.RDFS_RESOURCE_URI.equals(getURI())) {
			return true;
		}
		if (aClass instanceof OWLClass) {

			for (OWLClass c : ((OWLClass) aClass).getSuperClasses()) {

				if (isSuperClassOf(c)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Return all direct super classes of this class
	 * 
	 * @return
	 */
	@Override
	public Vector<OWLClass> getSuperClasses() {

		return superClasses;
	}

	/**
	 * Return all direct and infered super classes of this class
	 * 
	 * @return
	 */
	public List<OWLClass> getAllSuperClasses() {
		ArrayList<OWLClass> allSuperClasses = new ArrayList<OWLClass>();
		for (IFlexoOntologyClass<OWLTechnologyAdapter> cl : OntologyUtils.getAllSuperClasses(this)) {
			if (cl instanceof OWLClass) {
				allSuperClasses.add((OWLClass) cl);
			}
		}
		return allSuperClasses;
	}

	/**
	 * Add super class to this class
	 * 
	 * @param aClass
	 */
	public SubClassStatement addToSuperClasses(OWLClass father) {
		if (father instanceof OWLRestriction) {
			restrictions.add((OWLRestriction) father);
		}
		if (father != null) {
			getOntResource().addSuperClass(father.getOntResource());
			updateOntologyStatements();
			return getSubClassStatement(father);
		}
		logger.warning("Class " + father + " is null");
		return null;
	}

	public void removeFromSuperClasses(OWLClass aClass) {
		logger.warning("Not implemented");
	}

	/**
	 * Return a vector of Ontology class, as a subset of getSubClasses(), which correspond to all classes necessary to see all classes
	 * belonging to supplied context, which is an ontology
	 * 
	 * @param context
	 * @return
	 */
	@Override
	public List<OWLClass> getSubClasses(IFlexoOntology<OWLTechnologyAdapter> context) {
		ArrayList<OWLClass> returned = new ArrayList<OWLClass>();
		for (IFlexoOntologyClass<OWLTechnologyAdapter> aClass : context.getAccessibleClasses()) {
			if (aClass instanceof OWLClass && isSuperClassOf(aClass)) {
				returned.add((OWLClass) aClass);
			}
		}
		return returned;
	}

	@Override
	public String getDisplayableDescription() {
		/*String extendsLabel = " extends ";
		boolean isFirst = true;
		for (IFlexoOntologyClass s : superClasses) {
			extendsLabel += (isFirst ? "" : ",") + s.getName();
			isFirst = false;
		}
		return "Class " + getName() + extendsLabel;*/
		return getName();
	}

	@Override
	protected void recursivelySearchRangeAndDomains() {
		super.recursivelySearchRangeAndDomains();
		for (OWLClass aClass : getSuperClasses()) {
			propertiesTakingMySelfAsRange.addAll(aClass.getPropertiesTakingMySelfAsRange());
			propertiesTakingMySelfAsDomain.addAll(aClass.getPropertiesTakingMySelfAsDomain());
		}
		OWLClass CLASS_CONCEPT = getOntology().getClass(OWL_CLASS_URI);
		// CLASS_CONCEPT is generally non null but can be null when reading RDFS for exampel
		if (CLASS_CONCEPT != null) {
			propertiesTakingMySelfAsRange.addAll(CLASS_CONCEPT.getPropertiesTakingMySelfAsRange());
			propertiesTakingMySelfAsDomain.addAll(CLASS_CONCEPT.getPropertiesTakingMySelfAsDomain());
		}

		/*Vector<IFlexoOntologyClass> alreadyComputed = new Vector<IFlexoOntologyClass>();
		if (redefinesOriginalDefinition()) {
			_appendRangeAndDomains(getOriginalDefinition(), alreadyComputed);
		}
		for (IFlexoOntologyClass aClass : getSuperClasses()) {
			_appendRangeAndDomains(aClass, alreadyComputed);
		}*/
	}

	/*private void _appendRangeAndDomains(IFlexoOntologyClass superClass, Vector<IFlexoOntologyClass> alreadyComputed) {
		if (alreadyComputed.contains(superClass)) {
			return;
		}
		alreadyComputed.add(superClass);
		for (IFlexoOntologyStructuralProperty p : superClass.getDeclaredPropertiesTakingMySelfAsDomain()) {
			if (!propertiesTakingMySelfAsDomain.contains(p)) {
				propertiesTakingMySelfAsDomain.add(p);
			}
		}
		for (IFlexoOntologyStructuralProperty p : superClass.getDeclaredPropertiesTakingMySelfAsRange()) {
			if (!propertiesTakingMySelfAsRange.contains(p)) {
				propertiesTakingMySelfAsRange.add(p);
			}
		}
		for (IFlexoOntologyClass superSuperClass : superClass.getSuperClasses()) {
			_appendRangeAndDomains(superSuperClass, alreadyComputed);
		}
	}*/

	private IFlexoOntologyClass<OWLTechnologyAdapter> equivalentClass;
	private final List<IFlexoOntologyClass<OWLTechnologyAdapter>> equivalentClasses = new ArrayList<IFlexoOntologyClass<OWLTechnologyAdapter>>();

	@Override
	public void updateOntologyStatements(OntClass anOntResource) {
		super.updateOntologyStatements(anOntResource);
		equivalentClasses.clear();
		for (OWLStatement s : getSemanticStatements()) {
			if (s instanceof EquivalentClassStatement) {
				if (((EquivalentClassStatement) s).getEquivalentObject() instanceof IFlexoOntologyClass) {
					equivalentClass = (IFlexoOntologyClass<OWLTechnologyAdapter>) ((EquivalentClassStatement) s).getEquivalentObject();
					equivalentClasses.add(equivalentClass);
				}
			}
		}
	}

	/**
	 * Return equivalent class, asserting there is only one equivalent class statement
	 * 
	 * @return
	 */
	public IFlexoOntologyClass<OWLTechnologyAdapter> getEquivalentClass() {
		return equivalentClass;
	}

	/**
	 * Return all restrictions related to supplied property
	 * 
	 * @param property
	 * @return
	 */
	public List<OWLRestriction> getRestrictions(IFlexoOntologyStructuralProperty<OWLTechnologyAdapter> property) {
		List<OWLRestriction> returned = new ArrayList<OWLRestriction>();
		for (IFlexoOntologyClass<OWLTechnologyAdapter> c : getSuperClasses()) {
			if (c instanceof OWLRestriction) {
				OWLRestriction r = (OWLRestriction) c;
				if (r.getProperty() == property) {
					returned.add(r);
				}
			}
		}
		return returned;
	}

	@Override
	public String getHTMLDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("Class <b>" + getName() + "</b><br>");
		sb.append("<i>" + getURI() + "</i><br>");
		sb.append("<b>Asserted in:</b> " + getOntology().getURI() + "<br>");
		if (redefinesOriginalDefinition()) {
			sb.append("<b>Redefines:</b> " + getOriginalDefinition() + "<br>");
		}
		sb.append("<b>Superclasses:</b>");
		for (OWLClass c : getSuperClasses()) {
			sb.append(" " + c.getDisplayableDescription());
		}
		sb.append("</html>");
		return sb.toString();
	}

	/**
	 * Indicates if this class represents a named class
	 */
	@Override
	public boolean isNamedClass() {
		return StringUtils.isNotEmpty(getURI());
	}

	/**
	 * Indicates if this class represents the Thing root concept
	 */
	@Override
	public boolean isRootConcept() {
		return isNamedClass() && getURI().equals(OWL2URIDefinitions.OWL_THING_URI);
	}

	@Override
	public List<OWLRestriction> getStructuralFeatureAssociations() {
		return getRestrictions();
	}

	public List<OWLRestriction> getRestrictions() {
		return restrictions;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + Integer.toHexString(hashCode()) + ":" + getURI() + " (originalDefinition="
				+ getOriginalDefinition() + ") in " + getOntology();
	}

}
