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

package org.openflexo.technologyadapter.xml.model;

import java.lang.reflect.Type;

import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Initializer;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Parameter;
import org.openflexo.model.annotations.Setter;
import org.openflexo.xml.IXMLAttribute;


/**
 * 
 * Represents an XML Attribute in an XMLModel
 * 
 * @author xtof
 * 
 */
@ModelEntity
@ImplementationClass(XMLAttributeImpl.class)
public interface XMLAttribute  extends IXMLAttribute, XMLObject {

	public static String VALUE = "value";
	public static final String TYPE = "myType";

	@Initializer
	public XMLAttribute init(@Parameter(NAME) String s, @Parameter(TYPE) Type t);
	
	@Getter(VALUE)
	public String getValue();

	@Setter(VALUE)
	public void setValue(String o);

	@Getter(TYPE)
	public XMLType getType();
	
	@Setter(TYPE)
	public void setType(Type aType);
	
	
}
