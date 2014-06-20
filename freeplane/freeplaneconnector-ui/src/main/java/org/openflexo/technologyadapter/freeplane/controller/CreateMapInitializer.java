package org.openflexo.technologyadapter.freeplane.controller;

import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.actions.CreateMap;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class CreateMapInitializer extends ActionInitializer<CreateMap, IFreeplaneMap, IFreeplaneMap> {

    public CreateMapInitializer(final ControllerActionInitializer controllerActionInitializer) {
        super(CreateMap.actionType, controllerActionInitializer);
    }

}
