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

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.annotations.DeclareModelSlots;
import org.openflexo.foundation.fml.annotations.DeclareResourceTypes;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterBindingFactory;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.excel.fml.binding.ExcelBindingFactory;
import org.openflexo.technologyadapter.excel.model.ExcelCellRangeConverter;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookRepository;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
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
@DeclareResourceTypes({ ExcelWorkbookResourceFactory.class, SEVirtualModelInstanceResourceFactory.class })
public class ExcelTechnologyAdapter extends TechnologyAdapter {

	protected static final Logger logger = Logger.getLogger(ExcelTechnologyAdapter.class.getPackage().getName());

	private static final ExcelBindingFactory BINDING_FACTORY = new ExcelBindingFactory();

	/**
	 * 
	 * Constructor.
	 * 
	 * @throws TechnologyAdapterInitializationException
	 */
	public ExcelTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return "Excel technology adapter";
	}

	@Override
	public String getLocalizationDirectory() {
		return "FlexoLocalization/ExcelTechnologyAdapter";
	}

	@Override
	public ExcelTechnologyContextManager createTechnologyContextManager(FlexoResourceCenterService service) {
		return new ExcelTechnologyContextManager(this, service);
	}

	@Override
	public TechnologyAdapterBindingFactory getTechnologyAdapterBindingFactory() {
		return BINDING_FACTORY;
	}

	/**
	 * Initialize the supplied resource center with the technology<br>
	 * ResourceCenter is scanned, ResourceRepositories are created and new technology-specific resources are build and registered.
	 * 
	 * @param resourceCenter
	 */
	/*@Override
	public <I> void performInitializeResourceCenter(FlexoResourceCenter<I> resourceCenter) {
	
		ExcelWorkbookRepository wbRepository = resourceCenter.getRepository(ExcelWorkbookRepository.class, this);
		if (wbRepository == null) {
			wbRepository = createWorkbookRepository(resourceCenter);
		}
	
		Iterator<I> it = resourceCenter.iterator();
	
		while (it.hasNext()) {
			I item = it.next();
			// if (item instanceof File) {
			// System.out.println("searching " + item);
			// File candidateFile = (File) item;
			ExcelWorkbookResource wbRes = tryToLookupWorkbook(resourceCenter, item);
			// }
		}
	
		// Call it to update the current repositories
		notifyRepositoryStructureChanged();
	
	}*/

	/*protected ExcelWorkbookResource tryToLookupWorkbook(FlexoResourceCenter<?> resourceCenter, Object candidateElement) {
		ExcelTechnologyContextManager technologyContextManager = getTechnologyContextManager();
		if (isValidWorkbook(candidateElement)) {
			ExcelWorkbookResource wbRes = retrieveWorkbookResource(candidateElement, resourceCenter);
			ExcelWorkbookRepository wbRepository = resourceCenter.getRepository(ExcelWorkbookRepository.class, this);
			if (wbRes != null) {
				RepositoryFolder<ExcelWorkbookResource> folder;
				try {
					folder = wbRepository.getRepositoryFolder(candidateElement, true);
					wbRepository.registerResource(wbRes, folder);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				referenceResource(wbRes, resourceCenter);
				return wbRes;
			}
		}
		return null;
	}*/

	/**
	 * Instantiate new workbook resource stored in supplied model file<br>
	 * *
	 */
	/*public ExcelWorkbookResource retrieveWorkbookResource(Object workbook, FlexoResourceCenter<?> resourceCenter) {
	
		ExcelWorkbookResource returned = getTechnologyContextManager().getExcelWorkbookResource(workbook);
		if (returned == null) {
			if (workbook instanceof File) {
				returned = ExcelWorkbookResourceImpl.retrieveExcelWorkbookResource((File) workbook, getTechnologyContextManager(),
						resourceCenter);
			}
			else if (workbook instanceof InJarResourceImpl) {
				returned = ExcelWorkbookResourceImpl.retrieveExcelWorkbookResource((InJarResourceImpl) workbook,
						getTechnologyContextManager(), resourceCenter);
			}
			if (returned != null) {
				getTechnologyContextManager().registerExcelWorkbook(returned);
			}
			else {
				logger.warning("Cannot retrieve ExcelWorkbook resource for " + workbook);
			}
		}
	
		return returned;
	}*/

	/*public boolean isValidWorkbook(Object candidateElement) {
		if (candidateElement instanceof File && isValidWorkbookFile(((File) candidateElement))) {
			return true;
		}
		else if (candidateElement instanceof InJarResourceImpl && isValidWorkbookInJar((InJarResourceImpl) candidateElement)) {
			return true;
		}
		return false;
	}*/

	/**
	 * Return flag indicating if supplied file appears as a valid workbook
	 * 
	 * @param candidateFile
	 * 
	 * @return
	 */
	/*public boolean isValidWorkbookFile(File candidateFile) {
		return (candidateFile.getName().endsWith(".xlsx") || candidateFile.getName().endsWith(".xls"))
				&& !(candidateFile.getName().startsWith("~"));
	}*/

	/*public boolean isValidWorkbookInJar(InJarResourceImpl candidateInJar) {
		if (candidateInJar.getRelativePath().endsWith(".xlsx") || candidateInJar.getRelativePath().endsWith(".xls")) {
			return true;
		}
		return false;
	}*/

	@Override
	public <I> boolean isIgnorable(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}

	/*@Override
	public <I> boolean contentsAdded(FlexoResourceCenter<I> resourceCenter, I contents) {
		if (contents instanceof File) {
			File candidateFile = (File) contents;
			if (tryToLookupWorkbook(resourceCenter, candidateFile) != null) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public <I> boolean contentsDeleted(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}
	
	@Override
	public <I> boolean contentsModified(FlexoResourceCenter<I> resourceCenter, I contents) {
		return false;
	}
	
	@Override
	public <I> boolean contentsRenamed(FlexoResourceCenter<I> resourceCenter, I contents, String oldName, String newName) {
		return false;
	}*/

	@Override
	public ExcelTechnologyContextManager getTechnologyContextManager() {
		return (ExcelTechnologyContextManager) super.getTechnologyContextManager();
	}

	/**
	 * 
	 * Create a workbook repository for current {@link TechnologyAdapter} and supplied {@link FlexoResourceCenter}
	 * 
	 */
	/*public ExcelWorkbookRepository createWorkbookRepository(FlexoResourceCenter<?> resourceCenter) {
		ExcelWorkbookRepository returned = new ExcelWorkbookRepository(this, resourceCenter);
		resourceCenter.registerRepository(returned, ExcelWorkbookRepository.class, this);
		return returned;
	}*/

	public <I> ExcelWorkbookRepository<I> getExcelWorkbookRepository(FlexoResourceCenter<I> resourceCenter) {
		ExcelWorkbookRepository<I> returned = resourceCenter.retrieveRepository(ExcelWorkbookRepository.class, this);
		if (returned == null) {
			try {
				returned = ExcelWorkbookRepository.instanciateNewRepository(this, resourceCenter);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resourceCenter.registerRepository(returned, ExcelWorkbookRepository.class, this);
		}
		return returned;
	}

	@Deprecated
	public ExcelWorkbookResource createNewWorkbook(FlexoResourceCenter<File> rc, String excelFilename/*, String modelUri*/)
			throws SaveResourceException, ModelDefinitionException {

		if (rc instanceof FlexoProject) {
			File excelFile = new File(((FlexoProject<File>) rc).getProjectDirectory(), excelFilename);

			ExcelWorkbookResource workbookResource = getExcelWorkbookResourceFactory().makeResource(excelFile, rc, true);

			// ExcelWorkbookResource workbookResource = ExcelWorkbookResourceImpl.makeExcelWorkbookResource(/*modelUri,*/ excelFile,
			// getTechnologyContextManager(), rc);

			getTechnologyContextManager().registerResource(workbookResource);

			return workbookResource;
		}
		else {
			logger.warning("NOT IMPLEMENTED: not able to create an excelfile in a RC that is not a FlexoProject" + rc.toString());
			return null;
		}
	}

	@Deprecated
	public ExcelWorkbookResource createNewWorkbook(FlexoResourceCenter<?> resourceCenter, String resourceName, String resourceURI,
			String relativePath) throws SaveResourceException, ModelDefinitionException {

		if (resourceCenter instanceof FileSystemBasedResourceCenter) {
			File excelFile = ((FileSystemBasedResourceCenter) resourceCenter).retrieveResourceFile(resourceName, relativePath, ".xlsx");
			System.out.println("create workbook " + excelFile);

			ExcelWorkbookResource workbookResource = getExcelWorkbookResourceFactory().makeResource(excelFile,
					(FlexoResourceCenter<File>) resourceCenter, true);
			// ExcelWorkbookResource workbookResource = ExcelWorkbookResourceImpl.makeExcelWorkbookResource(/*modelUri,*/ excelFile,
			// getTechnologyContextManager(), resourceCenter);
			// getTechnologyContextManager().registerResource(workbookResource);
			return workbookResource;
		}
		return null;

	}

	@Override
	public String getIdentifier() {
		return "XLS";
	}

	public ExcelWorkbookResourceFactory getExcelWorkbookResourceFactory() {
		return getResourceFactory(ExcelWorkbookResourceFactory.class);
	}

	public <I> SEVirtualModelInstanceRepository<I> getSEVirtualModelInstanceRepository(FlexoResourceCenter<I> resourceCenter) {
		SEVirtualModelInstanceRepository<I> returned = resourceCenter.retrieveRepository(SEVirtualModelInstanceRepository.class, this);
		if (returned == null) {
			try {
				returned = SEVirtualModelInstanceRepository.instanciateNewRepository(this, resourceCenter);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resourceCenter.registerRepository(returned, SEVirtualModelInstanceRepository.class, this);
		}
		return returned;
	}

	private SEVirtualModelInstanceTypeFactory hbnVmiFactory;

	public SEVirtualModelInstanceTypeFactory getVirtualModelInstanceTypeFactory() {
		if (hbnVmiFactory == null) {
			hbnVmiFactory = new SEVirtualModelInstanceTypeFactory(this);
		}
		return hbnVmiFactory;
	}

	@Override
	public void initFMLModelFactory(FMLModelFactory fMLModelFactory) {
		super.initFMLModelFactory(fMLModelFactory);

		fMLModelFactory.addConverter(new ExcelCellRangeConverter(getServiceManager()));
	}

}
