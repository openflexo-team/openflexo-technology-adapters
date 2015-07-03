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

import java.awt.Color;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;


public class ExcelStyleManager {
	
	private final ExcelWorkbook workbook;
	
	public enum CellStyleFeature {
		Foreground, Background, Pattern, Alignment, Font, BorderBottom, BorderTop, BorderLeft, BorderRight
	}

	public enum CellBorderStyleFeature {
		BORDER_DASH_DOT,
		BORDER_DASH_DOT_DOT,
		BORDER_DASHED,
		BORDER_DOTTED,
		BORDER_DOUBLE,
		BORDER_HAIR,
		BORDER_MEDIUM,
		BORDER_MEDIUM_DASH_DOT,
		BORDER_MEDIUM_DASH_DOT_DOT,
		BORDER_MEDIUM_DASHED,
		BORDER_NONE,
		BORDER_SLANTED_DASH_DOT,
		BORDER_THICK,
		BORDER_THIN
	}

	public enum CellAlignmentStyleFeature {
		ALIGN_CENTER,
		ALIGN_CENTER_SELECTION,
		ALIGN_FILL,
		ALIGN_GENERAL,
		ALIGN_JUSTIFY,
		ALIGN_LEFT,
		ALIGN_RIGHT,
		VERTICAL_BOTTOM,
		VERTICAL_CENTER,
		VERTICAL_JUSTIFY,
		VERTICAL_TOP
	}

	public enum CellFontStyleFeature {
		ANSI_CHARSET,
		BOLDWEIGHT_BOLD,
		BOLDWEIGHT_NORMAL,
		COLOR_NORMAL,
		COLOR_RED,
		DEFAULT_CHARSET,
		SS_NONE,
		SS_SUB,
		SS_SUPER,
		SYMBOL_CHARSET,
		U_DOUBLE,
		U_DOUBLE_ACCOUNTING,
		U_NONE,
		U_SINGLE,
		U_SINGLE_ACCOUNTING
	}
	
	public enum CellPatternStyleFeature {
		ALT_BARS,
		BIG_SPOTS,
		BRICKS,
		DIAMONDS,
		FINE_DOTS,
		LEAST_DOTS,
		LESS_DOTS,
		NO_FILL,
		SOLID_FOREGROUND,
		SPARSE_DOTS,
		THICK_BACKWARD_DIAG,
		THICK_FORWARD_DIAG,
		THICK_HORZ_BANDS,
		THICK_VERT_BANDS,
		THIN_BACKWARD_DIAG,
		THIN_FORWARD_DIAG,
		THIN_HORZ_BANDS,
		THIN_VERT_BANDS;
	}
	
	public ExcelStyleManager(ExcelWorkbook workbook) {
		super();
		this.workbook = workbook;
	}

	public CellStyle udapteCellStyle(CellStyleFeature cellStyle, Object value, CellStyle oldStyle){
		// Then create a new style
		CellStyle newStyle = getWorkbook().getWorkbook().createCellStyle();
		// Apply the old parameters to the new style
		if(oldStyle!=null){
			newStyle.cloneStyleFrom(oldStyle);
		}
		// Then apply the new parameter to the new style
		switch (cellStyle) {
			case Alignment:
				newStyle.setAlignment(getPOIAlignmentStyle((CellAlignmentStyleFeature) value));
				break;
			case Font:
				newStyle.setFont((Font) value);
				break;
			case BorderBottom:
				newStyle.setBorderBottom(getPOIBorderStyle((CellBorderStyleFeature) value));
				break;
			case BorderLeft:
				newStyle.setBorderLeft(getPOIBorderStyle((CellBorderStyleFeature) value));
				break;
			case BorderRight:
				newStyle.setBorderRight(getPOIBorderStyle((CellBorderStyleFeature) value));
				break;
			case BorderTop:
				newStyle.setBorderTop(getPOIBorderStyle((CellBorderStyleFeature) value));
				break;
			case Foreground:
				if(newStyle instanceof HSSFCellStyle){
					setForeground(value, (HSSFCellStyle)newStyle);
				}else{
					setForeground(value, (XSSFCellStyle)newStyle);
				}
				break;
			case Background:
				if(newStyle instanceof HSSFCellStyle){
					setBackground(value, (HSSFCellStyle)newStyle);
				}else{
					setBackground(value, (XSSFCellStyle)newStyle);
				}
				break;
			case Pattern:
				newStyle.setFillPattern(getPOIPatternStyle((CellPatternStyleFeature) value));
			default:
				break;
		}
		return newStyle;
	}

