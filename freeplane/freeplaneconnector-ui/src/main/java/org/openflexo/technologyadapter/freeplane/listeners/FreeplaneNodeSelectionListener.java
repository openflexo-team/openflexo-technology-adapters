package org.openflexo.technologyadapter.freeplane.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.freeplane.features.map.INodeSelectionListener;
import org.freeplane.features.map.NodeModel;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.view.VirtualModelInstance.ObjectLookupResult;
import org.openflexo.technologyadapter.freeplane.fml.FMLControlledFreeplaneVirtualModelInstanceNature;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
import org.openflexo.view.controller.FlexoController;

public class FreeplaneNodeSelectionListener implements INodeSelectionListener {

    private final Map<NodeModel, IFreeplaneNode> nodesMap;
    private final FlexoController controller;
	private final boolean isFMLControlled;
	private VirtualModelInstance vmi;

    public FreeplaneNodeSelectionListener(final IFreeplaneMap map, final FlexoController controller) {
		this(map, controller, false);
    }

	public FreeplaneNodeSelectionListener(final IFreeplaneMap map, final FlexoController controller, final boolean isFMLControlled) {
		super();
		this.controller = controller;
		this.nodesMap = new HashMap<NodeModel, IFreeplaneNode>();
		this.nodesMap.put(map.getRoot().getNodeModel(), map.getRoot());
		addNodes(map.getRoot().getChildren());
		this.isFMLControlled = isFMLControlled;
	}

	public FreeplaneNodeSelectionListener(final VirtualModelInstance vmi, final FlexoController controller) {
		this(FMLControlledFreeplaneVirtualModelInstanceNature.getMap(vmi), controller, true);
		this.vmi = vmi;
	}

    @Override
    public void onDeselect(final NodeModel node) {
        // Notihg to implement here
    }

    @Override
    public void onSelect(final NodeModel node) {
        if (this.nodesMap.get(node) == null) {
            referenceNode(node);
        }
		final IFreeplaneNode flexoNode = this.nodesMap.get(node);
		if (this.isFMLControlled) {
			final ObjectLookupResult result = this.vmi.lookup(flexoNode);
			if (result != null) {
				this.controller.getSelectionManager().setSelectedObject(result.flexoConceptInstance);
				return;
			}
		}
        this.controller.getSelectionManager().setSelectedObject(this.nodesMap.get(node));
    }

    private boolean referenceNode(final NodeModel node) {
        if (node.getParentNode() == null) {
            // FreeNode, nothing to do. We don't support it in model, because
            // not initialized from start, dunno how to include them in the
            // model correctly.
            // Root node shouldn't be here (Has parent null too).
            return false;
        }
        if (this.nodesMap.get(node.getParentNode()) == null && !referenceNode(node.getParentNode())) {
            // In this case : Parent is not yet referenced so second condition
            // is called. When ( 0 && ...), the second part is not evaluated. If
            // parent referencing has failed too, we won't try to do something
            // here (no FreeNode support in model)
            return false;
        }
        // Case of null return can happen if there is something wrong in the
        // model. HashMap support it, won,t make other operations here.
        this.nodesMap.put(node, findModelForChild(this.nodesMap.get(node.getParentNode()).getChildren(), node));
        return true;
    }

    /**
     * Can return null if node is not represented in model
     * 
     * @param children
     * @param node
     * @return IFreeplaneNode which NodeModel is <code>node</code>
     */
    private IFreeplaneNode findModelForChild(final List<IFreeplaneNode> children, final NodeModel node) {
        IFreeplaneNode returned = null;
        for (final IFreeplaneNode child : children) {
            if (child.getNodeModel() == node) {
                returned = child;
            }
        }
        return returned;
    }

    public Map<NodeModel, IFreeplaneNode> getNodesMap() {
        return this.nodesMap;
    }

    public void addNodes(final List<IFreeplaneNode> nodes) {
        for (final IFreeplaneNode childNode : nodes) {
            this.nodesMap.put(childNode.getNodeModel(), childNode);
            if (childNode.getChildren() != null) {
                addNodes(childNode.getChildren());
            }
        }
    }
}
