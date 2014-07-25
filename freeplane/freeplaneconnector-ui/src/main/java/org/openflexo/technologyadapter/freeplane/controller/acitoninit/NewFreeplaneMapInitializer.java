package org.openflexo.technologyadapter.freeplane.controller.acitoninit;

import java.util.EventObject;

import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.technologyadapter.freeplane.libraries.FreeplaneFIBLibrary;
import org.openflexo.technologyadapter.freeplane.model.actions.NewFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.rm.IFreeplaneResource;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

public class NewFreeplaneMapInitializer
        extends
            ActionInitializer<NewFreeplaneMap, RepositoryFolder<IFreeplaneResource>, RepositoryFolder<IFreeplaneResource>> {

    public NewFreeplaneMapInitializer(final ControllerActionInitializer controllerActionInitializer) {
        super(NewFreeplaneMap.ACTION_TYPE, controllerActionInitializer);
    }

    @Override
    protected FlexoActionInitializer<NewFreeplaneMap> getDefaultInitializer() {
		return new NewFreeplaneMapDefaultInit();
    }

	private class NewFreeplaneMapDefaultInit extends FlexoActionInitializer<NewFreeplaneMap> {

        @Override
        public boolean run(final EventObject event, final NewFreeplaneMap action) {
			return NewFreeplaneMapInitializer.this.instanciateAndShowDialog(action, FreeplaneFIBLibrary.NEW_MAP_DIALOG_FIB);
        }

    }

}
