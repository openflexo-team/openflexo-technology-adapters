/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Powerpointconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.powerpoint;

import java.io.File;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareResourceFactories;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.powerpoint.fml.binding.PowerpointBindingFactory;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideShowRepository;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResource;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResourceFactory;

/**
 * This class defines and implements the Powerpoint technology adapter
 * 
 * @author sylvain, vincent, Christophe
 * 
 */
@DeclareModelSlots({ BasicPowerpointModelSlot.class })
@DeclareResourceFactories({ PowerpointSlideshowResourceFactory.class })
public class PowerpointTechnologyAdapter extends TechnologyAdapter<PowerpointTechnologyAdapter> {

	protected static final Logger logger = Logger.getLogger(PowerpointTechnologyAdapter.class.getPackage().getName());

	private static final PowerpointBindingFactory BINDING_FACTORY = new PowerpointBindingFactory();

	/**
	 * 
	 * Constructor.
	 * 
	 * @throws TechnologyAdapterInitializationException
	 */
	public PowerpointTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return "Powerpoint technology adapter";
	}

	@Override
	protected String getLocalizationDirectory() {
		return "FlexoLocalization/PowerPointTechnologyAdapter";
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		return BINDING_FACTORY;
	}

	/**
	 * Initialize the supplied resource center with the technology<br>
	 * ResourceCenter is scanned, ResourceRepositories are created and new technology-specific resources are build and registered.
	 * 
	 * @param resourceCenter
	 */
	/*@Override
	public <I> void performInitializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {
	
		PowerpointSlideShowRepository ssRepository = resourceCenter.getRepository(PowerpointSlideShowRepository.class, this);
		if (ssRepository == null) {
			ssRepository = createSlideshowRepository(resourceCenter);
		}
	
		Iterator<I> it = resourceCenter.iterator();
	
		while (it.hasNext()) {
			I item = it.next();
			if (item instanceof File) {
				// System.out.println("searching " + item);
				File candidateFile = (File) item;
				PowerpointSlideshowResource ssRes = tryToLookupSlideshow(resourceCenter, candidateFile);
			}
		}
	
		// Call it to update the current repositories
		notifyRepositoryStructureChanged();
	}*/

	/*protected PowerpointSlideshowResource tryToLookupSlideshow(FlexoResourceCenter<?> resourceCenter, File candidateFile) {
		PowerpointTechnologyContextManager technologyContextManager = getTechnologyContextManager();
		if (isValidSlideshowFile(candidateFile)) {
			PowerpointSlideshowResource ssRes = retrieveSlideshowResource(candidateFile, resourceCenter);
			PowerpointSlideShowRepository ssRepository = resourceCenter.getRepository(PowerpointSlideShowRepository.class, this);
			if (ssRes != null) {
				RepositoryFolder<PowerpointSlideshowResource> folder;
				try {
					folder = ssRepository.getRepositoryFolder(candidateFile, true);
					ssRepository.registerResource(ssRes, folder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				referenceResource(ssRes, resourceCenter);
				return ssRes;
			}
		}
		return null;
	}*/

	/**
	 * Instantiate new workbook resource stored in supplied model file<br>
	 * *
	 */
	/*public PowerpointSlideshowResource retrieveSlideshowResource(File slideshowFile, FlexoResourceCenter<?> resourceCenter) {
		PowerpointSlideshowResource ssResource = null;
	
		// TODO: try to look-up already found file
	
		ssResource = PowerpointSlideshowResourceImpl.retrievePowerpointSlideshowResource(slideshowFile, getTechnologyContextManager(),
				resourceCenter);
	
		return ssResource;
	}*/

	/**
	 * Return flag indicating if supplied file appears as a valid slide show
	 * 
	 * @param candidateFile
	 * 
	 * @return
	 */
	/*public boolean isValidSlideshowFile(File candidateFile) {
		return candidateFile.getName().endsWith(".pptx") || candidateFile.getName().endsWith(".ppt");
	}*/

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	/*@Override
	public <I> boolean contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		if (contents instanceof File) {
			File candidateFile = (File) contents;
			if (tryToLookupSlideshow(resourceCenter, candidateFile) != null) {
				return true;
			}
		}
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
	 * Create a workbook repository for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 */
	/*public PowerpointSlideShowRepository createSlideshowRepository(FlexoResourceCenter<?> resourceCenter) {
		PowerpointSlideShowRepository returned = new PowerpointSlideShowRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, PowerpointSlideShowRepository.class, this);
		return returned;
	}*/

	public <I> PowerpointSlideShowRepository<I> getPowerpointSlideShowRepository(FlexoResourceCenter<I> resourceCenter) {
		PowerpointSlideShowRepository<I> returned = resourceCenter.retrieveRepository(PowerpointSlideShowRepository.class, this);
		if (returned == null) {
			returned = PowerpointSlideShowRepository.instanciateNewRepository(this, resourceCenter);
			resourceCenter.registerRepository(returned, PowerpointSlideShowRepository.class, this);
		}
		return returned;
	}

	/**
	 * Create empty model.
	 * 
	 * @param modelFile
	 * @param modelUri
	 * @param metaModelResource
	 * @param technologyContextManager
	 * @return
	 * @throws ModelDefinitionException
	 * @throws SaveResourceException
	 */
	@Deprecated
	public PowerpointSlideshowResource createNewSlideshow(FlexoResourceCenter<File> rc, String pptFilename, String modelUri)
			throws SaveResourceException, ModelDefinitionException {

		if (rc instanceof FlexoProject) {
			File pptFile = new File(((FlexoProject<File>) rc).getProjectDirectory(), pptFilename);

			modelUri = pptFile.toURI().toString();

			PowerpointSlideshowResource slideshowResource = getPowerpointSlideshowResourceFactory().makeResource(pptFile, rc, true);

			getTechnologyContextManager().registerResource(slideshowResource);

			return slideshowResource;
		}
		else {
			logger.warning("UNABLE TO CREATE NEW FILE => not a FlexoProject: " + rc.toString());
			return null;
		}
	}

	@Override
	public String getIdentifier() {
		return "PPT";
	}

	public PowerpointSlideshowResourceFactory getPowerpointSlideshowResourceFactory() {
		return getResourceFactory(PowerpointSlideshowResourceFactory.class);
	}

	/*@Override
	protected <I> void foundFolder(FlexoResourceCenter<I> resourceCenter, I folder) throws IOException {
		super.foundFolder(resourceCenter, folder);
		if (resourceCenter.isDirectory(folder)) {
			getPowerpointSlideShowRepository(resourceCenter).getRepositoryFolder(folder, true);
		}
	}*/

}
