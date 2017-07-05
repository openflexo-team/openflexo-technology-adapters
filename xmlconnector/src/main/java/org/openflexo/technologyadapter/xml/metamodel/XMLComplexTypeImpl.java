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

package org.openflexo.technologyadapter.xml.metamodel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.xml.XMLCst;

public abstract class XMLComplexTypeImpl extends XMLTypeImpl implements XMLComplexType {

	private static final Logger logger = Logger.getLogger(XMLComplexTypeImpl.class.getPackage().getName());

	/* Properties */
	Map<String, XMLProperty> properties;

	public XMLComplexTypeImpl() {
		super();
		this.properties = new HashMap<String, XMLProperty>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<? extends XMLProperty> getProperties() {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		ArrayList lst = new ArrayList(properties.values());
		addSuperPropertiesToPropertyList(lst);
		Collections.sort(lst);
		return lst;
	}

	private void addSuperPropertiesToPropertyList(List<XMLProperty> list) {
		XMLType t = getSuperType();
		if (t != null && t instanceof XMLComplexType) {
			for (XMLProperty p : ((XMLComplexType) t).getProperties()) {
				list.add(p);
			}
			((XMLComplexTypeImpl) t).addSuperPropertiesToPropertyList(list);
		}
	}

	@Override
	public XMLProperty createProperty(String name, Type aType) {
		XMLProperty prop = null;

		if (!hasProperty(name)) {
			if (aType != null) {
				if (aType instanceof XMLComplexType) {
					prop = XMLMetaModelImpl.getModelFactory().newInstance(XMLObjectProperty.class, name, aType, this);
				}
				else if (aType instanceof XMLSimpleType) {
					prop = XMLMetaModelImpl.getModelFactory().newInstance(XMLDataProperty.class, name, aType, this);
				}
				else if (aType.equals(String.class)) {
					prop = XMLMetaModelImpl.getModelFactory().newInstance(XMLDataProperty.class, name, aType, this);
				}
				else {
					logger.warning("UNABLE to create a new property named [" + name + "] as it does not map to any known type: "
							+ aType.toString());
				}
			}
			else {
				logger.warning("UNABLE to create a new property named [" + name + "]  with a NULL type ");
			}
			if (prop != null)
				addProperty(prop);
		}
		return prop;
	}

	@Override
	public void addProperty(XMLProperty prop) {
		if (prop != null)
			properties.put(prop.getName(), prop);
	}

	@Override
	public Boolean hasProperty(String name) {
		return properties.containsKey(name);
	}

	@Override
	public XMLProperty getPropertyByName(String name) {
		if (name != null) {
			XMLProperty prop = properties.get(name);
			// Looks for the property in super-Type
			if (this.getSuperType() != null) {
				prop = ((XMLComplexType) this.getSuperType()).getPropertyByName(name);
			}
			// Creates the property for PCDATA
			if (prop == null && name.equals(XMLCst.CDATA_ATTR_NAME)) {
				prop = createProperty(name, this.getMetamodel().getTypeFromURI(XMLMetaModel.STR_SIMPLETYPE_URI));
			}
			return prop;
		}
		return null;
	}

}
