package org.openflexo.technologyadapter.freeplane.controller.acitoninit;

import java.util.EventObject;

import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
import org.openflexo.technologyadapter.freeplane.model.actions.NewSiblingNode;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class NewSiblingNodeInitializer extends ActionInitializer<NewSiblingNode, IFreeplaneNode, IFreeplaneMap> {

    public NewSiblingNodeInitializer(final ControllerActionInitializer controllerActionInitializer) {
        super(NewSiblingNode.actionType, controllerActionInitializer);
    }

    @Override
    protected FlexoActionInitializer<NewSiblingNode> getDefaultInitializer() {
        return new NewSiblingNodeDefaultInit();
    }

    private class NewSiblingNodeDefaultInit extends FlexoActionInitializer<NewSiblingNode> {

        @Override
        public boolean run(final EventObject event, final NewSiblingNode action) {
            // Maybe something to do here
            return true;
        }

    }

}
