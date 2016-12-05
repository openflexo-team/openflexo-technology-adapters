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

		return OWLOntology.createOWLEmptyOntology(resource.getURI(), resource.getFlexoIODelegate().getSerializationArtefactAsResource(),
				resource.getOntologyLibrary(), resource.getTechnologyAdapter());
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
			TechnologyContextManager<OWLTechnologyAdapter> technologyContextManager) throws ModelDefinitionException, IOException {
		OWLOntologyResource returned = super.initResourceForRetrieving(serializationArtefact, resourceCenter, technologyContextManager);
		returned.setURI(OWLOntology.findOntologyURI(returned.getFlexoIODelegate().getSerializationArtefactAsResource()));
		return returned;
	}

}
