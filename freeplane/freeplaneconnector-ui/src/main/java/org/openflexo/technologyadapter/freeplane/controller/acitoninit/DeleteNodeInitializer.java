package org.openflexo.technologyadapter.freeplane.controller.acitoninit;

import java.util.EventObject;

import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
import org.openflexo.technologyadapter.freeplane.model.actions.DeleteNode;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class DeleteNodeInitializer extends ActionInitializer<DeleteNode, IFreeplaneNode, IFreeplaneMap> {

    public DeleteNodeInitializer(final ControllerActionInitializer controllerActionInitializer) {
        super(DeleteNode.actionType, controllerActionInitializer);
    }

    @Override
    protected FlexoActionInitializer<DeleteNode> getDefaultInitializer() {
        return new DeleteNodeDefaultInit();
    }

    private class DeleteNodeDefaultInit extends FlexoActionInitializer<DeleteNode> {

        @Override
        public boolean run(final EventObject event, final DeleteNode action) {
            // Maybe something to do here
            return true;
        }

    }

}
