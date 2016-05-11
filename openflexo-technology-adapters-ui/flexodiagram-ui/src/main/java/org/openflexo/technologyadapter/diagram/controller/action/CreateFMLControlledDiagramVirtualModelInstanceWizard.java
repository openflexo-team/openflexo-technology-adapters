/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Fml-rt-technologyadapter-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.controller.action;

import java.awt.Image;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLControlledDiagramVirtualModelInstance;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.action.AbstractCreateVirtualModelInstanceWizard;

public class CreateFMLControlledDiagramVirtualModelInstanceWizard
		extends AbstractCreateVirtualModelInstanceWizard<CreateFMLControlledDiagramVirtualModelInstance> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(CreateFMLControlledDiagramVirtualModelInstanceWizard.class.getPackage().getName());

	public CreateFMLControlledDiagramVirtualModelInstanceWizard(CreateFMLControlledDiagramVirtualModelInstance action,
			FlexoController controller) {
		super(action, controller);
	}

	/*@Override
	protected AbstractCreateVirtualModelInstanceWizard<CreateBasicVirtualModelInstance>.AbstractChooseVirtualModel<?> makeChooseVirtualModel() {
		return new ChooseVirtualModel();
	}*/

	@Override
	protected ChooseVirtualModel makeChooseVirtualModel() {
		return new ChooseVirtualModel();
	}

	@Override
	protected ChooseAndConfigureCreationSchemeForFMLControlledDiagramVirtualModel makeChooseAndConfigureCreationScheme() {
		return new ChooseAndConfigureCreationSchemeForFMLControlledDiagramVirtualModel();
	}

	/**
	 * This step is used to set {@link VirtualModel} to be used, as well as name and title of the {@link VirtualModelInstance}
	 * 
	 * @author sylvain
	 * 
	 */
	@FIBPanel("Fib/Wizard/ChooseFMLControlledDiagramVirtualModel.fib")
	public class ChooseVirtualModel extends AbstractChooseVirtualModel<VirtualModel> {

		@Override
		public String getTitle() {
			return FlexoLocalization.localizedForKey("choose_fml_controlled_diagram_virtual_model");
		}

	}

	@FIBPanel("Fib/Wizard/ChooseAndConfigureCreationSchemeForFMLControlledDiagramVirtualModel.fib")
	public class ChooseAndConfigureCreationSchemeForFMLControlledDiagramVirtualModel
			extends AbstractChooseAndConfigureCreationScheme<VirtualModel> {

		public ChooseAndConfigureCreationSchemeForFMLControlledDiagramVirtualModel() {
			super(action.getCreationSchemeAction());
		}
	}

	@Override
	public String getWizardTitle() {
		return FlexoLocalization.localizedForKey("create_fml_controlled_diagram");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_MEDIUM_ICON, IconLibrary.NEW_32_32).getImage();
	}

}
