/*
 * (c) Copyright 2013-2014 Openflexo
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
import java.util.Collection;

import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Finder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PastingPoint;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;


@ModelEntity
@ImplementationClass(XMLMetaModelImpl.class)
public interface XMLMetaModel extends XMLObject, FlexoMetaModel<XMLMetaModel>, TechnologyObject<XMLTechnologyAdapter> {

	public static String TYPES = "types";
	public static String READ_ONLY = "readOnly";
	

	@Getter(value = TYPES, cardinality = Cardinality.LIST)
	@CloningStrategy(StrategyType.IGNORE)
	@Embedded
	public Collection<? extends XMLType> getTypes();


	@Finder(attribute = XMLType.URI, collection = TYPES, isMultiValued = true)
	public Type getTypeFromURI(String string);

	public Type createNewType(String uri, String localName);
	
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
