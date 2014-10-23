
#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import ${package}.metamodel.${technologyPrefix}MetaModel;
import org.openflexo.toolbox.IProgress;

/**
 * ${technologyPrefix} MetaModel Resource Implementation.
 * 
 * @author gbesancon
 */
public abstract class ${technologyPrefix}MetaModelResourceImpl extends FlexoResourceImpl<${technologyPrefix}MetaModel> implements ${technologyPrefix}MetaModelResource {

	protected static final Logger logger = Logger.getLogger(${technologyPrefix}MetaModelResourceImpl.class.getPackage().getName());

	/**
	 * 
	 * Follow the link.
	 * 
	 * @see ${package}.rm.${technologyPrefix}MetaModelResource#getMetaModel()
	 */
	@Override
	public ${technologyPrefix}MetaModel getMetaModelData() {
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
	public ${technologyPrefix}MetaModel loadResourceData(IProgress progress) throws ResourceLoadingCancelledException {

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
	public Class<${technologyPrefix}MetaModel> getResourceDataClass() {
		return ${technologyPrefix}MetaModel.class;
	}

}
