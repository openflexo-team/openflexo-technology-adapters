#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package};

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.DeclareModelSlot;
import org.openflexo.foundation.technologyadapter.DeclareModelSlots;
import org.openflexo.foundation.technologyadapter.DeclareRepositoryType;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.technologyadapter.csv.rm.${technologyPrefix}MetaModelResource;
import org.openflexo.technologyadapter.csv.rm.${technologyPrefix}ModelResource;

import ${package}.${technologyPrefix}TypeAwareModelSlot;
import ${package}.rm.${technologyPrefix}MetaModelRepository;
import ${package}.rm.${technologyPrefix}ModelRepository;


/**
 * This class defines and implements the ${technologyPrefix} technology adapter
 * 
 * @author ${author}
 * 
 */

@DeclareModelSlots({ // ModelSlot(s) declaration
@DeclareModelSlot(FML = "${technologyPrefix}TypeAwareModelSlot", modelSlotClass = ${technologyPrefix}TypeAwareModelSlot.class), 
})
@DeclareRepositoryType({ ${technologyPrefix}MetaModelRepository.class, ${technologyPrefix}ModelRepository.class })
public class ${technologyPrefix}TechnologyAdapter extends TechnologyAdapter {
	private static String ${technologyPrefix}_FILE_EXTENSION = ".${technologyExtension}";

	protected static final Logger logger = Logger.getLogger(${technologyPrefix}TechnologyAdapter.class.getPackage().getName());

	public ${technologyPrefix}TechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return new String("${technologyPrefix} Technology Adapter");
	}

	@Override
	public TechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <I> void initializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {
		// TODO Auto-generated method stub

	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <I> void contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub

	}

	@Override
	public <I> void contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		// TODO Auto-generated method stub

	}
	

	public ${technologyPrefix}ModelResource createNew${technologyPrefix}Model(FlexoProject project,
			String filename, String modelUri,
			${technologyPrefix}MetaModelResource metaModelResource) {
		// TODO Auto-generated method stub
		return null;
	}

	public ${technologyPrefix}ModelResource createNew${technologyPrefix}Model(
			FileSystemBasedResourceCenter resourceCenter, String relativePath,
			String filename, String modelUri,
			${technologyPrefix}MetaModelResource metaModelResource) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
