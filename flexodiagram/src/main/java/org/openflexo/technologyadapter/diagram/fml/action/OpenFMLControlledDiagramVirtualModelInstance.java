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
package org.openflexo.technologyadapter.diagram.fml.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.FlexoGUIAction;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;

public class OpenFMLControlledDiagramVirtualModelInstance extends
		FlexoGUIAction<OpenFMLControlledDiagramVirtualModelInstance, VirtualModelInstance, FlexoObject> {

	private static final Logger logger = Logger.getLogger(OpenFMLControlledDiagramVirtualModelInstance.class.getPackage().getName());

	public static FlexoActionType<OpenFMLControlledDiagramVirtualModelInstance, VirtualModelInstance, FlexoObject> actionType = new FlexoActionType<OpenFMLControlledDiagramVirtualModelInstance, VirtualModelInstance, FlexoObject>(
			"open_as_fml_controlled_diagram", FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public OpenFMLControlledDiagramVirtualModelInstance makeNewAction(VirtualModelInstance focusedObject,
				Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new OpenFMLControlledDiagramVirtualModelInstance(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(VirtualModelInstance virtualModelInstance, Vector<FlexoObject> globalSelection) {
			return virtualModelInstance.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE);
		}

		@Override
		public boolean isEnabledForSelection(VirtualModelInstance view, Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(view, globalSelection);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(OpenFMLControlledDiagramVirtualModelInstance.actionType, VirtualModelInstance.class);
	}

	OpenFMLControlledDiagramVirtualModelInstance(VirtualModelInstance focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public OpenFMLControlledDiagramVirtualModelInstance doAction() {
		System.out.println("Opening ControlledDiagramVirtualModelInstance");
		return super.doAction();
	}

}