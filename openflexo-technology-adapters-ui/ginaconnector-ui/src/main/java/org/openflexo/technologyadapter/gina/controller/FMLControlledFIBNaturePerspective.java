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

import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBViewNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBViewPointNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelNature;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBVirtualModelModuleView;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FMLNaturePerspective;
import org.openflexo.view.controller.FlexoController;

/**
 * A perspective representing all the resources interpretable by a {@link FMLRTTechnologyAdapter} according to FML-controlled diagram
 * perspectives:<br>
 * <ul>
 * <li>{@link FMLControlledFIBViewNature}</li>
 * <li>{@link FMLControlledFIBVirtualModelInstanceNature}</li>
 * <li>{@link FMLControlledFIBFlexoConceptInstanceNature}</li>
 * </ul>
 * 
 * @author sylvain
 * 
 * @param <TA>
 */
public class FMLControlledFIBNaturePerspective extends FMLNaturePerspective {

	static final Logger logger = Logger.getLogger(FMLControlledFIBNaturePerspective.class.getPackage().getName());

	public FMLControlledFIBNaturePerspective(FlexoController controller) {
		super(FMLControlledFIBViewPointNature.INSTANCE, FMLControlledFIBVirtualModelNature.INSTANCE, null,
				controller.getFMLTechnologyAdapter(), controller.getTechnologyAdapter(GINATechnologyAdapter.class), controller);
	}

	/**
	 * Return the technology adapter handling specific natures
	 * 
	 * @return
	 */
	@Override
	public GINATechnologyAdapter getHandlingTechnologyAdapter() {
		return (GINATechnologyAdapter) super.getHandlingTechnologyAdapter();
	}

	@Override
	public GINAAdapterController getHandlingTechnologyAdapterController() {
		return (GINAAdapterController) super.getHandlingTechnologyAdapterController();
	}

	@Override
	public FMLControlledFIBViewPointNature getViewpointNature() {
		return (FMLControlledFIBViewPointNature) super.getViewpointNature();
	}

	@Override
	public FMLControlledFIBVirtualModelNature getVirtualModelNature() {
		return (FMLControlledFIBVirtualModelNature) super.getVirtualModelNature();
	}

	@Override
	protected ModuleView<ViewPoint> createModuleViewForViewPoint(ViewPoint viewPoint) {
		// return new ViewPointView(viewPoint, getController(), this);
		return new EmptyPanel<ViewPoint>(getController(), this, viewPoint);
	}

	@Override
	protected ModuleView<VirtualModel> createModuleViewForVirtualModel(VirtualModel virtualModel) {
		if (virtualModel.hasNature(getVirtualModelNature())) {
			return new FMLControlledFIBVirtualModelModuleView(virtualModel, getController(), this);
		}
		return new EmptyPanel<VirtualModel>(getController(), this, virtualModel);
	}

	@Override
	protected ModuleView<FlexoConcept> createModuleViewForFlexoConcept(FlexoConcept flexoConcept) {
		// return new DiagramFlexoConceptView(flexoConcept, getController(), this);
		return new EmptyPanel<FlexoConcept>(getController(), this, flexoConcept);
	}

}
