/*
 * (c) Copyright 2013-2014 Openflexo
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
package org.openflexo.technologyadapter.diagram.controller.action;

import java.awt.Event;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoClipboard;
import org.openflexo.foundation.action.PasteAction.PastingContext;
import org.openflexo.foundation.view.ModelSlotInstance;
import org.openflexo.foundation.view.TypeAwareModelSlotInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.model.factory.Clipboard;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.vpm.controller.VirtualModelInstancePasteHandler;

/**
 * Paste Handler suitable for pasting something into a FMLControlledDiagram
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramPasteHandler extends VirtualModelInstancePasteHandler {

	private static final Logger logger = Logger.getLogger(FMLControlledDiagramPasteHandler.class.getPackage().getName());

	private final VirtualModelInstance virtualModelInstance;
	private final FMLControlledDiagramEditor editor;

	public static final int PASTE_DELTA = 10;
	public static final String REFLEXIVE_PASTE = "ReflexivePaste";

	public FMLControlledDiagramPasteHandler(VirtualModelInstance virtualModelInstance, FMLControlledDiagramEditor editor) {
		this.virtualModelInstance = virtualModelInstance;
		this.editor = editor;
	}

	public class FMLControlledDiagramPastingContext extends HeterogeneousPastingContext {
		public FMLControlledDiagramPastingContext(VirtualModelInstance holder, Event event) {
			super(holder, event);
		}

		public Diagram getDiagram() {
			return FMLControlledDiagramVirtualModelInstanceNature.getDiagram(getPastingPointHolder());
		}
	}

	@Override
	public PastingContext<VirtualModelInstance> retrievePastingContext(FlexoObject focusedObject, List<FlexoObject> globalSelection,
			FlexoClipboard clipboard, Event event) {

		PastingContext<VirtualModelInstance> returned = null;

		// System.out.println("retrievePastingContext() in FMLControlledDiagramPasteHander, focusedObject=" + focusedObject);

		if (virtualModelInstance.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
			if (focusedObject == FMLControlledDiagramVirtualModelInstanceNature.getDiagram(virtualModelInstance)) {
				System.out.println("Lookep up " + virtualModelInstance + " from " + focusedObject);
				returned = new FMLControlledDiagramPastingContext(virtualModelInstance, event);
			}
		}

		if (returned == null) {
			returned = super.retrievePastingContext(focusedObject, globalSelection, clipboard, event);
		}

		if (returned != null) {

			Clipboard clipboardLeader = clipboard.getLeaderClipboard();

			boolean reflexivePaste = false;

			for (Object originalContent : clipboardLeader.getOriginalContents()) {
				if (originalContent == focusedObject) {
					reflexivePaste = true;
				}
			}

			if (reflexivePaste) {
				returned.setPasteProperty(REFLEXIVE_PASTE, "true");
			} else {
				returned.setPasteProperty(REFLEXIVE_PASTE, "false");
			}
		}

		return returned;

	}

	@Override
	public void prepareClipboardForPasting(FlexoClipboard clipboard, PastingContext<VirtualModelInstance> pastingContext) {

		System.out.println("Preparing clipboard for pasting in FMLControlledDiagramPasteHander");

		super.prepareClipboardForPasting(clipboard, pastingContext);
	}

	@Override
	public Object getModelSlotSpecificPastingPointHolder(ModelSlotInstance<?, ?> modelSlotInstance,
			HeterogeneousPastingContext pastingContext) {
		if (modelSlotInstance.getModelSlot() instanceof TypedDiagramModelSlot) {
			return ((TypeAwareModelSlotInstance<Diagram, DiagramSpecification, TypedDiagramModelSlot>) modelSlotInstance)
					.getAccessedResourceData();
		}
		return super.getModelSlotSpecificPastingPointHolder(modelSlotInstance, pastingContext);
	}

	@Override
	public void prepareModelSlotSpecificClipboard(Clipboard modelSlotSpecificClipboard, ModelSlotInstance<?, ?> modelSlotInstance,
			HeterogeneousPastingContext pastingContext) {

		if (modelSlotInstance == FMLControlledDiagramVirtualModelInstanceNature.getModelSlotInstance(virtualModelInstance)) {

			System.out.println("Preparing clipboard for pasting in FMLControlledDiagramPasteHander");

			Clipboard diagramClipboard = modelSlotSpecificClipboard;

			// Setting positions

			System.out.println("reflexive paste=" + (pastingContext.getPasteProperty(REFLEXIVE_PASTE).equals("true")));

			if (virtualModelInstance.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)
					&& pastingContext.getPasteProperty(REFLEXIVE_PASTE).equals("true")) {

				System.out.println("C'est bien un reflexive paste, et voici le clipboard du diagram");
				System.out.println(diagramClipboard.debug());

				if (diagramClipboard.isSingleObject()) {
					if (diagramClipboard.getSingleContents() instanceof DiagramShape) {
						DiagramShape shapeBeingPasted = (DiagramShape) diagramClipboard.getSingleContents();
						System.out.println("La shape que je decale: " + shapeBeingPasted);
						shapeBeingPasted.getGraphicalRepresentation().setX(
								shapeBeingPasted.getGraphicalRepresentation().getX() + PASTE_DELTA);
						shapeBeingPasted.getGraphicalRepresentation().setY(
								shapeBeingPasted.getGraphicalRepresentation().getY() + PASTE_DELTA);
					}
				} else {
					for (Object o : diagramClipboard.getMultipleContents()) {
						if (o instanceof DiagramShape) {
							((DiagramShape) o).getGraphicalRepresentation().setX(
									((DiagramShape) o).getGraphicalRepresentation().getX() + PASTE_DELTA);
							((DiagramShape) o).getGraphicalRepresentation().setY(
									((DiagramShape) o).getGraphicalRepresentation().getY() + PASTE_DELTA);
						}
					}
				}
			}

			else if (editor.getSelectionManager() instanceof MouseSelectionManager) {
				if (diagramClipboard.isSingleObject()) {
					if (diagramClipboard.getSingleContents() instanceof DiagramShape) {
						DiagramShape shapeBeingPasted = (DiagramShape) diagramClipboard.getSingleContents();
						shapeBeingPasted.getGraphicalRepresentation().setX(
								((MouseSelectionManager) editor.getSelectionManager()).getLastClickedPoint().getX());
						shapeBeingPasted.getGraphicalRepresentation().setY(
								((MouseSelectionManager) editor.getSelectionManager()).getLastClickedPoint().getY());
					}
				} else {
					boolean deltaSet = false;
					double deltaX = Double.NaN;
					double deltaY = Double.NaN;

					for (Object o : diagramClipboard.getMultipleContents()) {
						if (o instanceof DiagramShape) {
							DiagramShape shape = (DiagramShape) o;
							if (!deltaSet) {
								deltaX = ((MouseSelectionManager) editor.getSelectionManager()).getLastClickedPoint().getX()
										- shape.getGraphicalRepresentation().getX();
								deltaY = ((MouseSelectionManager) editor.getSelectionManager()).getLastClickedPoint().getY()
										- shape.getGraphicalRepresentation().getY();
							}
							shape.getGraphicalRepresentation().setX(shape.getGraphicalRepresentation().getX() + deltaX);
							shape.getGraphicalRepresentation().setY(shape.getGraphicalRepresentation().getY() + deltaY);
							deltaSet = true;
						}
					}
				}

			}
		}
	}

}
