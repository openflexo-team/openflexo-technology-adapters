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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.openflexo.fge.Drawing.ContainerNode;
import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.PaletteElementSpecification;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.control.PaletteElement;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.DropSchemeAction;

public class ContextualPalette extends DiagramEditorPaletteModel implements PropertyChangeListener {

	private static final Logger logger = Logger.getLogger(ContextualPalette.class.getPackage().getName());

	private DiagramPalette diagramPalette;

	public ContextualPalette(DiagramPalette diagramPalette, DiagramEditor editor) {
		super(editor, diagramPalette.getName(),
				diagramPalette.getGraphicalRepresentation() != null ? (int) diagramPalette.getGraphicalRepresentation().getWidth() : 200,
				diagramPalette.getGraphicalRepresentation() != null ? (int) diagramPalette.getGraphicalRepresentation().getHeight() : 200,
				40, 30, 10, 10);

		this.diagramPalette = diagramPalette;

		for (DiagramPaletteElement element : diagramPalette.getElements()) {
			addElement(makePaletteElement(element, editor));
		}

		diagramPalette.getPropertyChangeSupport().addPropertyChangeListener(this);
	}

	@Override
	public void delete() {
		if (diagramPalette != null && diagramPalette.getPropertyChangeSupport() != null) {
			diagramPalette.getPropertyChangeSupport().removePropertyChangeListener(this);
		}
		super.delete();
		diagramPalette = null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == diagramPalette) {
			if (evt.getPropertyName().equals(DiagramPalette.PALETTE_ELEMENTS_KEY)) {
				if (evt.getNewValue() instanceof DiagramPaletteElement) {
					// Adding of a new DiagramPaletteElement
					ContextualPaletteElement e = makePaletteElement((DiagramPaletteElement) evt.getNewValue(), getEditor());
					addElement(e);
					// e.getGraphicalRepresentation().notifyObjectHierarchyHasBeenUpdated();
					// DrawingView<PaletteDrawing> oldPaletteView = getPaletteView();
					// updatePalette();
					// getController().updatePalette(diagramPalette, oldPaletteView);
					logger.warning("Sans doute des choses a faire ici ???");
				}
				else if (evt.getOldValue() instanceof DiagramPaletteElement) {
					ContextualPaletteElement e = getContextualPaletteElement((DiagramPaletteElement) evt.getOldValue());
					removeElement(e);
					// DrawingView<PaletteDrawing> oldPaletteView = getPaletteView();
					// updatePalette();
					// getController().updatePalette(diagramPalette, oldPaletteView);
					logger.warning("Sans doute des choses a faire ici ???");
				}
			}
		}
	}

	protected ContextualPaletteElement getContextualPaletteElement(DiagramPaletteElement element) {
		for (PaletteElement e : elements) {
			if (e instanceof ContextualPaletteElement && ((ContextualPaletteElement) e).diagramPaletteElement == element) {
				return (ContextualPaletteElement) e;
			}
		}
		return null;

	}

	private static Vector<DropScheme> getAvailableDropSchemes(FlexoConcept pattern, DrawingTreeNode<?, ?> target) {
		Vector<DropScheme> returned = new Vector<>();
		for (DropScheme dropScheme : pattern.getFlexoBehaviours(DropScheme.class)) {
			if (dropScheme.isTopTarget() && target instanceof DrawingGraphicalRepresentation) {
				returned.add(dropScheme);
			}
			/*if (target.getDrawable() instanceof DiagramShape) {
				DiagramShape targetShape = (DiagramShape) target.getDrawable();
				for (FlexoObjectReference<FlexoConceptInstance> ref : targetShape.getFlexoConceptReferences()) {
					if (dropScheme.isValidTarget(ref.getObject().getFlexoConcept(), ref.getObject().getRoleForActor(targetShape))) {
						returned.add(dropScheme);
					}
				}
			}*/
		}
		return returned;
	}

	protected ContextualPaletteElement makePaletteElement(final DiagramPaletteElement element, DiagramEditor editor) {
		// System.out.println("******* makePaletteElement with " + element);
		return new ContextualPaletteElement(element);
	}

	@Override
	protected PaletteElement buildPaletteElement(PaletteElementSpecification paletteElement) {
		// TODO
		// return new ContextualPaletteElement(paletteElement.getGraphicalRepresentation());
		return null;
	}

	@SuppressWarnings("serial")
	protected class ContextualPaletteElement implements PaletteElement {
		private DiagramPaletteElement diagramPaletteElement;

		public ContextualPaletteElement(final DiagramPaletteElement aPaletteElement) {
			diagramPaletteElement = aPaletteElement;
		}

		@Override
		public String getName() {
			return diagramPaletteElement.getName();
		}

		@Override
		public boolean acceptDragging(DrawingTreeNode<?, ?> target) {

			if (getEditor() instanceof FMLControlledDiagramEditor) {
				// System.out.println("Available DS = " + getAvailableDropSchemes(target, diagramPaletteElement));
				return getAvailableDropSchemes(target, diagramPaletteElement).size() > 0;
			}

			return getEditor() != null && target instanceof ContainerNode && (target.getDrawable() instanceof Diagram
					|| target.getDrawable() instanceof DiagramShape || target.getDrawable() instanceof FMLControlledDiagramElement);
		}

		@Override
		public boolean elementDragged(DrawingTreeNode<?, ?> target, FGEPoint dropLocation) {
			if (target != null) {

				if (getEditor() instanceof FMLControlledDiagramEditor) {
					return handleFMLControlledDrop(target, diagramPaletteElement, dropLocation, (FMLControlledDiagramEditor) getEditor());
				}
				else {
					return handleBasicGraphicalRepresentationDrop(target, getGraphicalRepresentation(), dropLocation, false, false, false,
							false, false, false);
				}

			}

			return false;
		}

		@Override
		public ShapeGraphicalRepresentation getGraphicalRepresentation() {
			return diagramPaletteElement.getGraphicalRepresentation();
		}

		@Override
		public void delete(Object... context) {
			diagramPaletteElement = null;
		}

	}

	public List<DropScheme> getAvailableDropSchemes(DrawingTreeNode<?, ?> target, DiagramPaletteElement paletteElement) {

		if (getEditor() instanceof FMLControlledDiagramEditor) {

			FMLRTVirtualModelInstance vmi = ((FMLControlledDiagramEditor) getEditor()).getVirtualModelInstance();
			TypedDiagramModelSlot ms = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(vmi.getVirtualModel());

			ArrayList<DropScheme> availableDropSchemes = new ArrayList<>();

			for (FMLDiagramPaletteElementBinding fmlDiagramPaletteElementBinding : ms.getPaletteElementBindings(paletteElement)) {

				DropScheme ds = fmlDiagramPaletteElementBinding.getDropScheme();

				if (target.getDrawable() instanceof Diagram) {
					if (ds.isTopTarget()) {
						availableDropSchemes.add(ds);
					}
				}
				else if (target.getDrawable() instanceof FMLControlledDiagramShape) {
					FMLControlledDiagramShape fmlControlledShape = (FMLControlledDiagramShape) target.getDrawable();
					if (ds.isValidTarget(fmlControlledShape.getFlexoConceptInstance().getFlexoConcept(), fmlControlledShape.getRole())) {
						availableDropSchemes.add(ds);
					}
				}
			}

			return availableDropSchemes;
		}

		return null;
	}

	public boolean handleFMLControlledDrop(DrawingTreeNode<?, ?> target, DiagramPaletteElement paletteElement, FGEPoint dropLocation,
			FMLControlledDiagramEditor editor) {

		FlexoConceptInstance parentFlexoConceptInstance = null;
		ShapeRole parentShapeRole = null;
		DiagramContainerElement<?> container = null;

		if (target.getDrawable() instanceof FMLControlledDiagramElement) {
			parentFlexoConceptInstance = ((FMLControlledDiagramElement<?, ?>) target.getDrawable()).getFlexoConceptInstance();
			container = (DiagramContainerElement<?>) ((FMLControlledDiagramElement<?, ?>) target.getDrawable()).getDiagramElement();
		}
		if (target.getDrawable() instanceof FMLControlledDiagramShape) {
			parentShapeRole = (ShapeRole) ((FMLControlledDiagramShape) target.getDrawable()).getRole();
			container = (DiagramContainerElement<?>) ((FMLControlledDiagramElement<?, ?>) target.getDrawable()).getDiagramElement();
		}
		if (target.getDrawable() instanceof Diagram) {
			container = (Diagram) target.getDrawable();
		}

		List<DropScheme> availableDropSchemes = getAvailableDropSchemes(target, paletteElement);

		if (availableDropSchemes.size() == 0) {
			logger.warning("Unexpected empty list: availableDropSchemes");
			return false;
		}
		else if (availableDropSchemes.size() > 1) {
			JPopupMenu popup = new JPopupMenu();
			for (final DropScheme dropScheme : availableDropSchemes) {
				JMenuItem menuItem = new JMenuItem(dropScheme.getDeclaringVirtualModel().getLocalizedDictionary()
						.localizedForKey(dropScheme.getLabel() != null ? dropScheme.getLabel() : dropScheme.getName()));
				menuItem.addActionListener(new DrawingShapeActionListener(editor, dropScheme, container, parentFlexoConceptInstance,
						parentShapeRole, paletteElement, dropLocation));
				popup.add(menuItem);
			}
			popup.show(editor.getDrawingView(), (int) dropLocation.x, (int) dropLocation.y);
		}
		else { // availableDropSchemes.size() == 1
			DropSchemeAction action = new DropSchemeAction(availableDropSchemes.get(0), editor.getVirtualModelInstance(), null,
					editor.getFlexoController().getEditor());
			action.setParentInformations(parentFlexoConceptInstance, parentShapeRole);
			action.setPaletteElement(paletteElement);
			action.setDropLocation(dropLocation);

			action.doAction();

			// The new shape has well be added to the diagram, and the drawing (which listen to the diagram) has well received the event
			// The drawing is now up-to-date... but there is something wrong if we are in FML-controlled mode.
			// Since the shape has been added BEFORE the FlexoConceptInstance has been set, the drawing only knows about the DiagamShape,
			// and not about an FMLControlledDiagramShape. That's why we need to notify again the new diagram element's parent, to be
			// sure that the Drawing can discover that the new shape is FML-controlled

			container.getPropertyChangeSupport().firePropertyChange(DiagramElement.INVALIDATE, null, container);

			return action.hasActionExecutionSucceeded();
		}

		return false;

	}

	public class DrawingShapeActionListener implements ActionListener {

		private final FMLControlledDiagramEditor controller;
		private final DropScheme dropScheme;
		private final DiagramPaletteElement paletteElement;
		private final FGEPoint dropLocation;
		private final FlexoConceptInstance parentFlexoConceptInstance;
		private final ShapeRole parentShapeRole;
		DiagramContainerElement<?> container;

		DrawingShapeActionListener(FMLControlledDiagramEditor controller, DropScheme dropScheme, DiagramContainerElement<?> container,
				FlexoConceptInstance parentFlexoConceptInstance, ShapeRole parentShapeRole, DiagramPaletteElement paletteElement,
				FGEPoint dropLocation) {
			this.controller = controller;
			this.dropScheme = dropScheme;
			this.parentFlexoConceptInstance = parentFlexoConceptInstance;
			this.parentShapeRole = parentShapeRole;
			this.paletteElement = paletteElement;
			this.dropLocation = dropLocation;
			this.container = container;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			DropSchemeAction action = new DropSchemeAction(dropScheme, controller.getVirtualModelInstance(), null,
					controller.getFlexoController().getEditor());
			action.setParentInformations(parentFlexoConceptInstance, parentShapeRole);
			action.setPaletteElement(paletteElement);
			action.setDropLocation(dropLocation);
			action.doAction();

			// The new shape has well be added to the diagram, and the drawing (which listen to the diagram) has well received the event
			// The drawing is now up-to-date... but there is something wrong if we are in FML-controlled mode.
			// Since the shape has been added BEFORE the FlexoConceptInstance has been set, the drawing only knows about the DiagamShape,
			// and not about an FMLControlledDiagramShape. That's why we need to notify again the new diagram element's parent, to be
			// sure that the Drawing can discover that the new shape is FML-controlled

			container.getPropertyChangeSupport().firePropertyChange(DiagramElement.INVALIDATE, null, container);

		}

	}
}
