/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexo-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.freeplane.controller;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.fml.FMLControlledFreeplaneVirtualModelInstanceNature;
import org.openflexo.technologyadapter.freeplane.listeners.FreeplaneListenersInitilizer;
import org.openflexo.technologyadapter.freeplane.view.FMLControlledFreeplaneModuleView;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FMLRTNaturePerspective;
import org.openflexo.view.controller.FlexoController;

/**
 * A perspective representing all the resources interpretable by a {@link FMLRTTechnologyAdapter} according to FML-controlled freeplane
 * perspectives:
 * 
 * @author sylvain
 * 
 */
public class FMLControlledFreeplaneNaturePerspective extends FMLRTNaturePerspective {

	static final Logger logger = Logger.getLogger(FMLControlledFreeplaneNaturePerspective.class.getPackage().getName());

	public FMLControlledFreeplaneNaturePerspective(FlexoController controller) {
		super(FMLControlledFreeplaneVirtualModelInstanceNature.INSTANCE, null, controller.getFMLRTTechnologyAdapter(),
				controller.getTechnologyAdapter(FreeplaneTechnologyAdapter.class), controller);
	}

	/**
	 * Return the technology adapter handling specific natures
	 * 
	 * @return
	 */
	@Override
	public FreeplaneTechnologyAdapter getHandlingTechnologyAdapter() {
		return (FreeplaneTechnologyAdapter) super.getHandlingTechnologyAdapter();
	}

	@Override
	public FreeplaneAdapterController getHandlingTechnologyAdapterController() {
		return (FreeplaneAdapterController) super.getHandlingTechnologyAdapterController();
	}

	@Override
	protected ModuleView<FMLRTVirtualModelInstance> createModuleViewForVirtualModelInstance(VirtualModelInstance<?, ?> vmInstance) {
		if (vmInstance instanceof FMLRTVirtualModelInstance) {
			FreeplaneListenersInitilizer.init((FMLRTVirtualModelInstance) vmInstance, getController());
			return new FMLControlledFreeplaneModuleView(getController(), (FMLRTVirtualModelInstance) vmInstance, this);
		}
		return null;
	}

	@Override
	protected ModuleView<FlexoConceptInstance> createModuleViewForFlexoConceptInstance(FlexoConceptInstance flexoConceptInstance) {
		return null;
	}

}
