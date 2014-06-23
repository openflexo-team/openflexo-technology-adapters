package org.openflexo.technologyadapter.freeplane.controller;

import java.util.EventObject;

import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.actions.CreateMap;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class CreateMapInitializer extends ActionInitializer<CreateMap, IFreeplaneMap, IFreeplaneMap> {

    public CreateMapInitializer(final ControllerActionInitializer controllerActionInitializer) {
        super(CreateMap.actionType, controllerActionInitializer);
    }

    @Override
    protected FlexoActionInitializer<CreateMap> getDefaultInitializer() {
        return new CreateMapDefaultInit();
    }

    private class CreateMapDefaultInit extends FlexoActionInitializer<CreateMap> {

        @Override
        public boolean run(final EventObject event, final CreateMap action) {
            return true;
        }

    }
}
