#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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


package ${package}.model;

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
import ${package}.${technologyPrefix}TechnologyAdapter;

/**
 * ${technologyPrefix} Object Individual.
 * 
 * @author ${author}
 */
public class ${technologyPrefix}ObjectIndividual extends ${technologyPrefix}ModelObjectImpl<Object> implements IFlexoOntologyIndividual<${technologyPrefix}TechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(${technologyPrefix}ObjectIndividual.class.getPackage().getName());

	private static ${technologyPrefix}ObjectIndividualReferenceObjectPropertyValueAsList containingPropertyValue;

	/**
	 * Constructor.
	 * 
	 * @param ontology
	 * @param object
	 */
	public ${technologyPrefix}ObjectIndividual(${technologyPrefix}Model ontology, Object object) {
		super(ontology, object);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyObject#getName()
	 */
	@Override
	public String getName() {
		return ${technologyPrefix}ModelURIBuilder.getName(object);
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
		return ${technologyPrefix}ModelURIBuilder.getUri(object);
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
	public IFlexoOntologyConceptContainer<${technologyPrefix}TechnologyAdapter> getContainer() {
		return ontology;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getStructuralFeatureAssociations()
	 */
	@Override
	public List<IFlexoOntologyFeatureAssociation<${technologyPrefix}TechnologyAdapter>> getStructuralFeatureAssociations() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getBehaviouralFeatureAssociations()
	 */
	@Override
	public List<? extends IFlexoOntologyFeatureAssociation<${technologyPrefix}TechnologyAdapter>> getBehaviouralFeatureAssociations() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#isSuperConceptOf(org.openflexo.foundation.ontology.IFlexoOntologyConcept)
	 */
	@Override
	public boolean isSuperConceptOf(IFlexoOntologyConcept<${technologyPrefix}TechnologyAdapter> concept) {
		return false;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#equalsToConcept(org.openflexo.foundation.ontology.IFlexoOntologyConcept)
	 */
	@Override
	public boolean equalsToConcept(IFlexoOntologyConcept<${technologyPrefix}TechnologyAdapter> concept) {
		return concept == this;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#isSubConceptOf(org.openflexo.foundation.ontology.IFlexoOntologyConcept)
	 */
	@Override
	public boolean isSubConceptOf(IFlexoOntologyConcept<${technologyPrefix}TechnologyAdapter> concept) {
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
	public List<IFlexoOntologyClass<${technologyPrefix}TechnologyAdapter>> getTypes() {
		return Collections.unmodifiableList(Collections.singletonList((IFlexoOntologyClass<${technologyPrefix}TechnologyAdapter>) ontology.getMetaModel()
				.getConverter().convertClass(ontology.getMetaModel(), object.eClass())));
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyIndividual#isIndividualOf(org.openflexo.foundation.ontology.IFlexoOntologyClass)
	 */
	@Override
	public boolean isIndividualOf(IFlexoOntologyClass<${technologyPrefix}TechnologyAdapter> aClass) {
		return getTypes().contains(aClass);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getPropertiesTakingMySelfAsRange()
	 */
	@Override
	@Deprecated
	public List<? extends IFlexoOntologyStructuralProperty<${technologyPrefix}TechnologyAdapter>> getPropertiesTakingMySelfAsRange() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getPropertiesTakingMySelfAsDomain()
	 */
	@Override
	@Deprecated
	public List<? extends IFlexoOntologyFeature<${technologyPrefix}TechnologyAdapter>> getPropertiesTakingMySelfAsDomain() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyObject#getTechnologyAdapter()
	 */
	@Override
	public ${technologyPrefix}TechnologyAdapter getTechnologyAdapter() {
		return ontology.getTechnologyAdapter();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyIndividual#getPropertyValues()
	 */
	@Override
	public List<IFlexoOntologyPropertyValue<${technologyPrefix}TechnologyAdapter>> getPropertyValues() {
		List<IFlexoOntologyPropertyValue<${technologyPrefix}TechnologyAdapter>> propertyValues = new ArrayList<IFlexoOntologyPropertyValue<${technologyPrefix}TechnologyAdapter>>();
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
			this.get${technologyPrefix}Model().get${technologyPrefix}Resource().getContents().remove(this.getObject());
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
	public IFlexoOntologyPropertyValue<${technologyPrefix}TechnologyAdapter> getPropertyValue(
			IFlexoOntologyStructuralProperty<${technologyPrefix}TechnologyAdapter> property) {
		IFlexoOntologyPropertyValue<${technologyPrefix}TechnologyAdapter> result = null;
		for (IFlexoOntologyPropertyValue<${technologyPrefix}TechnologyAdapter> propertyValue : getPropertyValues()) {
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
	public IFlexoOntologyPropertyValue<${technologyPrefix}TechnologyAdapter> addToPropertyValue(
			IFlexoOntologyStructuralProperty<${technologyPrefix}TechnologyAdapter> property, Object newValue) {
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
	public IFlexoOntologyPropertyValue<${technologyPrefix}TechnologyAdapter> removeFromPropertyValue(
			IFlexoOntologyStructuralProperty<${technologyPrefix}TechnologyAdapter> property, Object valueToRemove) {
		System.out.println("Property Values can't be modified.");
		return null;
	}

	@Override
	public String toString() {
		// return "${technologyPrefix}ObjectIndividual/" + getTypes().get(0) + ":" + getName() + "uri=" + getURI();
		return getTypes().get(0).getName() + ":" + getName();
	}

	public ${technologyPrefix}ObjectIndividualReferenceObjectPropertyValueAsList getContainingPropertyValue() {
		return this.containingPropertyValue;
	}

	public void setContainPropertyValue(${technologyPrefix}ObjectIndividualReferenceObjectPropertyValueAsList container) {
		containingPropertyValue = container;
	}

}