	private HSSFColor convertColorToHSSFColor(Color color){
		HSSFPalette palette = ((HSSFWorkbook)(getWorkbook().getWorkbook())).getCustomPalette();
		HSSFColor hssfColor = palette.findColor((byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue());
		if (hssfColor == null ){
		      palette.setColorAtIndex(HSSFColor.LAVENDER.index, (byte)color.getRed(), (byte)color.getGreen(),
		(byte)color.getBlue());
		      hssfColor = palette.getColor(HSSFColor.LAVENDER.index);
		}
		return hssfColor;
	}
	
	private void setForeground(Object value, HSSFCellStyle style){
		if (value instanceof Color) {
			style.setFillForegroundColor(convertColorToHSSFColor((Color) value).getIndex());
		}
		if (value instanceof String) {
			style.setFillForegroundColor(Short.parseShort((String) value));
		}
		if (value instanceof Long) {
			style.setFillForegroundColor(((Long) value).shortValue());
		} 
	}
	
	private void setForeground(Object value, XSSFCellStyle style){
		if (value instanceof Color) {
			style.setFillForegroundColor(new XSSFColor((Color) value));
		}
		if (value instanceof String) {
			style.setFillForegroundColor(Short.parseShort((String) value));
		}
		if (value instanceof Long) {
			style.setFillForegroundColor(((Long) value).shortValue());
		} 
	}
	
	private void setBackground(Object value, XSSFCellStyle style){
		if (value instanceof Color) {
			style.setFillBackgroundColor(new XSSFColor((Color) value));
		}
		if (value instanceof String) {
			style.setFillBackgroundColor(Short.parseShort((String) value));
		}
		if (value instanceof Long) {
			style.setFillBackgroundColor(((Long) value).shortValue());
		}
	}
	
	private void setBackground(Object value, HSSFCellStyle style){
		if (value instanceof Color) {
			style.setFillBackgroundColor(convertColorToHSSFColor((Color) value).getIndex());
		}
		if (value instanceof String) {
			style.setFillBackgroundColor(Short.parseShort((String) value));
		}
		if (value instanceof Long) {
			style.setFillBackgroundColor(((Long) value).shortValue());
		} 
	}
	
	
	private Short getPOIBorderStyle(CellBorderStyleFeature borderStyle) {
		if (borderStyle.name().equals("BORDER_DASH_DOT")) {
			return CellStyle.BORDER_DASH_DOT;
		} else if (borderStyle.name().equals("BORDER_DASH_DOT_DOT")) {
			return CellStyle.BORDER_DASH_DOT_DOT;
		} else if (borderStyle.name().equals("BORDER_DASHED")) {
			return CellStyle.BORDER_DASHED;
		} else if (borderStyle.name().equals("BORDER_DOTTED")) {
			return CellStyle.BORDER_DOTTED;
		} else if (borderStyle.name().equals("BORDER_DOUBLE")) {
			return CellStyle.BORDER_DOUBLE;
		} else if (borderStyle.name().equals("BORDER_HAIR")) {
			return CellStyle.BORDER_HAIR;
		} else if (borderStyle.name().equals("BORDER_MEDIUM")) {
			return CellStyle.BORDER_MEDIUM;
		} else if (borderStyle.name().equals("BORDER_MEDIUM_DASH_DOT")) {
			return CellStyle.BORDER_MEDIUM_DASH_DOT;
		} else if (borderStyle.name().equals("BORDER_MEDIUM_DASH_DOT_DOT")) {
			return CellStyle.BORDER_MEDIUM_DASH_DOT_DOT;
		} else if (borderStyle.name().equals("BORDER_MEDIUM_DASHED")) {
			return CellStyle.BORDER_MEDIUM_DASHED;
		} else if (borderStyle.name().equals("BORDER_NONE")) {
			return CellStyle.BORDER_NONE;
		} else if (borderStyle.name().equals("BORDER_SLANTED_DASH_DOT")) {
			return CellStyle.BORDER_SLANTED_DASH_DOT;
		} else if (borderStyle.name().equals("BORDER_THICK")) {
			return CellStyle.BORDER_THICK;
		} else if (borderStyle.name().equals("BORDER_THIN")) {
			return CellStyle.BORDER_THIN;
		}
		return null;
	}

	private Short getPOIAlignmentStyle(CellAlignmentStyleFeature alignmentStyle) {
		if (alignmentStyle.name().equals("ALIGN_CENTER")) {
			return CellStyle.ALIGN_CENTER;
		} else if (alignmentStyle.name().equals("ALIGN_CENTER_SELECTION")) {
			return CellStyle.ALIGN_CENTER_SELECTION;
		} else if (alignmentStyle.name().equals("ALIGN_FILL")) {
			return CellStyle.ALIGN_FILL;
		} else if (alignmentStyle.name().equals("ALIGN_GENERAL")) {
			return CellStyle.ALIGN_GENERAL;
		} else if (alignmentStyle.name().equals("ALIGN_JUSTIFY")) {
			return CellStyle.ALIGN_JUSTIFY;
		} else if (alignmentStyle.name().equals("ALIGN_LEFT")) {
			return CellStyle.ALIGN_LEFT;
		} else if (alignmentStyle.name().equals("ALIGN_RIGHT")) {
			return CellStyle.ALIGN_RIGHT;
		} else if (alignmentStyle.name().equals("VERTICAL_BOTTOM")) {
			return CellStyle.VERTICAL_BOTTOM;
		} else if (alignmentStyle.name().equals("VERTICAL_JUSTIFY")) {
			return CellStyle.VERTICAL_JUSTIFY;
		} else if (alignmentStyle.name().equals("VERTICAL_CENTER")) {
			return CellStyle.VERTICAL_CENTER;
		} else if (alignmentStyle.name().equals("VERTICAL_TOP")) {
			return CellStyle.VERTICAL_TOP;
		}
		return null;
	}
	
	private Short getPOIPatternStyle(CellPatternStyleFeature patternStyle) {
		if (patternStyle.name().equals("ALT_BARS")) {
			return CellStyle.ALT_BARS;
		} else if (patternStyle.name().equals("BIG_SPOTS")) {
			return CellStyle.BIG_SPOTS;
		} else if (patternStyle.name().equals("BRICKS")) {
			return CellStyle.BRICKS;
		} else if (patternStyle.name().equals("DIAMONDS")) {
			return CellStyle.DIAMONDS;
		} else if (patternStyle.name().equals("FINE_DOTS")) {
			return CellStyle.FINE_DOTS;
		} else if (patternStyle.name().equals("LEAST_DOTS")) {
			return CellStyle.LEAST_DOTS;
		} else if (patternStyle.name().equals("LESS_DOTS")) {
			return CellStyle.LESS_DOTS;
		} else if (patternStyle.name().equals("NO_FILL")) {
			return CellStyle.NO_FILL;
		} else if (patternStyle.name().equals("SOLID_FOREGROUND")) {
			return CellStyle.SOLID_FOREGROUND;
		} else if (patternStyle.name().equals("SPARSE_DOTS")) {
			return CellStyle.SPARSE_DOTS;
		} else if (patternStyle.name().equals("THICK_BACKWARD_DIAG")) {
			return CellStyle.THICK_BACKWARD_DIAG;
		} else if (patternStyle.name().equals("THICK_FORWARD_DIAG")) {
			return CellStyle.THICK_FORWARD_DIAG;
		} else if (patternStyle.name().equals("THICK_HORZ_BANDS")) {
			return CellStyle.THICK_HORZ_BANDS;
		} else if (patternStyle.name().equals("THICK_VERT_BANDS")) {
			return CellStyle.THICK_VERT_BANDS;
		} else if (patternStyle.name().equals("THIN_BACKWARD_DIAG")) {
			return CellStyle.THIN_BACKWARD_DIAG;
		} else if (patternStyle.name().equals("THIN_FORWARD_DIAG")) {
			return CellStyle.THIN_FORWARD_DIAG;
		} else if (patternStyle.name().equals("THIN_HORZ_BANDS")) {
			return CellStyle.THIN_HORZ_BANDS;
		} else if (patternStyle.name().equals("THIN_VERT_BANDS")) {
			return CellStyle.THIN_VERT_BANDS;
		}
		return null;
	}

	public ExcelWorkbook getWorkbook() {
		return workbook;
	}
}
