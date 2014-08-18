/*
 * (c) Copyright 2010-2012 AgileBirds
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


public abstract class XMLTypeImpl  implements XMLType {


	/* Properties */
	Map<String, XMLProperty>    properties;

	public XMLTypeImpl() {
		super();
		this.properties = new HashMap<String, XMLProperty>();
	}

	@Override
	public String getFullyQualifiedName() {
		if (getURI() != null && !getURI().isEmpty())
			return getURI();
		else
			return getName();
	}


	@Override
	public Collection<? extends XMLProperty> getProperties() {

		return properties.values();
	}

	@Override
	public XMLProperty createProperty(String name, Type t) {
		XMLProperty prop = null;
		if (!hasProperty(name)){
			if (t != null){
				if (t instanceof XMLType){
					prop = XMLMetaModelImpl.getModelFactory().newInstance( XMLObjectProperty.class, name, t);
				}
				else {
					prop = XMLMetaModelImpl.getModelFactory().newInstance( XMLDataProperty.class, name, t);
				}

			}
			else
			{
				prop = XMLMetaModelImpl.getModelFactory().newInstance( XMLProperty.class, name, String.class);
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
		XMLProperty attr = properties.get(name);
		if (attr == null && name.equals(NAME_ATTR)) {
			attr = createProperty( name, String.class);
		}
		return attr;
	}

}
