/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2012-2013 Openflexo
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
public interface XMLType  extends XMLObject , Type{

	public final String MM = "metamodel";

	// TODO : manage the calculation of FQN
	// TODO: check emnboitage avec URI et NSPrexiw => FQN
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
