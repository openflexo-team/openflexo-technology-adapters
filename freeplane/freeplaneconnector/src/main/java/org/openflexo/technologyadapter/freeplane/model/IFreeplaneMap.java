/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freeplane, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
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
