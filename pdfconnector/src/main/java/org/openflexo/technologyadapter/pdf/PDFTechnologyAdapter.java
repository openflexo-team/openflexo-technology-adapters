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

package org.openflexo.technologyadapter.pdf;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.rm.InJarResourceImpl;
import org.openflexo.technologyadapter.pdf.model.PDFDocument;
import org.openflexo.technologyadapter.pdf.rm.PDFDocumentRepository;
import org.openflexo.technologyadapter.pdf.rm.PDFDocumentResource;
import org.openflexo.technologyadapter.pdf.rm.PDFDocumentResourceImpl;

/**
 * This class defines and implements the DOCX technology adapter, which allows to manage .docx documents (format used in MS/Office)<br>
 * This technology adapter internally uses docx4j library.
 * 
 * 
 * @author sylvain
 * 
 */

@DeclareModelSlots({ PDFModelSlot.class })
// @DeclareRepositoryType({ PDFDocumentRepository.class })
// @DeclareVirtualModelInstanceNatures({ FMLControlledPDFVirtualModelInstanceNature.class })
public class PDFTechnologyAdapter extends TechnologyAdapter {

	public static String PDF_FILE_EXTENSION = ".pdf";

	protected static final Logger logger = Logger.getLogger(PDFTechnologyAdapter.class.getPackage().getName());

