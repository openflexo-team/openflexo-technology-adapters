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

package org.openflexo.technologyadapter.gina;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResource;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResourceImpl;
import org.openflexo.technologyadapter.gina.rm.GINAResourceRepository;

/**
 * This class defines and implements the GINA technology adapter
 * 
 * @author Sylvain Guérin
 * 
 */

@DeclareModelSlots({ GINAModelSlot.class })
@DeclareRepositoryType({ GINAResourceRepository.class })
public class GINATechnologyAdapter extends TechnologyAdapter {

	private static String GINA_FILE_EXTENSION = ".fib";

	private static final Logger LOGGER = Logger.getLogger(GINATechnologyAdapter.class.getPackage().getName());

	public GINATechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return new String("GINA Technology Adapter");
	}

	@Override
	public GINATechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
		return new GINATechnologyContextManager(this, service);
	}

	@Override
	public GINATechnologyContextManager getTechnologyContextManager() {
		// TODO Auto-generated method stub
		return (GINATechnologyContextManager) super.getTechnologyContextManager();
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <I> void initializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {
		// TODO Auto-generated method stub
		GINAResourceRepository currentRepository = resourceCenter.getRepository(GINAResourceRepository.class, this);
		if (currentRepository == null) {
			currentRepository = this.createNewGINRepository(resourceCenter);
		}

		for (final I item : resourceCenter) {
			if (item instanceof File) {
				this.initializeGINAFile(resourceCenter, (File) item);
			}
		}

	}

	/**
	 * Register file if it is a GIN file, and reference resource to <code>this</code>
	 * 
	 * @param resourceCenter
	 * @param candidateFile
	 */
	private <I> void initializeGINAFile(final FlexoResourceCenter<I> resourceCenter, final File candidateFile) {
		if (!this.isValidGINFile(candidateFile)) {
			return;
		}
		final GINAFIBComponentResourceImpl ginaconnectorResourceFile = (GINAFIBComponentResourceImpl) GINAFIBComponentResourceImpl.retrieveGINResource(candidateFile,
				this.getTechnologyContextManager());
		final GINAResourceRepository resourceRepository = resourceCenter.getRepository(GINAResourceRepository.class, this);
		if (ginaconnectorResourceFile != null) {
			try {
				final RepositoryFolder<GINAFIBComponentResource> folder = resourceRepository.getRepositoryFolder(candidateFile, true);
				resourceRepository.registerResource(ginaconnectorResourceFile, folder);
				this.referenceResource(ginaconnectorResourceFile, resourceCenter);
			} catch (final IOException e) {
				final String msg = "Error during getting GIN resource folder";
				LOGGER.log(Level.SEVERE, msg, e);
			}
		}

	}

	/**
	 * 
	 * @param candidateFile
	 * @return true if extension of file match <code>GINA_FILE_EXTENSION</code>
	 */
	public boolean isValidGINFile(final File candidateFile) {
		return candidateFile.getName().endsWith(GINA_FILE_EXTENSION);
	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <I> void contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub
		if (contents instanceof File) {
			this.initializeGINAFile(resourceCenter, (File) contents);
		}
	}

	@Override
	public <I> void contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub
	}

	public GINAFIBComponentResource createNewGINModel(FlexoProject project, String filename, String modelUri) {
		// TODO Auto-generated method stub
		final File file = new File(FlexoProject.getProjectSpecificModelsDirectory(project), filename);
		final GINAFIBComponentResourceImpl ginaconnectorResourceFile = (GINAFIBComponentResourceImpl) GINAFIBComponentResourceImpl.makeGINResource(modelUri, file,
				this.getTechnologyContextManager());
		this.getTechnologyContextManager().registerResource(ginaconnectorResourceFile);
		return ginaconnectorResourceFile;
	}

	/**
	 * Create a new GINAResourceRepository and register it in the given resource center.
	 * 
	 * @param resourceCenter
	 * @return the repository
	 */
	private GINAResourceRepository createNewGINRepository(final FlexoResourceCenter<?> resourceCenter) {
		final GINAResourceRepository repo = new GINAResourceRepository(this, resourceCenter);
		resourceCenter.registerRepository(repo, GINAResourceRepository.class, this);
		return repo;
	}

	@Override
	public String getIdentifier() {
		return "GINA";
	}

}
