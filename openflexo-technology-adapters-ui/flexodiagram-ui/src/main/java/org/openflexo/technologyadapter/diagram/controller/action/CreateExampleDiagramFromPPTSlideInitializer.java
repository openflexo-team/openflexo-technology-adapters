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
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramFromPPTSlide;
import org.openflexo.technologyadapter.diagram.fml.action.CreateExampleDiagramFromPPTSlide;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class CreateExampleDiagramFromPPTSlideInitializer extends ActionInitializer<CreateExampleDiagramFromPPTSlide, DiagramSpecification, ViewPointObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CreateExampleDiagramFromPPTSlideInitializer(ControllerActionInitializer actionInitializer) {
		super(CreateExampleDiagramFromPPTSlide.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<CreateExampleDiagramFromPPTSlide> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateExampleDiagramFromPPTSlide>() {
			@Override
			public boolean run(EventObject e, CreateExampleDiagramFromPPTSlide action) {
				return instanciateAndShowDialog(action, DiagramCst.CREATE_EXAMPLE_DIAGRAM_FROM_PPTSLIDE_DIALOG_FIB);
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<CreateExampleDiagramFromPPTSlide> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateExampleDiagramFromPPTSlide>() {
			@Override
			public boolean run(EventObject e, CreateExampleDiagramFromPPTSlide action) {
				getController().setCurrentEditedObjectAsModuleView(action.getNewDiagram());
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return DiagramIconLibrary.DIAGRAM_ICON;
	}

}
