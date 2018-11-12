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
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.xml.XMLRootElementInfo;
import org.openflexo.xml.XMLRootElementReader;

/**
 * Implementation of ResourceFactory for {@link XSDMetaModelResource}
 * 
 * @author sylvain
 *
 */
public class XSDMetaModelResourceFactory
		extends TechnologySpecificFlexoResourceFactory<XSDMetaModelResource, XMLMetaModel, XMLTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(XSDMetaModelResourceFactory.class.getPackage().getName());

	public static final String XSD_EXTENSION = ".xsd";

	protected static XMLRootElementReader REreader = new XMLRootElementReader();

	public XSDMetaModelResourceFactory() throws ModelDefinitionException {
		super(XSDMetaModelResource.class);
	}

	@Override
	public XMLMetaModel makeEmptyResourceData(XSDMetaModelResource resource) {
		// TODO
		return null;
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return resourceCenter.retrieveName(serializationArtefact).endsWith(XSD_EXTENSION);
	}

	@Override
	protected <I> XSDMetaModelResource registerResource(XSDMetaModelResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		// Register the resource in the XSDMetaModelRepository of supplied resource center
		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getXSDMetaModelRepository(resourceCenter));

		return resource;
	}

	@Override
	protected <I> XSDMetaModelResource initResourceForRetrieving(I serializationArtefact, FlexoResourceCenter<I> resourceCenter)
			throws ModelDefinitionException, IOException {
		XSDMetaModelResource returned = super.initResourceForRetrieving(serializationArtefact, resourceCenter);

		XMLRootElementInfo xmlRootElementInfo = resourceCenter.getXMLRootElementInfo(serializationArtefact);
		if (xmlRootElementInfo != null) {
			String uri = xmlRootElementInfo.getAttribute("targetNamespace");
			if (StringUtils.isNotEmpty(uri)) {
				returned.setURI(uri);
			}
		}
		return returned;
	}

	/*public static XSDMetaModelResource makeXSDMetaModelResource(File xsdMetaModelFile, String uri,
			XMLTechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class,
					XSDMetaModelResource.class));
			XSDMetaModelResource returned = factory.newInstance(XSDMetaModelResource.class);
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setURI(uri);
			returned.initName("Unnamed");
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(xsdMetaModelFile, factory));
	
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}*/

}
