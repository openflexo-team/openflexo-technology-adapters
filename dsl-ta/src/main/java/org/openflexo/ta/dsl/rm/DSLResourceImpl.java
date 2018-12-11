/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
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

package org.openflexo.ta.dsl.rm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.IOFlexoException;
import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.PamelaResourceImpl;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.StreamIODelegate;
import org.openflexo.ta.dsl.model.DSLModelFactory;
import org.openflexo.ta.dsl.model.DSLParser;
import org.openflexo.ta.dsl.model.DSLSystem;
import org.openflexo.ta.dsl.model.ParseException;
import org.openflexo.toolbox.FileUtils;

/**
 * Default implementation for a resource storing a {@link DSLSystem}
 * 
 * @author sylvain
 *
 */
public abstract class DSLResourceImpl extends PamelaResourceImpl<DSLSystem, DSLModelFactory> implements DSLResource {

	private static final Logger logger = Logger.getLogger(DSLResourceImpl.class.getPackage().getName());

	/**
	 * Convenient method to retrieve resource data
	 * 
	 * @return
	 */
	@Override
	public DSLSystem getDSLSystem() {
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

	/**
	 * Load the resource data of this resource.
	 * 
	 * @return the resource data.
	 * @throws IOFlexoException
	 */
	@Override
	public DSLSystem loadResourceData() throws IOFlexoException {

		if (getFlexoIOStreamDelegate() == null) {
			throw new IOFlexoException("Cannot load document with this IO/delegate: " + getIODelegate());
		}

		DSLSystem resourceData = null;
		try {
			resourceData = loadDocument(getFlexoIOStreamDelegate());
			getInputStream().close();
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

	/**
	 * Provides hook when {@link ResourceData} is unloaded
	 */
	@Override
	public void unloadResourceData(boolean deleteResourceData) {
		super.unloadResourceData(deleteResourceData);
	}

	/**
	 * Return type of {@link ResourceData}
	 */
	@Override
	public Class<DSLSystem> getResourceDataClass() {
		return DSLSystem.class;
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

	/**
	 * Resource saving safe implementation<br>
	 * Initial resource is first copied, then we write in a temporary file, renamed at the end when the seriaization has been successfully
	 * performed
	 */
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
				temporaryFile = ((FileIODelegate) getIODelegate()).createTemporaryArtefact(".txt");
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

	/**
	 * {@link ResourceData} internal loading implementation<br>
	 * (trivial here)
	 * 
	 * @param ioDelegate
	 * @return
	 * @throws IOException
	 */
	private <I> DSLSystem loadDocument(StreamIODelegate<I> ioDelegate) throws IOException {

		// BufferedReader br = new BufferedReader(new InputStreamReader(ioDelegate.getInputStream()));

		System.out.println("Et la on vient lire la resource " + getIODelegate().getSerializationArtefact());

		try {
			DSLSystem returned = DSLParser.parse(ioDelegate.getInputStream(), getFactory());
			ioDelegate.getInputStream().close();
			return returned;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * {@link ResourceData} internal saving implementation<br>
	 * (trivial here)
	 * 
	 * @throws IOException
	 */
	private void write(OutputStream out) throws SaveResourceException {
		logger.info("Writing " + getIODelegate().getSerializationArtefact());
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			// TODO
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new SaveResourceException(getIODelegate());
		} finally {
			try {
				out.close();
			} catch (IOException e) {
			}
		}
		logger.info("Wrote " + getIODelegate().getSerializationArtefact());
	}

}
