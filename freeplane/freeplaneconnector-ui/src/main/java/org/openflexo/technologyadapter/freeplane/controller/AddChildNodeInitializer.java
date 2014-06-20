package org.openflexo.technologyadapter.freeplane.controller;

import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
import org.openflexo.technologyadapter.freeplane.model.actions.AddChildNode;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class AddChildNodeInitializer extends ActionInitializer<AddChildNode, IFreeplaneNode, IFreeplaneMap> {

    public AddChildNodeInitializer(final ControllerActionInitializer controllerActionInitializer) {
        super(AddChildNode.actionType, controllerActionInitializer);
    }

}
