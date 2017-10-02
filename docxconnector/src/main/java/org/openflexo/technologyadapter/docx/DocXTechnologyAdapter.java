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
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.fml.annotations.DeclareResourceTypes;
import org.openflexo.foundation.fml.annotations.DeclareVirtualModelInstanceNatures;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.docx.model.DocXElementConverter;
import org.openflexo.technologyadapter.docx.model.DocXFragmentConverter;
import org.openflexo.technologyadapter.docx.model.IdentifierManagementStrategy;
import org.openflexo.technologyadapter.docx.nature.FMLControlledDocXVirtualModelInstanceNature;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResourceFactory;

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
@DeclareResourceTypes({ DocXDocumentResourceFactory.class })
@DeclareVirtualModelInstanceNatures({ FMLControlledDocXVirtualModelInstanceNature.class })
public class DocXTechnologyAdapter extends TechnologyAdapter {

	// Sets default idStrategy when no modelSlot is used
	private IdentifierManagementStrategy defaultIdStrategy = IdentifierManagementStrategy.Bookmark;

	protected static final Logger logger = Logger.getLogger(DocXTechnologyAdapter.class.getPackage().getName());

	public DocXTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	public IdentifierManagementStrategy getDefaultIDStrategy() {
		return defaultIdStrategy;
	}

	public void setDefaultIDStrategy(IdentifierManagementStrategy idStrategy) {
		this.defaultIdStrategy = idStrategy;
	}

	@Override
	public String getName() {
		return new String("DocX Technology Adapter");
	}

