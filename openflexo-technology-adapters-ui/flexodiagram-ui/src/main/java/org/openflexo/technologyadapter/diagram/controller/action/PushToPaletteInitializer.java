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

import java.awt.image.BufferedImage;
import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.components.widget.CommonFIB;
import org.openflexo.fge.Drawing.ShapeNode;
import org.openflexo.fge.ShadowStyle;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.ShapeBorder;
import org.openflexo.fge.swing.view.JShapeView;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.DiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.DiagramModuleView;
import org.openflexo.technologyadapter.diagram.fml.action.PushToPalette;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class PushToPaletteInitializer extends ActionInitializer<PushToPalette, DiagramShape, DiagramElement<?>> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public PushToPaletteInitializer(ControllerActionInitializer actionInitializer) {
		super(PushToPalette.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<PushToPalette> getDefaultInitializer() {
		return new FlexoActionInitializer<PushToPalette>() {
			@Override
			public boolean run(EventObject e, PushToPalette action) {
				if (getController().getCurrentModuleView() instanceof DiagramModuleView
						&& action.getFocusedObject().getGraphicalRepresentation() instanceof ShapeGraphicalRepresentation) {
					DiagramEditor c = ((DiagramModuleView) getController().getCurrentModuleView()).getController();
					ShapeNode<DiagramShape> shapeNode = c.getDrawing().getShapeNode(action.getFocusedObject());
					JShapeView shapeView = c.getDrawingView().shapeViewForNode(shapeNode);
					BufferedImage image = shapeView.getScreenshot();
					ShapeGraphicalRepresentation gr = shapeNode.getGraphicalRepresentation();
					ShapeBorder b = gr.getBorder();
					ShadowStyle ss = gr.getShadowStyle();
					// TODO: refactor this (see ScreenshotBuilder)
					logger.warning("Please repair ScreenshotGenerator");
					/*action.setScreenshot(ScreenshotGenerator.makeImage(image, b.getLeft(), b.getTop(),
							(int) gr.getWidth() + (ss.getDrawShadow() ? ss.getShadowBlur() : 0) + 1,
							(int) gr.getHeight() + (ss.getDrawShadow() ? ss.getShadowBlur() : 0) + 1));*/
					// action.setScreenshot(ScreenshotGenerator.trimImage(image));
				}

				return instanciateAndShowDialog(action, CommonFIB.PUSH_TO_PALETTE_DIALOG_FIB);
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<PushToPalette> getDefaultFinalizer() {
		return new FlexoActionFinalizer<PushToPalette>() {
			@Override
			public boolean run(EventObject e, PushToPalette action) {
				getController().setCurrentEditedObjectAsModuleView(action.palette);
				getController().getSelectionManager().setSelectedObject(action.getNewPaletteElement());
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return DiagramIconLibrary.DIAGRAM_PALETTE_ICON;
	}

}
