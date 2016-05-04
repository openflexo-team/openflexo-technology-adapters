/**
 * 
 * Copyright (c) 2013-2015, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Owlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.owl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.fml.annotations.DeclareResourceTypes;
import org.openflexo.foundation.fml.annotations.DeclareTechnologySpecificTypes;
import org.openflexo.foundation.ontology.technologyadapter.FlexoOntologyTechnologyContextManager;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.technologyadapter.owl.fml.binding.OWLBindingFactory;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLOntology.OntologyNotFoundException;
import org.openflexo.technologyadapter.owl.model.OWLOntologyAsMetaModelRepository;
import org.openflexo.technologyadapter.owl.model.OWLOntologyAsModelRepository;
import org.openflexo.technologyadapter.owl.model.OWLOntologyLibrary;
import org.openflexo.technologyadapter.owl.model.StatementWithProperty;
import org.openflexo.technologyadapter.owl.rm.OWLOntologyResource;
import org.openflexo.technologyadapter.owl.rm.OWLOntologyResourceImpl;

/**
 * This class defines and implements the OWL technology adapter
 * 
 * @author sylvain, luka
 * 
 */
@DeclareModelSlots({ OWLModelSlot.class })
@DeclareRepositoryType({ OWLOntologyAsModelRepository.class })
@DeclareTechnologySpecificTypes({ StatementWithProperty.class })
@DeclareResourceTypes({ OWLOntologyResource.class })
public class OWLTechnologyAdapter extends TechnologyAdapter {

	private static final Logger logger = Logger.getLogger(OWLTechnologyAdapter.class.getPackage().getName());

	private static final OWLBindingFactory BINDING_FACTORY = new OWLBindingFactory();

