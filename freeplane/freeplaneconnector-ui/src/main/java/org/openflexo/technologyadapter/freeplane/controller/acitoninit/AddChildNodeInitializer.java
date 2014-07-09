package org.openflexo.technologyadapter.freeplane.controller.acitoninit;

import java.util.EventObject;

import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
import org.openflexo.technologyadapter.freeplane.model.actions.NewChildNode;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class AddChildNodeInitializer extends ActionInitializer<NewChildNode, IFreeplaneNode, IFreeplaneMap> {

    public AddChildNodeInitializer(final ControllerActionInitializer controllerActionInitializer) {
        super(NewChildNode.actionType, controllerActionInitializer);
    }

    @Override
    protected FlexoActionInitializer<NewChildNode> getDefaultInitializer() {
        return new AddChildNodeDefaultInit();
    }

    private class AddChildNodeDefaultInit extends FlexoActionInitializer<NewChildNode> {

        @Override
        public boolean run(final EventObject event, final NewChildNode action) {
            // Maybe something to do here
            return true;
        }

    }

}
