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

import org.openflexo.diana.Drawing.DrawingTreeNode;
import org.openflexo.gina.controller.FIBController;
import org.openflexo.gina.controller.FIBSelectable;
import org.openflexo.gina.model.widget.FIBCustom;
import org.openflexo.gina.model.widget.FIBCustom.FIBCustomComponent;
import org.openflexo.selection.SelectionManager;
import org.openflexo.swing.CustomPopup.ApplyCancelListener;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.controller.paletteeditor.DiagramPaletteEditor;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.view.controller.FlexoController;

/**
 * An implementation of a FIBCustomComponent for a {@link DiagramPalette} editor, this JComponent embed a {@link DiagramPaletteEditor}
 * 
 * @author sylvain
 *
 */
@SuppressWarnings("serial")
public class PaletteEditorComponent extends JPanel implements FIBCustomComponent<DiagramPalette>, FIBSelectable<DiagramPaletteElement> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PaletteEditorComponent.class.getPackage().getName());

	private DiagramPalette diagramPalette;

	private SelectionManager selectionManager;
	private DiagramPaletteEditor diagramPaletteEditor;
	private final Vector<ApplyCancelListener> applyCancelListener;

	private final JLabel EMPTY_LABEL = new JLabel("<empty>");

	public PaletteEditorComponent() {
		super();
		setLayout(new BorderLayout());
		// add(EMPTY_LABEL,BorderLayout.CENTER);
		applyCancelListener = new Vector<>();
	}

	@Override
	public void delete() {
		if (diagramPaletteEditor != null) {
			diagramPaletteEditor.delete();
		}
	}

	public PaletteEditorComponent(DiagramPalette diagramPalette) {
		this();
		setEditedObject(diagramPalette);
	}

	@Override
	public void init(FIBCustom component, FIBController controller) {
	}

	@Override
	public DiagramPalette getEditedObject() {
		return diagramPalette;
	}

	@Override
	public void setEditedObject(DiagramPalette diagramPaletteToRepresent) {

		if ((diagramPaletteToRepresent != diagramPalette) || (getPaletteEditor() == null)) {
			logger.fine("PaletteEditorComponent: setEditedObject: " + diagramPaletteToRepresent);
			updateEditedObject(diagramPaletteToRepresent);
		}
	}

	/**
	 * Force update edited DiagramPalette<br>
	 * 
	 * @param diagramPaletteToRepresent
	 */
	private void updateEditedObject(DiagramPalette diagramPaletteToRepresent) {
		diagramPalette = diagramPaletteToRepresent;
		if (diagramPaletteEditor != null && diagramPaletteToRepresent != null) {
			if (diagramPaletteEditor.getDrawingView() != null) {
				remove(diagramPaletteEditor.getDrawingView());
			}
			removeAll();
			diagramPaletteEditor.delete();
			diagramPaletteEditor = null;
		}
		if (diagramPaletteToRepresent != null && getFlexoController() != null) {
			// What about the selection manager ???
			System.out.println("flexoController=" + getFlexoController());
			DiagramTechnologyAdapterController tac = (DiagramTechnologyAdapterController) getFlexoController()
					.getTechnologyAdapterController(DiagramTechnologyAdapter.class);
			diagramPaletteEditor = new DiagramPaletteEditor(diagramPaletteToRepresent, false, getFlexoController(), tac.getToolFactory());

			add(new JScrollPane(diagramPaletteEditor.getDrawingView()), BorderLayout.CENTER);

		}
		revalidate();
		repaint();
	}

	public SelectionManager getSelectionManager() {
		return selectionManager;
	}

	@CustomComponentParameter(name = "selectionManager", type = CustomComponentParameter.Type.OPTIONAL)
	public void setSelectionManager(SelectionManager selectionManager) {
		this.selectionManager = selectionManager;
		if (diagramPaletteEditor != null) {
			diagramPaletteEditor.setSelectionManager(selectionManager);
		}
	}

	private FlexoController flexoController;

	public FlexoController getFlexoController() {
		return flexoController;
	}

	@CustomComponentParameter(name = "flexoController", type = CustomComponentParameter.Type.OPTIONAL)
	public void setFlexoController(FlexoController flexoController) {
		this.flexoController = flexoController;
	}

	@Override
	public Class<DiagramPalette> getRepresentedType() {
		return DiagramPalette.class;
	}

	@Override
	public DiagramPalette getRevertValue() {
		return diagramPalette;
	}

	@Override
	public void setRevertValue(DiagramPalette object) {
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
	public DiagramPaletteElement getSelected() {
		if (diagramPaletteEditor != null) {
			if (diagramPaletteEditor.getSelectedObjects() == null) {
				return null;
			}
			if (diagramPaletteEditor.getSelectedObjects().size() > 0) {
				return (DiagramPaletteElement) diagramPaletteEditor.getSelectedObjects().get(0).getDrawable();
			}
			return null;
		}
		return null;
	}

	@Override
	public List<DiagramPaletteElement> getSelection() {
		if (diagramPaletteEditor != null) {
			if (diagramPaletteEditor.getSelectedObjects() == null) {
				return null;
			}
		}
		if (diagramPaletteEditor.getSelectedObjects().size() > 0) {
			List<DiagramPaletteElement> returned = new ArrayList<>();
			for (DrawingTreeNode<?, ?> dtn : diagramPaletteEditor.getSelectedObjects()) {
				returned.add((DiagramPaletteElement) dtn.getDrawable());
			}
			return returned;
		}
		return null;
	}

	@Override
	public boolean mayRepresent(DiagramPaletteElement o) {
		return false;
	}

	@Override
	public void objectAddedToSelection(DiagramPaletteElement o) {
		if (diagramPaletteEditor != null) {
			diagramPaletteEditor.fireObjectSelected(o);
		}
	}

	@Override
	public void objectRemovedFromSelection(DiagramPaletteElement o) {
		if (diagramPaletteEditor != null) {
			diagramPaletteEditor.fireObjectDeselected(o);
		}
	}

	@Override
	public void selectionResetted() {
		if (diagramPaletteEditor != null) {
			diagramPaletteEditor.fireResetSelection();
		}
	}

	@Override
	public void addToSelection(DiagramPaletteElement o) {
		if (diagramPaletteEditor != null) {
			diagramPaletteEditor.addToSelectedObjects(diagramPaletteEditor.getDrawing().getDrawingTreeNode(o));
		}
	}

	@Override
	public void removeFromSelection(DiagramPaletteElement o) {
		if (diagramPaletteEditor != null) {
			diagramPaletteEditor.removeFromSelectedObjects(diagramPaletteEditor.getDrawing().getDrawingTreeNode(o));
		}
	}

	@Override
	public void resetSelection() {
		if (diagramPaletteEditor != null) {
			diagramPaletteEditor.clearSelection();
		}
	}

	@Override
	public boolean synchronizedWithSelection() {
		return true;
	}

	public DiagramPaletteEditor getPaletteEditor() {
		return diagramPaletteEditor;
	}

}
