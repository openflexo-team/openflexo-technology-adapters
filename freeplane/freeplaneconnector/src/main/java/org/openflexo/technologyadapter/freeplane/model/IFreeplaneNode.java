package org.openflexo.technologyadapter.freeplane.model;

import java.util.List;

import org.freeplane.features.map.NodeModel;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.model.impl.FreeplaneNodeImpl;

@ModelEntity
@ImplementationClass(value = FreeplaneNodeImpl.class)
public interface IFreeplaneNode extends TechnologyObject<FreeplaneTechnologyAdapter> {

    public static final String NODE_MODEL_KEY = "nodeModel";

    public static final String PARENT_KEY     = "parent";

    public static final String CHILDREN_KEY   = "children";

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
     * Implementation needed to bind Freeplane add action to Openflexo add
     * action.
     * 
     * @param fpNodeModel
     * @return true if correctly added
     */
    public boolean addChild(NodeModel fpNodeModel);

    /**
     * Removed done by entity to remove. Nothing done to do it by index.
     * 
     * @param nodeToRemove
     */
    @Remover(value = CHILDREN_KEY)
    public void removeChild(IFreeplaneNode nodeToRemove);

    @Setter(value = CHILDREN_KEY)
    public void setChildren(List<IFreeplaneNode> list);
}
