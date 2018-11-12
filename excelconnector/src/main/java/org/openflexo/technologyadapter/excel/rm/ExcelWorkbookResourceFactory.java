/*
 * (c) Copyright 2013 Openflexo
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

package org.openflexo.technologyadapter.excel.rm;

import java.util.logging.Logger;

import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.StreamIODelegate;
import org.openflexo.foundation.resource.TechnologySpecificPamelaResourceFactory;
import org.openflexo.foundation.technologyadapter.TechnologyContextManager;
import org.openflexo.pamela.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelModelFactory;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;

/**
 * Implementation of ResourceFactory for {@link ExcelWorkbookResource}
 * 
 * @author sylvain
 *
 */
public class ExcelWorkbookResourceFactory
		extends TechnologySpecificPamelaResourceFactory<ExcelWorkbookResource, ExcelWorkbook, ExcelTechnologyAdapter, ExcelModelFactory> {

	private static final Logger logger = Logger.getLogger(ExcelWorkbookResourceFactory.class.getPackage().getName());

	public static String EXCEL_FILE_EXTENSION = ".xls";
	public static String EXCELX_FILE_EXTENSION = ".xlsx";

	public ExcelWorkbookResourceFactory() throws ModelDefinitionException {
		super(ExcelWorkbookResource.class);
	}

	@Override
	public ExcelWorkbook makeEmptyResourceData(ExcelWorkbookResource resource) {
		if (resource.getIODelegate() instanceof StreamIODelegate) {
			return resource.createOrLoadExcelWorkbook((StreamIODelegate<?>) resource.getIODelegate());
		}
		logger.severe("Cannot create excel workbook for this io delegate: " + resource.getIODelegate());
		return null;
	}

	@Override
	public <I> boolean isValidArtefact(I serializationArtefact, FlexoResourceCenter<I> resourceCenter) {
		return (resourceCenter.retrieveName(serializationArtefact).endsWith(EXCEL_FILE_EXTENSION)
				|| resourceCenter.retrieveName(serializationArtefact).endsWith(EXCELX_FILE_EXTENSION))
				&& !(resourceCenter.retrieveName(serializationArtefact).startsWith("~"));
	}

	@Override
	protected <I> ExcelWorkbookResource registerResource(ExcelWorkbookResource resource, FlexoResourceCenter<I> resourceCenter) {
		super.registerResource(resource, resourceCenter);

		// Register the resource in the ExcelWorkbookRepository of supplied
		// resource center
		registerResourceInResourceRepository(resource,
				getTechnologyAdapter(resourceCenter.getServiceManager()).getExcelWorkbookRepository(resourceCenter));

		return resource;
	}

	@Override
	public ExcelModelFactory makeResourceDataFactory(ExcelWorkbookResource resource,
			TechnologyContextManager<ExcelTechnologyAdapter> technologyContextManager) throws ModelDefinitionException {
		return new ExcelModelFactory(resource, technologyContextManager.getServiceManager().getEditingContext());
	}

}
