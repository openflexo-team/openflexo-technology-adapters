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
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.fml.action.CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteElement;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElementInitializer extends
		ActionInitializer<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement, DiagramPaletteElement, DiagramPaletteElement> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElementInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement>() {
			@Override
			public boolean run(EventObject e, CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement action) {
				action.setImage((DiagramIconLibrary.FML_PALETTE_ELEMENT_BINDING_ICON_32X32).getImage());
				return instanciateAndShowDialog(action,
						DiagramCst.CREATE_FML_DIAGRAM_PALETTE_ELEMENT_BINDING_FROM_DIAGRAM_PALETTE_DIALOG_FIB);
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement>() {
			@Override
			public boolean run(EventObject e, CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement action) {
				return true;
			}
		};
	}

	@Override
	protected FlexoExceptionHandler<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement> getDefaultExceptionHandler() {
		return new FlexoExceptionHandler<CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement>() {
			@Override
			public boolean handleException(FlexoException exception,
					CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElement action) {
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
		return DiagramIconLibrary.FML_PALETTE_ELEMENT_BINDING_ICON_16X16;
	}

}
