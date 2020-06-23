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

import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;

/**
 * Represents a simple line of a {@link XXText}
 * 
 * Note: Purpose of that class is to demonstrate API of a {@link TechnologyAdapter}, thus the semantics is here pretty simple: a
 * {@link XXText} is a plain text file contents, serialized as a {@link String}, and a {@link XXLine} is a line of that file, represented as
 * a String
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(value = XXLine.XXLineImpl.class)
@XMLElement
public interface XXLine extends XXObject {

	@PropertyIdentifier(type = XXText.class)
	public static final String XX_TEXT_KEY = "XXText";
	@PropertyIdentifier(type = String.class)
	public static final String VALUE_KEY = "value";
	@PropertyIdentifier(type = Integer.class)
	public static final String INDEX_KEY = "index";

	/**
	 * Return {@link XXText} where this {@link XXLine} is defined
	 * 
	 * @return
	 */
	@Getter(value = XX_TEXT_KEY)
	public XXText getXXText();

	/**
	 * Sets {@link XXText} where this {@link XXLine} is defined
	 * 
	 * @param text
	 */
	@Setter(XX_TEXT_KEY)
	public void setXXText(XXText text);

	/**
	 * Return value for this {@link XXLine}, as a String representing the line
	 * 
	 * @return
	 */
	@Getter(value = VALUE_KEY)
	public String getValue();

	/**
	 * Sets value for this {@link XXLine}, as a String representing the line
	 * 
	 * @return
	 */
	@Setter(VALUE_KEY)
	public void setValue(String aValue);

	/**
	 * Return index of line
	 * 
	 * @return
	 */
	@Getter(value = INDEX_KEY, defaultValue = "-1")
	public int getIndex();

	/**
	 * Sets index of line
	 * 
	 * @return
	 */
	@Setter(INDEX_KEY)
	public void setIndex(int index);

	/**
	 * Default base implementation for {@link XXLine}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class XXLineImpl extends XXObjectImpl implements XXLine {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(XXLine.class.getPackage().getName());

		public XXLineImpl() {
		}

		@Override
		public XXText getResourceData() {
			return getXXText();
		}

	}
}
