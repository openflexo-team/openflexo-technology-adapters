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

import java.util.Collection;

import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import org.openflexo.pamela.annotations.Adder;
import org.openflexo.pamela.annotations.CloningStrategy;
import org.openflexo.pamela.annotations.Embedded;
import org.openflexo.pamela.annotations.Finder;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PastingPoint;
import org.openflexo.pamela.annotations.Remover;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.CloningStrategy.StrategyType;
import org.openflexo.pamela.annotations.Getter.Cardinality;

@ModelEntity
@ImplementationClass(XMLMetaModelImpl.class)
public interface XMLMetaModel extends XMLObject, FlexoMetaModel<XMLMetaModel> {

	public static String TYPES = "types";
	public static String READ_ONLY = "readOnly";

	// static simple Types URI

	public static String STR_SIMPLETYPE_URI = "xs:string";

	@Getter(value = TYPES, cardinality = Cardinality.LIST)
	@CloningStrategy(StrategyType.IGNORE)
	@Embedded
	public Collection<? extends XMLType> getTypes();

	@Finder(attribute = XMLType.URI, collection = TYPES, isMultiValued = true)
	public XMLType getTypeFromURI(String string);

	/**
	 * Creates a new type in this MetaModel, simple or complex, depending on the parameters
	 * 
	 * @param uri
	 * @param localName
	 * @param simpleType
	 * @return
	 */
	public XMLType createNewType(String uri, String localName, boolean simpleType);

	@Adder(TYPES)
	@PastingPoint
	public void addType(XMLType aType);

	@Remover(TYPES)
	public void removeType(XMLType aType);

	@Override
	@Getter(value = READ_ONLY, defaultValue = "true")
	public boolean isReadOnly();

	@Setter(READ_ONLY)
	public void setReadOnly(boolean value);

}
