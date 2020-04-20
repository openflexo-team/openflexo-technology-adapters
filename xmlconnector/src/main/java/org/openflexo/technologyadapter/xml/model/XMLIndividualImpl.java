/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.xml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLObjectProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
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

public abstract class XMLIndividualImpl extends FlexoObjectImpl implements XMLIndividual {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
			.getLogger(XMLIndividualImpl.class.getPackage().getName());

	/* Properties */

	private Map<XMLComplexType, Set<XMLIndividualImpl>> children = null;
	private Map<XMLProperty, XMLPropertyValue> propertiesValues = null;
	private final String uuid;

	/**
	 * Default Constructor
	 * 
	 * @param adapter
	 */
	public XMLIndividualImpl() {
		super();
		uuid = UUID.randomUUID().toString();
		propertiesValues = new HashMap<>();
		children = new HashMap<>();
	}

	@Override
	public String getUUID() {
		return uuid;
	}

	@Override
	public String getContentDATA() {
		XMLProperty attr = this.getType().getPropertyByName(XMLCst.CDATA_ATTR_NAME);
		if (attr != null) {
			return this.getPropertyStringValue(attr);
		}
		return "";
	}

	@Override
	public void setContentDATA(String value) {
		XMLProperty attr = this.getType().getPropertyByName(XMLCst.CDATA_ATTR_NAME);
		if (attr != null) {
			addPropertyValue(XMLCst.CDATA_ATTR_NAME, value);
		}
	}

	@Override
	public String getName() {
		return getType().getName();
	}

	@Override
	public void removeChild(XMLIndividual indiv) {
		children.get(indiv.getType()).remove(indiv);
	}

	@Override
	public void addChild(XMLIndividual anIndividual) {
		XMLComplexType aType = anIndividual.getType();
		Set<XMLIndividualImpl> typedSet = children.get(aType);

		if (typedSet == null) {
			typedSet = new HashSet<>();
			children.put(aType, typedSet);
		}
		typedSet.add((XMLIndividualImpl) anIndividual);
		((XMLIndividualImpl) anIndividual).setParent(this);
	}

	@Override
	public List<XMLIndividual> getChildren() {

		List<XMLIndividual> returned = new ArrayList<>();

		for (Set<XMLIndividualImpl> s : children.values()) {
			returned.addAll(s);
		}
		return returned;
	}

	@Override
	public String getPropertyStringValue(XMLProperty prop) {
		XMLPropertyValue pv = propertiesValues.get(prop);
		if (pv != null) {
			return propertiesValues.get(prop).getStringValue();
		}
		return "";
	}

	@Override
	public List<? extends XMLPropertyValue> getPropertiesValues() {
		return new ArrayList<XMLPropertyValue>(propertiesValues.values());
	}

	@Override
	public XMLPropertyValue getPropertyValue(String attributeName) {

		XMLProperty attr = getType().getPropertyByName(attributeName);

		if (attr != null) {
			return propertiesValues.get(attr);
		}
		return null;
	}

	@Override
	public XMLPropertyValue getPropertyValue(XMLProperty prop) {

		if (prop != null) {
			return propertiesValues.get(prop);
		}
		return null;

	}

	@Override
	public void addPropertyValue(XMLProperty attr, XMLPropertyValue value) {
		// TODO
	}

	@Override
	public void deletePropertyValues(XMLProperty attr) {
		// TODO
	}

	@Override
	public void addPropertyValue(String name, Object value) {

		XMLProperty prop = getType().getPropertyByName(name);

		if (prop == null) {
			XMLMetaModel mm = getContainerModel().getMetaModel();
			if (!mm.isReadOnly()) {
				// TODO Manage complex types and actual types for objects.
				prop = this.getType().createProperty(name, mm.getTypeFromURI(XMLMetaModel.STR_SIMPLETYPE_URI));
			}
			else {
				logger.warning("CANNOT give a value  for a non existant attribute :" + name);
			}
		}
		if (prop != null) {
			XMLPropertyValue vals = propertiesValues.get(prop);

			if (vals == null) {

				if (prop instanceof XMLDataProperty) {
					vals = XMLModelImpl.getModelFactory().newInstance(XMLDataPropertyValue.class, prop);
					((XMLDataPropertyValue) vals).setValue(value);
					propertiesValues.put(prop, vals);
				}
				else {
					// TODO..... complex attributes, collections
				}
			}

			else {
				// TODO..... manage this case also
			}
		}

	}

	@Override
	public void addPropertyValue(XMLProperty prop, Object value) {
		XMLPropertyValue val = propertiesValues.get(prop);

		if (val == null) {

			if (prop instanceof XMLDataProperty) {
				val = XMLModelImpl.getModelFactory().newInstance(XMLDataPropertyValue.class, prop);
				((XMLDataPropertyValue) val).setValue(value);
				propertiesValues.put(prop, val);
			}
			else if (prop instanceof XMLObjectProperty) {

				val = XMLModelImpl.getModelFactory().newInstance(XMLObjectPropertyValue.class, prop);
				((XMLObjectPropertyValue) val).addToValues((XMLIndividual) value);
				propertiesValues.put(prop, val);
			}
		}

		if (val != null) {
			if (prop instanceof XMLDataProperty) {
				((XMLDataPropertyValue) val).setValue(value);
			}
			else if (prop instanceof XMLObjectProperty) {
				((XMLObjectPropertyValue) val).addToValues((XMLIndividual) value);
			}
		}

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

		for (XMLIndividual i : getChildren()) {
			element.appendChild(i.toXML(doc));
		}

		// TODO dump attributes !!!

		return element;
	}

	@Override
	public XMLTechnologyAdapter getTechnologyAdapter() {
		return this.getContainerModel().getTechnologyAdapter();
	}

	@Override
	public String getDisplayableDescription() {
		return "XML Individual of type: " + getName();

	}

}
