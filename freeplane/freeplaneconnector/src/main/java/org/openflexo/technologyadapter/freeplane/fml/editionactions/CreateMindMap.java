package org.openflexo.technologyadapter.freeplane.fml.editionactions;


import java.lang.reflect.Type;

import org.openflexo.foundation.view.action.FlexoBehaviourAction;
import org.openflexo.foundation.viewpoint.editionaction.AssignableAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.technologyadapter.freeplane.IFreeplaneModelSlot;
import org.openflexo.technologyadapter.freeplane.fml.IFreeplaneMapRole;
import org.openflexo.technologyadapter.freeplane.fml.editionactions.CreateMindMap.CreateMindMapImpl;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

@ImplementationClass(value = CreateMindMapImpl.class)
public interface CreateMindMap extends AssignableAction<IFreeplaneModelSlot, IFreeplaneMap> {

	public static abstract class CreateMindMapImpl extends AssignableActionImpl<IFreeplaneModelSlot, IFreeplaneMap> implements CreateMindMap {

		@Override
		public Type getAssignableType() {
			return IFreeplaneMap.class;
		}

		@Override
		public IFreeplaneMapRole getFlexoRole() {
			return (IFreeplaneMapRole) super.getFlexoRole();
		}

		@Override
		public IFreeplaneMap performAction(final FlexoBehaviourAction action) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
