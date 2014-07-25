package org.openflexo.technologyadapter.freeplane.model.actions;

import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mapio.MapIO;
import org.freeplane.features.mapio.mindmapmode.MMapIO;
import org.freeplane.features.mode.Controller;
import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.technologyadapter.freeplane.rm.IFreeplaneResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewFreeplaneMap
		extends
		FlexoAction<NewFreeplaneMap, RepositoryFolder<IFreeplaneResource>, RepositoryFolder<IFreeplaneResource>> {
	private static final class CreateFreeplaneMapActionType
			extends
			FlexoActionType<NewFreeplaneMap, RepositoryFolder<IFreeplaneResource>, RepositoryFolder<IFreeplaneResource>> {

		protected CreateFreeplaneMapActionType() {
			super("create_mind_map", FlexoActionType.newMenu, FlexoActionType.editGroup, FlexoActionType.ADD_ACTION_TYPE);
		}

		@Override
		public NewFreeplaneMap makeNewAction(final RepositoryFolder<IFreeplaneResource> folder,
				final Vector<RepositoryFolder<IFreeplaneResource>> maps, final FlexoEditor flexoEditor) {
			return new NewFreeplaneMap(folder, maps, flexoEditor);
		}

		@Override
		public boolean isVisibleForSelection(final RepositoryFolder<IFreeplaneResource> folder,
				final Vector<RepositoryFolder<IFreeplaneResource>> map) {
			return folder.getResourceRepository().getResourceClass().equals(IFreeplaneResource.class);
		}

		@Override
		public boolean isEnabledForSelection(final RepositoryFolder<IFreeplaneResource> folder,
				final Vector<RepositoryFolder<IFreeplaneResource>> map) {
			return true;
		}
	}

	public static final FlexoActionType<NewFreeplaneMap, RepositoryFolder<IFreeplaneResource>, RepositoryFolder<IFreeplaneResource>> ACTION_TYPE = new CreateFreeplaneMapActionType();

	private static final Logger LOGGER = Logger.getLogger(NewFreeplaneMap.class.getSimpleName());

	/**
	 * Property for fibs
	 */
	public String mapName;

	private NewFreeplaneMap(final RepositoryFolder<IFreeplaneResource> focusedObject,
			final Vector<RepositoryFolder<IFreeplaneResource>> globalSelection, final FlexoEditor editor) {
		super(ACTION_TYPE, focusedObject, globalSelection, editor);
	}

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, RepositoryFolder.class);
	}

	@Override
	protected void doAction(final Object objet) throws FlexoException {
		LOGGER.info("One day this action will create a new freeplane map with name " + this.mapName);
		// If no data have been load, initialization has not been done, so do it.
		FreeplaneBasicAdapter.getInstance();
		final MapModel newMap = new MapModel();
		final NodeModel root = new NodeModel(this.mapName, newMap);
		newMap.setRoot(root);

		Controller.getCurrentModeController().getMapController().fireMapCreated(newMap);
		Controller.getCurrentModeController().getMapController().newMapView(newMap);

		final String fileCreatedPath = this.getFocusedObject().getFile().getAbsolutePath() + System.getProperty("file.separator")
				+ this.mapName + ".mm";
		try {
			((MMapIO) Controller.getCurrentModeController().getExtension(MapIO.class)).writeToFile(newMap, new File(fileCreatedPath));
		} catch (final FileNotFoundException e) {
			final String msg = "";
			LOGGER.log(Level.SEVERE, msg, e);
		} catch (final IOException e) {
			final String msg = "";
			LOGGER.log(Level.SEVERE, msg, e);
		}
	}
}
