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
 * Represents a link of a {@link DSLSystem}
 * 
 * Note: Purpose of that class is to demonstrate API of a {@link TechnologyAdapter}, thus the semantics is very basic
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(value = DSLLink.DSLComponentImpl.class)
@XMLElement
public interface DSLLink extends DSLAbstractComponent {

	@PropertyIdentifier(type = DSLSlot.class)
	public static final String START_SLOT_KEY = "startSlot";
	@PropertyIdentifier(type = DSLSlot.class)
	public static final String END_SLOT_KEY = "endSlot";

	/**
	 * Return start {@link DSLSlot} of this link
	 * 
	 * @return
	 */
	@Getter(value = START_SLOT_KEY)
	public DSLSlot getStartSlot();

	/**
	 * Sets start {@link DSLSlot} of this link
	 * 
	 * @param workbook
	 */
	@Setter(START_SLOT_KEY)
	public void setStartSlot(DSLSlot slot);

	/**
	 * Return end {@link DSLSlot} of this link
	 * 
	 * @return
	 */
	@Getter(value = END_SLOT_KEY)
	public DSLSlot getEndSlot();

	/**
	 * Sets end {@link DSLSlot} of this link
	 * 
	 * @param workbook
	 */
	@Setter(END_SLOT_KEY)
	public void setEndSlot(DSLSlot slot);

	/**
	 * Default base implementation for {@link DSLLink}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class DSLComponentImpl extends DSLAbstractComponentImpl implements DSLLink {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(DSLLink.class.getPackage().getName());

		@Override
		public String toString() {
			return "[Link]" + getStartSlot() + "-" + getEndSlot();
		}

	}
}
