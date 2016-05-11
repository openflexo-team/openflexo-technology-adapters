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

package org.openflexo.technologyadapter.gina.rm;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.IOFlexoException;
import org.openflexo.foundation.InconsistentDataException;
import org.openflexo.foundation.InvalidModelDefinitionException;
import org.openflexo.foundation.InvalidXMLException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileFlexoIODelegate.FileFlexoIODelegateImpl;
import org.openflexo.foundation.resource.FlexoFileNotFoundException;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.GINATechnologyContextManager;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.technologyadapter.gina.model.GINAFactory;
import org.openflexo.toolbox.IProgress;

public abstract class GINAFIBComponentResourceImpl extends PamelaResourceImpl<GINAFIBComponent, GINAFactory>
		implements GINAFIBComponentResource {

	private static final Logger LOGGER = Logger.getLogger(GINAFIBComponentResourceImpl.class.getPackage().getName());

	public static GINAFIBComponentResource makeComponentResource(File componentFile,
			GINATechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(GINAFIBComponentResource.class, FileFlexoIODelegate.class));
			GINAFIBComponentResourceImpl returned = (GINAFIBComponentResourceImpl) factory.newInstance(GINAFIBComponentResource.class);
			returned.initName(componentFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(componentFile, factory));
			GINAFactory ginaFactory = new GINAFactory(returned, technologyContextManager.getServiceManager());
			returned.setFactory(ginaFactory);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);

			return returned;
		} catch (ModelDefinitionException e) {
			final String msg = "Error while initializing GINAFIBComponentResource";
			LOGGER.log(Level.SEVERE, msg, e);
		}
		return null;
	}

	public static GINAFIBComponentResource retrieveComponentResource(File componentFile,
			GINATechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(GINAFIBComponentResource.class, FileFlexoIODelegate.class));
			GINAFIBComponentResourceImpl returned = (GINAFIBComponentResourceImpl) factory.newInstance(GINAFIBComponentResource.class);
			returned.initName(componentFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(componentFile, factory));
			GINAFactory ginaFactory = new GINAFactory(returned, technologyContextManager.getServiceManager());
			returned.setFactory(ginaFactory);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			final String msg = "Error while initializing GIN model resource";
			LOGGER.log(Level.SEVERE, msg, e);
		}
		return null;
	}

	@Override
	public GINATechnologyAdapter getTechnologyAdapter() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(GINATechnologyAdapter.class);
		}
		return null;
	}

	@Override
	public GINAFIBComponent loadResourceData(IProgress progress) throws FlexoFileNotFoundException, IOFlexoException, InvalidXMLException,
			InconsistentDataException, InvalidModelDefinitionException {
		System.out.println("Hop, je charge mon composant FIB");
		GINAFIBComponent returned = super.loadResourceData(progress);
		System.out.println("J'obtiens: " + returned);
		System.out.println("Resource=" + returned.getResource());
		return returned;
	}

	/*@Override
	public GINAFIBComponentImpl loadResourceData(IProgress progress)
			throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException {
		// TODO: Auto-generated Method
		final GINAFIBComponentImpl ginaconnectorObject = (GINAFIBComponentImpl) MODEL_FACTORY.newInstance(GINAFIBComponent.class);
		ginaconnectorObject.setTechnologyAdapter(getTechnologyAdapter());
		ginaconnectorObject.setResource(this);
		// Now you have to add here your parsing and call the correct set.
		return ginaconnectorObject;
	}*/

	/*@Override
	public void save(IProgress progress) throws SaveResourceException {
		GINModel resourceData = null;
		try {
			resourceData = getResourceData(progress);
		} catch (FileNotFoundException e) {
			final String msg = "Error while saving GIN model resource";
			LOGGER.log(Level.SEVERE, msg, e);
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (ResourceLoadingCancelledException e) {
			final String msg = "Error while saving GIN model resource";
			LOGGER.log(Level.SEVERE, msg, e);
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (FlexoException e) {
			final String msg = "Error while saving GIN model resource";
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
	}*/

	/*private void writeToFile() throws SaveResourceException {
		// TODO : Auto-generated method skeleton.
		FileOutputStream out = null;
		try {
			FileFlexoIODelegate delegate = (FileFlexoIODelegate) getFlexoIODelegate();
			out = new FileOutputStream(delegate.getFile());
		} catch (FileNotFoundException e) {
			final String msg = "Error while saving GIN model resource";
			LOGGER.log(Level.SEVERE, msg, e);
			throw new SaveResourceException(getFlexoIODelegate());
		} finally {
			IOUtils.closeQuietly(out);
		}
	
		LOGGER.info("Wrote " + getFlexoIODelegate().toString());
	}*/

	@Override
	public Class<GINAFIBComponent> getResourceDataClass() {
		return GINAFIBComponent.class;
	}

}
