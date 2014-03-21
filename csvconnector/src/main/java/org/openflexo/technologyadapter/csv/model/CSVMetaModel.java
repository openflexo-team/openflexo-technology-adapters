/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2013 Openflexo
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

import java.util.List;

import org.openflexo.foundation.ontology.FlexoOntologyObjectImpl;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.ontology.IFlexoOntologyAnnotation;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyConcept;
import org.openflexo.foundation.ontology.IFlexoOntologyContainer;
import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyDataType;
import org.openflexo.foundation.ontology.IFlexoOntologyIndividual;
import org.openflexo.foundation.ontology.IFlexoOntologyMetaModel;
import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;

public class CSVMetaModel extends FlexoOntologyObjectImpl implements FlexoMetaModel<CSVMetaModel>,
		IFlexoOntologyMetaModel<CSVTechnologyAdapter> {
	@Override
	public FlexoResource<CSVMetaModel> getResource() {
		return null;
	}

	@Override
	public void setResource(FlexoResource<CSVMetaModel> resource) {
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public List<? extends IFlexoOntology<CSVTechnologyAdapter>> getImportedOntologies() {
		return null;
	}

	@Override
	public List<? extends IFlexoOntologyAnnotation> getAnnotations() {
		return null;
	}

	@Override
	public List<? extends IFlexoOntologyClass<CSVTechnologyAdapter>> getAccessibleClasses() {
		return null;
	}

	@Override
	public List<? extends IFlexoOntologyIndividual<CSVTechnologyAdapter>> getAccessibleIndividuals() {
		return null;
	}

	@Override
	public List<? extends IFlexoOntologyObjectProperty<CSVTechnologyAdapter>> getAccessibleObjectProperties() {
		return null;
	}

	@Override
	public List<? extends IFlexoOntologyDataProperty<CSVTechnologyAdapter>> getAccessibleDataProperties() {
		return null;
	}

	@Override
	public IFlexoOntologyConcept<CSVTechnologyAdapter> getDeclaredOntologyObject(String objectURI) {
		return null;
	}

	@Override
	public IFlexoOntologyClass<CSVTechnologyAdapter> getDeclaredClass(String classURI) {
		return null;
	}

	@Override
	public IFlexoOntologyIndividual<CSVTechnologyAdapter> getDeclaredIndividual(String individualURI) {
		return null;
	}

	@Override
	public IFlexoOntologyObjectProperty<CSVTechnologyAdapter> getDeclaredObjectProperty(String propertyURI) {
		return null;
	}

	@Override
	public IFlexoOntologyDataProperty<CSVTechnologyAdapter> getDeclaredDataProperty(String propertyURI) {
		return null;
	}

	@Override
	public IFlexoOntologyStructuralProperty<CSVTechnologyAdapter> getDeclaredProperty(String objectURI) {
		return null;
	}

	@Override
	public IFlexoOntologyClass<CSVTechnologyAdapter> getRootConcept() {
		return null;
	}

	@Override
	public void setName(String name) throws Exception {
	}

	@Override
	public List<? extends IFlexoOntologyContainer<CSVTechnologyAdapter>> getSubContainers() {
		return null;
	}

	@Override
	public List<? extends IFlexoOntologyConcept<CSVTechnologyAdapter>> getConcepts() {
		return null;
	}

	@Override
	public List<? extends IFlexoOntologyDataType<CSVTechnologyAdapter>> getDataTypes() {
		return null;
	}

	@Override
	public IFlexoOntologyConcept<CSVTechnologyAdapter> getOntologyObject(String objectURI) {
		return null;
	}

	@Override
	public IFlexoOntologyClass<CSVTechnologyAdapter> getClass(String classURI) {
		return null;
	}

	@Override
	public IFlexoOntologyIndividual<CSVTechnologyAdapter> getIndividual(String individualURI) {
		return null;
	}

	@Override
	public IFlexoOntologyObjectProperty<CSVTechnologyAdapter> getObjectProperty(String propertyURI) {
		return null;
	}

	@Override
	public IFlexoOntologyDataProperty<CSVTechnologyAdapter> getDataProperty(String propertyURI) {
		return null;
	}

	@Override
	public IFlexoOntologyStructuralProperty<CSVTechnologyAdapter> getProperty(String objectURI) {
		return null;
	}

	@Override
	public List<? extends IFlexoOntologyClass<CSVTechnologyAdapter>> getClasses() {
		return null;
	}

	@Override
	public List<? extends IFlexoOntologyIndividual<CSVTechnologyAdapter>> getIndividuals() {
		return null;
	}

	@Override
	public List<? extends IFlexoOntologyDataProperty<CSVTechnologyAdapter>> getDataProperties() {
		return null;
	}

	@Override
	public List<? extends IFlexoOntologyObjectProperty<CSVTechnologyAdapter>> getObjectProperties() {
		return null;
	}

	@Override
	public String getURI() {
		return null;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public void setIsReadOnly(boolean b) {
	}

	@Override
	public Object getObject(String objectURI) {
		return null;
	}

	@Override
	public CSVTechnologyAdapter getTechnologyAdapter() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public IFlexoOntology<CSVTechnologyAdapter> getFlexoOntology() {
		return null;
	}

	@Override
	public String getDisplayableDescription() {
		return null;
	}

	public String getFullyQualifiedName() {
		return null;
	}
}
