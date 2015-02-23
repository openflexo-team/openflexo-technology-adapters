/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Oslcconnector, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.oslc.rm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
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
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;
import org.openflexo.technologyadapter.oslc.model.io.FlexoOslcAdaptorConfiguration;
import org.openflexo.technologyadapter.oslc.model.io.OSLCModelConverter;
import org.openflexo.toolbox.IProgress;

public abstract class OSLCResourceResourceImpl extends FlexoResourceImpl<OSLCServiceProviderCatalog> implements OSLCResourceResource {
	private static final Logger logger = Logger.getLogger(OSLCResourceResourceImpl.class.getPackage().getName());

	private OSLCModelConverter converter;

	private FlexoOslcAdaptorConfiguration adaptorConfiguration;

	@Override
	public OSLCModelConverter getConverter() {
		return converter;
	}

	public void setConverter(OSLCModelConverter converter) {
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
	public OSLCServiceProviderCatalog loadResourceData(IProgress progress) throws ResourceLoadingCancelledException, FileNotFoundException,
			FlexoException {
		AbstractResource unit = null;

		if (getFlexoIODelegate().exists()) {
			try {
				// Retrieve the configuration from a configuration file
				adaptorConfiguration = loadAdaptorConfiguration();

				if (converter == null) {
					converter = new OSLCModelConverter(adaptorConfiguration);
					converter.setTechnologyAdapter(getTechnologyAdapter());
				}
				resourceData = converter.convertAllCoreResourcesFromCatalog();
				if (resourceData != null) {
					resourceData.setResource(this);
				}
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
			// resourceData.clearIsModified(false);
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
	public Class<OSLCServiceProviderCatalog> getResourceDataClass() {
		return OSLCServiceProviderCatalog.class;
	}

	/**
	 * load an oslc file and get the informations
	 * 
	 * @return
	 */
	private FlexoOslcAdaptorConfiguration loadAdaptorConfiguration() {
		FlexoOslcAdaptorConfiguration adaptor = null;
		File providerFile = (File) getFlexoIODelegate().getSerializationArtefact();
		Properties properties = new Properties();
		FileInputStream input = null;
		try {
			input = new FileInputStream(providerFile);
			properties.load(input);
			adaptor = new FlexoOslcAdaptorConfiguration(properties.getProperty("catalogUri"));
			adaptor.setAccessToken(properties.getProperty("accessToken"));
			adaptor.setRequestTokenUrl(properties.getProperty("requestTokenUrl"));
			adaptor.setAuthorizationUrl(properties.getProperty("authorizationUrl"));
			adaptor.setConsumerKey(properties.getProperty("consumerKey"));
			adaptor.setConsumerSecret(properties.getProperty("consumerSecret"));
			adaptor.setAuthURL(properties.getProperty("authURL"));
			adaptor.setPassword(properties.getProperty("password"));
			adaptor.setLogin(properties.getProperty("login"));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return adaptor;
	}

	@Override
	public FlexoOslcAdaptorConfiguration getAdaptorConfiguration() {
		return adaptorConfiguration;
	}

	public void setAdaptorConfiguration(FlexoOslcAdaptorConfiguration adaptorConfiguration) {
		this.adaptorConfiguration = adaptorConfiguration;
	}
}
