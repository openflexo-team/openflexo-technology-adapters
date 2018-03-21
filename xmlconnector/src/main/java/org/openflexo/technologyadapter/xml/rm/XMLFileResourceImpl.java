/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.xml.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.stream.XMLStreamException;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.foundation.resource.StreamIODelegate;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModelImpl;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLModelFactory;
import org.openflexo.technologyadapter.xml.model.XMLModelImpl;
import org.openflexo.toolbox.FileUtils;
import org.openflexo.toolbox.IProgress;
import org.openflexo.xml.XMLRootElementInfo;
import org.openflexo.xml.XMLRootElementReader;

/**
 * @author xtof
 * 
 */
public abstract class XMLFileResourceImpl extends FlexoResourceImpl<XMLModel> implements XMLFileResource {

	protected static final Logger logger = Logger.getLogger(XMLFileResourceImpl.class.getPackage().getName());
	protected static XMLRootElementReader REreader = new XMLRootElementReader();

	// Properties

	private boolean isLoaded = false;

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
	 * Retrieves the target Namespace from the file when not loaded or from MetamModel when it is loaded and exists
	 * 
	 * @throws IOException
	 * 
	 */
	@Override
	public String getTargetNamespace() throws IOException {

		if (!isLoaded()) {
			XMLRootElementInfo rootInfo;
			rootInfo = REreader.readRootElement(getIODelegate().getSerializationArtefactAsResource());
			return rootInfo.getURI();
		}
		return this.getModel().getMetaModel().getURI();
	}

	public static final String getTargetNamespace(File f) throws IOException {
		if (f != null && f.exists()) {
			XMLRootElementInfo rootInfo;
			rootInfo = REreader.readRootElement(f);
			return rootInfo.getURI();
		}
		throw new IOException("File Not Found ");
	}

	@Override
	public XMLModel loadResourceData(IProgress progress) throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException {

		if (getFlexoIOStreamDelegate() == null) {
			throw new FlexoException("Cannot load XML document with this IO/delegate: " + getIODelegate());
		}

		if (resourceData == null) {
			resourceData = XMLModelImpl.getModelFactory().newInstance(XMLModel.class);
			resourceData.setResource(this);

			attachMetamodel();

		}

		if (!isLoaded()) {

			try {

				FlexoMetaModelResource<XMLModel, XMLMetaModel, XMLTechnologyAdapter> mmRes = getMetaModelResource();

				XMLModelFactory factory = getTechnologyAdapter().getXMLModelFactory();

				factory.setContext(resourceData);

				factory.deserialize(getInputStream());

				factory.resetContext();

				if (mmRes != null) {
					resourceData.setMetaModel(mmRes.getMetaModelData());
				}

				isLoaded = true;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return resourceData;
	}

	// TODO: Ask Sylvain if this could no be tractable with Pamela => Code
	// duplication ?!?

	@Override
	public XMLModel getModel() {
		return getModelData();
	}

	@Override
	public XMLModel getModelData() {

		try {
			return getResourceData(null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlexoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

		/*
		 * if (resourceData == null) { resourceData =
		 * XMLModelImpl.getModelFactory().newInstance(XMLModel.class);
		 * resourceData.setResource(this); }
		 */
		// TODO : check lifecycle for Resource.... should it be loaded on
		// getModelData?
		/*
		 * if (!isLoaded()) { try { resourceData = loadResourceData(null); }
		 * catch (FileNotFoundException e) { e.printStackTrace(); } catch
		 * (ResourceLoadingCancelledException e) { e.printStackTrace(); } catch
		 * (FlexoException e) { e.printStackTrace(); } }
		 */
		// return resourceData;
	}

	@Override
	public void attachMetamodel() {

		FlexoMetaModelResource<XMLModel, XMLMetaModel, XMLTechnologyAdapter> mmRes = getMetaModelResource();
		if (mmRes != null) {
			resourceData.setMetaModel(mmRes.getMetaModelData());
		}
		else {
			// Create default meta-model, on the fly

			XMLMetaModel mm = XMLMetaModelImpl.getModelFactory().newInstance(XMLMetaModel.class);
			mm.setURI(getURI() + "/Metamodel");
			mm.setReadOnly(false);

			resourceData.setMetaModel(mm);
		}

		if (resourceData.getMetaModel() == null) {
			logger.warning("Setting a null Metamodel for Model " + this.getURI());
		}
	}

	@Override
	public Class<XMLModel> getResourceDataClass() {
		return XMLModel.class;

	}

	@Override
	public XMLModel getResourceData(IProgress progress)
			throws ResourceLoadingCancelledException, ResourceLoadingCancelledException, FileNotFoundException, FlexoException {

		if (isLoading()) {
			logger.warning("trying to load a resource data from itself, please investigate");
			return null;
		}
		if (isLoadable() && !isLoaded()) {
			setLoading(true);
			resourceData = loadResourceData(progress);
			setLoading(false);
			// That's fine, resource is loaded, now let's notify the loading of
			// the resources
			notifyResourceLoaded();
		}
		return resourceData;
	}

	// Lifecycle Management

	@Override
	public boolean isLoaded() {
		return isLoaded;
	}

	/**
	 * Return a FlexoIOStreamDelegate associated to this flexo resource
	 * 
	 * @return
	 */
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
	 * Save current resource data to current XML resource file.<br>
	 * Forces XML version to be the latest one.
	 * 
	 * @return
	 */
	protected final void saveResourceData(boolean clearIsModified) throws SaveResourceException, SaveResourcePermissionDeniedException {
		// System.out.println("PamelaResourceImpl Saving " + getFile());
		if (!getIODelegate().hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Permission denied : " + getIODelegate().toString());
			}
			throw new SaveResourcePermissionDeniedException(getIODelegate());
		}
		if (resourceData != null) {
			_saveResourceData(clearIsModified);
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Succeeding to save Resource " + this + " : " + getIODelegate().getSerializationArtefact());
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
			} catch (Exception e) {
				e.printStackTrace();
				if (temporaryFile != null) {
					temporaryFile.delete();
				}
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("Failed to save resource " + this);
				}
				getFlexoIOStreamDelegate().hasWrittenOnDisk(lock);
				throw new SaveResourceException(getIODelegate(), e);
			}
		}
		else {
			try {
				write(getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
				if (logger.isLoggable(Level.WARNING)) {
					logger.warning("Failed to save resource " + this);
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

	private void write(OutputStream out) throws IOException, XMLStreamException, ResourceLoadingCancelledException, FlexoException {
		System.out.println("Writing xml file in : " + getIODelegate().getSerializationArtefact());
		try (OutputStreamWriter outSW = new OutputStreamWriter(out, "UTF-8")) {
			XMLWriter<XMLFileResource, XMLModel> writer = new XMLWriter<>(this, outSW);
			writer.writeDocument();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} finally {
			out.close();
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

}