	public OWLTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return "OWL technology adapter";
	}

	/**
	 * Return the {@link FlexoOntologyTechnologyContextManager} for this technology shared by all {@link FlexoResourceCenter} declared in
	 * the scope of {@link FlexoResourceCenterService}
	 * 
	 * @return
	 */
	@Override
	public OWLOntologyLibrary getTechnologyContextManager() {
		return (OWLOntologyLibrary) super.getTechnologyContextManager();
	}

	public OWLOntologyLibrary getOntologyLibrary() {
		return getTechnologyContextManager();
	}

	/**
	 * Initialize the supplied resource center with the technology<br>
	 * ResourceCenter is scanned, ResourceRepositories are created and new technology-specific resources are build and registered.
	 * 
	 * @param resourceCenter
	 */
	@Override
	public <I> void performInitializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {

		OWLOntologyLibrary ontologyLibrary = getOntologyLibrary();

		OWLOntologyAsModelRepository ontModelRepository = resourceCenter.getRepository(OWLOntologyAsModelRepository.class, this);
		OWLOntologyAsMetaModelRepository ontMetaModelRepository = resourceCenter.getRepository(OWLOntologyAsMetaModelRepository.class,
				this);
		if (ontModelRepository == null) {
			ontModelRepository = createOntologyAsModelRepository(resourceCenter);
		}
		if (ontMetaModelRepository == null) {
			ontMetaModelRepository = createOntologyAsMetaModelRepository(resourceCenter);
		}

		Iterator<I> it = resourceCenter.iterator();

		while (it.hasNext()) {
			I item = it.next();
			if (item instanceof File) {
				File candidateFile = (File) item;
				OWLOntologyResource ontRes = tryToLookupOntology(resourceCenter, candidateFile);
			}
		}

		getTechnologyContextManager().init();

		// Call it to update the current repositories
		getPropertyChangeSupport().firePropertyChange("getAllRepositories()", null, resourceCenter);
	}

	protected OWLOntologyResource tryToLookupOntology(FlexoResourceCenter<?> resourceCenter, File candidateFile) {
		if (isValidOntologyFile(candidateFile)) {
			OWLOntologyResource ontRes = retrieveOntologyResource(candidateFile, resourceCenter);
			OWLOntologyAsModelRepository ontModelRepository = resourceCenter.getRepository(OWLOntologyAsModelRepository.class, this);
			OWLOntologyAsMetaModelRepository ontMetaModelRepository = resourceCenter.getRepository(OWLOntologyAsMetaModelRepository.class,
					this);
			if (ontRes != null) {
				RepositoryFolder<OWLOntologyResource> folder;
				try {
					folder = ontModelRepository.getRepositoryFolder(candidateFile, true);
					ontModelRepository.registerResource(ontRes, folder);
					folder = ontMetaModelRepository.getRepositoryFolder(candidateFile, true);
					ontMetaModelRepository.registerResource(ontRes, folder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				referenceResource(ontRes, resourceCenter);
				return ontRes;
			}
		}
		return null;
	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	/**
	 * Return flag indicating if supplied file represents a valid OWL ontology
	 * 
	 * @param aMetaModelFile
	 * @return
	 */
	public boolean isValidOntologyFile(File aMetaModelFile) {
		// TODO: also check that file is valid
		return aMetaModelFile.isFile() && aMetaModelFile.getName().endsWith(OWLOntologyResource.OWL_SUFFIX);
	}

	@Override
	public <I> boolean contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		if (contents instanceof File) {
			File candidateFile = (File) contents;
			if (tryToLookupOntology(resourceCenter, candidateFile) != null) {
				// This is valid ontology file, this one has just been registered
				return true;
			}
		}
		return false;
	}

	@Override
	public <I> boolean contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		if (contents instanceof File) {
			System.out
					.println("File DELETED " + ((File) contents).getName() + " in " + ((File) contents).getParentFile().getAbsolutePath());
		}
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

	public OWLOntologyResource retrieveOntologyResource(File owlFile, FlexoResourceCenter<?> resourceCenter) {

		// logger.info("Retrieving OWL MetaModelResource for " + aMetaModelFile.getAbsolutePath());

		OWLOntologyResource ontologyResource = OWLOntologyResourceImpl.retrieveOWLOntologyResource(owlFile, getOntologyLibrary(),
				resourceCenter);
		logger.fine("Found OWL ontology " + ontologyResource.getURI() + " file:" + owlFile.getAbsolutePath());
		return ontologyResource;

	}

	public OWLOntologyResource createNewOntology(FlexoProject project, String filename, String modelUri,
			FlexoResource<OWLOntology> metaModel) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("createNewOWLModel(), project=" + project);
		}
		logger.info("-------------> Create ontology for " + project.getProjectName());

		File owlFile = new File(FlexoProject.getProjectSpecificModelsDirectory(project), filename);
		OWLOntologyResource returned = OWLOntologyResourceImpl.makeOWLOntologyResource(modelUri, owlFile, getOntologyLibrary(), project);
		OWLOntology ontology = returned.getModel();
		if (metaModel != null) {
			try {
				ontology.importOntology(metaModel.getResourceData(null));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ResourceLoadingCancelledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FlexoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			returned.save(null);
		} catch (Exception e1) {
			// Warns about the exception
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Exception raised: " + e1.getClass().getName() + ". See console for details.");
			}
			e1.printStackTrace();
		}

		return returned;
	}

	public OWLOntologyResource createNewOntology(FileSystemBasedResourceCenter resourceCenter, String relativePath, String filename,
			String modelUri, OWLOntologyResource metaModelResource) {
		logger.warning("Not implemented yet");
		return null;
	}

	public OWLOntologyAsModelRepository createOntologyAsModelRepository(FlexoResourceCenter resourceCenter) {
		OWLOntologyAsModelRepository returned = new OWLOntologyAsModelRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, OWLOntologyAsModelRepository.class, this);
		return returned;
	}

	public OWLOntologyAsMetaModelRepository createOntologyAsMetaModelRepository(FlexoResourceCenter resourceCenter) {
		OWLOntologyAsMetaModelRepository returned = new OWLOntologyAsMetaModelRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, OWLOntologyAsMetaModelRepository.class, this);
		return returned;
	}

	@Override
	public OWLOntologyLibrary createTechnologyContextManager(FlexoResourceCenterService resourceCenterService) {
		return new OWLOntologyLibrary(this, resourceCenterService);
	}

	public String retrieveModelURI(File aModelFile, FlexoResource<OWLOntology> metaModelResource) {
		return OWLOntology.findOntologyURI(aModelFile);
	}

	/**
	 * Provides a hook to finalize initialization of a TechnologyAdapter.<br>
	 * This method is called:
	 * <ul>
	 * <li>after all TechnologyAdapter have been loaded</li>
	 * <li>after all {@link FlexoResourceCenter} have been initialized</li>
	 * </ul>
	 */
	/*@Override
	public void initialize() {
		getTechnologyContextManager().init();
	}*/

	/**
	 * Provides a hook to detect when a new resource center was added or discovered
	 * 
	 * @param newResourceCenter
	 */
	/*@Override
	public void resourceCenterAdded(FlexoResourceCenter newResourceCenter) {
		getTechnologyContextManager().init();
	}*/

	@Override
	public OWLBindingFactory getTechnologyAdapterBindingFactory() {
		return BINDING_FACTORY;
	}

	public String getExpectedOntologyExtension() {
		return OWLOntologyResource.OWL_SUFFIX;
	}

	@Override
	public String getIdentifier() {
		return "OWL";
	}

}
