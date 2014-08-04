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
import java.util.HashMap;
import java.util.Map;

import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.xml.model.XMLType;


public abstract class XMLMetaModelImpl  implements XMLMetaModel {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger.getLogger(XMLMetaModelImpl.class.getPackage()
			.getName());

	protected final Map<String, XMLType> types = new HashMap<String, XMLType>();

	 
    private static ModelFactory MF;
    
    static{
    	try {
			 MF = new ModelFactory(XMLMetaModel.class);
		} catch (ModelDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

	public static ModelFactory getModelFactory() {
		return MF;
	}

	public XMLMetaModelImpl() {
		
	}
	
	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public void setIsReadOnly(boolean b) {
	}

	public void save() throws SaveResourceException {
		logger.warning("XSDMetaModels are not supposed to be saved !!!");
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
	public Type createNewType(String uri, String localName) {

		XMLType aType = XMLMetaModelImpl.getModelFactory().newInstance(XMLType.class,this);
		aType.setURI(uri);
		aType.setName(localName);
		addType(aType);
		return aType;
	}


}
