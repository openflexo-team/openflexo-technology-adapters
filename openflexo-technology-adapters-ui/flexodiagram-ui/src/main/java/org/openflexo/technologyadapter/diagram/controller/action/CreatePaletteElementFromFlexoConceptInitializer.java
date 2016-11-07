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
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.gina.controller.FIBController.Status;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramModuleView;
import org.openflexo.technologyadapter.diagram.fml.action.CreatePaletteElementFromFlexoConcept;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.gui.view.FMLControlledDiagramVirtualModelView;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.TechnologyPerspective;

public class CreatePaletteElementFromFlexoConceptInitializer extends ActionInitializer<CreatePaletteElementFromFlexoConcept, FlexoConcept, FMLObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreatePaletteElementFromFlexoConceptInitializer(ControllerActionInitializer actionInitializer) {
		super(CreatePaletteElementFromFlexoConcept.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<CreatePaletteElementFromFlexoConcept> getDefaultInitializer() {
		return new FlexoActionInitializer<CreatePaletteElementFromFlexoConcept>() {
			@Override
			public boolean run(EventObject e, CreatePaletteElementFromFlexoConcept action) {

				if (getController().getCurrentModuleView() instanceof FMLControlledDiagramModuleView) {
					FMLControlledDiagramModuleView moduleView = (FMLControlledDiagramModuleView) getController().getCurrentModuleView();
					action.setVirtualModelResource(
							(VirtualModelResource) moduleView.getEditor().getVirtualModelInstance().getVirtualModel().getResource());
				}

				if (getController().getCurrentModuleView() instanceof FMLControlledDiagramVirtualModelView) {
					FMLControlledDiagramVirtualModelView moduleView = (FMLControlledDiagramVirtualModelView) getController()
							.getCurrentModuleView();
					action.setVirtualModelResource((VirtualModelResource) moduleView.getRepresentedObject().getResource());
				}

				Wizard wizard = new CreatePaletteElementFromFlexoConceptWizard(action, getController());
				WizardDialog dialog = new WizardDialog(wizard, getController());
				dialog.showDialog();
				if (dialog.getStatus() != Status.VALIDATED) {
					// Operation cancelled
					return false;
				}
				return true;
				// action.setImage((DiagramIconLibrary.DIAGRAM_PALETTE_ICON).getImage());
				// return instanciateAndShowDialog(action, DiagramCst.PUSH_TO_PALETTE_DIALOG_FIB);
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<CreatePaletteElementFromFlexoConcept> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreatePaletteElementFromFlexoConcept>() {
			@Override
			public boolean run(EventObject e, CreatePaletteElementFromFlexoConcept action) {
				// Switch to palette if in DiagramPerspective
				if (getController().getCurrentPerspective() instanceof TechnologyPerspective
						&& ((TechnologyPerspective) getController().getCurrentPerspective())
								.getTechnologyAdapter() instanceof DiagramTechnologyAdapter) {
					getController().setCurrentEditedObjectAsModuleView(action.getPalette());
					getController().getSelectionManager().setSelectedObject(action.getNewPaletteElement());
				}
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return DiagramIconLibrary.DIAGRAM_PALETTE_ICON;
	}

}
