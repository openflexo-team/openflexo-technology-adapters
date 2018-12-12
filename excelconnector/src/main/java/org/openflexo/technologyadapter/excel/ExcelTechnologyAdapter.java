/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel;

import java.util.logging.Logger;

import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareResourceFactory;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.technologyadapter.excel.fml.binding.ExcelBindingFactory;
import org.openflexo.technologyadapter.excel.model.ExcelCellRangeConverter;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookRepository;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResourceFactory;
import org.openflexo.technologyadapter.excel.semantics.fml.SEVirtualModelInstanceType.SEVirtualModelInstanceTypeFactory;
import org.openflexo.technologyadapter.excel.semantics.rm.SEVirtualModelInstanceRepository;
import org.openflexo.technologyadapter.excel.semantics.rm.SEVirtualModelInstanceResourceFactory;

/**
 * This class defines and implements the Excel technology adapter
 * 
 * @author sylvain, vincent, Christophe
 * 
 */
@DeclareModelSlots({ BasicExcelModelSlot.class, SemanticsExcelModelSlot.class })
@DeclareResourceFactory({ ExcelWorkbookResourceFactory.class, SEVirtualModelInstanceResourceFactory.class })
public class ExcelTechnologyAdapter extends TechnologyAdapter<ExcelTechnologyAdapter> {

	protected static final Logger logger = Logger.getLogger(ExcelTechnologyAdapter.class.getPackage().getName());

	private static final ExcelBindingFactory BINDING_FACTORY = new ExcelBindingFactory();

	@Override
	public String getName() {
		return "Excel technology adapter";
	}

	@Override
	public String getIdentifier() {
		return "XLS";
	}

	@Override
	protected String getLocalizationDirectory() {
		return "FlexoLocalization/ExcelTechnologyAdapter";
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		return BINDING_FACTORY;
	}

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	@Override
	public void initFMLModelFactory(FMLModelFactory fMLModelFactory) {
		super.initFMLModelFactory(fMLModelFactory);

		fMLModelFactory.addConverter(new ExcelCellRangeConverter(getServiceManager()));
	}

	public <I> ExcelWorkbookRepository<I> getExcelWorkbookRepository(FlexoResourceCenter<I> resourceCenter) {
		ExcelWorkbookRepository<I> returned = resourceCenter.retrieveRepository(ExcelWorkbookRepository.class, this);
		if (returned == null) {
			returned = ExcelWorkbookRepository.instanciateNewRepository(this, resourceCenter);
			resourceCenter.registerRepository(returned, ExcelWorkbookRepository.class, this);
		}
		return returned;
	}

	private ExcelWorkbookResourceFactory getExcelWorkbookResourceFactory() {
		return getResourceFactory(ExcelWorkbookResourceFactory.class);
	}

	public <I> SEVirtualModelInstanceRepository<I> getSEVirtualModelInstanceRepository(FlexoResourceCenter<I> resourceCenter) {
		SEVirtualModelInstanceRepository<I> returned = resourceCenter.retrieveRepository(SEVirtualModelInstanceRepository.class, this);
		if (returned == null) {
			returned = SEVirtualModelInstanceRepository.instanciateNewRepository(this, resourceCenter);
			resourceCenter.registerRepository(returned, SEVirtualModelInstanceRepository.class, this);
		}
		return returned;
	}

	private SEVirtualModelInstanceTypeFactory seVmiFactory;

	public SEVirtualModelInstanceTypeFactory getVirtualModelInstanceTypeFactory() {
		if (seVmiFactory == null) {
			seVmiFactory = new SEVirtualModelInstanceTypeFactory(this);
		}
		return seVmiFactory;
	}

}
