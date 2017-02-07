/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Workbook;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoIOStreamDelegate;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.model.semantics.ExcelModel;
import org.openflexo.toolbox.FileUtils;
import org.openflexo.toolbox.IProgress;

/**
 * Represents the resource associated to a {@link ExcelModel}
 * 
 * @author sguerin
 * 
 */
public abstract class ExcelWorkbookResourceImpl extends FlexoResourceImpl<ExcelWorkbook>
		implements ExcelWorkbookResource {

	private static final Logger logger = Logger.getLogger(ExcelWorkbookResourceImpl.class.getPackage().getName());

	private boolean isLoaded = false;

	/**
	 * Load the &quot;real&quot; load resource data of this resource.
	 * 
	 * @param progress
	 *            a progress monitor in case the resource data is not
	 *            immediately available.
	 * @return the resource data.
	 * @throws ResourceLoadingCancelledException
	 * @throws ResourceDependencyLoopException
	 * @throws FileNotFoundException
	 * @throws FlexoException
	 */
	@Override
	public ExcelWorkbook loadResourceData(IProgress progress) throws FlexoException {

		if (getFlexoIOStreamDelegate() == null) {
			throw new FlexoException("Cannot load Excel document with this IO/delegate: " + getFlexoIODelegate());
		}

		ExcelWorkbook resourceData = null;
		try {
			resourceData = ExcelWorkbookResourceFactory.createExcelWorkbook(getFlexoIOStreamDelegate());
		} catch (OfficeXmlFileException e) {
			throw new InvalidExcelFormatException(this, e);
		}

		if (resourceData == null) {
			logger.warning(
					"canno't retrieve resource data from serialization artifact " + getFlexoIODelegate().toString());
			return null;
		}

		resourceData.setResource(this);
		setResourceData(resourceData);

		return resourceData;
	}

	/**
	 * Save the &quot;real&quot; resource data of this resource.
	 * 
	 * @throws SaveResourceException
	 */
	/*
	 * @Override public void save(IProgress progress) throws
	 * SaveResourceException {
	 * 
	 * if (getFlexoIOStreamDelegate() == null) { throw new
	 * SaveResourceException(getFlexoIODelegate()); }
	 * 
	 * ExcelWorkbook resourceData; try { resourceData =
	 * getResourceData(progress); } catch (FileNotFoundException e) {
	 * e.printStackTrace(); throw new
	 * SaveResourceException(getFlexoIODelegate()); } catch
	 * (ResourceLoadingCancelledException e) { e.printStackTrace(); throw new
	 * SaveResourceException(getFlexoIODelegate()); } catch (FlexoException e) {
	 * e.printStackTrace(); throw new
	 * SaveResourceException(getFlexoIODelegate()); }
	 * 
	 * if (!getFlexoIODelegate().hasWritePermission()) { if
	 * (logger.isLoggable(Level.WARNING)) {
	 * logger.warning("Permission denied : " + getFlexoIODelegate().toString());
	 * } throw new SaveResourcePermissionDeniedException(getFlexoIODelegate());
	 * } if (resourceData != null) { FileWritingLock lock =
	 * getFlexoIODelegate().willWriteOnDisk();
	 * writeToFile(resourceData.getWorkbook());
	 * getFlexoIODelegate().hasWrittenOnDisk(lock);
	 * notifyResourceStatusChanged(); resourceData.clearIsModified(false); if
	 * (logger.isLoggable(Level.INFO)) {
	 * logger.info("Succeeding to save Resource " + getURI() + " : " +
	 * getFlexoIODelegate().toString()); } } }
	 */

	/**
	 * Write file.
	 * 
	 * @throws SaveResourceException
	 */
	private void write(Workbook workbook, OutputStream out) throws SaveResourceException {
		logger.info("Writing " + getFlexoIODelegate().getSerializationArtefact());
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} finally {
			IOUtils.closeQuietly(out);
		}
		logger.info("Wrote " + getFlexoIODelegate().getSerializationArtefact());
	}

	@Override
	public Class<ExcelWorkbook> getResourceDataClass() {
		return ExcelWorkbook.class;
	}

	/**
	 * Return a FlexoIOStreamDelegate associated to this flexo resource
	 * 
	 * @return
	 */
	public FlexoIOStreamDelegate<?> getFlexoIOStreamDelegate() {
		if (getFlexoIODelegate() instanceof FlexoIOStreamDelegate) {
			return (FlexoIOStreamDelegate<?>) getFlexoIODelegate();
		}
		return null;
	}

	public InputStream getInputStream() {
		if (getFlexoIOStreamDelegate() != null) {
			return getFlexoIOStreamDelegate().getInputStream();
		}
		return null;
	}

	public OutputStream getOutputStream() {
		if (getFlexoIOStreamDelegate() != null) {
			return getFlexoIOStreamDelegate().getOutputStream();
		}
		return null;
	}

	/**
	 * Save the &quot;real&quot; resource data of this resource.
	 * 
	 * @throws SaveResourceException
	 */
	@Override
	public final void save(IProgress progress) throws SaveResourceException {
		if (progress != null) {
			progress.setProgress(getLocales().localizedForKey("saving") + " " + this.getName());
		}
		if (!isLoaded()) {
			return;
		}
		if (!isDeleted()) {
			saveResourceData(true);
			resourceData.clearIsModified(false);
		}
	}

	/**
	 * Save current resource data to current XML resource file.<br>
	 * Forces XML version to be the latest one.
	 * 
	 * @return
	 */
	protected final void saveResourceData(boolean clearIsModified)
			throws SaveResourceException, SaveResourcePermissionDeniedException {
		// System.out.println("PamelaResourceImpl Saving " + getFile());
		if (!getFlexoIODelegate().hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Permission denied : " + getFlexoIODelegate().toString());
			}
			throw new SaveResourcePermissionDeniedException(getFlexoIODelegate());
		}
		if (resourceData != null) {
			_saveResourceData(resourceData, clearIsModified);
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Succeeding to save " + getFlexoIODelegate().getSerializationArtefact());
			}
		}
		if (clearIsModified) {
			try {
				getResourceData(null).clearIsModified(false);
				// No need to reset the last memory update since it is valid
				notifyResourceSaved();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void _saveResourceData(ExcelWorkbook excelWorkbook, boolean clearIsModified)
			throws SaveResourceException {

		if (getFlexoIOStreamDelegate() == null) {
			throw new SaveResourceException(getFlexoIODelegate());
		}

		FileWritingLock lock = getFlexoIOStreamDelegate().willWriteOnDisk();

		if (logger.isLoggable(Level.INFO)) {
			logger.info("Saving resource " + this + " : " + getFlexoIODelegate().getSerializationArtefact());
		}

		if (getFlexoIOStreamDelegate() instanceof FileFlexoIODelegate) {
			File temporaryFile = null;
			try {
				File fileToSave = ((FileFlexoIODelegate) getFlexoIOStreamDelegate()).getFile();
				// Make local copy
				makeLocalCopy(fileToSave);
				// Using temporary file
				temporaryFile = ((FileFlexoIODelegate) getFlexoIODelegate()).createTemporaryArtefact(".pdf");
				if (logger.isLoggable(Level.FINE)) {
					logger.finer("Creating temp file " + temporaryFile.getAbsolutePath());
				}
				write(excelWorkbook.getWorkbook(), new FileOutputStream(temporaryFile));
				System.out.println("Renamed " + temporaryFile + " to " + fileToSave);
				FileUtils.rename(temporaryFile, fileToSave);
			} catch (IOException e) {
				e.printStackTrace();
				if (temporaryFile != null) {
					temporaryFile.delete();
				}
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("Failed to save resource " + getFlexoIODelegate().getSerializationArtefact());
				}
				getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
				throw new SaveResourceException(getFlexoIODelegate(), e);
			}
		} else {
			write(excelWorkbook.getWorkbook(), getOutputStream());
		}

		getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
		if (clearIsModified) {
			notifyResourceStatusChanged();
		}
	}

	private void makeLocalCopy(File file) throws IOException {
		if (file != null && file.exists()) {
			String localCopyName = file.getName() + "~";
			File localCopy = new File(file.getParentFile(), localCopyName);
			FileUtils.copyFileToFile(file, localCopy);
		}
	}

}
