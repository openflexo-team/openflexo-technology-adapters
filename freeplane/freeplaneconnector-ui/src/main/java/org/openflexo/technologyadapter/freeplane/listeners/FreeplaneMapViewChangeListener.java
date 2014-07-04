package org.openflexo.technologyadapter.freeplane.listeners;

import org.freeplane.features.map.IMapChangeListener;
import org.freeplane.features.map.MapChangeEvent;
import org.freeplane.features.map.NodeModel;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

public class FreeplaneMapViewChangeListener implements IMapChangeListener {

    private final IFreeplaneMap bindedMap;

    public FreeplaneMapViewChangeListener(final IFreeplaneMap bindedMap) {
        super();
        this.bindedMap = bindedMap;
    }

    private void propagateChanges() {
        this.bindedMap.setModified(true);
    }

    @Override
    public void mapChanged(final MapChangeEvent event) {
        this.propagateChanges();
    }

    @Override
    public void onNodeDeleted(final NodeModel parent, final NodeModel child, final int index) {
        this.propagateChanges();
    }

    @Override
    public void onNodeInserted(final NodeModel parent, final NodeModel child, final int newIndex) {
        this.propagateChanges();
    }

    @Override
    public void onNodeMoved(final NodeModel oldParent, final int oldIndex, final NodeModel newParent, final NodeModel child,
            final int newIndex) {
        this.propagateChanges();
    }

    @Override
    public void onPreNodeMoved(final NodeModel oldParent, final int oldIndex, final NodeModel newParent, final NodeModel child,
            final int newIndex) {
        // Nothing to do in our case here.
    }

    @Override
    public void onPreNodeDelete(final NodeModel oldParent, final NodeModel selectedNode, final int index) {
        // Nothing to do in our case here.
    }

}
