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

package org.openflexo.technologyadapter.oslc;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.technologyadapter.DeclareModelSlot;
import org.openflexo.foundation.technologyadapter.DeclareModelSlots;
import org.openflexo.foundation.technologyadapter.DeclareRepositoryType;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.technologyadapter.oslc.rm.OSLCRepository;
import org.openflexo.technologyadapter.oslc.rm.OSLCResourceResource;
import org.openflexo.technologyadapter.oslc.rm.OSLCResourceResourceImpl;
import org.openflexo.technologyadapter.oslc.virtualmodel.bindings.OSLCBindingFactory;

/**
 * This class defines and implements the OSLC technology adapter
 * 
 * @author SomeOne
 * 
 */

@DeclareModelSlots({ // ModelSlot(s) declaration
@DeclareModelSlot(FML = "OSLCModelSlot", modelSlotClass = OSLCCoreModelSlot.class),
		@DeclareModelSlot(FML = "OSLCRMModelSlot", modelSlotClass = OSLCRMModelSlot.class) })
@DeclareRepositoryType({ OSLCRepository.class })
public class OSLCTechnologyAdapter extends TechnologyAdapter {
	private static String OSLC_FILE_EXTENSION = ".oslc";

	protected static final Logger logger = Logger.getLogger(OSLCTechnologyAdapter.class.getPackage().getName());

	private static final OSLCBindingFactory BINDING_FACTORY = new OSLCBindingFactory();

	public OSLCTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return new String("OSLC Technology Adapter");
	}

	@Override
	public TechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
		return new OSLCTechnologyContextManager(this, service);
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		return BINDING_FACTORY;
	}

	@Override
	public <I> void initializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {
		OSLCRepository oslcRepository = resourceCenter.getRepository(OSLCRepository.class, this);
		if (oslcRepository == null) {
			oslcRepository = createOSLCRepository(resourceCenter);
		}

		Iterator<I> it = resourceCenter.iterator();

		while (it.hasNext()) {
			I item = it.next();
			if (item instanceof File) {
				// System.out.println("searching " + item);
				File candidateFile = (File) item;
				tryToLookupOSLCResources(resourceCenter, candidateFile);
			}
		}
		// Call it to update the current repositories
		getPropertyChangeSupport().firePropertyChange("getAllRepositories()", null, resourceCenter);
	}

	protected OSLCResourceResource tryToLookupOSLCResources(FlexoResourceCenter<?> resourceCenter, File candidateFile) {
		if (isValidOSLCFile(candidateFile)) {
			OSLCResourceResource oslcRes = retrieveOSLCResource(candidateFile);
			OSLCRepository oslcRepository = resourceCenter.getRepository(OSLCRepository.class, this);
			if (oslcRes != null) {
				RepositoryFolder<OSLCResourceResource> folder;
				try {
					folder = oslcRepository.getRepositoryFolder(candidateFile, true);
					oslcRepository.registerResource(oslcRes, folder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				referenceResource(oslcRes, resourceCenter);
				return oslcRes;
			}
		}
		return null;
	}

	/**
	 * Instantiate new workbook resource stored in supplied model file<br>
	 * *
	 */
	public OSLCResourceResource retrieveOSLCResource(File cdlFile) {
		OSLCResourceResource oslcResource = null;

		// TODO: try to look-up already found file
		oslcResource = OSLCResourceResourceImpl.retrieveOSLCResource(cdlFile, getTechnologyContextManager());

		return oslcResource;
	}

	/**
	 * 
	 * Create a cdl unit repository for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 */
	public OSLCRepository createOSLCRepository(FlexoResourceCenter<?> resourceCenter) {
		OSLCRepository returned = new OSLCRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, OSLCRepository.class, this);
		return returned;
	}

	/**
	 * Return flag indicating if supplied file appears as a valid cdl
	 * 
	 * @param candidateFile
	 * 
	 * @return
	 */
	public boolean isValidOSLCFile(File candidateFile) {
		return candidateFile.getName().endsWith(OSLC_FILE_EXTENSION);
	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <I> void contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		if (contents instanceof File) {
			File candidateFile = (File) contents;
			tryToLookupOSLCResources(resourceCenter, candidateFile);
		}
		// Call it to update the current repositories
		getPropertyChangeSupport().firePropertyChange("getAllRepositories()", null, resourceCenter);
	}

	@Override
	public <I> void contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub

	}

	@Override
	public OSLCTechnologyContextManager getTechnologyContextManager() {
		// TODO Auto-generated method stub
		return (OSLCTechnologyContextManager) super.getTechnologyContextManager();
	}

}
