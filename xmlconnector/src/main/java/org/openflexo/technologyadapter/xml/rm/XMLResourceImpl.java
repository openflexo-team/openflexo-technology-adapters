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

package org.openflexo.technologyadapter.xml.rm;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.technologyadapter.FlexoMetaModelResource;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLModelImpl;

public abstract class XMLResourceImpl extends FlexoResourceImpl<XMLModel> implements XMLResource {
	
	protected static final Logger logger   = Logger.getLogger(XMLResourceImpl.class.getPackage().getName());


	@Override
    public XMLModel getModel() {
        return getModelData();
    }

	@Override
	public XMLModel getModelData() {

		if (resourceData == null) {
			resourceData =  XMLModelImpl.getModelFactory().newInstance(XMLModel.class);
			//, getTechnologyAdapter()); 
			//new XMLModel(this.getTechnologyAdapter());
			resourceData.setResource(this);
		}

		if (!isLoaded()) {
			try {
				resourceData = loadResourceData(null);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ResourceLoadingCancelledException e) {
				e.printStackTrace();
			} catch (FlexoException e) {
				e.printStackTrace();
			}
		}
		return resourceData;
	}

	@Override
	public void attachMetamodel(){
		FlexoMetaModelResource<XMLModel, XMLMetaModel, XMLTechnologyAdapter> mmRes = this.getMetaModelResource();
		if (mmRes != null) {
			resourceData.setMetaModel(mmRes.getMetaModelData());
		}
		if (resourceData.getMetaModel() == null) {
			logger.warning("Setting a null Metamodel for Model " + this.getURI());
		}
	}
	
}
