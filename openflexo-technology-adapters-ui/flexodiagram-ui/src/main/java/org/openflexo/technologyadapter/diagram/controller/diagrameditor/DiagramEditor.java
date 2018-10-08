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

import java.awt.Component;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.openflexo.fge.Drawing;
import org.openflexo.fge.Drawing.ConnectorNode;
import org.openflexo.fge.Drawing.ContainerNode;
import org.openflexo.fge.Drawing.ShapeNode;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.LocationConstraints;
import org.openflexo.fge.control.actions.DrawShapeAction;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.fge.swing.control.SwingToolFactory;
import org.openflexo.fge.swing.control.tools.JDianaLayoutWidget;
import org.openflexo.fge.swing.control.tools.JDianaPalette;
import org.openflexo.fge.swing.control.tools.JDianaStyles;
import org.openflexo.fge.swing.control.tools.JDianaToolSelector;
import org.openflexo.fge.swing.view.JDrawingView;
import org.openflexo.foundation.action.FlexoUndoManager.FlexoActionCompoundEdit;
import org.openflexo.gina.FIBLibrary;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.swing.utils.JFIBDialog;
import org.openflexo.gina.swing.view.SwingViewFactory;
import org.openflexo.selection.SelectionManagingDianaEditor;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.AddShape;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.view.FlexoFrame;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.FlexoFIBController;

/**
 * Abstraction of editor of a {@link Diagram}
 * 
 * @author sylvain
 * 
 */
public abstract class DiagramEditor extends SelectionManagingDianaEditor<Diagram> {

	private static final Logger logger = Logger.getLogger(DiagramEditor.class.getPackage().getName());

	private final FlexoController flexoController;
	// private FreeDiagramModuleView moduleView;

	private JTabbedPane paletteView;
	private Vector<DiagramPalette> orderedPalettes;

	private JPanel toolsPanel;
	private JDianaToolSelector toolSelector;
	private JDianaLayoutWidget layoutWidget;
	private JDianaStyles stylesWidget;
	private JDianaPalette commonPalette;
	private DiagramEditorPaletteModel commonPaletteModel;
	private Hashtable<DiagramPalette, ContextualPalette> contextualPaletteModels;
	private Hashtable<DiagramPalette, JDianaPalette> contextualPalettes;

	private final SwingToolFactory swingToolFactory;

	public DiagramEditorPaletteModel makeCommonPalette() {
		return new CommonPalette(this);
	}

	public ContextualPalette makeContextualPalette(DiagramPalette palette) {
		return new ContextualPalette(palette, this);
	}

	public DiagramEditor(Drawing<Diagram> diagramDrawing, boolean readOnly, FlexoController controller, SwingToolFactory swingToolFactory) {
		super(diagramDrawing, controller != null ? controller.getSelectionManager() : null,
				((DiagramResource) diagramDrawing.getModel().getResource()).getFactory(), swingToolFactory);

		flexoController = controller;
		this.swingToolFactory = swingToolFactory;

		if (diagramDrawing.getModel().getDiagramSpecification() != null) {
			diagramDrawing.getModel().getDiagramSpecification().getPropertyChangeSupport().addPropertyChangeListener(this);
		}

		if (!readOnly) {

			toolSelector = swingToolFactory.makeDianaToolSelector(this);
			stylesWidget = swingToolFactory.makeDianaStyles();
			stylesWidget.attachToEditor(this);
			layoutWidget = swingToolFactory.makeDianaLayoutWidget();
			layoutWidget.attachToEditor(this);

			toolsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			toolsPanel.add(toolSelector.getComponent());
			// This panel is in its current state a duplication of the right side panel
			// toolsPanel.add(stylesWidget.getComponent());
			toolsPanel.add(layoutWidget.getComponent());

			commonPaletteModel = makeCommonPalette();

			commonPalette = swingToolFactory.makeDianaPalette(commonPaletteModel);
			commonPalette.attachToEditor(this);

			contextualPaletteModels = new Hashtable<>();
			contextualPalettes = new Hashtable<>();

			if (diagramDrawing.getModel().getDiagramSpecification() != null) {
				for (DiagramPalette palette : diagramDrawing.getModel().getDiagramSpecification().getPalettes()) {
					ContextualPalette contextualPaletteModel = makeContextualPalette(palette);
					contextualPaletteModels.put(palette, contextualPaletteModel);
					JDianaPalette dianaPalette = swingToolFactory.makeDianaPalette(contextualPaletteModel);
					dianaPalette.attachToEditor(this);
					contextualPalettes.put(palette, dianaPalette);
				}
			}
		}

		DrawShapeAction drawShapeAction = new DrawShapeAction() {
			@Override
			public void performedDrawNewShape(ShapeGraphicalRepresentation graphicalRepresentation, ContainerNode<?, ?> parentNode) {
				/*System.out.println("OK, perform draw new shape with " + graphicalRepresentation + " and parent: "
						+ parentGraphicalRepresentation);*/

				/*AddShape action = AddShape.actionType.makeNewAction(getDiagram(), null, getFlexoController().getEditor());
				action.setGraphicalRepresentation(graphicalRepresentation);
				action.setNewShapeName(graphicalRepresentation.getText());
				if (action.getNewShapeName() == null) {
					action.setNewShapeName(FlexoLocalization.localizedForKey("shape"));
				}
				
				action.doAction();*/

				handleNewShapeCreation(graphicalRepresentation, parentNode, graphicalRepresentation.getLocation(), true, true, true, true,
						false, false);

			}
		};

		setDrawShapeAction(drawShapeAction);

		setDrawCustomShapeAction(drawShapeAction);

		if (!readOnly) {
			toolSelector.handleToolChanged();
		}

	}

