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

package org.openflexo.technologyadapter.csv.rm;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;
import org.openflexo.technologyadapter.csv.model.CSVModel;

/**
 * Implementation of ResourceFactory for {@link CSVModelResource}
 * 
 * @author sylvain
 *
 */
public class CSVModelResourceFactory extends FlexoResourceFactory<CSVModelResource, CSVModel, CSVTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(CSVModelResourceFactory.class.getPackage().getName());

	private static String CSV_FILE_EXTENSION = ".csv";

	public CSVModelResourceFactory() throws ModelDefinitionException {
		super(CSVModelResource.class);
	}

	@Override
	public CSVModel makeEmptyResourceData(CSVModelResource resource) {
		return new CSVModel(resource.getURI(), resource.getTechnologyAdapter());
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(CSV_FILE_EXTENSION);
	}

	@Override
	protected <I> CSVModelResource registerResource(CSVModelResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<CSVTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Register the resource in the CSVModelRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				technologyContextManager.getTechnologyAdapter().getCSVModelResourceRepository(resourceCenter));

		return resource;
	}

	/*public static CSVModelResource makeCSVModelResource(String modelURI, File modelFile,
			CSVTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, CSVModelResource.class));
			CSVModelResourceImpl returned = (CSVModelResourceImpl) factory.newInstance(CSVModelResource.class);
			returned.initName(modelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
	
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
	
	public static CSVModelResource retrieveCSVModelResource(File modelFile, CSVTechnologyContextManager technologyContextManager,
			FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, CSVModelResource.class));
			CSVModelResourceImpl returned = (CSVModelResourceImpl) factory.newInstance(CSVModelResource.class);
			returned.initName(modelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
			returned.setURI(modelFile.toURI().toString());
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