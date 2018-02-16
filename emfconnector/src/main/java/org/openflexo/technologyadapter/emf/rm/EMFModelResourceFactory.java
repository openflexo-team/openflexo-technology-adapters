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

import java.io.IOException;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.TechnologySpecificFlexoResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.EMFTechnologyContextManager;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.io.EMFModelConverter;

/**
 * Implementation of ResourceFactory for {@link EMFModelResource}
 * 
 * @author sylvain
 *
 */
public class EMFModelResourceFactory extends TechnologySpecificFlexoResourceFactory<EMFModelResource, EMFModel, EMFTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(EMFModelResourceFactory.class.getPackage().getName());

	public EMFModelResourceFactory() throws ModelDefinitionException {
		super(EMFModelResource.class);
	}

	@Override
	public EMFModel makeEmptyResourceData(EMFModelResource resource) {
		// Creates the EMF model from scratch
		EMFModelConverter converter = new EMFModelConverter();
		return converter.convertModel(resource.getMetaModelResource().getMetaModelData(), resource.getEMFResource());
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return true;
	}

	@Override
	public <I> I getConvertableArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return null;
	}

	public <I> EMFModelResource makeEMFModelResource(I serializationArtefact, EMFMetaModelResource metaModelResource,
			FlexoResourceCenter<I> resourceCenter, String name, String uri, boolean createEmptyContents)
			throws SaveResourceException, ModelDefinitionException {

		EMFModelResource returned = initResourceForCreation(serializationArtefact, resourceCenter, name, uri);
		returned.setMetaModelResource(metaModelResource);
		registerResource(returned, resourceCenter);

		if (createEmptyContents) {
			createEmptyContents(returned);
			returned.save(null);
		}

		return returned;
	}

	@Override
	protected <I> EMFModelResource registerResource(EMFModelResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getEMFModelRepository(resourceCenter));

		return resource;
	}

	@Override
	public <I> EMFModelResource retrieveResource(I serializationArtefact, FlexoResourceCenter<I> resourceCenter)
			throws ModelDefinitionException, IOException {

		TechnologyContextManager<EMFTechnologyAdapter> technologyContextManager = getTechnologyContextManager(
				resourceCenter.getServiceManager());

		for (EMFMetaModelResource mmRes : ((EMFTechnologyContextManager) technologyContextManager).getAllMetaModelResources()) {
			if (isValidSerializationArtefact(serializationArtefact, resourceCenter, mmRes)) {
				EMFModelResource returned = initResourceForRetrieving(serializationArtefact, resourceCenter);
				returned.setMetaModelResource(mmRes);
				registerResource(returned, resourceCenter);
			}
		}

		return null;
	}

	/**
	 * Return flag indicating if supplied serialization artefact represents a valid model conform to supplied meta-model
	 * 
	 * @param aModelFile
	 * @param metaModelResource
	 * @param technologyContextManager
	 * @return
	 */
	public <I> boolean isValidSerializationArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter,
			EMFMetaModelResource metaModelResource) {
		if (resourceCenter.exists(serializationArtefact) && !resourceCenter.isDirectory(serializationArtefact)) {
			// TODO syntaxic check and conformity to XMI
			if (resourceCenter.retrieveName(serializationArtefact)
					.endsWith(metaModelResource.getTechnologyAdapter().getExpectedModelExtension(metaModelResource))) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected <I> EMFModelResource initResourceForRetrieving(I serializationArtefact, FlexoResourceCenter<I> resourceCenter)
			throws ModelDefinitionException, IOException {
		EMFModelResource returned = super.initResourceForRetrieving(serializationArtefact, resourceCenter);

		// TODO: uri management ???
		/*XMLRootElementInfo xmlRootElementInfo = resourceCenter.getXMLRootElementInfo(serializationArtefact);
		if (xmlRootElementInfo != null) {
			String uri = xmlRootElementInfo.getAttribute("targetNamespace");
			if (StringUtils.isNotEmpty(uri)) {
				returned.setURI(uri);
			}
		}*/

		return returned;
	}

}
