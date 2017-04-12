/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Freeplane, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.freeplane.fml.editionactions;

import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.map.NodeModel;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.gina.annotation.FIBPanel;
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
@FML("AddChildNodeAction")
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

	public abstract static class AddChildNodeActionImpl
			extends TechnologySpecificActionImpl<FreeplaneModelSlot, IFreeplaneMap, IFreeplaneNode> implements AddChildNodeAction {

		private static final Logger LOGGER = Logger.getLogger(AddChildNodeActionImpl.class.getPackage().getName());

		private DataBinding<IFreeplaneNode> parent;

		private DataBinding<String> nodeText;

		@Override
		public Type getAssignableType() {
			return IFreeplaneNode.class;
		}

		@Override
		public IFreeplaneNodeRole getAssignedFlexoProperty() {
			return (IFreeplaneNodeRole) super.getAssignedFlexoProperty();
		}

		@Override
		public IFreeplaneNode execute(final RunTimeEvaluationContext evaluationContext) {
			final FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot> modelSlotInstance = getModelSlotInstance(evaluationContext);
			if (modelSlotInstance.getResourceData() != null) {
				final IFreeplaneNode bindedParent = getParent(evaluationContext);
				final String bindedNodeText = getBindedNodeText(evaluationContext);
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

		private IFreeplaneNode getParent(final RunTimeEvaluationContext evaluationContext) {
			final String errorMsg = "Error while getting binding value for action " + evaluationContext;
			try {
				return getParent().getBindingValue(evaluationContext);
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

		private String getBindedNodeText(RunTimeEvaluationContext evaluationContext) {
			final String errorMsg = "Error while getting binding value for action " + evaluationContext;
			try {
				return getNodeText().getBindingValue(evaluationContext);
			} catch (final Exception e) {
				LOGGER.log(Level.SEVERE, errorMsg, e);
			}
			return "";
		}

		@SuppressWarnings("unchecked")
		@Override
		public FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot> getModelSlotInstance(
				final RunTimeEvaluationContext evaluationContext) {
			return (FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot>) super.getModelSlotInstance(evaluationContext);
		}
	}
}
