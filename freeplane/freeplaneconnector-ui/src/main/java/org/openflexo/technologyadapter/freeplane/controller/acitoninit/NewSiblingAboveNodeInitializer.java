package org.openflexo.technologyadapter.freeplane.controller.acitoninit;

import java.util.EventObject;

import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
import org.openflexo.technologyadapter.freeplane.model.actions.NewSiblingAboveNode;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class NewSiblingAboveNodeInitializer extends ActionInitializer<NewSiblingAboveNode, IFreeplaneNode, IFreeplaneMap> {

    public NewSiblingAboveNodeInitializer(final ControllerActionInitializer controllerActionInitializer) {
        super(NewSiblingAboveNode.ACTION_TYPE, controllerActionInitializer);
    }

    @Override
    protected FlexoActionInitializer<NewSiblingAboveNode> getDefaultInitializer() {
        return new NewSiblingAboveNodeDefaultInit();
    }

    private class NewSiblingAboveNodeDefaultInit extends FlexoActionInitializer<NewSiblingAboveNode> {

        @Override
        public boolean run(final EventObject event, final NewSiblingAboveNode action) {
            // Maybe something to do here
            return true;
        }

    }

}
