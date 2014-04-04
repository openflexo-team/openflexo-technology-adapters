/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.diagram.gui.view;

import org.openflexo.components.widget.CommonFIB;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.gui.widget.FlexoConceptPreviewComponent;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterControllerService;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.vpm.view.FlexoConceptView;

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
		System.out.println(">>>>>>> Prefered size=" + previewComponent.getPreferredSize());
	}

	public DiagramTechnologyAdapterController getDiagramTechnologyAdapterController(FlexoController controller) {
		TechnologyAdapterControllerService tacService = controller.getApplicationContext().getTechnologyAdapterControllerService();
		return tacService.getTechnologyAdapterController(DiagramTechnologyAdapterController.class);
	}

	@Override
	public void show(final FlexoController controller, FlexoPerspective perspective) {

		System.out.println(">>>>>>>>>>> show() for " + this);
		System.out.println(">>>>>>> Prefered size=" + previewComponent.getPreferredSize());

		// Sets palette view of editor to be the top right view
		perspective.setTopRightView(previewComponent);

		getDiagramTechnologyAdapterController(controller).getInspectors().attachToEditor(previewComponent.getPreviewController());
		getDiagramTechnologyAdapterController(controller).getDialogInspectors().attachToEditor(previewComponent.getPreviewController());

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

}
