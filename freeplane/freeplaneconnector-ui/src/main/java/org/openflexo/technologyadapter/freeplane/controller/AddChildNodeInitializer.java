package org.openflexo.technologyadapter.freeplane.controller;

import java.util.EventObject;

import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
import org.openflexo.technologyadapter.freeplane.model.actions.AddChildNode;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class AddChildNodeInitializer extends ActionInitializer<AddChildNode, IFreeplaneNode, IFreeplaneMap> {

    public AddChildNodeInitializer(final ControllerActionInitializer controllerActionInitializer) {
        super(AddChildNode.actionType, controllerActionInitializer);
    }

    @Override
    protected FlexoActionInitializer<AddChildNode> getDefaultInitializer() {
        return new AddChildNodeDefaultInit();
    }

    private class AddChildNodeDefaultInit extends FlexoActionInitializer<AddChildNode> {

        @Override
        public boolean run(final EventObject event, final AddChildNode action) {
            // Maybe something to do here
            return true;
        }

    }

}
