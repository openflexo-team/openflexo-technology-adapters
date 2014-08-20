/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2012-2013 Openflexo
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
import org.openflexo.foundation.resource.FlexoFileResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
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
public abstract class XMLFileResourceImpl extends FlexoFileResourceImpl<XMLModel> implements XMLFileResource {

	protected static final Logger logger   = Logger.getLogger(XMLFileResourceImpl.class.getPackage().getName());
	protected static XMLRootElementReader REreader = new XMLRootElementReader();

	// Properties

	private boolean               isLoaded = false;

	/**
	 * 
	 * @param modelURI
	 * @param xmlFile
	 * @param technologyContextManager
	 * @return
	 */
	public static XMLFileResource makeXMLFileResource(File xmlFile, XMLTechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(XMLFileResource.class);
			XMLFileResourceImpl returned = (XMLFileResourceImpl) factory.newInstance(XMLFileResource.class);
			returned.setName(xmlFile.getName());
			returned.setFile(xmlFile);
			returned.setURI(xmlFile.toURI().toString());
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);

			technologyContextManager.registerResource(returned);
			
			if (!xmlFile.exists()) {

				if (returned.resourceData == null) {
					returned.resourceData = XMLModelImpl.getModelFactory().newInstance(XMLModel.class);
					returned.resourceData.setResource(returned);
				}
				
				returned.save(null);
				returned.isLoaded = true;
			}
			else {
				// TODO: File exists, what should I Do
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
				throw new SaveResourceException(this);
			}
		}

		if (!hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Permission denied : " + getFile().getAbsolutePath());
			}
			throw new SaveResourcePermissionDeniedException(this);
		}

		if (resourceData != null) {
			FileWritingLock lock = willWriteOnDisk();
			writeToFile();
			hasWrittenOnDisk(lock);
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
	 * Retrieves the target Namespace from the file when not loaded
	 * or from MetamModel when it is loaded and exists
	 * 
	 */
	@Override
	public String getTargetNamespace() {
		
		if (!isLoaded()){
			XMLRootElementInfo rootInfo;
			try {
				rootInfo = REreader.readRootElement(this.getFile());
			} catch (IOException e) {
				logger.warning("Unable to read Root Node from File" + e.getMessage());
				return null;
			}			
			return  rootInfo.getURI();
		}
		else{
			return this.getModel().getMetaModel().getURI();
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
			throw new SaveResourceException(this);
		} finally {
			IOUtils.closeQuietly(out);
		}
		logger.info("Wrote " + getFile());
	}

	@Override
	public XMLModel loadResourceData(IProgress progress) throws ResourceLoadingCancelledException, FileNotFoundException,

	FlexoException {

		if (resourceData == null) {
			resourceData =  XMLModelImpl.getModelFactory().newInstance(XMLModel.class);
			resourceData.setResource(this);
			
			attachMetamodel();
			
		}

		if (!isLoaded()) {

			try {

				FlexoMetaModelResource<XMLModel, XMLMetaModel, XMLTechnologyAdapter> mmRes = ((XMLFileResource) resourceData.getResource()).getMetaModelResource();
				if (resourceData.getMetaModel() == null && mmRes != null){
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
			resourceData =  XMLModelImpl.getModelFactory().newInstance(XMLModel.class);
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
	public void attachMetamodel(){
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

	// Lifecycle Management

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}

}
