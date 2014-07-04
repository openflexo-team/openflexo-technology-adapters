package org.openflexo.technologyadapter.freeplane.controller.acitoninit;

import java.util.EventObject;

import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.technologyadapter.freeplane.libraries.FreeplaneFIBLibrary;
import org.openflexo.technologyadapter.freeplane.model.actions.CreateFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.rm.IFreeplaneResource;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class CreateFreeplaneMapInitializer
        extends
            ActionInitializer<CreateFreeplaneMap, RepositoryFolder<IFreeplaneResource>, RepositoryFolder<IFreeplaneResource>> {

    public CreateFreeplaneMapInitializer(final ControllerActionInitializer controllerActionInitializer) {
        super(CreateFreeplaneMap.actionType, controllerActionInitializer);
    }

    @Override
    protected FlexoActionInitializer<CreateFreeplaneMap> getDefaultInitializer() {
        return new CreateFreeplaneMapDefaultInit();
    }

    private class CreateFreeplaneMapDefaultInit extends FlexoActionInitializer<CreateFreeplaneMap> {

        @Override
        public boolean run(final EventObject event, final CreateFreeplaneMap action) {
            return CreateFreeplaneMapInitializer.this.instanciateAndShowDialog(action, FreeplaneFIBLibrary.CREATE_MAP_DIALOG_FIB);
        }

    }

}
