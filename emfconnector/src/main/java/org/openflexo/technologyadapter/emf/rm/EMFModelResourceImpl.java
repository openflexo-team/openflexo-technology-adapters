/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.resource.Resource;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.foundation.resource.FileFlexoIODelegate.FileFlexoIODelegateImpl;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.EMFTechnologyContextManager;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.io.EMFModelConverter;
import org.openflexo.toolbox.IProgress;

/**
 * Represents the resource associated to a {@link OWLOntology}
 * 
 * @author sguerin
 * 
 */
public abstract class EMFModelResourceImpl extends FlexoResourceImpl<EMFModel> implements EMFModelResource {

	private static final Logger logger = Logger.getLogger(EMFModelResourceImpl.class.getPackage().getName());

	/** Model Resource. */
	protected Resource modelResource;

	/**
	 * Creates a new {@link OWLOntologyResource} asserting this is an explicit creation: no file is present on file system<br>
	 * This method should not be used to retrieve the resource from a file in the file system, use
	 * {@link #retrieveOWLOntologyResource(File, OWLOntologyLibrary)} instead
	 * 
	 * @param ontologyURI
	 * @param owlFile
	 * @param ontologyLibrary
	 * @return
	 */
	public static EMFModelResource makeEMFModelResource(String modelURI, File modelFile, EMFMetaModelResource emfMetaModelResource,
			EMFTechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class,EMFModelResource.class));
			EMFModelResourceImpl returned = (EMFModelResourceImpl) factory.newInstance(EMFModelResource.class);
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			returned.setName(modelFile.getName());
			
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));

			//returned.setFile(modelFile);
			// TODO: URI should be defined by the parameter,because its not manageable (FOR NOW)
			returned.setURI(modelFile.toURI().toString());
			returned.setMetaModelResource(emfMetaModelResource);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			technologyContextManager.registerModel(returned);
			// Creates the EMF model from scratch
			EMFModelConverter converter = new EMFModelConverter();
			EMFModel resourceData = converter.convertModel(returned.getMetaModelResource().getMetaModelData(), returned.getEMFResource());
			returned.setResourceData(resourceData);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * URI here is the full path to the file
	 */
	@Override
	public String getURI() {
		return getFileFlexoIODelegate().getFile().toURI().toString();
	}

	/**
	 * Instanciates a new {@link OWLOntologyResource} asserting we are about to built a resource matching an existing file in the file
	 * system<br>
	 * This method should not be used to explicitely build a new ontology
	 * 
	 * @param owlFile
	 * @param ontologyLibrary
	 * @return
	 */
	public static EMFModelResource retrieveEMFModelResource(File modelFile, EMFMetaModelResource emfMetaModelResource,
			EMFTechnologyContextManager technologyContextManager) {
		try {
			ModelFactory factory = new ModelFactory(ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class,EMFModelResource.class));
			EMFModelResourceImpl returned = (EMFModelResourceImpl) factory.newInstance(EMFModelResource.class);
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			returned.setName(modelFile.getName());
			//returned.setFile(modelFile);

			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
			
			returned.setURI(modelFile.toURI().toString());
			returned.setMetaModelResource(emfMetaModelResource);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			technologyContextManager.registerModel(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Load the &quot;real&quot; load resource data of this resource.
	 * 
	 * @param progress
	 *            a progress monitor in case the resource data is not immediately available.
	 * @return the resource data.
	 * @throws ResourceLoadingCancelledException
	 * @throws ResourceDependencyLoopException
	 * @throws FileNotFoundException
	 * @throws FlexoException
	 */
	@Override
	public EMFModel loadResourceData(IProgress progress) throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException {
		try {
			getEMFResource().load(null);
			EMFModelConverter converter = new EMFModelConverter();
			EMFModel resourceData = converter.convertModel(getMetaModelResource().getMetaModelData(), getEMFResource());
			setResourceData(resourceData);
			return resourceData;
		} catch (IOException e) {
			throw new FlexoException(e);
		}
	}

	/**
	 * Save the &quot;real&quot; resource data of this resource.
	 * 
	 * @throws SaveResourceException
	 */
	@Override
	public void save(IProgress progress) throws SaveResourceException {
		EMFModel resourceData;
		try {
			resourceData = getResourceData(progress);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (FlexoException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		}

		if (!getFlexoIODelegate().hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				//logger.warning("Permission denied : " + getFile().getAbsolutePath());
				logger.warning("Permission denied : " + getFlexoIODelegate().toString());
			}
			throw new SaveResourcePermissionDeniedException(getFlexoIODelegate());
		}
		if (resourceData != null) {
			FileWritingLock lock = getFlexoIODelegate().willWriteOnDisk();
			writeToFile();
			getFlexoIODelegate().hasWrittenOnDisk(lock);
			notifyResourceStatusChanged();
			resourceData.clearIsModified(false);
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Succeeding to save Resource " + getURI() + " : " + getFlexoIODelegate().toString());
			}
		}
	}

	@Override
	public EMFModel getModelData() {
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
			return null;
		}
	}

	@Override
	public EMFModel getModel() {
		return getModelData();
	}

	/**
	 * Write file.
	 * 
	 * @throws SaveResourceException
	 */
	private void writeToFile() throws SaveResourceException {
		try {
			getEMFResource().save(null);
			logger.info("Wrote " + getFlexoIODelegate().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Getter of EMF Model Resource.
	 * 
	 * @return the modelResource value
	 */
	public Resource getEMFResource() {
		if (modelResource == null) {
			EMFMetaModelResource mmResource = (EMFMetaModelResource) getMetaModelResource();
			if (mmResource == null) {
				logger.warning("EMFModel has no meta-model !!!");
				return null;
			}
			else { 
				if (!mmResource.isLoaded()){
					try {
						mmResource.loadResourceData(null);
					} catch (FileNotFoundException e) {
						logger.warning("Cannot load EMF MetaModel");
						return null;
					} catch (ResourceLoadingCancelledException e) {
						logger.warning("Cannot load EMF MetaModel");
						return null;
					} catch (FlexoException e) {
						logger.warning("Cannot load EMF MetaModel");
						return null;
					}
				}
				
			}
			// TODO: should be refactored with IODelegates Also
			modelResource = mmResource.getEMFResourceFactory()
					.createResource(org.eclipse.emf.common.util.URI.createFileURI(getFileFlexoIODelegate().getFile().getAbsolutePath()));
		}
		return modelResource;
	}
	
	private FileFlexoIODelegate getFileFlexoIODelegate(){
		return (FileFlexoIODelegate)getFlexoIODelegate();
	}

	@Override
	public Class<EMFModel> getResourceDataClass() {
		return EMFModel.class;
	}

}
