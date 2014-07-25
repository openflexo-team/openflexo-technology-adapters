package org.openflexo.technologyadapter.freeplane.fml.editionactions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.view.action.FlexoBehaviourAction;
import org.openflexo.foundation.viewpoint.editionaction.FetchRequest;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.freeplane.IFreeplaneModelSlot;
import org.openflexo.technologyadapter.freeplane.fml.editionactions.SelectAllNodes.SelectAllNodesImpl;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

@ModelEntity
@ImplementationClass(SelectAllNodesImpl.class)
@XMLElement
public interface SelectAllNodes extends FetchRequest<IFreeplaneModelSlot, IFreeplaneNode> {

	public abstract class SelectAllNodesImpl extends FetchRequestImpl<IFreeplaneModelSlot, IFreeplaneNode> implements SelectAllNodes {

		private static final Logger LOGGER = Logger.getLogger(SelectAllNodes.class.getPackage().getName());

		@Override
		public List<IFreeplaneNode> performAction(final FlexoBehaviourAction action) {
			if (getModelSlotInstance(action) == null) {
				return Collections.emptyList();
			}
			if (getModelSlotInstance(action).getAccessedResourceData() == null) {
				LOGGER.log(Level.SEVERE, "Action perform on null accessed resource data");
				return Collections.emptyList();
			}
			final IFreeplaneMap map = (IFreeplaneMap) getModelSlotInstance(action).getAccessedResourceData();
			final IFreeplaneNode root = map.getRoot();
			final List<IFreeplaneNode> returned = new ArrayList<IFreeplaneNode>();
			returned.add(root);
			addAllChildren(root, returned);
			return returned;
		}

		private void addAllChildren(final IFreeplaneNode root, final List<IFreeplaneNode> nodeList) {
			nodeList.addAll(root.getChildren());
			for (final IFreeplaneNode child : root.getChildren()) {
				addAllChildren(child, nodeList);
			}
		}

		@Override
		public Type getFetchedType() {
			return IFreeplaneNode.class;
		}
	}
}
