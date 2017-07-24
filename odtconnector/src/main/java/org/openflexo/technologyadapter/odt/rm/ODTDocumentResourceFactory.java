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
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.odt.ODTTechnologyAdapter;
import org.openflexo.technologyadapter.odt.model.ODTDocument;

/**
 * Implementation of ResourceFactory for {@link ODTDocumentResource}
 * 
 * @author sylvain
 *
 */
public class ODTDocumentResourceFactory extends FlexoResourceFactory<ODTDocumentResource, ODTDocument, ODTTechnologyAdapter> {

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
	protected <I> ODTDocumentResource registerResource(ODTDocumentResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<ODTTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Register the resource in the ODTDocumentRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				technologyContextManager.getTechnologyAdapter().getODTDocumentRepository(resourceCenter));

		return resource;
	}

	/*
	public static ODTDocumentResource makeODTDocumentResource(String modelURI, File modelFile,
			ODTTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(ODTDocumentResource.class, FileFlexoIODelegate.class));
			ODTDocumentResourceImpl returned = (ODTDocumentResourceImpl) factory.newInstance(ODTDocumentResource.class);
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
	
	public static ODTDocumentResource retrieveODTDocumentResource(File modelFile, ODTTechnologyContextManager technologyContextManager,
			FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(ODTDocumentResource.class, FileFlexoIODelegate.class));
			ODTDocumentResourceImpl returned = (ODTDocumentResourceImpl) factory.newInstance(ODTDocumentResource.class);
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
	}
	*/

}
