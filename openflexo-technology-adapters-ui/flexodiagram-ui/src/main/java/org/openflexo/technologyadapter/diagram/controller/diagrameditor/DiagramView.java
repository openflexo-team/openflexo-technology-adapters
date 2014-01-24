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
package org.openflexo.technologyadapter.diagram.controller.diagrameditor;

import java.awt.Graphics;
import java.util.logging.Logger;

import org.openflexo.fge.swing.view.JDrawingView;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.DrawEdgeControl.DrawEdgeAction;
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

	public void setDrawEdgeAction(DrawEdgeAction action) {
		_drawEdgeAction = action;
	}

	public void resetDrawEdgeAction() {
		_drawEdgeAction = null;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		boolean isBuffering = isBuffering();
		super.paint(g);
		if (_drawEdgeAction != null && !isBuffering) {
			_drawEdgeAction.paint(g, getController());
		}
	}

}
