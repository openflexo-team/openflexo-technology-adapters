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

package org.openflexo.technologyadapter.excel.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.BasicExcelModelSlot;
import org.openflexo.technologyadapter.excel.model.ExcelRow;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;

@ModelEntity
@ImplementationClass(AddExcelSheet.AddExcelSheetImpl.class)
@XMLElement
@FML("AddExcelSheet")
public interface AddExcelSheet extends ExcelAction<ExcelSheet> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String SHEET_NAME_KEY = "sheetName";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String SHEET_ROWS_KEY = "sheetRows";
	@PropertyIdentifier(type = boolean.class)
	public static final String OVERRIDE_KEY = "override";

	@Getter(value = SHEET_NAME_KEY)
	@XMLAttribute
	public DataBinding<String> getSheetName();

	@Setter(SHEET_NAME_KEY)
	public void setSheetName(DataBinding<String> sheetName);

	@Getter(value = SHEET_ROWS_KEY)
	@XMLAttribute
	public DataBinding<List<ExcelRow>> getSheetRows();

	@Setter(SHEET_ROWS_KEY)
	public void setSheetRows(DataBinding<List<ExcelRow>> sheetRows);

	@Getter(value = OVERRIDE_KEY, defaultValue = "false")
	@XMLAttribute
	public boolean getOverride();

	@Setter(OVERRIDE_KEY)
	public void setOverride(boolean override);

	public static abstract class AddExcelSheetImpl
			extends TechnologySpecificActionDefiningReceiverImpl<BasicExcelModelSlot, ExcelWorkbook, ExcelSheet> implements AddExcelSheet {

		private static final Logger logger = Logger.getLogger(AddExcelSheet.class.getPackage().getName());

		private DataBinding<String> sheetName;

		private DataBinding<List<ExcelRow>> sheetRows;

		private boolean override = false;

		public AddExcelSheetImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return ExcelSheet.class;
		}

		@Override
		public ExcelSheet execute(RunTimeEvaluationContext evaluationContext) {

			ExcelSheet result = null;

			ExcelWorkbook excelWB = getReceiver(evaluationContext);
			// Sheet sheet = null;
			try {
				if (excelWB != null) {
					String name = getSheetName().getBindingValue(evaluationContext);
					if (name != null) {
						// Create or retrieve this sheet
						result = excelWB.getConverter().newSheet(name, getOverride());

						// sheet = retrieveOrCreateSheet(wb, name);
						// Instanciate Wrapper.
						// result = modelSlotInstance.getAccessedResourceData().getConverter().convertExcelSheetToSheet(sheet,
						// modelSlotInstance.getAccessedResourceData(), null);
						// modelSlotInstance.getAccessedResourceData().addToExcelSheets(result);
						excelWB.setIsModified();
					}
					else {
						logger.warning("Create a sheet requires a name");
					}
				}
				else {
					logger.warning("Create a sheet requires a workbook");
				}
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			return result;

		}

		@Override
		public DataBinding<String> getSheetName() {
			if (sheetName == null) {
				sheetName = new DataBinding<>(this, String.class, DataBinding.BindingDefinitionType.GET);
				sheetName.setBindingName("sheetName");
			}
			return sheetName;
		}

		@Override
		public void setSheetName(DataBinding<String> sheetName) {
			if (sheetName != null) {
				sheetName.setOwner(this);
				sheetName.setDeclaredType(String.class);
				sheetName.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				sheetName.setBindingName("sheetName");
			}
			this.sheetName = sheetName;
		}

		@Override
		public DataBinding<List<ExcelRow>> getSheetRows() {
			if (sheetRows == null) {
				sheetRows = new DataBinding<>(this, List.class, DataBinding.BindingDefinitionType.GET);
				sheetRows.setBindingName("sheetRows");
			}
			return sheetRows;
		}

		@Override
		public void setSheetRows(DataBinding<List<ExcelRow>> sheetRows) {
			if (sheetRows != null) {
				sheetRows.setOwner(this);
				sheetRows.setDeclaredType(List.class);
				sheetRows.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				sheetRows.setBindingName("sheetRows");
			}
			this.sheetRows = sheetRows;
		}

		@Override
		public boolean getOverride() {
			return override;
		}

		@Override
		public void setOverride(boolean override) {
			this.override = override;
		}
	}
}
