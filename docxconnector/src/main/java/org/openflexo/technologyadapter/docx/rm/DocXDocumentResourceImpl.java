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

package org.openflexo.technologyadapter.docx.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.task.Progress;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFactory;
import org.openflexo.technologyadapter.docx.model.IdentifierManagementStrategy;
import org.openflexo.toolbox.FileUtils;

public abstract class DocXDocumentResourceImpl extends PamelaResourceImpl<DocXDocument, DocXFactory> implements DocXDocumentResource {
	private static final Logger logger = Logger.getLogger(DocXDocumentResourceImpl.class.getPackage().getName());

	@Override
	protected DocXDocument performLoad() throws IOException, Exception {

		if (getFlexoIOStreamDelegate() == null) {
			throw new FlexoException("Cannot load DocX document with this IO/delegate: " + getIODelegate());
		}

		Progress.progress(getLocales().localizedForKey("loading") + " " + getIODelegate().getSerializationArtefact());
		try {
			WordprocessingMLPackage wpmlPackage = WordprocessingMLPackage.load(getInputStream());

			DocXDocument returned = getFactory().makeNewDocXDocument(wpmlPackage);
			return returned;
		} catch (Docx4JException e) {
			e.printStackTrace();
			throw new FlexoException(e);
		}
	}

	@Override
	public void stopDeserializing() {
		super.stopDeserializing();

		if (getFactory().getIDStrategy() == IdentifierManagementStrategy.Bookmark) {

			if (getFactory().someIdHaveBeenGeneratedAccordingToBookmarkManagementStrategy) {
				getLoadedResourceData().setModified(true);
			}
		}
	}

	@Override
	protected void _saveResourceData(boolean clearIsModified) throws SaveResourceException {

		if (getFlexoIOStreamDelegate() == null) {
			throw new SaveResourceException(getIODelegate());
		}

		FileWritingLock lock = getFlexoIOStreamDelegate().willWriteOnDisk();

		if (logger.isLoggable(Level.INFO)) {
			logger.info("Saving resource " + this + " : " + getIODelegate().getSerializationArtefact() + " version=" + getModelVersion());
		}

		if (getFlexoIOStreamDelegate() instanceof FileIODelegate) {
			File temporaryFile = null;
			try {
				File fileToSave = ((FileIODelegate) getFlexoIOStreamDelegate()).getFile();
				// Make local copy
				makeLocalCopy(fileToSave);
				// Using temporary file
				temporaryFile = ((FileIODelegate) getIODelegate()).createTemporaryArtefact(".OF_TEMP.docx");
				if (logger.isLoggable(Level.FINE)) {
					logger.finer("Creating temp file " + temporaryFile.getAbsolutePath());
				}
				write(new FileOutputStream(temporaryFile));
				System.out.println("Renamed " + temporaryFile + " to " + fileToSave);
				FileUtils.rename(temporaryFile, fileToSave);
				if (clearIsModified) {
					notifyResourceStatusChanged();
				}
			} catch (IOException e) {
				e.printStackTrace();
				if (temporaryFile != null) {
					temporaryFile.delete();
				}
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("Failed to save resource " + this + " with model version " + getModelVersion());
				}
				getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
				throw new SaveResourceException(getIODelegate(), e);
			}
		}
		else {
			try {
				write(getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("Failed to save resource " + this + " with model version " + getModelVersion());
				}
				getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
				throw new SaveResourceException(getIODelegate(), e);
			}
		}

		getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
		if (clearIsModified) {
			notifyResourceStatusChanged();
		}

	}

	private void write(OutputStream out) throws SaveResourceException, IOException {
		System.out.println("Writing docx file in : " + getIODelegate().getSerializationArtefact());
		try {
			getDocument().getWordprocessingMLPackage().save(out);

		} catch (Docx4JException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} finally {
			IOUtils.closeQuietly(out);
		}

		System.out.println("Wrote : " + getIODelegate().getSerializationArtefact());
	}

	private static void makeLocalCopy(File file) throws IOException {
		if (file != null && file.exists()) {
			String localCopyName = file.getName() + "~";
			File localCopy = new File(file.getParentFile(), localCopyName);
			FileUtils.copyFileToFile(file, localCopy);
		}
	}

	@Override
	public DocXDocument getDocument() {
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
	public Class<DocXDocument> getResourceDataClass() {
		return DocXDocument.class;
	}

	/*
	 * private File getFile() { return getFileFlexoIODelegate().getFile(); }
	 * 
	 * public FileFlexoIODelegate getFileFlexoIODelegate() { return
	 * (FileFlexoIODelegate) getFlexoIODelegate(); }
	 */

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
