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

import org.apache.poi.ss.util.CellReference;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;

/**
 * Represents an Excel column
 * 
 * @author vincent, sylvain
 * 
 */
@ModelEntity
@ImplementationClass(value = ExcelCell.ExcelCellImpl.class)
@XMLElement
public interface ExcelColumn extends ExcelObject {

	public static String getColumnLetters(int colIndex) {
		return CellReference.convertNumToColString(colIndex);
	}

	public static int getColumnIndex(String columnLetter) {
		return CellReference.convertColStringToIndex(columnLetter.toUpperCase());
	}

	@PropertyIdentifier(type = Integer.class)
	public static final String COLUMN_INDEX = "columnIndex";
	@PropertyIdentifier(type = ExcelSheet.class)
	public static final String EXCEL_SHEET_KEY = "excelSheet";

	/**
	 * Return {@link ExcelSheet} where this {@link ExcelRow} is defined
	 * 
	 * @return
	 */
	@Getter(value = EXCEL_SHEET_KEY)
	public ExcelSheet getExcelSheet();

	/**
	 * Sets {@link ExcelSheet} where this {@link ExcelRow} is defined
	 * 
	 * @param workbook
	 */
	@Setter(EXCEL_SHEET_KEY)
	public void setExcelSheet(ExcelSheet sheet);

	/**
	 * Get index of column beeing represented
	 * 
	 * @return
	 */
	@Getter(value = COLUMN_INDEX, defaultValue = "-1")
	public int getColumnIndex();

	/**
	 * Sets index of column beeing represented
	 * 
	 * @param sheet
	 */
	@Setter(COLUMN_INDEX)
	public void setColumnIndex(int index);

	/**
	 * Default base implementation for {@link ExcelColumn}
	 * 
	 * @author sylvain
	 *
	 */
	public static abstract class ExcelColumnImpl extends ExcelObjectImpl implements ExcelColumn {

	}
}
