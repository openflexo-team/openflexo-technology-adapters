package org.openflexo.technologyadapter.freeplane.fml.editionactions;

import java.lang.reflect.Type;

import org.openflexo.foundation.view.action.FlexoBehaviourAction;
import org.openflexo.foundation.viewpoint.editionaction.AssignableAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.technologyadapter.freeplane.IFreeplaneModelSlot;
import org.openflexo.technologyadapter.freeplane.fml.IFreeplaneNodeRole;
import org.openflexo.technologyadapter.freeplane.fml.editionactions.AddChildNodeAction.AddChildNodeActionImpl;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

@ModelEntity
@ImplementationClass(value = AddChildNodeActionImpl.class)
public interface AddChildNodeAction extends AssignableAction<IFreeplaneModelSlot, IFreeplaneNode> {

	public static abstract class AddChildNodeActionImpl extends AssignableActionImpl<IFreeplaneModelSlot, IFreeplaneNode> implements AddChildNodeAction {
		
		@Override
		public Type getAssignableType() {
			return IFreeplaneNode.class;
		}

		@Override
		public IFreeplaneNodeRole getFlexoRole() {
			return (IFreeplaneNodeRole) super.getFlexoRole();
		}

		@Override
		public IFreeplaneNode performAction(final FlexoBehaviourAction action) {
			// TODO : implement this.
			return null;
		}
	}
}
