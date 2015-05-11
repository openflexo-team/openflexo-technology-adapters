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

package org.openflexo.technologyadapter.docx;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.rm.InJarResourceImpl;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResourceImpl;

/**
 * This class defines and implements the DOCX technology adapter, which allows to manage .docx documents (format used in MS/Office)<br>
 * This technology adapter internally uses docx4j library.
 * 
 * 
 * @author sylvain
 * 
 */

@DeclareModelSlots({ DocXModelSlot.class })
@DeclareRepositoryType({ DocXDocumentRepository.class })
public class DocXTechnologyAdapter extends TechnologyAdapter {

	private static String DOCX_FILE_EXTENSION = ".docx";

	protected static final Logger logger = Logger.getLogger(DocXTechnologyAdapter.class.getPackage().getName());

	public DocXTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return new String("DocX Technology Adapter");
	}

	@Override
	public DocXTechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
		return new DocXTechnologyContextManager(this, getTechnologyAdapterService().getServiceManager().getResourceCenterService());
	}

	@Override
	public DocXTechnologyContextManager getTechnologyContextManager() {
		return (DocXTechnologyContextManager) super.getTechnologyContextManager();
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <I> void initializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {

		DocXDocumentRepository docXRepository = resourceCenter.getRepository(DocXDocumentRepository.class, this);
		if (docXRepository == null) {
			docXRepository = createDocXDocumentRepository(resourceCenter);
		}

		Iterator<I> it = resourceCenter.iterator();

		while (it.hasNext()) {
			I item = it.next();
			// if (item instanceof File) {
			// System.out.println("searching " + item);
			// File candidateFile = (File) item;
			DocXDocumentResource wbRes = tryToLookupDocX(resourceCenter, item);
			// }
		}

		// Call it to update the current repositories
		getPropertyChangeSupport().firePropertyChange("getAllRepositories()", null, resourceCenter);
	}

	protected DocXDocumentResource tryToLookupDocX(FlexoResourceCenter<?> resourceCenter, Object candidateElement) {
		DocXTechnologyContextManager technologyContextManager = getTechnologyContextManager();
		if (isValidDocX(candidateElement)) {
			DocXDocumentResource wbRes = retrieveDocXResource(candidateElement);
			DocXDocumentRepository wbRepository = resourceCenter.getRepository(DocXDocumentRepository.class, this);
			if (wbRes != null) {
				RepositoryFolder<DocXDocumentResource> folder;
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
	}

	/**
	 * Instantiate new workbook resource stored in supplied model file<br>
	 * *
	 */
	public DocXDocumentResource retrieveDocXResource(Object docXDocumentItem) {

		DocXDocumentResource returned = null; // getTechnologyContextManager().getExcelWorkbookResource(workbook);
		if (returned == null) {
			if (docXDocumentItem instanceof File) {
				returned = DocXDocumentResourceImpl.retrieveDocXDocumentResource((File) docXDocumentItem, getTechnologyContextManager());
			}
			if (returned != null) {
				getTechnologyContextManager().registerDocXDocumentResource(returned);
			} else {
				logger.warning("Cannot retrieve DocXDocumentResource resource for " + docXDocumentItem);
			}
		}

		return returned;
	}

	public boolean isValidDocX(Object candidateElement) {
		if (candidateElement instanceof File && isValidDocXFile(((File) candidateElement))) {
			return true;
		} else if (candidateElement instanceof InJarResourceImpl && isValidDocXInJar((InJarResourceImpl) candidateElement)) {
			return true;
		}
		return false;
	}

	/**
	 * Return flag indicating if supplied file appears as a valid workbook
	 * 
	 * @param candidateFile
	 * 
	 * @return
	 */
	public boolean isValidDocXFile(File candidateFile) {
		return candidateFile.getName().endsWith(DOCX_FILE_EXTENSION);
	}

	public boolean isValidDocXInJar(InJarResourceImpl candidateInJar) {
		if (candidateInJar.getRelativePath().endsWith(DOCX_FILE_EXTENSION)) {
			return true;
		}
		return false;
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
			DocXDocumentResource docXRes = tryToLookupDocX(resourceCenter, candidateFile);
		}
	}

	@Override
	public <I> void contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * Create a {@link DocXDocumentRepository} for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 */
	public DocXDocumentRepository createDocXDocumentRepository(FlexoResourceCenter<?> resourceCenter) {
		DocXDocumentRepository returned = new DocXDocumentRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, DocXDocumentRepository.class, this);
		return returned;
	}

	public DocXDocumentResource createNewDocXDocument(FlexoProject project, String filename, String modelUri) {
		// TODO Auto-generated method stub
		return null;
	}

	public DocXDocumentResource createNewDocXDocument(FileSystemBasedResourceCenter resourceCenter, String relativePath, String filename,
			String modelUri) {
		// TODO Auto-generated method stub
		return null;
	}

}
