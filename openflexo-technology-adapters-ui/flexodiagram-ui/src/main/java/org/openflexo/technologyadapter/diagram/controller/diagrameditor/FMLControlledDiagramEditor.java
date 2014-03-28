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

import org.openflexo.fge.swing.control.SwingToolFactory;
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
}
