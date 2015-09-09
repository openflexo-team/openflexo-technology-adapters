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
import java.util.StringTokenizer;

import javax.xml.bind.JAXBElement;

import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.P;
import org.docx4j.wml.Tc;
import org.openflexo.foundation.doc.FlexoDocument;
import org.openflexo.foundation.doc.FlexoParagraph;
import org.openflexo.foundation.doc.FlexoTableCell;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

/**
 * Implementation of {@link FlexoTableCell} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXTableCell.DocXTableCellImpl.class)
@XMLElement
public interface DocXTableCell extends FlexoTableCell<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = Tc.class)
	public static final String TC_KEY = "tc";

	@Getter(value = TC_KEY, ignoreType = true)
	@CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneTc()")
	public Tc getTc();

	@Setter(TC_KEY)
	public void setTc(Tc tr);

	public Tc cloneTc();

	/**
	 * This is the starting point for updating {@link DocXTableCell} with the Tc provided from docx4j library<br>
	 * Take care that the supplied tc is the object we should update with, but that {@link #getTc()} is unsafe in this context, because
	 * return former value
	 */
	public void updateFromTc(Tc tc, DocXFactory factory);

	public static abstract class DocXTableCellImpl extends FlexoTableCellImpl<DocXDocument, DocXTechnologyAdapter>implements DocXTableCell {

		private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
				.getLogger(DocXTableCellImpl.class.getPackage().getName());

		private final Map<P, DocXParagraph> paragraphs = new HashMap<P, DocXParagraph>();

		public DocXTableCellImpl() {
			super();
		}

		@Override
		public void setTc(Tc tc) {
			if ((tc == null && getTc() != null) || (tc != null && !tc.equals(getTc()))) {
				if (tc != null) {
					updateFromTc(tc, (getResourceData() != null && getResourceData().getResource() != null
							? ((DocXDocumentResource) getResourceData().getResource()).getFactory() : null));
				}
			}
		}

		/**
		 * This is the starting point for updating {@link DocXTableCell} with the Tc provided from docx4j library<br>
		 * Take care that the supplied p is the object we should update with, but that {@link #getTc()} is unsafe in this context, because
		 * return former value
		 */
		@Override
		public void updateFromTc(Tc tc, DocXFactory factory) {

			List<FlexoParagraph<DocXDocument, DocXTechnologyAdapter>> paragraphsToRemove = new ArrayList<FlexoParagraph<DocXDocument, DocXTechnologyAdapter>>(
					getParagraphs());

			int currentIndex = 0;

			for (Object o : tc.getContent()) {
				if (o instanceof JAXBElement) {
					o = ((JAXBElement) o).getValue();
				}
				if (o instanceof P) {
					DocXParagraph paragraph = paragraphs.get(o);
					if (paragraph == null) {
						// System.out.println("# Create new paragraph for " + o);
						paragraph = factory.makeNewDocXParagraph((P) o);
						internallyInsertParagraphAtIndex(paragraph, currentIndex);
					}
					else {
						// OK paragraph was found
						if (getParagraphs().indexOf(paragraph) != currentIndex) {
							// Paragraph was existing but is not at the right position
							internallyMoveParagraphToIndex(paragraph, currentIndex);
						}
						else {
							// System.out.println("# Found existing paragraph for " + o);
						}
						paragraphsToRemove.remove(paragraph);
					}
					currentIndex++;
				}
			}

			for (FlexoParagraph<DocXDocument, DocXTechnologyAdapter> p : paragraphsToRemove) {
				internallyRemoveFromParagraphs(p);
			}

			performSuperSetter(TC_KEY, tc);

		}

		/**
		 * Insert paragraph to this {@link FlexoDocument} at supplied index (public API).<br>
		 * Paragraph will be inserted to underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		@Override
		public void insertParagraphAtIndex(FlexoParagraph<DocXDocument, DocXTechnologyAdapter> aParagraph, int index) {

			Tc tc = getTc();
			if (aParagraph instanceof DocXParagraph) {
				P p = ((DocXParagraph) aParagraph).getP();
				tc.getContent().add(index, p);
				internallyInsertParagraphAtIndex(aParagraph, index);
			}
			else {
				logger.warning("Unexpected paragraph: " + aParagraph);
			}
		}

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of paragraph inserting (calling
		 * this assume that added paragraph is already present in underlying {@link WordprocessingMLPackage})
		 * 
		 * @param addedParagraph
		 */
		private void internallyInsertParagraphAtIndex(FlexoParagraph<DocXDocument, DocXTechnologyAdapter> addedParagraph, int index) {
			performSuperAdder(PARAGRAPHS_KEY, addedParagraph, index);
			internallyHandleParagraphAdding(addedParagraph);
		}

		/**
		 * Internally used to handle paragraph adding in wrapping model
		 * 
		 * @param anParagraph
		 */
		private void internallyHandleParagraphAdding(FlexoParagraph<DocXDocument, DocXTechnologyAdapter> anParagraph) {
			if (anParagraph instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) anParagraph;
				if (paragraph.getP() != null) {
					paragraphs.put(paragraph.getP(), paragraph);
				}
			}
		}

		/**
		 * Moved paragraph to this {@link FlexoDocument} at supplied index (public API).<br>
		 * Paragraph will be moved inside underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		@Override
		public void moveParagraphToIndex(FlexoParagraph<DocXDocument, DocXTechnologyAdapter> anParagraph, int index) {
			// TODO: implement moving in Tc
			internallyMoveParagraphToIndex(anParagraph, index);
		}

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of paragraph moving (calling
		 * this assume that moved paragraph has already been moved in underlying {@link WordprocessingMLPackage})
		 * 
		 * @param addedParagraph
		 */
		private void internallyMoveParagraphToIndex(FlexoParagraph<DocXDocument, DocXTechnologyAdapter> anParagraph, int index) {
			List<FlexoParagraph<DocXDocument, DocXTechnologyAdapter>> paragraphs = getParagraphs();
			paragraphs.remove(anParagraph);
			paragraphs.add(index, anParagraph);
		}

		/**
		 * Add paragraph to this {@link FlexoDocument} (public API).<br>
		 * Paragraph will be added to underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		@Override
		public void addToParagraphs(FlexoParagraph<DocXDocument, DocXTechnologyAdapter> anParagraph) {
			if (isCreatedByCloning()) {
				internallyAddToParagraphs(anParagraph);
				return;
			}
			if (anParagraph instanceof DocXParagraph) {
				P toAdd = ((DocXParagraph) anParagraph).getP();
				getTc().getContent().add(toAdd);
			}
			internallyAddToParagraphs(anParagraph);
		}

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of paragraph adding (calling
		 * this assume that added paragraph is already present in underlying {@link WordprocessingMLPackage})
		 * 
		 * @param addedParagraph
		 */
		private void internallyAddToParagraphs(FlexoParagraph<DocXDocument, DocXTechnologyAdapter> addedParagraph) {
			performSuperAdder(PARAGRAPHS_KEY, addedParagraph);
			internallyHandleParagraphAdding(addedParagraph);
		}

		/**
		 * Remove paragraph from this {@link FlexoDocument} (public API).<br>
		 * Paragraph will be removed to underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		@Override
		public void removeFromParagraphs(FlexoParagraph<DocXDocument, DocXTechnologyAdapter> anParagraph) {
			if (anParagraph instanceof DocXParagraph) {
				P toRemove = ((DocXParagraph) anParagraph).getP();
				if (!DocXUtils.removeFromList(toRemove, getTc().getContent())) {
					logger.warning("P item not present in Tc. Please investigate...");
				}
			}

			// Then internal
			internallyRemoveFromParagraphs(anParagraph);
		}

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of paragraph removing (calling
		 * this assume that removed paragraph has already been removed from underlying {@link WordprocessingMLPackage})
		 * 
		 * @param removedParagraph
		 */
		private void internallyRemoveFromParagraphs(FlexoParagraph<DocXDocument, DocXTechnologyAdapter> removedParagraph) {
			if (removedParagraph instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) removedParagraph;
				if (paragraph.getP() != null) {
					paragraphs.remove(paragraph.getP());
				}
			}
			performSuperRemover(PARAGRAPHS_KEY, removedParagraph);
		}

		@Override
		public List<FlexoParagraph<DocXDocument, DocXTechnologyAdapter>> getElements() {
			return getParagraphs();
		}

		@Override
		public DocXParagraph getElementWithIdentifier(String identifier) {
			return getParagraphWithIdentifier(identifier);
		}

		@Override
		public DocXParagraph getParagraphWithIdentifier(String identifier) {
			for (DocXParagraph p : paragraphs.values()) {
				if (p.getIdentifier().equals(identifier)) {
					return p;
				}
			}
			return null;
		}

		@Override
		public DocXDocument getFlexoDocument() {
			if (getRow() != null) {
				return getRow().getFlexoDocument();
			}
			return null;
		}

		@Override
		public Tc cloneTc() {

			if (getTc() == null) {
				return null;
			}
			Tc copiedTc = XmlUtils.deepCopy(getTc());

			return copiedTc;
		}

		@Override
		public String getIdentifier() {
			if (getParagraphs().size() > 0) {
				return "Cell" + getParagraphs().get(0).getIdentifier();
			}
			return getRow().getIdentifier() + "Cell" + getIndex();
		}

		@Override
		public String toString() {
			return "DocXTableCell\n" + (getTc() != null ? DocXUtils.printContents(getTc(), 2) : "null");
		}

		/**
		 * Return a string representation (plain text) of contents of the cell
		 * 
		 * @return
		 */
		@Override
		public String getRawText() {
			StringBuffer sb = new StringBuffer();
			for (DocXParagraph paragraph : paragraphs.values()) {
				sb.append(paragraph.getRawText());
			}
			return sb.toString();
		}

		@Override
		public void setRawText(String someText) {

			List<String> lines = new ArrayList<String>();

			if (someText == null) {
				someText = "null";
			}

			StringTokenizer st = new StringTokenizer(someText, "\n");
			while (st.hasMoreElements()) {
				lines.add(st.nextToken());
			}

			if (getParagraphs().size() == 0) {
				for (int i = 0; i < lines.size(); i++) {
					// Add paragraph
					DocXParagraph paragraph = getFlexoDocument().getFactory().makeNewDocXParagraph(lines.get(i));
					addToParagraphs(paragraph);
				}

				return;
			}

			while (getParagraphs().size() > lines.size()) {
				// Remove extra paragraphs
				removeFromParagraphs(getParagraphs().get(getParagraphs().size() - 1));
			}

			for (int i = 0; i < lines.size(); i++) {
				if (i < getParagraphs().size()) {
					getParagraphs().get(i).setRawText(lines.get(i));
				}
				else {
					// Add paragraph
					DocXParagraph paragraph = getFlexoDocument().getFactory().makeNewDocXParagraph(lines.get(i));
					addToParagraphs(paragraph);
				}
			}

		}

	}

}
