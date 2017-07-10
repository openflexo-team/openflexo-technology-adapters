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

import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.FGEModelFactory;
import org.openflexo.fge.FGEModelFactoryImpl;
import org.openflexo.fge.swing.view.JDrawingView;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.gina.controller.FIBController;
import org.openflexo.gina.controller.FIBSelectable;
import org.openflexo.gina.model.widget.FIBCustom;
import org.openflexo.gina.model.widget.FIBCustom.FIBCustomComponent;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.selection.SelectionManager;
import org.openflexo.swing.CustomPopup.ApplyCancelListener;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;

// TODO: this should inherit from FIBJPanel
public class FlexoConceptPreviewComponent extends JPanel
		implements FIBCustomComponent<FlexoConcept>, FIBSelectable<GraphicalElementRole<?, ?>> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FlexoConceptPreviewComponent.class.getPackage().getName());

	private FlexoConcept flexoConcept;

	private SelectionManager selectionManager;
	private FlexoConceptPreviewController previewController;
	private final Vector<ApplyCancelListener> applyCancelListener;

	private final JLabel EMPTY_LABEL = new JLabel("<empty>");

	/**
	 * This is the FGE model factory shared by all preview components
	 */
	public static FGEModelFactory FACTORY = null;

	static {
		try {
			FACTORY = new FGEModelFactoryImpl();
		} catch (ModelDefinitionException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
	}

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
				previewController = new FlexoConceptPreviewController(object, selectionManager, FACTORY);
				JDrawingView<FlexoConcept> drawingView = previewController.getDrawingView();
				add(drawingView, BorderLayout.CENTER);
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
		if (previewController != null) {
			previewController.setSelectionManager(selectionManager);
		}
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
