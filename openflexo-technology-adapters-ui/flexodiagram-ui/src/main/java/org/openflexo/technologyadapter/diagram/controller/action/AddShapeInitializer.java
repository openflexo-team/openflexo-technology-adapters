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

import org.openflexo.fge.Drawing.ShapeNode;
import org.openflexo.fge.swing.view.JShapeView;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramModuleView;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.AddShape;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class AddShapeInitializer extends ActionInitializer<AddShape, DiagramContainerElement<?>, DiagramElement<?>> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public AddShapeInitializer(ControllerActionInitializer actionInitializer) {
		super(AddShape.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<AddShape> getDefaultInitializer() {
		return new FlexoActionInitializer<AddShape>() {
			@Override
			public boolean run(EventObject e, AddShape action) {
				if ((action.getNewShapeName() != null || action.isNameSetToNull()) && action.getParent() != null) {
					return true;
				}

				DiagramElement<?> parent = action.getParent();
				if (parent != null) {
					String newName = FlexoController.askForString(action.getLocales().localizedForKey("name_for_new_shape"));
					if (newName == null || StringUtils.isEmpty(newName)) {
						return false;
					}
					action.setNewShapeName(newName);
					return true;
				}
				return false;
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<AddShape> getDefaultFinalizer() {
		return new FlexoActionFinalizer<AddShape>() {
			@Override
			public boolean run(EventObject e, AddShape action) {

				getController().getSelectionManager().setSelectedObject(action.getNewShape());

				// System.out.println("Searching module view for " + action.getNewShape().getDiagram());

				ModuleView<?> moduleView = getController().moduleViewForObject(action.getNewShape().getDiagram(), false);

				if (moduleView != null) {

					// System.out.println("moduleView=" + moduleView);
					// System.out.println("editor=" + ((FreeDiagramModuleView) moduleView).getEditor());
					// System.out.println("drawing=" + ((FreeDiagramModuleView) moduleView).getEditor().getDrawing());
					ShapeNode<DiagramShape> shapeNode = ((FreeDiagramModuleView) moduleView).getEditor().getDrawing()
							.getShapeNode(action.getNewShape());
					JShapeView<DiagramShape> shapeView = ((FreeDiagramModuleView) moduleView).getEditor().getDrawingView()
							.shapeViewForNode(shapeNode);
					if (action.getNewShape() != null) {
						if (shapeView.getLabelView() != null) {
							shapeNode.setContinuousTextEditing(true);
							shapeView.getLabelView().startEdition();
						}
					}
				}
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return DiagramIconLibrary.SHAPE_ICON;
	}

}
