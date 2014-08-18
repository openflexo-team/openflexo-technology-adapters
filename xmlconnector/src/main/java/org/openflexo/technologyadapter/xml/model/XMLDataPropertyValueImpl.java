/*
 * 
 * (c) Copyright 2010-2011 AgileBirds
 * (c) Copyright 2014- Openflexo
 * 
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


/**
 * Implementation of an Data Property values in XSD/XML technology.<br>
 * 
 * @author sylvain, xtof
 */
public abstract class XMLDataPropertyValueImpl  implements XMLDataPropertyValue {


	@Override
	public boolean equals(Object obj) {
		// One Single Value per DataProperty in XML
		return getValue().equals(obj);

	}

	@Override
	public String toString() {

		return getValue().toString();
	}

	@Override
	public String getStringValue(){
		// TODO manage this better.
		return getValue().toString();
	}

}
