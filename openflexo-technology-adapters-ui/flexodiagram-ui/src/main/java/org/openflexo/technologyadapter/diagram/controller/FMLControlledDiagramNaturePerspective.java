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

import org.openflexo.fml.controller.view.StandardFlexoConceptView;
import org.openflexo.fml.controller.view.VirtualModelView;
import org.openflexo.fml.rt.controller.view.VirtualModelInstanceView;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramModuleView;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramFlexoConceptInstanceNature;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramFlexoConceptNature;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.gui.view.DiagramFlexoConceptView;
import org.openflexo.technologyadapter.diagram.gui.view.FMLControlledDiagramVirtualModelView;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.SpecificNaturePerspective;

/**
 * A perspective representing all the resources interpretable by {@link DiagramTechnologyAdapter} according to FML-controlled diagram
 * perspectives:<br>
 * <ul>
 * <li>{@link FMLControlledDiagramVirtualModelNature}</li>
 * <li>{@link FMLControlledDiagramFlexoConceptNature}</li>
 * <li>{@link FMLControlledDiagramVirtualModelInstanceNature}</li>
 * <li>{@link FMLControlledDiagramFlexoConceptInstanceNature}</li>
 * </ul>
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramNaturePerspective extends SpecificNaturePerspective<DiagramTechnologyAdapter> {

	static final Logger logger = Logger.getLogger(FMLControlledDiagramNaturePerspective.class.getPackage().getName());

	public FMLControlledDiagramNaturePerspective(FlexoController controller) {
		super(controller.getTechnologyAdapter(DiagramTechnologyAdapter.class), FMLControlledDiagramVirtualModelNature.INSTANCE,
				FMLControlledDiagramFlexoConceptNature.INSTANCE, FMLControlledDiagramVirtualModelInstanceNature.INSTANCE,
				FMLControlledDiagramFlexoConceptInstanceNature.INSTANCE, controller);
	}

	@Override
	public String getName() {
		return "diagram_perspective";
	}

	@Override
	public DiagramTechnologyAdapterController getTechnologyAdapterController() {
		return (DiagramTechnologyAdapterController) super.getTechnologyAdapterController();
	}

	@Override
	protected ModuleView<VirtualModel> createModuleViewForVirtualModel(VirtualModel virtualModel) {
		if (virtualModel.hasNature(getVirtualModelNature())) {
			return new FMLControlledDiagramVirtualModelView(virtualModel, getController(), this);
		}
		return new VirtualModelView(virtualModel, getController(), this);
	}

	@Override
	protected ModuleView<FlexoConcept> createModuleViewForFlexoConcept(FlexoConcept flexoConcept) {
		if (flexoConcept.hasNature(getFlexoConceptNature())) {
			return new DiagramFlexoConceptView(flexoConcept, getController(), this);
		}
		return new StandardFlexoConceptView(flexoConcept, getController(), this);
	}

	@Override
	protected ModuleView<? extends VirtualModelInstance<?, ?>> createModuleViewForVirtualModelInstance(
			FMLRTVirtualModelInstance vmInstance) {
		if (vmInstance.hasNature(getVirtualModelInstanceNature())) {
			FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor(vmInstance, false, getController(),
					getTechnologyAdapterController().getToolFactory());
			return new FMLControlledDiagramModuleView(editor, this);
		}
		return new VirtualModelInstanceView(vmInstance, getController(), this);
	}

	@Override
	protected ModuleView<FlexoConceptInstance> createModuleViewForFlexoConceptInstance(FlexoConceptInstance flexoConceptInstance) {
		return null;
	}

}
