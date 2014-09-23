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
package org.openflexo.technologyadapter.diagram.model.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.antar.binding.BindingVariable;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.view.VirtualModelInstanceObject;
import org.openflexo.foundation.view.action.FlexoBehaviourAction;
import org.openflexo.foundation.viewpoint.FlexoBehaviour;
import org.openflexo.foundation.viewpoint.binding.FlexoRoleBindingVariable;
import org.openflexo.technologyadapter.diagram.fml.DiagramEditionScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.fml.binding.DiagramBehaviourBindingModel;
import org.openflexo.technologyadapter.diagram.model.Diagram;

/**
 * 
 * @author sylvain
 * 
 * @param <A>
 * @param <ES>
 * @param <O>
 */
public abstract class DiagramEditionSchemeAction<A extends FlexoBehaviourAction<A, ES, O>, ES extends FlexoBehaviour & DiagramEditionScheme, O extends VirtualModelInstanceObject>
		extends FlexoBehaviourAction<A, ES, O> {

	private static final Logger logger = Logger.getLogger(DiagramEditionSchemeAction.class.getPackage().getName());

	DiagramEditionSchemeAction(FlexoActionType<A, O, VirtualModelInstanceObject> actionType, O focusedObject,
			Vector<VirtualModelInstanceObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	/*@Override
	public Diagram getVirtualModelInstance() {
		return (Diagram) super.getVirtualModelInstance();
	}

	public Diagram getDiagram() {
		return getVirtualModelInstance();
	}*/

	public abstract Diagram getDiagram();

	@Override
	public Object getValue(BindingVariable variable) {
		if (variable.getVariableName().equals(DiagramBehaviourBindingModel.TOP_LEVEL)) {
			return FMLControlledDiagramVirtualModelInstanceNature.getDiagram(getVirtualModelInstance());
		}
		return super.getValue(variable);
	}

	@Override
	public void setValue(Object value, BindingVariable variable) {
		if (variable instanceof FlexoRoleBindingVariable) {
			getFlexoConceptInstance().setFlexoActor(value, ((FlexoRoleBindingVariable) variable).getFlexoRole());
			return;
		}
		super.setValue(value, variable);
	}

}
