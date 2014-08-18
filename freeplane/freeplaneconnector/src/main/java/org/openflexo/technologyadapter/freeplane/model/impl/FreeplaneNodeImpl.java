package org.openflexo.technologyadapter.freeplane.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.attribute.Attribute;
import org.freeplane.features.attribute.NodeAttributeTableModel;
import org.freeplane.features.map.NodeModel;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

public abstract class FreeplaneNodeImpl extends FlexoObjectImpl implements IFreeplaneNode {

	private static final Logger LOGGER = Logger.getLogger(FreeplaneNodeImpl.class.getName());

	private FreeplaneTechnologyAdapter technologyAdapter;

	private NodeModel nodeModel;

	public FreeplaneNodeImpl() {
	}

	public void setTechnologyAdapter(final FreeplaneTechnologyAdapter technologyAdapter) {
		this.technologyAdapter = technologyAdapter;
	}

	@Override
	public FreeplaneTechnologyAdapter getTechnologyAdapter() {
		return this.technologyAdapter;
	}

	/**
	 * Recursive call to initialize all child too, so don't do it in caller.
	 *
	 * @param model that will be set
	 */
	@Override
	@Setter(value = NODE_MODEL_KEY)
	public void setNodeModel(final NodeModel model) {
		try {
			this.nodeModel = model;
			final ModelFactory factory = new ModelFactory(IFreeplaneNode.class);
			final List<IFreeplaneNode> modelizedChildren = new ArrayList<IFreeplaneNode>();
			for (final NodeModel fpChild : model.getChildren()) {
				final FreeplaneNodeImpl child = (FreeplaneNodeImpl) factory.newInstance(IFreeplaneNode.class);
				child.setTechnologyAdapter(this.getTechnologyAdapter());
				child.setParent(this);
				child.setNodeModel(fpChild);
				modelizedChildren.add(child);
			}
			this.setChildren(modelizedChildren);
		} catch (final ModelDefinitionException e) {
			final String msg = "";
			LOGGER.log(Level.SEVERE, msg, e);
		}
	}

	@Override
	public void addFreeplaneChild(final NodeModel fpNodeModel) {
		try {
			final ModelFactory factory = new ModelFactory(IFreeplaneNode.class);
			final FreeplaneNodeImpl child = (FreeplaneNodeImpl) factory.newInstance(IFreeplaneNode.class);
			child.setTechnologyAdapter(this.getTechnologyAdapter());
			child.setNodeModel(fpNodeModel);
			child.setParent(this);
			this.addChild(child);
		} catch (final ModelDefinitionException e) {
			final String msg = "Error while adding a child to a node in model.";
			LOGGER.log(Level.SEVERE, msg, e);
		}
	}

	@Override
	@Getter(value = NODE_MODEL_KEY, ignoreType = true)
	public NodeModel getNodeModel() {
		return this.nodeModel;
	}

	@Override
	public String getUri() {
		return "Node=" + this.getNodeModel().getID();
	}

	@Override
	public Vector<Attribute> getNodeAttributes() {
		NodeAttributeTableModel attributeTable = NodeAttributeTableModel.getModel(getNodeModel());
		return attributeTable.getAttributes();
	}
}
