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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.IOFlexoException;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.StreamIODelegate;
import org.openflexo.technologyadapter.excel.model.BasicExcelModelConverter;
import org.openflexo.technologyadapter.excel.model.ExcelModelFactory;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.toolbox.FileUtils;

/**
 * Represents the resource associated to a {@link ExcelWorkbook}
 * 
 * @author sguerin
 * 
 */
public abstract class ExcelWorkbookResourceImpl extends PamelaResourceImpl<ExcelWorkbook, ExcelModelFactory>
		implements ExcelWorkbookResource {

	private static final Logger logger = Logger.getLogger(ExcelWorkbookResourceImpl.class.getPackage().getName());

	// unused private boolean isLoaded = false;

	private BasicExcelModelConverter converter;

	public ExcelWorkbookResourceImpl() {
	}

	@Override
	public BasicExcelModelConverter getConverter() {
		return converter;
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
	public ExcelWorkbook loadResourceData() throws IOFlexoException {

		converter = new BasicExcelModelConverter(this);

		if (getFlexoIOStreamDelegate() == null) {
			throw new IOFlexoException("Cannot load Excel document with this IO/delegate: " + getIODelegate());
		}

		ExcelWorkbook resourceData = null;
		try {
			resourceData = createOrLoadExcelWorkbook(getFlexoIOStreamDelegate());
			getInputStream().close();
		} catch (OfficeXmlFileException e) {
			throw new IOFlexoException(e.getMessage());
		} catch (IOException e) {
			throw new IOFlexoException(e);
		}

		if (resourceData == null) {
			logger.warning("canno't retrieve resource data from serialization artifact " + getIODelegate().toString());
			return null;
		}

		resourceData.setResource(this);
		setResourceData(resourceData);

		return resourceData;
	}

	@Override
	public void unloadResourceData(boolean deleteResourceData) {
		super.unloadResourceData(deleteResourceData);
		if (converter != null) {
			converter.delete();
		}
		converter = null;
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
	private void write(OutputStream out) throws SaveResourceException {
		logger.info("Writing " + getIODelegate().getSerializationArtefact());
		try {
			getExcelWorkbook().getWorkbook().write(out);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} finally {
			try {
				out.close();
			} catch (IOException e) {}
		}
		logger.info("Wrote " + getIODelegate().getSerializationArtefact());
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
	@Override
	public StreamIODelegate<?> getFlexoIOStreamDelegate() {
		if (getIODelegate() instanceof StreamIODelegate) {
			return (StreamIODelegate<?>) getIODelegate();
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
	/*@Override
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
	}*/

	/**
	 * Save current resource data to current XML resource file.<br>
	 * Forces XML version to be the latest one.
	 * 
	 * @return
	 */
	/*@Override
	protected final void saveResourceData(boolean clearIsModified) throws SaveResourceException, SaveResourcePermissionDeniedException {
		// System.out.println("PamelaResourceImpl Saving " + getFile());
		if (!getIODelegate().hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Permission denied : " + getIODelegate().toString());
			}
			throw new SaveResourcePermissionDeniedException(getIODelegate());
		}
		if (resourceData != null) {
			_saveResourceData(resourceData, clearIsModified);
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Succeeding to save " + getIODelegate().getSerializationArtefact());
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
	}*/

	@Override
	protected void _saveResourceData(boolean clearIsModified) throws SaveResourceException {

		if (getFlexoIOStreamDelegate() == null) {
			throw new SaveResourceException(getIODelegate());
		}

		FileWritingLock lock = getFlexoIOStreamDelegate().willWriteOnDisk();

		if (logger.isLoggable(Level.INFO)) {
			logger.info("Saving resource " + this + " : " + getIODelegate().getSerializationArtefact());
		}

		if (getFlexoIOStreamDelegate() instanceof FileIODelegate) {
			File temporaryFile = null;
			try {
				File fileToSave = ((FileIODelegate) getFlexoIOStreamDelegate()).getFile();
				// Make local copy
				makeLocalCopy(fileToSave);
				// Using temporary file
				temporaryFile = ((FileIODelegate) getIODelegate()).createTemporaryArtefact(".pdf");
				if (logger.isLoggable(Level.FINE)) {
					logger.finer("Creating temp file " + temporaryFile.getAbsolutePath());
				}
				try (FileOutputStream fos = new FileOutputStream(temporaryFile)) {
					write(fos);
				}
				System.out.println("Renamed " + temporaryFile + " to " + fileToSave);
				FileUtils.rename(temporaryFile, fileToSave);
			} catch (IOException e) {
				e.printStackTrace();
				if (temporaryFile != null) {
					temporaryFile.delete();
				}
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("Failed to save resource " + getIODelegate().getSerializationArtefact());
				}
				getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
				throw new SaveResourceException(getIODelegate(), e);
			}
		}
		else {
			write(getOutputStream());
		}

		getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
		if (clearIsModified) {
			notifyResourceStatusChanged();
		}
	}

	private static void makeLocalCopy(File file) throws IOException {
		if (file != null && file.exists()) {
			String localCopyName = file.getName() + "~";
			File localCopy = new File(file.getParentFile(), localCopyName);
			FileUtils.copyFileToFile(file, localCopy);
		}
	}

	@Override
	public <I> ExcelWorkbook createOrLoadExcelWorkbook(StreamIODelegate<I> ioDelegate) {
		Workbook wb = null;
		ExcelWorkbook newWorkbook = null;
		try {
			if (!ioDelegate.exists() && ioDelegate.getSerializationArtefactName().endsWith(".xls")) {
				wb = new HSSFWorkbook();
				wb.createSheet("Default");
			}
			else if (!ioDelegate.exists() && ioDelegate.getSerializationArtefactName().endsWith(".xlsx")) {
				wb = new XSSFWorkbook();
				wb.createSheet("Default");
			}
			else {
				wb = WorkbookFactory.create(ioDelegate.getInputStream());
			}
			BasicExcelModelConverter converter = getConverter();
			newWorkbook = converter.convertExcelWorkbook(wb);
			// TODO: FD => I would like to close wb here (wb.close()) but this breaks unit test don't know why
			// by consequence I don't know whether the workbook gets closed at some point or not
			// maybe there is a resource leak here
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
		return newWorkbook;
	}

	@Override
	public ExcelWorkbook getExcelWorkbook() {
		try {
			return getResourceData();
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

}
