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
import org.openflexo.FlexoCst;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.icon.IconLibrary;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.fml.action.DeleteDiagramElementsAndFlexoConceptInstances;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class DeleteDiagramElementsAndFlexoConceptInstancesInitializer
		extends ActionInitializer<DeleteDiagramElementsAndFlexoConceptInstances, FlexoObject, FlexoObject> {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public DeleteDiagramElementsAndFlexoConceptInstancesInitializer(ControllerActionInitializer actionInitializer) {
		super(DeleteDiagramElementsAndFlexoConceptInstances.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<DeleteDiagramElementsAndFlexoConceptInstances> getDefaultInitializer() {
		return (e, action) -> {
			getController().getSelectionManager().resetSelection();
			return instanciateAndShowDialog(action, DiagramCst.DELETE_DIAGRAM_ELEMENTS_AND_FLEXO_CONCEPT_INSTANCES_DIALOG_FIB);
		};
	}

	@Override
	protected FlexoActionFinalizer<DeleteDiagramElementsAndFlexoConceptInstances> getDefaultFinalizer() {
		return (e, action) -> {
			if (getControllerActionInitializer().getController().getSelectionManager().getLastSelectedObject() != null
					&& getControllerActionInitializer().getController().getSelectionManager().getLastSelectedObject().isDeleted()) {
				getControllerActionInitializer().getController().getSelectionManager().resetSelection();
			}
			return true;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionType actionType) {
		return IconLibrary.DELETE_ICON;
	}

	@Override
	protected KeyStroke getShortcut() {
		return KeyStroke.getKeyStroke(FlexoCst.DELETE_KEY_CODE, 0);
	}

}
