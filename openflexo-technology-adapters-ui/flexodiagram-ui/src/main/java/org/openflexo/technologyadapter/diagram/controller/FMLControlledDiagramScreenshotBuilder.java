/*
 * (c) Copyright 2013-2014 Openflexo
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
package org.openflexo.technologyadapter.diagram.controller;

import javax.swing.JComponent;

import org.openflexo.fge.Drawing.ShapeNode;
import org.openflexo.fge.swing.view.JShapeView;
import org.openflexo.foundation.resource.ScreenshotBuilder;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramShape;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

public class FMLControlledDiagramScreenshotBuilder extends ScreenshotBuilder<DiagramElement<?>> {

	private FMLControlledDiagramEditor editor;
	
	public FMLControlledDiagramScreenshotBuilder() {
		super();
	}
	
	public void setDrawing(FMLControlledDiagramEditor editor) {
		this.editor = editor;
	}

	@Override
	public String getScreenshotName(DiagramElement<?> o) {
		return o.getName();
	}
	
	@Override
	public JComponent getScreenshotComponent(DiagramElement<?> diagramElement) {
		if(diagramElement instanceof Diagram){
			setHasParent(false);
			return editor.getDrawingView();
		}else if(diagramElement instanceof DiagramShape){
			setHasParent(true);
			FMLControlledDiagramShape shape = editor.getDrawing().getFederatedShape((DiagramShape) diagramElement);
			ShapeNode shapeNode = editor.getDrawing().getShapeNode(shape);
			JShapeView<DiagramShape> shapeView = editor.getDrawingView().shapeViewForNode(shapeNode);
			return shapeView;
		}
		return null;
	}

}
