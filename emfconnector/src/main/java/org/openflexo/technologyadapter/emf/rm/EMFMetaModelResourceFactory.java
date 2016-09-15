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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.ClassLoaderIODelegate;
import org.openflexo.foundation.resource.FlexoIODelegate;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.EMFTechnologyContextManager;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource.EMFMetaModelType;

/**
 * Implementation of ResourceFactory for {@link EMFMetaModelResource}
 * 
 * @author sylvain
 *
 */
public class EMFMetaModelResourceFactory extends FlexoResourceFactory<EMFMetaModelResource, EMFMetaModel, EMFTechnologyAdapter> {

	private static final Logger logger = Logger.getLogger(EMFMetaModelResourceFactory.class.getPackage().getName());

	public static final String PROPERTIES_SUFFIX = ".properties";

	public static final String URI_KEY = "URI";
	public static final String EXTENSION_KEY = "EXTENSION";
	public static final String PACKAGE_KEY = "PACKAGE";
	public static final String RESOURCE_FACTORY_KEY = "RESOURCE_FACTORY";

	public static String PROPERTY_TYPE = "TYPE";
	public static String TYPE_METAMODEL = "standard";
	public static String TYPE_PROFILE = "profile";
	public static String TYPE_XTEXT = "xtext";
	public static String PROPERTY_XTEXT_STANDALONE_SETUP = "XTEXT_STANDALONE_SETUP";

	public EMFMetaModelResourceFactory() throws ModelDefinitionException {
		super(EMFMetaModelResource.class, XtextEMFMetaModelResource.class);
	}

	@Override
	public EMFMetaModel makeEmptyResourceData(EMFMetaModelResource resource) {
		// TODO
		return null;
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {

		if (!resourceCenter.isDirectory(serializationArtefact)) {
			return false;
		}

		Properties properties;
		try {
			properties = resourceCenter.getProperties(serializationArtefact);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (properties == null) {
			return false;
		}

		String uri = properties.getProperty(URI_KEY);
		String extension = properties.getProperty(EXTENSION_KEY);
		String ePackageClassName = properties.getProperty(PACKAGE_KEY);
		String resourceFactoryClassName = properties.getProperty(RESOURCE_FACTORY_KEY);

		List<I> jarEntries = new ArrayList<>();
		for (I content : resourceCenter.getContents(serializationArtefact)) {
			if (resourceCenter.retrieveName(content).endsWith(".jar")) {
				jarEntries.add(content);
			}
		}

		if (uri != null && extension != null && ePackageClassName != null && resourceFactoryClassName != null && jarEntries.size() > 0) {
			System.out.println("Found valid EMF metamodel: " + serializationArtefact);
		}

		return (uri != null && extension != null && ePackageClassName != null && resourceFactoryClassName != null && jarEntries.size() > 0);

	}

	@Override
	protected <I> EMFMetaModelResource registerResource(EMFMetaModelResource resource, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<EMFTechnologyAdapter> technologyContextManager) {
		super.registerResource(resource, resourceCenter, technologyContextManager);

		// Depending on the MM Type, you must do different things

		if (resource.getMetaModelType() == EMFMetaModelType.Profile) {
			((EMFTechnologyContextManager) technologyContextManager).registerProfile(resource);
		}
		else {
			((EMFTechnologyContextManager) technologyContextManager).registerMetaModel(resource);
		}

		// Register the resource in the EMFMetaModelRepository of supplied resource center
		if (resourceCenter != null) {
			registerResourceInResourceRepository(resource,
					technologyContextManager.getTechnologyAdapter().getEMFMetaModelRepository(resourceCenter));
		}

		return resource;
	}

	public <I> EMFMetaModelResource retrieveResourceFromClassPath(String metaModelName, String metaModelURI, String metaModelExtension,
			String pkgClassName, String factoryClassName, TechnologyContextManager<EMFTechnologyAdapter> technologyContextManager)
					throws ModelDefinitionException {

		FlexoResourceCenter<I> resourceCenter = null;

		EMFMetaModelResource returned = newInstance(EMFMetaModelResource.class);
		returned.setMetaModelType(EMFMetaModelType.Standard);

		returned.setResourceCenter(resourceCenter);
		returned.initName(metaModelName);
		returned.setURI(metaModelURI);

		returned.setModelFileExtension(metaModelExtension);
		returned.setPackageClassName(pkgClassName);
		returned.setResourceFactoryClassName(factoryClassName);

		ClassLoaderIODelegate ioDelegate = newInstance(ClassLoaderIODelegate.class);
		ioDelegate.setSerializationArtefact(getClass().getClassLoader());

		returned.setFlexoIODelegate(ioDelegate);

		registerResource(returned, resourceCenter, technologyContextManager);
		return returned;
	}

	@Override
	protected <I> EMFMetaModelResource initResourceForRetrieving(I serializationArtefact, FlexoResourceCenter<I> resourceCenter,
			TechnologyContextManager<EMFTechnologyAdapter> technologyContextManager) throws ModelDefinitionException, IOException {
		EMFMetaModelResource returned = null;

		Properties properties = resourceCenter.getProperties(serializationArtefact);

		String uri = properties.getProperty(URI_KEY);
		String extension = properties.getProperty(EXTENSION_KEY);
		String ePackageClassName = properties.getProperty(PACKAGE_KEY);
		String resourceFactoryClassName = properties.getProperty(RESOURCE_FACTORY_KEY);

		String mmType = properties.getProperty(PROPERTY_TYPE);

		if (mmType != null && mmType.equals(TYPE_XTEXT)) {
			returned = newInstance(XtextEMFMetaModelResource.class);
			returned.setMetaModelType(EMFMetaModelType.XText);
			((XtextEMFMetaModelResource) returned).setStandaloneSetupClassName(properties.getProperty(PROPERTY_XTEXT_STANDALONE_SETUP));
		}
		else if (mmType != null && mmType.equals(TYPE_PROFILE)) {
			returned = newInstance(EMFMetaModelResource.class);
			returned.setMetaModelType(EMFMetaModelType.Profile);
		}
		else /*if (mmType != null && mmType.equals(TYPE_METAMODEL))*/ {
			returned = newInstance(EMFMetaModelResource.class);
			returned.setMetaModelType(EMFMetaModelType.Standard);
		}

		returned.setResourceCenter(resourceCenter);
		returned.initName(resourceCenter.retrieveName(serializationArtefact));
		returned.setURI(uri);

		returned.setModelFileExtension(extension);
		returned.setPackageClassName(ePackageClassName);
		returned.setResourceFactoryClassName(resourceFactoryClassName);

		returned.setFlexoIODelegate(makeFlexoIODelegate(serializationArtefact, resourceCenter));

		return returned;
	}

	@Override
	protected <I> FlexoIODelegate<I> makeFlexoIODelegate(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {

		/*if (serializationArtefact instanceof File) {
			return (FlexoIODelegate<I>) MMFromJarsInDirIODelegateImpl.makeMMFromJarsInDirIODelegate((File) serializationArtefact, this);
		}
		return super.makeFlexoIODelegate(serializationArtefact, resourceCenter);*/

		return resourceCenter.makeDirectoryBasedFlexoIODelegate(serializationArtefact, "", PROPERTIES_SUFFIX, this);
	}

}
