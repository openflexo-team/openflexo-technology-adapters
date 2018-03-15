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
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.FGEModelFactory;
import org.openflexo.fge.FGEModelFactoryImpl;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.gina.controller.FIBController.Status;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class CreateDiagramPaletteInitializer extends ActionInitializer<CreateDiagramPalette, DiagramSpecification, FlexoObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreateDiagramPaletteInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateDiagramPalette.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionRunnable<CreateDiagramPalette, DiagramSpecification, FlexoObject> getDefaultInitializer() {
		return (e, action) -> {

			try {
				FGEModelFactory factory = new FGEModelFactoryImpl();
				action.setGraphicalRepresentation(makePaletteGraphicalRepresentation(factory));
			} catch (ModelDefinitionException e1) {
				e1.printStackTrace();
			}

			Wizard wizard = new CreateDiagramPaletteWizard(action, getController());
			WizardDialog dialog = new WizardDialog(wizard, getController());
			dialog.showDialog();
			if (dialog.getStatus() != Status.VALIDATED) {
				// Operation cancelled
				return false;
			}
			return true;

			/*FGEModelFactory factory;
			try {
				FGEModelFactory factory = new FGEModelFactoryImpl();
				action.setGraphicalRepresentation(makePaletteGraphicalRepresentation(factory));
				return instanciateAndShowDialog(action, DiagramCst.CREATE_PALETTE_DIALOG_FIB);
			} catch (ModelDefinitionException e1) {
				e1.printStackTrace();
				return false;
			}*/
		};
	}

	@Override
	protected FlexoActionRunnable<CreateDiagramPalette, DiagramSpecification, FlexoObject> getDefaultFinalizer() {
		return (e, action) -> {
			getController().focusOnTechnologyAdapter(getController().getTechnologyAdapter(DiagramTechnologyAdapter.class));
			getController().setCurrentEditedObjectAsModuleView(action.getNewPalette());
			return true;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory<CreateDiagramPalette, DiagramSpecification, FlexoObject> actionFactory) {
		return IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_PALETTE_ICON, IconLibrary.NEW_MARKER);
	}

	protected DrawingGraphicalRepresentation makePaletteGraphicalRepresentation(FGEModelFactory factory) {
		DrawingGraphicalRepresentation gr = factory.makeDrawingGraphicalRepresentation();
		gr.setDrawWorkingArea(true);
		gr.setWidth(260);
		gr.setHeight(300);
		gr.setIsVisible(true);
		return gr;
	}

}
