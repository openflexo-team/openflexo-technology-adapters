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

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileFlexoIODelegate.FileFlexoIODelegateImpl;
import org.apache.commons.io.IOUtils;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;

import ${package}.${technologyPrefix}TechnologyAdapter;
import ${package}.${technologyPrefix}TechnologyContextManager;
import ${package}.model.${technologyPrefix}Model;

import org.openflexo.toolbox.IProgress;

public abstract class ${technologyPrefix}ModelResourceImpl extends FlexoResourceImpl<${technologyPrefix}Model> implements ${technologyPrefix}ModelResource {
	private static final Logger logger = Logger.getLogger(${technologyPrefix}ModelResourceImpl.class.getPackage().getName());

	public static ${technologyPrefix}ModelResource make${technologyPrefix}ModelResource(String modelURI, File modelFile,
			${technologyPrefix}TechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(${technologyPrefix}ModelResource.class);
			${technologyPrefix}ModelResourceImpl returned = (${technologyPrefix}ModelResourceImpl) factory.newInstance(${technologyPrefix}ModelResource.class);
			returned.setName(modelFile.getName());
			returned.setFile(modelFile);
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

	public static ${technologyPrefix}ModelResource retrieve${technologyPrefix}ModelResource(File modelFile, ${technologyPrefix}TechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(${technologyPrefix}ModelResource.class);
			${technologyPrefix}ModelResourceImpl returned = (${technologyPrefix}ModelResourceImpl) factory.newInstance(${technologyPrefix}ModelResource.class);
			returned.setName(modelFile.getName());
			returned.setFile(modelFile);
			returned.setURI(modelFile.toURI().toString());
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
	public ${technologyPrefix}Model loadResourceData(IProgress progress) throws ResourceLoadingCancelledException,
			FileNotFoundException, FlexoException {
		${technologyPrefix}Model returned = new ${technologyPrefix}Model(getURI(), getFile(), (${technologyPrefix}TechnologyAdapter) getTechnologyAdapter());
		returned.loadWhenUnloaded();
		return returned;
	}

	@Override
	public void save(IProgress progress) throws SaveResourceException {
		try {
			resourceData = getResourceData(progress);
		} catch (FileNotFoundException e) {
			${technologyPrefix}Model resourceData;
			e.printStackTrace();
			throw new SaveResourceException(this);
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			throw new SaveResourceException(this);
		} catch (FlexoException e) {
			e.printStackTrace();
			throw new SaveResourceException(this);
		}
		${technologyPrefix}Model resourceData = null;

		if (!hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Permission denied : " + getFile().getAbsolutePath());
			}
			throw new SaveResourcePermissionDeniedException(this);
		}
		if (resourceData != null) {
			FileWritingLock lock = getFlexoIODelegate().willWriteOnDisk();
			writeToFile();
			hasWrittenOnDisk(lock);
			notifyResourceStatusChanged();
			resourceData.clearIsModified(false);
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Succeeding to save Resource " + getURI() + " : " + getFile().getName());
			}
		}
	}

	@Override
	public ${technologyPrefix}Model getModelData() {
		try {
			return getResourceData(null);
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (FlexoException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ${technologyPrefix}Model getModel() {
		return getModelData();
	}

	private void writeToFile() throws SaveResourceException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(getFile());
			StreamResult result = new StreamResult(out);
			TransformerFactory factory = TransformerFactory.newInstance(
					"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);

			Transformer transformer = factory.newTransformer();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SaveResourceException(this);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			throw new SaveResourceException(this);
		} finally {
			IOUtils.closeQuietly(out);
		}

		logger.info("Wrote " + getFile());
	}

	@Override
	public Class<${technologyPrefix}Model> getResourceDataClass() {
		return ${technologyPrefix}Model.class;
	}
}
