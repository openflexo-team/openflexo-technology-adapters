/*
 * (c) Copyright 2010-2011 AgileBirds
 * (c) Copyright 2014 - Openflexo
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

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of an Object Property values in XSD/XML technology.<br>
 * Value is an instance of {@link XSOntIndividual}
 * 
 * @author sylvain
 */
public abstract class XMLObjectPropertyValueImpl  implements XMLObjectPropertyValue {

	private List<XMLIndividual> values = null;

	XMLObjectPropertyValueImpl(){
		values = new ArrayList<XMLIndividual>();
	}
	
	@Override
	public List<XMLIndividual> getValues() {
		return values;
	}

	@Override
	public void addToValues(XMLIndividual value) {
		values.add(value);
	}

	@Override
	public void removeFromValues(XMLIndividual value) {
		values.remove(value);
	}
}
