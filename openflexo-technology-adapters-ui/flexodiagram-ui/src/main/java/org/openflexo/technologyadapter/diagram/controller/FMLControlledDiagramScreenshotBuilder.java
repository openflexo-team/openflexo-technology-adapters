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

package org.openflexo.technologyadapter.diagram.controller;

import javax.swing.*;

import org.openflexo.diana.ScreenshotBuilder;
import org.openflexo.diana.Drawing.ShapeNode;
import org.openflexo.diana.swing.view.JShapeView;
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
