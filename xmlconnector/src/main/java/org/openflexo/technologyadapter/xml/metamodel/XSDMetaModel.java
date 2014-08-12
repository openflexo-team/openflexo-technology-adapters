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
import java.util.List;

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
import org.openflexo.technologyadapter.xml.model.XMLType;


@ModelEntity
@ImplementationClass(XSDMetaModelImpl.class)
public interface XSDMetaModel extends XMLMetaModel {

	public static String TYPES = "types";
	

	@Override
	@Getter(value = TYPES, cardinality = Cardinality.LIST)
	@CloningStrategy(StrategyType.IGNORE)
	@Embedded
	public List<? extends XMLType> getTypes();


	@Override
	@Finder(attribute = XMLType.URI, collection = TYPES, isMultiValued = true)
	public Type getTypeFromURI(String string);

	@Override
	public Type createNewType(String uri, String localName);
	
	@Override
	@Adder(TYPES)
	@PastingPoint
	public void addType(XMLType aType);

	@Override
	@Remover(TYPES)
	public void removeType(XMLType aType);
	


/*
	@Override
	@Getter(value = "resource")
	public FlexoResource<XMLMetaModel> getResource();

	@Override
	@Setter(value = "resource")
	public void setResource(FlexoResource<XMLMetaModel> resource);

	public IFlexoOntologyDataProperty getDataProperty(String propertyURI);
	*/
}
