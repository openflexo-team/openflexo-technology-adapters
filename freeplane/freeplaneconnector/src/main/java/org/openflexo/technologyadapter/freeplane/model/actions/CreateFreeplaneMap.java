package org.openflexo.technologyadapter.freeplane.model.actions;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.ActionGroup;
import org.openflexo.foundation.action.ActionMenu;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.technologyadapter.freeplane.rm.IFreeplaneResource;

public class CreateFreeplaneMap
        extends
            FlexoAction<CreateFreeplaneMap, RepositoryFolder<IFreeplaneResource>, RepositoryFolder<IFreeplaneResource>> {
    private static final class CreateFreeplaneMapActionType
            extends
                FlexoActionType<CreateFreeplaneMap, RepositoryFolder<IFreeplaneResource>, RepositoryFolder<IFreeplaneResource>> {

        protected CreateFreeplaneMapActionType(final String actionName, final ActionMenu actionMenu, final ActionGroup actionGroup,
                final int actionCategory) {
            super(actionName, actionMenu, actionGroup, actionCategory);
        }

        @Override
        public CreateFreeplaneMap makeNewAction(final RepositoryFolder<IFreeplaneResource> node,
                final Vector<RepositoryFolder<IFreeplaneResource>> maps, final FlexoEditor flexoEditor) {
            return new CreateFreeplaneMap(node, maps, flexoEditor);
        }

        @Override
        public boolean isVisibleForSelection(final RepositoryFolder<IFreeplaneResource> node,
                final Vector<RepositoryFolder<IFreeplaneResource>> map) {
            return node.getResourceRepository().getResourceClass().equals(IFreeplaneResource.class);
        }

        @Override
        public boolean isEnabledForSelection(final RepositoryFolder<IFreeplaneResource> node,
                final Vector<RepositoryFolder<IFreeplaneResource>> map) {
            return true;
        }
    }

    public static final FlexoActionType<CreateFreeplaneMap, RepositoryFolder<IFreeplaneResource>, RepositoryFolder<IFreeplaneResource>> actionType = new CreateFreeplaneMapActionType(
            "create_mind_map", FlexoActionType.newMenu, FlexoActionType.editGroup, FlexoActionType.ADD_ACTION_TYPE);

    private static final Logger LOGGER = Logger.getLogger(CreateFreeplaneMap.class.getSimpleName());

    /** Property for fibs */
    public String mapName;

    public CreateFreeplaneMap(final RepositoryFolder<IFreeplaneResource> focusedObject,
            final Vector<RepositoryFolder<IFreeplaneResource>> globalSelection, final FlexoEditor editor) {
        super(actionType, focusedObject, globalSelection, editor);
    }

    static {
        FlexoObjectImpl.addActionForClass(actionType, RepositoryFolder.class);
    }

    @Override
    protected void doAction(final Object objet) throws FlexoException {
        LOGGER.info("One day this action will create a new freeplane map with name " + this.mapName);
    }
}
