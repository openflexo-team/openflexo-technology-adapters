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

package org.openflexo.technologyadapter.odt.rm;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.TechnologySpecificFlexoResourceFactory;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.odt.ODTTechnologyAdapter;
import org.openflexo.technologyadapter.odt.model.ODTDocument;

/**
 * Implementation of ResourceFactory for {@link ODTDocumentResource}
 * 
 * @author sylvain
 *
 */
public class ODTDocumentResourceFactory
		extends TechnologySpecificFlexoResourceFactory<ODTDocumentResource, ODTDocument, ODTTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(ODTDocumentResourceFactory.class.getPackage().getName());

	private static String ODT_FILE_EXTENSION = ".odt";

	public ODTDocumentResourceFactory() throws ModelDefinitionException {
		super(ODTDocumentResource.class);
	}

	@Override
	public ODTDocument makeEmptyResourceData(ODTDocumentResource resource) {
		// TODO
		return null;
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(ODT_FILE_EXTENSION);
	}

	@Override
	public <I> I getConvertableArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return null;
	}

	@Override
	protected <I> ODTDocumentResource registerResource(ODTDocumentResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		// Register the resource in the ODTDocumentRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getODTDocumentRepository(resourceCenter));

		return resource;
	}

}
