/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Powerpointconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.powerpoint.fml.binding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.Bindable;
import org.openflexo.connie.BindingFactory;
import org.openflexo.connie.binding.FunctionPathElement;
import org.openflexo.connie.binding.IBindingPathElement;
import org.openflexo.connie.binding.SimplePathElement;
import org.openflexo.foundation.fml.TechnologySpecificType;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlide;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;

/**
 * This class represent the {@link BindingFactory} dedicated to handle Powerpoint technology-specific binding elements
 * 
 * @author sylvain, vincent
 * 
 */
public final class PowerpointBindingFactory extends TechnologyAdapterBindingFactory {
	static final Logger logger = Logger.getLogger(PowerpointBindingFactory.class.getPackage().getName());

	public PowerpointBindingFactory() {
		super();
	}

	@Override
	protected SimplePathElement<?> makeSimplePathElement(Object object, IBindingPathElement parent, Bindable bindable) {
		logger.warning("Unexpected " + object);
		return null;
	}

	@Override
	public boolean handleType(TechnologySpecificType<?> technologySpecificType) {
		if (technologySpecificType instanceof PowerpointSlideshow) {
			return true;
		}
		if (technologySpecificType instanceof PowerpointSlide) {
			return true;
		}
		return false;
	}

	@Override
	public List<? extends SimplePathElement<?>> getAccessibleSimplePathElements(IBindingPathElement parent, Bindable bindable) {
		List<SimplePathElement<?>> returned = new ArrayList<>();
		if (parent instanceof PowerpointSlideshow) {
			for (PowerpointSlide sheet : ((PowerpointSlideshow) parent).getPowerpointSlides()) {
				returned.add(getSimplePathElement(sheet, parent, bindable));
			}
		}
		return returned;
	}

	@Override
	public List<? extends FunctionPathElement<?>> getAccessibleFunctionPathElements(IBindingPathElement parent, Bindable bindable) {
		// TODO
		return Collections.emptyList();
	}

}
