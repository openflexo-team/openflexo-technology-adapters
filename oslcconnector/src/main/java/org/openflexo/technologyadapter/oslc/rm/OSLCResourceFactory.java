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
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
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
public class OSLCResourceFactory extends FlexoResourceFactory<OSLCResourceResource, OSLCServiceProviderCatalog, OSLCTechnologyAdapter> {

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
	protected <I> OSLCResourceResource registerResource(OSLCResourceResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<OSLCTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Register the resource in the OSLCRepository of supplied resource center
		registerResourceInResourceRepository(resource, technologyContextManager.getTechnologyAdapter().getOSLCRepository(resourceCenter));

		return resource;
	}

	/*public static OSLCResourceResource makeOSLCResource(String modelURI, File modelFile,
			OSLCTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, OSLCResourceResource.class));
			OSLCResourceResourceImpl returned = (OSLCResourceResourceImpl) factory.newInstance(OSLCResourceResource.class);
			FileFlexoIODelegate fileIODelegate = factory.newInstance(FileFlexoIODelegate.class);
			returned.setFlexoIODelegate(fileIODelegate);
			fileIODelegate.setFile(modelFile);
			returned.initName(modelFile.getName());
			returned.setURI(modelURI);
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);
	
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static OSLCResourceResource retrieveOSLCResource(File oslcFile, OSLCTechnologyContextManager technologyContextManager,
			FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, OSLCResourceResource.class));
			OSLCResourceResourceImpl returned = (OSLCResourceResourceImpl) factory.newInstance(OSLCResourceResource.class);
			FileFlexoIODelegate fileIODelegate = factory.newInstance(FileFlexoIODelegate.class);
			returned.setFlexoIODelegate(fileIODelegate);
			fileIODelegate.setFile(oslcFile);
			returned.initName(oslcFile.getName());
			returned.setURI(oslcFile.toURI().toString());
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}*/

}
