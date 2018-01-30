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

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EPackage;
import org.openflexo.foundation.ontology.FlexoOntologyObjectImpl;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.ontology.IFlexoOntologyAnnotation;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyDataType;
import org.openflexo.foundation.ontology.IFlexoOntologyIndividual;
import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.metamodel.io.EMFMetaModelConverter;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;

/**
 * EMF Ecore MetaModel seen as a Flexo Ontology.
 * 
 * @author gbesancon
 */
public class EMFMetaModel extends FlexoOntologyObjectImpl<EMFTechnologyAdapter>
		implements FlexoMetaModel<EMFMetaModel>, IFlexoOntology<EMFTechnologyAdapter> {
	/** MetaModel Resource. */
	protected EMFMetaModelResource metaModelResource;
	/** Adapter. */
	protected final EMFTechnologyAdapter adapter;
	/** Package. */
	protected final EPackage ePackage;
	/** Converter. */
	protected final EMFMetaModelConverter converter;

	/**
	 * Constructor.
	 */
	public EMFMetaModel(EMFMetaModelConverter converter, EPackage ePackage, EMFTechnologyAdapter adapter) {
		this.adapter = adapter;
		this.ePackage = ePackage;
		this.converter = converter;

	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getName()
	 */
	@Override
	public String getName() {
		return ePackage.getName();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyObject#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) throws Exception {
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getURI()
	 */
	@Override
	public String getURI() {
		return EMFMetaModelURIBuilder.getUri(ePackage);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getVersion()
	 */
	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getDescription()
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
	 * Getter of converter.
	 * 
	 * @return the converter value
	 */
	public EMFMetaModelConverter getConverter() {
		return converter;
	}

	@Override
	public EMFMetaModelResource getResource() {
		return metaModelResource;
	}

	@Override
	public void setResource(org.openflexo.foundation.resource.FlexoResource<EMFMetaModel> resource) {
		metaModelResource = (EMFMetaModelResource) resource;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.technologyadapter.FlexoMetaModel#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return true;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.technologyadapter.FlexoMetaModel#setIsReadOnly(boolean)
	 */
	@Override
	public void setIsReadOnly(boolean b) {
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getRootConcept()
	 */
	@Override
	public IFlexoOntologyClass<EMFTechnologyAdapter> getRootConcept() {
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getImportedOntologies()
	 */
	@Override
	public List<IFlexoOntology<EMFTechnologyAdapter>> getImportedOntologies() {
		return Collections.emptyList();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getSubContainers()
	 */
	@Override
	public List<EMFPackageContainer> getSubContainers() {
		return Collections.singletonList(converter.convertPackage(this, ePackage));
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.technologyadapter.FlexoMetaModel#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(String objectURI) {
		return getOntologyObject(objectURI);
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
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getDeclaredOntologyObject(java.lang.String)
	 */
	@Override
	public IFlexoOntologyConcept<EMFTechnologyAdapter> getDeclaredOntologyObject(String objectURI) {
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getAnnotations()
	 */
	@Override
	public List<IFlexoOntologyAnnotation> getAnnotations() {
		List<IFlexoOntologyAnnotation> annotations = null;
		if (ePackage.getEAnnotations() != null && ePackage.getEAnnotations().size() != 0) {
			annotations = new ArrayList<>();
			for (EAnnotation annotation : ePackage.getEAnnotations()) {
				annotations.add(this.getConverter().convertAnnotation(this, annotation));
			}
		}
		else {
			annotations = Collections.emptyList();
		}
		return annotations;
	}

	/**
	 * 
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getDataTypes()
	 */
	@Override
	public List<IFlexoOntologyDataType<EMFTechnologyAdapter>> getDataTypes() {
		List<IFlexoOntologyDataType<EMFTechnologyAdapter>> result = new ArrayList<>();
		for (IFlexoOntologyDataType<EMFTechnologyAdapter> dataType : converter.getDataTypes().values()) {
			result.add(dataType);
		}
		return Collections.unmodifiableList(result);
	}

	/**
	 * 
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getConcepts()
	 */
	@Override
	public List<IFlexoOntologyConcept<EMFTechnologyAdapter>> getConcepts() {
		List<IFlexoOntologyConcept<EMFTechnologyAdapter>> result = new ArrayList<>();
		result.addAll(getClasses());
		result.addAll(getIndividuals());
		result.addAll(getDataProperties());
		result.addAll(getObjectProperties());
		return Collections.unmodifiableList(result);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getClasses()
	 */
	@Override
	public List<? extends IFlexoOntologyClass<EMFTechnologyAdapter>> getClasses() {
		List<IFlexoOntologyClass<EMFTechnologyAdapter>> result = new ArrayList<>();
		for (EMFClassClass aClass : converter.getClasses().values()) {
			result.add(aClass);
		}
		for (EMFEnumClass aClass : converter.getEnums().values()) {
			result.add(aClass);
		}
		return Collections.unmodifiableList(result);
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
			if (result != null)
				break;
		}
		return result;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getDeclaredClass(java.lang.String)
	 */
	@Override
	public IFlexoOntologyClass<EMFTechnologyAdapter> getDeclaredClass(String classURI) {
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getAccessibleClasses()
	 */
	@Override
	public List<? extends IFlexoOntologyClass<EMFTechnologyAdapter>> getAccessibleClasses() {
		return getClasses();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getIndividuals()
	 */
	@Override
	public List<IFlexoOntologyIndividual<EMFTechnologyAdapter>> getIndividuals() {
		List<IFlexoOntologyIndividual<EMFTechnologyAdapter>> result = new ArrayList<>();
		for (IFlexoOntologyIndividual<EMFTechnologyAdapter> individual : converter.getEnumLiterals().values()) {
			result.add(individual);
		}
		return Collections.unmodifiableList(result);
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
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getDeclaredIndividual(java.lang.String)
	 */
	@Override
	public IFlexoOntologyIndividual<EMFTechnologyAdapter> getDeclaredIndividual(String individualURI) {
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getAccessibleIndividuals()
	 */
	@Override
	public List<? extends IFlexoOntologyIndividual<EMFTechnologyAdapter>> getAccessibleIndividuals() {
		return getIndividuals();
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
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getDeclaredProperty(java.lang.String)
	 */
	@Override
	public IFlexoOntologyStructuralProperty<EMFTechnologyAdapter> getDeclaredProperty(String objectURI) {
		return getProperty(objectURI);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getDataProperties()
	 */
	@Override
	public List<IFlexoOntologyDataProperty<EMFTechnologyAdapter>> getDataProperties() {
		List<IFlexoOntologyDataProperty<EMFTechnologyAdapter>> result = new ArrayList<>();
		for (IFlexoOntologyDataProperty<EMFTechnologyAdapter> dataProperty : converter.getDataAttributes().values()) {
			result.add(dataProperty);
		}
		return Collections.unmodifiableList(result);
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
				if (result != null)
					break;
			}
		}
		return result;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getDeclaredDataProperty(java.lang.String)
	 */
	@Override
	public IFlexoOntologyDataProperty<EMFTechnologyAdapter> getDeclaredDataProperty(String propertyURI) {
		return getDataProperty(propertyURI);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getAccessibleDataProperties()
	 */
	@Override
	public List<? extends IFlexoOntologyDataProperty<EMFTechnologyAdapter>> getAccessibleDataProperties() {
		return getDataProperties();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntologyConceptContainer#getObjectProperties()
	 */
	@Override
	public List<? extends IFlexoOntologyObjectProperty<EMFTechnologyAdapter>> getObjectProperties() {
		List<IFlexoOntologyObjectProperty<EMFTechnologyAdapter>> result = new ArrayList<>();
		for (IFlexoOntologyObjectProperty<EMFTechnologyAdapter> objectProperty : converter.getObjectAttributes().values()) {
			result.add(objectProperty);
		}
		for (IFlexoOntologyObjectProperty<EMFTechnologyAdapter> objectProperty : converter.getReferences().values()) {
			result.add(objectProperty);
		}
		return Collections.unmodifiableList(result);
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
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getAccessibleObjectProperties()
	 */
	@Override
	public List<? extends IFlexoOntologyObjectProperty<EMFTechnologyAdapter>> getAccessibleObjectProperties() {
		return getObjectProperties();
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.IFlexoOntology#getDeclaredObjectProperty(java.lang.String)
	 */
	@Override
	public IFlexoOntologyObjectProperty<EMFTechnologyAdapter> getDeclaredObjectProperty(String propertyURI) {
		return getObjectProperty(propertyURI);
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.technologyadapter.FlexoMetaModel#getTechnologyAdapter()
	 */
	@Override
	public EMFTechnologyAdapter getTechnologyAdapter() {
		return adapter;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.ontology.FlexoOntologyObjectImpl#getFlexoOntology()
	 */
	@Override
	public IFlexoOntology<EMFTechnologyAdapter> getFlexoOntology() {
		return this;
	}

	@Override
	public String toString() {
		return "EMFMetaModel:" + getURI();
	}
}
