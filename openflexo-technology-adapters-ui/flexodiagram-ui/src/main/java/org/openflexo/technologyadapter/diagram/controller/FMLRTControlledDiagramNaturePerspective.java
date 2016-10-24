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

package org.openflexo.technologyadapter.diagram.controller;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramModuleView;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramViewNature;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FMLRTNaturePerspective;
import org.openflexo.view.controller.FlexoController;

/**
 * A perspective representing all the resources interpretable by a {@link FMLRTTechnologyAdapter} according to FML-controlled diagram
 * perspectives:<br>
 * <ul>
 * <li>{@link FMLControlledDiagramViewNature}</li>
 * <li>{@link FMLControlledDiagramVirtualModelInstanceNature}</li>
 * <li>{@link FMLControlledDiagramFlexoConceptInstanceNature}</li>
 * </ul>
 * 
 * @author sylvain
 * 
 * @param <TA>
 */
public class FMLRTControlledDiagramNaturePerspective extends FMLRTNaturePerspective {

	static final Logger logger = Logger.getLogger(FMLRTControlledDiagramNaturePerspective.class.getPackage().getName());

	public FMLRTControlledDiagramNaturePerspective(FlexoController controller) {
		super(FMLControlledDiagramViewNature.INSTANCE, FMLControlledDiagramVirtualModelInstanceNature.INSTANCE, null,
				controller.getFMLRTTechnologyAdapter(), controller.getTechnologyAdapter(DiagramTechnologyAdapter.class), controller);
	}

	/**
	 * Return the technology adapter handling specific natures
	 * 
	 * @return
	 */
	@Override
	public DiagramTechnologyAdapter getHandlingTechnologyAdapter() {
		return (DiagramTechnologyAdapter) super.getHandlingTechnologyAdapter();
	}

	@Override
	public DiagramTechnologyAdapterController getHandlingTechnologyAdapterController() {
		return (DiagramTechnologyAdapterController) super.getHandlingTechnologyAdapterController();
	}

	@Override
	protected ModuleView<View> createModuleViewForView(View view) {
		return null;
	}

	@Override
	protected ModuleView<VirtualModelInstance> createModuleViewForVirtualModelInstance(VirtualModelInstance vmInstance) {
		FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor(vmInstance, false, getController(),
				getHandlingTechnologyAdapterController().getToolFactory());
		return new FMLControlledDiagramModuleView(editor, this);
	}

	@Override
	protected ModuleView<FlexoConceptInstance> createModuleViewForFlexoConceptInstance(FlexoConceptInstance flexoConceptInstance) {
		return null;
	}

}
