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

import org.openflexo.foundation.resource.ScreenshotBuilder;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.model.Diagram;

public class FMLControlledDiagramScreenshotBuilder extends ScreenshotBuilder<Diagram> {

	private FMLControlledDiagramEditor editor;
	
	public void setDrawing(FMLControlledDiagramEditor editor) {
		this.editor = editor;
	}

	@Override
	public String getScreenshotName(Diagram o) {
		return o.getName();
	}
	
	@Override
	public JComponent getScreenshotComponent(Diagram diagram) {
		return editor.getDrawingView();
	}

}
