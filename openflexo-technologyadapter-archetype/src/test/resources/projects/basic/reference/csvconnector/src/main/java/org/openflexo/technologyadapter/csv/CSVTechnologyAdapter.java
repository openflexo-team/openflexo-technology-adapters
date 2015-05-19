/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Openflexo-technologyadapter-archetype, a component of the software infrastructure 
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


package org.openflexo.technologyadapter.csv;

import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareRepositoryType;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.foundation.fml.VirtualModel;
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

@DeclareModelSlots({CSVTypeAwareModelSlot.class})
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
