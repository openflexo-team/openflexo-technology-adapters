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

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.Text;
import org.openflexo.foundation.resource.ResourceData;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PastingPoint;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.toolbox.StringUtils;

@ModelEntity
@ImplementationClass(DocXDocument.DocXDocumentImpl.class)
@XMLElement
public interface DocXDocument extends DocXObject, ResourceData<DocXDocument> {

	@PropertyIdentifier(type = WordprocessingMLPackage.class)
	public static final String WORD_PROCESSING_ML_PACKAGE_KEY = "wordprocessingMLPackage";

	@PropertyIdentifier(type = DocXParagraph.class, cardinality = Cardinality.LIST)
	public static final String PARAGRAPHS_KEY = "paragraphs";

	@Getter(value = WORD_PROCESSING_ML_PACKAGE_KEY, ignoreType = true)
	public WordprocessingMLPackage getWordprocessingMLPackage();

	@Setter(WORD_PROCESSING_ML_PACKAGE_KEY)
	public void setWordprocessingMLPackage(WordprocessingMLPackage wpmlPackage);

	/**
	 * Return the list of shapes contained in this container
	 * 
	 * @return
	 */
	@Getter(value = PARAGRAPHS_KEY, cardinality = Cardinality.LIST, inverse = DocXParagraph.DOCX_DOCUMENT_KEY)
	@XMLElement(primary = true)
	@CloningStrategy(StrategyType.CLONE)
	@Embedded
	public List<DocXParagraph> getDocXParagraphs();

	@Setter(PARAGRAPHS_KEY)
	public void setDocXParagraphs(List<DocXParagraph> someParagraphs);

	@Adder(PARAGRAPHS_KEY)
	@PastingPoint
	public void addToDocXParagraphs(DocXParagraph aParagraph);

	@Remover(PARAGRAPHS_KEY)
	public void removeFromDocXParagraphs(DocXParagraph aParagraph);

	/**
	 * This is the starting point for updating {@link DocXDocument} with the document provided from docx4j library<br>
	 * Take care that the supplied wpmlPackage is the object we should update with, but that {@link #getWordprocessingMLPackage()} is unsafe
	 * in this context, because return former value
	 */
	public void updateFromWordprocessingMLPackage(WordprocessingMLPackage wpmlPackage, DocXFactory factory);

	public static abstract class DocXDocumentImpl extends DocXObjectImpl implements DocXDocument {

		private List<DocXParagraph> paragraphs;

		@Override
		public DocXDocument getDocXDocument() {
			return this;
		}

		@Override
		public void setWordprocessingMLPackage(WordprocessingMLPackage wpmlPackage) {
			if ((wpmlPackage == null && getWordprocessingMLPackage() != null)
					|| (wpmlPackage != null && !wpmlPackage.equals(getWordprocessingMLPackage()))) {
				updateFromWordprocessingMLPackage(wpmlPackage, ((DocXDocumentResource) getResource()).getFactory());
				performSuperSetter(WORD_PROCESSING_ML_PACKAGE_KEY, wpmlPackage);
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
					System.out.println("paraId=" + ((P) o).getParaId());
					addToDocXParagraphs(paragraph);
				}
			}

			System.out.println("Je lis ca:");
			System.out.println(printContents(wpmlPackage.getMainDocumentPart(), 0));
		}

		private static String printContents(Object obj, int indent) {
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
