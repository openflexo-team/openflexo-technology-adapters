package org.openflexo.technologyadapter.freeplane.model.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.main.application.FreeplaneBasicAdapter;
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
		final String errorMsg = "Exception raised while creating new MindMap";
        // If no data have been load, initilalization has not been done, so do it.
        FreeplaneBasicAdapter.getInstance();
		final MapModel newMap = new MapModel();
		// Equivalent to createNewRoot, but the Method attach of nodeModel is
		// not public just without modifier ...
		final NodeModel root = new NodeModel(this.mapName, newMap);
		newMap.setRoot(root);
		try {
			final Method attach = NodeModel.class.getDeclaredMethod("attach");
			attach.setAccessible(true);
			attach.invoke(root);
		} catch (final IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, errorMsg, e);
		} catch (final NoSuchMethodException e) {
			LOGGER.log(Level.SEVERE, errorMsg, e);
		} catch (final SecurityException e) {
			LOGGER.log(Level.SEVERE, errorMsg, e);
		} catch (final IllegalArgumentException e) {
			LOGGER.log(Level.SEVERE, errorMsg, e);
		} catch (final InvocationTargetException e) {
			LOGGER.log(Level.SEVERE, errorMsg, e);
		}
		Controller.getCurrentController().getModeController().getMapController().fireMapCreated(newMap);
		Controller.getCurrentController().getModeController().getMapController().newMapView(newMap);
    }
}
