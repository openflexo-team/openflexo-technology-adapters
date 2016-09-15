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

package org.openflexo.technologyadapter.emf.rm;

import java.io.File;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoIODelegate;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.EMFTechnologyContextManager;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.metamodel.io.MMFromJarsInDirIODelegate;
import org.openflexo.technologyadapter.emf.metamodel.io.MMFromJarsInDirIODelegate.MMFromJarsInDirIODelegateImpl;

/**
 * Implementation of ResourceFactory for {@link EMFMetaModelResource}
 * 
 * @author sylvain
 *
 */
public class EMFMetaModelResourceFactory extends FlexoResourceFactory<EMFMetaModelResource, EMFMetaModel, EMFTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(EMFMetaModelResourceFactory.class.getPackage().getName());

	public EMFMetaModelResourceFactory() throws ModelDefinitionException {
		super(EMFMetaModelResource.class, MMFromJarsInDirIODelegate.class, XtextEMFMetaModelResource.class);
	}

	@Override
	public EMFMetaModel makeEmptyResourceData(EMFMetaModelResource resource) {
		// TODO
		return null;
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {

		// System.out.println("On se demande si l'artefact est bon: " + serializationArtefact);

		if (serializationArtefact instanceof File) {
			boolean returned = MMFromJarsInDirIODelegateImpl.isValidMetaModelFile((File) serializationArtefact);
			if (returned) {
				System.out.println("Found valid EMF metamodel: " + serializationArtefact);
			}
			return returned;
		}
		// TODO: other kind of artefacts
		return false;
	}

	@Override
	protected <I> EMFMetaModelResource registerResource(EMFMetaModelResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<EMFTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Register the resource in the EMFMetaModelRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				technologyContextManager.getTechnologyAdapter().getEMFMetaModelRepository(resourceCenter));

		return resource;
	}

	/*@Override
	protected <I> EMFMetaModelResource initResourceForRetrieving(I serializationArtefact, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<EMFTechnologyAdapter> technologyContextManager) throws ModelDefinitionException {
		EMFMetaModelResource returned = super.initResourceForRetrieving(serializationArtefact, resourceCenter, technologyContextManager);
	
		return returned;
	}*/

	// Life-cyle is not managed the same way as usual: some methods are overriden here
	// The IO delegate is instantiated first, then used as a factory to retrieve the resource
	// TODO: it should be nice to refactor this to conform to usual procedure
	@Override
	public <I> EMFMetaModelResource retrieveResource(I serializationArtefact, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<EMFTechnologyAdapter> technologyContextManager) throws ModelDefinitionException {

		if (serializationArtefact instanceof File) {
			File aMetaModelFile = (File) serializationArtefact;
			MMFromJarsInDirIODelegate iodelegate = MMFromJarsInDirIODelegateImpl.makeMMFromJarsInDirIODelegate(aMetaModelFile, this);

			EMFMetaModelResource returned = iodelegate.retrieveMetaModelResource(aMetaModelFile, this,
					(EMFTechnologyContextManager) technologyContextManager);
			returned.setResourceCenter(resourceCenter);
			returned.initName(resourceCenter.retrieveName(serializationArtefact));
			returned.setFlexoIODelegate(iodelegate);

			registerResource(returned, resourceCenter, technologyContextManager);
			return returned;
		}

		return super.retrieveResource(serializationArtefact, resourceCenter, technologyContextManager);
	}

	@Override
	protected <I> FlexoIODelegate<I> makeFlexoIODelegate(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {

		if (serializationArtefact instanceof File) {
			return (FlexoIODelegate<I>) MMFromJarsInDirIODelegateImpl.makeMMFromJarsInDirIODelegate((File) serializationArtefact, this);
		}
		return super.makeFlexoIODelegate(serializationArtefact, resourceCenter);

	}

}
