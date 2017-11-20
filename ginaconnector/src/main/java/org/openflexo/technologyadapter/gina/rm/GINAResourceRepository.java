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

import java.io.IOException;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResourceRepository;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;

@ModelEntity
public interface GINAResourceRepository<I>
		extends TechnologyAdapterResourceRepository<GINAFIBComponentResource, GINATechnologyAdapter, GINAFIBComponent, I> {

	public static <I> GINAResourceRepository<I> instanciateNewRepository(GINATechnologyAdapter technologyAdapter,
			FlexoResourceCenter<I> resourceCenter) throws IOException {
		ModelFactory factory;
		try {
			factory = new ModelFactory(GINAResourceRepository.class);
			GINAResourceRepository<I> newRepository = factory.newInstance(GINAResourceRepository.class);
			newRepository.setTechnologyAdapter(technologyAdapter);
			newRepository.setResourceCenter(resourceCenter);
			newRepository.setBaseArtefact(resourceCenter.getBaseArtefact());
			newRepository.getRootFolder().setRepositoryContext(null);
			return newRepository;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

}
