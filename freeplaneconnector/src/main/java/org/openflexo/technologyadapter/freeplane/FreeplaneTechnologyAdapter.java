/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Freeplane, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
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
import org.openflexo.foundation.fml.annotations.DeclareResourceTypes;
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
@DeclareResourceTypes({ IFreeplaneResource.class })
public class FreeplaneTechnologyAdapter extends TechnologyAdapter {

	public static final String FREEPLANE_FILE_EXTENSION = ".mm";

	private static final Logger LOGGER = Logger.getLogger(FreeplaneTechnologyAdapter.class.getPackage().getName());

	public FreeplaneTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return "Freeplane Technology Adapter";
	}

	@Override
	public String getLocalizationDirectory() {
		return "FlexoLocalization/FreePlane";
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
	public <I> void performInitializeResourceCenter(final FlexoResourceCenter<I> resourceCenter) {
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
		notifyRepositoryStructureChanged();
	}

	/**
	 * Refactoring for readability. Register file if it is a freeplane file, and reference resource to <code>this</code>
	 *
	 * @param resourceCenter
	 * @param candidateFile
	 */
	private <I> IFreeplaneResource initializeFreeplaneFile(final FlexoResourceCenter<I> resourceCenter, final File candidateFile) {
		if (!this.isValidFreeplaneFile(candidateFile)) {
			return null;
		}
		final FreeplaneResourceImpl freeplaneResourceFile = (FreeplaneResourceImpl) FreeplaneResourceImpl
				.makeFreeplaneResource(candidateFile, this.getTechnologyContextManager(), resourceCenter);
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
			return freeplaneResourceFile;
		}
		return null;
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
	public <I> boolean contentsAdded(final FlexoResourceCenter<I> resourceCenter, final I contents) {
		if (contents instanceof File) {
			return (initializeFreeplaneFile(resourceCenter, (File) contents) != null);
		}
		return false;
	}

	/**
	 * Nothing done. Maybe an unregister should be called.
	 *
	 * @param resourceCenter
	 * @param contents
	 */
	@Override
	public <I> boolean contentsDeleted(final FlexoResourceCenter<I> resourceCenter, final I contents) {
		// Noting particular to implement
		return false;
	}

	@Override
	public <I> boolean contentsModified(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <I> boolean contentsRenamed(FlexoResourceCenter<I> resourceCenter, I contents, String oldName, String newName) {
		// TODO Auto-generated method stub
		return false;
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
				file, this.getTechnologyContextManager(), project);
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

	public TechnologyAdapterResource<IFreeplaneMap, FreeplaneTechnologyAdapter> createNewFreeplaneMap(final FlexoResourceCenter<?> rc,
			final String filename) {
		if (rc instanceof FlexoProject){
			File freeplaneFile = new File(FlexoProject.getProjectSpecificModelsDirectory((FlexoProject) rc), filename);
			String modelUri = freeplaneFile.toURI().toString();
			IFreeplaneResource returned = FreeplaneResourceImpl.makeFreeplaneResource(modelUri, freeplaneFile, getTechnologyContextManager(),
					rc);

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
		else {
			LOGGER.warning("INVESTIGATE: NOT ABLE TO CREATE A NEW FREEPLANE RESOURCE => not a project: " + rc.toString());
			return null;
		}
	}

	@Override
	public String getIdentifier() {
		return "FRP";
	}

}
