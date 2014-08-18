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

package org.openflexo.technologyadapter.xml.metamodel;

import java.lang.reflect.Type;

import org.openflexo.technologyadapter.xml.model.XMLIndividual;


/**
 * 
 * Represents an XML Attribute in an XMLModel
 * 
 * @author xtof
 * 
 */
public abstract class XMLAttributeImpl implements XMLAttribute {

	// Properties
	private XMLIndividual container;
	private Type aType;

	public XMLAttributeImpl() {
		super();
	}


	@Override
	public boolean isSimpleAttribute() {
		return true;
	}

	@Override
	public boolean isElement() {
		return false;
	}

	@Override
	public void addValue(IXMLIndividual<?, ?> indiv, Object value) {
		setValue((String) value);

	}

	@Override
	public Type getAttributeType() {
		return getType();
	}


	public String getDisplayableDescription() {
		return this.getName();
	}

}
