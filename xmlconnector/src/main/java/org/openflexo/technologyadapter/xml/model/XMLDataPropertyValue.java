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

import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;

/**
 * Implementation of an Data Property values in XSD/XML technology.<br>
 * 
 * @author sylvain, xtof
 */
@ModelEntity
@ImplementationClass(XMLDataPropertyValueImpl.class)
public interface XMLDataPropertyValue extends XMLPropertyValue  {

	final String VALUE = "value";

	@Getter(value = VALUE, ignoreType = true)
	public Object getValue();
	
	@Setter(VALUE)
	public void setValue(Object value);
	
}
