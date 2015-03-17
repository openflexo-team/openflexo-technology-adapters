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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.FetchRequest;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.freeplane.FreeplaneModelSlot;
import org.openflexo.technologyadapter.freeplane.fml.editionactions.SelectAllNodes.SelectAllNodesImpl;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

@ModelEntity
@ImplementationClass(SelectAllNodesImpl.class)
@XMLElement
@FML("SelectAllNodes")
public interface SelectAllNodes extends FetchRequest<FreeplaneModelSlot, IFreeplaneNode> {

	public abstract class SelectAllNodesImpl extends FetchRequestImpl<FreeplaneModelSlot, IFreeplaneNode> implements SelectAllNodes {

		private static final Logger LOGGER = Logger.getLogger(SelectAllNodes.class.getPackage().getName());

		@Override
		public List<IFreeplaneNode> execute(final FlexoBehaviourAction action) {
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
