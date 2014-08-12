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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.xml.IXMLAttribute;
import org.openflexo.xml.IXMLIndividual;
import org.openflexo.xml.XMLCst;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * an XMLIndividual represents a single instance of XML Element in a XMLModel
 * 
 * 
 * @author xtof
 * 
 */

public abstract class XMLIndividualImpl implements XMLIndividual {


	private static final java.util.logging.Logger  logger = org.openflexo.logging.FlexoLogger.getLogger(XMLIndividualImpl.class
			.getPackage().getName());

	/* Properties */

	private Map<XMLType, Set<XMLIndividualImpl>>  children  = null;
	private Map<String, XMLAttribute> attributes   = null;
	private final String uuid;
	
	/**
	 * Default Constructor
	 * 
	 * @param adapter
	 */
	public XMLIndividualImpl() {
		super();
		uuid = UUID.randomUUID().toString();
		attributes = new HashMap<String, XMLAttribute>();
		children = new HashMap<XMLType, Set<XMLIndividualImpl>>();
	}
	
	@Override
	public String getUUID(){
		return uuid;
	}

	/* (non-Javadoc)
	 * @see org.openflexo.technologyadapter.xml.model.IXMLIndividual#getContentDATA()
	 */
	@Override
	public String getContentDATA() {
		XMLAttribute attr = attributes.get(XMLCst.CDATA_ATTR_NAME);
		if (attr != null) {
			return attr.getValue();
		}
		return "";
	}

	// ************ Accessors

	/* (non-Javadoc)
	 * @see org.openflexo.technologyadapter.xml.model.IXMLIndividual#getTechnologyAdapter()
	 */
	public XMLTechnologyAdapter getTechnologyAdapter() {
		return getContainerModel().getTechnologyAdapter();
	}

	@Override
	public String getName(){
		return getType().getName();
	}

	@Override
	public String getFullyQualifiedName() {
		// TODO Auto-generated method stub
		return getName();
	}

	/* (non-Javadoc)
	 * @see org.openflexo.technologyadapter.xml.model.IXMLIndividual#getAttributeValue(java.lang.String)
	 */
	@Override
	public Object getAttributeValue(String attributeName) {

		XMLAttribute attr =  attributes.get(attributeName);

		if (attr != null) {
			return attr.getValue();
		}
		else
			return null;
	}

	@Override
	public void removeChild(XMLIndividual indiv){
		children.get(indiv.getType()).remove(indiv);
	}

	@Override
	public void addChild(XMLIndividual anIndividual) {
		XMLType aType = anIndividual.getType();
		Set<XMLIndividualImpl> typedSet = children.get(aType);

		if (typedSet == null) {
			typedSet = new HashSet<XMLIndividualImpl>();
			children.put(aType, typedSet);
		}
		typedSet.add((XMLIndividualImpl) anIndividual);
		((XMLIndividualImpl) anIndividual).setParent(this);
	}

	@Override
	public List<XMLIndividual> getChildren() {

		List<XMLIndividual> returned = new ArrayList<XMLIndividual>();

		for (Set<XMLIndividualImpl> s : children.values()) {
			returned.addAll(s);
		}
		return returned;
	}

	/* (non-Javadoc)
	 * @see org.openflexo.technologyadapter.xml.model.IXMLIndividual#getAttributes()
	 */
	@Override
	public Collection<? extends XMLAttribute> getAttributes() {
		return attributes.values();
	}

	@Override
	public Object createAttribute(String attrLName, Type aType, String value) {
		XMLAttribute attr = XMLModelImpl.getModelFactory().newInstance(XMLAttribute.class, attrLName, aType);
		attr.setValue(value);

		if (attributes == null) {
			logger.warning("Attribute collection is null");
			attributes = new HashMap<String, XMLAttribute>();
		}

		attributes.put(attrLName, attr);

		return attr;
	}

	@Override
	public XMLAttribute getAttributeByName(String aName) {
		return attributes.get(aName);
	}

	
	/* (non-Javadoc)
	 * @see org.openflexo.technologyadapter.xml.model.IXMLIndividual#toXML(org.w3c.dom.Document)
	 */
	@Override
	public Element toXML(Document doc) {
		String nsURI = getType().getURI();
		Element element = null;
		if (nsURI != null) {
			element = doc.createElementNS(nsURI, getType().getFullyQualifiedName());
		}
		else {
			element = doc.createElement(getType().getName());
		}

		for (IXMLIndividual<XMLIndividual, XMLAttribute> i : getChildren()) {
			element.appendChild(i.toXML(doc));
		}

		// TODO dump attributes !!!

		return element;
	}

	@Override
	public String getAttributeStringValue(IXMLAttribute a) {
		return ((XMLAttributeImpl) a).getValue().toString();
	}

}
