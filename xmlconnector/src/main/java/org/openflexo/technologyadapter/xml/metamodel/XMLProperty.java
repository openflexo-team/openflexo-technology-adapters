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

import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Initializer;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Parameter;
import org.openflexo.model.annotations.Setter;


/**
 * 
 * Represents an XML Attribute in an XMLModel
 * 
 * @author xtof
 * 
 */
@ModelEntity
@ImplementationClass(XMLPropertyImpl.class)
public interface XMLProperty  extends XMLObject {

	/**
	 * The Type of the given attribute. This might be a simple type
	 */
	public static final String TYPE = "myType";
	/**
	 * XMLType containing the given attribute
	 */
	public static final String CONTAINER = "container";
	/**
	 * This indicates if property was created from an XML element or attribute
	 */

	public static final String IS_FROM_ELEMENT = "isFromElement";
	
	@Initializer
	public XMLProperty init(@Parameter(NAME) String s, @Parameter(TYPE) Type t);
	
	@Getter(CONTAINER)
	public XMLType getContainer();

	@Setter(CONTAINER)
	public void setContainer(XMLType containedIn);
	
	@Getter(value = TYPE, ignoreType=true)
	public Type getType();
	
	@Setter(TYPE)
	public void setType(Type aType);
	
	/**
	 * Returns true if this property was created from an XML element and false if from an XMLAttribute
	 * @return
	 */
	@Getter(value = IS_FROM_ELEMENT, defaultValue = "false")
	public boolean isFromXMLElement();

	@Setter(IS_FROM_ELEMENT)
	public void setIsFromElement(boolean fromElement);
	
}
