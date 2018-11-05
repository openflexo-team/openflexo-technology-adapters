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

package org.openflexo.technologyadapter.oslc.rm;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.TechnologySpecificFlexoResourceFactory;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.oslc.OSLCTechnologyAdapter;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;

/**
 * Implementation of ResourceFactory for {@link OSLCResource}
 * 
 * @author sylvain
 *
 */
public class OSLCResourceFactory
		extends TechnologySpecificFlexoResourceFactory<OSLCResourceResource, OSLCServiceProviderCatalog, OSLCTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(OSLCResourceFactory.class.getPackage().getName());

	private static String OSLC_FILE_EXTENSION = ".oslc";

	public OSLCResourceFactory() throws ModelDefinitionException {
		super(OSLCResourceResource.class);
	}

	@Override
	public OSLCServiceProviderCatalog makeEmptyResourceData(OSLCResourceResource resource) {
		// return new OSLC(resource.getTechnologyAdapter());
		// TODO
		return null;
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(OSLC_FILE_EXTENSION);
	}

	@Override
	public <I> I getConvertableArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return null;
	}

	@Override
	protected <I> OSLCResourceResource registerResource(OSLCResourceResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		// Register the resource in the OSLCRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getOSLCRepository(resourceCenter));

		return resource;
	}

}