	public boolean handleNewShapeCreation(ShapeGraphicalRepresentation shapeGR, ContainerNode<?, ?> parentNode, FGEPoint dropLocation,
			boolean applyCurrentForeground, boolean applyCurrentBackground, boolean applyCurrentTextStyle, boolean applyCurrentShadowStyle,
			boolean isImage, boolean resize) {

		DiagramContainerElement<?> container = (DiagramContainerElement<?>) parentNode.getDrawable();

		FlexoActionCompoundEdit edit = (FlexoActionCompoundEdit) getUndoManager().startRecording("Making new shape");

		shapeGR.setIsReadOnly(false);
		shapeGR.setIsFocusable(true);
		shapeGR.setIsSelectable(true);
		shapeGR.setLocationConstraints(LocationConstraints.FREELY_MOVABLE);

		if (resize) {
			if (shapeGR.getShapeSpecification().getShapeType() == ShapeType.SQUARE
					|| shapeGR.getShapeSpecification().getShapeType() == ShapeType.CIRCLE) {
				shapeGR.setWidth(40);
				shapeGR.setHeight(40);
			}
			else {
				shapeGR.setWidth(50);
				shapeGR.setHeight(40);
			}
		}
		if (applyCurrentForeground) {
			shapeGR.setForeground(getInspectedForegroundStyle().cloneStyle());
		}
		if (applyCurrentBackground) {
			shapeGR.setBackground(getInspectedBackgroundStyle().cloneStyle());
		}
		if (applyCurrentTextStyle) {
			shapeGR.setTextStyle(getInspectedTextStyle().cloneStyle());
		}
		if (applyCurrentShadowStyle) {
			shapeGR.setShadowStyle(getInspectedShadowStyle().cloneStyle());
		}

		shapeGR.setX(dropLocation.x);
		shapeGR.setY(dropLocation.y);
		// shapeGR.setAllowToLeaveBounds(true);

		if (isImage) {
			FIBComponent fibComponent = getFIBLibrary().retrieveFIBComponent(DiagramCst.IMPORT_IMAGE_FILE_DIALOG_FIB);
			JFIBDialog dialog = JFIBDialog.instanciateAndShowDialog(fibComponent, shapeGR, FlexoFrame.getActiveFrame(), true,
					new FlexoFIBController(fibComponent, SwingViewFactory.INSTANCE, getFlexoController()));
		}

		AddShape action = AddShape.actionType.makeNewAction(container, null, getFlexoController().getEditor());
		action.setGraphicalRepresentation(shapeGR);
		action.setNewShapeName(shapeGR.getText());
		if (action.getNewShapeName() == null) {
			action.setNewShapeName(action.getLocales().localizedForKey("shape"));
		}

		// action.nameSetToNull = true;
		// action.setNewShapeName(FlexoLocalization.localizedForKey("unnamed"));

		action.setCompoundEdit(edit);
		action.doAction();

		DiagramShape newShape = action.getNewShape();

		// getEditor().getUndoManager().stopRecording(edit);

		setCurrentTool(EditorTool.SelectionTool);
		setSelectedObject(getDrawing().getDrawingTreeNode(newShape));

		return action.hasActionExecutionSucceeded();

	}

	@Override
	public DiagramFactory getFactory() {
		return (DiagramFactory) super.getFactory();
	}

	@Override
	public void delete() {
		if (getDiagram() != null && getDiagram().getDiagramSpecification() != null) {
			getDiagram().getDiagramSpecification().getPropertyChangeSupport().removePropertyChangeListener(this);
		}
		/*if (flexoController != null) {
			if (getDrawingView() != null && moduleView != null) {
				flexoController.removeModuleView(moduleView);
			}
		}*/
		super.delete();
		getDrawing().delete();
	}

	public JPanel getToolsPanel() {
		return toolsPanel;
	}

	@Override
	public JDrawingView<Diagram> makeDrawingView() {
		return new DiagramView(this);
	}

	public FlexoController getFlexoController() {
		return flexoController;
	}

	@Override
	public DiagramView getDrawingView() {
		return (DiagramView) super.getDrawingView();
	}

	/*public FreeDiagramModuleView getModuleView() {
		if (moduleView == null) {
			moduleView = new FreeDiagramModuleView(this, flexoController.getCurrentPerspective());
		}
		return moduleView;
	}*/

	public JDianaPalette getCommonPalette() {
		return commonPalette;
	}

