/*
 * (c) Copyright 2013- Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.technologyadapter.freeplane;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mapio.MapIO;
import org.freeplane.features.mapio.mindmapmode.MMapIO;
import org.freeplane.features.mode.Controller;
import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.rm.FreeplaneResourceImpl;
import org.openflexo.technologyadapter.freeplane.rm.FreeplaneResourceRepository;
import org.openflexo.technologyadapter.freeplane.rm.IFreeplaneResource;

/**
 * This class defines and implements the Freeplane technology adapter
 *
 * @author SomeOne
 */

@DeclareModelSlots({ FreeplaneModelSlot.class })
@DeclareRepositoryType({ FreeplaneResourceRepository.class })
public class FreeplaneTechnologyAdapter extends TechnologyAdapter {

	public static final String FREEPLANE_FILE_EXTENSION = ".mm";

	private static final Logger LOGGER = Logger.getLogger(FreeplaneTechnologyAdapter.class.getPackage().getName());

	public FreeplaneTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return "Freeplane Technology Adapter";
	}

	/**
	 * @return FreeplaneTechnologyContextManager
	 */
	@Override
	public FreeplaneTechnologyContextManager createTechnologyContextManager(final FlexoResourceCenterService service) {
		return new FreeplaneTechnologyContextManager(this, service);
	}

	@Override
	public FreeplaneTechnologyContextManager getTechnologyContextManager() {
		return (FreeplaneTechnologyContextManager) super.getTechnologyContextManager();
	}

	/**
	 * TODO : know what object to create or return here and do it.
	 *
	 * @return null
	 */
	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		return null;
	}

	@Override
	public <I> void initializeResourceCenter(final FlexoResourceCenter<I> resourceCenter) {
		// TODO Auto-generated method stub
		FreeplaneResourceRepository currentRepository = resourceCenter.getRepository(FreeplaneResourceRepository.class, this);
		if (currentRepository == null) {
			this.createNewFreeplaneRepository(resourceCenter);
		}

		for (final I item : resourceCenter) {
			if (item instanceof File) {
				this.initializeFreeplaneFile(resourceCenter, (File) item);
			}
		}
		// Call it to update the current repositories
		getPropertyChangeSupport().firePropertyChange("getAllRepositories()", null, resourceCenter);
	}

	/**
	 * Refactoring for readability. Register file if it is a freeplane file, and reference resource to <code>this</code>
	 *
	 * @param resourceCenter
	 * @param candidateFile
	 */
	private <I> void initializeFreeplaneFile(final FlexoResourceCenter<I> resourceCenter, final File candidateFile) {
		if (!this.isValidFreeplaneFile(candidateFile)) {
			return;
		}
		final FreeplaneResourceImpl freeplaneResourceFile = (FreeplaneResourceImpl) FreeplaneResourceImpl.makeFreeplaneResource(
				candidateFile, this.getTechnologyContextManager());
		final FreeplaneResourceRepository resourceRepository = resourceCenter.getRepository(FreeplaneResourceRepository.class, this);
		if (freeplaneResourceFile != null) {
			try {
				final RepositoryFolder<IFreeplaneResource> folder = resourceRepository.getRepositoryFolder(candidateFile, true);
				resourceRepository.registerResource(freeplaneResourceFile, folder);
				this.referenceResource(freeplaneResourceFile, resourceCenter);
			} catch (final IOException e) {
				final String msg = "Error during getting Freeplane resource folder";
				LOGGER.log(Level.SEVERE, msg, e);
			}
		}
	}

	/**
	 * @param candidateFile
	 * @return true if extension of file match <code>FREEPLANE_FILE_EXTENSION</code>
	 */
	private boolean isValidFreeplaneFile(final File candidateFile) {
		return candidateFile.getName().endsWith(FREEPLANE_FILE_EXTENSION);
	}

	/**
	 * Don't know what it is used for, let default value
	 *
	 * @param resourceCenter
	 * @param contents
	 * @return false
	 */
	@Override
	public <I> boolean isIgnorable(final FlexoResourceCenter<I> resourceCenter, final I contents) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openflexo.foundation.technologyadapter.TechnologyAdapter#contentsAdded(org.openflexo.foundation.resource.FlexoResourceCenter, java.lang.Object)
	 */
	@Override
	public <I> void contentsAdded(final FlexoResourceCenter<I> resourceCenter, final I contents) {
		if (contents instanceof File) {
			this.initializeFreeplaneFile(resourceCenter, (File) contents);
		}
	}

	/**
	 * Nothing done. Maybe an unregister should be called.
	 *
	 * @param resourceCenter
	 * @param contents
	 */
	@Override
	public <I> void contentsDeleted(final FlexoResourceCenter<I> resourceCenter, final I contents) {
		// Noting particular to implement
	}

	/**
	 * Create a FreeplaneResource from a .mm file, given a FlexoProject.
	 *
	 * @param project
	 * @param filename
	 * @param modelUri
	 * @return FreeplaneResourceImpl
	 */
	public IFreeplaneResource createNewFreeplaneModel(final FlexoProject project, final String filename, final String modelUri) {
		final File file = new File(FlexoProject.getProjectSpecificModelsDirectory(project), filename);
		final FreeplaneResourceImpl freeplaneResourceFile = (FreeplaneResourceImpl) FreeplaneResourceImpl.makeFreeplaneResource(modelUri,
				file, this.getTechnologyContextManager());
		this.getTechnologyContextManager().registerResource(freeplaneResourceFile);
		return freeplaneResourceFile;
	}

	/**
	 * Create a new FreeplaneResourceRepository and register it in the given resource center.
	 *
	 * @param resourceCenter
	 * @return the repository
	 */
	private FreeplaneResourceRepository createNewFreeplaneRepository(final FlexoResourceCenter<?> resourceCenter) {
		final FreeplaneResourceRepository repo = new FreeplaneResourceRepository(this, resourceCenter);
		resourceCenter.registerRepository(repo, FreeplaneResourceRepository.class, this);
		return repo;
	}

	public TechnologyAdapterResource<IFreeplaneMap, FreeplaneTechnologyAdapter> createNewFreeplaneMap(final FlexoProject project,
			final String filename) {

		File freeplaneFile = new File(FlexoProject.getProjectSpecificModelsDirectory(project), filename);
		String modelUri = freeplaneFile.toURI().toString();
		IFreeplaneResource returned = FreeplaneResourceImpl.makeFreeplaneResource(modelUri, freeplaneFile, getTechnologyContextManager());

		// Maybe noi initialized yet
		FreeplaneBasicAdapter.getInstance();
		final MapModel newMap = new MapModel();
		final NodeModel root = new NodeModel(filename, newMap);
		newMap.setRoot(root);

		Controller.getCurrentModeController().getMapController().fireMapCreated(newMap);
		Controller.getCurrentModeController().getMapController().newMapView(newMap);
		try {
			((MMapIO) Controller.getCurrentModeController().getExtension(MapIO.class)).writeToFile(newMap, freeplaneFile);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Exception raised during empty map creation", e);
		}
		return returned;
	}
}
