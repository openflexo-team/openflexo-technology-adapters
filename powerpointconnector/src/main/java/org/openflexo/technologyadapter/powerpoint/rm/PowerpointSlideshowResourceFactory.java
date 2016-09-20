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
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.powerpoint.PowerpointTechnologyAdapter;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;

/**
 * Implementation of ResourceFactory for {@link PowerpointSlideshowResource}
 * 
 * @author sylvain
 *
 */
public class PowerpointSlideshowResourceFactory
		extends FlexoResourceFactory<PowerpointSlideshowResource, PowerpointSlideshow, PowerpointTechnologyAdapter> {

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
	protected <I> PowerpointSlideshowResource registerResource(PowerpointSlideshowResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<PowerpointTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Register the resource in the PowerpointSlideshowRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				technologyContextManager.getTechnologyAdapter().getPowerpointSlideShowRepository(resourceCenter));

		return resource;
	}

	/*
	
		public static PowerpointSlideshowResource makePowerpointSlideshowResource(String modelURI, File powerpointFile,
			PowerpointTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, PowerpointSlideshowResource.class));
			PowerpointSlideshowResourceImpl returned = (PowerpointSlideshowResourceImpl) factory
					.newInstance(PowerpointSlideshowResource.class);
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			returned.initName(powerpointFile.getName());
	
			// returned.setFile(powerpointFile);
			FileFlexoIODelegate fileIODelegate = factory.newInstance(FileFlexoIODelegate.class);
			returned.setFlexoIODelegate(fileIODelegate);
			fileIODelegate.setFile(powerpointFile);
	
			returned.setURI(modelURI);
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			technologyContextManager.registerResource(returned);
	
			PowerpointSlideshow resourceData = new PowerpointSlideshow(technologyContextManager.getTechnologyAdapter());
			returned.setResourceData(resourceData);
			resourceData.setResource(returned);
	
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static PowerpointSlideshowResource retrievePowerpointSlideshowResource(File modelFile,
			PowerpointTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, PowerpointSlideshowResource.class));
			PowerpointSlideshowResourceImpl returned = (PowerpointSlideshowResourceImpl) factory
					.newInstance(PowerpointSlideshowResource.class);
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			returned.initName(modelFile.getName());
	
			// returned.setFile(modelFile);
			FileFlexoIODelegate fileIODelegate = factory.newInstance(FileFlexoIODelegate.class);
			returned.setFlexoIODelegate(fileIODelegate);
			fileIODelegate.setFile(modelFile);
	
			returned.setURI(modelFile.toURI().toString());
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			technologyContextManager.registerResource(returned);
	
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	 */
}
