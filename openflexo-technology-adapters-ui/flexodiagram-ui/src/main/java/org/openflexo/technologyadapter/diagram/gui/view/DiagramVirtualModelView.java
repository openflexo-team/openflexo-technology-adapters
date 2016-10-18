/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.gui.view;

import org.openflexo.fml.controller.view.VirtualModelView;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterControllerService;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * This is the module view representing a {@link VirtualModel} which has the FML-controlled Diagram nature<br>
 * 
 * @author sguerin
 * 
 */
public class DiagramVirtualModelView extends VirtualModelView {

	// private final FlexoConceptPreviewComponent previewComponent;

	public DiagramVirtualModelView(VirtualModel virtualModel, FlexoController controller, FlexoPerspective perspective) {
		super(virtualModel, DiagramCst.FML_CONTROLLED_VIRTUAL_MODEL_VIEW_FIB, controller, perspective);
		// previewComponent = new FlexoConceptPreviewComponent(flexoConcept);
		// previewComponent.setSelectionManager(controller.getSelectionManager());
	}

	public DiagramTechnologyAdapterController getDiagramTechnologyAdapterController(FlexoController controller) {
		TechnologyAdapterControllerService tacService = controller.getApplicationContext().getTechnologyAdapterControllerService();
		return tacService.getTechnologyAdapterController(DiagramTechnologyAdapterController.class);
	}

	@Override
	public void show(final FlexoController controller, FlexoPerspective perspective) {

		// Sets palette view of editor to be the top right view
		// perspective.setTopRightView(previewComponent);

		// getDiagramTechnologyAdapterController(controller).getInspectors().attachToEditor(previewComponent.getPreviewController());
		// getDiagramTechnologyAdapterController(controller).getDialogInspectors().attachToEditor(previewComponent.getPreviewController());

		perspective.setBottomRightView(getDiagramTechnologyAdapterController(controller).getInspectors().getPanelGroup());

		/*SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Force right view to be visible
				controller.getControllerModel().setRightViewVisible(true);
			}
		});*/

		controller.getControllerModel().setRightViewVisible(true);

		revalidate();
		repaint();
	}

	@Override
	public void willHide() {
		super.willHide();
		getPerspective().getController().getControllerModel().setRightViewVisible(false);
	}

}
