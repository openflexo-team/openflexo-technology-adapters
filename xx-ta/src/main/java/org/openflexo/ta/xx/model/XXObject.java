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

package org.openflexo.ta.xx.model;

import java.util.logging.Logger;

import org.openflexo.foundation.InnerResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.ta.xx.XXTechnologyAdapter;

/**
 * Common API for all objects involved in XX model
 * 
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
public interface XXObject extends InnerResourceData<XXText>, TechnologyObject<XXTechnologyAdapter> {

	public XXModelFactory getFactory();

	/**
	 * Default base implementation for {@link XXObject}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class XXObjectImpl extends FlexoObjectImpl implements XXObject {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(XXObjectImpl.class.getPackage().getName());

		@Override
		public XXTechnologyAdapter getTechnologyAdapter() {
			if (getResourceData() != null && getResourceData().getResource() != null) {
				return getResourceData().getResource().getTechnologyAdapter();
			}
			return null;
		}

		@Override
		public XXModelFactory getFactory() {
			return getResourceData().getResource().getFactory();
		}

	}
}
