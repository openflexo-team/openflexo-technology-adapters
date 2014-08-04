package org.openflexo.technologyadapter.freeplane.fml.editionactions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.map.NodeModel;
import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.binding.DataBinding.BindingDefinitionType;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.view.FreeModelSlotInstance;
import org.openflexo.foundation.view.action.FlexoBehaviourAction;
import org.openflexo.foundation.viewpoint.annotations.FIBPanel;
import org.openflexo.foundation.viewpoint.editionaction.AssignableAction;
import org.openflexo.model.annotations.*;
import org.openflexo.technologyadapter.freeplane.IFreeplaneModelSlot;
import org.openflexo.technologyadapter.freeplane.fml.editionactions.AddChildNodeAction.AddChildNodeActionImpl;
import org.openflexo.technologyadapter.freeplane.fml.structural.IFreeplaneNodeRole;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

@ModelEntity
@XMLElement
@FIBPanel("Fib/AddChildNodePanel.fib")
@ImplementationClass(value = AddChildNodeActionImpl.class)
public interface AddChildNodeAction extends AssignableAction<IFreeplaneModelSlot, IFreeplaneNode> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String PARENT_KEY = "parent";

	@Getter(value = PARENT_KEY)
	@XMLAttribute
	public DataBinding<IFreeplaneNode> getParent();

	@Setter(value = PARENT_KEY)
	public void setParent(DataBinding<IFreeplaneNode> parent);

	public abstract static class AddChildNodeActionImpl extends AssignableActionImpl<IFreeplaneModelSlot, IFreeplaneNode>
			implements AddChildNodeAction {

		private static final Logger LOGGER = Logger.getLogger(AddChildNodeActionImpl.class.getPackage().getName());

		private DataBinding<IFreeplaneNode> parent;

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
			final FreeModelSlotInstance<IFreeplaneMap, IFreeplaneModelSlot> modelSlotInstance = getModelSlotInstance(action);
			if (modelSlotInstance.getResourceData() != null) {
				final IFreeplaneNode bindedParent = getParent(action);
				NodeModel nodeModel = new NodeModel(bindedParent.getNodeModel().getMap());
				bindedParent.getNodeModel().insert(nodeModel);
				bindedParent.addFreeplaneChild(nodeModel);
				modelSlotInstance.getResourceData().setIsModified();
				bindedParent.setModified(true);
				return bindedParent;
			}
			return null;
		}

		@Override
		public DataBinding<IFreeplaneNode> getParent() {
			if (this.parent == null) {
				parent = new DataBinding<IFreeplaneNode>(this, IFreeplaneNode.class, BindingDefinitionType.GET);
				parent.setBindingName(PARENT_KEY);
			}
			return this.parent;
		}

		private IFreeplaneNode getParent(final FlexoBehaviourAction<?, ?, ?> action) {
			final String errorMsg = "Error while getting binding value for action " + action;
			try {
				return getParent().getBindingValue(action);
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
		public void setParent(final DataBinding<IFreeplaneNode> parent) {
			if (parent != null) {
				parent.setOwner(this);
				parent.setDeclaredType(IFreeplaneNode.class);
				parent.setBindingDefinitionType(BindingDefinitionType.GET);
				parent.setBindingName(PARENT_KEY);
			}
			this.parent = parent;
		}

		@SuppressWarnings("unchecked")
		@Override
		public FreeModelSlotInstance<IFreeplaneMap, IFreeplaneModelSlot> getModelSlotInstance(final FlexoBehaviourAction<?, ?, ?> action) {
			return (FreeModelSlotInstance<IFreeplaneMap, IFreeplaneModelSlot>) super.getModelSlotInstance(action);
		}
	}
}
