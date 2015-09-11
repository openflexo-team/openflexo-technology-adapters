/**
 * 
 * Copyright (c) 2015, Openflexo
 * 
 * This file is part of Oslcconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.oslc;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
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

@DeclareModelSlots({ OSLCCoreModelSlot.class, OSLCRMModelSlot.class })
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
				tryToLookupOSLCResources((FlexoResourceCenter<File>) resourceCenter, candidateFile);
			}
		}
		// Call it to update the current repositories
		getPropertyChangeSupport().firePropertyChange("getAllRepositories()", null, resourceCenter);
	}

	protected OSLCResourceResource tryToLookupOSLCResources(FlexoResourceCenter<File> resourceCenter, File candidateFile) {
		if (isValidOSLCFile(candidateFile)) {
			OSLCResourceResource oslcRes = retrieveOSLCResource(candidateFile, resourceCenter);
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
	public <I> OSLCResourceResource retrieveOSLCResource(I cdlFile, FlexoResourceCenter<I> resourceCenter) {
		OSLCResourceResource oslcResource = null;

		// TODO: try to look-up already found file
		if (cdlFile instanceof File) {
			oslcResource = OSLCResourceResourceImpl.retrieveOSLCResource((File) cdlFile, getTechnologyContextManager(), resourceCenter);

			return oslcResource;
		}
		return null;
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
			tryToLookupOSLCResources((FlexoResourceCenter<File>) resourceCenter, candidateFile);
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

	@Override
	public String getIdentifier() {
		return "OSLC";
	}

}
