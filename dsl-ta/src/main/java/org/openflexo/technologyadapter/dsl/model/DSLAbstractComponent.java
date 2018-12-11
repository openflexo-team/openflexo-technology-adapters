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

package org.openflexo.technologyadapter.dsl.model;

import java.util.logging.Logger;

import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;

/**
 * Represents an abstract component of a {@link DSLSystem} (a {@link DSLComponent} or a {@link DSLLink})
 * 
 * Note: Purpose of that class is to demonstrate API of a {@link TechnologyAdapter}, thus the semantics is here very basic
 * 
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
public interface DSLAbstractComponent extends DSLObject {

	@PropertyIdentifier(type = DSLSystem.class)
	public static final String SYSTEM_KEY = "system";

	/**
	 * Return {@link DSLSystem} where this {@link DSLAbstractComponent} is defined
	 * 
	 * @return
	 */
	@Getter(value = SYSTEM_KEY)
	public DSLSystem getSystem();

	/**
	 * Sets {@link DSLSystem} where this {@link DSLAbstractComponent} is defined
	 * 
	 * @param workbook
	 */
	@Setter(SYSTEM_KEY)
	public void setSystem(DSLSystem system);

	/**
	 * Default base implementation for {@link DSLAbstractComponent}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class DSLAbstractComponentImpl extends DSLObjectImpl implements DSLAbstractComponent {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(DSLAbstractComponent.class.getPackage().getName());

		@Override
		public DSLSystem getResourceData() {
			return getSystem();
		}

	}
}
