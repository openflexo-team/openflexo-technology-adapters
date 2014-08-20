/*
 * (c) Copyright 2014- Openflexo
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

import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResource;


public abstract class XSDMetaModelImpl extends XMLMetaModelImpl implements XSDMetaModel {
//public abstract class XSDMetaModelImpl extends XSOntology implements XMLMetaModel {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger.getLogger(XSDMetaModelImpl.class.getPackage()
			.getName());
	

	private XSDMetaModelResource xsdResource;
	
    private static ModelFactory MF;
    
    static{
    	try {
			 MF = new ModelFactory(XSDMetaModel.class);
		} catch (ModelDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

	public static ModelFactory getModelFactory() {
		return MF;
	}


	@Override
	public FlexoResource<XMLMetaModel> getResource() {
		return xsdResource;
	}
	
	@Override
	public void setResource(FlexoResource<XMLMetaModel> resource) {
		this.xsdResource = (XSDMetaModelResource) resource;
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	public void setIsReadOnly(boolean b) {
	}

	@Override
	public XMLTechnologyAdapter getTechnologyAdapter() {
		if (this.xsdResource != null)
			return xsdResource.getTechnologyAdapter();
		return null;
	}

}
