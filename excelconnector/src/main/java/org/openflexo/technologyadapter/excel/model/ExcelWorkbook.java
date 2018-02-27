/**
 * 
 * Copyright (c) 2014, Openflexo
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

package org.openflexo.technologyadapter.excel.model;

import java.util.List;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PastingPoint;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;

/**
 * Represents an excel workbook, as a concept wrapping {@link Workbook} POI concept
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(value = ExcelWorkbook.ExcelWorkbookImpl.class)
@XMLElement
@Imports({ @Import(ExcelCellRange.class) })
public interface ExcelWorkbook extends ExcelObject, ResourceData<ExcelWorkbook> {

	@PropertyIdentifier(type = Workbook.class)
	public static final String WORKBOOK_KEY = "workbook";
	@PropertyIdentifier(type = ExcelSheet.class, cardinality = Cardinality.LIST)
	public static final String EXCEL_SHEETS_KEY = "excelSheets";

	/**
	 * Return name of the workbook
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Return workbook wrapped by this {@link ExcelWorkbook}
	 * 
	 * @return
	 */
	@Getter(value = WORKBOOK_KEY, ignoreType = true)
	public Workbook getWorkbook();

	/**
	 * Sets workbook wrapped by this {@link ExcelWorkbook}
	 * 
	 * @param workbook
	 */
	@Setter(WORKBOOK_KEY)
	public void setWorkbook(Workbook workbook);

	/**
	 * Return all {@link ExcelSheet} defined in this {@link ExcelWorkbook}
	 * 
	 * @return
	 */
	@Getter(value = EXCEL_SHEETS_KEY, cardinality = Cardinality.LIST, inverse = ExcelSheet.EXCEL_WORKBOOK_KEY)
	@XMLElement
	@Embedded
	@CloningStrategy(StrategyType.CLONE)
	public List<ExcelSheet> getExcelSheets();

	@Setter(EXCEL_SHEETS_KEY)
	public void setExcelSheets(List<ExcelSheet> excelSheets);

	@Adder(EXCEL_SHEETS_KEY)
	@PastingPoint
	public void addToExcelSheets(ExcelSheet anExcelSheet);

	@Remover(EXCEL_SHEETS_KEY)
	public void removeFromExcelSheets(ExcelSheet anExcelSheet);

	/**
	 * Get an Excel Sheet within a workbook using its name.
	 * 
	 * @param name
	 * @return
	 */
	public ExcelSheet getExcelSheetByName(String name);

	/**
	 * Get an ExcelSheet(Technology adapter abstraction) from a Sheet(Poi abstraction).
	 * 
	 * @param sheet
	 * @return
	 */
	public ExcelSheet getExcelSheetFromSheet(Sheet sheet);

	/**
	 * Get an Excel Sheet using its position in a workbook in the list of sheets
	 * 
	 * @param position
	 * @return
	 */
	public ExcelSheet getExcelSheetAtPosition(int position);

	public ExcelStyleManager getStyleManager();

	public BasicExcelModelConverter getConverter();

	@Override
	public ExcelWorkbookResource getResource();

	/**
	 * Default base implementation for {@link ExcelWorkbook}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class ExcelWorkbookImpl extends ExcelObjectImpl implements ExcelWorkbook {

		private static final Logger logger = Logger.getLogger(ExcelObjectImpl.class.getPackage().getName());

		// private Workbook workbook;

		// private ExcelWorkbookResource resource;
		// private List<ExcelSheet> excelSheets;

		// private ArrayList<ExcelObject> accessibleExcelObjects;

		private final ExcelStyleManager styleManager;

		public ExcelWorkbookImpl() {
			styleManager = new ExcelStyleManager(this);
		}

		/*@Override
		public Workbook getWorkbook() {
			return workbook;
		}*/

		/*public ExcelWorkbook(Workbook workbook, ExcelTechnologyAdapter adapter) {
			super(adapter);
			this.workbook = workbook;
			excelSheets = new ArrayList<ExcelSheet>();
			styleManager = new ExcelStyleManager(this);
		}
		
		public ExcelWorkbook(ExcelTechnologyAdapter adapter) {
			super(adapter);
			excelSheets = new ArrayList<ExcelSheet>();
			styleManager = new ExcelStyleManager(this);
		}
		
		public ExcelWorkbook(Workbook workbook, BasicExcelModelConverter converter, ExcelTechnologyAdapter adapter) {
			super(adapter);
			this.workbook = workbook;
			this.converter = converter;
			excelSheets = new ArrayList<ExcelSheet>();
			styleManager = new ExcelStyleManager(this);
		}*/

		@Override
		public ExcelWorkbook getResourceData() {
			return this;
		}

		@Override
		public ExcelWorkbookResource getResource() {
			return (ExcelWorkbookResource) performSuperGetter(FLEXO_RESOURCE);
		}

		@Override
		public String getName() {
			return getResource().getName();
		}

		/*@Override
		public List<ExcelSheet> getExcelSheets() {
			return excelSheets;
		}
		
		@Override
		public void addToExcelSheets(ExcelSheet newExcelSheet) {
			this.excelSheets.add(newExcelSheet);
			addToAccessibleExcelObjects(newExcelSheet);
		}*/

		/**
		 * Get an Excel Sheet within a workbook using its name.
		 * 
		 * @param name
		 * @return
		 */
		@Override
		public ExcelSheet getExcelSheetByName(String name) {
			for (ExcelSheet excelSheet : getExcelSheets()) {
				if (excelSheet.getName().equals(name)) {
					return excelSheet;
				}
			}
			return null;
			/*Sheet sheet = workbook.getSheet(name);
			return getExcelSheetFromSheet(sheet);*/
		}

		/**
		 * Get an Excel Sheet using its position in a workbook in the list of sheets
		 * 
		 * @param place
		 * @return
		 */
		@Override
		public ExcelSheet getExcelSheetAtPosition(int position) {
			/*Sheet sheet = workbook.getSheetAt(position);
			return getExcelSheetFromSheet(sheet);*/
			return getExcelSheets().get(position);
		}

		/**
		 * Get an ExcelSheet(Technology adapter abstraction) from a Sheet(Poi abstraction).
		 * 
		 * @param sheet
		 * @return
		 */
		@Override
		public ExcelSheet getExcelSheetFromSheet(Sheet sheet) {
			for (ExcelSheet excelSheet : getExcelSheets()) {
				if (excelSheet.getSheet().equals(sheet)) {
					return excelSheet;
				}
			}
			logger.warning("No converted sheet found for " + sheet.getSheetName());
			return null;
		}

		@Override
		public ExcelStyleManager getStyleManager() {
			return styleManager;
		}

		@Override
		public BasicExcelModelConverter getConverter() {
			return getResource().getConverter();
		}

		@Override
		public String toString() {
			return super.toString() + "-" + getResource();
		}
	}

}
