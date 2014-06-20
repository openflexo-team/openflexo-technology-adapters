package org.openflexo.technologyadapter.freeplane.model.actions;

import java.util.Vector;

import org.freeplane.features.map.NodeModel;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.action.ActionGroup;
import org.openflexo.foundation.action.ActionMenu;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

public class AddChildNode extends FlexoAction<AddChildNode, IFreeplaneNode, IFreeplaneMap> {

    private static final class AddChildNodeActionType extends FlexoActionType<AddChildNode, IFreeplaneNode, IFreeplaneMap> {

        protected AddChildNodeActionType(final String actionName, final ActionMenu actionMenu, final ActionGroup actionGroup, final int actionCategory) {
            super(actionName, actionMenu, actionGroup, actionCategory);
        }

        @Override
        public AddChildNode makeNewAction(final IFreeplaneNode node, final Vector<IFreeplaneMap> maps, final FlexoEditor flexoEditor) {
            return new AddChildNode(node, maps, flexoEditor);
        }

        @Override
        public boolean isVisibleForSelection(final IFreeplaneNode node, final Vector<IFreeplaneMap> map) {
            return node != null && node.getNodeModel().isVisible();
        }

        @Override
        public boolean isEnabledForSelection(final IFreeplaneNode node, final Vector<IFreeplaneMap> map) {
            return node != null && !node.getNodeModel().getMap().isReadOnly();
        }
    }

    public static FlexoActionType<AddChildNode, IFreeplaneNode, IFreeplaneMap> actionType = new AddChildNodeActionType("create_data_property", FlexoActionType.newMenu, FlexoActionType.defaultGroup,
                                                                                                  FlexoActionType.ADD_ACTION_TYPE);

    public AddChildNode(final IFreeplaneNode focusedObject, final Vector<IFreeplaneMap> globalSelection, final FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    @Override
    protected void doAction(final Object objet) throws FlexoException {
        if (objet instanceof IFreeplaneNode) {
            final IFreeplaneNode node = (IFreeplaneNode) objet;
            node.getNodeModel().insert(new NodeModel(node.getNodeModel().getMap()));
        }
    }

}
