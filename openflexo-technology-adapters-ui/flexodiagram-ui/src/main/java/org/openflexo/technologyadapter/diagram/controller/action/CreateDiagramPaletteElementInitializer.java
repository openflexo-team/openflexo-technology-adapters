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

import java.util.logging.Logger;
import javax.swing.*;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation.LocationConstraints;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPaletteElement;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPaletteFactory;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class CreateDiagramPaletteElementInitializer extends ActionInitializer<CreateDiagramPaletteElement, DiagramPalette, FMLObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreateDiagramPaletteElementInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateDiagramPaletteElement.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<CreateDiagramPaletteElement> getDefaultInitializer() {
		return (e, action) -> {
			/*if (action.getNewElementName() != null && (action.getFocusedObject() != null))
				return true;*/

			action.setNewElementName(FlexoController.askForString(action.getLocales().localizedForKey("name_for_new_element")));
			if (action.getGraphicalRepresentation() == null) {
				action.setGraphicalRepresentation(makePaletteElementGraphicalRepresentation(ShapeType.RECTANGLE, action));
			}
			return true;
		};
	}

	protected ShapeGraphicalRepresentation makePaletteElementGraphicalRepresentation(ShapeType st, CreateDiagramPaletteElement action) {
		DiagramPaletteFactory factory = action.getFocusedObject().getResource().getFactory();

		ShapeGraphicalRepresentation gr = factory.makeShapeGraphicalRepresentation(st);
		gr.setX(100);
		gr.setY(100);
		gr.setWidth(50);
		gr.setHeight(50);
		gr.setIsVisible(true);
		gr.setIsFloatingLabel(false);
		gr.setIsSelectable(true);
		gr.setIsFocusable(true);
		gr.setIsReadOnly(false);
		gr.setLocationConstraints(LocationConstraints.FREELY_MOVABLE);
		gr.setLayer(1);
		return gr;
	}

	@Override
	protected FlexoActionFinalizer<CreateDiagramPaletteElement> getDefaultFinalizer() {
		return (e, action) -> {
			getController().getSelectionManager().setSelectedObject(action.getNewElement());
			return true;
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return DiagramIconLibrary.SHAPE_ICON;
	}

}
