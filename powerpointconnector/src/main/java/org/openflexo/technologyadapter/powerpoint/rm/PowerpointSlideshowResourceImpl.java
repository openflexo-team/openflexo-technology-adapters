/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Powerpointconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.powerpoint.rm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.powerpoint.PowerpointTechnologyContextManager;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;
import org.openflexo.technologyadapter.powerpoint.model.io.BasicPowerpointModelConverter;
import org.openflexo.toolbox.IProgress;

/**
 * Represents the resource associated to a {@link PowerpointSlideShow}
 * 
 * @author vincent,sguerin
 * 
 */
public abstract class PowerpointSlideshowResourceImpl extends FlexoResourceImpl<PowerpointSlideshow>implements PowerpointSlideshowResource {

	private static final Logger logger = Logger.getLogger(PowerpointSlideshowResourceImpl.class.getPackage().getName());

	/**
	 * Creates a new {@link ExcelModelResource} asserting this is an explicit creation: no file is present on file system<br>
	 * This method should not be used to retrieve the resource from a file in the file system, use
	 * {@link #retrieveOWLOntologyResource(File, OWLOntologyLibrary)} instead
	 * 
	 * @param ontologyURI
	 * @param owlFile
	 * @param ontologyLibrary
	 * @return
	 */
	public static PowerpointSlideshowResource makePowerpointSlideshowResource(String modelURI, File powerpointFile,
			PowerpointTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, PowerpointSlideshowResource.class));
			PowerpointSlideshowResourceImpl returned = (PowerpointSlideshowResourceImpl) factory
					.newInstance(PowerpointSlideshowResource.class);
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			returned.initName(powerpointFile.getName());

			// returned.setFile(powerpointFile);
			FileFlexoIODelegate fileIODelegate = factory.newInstance(FileFlexoIODelegate.class);
			returned.setFlexoIODelegate(fileIODelegate);
			fileIODelegate.setFile(powerpointFile);

			returned.setURI(modelURI);
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			technologyContextManager.registerResource(returned);

			PowerpointSlideshow resourceData = new PowerpointSlideshow(technologyContextManager.getTechnologyAdapter());
			returned.setResourceData(resourceData);
			resourceData.setResource(returned);

			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Instanciates a new {@link OWLOntologyResource} asserting we are about to built a resource matching an existing file in the file
	 * system<br>
	 * 
	 */
	public static PowerpointSlideshowResource retrievePowerpointSlideshowResource(File modelFile,
			PowerpointTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(FileFlexoIODelegate.class, PowerpointSlideshowResource.class));
			PowerpointSlideshowResourceImpl returned = (PowerpointSlideshowResourceImpl) factory
					.newInstance(PowerpointSlideshowResource.class);
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			returned.initName(modelFile.getName());

			// returned.setFile(modelFile);
			FileFlexoIODelegate fileIODelegate = factory.newInstance(FileFlexoIODelegate.class);
			returned.setFlexoIODelegate(fileIODelegate);
			fileIODelegate.setFile(modelFile);

			returned.setURI(modelFile.toURI().toString());
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getTechnologyAdapter().getTechnologyAdapterService().getServiceManager());
			technologyContextManager.registerResource(returned);

			/*try {
				PowerpointSlideshow resourceData = returned.loadResourceData(null);
				returned.setResourceData(resourceData);
				resourceData.setResource(returned);
				returned.save(null);
			} catch (SaveResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidPowerpointFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

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
	public PowerpointSlideshow loadResourceData(IProgress progress) throws InvalidPowerpointFormatException {

		PowerpointSlideshow resourceData = null;
		SlideShow ssOpenned = null;
		FileFlexoIODelegate delegate = (FileFlexoIODelegate) getFlexoIODelegate();
		try {
			if (!getFlexoIODelegate().exists()) {

				// Creates a new file
				delegate.getFile().createNewFile();
				ssOpenned = new SlideShow();

				BasicPowerpointModelConverter converter = new BasicPowerpointModelConverter();
				resourceData = converter.convertPowerpointSlideshow(ssOpenned, getTechnologyAdapter());
				// TODO how to change this?
				resourceData.setResource(this/*retrieveExcelWorkbookResource(getFile(), getTechnologyContextManager())*/);
				setResourceData(resourceData);
				FileOutputStream fos = new FileOutputStream(delegate.getFile());
				ssOpenned.write(fos);
				fos.close();
			}
			else {
				FileInputStream fis = new FileInputStream(delegate.getFile());
				ssOpenned = new SlideShow(fis);
				BasicPowerpointModelConverter converter = new BasicPowerpointModelConverter();
				resourceData = converter.convertPowerpointSlideshow(ssOpenned, getTechnologyAdapter());
				resourceData.setResource(this);
				setResourceData(resourceData);
				fis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OfficeXmlFileException e) {
			// TODO: load an XSSFWorkbook
			throw new InvalidPowerpointFormatException(this, e);
		}
		return resourceData;
	}

	/**
	 * Save the &quot;real&quot; resource data of this resource.
	 * 
	 * @throws SaveResourceException
	 */
	@Override
	public void save(IProgress progress) throws SaveResourceException {
		PowerpointSlideshow resourceData;
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
				logger.warning("Permission denied : " + getFlexoIODelegate().toString());
			}
			throw new SaveResourcePermissionDeniedException(getFlexoIODelegate());
		}
		if (resourceData != null) {
			FileWritingLock lock = getFlexoIODelegate().willWriteOnDisk();
			writeToFile(resourceData.getSlideShow());
			getFlexoIODelegate().hasWrittenOnDisk(lock);
			notifyResourceStatusChanged();
			resourceData.clearIsModified(false);
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Succeeding to save Resource " + getURI() + " : " + getFlexoIODelegate().toString());
			}
		}
	}

	/**
	 * Write file.
	 * 
	 * @throws SaveResourceException
	 */
	private void writeToFile(SlideShow slideshow) throws SaveResourceException {
		logger.info("Wrote " + getFlexoIODelegate().toString());
		FileOutputStream fileOut;
		FileFlexoIODelegate delegate = (FileFlexoIODelegate) getFlexoIODelegate();
		try {
			fileOut = new FileOutputStream(delegate.getFile());
			slideshow.write(fileOut);
			fileOut.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Class<PowerpointSlideshow> getResourceDataClass() {
		return PowerpointSlideshow.class;
	}
}
