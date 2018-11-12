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
import org.openflexo.foundation.fml.annotations.DeclareResourceTypes;
import org.openflexo.foundation.fml.annotations.DeclareVirtualModelInstanceNatures;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
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
@DeclareResourceTypes({ DocXDocumentResourceFactory.class })
@DeclareVirtualModelInstanceNatures({ FMLControlledDocXVirtualModelInstanceNature.class })
public class DocXTechnologyAdapter extends TechnologyAdapter<DocXTechnologyAdapter> {

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
	protected String getLocalizationDirectory() {
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
		return null;
	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	public <I> DocXDocumentRepository<I> getDocXDocumentRepository(FlexoResourceCenter<I> resourceCenter) {
		DocXDocumentRepository<I> returned = resourceCenter.retrieveRepository(DocXDocumentRepository.class, this);
		if (returned == null) {
			returned = DocXDocumentRepository.instanciateNewRepository(this, resourceCenter);
			resourceCenter.registerRepository(returned, DocXDocumentRepository.class, this);
		}
		return returned;
	}

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

		File docXFile = new File(resourceCenter.getRootDirectory() + relativePath, filename);

		DocXDocumentResource docXDocumentResource = null;
		try {
			docXDocumentResource = getDocXDocumentResourceFactory().makeResource(docXFile, resourceCenter, true);
		} catch (SaveResourceException e) {
			e.printStackTrace();
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
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
}
