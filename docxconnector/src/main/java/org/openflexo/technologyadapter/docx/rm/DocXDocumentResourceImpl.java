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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FileFlexoIODelegate.FileFlexoIODelegateImpl;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.InJarFlexoIODelegate;
import org.openflexo.foundation.resource.InJarFlexoIODelegate.InJarFlexoIODelegateImpl;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.rm.InJarResourceImpl;
import org.openflexo.technologyadapter.docx.DocXTechnologyContextManager;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXFactory;
import org.openflexo.technologyadapter.docx.model.DocXFactory.IdentifierManagementStrategy;
import org.openflexo.toolbox.FileUtils;

public abstract class DocXDocumentResourceImpl extends PamelaResourceImpl<DocXDocument, DocXFactory>implements DocXDocumentResource {
	private static final Logger logger = Logger.getLogger(DocXDocumentResourceImpl.class.getPackage().getName());

	public static DocXDocumentResource makeDocXDocumentResource(File modelFile, DocXTechnologyContextManager technologyContextManager,
			FlexoResourceCenter<?> resourceCenter, IdentifierManagementStrategy idStrategy) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(DocXDocumentResource.class, FileFlexoIODelegate.class));
			DocXDocumentResourceImpl returned = (DocXDocumentResourceImpl) factory.newInstance(DocXDocumentResource.class);
			returned.initName(modelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
			DocXFactory docXFactory = new DocXFactory(returned, technologyContextManager.getServiceManager().getEditingContext(),
					idStrategy);
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

	public static DocXDocumentResource retrieveDocXDocumentResource(File modelFile, DocXTechnologyContextManager technologyContextManager,
			FlexoResourceCenter<?> resourceCenter, IdentifierManagementStrategy idStrategy) {
		try {
			ModelFactory factory = new ModelFactory(
					ModelContextLibrary.getCompoundModelContext(DocXDocumentResource.class, FileFlexoIODelegate.class));
			DocXDocumentResourceImpl returned = (DocXDocumentResourceImpl) factory.newInstance(DocXDocumentResource.class);
			returned.initName(modelFile.getName());
			returned.setFlexoIODelegate(FileFlexoIODelegateImpl.makeFileFlexoIODelegate(modelFile, factory));
			DocXFactory docXFactory = new DocXFactory(returned, technologyContextManager.getServiceManager().getEditingContext(),
					idStrategy);
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
	
	// TODO : have more optimized code between methods referring to different kind of resources
	public static DocXDocumentResource retrieveDocXDocumentResource(InJarResourceImpl jarResource,
			DocXTechnologyContextManager technologyContextManager, FlexoResourceCenter<?> resourceCenter,
			IdentifierManagementStrategy idStrategy) {	try {
				ModelFactory factory = new ModelFactory(
						ModelContextLibrary.getCompoundModelContext(DocXDocumentResource.class, InJarFlexoIODelegate.class));
				DocXDocumentResourceImpl returned = (DocXDocumentResourceImpl) factory.newInstance(DocXDocumentResource.class);
				returned.initName(jarResource.getURL().getFile());
				returned.setFlexoIODelegate(InJarFlexoIODelegateImpl.makeInJarFlexoIODelegate(jarResource, factory));
				DocXFactory docXFactory = new DocXFactory(returned, technologyContextManager.getServiceManager().getEditingContext(),
						idStrategy);
				returned.setFactory(docXFactory);

				System.out.println("J'initialise le RC!!");
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
	protected DocXDocument performLoad() throws IOException, Exception {

		try {
			WordprocessingMLPackage wpmlPackage = WordprocessingMLPackage.load(getFile());

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
			temporaryFile = File.createTempFile("temp", ".docx", dir);
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

		if (clearIsModified) {
			if (getFactory().getIDStrategy() == IdentifierManagementStrategy.Bookmark) {
				getFactory().someIdHaveBeenGeneratedAccordingToBookmarkManagementStrategy = false;
			}
		}
	}

	private void writeToFile(File docxDir) throws SaveResourceException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(docxDir);
			/*StreamResult result = new StreamResult(out);
			TransformerFactory factory = TransformerFactory
					.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);
			
			Transformer transformer = factory.newTransformer();*/

			System.out.println("Writing docx file in : " + docxDir);

			// System.out.println("getDocument().getWordprocessingMLPackage().getMainDocumentPart()="
			// + getDocument().getWordprocessingMLPackage().getMainDocumentPart());

			/*for (Object o : getDocument().getWordprocessingMLPackage().getMainDocumentPart().getContent()) {
				System.out.println("% " + o);
			}*/

			// System.out.println(XmlUtils.marshaltoString(getDocument().getWordprocessingMLPackage().getMainDocumentPart()));

			getDocument().getWordprocessingMLPackage().save(out);

		} catch (Docx4JException e) {
			e.printStackTrace();
			throw new SaveResourceException(getFlexoIODelegate());
		} catch (FileNotFoundException e) {
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

	private File getFile() {
		return getFileFlexoIODelegate().getFile();
	}

	public FileFlexoIODelegate getFileFlexoIODelegate() {
		return (FileFlexoIODelegate) getFlexoIODelegate();
	}


}
