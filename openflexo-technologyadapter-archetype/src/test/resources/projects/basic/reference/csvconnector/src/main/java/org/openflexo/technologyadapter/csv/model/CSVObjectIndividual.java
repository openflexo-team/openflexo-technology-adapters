/*
 * (c) Copyright 2013- Openflexo
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


package org.openflexo.technologyadapter.csv.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.ontology.IFlexoOntologyAnnotation;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer;
import org.openflexo.foundation.ontology.IFlexoOntologyConceptVisitor;
import org.openflexo.foundation.ontology.IFlexoOntologyFeature;
import org.openflexo.foundation.ontology.IFlexoOntologyFeatureAssociation;
import org.openflexo.foundation.ontology.IFlexoOntologyIndividual;
import org.openflexo.foundation.ontology.IFlexoOntologyPropertyValue;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;

/**
 * CSV Object Individual.
 * 
 * @author Jean Le Paon
 */
public class CSVObjectIndividual extends CSVModelObjectImpl<Object> implements IFlexoOntologyIndividual<CSVTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(CSVObjectIndividual.class.getPackage().getName());

	/**
	 * Constructor.
	 * 
	 * @param ontology
	 * @param object
	 */
	public CSVObjectIndividual(CSVModel ontology, Object object) {
		super(ontology, object);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyObject#getName()
	 */
	@Override
	public String getName() {
		return CSVModelURIBuilder.getName(object);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyObject#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) throws Exception {
		System.out.println("Name can't be modified.");
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyObject#getURI()
	 */
	@Override
	public String getURI() {
		return CSVModelURIBuilder.getUri(object);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyObject#getDescription()
	 */
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.FlexoOntologyObjectImpl#getDisplayableDescription()
	 */
	@Override
	public String getDisplayableDescription() {
		return getDescription();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getAnnotations()
	 */
	@Override
	public List<IFlexoOntologyAnnotation> getAnnotations() {
		// FIXME
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getContainer()
	 */
	@Override
	public IFlexoOntologyConceptContainer<CSVTechnologyAdapter> getContainer() {
		return ontology;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getStructuralFeatureAssociations()
	 */
	@Override
	public List<IFlexoOntologyFeatureAssociation<CSVTechnologyAdapter>> getStructuralFeatureAssociations() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getBehaviouralFeatureAssociations()
	 */
	@Override
	public List<? extends IFlexoOntologyFeatureAssociation<CSVTechnologyAdapter>> getBehaviouralFeatureAssociations() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#isSuperConceptOf(org.openflexo.foundation.ontology.IFlexoOntologyConcept)
	 */
	@Override
	public boolean isSuperConceptOf(IFlexoOntologyConcept<CSVTechnologyAdapter> concept) {
		return false;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#equalsToConcept(org.openflexo.foundation.ontology.IFlexoOntologyConcept)
	 */
	@Override
	public boolean equalsToConcept(IFlexoOntologyConcept<CSVTechnologyAdapter> concept) {
		return concept == this;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#isSubConceptOf(org.openflexo.foundation.ontology.IFlexoOntologyConcept)
	 */
	@Override
	public boolean isSubConceptOf(IFlexoOntologyConcept<CSVTechnologyAdapter> concept) {
		return false;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#accept(org.openflexo.foundation.ontology.IFlexoOntologyConceptVisitor)
	 */
	@Override
	public <T> T accept(IFlexoOntologyConceptVisitor<T> visitor) {
		return visitor.visit(this);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyIndividual#getTypes()
	 */
	@Override
	public List<IFlexoOntologyClass<CSVTechnologyAdapter>> getTypes() {
		// TODO
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyIndividual#isIndividualOf(org.openflexo.foundation.ontology.IFlexoOntologyClass)
	 */
	@Override
	public boolean isIndividualOf(IFlexoOntologyClass<CSVTechnologyAdapter> aClass) {
		return getTypes().contains(aClass);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getPropertiesTakingMySelfAsRange()
	 */
	@Override
	@Deprecated
	public List<? extends IFlexoOntologyStructuralProperty<CSVTechnologyAdapter>> getPropertiesTakingMySelfAsRange() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getPropertiesTakingMySelfAsDomain()
	 */
	@Override
	@Deprecated
	public List<? extends IFlexoOntologyFeature<CSVTechnologyAdapter>> getPropertiesTakingMySelfAsDomain() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyObject#getTechnologyAdapter()
	 */
	@Override
	public CSVTechnologyAdapter getTechnologyAdapter() {
		return ontology.getTechnologyAdapter();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyIndividual#getPropertyValues()
	 */
	@Override
	public List<IFlexoOntologyPropertyValue<CSVTechnologyAdapter>> getPropertyValues() {
		List<IFlexoOntologyPropertyValue<CSVTechnologyAdapter>> propertyValues = new ArrayList<IFlexoOntologyPropertyValue<CSVTechnologyAdapter>>();
		
		// TODO : implement the List
		
		return Collections.unmodifiableList(propertyValues);
	}

	@Override
	public boolean delete(Object... context) {
		// TODO 
		return super.delete(context);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyIndividual#getPropertyValue(org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty)
	 */
	@Override
	public IFlexoOntologyPropertyValue<CSVTechnologyAdapter> getPropertyValue(
			IFlexoOntologyStructuralProperty<CSVTechnologyAdapter> property) {
		IFlexoOntologyPropertyValue<CSVTechnologyAdapter> result = null;
		for (IFlexoOntologyPropertyValue<CSVTechnologyAdapter> propertyValue : getPropertyValues()) {
			if (result == null && property.equalsToConcept(propertyValue.getProperty())) {
				result = propertyValue;
			}
		}
		return result;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyIndividual#addToPropertyValue(org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty,
	 *      java.lang.Object)
	 */
	@Override
	public IFlexoOntologyPropertyValue<CSVTechnologyAdapter> addToPropertyValue(
			IFlexoOntologyStructuralProperty<CSVTechnologyAdapter> property, Object newValue) {
		System.out.println("Property Values can't be modified.");
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyIndividual#removeFromPropertyValue(org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty,
	 *      java.lang.Object)
	 */
	@Override
	public IFlexoOntologyPropertyValue<CSVTechnologyAdapter> removeFromPropertyValue(
			IFlexoOntologyStructuralProperty<CSVTechnologyAdapter> property, Object valueToRemove) {
		System.out.println("Property Values can't be modified.");
		return null;
	}

	@Override
	public String toString() {
		// return "CSVObjectIndividual/" + getTypes().get(0) + ":" + getName() + "uri=" + getURI();
		return getTypes().get(0).getName() + ":" + getName();
	}

}
