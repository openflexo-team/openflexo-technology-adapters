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

package org.openflexo.technologyadapter.gina.rm;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.TechnologySpecificPamelaResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.technologyadapter.gina.model.GINAFactory;

/**
 * Implementation of PamelaResourceFactory for {@link DocXDocumentResource}
 * 
 * @author sylvain
 *
 */
public class GINAFIBComponentResourceFactory
		extends TechnologySpecificPamelaResourceFactory<GINAFIBComponentResource, GINAFIBComponent, GINATechnologyAdapter, GINAFactory> {

	private static final Logger logger = Logger.getLogger(GINAFIBComponentResourceFactory.class.getPackage().getName());

	public static String GINA_COMPONENT_EXTENSION = ".fib";
	public static String GINA_INSPECTOR_EXTENSION = ".inspector";

	public GINAFIBComponentResourceFactory() throws ModelDefinitionException {
		super(GINAFIBComponentResource.class);
	}

	@Override
	public GINAFactory makeResourceDataFactory(GINAFIBComponentResource resource,
			TechnologyContextManager<GINATechnologyAdapter> technologyContextManager) throws ModelDefinitionException {
		return new GINAFactory(resource, technologyContextManager.getServiceManager());
	}

	@Override
	public GINAFIBComponent makeEmptyResourceData(GINAFIBComponentResource resource) {
		return resource.getFactory().makeNewGINAFIBComponent();
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(GINA_COMPONENT_EXTENSION)
				|| resourceCenter.retrieveName(serializationArtefact).endsWith(GINA_INSPECTOR_EXTENSION);
	}

	@Override
	public <I> I getConvertableArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return null;
	}

	public <I> GINAFIBComponentResource makeGINAFIBComponentResource(String baseName, RepositoryFolder<GINAFIBComponentResource, I> folder,
			boolean createEmptyContents) throws SaveResourceException, ModelDefinitionException {

		FlexoResourceCenter<I> rc = folder.getResourceRepository().getResourceCenter();

		String artefactName = baseName.endsWith(GINA_COMPONENT_EXTENSION) ? baseName : baseName + GINA_COMPONENT_EXTENSION;

		I serializationArtefact = rc.createEntry(artefactName, folder.getSerializationArtefact());

		GINAFIBComponentResource newResource = makeResource(serializationArtefact, rc, true);

		return newResource;
	}

	@Override
	protected <I> GINAFIBComponentResource registerResource(GINAFIBComponentResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		// Register the resource in the GINAResourceRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getGINAResourceRepository(resourceCenter));

		return resource;
	}

	/*public static GINAFIBComponentResource makeComponentResource(File componentFile,
			GINATechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(GINAFIBComponentResource.class, FileFlexoIODelegate.class));
			GINAFIBComponentResourceImpl returned = (GINAFIBComponentResourceImpl) factory.newInstance(GINAFIBComponentResource.class);
			returned.initName(componentFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(componentFile, factory));
			GINAFactory ginaFactory = new GINAFactory(returned, technologyContextManager.getServiceManager());
			returned.setFactory(ginaFactory);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);
	
			return returned;
		} catch (ModelDefinitionException e) {
			final String msg = "Error while initializing GINAFIBComponentResource";
			LOGGER.log(Level.SEVERE, msg, e);
		}
		return null;
	}
	
	public static GINAFIBComponentResource retrieveComponentResource(File componentFile,
			GINATechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(GINAFIBComponentResource.class, FileFlexoIODelegate.class));
			GINAFIBComponentResourceImpl returned = (GINAFIBComponentResourceImpl) factory.newInstance(GINAFIBComponentResource.class);
			returned.initName(componentFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(componentFile, factory));
			GINAFactory ginaFactory = new GINAFactory(returned, technologyContextManager.getServiceManager());
			returned.setFactory(ginaFactory);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			final String msg = "Error while initializing GIN model resource";
			LOGGER.log(Level.SEVERE, msg, e);
		}
		return null;
	}*/

}
