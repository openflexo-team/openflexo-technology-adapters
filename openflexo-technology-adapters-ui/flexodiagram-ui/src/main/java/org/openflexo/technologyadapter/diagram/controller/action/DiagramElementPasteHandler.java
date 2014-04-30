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
import org.openflexo.foundation.action.PasteAction.DefaultPastingContext;
import org.openflexo.foundation.action.PasteAction.PasteHandler;
import org.openflexo.foundation.action.PasteAction.PastingContext;
import org.openflexo.model.factory.Clipboard;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.StringUtils;

/**
 * Paste Handler suitable for pasting {@link DiagramElement} instances into {@link DiagramElement}<br>
 * Names are translated according to a basic "-copy" appending strategy (incrementing an index) while positions are also either incremented
 * with a given PASTE_DELTA increment, or set to last clicked point, depending on the pasting context
 * 
 * @author sylvain
 * 
 */
public class DiagramElementPasteHandler implements PasteHandler<DiagramContainerElement> {

	private static final Logger logger = Logger.getLogger(DiagramElementPasteHandler.class.getPackage().getName());

	private final MouseSelectionManager selectionManager;

	public static final int PASTE_DELTA = 10;
	public static final String COPY_SUFFIX = "-copy";
	public static final String REFLEXIVE_PASTE = "ReflexivePaste";

	public DiagramElementPasteHandler(MouseSelectionManager selectionManager) {
		this.selectionManager = selectionManager;
	}

	@Override
	public Class<DiagramContainerElement> getPastingPointHolderType() {
		return DiagramContainerElement.class;
	}

	@Override
	public PastingContext<DiagramContainerElement> retrievePastingContext(FlexoObject focusedObject, List<FlexoObject> globalSelection,
			FlexoClipboard clipboard, Event event) {

		if (!(focusedObject instanceof DiagramContainerElement)) {
			return null;
		}

		PastingContext<DiagramContainerElement> returned = new DefaultPastingContext<DiagramContainerElement>(
				(DiagramContainerElement) focusedObject, event);

		Clipboard leaderClipboard = clipboard.getLeaderClipboard();

		boolean reflexivePaste = false;

		for (Object originalContent : leaderClipboard.getOriginalContents()) {
			if (originalContent == focusedObject) {
				reflexivePaste = true;
			}
		}

		if (reflexivePaste) {
			returned.setPasteProperty(REFLEXIVE_PASTE, "true");
		} else {
			returned.setPasteProperty(REFLEXIVE_PASTE, "false");
		}

		return returned;
	}

	@Override
	public void prepareClipboardForPasting(FlexoClipboard clipboard, PastingContext<DiagramContainerElement> pastingContext) {

		Clipboard leaderClipboard = clipboard.getLeaderClipboard();

		// Translating names
		if (leaderClipboard.isSingleObject()) {
			if (leaderClipboard.getSingleContents() instanceof DiagramShape) {
				translateName((DiagramShape) leaderClipboard.getSingleContents());
			} else if (leaderClipboard.getSingleContents() instanceof DiagramConnector) {
				translateName((DiagramConnector) leaderClipboard.getSingleContents());
			}
		} else {
			for (Object o : leaderClipboard.getMultipleContents()) {
				if (o instanceof DiagramShape) {
					translateName((DiagramShape) o);
				} else if (o instanceof DiagramConnector) {
					translateName((DiagramConnector) o);
				}
			}
		}

		// Setting positions
		if (pastingContext.getPasteProperty(REFLEXIVE_PASTE).equals("true")) {
			if (leaderClipboard.isSingleObject()) {
				if (leaderClipboard.getSingleContents() instanceof DiagramShape) {
					DiagramShape shapeBeingPasted = (DiagramShape) leaderClipboard.getSingleContents();
					shapeBeingPasted.getGraphicalRepresentation().setX(shapeBeingPasted.getGraphicalRepresentation().getX() + PASTE_DELTA);
					shapeBeingPasted.getGraphicalRepresentation().setY(shapeBeingPasted.getGraphicalRepresentation().getY() + PASTE_DELTA);
				}
			} else {
				for (Object o : leaderClipboard.getMultipleContents()) {
					if (o instanceof DiagramShape) {
						((DiagramShape) o).getGraphicalRepresentation().setX(
								((DiagramShape) o).getGraphicalRepresentation().getX() + PASTE_DELTA);
						((DiagramShape) o).getGraphicalRepresentation().setY(
								((DiagramShape) o).getGraphicalRepresentation().getY() + PASTE_DELTA);
					}
				}
			}
		}

		else {
			if (leaderClipboard.isSingleObject()) {
				if (leaderClipboard.getSingleContents() instanceof DiagramShape) {
					DiagramShape shapeBeingPasted = (DiagramShape) leaderClipboard.getSingleContents();
					shapeBeingPasted.getGraphicalRepresentation().setX(selectionManager.getLastClickedPoint().getX());
					shapeBeingPasted.getGraphicalRepresentation().setY(selectionManager.getLastClickedPoint().getY());
				}
			} else {
				boolean deltaSet = false;
				double deltaX = Double.NaN;
				double deltaY = Double.NaN;

				for (Object o : leaderClipboard.getMultipleContents()) {
					if (o instanceof DiagramShape) {
						DiagramShape shape = (DiagramShape) o;
						if (!deltaSet) {
							deltaX = selectionManager.getLastClickedPoint().getX() - shape.getGraphicalRepresentation().getX();
							deltaY = selectionManager.getLastClickedPoint().getY() - shape.getGraphicalRepresentation().getY();
						}
						shape.getGraphicalRepresentation().setX(shape.getGraphicalRepresentation().getX() + deltaX);
						shape.getGraphicalRepresentation().setY(shape.getGraphicalRepresentation().getY() + deltaY);
						deltaSet = true;
					}
				}
			}

		}
	}

	@Override
	public void finalizePasting(FlexoClipboard clipboard, PastingContext<DiagramContainerElement> pastingContext) {
		// Nothing to do
	}

	private String translateName(DiagramElement<?> diagramElement) {
		String oldName = diagramElement.getName();
		if (StringUtils.isEmpty(oldName)) {
			return null;
		}
		String newName;
		if (oldName.endsWith(COPY_SUFFIX)) {
			newName = oldName + "2";
		} else if (oldName.contains(COPY_SUFFIX)) {
			try {
				int currentIndex = Integer.parseInt(oldName.substring(oldName.lastIndexOf(COPY_SUFFIX) + COPY_SUFFIX.length()));
				newName = oldName.substring(0, oldName.lastIndexOf(COPY_SUFFIX)) + COPY_SUFFIX + (currentIndex + 1);
			} catch (NumberFormatException e) {
				logger.warning("Could not parse as int " + oldName.substring(oldName.lastIndexOf(COPY_SUFFIX)));
				newName = oldName + COPY_SUFFIX;
			}
		} else {
			newName = oldName + COPY_SUFFIX;
		}
		System.out.println("translating name from " + oldName + " to " + newName);
		diagramElement.setName(newName);
		return newName;
	}

}
