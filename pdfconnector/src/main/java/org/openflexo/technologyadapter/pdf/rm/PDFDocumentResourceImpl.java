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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileFlexoIODelegate.FileFlexoIODelegateImpl;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.task.Progress;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.pdf.PDFTechnologyContextManager;
import org.openflexo.technologyadapter.pdf.model.PDFDocument;
import org.openflexo.technologyadapter.pdf.model.PDFFactory;
import org.openflexo.toolbox.FileUtils;

public abstract class PDFDocumentResourceImpl extends PamelaResourceImpl<PDFDocument, PDFFactory>implements PDFDocumentResource {
	private static final Logger logger = Logger.getLogger(PDFDocumentResourceImpl.class.getPackage().getName());

	public static PDFDocumentResource makePDFDocumentResource(File modelFile, PDFTechnologyContextManager technologyContextManager,
			FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(PDFDocumentResource.class, FileFlexoIODelegate.class));
			PDFDocumentResourceImpl returned = (PDFDocumentResourceImpl) factory.newInstance(PDFDocumentResource.class);
			returned.initName(modelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
			PDFFactory docXFactory = new PDFFactory(returned, technologyContextManager.getServiceManager().getEditingContext());
			returned.setFactory(docXFactory);

			// returned.setURI(modelURI);
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getServiceManager());
			returned.setTechnologyAdapter(technologyContextManager.getTechnologyAdapter());
			returned.setTechnologyContextManager(technologyContextManager);
			technologyContextManager.registerResource(returned);

			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static PDFDocumentResource retrievePDFDocumentResource(File modelFile, PDFTechnologyContextManager technologyContextManager,
			FlexoResourceCenter<?> resourceCenter) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(PDFDocumentResource.class, FileFlexoIODelegate.class));
			PDFDocumentResourceImpl returned = (PDFDocumentResourceImpl) factory.newInstance(PDFDocumentResource.class);
			returned.initName(modelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
			PDFFactory docXFactory = new PDFFactory(returned, technologyContextManager.getServiceManager().getEditingContext());
			returned.setFactory(docXFactory);

			// returned.setURI(modelFile.toURI().toString());
			returned.setResourceCenter(resourceCenter);
			returned.setServiceManager(technologyContextManager.getServiceManager());
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
	protected PDFDocument performLoad() throws IOException, Exception {

		Progress.progress(FlexoLocalization.localizedForKey("loading") + " " + getFile().getName());
		System.out.println("************* >> HERE in PDFDocumentResource with thread: " + Thread.currentThread());
		PDDocument document = PDDocument.load(getFile());
		PDFDocument returned = getFactory().makeNewPDFDocument(document);
		return returned;
	}

	@Override
	protected void _saveResourceData(boolean clearIsModified) throws SaveResourceException {
		File temporaryFile = null;
		FileWritingLock lock = getFlexoIOStreamDelegate().willWriteOnDisk();

		if (logger.isLoggable(Level.INFO)) {
			logger.info("Saving resource " + this + " : " + getFile() + " version=" + getModelVersion());
		}

		try {
			File dir = getFile().getParentFile();
			if (!dir.exists()) {
				willWrite(dir);
				dir.mkdirs();
			}
			willWrite(getFile());
			// Make local copy
			makeLocalCopy();
			// Using temporary file
			temporaryFile = File.createTempFile("temp", ".pdf", dir);
			if (logger.isLoggable(Level.FINE)) {
				logger.finer("Creating temp file " + temporaryFile.getAbsolutePath());
			}
			writeToFile(temporaryFile);

			System.out.println("Renamed " + temporaryFile + " to " + getFile());
			FileUtils.rename(temporaryFile, getFile());
			getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
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
			throw new SaveResourceException(getFlexoIODelegate(), e);
		}
	}

	private void writeToFile(File file) throws SaveResourceException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);

			System.out.println("Writing pdf file in : " + file);

			getDocument().getPDDocument().save(out);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (IOException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} finally {
			IOUtils.closeQuietly(out);
		}

		logger.info("Wrote " + getFile());
	}

	private void makeLocalCopy() throws IOException {
		if (getFile() != null && getFile().exists()) {
			String localCopyName = getFile().getName() + "~";
			File localCopy = new File(getFile().getParentFile(), localCopyName);
			FileUtils.copyFileToFile(getFile(), localCopy);
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

	private File getFile() {
		return getFileFlexoIODelegate().getFile();
	}

	public FileFlexoIODelegate getFileFlexoIODelegate() {
		return (FileFlexoIODelegate) getFlexoIODelegate();
	}

}
