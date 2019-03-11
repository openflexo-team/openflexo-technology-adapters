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

package org.openflexo.technologyadapter.diagram.controller.diagrameditor;

import java.awt.Graphics;
import java.util.logging.Logger;

import org.openflexo.diana.swing.view.JDrawingView;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.DrawEdgeControl.DrawEdgeAction;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.DrawRectangleControl.DrawRectangleAction;
import org.openflexo.technologyadapter.diagram.model.Diagram;

@SuppressWarnings("serial")
public class DiagramView extends JDrawingView<Diagram> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DiagramView.class.getPackage().getName());

	public DiagramView(DiagramEditor controller) {
		super(controller);
	}

	@Override
	public DiagramEditor getController() {
		return (DiagramEditor) super.getController();
	}

	private DrawEdgeAction _drawEdgeAction;
	private DrawRectangleAction _drawRectangleAction;

	public void setDrawEdgeAction(DrawEdgeAction action) {
		_drawEdgeAction = action;
	}

	public void setDrawRectangleAction(DrawRectangleAction action) {
		_drawRectangleAction = action;
	}

	public void resetDrawEdgeAction() {
		_drawEdgeAction = null;
		repaint();
	}

	private FMLControlledDiagramFloatingPalette fMLControlledDiagramFloatingPalette;

	public void setFloatingPalette(FMLControlledDiagramFloatingPalette palette) {
		fMLControlledDiagramFloatingPalette = palette;
	}

	public void resetFloatingPalette() {
		fMLControlledDiagramFloatingPalette = null;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		boolean isBuffering = isBuffering();
		super.paint(g);
		if (_drawEdgeAction != null && !isBuffering) {
			_drawEdgeAction.paint(g, getController());
		}
		if (_drawRectangleAction != null && !isBuffering) {
			_drawRectangleAction.paint(g, getController());
		}
		if (fMLControlledDiagramFloatingPalette != null && !isBuffering) {
			fMLControlledDiagramFloatingPalette.paint(g, getController());
		}
	}

}
