package org.openflexo.technologyadapter.freeplane.model.actions;

import java.awt.event.ActionEvent;
import java.util.Vector;

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

public class NewSiblingAboveNode extends FlexoAction<NewSiblingAboveNode, IFreeplaneNode, IFreeplaneMap> {

    private static final class NewSiblingAboveNodeActionType extends FlexoActionType<NewSiblingAboveNode, IFreeplaneNode, IFreeplaneMap> {

        public NewSiblingAboveNodeActionType(final String actionName, final ActionMenu actionMenu, final ActionGroup actionGroup,
                final int actionCategory) {
            super(actionName, actionMenu, actionGroup, actionCategory);
        }

        @Override
        public NewSiblingAboveNode makeNewAction(final IFreeplaneNode focusedObject, final Vector<IFreeplaneMap> globalSelection,
                final FlexoEditor editor) {
            return new NewSiblingAboveNode(actionType, focusedObject, globalSelection, editor);
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

    public static final FlexoActionType<NewSiblingAboveNode, IFreeplaneNode, IFreeplaneMap> actionType = new NewSiblingAboveNodeActionType(
            "add_sibling_above_node", FlexoActionType.newMenu, FlexoActionType.editGroup, FlexoActionType.ADD_ACTION_TYPE);

    static {
        FlexoObjectImpl.addActionForClass(actionType, IFreeplaneNode.class);
    }

    public NewSiblingAboveNode(final FlexoActionType<NewSiblingAboveNode, IFreeplaneNode, IFreeplaneMap> actionType,
            final IFreeplaneNode focusedObject, final Vector<IFreeplaneMap> globalSelection, final FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    @Override
    protected void doAction(final Object context) throws FlexoException {
        Controller.getCurrentModeController().getAction("NewPreviousSiblingAction").actionPerformed((ActionEvent) context);
    }

}
