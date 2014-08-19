/*
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
package org.openflexo.technologyadapter.xml.metamodel;

import java.lang.reflect.Type;
import java.util.Collection;

import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Finder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Remover;

@ModelEntity
@ImplementationClass(XMLComplexTypeImpl.class)
public interface XMLComplexType  extends XMLType {
	
	final String PROPERTIES = "properties";

	@Override
	@Getter(value = PROPERTIES, cardinality = Cardinality.LIST)
	@Embedded
	public Collection<? extends XMLProperty> getProperties();

	@Finder(attribute = XMLProperty.URI, collection = PROPERTIES, isMultiValued = true)
    public XMLProperty getPropertyByName(String name);

    public XMLProperty createProperty(String name, Type t);
    
	public Boolean hasProperty(String name);
	
	@Adder(PROPERTIES)
	public void addProperty(XMLProperty anAttribute);

	@Remover(PROPERTIES)
	public void removeProperty(XMLProperty anAttribute);
	
	
	
}
