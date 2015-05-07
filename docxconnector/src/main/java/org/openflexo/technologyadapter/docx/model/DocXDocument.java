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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.docx4j.model.styles.Node;
import org.docx4j.model.styles.StyleTree;
import org.docx4j.model.styles.StyleTree.AugmentedStyle;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.Style;
import org.docx4j.wml.Text;
import org.openflexo.foundation.doc.FlexoDocument;
import org.openflexo.foundation.doc.FlexoDocumentElement;
import org.openflexo.foundation.doc.FlexoStyle;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.toolbox.StringUtils;

/**
 * Implementation of {@link FlexoDocument} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXDocument.DocXDocumentImpl.class)
@XMLElement
public interface DocXDocument extends DocXObject, FlexoDocument<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = WordprocessingMLPackage.class)
	public static final String WORD_PROCESSING_ML_PACKAGE_KEY = "wordprocessingMLPackage";

	@Getter(value = WORD_PROCESSING_ML_PACKAGE_KEY, ignoreType = true)
	public WordprocessingMLPackage getWordprocessingMLPackage();

	@Setter(WORD_PROCESSING_ML_PACKAGE_KEY)
	public void setWordprocessingMLPackage(WordprocessingMLPackage wpmlPackage);

	/**
	 * This is the starting point for updating {@link DocXDocument} with the document provided from docx4j library<br>
	 * Take care that the supplied wpmlPackage is the object we should update with, but that {@link #getWordprocessingMLPackage()} is unsafe
	 * in this context, because return former value
	 */
	public void updateFromWordprocessingMLPackage(WordprocessingMLPackage wpmlPackage, DocXFactory factory);

	public String debugContents();

	public String debugStructuredContents();

	public static abstract class DocXDocumentImpl extends FlexoDocumentImpl<DocXDocument, DocXTechnologyAdapter>implements DocXDocument {

		@Override
		public DocXDocument getFlexoDocument() {
			return this;
		}

		@Override
		public void setWordprocessingMLPackage(WordprocessingMLPackage wpmlPackage) {
			if ((wpmlPackage == null && getWordprocessingMLPackage() != null)
					|| (wpmlPackage != null && !wpmlPackage.equals(getWordprocessingMLPackage()))) {
				updateFromWordprocessingMLPackage(wpmlPackage, ((DocXDocumentResource) getResource()).getFactory());
			}
		}

		/**
		 * This is the starting point for updating {@link DocXDocument} with the document provided from docx4j library<br>
		 * Take care that the supplied wpmlPackage is the object we should update with, but that {@link #getWordprocessingMLPackage()} is
		 * unsafe in this context, because return former value
		 */
		@Override
		public void updateFromWordprocessingMLPackage(WordprocessingMLPackage wpmlPackage, DocXFactory factory) {

			System.out.println("wpmlPackage=" + wpmlPackage);

			MainDocumentPart mdp = wpmlPackage.getMainDocumentPart();
			for (Object o : mdp.getContent()) {
				if (o instanceof P) {
					DocXParagraph paragraph = factory.makeNewDocXParagraph((P) o);
					addToElements(paragraph);
				}
			}

			updateStylesFromWmlPackage(wpmlPackage, factory);

			// Then we call generic method, where notification will be thrown
			performSuperSetter(WORD_PROCESSING_ML_PACKAGE_KEY, wpmlPackage);

		}

		private final Map<Style, DocXStyle> styles = new HashMap<>();

		private void updateStylesFromWmlPackage(WordprocessingMLPackage wpmlPackage, DocXFactory factory) {

			StyleTree styleTree = wpmlPackage.getMainDocumentPart().getStyleTree();

			List<DocXStyle> stylesToRemove = new ArrayList<>(styles.values());

			registerStyle(styleTree.getParagraphStylesTree().getRootElement(), null, stylesToRemove, factory);

			for (DocXStyle styleToRemove : stylesToRemove) {
				removeFromStyles(styleToRemove);
			}

			/*StyleDefinitionsPart sdp = wpmlPackage.getMainDocumentPart().getStyleDefinitionsPart();
			try {
				for (Style s : sdp.getContents().getStyle()) {
					System.out.println("# style " + s.getName().getVal() + " "
							+ (s.getUiPriority() != null ? s.getUiPriority().getVal() : ""));
				}
			} catch (Docx4JException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

			/*for (FlexoStyle<DocXDocument, DocXTechnologyAdapter> s : getStyles()) {
				System.out.println("% Style " + s.getStyleId() + " name=" + s.getName());
			}*/

			// Temporaray solution to provide structuring styles
			for (int i = 1; i < 10; i++) {
				DocXStyle headingStyle = (DocXStyle) getStyleByName("heading " + i);
				if (headingStyle != null) {
					addToStructuringStyles(headingStyle);
				}
			}

			for (DocXParagraph p : getElements(DocXParagraph.class)) {
				if (p.getP() != null && p.getP().getPPr() != null && p.getP().getPPr().getPStyle() != null) {
					String styleName = p.getP().getPPr().getPStyle().getVal();
					FlexoStyle<DocXDocument, DocXTechnologyAdapter> paragraphStyle = getStyleByIdentifier(styleName);
					if (paragraphStyle != null) {
						p.setStyle(paragraphStyle);
					}
				}
			}

		}

		private void registerStyle(Node<AugmentedStyle> styleNode, Node<AugmentedStyle> parentNode, List<DocXStyle> stylesToRemove,
				DocXFactory factory) {
			if (styleNode != null && styleNode.getData() != null) {
				Style style = styleNode.getData().getStyle();
				// System.out.println("Registering Style " + style.getName().getVal());
				// System.out.println("StyleId " + style.getStyleId());
				// System.out.println("Aliases " + style.getAliases());
				DocXStyle parentStyle = (parentNode != null ? styles.get(parentNode.data.getStyle()) : null);
				DocXStyle docXStyle = styles.get(style);
				if (docXStyle != null) {
					stylesToRemove.remove(docXStyle);
				} else {
					docXStyle = factory.makeNewDocXStyle(style, parentStyle);
					addToStyles(docXStyle);
				}
				docXStyle.updateFromStyle(style, factory);
				for (Node<AugmentedStyle> child : styleNode.getChildren()) {
					registerStyle(child, styleNode, stylesToRemove, factory);
				}
			}
		}

		@Override
		public void addToStyles(FlexoStyle<DocXDocument, DocXTechnologyAdapter> aStyle) {
			performSuperAdder(STYLES_KEY, aStyle);
			styles.put(((DocXStyle) aStyle).getStyle(), (DocXStyle) aStyle);
		}

		@Override
		public void removeFromStyles(FlexoStyle<DocXDocument, DocXTechnologyAdapter> aStyle) {
			performSuperRemover(STYLES_KEY, aStyle);
			styles.remove(((DocXStyle) aStyle).getStyle());
		}

		@Override
		public String debugContents() {
			return printContents(getWordprocessingMLPackage().getMainDocumentPart(), 0);
		}

		@Override
		public String debugStructuredContents() {
			StringBuffer result = new StringBuffer();
			for (FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> e : getRootElements()) {
				result.append(debugStructuredContents(e, 1));
			}
			return result.toString();
		}

		private String debugStructuredContents(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> element, int indent) {
			StringBuffer result = new StringBuffer();
			result.append(StringUtils.buildWhiteSpaceIndentation(indent * 2) + " > " + element + "\n");
			for (FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> e : element.getChildrenElements()) {
				result.append(debugStructuredContents(e, indent + 1));
			}
			return result.toString();
		}

		private String printContents(Object obj, int indent) {
			StringBuffer result = new StringBuffer();
			if (obj instanceof JAXBElement)
				obj = ((JAXBElement<?>) obj).getValue();

			result.append(
					StringUtils.buildWhiteSpaceIndentation(indent * 2) + " > " + "[" + obj.getClass().getSimpleName() + "] " + obj + "\n");

			if (obj instanceof ContentAccessor) {
				indent++;
				List<?> children = ((ContentAccessor) obj).getContent();
				for (Object child : children) {
					result.append(printContents(child, indent));
				}

			}
			return result.toString();
		}

		private static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
			List<Object> result = new ArrayList<Object>();
			if (obj instanceof JAXBElement)
				obj = ((JAXBElement<?>) obj).getValue();

			if (obj.getClass().equals(toSearch))
				result.add(obj);
			else if (obj instanceof ContentAccessor) {
				List<?> children = ((ContentAccessor) obj).getContent();
				for (Object child : children) {
					result.addAll(getAllElementFromObject(child, toSearch));
				}

			}
			return result;
		}

		private void replacePlaceholder(WordprocessingMLPackage template, String name, String placeholder) {
			List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), Text.class);

			for (Object text : texts) {
				Text textElement = (Text) text;
				if (textElement.getValue().equals(placeholder)) {
					textElement.setValue(name);
				}
			}
		}

	}

}
