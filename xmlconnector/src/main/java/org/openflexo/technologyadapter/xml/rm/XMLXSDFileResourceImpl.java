/*
 * (c) Copyright 2013-2014 Openflexo
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
import org.openflexo.technologyadapter.xml.model.XMLXSDModel;
import org.openflexo.technologyadapter.xml.model.XMLXSDModelFactory;
import org.openflexo.toolbox.IProgress;

/**
 * @author xtof
 * 
 */

// TODO : there is code to refactor to be able to merge XMLFileResourceImpl &
// XMLXSDFileResourceImpl

public abstract class XMLXSDFileResourceImpl extends FlexoFileResourceImpl<XMLXSDModel> implements XMLFileResource {

    // Constants

    static final String           CDATA_TYPE_NAME   = "CDATA";

    protected static final Logger logger            = Logger.getLogger(XMLXSDFileResourceImpl.class.getPackage().getName());

    private XSDMetaModelResource  metamodelResource = null;

    // Properties

    private boolean               isLoaded          = false;

    /**
     * 
     * @param modelURI
     * @param xmlFile
     * @param metaModelResource
     * @param technologyContextManager
     * @return
     */
    public static XMLXSDFileResource makeXMLXSDFileResource(String modelUri, File xmlFile, XSDMetaModelResource metaModelResource,
            XMLTechnologyContextManager technologyContextManager) {

        try {
            ModelFactory factory = new ModelFactory(XMLXSDFileResource.class);
            XMLXSDFileResourceImpl returned = (XMLXSDFileResourceImpl) factory.newInstance(XMLXSDFileResource.class);
            returned.setName(xmlFile.getName());
            returned.setFile(xmlFile);
            returned.setURI(xmlFile.toURI().toString());
            returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
            returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
            returned.setTechnologyContextManager(technologyContextManager);
            returned.setResourceData(new XMLXSDModel(modelUri, xmlFile, technologyContextManager.getTechnologyAdapter()));
            returned.getModel().setResource(returned);
            returned.setMetaModelResource(metaModelResource);

            technologyContextManager.registerResource(returned);

            if (!xmlFile.exists()) {
                returned.save(null);
                returned.isLoaded = true;
            }
            return returned;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.openflexo.foundation.resource.FlexoResource#save(org.openflexo.toolbox.IProgress)
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
            resourceData.clearIsModified(false);
            if (logger.isLoggable(Level.INFO)) {
                logger.info("Succeeding to save Resource " + getURI() + " : " + getFile().getName());
            }
        }
        else
            logger.warning("Model is Empty!");

    }

    private void writeToFile() throws SaveResourceException {

        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(getFile()), "UTF-8");
            XMLWriter<XMLXSDFileResource, XMLXSDModel> writer = new XMLWriter<XMLXSDFileResource, XMLXSDModel>(this, out);

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
    public XMLXSDModel getModel() {
        return resourceData;
    }

    @Override
    public XMLXSDModel getModelData() {
        if (!isLoaded()) {
            try {
                resourceData = loadResourceData(null);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ResourceLoadingCancelledException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FlexoException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resourceData;
    }

    /**
     * URI here is the full path to the file
     */
    @Override
    public String getURI() {
        return this.getFile().toURI().toString();
    }

    @Override
    public XMLXSDModel loadResourceData(IProgress progress) throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException {

        if (!isLoaded()) {

            try {

                XMLXSDModelFactory factory = this.getTechnologyAdapter().getXMLXSDModelFactory();

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

    @Override
    public FlexoMetaModelResource<XMLXSDModel, XMLMetaModel, XMLTechnologyAdapter> getMetaModelResource() {
        return metamodelResource;
    }

    /* (non-Javadoc)
     * @see org.openflexo.foundation.technologyadapter.FlexoModelResource#setMetaModelResource(org.openflexo.foundation.technologyadapter.FlexoMetaModelResource)
     */
    @Override
    public void setMetaModelResource(FlexoMetaModelResource<XMLXSDModel, XMLMetaModel, XMLTechnologyAdapter> mmRes) {

        metamodelResource = (XSDMetaModelResource) mmRes;

        this.getModel().setMetaModel(mmRes.getMetaModelData());

    }

    @Override
    public Class<XMLXSDModel> getResourceDataClass() {
        return XMLXSDModel.class;
    }

    // Lifecycle Management

    @Override
    public boolean isLoaded() {
        return isLoaded;
    }

}
