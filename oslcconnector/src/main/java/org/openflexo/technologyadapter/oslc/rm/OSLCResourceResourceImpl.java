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

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileIODelegate.FileIODelegateImpl;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;
import org.openflexo.technologyadapter.oslc.model.io.FlexoOslcAdaptorConfiguration;
import org.openflexo.technologyadapter.oslc.model.io.OSLCModelConverter;

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

	@Override
	public OSLCServiceProviderCatalog loadResourceData() throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException {
		// Unused AbstractResource unit = null;

		if (getIODelegate().exists()) {
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
	public void save() throws SaveResourceException {
		try {
			resourceData = getResourceData();
		} catch (FileNotFoundException e) {
			// Unused OSLCResourceResource resourceData;
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} catch (FlexoException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		}
		OSLCResource resourceData = null;

		if (!getIODelegate().hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Permission denied : " + ((File) getIODelegate().getSerializationArtefact()).getAbsolutePath());
			}
			throw new SaveResourcePermissionDeniedException(getIODelegate());
		}
		if (resourceData != null) {
			FileWritingLock lock = ((FileIODelegateImpl) getIODelegate()).willWriteOnDisk();
			writeToFile();
			((FileIODelegateImpl) getIODelegate()).hasWrittenOnDisk(lock);
			notifyResourceStatusChanged();
			// resourceData.clearIsModified(false);
			if (logger.isLoggable(Level.INFO)) {
				logger.info(
						"Succeeding to save Resource " + getURI() + " : " + ((File) getIODelegate().getSerializationArtefact()).getName());
			}
		}
	}

	private void writeToFile() throws SaveResourceException {
		try (FileOutputStream out = new FileOutputStream(((File) getIODelegate().getSerializationArtefact()))) {
			// Unused StreamResult result =
			new StreamResult(out);
			TransformerFactory factory = TransformerFactory
					.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);

			// Unused Transformer transformer =
			factory.newTransformer();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Wrote " + (getIODelegate().getSerializationArtefact()));
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
		File providerFile = (File) getIODelegate().getSerializationArtefact();
		Properties properties = new Properties();
		try (FileInputStream input = new FileInputStream(providerFile)) {
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
