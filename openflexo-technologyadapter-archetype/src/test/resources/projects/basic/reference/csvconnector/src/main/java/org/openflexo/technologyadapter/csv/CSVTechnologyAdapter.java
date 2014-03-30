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

package org.openflexo.technologyadapter.csv;

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
import org.openflexo.technologyadapter.csv.rm.CSVMetaModelResource;
import org.openflexo.technologyadapter.csv.rm.CSVModelResource;

import org.openflexo.technologyadapter.csv.CSVTypeAwareModelSlot;
import org.openflexo.technologyadapter.csv.rm.CSVMetaModelRepository;
import org.openflexo.technologyadapter.csv.rm.CSVModelRepository;


/**
 * This class defines and implements the CSV technology adapter
 * 
 * @author Jean Le Paon
 * 
 */

@DeclareModelSlots({ // ModelSlot(s) declaration
@DeclareModelSlot(FML = "CSVTypeAwareModelSlot", modelSlotClass = CSVTypeAwareModelSlot.class), 
})
@DeclareRepositoryType({ CSVMetaModelRepository.class, CSVModelRepository.class })
public class CSVTechnologyAdapter extends TechnologyAdapter {
	private static String CSV_FILE_EXTENSION = ".csv";

	protected static final Logger logger = Logger.getLogger(CSVTechnologyAdapter.class.getPackage().getName());

	public CSVTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return new String("CSV Technology Adapter");
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
	

	public CSVModelResource createNewCSVModel(FlexoProject project,
			String filename, String modelUri,
			CSVMetaModelResource metaModelResource) {
		// TODO Auto-generated method stub
		return null;
	}

	public CSVModelResource createNewCSVModel(
			FileSystemBasedResourceCenter resourceCenter, String relativePath,
			String filename, String modelUri,
			CSVMetaModelResource metaModelResource) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
