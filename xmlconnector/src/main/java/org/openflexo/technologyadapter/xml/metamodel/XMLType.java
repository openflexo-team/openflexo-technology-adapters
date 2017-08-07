/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

@ModelEntity
@ImplementationClass(XMLTypeImpl.class)
public interface XMLType extends XMLObject, Type {

	public final String MM = "metamodel";

	// TODO : manage the calculation of FQN
	// TODO: check emboitage avec URI et NSPrexiw => FQN
	public final String FQN = "fullyQualifiedName";
	public final String SUPERTYPE = "superType";
	public final String ABSTRACT = "abstract";

	static final String NAME_ATTR = "name";

	@Initializer
	public XMLType init(@Parameter(MM) XMLMetaModel mm);

	@Getter(FQN)
	public String getFullyQualifiedName();

	@Getter(MM)
	XMLMetaModel getMetamodel();

	@Setter(NAME)
	public void setName(String name);

	@Getter(SUPERTYPE)
	public XMLType getSuperType();

	@Setter(SUPERTYPE)
	public void setSuperType(XMLType t);

	@Getter(value = ABSTRACT, defaultValue = "false")
	public boolean isAbstract();

	@Setter(ABSTRACT)
	public void setIsAbstract(boolean t);

}
