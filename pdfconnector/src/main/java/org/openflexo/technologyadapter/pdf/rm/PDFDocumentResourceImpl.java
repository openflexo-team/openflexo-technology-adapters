/*
 * (c) Copyright 2015 Openflexo
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

package org.openflexo.technologyadapter.pdf.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.task.Progress;
import org.openflexo.technologyadapter.pdf.model.PDFDocument;
import org.openflexo.technologyadapter.pdf.model.PDFFactory;
import org.openflexo.toolbox.FileUtils;

public abstract class PDFDocumentResourceImpl extends PamelaResourceImpl<PDFDocument, PDFFactory>
		implements PDFDocumentResource {
	private static final Logger logger = Logger.getLogger(PDFDocumentResourceImpl.class.getPackage().getName());

	@Override
	protected PDFDocument performLoad() throws IOException, Exception {

		if (getFlexoIOStreamDelegate() == null) {
			throw new FlexoException("Cannot load PDF document with this IO/delegate: " + getFlexoIODelegate());
		}

		Progress.progress(
				getLocales().localizedForKey("loading") + " " + getFlexoIODelegate().getSerializationArtefact());
		PDDocument document = PDDocument.load(getInputStream());
		PDFDocument returned = getFactory().makeNewPDFDocument(document);
		return returned;
	}

	@Override
	protected void _saveResourceData(boolean clearIsModified) throws SaveResourceException {

		if (getFlexoIOStreamDelegate() == null) {
			throw new SaveResourceException(getFlexoIODelegate());
		}

		FileWritingLock lock = getFlexoIOStreamDelegate().willWriteOnDisk();

		if (logger.isLoggable(Level.INFO)) {
			logger.info("Saving resource " + this + " : " + getFlexoIODelegate().getSerializationArtefact()
					+ " version=" + getModelVersion());
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
				write(new FileOutputStream(temporaryFile));
				System.out.println("Renamed " + temporaryFile + " to " + fileToSave);
				FileUtils.rename(temporaryFile, fileToSave);
			} catch (IOException e) {
				e.printStackTrace();
				if (temporaryFile != null) {
					temporaryFile.delete();
				}
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("Failed to save resource " + this + " with model version " + getModelVersion());
				}
				getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
				throw new SaveResourceException(getFlexoIODelegate(), e);
			}
		} else {
			try {
				write(getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("Failed to save resource " + this + " with model version " + getModelVersion());
				}
				getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
				throw new SaveResourceException(getFlexoIODelegate(), e);
			}
		}

		getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
		if (clearIsModified) {
			notifyResourceStatusChanged();
		}
	}

	private void write(OutputStream out) throws SaveResourceException, IOException {
		try {
			System.out.println("Writing pdf file in : " + getFlexoIODelegate().getSerializationArtefact());
			getDocument().getPDDocument().save(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} finally {
			IOUtils.closeQuietly(out);
		}
		System.out.println("Wrote : " + getFlexoIODelegate().getSerializationArtefact());
	}

	private void makeLocalCopy(File file) throws IOException {
		if (file != null && file.exists()) {
			String localCopyName = file.getName() + "~";
			File localCopy = new File(file.getParentFile(), localCopyName);
			FileUtils.copyFileToFile(file, localCopy);
		}
	}

	@Override
	public PDFDocument getDocument() {
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
	public Class<PDFDocument> getResourceDataClass() {
		return PDFDocument.class;
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

}