	public PDFTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return new String("PDF Technology Adapter");
	}

	@Override
	public PDFTechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
		return new PDFTechnologyContextManager(this, getTechnologyAdapterService().getServiceManager().getResourceCenterService());
	}

	@Override
	public PDFTechnologyContextManager getTechnologyContextManager() {
		return (PDFTechnologyContextManager) super.getTechnologyContextManager();
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <I> void performInitializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {

		PDFDocumentRepository pdfRepository = resourceCenter.getRepository(PDFDocumentRepository.class, this);
		if (pdfRepository == null) {
			pdfRepository = createPDFDocumentRepository(resourceCenter);
		}

		Iterator<I> it = resourceCenter.iterator();

		while (it.hasNext()) {
			I item = it.next();
			// if (item instanceof File) {
			// System.out.println("searching " + item);
			// }
			// File candidateFile = (File) item;
			PDFDocumentResource wbRes = tryToLookupPDF(resourceCenter, item);
			// }
		}

		// Call it to update the current repositories
		getPropertyChangeSupport().firePropertyChange("getAllRepositories()", null, resourceCenter);
	}

	protected PDFDocumentResource tryToLookupPDF(FlexoResourceCenter<?> resourceCenter, Object candidateElement) {
		PDFTechnologyContextManager technologyContextManager = getTechnologyContextManager();
		if (isValidPDF(candidateElement)) {
			PDFDocumentResource pdfDocumentResource = retrievePDFResource(candidateElement, resourceCenter);
			referenceResource(pdfDocumentResource, resourceCenter);
			/*PDFDocumentRepository pdfDocumentRepository = resourceCenter.getRepository(PDFDocumentRepository.class, this);
			if (pdfDocumentResource != null) {
				RepositoryFolder<PDFDocumentResource> folder;
				try {
					folder = pdfDocumentRepository.getRepositoryFolder(candidateElement, true);
					pdfDocumentRepository.registerResource(pdfDocumentResource, folder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				referenceResource(pdfDocumentResource, resourceCenter);
				return pdfDocumentResource;
			}*/
		}
		return null;
	}

	@Override
	public void referenceResource(FlexoResource<?> resource, FlexoResourceCenter<?> resourceCenter) {
		super.referenceResource(resource, resourceCenter);
		if (resource instanceof PDFDocumentResource) {
			registerInPDFDocumentRepository((PDFDocumentResource) resource, resourceCenter);
		}
	}

	private void registerInPDFDocumentRepository(PDFDocumentResource pdfDocumentResource, FlexoResourceCenter<?> resourceCenter) {
		if (pdfDocumentResource == null) {
			return;
		}
		PDFDocumentRepository pdfDocumentRepository = resourceCenter.getRepository(PDFDocumentRepository.class, this);
		if (pdfDocumentResource.getFlexoIODelegate() instanceof FileFlexoIODelegate) {
			RepositoryFolder<PDFDocumentResource> folder;
			try {
				folder = pdfDocumentRepository
						.getRepositoryFolder(((FileFlexoIODelegate) pdfDocumentResource.getFlexoIODelegate()).getFile(), true);
				pdfDocumentRepository.registerResource(pdfDocumentResource, folder);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Instantiate new workbook resource stored in supplied model file<br>
	 * *
	 */
	public PDFDocumentResource retrievePDFResource(Object pdfDocumentItem, FlexoResourceCenter<?> resourceCenter) {

		PDFDocumentResource returned = null; // getTechnologyContextManager().getExcelWorkbookResource(workbook);
		if (returned == null) {
			if (pdfDocumentItem instanceof File) {
				returned = PDFDocumentResourceImpl.retrievePDFDocumentResource((File) pdfDocumentItem, getTechnologyContextManager(),
						resourceCenter);
			}
			if (returned != null) {
				getTechnologyContextManager().registerPDFDocumentResource(returned);
			}
			else {
				logger.warning("Cannot retrieve PDFDocumentResource resource for " + pdfDocumentItem);
			}
		}

		return returned;
	}

	public boolean isValidPDF(Object candidateElement) {
		if (candidateElement instanceof File && isValidPDFFile(((File) candidateElement))) {
			return true;
		}
		else if (candidateElement instanceof InJarResourceImpl && isValidPDFInJar((InJarResourceImpl) candidateElement)) {
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
	public boolean isValidPDFFile(File candidateFile) {
		return candidateFile.getName().endsWith(PDF_FILE_EXTENSION);
	}

	public boolean isValidPDFInJar(InJarResourceImpl candidateInJar) {
		if (candidateInJar.getRelativePath().endsWith(PDF_FILE_EXTENSION)) {
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
	public <I> boolean contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		if (contents instanceof File) {
			File candidateFile = (File) contents;
			PDFDocumentResource pdfRes = tryToLookupPDF(resourceCenter, candidateFile);
			return pdfRes != null;
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
	}

	/**
	 * 
	 * Create a {@link PDFDocumentRepository} for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 */
	public PDFDocumentRepository createPDFDocumentRepository(FlexoResourceCenter<?> resourceCenter) {
		PDFDocumentRepository returned = new PDFDocumentRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, PDFDocumentRepository.class, this);
		return returned;
	}

	/**
	 * Create a new {@link PDFDocumentResource} using supplied configuration options<br>
	 * 
	 * @param project
	 * @param filename
	 * @param modelUri
	 * @param createEmptyDocument
	 *            a flag indicating if created resource should encodes an empty (but existing) document or if resource data should remain
	 *            empty
	 * @return
	 */
	public PDFDocumentResource createNewPDFDocumentResource(FlexoProject project, String filename, boolean createEmptyDocument) {

		return createNewPDFDocumentResource(project, File.separator + "PDF", filename, createEmptyDocument);

	}

	/**
	 * Create a new {@link PDFDocumentResource} using supplied configuration options<br>
	 * 
	 * @param resourceCenter
	 * @param relativePath
	 * @param filename
	 * @param createEmptyDocument
	 *            a flag indicating if created resource should encodes an empty (but existing) document or if resource data should remain
	 *            empty
	 * @return
	 */
	public PDFDocumentResource createNewPDFDocumentResource(FileSystemBasedResourceCenter resourceCenter, String relativePath,
			String filename, boolean createEmptyDocument) {

		if (!relativePath.startsWith(File.separator)) {
			relativePath = File.separator + relativePath;
		}

		File pdfFile = new File(resourceCenter.getDirectory() + relativePath, filename);

		PDFDocumentResource pdfDocumentResource = PDFDocumentResourceImpl.makePDFDocumentResource(pdfFile, getTechnologyContextManager(),
				resourceCenter);

		referenceResource(pdfDocumentResource, resourceCenter);

		if (createEmptyDocument) {
			PDFDocument document = pdfDocumentResource.getFactory().makeNewPDFDocument();
			document.setResource(pdfDocumentResource);
			pdfDocumentResource.setResourceData(document);
			pdfDocumentResource.setModified(true);
		}

		return pdfDocumentResource;
	}

	@Override
	public void initFMLModelFactory(FMLModelFactory fMLModelFactory) {
		super.initFMLModelFactory(fMLModelFactory);

		// fMLModelFactory.addConverter(new PDFFragmentConverter());
		// fMLModelFactory.addConverter(new PDFElementConverter());
	}

	@Override
	public String getIdentifier() {
		return "PDF";
	}

}
