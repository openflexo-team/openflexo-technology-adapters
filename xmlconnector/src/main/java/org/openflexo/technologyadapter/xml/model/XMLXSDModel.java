/*
 * (c) Copyright 2010-2012 AgileBirds
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
package org.openflexo.technologyadapter.xml.model;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openflexo.foundation.ontology.IFlexoOntologyDataProperty;
import org.openflexo.foundation.ontology.IFlexoOntologyMetaModel;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.FlexoModel;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XSOntClass;
import org.openflexo.technologyadapter.xml.metamodel.XSOntProperty;
import org.openflexo.xml.IXMLIndividual;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class XMLXSDModel extends XSOntology implements FlexoModel<XMLXSDModel, XMLMetaModel>, IXMLModel,
		TechnologyObject<XMLTechnologyAdapter> {

	private XMLMetaModel metaModel = null;
	private XSOntIndividual rootElem = null;

	protected static final Logger logger = Logger.getLogger(XMLXSDModel.class.getPackage().getName());

	public XMLXSDModel(String ontologyURI, File xmlFile, XMLTechnologyAdapter adapter) {
		super(ontologyURI, xmlFile, adapter);
		uri = xmlFile.toURI().toString();
	}

	public XMLXSDModel(String ontologyURI, File xmlFile, XMLTechnologyAdapter adapter, XMLMetaModel mm) {
		super(ontologyURI, xmlFile, adapter);
		metaModel = mm;
		uri = xmlFile.toURI().toString();
	}

	public void setMetaModel(XMLMetaModel metaModelData) {
		this.metaModel = metaModelData;
		setChanged();
	}

	@Override
	public void setNamespace(String uri, String prefix) {
		// does nothing as Namespace comes from MetaModel
		return;

	}

	@Override
	public String getNamespacePrefix() {
		// TODO Should default to something
		return null;
	}

	@Override
	public String getNamespaceURI() {
		return this.getMetaModel().getURI();
	}

	@Override
	public XMLMetaModel getMetaModel() {
		return metaModel;
	}

	@Override
	public List<IFlexoOntologyMetaModel<XMLTechnologyAdapter>> getMetaModels() {
		List<IFlexoOntologyMetaModel<XMLTechnologyAdapter>> list = new ArrayList<IFlexoOntologyMetaModel<XMLTechnologyAdapter>>();
		list.add((IFlexoOntologyMetaModel<XMLTechnologyAdapter>) metaModel);
		return list;
	}

	@Override
	public Object addNewIndividual(Type aType) {

		XSOntIndividual indiv = this.createOntologyIndividual((XSOntClass) aType);
		individuals.put(indiv.getUUID(), indiv);
		setChanged();
		return indiv;

	}

	@Override
	public void setRoot(IXMLIndividual<?, ?> anIndividual) {
		rootElem = (XSOntIndividual) anIndividual;
		setChanged();

	}

	/**
	 * @return the rootNode
	 */
	@Override
	public IXMLIndividual<XSOntIndividual, XSOntProperty> getRoot() {
		return rootElem;
	}

	public void save() throws SaveResourceException {
		getResource().save(null);
	}

	@Override
	public List<? extends IFlexoOntologyDataProperty<XMLTechnologyAdapter>> getAccessibleDataProperties() {
		// Those should only be available for MetaModels
		return Collections.emptyList();
	}

	@Override
	public IFlexoOntologyDataProperty getDataProperty(String propertyURI) {
		// TODO, this is not that clean!
		logger.info("This should not happen: properties belong to the MetaModel");
		return this.getMetaModel().getDataProperty(propertyURI);
	}

	@Override
	public List<? extends IFlexoOntologyDataProperty<XMLTechnologyAdapter>> getDataProperties() {
		// Those should only be available for MetaModels
		return Collections.emptyList();
	}

	@Override
	public Document toXML() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		XSOntIndividual rootIndiv = (XSOntIndividual) getRoot();

		if (rootIndiv != null) {
			Element rootNode = rootIndiv.toXML(doc);
			doc.appendChild(rootNode);
		}
		return doc;
	}

}
