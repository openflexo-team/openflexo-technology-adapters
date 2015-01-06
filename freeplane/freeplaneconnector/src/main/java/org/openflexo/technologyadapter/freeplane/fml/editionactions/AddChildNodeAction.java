package org.openflexo.technologyadapter.freeplane.fml.editionactions;

import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.map.NodeModel;
import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.binding.DataBinding.BindingDefinitionType;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.freeplane.FreeplaneModelSlot;
import org.openflexo.technologyadapter.freeplane.fml.editionactions.AddChildNodeAction.AddChildNodeActionImpl;
import org.openflexo.technologyadapter.freeplane.fml.structural.IFreeplaneNodeRole;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

@ModelEntity
@XMLElement
@FIBPanel("Fib/AddChildNodePanel.fib")
@ImplementationClass(value = AddChildNodeActionImpl.class)
public interface AddChildNodeAction extends FreePlaneAction<IFreeplaneNode> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String PARENT_KEY = "parent";

	@Getter(value = PARENT_KEY)
	@XMLAttribute
	public DataBinding<IFreeplaneNode> getParent();

	@Setter(value = PARENT_KEY)
	public void setParent(DataBinding<IFreeplaneNode> parent);

	@PropertyIdentifier(type = DataBinding.class)
	public static final String NODE_TEXT_KEY = "nodeText";

	@Getter(value = NODE_TEXT_KEY)
	@XMLAttribute
	public DataBinding<String> getNodeText();

	@Setter(value = NODE_TEXT_KEY)
	public void setNodeText(DataBinding<String> nodeText);

	public abstract static class AddChildNodeActionImpl extends TechnologySpecificActionImpl<FreeplaneModelSlot, IFreeplaneNode> implements
			AddChildNodeAction {

		private static final Logger LOGGER = Logger.getLogger(AddChildNodeActionImpl.class.getPackage().getName());

		private DataBinding<IFreeplaneNode> parent;

		private DataBinding<String> nodeText;

		@Override
		public Type getAssignableType() {
			return IFreeplaneNode.class;
		}

		@Override
		public IFreeplaneNodeRole getFlexoRole() {
			return (IFreeplaneNodeRole) super.getFlexoRole();
		}

		@Override
		public IFreeplaneNode execute(final FlexoBehaviourAction action) {
			final FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot> modelSlotInstance = getModelSlotInstance(action);
			if (modelSlotInstance.getResourceData() != null) {
				final IFreeplaneNode bindedParent = getParent(action);
				final String bindedNodeText = getBindedNodeText(action);
				NodeModel nodeModel = new NodeModel(bindedParent.getNodeModel().getMap());
				nodeModel.setUserObject(bindedNodeText);
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
			} catch (final Exception e) {
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

		@Override
		public DataBinding<String> getNodeText() {
			if (this.nodeText == null) {
				this.nodeText = new DataBinding<String>(this, String.class, BindingDefinitionType.GET);
				this.nodeText.setBindingName(NODE_TEXT_KEY);
			}
			return this.nodeText;
		}

		@Override
		public void setNodeText(DataBinding<String> pNodeText) {
			if (pNodeText != null) {
				pNodeText.setOwner(this);
				pNodeText.setDeclaredType(String.class);
				pNodeText.setBindingDefinitionType(BindingDefinitionType.GET);
				pNodeText.setBindingName(NODE_TEXT_KEY);
			}
			this.nodeText = pNodeText;
		}

		private String getBindedNodeText(FlexoBehaviourAction<?, ?, ?> action) {
			final String errorMsg = "Error while getting binding value for action " + action;
			try {
				return getNodeText().getBindingValue(action);
			} catch (final Exception e) {
				LOGGER.log(Level.SEVERE, errorMsg, e);
			}
			return "";
		}

		@SuppressWarnings("unchecked")
		@Override
		public FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot> getModelSlotInstance(final FlexoBehaviourAction<?, ?, ?> action) {
			return (FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot>) super.getModelSlotInstance(action);
		}
	}
}
