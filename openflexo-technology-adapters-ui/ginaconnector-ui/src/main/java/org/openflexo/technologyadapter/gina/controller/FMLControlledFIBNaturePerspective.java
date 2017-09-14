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

package org.openflexo.technologyadapter.gina.controller;

import java.util.logging.Logger;

import org.openflexo.fml.controller.view.StandardFlexoConceptView;
import org.openflexo.fml.controller.view.VirtualModelView;
import org.openflexo.fml.rt.controller.view.VirtualModelInstanceView;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBFlexoConceptInstanceNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBFlexoConceptNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelNature;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBVirtualModelInstanceModuleView;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBVirtualModelModuleView;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.SpecificNaturePerspective;

/**
 * A perspective representing all the resources interpretable by {@link GINATechnologyAdapter} according to FML-controlled fib
 * perspectives:<br>
 * <ul>
 * <li>{@link FMLControlledFIBVirtualModelNature}</li>
 * <li>{@link FMLControlledFIBFlexoConceptNature}</li>
 * <li>{@link FMLControlledFIBVirtualModelInstanceNature}</li>
 * <li>{@link FMLControlledFIBFlexoConceptInstanceNature}</li>
 * </ul>
 * 
 * @author sylvain
 * 
 */
public class FMLControlledFIBNaturePerspective extends SpecificNaturePerspective<GINATechnologyAdapter> {

	static final Logger logger = Logger.getLogger(FMLControlledFIBNaturePerspective.class.getPackage().getName());

	public FMLControlledFIBNaturePerspective(FlexoController controller) {
		super(controller.getTechnologyAdapter(GINATechnologyAdapter.class), FMLControlledFIBVirtualModelNature.INSTANCE,
				FMLControlledFIBFlexoConceptNature.INSTANCE, FMLControlledFIBVirtualModelInstanceNature.INSTANCE,
				FMLControlledFIBFlexoConceptInstanceNature.INSTANCE, controller);
	}

	@Override
	public String getName() {
		return "GUI_perspective";
	}

	@Override
	public GINAAdapterController getTechnologyAdapterController() {
		return (GINAAdapterController) super.getTechnologyAdapterController();
	}

	@Override
	public FMLControlledFIBVirtualModelNature getVirtualModelNature() {
		return (FMLControlledFIBVirtualModelNature) super.getVirtualModelNature();
	}

	@Override
	protected ModuleView<VirtualModel> createModuleViewForVirtualModel(VirtualModel virtualModel) {

		if (virtualModel.hasNature(getVirtualModelNature())) {
			return new FMLControlledFIBVirtualModelModuleView(virtualModel, getController(), this);
		}
		return new VirtualModelView(virtualModel, getController(), this);
	}

	@Override
	protected ModuleView<FlexoConcept> createModuleViewForFlexoConcept(FlexoConcept flexoConcept) {
		return new StandardFlexoConceptView(flexoConcept, getController(), this);
	}

	@Override
	protected ModuleView<? extends VirtualModelInstance<?, ?>> createModuleViewForVirtualModelInstance(
			FMLRTVirtualModelInstance vmInstance) {
		if (vmInstance.hasNature(getVirtualModelInstanceNature())) {
			return new FMLControlledFIBVirtualModelInstanceModuleView(vmInstance, getController(), this,
					getController().getTechnologyAdapter(GINATechnologyAdapter.class).getLocales());
		}
		return new VirtualModelInstanceView(vmInstance, getController(), this);
	}

	@Override
	protected ModuleView<FlexoConceptInstance> createModuleViewForFlexoConceptInstance(FlexoConceptInstance flexoConceptInstance) {
		return null;
	}

}
