/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.model;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.InnerResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;

/**
 * Common API for all objects involved in Excel model
 * 
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
public interface ExcelObject extends FlexoObject, InnerResourceData<ExcelWorkbook>, TechnologyObject<ExcelTechnologyAdapter> {

	public String getSerializationIdentifier();

	public ExcelModelFactory getFactory();

	/**
	 * Default base implementation for {@link ExcelObject}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class ExcelObjectImpl extends FlexoObjectImpl implements ExcelObject {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(ExcelObjectImpl.class.getPackage().getName());

		@Override
		public ExcelTechnologyAdapter getTechnologyAdapter() {
			if (getResourceData() != null && getResourceData().getResource() != null) {
				return getResourceData().getResource().getTechnologyAdapter();
			}
			return null;
		}

		@Override
		public ExcelModelFactory getFactory() {
			return getResourceData().getResource().getFactory();
		}

		@Override
		public final String getSerializationIdentifier() {
			if (getResourceData() != null) {
				return getResourceData().getResource().getConverter().toSerializationIdentifier(this);
			}
			return "???";
		}

		@Override
		public String toString() {
			return getImplementedInterface().getSimpleName() + "-" + getSerializationIdentifier();
		}

	}
}
