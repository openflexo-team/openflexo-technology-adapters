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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.editionaction.AbstractFetchRequest;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.technologyadapter.excel.BasicExcelModelSlot;
import org.openflexo.technologyadapter.excel.model.ExcelRow;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;

@ModelEntity(isAbstract = true)
@ImplementationClass(AbstractSelectExcelRow.AbstractSelectExcelRowImpl.class)
public interface AbstractSelectExcelRow<AT> extends AbstractFetchRequest<BasicExcelModelSlot, ExcelWorkbook, ExcelRow, AT> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String EXCEL_SHEET_KEY = "excelSheet";

	@Getter(value = EXCEL_SHEET_KEY)
	@XMLAttribute
	public DataBinding<ExcelSheet> getExcelSheet();

	@Setter(EXCEL_SHEET_KEY)
	public void setExcelSheet(DataBinding<ExcelSheet> excelSheet);

	public static abstract class AbstractSelectExcelRowImpl<AT>
			extends AbstractFetchRequestImpl<BasicExcelModelSlot, ExcelWorkbook, ExcelRow, AT> implements AbstractSelectExcelRow<AT> {

		private static final Logger logger = Logger.getLogger(AbstractSelectExcelRow.class.getPackage().getName());

		private DataBinding<ExcelSheet> excelSheet;

		public AbstractSelectExcelRowImpl() {
			super();
		}

		@Override
		public Type getFetchedType() {
			return ExcelRow.class;
		}

		@Override
		public List<ExcelRow> performExecute(RunTimeEvaluationContext evaluationContext) {

			ExcelWorkbook excelWorkbook = getReceiver(evaluationContext);

			//System.out.println("getReceiver()=" + getReceiver());
			//System.out.println("getReceiver() valid=" + getReceiver().isValid());
			//System.out.println("getReceiver() reason=" + getReceiver().invalidBindingReason());
			//System.out.println("excelWorkbook=" + excelWorkbook);

			List<ExcelRow> selectedExcelRows = new ArrayList<>();
			ExcelSheet excelSheet;
			try {
				excelSheet = getExcelSheet().getBindingValue(evaluationContext);

				//System.out.println("getExcelSheet()=" + getExcelSheet());
				//System.out.println("excelSheet=" + excelSheet);

				if (excelSheet != null) {
					selectedExcelRows.addAll(excelSheet.getExcelRows());
				}
				else {
					for (ExcelSheet excelSheetItem : excelWorkbook.getExcelSheets()) {
						selectedExcelRows.addAll(excelSheetItem.getExcelRows());
					}
				}
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}

			List<ExcelRow> returned = filterWithConditions(selectedExcelRows, evaluationContext);

			return returned;

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
	}
}
