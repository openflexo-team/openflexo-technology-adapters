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
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.gina.controller.FIBController.Status;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramModuleView;
import org.openflexo.technologyadapter.diagram.fml.action.CreatePaletteElementFromShape;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.gui.view.FMLControlledDiagramVirtualModelView;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class CreatePaletteElementFromShapeInitializer
		extends ActionInitializer<CreatePaletteElementFromShape, DiagramShape, DiagramElement<?>> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreatePaletteElementFromShapeInitializer(ControllerActionInitializer actionInitializer) {
		super(CreatePaletteElementFromShape.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionRunnable<CreatePaletteElementFromShape, DiagramShape, DiagramElement<?>> getDefaultInitializer() {
		return (e, action) -> {

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

			Wizard wizard = new CreatePaletteElementFromShapeWizard(action, getController());
			WizardDialog dialog = new WizardDialog(wizard, getController());
			dialog.showDialog();
			if (dialog.getStatus() != Status.VALIDATED) {
				// Operation cancelled
				return false;
			}
			return true;
			// action.setImage((DiagramIconLibrary.DIAGRAM_PALETTE_ICON).getImage());
			// return instanciateAndShowDialog(action, DiagramCst.PUSH_TO_PALETTE_DIALOG_FIB);
		};
	}

	@Override
	protected FlexoActionRunnable<CreatePaletteElementFromShape, DiagramShape, DiagramElement<?>> getDefaultFinalizer() {
		return (e, action) -> {
			// Switch to palette if in DiagramPerspective
			getController().focusOnTechnologyAdapter(getController().getTechnologyAdapter(DiagramTechnologyAdapter.class));
			getController().setCurrentEditedObjectAsModuleView(action.getPalette());
			getController().getSelectionManager().setSelectedObject(action.getNewPaletteElement());
			return true;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory<CreatePaletteElementFromShape, DiagramShape, DiagramElement<?>> actionType) {
		return DiagramIconLibrary.DIAGRAM_PALETTE_ICON;
	}

}
