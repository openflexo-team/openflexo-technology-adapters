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
package org.openflexo.technologyadapter.xml.fml.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.antar.binding.BindingFactory;
import org.openflexo.antar.binding.BindingPathElement;
import org.openflexo.antar.binding.FunctionPathElement;
import org.openflexo.antar.binding.SimplePathElement;
import org.openflexo.foundation.fml.TechnologySpecificCustomType;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.technologyadapter.xml.metamodel.XMLDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLObject;
import org.openflexo.technologyadapter.xml.metamodel.XMLObjectPropertyImpl;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;

/**
 * This class represent the {@link BindingFactory} dedicated to handle XSD technology-specific binding elements
 * 
 * @author sylvain, christophe
 * 
 */

public final class XMLBindingFactory extends TechnologyAdapterBindingFactory {
	static final Logger logger = Logger.getLogger(XMLBindingFactory.class.getPackage().getName());

	public XMLBindingFactory() {
		super();
	}

	@Override
	protected SimplePathElement makeSimplePathElement(Object object, BindingPathElement parent) {
		if (object instanceof XMLDataProperty) {
			XMLDataProperty attr = (XMLDataProperty) object;

			return new XMLDataPropertyPathElement(parent, attr);
		}
		else if (object instanceof XMLObjectPropertyImpl) {
			XMLObjectPropertyImpl attr = (XMLObjectPropertyImpl) object;
			return new XMLObjectPropertyPathElement(parent, attr);

		}
		logger.warning("Unexpected " + object);
		return null;
	}

	@Override
	public boolean handleType(TechnologySpecificCustomType technologySpecificType) {
		if (technologySpecificType instanceof XMLObject) {
			return true;
		}
		return false;
	}

	@Override
	public List<? extends SimplePathElement> getAccessibleSimplePathElements(BindingPathElement parent) {

		List<SimplePathElement> returned = new ArrayList<SimplePathElement>();

		if (parent instanceof XMLIndividual) {
			for (XMLProperty attr : ((XMLIndividual) parent).getType().getProperties()) {
				returned.add(getSimplePathElement(attr, parent));
			}
		}

		returned.addAll(super.getAccessibleSimplePathElements(parent));

		return returned;
	}

	@Override
	public List<? extends FunctionPathElement> getAccessibleFunctionPathElements(BindingPathElement parent) {
		return super.getAccessibleFunctionPathElements(parent);
	}

}
