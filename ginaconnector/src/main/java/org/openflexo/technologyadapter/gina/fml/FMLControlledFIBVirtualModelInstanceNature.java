/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.gina.fml;

import java.awt.image.BufferedImage;

import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceNature;
import org.openflexo.foundation.nature.ScreenshotableNature;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;

/**
 * Define the "FML-controlled FIBComponent" nature of a {@link VirtualModelInstance}<br>
 * 
 * We intend here to interpret a single model slot of {@link FIBComponentModelSlot} as the component to represent in
 * {@link VirtualModelInstance} tooling
 * 
 * A {@link FMLControlledFIBVirtualModelInstanceNature} might be seen as an interpretation of a given {@link VirtualModelInstance}
 * 
 * @author sylvain
 * 
 */
public class FMLControlledFIBVirtualModelInstanceNature implements VirtualModelInstanceNature, ScreenshotableNature<VirtualModelInstance> {

	public static FMLControlledFIBVirtualModelInstanceNature INSTANCE = new FMLControlledFIBVirtualModelInstanceNature();

	// Prevent external instantiation
	private FMLControlledFIBVirtualModelInstanceNature() {
	}

	/**
	 * Return boolean indicating if supplied {@link VirtualModelInstance} might be interpreted as a FML-controlled FIBComponent
	 */
	@Override
	public boolean hasNature(VirtualModelInstance virtualModelInstance) {

		// The corresponding VirtualModel should have FMLControlledDiagramVirtualModelNature
		if (!virtualModelInstance.getVirtualModel().hasNature(FMLControlledFIBVirtualModelNature.INSTANCE)) {
			return false;
		}

		FIBComponentModelSlot fibMS = virtualModelInstance.getVirtualModel().getModelSlots(FIBComponentModelSlot.class).get(0);

		FreeModelSlotInstance<GINAFIBComponent, FIBComponentModelSlot> msInstance = (FreeModelSlotInstance<GINAFIBComponent, FIBComponentModelSlot>) virtualModelInstance
				.getModelSlotInstance(fibMS);

		if (msInstance == null) {
			return false;
		}

		/*if (msInstance.getAccessedResourceData() == null) {
			return false;
		}*/

		return true;
	}

	public static FreeModelSlotInstance<GINAFIBComponent, FIBComponentModelSlot> getModelSlotInstance(
			VirtualModelInstance virtualModelInstance) {
		return INSTANCE._getModelSlotInstance(virtualModelInstance);

	}

	public static GINAFIBComponent getGINAFIBComponent(VirtualModelInstance virtualModelInstance) {
		return INSTANCE._getGINAFIBComponent(virtualModelInstance);
	}

	private FreeModelSlotInstance<GINAFIBComponent, FIBComponentModelSlot> _getModelSlotInstance(
			VirtualModelInstance virtualModelInstance) {
		FIBComponentModelSlot fibMS = virtualModelInstance.getVirtualModel().getModelSlots(FIBComponentModelSlot.class).get(0);

		return (FreeModelSlotInstance<GINAFIBComponent, FIBComponentModelSlot>) virtualModelInstance.getModelSlotInstance(fibMS);

	}

	private GINAFIBComponent _getGINAFIBComponent(VirtualModelInstance virtualModelInstance) {
		return _getModelSlotInstance(virtualModelInstance).getAccessedResourceData();
	}

	@Override
	public BufferedImage getScreenshot(VirtualModelInstance object) {
		System.out.println("Please perform the screenshot here !!!!!!!!!");
		return null;
	}
}
