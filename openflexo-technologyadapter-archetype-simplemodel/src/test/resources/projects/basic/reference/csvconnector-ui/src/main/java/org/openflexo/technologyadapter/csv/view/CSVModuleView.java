/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technologyadapter-archetype-simplemodel, a component of the software infrastructure 
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


package org.openflexo.technologyadapter.csv.view;

import java.util.logging.Logger;

import javax.swing.JPanel;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.technologyadapter.csv.model.CSVModel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Module view is typed with generally the resource data, but can be done with any TechnologyObject.
 */
public class CSVModuleView extends JPanel implements ModuleView<CSVModel>{

	private final FlexoController controller;

	private final CSVModel representedObject;

	private final FlexoPerspective perspective;
	/**
	 * Initialize needed attribute. All are final.
	 *
	 * @param controller The flexo controller
	 * @param representedObject CSVModel object that will be represented
	 * @param perspective
	 */
	public CSVModuleView(CSVModel representedObject, FlexoController controller, FlexoPerspective perspective) {
		this.controller = controller;
		this.representedObject = representedObject;
		this.perspective = perspective;
	}

	@Override
	public void show(FlexoController flexoController, FlexoPerspective flexoPerspective) {
		// If you want to add right and left panels to your module view, do it here. Un comment following code with your component.
		// SwingUtilities.invokeLater(new Runnable() {
		//		@Override
		//		public void run() {
		//			perspective.setTopRightView(customJComponent);
		//			controller.getControllerModel().setRightViewVisible(true);
		//			}
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
	public CSVModel getRepresentedObject() {
		return representedObject;
	}

	@Override
	public boolean isAutoscrolled() {
		// If you want to handle scrollable by yourself instead of letting Openflexo doing it, change return to true.
		return false;
	}
}
