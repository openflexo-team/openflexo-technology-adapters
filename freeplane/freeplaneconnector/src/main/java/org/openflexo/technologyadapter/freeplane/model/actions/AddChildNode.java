package org.openflexo.technologyadapter.freeplane.model.actions;

import java.util.Vector;

import org.freeplane.features.map.NodeModel;
import org.freeplane.features.map.mindmapmode.MMapController;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.mode.ModeController;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.ActionGroup;
import org.openflexo.foundation.action.ActionMenu;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
import org.openflexo.technologyadapter.freeplane.model.impl.FreeplaneNodeImpl;

public class AddChildNode extends FlexoAction<AddChildNode, IFreeplaneNode, IFreeplaneMap> {

    private static final class AddChildNodeActionType extends FlexoActionType<AddChildNode, IFreeplaneNode, IFreeplaneMap> {

        protected AddChildNodeActionType(final String actionName, final ActionMenu actionMenu, final ActionGroup actionGroup,
                final int actionCategory) {
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

    public static final FlexoActionType<AddChildNode, IFreeplaneNode, IFreeplaneMap> actionType = new AddChildNodeActionType(
                                                                                                        "add_child_node",
                                                                                                        FlexoActionType.newMenu,
                                                                                                        FlexoActionType.editGroup,
                                                                                                        FlexoActionType.ADD_ACTION_TYPE);

    public AddChildNode(final IFreeplaneNode focusedObject, final Vector<IFreeplaneMap> globalSelection, final FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    static {
        FlexoObjectImpl.addActionForClass(actionType, FreeplaneNodeImpl.class);
    }

    @Override
    protected void doAction(final Object objet) throws FlexoException {
        if (objet instanceof IFreeplaneNode) {
            // Freeplane code from NewChildAction.
            final ModeController modeController = Controller.getCurrentModeController();
            final MMapController mapController = (MMapController) modeController.getMapController();
            final NodeModel newNode = mapController.addNewNode(MMapController.NEW_CHILD);
            ((IFreeplaneNode) objet).addChild(newNode);
        }
    }

}
