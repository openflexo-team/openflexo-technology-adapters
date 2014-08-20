/*
 * (c) Copyright 2012-2014 Openflexo
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
package org.openflexo.technologyadapter.xml.metamodel;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.xml.XMLCst;


public abstract class XMLComplexTypeImpl  implements XMLComplexType {

	private static final Logger logger = Logger.getLogger(XMLComplexTypeImpl.class.getPackage().getName());


	/* Properties */
	Map<String, XMLProperty>    properties;

	public XMLComplexTypeImpl() {
		super();
		this.properties = new HashMap<String, XMLProperty>();
	}

	@Override
	public Collection<? extends XMLProperty> getProperties() {
	
		return properties.values();
	}

	@Override
	public XMLProperty createProperty(String name, Type aType) {
		XMLProperty prop = null;

		if (!hasProperty(name)){
			if (aType != null){
				if (aType instanceof XMLComplexType){
					prop = XMLMetaModelImpl.getModelFactory().newInstance( XMLObjectProperty.class, name, aType);
				}
				else if (aType instanceof XMLSimpleType) {
					prop = XMLMetaModelImpl.getModelFactory().newInstance( XMLDataProperty.class, name, aType);
				}
				else{
					logger.warning("UNABLE to create a new property named ["+ name+"] as it does not map to any known type: " + aType.toString());
				}
			}
			else
			{
				logger.warning("UNABLE to create a new property named ["+ name+"]  with a NULL type ");
			}
			if (prop != null) addProperty(prop);
		}
		return prop;
	}

	@Override
	public void addProperty(XMLProperty prop){
		if (prop != null) properties.put(prop.getName(), prop );
	}

	@Override
	public Boolean hasProperty(String name) {
		return properties.containsKey(name);
	}

	@Override
	public XMLProperty getPropertyByName(String name) {
		
		XMLProperty prop = properties.get(name);
		// Looks for the property in super-Type
		if (this.getSuperType() != null){
			prop = ((XMLComplexType) this.getSuperType()).getPropertyByName(name);
		}
		// Creates the property for PCDATA
		if (prop == null && name.equals(XMLCst.CDATA_ATTR_NAME)) {
			prop = createProperty( name, this.getMetaModel().getTypeFromURI(XMLMetaModel.STR_SIMPLETYPE_URI));
		}
				
		return prop;
	}
	

}
