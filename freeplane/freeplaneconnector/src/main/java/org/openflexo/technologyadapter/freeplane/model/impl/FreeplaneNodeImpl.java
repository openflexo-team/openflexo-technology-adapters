package org.openflexo.technologyadapter.freeplane.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.map.NodeModel;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

public abstract class FreeplaneNodeImpl implements IFreeplaneNode {

    private static final Logger        LOGGER = Logger.getLogger(FreeplaneNodeImpl.class.getName());

    private FreeplaneTechnologyAdapter technologyAdapter;

    public FreeplaneNodeImpl(final FreeplaneTechnologyAdapter technologyAdapter) {
        this.technologyAdapter = technologyAdapter;
    }

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
     * I fear the infinite loop. Parent set child that set parent that set
     * child, ...<br>
     * Skip the set call to setParent() and parent.setNodeModel(), cause the
     * only call seems to be done from load resourceData, so everything is
     * developed from root so all the tree would be correctly initialized
     * without this dangerous call.
     */
    @Override
    @Setter("nodeModel")
    public void setNodeModel(final NodeModel model) {
        try {
            final ModelFactory factory = new ModelFactory(IFreeplaneNode.class);
            final List<IFreeplaneNode> modelizedChildren = new ArrayList<IFreeplaneNode>();
            for (final NodeModel fpChild : model.getChildren()) {
                final FreeplaneNodeImpl child = (FreeplaneNodeImpl) factory.newInstance(IFreeplaneNode.class);
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
}
