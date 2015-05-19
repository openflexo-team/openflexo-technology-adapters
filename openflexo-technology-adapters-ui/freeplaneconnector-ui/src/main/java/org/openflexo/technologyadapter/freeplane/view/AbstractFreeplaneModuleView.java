/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freeplane, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.freeplane.view;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Abstract ModuleView to represent a Freeplane map, which can be in a federation context or in a free editing context. Inherit from
 * JScrollPane, to contain Freeplane MapView component. Can be source of NPE because they have unchecked runtime dependencies to a docking
 * framework unused in here.
 */
public abstract class AbstractFreeplaneModuleView<T extends FlexoObject> extends JScrollPane implements ModuleView<T> {

	protected final FlexoController controller;

	protected final T representedObject;

	protected final FlexoPerspective perspective;

	/**
	 * Initialize needed attribute. All are final, no implicit call of super should be done.
	 *
	 * @param controller
	 * @param representedObject
	 * @param perspective
	 */
	protected AbstractFreeplaneModuleView(FlexoController controller, T representedObject, FlexoPerspective perspective) {
		super();
		this.controller = controller;
		this.representedObject = representedObject;
		this.perspective = perspective;
	}

	/**
	 * Initialize needed attribute. All are final, no implicit call of super should be done. Add a viewportView to ScrollPane.
	 *
	 * @param controller
	 * @param representedObject
	 * @param perspective
	 * @param viewportView
	 */
	public AbstractFreeplaneModuleView(FlexoController controller, T representedObject, FlexoPerspective perspective,
			JComponent viewportView) {
		this(controller, representedObject, perspective);
		this.setViewportView(viewportView);
	}

	@Override
	public T getRepresentedObject() {
		return representedObject;
	}

	/**
	 * Remove ModuleView from controller. A Freeplane action may be needed but nothing is done at the moment.
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
	 * Update right view to have Freeplane icon scrollbar.
	 */
	@Override
	public void willShow() {
		this.displayIconToolBar(this.controller, this.perspective);
	}

	/**
	 * Nothing done on this ModuleView
	 */
	@Override
	public void willHide() {
		// Nothing to implement
	}

	@Override
	public void show(FlexoController flexoController, FlexoPerspective flexoPerspective) {
		this.displayIconToolBar(flexoController, flexoPerspective);
	}

	/*
	* Needed Update on swap from FMLControlled to Free ModuleView, so refactor to call it in show and will show.
	*/
	private void displayIconToolBar(final FlexoController controller, final FlexoPerspective perspective) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				perspective.setTopRightView(FreeplaneBasicAdapter.getInstance().getIconToolbar());
				controller.getControllerModel().setRightViewVisible(true);
			}
		});
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}
}
