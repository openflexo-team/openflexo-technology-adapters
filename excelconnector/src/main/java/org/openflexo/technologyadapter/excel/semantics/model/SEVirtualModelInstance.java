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

import org.apache.poi.ss.usermodel.Row;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.ExcelTechnologyAdapter;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;
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
	 * Build and return a {@link String} object which acts as key identifier for supplied row, asserting this is the support object for a
	 * {@link SEFlexoConceptInstance} of supplied {@link FlexoConcept}
	 * 
	 * <ul>
	 * <li>If key of declaring {@link FlexoConcept} is simple, just return the value of key property</li>
	 * <li>If Key is composite, return an Object array with all the values of properties composing composite key, in the order where those
	 * properties are declared in keyProperties of related {@link FlexoConcept}</li>
	 * </ul>
	 * 
	 * @param row
	 *            row in excel workbook
	 * @param concept
	 *            type of related {@link SEFlexoConceptInstance}
	 * @return
	 */
	public String getIdentifier(Row row, FlexoConcept concept);

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
	public SEFlexoConceptInstance getFlexoConceptInstance(Row row, FlexoConceptInstance container, FlexoConcept concept);

	/**
	 * Retrieve (build if not existant) {@link SEFlexoConceptInstance} with supplied identifier asserting returned
	 * {@link SEFlexoConceptInstance} object has supplied concept type and container<br>
	 * 
	 * This {@link SEVirtualModelInstance} has an internal caching scheme allowing to store {@link SEFlexoConceptInstance} relatively to
	 * their related {@link FlexoConcept} (their type) and their identifier
	 * 
	 * @param rowId
	 *            identifier for row
	 * @param container
	 *            container (eventually null) of returned {@link SEFlexoConceptInstance}
	 * @param concept
	 *            type of returned {@link SEFlexoConceptInstance}
	 * @return
	 */
	public SEFlexoConceptInstance getFlexoConceptInstance(String rowId, FlexoConceptInstance container, FlexoConcept concept);

	/**
	 * Instantiate and register a new {@link SEFlexoConceptInstance}
	 * 
	 * @param pattern
	 * @return
	 */
	@Override
	public SEFlexoConceptInstance makeNewFlexoConceptInstance(FlexoConcept concept);

	/**
	 * Instantiate and register a new {@link SEFlexoConceptInstance} in a container FlexoConceptInstance
	 * 
	 * @param pattern
	 * @return
	 */
	@Override
	public SEFlexoConceptInstance makeNewFlexoConceptInstance(FlexoConcept concept, FlexoConceptInstance container);

	abstract class SEVirtualModelInstanceImpl extends VirtualModelInstanceImpl<SEVirtualModelInstance, ExcelTechnologyAdapter>
			implements SEVirtualModelInstance {

		private static final Logger logger = FlexoLogger.getLogger(SEVirtualModelInstance.class.getPackage().toString());

		// Stores all FCIs related to their identifier
		private Map<FlexoConcept, Map<String, SEFlexoConceptInstance>> instances = new HashMap<>();

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

		/**
		 * Build and return a {@link String} object which acts as key identifier for supplied row, asserting this is the support object for
		 * a {@link SEFlexoConceptInstance} of supplied {@link FlexoConcept}
		 * 
		 * <ul>
		 * <li>If key of declaring {@link FlexoConcept} is simple, just return the value of key property</li>
		 * <li>If Key is composite, return an Object array with all the values of properties composing composite key, in the order where
		 * those properties are declared in keyProperties of related {@link FlexoConcept}</li>
		 * </ul>
		 * 
		 * @param row
		 *            row in excel workbook
		 * @param concept
		 *            type of related {@link SEFlexoConceptInstance}
		 * @return
		 */
		@Override
		public String getIdentifier(Row row, FlexoConcept concept) {
			/*if (concept.getKeyProperties().size() == 0) {
				return null;
			}
			if (concept.getKeyProperties().size() == 1) {
				return hbnMap.get(concept.getKeyProperties().get(0).getName()).toString();
			}
			// composite key
			StringBuffer sb = new StringBuffer();
			boolean isFirst = true;
			for (FlexoProperty<?> keyP : concept.getKeyProperties()) {
				sb.append((isFirst ? "" : ",") + keyP.getName() + "=" + hbnMap.get(keyP.getName()));
				isFirst = false;
			}
			return sb.toString();*/
			return row.getSheet().getSheetName() + "/row[" + row.getRowNum() + "]";
		}

		@Override
		public SEVirtualModelInstanceModelFactory getFactory() {
			return (SEVirtualModelInstanceModelFactory) super.getFactory();
		}

		/**
		 * Retrieve (build if not existant) {@link SEFlexoConceptInstance} with supplied support object (a map managed by Hibernate
		 * Framework), asserting returned {@link SEFlexoConceptInstance} object has supplied concept type and container<br>
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
		 */
		@Override
		public SEFlexoConceptInstance getFlexoConceptInstance(Row row, FlexoConceptInstance container, FlexoConcept concept) {

			if (concept == null) {
				logger.warning("Could not obtain HbnFlexoConceptInstance with null FlexoConcept");
			}

			String identifier = getIdentifier(row, concept);
			// System.out.println("Building object with: " + hbnMap + " id=" + identifier);

			Map<String, SEFlexoConceptInstance> mapForConcept = instances.computeIfAbsent(concept, (newConcept) -> {
				return new HashMap<>();
			});

			return mapForConcept.computeIfAbsent(identifier, (newId) -> {
				return getFactory().newFlexoConceptInstance(this, container, row, concept);
			});
		}

		/**
		 * Retrieve (build if not existant) {@link SEFlexoConceptInstance} with supplied identifier asserting returned
		 * {@link SEFlexoConceptInstance} object has supplied concept type and container<br>
		 * 
		 * This {@link SEVirtualModelInstance} has an internal caching scheme allowing to store {@link SEFlexoConceptInstance} relatively to
		 * their related {@link FlexoConcept} (their type) and their identifier
		 * 
		 * @param identifier
		 *            identifier for searched object
		 * @param container
		 *            container (eventually null) of returned {@link SEFlexoConceptInstance}
		 * @param concept
		 *            type of returned {@link SEFlexoConceptInstance}
		 * @return
		 */
		@Override
		public SEFlexoConceptInstance getFlexoConceptInstance(String identifier, FlexoConceptInstance container, FlexoConcept concept) {

			Map<String, SEFlexoConceptInstance> mapForConcept = instances.computeIfAbsent(concept, (newConcept) -> {
				return new HashMap<>();
			});

			return mapForConcept.computeIfAbsent(identifier, (newId) -> {
				/*Map<String, Object> hbnMap;
				try {
					hbnMap = (Map<String, Object>) getDefaultSession().get(concept.getName(), identifier);
					return getFactory().newFlexoConceptInstance(this, container, hbnMap, concept);
				} catch (HbnException e) {
					e.printStackTrace();
					return null;
				}*/
				System.out.println("Un truc a faire la");
				return null;
			});
		}

		/**
		 * Instanciate and register a new {@link FlexoConceptInstance}
		 * 
		 * @param pattern
		 * @return
		 */
		@Override
		public SEFlexoConceptInstance makeNewFlexoConceptInstance(FlexoConcept concept) {

			return makeNewFlexoConceptInstance(concept, null);
		}

		/**
		 * Instantiate and register a new {@link FlexoConceptInstance} in a container FlexoConceptInstance
		 * 
		 * @param pattern
		 * @return
		 */
		@Override
		public SEFlexoConceptInstance makeNewFlexoConceptInstance(FlexoConcept concept, FlexoConceptInstance container) {

			SEFlexoConceptInstance returned = getResource().getFactory().newInstance(SEFlexoConceptInstance.class, new HashMap<>(),
					concept);

			if (container != null) {
				container.addToEmbeddedFlexoConceptInstances(returned);
			}
			addToFlexoConceptInstances(returned);
			return returned;
		}
	}
}
