/*
 * (c) Copyright 2010-2011 AgileBirds
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
package org.openflexo.technologyadapter.emf.rm;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.ModelRepository;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;

public class EMFModelRepository extends ModelRepository<EMFModelResource, EMFModel, EMFMetaModel, EMFTechnologyAdapter> {

	public EMFModelRepository(EMFTechnologyAdapter adapter, FlexoResourceCenter<?> resourceCenter) {
		super(adapter, resourceCenter);
	}

	private static final String DEFAULT_BASE_URI = "http://www.openflexo.org/EMFTechnologyAdapter/Models";

	@Override
	public String getDefaultBaseURI() {
		return DEFAULT_BASE_URI;
	}

}
