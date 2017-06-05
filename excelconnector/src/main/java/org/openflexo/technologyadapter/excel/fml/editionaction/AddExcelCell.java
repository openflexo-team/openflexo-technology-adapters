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
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Cell;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.excel.BasicExcelModelSlot;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelCell.CellType;
import org.openflexo.technologyadapter.excel.model.ExcelRow;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;

@ModelEntity
@ImplementationClass(AddExcelCell.AddExcelCellImpl.class)
@XMLElement
@FML("AddExcelCell")
public interface AddExcelCell extends ExcelAction<ExcelCell> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String ROW_KEY = "row";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String VALUE_KEY = "value";
	@PropertyIdentifier(type = CellType.class)
	public static final String CELL_TYPE_KEY = "cellType";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String COLUMN_INDEX_KEY = "columnIndex";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String ROW_INDEX_KEY = "rowIndex";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String SHEET_KEY = "sheet";
	@PropertyIdentifier(type = boolean.class)
	public static final String IS_ROW_INDEX_KEY = "isRowIndex";

	@Getter(value = ROW_KEY)
	@XMLAttribute
	public DataBinding<ExcelRow> getRow();

	@Setter(ROW_KEY)
	public void setRow(DataBinding<ExcelRow> row);

	@Getter(value = VALUE_KEY)
	@XMLAttribute
	public DataBinding<Object> getValue();

	@Setter(VALUE_KEY)
	public void setValue(DataBinding<Object> value);

	@Getter(value = CELL_TYPE_KEY)
	@XMLAttribute
	public CellType getCellType();

	@Setter(CELL_TYPE_KEY)
	public void setCellType(CellType cellType);

	@Getter(value = COLUMN_INDEX_KEY)
	@XMLAttribute
	public DataBinding<Integer> getColumnIndex();

	@Setter(COLUMN_INDEX_KEY)
	public void setColumnIndex(DataBinding<Integer> columnIndex);

	@Getter(value = ROW_INDEX_KEY)
	@XMLAttribute
	public DataBinding<Integer> getRowIndex();

	@Setter(ROW_INDEX_KEY)
	public void setRowIndex(DataBinding<Integer> rowIndex);

	@Getter(value = SHEET_KEY)
	@XMLAttribute
	public DataBinding<ExcelSheet> getSheet();

	@Setter(SHEET_KEY)
	public void setSheet(DataBinding<ExcelSheet> sheet);

	@Getter(value = IS_ROW_INDEX_KEY, defaultValue = "false")
	@XMLAttribute
	public boolean isRowIndex();

	@Setter(IS_ROW_INDEX_KEY)
	public void setRowIndex(boolean isRowIndex);

	public List<CellType> getAvailableCellTypes();

	public static abstract class AddExcelCellImpl extends TechnologySpecificActionImpl<BasicExcelModelSlot, ExcelWorkbook, ExcelCell>
			implements AddExcelCell {

		private static final Logger logger = Logger.getLogger(AddExcelCell.class.getPackage().getName());

		private DataBinding<Object> value;

		private DataBinding<Integer> columnIndex;

		private DataBinding<Integer> rowIndex;

		private DataBinding<ExcelRow> row;

		private DataBinding<ExcelSheet> sheet;

		private CellType cellType = null;

		// private boolean isRowIndex = false;

		public AddExcelCellImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return ExcelCell.class;
		}

		@Override
		public ExcelCell execute(RunTimeEvaluationContext evaluationContext) {

			ExcelCell excelCell = null;

			FreeModelSlotInstance<ExcelWorkbook, BasicExcelModelSlot> modelSlotInstance = getModelSlotInstance(evaluationContext);
			if (modelSlotInstance != null && modelSlotInstance.getResourceData() != null) {

				try {
					ExcelRow excelRow = null;
					if (isRowIndex()) {
						Integer rowIndex = getRowIndex().getBindingValue(evaluationContext);
						ExcelSheet excelSheet = getSheet().getBindingValue(evaluationContext);
						if (excelSheet != null && rowIndex != null) {
							excelRow = excelSheet.getRowAt(rowIndex);
						}
						else if (excelSheet == null) {
							logger.severe("Excel sheet is not defined.");
						}
						else if (rowIndex == null) {
							logger.severe("Row index is not defined.");
						}

					}
					else {
						excelRow = getRow().getBindingValue(evaluationContext);
					}

					Integer columnIndex = getColumnIndex().getBindingValue(evaluationContext);
					// If this is possible, create the cell
					if (columnIndex != null) {
						if (excelRow != null) {
							Cell cell = null;
							Object value = getValue().getBindingValue(evaluationContext);
							// If this cell exists, just get it
							if (excelRow.getCellAt(columnIndex) != null) {
								excelCell = excelRow.getCellAt(columnIndex);
							}
							else {
								cell = excelRow.getRow().createCell(columnIndex);
								excelCell = modelSlotInstance.getAccessedResourceData().getConverter().convertExcelCellToCell(cell,
										excelRow, null);
							}
							if (value != null) {
								excelCell.setCellValue(value);
							}
							else {
								logger.warning("Create a cell requires a value.");
							}
							modelSlotInstance.getResourceData().setIsModified();
						}
						else {
							logger.warning("Create a cell requires a row.");
						}
					}
					else {
						logger.warning("Create a cell requires a column index.");
					}
				} catch (TypeMismatchException e) {
					e.printStackTrace();
				} catch (NullReferenceException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

			}
			else {
				logger.warning("Model slot not correctly initialised : model is null");
				return null;
			}

			return excelCell;
		}

		@Override
		public FreeModelSlotInstance<ExcelWorkbook, BasicExcelModelSlot> getModelSlotInstance(RunTimeEvaluationContext evaluationContext) {
			return (FreeModelSlotInstance<ExcelWorkbook, BasicExcelModelSlot>) super.getModelSlotInstance(evaluationContext);
		}

		@Override
		public DataBinding<Object> getValue() {
			if (value == null) {
				value = new DataBinding<>(this, Object.class, DataBinding.BindingDefinitionType.GET);
				value.setBindingName("value");
			}
			return value;
		}

		@Override
		public void setValue(DataBinding<Object> value) {
			if (value != null) {
				value.setOwner(this);
				value.setDeclaredType(Object.class);
				value.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				value.setBindingName("value");
			}
			this.value = value;
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

		@Override
		public DataBinding<Integer> getColumnIndex() {
			if (columnIndex == null) {
				columnIndex = new DataBinding<>(this, Integer.class, DataBinding.BindingDefinitionType.GET);
				columnIndex.setBindingName("columnIndex");
			}
			return columnIndex;
		}

		@Override
		public void setColumnIndex(DataBinding<Integer> columnIndex) {
			if (columnIndex != null) {
				columnIndex.setOwner(this);
				columnIndex.setDeclaredType(Integer.class);
				columnIndex.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				columnIndex.setBindingName("columnIndex");
			}
			this.columnIndex = columnIndex;
		}

		@Override
		public CellType getCellType() {
			if (cellType == null) {
				if (_cellTypeName != null) {
					for (CellType cellType : getAvailableCellTypes()) {
						if (cellType.name().equals(_cellTypeName)) {
							return cellType;
						}
					}
				}
			}
			return cellType;
		}

		@Override
		public void setCellType(CellType cellType) {
			this.cellType = cellType;
		}

		private List<CellType> availableCellTypes = null;

		@Override
		public List<CellType> getAvailableCellTypes() {
			if (availableCellTypes == null) {
				availableCellTypes = new Vector<>();
				for (CellType cellType : ExcelCell.CellType.values()) {
					availableCellTypes.add(cellType);
				}
			}
			return availableCellTypes;
		}

		private String _cellTypeName = null;

		public String _getGraphicalFeatureName() {
			if (getCellType() == null) {
				return _cellTypeName;
			}
			return getCellType().name();
		}

		public void _setCellTypeName(String cellTypeName) {
			_cellTypeName = cellTypeName;
		}

		@Override
		public DataBinding<ExcelRow> getRow() {
			if (row == null) {
				row = new DataBinding<>(this, ExcelRow.class, DataBinding.BindingDefinitionType.GET);
				row.setBindingName("row");
			}
			return row;
		}

		@Override
		public void setRow(DataBinding<ExcelRow> row) {
			if (row != null) {
				row.setOwner(this);
				row.setDeclaredType(ExcelRow.class);
				row.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				row.setBindingName("row");
			}
			this.row = row;
		}

		@Override
		public DataBinding<ExcelSheet> getSheet() {
			if (sheet == null) {
				sheet = new DataBinding<>(this, ExcelSheet.class, DataBinding.BindingDefinitionType.GET);
				sheet.setBindingName("sheet");
			}
			return sheet;
		}

		@Override
		public void setSheet(DataBinding<ExcelSheet> sheet) {
			if (sheet != null) {
				sheet.setOwner(this);
				sheet.setDeclaredType(ExcelSheet.class);
				sheet.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				sheet.setBindingName("sheet");
			}
			this.sheet = sheet;
		}

		/*@Override
		public boolean isRowIndex() {
			return isRowIndex;
		}
		
		@Override
		public void setRowIndex(boolean isRowIndex) {
			this.isRowIndex = isRowIndex;
		}*/

	}
}
