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

import org.openflexo.fml.controller.CommonFIB;
import org.openflexo.fml.controller.view.FlexoConceptView;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.gui.widget.FlexoConceptPreviewComponent;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterControllerService;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * This is the module view representing a standard FlexoConcept (an FlexoConcept which is not a VirtualModel, nor part of a
 * DiagramSpecification)<br>
 * 
 * @author sguerin
 * 
 */
public class DiagramFlexoConceptView extends FlexoConceptView<FlexoConcept> {

	private final FlexoConceptPreviewComponent previewComponent;

	public DiagramFlexoConceptView(FlexoConcept flexoConcept, FlexoController controller, FlexoPerspective perspective) {
		super(flexoConcept, CommonFIB.STANDARD_FLEXO_CONCEPT_VIEW_FIB, controller, perspective);
		previewComponent = new FlexoConceptPreviewComponent(flexoConcept);
		previewComponent.setSelectionManager(controller.getSelectionManager());
	}

	public DiagramTechnologyAdapterController getDiagramTechnologyAdapterController(FlexoController controller) {
		TechnologyAdapterControllerService tacService = controller.getApplicationContext().getTechnologyAdapterControllerService();
		return tacService.getTechnologyAdapterController(DiagramTechnologyAdapterController.class);
	}

	@Override
	public void willShow() {
		super.willShow();

		// Sets palette view of editor to be the top right view
		getPerspective().setTopRightView(previewComponent);

		getDiagramTechnologyAdapterController(getFlexoController()).getInspectors().attachToEditor(previewComponent.getPreviewController());
		getDiagramTechnologyAdapterController(getFlexoController()).getDialogInspectors()
				.attachToEditor(previewComponent.getPreviewController());

		getPerspective().setBottomRightView(getDiagramTechnologyAdapterController(getFlexoController()).getInspectors().getPanelGroup());

		getFlexoController().getControllerModel().setRightViewVisible(true);

	}

	@Override
	public void willHide() {
		super.willHide();
		getPerspective().getController().getControllerModel().setRightViewVisible(false);
	}

}
