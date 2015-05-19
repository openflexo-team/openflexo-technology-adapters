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

package org.openflexo.technologyadapter.freeplane.model.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.map.MapModel;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.freeplane.FreeplaneTechnologyAdapter;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;

public abstract class FreeplaneMapImpl extends FlexoObjectImpl implements IFreeplaneMap {

	private static final Logger LOGGER = Logger.getLogger(FreeplaneMapImpl.class.getPackage().getName());

	private FreeplaneTechnologyAdapter technologyAdapter;

	private MapModel mapModel;

	public FreeplaneMapImpl() {
	}

	public void setTechnologyAdapter(final FreeplaneTechnologyAdapter technologyAdapter) {
		this.technologyAdapter = technologyAdapter;
	}

	/* (non-Javadoc)
	 * @see org.openflexo.foundation.technologyadapter.TechnologyObject#getTechnologyAdapter()
	 */
	@Override
	public FreeplaneTechnologyAdapter getTechnologyAdapter() {
		return this.technologyAdapter;
	}

	@Override
	@Setter(value = MAP_MODEL_KEY)
	public void setMapModel(final MapModel map) {
		this.mapModel = map;
		try {
			final ModelFactory factory = new ModelFactory(IFreeplaneNode.class);
			final FreeplaneNodeImpl node = (FreeplaneNodeImpl) factory.newInstance(IFreeplaneNode.class);
			node.setTechnologyAdapter(getTechnologyAdapter());
			// recursive call inside, don't be preoccupied by children
			// initialization.
			node.setNodeModel(map.getRootNode());
			setRootNode(node);
		} catch (final ModelDefinitionException e) {
			final String msg = "Error while setting MapModel of map " + this;
			LOGGER.log(Level.SEVERE, msg, e);
		}
	}

	@Override
	@Getter(value = MAP_MODEL_KEY, ignoreType = true)
	public MapModel getMapModel() {
		return this.mapModel;
	}

	@Override
	public String getUri() {
		return "MAP=" + getMapModel().getTitle() + ",Root" + getRoot().getUri();
	}
}
