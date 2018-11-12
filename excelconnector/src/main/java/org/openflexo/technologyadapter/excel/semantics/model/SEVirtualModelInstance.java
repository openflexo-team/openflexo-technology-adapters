/*
 * Copyright (c) 2013-2017, Openflexo
 *
 * This file is part of Flexo-foundation, a component of the software infrastructure
 * developed at Openflexo.
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
 *           Additional permission under GNU GPL version 3 section 7
 *           If you modify this Program, or any covered work, by linking or
 *           combining it with software containing parts covered by the terms
 *           of EPL 1.0, the licensors of this Program grant you additional permission
 *           to convey the resulting work.
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

package org.openflexo.technologyadapter.excel.semantics.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.Import;
import org.openflexo.pamela.annotations.Imports;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
import org.openflexo.technologyadapter.excel.semantics.fml.SEDataAreaRole;
import org.openflexo.toolbox.StringUtils;

/**
 * A JDBC/Hibernate-specific {@link VirtualModelInstance} reflecting distants objects accessible through a {@link HbnModelSlot} configured
 * with a {@link VirtualModel}<br>
 * 
 * This {@link VirtualModelInstance} implementation implements Hibernate framework on a given database to provide a database connection.<br>
 * Call {@link #connectToDB()} to realize connection to database. This call initialize a {@link SessionFactory} and open a defaut
 * connection.
 * 
 * This {@link SEVirtualModelInstance} has an internal caching scheme allowing to store {@link SEFlexoConceptInstance} relatively to their
 * related {@link FlexoConcept} (their type) and their identifier
 * 
 * 
 */
@ModelEntity
@ImplementationClass(SEVirtualModelInstance.SEVirtualModelInstanceImpl.class)
@Imports(@Import(SEFlexoConceptInstance.class))
@XMLElement
public interface SEVirtualModelInstance extends VirtualModelInstance<SEVirtualModelInstance, ExcelTechnologyAdapter> {

	@PropertyIdentifier(type = ExcelWorkbookResource.class)
	String EXCEL_WORKBOOK_RESOURCE = "excelWorkbookResource";
	@PropertyIdentifier(type = String.class)
	String EXCEL_WORKBOOK_URI = "excelWorkbookURI";

	@Getter(EXCEL_WORKBOOK_RESOURCE)
	public ExcelWorkbookResource getExcelWorkbookResource();

	@Setter(EXCEL_WORKBOOK_RESOURCE)
	public void setExcelWorkbookResource(ExcelWorkbookResource excelWorkbookResource);

	@Getter(EXCEL_WORKBOOK_URI)
	@XMLAttribute
	public String getExcelWorkbookURI();

	@Setter(EXCEL_WORKBOOK_URI)
	public void setExcelWorkbookURI(String excelWorkbook);

	/**
	 * Retrieve (build if not existant) {@link SEFlexoConceptInstance} with supplied support object (a row in an excel workbook), asserting
	 * returned {@link SEFlexoConceptInstance} object has supplied concept type and container<br>
	 * 
	 * This {@link SEVirtualModelInstance} has an internal caching scheme allowing to store {@link SEFlexoConceptInstance} relatively to
	 * their related {@link FlexoConcept} (their type) and their identifier
	 * 
	 * @param row
	 *            row in excel workbook
	 * @param container
	 *            container (eventually null) of returned {@link SEFlexoConceptInstance}
	 * @param concept
	 *            type of returned {@link SEFlexoConceptInstance}
	 * @return
	 */
	// public SEFlexoConceptInstance getFlexoConceptInstance(Row row, FlexoConceptInstance container, SEDataAreaRole dataAreaRole)
	// throws ExcelMappingException;

	/**
	 * Instantiate and register a new {@link SEFlexoConceptInstance}
	 * 
	 * @param pattern
	 * @return
	 */
	// @Override
	// public SEFlexoConceptInstance makeNewFlexoConceptInstance(FlexoConcept concept);

	/**
	 * Instantiate and register a new {@link SEFlexoConceptInstance} in a container FlexoConceptInstance
	 * 
	 * @param pattern
	 * @return
	 */
	// @Override
	// public SEFlexoConceptInstance makeNewFlexoConceptInstance(FlexoConcept concept, FlexoConceptInstance container);

	/**
	 * Update all data area by connecting to the excel workbook
	 */
	public void updateData() throws ExcelMappingException;

