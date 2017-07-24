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

package org.openflexo.technologyadapter.freeplane.rm;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mapio.MapIO;
import org.freeplane.features.mapio.mindmapmode.MMapIO;
import org.freeplane.features.mode.Controller;
import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.impl.FreeplaneMapImpl;

/**
 * Implementation of ResourceFactory for {@link IFreeplaneResource}
 * 
 * @author sylvain
 *
 */
public class FreeplaneResourceFactory extends FlexoResourceFactory<IFreeplaneResource, IFreeplaneMap, FreeplaneTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(FreeplaneResourceFactory.class.getPackage().getName());

	protected static ModelFactory MODEL_FACTORY;

	static {
		try {
			MODEL_FACTORY = new ModelFactory(IFreeplaneMap.class);
		} catch (final ModelDefinitionException e) {
			final String msg = "Error while initializing Freeplane model resource";
			logger.severe(msg);
		}
	}

	public static final String FREEPLANE_FILE_EXTENSION = ".mm";

	public FreeplaneResourceFactory() throws ModelDefinitionException {
		super(IFreeplaneResource.class);
	}

	@Override
	public IFreeplaneMap makeEmptyResourceData(IFreeplaneResource resource) {

		if (resource.getIODelegate() instanceof FileIODelegate) {

			final FreeplaneMapImpl map = (FreeplaneMapImpl) MODEL_FACTORY.newInstance(IFreeplaneMap.class);
			map.setTechnologyAdapter(resource.getTechnologyAdapter());

			File serializationArtefact = (File) resource.getIODelegate().getSerializationArtefact();

			// Maybe noi initialized yet
			FreeplaneBasicAdapter.getInstance();
			final MapModel newMap = new MapModel();
			final NodeModel root = new NodeModel(serializationArtefact.getName(), newMap);
			newMap.setRoot(root);

			Controller.getCurrentModeController().getMapController().fireMapCreated(newMap);
			Controller.getCurrentModeController().getMapController().newMapView(newMap);
			try {
				((MMapIO) Controller.getCurrentModeController().getExtension(MapIO.class)).writeToFile(newMap, serializationArtefact);
			} catch (IOException e) {
				logger.severe("Exception raised during empty map creation");
			}

			map.setMapModel(newMap);

			return map;
		}
		logger.warning("makeEmptyResourceData() not supported for non-File serialization artefacts");
		return null;
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(FREEPLANE_FILE_EXTENSION);
	}

	@Override
	public <I> I getConvertableArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return null;
	}

	@Override
	protected <I> IFreeplaneResource registerResource(IFreeplaneResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<FreeplaneTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Register the resource in the FreeplaneResourceRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				technologyContextManager.getTechnologyAdapter().getFreeplaneResourceRepository(resourceCenter));

		return resource;
	}

	@Override
	protected <I> IFreeplaneResource initResourceForRetrieving(I serializationArtefact, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<FreeplaneTechnologyAdapter> technologyContextManager) throws ModelDefinitionException, IOException {
		IFreeplaneResource returned = super.initResourceForRetrieving(serializationArtefact, resourceCenter, technologyContextManager);
		return returned;
	}

	/*public static IFreeplaneResource makeFreeplaneResource(final String modelURI, final File modelFile,
			final FreeplaneTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter) {
		try {
			final ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, IFreeplaneResource.class));
			final FreeplaneResourceImpl returned = (FreeplaneResourceImpl) factory.newInstance(IFreeplaneResource.class);
	
			returned.initName(modelFile.getName());
			// returned.setFile(modelFile);
	
			FileFlexoIODelegate fileIODelegate = factory.newInstance(FileFlexoIODelegate.class);
			returned.setFlexoIODelegate(fileIODelegate);
			fileIODelegate.setFile(modelFile);
	
			returned.setURI(modelURI);
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);
			return returned;
	
		} catch (final ModelDefinitionException e) {
			final String msg = "Error while initializing Freeplane model resource";
			LOGGER.log(Level.SEVERE, msg, e);
		}
		return null;
	}
	
	public static IFreeplaneResource makeFreeplaneResource(final File modelFile,
			final FreeplaneTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter) {
		try {
			final ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, IFreeplaneResource.class));
			final FreeplaneResourceImpl returned = (FreeplaneResourceImpl) factory.newInstance(IFreeplaneResource.class);
			returned.initName(modelFile.getName());
			// returned.setFile(modelFile);
	
			FileFlexoIODelegate fileIODelegate = factory.newInstance(FileFlexoIODelegate.class);
			returned.setFlexoIODelegate(fileIODelegate);
			fileIODelegate.setFile(modelFile);
	
			returned.setURI(modelFile.toURI().toString());
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);
			return returned;
		} catch (final ModelDefinitionException e) {
			final String msg = "Error while initializing Freeplane model resource";
			LOGGER.log(Level.SEVERE, msg, e);
		}
		return null;
	}*/

}
