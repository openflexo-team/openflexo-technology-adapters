/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Csvconnector, a component of the software infrastructure 
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
import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyStructuralProperty;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.technologyadapter.FlexoModel;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;
import org.openflexo.technologyadapter.csv.rm.CSVModelResource;

public class CSVModel extends FlexoOntologyObjectImpl<CSVTechnologyAdapter>
		implements FlexoModel<CSVModel, CSVMetaModel>, IFlexoOntology<CSVTechnologyAdapter> {
	private CSVModelResource csvResource;

	public CSVModel(String uri, CSVTechnologyAdapter technologyAdapter) {
	}

	@Override
	public FlexoResource<CSVModel> getResource() {
		return this.csvResource;
	}

	@Override
	public void setResource(FlexoResource<CSVModel> resource) {
		this.csvResource = ((CSVModelResource) resource);
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
	public void setName(String name) {
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
	public CSVMetaModel getMetaModel() {
		return null;
	}

	@Override
	public String getURI() {
		return null;
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

	public void loadWhenUnloaded() {
	}

	@Override
	public String getDescription() {
		return null;
	}

}
