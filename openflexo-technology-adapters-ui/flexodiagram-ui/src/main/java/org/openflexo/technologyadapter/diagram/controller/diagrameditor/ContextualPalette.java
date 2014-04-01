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

import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.fge.Drawing.ContainerNode;
import org.openflexo.fge.Drawing.DrawingTreeNode;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.control.PaletteElement;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.foundation.utils.FlexoObjectReference;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.DropSchemeAction;

public class ContextualPalette extends AbstractDiagramPalette implements PropertyChangeListener {

	private static final Logger logger = Logger.getLogger(ContextualPalette.class.getPackage().getName());

	private DiagramPalette diagramPalette;

	public ContextualPalette(DiagramPalette diagramPalette, DiagramEditor editor) {
		super(editor, diagramPalette.getGraphicalRepresentation() != null ? (int) diagramPalette.getGraphicalRepresentation().getWidth()
				: 300, diagramPalette.getGraphicalRepresentation() != null ? (int) diagramPalette.getGraphicalRepresentation().getHeight()
				: 300, diagramPalette.getName());

		this.diagramPalette = diagramPalette;

		for (DiagramPaletteElement element : diagramPalette.getElements()) {
			addElement(makePaletteElement(element));
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
					ContextualPaletteElement e = makePaletteElement((DiagramPaletteElement) evt.getNewValue());
					addElement(e);
					// e.getGraphicalRepresentation().notifyObjectHierarchyHasBeenUpdated();
					// DrawingView<PaletteDrawing> oldPaletteView = getPaletteView();
					// updatePalette();
					// getController().updatePalette(diagramPalette, oldPaletteView);
					logger.warning("Sans doute des choses a faire ici ???");
				} else if (evt.getOldValue() instanceof DiagramPaletteElement) {
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

	private Vector<DropScheme> getAvailableDropSchemes(FlexoConcept pattern, DrawingTreeNode<?, ?> target) {
		Vector<DropScheme> returned = new Vector<DropScheme>();
		for (DropScheme dropScheme : pattern.getFlexoBehaviours(DropScheme.class)) {
			if (dropScheme.isTopTarget() && target instanceof DrawingGraphicalRepresentation) {
				returned.add(dropScheme);
			}
			if (target.getDrawable() instanceof DiagramShape) {
				DiagramShape targetShape = (DiagramShape) target.getDrawable();
				for (FlexoObjectReference<FlexoConceptInstance> ref : targetShape.getFlexoConceptReferences()) {
					if (dropScheme.isValidTarget(ref.getObject().getFlexoConcept(), ref.getObject().getRoleForActor(targetShape))) {
						returned.add(dropScheme);
					}
				}
			}
		}
		return returned;
	}

	private ContextualPaletteElement makePaletteElement(final DiagramPaletteElement element) {
		System.out.println("******* makePaletteElement with " + element);
		return new ContextualPaletteElement(element);
	}

	@SuppressWarnings("serial")
	protected class ContextualPaletteElement implements PaletteElement {
		private DiagramPaletteElement diagramPaletteElement;

		public ContextualPaletteElement(final DiagramPaletteElement aPaletteElement) {
			diagramPaletteElement = aPaletteElement;
		}

		@Override
		public boolean acceptDragging(DrawingTreeNode<?, ?> target) {
			return getEditor() != null && target instanceof ContainerNode
					&& (target.getDrawable() instanceof Diagram || target.getDrawable() instanceof DiagramShape);
		}

		@Override
		public boolean elementDragged(DrawingTreeNode<?, ?> target, FGEPoint dropLocation) {
			if (target.getDrawable() instanceof DiagramContainerElement) {

				if (getEditor() instanceof FMLControlledDiagramEditor) {
					return handleFMLControlledDrop(target, diagramPaletteElement, dropLocation, (FMLControlledDiagramEditor) getEditor());
				} else {
					return handleBasicGraphicalRepresentationDrop(target, getGraphicalRepresentation(), dropLocation, false, false, false,
							false, false, false);
				}

				// TODO: uncomment and fix following

				/*DropScheme dropScheme = diagramPaletteElement.getDropScheme();

				logger.info("Drop scheme being applied: " + dropScheme);
				System.out.println("Drop scheme being applied: " + dropScheme);

				Hashtable<GraphicalElementRole, ExampleDiagramObject> grHierarchy = new Hashtable<GraphicalElementRole, ExampleDiagramObject>();

				for (EditionAction action : dropScheme.getActions()) {
					if (action instanceof AddShape) {
						ShapeRole role = ((AddShape) action).getPatternRole();
						ShapeGraphicalRepresentation shapeGR = (ShapeGraphicalRepresentation) diagramPaletteElement
								.getOverridingGraphicalRepresentation(role);
						if (shapeGR == null) {
							shapeGR = role.getGraphicalRepresentation();
						}
						ExampleDiagramObject container = null;
						if (role.getParentShapePatternRole() != null) {
							logger.info("Adding shape " + role + " under " + role.getParentShapePatternRole());
							container = grHierarchy.get(role.getParentShapePatternRole());
						} else {
							logger.info("Adding shape " + role + " as root");
							container = rootContainer;
							if (diagramPaletteElement.getBoundLabelToElementName()) {
								shapeGR.setText(diagramPaletteElement.getName());
							}
							shapeGR.setX(dropLocation.x);
							shapeGR.setY(dropLocation.y);
						}
						AddExampleDiagramShape addShapeAction = AddExampleDiagramShape.actionType.makeNewAction(container, null,
								getEditor().getVPMController().getEditor());
						addShapeAction.graphicalRepresentation = shapeGR;
						addShapeAction.newShapeName = role.getPatternRoleName();
						if (role.getParentShapePatternRole() == null) {
							addShapeAction.newShapeName = diagramPaletteElement.getName();
						}
						addShapeAction.doAction();
						grHierarchy.put(role, addShapeAction.getNewShape());
					}
				}*/

				/*ShapeGraphicalRepresentation shapeGR = getGraphicalRepresentation().clone();
				shapeGR.setIsSelectable(true);
				shapeGR.setIsFocusable(true);
				shapeGR.setIsReadOnly(false);
				shapeGR.setLocationConstraints(LocationConstraints.FREELY_MOVABLE);
				shapeGR.setLocation(dropLocation);
				shapeGR.setLayer(containerGR.getLayer() + 1);
				shapeGR.setAllowToLeaveBounds(true);

				AddExampleDiagramShape action = AddExampleDiagramShape.actionType.makeNewAction(container, null, getController()
						.getCEDController().getEditor());
				action.graphicalRepresentation = shapeGR;
				action.newShapeName = shapeGR.getText();
				if (action.newShapeName == null) {
					action.newShapeName = FlexoLocalization.localizedForKey("shape");
					// action.nameSetToNull = true;
					// action.setNewShapeName(FlexoLocalization.localizedForKey("unnamed"));
				}

				action.doAction();
				return action.hasActionExecutionSucceeded();*/

				// return true;
			}

			return false;
		}

		@Override
		public ShapeGraphicalRepresentation getGraphicalRepresentation() {
			return diagramPaletteElement.getGraphicalRepresentation();
		}

		@Override
		public void delete() {
			diagramPaletteElement = null;
		}

	}

	public boolean handleFMLControlledDrop(DrawingTreeNode<?, ?> target, DiagramPaletteElement paletteElement, FGEPoint dropLocation,
			FMLControlledDiagramEditor editor) {

		DiagramContainerElement<?> rootContainer = (DiagramContainerElement<?>) target.getDrawable();
		VirtualModelInstance vmi = editor.getVirtualModelInstance();
		TypedDiagramModelSlot ms = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(vmi.getVirtualModel());
		System.out.println("ms=" + ms);
		System.out.println("bindings=" + ms.getPaletteElementBindings());
		FMLDiagramPaletteElementBinding binding = ms.getPaletteElementBinding(paletteElement);
		System.out.println("binding=" + binding);
		DropScheme dropScheme = binding.getDropScheme();
		System.out.println("dropScheme=" + dropScheme);

		DropSchemeAction action = DropSchemeAction.actionType.makeNewAction(vmi, null, editor.getFlexoController().getEditor());
		action.setDropScheme(dropScheme);
		action.setParent(rootContainer);
		action.setPaletteElement(paletteElement);
		action.setDropLocation(dropLocation);

		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());

		return false;

	}
}
