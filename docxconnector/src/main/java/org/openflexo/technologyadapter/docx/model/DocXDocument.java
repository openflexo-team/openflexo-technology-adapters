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

import org.docx4j.model.styles.Node;
import org.docx4j.model.styles.StyleTree;
import org.docx4j.model.styles.StyleTree.AugmentedStyle;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.Text;
import org.openflexo.foundation.doc.FlexoDocument;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.toolbox.StringUtils;

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

	public static abstract class DocXDocumentImpl extends FlexoDocumentImpl<DocXDocument, DocXTechnologyAdapter> implements DocXDocument {

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

			StyleTree styleTree = mdp.getStyleTree();

			System.out.println("les styles disponibles=" + styleTree.getParagraphStylesTree());

			registerStyle(styleTree.getParagraphStylesTree().getRootElement(), null);

			performSuperSetter(WORD_PROCESSING_ML_PACKAGE_KEY, wpmlPackage);

		}

		private void registerStyle(Node<AugmentedStyle> styleNode, Node<AugmentedStyle> parentNode) {
			if (styleNode != null && styleNode.getData() != null) {
				System.out.println("> Found style " + styleNode + " based on " + parentNode);
				for (Node<AugmentedStyle> child : styleNode.getChildren()) {
					registerStyle(child, styleNode);
				}
			}
		}

		@Override
		public String debugContents() {
			return printContents(getWordprocessingMLPackage().getMainDocumentPart(), 0);
		}

		private String printContents(Object obj, int indent) {
			StringBuffer result = new StringBuffer();
			if (obj instanceof JAXBElement)
				obj = ((JAXBElement<?>) obj).getValue();

			result.append(StringUtils.buildWhiteSpaceIndentation(indent * 2) + " > " + "[" + obj.getClass().getSimpleName() + "] " + obj
					+ "\n");

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
