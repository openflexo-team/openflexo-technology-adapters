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

import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;

/**
 * 
 * Represents an XML Attribute in an XMLModel
 * 
 * @author xtof
 * 
 */
public abstract class XMLPropertyImpl extends FlexoObjectImpl implements XMLProperty {

	public XMLPropertyImpl() {
		super();
	}

	@Override
	public int compareTo(Object arg0) {
		if (arg0 instanceof XMLProperty) {
			return this.getName().compareTo(((XMLProperty) arg0).getName());
		} else
			return -1;
	}

	@Override
	public XMLTechnologyAdapter getTechnologyAdapter() {
		return this.getContainer().getTechnologyAdapter();
	}

	@Override
	public String getDisplayableDescription() {
		if (this instanceof XMLDataProperty) {
			return "XML Simple property named : " + this.getName();
		} else if (this instanceof XMLObjectProperty) {
			return "XML Object Property named : " + this.getName();
		} else
			return "(Unknown)";
	}

}
