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

package org.openflexo.technologyadapter.xml.rm;

import java.io.IOException;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.resource.StreamIODelegate;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLModelImpl;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.xml.XMLRootElementInfo;

/**
 * Implementation of ResourceFactory for {@link XMLFileResource}
 * 
 * @author sylvain
 *
 */

public class XMLResourceFactory extends FlexoResourceFactory<XMLResource, XMLModel, XMLTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(XMLResourceFactory.class.getPackage().getName());

	public static final String XML_EXTENSION = ".xml";

	public XMLResourceFactory() throws ModelDefinitionException {
		// TODO: some better management of Files some day
		super(XMLResource.class);
	}

	@Override
	public XMLModel makeEmptyResourceData(XMLResource resource) {

		XMLModel model = null;

		if (resource.getIODelegate() instanceof StreamIODelegate) {
			model = createXMLEmptyModel((StreamIODelegate) resource.getIODelegate());
			// Sets Meta-Model

			FlexoMetaModelResource<XMLModel, XMLMetaModel, XMLTechnologyAdapter> mmRes = resource.getMetaModelResource();
			if (mmRes != null && model != null) {
				model.setMetaModel(mmRes.getLoadedResourceData());
			}
			return model;
		}

		logger.severe("Cannot create XML Resource for this io delegate: " + resource.getIODelegate());
		return null;

	}

	public static <I> XMLModel createXMLEmptyModel(StreamIODelegate<I> ioDelegate) {
		XMLModel model = null;
		if (!ioDelegate.exists() && ioDelegate.getSerializationArtefactName().endsWith(".xml")) {
			model = XMLModelImpl.getModelFactory().newInstance(XMLModel.class);
		}
		return model;
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(XML_EXTENSION);
	}

	@Override
	protected <I> XMLResource registerResource(XMLResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<XMLTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Register the resource in the XMLModelRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				technologyContextManager.getTechnologyAdapter().getXMLModelRepository(resourceCenter));

		return resource;
	}

	@Override
	protected <I> XMLResource initResourceForRetrieving(I serializationArtefact, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<XMLTechnologyAdapter> technologyContextManager) throws ModelDefinitionException, IOException {

		// TODO: better management of files to support copy on save
		XMLResource returned = super.initResourceForRetrieving(serializationArtefact, resourceCenter, technologyContextManager);
		if (returned != null) {
			XMLRootElementInfo xmlRootElementInfo = resourceCenter.getXMLRootElementInfo(serializationArtefact);
			if (xmlRootElementInfo != null) {
				String mmURI = xmlRootElementInfo.getURI();
				if (StringUtils.isNotEmpty(mmURI)) {
					XSDMetaModelResource mmRes = (XSDMetaModelResource) technologyContextManager.getResourceWithURI(mmURI);
					returned.setMetaModelResource(mmRes);
				}
			}
		}
		return returned;
	}

}
