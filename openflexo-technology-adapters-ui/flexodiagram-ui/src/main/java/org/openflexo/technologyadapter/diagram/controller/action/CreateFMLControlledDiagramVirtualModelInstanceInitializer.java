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

import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.components.wizard.Wizard;
import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.fib.controller.FIBController.Status;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLControlledDiagramVirtualModelInstance;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class CreateFMLControlledDiagramVirtualModelInstanceInitializer
		extends ActionInitializer<CreateFMLControlledDiagramVirtualModelInstance, View, FlexoObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreateFMLControlledDiagramVirtualModelInstanceInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateFMLControlledDiagramVirtualModelInstance.actionType, actionInitializer);
	}

	// TODO: reimplement this
	private Status chooseAndConfigureCreationScheme(CreateFMLControlledDiagramVirtualModelInstance action) {
		return instanciateShowDialogAndReturnStatus(action, null /*CommonFIB.CHOOSE_AND_CONFIGURE_CREATION_SCHEME_DIALOG_FIB*/);
	}

	@Override
	protected FlexoActionInitializer<CreateFMLControlledDiagramVirtualModelInstance> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateFMLControlledDiagramVirtualModelInstance>() {
			@Override
			public boolean run(EventObject e, CreateFMLControlledDiagramVirtualModelInstance action) {

				if (action.skipChoosePopup()) {
					return true;
				}
				else {
					if (action.getFocusedObject() != null && action.getFocusedObject().getViewPoint() != null) {
						// @Brutal
						// TODO: Instead of doing this, it would be better to handle resources in wizard FIB
						action.getFocusedObject().getViewPoint().loadVirtualModelsWhenUnloaded();
					}
					Wizard wizard = new CreateFMLControlledDiagramVirtualModelInstanceWizard(action, getController());
					WizardDialog dialog = new WizardDialog(wizard, getController());
					dialog.showDialog();
					if (dialog.getStatus() != Status.VALIDATED) {
						// Operation cancelled
						return false;
					}
					return true;
				}

				/*if (action.skipChoosePopup) {
					return true;
				} else {
					int step = 0;
					boolean shouldContinue = true;
					while (shouldContinue) {
						Status result;
						if (step == 0) {
							result = instanciateShowDialogAndReturnStatus(action, CommonFIB.CREATE_VIRTUAL_MODEL_INSTANCE_DIALOG_FIB);
						} else if (step == action.getStepsNumber() - 1 && action.getVirtualModel() != null
								&& action.getVirtualModel().hasCreationScheme()) {
							result = chooseAndConfigureCreationScheme(action);
						}else {
							ModelSlot<?> configuredModelSlot = action.getVirtualModel().getModelSlots().get(step - 1);
							result = instanciateShowDialogAndReturnStatus(action.getModelSlotInstanceConfiguration(configuredModelSlot),
									getModelSlotInstanceConfigurationFIB(configuredModelSlot.getClass()));
						}
						if (result == Status.CANCELED) {
							return false;
						} else if (result == Status.VALIDATED) {
							return true;
						} else if (result == Status.NEXT && step + 1 <= action.getStepsNumber()) {
							step = step + 1;
						} else if (result == Status.BACK && step - 1 >= 0) {
							step = step - 1;
						}
					}
				
					return instanciateAndShowDialog(action, CommonFIB.CREATE_VIRTUAL_MODEL_INSTANCE_DIALOG_FIB);
				}*/

				// logger.warning("!!!!!!! Please reimplement me");
				// return false;
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<CreateFMLControlledDiagramVirtualModelInstance> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateFMLControlledDiagramVirtualModelInstance>() {
			@Override
			public boolean run(EventObject e, CreateFMLControlledDiagramVirtualModelInstance action) {

				DiagramTechnologyAdapterController diagramTAController = (DiagramTechnologyAdapterController) getController()
						.getTechnologyAdapterController(DiagramTechnologyAdapter.class);
				getController().setCurrentEditedObjectAsModuleView(action.getNewVirtualModelInstance(),
						diagramTAController.getFMLRTControlledDiagramNaturePerspective());
				return true;
			}
		};
	}

	@Override
	protected FlexoExceptionHandler<CreateFMLControlledDiagramVirtualModelInstance> getDefaultExceptionHandler() {
		return new FlexoExceptionHandler<CreateFMLControlledDiagramVirtualModelInstance>() {
			@Override
			public boolean handleException(FlexoException exception, CreateFMLControlledDiagramVirtualModelInstance action) {
				if (exception instanceof NotImplementedException) {
					FlexoController.notify(FlexoLocalization.localizedForKey("not_implemented_yet"));
					return true;
				}
				return false;
			}
		};
	}

	/**
	 * @author Vincent This method has to be removed as soon as we will have a real Wizard Management. Its purpose is to handle the
	 *         separation of FIBs for Model Slot Configurations.
	 * @return File that correspond to the FIB
	 */
	/*private Resource getModelSlotInstanceConfigurationFIB(Class<? extends ModelSlot> modelSlotClass) {
		if (TypeAwareModelSlot.class.isAssignableFrom(modelSlotClass)) {
			return CommonFIB.CONFIGURE_TYPE_AWARE_MODEL_SLOT_INSTANCE_DIALOG_FIB;
		}
		if (FreeModelSlot.class.isAssignableFrom(modelSlotClass)) {
			return CommonFIB.CONFIGURE_FREE_MODEL_SLOT_INSTANCE_DIALOG_FIB;
		}
		if (FMLRTModelSlot.class.isAssignableFrom(modelSlotClass)) {
			return CommonFIB.CONFIGURE_VIRTUAL_MODEL_SLOT_INSTANCE_DIALOG_FIB;
		}
		return null;
	}*/

	@Override
	protected Icon getEnabledIcon() {
		return DiagramIconLibrary.DIAGRAM_ICON;
	}

}
