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

package org.openflexo.technologyadapter.oslc.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileFlexoIODelegate.FileFlexoIODelegateImpl;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.oslc.OSLCTechnologyContextManager;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.io.OSLCCoreModelConverter;
import org.openflexo.technologyadapter.oslc.model.io.OSLCResourceClient;
import org.openflexo.toolbox.FileUtils;
import org.openflexo.toolbox.IProgress;

public abstract class OSLCResourceResourceImpl extends FlexoResourceImpl<OSLCResource> implements OSLCResourceResource {
	private static final Logger logger = Logger.getLogger(OSLCResourceResourceImpl.class.getPackage().getName());

	private OSLCCoreModelConverter converter;

	@Override
	public OSLCCoreModelConverter getConverter() {
		return converter;
	}

	public void setConverter(OSLCCoreModelConverter converter) {
		this.converter = converter;
	}

	public static OSLCResourceResource makeOSLCResource(String modelURI, File modelFile,
			OSLCTechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class,
					OSLCResourceResource.class));
			OSLCResourceResourceImpl returned = (OSLCResourceResourceImpl) factory.newInstance(OSLCResourceResource.class);
			FileFlexoIODelegate fileIODelegate = factory.newInstance(FileFlexoIODelegate.class);
			returned.setFlexoIODelegate(fileIODelegate);
			fileIODelegate.setFile(modelFile);
			returned.setName(modelFile.getName());
			returned.setURI(modelURI);
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

	public static OSLCResourceResource retrieveOSLCResource(File cdlFile, OSLCTechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class,
					OSLCResourceResource.class));
			OSLCResourceResourceImpl returned = (OSLCResourceResourceImpl) factory.newInstance(OSLCResourceResource.class);
			FileFlexoIODelegate fileIODelegate = factory.newInstance(FileFlexoIODelegate.class);
			returned.setFlexoIODelegate(fileIODelegate);
			fileIODelegate.setFile(cdlFile);
			returned.setName(cdlFile.getName());
			returned.setURI(cdlFile.toURI().toString());
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

	@Override
	public OSLCResource loadResourceData(IProgress progress) throws ResourceLoadingCancelledException, FileNotFoundException,
			FlexoException {
		AbstractResource unit = null;

		if (getFlexoIODelegate().exists()) {
			try {
				// Retrieve the URL of the provider from a configuration file
				OSLCResourceClient client = createClientFromProviderURL();

				if (converter == null) {
					converter = new OSLCCoreModelConverter(client);
					converter.setTechnologyAdapter(getTechnologyAdapter());
				}
				resourceData = converter.convertAllCoreResources();
				resourceData.setResource(this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		setResourceData(resourceData);
		return resourceData;
	}

	@Override
	public void save(IProgress progress) throws SaveResourceException {
		try {
			resourceData = getResourceData(progress);
		} catch (FileNotFoundException e) {
			OSLCResourceResource resourceData;
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (FlexoException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		}
		OSLCResource resourceData = null;

		if (!getFlexoIODelegate().hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Permission denied : " + ((File) getFlexoIODelegate().getSerializationArtefact()).getAbsolutePath());
			}
			throw new SaveResourcePermissionDeniedException(getFlexoIODelegate());
		}
		if (resourceData != null) {
			FileWritingLock lock = ((FileFlexoIODelegateImpl) getFlexoIODelegate()).willWriteOnDisk();
			writeToFile();
			((FileFlexoIODelegateImpl) getFlexoIODelegate()).hasWrittenOnDisk(lock);
			notifyResourceStatusChanged();
			resourceData.clearIsModified(false);
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Succeeding to save Resource " + getURI() + " : "
						+ ((File) getFlexoIODelegate().getSerializationArtefact()).getName());
			}
		}
	}

	private void writeToFile() throws SaveResourceException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(((File) getFlexoIODelegate().getSerializationArtefact()));
			StreamResult result = new StreamResult(out);
			TransformerFactory factory = TransformerFactory.newInstance(
					"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);

			Transformer transformer = factory.newTransformer();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} finally {
			IOUtils.closeQuietly(out);
		}

		logger.info("Wrote " + (getFlexoIODelegate().getSerializationArtefact()));
	}

	@Override
	public Class<OSLCResource> getResourceDataClass() {
		return OSLCResource.class;
	}

	private OSLCResourceClient createClientFromProviderURL() {
		OSLCResourceClient client = null;
		try {
			File providerFile = (File) getFlexoIODelegate().getSerializationArtefact();
			String provider = FileUtils.fileContents(providerFile);

			provider = provider.replaceAll("(\\r|\\n)", "");
			client = new OSLCResourceClient(provider);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return client;
	}
}
