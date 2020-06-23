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

package org.openflexo.technologyadapter.powerpoint.rm;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.TechnologySpecificFlexoResourceFactory;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.powerpoint.PowerpointTechnologyAdapter;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;

/**
 * Implementation of ResourceFactory for {@link PowerpointSlideshowResource}
 * 
 * @author sylvain
 *
 */
public class PowerpointSlideshowResourceFactory
		extends TechnologySpecificFlexoResourceFactory<PowerpointSlideshowResource, PowerpointSlideshow, PowerpointTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(PowerpointSlideshowResourceFactory.class.getPackage().getName());

	public static String PPT_FILE_EXTENSION = ".ppt";
	public static String PPTX_FILE_EXTENSION = ".pptx";

	public PowerpointSlideshowResourceFactory() throws ModelDefinitionException {
		super(PowerpointSlideshowResource.class);
	}

	@Override
	public PowerpointSlideshow makeEmptyResourceData(PowerpointSlideshowResource resource) {
		return new PowerpointSlideshow(resource.getTechnologyAdapter());
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(PPT_FILE_EXTENSION)
				|| resourceCenter.retrieveName(serializationArtefact).endsWith(PPTX_FILE_EXTENSION);
	}

	@Override
	public <I> PowerpointSlideshowResource registerResource(PowerpointSlideshowResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		// Register the resource in the PowerpointSlideshowRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getPowerpointSlideShowRepository(resourceCenter));

		return resource;
	}

}
