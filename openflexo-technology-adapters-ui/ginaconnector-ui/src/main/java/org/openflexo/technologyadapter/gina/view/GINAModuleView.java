/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.gina.view;

import javax.swing.JPanel;

import org.openflexo.technologyadapter.gina.fml.model.GINAFIBComponent;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Module view is typed with generally the resource data, but can be done with any TechnologyObject.
 */
public class GINAModuleView extends JPanel implements ModuleView<GINAFIBComponent> {

	private final FlexoController controller;

	private final GINAFIBComponent representedObject;

	private final FlexoPerspective perspective;

	/**
	 * Initialize needed attribute. All are final.
	 *
	 * @param controller
	 *            The flexo controller
	 * @param representedObject
	 *            GINModel object that will be represented
	 * @param perspective
	 */
	public GINAModuleView(GINAFIBComponent representedObject, FlexoController controller, FlexoPerspective perspective) {
		this.controller = controller;
		this.representedObject = representedObject;
		this.perspective = perspective;
	}

	@Override
	public void show(FlexoController flexoController, FlexoPerspective flexoPerspective) {
		// If you want to add right and left panels to your module view, do it here. Un comment following code with your component.
		// SwingUtilities.invokeLater(new Runnable() {
		// @Override
		// public void run() {
		// perspective.setTopRightView(customJComponent);
		// controller.getControllerModel().setRightViewVisible(true);
		// }
		// });
	}

	/**
	 * Remove ModuleView from controller.
	 */
	@Override
	public void deleteModuleView() {
		this.controller.removeModuleView(this);
	}

	/**
	 * @return perspective given during construction of ModuleView.
	 */
	@Override
	public FlexoPerspective getPerspective() {
		return this.perspective;
	}

	/**
	 * Nothing done on this ModuleView
	 */
	@Override
	public void willShow() {
		// Nothing to implement by default, empty body
	}

	/**
	 * Nothing done on this ModuleView
	 */
	@Override
	public void willHide() {
		// Nothing to implement by default, empty body
	}

	@Override
	public GINAFIBComponent getRepresentedObject() {
		return representedObject;
	}

	@Override
	public boolean isAutoscrolled() {
		// If you want to handle scrollable by yourself instead of letting Openflexo doing it, change return to true.
		return false;
	}
}
