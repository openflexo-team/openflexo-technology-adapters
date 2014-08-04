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

package org.openflexo.technologyadapter.xml.model;

import java.lang.reflect.Type;
import java.util.List;

import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import org.openflexo.foundation.technologyadapter.FlexoModel;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Finder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Initializer;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Parameter;
import org.openflexo.model.annotations.PastingPoint;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.xml.IXMLType;

/**
 * @author xtof
 * 
 */
@ModelEntity
@ImplementationClass(XMLModelImpl.class)
public interface XMLModel extends XMLObject, FlexoModel<XMLModel, XMLMetaModel>, FlexoMetaModel<XMLModel>,
			IXMLModel, TechnologyObject<XMLTechnologyAdapter> {

	public static final String MM = "metaModel";
	public static final String RSC = "resource";
	public static final String IND = "individuals";
	public static final String ROOT = "root";
	// public static final String TA = "technologyAdapter";


	@Initializer
	public XMLModel init();

	@Initializer
	public XMLModel init(@Parameter(MM) XMLMetaModel mm);
/*
	@Initializer
	public XMLModel init(@Parameter(TA) TechnologyAdapter ta, @Parameter(MM) XMLMetaModel mm);
	
	
	@Override
	@Getter(TA)
	XMLTechnologyAdapter getTechnologyAdapter();
	*/
	
	@Override
	@Getter(MM)
	XMLMetaModel getMetaModel();
	
	@Setter(MM)
	void setMetaModel(XMLMetaModel mm);
	

	@Override
	@Getter(RSC)
	public FlexoResource<XMLModel> getResource();

	@Override
	@Setter(RSC)
	public void setResource(FlexoResource<XMLModel> resource);
	
	@Override
	@Getter(ROOT)
	public XMLIndividual getRoot();
	
	@Setter(ROOT)
	public void setRoot(XMLIndividual indiv);
	
	@Getter(value = IND, cardinality = Cardinality.LIST)
	@CloningStrategy(StrategyType.CLONE)
	@Embedded
	public List<? extends XMLIndividual> getIndividuals();


	//TODO ask Syl pourkoi on ne peut pas avoir +eurs adders...
	@Override
	public Object addNewIndividual(Type aType);
	
	@Adder(IND)
	@PastingPoint
	public void addIndividual(XMLIndividual ind);

	@Remover(IND)
	public void removeFromNIndividuals(XMLIndividual ind);
	
	@Finder(attribute = XMLIndividual.TYPE, collection = IND, isMultiValued = true)
	public List<? extends XMLIndividual> getIndividualsOfType(IXMLType aType);


}
