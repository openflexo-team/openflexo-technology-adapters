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

import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;

@ModelEntity
// @ImplementationClass(XMLTypeImpl.class)
public interface XMLSimpleType  extends XMLType {

	final String BASICTYPE = "basicType";
	/*
	 * Property that indicates that this particular simpleType is extracted from an Element or an Attribute
	 */
	final String MAPSTOELEMENT = "mapsToElement";
	
	@Getter(value = BASICTYPE, ignoreType = true)
	public Type getBasicType();
	
	@Setter(value = BASICTYPE)
	public void setBasicType(Type aType);
	
	@Getter(value = MAPSTOELEMENT, defaultValue = "false")
	public boolean mapsToElement();
	
	@Setter(value = MAPSTOELEMENT)
	public void setMapsToElement(boolean val);
	
	
}
