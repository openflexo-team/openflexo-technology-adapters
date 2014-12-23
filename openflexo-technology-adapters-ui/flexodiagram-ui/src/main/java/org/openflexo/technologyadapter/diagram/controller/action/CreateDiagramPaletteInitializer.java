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

import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.FGEModelFactory;
import org.openflexo.fge.FGEModelFactoryImpl;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.icon.FMLIconLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class CreateDiagramPaletteInitializer extends ActionInitializer<CreateDiagramPalette, DiagramSpecification, FlexoObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreateDiagramPaletteInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateDiagramPalette.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<CreateDiagramPalette> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateDiagramPalette>() {
			@Override
			public boolean run(EventObject e, CreateDiagramPalette action) {
				FGEModelFactory factory;
				try {
					factory = new FGEModelFactoryImpl();
					action.setGraphicalRepresentation(makePaletteGraphicalRepresentation(factory));
					return instanciateAndShowDialog(action, DiagramCst.CREATE_PALETTE_DIALOG_FIB);
				} catch (ModelDefinitionException e1) {
					e1.printStackTrace();
					return false;
				}
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<CreateDiagramPalette> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateDiagramPalette>() {
			@Override
			public boolean run(EventObject e, CreateDiagramPalette action) {
				getController().setCurrentEditedObjectAsModuleView(action.getNewPalette());
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return FMLIconLibrary.VIEWPOINT_ICON;
	}

	protected DrawingGraphicalRepresentation makePaletteGraphicalRepresentation(FGEModelFactory factory) {
		DrawingGraphicalRepresentation gr = factory.makeDrawingGraphicalRepresentation();
		gr.setDrawWorkingArea(true);
		gr.setWidth(260);
		gr.setHeight(300);
		gr.setIsVisible(true);
		return gr;
	}

}
