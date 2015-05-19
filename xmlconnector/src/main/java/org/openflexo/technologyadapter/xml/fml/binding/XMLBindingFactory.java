/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.xml.fml.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.BindingFactory;
import org.openflexo.connie.binding.BindingPathElement;
import org.openflexo.connie.binding.FunctionPathElement;
import org.openflexo.connie.binding.SimplePathElement;
import org.openflexo.foundation.fml.TechnologySpecificType;
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
	public boolean handleType(TechnologySpecificType technologySpecificType) {
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
