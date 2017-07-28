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
import javax.swing.*;
import org.openflexo.fge.Drawing.ShapeNode;
import org.openflexo.fge.swing.view.JShapeView;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceObject;
import org.openflexo.technologyadapter.diagram.controller.DropSchemeParametersRetriever;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramModuleView;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.DropSchemeAction;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class DropSchemeActionInitializer extends ActionInitializer<DropSchemeAction, FMLRTVirtualModelInstance, VirtualModelInstanceObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public DropSchemeActionInitializer(ControllerActionInitializer actionInitializer) {
		super(DropSchemeAction.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<DropSchemeAction> getDefaultInitializer() {
		return new FlexoActionInitializer<DropSchemeAction>() {
			@Override
			public boolean run(EventObject e, DropSchemeAction action) {
				getController().willExecute(action);
				DropSchemeParametersRetriever parameterRetriever = new DropSchemeParametersRetriever(action, getController());
				if (action.escapeParameterRetrievingWhenValid && parameterRetriever.isSkipable()) {
					return true;
				}
				return parameterRetriever.retrieveParameters();
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<DropSchemeAction> getDefaultFinalizer() {
		return (e, action) -> {
			/*	DiagramShape shape = action.getPrimaryShape();
				logger.info("border5 = " + ((ShapeGraphicalRepresentation) shape.getGraphicalRepresentation()).getBorder());
				if (shape.getParent() != action.getParent()) {
					DiagramShapeGR parentNode = (DiagramShapeGR) shape.getParent().getGraphicalRepresentation();
					DiagramShapeGR expectedGR = (DiagramShapeGR) action.getParent().getGraphicalRepresentation();
					DiagramShapeGR myGR = (DiagramShapeGR) action.getPrimaryShape().getGraphicalRepresentation();
					Point p = new Point((int) myGR.getX(), (int) myGR.getY());
					Point newP = GraphicalRepresentation.convertPoint(expectedGR, p, parentNode, 1.0);
					myGR.setLocation(new FGEPoint(newP.x, newP.y));
					logger.info("border6 = " + myGR.getBorder());
					logger.info("ShapeSpecification has been relocated");
				}*/

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
	protected FlexoExceptionHandler<DropSchemeAction> getDefaultExceptionHandler() {
		return (exception, action) -> {
			if (exception instanceof NotImplementedException) {
				FlexoController.notify(action.getLocales().localizedForKey("not_implemented_yet"));
				return true;
			}
			return false;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionType actionType) {
		return DiagramIconLibrary.SHAPE_ICON;
	}

}
