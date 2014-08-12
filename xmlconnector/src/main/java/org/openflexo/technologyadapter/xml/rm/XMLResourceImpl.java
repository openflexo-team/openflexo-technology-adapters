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

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLModelImpl;

public abstract class XMLResourceImpl extends FlexoResourceImpl<XMLModel> implements XMLResource {


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


}
