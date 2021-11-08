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
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
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
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;

@ModelEntity
@ImplementationClass(MergeCells.MergeCellsImpl.class)
@XMLElement
@FML("AddExcelCell")
public interface MergeCells extends ExcelAction<ExcelCell> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String START_CELL_KEY = "startCell";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String END_CELL_KEY = "endCell";

	@Getter(value = START_CELL_KEY)
	@XMLAttribute
	public DataBinding<ExcelCell> getStartCell();

	@Setter(START_CELL_KEY)
	public void setStartCell(DataBinding<ExcelCell> cell);

	@Getter(value = END_CELL_KEY)
	@XMLAttribute
	public DataBinding<ExcelCell> getEndCell();

	@Setter(END_CELL_KEY)
	public void setEndCell(DataBinding<ExcelCell> cell);

	public static abstract class MergeCellsImpl
			extends TechnologySpecificActionDefiningReceiverImpl<BasicExcelModelSlot, ExcelWorkbook, ExcelCell> implements MergeCells {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(MergeCells.class.getPackage().getName());

		private DataBinding<ExcelCell> startCell;
		private DataBinding<ExcelCell> endCell;

		@Override
		public Type getAssignableType() {
			return ExcelCell.class;
		}

		@Override
		public ExcelCell execute(RunTimeEvaluationContext evaluationContext) {

			try {
				ExcelCell startCell = getStartCell().getBindingValue(evaluationContext);
				ExcelCell endCell = getEndCell().getBindingValue(evaluationContext);

				System.out.println("On doit merger " + startCell + " et " + endCell);

				CellRangeAddress region = new CellRangeAddress(startCell.getRowIndex(), endCell.getRowIndex(), startCell.getColumnIndex(),
						endCell.getColumnIndex());
				startCell.getExcelSheet().getSheet().addMergedRegion(region);

				RegionUtil.setBorderTop(BorderStyle.MEDIUM, region, startCell.getExcelSheet().getSheet());
				RegionUtil.setBorderLeft(BorderStyle.MEDIUM, region, startCell.getExcelSheet().getSheet());
				RegionUtil.setBorderRight(BorderStyle.MEDIUM, region, startCell.getExcelSheet().getSheet());
				RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region, startCell.getExcelSheet().getSheet());

				return startCell;
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		public DataBinding<ExcelCell> getStartCell() {
			if (startCell == null) {
				startCell = new DataBinding<>(this, ExcelCell.class, DataBinding.BindingDefinitionType.GET);
				startCell.setBindingName("startCell");
			}
			return startCell;
		}

		@Override
		public void setStartCell(DataBinding<ExcelCell> startCell) {
			if (startCell != null) {
				startCell.setOwner(this);
				startCell.setDeclaredType(ExcelCell.class);
				startCell.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				startCell.setBindingName("startCell");
			}
			this.startCell = startCell;
		}

		@Override
		public DataBinding<ExcelCell> getEndCell() {
			if (endCell == null) {
				endCell = new DataBinding<>(this, ExcelCell.class, DataBinding.BindingDefinitionType.GET);
				endCell.setBindingName("endCell");
			}
			return endCell;
		}

		@Override
		public void setEndCell(DataBinding<ExcelCell> endCell) {
			if (endCell != null) {
				endCell.setOwner(this);
				endCell.setDeclaredType(ExcelCell.class);
				endCell.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				endCell.setBindingName("endCell");
			}
			this.endCell = endCell;
		}

	}
}
