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

import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.fml.annotations.DeclareResourceTypes;
import org.openflexo.foundation.fml.annotations.DeclareVirtualModelInstanceNatures;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResourceFactory;
import org.openflexo.technologyadapter.gina.rm.GINAResourceRepository;

/**
 * This class defines and implements the GINA technology adapter
 * 
 * @author Sylvain Guérin
 * 
 */

@DeclareModelSlots({ FIBComponentModelSlot.class })
@DeclareRepositoryType({ GINAResourceRepository.class })
@DeclareVirtualModelInstanceNatures({ FMLControlledFIBVirtualModelInstanceNature.class })
@DeclareResourceTypes({ GINAFIBComponentResourceFactory.class })
public class GINATechnologyAdapter extends TechnologyAdapter {

	private static final Logger LOGGER = Logger.getLogger(GINATechnologyAdapter.class.getPackage().getName());

	public GINATechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return new String("GINA Technology Adapter");
	}

	@Override
	public String getLocalizationDirectory() {
		return "FlexoLocalization/GINATechnologyAdapter";
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

	/*@Override
	public <I> void performInitializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {
		GINAResourceRepository currentRepository = resourceCenter.getRepository(GINAResourceRepository.class, this);
		if (currentRepository == null) {
			currentRepository = createNewGINAResourceRepository(resourceCenter);
		}
	
		for (final I item : resourceCenter) {
			if (item instanceof File) {
				this.initializeGINAFile(resourceCenter, (File) item);
			}
		}
	
		// Call it to update the current repositories
		notifyRepositoryStructureChanged();
	
	}*/

	/**
	 * Register file if it is a GIN file, and reference resource to <code>this</code>
	 * 
	 * @param resourceCenter
	 * @param candidateFile
	 */
	/*private <I> GINAFIBComponentResource initializeGINAFile(final FlexoResourceCenter<I> resourceCenter, final File candidateFile) {
		if (!isValidGINAComponent(candidateFile)) {
			return null;
		}
		final GINAFIBComponentResourceImpl ginaconnectorResourceFile = (GINAFIBComponentResourceImpl) GINAFIBComponentResourceImpl
				.retrieveComponentResource(candidateFile, this.getTechnologyContextManager());
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
			return ginaconnectorResourceFile;
		}
		return null;
	}*/

	/**
	 * 
	 * @param candidateFile
	 * @return true if extension of file match <code>GINA_FILE_EXTENSION</code>
	 */
	/*public boolean isValidGINAComponent(final File candidateFile) {
		return candidateFile.getName().endsWith(GINA_COMPONENT_EXTENSION) || candidateFile.getName().endsWith(GINA_INSPECTOR_EXTENSION);
	}*/

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub
		return false;
	}

	/*@Override
	public <I> boolean contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub
		if (contents instanceof File) {
			return (initializeGINAFile(resourceCenter, (File) contents) != null);
		}
		return false;
	}
	
	@Override
	public <I> boolean contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub
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
	}*/

	/**
	 * Create a new GINAResourceRepository and register it in the given resource center.
	 * 
	 * @param resourceCenter
	 * @return the repository
	 */
	/*private GINAResourceRepository createNewGINAResourceRepository(final FlexoResourceCenter<?> resourceCenter) {
		final GINAResourceRepository repo = new GINAResourceRepository(this, resourceCenter);
		resourceCenter.registerRepository(repo, GINAResourceRepository.class, this);
		return repo;
	}*/

	public <I> GINAResourceRepository<I> getGINAResourceRepository(FlexoResourceCenter<I> resourceCenter) {
		GINAResourceRepository<I> returned = resourceCenter.getRepository(GINAResourceRepository.class, this);
		if (returned == null) {
			returned = new GINAResourceRepository<I>(this, resourceCenter);
			resourceCenter.registerRepository(returned, GINAResourceRepository.class, this);
		}
		return returned;
	}

	@Override
	public String getIdentifier() {
		return "GINA";
	}

	public GINAFIBComponentResourceFactory getGINAFIBComponentResourceFactory() {
		return getResourceFactory(GINAFIBComponentResourceFactory.class);
	}

}
