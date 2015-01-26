/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012, THALES SYSTEMES AEROPORTES - All Rights Reserved
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf.metamodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer;
import org.openflexo.foundation.ontology.IFlexoOntologyContainer;
import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyDataType;
import org.openflexo.foundation.ontology.IFlexoOntologyIndividual;
import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;

/**
 * EMF Package Container.
 * 
 * @author gbesancon
 */
public class EMFPackageContainer extends AEMFMetaModelObjectImpl<EPackage> implements IFlexoOntologyContainer<EMFTechnologyAdapter> {
	/**
	 * Constructor.
	 */
	public EMFPackageContainer(EMFMetaModel metaModel, EPackage aPackage) {
		super(metaModel, aPackage);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyContainer#getName()
	 */
	@Override
	public String getName() {
		return object.getName();
	}

	@Override
	public void setName(String name) throws Exception {
		object.setName(name);
	}

	@Override
	public String getURI() {
		return object.getNsURI();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyContainer#getDescription()
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
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyContainer#getParent()
	 */
	@Override
	public IFlexoOntologyConceptContainer<EMFTechnologyAdapter> getParent() {
		IFlexoOntologyConceptContainer<EMFTechnologyAdapter> result = null;
		if (object.getESuperPackage() != null) {
			result = ontology.getConverter().convertPackage(ontology, object.getESuperPackage());
		} else {
			result = ontology;
		}
		return result;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyContainer#getSubContainers()
	 */
	@Override
	public List<IFlexoOntologyContainer<EMFTechnologyAdapter>> getSubContainers() {
		List<IFlexoOntologyContainer<EMFTechnologyAdapter>> containers = new ArrayList<IFlexoOntologyContainer<EMFTechnologyAdapter>>();
		for (EPackage eSubPackage : object.getESubpackages()) {
			containers.add(ontology.getConverter().convertPackage(ontology, eSubPackage));
		}
		return Collections.unmodifiableList(containers);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getConcepts()
	 */
	@Override
	public List<IFlexoOntologyConcept<EMFTechnologyAdapter>> getConcepts() {
		List<IFlexoOntologyConcept<EMFTechnologyAdapter>> result = new ArrayList<IFlexoOntologyConcept<EMFTechnologyAdapter>>();
		result.addAll(getClasses());
		result.addAll(getIndividuals());
		result.addAll(getDataProperties());
		result.addAll(getObjectProperties());
		return Collections.unmodifiableList(result);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getOntologyObject(java.lang.String)
	 */
	@Override
	public IFlexoOntologyConcept<EMFTechnologyAdapter> getOntologyObject(String objectURI) {
		IFlexoOntologyConcept<EMFTechnologyAdapter> result = null;
		for (IFlexoOntologyConcept<EMFTechnologyAdapter> concept : getConcepts()) {
			if (concept.getURI().equalsIgnoreCase(objectURI)) {
				result = concept;
			}
		}
		return result;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getIndividuals()
	 */
	@Override
	public List<? extends IFlexoOntologyIndividual<EMFTechnologyAdapter>> getIndividuals() {
		List<IFlexoOntologyIndividual<EMFTechnologyAdapter>> concepts = new ArrayList<IFlexoOntologyIndividual<EMFTechnologyAdapter>>();
		for (EClassifier classifier : object.getEClassifiers()) {
			if (classifier.eClass().getClassifierID() == EcorePackage.EENUM) {
				EEnum eEnum = (EEnum) classifier;
				for (EEnumLiteral eEnumLiteral : eEnum.getELiterals()) {
					concepts.add(ontology.getConverter().convertEnumLiteral(ontology, eEnumLiteral));
				}
			}
		}
		return Collections.unmodifiableList(concepts);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getIndividual(java.lang.String)
	 */
	@Override
	public IFlexoOntologyIndividual<EMFTechnologyAdapter> getIndividual(String individualURI) {
		IFlexoOntologyIndividual<EMFTechnologyAdapter> result = null;
		for (IFlexoOntologyIndividual<EMFTechnologyAdapter> individual : getIndividuals()) {
			if (individual.getURI().equalsIgnoreCase(individualURI)) {
				result = individual;
			}
		}
		return result;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getClasses()
	 */
	@Override
	public List<? extends IFlexoOntologyClass<EMFTechnologyAdapter>> getClasses() {
		List<IFlexoOntologyClass<EMFTechnologyAdapter>> concepts = new ArrayList<IFlexoOntologyClass<EMFTechnologyAdapter>>();
		for (EClassifier classifier : object.getEClassifiers()) {
			if (classifier.eClass().getClassifierID() == EcorePackage.ECLASS) {
				EClass eClass = (EClass) classifier;
				concepts.add(ontology.getConverter().convertClass(ontology, eClass));
			} else if (classifier.eClass().getClassifierID() == EcorePackage.EENUM) {
				EEnum eEnum = (EEnum) classifier;
				concepts.add(ontology.getConverter().convertEnum(ontology, eEnum));
			}
		}
		return Collections.unmodifiableList(concepts);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getClass(java.lang.String)
	 */
	@Override
	public IFlexoOntologyClass<EMFTechnologyAdapter> getClass(String classURI) {
		IFlexoOntologyClass<EMFTechnologyAdapter> result = null;
		for (IFlexoOntologyClass<EMFTechnologyAdapter> aClass : getClasses()) {
			if (aClass.getURI().equalsIgnoreCase(classURI)) {
				result = aClass;
			}
		}
		return result;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getProperty(java.lang.String)
	 */
	@Override
	public IFlexoOntologyStructuralProperty<EMFTechnologyAdapter> getProperty(String objectURI) {
		IFlexoOntologyStructuralProperty<EMFTechnologyAdapter> result = null;
		if (result == null) {
			result = getDataProperty(objectURI);
		}
		if (result == null) {
			result = getObjectProperty(objectURI);
		}
		return result;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getDataProperties()
	 */
	@Override
	public List<? extends IFlexoOntologyDataProperty<EMFTechnologyAdapter>> getDataProperties() {
		List<IFlexoOntologyDataProperty<EMFTechnologyAdapter>> concepts = new ArrayList<IFlexoOntologyDataProperty<EMFTechnologyAdapter>>();
		for (EClassifier classifier : object.getEClassifiers()) {
			if (classifier.eClass().getClassifierID() == EcorePackage.ECLASS) {
				EClass eClass = (EClass) classifier;
				for (EStructuralFeature feature : eClass.getEStructuralFeatures()) {
					if (feature.eClass().getClassifierID() == EcorePackage.EATTRIBUTE) {
						EAttribute attribute = (EAttribute) feature;
						if (attribute.getEAttributeType().eClass().getClassifierID() == EcorePackage.EDATA_TYPE) {
							concepts.add(ontology.getConverter().convertAttributeDataProperty(ontology, attribute));
						}
					}
				}
			}
		}
		return Collections.unmodifiableList(concepts);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getDataProperty(java.lang.String)
	 */
	@Override
	public IFlexoOntologyDataProperty<EMFTechnologyAdapter> getDataProperty(String propertyURI) {
		IFlexoOntologyDataProperty<EMFTechnologyAdapter> result = null;
		for (IFlexoOntologyDataProperty<EMFTechnologyAdapter> dataProperty : getDataProperties()) {
			if (dataProperty.getURI().equalsIgnoreCase(propertyURI)) {
				result = dataProperty;
			}
		}
		return result;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getObjectProperties()
	 */
	@Override
	public List<? extends IFlexoOntologyObjectProperty<EMFTechnologyAdapter>> getObjectProperties() {
		List<IFlexoOntologyObjectProperty<EMFTechnologyAdapter>> concepts = new ArrayList<IFlexoOntologyObjectProperty<EMFTechnologyAdapter>>();
		for (EClassifier classifier : object.getEClassifiers()) {
			if (classifier.eClass().getClassifierID() == EcorePackage.ECLASS) {
				EClass eClass = (EClass) classifier;
				for (EStructuralFeature feature : eClass.getEStructuralFeatures()) {
					if (feature.eClass().getClassifierID() == EcorePackage.EREFERENCE) {
						EReference reference = (EReference) feature;
						concepts.add(ontology.getConverter().convertReferenceObjectProperty(ontology, reference));
					} else if (feature.eClass().getClassifierID() == EcorePackage.EATTRIBUTE) {
						EAttribute attribute = (EAttribute) feature;
						if (attribute.getEAttributeType().eClass().getClassifierID() == EcorePackage.EENUM) {
							concepts.add(ontology.getConverter().convertAttributeObjectProperty(ontology, attribute));
						}
					}
				}
			}
		}
		return Collections.unmodifiableList(concepts);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getObjectProperty(java.lang.String)
	 */
	@Override
	public IFlexoOntologyObjectProperty<EMFTechnologyAdapter> getObjectProperty(String propertyURI) {
		IFlexoOntologyObjectProperty<EMFTechnologyAdapter> result = null;
		for (IFlexoOntologyObjectProperty<EMFTechnologyAdapter> objectProperty : getObjectProperties()) {
			if (objectProperty.getURI().equalsIgnoreCase(propertyURI)) {
				result = objectProperty;
			}
		}
		return result;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getDataTypes()
	 */
	@Override
	public List<IFlexoOntologyDataType<EMFTechnologyAdapter>> getDataTypes() {
		List<IFlexoOntologyDataType<EMFTechnologyAdapter>> dataTypes = new ArrayList<IFlexoOntologyDataType<EMFTechnologyAdapter>>();
		for (EClassifier classifier : object.getEClassifiers()) {
			if (classifier.eClass().getClassifierID() == EcorePackage.EDATA_TYPE) {
				EDataType eDataType = (EDataType) classifier;
				dataTypes.add(ontology.getConverter().convertDataType(ontology, eDataType));
			}
		}
		return Collections.unmodifiableList(dataTypes);
	}
}
