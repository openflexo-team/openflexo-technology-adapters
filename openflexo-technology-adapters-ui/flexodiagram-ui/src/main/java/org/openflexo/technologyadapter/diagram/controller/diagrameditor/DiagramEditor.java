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

import java.awt.Component;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import org.openflexo.fge.control.actions.DrawShapeAction;
import org.openflexo.fge.swing.control.SwingToolFactory;
import org.openflexo.fge.swing.control.tools.JDianaLayoutWidget;
import org.openflexo.fge.swing.control.tools.JDianaPalette;
import org.openflexo.fge.swing.control.tools.JDianaStyles;
import org.openflexo.fge.swing.control.tools.JDianaToolSelector;
import org.openflexo.fge.swing.view.JDrawingView;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.selection.SelectionManagingDianaEditor;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.AddShape;
import org.openflexo.technologyadapter.diagram.rm.DiagramResource;
import org.openflexo.view.controller.FlexoController;

/**
 * Abstraction of editor of a {@link Diagram}
 * 
 * @author sylvain
 * 
 */
public abstract class DiagramEditor extends SelectionManagingDianaEditor<Diagram> implements PropertyChangeListener /* GraphicalFlexoObserver*/{

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
	private AbstractDiagramPalette commonPaletteModel;
	private Hashtable<DiagramPalette, AbstractDiagramPalette> contextualPaletteModels;
	private Hashtable<DiagramPalette, JDianaPalette> contextualPalettes;

	private final SwingToolFactory swingToolFactory;

	public AbstractDiagramPalette makeCommonPalette() {
		return new CommonPalette(this);
	}

	public DiagramEditor(Drawing<Diagram> diagramDrawing, boolean readOnly, FlexoController controller, SwingToolFactory swingToolFactory) {
		super(diagramDrawing, controller != null ? controller.getSelectionManager() : null, ((DiagramResource) diagramDrawing.getModel()
				.getResource()).getFactory(), swingToolFactory);

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
			toolsPanel.add(stylesWidget.getComponent());
			toolsPanel.add(layoutWidget.getComponent());

			commonPaletteModel = makeCommonPalette();

			commonPalette = swingToolFactory.makeDianaPalette(commonPaletteModel);
			commonPalette.attachToEditor(this);

			contextualPaletteModels = new Hashtable<DiagramPalette, AbstractDiagramPalette>();
			contextualPalettes = new Hashtable<DiagramPalette, JDianaPalette>();

			System.out.println("Hop, je regarde les palettes pour le DiagramSpecification="
					+ diagramDrawing.getModel().getDiagramSpecification());

			if (diagramDrawing.getModel().getDiagramSpecification() != null) {
				System.out.println("Les palettes=" + diagramDrawing.getModel().getDiagramSpecification().getPalettes());
				for (DiagramPalette palette : diagramDrawing.getModel().getDiagramSpecification().getPalettes()) {
					System.out.println("Palette " + palette);
					System.out.println("elements " + palette.getElements());
					for (DiagramPaletteElement e : palette.getElements()) {
						System.out.println("e: " + e + " gr=" + e.getGraphicalRepresentation());
					}
					ContextualPalette contextualPaletteModel = new ContextualPalette(palette, this);
					contextualPaletteModels.put(palette, contextualPaletteModel);
					JDianaPalette dianaPalette = swingToolFactory.makeDianaPalette(contextualPaletteModel);
					dianaPalette.attachToEditor(this);
					contextualPalettes.put(palette, dianaPalette);
				}
			}
		}

		setDrawShapeAction(new DrawShapeAction() {
			@Override
			public void performedDrawNewShape(ShapeGraphicalRepresentation graphicalRepresentation, ContainerNode<?, ?> parentNode) {
				/*System.out.println("OK, perform draw new shape with " + graphicalRepresentation + " et parent: "
						+ parentGraphicalRepresentation);*/

				AddShape action = AddShape.actionType.makeNewAction(getDiagram(), null, getFlexoController().getEditor());
				action.setGraphicalRepresentation(graphicalRepresentation);
				action.setNewShapeName(graphicalRepresentation.getText());
				if (action.getNewShapeName() == null) {
					action.setNewShapeName(FlexoLocalization.localizedForKey("shape"));
				}

				action.doAction();

			}
		});

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

	public JTabbedPane getPaletteView() {
		if (paletteView == null) {

			logger.info("Building PaletteView with " + contextualPalettes);

			paletteView = new JTabbedPane();
			orderedPalettes = new Vector<DiagramPalette>(contextualPalettes.keySet());
			Collections.sort(orderedPalettes);

			System.out.println("orderedPalettes=" + orderedPalettes);

			for (DiagramPalette palette : orderedPalettes) {
				paletteView.add(palette.getName(), contextualPalettes.get(palette).getPaletteViewInScrollPane());
			}
			paletteView.add(FlexoLocalization.localizedForKey("Common", getCommonPalette().getPaletteViewInScrollPane()),
					getCommonPalette().getPaletteViewInScrollPane());
			paletteView.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					if (paletteView.getSelectedIndex() < orderedPalettes.size()) {
						activatePalette(contextualPalettes.get(orderedPalettes.elementAt(paletteView.getSelectedIndex())));
					} else if (paletteView.getSelectedIndex() == orderedPalettes.size()) {
						activatePalette(getCommonPalette());
					}
				}
			});
			paletteView.setSelectedIndex(0);
			if (orderedPalettes.size() > 0) {
				activatePalette(contextualPalettes.get(orderedPalettes.firstElement()));
			} else {
				activatePalette(getCommonPalette());
			}
		}
		return paletteView;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
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
				} else if (evt.getOldValue() instanceof DiagramPalette) {
					logger.info("Handling palette removed");
					DiagramPalette palette = (DiagramPalette) evt.getOldValue();
					JDianaPalette removedPalette = contextualPalettes.get(palette);
					removedPalette.delete();
					AbstractDiagramPalette removedPaletteModel = contextualPaletteModels.get(palette);
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
		} else {
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

}
