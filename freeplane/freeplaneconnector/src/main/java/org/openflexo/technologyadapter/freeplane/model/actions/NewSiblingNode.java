package org.openflexo.technologyadapter.freeplane.model.actions;

import java.util.Vector;

import org.freeplane.features.map.NodeModel;
import org.freeplane.features.map.mindmapmode.MMapController;
import org.freeplane.features.mode.Controller;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.ActionGroup;
import org.openflexo.foundation.action.ActionMenu;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

public class NewSiblingNode extends FlexoAction<NewSiblingNode, IFreeplaneNode, IFreeplaneMap> {

    private static final class NewSiblingNodeActionType extends FlexoActionType<NewSiblingNode, IFreeplaneNode, IFreeplaneMap> {

        public NewSiblingNodeActionType(final String actionName, final ActionMenu actionMenu, final ActionGroup actionGroup,
                final int actionCategory) {
            super(actionName, actionMenu, actionGroup, actionCategory);
        }

        @Override
        public NewSiblingNode makeNewAction(final IFreeplaneNode focusedObject, final Vector<IFreeplaneMap> globalSelection,
                final FlexoEditor editor) {
            return new NewSiblingNode(actionType, focusedObject, globalSelection, editor);
        }

        @Override
        public boolean isVisibleForSelection(final IFreeplaneNode node, final Vector<IFreeplaneMap> globalSelection) {
            return node != null && node.getNodeModel().isVisible();
        }

        @Override
        public boolean isEnabledForSelection(final IFreeplaneNode node, final Vector<IFreeplaneMap> globalSelection) {
            return node != null && !node.getNodeModel().getMap().isReadOnly();
        }

    }

    public static final FlexoActionType<NewSiblingNode, IFreeplaneNode, IFreeplaneMap> actionType = new NewSiblingNodeActionType(
            "add_sibling_node", AddChildNode.FREEPLANE_MENU, FlexoActionType.editGroup, FlexoActionType.ADD_ACTION_TYPE);

    static {
        FlexoObjectImpl.addActionForClass(actionType, IFreeplaneNode.class);
    }

    public NewSiblingNode(final FlexoActionType<NewSiblingNode, IFreeplaneNode, IFreeplaneMap> actionType,
            final IFreeplaneNode focusedObject, final Vector<IFreeplaneMap> globalSelection, final FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    @Override
    protected void doAction(final Object context) throws FlexoException {
        // Some Copy-paste from freeplane To allow us to update our model.
        final NodeModel brother = ((MMapController) Controller.getCurrentModeController().getMapController())
                .addNewNode(MMapController.NEW_SIBLING_BEHIND);
        this.getFocusedObject().getParent().addChild(brother);
        this.getFocusedObject().setModified(true);
    }

}
