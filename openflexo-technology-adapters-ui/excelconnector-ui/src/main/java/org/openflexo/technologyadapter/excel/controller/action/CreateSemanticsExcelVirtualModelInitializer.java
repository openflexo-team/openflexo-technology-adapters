/*
 * Copyright (c) 2013-2017, Openflexo
 *
 * This file is part of Flexo-foundation, a component of the software infrastructure
 * developed at Openflexo.
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
 *           Additional permission under GNU GPL version 3 section 7
 *           If you modify this Program, or any covered work, by linking or
 *           combining it with software containing parts covered by the terms
 *           of EPL 1.0, the licensors of this Program grant you additional permission
 *           to convey the resulting work.
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
package org.openflexo.technologyadapter.excel.controller.action;

import javax.swing.Icon;

import org.openflexo.components.wizard.Wizard;
import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.gina.controller.FIBController;
import org.openflexo.icon.FMLIconLibrary;
import org.openflexo.icon.IconFactory;
import org.openflexo.model.exceptions.ModelExecutionException;
import org.openflexo.technologyadapter.excel.action.CreateSemanticsExcelVirtualModel;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class CreateSemanticsExcelVirtualModelInitializer
		extends ActionInitializer<CreateSemanticsExcelVirtualModel, FlexoObject, FMLObject> {
	public CreateSemanticsExcelVirtualModelInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateSemanticsExcelVirtualModel.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionRunnable<CreateSemanticsExcelVirtualModel, FlexoObject, FMLObject> getDefaultInitializer() {
		return (e, action) -> {
			Wizard wizard = new CreateSemanticsExcelVirtualModelWizard(action, getController());
			WizardDialog dialog = new WizardDialog(wizard, getController());
			dialog.showDialog();
			if (dialog.getStatus() != FIBController.Status.VALIDATED) {
				// Operation cancelled
				return false;
			}
			return true;
		};
	}

	@Override
	protected FlexoExceptionHandler<CreateSemanticsExcelVirtualModel, FlexoObject, FMLObject> getDefaultExceptionHandler() {
		return (exception, action) -> {
			Throwable cause = exception.getCause();
			if (cause instanceof ModelExecutionException) {
				String message = cause.getMessage();
				int indexOf = message.lastIndexOf("message:");
				if (indexOf >= 0) {
					message = message.substring(indexOf + 9);
				}
				FlexoController.showError("Can't create model", message);
				return true;
			}
			return false;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory<CreateSemanticsExcelVirtualModel, FlexoObject, FMLObject> actionType) {
		return IconFactory.getImageIcon(FMLIconLibrary.VIRTUAL_MODEL_ICON, FMLIconLibrary.NEW_MARKER);
	}

}
