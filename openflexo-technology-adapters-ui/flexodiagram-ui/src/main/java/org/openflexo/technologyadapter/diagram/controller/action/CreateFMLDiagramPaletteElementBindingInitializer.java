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

package org.openflexo.technologyadapter.diagram.controller.action;

import java.util.EventObject;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class CreateFMLDiagramPaletteElementBindingInitializer
		extends ActionInitializer<CreateFMLDiagramPaletteElementBinding, TypedDiagramModelSlot, FMLObject> {

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
					FlexoController.notify(action.getLocales().localizedForKey("not_implemented_yet"));
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
