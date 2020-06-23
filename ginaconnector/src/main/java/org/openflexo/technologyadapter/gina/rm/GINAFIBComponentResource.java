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

package org.openflexo.technologyadapter.gina.rm;

import org.openflexo.foundation.resource.PamelaResource;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.GINATechnologyContextManager;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.technologyadapter.gina.model.GINAFactory;

@ModelEntity
@ImplementationClass(GINAFIBComponentResourceImpl.class)
public interface GINAFIBComponentResource extends
		TechnologyAdapterResource<GINAFIBComponent, GINATechnologyAdapter>,
		PamelaResource<GINAFIBComponent, GINAFactory>
{

	String TECHNOLOGY_CONTEXT_MANAGER = "technologyContextManager";

	@Getter(value = "technologyContextManager", ignoreType = true)
	GINATechnologyContextManager getTechnologyContextManager();

	@Setter("technologyContextManager")
	void setTechnologyContextManager(GINATechnologyContextManager paramGINTechnologyContextManager);
}
