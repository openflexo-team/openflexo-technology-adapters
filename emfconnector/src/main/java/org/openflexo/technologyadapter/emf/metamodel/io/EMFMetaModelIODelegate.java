/**
 * 
 * Copyright (c) 2013-2015, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf.metamodel.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.openflexo.foundation.resource.FileWritingLock;
import org.openflexo.foundation.resource.FlexoIODelegate;
import org.openflexo.model.annotations.Implementation;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.emf.EMFTechnologyContextManager;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;
import org.openflexo.toolbox.FileUtils;

/**
 * An IO Delegate that can read an EMFMetaModel Resource
 * 
 * @author xtof
 */

@ModelEntity
@XMLElement
public interface EMFMetaModelIODelegate<I> extends FlexoIODelegate<I> {

	/** Loads the Metamodel and updates EMF registries **/
	public EMFMetaModel loadMetaModel(EMFTechnologyContextManager ctxtManager);

	@Implementation
	public abstract class EMFMetaModelIODelegateImpl<I> implements EMFMetaModelIODelegate<I> {

		protected static final Logger logger = Logger.getLogger(EMFMetaModelIODelegate.class.getPackage().getName());

		
		/** a Metamodel is ReadOnly and undeletable **/
		@Override
		public boolean isReadOnly() {
			return true;
		}

		@Override
		public boolean delete() {
			return false;
		}


		@Override
		public boolean hasWritePermission() {
			return false;
		}

		@Override
		public FileWritingLock willWriteOnDisk() {
			// Nothing todo has a MM is never written
			return null;
		}


		@Override
		public void hasWrittenOnDisk(FileWritingLock lock) {
			// Nothing todo has a MM is never written

		}

		@Override
		public void notifyHasBeenWrittenOnDisk() {
			// Nothing todo has a MM is never written

		}

	}


}
