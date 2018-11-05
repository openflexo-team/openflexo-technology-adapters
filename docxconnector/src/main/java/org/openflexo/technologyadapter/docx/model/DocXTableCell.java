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
import org.docx4j.wml.SdtBlock;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.Tc;
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.doc.FlexoDocTableCell;
import org.openflexo.foundation.doc.FlexoDocument;
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
 * Implementation of {@link FlexoDocTableCell} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXTableCell.DocXTableCellImpl.class)
@XMLElement
public interface DocXTableCell extends FlexoDocTableCell<DocXDocument, DocXTechnologyAdapter> {

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

	/**
	 * Search and return in all rows the {@link DocXParagraph} matching supplied {@link P}
	 * 
	 * @param p
	 * @return
	 */
	public DocXParagraph getParagraph(P p);

	public static abstract class DocXTableCellImpl extends FlexoTableCellImpl<DocXDocument, DocXTechnologyAdapter>
			implements DocXTableCell {

		private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
				.getLogger(DocXTableCellImpl.class.getPackage().getName());

		private final Map<P, DocXParagraph> paragraphs = new HashMap<>();
		private final Map<Tbl, DocXTable> tables = new HashMap<>();
		private final Map<SdtBlock, DocXSdtBlock> sdtBlocks = new HashMap<>();
		private final Map<Object, DocXUnmappedElement<?>> unmappedElements = new HashMap<>();
		private final Map<String, DocXElement<?>> elementsForIdentifier = new HashMap<>();

		@Override
		public void setTc(Tc tc) {
			if ((tc == null && getTc() != null) || (tc != null && !tc.equals(getTc()))) {
				if (tc != null) {
					updateFromTc(tc,
							(getResourceData() != null && getResourceData().getResource() != null
									? ((DocXDocumentResource) getResourceData().getResource()).getFactory()
									: null));
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

			List<FlexoDocElement<DocXDocument, DocXTechnologyAdapter>> elementsToRemove = new ArrayList<>(getElements());

			// This map stores old document hierarchy
			Map<FlexoDocElement<DocXDocument, DocXTechnologyAdapter>, List<FlexoDocElement<DocXDocument, DocXTechnologyAdapter>>> oldChildren = new HashMap<>();
			for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> e : getElements()) {
				oldChildren.put(e, new ArrayList<>(e.getChildrenElements()));
				e.invalidateChildrenElements();
			}

			int currentIndex = 0;

			for (Object o : tc.getContent()) {
				if (o instanceof JAXBElement) {
					o = ((JAXBElement<?>) o).getValue();
				}
				if (o instanceof P) {
					DocXParagraph paragraph = paragraphs.get(o);
					if (paragraph == null) {
						// System.out.println("# Create new paragraph for " + o);
						paragraph = factory.makeNewDocXParagraph((P) o);
						internallyInsertElementAtIndex(paragraph, currentIndex);
					}
					else {
						// OK paragraph was found
						if (getElements().indexOf(paragraph) != currentIndex) {
							// Paragraph was existing but is not at the right position
							internallyMoveElementToIndex(paragraph, currentIndex);
						}
						else {
							// System.out.println("# Found existing paragraph for " + o);
						}
						elementsToRemove.remove(paragraph);
					}
				}
				else if (o instanceof Tbl) {
					DocXTable table = tables.get(o);
					if (table == null) {
						// System.out.println("# Create new table for " + o);
						table = factory.makeNewDocXTable((Tbl) o);
						internallyInsertElementAtIndex(table, currentIndex);
					}
					else {
						// OK table was found
						if (getElements().indexOf(table) != currentIndex) {
							// Paragraph was existing but is not at the right position
							internallyMoveElementToIndex(table, currentIndex);
						}
						else {
							// System.out.println("# Found existing table for " + o);
						}
						elementsToRemove.remove(table);
					}
				}
				else if (o instanceof SdtBlock) {
					DocXSdtBlock sdtBlockElement = sdtBlocks.get(o);
					if (sdtBlockElement == null) {
						System.out.println("# Create new SdtBlock for " + o);
						sdtBlockElement = factory.makeNewSdtBlock((SdtBlock) o);
						internallyInsertElementAtIndex(sdtBlockElement, currentIndex);
					}
					else {
						// OK sdtBlock was found
						if (getElements().indexOf(sdtBlockElement) != currentIndex) {
							// SdtBlock was existing but is not at the right position
							internallyMoveElementToIndex(sdtBlockElement, currentIndex);
						}
						else {
							// System.out.println("# Found existing table for " + o);
						}
						elementsToRemove.remove(sdtBlockElement);
					}
				}
				else {
					DocXUnmappedElement<?> unmappedElement = unmappedElements.get(o);
					if (unmappedElement == null) {
						System.out.println("# Create new DocXUnmappedElement for " + o);
						unmappedElement = factory.makeNewUnmappedElement(o);
						internallyInsertElementAtIndex(unmappedElement, currentIndex);
					}
					else {
						// OK element was found
						if (getElements().indexOf(unmappedElement) != currentIndex) {
							// Paragraph was existing but is not at the right position
							internallyMoveElementToIndex(unmappedElement, currentIndex);
						}
						else {
							// System.out.println("# Found existing table for " + o);
						}
						elementsToRemove.remove(unmappedElement);
					}
				}
				currentIndex++;
			}

			for (FlexoDocElement<DocXDocument, DocXTechnologyAdapter> e : elementsToRemove) {
				// System.out.println("# Remove paragraph for " + e);
				internallyRemoveFromElements(e);
			}

			performSuperSetter(TC_KEY, tc);

		}

		/**
		 * Insert paragraph to this {@link FlexoDocument} at supplied index (public API).<br>
		 * Paragraph will be inserted to underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		/*@Override
		public void insertParagraphAtIndex(FlexoDocParagraph<DocXDocument, DocXTechnologyAdapter> aParagraph, int index) {
		
			Tc tc = getTc();
			if (aParagraph instanceof DocXParagraph) {
				P p = ((DocXParagraph) aParagraph).getP();
				tc.getContent().add(index, p);
				internallyInsertParagraphAtIndex(aParagraph, index);
			}
			else {
				logger.warning("Unexpected paragraph: " + aParagraph);
			}
		}*/

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of paragraph inserting (calling
		 * this assume that added paragraph is already present in underlying {@link WordprocessingMLPackage})
		 * 
		 * @param addedParagraph
		 */
		/*private void internallyInsertParagraphAtIndex(FlexoDocParagraph<DocXDocument, DocXTechnologyAdapter> addedParagraph, int index) {
			performSuperAdder(PARAGRAPHS_KEY, addedParagraph, index);
			internallyHandleParagraphAdding(addedParagraph);
		}*/

		/**
		 * Internally used to handle paragraph adding in wrapping model
		 * 
		 * @param anParagraph
		 */
		/*private void internallyHandleParagraphAdding(FlexoDocParagraph<DocXDocument, DocXTechnologyAdapter> anParagraph) {
			if (anParagraph instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) anParagraph;
				if (paragraph.getP() != null) {
					paragraphs.put(paragraph.getP(), paragraph);
				}
			}
		}*/

		/**
		 * Moved paragraph to this {@link FlexoDocument} at supplied index (public API).<br>
		 * Paragraph will be moved inside underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		/*@Override
		public void moveParagraphToIndex(FlexoDocParagraph<DocXDocument, DocXTechnologyAdapter> anParagraph, int index) {
			// TODO: implement moving in Tc
			internallyMoveParagraphToIndex(anParagraph, index);
		}*/

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of paragraph moving (calling
		 * this assume that moved paragraph has already been moved in underlying {@link WordprocessingMLPackage})
		 * 
		 * @param addedParagraph
		 */
		/*private void internallyMoveParagraphToIndex(FlexoDocParagraph<DocXDocument, DocXTechnologyAdapter> anParagraph, int index) {
			List<FlexoDocParagraph<DocXDocument, DocXTechnologyAdapter>> paragraphs = getParagraphs();
			paragraphs.remove(anParagraph);
			paragraphs.add(index, anParagraph);
		}*/

		/**
		 * Add paragraph to this {@link FlexoDocument} (public API).<br>
		 * Paragraph will be added to underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		/*@Override
		public void addToParagraphs(FlexoDocParagraph<DocXDocument, DocXTechnologyAdapter> anParagraph) {
			if (isCreatedByCloning()) {
				internallyAddToParagraphs(anParagraph);
				return;
			}
			if (anParagraph instanceof DocXParagraph) {
				P toAdd = ((DocXParagraph) anParagraph).getP();
				getTc().getContent().add(toAdd);
			}
			internallyAddToParagraphs(anParagraph);
		}*/

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of paragraph adding (calling
		 * this assume that added paragraph is already present in underlying {@link WordprocessingMLPackage})
		 * 
		 * @param addedParagraph
		 */
		/*private void internallyAddToParagraphs(FlexoDocParagraph<DocXDocument, DocXTechnologyAdapter> addedParagraph) {
			performSuperAdder(PARAGRAPHS_KEY, addedParagraph);
			internallyHandleParagraphAdding(addedParagraph);
		}*/

		/**
		 * Remove paragraph from this {@link FlexoDocument} (public API).<br>
		 * Paragraph will be removed to underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		/*@Override
		public void removeFromParagraphs(FlexoDocParagraph<DocXDocument, DocXTechnologyAdapter> anParagraph) {
			if (anParagraph instanceof DocXParagraph) {
				P toRemove = ((DocXParagraph) anParagraph).getP();
				if (!DocXUtils.removeFromList(toRemove, getTc().getContent())) {
					logger.warning("P item not present in Tc. Please investigate...");
				}
			}
		
			// Then internal
			internallyRemoveFromParagraphs(anParagraph);
		}*/

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of paragraph removing (calling
		 * this assume that removed paragraph has already been removed from underlying {@link WordprocessingMLPackage})
		 * 
		 * @param removedParagraph
		 */
		/*private void internallyRemoveFromParagraphs(FlexoDocParagraph<DocXDocument, DocXTechnologyAdapter> removedParagraph) {
			if (removedParagraph instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) removedParagraph;
				if (paragraph.getP() != null) {
					paragraphs.remove(paragraph.getP());
				}
			}
			performSuperRemover(PARAGRAPHS_KEY, removedParagraph);
		}*/

		/*@Override
		public List<FlexoDocElement<DocXDocument, DocXTechnologyAdapter>> getElements() {
			return (List) getParagraphs();
		}*/

		// TODO: big code duplication with DocXDocument
		// Please refactor this to avoid code duplication

		/**
		 * Insert element to this {@link FlexoDocument} at supplied index (public API).<br>
		 * Element will be inserted to underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		@Override
		public void insertElementAtIndex(FlexoDocElement<DocXDocument, DocXTechnologyAdapter> anElement, int index) {

			Tc tc = getTc();
			if (anElement instanceof DocXParagraph) {
				P p = ((DocXParagraph) anElement).getP();
				tc.getContent().add(index, p);
				internallyInsertElementAtIndex(anElement, index);
			}
			if (anElement instanceof DocXTable) {
				Tbl t = ((DocXTable) anElement).getTbl();
				tc.getContent().add(index, t);
				internallyInsertElementAtIndex(anElement, index);
			}
			else {
				logger.warning("Unexpected element: " + anElement);
			}

		}

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of element inserting (calling
		 * this assume that added element is already present in underlying {@link WordprocessingMLPackage})
		 * 
		 * @param addedElement
		 */
		private void internallyInsertElementAtIndex(FlexoDocElement<DocXDocument, DocXTechnologyAdapter> addedElement, int index) {
			performSuperAdder(ELEMENTS_KEY, addedElement, index);
			internallyHandleElementAdding(addedElement);
		}

		/**
		 * Internally used to handle element adding in wrapping model
		 * 
		 * @param anElement
		 */
		private void internallyHandleElementAdding(FlexoDocElement<DocXDocument, DocXTechnologyAdapter> anElement) {
			if (anElement instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) anElement;
				if (paragraph.getP() != null) {
					paragraphs.put(paragraph.getP(), paragraph);
				}
			}
			if (anElement instanceof DocXTable) {
				DocXTable table = (DocXTable) anElement;
				if (table.getTbl() != null) {
					tables.put(table.getTbl(), table);
				}
			}
			if (anElement instanceof DocXSdtBlock) {
				DocXSdtBlock docXSdtBlock = (DocXSdtBlock) anElement;
				if (docXSdtBlock.getSdtBlock() != null) {
					sdtBlocks.put(docXSdtBlock.getSdtBlock(), docXSdtBlock);
				}
			}
			if (anElement instanceof DocXUnmappedElement) {
				DocXUnmappedElement<?> unmappedElement = (DocXUnmappedElement<?>) anElement;
				if (unmappedElement.getDocXObject() != null) {
					unmappedElements.put(unmappedElement.getDocXObject(), unmappedElement);
				}
			}
			if (anElement.getIdentifier() != null) {
				// System.out.println("Register " + anElement + " for " + anElement.getIdentifier());
				elementsForIdentifier.put(anElement.getIdentifier(), (DocXElement<?>) anElement);
			}
			else {
				logger.warning("internallyHandleElementAdding() called for element with null identifier: " + anElement);
			}
		}

		/**
		 * Moved element to this {@link FlexoDocument} at supplied index (public API).<br>
		 * Element will be moved inside underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		@Override
		public void moveElementToIndex(FlexoDocElement<DocXDocument, DocXTechnologyAdapter> anElement, int index) {
			// TODO: implement moving in WordProcessingMLPackage
			internallyMoveElementToIndex(anElement, index);
		}

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of element moving (calling this
		 * assume that moved element has already been moved in underlying {@link WordprocessingMLPackage})
		 * 
		 * @param addedElement
		 */
		private void internallyMoveElementToIndex(FlexoDocElement<DocXDocument, DocXTechnologyAdapter> anElement, int index) {
			List<FlexoDocElement<DocXDocument, DocXTechnologyAdapter>> elements = getElements();
			elements.remove(anElement);
			elements.add(index, anElement);
		}

		/**
		 * Add element to this {@link FlexoDocument} (public API).<br>
		 * Element will be added to underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		@Override
		public void addToElements(FlexoDocElement<DocXDocument, DocXTechnologyAdapter> anElement) {
			if (isCreatedByCloning()) {
				internallyAddToElements(anElement);
				return;
			}
			Tc tc = getTc();
			if (anElement instanceof DocXParagraph) {
				P toAdd = ((DocXParagraph) anElement).getP();
				tc.getContent().add(toAdd);
			}
			else if (anElement instanceof DocXTable) {
				Tbl toAdd = ((DocXTable) anElement).getTbl();
				tc.getContent().add(toAdd);
			}
			internallyAddToElements(anElement);
		}

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of element adding (calling this
		 * assume that added element is already present in underlying {@link WordprocessingMLPackage})
		 * 
		 * @param addedElement
		 */
		private void internallyAddToElements(FlexoDocElement<DocXDocument, DocXTechnologyAdapter> addedElement) {
			performSuperAdder(ELEMENTS_KEY, addedElement);
			internallyHandleElementAdding(addedElement);
		}

		/**
		 * Remove element from this {@link FlexoDocument} (public API).<br>
		 * Element will be removed to underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		@Override
		public void removeFromElements(FlexoDocElement<DocXDocument, DocXTechnologyAdapter> anElement) {

			// Removing in Tc
			Tc tc = getTc();
			if (anElement instanceof DocXParagraph) {
				P toRemove = ((DocXParagraph) anElement).getP();
				if (tc.getContent().contains(toRemove)) {
					tc.getContent().remove(toRemove);
				}
			}
			else if (anElement instanceof DocXTable) {
				Tbl toRemove = ((DocXTable) anElement).getTbl();
				if (!DocXUtils.removeFromList(toRemove, tc.getContent())) {
					logger.warning("Tbl item not present in document root element list. Please investigate...");
				}
			}

			// Then internal
			internallyRemoveFromElements(anElement);
		}

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of element removing (calling
		 * this assume that removed element has already been removed from underlying {@link WordprocessingMLPackage})
		 * 
		 * @param removedElement
		 */
		private void internallyRemoveFromElements(FlexoDocElement<DocXDocument, DocXTechnologyAdapter> removedElement) {
			if (removedElement instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) removedElement;
				if (paragraph.getP() != null) {
					paragraphs.remove(paragraph.getP());
				}
			}
			if (removedElement instanceof DocXTable) {
				DocXTable table = (DocXTable) removedElement;
				if (table.getTbl() != null) {
					tables.remove(table.getTbl());
				}
			}
			if (removedElement.getIdentifier() != null) {
				elementsForIdentifier.remove(removedElement.getIdentifier());
			}
			else {
				logger.warning("removeFromElements() called for element with null identifier: " + removedElement);
			}
			performSuperRemover(ELEMENTS_KEY, removedElement);
		}

