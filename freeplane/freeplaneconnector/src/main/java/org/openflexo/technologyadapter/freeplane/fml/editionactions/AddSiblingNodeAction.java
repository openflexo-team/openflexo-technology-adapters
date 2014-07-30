package org.openflexo.technologyadapter.freeplane.fml.editionactions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.map.NodeModel;
import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.view.FreeModelSlotInstance;
import org.openflexo.foundation.view.action.FlexoBehaviourAction;
import org.openflexo.foundation.viewpoint.annotations.FIBPanel;
import org.openflexo.foundation.viewpoint.editionaction.AssignableAction;
import org.openflexo.model.annotations.*;
import org.openflexo.technologyadapter.freeplane.IFreeplaneModelSlot;
import org.openflexo.technologyadapter.freeplane.fml.editionactions.AddSiblingNodeAction.AddSiblingNodeActionImpl;
import org.openflexo.technologyadapter.freeplane.fml.structural.IFreeplaneNodeRole;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

/**
 * Edition Action to allow an add of a sibling to a «selected» node. Parametrized in dialog with above or behind current node.
 */
@ModelEntity
@FIBPanel("Fib/AddSiblingNodePanel.fib")
@XMLElement
@ImplementationClass(value = AddSiblingNodeActionImpl.class)
public interface AddSiblingNodeAction extends AssignableAction<IFreeplaneModelSlot, IFreeplaneNode> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String TARGET_NODE_KEY = "targetNode";

	@Getter(value = TARGET_NODE_KEY)
	@XMLAttribute
	public DataBinding<IFreeplaneNode> getTargetNode();

	@Setter(value = TARGET_NODE_KEY)
	public void setTargetNode(DataBinding<IFreeplaneNode> targetNode);

	public abstract static class AddSiblingNodeActionImpl extends AssignableActionImpl<IFreeplaneModelSlot, IFreeplaneNode>
			implements AddSiblingNodeAction {

		private static final Logger LOGGER = Logger.getLogger(AddSiblingNodeAction.class.getPackage().getName());

		private DataBinding<IFreeplaneNode> targetNode;

		@Override
		public DataBinding<IFreeplaneNode> getTargetNode() {
			if (targetNode == null) {
				targetNode = new DataBinding<IFreeplaneNode>(this, getAssignableType(), DataBinding.BindingDefinitionType.GET);
				targetNode.setBindingName(TARGET_NODE_KEY);
			}
			return targetNode;
		}

		@Override
		public void setTargetNode(DataBinding<IFreeplaneNode> targetNode) {
			if (targetNode != null) {
				targetNode.setOwner(this);
				targetNode.setDeclaredType(IFreeplaneNode.class);
				targetNode.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				targetNode.setBindingName(TARGET_NODE_KEY);
			}
			this.targetNode = targetNode;
		}

		@Override
		public IFreeplaneNode performAction(FlexoBehaviourAction action) {
			final FreeModelSlotInstance<IFreeplaneMap, IFreeplaneModelSlot> modelSlotInstance = getModelSlotInstance(action);
			if (modelSlotInstance.getResourceData() != null) {
				final IFreeplaneNode bindedTarget = getTargetNode(action);
				final NodeModel freeplaneParent = bindedTarget.getNodeModel().getParentNode();
				int nodeIndex = freeplaneParent.getIndex(bindedTarget.getNodeModel());
				//currently above, TODO : case distinction.
				nodeIndex++;
				final NodeModel newNode = new NodeModel(bindedTarget.getNodeModel().getMap());
				freeplaneParent.insert(newNode, nodeIndex);
				bindedTarget.getParent().addChild(newNode);
				modelSlotInstance.getResourceData().setIsModified();
				bindedTarget.setModified(true);
				return bindedTarget;
			}
			return null;
		}

		private IFreeplaneNode getTargetNode(FlexoBehaviourAction<?,?,?> action) {
			final String errorMsg = "Error while getting binding value for action " + action;
			try {
				return getTargetNode().getBindingValue(action);
			} catch (final TypeMismatchException e) {
				LOGGER.log(Level.SEVERE, errorMsg, e);
			} catch (final NullReferenceException e) {
				LOGGER.log(Level.SEVERE, errorMsg, e);
			} catch (final InvocationTargetException e) {
				LOGGER.log(Level.SEVERE, errorMsg, e);
			}
			return null;
		}

		@Override
		public FreeModelSlotInstance<IFreeplaneMap, IFreeplaneModelSlot> getModelSlotInstance(final FlexoBehaviourAction<?, ?, ?> action) {
			return (FreeModelSlotInstance<IFreeplaneMap, IFreeplaneModelSlot>) super.getModelSlotInstance(action);
		}

		@Override
		public Type getAssignableType() {
			return IFreeplaneNode.class;
		}

		@Override
		public IFreeplaneNodeRole getFlexoRole() {
			return (IFreeplaneNodeRole) super.getFlexoRole();
		}

	}
}
