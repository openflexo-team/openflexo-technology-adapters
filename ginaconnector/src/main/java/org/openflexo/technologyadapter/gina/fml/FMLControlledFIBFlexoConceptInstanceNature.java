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
import java.util.logging.Logger;

import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstanceNature;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.nature.ScreenshotableNature;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;

/**
 * Define the "FML-controlled FIBComponent" nature of a {@link FlexoConceptInstance}<br>
 * 
 * We intend here to interpret a single model slot of {@link FIBComponentModelSlot} as the component to represent in
 * {@link FlexoConceptInstance} tooling
 * 
 * A {@link FMLControlledFIBFlexoConceptInstanceNature} might be seen as an interpretation of a given {@link FlexoConceptInstance}
 * 
 * @author sylvain
 * 
 */
public class FMLControlledFIBFlexoConceptInstanceNature implements FlexoConceptInstanceNature, ScreenshotableNature<FlexoConceptInstance> {

	static final Logger logger = Logger.getLogger(FMLControlledFIBFlexoConceptInstanceNature.class.getPackage().getName());

	public static FMLControlledFIBFlexoConceptInstanceNature INSTANCE = new FMLControlledFIBFlexoConceptInstanceNature();

	// Prevent external instantiation
	private FMLControlledFIBFlexoConceptInstanceNature() {
	}

	/**
	 * Return boolean indicating if supplied {@link FMLRTVirtualModelInstance} might be interpreted as a FML-controlled FIBComponent
	 */
	@Override
	public boolean hasNature(FlexoConceptInstance flexoConceptInstance) {

		// The corresponding VirtualModel should have FMLControlledDiagramVirtualModelNature
		if (!flexoConceptInstance.getFlexoConcept().hasNature(FMLControlledFIBFlexoConceptNature.INSTANCE)) {
			return false;
		}

		FIBComponentModelSlot fibMS = flexoConceptInstance.getFlexoConcept().getDeclaredProperties(FIBComponentModelSlot.class).get(0);

		GINAFIBComponent fibComponent = flexoConceptInstance.getFlexoPropertyValue(fibMS);

		if (fibComponent == null) {
			return false;
		}

		return true;
	}

	public static GINAFIBComponent getGINAFIBComponent(FlexoConceptInstance flexoConceptInstance) {
		return INSTANCE._getGINAFIBComponent(flexoConceptInstance);
	}

	private GINAFIBComponent _getGINAFIBComponent(FlexoConceptInstance flexoConceptInstance) {
		FIBComponentModelSlot fibMS = flexoConceptInstance.getFlexoConcept().getModelSlots(FIBComponentModelSlot.class).get(0);
		return flexoConceptInstance.getFlexoPropertyValue(fibMS);

	}

	@Override
	public BufferedImage getScreenshot(FlexoConceptInstance object) {
		System.out.println("Please perform the screenshot here !!!!!!!!!");
		return null;
	}
}
