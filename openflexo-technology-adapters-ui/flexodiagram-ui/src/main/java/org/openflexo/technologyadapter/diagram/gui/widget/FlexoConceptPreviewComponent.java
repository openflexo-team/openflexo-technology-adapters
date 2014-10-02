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
package org.openflexo.technologyadapter.diagram.gui.widget;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fib.controller.FIBController;
import org.openflexo.fib.controller.FIBSelectable;
import org.openflexo.fib.model.FIBCustom;
import org.openflexo.fib.model.FIBCustom.FIBCustomComponent;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.selection.SelectionManager;
import org.openflexo.swing.CustomPopup.ApplyCancelListener;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;

// TODO: this should inherit from FIBJPanel
public class FlexoConceptPreviewComponent extends JPanel implements FIBCustomComponent<FlexoConcept, JPanel>,
		FIBSelectable<GraphicalElementRole<?, ?>> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FlexoConceptPreviewComponent.class.getPackage().getName());

	private FlexoConcept flexoConcept;

	private SelectionManager selectionManager;
	private FlexoConceptPreviewController previewController;
	private final Vector<ApplyCancelListener> applyCancelListener;

	private final JLabel EMPTY_LABEL = new JLabel("<empty>");

	public FlexoConceptPreviewComponent() {
		super();
		setLayout(new BorderLayout());
		// add(EMPTY_LABEL,BorderLayout.CENTER);
		applyCancelListener = new Vector<ApplyCancelListener>();
	}

	@Override
	public void delete() {
		if (previewController != null) {
			previewController.delete();
		}
	}

	public FlexoConceptPreviewComponent(FlexoConcept flexoConcept) {
		this();
		setEditedObject(flexoConcept);
	}

	@Override
	public void init(FIBCustom component, FIBController controller) {
	}

	@Override
	public FlexoConcept getEditedObject() {
		return flexoConcept;
	}

	@Override
	public void setEditedObject(FlexoConcept object) {
		if (object != flexoConcept) {
			logger.fine("FlexoConceptPreview: setEditedObject: " + object);
			flexoConcept = object;
			if (previewController != null && object != null) {
				if (previewController.getDrawingView() != null) {
					remove(previewController.getDrawingView());
				}
				previewController.delete();
				previewController = null;
			}
			if (object != null) {
				previewController = new FlexoConceptPreviewController(object, selectionManager);
				add(previewController.getDrawingView(), BorderLayout.CENTER);
			}
			revalidate();
			repaint();
		}
	}

	public SelectionManager getSelectionManager() {
		return selectionManager;
	}

	public void setSelectionManager(SelectionManager selectionManager) {
		this.selectionManager = selectionManager;
		if (previewController != null) {
			previewController.setSelectionManager(selectionManager);
		}
	}

	@Override
	public JPanel getJComponent() {
		return this;
	}

	@Override
	public Class<FlexoConcept> getRepresentedType() {
		return FlexoConcept.class;
	}

	@Override
	public FlexoConcept getRevertValue() {
		return flexoConcept;
	}

	@Override
	public void setRevertValue(FlexoConcept object) {
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
	public GraphicalElementRole<?, ?> getSelected() {
		if (previewController != null) {
			if (previewController.getSelectedObjects() == null) {
				return null;
			}
			if (previewController.getSelectedObjects().size() > 0) {
				return (GraphicalElementRole<?, ?>) previewController.getSelectedObjects().get(0).getDrawable();
			}
			return null;
		}
		return null;
	}

	@Override
	public List<GraphicalElementRole<?, ?>> getSelection() {
		if (previewController != null) {
			if (previewController.getSelectedObjects() == null) {
				return null;
			}
		}
		if (previewController.getSelectedObjects().size() > 0) {
			List<GraphicalElementRole<?, ?>> returned = new ArrayList<GraphicalElementRole<?, ?>>();
			for (DrawingTreeNode<?, ?> dtn : previewController.getSelectedObjects()) {
				returned.add((GraphicalElementRole<?, ?>) dtn.getDrawable());
			}
			return returned;
		}
		return null;
	}

	@Override
	public boolean mayRepresent(GraphicalElementRole<?, ?> o) {
		return o instanceof FlexoRole && ((FlexoRole) o).getFlexoConcept() == flexoConcept;
	}

	@Override
	public void objectAddedToSelection(GraphicalElementRole<?, ?> o) {
		if (previewController != null) {
			previewController.fireObjectSelected(o);
		}
	}

	@Override
	public void objectRemovedFromSelection(GraphicalElementRole<?, ?> o) {
		if (previewController != null) {
			previewController.fireObjectDeselected(o);
		}
	}

	@Override
	public void selectionResetted() {
		if (previewController != null) {
			previewController.fireResetSelection();
		}
	}

	@Override
	public void addToSelection(GraphicalElementRole<?, ?> o) {
		if (previewController != null) {
			previewController.addToSelectedObjects(previewController.getDrawing().getDrawingTreeNode(o));
		}
	}

	@Override
	public void removeFromSelection(GraphicalElementRole<?, ?> o) {
		if (previewController != null) {
			previewController.removeFromSelectedObjects(previewController.getDrawing().getDrawingTreeNode(o));
		}
	}

	@Override
	public void resetSelection() {
		if (previewController != null) {
			previewController.clearSelection();
		}
	}

	@Override
	public boolean synchronizedWithSelection() {
		return true;
	}

	public FlexoConceptPreviewController getPreviewController() {
		return previewController;
	}
}
