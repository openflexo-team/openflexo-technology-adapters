/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.freeplane.model;

import java.util.List;
import java.util.Vector;

import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.map.NodeModel;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.model.impl.FreeplaneNodeImpl;

@ModelEntity
@XMLElement
@FML("FreeplaneNodeRole")
@ImplementationClass(value = FreeplaneNodeImpl.class)
public interface IFreeplaneNode extends TechnologyObject<FreeplaneTechnologyAdapter> {

	public static final String NODE_MODEL_KEY = "nodeModel";

	public static final String PARENT_KEY = "parent";

	public static final String CHILDREN_KEY = "children";

	@Getter(value = NODE_MODEL_KEY, ignoreType = true)
	public NodeModel getNodeModel();

	@Setter(value = NODE_MODEL_KEY)
	public void setNodeModel(NodeModel model);

	@Getter(value = PARENT_KEY)
	public IFreeplaneNode getParent();

	@Setter(value = PARENT_KEY)
	public void setParent(IFreeplaneNode node);

	@Getter(value = CHILDREN_KEY, cardinality = Cardinality.LIST)
	public List<IFreeplaneNode> getChildren();

	@Adder(value = CHILDREN_KEY)
	public void addChild(IFreeplaneNode node);

	/**
	 * Add a node with a freeplane object instead of an Openflexo one.<br> Back to a void return to have an API more consistent.
	 *
	 * @param fpNodeModel the freeplane object to add with pamela entity initialization.
	 */
	public void addFreeplaneChild(NodeModel fpNodeModel);

	/**
	 * Removed done by entity to remove. Nothing done to do it by index.
	 *
	 * @param node to remove
	 */
	@Remover(value = CHILDREN_KEY)
	public void removeChild(IFreeplaneNode node);

	@Setter(value = CHILDREN_KEY)
	public void setChildren(List<IFreeplaneNode> list);

	public String getUri();

	public Vector<Attribute> getNodeAttributes();
}
