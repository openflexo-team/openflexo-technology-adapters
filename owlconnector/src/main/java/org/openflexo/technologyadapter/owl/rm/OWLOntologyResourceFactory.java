/*
 * (c) Copyright 2013 Openflexo
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

package org.openflexo.technologyadapter.owl.rm;

import java.io.File;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.technologyadapter.owl.model.OWLOntology;

/**
 * Implementation of ResourceFactory for {@link OWLOntologyResource}
 * 
 * @author sylvain
 *
 */
public class OWLOntologyResourceFactory extends FlexoResourceFactory<OWLOntologyResource, OWLOntology, OWLTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(OWLOntologyResourceFactory.class.getPackage().getName());

	public static String OWL_FILE_EXTENSION = ".owl";

	public OWLOntologyResourceFactory() throws ModelDefinitionException {
		super(OWLOntologyResource.class);
	}

	@Override
	public OWLOntology makeEmptyResourceData(OWLOntologyResource resource) {
		if (resource.getFlexoIODelegate() instanceof FileFlexoIODelegate) {
			return OWLOntology.createOWLEmptyOntology(resource.getURI(), ((FileFlexoIODelegate) resource.getFlexoIODelegate()).getFile(),
					resource.getOntologyLibrary(), resource.getTechnologyAdapter());
		}
		logger.warning("makeEmptyResourceData() not supported for non-File serialization artefacts");
		return null;
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(OWL_FILE_EXTENSION);
	}

	@Override
	protected <I> OWLOntologyResource registerResource(OWLOntologyResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<OWLTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		resource.setOntologyLibrary(technologyContextManager.getTechnologyAdapter().getOntologyLibrary());

		// Register the resource in the OWLOntologyRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				technologyContextManager.getTechnologyAdapter().getOWLOntologyRepository(resourceCenter));

		return resource;
	}

	@Override
	protected <I> OWLOntologyResource initResourceForRetrieving(I serializationArtefact, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<OWLTechnologyAdapter> technologyContextManager) throws ModelDefinitionException {
		OWLOntologyResource returned = super.initResourceForRetrieving(serializationArtefact, resourceCenter, technologyContextManager);
		if (serializationArtefact instanceof File) {
			returned.setURI(OWLOntology.findOntologyURI((File) serializationArtefact));
		}
		return returned;
	}

	/**
	 * Creates a new {@link OWLOntologyResource} asserting this is an explicit creation: no file is present on file system<br>
	 * This method should not be used to retrieve the resource from a file in the file system, use
	 * {@link #retrieveOWLOntologyResource(File, OWLOntologyLibrary)} instead
	 * 
	 * @param ontologyURI
	 * @param owlFile
	 * @param ontologyLibrary
	 * @return
	 */
	/*public static OWLOntologyResource makeOWLOntologyResource(String ontologyURI, File owlFile, OWLOntologyLibrary ontologyLibrary,
			FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, OWLOntologyResource.class));
			OWLOntologyResourceImpl returned = (OWLOntologyResourceImpl) factory.newInstance(OWLOntologyResource.class);
			returned.setTechnologyAdapter(ontologyLibrary.getTechnologyAdapter());
			returned.setOntologyLibrary(ontologyLibrary);
			returned.initName(owlFile.getName());
			// returned.setFile(owlFile);
	
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(owlFile, factory));
	
			returned.setURI(ontologyURI);
			// Register the ontology
			ontologyLibrary.registerResource(returned);
	
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(ontologyLibrary.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
	
			// Creates the ontology
			returned.setResourceData(
					OWLOntology.createOWLEmptyOntology(ontologyURI, owlFile, ontologyLibrary, ontologyLibrary.getTechnologyAdapter()));
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}*/

	/**
	 * Instanciates a new {@link OWLOntologyResource} asserting we are about to built a resource matching an existing file in the file
	 * system<br>
	 * This method should not be used to explicitely build a new ontology
	 * 
	 * @param owlFile
	 * @param ontologyLibrary
	 * @return
	 */
	/*public static OWLOntologyResource retrieveOWLOntologyResource(File owlFile, OWLOntologyLibrary ontologyLibrary,
			FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, OWLOntologyResource.class));
			OWLOntologyResourceImpl returned = (OWLOntologyResourceImpl) factory.newInstance(OWLOntologyResource.class);
			returned.setTechnologyAdapter(ontologyLibrary.getTechnologyAdapter());
			returned.setOntologyLibrary(ontologyLibrary);
			returned.initName(OWLOntology.findOntologyName(owlFile));
	
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(owlFile, factory));
	
			returned.setURI(OWLOntology.findOntologyURI(owlFile));
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(ontologyLibrary.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			// Register the ontology
			ontologyLibrary.registerOntology(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}*/

}
