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

import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.diana.Drawing.ShapeNode;
import org.openflexo.diana.swing.view.JShapeView;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.gina.controller.FIBController.Status;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramModuleView;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.DrawRectangleSchemeAction;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class DrawRectangleSchemeActionInitializer
		extends ActionInitializer<DrawRectangleSchemeAction, FMLRTVirtualModelInstance, VirtualModelInstanceObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public DrawRectangleSchemeActionInitializer(ControllerActionInitializer actionInitializer) {
		super(DrawRectangleSchemeAction.class, actionInitializer);
	}

	@Override
	protected FlexoActionRunnable<DrawRectangleSchemeAction, FMLRTVirtualModelInstance, VirtualModelInstanceObject> getDefaultInitializer() {
		return (e, action) -> {
			getController().willExecute(action);

			DrawRectangleSchemeActionWizard wizard = new DrawRectangleSchemeActionWizard(action, getController());
			if (!wizard.isSkipable()) {
				WizardDialog dialog = new WizardDialog(wizard, getController());
				dialog.showDialog();
				if (dialog.getStatus() != Status.VALIDATED) {
					// Operation cancelled
					return false;
				}
			}
			return true;
			/*getController().willExecute(action);
			DropSchemeParametersRetriever parameterRetriever = new DropSchemeParametersRetriever(action, getController());
			if (action.escapeParameterRetrievingWhenValid && parameterRetriever.isSkipable()) {
				return true;
			}
			return parameterRetriever.retrieveParameters();*/
		};
	}

	@Override
	protected FlexoActionRunnable<DrawRectangleSchemeAction, FMLRTVirtualModelInstance, VirtualModelInstanceObject> getDefaultFinalizer() {
		return (e, action) -> {
			getController().getSelectionManager().setSelectedObject(action.getPrimaryShape());
			if (action.getPrimaryShape() != null) {
				ModuleView<?> moduleView = getController().moduleViewForObject(action.getPrimaryShape().getDiagram(), false);
				if (moduleView instanceof FreeDiagramModuleView) {
					ShapeNode<DiagramShape> shapeNode = ((FreeDiagramModuleView) moduleView).getEditor().getDrawing()
							.getShapeNode(action.getPrimaryShape());
					JShapeView<DiagramShape> shapeView = ((FreeDiagramModuleView) moduleView).getEditor().getDrawingView()
							.shapeViewForNode(shapeNode);
					if (shapeView != null) {
						if (shapeView.getLabelView() != null) {
							shapeNode.setContinuousTextEditing(true);
							shapeView.getLabelView().startEdition();
							// shapeView.continueEdition();
						}
					}
				}
			}

			return true;
		};
	}

	@Override
	protected FlexoExceptionHandler<DrawRectangleSchemeAction, FMLRTVirtualModelInstance, VirtualModelInstanceObject> getDefaultExceptionHandler() {
		return (exception, action) -> {
			if (exception instanceof NotImplementedException) {
				FlexoController.notify(action.getLocales().localizedForKey("not_implemented_yet"));
				return true;
			}
			return false;
		};
	}

	@Override
	protected Icon getEnabledIcon(
			FlexoActionFactory<DrawRectangleSchemeAction, FMLRTVirtualModelInstance, VirtualModelInstanceObject> actionFactory) {
		return DiagramIconLibrary.SHAPE_ICON;
	}

}
