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

import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.icon.IconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.fml.action.DeleteDiagramSpecification;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class DeleteDiagramSpecificationInitializer extends
		ActionInitializer<DeleteDiagramSpecification, DiagramSpecification, ViewPointObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public DeleteDiagramSpecificationInitializer(ControllerActionInitializer actionInitializer) {
		super(DeleteDiagramSpecification.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<DeleteDiagramSpecification> getDefaultInitializer() {
		return new FlexoActionInitializer<DeleteDiagramSpecification>() {
			@Override
			public boolean run(EventObject e, DeleteDiagramSpecification action) {
				return FlexoController.confirm(FlexoLocalization
						.localizedForKey("would_you_really_like_to_delete_this_diagram_specification"));
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return IconLibrary.DELETE_ICON;
	}

}