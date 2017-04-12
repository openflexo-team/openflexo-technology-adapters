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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.map.NodeModel;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
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
@FML("AddSiblingNodeAction")
public interface AddSiblingNodeAction extends FreePlaneAction<IFreeplaneNode> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String TARGET_NODE_KEY = "targetNode";

	@PropertyIdentifier(type = DataBinding.class)
	public static final String IS_ABOVE_KEY = "above";

	/* Getters and setters */

	@Getter(value = TARGET_NODE_KEY)
	@XMLAttribute
	public DataBinding<IFreeplaneNode> getTargetNode();

	@Setter(value = TARGET_NODE_KEY)
	public void setTargetNode(DataBinding<IFreeplaneNode> targetNode);

	@Getter(value = IS_ABOVE_KEY)
	@XMLAttribute
	public DataBinding<Boolean> isAbove();

	@Setter(value = IS_ABOVE_KEY)
	public void setIsAbove(DataBinding<Boolean> isAbove);

	public abstract static class AddSiblingNodeActionImpl
			extends TechnologySpecificActionImpl<FreeplaneModelSlot, IFreeplaneMap, IFreeplaneNode> implements AddSiblingNodeAction {

		private static final Logger LOGGER = Logger.getLogger(AddSiblingNodeAction.class.getPackage().getName());

		private DataBinding<IFreeplaneNode> targetNode;

		private DataBinding<Boolean> above;

		@Override
		public DataBinding<IFreeplaneNode> getTargetNode() {
			if (targetNode == null) {
				targetNode = new DataBinding<IFreeplaneNode>(this, IFreeplaneNode.class, BindingDefinitionType.GET);
				targetNode.setBindingName(TARGET_NODE_KEY);
			}
			return targetNode;
		}

		@Override
		public DataBinding<Boolean> isAbove() {
			if (above == null) {
				above = new DataBinding<Boolean>(this, Boolean.class, BindingDefinitionType.GET);
				above.setBindingName(IS_ABOVE_KEY);
			}
			return above;
		}

		@Override
		public void setTargetNode(DataBinding<IFreeplaneNode> targetNode) {
			if (targetNode != null) {
				targetNode.setOwner(this);
				targetNode.setDeclaredType(IFreeplaneNode.class);
				targetNode.setBindingDefinitionType(BindingDefinitionType.GET);
				targetNode.setBindingName(TARGET_NODE_KEY);
			}
			this.targetNode = targetNode;
		}

		@Override
		public void setIsAbove(DataBinding<Boolean> above) {
			if (above != null) {
				above.setOwner(this);
				above.setDeclaredType(Boolean.class);
				above.setBindingDefinitionType(BindingDefinitionType.GET);
				above.setBindingName(IS_ABOVE_KEY);
			}
			this.above = above;
		}

		@Override
		public IFreeplaneNode execute(RunTimeEvaluationContext evaluationContext) {
			final FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot> modelSlotInstance = getModelSlotInstance(evaluationContext);
			if (modelSlotInstance.getResourceData() != null) {
				final IFreeplaneNode bindedTarget = getTargetNode(evaluationContext);
				final NodeModel freeplaneParent = bindedTarget.getNodeModel().getParentNode();
				int nodeIndex = freeplaneParent.getIndex(bindedTarget.getNodeModel());
				if (!isAbove(evaluationContext)) {
					nodeIndex++;
				}
				final NodeModel newNode = new NodeModel(bindedTarget.getNodeModel().getMap());
				freeplaneParent.insert(newNode, nodeIndex);
				bindedTarget.getParent().addFreeplaneChild(newNode);
				modelSlotInstance.getResourceData().setIsModified();
				bindedTarget.setModified(true);
				return bindedTarget;
			}
			return null;
		}

		private IFreeplaneNode getTargetNode(RunTimeEvaluationContext evaluationContext) {
			final String errorMsg = "Error while getting binding value for action " + evaluationContext;
			try {
				return getTargetNode().getBindingValue(evaluationContext);
			} catch (final TypeMismatchException e) {
				LOGGER.log(Level.SEVERE, errorMsg, e);
			} catch (final NullReferenceException e) {
				LOGGER.log(Level.SEVERE, errorMsg, e);
			} catch (final InvocationTargetException e) {
				LOGGER.log(Level.SEVERE, errorMsg, e);
			}
			return null;
		}

		private boolean isAbove(RunTimeEvaluationContext evaluationContext) {
			final String errorMsg = "Error while getting binding value for action " + evaluationContext;
			try {
				return isAbove().getBindingValue(evaluationContext);
			} catch (final TypeMismatchException e) {
				LOGGER.log(Level.SEVERE, errorMsg, e);
			} catch (final NullReferenceException e) {
				LOGGER.log(Level.SEVERE, errorMsg, e);
			} catch (final InvocationTargetException e) {
				LOGGER.log(Level.SEVERE, errorMsg, e);
			}
			return false;
		}

		@Override
		public FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot> getModelSlotInstance(
				final RunTimeEvaluationContext evaluationContext) {
			return (FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot>) super.getModelSlotInstance(evaluationContext);
		}

		@Override
		public Type getAssignableType() {
			return IFreeplaneNode.class;
		}

		@Override
		public IFreeplaneNodeRole getAssignedFlexoProperty() {
			return (IFreeplaneNodeRole) super.getAssignedFlexoProperty();
		}

	}
}