	@Override
	public String getLocalizationDirectory() {
		return "FlexoLocalization/DocXTechnologyAdapter";
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

	/*protected DocXDocumentResource tryToLookupDocX(FlexoResourceCenter<?> resourceCenter, Object candidateElement) {
		DocXTechnologyContextManager technologyContextManager = getTechnologyContextManager();
		if (isValidDocX(candidateElement)) {
			DocXDocumentResource docXDocumentResource = retrieveDocXResource(candidateElement, resourceCenter, defaultIdStrategy);
			referenceResource(docXDocumentResource, resourceCenter);
			// DocXDocumentRepository docXDocumentRepository = resourceCenter.getRepository(DocXDocumentRepository.class, this);
			// if (docXDocumentResource != null) {
			// RepositoryFolder<DocXDocumentResource> folder;
			// try {
			// folder = docXDocumentRepository.getRepositoryFolder(candidateElement, true);
			// docXDocumentRepository.registerResource(docXDocumentResource, folder);
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// }
			// referenceResource(docXDocumentResource, resourceCenter);
			// return docXDocumentResource;
			// }
		}
		return null;
	}*/

	/*@Override
	public void referenceResource(FlexoResource<?> resource, FlexoResourceCenter<?> resourceCenter) {
		super.referenceResource(resource, resourceCenter);
		if (resource instanceof DocXDocumentResource) {
			registerInDocXDocumentRepository((DocXDocumentResource) resource, resourceCenter);
		}
	}*/

	/*private void registerInDocXDocumentRepository(DocXDocumentResource docXDocumentResource, FlexoResourceCenter<?> resourceCenter) {
		if (docXDocumentResource == null) {
			return;
		}
		DocXDocumentRepository docXDocumentRepository = resourceCenter.getRepository(DocXDocumentRepository.class, this);
		if (docXDocumentResource.getFlexoIODelegate() instanceof FileFlexoIODelegate) {
			RepositoryFolder<DocXDocumentResource> folder;
			try {
				folder = docXDocumentRepository
						.getRepositoryFolder(((FileFlexoIODelegate) docXDocumentResource.getFlexoIODelegate()).getFile(), true);
				docXDocumentRepository.registerResource(docXDocumentResource, folder);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}*/

	/**
	 * Instantiate new workbook resource stored in supplied model file<br>
	 *
	 */
	/*@Deprecated
	public DocXDocumentResource retrieveDocXResource(Object docXDocumentItem, FlexoResourceCenter<?> resourceCenter,
			IdentifierManagementStrategy idStrategy) {
	
		DocXDocumentResource returned = null;
		if (docXDocumentItem instanceof File) {
			returned = DocXDocumentResourceImpl.retrieveDocXDocumentResource((File) docXDocumentItem, getTechnologyContextManager(),
					resourceCenter, idStrategy);
		}
		else if (docXDocumentItem instanceof InJarResourceImpl) {
			returned = DocXDocumentResourceImpl.retrieveDocXDocumentResource((InJarResourceImpl) docXDocumentItem,
					getTechnologyContextManager(), resourceCenter, idStrategy);
		}
		if (returned != null) {
			getTechnologyContextManager().registerDocXDocumentResource(returned);
		}
		else {
			logger.warning("Cannot retrieve DocXDocumentResource resource for " + docXDocumentItem);
		}
	
		return returned;
	}*/

	/*public boolean isValidDocX(Object candidateElement) {
		if (candidateElement instanceof File && isValidDocXFile(((File) candidateElement))) {
			return true;
		}
		else if (candidateElement instanceof InJarResourceImpl && isValidDocXInJar((InJarResourceImpl) candidateElement)) {
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
	/*public boolean isValidDocXFile(File candidateFile) {
		return candidateFile.getName().endsWith(DOCX_FILE_EXTENSION);
	}*/

	/*public boolean isValidDocXInJar(InJarResourceImpl candidateInJar) {
		if (candidateInJar.getRelativePath().endsWith(DOCX_FILE_EXTENSION)) {
			return true;
		}
		return false;
	}*/

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	/*@Override
	public <I> boolean contentsAdded(FlexoResourceCenter<I> resourceCenter, I serializationArtefact) {
		boolean hasBeenLookedUp = false;
		if (!isIgnorable(resourceCenter, serializationArtefact)) {
			for (FlexoResourceFactory<?, ?, ?> resourceFactory : getResourceFactories()) {
				FlexoResource<?> resource = tryToLookupResource(resourceFactory, resourceCenter, serializationArtefact);
				if (resource != null) {
					hasBeenLookedUp = true;
				}
			}
		}
		return hasBeenLookedUp;
	}*/

	/*@Override
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

	public <I> DocXDocumentRepository<I> getDocXDocumentRepository(FlexoResourceCenter<I> resourceCenter) {
		DocXDocumentRepository<I> returned = resourceCenter.retrieveRepository(DocXDocumentRepository.class, this);
		if (returned == null) {
			returned = new DocXDocumentRepository<>(this, resourceCenter);
			resourceCenter.registerRepository(returned, DocXDocumentRepository.class, this);
		}
		return returned;
	}

	/**
	 * 
	 * Create a {@link DocXDocumentRepository} for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 */
	/*public DocXDocumentRepository createDocXDocumentRepository(FlexoResourceCenter<?> resourceCenter) {
		DocXDocumentRepository returned = new DocXDocumentRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, DocXDocumentRepository.class, this);
		return returned;
	}*/

	/**
	 * Create a new {@link DocXDocumentResource} using supplied configuration options<br>
	 * 
	 * @param project
	 * @param filename
	 * @param modelUri
	 * @param createEmptyDocument
	 *            a flag indicating if created resource should encodes an empty (but existing) document or if resource data should remain
	 *            empty
	 * @return
	 */
	/*@Deprecated
	public DocXDocumentResource createNewDocXDocumentResource(FlexoResourceCenter<?> rc, String filename, boolean createEmptyDocument,
			IdentifierManagementStrategy idStrategy) {
	
		if (rc instanceof FileSystemBasedResourceCenter) {
			return createNewDocXDocumentResource((FileSystemBasedResourceCenter) rc, File.separator + "DocX", filename, createEmptyDocument,
					idStrategy);
		}
		else {
			logger.warning(
					"INVESTIGATE: not implemented yet, not able to create a DocX file in a Rc that is not fileBased: " + rc.toString());
			return null;
		}
	
	}*/

	/**
	 * Create a new {@link DocXDocumentResource} using supplied configuration options<br>
	 * 
	 * @param resourceCenter
	 * @param relativePath
	 * @param filename
	 * @param createEmptyDocument
	 *            a flag indicating if created resource should encodes an empty (but existing) document or if resource data should remain
	 *            empty
	 * @return
	 */
	@Deprecated
	public DocXDocumentResource createNewDocXDocumentResource(FileSystemBasedResourceCenter resourceCenter, String relativePath,
			String filename, boolean createEmptyDocument, IdentifierManagementStrategy idStrategy) {

		if (!relativePath.startsWith(File.separator)) {
			relativePath = File.separator + relativePath;
		}

		File docXFile = new File(resourceCenter.getDirectory() + relativePath, filename);

		DocXDocumentResource docXDocumentResource = null;
		try {
			docXDocumentResource = getDocXDocumentResourceFactory().makeResource(docXFile, resourceCenter, getTechnologyContextManager(),
					true);
		} catch (SaveResourceException e) {
			e.printStackTrace();
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}

		// DocXDocumentResource docXDocumentResource = DocXDocumentResourceImpl.makeDocXDocumentResource(docXFile,
		// getTechnologyContextManager(), resourceCenter, idStrategy);

		// referenceResource(docXDocumentResource, resourceCenter);

		/*if (createEmptyDocument) {
			DocXDocument document = docXDocumentResource.getFactory().makeNewDocXDocument();
			document.setResource(docXDocumentResource);
			docXDocumentResource.setResourceData(document);
			docXDocumentResource.setModified(true);
		}*/

		return docXDocumentResource;
	}

	@Override
	public void initFMLModelFactory(FMLModelFactory fMLModelFactory) {
		super.initFMLModelFactory(fMLModelFactory);

		fMLModelFactory.addConverter(new DocXFragmentConverter());
		fMLModelFactory.addConverter(new DocXElementConverter());
	}

	@Override
	public String getIdentifier() {
		return "DOCX";
	}

	public DocXDocumentResourceFactory getDocXDocumentResourceFactory() {
		return getResourceFactory(DocXDocumentResourceFactory.class);
	}

	/*@Override
	protected <I> void foundFolder(FlexoResourceCenter<I> resourceCenter, I folder) throws IOException {
		super.foundFolder(resourceCenter, folder);
		if (resourceCenter.isDirectory(folder)) {
			getDocXDocumentRepository(resourceCenter).getRepositoryFolder(folder, true);
		}
	}*/

}
