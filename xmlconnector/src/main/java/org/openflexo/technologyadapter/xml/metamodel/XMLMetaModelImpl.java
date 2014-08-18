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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.openflexo.model.ModelContextLibrary;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.xml.model.XMLModel;

/**
 * A simple MetaModeling Structure that is not backed up in an XSD file and where you can
 * create new types freely
 * 
 * @author xtof
 *
 */

public abstract class XMLMetaModelImpl  implements XMLMetaModel {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger.getLogger(XMLMetaModelImpl.class.getPackage()
			.getName());

	protected final Map<String, XMLType> types = new HashMap<String, XMLType>();

	 
    private static ModelFactory MF;
    
    static{
    	try {
			MF = new ModelFactory(ModelContextLibrary.getCompoundModelContext(XMLModel.class,
									  										  XMLType.class,
									  										  XMLProperty.class,
									  										  XMLDataProperty.class,
									  										  XMLObjectProperty.class));
		} catch (ModelDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

	public static ModelFactory getModelFactory() {
		return MF;
	}


	@Override
	public Type getTypeFromURI(String uri) {
		return types.get(uri);
	}

	@Override
	public void addType(XMLType aType){
		types.put(aType.getURI(), aType);
	}

	@Override
	public void removeType(XMLType aType){
		types.remove(aType);
	}

	@Override
	public Collection<? extends XMLType> getTypes(){
		return types.values();
	}

	@Override
	public Type createNewType(String uri, String localName) {

		XMLType aType = XMLMetaModelImpl.getModelFactory().newInstance(XMLType.class,this);
		aType.setIsAbstract(false);
		aType.setURI(uri);
		aType.setName(localName);
		addType(aType);
		return aType;
	}


}
