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
package org.openflexo.technologyadapter.diagram.controller.action;

import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.fml.action.PushToPalette;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class PushToPaletteInitializer extends ActionInitializer<PushToPalette, DiagramShape, DiagramElement<?>> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public PushToPaletteInitializer(ControllerActionInitializer actionInitializer) {
		super(PushToPalette.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<PushToPalette> getDefaultInitializer() {
		return new FlexoActionInitializer<PushToPalette>() {
			@Override
			public boolean run(EventObject e, PushToPalette action) {
				action.setImage((DiagramIconLibrary.DIAGRAM_PALETTE_ICON).getImage());
				return instanciateAndShowDialog(action, DiagramCst.PUSH_TO_PALETTE_DIALOG_FIB);
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<PushToPalette> getDefaultFinalizer() {
		return new FlexoActionFinalizer<PushToPalette>() {
			@Override
			public boolean run(EventObject e, PushToPalette action) {
				getController().setCurrentEditedObjectAsModuleView(action.palette);
				getController().getSelectionManager().setSelectedObject(action.getNewPaletteElement());
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return DiagramIconLibrary.DIAGRAM_PALETTE_ICON;
	}

}
