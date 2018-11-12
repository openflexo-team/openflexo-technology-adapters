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
import org.openflexo.foundation.resource.TechnologySpecificFlexoResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
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
public class XMLFileResourceFactory extends TechnologySpecificFlexoResourceFactory<XMLFileResource, XMLModel, XMLTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(XMLFileResourceFactory.class.getPackage().getName());

	public static final String XML_EXTENSION = ".xml";

	public XMLFileResourceFactory() throws ModelDefinitionException {
		super(XMLFileResource.class);
	}

	@Override
	public XMLModel makeEmptyResourceData(XMLFileResource resource) {
		return XMLModelImpl.getModelFactory().newInstance(XMLModel.class);
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(XML_EXTENSION);
	}

	@Override
	protected <I> XMLFileResource registerResource(XMLFileResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		// Register the resource in the XMLModelRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getXMLModelRepository(resourceCenter));

		return resource;
	}

	@Override
	protected <I> XMLFileResource initResourceForRetrieving(I serializationArtefact, FlexoResourceCenter<I> resourceCenter)
			throws ModelDefinitionException, IOException {
		XMLFileResource returned = super.initResourceForRetrieving(serializationArtefact, resourceCenter);

		TechnologyContextManager<XMLTechnologyAdapter> technologyContextManager = getTechnologyContextManager(
				resourceCenter.getServiceManager());

		XMLRootElementInfo xmlRootElementInfo = resourceCenter.getXMLRootElementInfo(serializationArtefact);
		if (xmlRootElementInfo != null) {
			String mmURI = xmlRootElementInfo.getURI();
			if (StringUtils.isNotEmpty(mmURI)) {
				XSDMetaModelResource mmRes = (XSDMetaModelResource) technologyContextManager.getResourceWithURI(mmURI);
				returned.setMetaModelResource(mmRes);
			}
		}
		return returned;
	}

	/**
	 * 
	 * @param modelURI
	 * @param xmlFile
	 * @param technologyContextManager
	 * @return
	 */
	/*public static XMLFileResource makeXMLFileResource(File xmlFile, XMLTechnologyContextManager technologyContextManager,
			FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, XMLFileResource.class));
			XMLFileResourceImpl returned = (XMLFileResourceImpl) factory.newInstance(XMLFileResource.class);
			returned.initName(xmlFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(xmlFile, factory));
	
			returned.setURI(xmlFile.toURI().toString());
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
	
			technologyContextManager.registerResource(returned);
	
			if (!xmlFile.exists()) {
	
				if (returned.resourceData == null) {
					returned.resourceData = XMLModelImpl.getModelFactory().newInstance(XMLModel.class);
					returned.resourceData.setResource(returned);
				}
	
				returned.isLoaded = true;
				returned.save(null);
			}
	
			return returned;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/

}
