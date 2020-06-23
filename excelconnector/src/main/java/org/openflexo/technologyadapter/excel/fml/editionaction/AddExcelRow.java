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
import org.openflexo.pamela.annotations.DefineValidationRule;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.BasicExcelModelSlot;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelRow;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;

@ModelEntity
@ImplementationClass(AddExcelRow.AddExcelRowImpl.class)
@XMLElement
@FML("AddExcelRow")
public interface AddExcelRow extends ExcelAction<ExcelRow> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String EXCEL_SHEET_KEY = "excelSheet";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String EXCEL_CELLS_KEY = "excelCells";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String ROW_INDEX_KEY = "rowIndex";

	@Getter(value = EXCEL_SHEET_KEY)
	@XMLAttribute
	public DataBinding<ExcelSheet> getExcelSheet();

	@Setter(EXCEL_SHEET_KEY)
	public void setExcelSheet(DataBinding<ExcelSheet> excelSheet);

	@Getter(value = EXCEL_CELLS_KEY)
	@XMLAttribute
	public DataBinding<List<ExcelCell>> getExcelCells();

	@Setter(EXCEL_CELLS_KEY)
	public void setExcelCells(DataBinding<List<ExcelCell>> excelCells);

	@Getter(value = ROW_INDEX_KEY)
	@XMLAttribute
	public DataBinding<Integer> getRowIndex();

	@Setter(ROW_INDEX_KEY)
	public void setRowIndex(DataBinding<Integer> rowIndex);

	public static abstract class AddExcelRowImpl
			extends TechnologySpecificActionDefiningReceiverImpl<BasicExcelModelSlot, ExcelWorkbook, ExcelRow> implements AddExcelRow {

		private static final Logger logger = Logger.getLogger(AddExcelRow.class.getPackage().getName());

		private DataBinding<List<ExcelCell>> excelCells;

		private DataBinding<ExcelSheet> excelSheet;

		private DataBinding<Integer> rowIndex;

		@Override
		public Type getAssignableType() {
			return ExcelRow.class;
		}

		@Override
		public ExcelRow execute(RunTimeEvaluationContext evaluationContext) {

			ExcelRow excelRow = null;

			try {
				ExcelSheet excelSheet = getExcelSheet().getBindingValue(evaluationContext);
				if (excelSheet != null) {
					Integer rowIndex = getRowIndex().getBindingValue(evaluationContext);
					if (rowIndex != null) {
						if (excelSheet.getRowAt(rowIndex) != null) {
							excelRow = excelSheet.getRowAt(rowIndex);
						}
						else {
							excelRow = excelSheet.insertRowAt(rowIndex);
							/*Row row = excelSheet.getSheet().createRow(rowIndex);
							BasicExcelModelConverter converter = ((ExcelWorkbookResource) excelSheet.getResourceData().getResource())
									.getConverter();
							excelRow = converter.convertExcelRowToRow(row, excelSheet, null);
							excelSheet.addToExcelRows(excelRow);*/
						}
						if (getExcelCells().getBindingValue(evaluationContext) != null) {
							excelRow.getExcelCells().addAll(getExcelCells().getBindingValue(evaluationContext));
						}
						excelSheet.getExcelWorkbook().setIsModified();
						excelSheet.getExcelWorkbook().getResource().setModified(true);

					}
					else {
						logger.warning("Create a row requires a rowindex");
					}
				}
				else {
					logger.warning("Create a row requires a sheet");
					System.out.println("Alors ca marche pas, donc.");
					System.out.println("j'essaie d'evaluer: " + getExcelSheet());
					System.out.println("valid=" + getExcelSheet().isValid());
					System.out.println("reason:" + getExcelSheet().invalidBindingReason());
					System.out.println("evaluationContext=" + evaluationContext);
				}

			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			return excelRow;

		}

		@Override
		public DataBinding<List<ExcelCell>> getExcelCells() {
			if (excelCells == null) {
				excelCells = new DataBinding<>(this, List.class, DataBinding.BindingDefinitionType.GET);
				excelCells.setBindingName("excelCells");
			}
			return excelCells;
		}

		@Override
		public void setExcelCells(DataBinding<List<ExcelCell>> excelCells) {
			if (excelCells != null) {
				excelCells.setOwner(this);
				excelCells.setDeclaredType(List.class);
				excelCells.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				excelCells.setBindingName("excelCells");
			}
			this.excelCells = excelCells;
		}

		@Override
		public DataBinding<ExcelSheet> getExcelSheet() {
			if (excelSheet == null) {
				excelSheet = new DataBinding<>(this, ExcelSheet.class, DataBinding.BindingDefinitionType.GET);
				excelSheet.setBindingName("excelSheet");
			}
			return excelSheet;
		}

		@Override
		public void setExcelSheet(DataBinding<ExcelSheet> excelSheet) {
			if (excelSheet != null) {
				excelSheet.setOwner(this);
				excelSheet.setDeclaredType(ExcelSheet.class);
				excelSheet.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				excelSheet.setBindingName("excelSheet");
			}
			this.excelSheet = excelSheet;
		}

		@Override
		public DataBinding<Integer> getRowIndex() {
			if (rowIndex == null) {
				rowIndex = new DataBinding<>(this, Integer.class, DataBinding.BindingDefinitionType.GET);
				rowIndex.setBindingName("rowIndex");
			}
			return rowIndex;
		}

		@Override
		public void setRowIndex(DataBinding<Integer> rowIndex) {
			if (rowIndex != null) {
				rowIndex.setOwner(this);
				rowIndex.setDeclaredType(Integer.class);
				rowIndex.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				rowIndex.setBindingName("rowIndex");
			}
			this.rowIndex = rowIndex;
		}

	}

	@DefineValidationRule
	public static class SheetBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddExcelRow> {
		public SheetBindingIsRequiredAndMustBeValid() {
			super("'sheet'_binding_is_required_and_must_be_valid", AddExcelRow.class);
		}

		@Override
		public DataBinding<ExcelSheet> getBinding(AddExcelRow object) {
			return object.getExcelSheet();
		}

	}

	@DefineValidationRule
	public static class RowIndexBindingIsRequiredAndMustBeValid extends BindingIsRequiredAndMustBeValid<AddExcelRow> {
		public RowIndexBindingIsRequiredAndMustBeValid() {
			super("'rowindex'_binding_is_required_and_must_be_valid", AddExcelRow.class);
		}

		@Override
		public DataBinding<Integer> getBinding(AddExcelRow object) {
			return object.getRowIndex();
		}

	}

}
