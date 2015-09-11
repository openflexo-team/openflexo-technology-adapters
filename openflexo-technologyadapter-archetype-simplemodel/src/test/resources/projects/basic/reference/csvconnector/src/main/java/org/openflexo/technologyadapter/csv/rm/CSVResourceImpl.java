/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technologyadapter-archetype-simplemodel, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.csv.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.csv.CSVTechnologyAdapter;
import org.openflexo.technologyadapter.csv.CSVTechnologyContextManager;
import org.openflexo.technologyadapter.csv.model.CSVModel;
import org.openflexo.technologyadapter.csv.model.CSVModelImpl;
import org.openflexo.toolbox.IProgress;

public abstract class CSVResourceImpl extends FlexoResourceImpl<CSVModel>implements CSVResource {

	private static final Logger LOGGER = Logger.getLogger(CSVResourceImpl.class.getPackage().getName());

	private static ModelFactory MODEL_FACTORY;

	static {
		try {
			MODEL_FACTORY = new ModelFactory(CSVModel.class);
		} catch (final ModelDefinitionException e) {
			final String msg = "Error while initializing CSV model resource";
			LOGGER.log(Level.SEVERE, msg, e);
		}
	}

	public static CSVResource makeCSVResource(String modelURI, File modelFile, CSVTechnologyContextManager technologyContextManager,
			FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(CSVResource.class, FileFlexoIODelegate.class));
			CSVResourceImpl returned = (CSVResourceImpl) factory.newInstance(CSVResource.class);
			returned.initName(modelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));

			returned.setURI(modelURI);
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);

			return returned;
		} catch (ModelDefinitionException e) {
			final String msg = "Error while initializing CSV model resource";
			LOGGER.log(Level.SEVERE, msg, e);
		}
		return null;
	}

	public static CSVResource retrieveCSVResource(File modelFile, CSVTechnologyContextManager technologyContextManager,
			FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(CSVResource.class, FileFlexoIODelegate.class));
			CSVResourceImpl returned = (CSVResourceImpl) factory.newInstance(CSVResource.class);
			returned.initName(modelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
			returned.setURI(modelFile.toURI().toString());
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			final String msg = "Error while initializing CSV model resource";
			LOGGER.log(Level.SEVERE, msg, e);
		}
		return null;
	}

	@Override
	public CSVTechnologyAdapter getTechnologyAdapter() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(CSVTechnologyAdapter.class);
		}
		return null;
	}

	@Override
	public CSVModel loadResourceData(IProgress progress) throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException {
		// TODO: Auto-generated Method
		final CSVModelImpl csvObject = (CSVModelImpl) MODEL_FACTORY.newInstance(CSVModel.class);
		csvObject.setTechnologyAdapter(getTechnologyAdapter());
		csvObject.setResource(this);
		// Now you have to add here your parsing and call the correct set.
		return csvObject;
	}

	@Override
	public void save(IProgress progress) throws SaveResourceException {
		CSVModel resourceData = null;
		try {
			resourceData = getResourceData(progress);
		} catch (FileNotFoundException e) {
			final String msg = "Error while saving CSV model resource";
			LOGGER.log(Level.SEVERE, msg, e);
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (ResourceLoadingCancelledException e) {
			final String msg = "Error while saving CSV model resource";
			LOGGER.log(Level.SEVERE, msg, e);
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (FlexoException e) {
			final String msg = "Error while saving CSV model resource";
			LOGGER.log(Level.SEVERE, msg, e);
			throw new SaveResourceException(getFlexoIODelegate());
		}

		if (!getFlexoIODelegate().hasWritePermission()) {
			if (LOGGER.isLoggable(Level.WARNING)) {
				LOGGER.warning("Permission denied : " + getFlexoIODelegate().toString());
			}
			throw new SaveResourcePermissionDeniedException(getFlexoIODelegate());
		}
		if (resourceData != null) {
			FileWritingLock lock = getFlexoIODelegate().willWriteOnDisk();
			writeToFile();
			getFlexoIODelegate().hasWrittenOnDisk(lock);
			notifyResourceStatusChanged();
			resourceData.clearIsModified(false);
			if (LOGGER.isLoggable(Level.INFO)) {
				LOGGER.info("Succeeding to save Resource " + getURI() + " : " + getFlexoIODelegate().toString());
			}
		}
	}

	private void writeToFile() throws SaveResourceException {
		// TODO : Auto-generated method skeleton.
		FileOutputStream out = null;
		try {
			FileFlexoIODelegate delegate = (FileFlexoIODelegate) getFlexoIODelegate();
			out = new FileOutputStream(delegate.getFile());
		} catch (FileNotFoundException e) {
			final String msg = "Error while saving CSV model resource";
			LOGGER.log(Level.SEVERE, msg, e);
			throw new SaveResourceException(getFlexoIODelegate());
		} finally {
			IOUtils.closeQuietly(out);
		}

		LOGGER.info("Wrote " + getFlexoIODelegate().toString());
	}

	@Override
	public Class<CSVModel> getResourceDataClass() {
		return CSVModel.class;
	}
}
