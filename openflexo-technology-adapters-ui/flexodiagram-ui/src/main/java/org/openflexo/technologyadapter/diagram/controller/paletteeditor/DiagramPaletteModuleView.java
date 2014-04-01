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
package org.openflexo.technologyadapter.diagram.controller.paletteeditor;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterControllerService;
import org.openflexo.view.controller.model.FlexoPerspective;

public class DiagramPaletteModuleView extends JPanel implements ModuleView<DiagramPalette>, PropertyChangeListener {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DiagramPaletteModuleView.class.getPackage().getName());

	private final DiagramPaletteEditor _controller;
	private final FlexoPerspective perspective;

	public DiagramPaletteModuleView(final DiagramPaletteEditor controller, FlexoPerspective perspective) {
		super();
		setLayout(new BorderLayout());
		_controller = controller;
		this.perspective = perspective;

		add(controller.getDrawingView(), BorderLayout.CENTER);
		revalidate();

		getRepresentedObject().getPropertyChangeSupport().addPropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
	}

	public DiagramPaletteEditor getController() {
		return _controller;
	}

	@Override
	public void deleteModuleView() {
		getRepresentedObject().getPropertyChangeSupport().removePropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
		getController().delete();
	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}

	@Override
	public DiagramPalette getRepresentedObject() {
		return _controller.getDiagramPalette();
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}

	@Override
	public void willHide() {
	}

	@Override
	public void willShow() {
		getPerspective().focusOnObject(getRepresentedObject());
	}

	public DiagramTechnologyAdapterController getDiagramTechnologyAdapterController(FlexoController controller) {
		TechnologyAdapterControllerService tacService = controller.getApplicationContext().getTechnologyAdapterControllerService();
		return tacService.getTechnologyAdapterController(DiagramTechnologyAdapterController.class);
	}

	@Override
	public void show(final FlexoController controller, FlexoPerspective perspective) {
		// Sets palette view of editor to be the top right view
		perspective.setTopRightView(getController().getPaletteView());
		// perspective.setHeader(((FreeDiagramModuleView) moduleView).getEditor().getS());

		getDiagramTechnologyAdapterController(controller).getInspectors().attachToEditor(getController());

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Force right view to be visible
				controller.getControllerModel().setRightViewVisible(true);
			}
		});

		controller.getControllerModel().setRightViewVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == getRepresentedObject() && evt.getPropertyName().equals(getRepresentedObject().getDeletedProperty())) {
			deleteModuleView();
		}
	}

}
