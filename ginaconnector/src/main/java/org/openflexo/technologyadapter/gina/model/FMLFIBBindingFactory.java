/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Gina-core, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.gina.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.Bindable;
import org.openflexo.connie.binding.IBindingPathElement;
import org.openflexo.connie.binding.SimplePathElement;
import org.openflexo.foundation.fml.FMLBindingFactory;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.FIBVariable;
import org.openflexo.gina.model.FIBViewType;
import org.openflexo.gina.model.bindings.FIBVariablePathElement;

/**
 * Extends {@link FMLBindingFactory} by providing access to GINA model
 * 
 * @author sylvain
 *
 */
public class FMLFIBBindingFactory extends FMLBindingFactory {

	static final Logger logger = Logger.getLogger(FMLFIBBindingFactory.class.getPackage().getName());

	public FMLFIBBindingFactory(VirtualModel virtualModel) {
		super(virtualModel);
	}

	@Override
	protected SimplePathElement<?> makeSimplePathElement(Object object, IBindingPathElement parent, Bindable bindable) {
		if (object instanceof FIBVariable) {
			return new FIBVariablePathElement(parent, (FIBVariable<?>) object, bindable);
		}
		return super.makeSimplePathElement(object, parent, bindable);
	}

	@Override
	public List<? extends SimplePathElement<?>> getAccessibleSimplePathElements(IBindingPathElement parent, Bindable bindable) {

		if (parent != null) {
			Type pType = parent.getType();

			if (pType instanceof FIBViewType) {
				List<SimplePathElement<?>> returned = new ArrayList<>();
				FIBComponent concept = ((FIBViewType<?>) pType).getFIBComponent();

				if (concept != null) {
					for (FIBVariable<?> variable : concept.getVariables()) {
						returned.add(getSimplePathElement(variable, parent, bindable));
					}
				}

				returned.addAll(((FIBViewType<?>) pType).getAccessibleSimplePathElements(parent, bindable));

				return returned;
			}

			// In all other cases, consider it using FML/Java rules
			return super.getAccessibleSimplePathElements(parent, bindable);
		}
		else {
			logger.warning("Trying to find accessible path elements for a NULL parent");
			return Collections.EMPTY_LIST;
		}
	}

}
