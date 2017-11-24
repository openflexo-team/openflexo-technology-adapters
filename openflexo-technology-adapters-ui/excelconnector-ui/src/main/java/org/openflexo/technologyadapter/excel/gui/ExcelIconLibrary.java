/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.excel.gui;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.icon.IconMarker;
import org.openflexo.icon.ImageIconResource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.excel.model.ExcelCell;
import org.openflexo.technologyadapter.excel.model.ExcelColumn;
import org.openflexo.technologyadapter.excel.model.ExcelRow;
import org.openflexo.technologyadapter.excel.model.ExcelSheet;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;

public class ExcelIconLibrary {

	private static final Logger logger = Logger.getLogger(ExcelIconLibrary.class.getPackage().getName());

	public static final ImageIconResource EXCEL_TECHNOLOGY_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/ExcelBig.png"));
	public static final ImageIconResource EXCEL_TECHNOLOGY_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/ExcelSmall.png"));
	public static final ImageIconResource EXCEL_GRAPHICAL_ACTION_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/GraphicalActionIcon.png"));

	public static final ImageIconResource ADD_EXCEL_CELL_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/AddExcelCell.png"));
	public static final ImageIconResource ADD_EXCEL_ROW_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/AddExcelRow.png"));
	public static final ImageIconResource ADD_EXCEL_SHEET_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/ExcelSmall.png"));

	public static final ImageIconResource EXCEL_CELL_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/ExcelCell.png"));
	public static final ImageIconResource EXCEL_ROW_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/ExcelRow.png"));
	public static final ImageIconResource EXCEL_COLUMN_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/ExcelColumn.png"));
	public static final ImageIconResource EXCEL_SHEET_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/ExcelSmall.png"));

	public static final ImageIconResource EXCEL_MARKER_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/ExcelMarker.png"));
	public static final IconMarker EXCEL_MARKER = new IconMarker(EXCEL_MARKER_ICON, 8, 0);

	public static ImageIcon iconForObject(Class<? extends TechnologyObject<?>> objectClass) {
		if (ExcelWorkbook.class.isAssignableFrom(objectClass)) {
			return EXCEL_TECHNOLOGY_ICON;
		}
		else if (ExcelCell.class.isAssignableFrom(objectClass)) {
			return EXCEL_CELL_ICON;
		}
		else if (ExcelSheet.class.isAssignableFrom(objectClass)) {
			return EXCEL_SHEET_ICON;
		}
		else if (ExcelRow.class.isAssignableFrom(objectClass)) {
			return EXCEL_ROW_ICON;
		}
		else if (ExcelColumn.class.isAssignableFrom(objectClass)) {
			return EXCEL_COLUMN_ICON;
		}
		logger.warning("No icon for " + objectClass);
		return null;
	}

}