		@Override
		public DocXParagraph getElementWithIdentifier(String identifier) {
			return getParagraphWithIdentifier(identifier);
		}

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
			if (getElements().size() > 0) {
				return "Cell" + getElements().get(0).getIdentifier();
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

			List<String> lines = new ArrayList<>();

			if (someText == null) {
				someText = "null";
			}

			StringTokenizer st = new StringTokenizer(someText, "\n");
			while (st.hasMoreElements()) {
				lines.add(st.nextToken());
			}

			if (getElements().size() == 0) {
				for (int i = 0; i < lines.size(); i++) {
					// Add paragraph
					DocXParagraph paragraph = getFlexoDocument().getFactory().makeNewDocXParagraph(lines.get(i));
					addToElements(paragraph);
				}

				return;
			}

			while (getElements().size() > lines.size()) {
				// Remove extra paragraphs
				removeFromElements(getElements().get(getElements().size() - 1));
			}

			for (int i = 0; i < lines.size(); i++) {
				if (i < getElements().size()) {
					if (getElements().get(i) instanceof DocXParagraph) {
						((DocXParagraph) getElements().get(i)).setRawText(lines.get(i));
					}
				}
				else {
					// Add paragraph
					DocXParagraph paragraph = getFlexoDocument().getFactory().makeNewDocXParagraph(lines.get(i));
					addToElements(paragraph);
				}
			}

		}

		@Override
		public DocXParagraph getParagraph(P p) {
			return paragraphs.get(p);
		}

	}

}
