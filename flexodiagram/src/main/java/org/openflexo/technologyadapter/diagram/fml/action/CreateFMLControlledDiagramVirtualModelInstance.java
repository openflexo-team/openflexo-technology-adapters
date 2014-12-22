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
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fmlrt.View;
import org.openflexo.foundation.fmlrt.action.CreateVirtualModelInstance;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramViewNature;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;

public class CreateFMLControlledDiagramVirtualModelInstance extends
		CreateVirtualModelInstance<CreateFMLControlledDiagramVirtualModelInstance> {

	private static final Logger logger = Logger.getLogger(CreateFMLControlledDiagramVirtualModelInstance.class.getPackage().getName());

	public static FlexoActionType<CreateFMLControlledDiagramVirtualModelInstance, View, FlexoObject> actionType = new FlexoActionType<CreateFMLControlledDiagramVirtualModelInstance, View, FlexoObject>(
			"create_fml_controlled_diagram", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFMLControlledDiagramVirtualModelInstance makeNewAction(View focusedObject, Vector<FlexoObject> globalSelection,
				FlexoEditor editor) {
			return new CreateFMLControlledDiagramVirtualModelInstance(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(View view, Vector<FlexoObject> globalSelection) {
			return view.hasNature(FMLControlledDiagramViewNature.INSTANCE);
		}

		@Override
		public boolean isEnabledForSelection(View view, Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(view, globalSelection);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFMLControlledDiagramVirtualModelInstance.actionType, View.class);
	}

	CreateFMLControlledDiagramVirtualModelInstance(View focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public CreateFMLControlledDiagramVirtualModelInstance doAction() {
		System.out.println("Creating FMLControlledDiagramVirtualModelInstance");
		return super.doAction();
	}
	
	@Override
	public boolean isVisible(VirtualModel virtualModel){
		if(virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE)){
			return true;
		}else{
			return false;
		}
	}

}