	public String getCommonPaletteTitle() {
		return "Common";
	}

	public Collection<ContextualPalette> getContextualPalettes() {
		return contextualPaletteModels.values();
	}

	public JTabbedPane getPaletteView() {
		if (paletteView == null) {
			paletteView = makePaletteView();
		}
		return paletteView;
	}

	protected JTabbedPane makePaletteView() {
		// logger.info("Building PaletteView with " + contextualPalettes);

		paletteView = new JTabbedPane();
		orderedPalettes = new Vector<>(contextualPalettes.keySet());
		Collections.sort(orderedPalettes);

		for (DiagramPalette palette : orderedPalettes) {
			paletteView.add(palette.getName(), contextualPalettes.get(palette).getPaletteViewInScrollPane());
		}
		paletteView.add(getCommonPaletteTitle(), getCommonPalette().getPaletteViewInScrollPane());
		paletteView.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (paletteView.getSelectedIndex() < orderedPalettes.size()) {
					activatePalette(contextualPalettes.get(orderedPalettes.elementAt(paletteView.getSelectedIndex())));
				}
				else if (paletteView.getSelectedIndex() == orderedPalettes.size()) {
					activatePalette(getCommonPalette());
				}
			}
		});
		paletteView.setSelectedIndex(0);
		if (orderedPalettes.size() > 0) {
			activatePalette(contextualPalettes.get(orderedPalettes.firstElement()));
		}
		else {
			activatePalette(getCommonPalette());
		}
		return paletteView;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
		// Prevent DiagramSpecification load (deserialization) during evt management thats causes deadlock
		if (evt.getSource() != getDrawing()) {
			if (evt.getSource() == getDiagram().getDiagramSpecification() && paletteView != null) {
				if (evt.getPropertyName().equals(DiagramSpecification.PALETTES_KEY)) {
					if (evt.getNewValue() instanceof DiagramPalette) {
						logger.info("Handling palette added");
						DiagramPalette palette = (DiagramPalette) evt.getNewValue();
						ContextualPalette newContextualPalette = new ContextualPalette(palette, this);
						contextualPaletteModels.put(palette, newContextualPalette);
						JDianaPalette dianaPalette = swingToolFactory.makeDianaPalette(newContextualPalette);
						dianaPalette.attachToEditor(this);
						contextualPalettes.put(palette, dianaPalette);
						paletteView.add(palette.getName(), dianaPalette.getPaletteViewInScrollPane());
						paletteView.revalidate();
						paletteView.repaint();
					}
					else if (evt.getOldValue() instanceof DiagramPalette) {
						logger.info("Handling palette removed");
						DiagramPalette palette = (DiagramPalette) evt.getOldValue();
						JDianaPalette removedPalette = contextualPalettes.get(palette);
						removedPalette.delete();
						DiagramEditorPaletteModel removedPaletteModel = contextualPaletteModels.get(palette);
						removedPaletteModel.delete();
						// unregisterPalette(removedPalette);
						contextualPalettes.remove(palette);
						contextualPaletteModels.remove(palette);
						paletteView.remove(removedPalette.getPaletteViewInScrollPane());
						paletteView.revalidate();
						paletteView.repaint();
					}
				}
			}
		}
	}

	protected void updatePalette(DiagramPalette palette, JDianaPalette oldPaletteView) {
		if (getPaletteView() != null) {
			// System.out.println("update palette with " + oldPaletteView);
			int index = -1;
			for (int i = 0; i < getPaletteView().getComponentCount(); i++) {
				// System.out.println("> " + paletteView.getComponentAt(i));
				Component c = getPaletteView().getComponentAt(i);
				if (SwingUtilities.isDescendingFrom(oldPaletteView.getComponent(), c)) {
					index = i;
					System.out.println("Found index " + index);
				}
			}
			if (index > -1) {
				getPaletteView().remove(getPaletteView().getComponentAt(index));
				JDianaPalette p = contextualPalettes.get(palette);
				p.updatePalette();
				getPaletteView().insertTab(palette.getName(), null, p.getPaletteViewInScrollPane(), null, index);
			}
			getPaletteView().revalidate();
			getPaletteView().repaint();
		}
		else {
			logger.warning("updatePalette() called with null value for paletteView");
		}
	}

	public Diagram getDiagram() {
		return getDrawing().getModel();
	}

	protected DiagramShape getShapeForShapeNode(ShapeNode<?> shapeNode) {
		if (shapeNode.getDrawable() instanceof DiagramShape) {
			return (DiagramShape) shapeNode.getDrawable();
		}
		return null;
	}

	protected DiagramConnector getConnectorForConnectorNode(ConnectorNode<?> connectorNode) {
		if (connectorNode.getDrawable() instanceof DiagramConnector) {
			return (DiagramConnector) connectorNode.getDrawable();
		}
		return null;
	}

	public FIBLibrary getFIBLibrary() {
		if (getFlexoController() != null) {
			return getFlexoController().getApplicationFIBLibraryService().getApplicationFIBLibrary();
		}
		return null;
	}
}
