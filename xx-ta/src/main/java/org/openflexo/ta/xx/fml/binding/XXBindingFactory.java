/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
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

package org.openflexo.ta.xx.fml.binding;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.Bindable;
import org.openflexo.connie.binding.FunctionPathElement;
import org.openflexo.connie.binding.IBindingPathElement;
import org.openflexo.connie.binding.SimplePathElement;
import org.openflexo.foundation.fml.TechnologySpecificType;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;

/**
 * Defines XX-specific types binding path element strategy<br>
 * 
 * This technology actually defines no types
 * 
 * @author sylvain
 *
 */
public final class XXBindingFactory extends TechnologyAdapterBindingFactory {
	static final Logger logger = Logger.getLogger(XXBindingFactory.class.getPackage().getName());

	@Override
	protected SimplePathElement makeSimplePathElement(Object object, IBindingPathElement parent, Bindable bindable) {
		logger.warning("Unexpected " + object);
		return null;
	}

	@Override
	public boolean handleType(TechnologySpecificType<?> technologySpecificType) {
		return false;
	}

	// The methods below give entry points for customization
	@Override
	public List<? extends SimplePathElement<?>> getAccessibleSimplePathElements(IBindingPathElement element, Bindable bindable) {
		return super.getAccessibleSimplePathElements(element, bindable);
	}

	@Override
	public List<? extends FunctionPathElement<?>> getAccessibleFunctionPathElements(IBindingPathElement parent, Bindable bindable) {
		return super.getAccessibleFunctionPathElements(parent, bindable);
	}

	@Override
	public SimplePathElement<?> makeSimplePathElement(IBindingPathElement parent, String propertyName, Bindable bindable) {
		// We want to avoid code duplication, so iterate on all accessible simple path element and choose the right one
		for (SimplePathElement<?> e : getAccessibleSimplePathElements(parent, bindable)) {
			if (e.getLabel().equals(propertyName)) {
				return e;
			}
		}
		return super.makeSimplePathElement(parent, propertyName, bindable);
	}

}
