/*
 * (c) Copyright 2012-2013 Openflexo
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

/** Copyright (c) 2012, THALES SYSTEMES AEROPORTES - All Rights Reserved
 * Author : Gilles Besançon
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
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or 
 * combining it with eclipse EMF (or a modified version of that library), 
 * containing parts covered by the terms of EPL 1.0, the licensors of this 
 * Program grant you additional permission to convey the resulting work.
 *
 * Contributors :
 *
 */

package org.openflexo.technologyadapter.emf.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
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
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.metamodel.EMFAttributeDataProperty;

/**
 * EMF Object Individual.
 * 
 * @author gbesancon
 */
public class EMFObjectIndividual extends AEMFModelObjectImpl<EObject> implements IFlexoOntologyIndividual<EMFTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(EMFObjectIndividual.class.getPackage().getName());

	private static EMFObjectIndividualReferenceObjectPropertyValueAsList containingPropertyValue;

	/**
	 * Constructor.
	 * 
	 * @param ontology
	 * @param object
	 */
	public EMFObjectIndividual(EMFModel ontology, EObject object) {
		super(ontology, object);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyObject#getName()
	 */
	@Override
	public String getName() {
		return EMFModelURIBuilder.getName(object);
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
		return EMFModelURIBuilder.getUri(object);
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
	public IFlexoOntologyConceptContainer<EMFTechnologyAdapter> getContainer() {
		return ontology;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getStructuralFeatureAssociations()
	 */
	@Override
	public List<IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter>> getStructuralFeatureAssociations() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getBehaviouralFeatureAssociations()
	 */
	@Override
	public List<? extends IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter>> getBehaviouralFeatureAssociations() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#isSuperConceptOf(org.openflexo.foundation.ontology.IFlexoOntologyConcept)
	 */
	@Override
	public boolean isSuperConceptOf(IFlexoOntologyConcept<EMFTechnologyAdapter> concept) {
		return false;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#equalsToConcept(org.openflexo.foundation.ontology.IFlexoOntologyConcept)
	 */
	@Override
	public boolean equalsToConcept(IFlexoOntologyConcept<EMFTechnologyAdapter> concept) {
		return concept == this;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#isSubConceptOf(org.openflexo.foundation.ontology.IFlexoOntologyConcept)
	 */
	@Override
	public boolean isSubConceptOf(IFlexoOntologyConcept<EMFTechnologyAdapter> concept) {
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
	public List<IFlexoOntologyClass<EMFTechnologyAdapter>> getTypes() {
		return Collections.unmodifiableList(Collections.singletonList((IFlexoOntologyClass<EMFTechnologyAdapter>) ontology.getMetaModel()
				.getConverter().convertClass(ontology.getMetaModel(), object.eClass())));
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyIndividual#isIndividualOf(org.openflexo.foundation.ontology.IFlexoOntologyClass)
	 */
	@Override
	public boolean isIndividualOf(IFlexoOntologyClass<EMFTechnologyAdapter> aClass) {
		return getTypes().contains(aClass);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getPropertiesTakingMySelfAsRange()
	 */
	@Override
	@Deprecated
	public List<? extends IFlexoOntologyStructuralProperty<EMFTechnologyAdapter>> getPropertiesTakingMySelfAsRange() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getPropertiesTakingMySelfAsDomain()
	 */
	@Override
	@Deprecated
	public List<? extends IFlexoOntologyFeature<EMFTechnologyAdapter>> getPropertiesTakingMySelfAsDomain() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyObject#getTechnologyAdapter()
	 */
	@Override
	public EMFTechnologyAdapter getTechnologyAdapter() {
		return ontology.getTechnologyAdapter();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyIndividual#getPropertyValues()
	 */
	@Override
	public List<IFlexoOntologyPropertyValue<EMFTechnologyAdapter>> getPropertyValues() {
		List<IFlexoOntologyPropertyValue<EMFTechnologyAdapter>> propertyValues = new ArrayList<IFlexoOntologyPropertyValue<EMFTechnologyAdapter>>();
		for (EStructuralFeature structuralFeature : object.eClass().getEAllStructuralFeatures()) {
			propertyValues.add(ontology.getConverter().getPropertyValues().get(object).get(structuralFeature));
		}
		return Collections.unmodifiableList(propertyValues);
	}

	@Override
	public boolean delete() {
		// TODO XTOF => implement an actual deletion mechanism
		// TODO URGENT => there might be a memory leak here !
		logger.warning("YOU NEED TO IMPLEMENT AN ACTUAL DELETION MECHANISM");
		if (this.containingPropertyValue == null) {
			this.getEMFModel().getEMFResource().getContents().remove(this.getObject());
		} else {
			containingPropertyValue.remove(this);
		}
		return super.delete();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyIndividual#getPropertyValue(org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty)
	 */
	@Override
	public IFlexoOntologyPropertyValue<EMFTechnologyAdapter> getPropertyValue(
			IFlexoOntologyStructuralProperty<EMFTechnologyAdapter> property) {
		IFlexoOntologyPropertyValue<EMFTechnologyAdapter> result = null;
		for (IFlexoOntologyPropertyValue<EMFTechnologyAdapter> propertyValue : getPropertyValues()) {
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
	public IFlexoOntologyPropertyValue<EMFTechnologyAdapter> addToPropertyValue(
			IFlexoOntologyStructuralProperty<EMFTechnologyAdapter> property, Object newValue) {

		EObject obj = this.getObject();
		if (property instanceof EMFAttributeDataProperty) {
			EAttribute Eattr = (EAttribute) ((EMFAttributeDataProperty) property).getObject();
			obj.eSet(Eattr, newValue);
		}
		else {
			System.out.println("Property Values can't be modified : " + property);
		}
		
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyIndividual#removeFromPropertyValue(org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty,
	 *      java.lang.Object)
	 */
	@Override
	public IFlexoOntologyPropertyValue<EMFTechnologyAdapter> removeFromPropertyValue(
			IFlexoOntologyStructuralProperty<EMFTechnologyAdapter> property, Object valueToRemove) {
		System.out.println("Property Values can't be modified.");
		return null;
	}

	@Override
	public String toString() {
		// return "EMFObjectIndividual/" + getTypes().get(0) + ":" + getName() + "uri=" + getURI();
		return getTypes().get(0).getName() + ":" + getName();
	}

	public EMFObjectIndividualReferenceObjectPropertyValueAsList getContainingPropertyValue() {
		return this.containingPropertyValue;
	}

	public void setContainPropertyValue(EMFObjectIndividualReferenceObjectPropertyValueAsList container) {
		containingPropertyValue = container;
	}

}
