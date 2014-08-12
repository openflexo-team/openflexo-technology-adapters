/*
 * (c) Copyright 2010-2012 AgileBirds
 * (c) Copyright 2012-2014 Openflexo
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
package org.openflexo.technologyadapter.xml.model;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Initializer;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Parameter;
import org.openflexo.model.annotations.PastingPoint;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.xml.IXMLIndividual;
/**
 * 
 * an XMLIndividual represents a single instance of XML Element in a XMLModel
 * 
 * 
 * @author xtof
 * 
 */
@ModelEntity
@ImplementationClass(XMLIndividualImpl.class)
public interface XMLIndividual extends XMLObject, IXMLIndividual<XMLIndividual, XMLAttribute> {

	public static final String TYPE = "myType";
	public static final String MODEL = "containerModel";
	public static final String _UUID = "uuid";
	public static final String CHILD = "children";
	public static final String PARENT = "parent";
	public static final String ATTR = "attributes";

	@Initializer
	public XMLIndividual init(@Parameter(MODEL) XMLModel m, @Parameter(TYPE) XMLType t);

	@Getter(MODEL)
    public XMLModel getContainerModel();

	@Override
	@Getter(TYPE)
	public XMLType getType();
	
	@Override
	@Setter(TYPE)
	public void setType(Type aType);
	
	@Override
	@Getter(_UUID)
	public String getUUID();
	

	@Override
	@Getter(PARENT)
	public XMLIndividual getParent();
	
	@Setter(PARENT)
	public void setParent(XMLIndividual xmlind);
	

	@Override
	@Getter(value = CHILD, cardinality = Cardinality.LIST, inverse = PARENT)
	@CloningStrategy(StrategyType.IGNORE)
	@Embedded
	public List<XMLIndividual> getChildren();

	@Remover(CHILD)
	public void removeChild(XMLIndividual ind);

	@Adder(CHILD)
	@PastingPoint
	public void addChild(XMLIndividual ind);
	
	@Override
	@Getter(value = ATTR, cardinality = Cardinality.LIST)
	public Collection<? extends XMLAttribute> getAttributes();
	
	@Override
    public Object createAttribute(String attrLName, Type aType, String value);

	@Adder(value = ATTR)
	public Object addAttribute(XMLAttribute attr);
	
	@Remover(value = ATTR)
	public Object deleteAttribute(XMLAttribute attr);
	
}
