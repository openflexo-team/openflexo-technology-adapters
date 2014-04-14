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

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class CreateFMLDiagramPaletteElementBindingInitializer extends ActionInitializer<CreateFMLDiagramPaletteElementBinding, TypedDiagramModelSlot, ViewPointObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreateFMLDiagramPaletteElementBindingInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateFMLDiagramPaletteElementBinding.actionType, actionInitializer);
	}


	@Override
	protected FlexoActionInitializer<CreateFMLDiagramPaletteElementBinding> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateFMLDiagramPaletteElementBinding>() {
			@Override
			public boolean run(EventObject e, CreateFMLDiagramPaletteElementBinding action) {
				return instanciateAndShowDialog(action, DiagramCst.CREATE_FML_DIAGRAM_PALETTE_ELEMENT_BINDING_DIALOG_FIB);
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<CreateFMLDiagramPaletteElementBinding> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateFMLDiagramPaletteElementBinding>() {
			@Override
			public boolean run(EventObject e, CreateFMLDiagramPaletteElementBinding action) {
				return true;
			}
		};
	}

	@Override
	protected FlexoExceptionHandler<CreateFMLDiagramPaletteElementBinding> getDefaultExceptionHandler() {
		return new FlexoExceptionHandler<CreateFMLDiagramPaletteElementBinding>() {
			@Override
			public boolean handleException(FlexoException exception, CreateFMLDiagramPaletteElementBinding action) {
				if (exception instanceof NotImplementedException) {
					FlexoController.notify(FlexoLocalization.localizedForKey("not_implemented_yet"));
					return true;
				}
				return false;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return DiagramIconLibrary.DIAGRAM_ICON;
	}

}
