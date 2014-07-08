/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.freeplane.model;

import org.freeplane.features.map.MapModel;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.model.impl.FreeplaneMapImpl;

@ModelEntity
@ImplementationClass(value = FreeplaneMapImpl.class)
public interface IFreeplaneMap extends TechnologyObject<FreeplaneTechnologyAdapter>, ResourceData<IFreeplaneMap> {

	public static final String MAP_MODEL_KEY = "mapModel";

	public static final String ROOT_NODE_KEY = "rootNode";

	/**
	 * 
	 * @return the instance of the freeplane MapMadel object
	 */
	@Getter(value = MAP_MODEL_KEY, ignoreType = true)
	public MapModel getMapModel();

	@Setter(value = MAP_MODEL_KEY)
	public void setMapModel(MapModel map);

	@Getter(value = ROOT_NODE_KEY)
	public IFreeplaneNode getRoot();

	@Setter(value = ROOT_NODE_KEY)
	public void setRootNode(IFreeplaneNode node);

	public String getUri();

}
