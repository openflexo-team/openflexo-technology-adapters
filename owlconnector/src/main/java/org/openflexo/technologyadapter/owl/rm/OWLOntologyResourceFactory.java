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

import java.io.IOException;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.TechnologySpecificFlexoResourceFactory;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.technologyadapter.owl.model.OWLOntology;

/**
 * Implementation of ResourceFactory for {@link OWLOntologyResource}
 * 
 * @author sylvain
 *
 */
public class OWLOntologyResourceFactory
		extends TechnologySpecificFlexoResourceFactory<OWLOntologyResource, OWLOntology, OWLTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(OWLOntologyResourceFactory.class.getPackage().getName());

	public static String OWL_FILE_EXTENSION = ".owl";

	public OWLOntologyResourceFactory() throws ModelDefinitionException {
		super(OWLOntologyResource.class);
	}

	@Override
	public OWLOntology makeEmptyResourceData(OWLOntologyResource resource) {

		return OWLOntology.createOWLEmptyOntology(resource.getURI(), resource.getIODelegate().getSerializationArtefactAsResource(),
				resource.getOntologyLibrary(), resource.getTechnologyAdapter());
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(OWL_FILE_EXTENSION);
	}

	@Override
	public <I> I getConvertableArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return null;
	}

	@Override
	protected <I> OWLOntologyResource registerResource(OWLOntologyResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		resource.setOntologyLibrary(getTechnologyAdapter(resourceCenter.getServiceManager()).getOntologyLibrary());

		// Register the resource in the OWLOntologyRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getOWLOntologyRepository(resourceCenter));

		return resource;
	}

	@Override
	protected <I> OWLOntologyResource initResourceForRetrieving(I serializationArtefact, FlexoResourceCenter<I> resourceCenter)
			throws ModelDefinitionException, IOException {
		OWLOntologyResource returned = super.initResourceForRetrieving(serializationArtefact, resourceCenter);
		returned.setURI(OWLOntology.findOntologyURI(returned.getIODelegate().getSerializationArtefactAsResource()));
		return returned;
	}

}