	abstract class SEVirtualModelInstanceImpl extends VirtualModelInstanceImpl<SEVirtualModelInstance, ExcelTechnologyAdapter>
			implements SEVirtualModelInstance {

		private static final Logger logger = FlexoLogger.getLogger(SEVirtualModelInstance.class.getPackage().toString());

		// Stores all FCIs related to their SEDataAreaRole
		// private Map<FlexoConcept, List<SEFlexoConceptInstance>> instances = new HashMap<>();
		// private Map<FlexoConcept, SEDataArea<SEFlexoConceptInstance>> instancesList = new HashMap<>();

		private Map<SEDataAreaRole, SEDataArea<SEFlexoConceptInstance>> instances = new HashMap<>();

		private ExcelWorkbookResource wbResource;
		private String wbURI;

		@Override
		public ExcelWorkbookResource getExcelWorkbookResource() {
			if (wbResource == null && StringUtils.isNotEmpty(wbURI) && getServiceManager() != null
					&& getServiceManager().getResourceManager() != null) {
				wbResource = (ExcelWorkbookResource) getServiceManager().getResourceManager().getResource(wbURI, ExcelWorkbook.class);
				logger.info("Looked-up " + wbResource + " for " + wbURI);
			}
			return wbResource;
		}

		@Override
		public void setExcelWorkbookResource(ExcelWorkbookResource excelWorkbookResource) {
			this.wbResource = excelWorkbookResource;
		}

		@Override
		public String getExcelWorkbookURI() {
			if (wbResource != null) {
				return wbResource.getURI();
			}
			return wbURI;
		}

		@Override
		public void setExcelWorkbookURI(String excelWorkbookURI) {
			this.wbURI = excelWorkbookURI;
		}

		@Override
		public ExcelTechnologyAdapter getTechnologyAdapter() {
			if (getResource() != null) {
				return getResource().getTechnologyAdapter();
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<FlexoConceptInstance> getFlexoConceptInstances() {
			if (isSerializing()) {
				// FCI are not serialized
				return null;
			}
			return (List<FlexoConceptInstance>) performSuperGetter(FLEXO_CONCEPT_INSTANCES_KEY);
		}

		@Override
		public SEVirtualModelInstanceModelFactory getFactory() {
			return (SEVirtualModelInstanceModelFactory) super.getFactory();
		}

		@Override
		public <T> List<T> getFlexoActorList(FlexoRole<T> flexoRole) {
			if (flexoRole instanceof SEDataAreaRole) {
				return (List<T>) instances.get(flexoRole);
			}
			return super.getFlexoActorList(flexoRole);
		}

		/**
		 * Retrieve (build if not existant) {@link SEFlexoConceptInstance} with supplied support object (an excel row), asserting returned
		 * {@link SEFlexoConceptInstance} object has supplied concept type and container<br>
		 * 
		 * This {@link SEVirtualModelInstance} has an internal caching scheme allowing to store {@link SEFlexoConceptInstance} relatively to
		 * their related {@link FlexoConcept} (their type) and their identifier
		 * 
		 * @param hbnMap
		 *            hibernate support object
		 * @param container
		 *            container (eventually null) of returned {@link SEFlexoConceptInstance}
		 * @param concept
		 *            type of returned {@link SEFlexoConceptInstance}
		 * @return
		 * @throws ExcelMappingException
		 */
		/*@Override
		public SEFlexoConceptInstance getFlexoConceptInstance(Row row, FlexoConceptInstance container, SEDataAreaRole dataAreaRole)
				throws ExcelMappingException {
		
			if (dataAreaRole == null) {
				logger.warning("Could not obtain SEFlexoConceptInstance from null dataArea");
			}
			if (dataAreaRole.getFlexoConceptType() == null) {
				logger.warning("Could not obtain SEFlexoConceptInstance from null FlexoConcept");
			}
		
			System.out.println("Nouvelle instance pour " + row.getRowNum());
		
			// String identifier = getIdentifier(row, concept);
			// System.out.println("Building object with: " + hbnMap + " id=" + identifier);
		
			//getFactory()
			
			List<SEFlexoConceptInstance> mapForConcept = instances.computeIfAbsent(dataAreaRole.getFlexoConceptType(), (newConcept) -> {
				return new ArrayList<>();
			});
		
			SEFlexoConceptInstance returned = mapForConcept.computeIfAbsent(row.getRowNum(), (newId) -> {
				return getFactory().newFlexoConceptInstance(this, container, row, dataAreaRole.getFlexoConceptType());
			});
		
			SEDataArea<SEFlexoConceptInstance> dataArea = instancesList.get(dataAreaRole.getFlexoConceptType());
			
			
			
			if (dataArea != null) {
				dataArea.add(returned);
			}
		
			return returned;
		}*/

		/**
		 * Instanciate and register a new {@link FlexoConceptInstance}
		 * 
		 * @param pattern
		 * @return
		 */
		/*@Override
		public SEFlexoConceptInstance makeNewFlexoConceptInstance(FlexoConcept concept) {
		
			return makeNewFlexoConceptInstance(concept, null);
		}*/

		/**
		 * Instantiate and register a new {@link FlexoConceptInstance} in a container FlexoConceptInstance
		 * 
		 * @param pattern
		 * @return
		 */
		/*@Override
		public SEFlexoConceptInstance makeNewFlexoConceptInstance(FlexoConcept concept, FlexoConceptInstance container) {
		
			SEFlexoConceptInstance returned = getResource().getFactory().newInstance(SEFlexoConceptInstance.class, concept);
		
			if (container != null) {
				container.addToEmbeddedFlexoConceptInstances(returned);
			}
			addToFlexoConceptInstances(returned);
			return returned;
		}*/

		@Override
		protected SEFlexoConceptInstance buildNewFlexoConceptInstance(FlexoConcept concept) {
			return getResource().getFactory().newInstance(SEFlexoConceptInstance.class, concept);
		}

		@Override
		public void updateData() throws ExcelMappingException {
			System.out.println("------------> Looking-up excel file: " + getExcelWorkbookResource() + " for " + getVirtualModel());
			List<SEDataAreaRole> dataAreaRoles = getVirtualModel().getAccessibleProperties(SEDataAreaRole.class);
			if (dataAreaRoles != null) {
				for (SEDataAreaRole dataAreaRole : dataAreaRoles) {
					updateDataAreaRole(dataAreaRole);
				}
			}
		}

		private SEDataArea<SEFlexoConceptInstance> updateDataAreaRole(SEDataAreaRole dataAreaRole) {

			SEDataArea<SEFlexoConceptInstance> returned = instances.get(dataAreaRole);

			if (returned == null) {
				returned = new SEDataArea<>(dataAreaRole, this, null);
				instances.put(dataAreaRole, returned);
			}

			returned.update();

			return returned;

			/*Map<Integer, SEFlexoConceptInstance> allFCI = instances.get(dataAreaRole);
			
			if (allFCI == null) {
				allFCI = new LinkedHashMap<>();
				instances.put(dataAreaRole.getFlexoConceptType(), allFCI);
			}
			ExcelCellRange matchingRange = getRange(dataAreaRole);
			// System.out.println("matchingRange=" + matchingRange);
			
			int startRowIndex = matchingRange.getTopLeftCell().getRowIndex();
			int endRowIndex = matchingRange.getBottomRightCell().getRowIndex();
			for (int currentIndex = startRowIndex; currentIndex <= endRowIndex; currentIndex++) {
				ExcelRow excelRow = matchingRange.getExcelSheet().getRowAt(currentIndex);
				int fciIndex = currentIndex - startRowIndex;
				if (currentIndex < allFCI.size()) {
					// Update existing instance using row
					SEFlexoConceptInstance seFCI = allFCI.get(fciIndex);
					seFCI.setRowSupportObject(excelRow.getRow());
				}
				else {
					// New instance
					SEFlexoConceptInstance seFCI = getFlexoConceptInstance(excelRow.getRow(), null, dataAreaRole);
					allFCI.put(excelRow.getRowIndex(), seFCI);
				}
			}
			// What about instances to be deleted
			for (Integer rowIndex : new ArrayList<>(allFCI.keySet())) {
				if (rowIndex < startRowIndex || rowIndex > endRowIndex) {
					SEFlexoConceptInstance fciToRemove = allFCI.get(rowIndex);
					allFCI.remove(rowIndex);
					fciToRemove.delete();
				}
			}
			
			// Rebuilt the list of concept instances for this type
			SEDataArea<SEFlexoConceptInstance> newDataArea = new SEDataArea<>(dataAreaRole, matchingRange, allFCI.values());
			instancesList.put(dataAreaRole.getFlexoConceptType(), newDataArea);
			
			return allFCI;*/
		}

	}
}
