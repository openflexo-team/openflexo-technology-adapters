/*
 * (c) Copyright 2010-2011 AgileBirds
 * (c) Copyright 2013-2014 Openflexo
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
import org.openflexo.model.annotations.Initializer;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Parameter;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.xml.metamodel.XMLObject;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;

/**
 * Implementation of a Property value in XSD/XML technology
 * 
 * @author sylvain, xtof
 */
@ModelEntity
public abstract interface XMLPropertyValue  extends XMLObject {

	final String PROPERTY = "property";

	@Initializer
	public void XMLPropertyValue(@Parameter(PROPERTY) XMLProperty prop);
	
	@Getter(PROPERTY)
	public XMLProperty getProperty();
	
	@Setter(PROPERTY)
	public void setProperty(XMLProperty prop);
	
	public String getStringValue();

	@Override
	public boolean equals(Object obj);

}
