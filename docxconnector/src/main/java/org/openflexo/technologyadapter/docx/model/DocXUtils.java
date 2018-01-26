/*
 * (c) Copyright 2013 Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.technologyadapter.docx.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Text;
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.doc.FlexoDocRun;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.toolbox.StringUtils;

/**
 * Utils used in the context of {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
public class DocXUtils {

	public static String debugStructuredContents(FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element, int indent) {
		if (element instanceof DocXParagraph) {
			DocXParagraph paragraph = (DocXParagraph) element;
			StringBuffer result = new StringBuffer();
			result.append(StringUtils.buildWhiteSpaceIndentation(indent * 2) + " > [" + paragraph.getIdentifier() + "/"
					+ paragraph.getIndex() + "] { ");
			for (FlexoDocRun<DocXDocument, DocXTechnologyAdapter> run : paragraph.getRuns()) {
				if (run instanceof DocXTextRun) {
					result.append("(" + ((DocXTextRun) run).getText() + ")");
				}
				if (run instanceof DocXDrawingRun) {
					result.append("(DRAWING)");
				}
			}
			result.append(" }\n");
			for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> e : element.getChildrenElements()) {
				result.append(debugStructuredContents(e, indent + 1));
			}
			return result.toString();
		}
		if (element instanceof DocXTable) {
			DocXTable table = (DocXTable) element;
			StringBuffer result = new StringBuffer();
			for (int i = 0; i < table.getTableRows().size(); i++) {
				result.append(StringUtils.buildWhiteSpaceIndentation(indent * 2)
						+ (i == 0 ? " > [" + table.getIdentifier() + "/" + table.getIndex() + "]" : "           ") + "/"
						+ table.getTableRows().get(i).getIdentifier() + "/");
				for (int j = 0; j < table.getTableRows().get(i).getTableCells().size(); j++) {
					result.append("[ " + " " + debugStructuredContents((DocXTableCell) table.getCell(i, j)) + "] ");
				}
				result.append("\n");
			}
			return result.toString();
		}
		if (element instanceof DocXSdtBlock) {
			DocXSdtBlock sdtBlock = (DocXSdtBlock) element;
			StringBuffer result = new StringBuffer();
			result.append(StringUtils.buildWhiteSpaceIndentation(indent * 2) + " > [DocXSdtBlock" + sdtBlock.getIdentifier() + "/"
					+ sdtBlock.getIndex() + "] { ");
			result.append(" }\n");
			for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> e : element.getChildrenElements()) {
				result.append(debugStructuredContents(e, indent + 1));
			}
			return result.toString();
		}
		if (element instanceof DocXUnmappedElement) {
			DocXUnmappedElement unmapped = (DocXUnmappedElement) element;
			StringBuffer result = new StringBuffer();
			result.append(StringUtils.buildWhiteSpaceIndentation(indent * 2) + " > [DocXUnmappedElement/" + unmapped.getDocXObject() + "/"
					+ unmapped.getIndex() + "] { ");
			result.append(" }\n");
			for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> e : element.getChildrenElements()) {
				result.append(debugStructuredContents(e, indent + 1));
			}
			return result.toString();
		}
		return StringUtils.buildWhiteSpaceIndentation(indent * 2) + " ???";
	}

	public static String debugStructuredContents(DocXParagraph paragraph) {
		StringBuffer result = new StringBuffer();
		result.append("{ ");
		for (FlexoDocRun<DocXDocument, DocXTechnologyAdapter> run : paragraph.getRuns()) {
			if (run instanceof DocXTextRun) {
				result.append("(" + ((DocXTextRun) run).getText() + ")");
			}
			if (run instanceof DocXDrawingRun) {
				result.append("(DRAWING)");
			}
		}
		result.append(" }");
		return result.toString();
	}

	public static String debugStructuredContents(DocXTableCell cell) {
		StringBuffer result = new StringBuffer();
		for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element : cell.getElements()) {
			if (element instanceof DocXParagraph) {
				result.append(debugStructuredContents((DocXParagraph) element) + " ");
			}
		}
		return result.toString();
	}

	public static String printContents(Object obj, int indent) {
		StringBuffer result = new StringBuffer();
		if (obj instanceof JAXBElement)
			obj = ((JAXBElement<?>) obj).getValue();

		String objectAsString = obj.toString();
		if (obj instanceof Text) {
			objectAsString = objectAsString + "[" + ((Text) obj).getValue() + "]";
		}

		result.append(StringUtils.buildWhiteSpaceIndentation(indent * 2) + " > " + "[" + obj.getClass().getSimpleName() + "] "
				+ objectAsString + "\n");

		if (obj instanceof ContentAccessor) {
			indent++;
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.append(printContents(child, indent));
			}

		}
		return result.toString();
	}

	public static <T> List<T> getAllElementsFromObject(Object obj, Class<T> toSearch) {
		List<T> result = new ArrayList<T>();
		if (obj instanceof JAXBElement)
			obj = ((JAXBElement<?>) obj).getValue();

		if (toSearch.isAssignableFrom(obj.getClass())) {
			result.add((T) obj);
		}
		else if (obj instanceof ContentAccessor) {
			List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.addAll(getAllElementsFromObject(child, toSearch));
			}

		}
		return result;
	}

	/*private void replacePlaceholder(WordprocessingMLPackage template, String name, String placeholder) {
		List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);
	
		for (Object text : texts) {
			Text textElement = (Text) text;
			if (textElement.getValue().equals(placeholder)) {
				textElement.setValue(name);
			}
		}
	}*/

	/**
	 * Handle removing of an item in a list where objects are possibly embedded as {@link JAXBElement}
	 * 
	 * @param o
	 * @param list
	 * @return
	 */
	public static boolean removeFromList(Object o, List<Object> list) {
		if (list.contains(o)) {
			list.remove(o);
			return true;
		}
		for (Object o2 : list) {
			if (o2 instanceof JAXBElement) {
				Object value = ((JAXBElement<?>) o2).getValue();
				if (o.equals(value)) {
					list.remove(o2);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Handle removing of an item in a list where objects are possibly embedded as {@link JAXBElement}
	 * 
	 * @param o
	 * @param list
	 * @return
	 */
	public static int indexOf(Object o, ContentAccessor container) {
		List<Object> list = container.getContent();
		if (list.contains(o)) {
			return list.indexOf(o);
		}
		int i = 0;
		for (Object o2 : list) {
			if (o2 instanceof JAXBElement) {
				Object value = ((JAXBElement<?>) o2).getValue();
				if (o.equals(value)) {
					return i;
				}
			}
			i++;
		}
		return -1;
	}

}
