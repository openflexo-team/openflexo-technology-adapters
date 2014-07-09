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

import org.openflexo.foundation.DefaultFlexoObject;
import org.openflexo.foundation.ontology.IFlexoOntologyPropertyValue;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.metamodel.XSOntProperty;

/**
 * Implementation of a Property value in XSD/XML technology
 * 
 * @author sylvain
 */
// TODO replace with full PAMELA implementation
public abstract class XSPropertyValue extends DefaultFlexoObject implements IFlexoOntologyPropertyValue<XMLTechnologyAdapter> {

	@Override
	public abstract XSOntProperty getProperty();

	@Override
	public XMLTechnologyAdapter getTechnologyAdapter() {
		return getProperty().getTechnologyAdapter();
	}
}
