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

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.diana.Drawing.ConnectorNode;
import org.openflexo.diana.Drawing.DrawingTreeNode;
import org.openflexo.diana.Drawing.RootNode;
import org.openflexo.diana.Drawing.ShapeNode;
import org.openflexo.diana.swing.control.SwingToolFactory;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.technologyadapter.diagram.controller.action.FMLControlledDiagramPasteHandler;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.view.controller.FlexoController;

/**
 * Editor of a Diagram in controlled mode: the Diagram is edited in a federated mode<br>
 * There is a VirtualModel controlling the edition of the diagram
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramEditor extends DiagramEditor {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FMLControlledDiagramEditor.class.getPackage().getName());

	private final FMLControlledDiagramPasteHandler pasteHandler;

	public FMLControlledDiagramEditor(FMLRTVirtualModelInstance vmInstance, boolean readOnly, FlexoController controller,
			SwingToolFactory swingToolFactory) {
		super(new FMLControlledDiagramDrawing(vmInstance, readOnly), readOnly, controller, swingToolFactory);
		// We have to switch properly between those paste handlers
		// AND do not forget to destroy them
		pasteHandler = new FMLControlledDiagramPasteHandler(vmInstance, this);
	}

	public FMLControlledDiagramPasteHandler getPasteHandler() {
		return pasteHandler;
	}

	@Override
	public void delete() {
		getFlexoController().getEditingContext().unregisterPasteHandler(pasteHandler);
		super.delete();
	}

	public FMLRTVirtualModelInstance getVirtualModelInstance() {
		return getDrawing().getVirtualModelInstance();
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
		else if (node.getDrawable() instanceof Diagram && node instanceof RootNode) {
			return getVirtualModelInstance();
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
			List<FMLControlledDiagramElement<?, ?>> allFMLControlledDiagramElements = getDrawing()
					.getFMLControlledDiagramElements((FlexoConceptInstance) object);
			if (allFMLControlledDiagramElements != null && allFMLControlledDiagramElements.size() > 0) {
				// Return first one !
				return allFMLControlledDiagramElements.get(0);
			}
		}
		return super.getRepresentedFlexoObject(object);
	}

	@Override
	protected DiagramShape getShapeForShapeNode(ShapeNode<?> shapeNode) {
		if (shapeNode.getDrawable() instanceof FMLControlledDiagramShape) {
			return ((FMLControlledDiagramShape) shapeNode.getDrawable()).getDiagramElement();
		}
		return super.getShapeForShapeNode(shapeNode);
	}

	@Override
	protected DiagramConnector getConnectorForConnectorNode(ConnectorNode<?> connectorNode) {
		if (connectorNode.getDrawable() instanceof FMLControlledDiagramConnector) {
			return ((FMLControlledDiagramConnector) connectorNode.getDrawable()).getDiagramElement();
		}
		return super.getConnectorForConnectorNode(connectorNode);
	}
}
