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

package org.openflexo.technologyadapter.odt.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.SaveResourcePermissionDeniedException;
import org.openflexo.technologyadapter.odt.model.ODTDocument;
import org.openflexo.toolbox.IProgress;

public abstract class ODTDocumentResourceImpl extends FlexoResourceImpl<ODTDocument> implements ODTDocumentResource {
	private static final Logger logger = Logger.getLogger(ODTDocumentResourceImpl.class.getPackage().getName());

	@Override
	public ODTDocument loadResourceData(IProgress progress)
			throws ResourceLoadingCancelledException, FileNotFoundException, FlexoException {

		System.out.println("Hop, on charge le document " + getFile());

		return null;
	}

	@Override
	public void save(IProgress progress) throws SaveResourceException {
		try {
			resourceData = getResourceData(progress);
		} catch (FileNotFoundException e) {
			ODTDocument resourceData;
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} catch (FlexoException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		}

		if (!getIODelegate().hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Permission denied : " + getIODelegate().toString());
			}
			throw new SaveResourcePermissionDeniedException(getIODelegate());
		}
		if (resourceData != null) {
			FileWritingLock lock = getIODelegate().willWriteOnDisk();
			writeToFile();
			getIODelegate().hasWrittenOnDisk(lock);
			notifyResourceStatusChanged();
			resourceData.clearIsModified(false);
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Succeeding to save Resource " + getURI() + " : " + getFile().getName());
			}
		}
	}

	@Override
	public ODTDocument getODTDocument() {
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

	private void writeToFile() throws SaveResourceException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(getFile());
			// Unused StreamResult result =
			new StreamResult(out);
			TransformerFactory factory = TransformerFactory
					.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);

			// Unused Transformer transformer =
			factory.newTransformer();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} finally {
			IOUtils.closeQuietly(out);
		}

		logger.info("Wrote " + getFile());
	}

	@Override
	public Class<ODTDocument> getResourceDataClass() {
		return ODTDocument.class;
	}

	private File getFile() {
		return getFileFlexoIODelegate().getFile();
	}

	public FileIODelegate getFileFlexoIODelegate() {
		return (FileIODelegate) getIODelegate();
	}
}
