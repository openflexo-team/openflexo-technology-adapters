
/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.csv.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoFileResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.csv.metamodel.CSVMetaModel;
import org.openflexo.toolbox.IProgress;

/**
 * CSV MetaModel Resource Implementation.
 * 
 * @author gbesancon
 */
public abstract class CSVMetaModelResourceImpl extends FlexoFileResourceImpl<CSVMetaModel> implements CSVMetaModelResource {

	protected static final Logger logger = Logger.getLogger(CSVMetaModelResourceImpl.class.getPackage().getName());

	/**
	 * 
	 * Follow the link.
	 * 
	 * @see org.openflexo.technologyadapter.csv.rm.CSVMetaModelResource#getMetaModel()
	 */
	@Override
	public CSVMetaModel getMetaModelData() {
		try {
			return getResourceData(null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
		} catch (FlexoException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.resource.FlexoResource#loadResourceData(org.openflexo.toolbox.IProgress)
	 */
	@Override
	public CSVMetaModel loadResourceData(IProgress progress) throws ResourceLoadingCancelledException {

		// TODO : Implement Resource Loading
		return null;
	}

	/**
	 * Follow the link.
	 * 
	 * @see org.openflexo.foundation.resource.FlexoResource#save(org.openflexo.toolbox.IProgress)
	 */
	@Override
	public void save(IProgress progress) {
		logger.info("MetaModel is not supposed to be modified.");
	}

	@Override
	public Class<CSVMetaModel> getResourceDataClass() {
		return CSVMetaModel.class;
	}

}
