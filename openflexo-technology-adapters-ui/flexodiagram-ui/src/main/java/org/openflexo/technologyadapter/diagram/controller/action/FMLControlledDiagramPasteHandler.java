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
import org.openflexo.foundation.action.PasteAction.DefaultPastingContext;
import org.openflexo.foundation.action.PasteAction.PastingContext;
import org.openflexo.foundation.view.ModelSlotInstance;
import org.openflexo.foundation.view.TypeAwareModelSlotInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.viewpoint.FlexoConceptObject;
import org.openflexo.model.factory.Clipboard;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.vpm.controller.VirtualModelInstancePasteHandler;

/**
 * Paste Handler suitable for pasting something into a FMLControlledDiagram
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramPasteHandler extends VirtualModelInstancePasteHandler {

	private static final Logger logger = Logger.getLogger(FMLControlledDiagramPasteHandler.class.getPackage().getName());

	public static final String COPY_SUFFIX = "-copy";

	private final VirtualModelInstance virtualModelInstance;

	public FMLControlledDiagramPasteHandler(VirtualModelInstance virtualModelInstance) {
		this.virtualModelInstance = virtualModelInstance;
	}

	public class FMLControlledDiagramPastingContext extends DefaultPastingContext<VirtualModelInstance> {
		public FMLControlledDiagramPastingContext(VirtualModelInstance holder, Event event) {
			super(holder, event);
		}

		public Diagram getDiagram() {
			return FMLControlledDiagramVirtualModelInstanceNature.getDiagram(getPastingPointHolder());
		}
	}

	@Override
	public PastingContext<VirtualModelInstance> retrievePastingContext(FlexoObject focusedObject, List<FlexoObject> globalSelection,
			Clipboard clipboard, Event event) {

		if (virtualModelInstance.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
			if (focusedObject == FMLControlledDiagramVirtualModelInstanceNature.getDiagram(virtualModelInstance)) {
				System.out.println("Lookep up " + virtualModelInstance + " from " + focusedObject);
				return new FMLControlledDiagramPastingContext(virtualModelInstance, event);
			}
		}

		return super.retrievePastingContext(focusedObject, globalSelection, clipboard, event);
	}

	@Override
	public void prepareClipboardForPasting(Clipboard clipboard, PastingContext<VirtualModelInstance> pastingContext) {

		super.prepareClipboardForPasting(clipboard, pastingContext);

		// Translating names
		/*if (clipboard.isSingleObject()) {
			if (clipboard.getSingleContents() instanceof FlexoConceptObject) {
				translateName((FlexoConceptObject) clipboard.getSingleContents());
			}
		} else {
			for (Object o : clipboard.getMultipleContents()) {
				if (o instanceof FlexoConceptObject) {
					translateName((FlexoConceptObject) o);
				}
			}
		}*/
	}

	private String translateName(FlexoConceptObject object) {
		String oldName = object.getName();
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
		object.setName(newName);
		return newName;
	}

	@Override
	public Object getModelSlotSpecificPastingPointHolder(ModelSlotInstance<?, ?> modelSlotInstance, PastingContext<?> pastingContext) {
		if (modelSlotInstance.getModelSlot() instanceof TypedDiagramModelSlot) {
			return ((TypeAwareModelSlotInstance<Diagram, DiagramSpecification, TypedDiagramModelSlot>) modelSlotInstance)
					.getAccessedResourceData();
		}
		return super.getModelSlotSpecificPastingPointHolder(modelSlotInstance, pastingContext);
	}

}
