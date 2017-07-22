/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Freeplane, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.freeplane.fml;

import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceNature;
import org.openflexo.technologyadapter.freeplane.FreeplaneModelSlot;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

public class FMLControlledFreeplaneVirtualModelInstanceNature implements VirtualModelInstanceNature {

	public static final FMLControlledFreeplaneVirtualModelInstanceNature INSTANCE = new FMLControlledFreeplaneVirtualModelInstanceNature();

	@Override
	public boolean hasNature(final FMLRTVirtualModelInstance concept) {
		if (concept != null && concept.getVirtualModel() != null) {
			return concept.getVirtualModel().hasNature(FMLControlledFreeplaneVirtualModelNature.INSTANCE);
		}
		return false;
	}

	public static IFreeplaneMap getMap(final FMLRTVirtualModelInstance vmi) {
		return INSTANCE.getModelSlotInstance(vmi).getAccessedResourceData();
	}

	private FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot> getModelSlotInstance(final FMLRTVirtualModelInstance vmi) {
		final FreeplaneModelSlot modelSlot = vmi.getVirtualModel().getModelSlots(FreeplaneModelSlot.class).get(0);
		return (FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot>) vmi.getModelSlotInstance(modelSlot);
	}

}
