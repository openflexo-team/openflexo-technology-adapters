/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.xml.metamodel;

import java.lang.reflect.Type;

import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Initializer;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Parameter;
import org.openflexo.model.annotations.Setter;

/**
 * 
 * Represents an XML Attribute in an XMLModel
 * 
 * @author xtof
 * 
 */
@ModelEntity
@ImplementationClass(XMLPropertyImpl.class)
public interface XMLProperty extends XMLObject, Comparable<XMLProperty> {

	/**
	 * The Type of the given attribute. This might be a simple type
	 */
	public static final String TYPE = "myType";
	/**
	 * XMLType containing the given attribute
	 */
	public static final String CONTAINER = "container";
	/**
	 * This indicates if property was created from an XML element or attribute
	 */

	public static final String IS_FROM_ELEMENT = "isFromElement";

	@Initializer
	public XMLProperty init(@Parameter(NAME) String s, @Parameter(TYPE) Type t, @Parameter(CONTAINER) XMLType container);

	@Getter(CONTAINER)
	public XMLType getContainer();

	@Setter(CONTAINER)
	public void setContainer(XMLType containedIn);

	@Getter(value = TYPE, ignoreType = true)
	public XMLType getType();

	@Setter(TYPE)
	public void setType(XMLType aType);

	/**
	 * Returns true if this property was created from an XML element and false if from an XMLAttribute
	 * 
	 * @return
	 */
	@Getter(value = IS_FROM_ELEMENT, defaultValue = "false")
	public boolean isFromXMLElement();

	@Setter(IS_FROM_ELEMENT)
	public void setIsFromElement(boolean fromElement);

}
