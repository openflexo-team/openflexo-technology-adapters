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
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramEditor;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

public class DiagramShapeScreenshotBuilder extends ScreenshotBuilder<DiagramShape> {
	
	public DiagramShapeScreenshotBuilder() {
		setHasParent(true);
	}
	
	@Override
	public String getScreenshotName(DiagramShape o) {
		return o.getName();
	}

	@Override
	public JComponent getScreenshotComponent(DiagramShape diagramShape) {
		FreeDiagramEditor editor = new FreeDiagramEditor(diagramShape.getDiagram(), true);
		ShapeNode<DiagramShape> shapeNode = editor.getDrawing().getShapeNode(diagramShape);
		JShapeView<DiagramShape> shapeView = editor.getDrawingView().shapeViewForNode(shapeNode);
		return shapeView;
	}
	
}
