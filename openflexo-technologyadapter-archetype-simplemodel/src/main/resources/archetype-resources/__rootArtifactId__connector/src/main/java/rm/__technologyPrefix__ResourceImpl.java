#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoFileResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import ${package}.${technologyPrefix}TechnologyContextManager;
import ${package}.model.${technologyPrefix}Model;
import org.openflexo.toolbox.IProgress;

public abstract class ${technologyPrefix}ResourceImpl extends FlexoFileResourceImpl<${technologyPrefix}Model> implements ${technologyPrefix}Resource {
    
    private static final Logger LOGGER = Logger.getLogger(${technologyPrefix}ResourceImpl.class.getPackage().getName());

    public static ${technologyPrefix}Resource make${technologyPrefix}Resource(String modelURI, File modelFile,
            ${technologyPrefix}TechnologyContextManager technologyContextManager) {
        try {
            ModelFactory factory = new ModelFactory(${technologyPrefix}Resource.class);
            ${technologyPrefix}ResourceImpl returned = (${technologyPrefix}ResourceImpl) factory.newInstance(${technologyPrefix}Resource.class);
            returned.setName(modelFile.getName());
            returned.setFile(modelFile);
            returned.setURI(modelURI);
            returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
            returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
            returned.setTechnologyContextManager(technologyContextManager);
            technologyContextManager.registerResource(returned);

            return returned;
        } catch (ModelDefinitionException e) {
            final String msg = "Error while initializing ${technologyPrefix} model resource";
            LOGGER.log(Level.SEVERE, msg, e);
        }
        return null;
    }

    public static ${technologyPrefix}Resource retrieve${technologyPrefix}Resource(File modelFile, ${technologyPrefix}TechnologyContextManager technologyContextManager) {
        try {
            ModelFactory factory = new ModelFactory(${technologyPrefix}Resource.class);
            ${technologyPrefix}ResourceImpl returned = (${technologyPrefix}ResourceImpl) factory.newInstance(${technologyPrefix}Resource.class);
            returned.setName(modelFile.getName());
            returned.setFile(modelFile);
            returned.setURI(modelFile.toURI().toString());
            returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
            returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
            returned.setTechnologyContextManager(technologyContextManager);
            technologyContextManager.registerResource(returned);
            return returned;
        } catch (ModelDefinitionException e) {
            final String msg = "Error while initializing ${technologyPrefix} model resource";
            LOGGER.log(Level.SEVERE, msg, e);
        }
        return null;
    }

    @Override
    public ${technologyPrefix}Model loadResourceData(IProgress progress) throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException {
        // TODO: Auto-generated Method
        return null;
    }

    @Override
    public void save(IProgress progress) throws SaveResourceException {
        ${technologyPrefix}Model resourceData = null;
        try {
            resourceData = getResourceData(progress);
        } catch (FileNotFoundException e) {
            final String msg = "Error while saving ${technologyPrefix} model resource";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new SaveResourceException(this);
        } catch (ResourceLoadingCancelledException e) {
            final String msg = "Error while saving ${technologyPrefix} model resource";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new SaveResourceException(this);
        } catch (FlexoException e) {
            final String msg = "Error while saving ${technologyPrefix} model resource";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new SaveResourceException(this);
        }

        if (!hasWritePermission()) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("Permission denied : " + getFile().getAbsolutePath());
            }
            throw new SaveResourcePermissionDeniedException(this);
        }
        if (resourceData != null) {
            FlexoFileResourceImpl.FileWritingLock lock = willWriteOnDisk();
            writeToFile();
            hasWrittenOnDisk(lock);
            notifyResourceStatusChanged();
            resourceData.clearIsModified(false);
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("Succeeding to save Resource " + getURI() + " : " + getFile().getName());
            }
        }
    }

    private void writeToFile() throws SaveResourceException {
        //TODO : Auto-generated method skeletton.
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(getFile());
        } catch (FileNotFoundException e) {
            final String msg = "Error while saving ${technologyPrefix} model resource";
            LOGGER.log(Level.SEVERE, msg, e);
            throw new SaveResourceException(this);
        } finally {
            IOUtils.closeQuietly(out);
        }

        LOGGER.info("Wrote " + getFile());
    }

    @Override
    public Class<${technologyPrefix}Model> getResourceDataClass() {
        return ${technologyPrefix}Model.class;
    }
}
