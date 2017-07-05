/**
 * 
 * Copyright (c) 2014-2017, Openflexo
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.openflexo.foundation.resource.FileIODelegate;
import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.toolbox.FileUtils;

/**
 * @author xtof
 * 
 */
@Deprecated
public abstract class XMLFileResourceImpl extends XMLResourceImpl implements XMLFileResource {

	public void _saveResourceData(boolean clearIsModified) throws SaveResourceException {

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
				temporaryFile = ((FileIODelegate) getIODelegate()).createTemporaryArtefact(".OF_TEMP.xml");
				if (logger.isLoggable(Level.FINE)) {
					logger.finer("Creating temp file " + temporaryFile.getAbsolutePath());
				}
				write(new FileOutputStream(temporaryFile));
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

	private void makeLocalCopy(File file) throws IOException {
		if (file != null && file.exists()) {
			String localCopyName = file.getName() + "~";
			File localCopy = new File(file.getParentFile(), localCopyName);
			FileUtils.copyFileToFile(file, localCopy);
		}
	}

}
