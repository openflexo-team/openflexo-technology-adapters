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
package org.openflexo.technologyadapter.diagram.controller.diagrameditor;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.swing.control.SwingToolFactory;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.view.controller.FlexoController;

/**
 * Editor of a Diagram in controlled mode: the Diagram is edited in a federated mode<br>
 * There is a VirtualModel controlling the edition of the diagram
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramEditor extends DiagramEditor {

	private static final Logger logger = Logger.getLogger(FMLControlledDiagramEditor.class.getPackage().getName());

	private final VirtualModelInstance virtualModelInstance;

	public FMLControlledDiagramEditor(VirtualModelInstance vmInstance, boolean readOnly, FlexoController controller,
			SwingToolFactory swingToolFactory) {
		super(new FMLControlledDiagramDrawing(vmInstance, readOnly), readOnly, controller, swingToolFactory);
		this.virtualModelInstance = vmInstance;
	}

	public VirtualModelInstance getVirtualModelInstance() {
		return virtualModelInstance;
	}

	@Override
	public FMLControlledDiagramDrawing getDrawing() {
		return (FMLControlledDiagramDrawing) super.getDrawing();
	}

	/**
	 * Return the {@link FlexoObject} beeing represented through the supplied {@link DrawingTreeNode}.<br>
	 * This hook allows to implement a disalignment between the representation and the represented object<br>
	 * Here, we have to translate {@link FMLControlledDiagramElement} to {@link FlexoConceptInstance}<br>
	 * 
	 * @param node
	 * @return
	 */
	@Override
	protected FlexoObject getDrawableForDrawingTreeNode(DrawingTreeNode<?, ?> node) {
		if (node.getDrawable() instanceof FMLControlledDiagramElement) {
			return ((FMLControlledDiagramElement<?, ?>) node.getDrawable()).getFlexoConceptInstance();
		}
		return super.getDrawableForDrawingTreeNode(node);
	}

	/**
	 * Return the {@link FlexoObject} which is used as drawable in DrawingTreeNode<br>
	 * This hook allows to implement a disalignment between the representation and the represented object<br>
	 * Here, we have to translate {@link FlexoConceptInstance} to {@link FMLControlledDiagramElement}<br>
	 * 
	 * @param object
	 * @return
	 */
	@Override
	protected FlexoObject getRepresentedFlexoObject(FlexoObject object) {
		if (object instanceof FlexoConceptInstance) {
			List<FMLControlledDiagramElement<?, ?>> allFMLControlledDiagramElements = getDrawing().getFMLControlledDiagramElements(
					(FlexoConceptInstance) object);
			if (allFMLControlledDiagramElements != null && allFMLControlledDiagramElements.size() > 0) {
				// Return first one !
				return allFMLControlledDiagramElements.get(0);
			}
		}
		return super.getRepresentedFlexoObject(object);
	}
}
