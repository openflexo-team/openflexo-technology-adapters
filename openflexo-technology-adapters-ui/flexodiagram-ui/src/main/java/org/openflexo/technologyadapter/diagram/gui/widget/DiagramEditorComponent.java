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

package org.openflexo.technologyadapter.diagram.gui.widget;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.gina.controller.FIBController;
import org.openflexo.gina.controller.FIBSelectable;
import org.openflexo.gina.model.widget.FIBCustom;
import org.openflexo.gina.model.widget.FIBCustom.FIBCustomComponent;
import org.openflexo.selection.SelectionManager;
import org.openflexo.swing.CustomPopup.ApplyCancelListener;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramEditor;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.view.controller.FlexoController;

/**
 * An implementation of a FIBCustomComponent for a {@link Diagram} editor, this JComponent embed a FreeDiagramEditor
 * 
 * @author sylvain
 *
 */
@SuppressWarnings("serial")
public class DiagramEditorComponent extends JPanel implements FIBCustomComponent<Diagram>, FIBSelectable<DiagramElement<?>> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DiagramEditorComponent.class.getPackage().getName());

	private Diagram diagram;

	private SelectionManager selectionManager;
	private FreeDiagramEditor diagramEditor;
	private final Vector<ApplyCancelListener> applyCancelListener;

	private final JLabel EMPTY_LABEL = new JLabel("<empty>");

	public DiagramEditorComponent() {
		super();
		setLayout(new BorderLayout());
		// add(EMPTY_LABEL,BorderLayout.CENTER);
		applyCancelListener = new Vector<ApplyCancelListener>();
	}

	@Override
	public void delete() {
		if (diagramEditor != null) {
			diagramEditor.delete();
		}
	}

	public DiagramEditorComponent(Diagram diagram) {
		this();
		setEditedObject(diagram);
	}

	@Override
	public void init(FIBCustom component, FIBController controller) {
	}

	@Override
	public Diagram getEditedObject() {
		return diagram;
	}

	// private FlexoController flexoController

	@Override
	public void setEditedObject(Diagram object) {

		logger.info("************************** setEditedObject with " + object);

		if (object != diagram) {
			logger.fine("DiagramEditorComponent: setEditedObject: " + object);
			diagram = object;
			if (diagramEditor != null && object != null) {
				if (diagramEditor.getDrawingView() != null) {
					remove(diagramEditor.getDrawingView());
				}
				removeAll();
				diagramEditor.delete();
				diagramEditor = null;
			}
			if (object != null) {
				// What about the selection manager ???
				DiagramTechnologyAdapterController tac = (DiagramTechnologyAdapterController) getFlexoController()
						.getTechnologyAdapterController(DiagramTechnologyAdapter.class);
				diagramEditor = new FreeDiagramEditor(object, false, getFlexoController(), tac.getToolFactory());

				add(diagramEditor.getToolsPanel(), BorderLayout.NORTH);
				add(new JScrollPane(diagramEditor.getDrawingView()), BorderLayout.CENTER);

			}
			revalidate();
			repaint();
		}
	}

	public SelectionManager getSelectionManager() {
		return selectionManager;
	}

	@CustomComponentParameter(name = "selectionManager", type = CustomComponentParameter.Type.OPTIONAL)
	public void setSelectionManager(SelectionManager selectionManager) {
		this.selectionManager = selectionManager;
		if (diagramEditor != null) {
			diagramEditor.setSelectionManager(selectionManager);
		}
	}

	private FlexoController flexoController;

	public FlexoController getFlexoController() {
		return flexoController;
	}

	@CustomComponentParameter(name = "flexoController", type = CustomComponentParameter.Type.OPTIONAL)
	public void setFlexoController(FlexoController flexoController) {
		this.flexoController = flexoController;
		System.out.println("---------- YES ! setFlexoController with " + flexoController);
	}

	@Override
	public Class<Diagram> getRepresentedType() {
		return Diagram.class;
	}

	@Override
	public Diagram getRevertValue() {
		return diagram;
	}

	@Override
	public void setRevertValue(Diagram object) {
	}

	@Override
	public void addApplyCancelListener(ApplyCancelListener l) {
		applyCancelListener.add(l);
	}

	@Override
	public void removeApplyCancelListener(ApplyCancelListener l) {
		applyCancelListener.remove(l);
	}

	@Override
	public DiagramElement<?> getSelected() {
		if (diagramEditor != null) {
			if (diagramEditor.getSelectedObjects() == null) {
				return null;
			}
			if (diagramEditor.getSelectedObjects().size() > 0) {
				return (DiagramElement<?>) diagramEditor.getSelectedObjects().get(0).getDrawable();
			}
			return null;
		}
		return null;
	}

	@Override
	public List<DiagramElement<?>> getSelection() {
		if (diagramEditor != null) {
			if (diagramEditor.getSelectedObjects() == null) {
				return null;
			}
		}
		if (diagramEditor.getSelectedObjects().size() > 0) {
			List<DiagramElement<?>> returned = new ArrayList<>();
			for (DrawingTreeNode<?, ?> dtn : diagramEditor.getSelectedObjects()) {
				returned.add((DiagramElement<?>) dtn.getDrawable());
			}
			return returned;
		}
		return null;
	}

	@Override
	public boolean mayRepresent(DiagramElement<?> o) {
		// return o instanceof FlexoRole && ((FlexoRole) o).getFlexoConcept() == diagram;
		return false;
	}

	@Override
	public void objectAddedToSelection(DiagramElement<?> o) {
		if (diagramEditor != null) {
			diagramEditor.fireObjectSelected(o);
		}
	}

	@Override
	public void objectRemovedFromSelection(DiagramElement<?> o) {
		if (diagramEditor != null) {
			diagramEditor.fireObjectDeselected(o);
		}
	}

	@Override
	public void selectionResetted() {
		if (diagramEditor != null) {
			diagramEditor.fireResetSelection();
		}
	}

	@Override
	public void addToSelection(DiagramElement<?> o) {
		if (diagramEditor != null) {
			diagramEditor.addToSelectedObjects(diagramEditor.getDrawing().getDrawingTreeNode(o));
		}
	}

	@Override
	public void removeFromSelection(DiagramElement<?> o) {
		if (diagramEditor != null) {
			diagramEditor.removeFromSelectedObjects(diagramEditor.getDrawing().getDrawingTreeNode(o));
		}
	}

	@Override
	public void resetSelection() {
		if (diagramEditor != null) {
			diagramEditor.clearSelection();
		}
	}

	@Override
	public boolean synchronizedWithSelection() {
		return true;
	}

	public FreeDiagramEditor getDiagramEditor() {
		return diagramEditor;
	}

}
