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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.docx4j.model.styles.Node;
import org.docx4j.model.styles.StyleTree;
import org.docx4j.model.styles.StyleTree.AugmentedStyle;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.Style;
import org.docx4j.wml.Tbl;
import org.openflexo.foundation.doc.FlexoDocument;
import org.openflexo.foundation.doc.FlexoDocumentElement;
import org.openflexo.foundation.doc.FlexoDocumentFragment.FragmentConsistencyException;
import org.openflexo.foundation.doc.FlexoParagraph;
import org.openflexo.foundation.doc.FlexoStyle;
import org.openflexo.foundation.doc.FlexoTable;
import org.openflexo.foundation.resource.FlexoResource;
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

	@Override
	public String debugContents();

	@Override
	public String debugStructuredContents();

	public DocXParagraph getParagraph(P p);

	public DocXTable getTable(Tbl tbl);

	@Override
	public DocXFactory getFactory();

	@Override
	public DocXStyle activateStyle(String styleId);

	// TODO: we cannot do that: issue with PAMELA, please fix this (see issue PAMELA-7)
	// @Override
	// public DocXStyle getStyleByIdentifier(String styleId);

	@Override
	public DocXFragment getFragment(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> startElement,
			FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> endElement) throws FragmentConsistencyException;

	public static abstract class DocXDocumentImpl extends FlexoDocumentImpl<DocXDocument, DocXTechnologyAdapter> implements DocXDocument {

		private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger.getLogger(DocXDocumentImpl.class
				.getPackage().getName());

		private final Map<Style, DocXStyle> styles = new HashMap<Style, DocXStyle>();

		private final Map<P, DocXParagraph> paragraphs = new HashMap<P, DocXParagraph>();
		private final Map<Tbl, DocXTable> tables = new HashMap<Tbl, DocXTable>();
		private final Map<String, DocXElement> elementsForIdentifier = new HashMap<String, DocXElement>();

		@Override
		public DocXDocument getFlexoDocument() {
			return this;
		}

		@Override
		public void setWordprocessingMLPackage(WordprocessingMLPackage wpmlPackage) {

			if ((wpmlPackage == null && getWordprocessingMLPackage() != null)
					|| (wpmlPackage != null && !wpmlPackage.equals(getWordprocessingMLPackage()))) {
				if (wpmlPackage != null && getResource() != null) {
					updateFromWordprocessingMLPackage(wpmlPackage, getResource().getFactory());
				}
			}
		}

		/**
		 * This is the starting point for updating {@link DocXDocument} with the document provided from docx4j library<br>
		 * Take care that the supplied wpmlPackage is the object we should update with, but that {@link #getWordprocessingMLPackage()} is
		 * unsafe in this context, because return former value
		 */
		@Override
		public void updateFromWordprocessingMLPackage(WordprocessingMLPackage wpmlPackage, DocXFactory factory) {

			// System.out.println("updateFromWordprocessingMLPackage with " + wpmlPackage);

			List<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>> elementsToRemove = new ArrayList<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>>(
					getElements());

			List<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>> oldRoots = new ArrayList<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>>(
					getRootElements());

			// This map stores old document hierarchy
			Map<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>, List<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>>> oldChildren = new HashMap<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>, List<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>>>();
			for (FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> e : getElements()) {
				oldChildren.put(e, new ArrayList<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>>(e.getChildrenElements()));
				e.invalidateChildrenElements();
			}

			// We don't want to changes to be fired during update cycle.
			postponeRootElementChangedNotifications = true;

			int currentIndex = 0;

			MainDocumentPart mdp = wpmlPackage.getMainDocumentPart();
			for (Object o : mdp.getContent()) {
				if (o instanceof JAXBElement) {
					o = ((JAXBElement) o).getValue();
				}
				if (o instanceof P) {
					DocXParagraph paragraph = paragraphs.get(o);
					if (paragraph == null) {
						// System.out.println("# Create new paragraph for " + o);
						paragraph = factory.makeNewDocXParagraph((P) o);
						internallyInsertElementAtIndex(paragraph, currentIndex);
					} else {
						// OK paragraph was found
						if (getElements().indexOf(paragraph) != currentIndex) {
							// Paragraph was existing but is not at the right position
							internallyMoveElementToIndex(paragraph, currentIndex);
						} else {
							// System.out.println("# Found existing paragraph for " + o);
						}
						elementsToRemove.remove(paragraph);
					}
					currentIndex++;
				} else if (o instanceof Tbl) {
					System.out.println("Hop, une table");
					DocXTable table = tables.get(o);
					if (table == null) {
						System.out.println("# Create new table for " + o);
						table = factory.makeNewDocXTable((Tbl) o);
						internallyInsertElementAtIndex(table, currentIndex);
					} else {
						// OK table was found
						if (getElements().indexOf(table) != currentIndex) {
							// Paragraph was existing but is not at the right position
							internallyMoveElementToIndex(table, currentIndex);
						} else {
							// System.out.println("# Found existing table for " + o);
						}
						elementsToRemove.remove(table);
					}
					currentIndex++;
				}
			}

			for (FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> e : elementsToRemove) {
				// System.out.println("# Remove paragraph for " + e);
				internallyRemoveFromElements(e);
			}

			updateStylesFromWmlPackage(wpmlPackage, factory);

			// Then we call generic method, where notification will be thrown
			performSuperSetter(WORD_PROCESSING_ML_PACKAGE_KEY, wpmlPackage);

			// OK, since we have disactivated notification, we have to notify the whole document hierarchy
			postponeRootElementChangedNotifications = false;

			// First we notify root elements if they have changed
			// notifyRootElementsChanged();

			invalidateRootElements();
			if (!oldRoots.equals(getRootElements())) {
				// System.out.println("************** We notify root elements");
				// System.out.println("old: " + oldRoots);
				// System.out.println("new: " + getRootElements());
				// notifyRootElementsChanged();
			}

			// Then we iterate on all elements to see if some structural modifications need to be fired
			for (FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> e : getElements()) {
				List<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>> oldChild = oldChildren.get(e);
				if (oldChild == null || !oldChild.equals(e.getChildrenElements())) {
					if (e.getChildrenElements().size() > 0) {
						if (e instanceof DocXParagraph) {
							// System.out.println("********* We notify element " + e + " [" + ((DocXParagraph) e).getRawText() + "]");
							// System.out.println("old: " + oldChild);
							// System.out.println("new: " + e.getChildrenElements());
						}
						e.notifyChildrenElementsChanged();
					}
				}
			}

		}

		private void updateStylesFromWmlPackage(WordprocessingMLPackage wpmlPackage, DocXFactory factory) {

			StyleTree styleTree = wpmlPackage.getMainDocumentPart().getStyleTree(true);

			List<DocXStyle> stylesToRemove = new ArrayList<>(styles.values());

			registerStyle(styleTree.getParagraphStylesTree().getRootElement(), null, stylesToRemove, factory);

			StyleDefinitionsPart sdp = wpmlPackage.getMainDocumentPart().getStyleDefinitionsPart();
			try {
				for (Style style : sdp.getContents().getStyle()) {
					// System.out.println("# style " + style.getName().getVal() + " "
					// + (style.getUiPriority() != null ? style.getUiPriority().getVal() : ""));

					DocXStyle docXStyle = styles.get(style);
					if (docXStyle != null) {
						stylesToRemove.remove(docXStyle);
					} else {
						if (style.getBasedOn() != null && StringUtils.isNotEmpty(style.getBasedOn().getVal())) {
							// System.out.println("looking up: " + style.getBasedOn().getVal());
							DocXStyle parentStyle = (DocXStyle) getStyleByIdentifier(style.getBasedOn().getVal());
							// System.out.println("parentStyle: " + parentStyle);
							docXStyle = factory.makeNewDocXStyle(style, parentStyle);
						} else {
							docXStyle = factory.makeNewDocXStyle(style, null);
						}
						addToStyles(docXStyle);
					}
					docXStyle.updateFromStyle(style, factory);

				}
			} catch (Docx4JException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (DocXStyle styleToRemove : stylesToRemove) {
				removeFromStyles(styleToRemove);
			}

			// Temporaray solution to provide structuring styles
			for (int i = 1; i < 10; i++) {
				DocXStyle headingStyle = (DocXStyle) getStyleByName("heading " + i);
				if (headingStyle != null && !(getStructuringStyles().contains(headingStyle))) {
					addToStructuringStyles(headingStyle);
				}
			}

			/*for (DocXParagraph p : getElements(DocXParagraph.class)) {
				if (p.getP() != null && p.getP().getPPr() != null && p.getP().getPPr().getPStyle() != null) {
					String styleName = p.getP().getPPr().getPStyle().getVal();
					FlexoStyle<DocXDocument, DocXTechnologyAdapter> paragraphStyle = getStyleByIdentifier(styleName);
					if (paragraphStyle != null) {
						p.setStyle(paragraphStyle);
					}
				}
			}*/

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
			return DocXUtils.printContents(getWordprocessingMLPackage().getMainDocumentPart(), 0);
		}

		@Override
		public String debugStructuredContents() {
			StringBuffer result = new StringBuffer();
			for (FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> e : getRootElements()) {
				result.append(DocXUtils.debugStructuredContents(e, 1));
			}
			return result.toString();
		}

		@Override
		public DocXFactory getFactory() {
			return (DocXFactory) super.getFactory();
		}

		private DocXDocumentResource resource;

		@Override
		public DocXDocumentResource getResource() {
			return resource;
		}

		@Override
		public void setResource(FlexoResource<DocXDocument> resource) {
			if ((resource == null && this.resource != null) || (resource != null && !resource.equals(this.resource))) {
				FlexoResource<DocXDocument> oldValue = this.resource;
				this.resource = (DocXDocumentResource) resource;
				getPropertyChangeSupport().firePropertyChange("resource", oldValue, resource);
			}
		}

		/**
		 * Insert element to this {@link FlexoDocument} at supplied index (public API).<br>
		 * Element will be inserted to underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		@Override
		public void insertElementAtIndex(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> anElement, int index) {
			ContentAccessor parent = getWordprocessingMLPackage().getMainDocumentPart();
			((DocXElement) anElement).appendToWordprocessingMLPackage(parent, index);
			internallyInsertElementAtIndex(anElement, index);
		}

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of element inserting (calling
		 * this assume that added element is already present in underlying {@link WordprocessingMLPackage})
		 * 
		 * @param addedElement
		 */
		private void internallyInsertElementAtIndex(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> addedElement, int index) {
			performSuperAdder(ELEMENTS_KEY, addedElement, index);
			internallyHandleElementAdding(addedElement);
		}

		/**
		 * Internally used to handle element adding in wrapping model
		 * 
		 * @param anElement
		 */
		private void internallyHandleElementAdding(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> anElement) {
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
			if (anElement.getIdentifier() != null) {
				// System.out.println("Register " + anElement + " for " + anElement.getIdentifier());
				elementsForIdentifier.put(anElement.getIdentifier(), (DocXElement) anElement);
			} else {
				logger.warning("internallyHandleElementAdding() called for element with null identifier: " + anElement);
			}
			invalidateRootElements();
			notifyRootElementsChanged();
		}

		/**
		 * Moved element to this {@link FlexoDocument} at supplied index (public API).<br>
		 * Element will be moved inside underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		@Override
		public void moveElementToIndex(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> anElement, int index) {
			// TODO: implement moving in WordProcessingMLPackage
			internallyMoveElementToIndex(anElement, index);
		}

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of element moving (calling this
		 * assume that moved element has already been moved in underlying {@link WordprocessingMLPackage})
		 * 
		 * @param addedElement
		 */
		private void internallyMoveElementToIndex(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> anElement, int index) {
			List<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>> elements = getElements();
			elements.remove(anElement);
			elements.add(index, anElement);
			invalidateRootElements();
			notifyRootElementsChanged();
		}

		/**
		 * Add element to this {@link FlexoDocument} (public API).<br>
		 * Element will be added to underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		@Override
		public void addToElements(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> anElement) {
			if (isCreatedByCloning()) {
				internallyAddToElements(anElement);
				return;
			}
			if (anElement instanceof DocXParagraph) {
				P toAdd = ((DocXParagraph) anElement).getP();
				getWordprocessingMLPackage().getMainDocumentPart().getContent().add(toAdd);
			} else if (anElement instanceof DocXTable) {
				Tbl toAdd = ((DocXTable) anElement).getTbl();
				getWordprocessingMLPackage().getMainDocumentPart().getContent().add(toAdd);
			}
			internallyAddToElements(anElement);
		}

		/**
		 * Internally used to update {@link FlexoDocument} object according to wrapped model in the context of element adding (calling this
		 * assume that added element is already present in underlying {@link WordprocessingMLPackage})
		 * 
		 * @param addedElement
		 */
		private void internallyAddToElements(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> addedElement) {
			performSuperAdder(ELEMENTS_KEY, addedElement);
			internallyHandleElementAdding(addedElement);
		}

		/**
		 * Remove element from this {@link FlexoDocument} (public API).<br>
		 * Element will be removed to underlying {@link WordprocessingMLPackage} and {@link FlexoDocument} will be updated accordingly
		 */
		@Override
		public void removeFromElements(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> anElement) {
			// Removing in WordProcessingMLPackage
			if (anElement instanceof DocXParagraph) {
				P toRemove = ((DocXParagraph) anElement).getP();
				if (getWordprocessingMLPackage().getMainDocumentPart().getContent().contains(toRemove)) {
					getWordprocessingMLPackage().getMainDocumentPart().getContent().remove(toRemove);
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
		private void internallyRemoveFromElements(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> removedElement) {
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
			} else {
				logger.warning("removeFromElements() called for element with null identifier: " + removedElement);
			}
			performSuperRemover(ELEMENTS_KEY, removedElement);
			invalidateRootElements();
			notifyRootElementsChanged();
		}

		@Override
		public DocXElement getElementWithIdentifier(String identifier) {
			DocXElement returned = elementsForIdentifier.get(identifier);
			if (returned == null) {
				for (DocXTable table : tables.values()) {
					returned = (DocXElement) table.getElementWithIdentifier(identifier);
					if (returned != null) {
						return returned;
					}
				}
			}
			return returned;
		}

		@Override
		public List<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>> getElementsWithBaseIdentifier(String baseIdentifier) {
			List<FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter>> returned = new ArrayList<>();
			for (FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> e : getElements()) {
				if (e.getBaseIdentifier() != null && e.getBaseIdentifier().equals(baseIdentifier)) {
					returned.add(e);
				}
			}
			return returned;
		}

		protected void reindexElement(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> anElement, String oldIdentifier) {
			elementsForIdentifier.remove(oldIdentifier);
			elementsForIdentifier.put(anElement.getIdentifier(), (DocXElement) anElement);
		}

		@Override
		public DocXParagraph getParagraph(P p) {
			return paragraphs.get(p);
		}

		@Override
		public DocXTable getTable(Tbl tbl) {
			return tables.get(tbl);
		}

		@Override
		public DocXFragment getFragment(FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> startElement,
				FlexoDocumentElement<DocXDocument, DocXTechnologyAdapter> endElement) throws FragmentConsistencyException {
			return (DocXFragment) super.getFragment(startElement, endElement);
		}

		@Override
		public Collection<String> getKnownStyleIds() {

			return StyleDefinitionsPart.getKnownStyles().keySet();
		}

		@Override
		public DocXStyle activateStyle(String styleId) {
			System.out.println("On active: " + styleId);
			DocXStyle returned = (DocXStyle) getStyleByIdentifier(styleId);
			System.out.println("A priori je l'ai pas");
			if (returned == null) {
				if (getWordprocessingMLPackage().getMainDocumentPart().getPropertyResolver().activateStyle(styleId)) {
					System.out.println("Je viens de le trouver dans les known");
					updateStylesFromWmlPackage(getWordprocessingMLPackage(), getFactory());
					returned = (DocXStyle) getStyleByIdentifier(styleId);
					return returned;
				} else {
					logger.warning("Not found style: " + styleId);
					return null;
				}
			}
			return returned;
		}

		@Override
		public DocXParagraph addStyledParagraphOfText(FlexoStyle<DocXDocument, DocXTechnologyAdapter> style, String text) {

			DocXParagraph returned = makeStyledParagraph(style, text);
			addToElements(returned);
			return returned;
		}

		@Override
		public FlexoParagraph<DocXDocument, DocXTechnologyAdapter> insertStyledParagraphOfTextAtIndex(
				FlexoStyle<DocXDocument, DocXTechnologyAdapter> style, String text, int index) {
			DocXParagraph returned = makeStyledParagraph(style, text);
			insertElementAtIndex(returned, index);
			return returned;
		}

		private DocXParagraph makeStyledParagraph(FlexoStyle<DocXDocument, DocXTechnologyAdapter> style, String text) {
			P p = getWordprocessingMLPackage().getMainDocumentPart().createParagraphOfText(text);
			DocXParagraph returned = getFactory().makeNewDocXParagraph(p);
			returned.setStyle(style);
			return returned;
		}

		@Override
		public DocXTable addTable(int rows, int cols) {
			DocXTable returned = makeTable(rows, cols);
			addToElements(returned);
			return returned;
		}

		@Override
		public FlexoTable<DocXDocument, DocXTechnologyAdapter> insertTableAtIndex(int rows, int cols, int index) {
			DocXTable returned = makeTable(rows, cols);
			insertElementAtIndex(returned, index);
			return returned;
		}

		private DocXTable makeTable(int rows, int cols) {
			Tbl tbl = TblFactory.createTable(rows, cols, 100);
			DocXTable returned = getFactory().makeNewDocXTable(tbl);
			return returned;
		}
	}

}
