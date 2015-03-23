/**
 * 
 * Copyright (c) 2013-2015, Openflexo
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
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.ontology.IFlexoOntologyAnnotation;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer;
import org.openflexo.foundation.ontology.IFlexoOntologyConceptVisitor;
import org.openflexo.foundation.ontology.IFlexoOntologyFeatureAssociation;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;

/**
 * EMF Class.
 * 
 * @author gbesancon
 */
public class EMFClassClass extends AEMFMetaModelObjectImpl<EClass> implements IFlexoOntologyClass<EMFTechnologyAdapter> {
	/**
	 * Constructor.
	 */
	public EMFClassClass(EMFMetaModel metaModel, EClass aClass) {
		super(metaModel, aClass);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getURI()
	 */
	@Override
	public String getURI() {
		return EMFMetaModelURIBuilder.getUri(object);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getName()
	 */
	@Override
	public String getName() {
		return object.getName();
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
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getDescription()
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
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getContainer()
	 */
	@Override
	public IFlexoOntologyConceptContainer<EMFTechnologyAdapter> getContainer() {
		return ontology.getConverter().convertPackage(ontology, object.getEPackage());
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getAnnotations()
	 */
	@Override
	public List<IFlexoOntologyAnnotation> getAnnotations() {
		List<IFlexoOntologyAnnotation> annotations = null;
		if (object.getEAnnotations() != null && object.getEAnnotations().size() != 0) {
			annotations = new ArrayList<IFlexoOntologyAnnotation>();
			for (EAnnotation annotation : object.getEAnnotations()) {
				annotations.add(ontology.getConverter().convertAnnotation(ontology, annotation));
			}
		} else {
			annotations = Collections.emptyList();
		}
		return annotations;
	}

	/**
	 * Return declared association with features for this concept.<br>
	 * Note that parent classes are not taken under account
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getFeatureAssociations()
	 */
	public List<IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter>> getDeclaredFeatureAssociations() {
		List<IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter>> featureAssociations = new ArrayList<IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter>>(
				0);
		for (EAttribute attribute : object.getEAttributes()) {
			featureAssociations.add(ontology.getConverter().convertAttributeAssociation(ontology, attribute, this));
		}
		for (EReference reference : object.getEReferences()) {
			featureAssociations.add(ontology.getConverter().convertReferenceAssociation(ontology, reference,this));
		}
		return Collections.unmodifiableList(featureAssociations);
	}

	/**
	 * Return association with features for this concept.<br>
	 * Note that this method consider inheritance
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#getStructuralFeatureAssociations()
	 */
	@Override
	public List<IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter>> getStructuralFeatureAssociations() {
		List<IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter>> featureAssociations = new ArrayList<IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter>>();
		appendFeatureAssociation(this, featureAssociations);
		return Collections.unmodifiableList(featureAssociations);
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

	private void appendFeatureAssociation(EMFClassClass aClass, List<IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter>> answer) {
		answer.addAll(aClass.getDeclaredFeatureAssociations());
		for (IFlexoOntologyClass<EMFTechnologyAdapter> superClass : aClass.getSuperClasses()) {
			if (superClass instanceof EMFClassClass) {
				appendFeatureAssociation((EMFClassClass) superClass, answer);
			}
		}
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#isSuperConceptOf(org.openflexo.foundation.ontology.IFlexoOntologyConcept)
	 */
	@Override
	public boolean isSuperConceptOf(IFlexoOntologyConcept<EMFTechnologyAdapter> concept) {
		return concept instanceof IFlexoOntologyClass ? isSuperClassOf((IFlexoOntologyClass<EMFTechnologyAdapter>) concept) : false;
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
		return concept instanceof IFlexoOntologyClass ? isSubClassOf((IFlexoOntologyClass<EMFTechnologyAdapter>) concept) : false;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConcept#accept(org.openflexo.foundation.ontology.IFlexoOntologyVisitor)
	 */
	@Override
	public <T> T accept(IFlexoOntologyConceptVisitor<T> visitor) {
		return visitor.visit(this);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyClass#getSuperClasses()
	 */
	@Override
	public List<IFlexoOntologyClass<EMFTechnologyAdapter>> getSuperClasses() {
		
		
		List<IFlexoOntologyClass<EMFTechnologyAdapter>> superClasses = new ArrayList<IFlexoOntologyClass<EMFTechnologyAdapter>>();
		for (EClass superClass : object.getESuperTypes()) {
			superClasses.add(ontology.getConverter().convertClass(ontology, superClass));
		}
		return Collections.unmodifiableList(superClasses);
	}

	
	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyClass#getSubClasses(org.openflexo.foundation.ontology.IFlexoOntology)
	 */
	@Override
	public List<? extends IFlexoOntologyClass<EMFTechnologyAdapter>> getSubClasses(IFlexoOntology<EMFTechnologyAdapter> context) {
		
		System.out.println("Looking for subclasses of: " + this.getName());
		
		List<IFlexoOntologyClass<EMFTechnologyAdapter>> subClasses = new ArrayList<IFlexoOntologyClass<EMFTechnologyAdapter>>();
		if (context instanceof EMFMetaModel) {
			for (Entry<EClass, EMFClassClass> classEntry : ontology.getConverter().getClasses().entrySet()) {
				if (classEntry.getValue().getOntology() == context) {
					if (classEntry.getKey().getESuperTypes().contains(object) && classEntry.getValue() != this) {
						subClasses.add(classEntry.getValue());
					}
				}
			}
		}
		return Collections.unmodifiableList(subClasses);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyClass#isSuperClassOf(org.openflexo.foundation.ontology.IFlexoOntologyClass)
	 */
	@Override
	public boolean isSuperClassOf(IFlexoOntologyClass<EMFTechnologyAdapter> aClass) {
		boolean isSuperClass = false;
		if (aClass instanceof EMFClassClass && aClass != this) {
			isSuperClass = object.isSuperTypeOf(((EMFClassClass) aClass).getObject());
		}
		return isSuperClass;
	}

	/**
	 * Is SubClass Of.
	 * 
	 * @param aClass
	 * @return
	 */
	public boolean isSubClassOf(IFlexoOntologyClass<EMFTechnologyAdapter> aClass) {
		boolean isSubClass = false;
		if (aClass instanceof EMFClassClass && aClass != this) {
			isSubClass = ((EMFClassClass) aClass).getObject().isSuperTypeOf(object);
		}
		return isSubClass;
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
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyClass#isNamedClass()
	 */
	@Override
	@Deprecated
	public boolean isNamedClass() {
		return true;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyClass#isRootConcept()
	 */
	@Override
	@Deprecated
	public boolean isRootConcept() {
		return getName().equalsIgnoreCase("EObject");
	}

	@Override
	public String toString() {
		return "EMFClassClass:" + getName();
	}
}
