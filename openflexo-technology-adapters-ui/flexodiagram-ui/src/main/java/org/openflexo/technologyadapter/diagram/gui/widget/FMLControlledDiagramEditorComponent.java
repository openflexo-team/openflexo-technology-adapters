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
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.openflexo.diana.Drawing.DrawingTreeNode;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.gina.controller.FIBController;
import org.openflexo.gina.controller.FIBSelectable;
import org.openflexo.gina.model.widget.FIBCustom;
import org.openflexo.gina.model.widget.FIBCustom.FIBCustomComponent;
import org.openflexo.selection.SelectionManager;
import org.openflexo.swing.CustomPopup.ApplyCancelListener;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.toolbox.HasPropertyChangeSupport;
import org.openflexo.view.controller.FlexoController;

/**
 * An implementation of a FIBCustomComponent for a FML-controlled diagram editor<br>
 * This JComponent embed a {@link FMLControlledDiagramEditor}<br>
 * Edited object is a {@link FMLRTVirtualModelInstance} with the FML-controlled diagram nature
 * 
 * @author sylvain
 *
 */
@SuppressWarnings("serial")
public class FMLControlledDiagramEditorComponent extends JPanel
		implements FIBCustomComponent<FMLRTVirtualModelInstance>, FIBSelectable<FlexoConceptInstance>, HasPropertyChangeSupport {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FMLControlledDiagramEditorComponent.class.getPackage().getName());

	public static final String DIAGRAM_EDITOR = "diagramEditor";

	private FMLRTVirtualModelInstance virtualModelInstance;

	private SelectionManager selectionManager;
	private FMLControlledDiagramEditor diagramEditor;
	private final Vector<ApplyCancelListener> applyCancelListener;

	private String infoMessage = " CTRL-drag to draw edges";

	private JPanel bottomPanel;

	private PropertyChangeSupport pcSupport;

	public FMLControlledDiagramEditorComponent() {
		super();
		setLayout(new BorderLayout());
		pcSupport = new PropertyChangeSupport(this);
		// add(EMPTY_LABEL,BorderLayout.CENTER);
		applyCancelListener = new Vector<>();
	}

	public FMLControlledDiagramEditorComponent(FMLRTVirtualModelInstance diagramVMI) {
		this();
		setEditedObject(diagramVMI);
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		return pcSupport;
	}

	@Override
	public String getDeletedProperty() {
		return "deleted";
	}

	@Override
	public void delete() {
		if (diagramEditor != null) {
			diagramEditor.delete();
		}
		getPropertyChangeSupport().firePropertyChange(getDeletedProperty(), false, true);
	}

	@Override
	public void init(FIBCustom component, FIBController controller) {
	}

	@Override
	public FMLRTVirtualModelInstance getEditedObject() {
		return virtualModelInstance;
	}

	// private FlexoController flexoController

	/**
	 * Sets (update if different) edited diagram
	 */
	@Override
	public void setEditedObject(FMLRTVirtualModelInstance diagramVMI) {

		System.out.println("Hop, avec " + diagramVMI);

		if (virtualModelInstance != diagramVMI || getDiagramEditor() == null) {
			updateEditedObject(diagramVMI);
		}
	}

	/**
	 * Force update edited Diagram<br>
	 * 
	 * @param diagramToRepresent
	 */
	private void updateEditedObject(FMLRTVirtualModelInstance diagramVMI) {

		FMLControlledDiagramEditor oldEditor = diagramEditor;

		System.out.println("DiagramEditorComponent: setEditedObject: " + diagramVMI);
		virtualModelInstance = diagramVMI;
		if (diagramEditor != null && diagramVMI != null) {
			if (diagramEditor.getDrawingView() != null) {
				remove(diagramEditor.getDrawingView());
			}
			removeAll();
			diagramEditor.delete();
			diagramEditor = null;
		}
		System.out.println("Controller=" + getFlexoController());
		if (diagramVMI != null && getFlexoController() != null) {
			// What about the selection manager ???
			DiagramTechnologyAdapterController tac = (DiagramTechnologyAdapterController) getFlexoController()
					.getTechnologyAdapterController(DiagramTechnologyAdapter.class);
			System.out.println("On construit l'editeur...");
			diagramEditor = new FMLControlledDiagramEditor(diagramVMI, false, getFlexoController(), tac.getToolFactory());

			add(diagramEditor.getToolsPanel(), BorderLayout.NORTH);
			add(new JScrollPane(diagramEditor.getDrawingView()), BorderLayout.CENTER);
			bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.add(getFlexoController().makeInfoLabel(), BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			getFlexoController().setInfoMessage(getInfoMessage(), false);

		}
		revalidate();
		repaint();

		getPropertyChangeSupport().firePropertyChange(DIAGRAM_EDITOR, oldEditor, diagramEditor);
	}

	public JPanel getBottomPanel() {
		return bottomPanel;
	}

	public String getInfoMessage() {
		return infoMessage;
	}

	@CustomComponentParameter(name = "infoMessage", type = CustomComponentParameter.Type.OPTIONAL)
	public void setInfoMessage(String infoMessage) {
		if ((infoMessage == null && this.infoMessage != null) || (infoMessage != null && !infoMessage.equals(this.infoMessage))) {
			this.infoMessage = infoMessage;
		}
	}

	public SelectionManager getSelectionManager() {
		if (selectionManager == null && flexoController != null) {
			return flexoController.getSelectionManager();
		}
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
		if (diagramEditor != null && diagramEditor.getSelectionManager() == null) {
			diagramEditor.setSelectionManager(flexoController.getSelectionManager());
		}
	}

	@Override
	public Class<FMLRTVirtualModelInstance> getRepresentedType() {
		return FMLRTVirtualModelInstance.class;
	}

	@Override
	public FMLRTVirtualModelInstance getRevertValue() {
		return virtualModelInstance;
	}

	@Override
	public void setRevertValue(FMLRTVirtualModelInstance object) {
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
	public FlexoConceptInstance getSelected() {
		if (diagramEditor != null) {
			if (diagramEditor.getSelectedObjects() == null) {
				return null;
			}
			if (diagramEditor.getSelectedObjects().size() > 0) {
				return (FlexoConceptInstance) diagramEditor.getSelectedObjects().get(0).getDrawable();
			}
			return null;
		}
		return null;
	}

	@Override
	public List<FlexoConceptInstance> getSelection() {
		if (diagramEditor != null) {
			if (diagramEditor.getSelectedObjects() == null) {
				return null;
			}
		}
		if (diagramEditor.getSelectedObjects().size() > 0) {
			List<FlexoConceptInstance> returned = new ArrayList<>();
			for (DrawingTreeNode<?, ?> dtn : diagramEditor.getSelectedObjects()) {
				returned.add((FlexoConceptInstance) dtn.getDrawable());
			}
			return returned;
		}
		return null;
	}

	@Override
	public boolean mayRepresent(FlexoConceptInstance o) {
		// return o instanceof FlexoRole && ((FlexoRole) o).getFlexoConcept() == diagram;
		// return diagramEditor.get;
		return diagramEditor.getDrawing().getDrawingTreeNode(o) != null;
	}

	@Override
	public void objectAddedToSelection(FlexoConceptInstance o) {
		if (diagramEditor != null) {
			diagramEditor.fireObjectSelected(o);
		}
	}

	@Override
	public void objectRemovedFromSelection(FlexoConceptInstance o) {
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
	public void addToSelection(FlexoConceptInstance o) {
		if (diagramEditor != null) {
			diagramEditor.addToSelectedObjects(diagramEditor.getDrawing().getDrawingTreeNode(o));
		}
	}

	@Override
	public void removeFromSelection(FlexoConceptInstance o) {
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

	public FMLControlledDiagramEditor getDiagramEditor() {
		return diagramEditor;
	}

}
