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

package org.openflexo.technologyadapter.diagram.controller.action;

import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.components.wizard.Wizard;
import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.gina.controller.FIBController.Status;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLControlledDiagramVirtualModelInstance;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class CreateFMLControlledDiagramVirtualModelInstanceInitializer
		extends ActionInitializer<CreateFMLControlledDiagramVirtualModelInstance, FlexoObject, FlexoObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreateFMLControlledDiagramVirtualModelInstanceInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateFMLControlledDiagramVirtualModelInstance.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionRunnable<CreateFMLControlledDiagramVirtualModelInstance, FlexoObject, FlexoObject> getDefaultInitializer() {
		return (e, action) -> {

			if (action.skipChoosePopup()) {
				return true;
			}
			if (action.getFocusedObject() instanceof FMLRTVirtualModelInstance
					&& ((FMLRTVirtualModelInstance) action.getFocusedObject()).getVirtualModel().getContainerVirtualModel() != null) {
				// @Brutal
				// TODO: Instead of doing this, it would be better to handle resources in wizard FIB
				((FMLRTVirtualModelInstance) action.getFocusedObject()).getVirtualModel().getContainerVirtualModel()
						.loadContainedVirtualModelsWhenUnloaded();
			}
			Wizard wizard = new CreateFMLControlledDiagramVirtualModelInstanceWizard(action, getController());
			WizardDialog dialog = new WizardDialog(wizard, getController());
			dialog.showDialog();
			if (dialog.getStatus() != Status.VALIDATED) {
				// Operation cancelled
				return false;
			}
			return true;
		};
	}

	@Override
	protected FlexoActionRunnable<CreateFMLControlledDiagramVirtualModelInstance, FlexoObject, FlexoObject> getDefaultFinalizer() {
		return (e, action) -> {
			getController().focusOnTechnologyAdapter(getController().getTechnologyAdapter(DiagramTechnologyAdapter.class));
			return true;
		};
	}

	@Override
	protected FlexoExceptionHandler<CreateFMLControlledDiagramVirtualModelInstance, FlexoObject, FlexoObject> getDefaultExceptionHandler() {
		return (exception, action) -> {
			if (exception instanceof NotImplementedException) {
				FlexoController.notify(action.getLocales().localizedForKey("not_implemented_yet"));
				return true;
			}
			return false;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory<CreateFMLControlledDiagramVirtualModelInstance, FlexoObject, FlexoObject> actionType) {
		return DiagramIconLibrary.DIAGRAM_ICON;
	}

}
