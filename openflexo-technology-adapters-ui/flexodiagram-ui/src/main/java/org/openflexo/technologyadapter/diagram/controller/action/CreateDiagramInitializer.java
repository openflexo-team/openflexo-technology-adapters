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
package org.openflexo.technologyadapter.diagram.controller.action;

import java.io.File;
import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.components.widget.CommonFIB;
import org.openflexo.fib.controller.FIBController.Status;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.FMLModelSlot;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.rm.Resource;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.model.action.CreateDiagram;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class CreateDiagramInitializer extends ActionInitializer<CreateDiagram, RepositoryFolder, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreateDiagramInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateDiagram.actionType, actionInitializer);
	}

	private Status chooseVirtualModel(CreateDiagram action) {
		return instanciateShowDialogAndReturnStatus(action, DiagramCst.CREATE_DIAGRAM_DIALOG_FIB);
	}

	private Status chooseAndConfigureCreationScheme(CreateDiagram action) {
		return instanciateShowDialogAndReturnStatus(action, CommonFIB.CHOOSE_AND_CONFIGURE_CREATION_SCHEME_DIALOG_FIB);
	}

	private Status configureModelSlot(CreateDiagram action, ModelSlot configuredModelSlot) {
		/*return instanciateShowDialogAndReturnStatus(action.getModelSlotInstanceConfiguration(configuredModelSlot),
				getModelSlotInstanceConfigurationFIB(configuredModelSlot.getClass()));*/
		return null;
	}

	@Override
	protected FlexoActionInitializer<CreateDiagram> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateDiagram>() {
			@Override
			public boolean run(EventObject e, CreateDiagram action) {
				if (action.skipChoosePopup) {
					return true;
				} else {

					return instanciateAndShowDialog(action, DiagramCst.CREATE_DIAGRAM_DIALOG_FIB);

					// int step = 0;
					// boolean shouldContinue = true;
					/*while (shouldContinue) {
						Status result;
						if (step == 0) {
							result = chooseVirtualModel(action);
						} else if (step == action.getStepsNumber() - 1 && action.getDiagramSpecification() != null
								&& action.getDiagramSpecification().hasCreationScheme()) {
							result = chooseAndConfigureCreationScheme(action);
						} else {
							ModelSlot configuredModelSlot = action.getVirtualModel().getModelSlots().get(step - 1);
							// result = configureModelSlot(action, configuredModelSlot);

							//
							result = instanciateShowDialogAndReturnStatus(action.getModelSlotInstanceConfiguration(configuredModelSlot),
									getModelSlotInstanceConfigurationFIB(configuredModelSlot.getClass()));
						}
						if (result == Status.CANCELED) {
							return false;
						} else if (result == Status.VALIDATED) {
							return true;
						} else if (result == Status.NEXT && step + 1 < action.getStepsNumber()) {
							step = step + 1;
						} else if (result == Status.BACK && step - 1 >= 0) {
							step = step - 1;
						}
					}*/

					// return instanciateAndShowDialog(action, VECst.CREATE_VIRTUAL_MODEL_INSTANCE_DIALOG_FIB);

					// return false;
				}

			}
		};
	}

	@Override
	protected FlexoActionFinalizer<CreateDiagram> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateDiagram>() {
			@Override
			public boolean run(EventObject e, CreateDiagram action) {
				// getController().setCurrentEditedObjectAsModuleView(action.getNewDiagram());
				getController().selectAndFocusObject(action.getNewDiagram());
				return true;
			}
		};
	}

	@Override
	protected FlexoExceptionHandler<CreateDiagram> getDefaultExceptionHandler() {
		return new FlexoExceptionHandler<CreateDiagram>() {
			@Override
			public boolean handleException(FlexoException exception, CreateDiagram action) {
				if (exception instanceof NotImplementedException) {
					FlexoController.notify(FlexoLocalization.localizedForKey("not_implemented_yet"));
					return true;
				}
				return false;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return DiagramIconLibrary.DIAGRAM_ICON;
	}

	/**
	 * @author Vincent This method has to be removed as soon as we will have a real Wizard Management. Its purpose is to handle the
	 *         separation of FIBs for Model Slot Configurations.
	 * @return File that correspond to the FIB
	 */
	private Resource getModelSlotInstanceConfigurationFIB(Class modelSlotClass) {
		if (TypeAwareModelSlot.class.isAssignableFrom(modelSlotClass)) {
			return CommonFIB.CONFIGURE_TYPE_AWARE_MODEL_SLOT_INSTANCE_DIALOG_FIB;
		}
		if (FreeModelSlot.class.isAssignableFrom(modelSlotClass)) {
			return CommonFIB.CONFIGURE_FREE_MODEL_SLOT_INSTANCE_DIALOG_FIB;
		}
		if (FMLModelSlot.class.isAssignableFrom(modelSlotClass)) {
			return CommonFIB.CONFIGURE_VIRTUAL_MODEL_SLOT_INSTANCE_DIALOG_FIB;
		}
		return null;
	}

}
