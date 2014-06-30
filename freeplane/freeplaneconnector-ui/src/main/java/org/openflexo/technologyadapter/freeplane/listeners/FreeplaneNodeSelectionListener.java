package org.openflexo.technologyadapter.freeplane.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.freeplane.features.map.INodeSelectionListener;
import org.freeplane.features.map.NodeModel;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
import org.openflexo.view.controller.FlexoController;

public class FreeplaneNodeSelectionListener implements INodeSelectionListener {

    private final Map<NodeModel, IFreeplaneNode> nodesMap;
    private final FlexoController                controller;

    public FreeplaneNodeSelectionListener(final IFreeplaneMap map, final FlexoController controller) {
        super();
        this.controller = controller;
        this.nodesMap = new HashMap<NodeModel, IFreeplaneNode>();
        this.nodesMap.put(map.getRoot().getNodeModel(), map.getRoot());
        this.addNodes(map.getRoot().getChildren());
    }

    @Override
    public void onDeselect(final NodeModel node) {
        // Notihg to implement here
    }

    @Override
    public void onSelect(final NodeModel node) {
        this.controller.getSelectionManager().setSelectedObject(this.nodesMap.get(node));
    }

    public Map<NodeModel, IFreeplaneNode> getNodesMap() {
        return this.nodesMap;
    }

    public void addNodes(final List<IFreeplaneNode> nodes) {
        for (final IFreeplaneNode childNode : nodes) {
            this.nodesMap.put(childNode.getNodeModel(), childNode);
            if (childNode.getChildren() != null) {
                this.addNodes(childNode.getChildren());
            }
        }
    }
}
