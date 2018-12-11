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

package org.openflexo.ta.dsl.model;

import java.util.logging.Logger;

import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;

/**
 * Represents a component of a {@link DSLSystem}
 * 
 * Note: Purpose of that class is to demonstrate API of a {@link TechnologyAdapter}, thus the semantics is here pretty simple: a
 * {@link DSLSystem} is a plain text file contents, serialized as a {@link String}, and a {@link DSLSlot} is a line of that file,
 * represented as a String
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(value = DSLSlot.DSLSlotImpl.class)
@XMLElement
public interface DSLSlot extends DSLObject {

	@PropertyIdentifier(type = DSLComponent.class)
	public static final String COMPONENT_KEY = "component";

	/**
	 * Return {@link DSLComponent} where this {@link DSLSlot} is defined
	 * 
	 * @return
	 */
	@Getter(value = COMPONENT_KEY)
	public DSLComponent getComponent();

	/**
	 * Sets {@link DSLComponent} where this {@link DSLSlot} is defined
	 * 
	 * @param workbook
	 */
	@Setter(COMPONENT_KEY)
	public void setComponent(DSLComponent component);

	/**
	 * Default base implementation for {@link DSLSlot}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class DSLSlotImpl extends DSLObjectImpl implements DSLSlot {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(DSLSlot.class.getPackage().getName());

		@Override
		public DSLSystem getResourceData() {
			return getComponent().getResourceData();
		}

		@Override
		public String toString() {
			return "[Slot]" + getComponent().getName() + "." + getName();
		}
	}
}
