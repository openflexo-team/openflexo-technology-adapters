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
package org.openflexo.technologyadapter.diagram.fml;

import org.openflexo.foundation.viewpoint.TechnologySpecificFlexoBehaviour;
import org.openflexo.model.annotations.Implementation;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;

/**
 * Represents a behavioural feature which triggers in the context of a diagram edition (interaction)
 * 
 * @author sylvain
 * 
 */
@ModelEntity(isAbstract = true)
public interface DiagramFlexoBehaviour extends TechnologySpecificFlexoBehaviour {

	@Override
	public DiagramTechnologyAdapter getTechnologyAdapter();

	@Implementation
	public static abstract class DiagramEditionSchemeImpl implements DiagramFlexoBehaviour {

		@Override
		public DiagramTechnologyAdapter getTechnologyAdapter() {
			if (getServiceManager() != null) {
				return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
			}
			return null;
		}
	}

}
