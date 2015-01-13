/*
 * (c) Copyright 2013-2014 Openflexo
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.rm.XMLFileResource;
import org.openflexo.xml.XMLCst;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author xtof
 * 
 */

public abstract class XMLModelImpl extends FlexoObjectImpl implements XMLModel {

	// Constants

	private static final String Version = "0";

	// Attributes

	protected static final Logger logger = Logger.getLogger(XMLModelImpl.class.getPackage().getName());
	private FlexoResource<?> xmlResource;
	private final boolean isReadOnly = true;

	private final Map<String, XMLIndividual> individuals;

	private final List<String> namespace = new ArrayList<String>();

	private static ModelFactory MF;

	static {
		try {
			MF = new ModelFactory(ModelContextLibrary.getCompoundModelContext(XMLModel.class, XMLIndividual.class, XMLPropertyValue.class,
					XMLDataPropertyValue.class, XMLObjectPropertyValue.class));
		} catch (ModelDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ModelFactory getModelFactory() {
		return MF;
	}

	public XMLModelImpl() {
		super();
		individuals = new HashMap<String, XMLIndividual>();
	}

	@Override
	public String getName() {
		if (xmlResource != null) {
			return xmlResource.getName();
		} else
			return "";
	}

	@Override
	public void setNamespace(String uri, String prefix) {
		namespace.add(XMLModel.NSPREFIX_INDEX, prefix);
		namespace.add(XMLModel.NSURI_INDEX, uri);
	}

	@Override
	public List<String> getNamespace() {

		if (namespace.size() < 2 && this.getMetaModel() != null) {
			namespace.add(XMLModel.NSPREFIX_INDEX, XMLCst.DEFAULT_NS);
			namespace.add(XMLModel.NSURI_INDEX, this.getMetaModel().getURI());
		}

		return namespace;

	}

	@Override
	public XMLFileResource getResource() {
		return (XMLFileResource) xmlResource;
	}

	@Override
	public void setResource(FlexoResource<XMLModel> resource) {
		this.xmlResource = resource;
	}

	@Override
	public String getURI() {
		return xmlResource.getURI();
	}

	@Override
	public Object getObject(String objectURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends XMLIndividual> getIndividuals() {
		return new ArrayList<XMLIndividual>(individuals.values());
	}

	// TODO, TO BE OPTIMIZED
	@Override
	public List<XMLIndividual> getIndividualsOfType(XMLType aType) {
		ArrayList<XMLIndividual> returned = new ArrayList<XMLIndividual>();
		for (XMLIndividual o : individuals.values()) {
			if (o.getType() == aType) {
				returned.add(o);
			}
		}
		return returned;
	}

	@Override
	public Object addNewIndividual(Type aType) {
		XMLIndividual anIndividual = getModelFactory().newInstance(XMLIndividual.class, this, aType);
		// XMLIndividual anIndividual = new XMLIndividual(this, (XMLType) aType);
		this.addIndividual(anIndividual);
		return anIndividual;
	}

	@Override
	public void addIndividual(XMLIndividual anIndividual) {
		individuals.put(anIndividual.getUUID(), anIndividual);
	}

	public Document toXML() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		XMLIndividual rootIndiv = getRoot();

		if (rootIndiv != null) {
			Element rootNode = rootIndiv.toXML(doc);
			doc.appendChild(rootNode);
		}

		return doc;
	}

	@Override
	public XMLTechnologyAdapter getTechnologyAdapter() {
		XMLFileResource rsc = this.getResource();
		if (rsc != null)
			return rsc.getTechnologyAdapter();
		return null;
	}

}
