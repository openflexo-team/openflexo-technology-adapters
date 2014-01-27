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

import java.util.logging.Logger;

import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.control.MouseControl.MouseButton;
import org.openflexo.fge.control.MouseControlContext;
import org.openflexo.fge.control.actions.MouseClickControlActionImpl;
import org.openflexo.fge.control.actions.MouseClickControlImpl;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.technologyadapter.diagram.controller.FMLControlledDiagramMouseClickControl;
import org.openflexo.technologyadapter.diagram.fml.ConnectorPatternRole;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementAction.ActionMask;
import org.openflexo.technologyadapter.diagram.fml.ShapePatternRole;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * This is the abstraction of a drawing representing a {@link Diagram} in controlled mode: the Diagram is edited in a federated mode<br>
 * There is a VirtualModel controlling the edition of the diagram
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramDrawing extends AbstractDiagramDrawing {

	private static final Logger logger = Logger.getLogger(FMLControlledDiagramDrawing.class.getPackage().getName());

	private final VirtualModelInstance vmInstance;

	public FMLControlledDiagramDrawing(Diagram model, VirtualModelInstance vmInstance, boolean readOnly) {
		super(model, readOnly);
		this.vmInstance = vmInstance;
	}

	@Override
	protected DrawingGraphicalRepresentation retrieveGraphicalRepresentation(Diagram diagram, DiagramFactory factory) {
		DrawingGraphicalRepresentation returned = super.retrieveGraphicalRepresentation(diagram, factory);
		return returned;
	}

	@Override
	protected ShapeGraphicalRepresentation retrieveGraphicalRepresentation(DiagramShape shape, DiagramFactory factory) {
		ShapeGraphicalRepresentation returned = super.retrieveGraphicalRepresentation(shape, factory);
		if (shape != null) {
			ShapePatternRole patternRole = shape.getPatternRole(vmInstance);
			if (patternRole != null) {
				for (ActionMask mask : shape.getPatternRole(vmInstance).getReferencedMasks()) {
					returned.addToMouseClickControls(new FMLControlledDiagramMouseClickControl(mask, patternRole, vmInstance, factory));
				}
			}
		}

		return returned;
	}

	@Override
	protected ConnectorGraphicalRepresentation retrieveGraphicalRepresentation(DiagramConnector connector, DiagramFactory factory) {
		ConnectorGraphicalRepresentation returned = super.retrieveGraphicalRepresentation(connector, factory);

		boolean doubleClickUsed = false;
		if (connector != null) {
			ConnectorPatternRole patternRole = connector.getPatternRole(vmInstance);
			if (patternRole != null) {
				for (ActionMask mask : patternRole.getReferencedMasks()) {
					returned.addToMouseClickControls(new FMLControlledDiagramMouseClickControl(mask, patternRole, vmInstance, factory));
					doubleClickUsed |= mask == ActionMask.DoubleClick;
				}
			}
		}

		if (!doubleClickUsed) {
			returned.addToMouseClickControls(new MouseClickControlImpl<DiagramEditor>("reset_layout", MouseButton.LEFT, 2,
					new MouseClickControlActionImpl<DiagramEditor>() {

						@Override
						public boolean handleClick(org.openflexo.fge.Drawing.DrawingTreeNode<?, ?> node, DiagramEditor controller,
								MouseControlContext context) {
							if (node instanceof ConnectorNode) {
								((ConnectorNode<?>) node).refreshConnector();
							}
							return true;
						}
					}, false, false, false, false, factory));
		}

		return returned;
	}

}
