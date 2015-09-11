/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.xml.rm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileFlexoIODelegate.FileFlexoIODelegateImpl;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.XMLTechnologyContextManager;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLModelFactory;
import org.openflexo.technologyadapter.xml.model.XMLModelImpl;
import org.openflexo.toolbox.IProgress;
import org.openflexo.xml.XMLRootElementInfo;
import org.openflexo.xml.XMLRootElementReader;

/**
 * @author xtof
 * 
 */
public abstract class XMLFileResourceImpl extends FlexoResourceImpl<XMLModel>implements XMLFileResource {

	protected static final Logger logger = Logger.getLogger(XMLFileResourceImpl.class.getPackage().getName());
	protected static XMLRootElementReader REreader = new XMLRootElementReader();

	// Properties

	private boolean isLoaded = false;

	/**
	 * 
	 * @param modelURI
	 * @param xmlFile
	 * @param technologyContextManager
	 * @return
	 */
	public static XMLFileResource makeXMLFileResource(File xmlFile, XMLTechnologyContextManager technologyContextManager,
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openflexo.foundation.resource.FlexoResource#save(org.openflexo.toolbox
	 * .IProgress)
	 */

	@Override
	public void save(IProgress progress) throws SaveResourceException {

		File myFile = this.getFile();

		if (!myFile.exists()) {
			// Creates a new file
			try {
				myFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new SaveResourceException(getFlexoIODelegate());
			}
		}

		if (!getFlexoIODelegate().hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Permission denied : " + getFile().getAbsolutePath());
			}
			throw new SaveResourcePermissionDeniedException(getFlexoIODelegate());
		}

		if (resourceData != null) {
			FileWritingLock lock = getFlexoIODelegate().willWriteOnDisk();
			writeToFile();
			getFlexoIODelegate().hasWrittenOnDisk(lock);
			notifyResourceStatusChanged();
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Succeeding to save Resource " + getURI() + " : " + getFile().getName());
			}
		}

	}

	/**
	 * URI here is the full path to the file
	 */
	@Override
	public String getURI() {
		if (getFile() != null) {
			return getFile().toURI().toString();
		}
		return "uri_not_found";
	}

	/**
	 * Retrieves the target Namespace from the file when not loaded or from MetamModel when it is loaded and exists
	 * 
	 * @throws IOException
	 * 
	 */
	@Override
	public String getTargetNamespace() throws IOException {

		if (!isLoaded()) {
			XMLRootElementInfo rootInfo;
			rootInfo = REreader.readRootElement(this.getFile());
			return rootInfo.getURI();
		}
		else {
			return this.getModel().getMetaModel().getURI();
		}

	}

	public static final String getTargetNamespace(File f) throws IOException {
		if (f != null && f.exists()) {
			XMLRootElementInfo rootInfo;
			rootInfo = REreader.readRootElement(f);
			return rootInfo.getURI();
		}
		else {
			throw new IOException("File Not Found ");
		}
	}

	private void writeToFile() throws SaveResourceException {

		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(getFile()), "UTF-8");
			XMLWriter<XMLFileResource, XMLModel> writer = new XMLWriter<XMLFileResource, XMLModel>(this, out);

			writer.writeDocument();

		} catch (Exception e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} finally {
			IOUtils.closeQuietly(out);
		}
		logger.info("Wrote " + getFile());
	}

	@Override
	public XMLModel loadResourceData(IProgress progress) throws ResourceLoadingCancelledException, FileNotFoundException,

	FlexoException {

		if (resourceData == null) {
			resourceData = XMLModelImpl.getModelFactory().newInstance(XMLModel.class);
			resourceData.setResource(this);

			attachMetamodel();

		}

		if (!isLoaded()) {

			try {

				FlexoMetaModelResource<XMLModel, XMLMetaModel, XMLTechnologyAdapter> mmRes = ((XMLFileResource) resourceData.getResource())
						.getMetaModelResource();
				if (resourceData.getMetaModel() == null && mmRes != null) {
					resourceData.setMetaModel(mmRes.getMetaModelData());
				}

				XMLModelFactory factory = getTechnologyAdapter().getXMLModelFactory();

				factory.setContext(resourceData);

				factory.deserialize(new FileInputStream(this.getFile()));

				factory.resetContext();

				isLoaded = true;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return resourceData;
	}

	// TODO: Ask Sylvain if this could no be tractable with Pamela => Code duplication ?!?

	@Override
	public XMLModel getModel() {
		return getModelData();
	}

	@Override
	public XMLModel getModelData() {

		if (resourceData == null) {
			resourceData = XMLModelImpl.getModelFactory().newInstance(XMLModel.class);
			resourceData.setResource(this);
		}
		// TODO : check lifecycle for Resource.... should it be loaded on getModelData?
		/*
		if (!isLoaded()) {
			try {
				resourceData = loadResourceData(null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ResourceLoadingCancelledException e) {
				e.printStackTrace();
			} catch (FlexoException e) {
				e.printStackTrace();
			}
		}
		 */
		return resourceData;
	}

	@Override
	public void attachMetamodel() {
		FlexoMetaModelResource<XMLModel, XMLMetaModel, XMLTechnologyAdapter> mmRes = this.getMetaModelResource();
		if (mmRes != null) {
			resourceData.setMetaModel(mmRes.getMetaModelData());
		}
		if (resourceData.getMetaModel() == null) {
			logger.warning("Setting a null Metamodel for Model " + this.getURI());
		}
	}

	@Override
	public Class<XMLModel> getResourceDataClass() {
		return XMLModel.class;

	}

	@Override
	public synchronized XMLModel getResourceData(IProgress progress)
			throws ResourceLoadingCancelledException, ResourceLoadingCancelledException, FileNotFoundException, FlexoException {

		if (isLoading()) {
			logger.warning("trying to load a resource data from itself, please investigate");
			return null;
		}
		if (isLoadable() && !isLoaded()) {
			setLoading(true);
			resourceData = loadResourceData(progress);
			setLoading(false);
			// That's fine, resource is loaded, now let's notify the loading of the resources
			notifyResourceLoaded();
		}
		return resourceData;
	}

	// Lifecycle Management

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}

	@Override
	public FileFlexoIODelegate getFileFlexoIODelegate() {
		return (FileFlexoIODelegate) getFlexoIODelegate();
	}

	private File getFile() {
		return getFileFlexoIODelegate().getFile();
	}
}
