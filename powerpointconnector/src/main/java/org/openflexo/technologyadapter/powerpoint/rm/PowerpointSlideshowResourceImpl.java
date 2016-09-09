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
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;
import org.openflexo.technologyadapter.powerpoint.model.io.BasicPowerpointModelConverter;
import org.openflexo.toolbox.IProgress;

/**
 * Represents the resource associated to a {@link PowerpointSlideShow}
 * 
 * @author vincent,sguerin
 * 
 */
public abstract class PowerpointSlideshowResourceImpl extends FlexoResourceImpl<PowerpointSlideshow>
		implements PowerpointSlideshowResource {

	private static final Logger logger = Logger.getLogger(PowerpointSlideshowResourceImpl.class.getPackage().getName());

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
