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

import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.openflexo.fge.Drawing.RootNode;
import org.openflexo.fge.Drawing.ShapeNode;
import org.openflexo.fge.swing.view.JDrawingView;
import org.openflexo.fge.swing.view.JShapeView;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.resource.ScreenshotBuilder;
import org.openflexo.foundation.resource.ScreenshotBuilder.ScreenshotImage;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.DiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.DiagramModuleView;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.ExportDiagramToImageAction;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class ExportDiagramToImageInitializer extends ActionInitializer<ExportDiagramToImageAction, DiagramElement<?>, DiagramElement<?>> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public ExportDiagramToImageInitializer(ControllerActionInitializer actionInitializer) {
		super(ExportDiagramToImageAction.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<ExportDiagramToImageAction> getDefaultInitializer() {
		return new FlexoActionInitializer<ExportDiagramToImageAction>() {
			@Override
			public boolean run(EventObject e, ExportDiagramToImageAction action) {

				if (getController().getCurrentModuleView() instanceof DiagramModuleView) {
					DiagramEditor c = ((DiagramModuleView) getController().getCurrentModuleView()).getEditor();

					if (action.getFocusedObject() instanceof DiagramShape) {
						ShapeNode<DiagramShape> shapeNode = c.getDrawing().getShapeNode((DiagramShape) action.getFocusedObject());
						final JShapeView<DiagramShape> shapeView = c.getDrawingView().shapeViewForNode(shapeNode);
						// shapeView.getScreenshot();
						// BufferedImage image = shapeView.getScreenshot();
						// ShapeBorder b = shapeNode.getBorder();
						// ShadowStyle ss = shapeNode.getShadowStyle();
						// TODO: do it in action, not in initializer
						ScreenshotBuilder<DiagramShape> builder = new ScreenshotBuilder<DiagramShape>() {
							@Override
							public JComponent getScreenshotComponent(DiagramShape object) {
								return shapeView;
							}

							@Override
							public String getScreenshotName(DiagramShape o) {
								return o.getName();
							}
						};
						ScreenshotImage<DiagramShape> screenshotImage = builder.getImage((DiagramShape) action.getFocusedObject());
						action.setScreenshot(screenshotImage);
						/*action.setScreenshot(ScreenshotGenerator.makeImage(image, b.left, b.top, (int) gr.getWidth()
								+ (ss.getDrawShadow() ? ss.getShadowBlur() : 0) + 1,
								(int) gr.getHeight() + (ss.getDrawShadow() ? ss.getShadowBlur() : 0) + 1));*/
					} else if (action.getFocusedObject() instanceof Diagram) {
						RootNode<Diagram> rootNode = c.getDrawing().getRoot();
						final JDrawingView<Diagram> diagramView = c.getDrawingView();
						// shapeView.getScreenshot();
						// BufferedImage image = shapeView.getScreenshot();
						// ShapeBorder b = shapeNode.getBorder();
						// ShadowStyle ss = shapeNode.getShadowStyle();
						// TODO: do it in action, not in initializer
						ScreenshotBuilder<Diagram> builder = new ScreenshotBuilder<Diagram>() {
							@Override
							public JComponent getScreenshotComponent(Diagram object) {
								return diagramView;
							}

							@Override
							public String getScreenshotName(Diagram o) {
								return o.getName();
							}
						};
						ScreenshotImage<Diagram> screenshotImage = builder.getImage((Diagram) action.getFocusedObject());
						action.setScreenshot(screenshotImage);
						/*action.setScreenshot(ScreenshotGenerator.makeImage(image, b.left, b.top, (int) gr.getWidth()
								+ (ss.getDrawShadow() ? ss.getShadowBlur() : 0) + 1,
								(int) gr.getHeight() + (ss.getDrawShadow() ? ss.getShadowBlur() : 0) + 1));*/
					}
					return action.saveAsJpeg();
				}

				return false;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return IconLibrary.EXPORT_ICON;
	}

}
