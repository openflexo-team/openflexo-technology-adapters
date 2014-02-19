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

import org.openflexo.components.widget.CommonFIB;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.icon.VPMIconLibrary;
import org.openflexo.technologyadapter.diagram.fml.action.DeclareShapeInEditionPattern;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class DeclareShapeInEditionPatternInitializer extends
		ActionInitializer<DeclareShapeInEditionPattern, DiagramShape, DiagramElement<?>> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public DeclareShapeInEditionPatternInitializer(ControllerActionInitializer actionInitializer) {
		super(DeclareShapeInEditionPattern.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<DeclareShapeInEditionPattern> getDefaultInitializer() {
		return new FlexoActionInitializer<DeclareShapeInEditionPattern>() {
			@Override
			public boolean run(EventObject e, DeclareShapeInEditionPattern action) {

				return instanciateAndShowDialog(action, CommonFIB.DECLARE_SHAPE_IN_FLEXO_CONCEPT_DIALOG_FIB);
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<DeclareShapeInEditionPattern> getDefaultFinalizer() {
		return new FlexoActionFinalizer<DeclareShapeInEditionPattern>() {
			@Override
			public boolean run(EventObject e, DeclareShapeInEditionPattern action) {
				getController().setCurrentEditedObjectAsModuleView(action.getFlexoConcept());
				getController().getSelectionManager().setSelectedObject(action.getFlexoConcept());
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return VPMIconLibrary.FLEXO_CONCEPT_ICON;
	}
}
