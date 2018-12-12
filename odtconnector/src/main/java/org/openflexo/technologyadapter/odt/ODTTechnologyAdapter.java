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

package org.openflexo.technologyadapter.odt;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareResourceFactory;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.technologyadapter.odt.rm.ODTDocumentRepository;
import org.openflexo.technologyadapter.odt.rm.ODTDocumentResourceFactory;

/**
 * This class defines and implements the ODT technology adapter, which allows to manage .odt documents (OpenDocument format, which is the
 * default format for OpenOffice)<br>
 * 
 * This technology adapter internally use JOpenDocument library.
 * 
 * @author sylvain
 * 
 */

@DeclareModelSlots({ ODTModelSlot.class })
@DeclareResourceFactory({ ODTDocumentResourceFactory.class })
public class ODTTechnologyAdapter extends TechnologyAdapter<ODTTechnologyAdapter> {

	protected static final Logger logger = Logger.getLogger(ODTTechnologyAdapter.class.getPackage().getName());

	public ODTTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return new String("ODT Technology Adapter");
	}

	@Override
	protected String getLocalizationDirectory() {
		return "FlexoLocalization/ODTTechnologyAdapter";
	}

	@Override
	public ODTTechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
		return new ODTTechnologyContextManager(this, getTechnologyAdapterService().getServiceManager().getResourceCenterService());
	}

	@Override
	public ODTTechnologyContextManager getTechnologyContextManager() {
		return (ODTTechnologyContextManager) super.getTechnologyContextManager();
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		return null;
	}

	/*@Override
	public <I> void performInitializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {
	
		ODTDocumentRepository odtRepository = resourceCenter.getRepository(ODTDocumentRepository.class, this);
		if (odtRepository == null) {
			odtRepository = createODTDocumentRepository(resourceCenter);
		}
	
		Iterator<I> it = resourceCenter.iterator();
	
		while (it.hasNext()) {
			I item = it.next();
			// if (item instanceof File) {
			// System.out.println("searching " + item);
			// File candidateFile = (File) item;
			ODTDocumentResource wbRes = tryToLookupODT(resourceCenter, item);
			// }
		}
	
		// Call it to update the current repositories
		notifyRepositoryStructureChanged();
	}*/

	/*protected <I> ODTDocumentResource tryToLookupODT(FlexoResourceCenter<I> resourceCenter, I candidateElement) {
		ODTTechnologyContextManager technologyContextManager = getTechnologyContextManager();
		if (isValidODT(candidateElement)) {
			ODTDocumentResource wbRes = retrieveODTResource(candidateElement, resourceCenter);
			ODTDocumentRepository wbRepository = resourceCenter.getRepository(ODTDocumentRepository.class, this);
			if (wbRes != null) {
				RepositoryFolder<ODTDocumentResource> folder;
				try {
					folder = wbRepository.getRepositoryFolder(candidateElement, true);
					wbRepository.registerResource(wbRes, folder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				referenceResource(wbRes, resourceCenter);
				return wbRes;
			}
		}
		return null;
	}*/

	/**
	 * Instantiate new workbook resource stored in supplied model file<br>
	 * *
	 */
	/*public ODTDocumentResource retrieveODTResource(Object odtDocumentItem, FlexoResourceCenter<?> resourceCenter) {
	
		ODTDocumentResource returned = null; // getTechnologyContextManager().getExcelWorkbookResource(workbook);
		if (returned == null) {
			if (odtDocumentItem instanceof File) {
				returned = ODTDocumentResourceImpl.retrieveODTDocumentResource((File) odtDocumentItem, getTechnologyContextManager(),
						resourceCenter);
			}
			if (returned != null) {
				getTechnologyContextManager().registerODTDocumentResource(returned);
			}
			else {
				logger.warning("Cannot retrieve ODTDocumentResource resource for " + odtDocumentItem);
			}
		}
	
		return returned;
	}*/

	/*public boolean isValidODT(Object candidateElement) {
		if (candidateElement instanceof File && isValidODTFile(((File) candidateElement))) {
			return true;
		}
		else if (candidateElement instanceof InJarResourceImpl && isValidODTInJar((InJarResourceImpl) candidateElement)) {
			return true;
		}
		return false;
	}*/

	/**
	 * Return flag indicating if supplied file appears as a valid workbook
	 * 
	 * @param candidateFile
	 * 
	 * @return
	 */
	/*public boolean isValidODTFile(File candidateFile) {
		return candidateFile.getName().endsWith(ODT_FILE_EXTENSION);
	}*/

	/*public boolean isValidODTInJar(InJarResourceImpl candidateInJar) {
		if (candidateInJar.getRelativePath().endsWith(ODT_FILE_EXTENSION)) {
			return true;
		}
		return false;
	}*/

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	/*@Override
	public <I> boolean contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}
	
	@Override
	public <I> boolean contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}
	
	@Override
	public <I> boolean contentsModified(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}
	
	@Override
	public <I> boolean contentsRenamed(FlexoResourceCenter<I> resourceCenter, I contents, String oldName, String newName) {
		return false;
	}*/

	/**
	 * 
	 * Create a {@link ODTDocumentRepository} for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 */
	/*public ODTDocumentRepository createODTDocumentRepository(FlexoResourceCenter<?> resourceCenter) {
		ODTDocumentRepository returned = new ODTDocumentRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, ODTDocumentRepository.class, this);
		return returned;
	}*/

	@SuppressWarnings("unchecked")
	public <I> ODTDocumentRepository<I> getODTDocumentRepository(FlexoResourceCenter<I> resourceCenter) {
		ODTDocumentRepository<I> returned = resourceCenter.retrieveRepository(ODTDocumentRepository.class, this);
		if (returned == null) {
			returned = ODTDocumentRepository.instanciateNewRepository(this, resourceCenter);
			resourceCenter.registerRepository(returned, ODTDocumentRepository.class, this);
		}
		return returned;
	}

	@Override
	public String getIdentifier() {
		return "ODT";
	}

}
