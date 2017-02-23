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

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.JAXBElement;

import org.docx4j.TextUtils;
import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.wml.CTBookmark;
import org.docx4j.wml.CTMarkupRange;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.PStyle;
import org.docx4j.wml.R;
import org.openflexo.foundation.doc.FlexoDocParagraph;
import org.openflexo.foundation.doc.FlexoDocRun;
import org.openflexo.foundation.doc.FlexoDocStyle;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.Import;
import org.openflexo.model.annotations.Imports;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument.DocXDocumentImpl;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

/**
 * Implementation of {@link FlexoDocParagraph} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXParagraph.DocXParagraphImpl.class)
@XMLElement
@Imports({ @Import(DocXTextRun.class), @Import(DocXDrawingRun.class) })
public interface DocXParagraph extends DocXElement<P>, FlexoDocParagraph<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = P.class)
	public static final String P_KEY = "p";
	@PropertyIdentifier(type = CTBookmark.class)
	public static final String BOOKMARK_KEY = "bookmark";

	@Getter(value = P_KEY, ignoreType = true)
	// We need to clone (reference) container first, in order to have container not null when executing setP()
	@CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneP()", cloneAfterProperty = CONTAINER_KEY)
	public P getP();

	@Setter(P_KEY)
	public void setP(P p);

	@Getter(value = BOOKMARK_KEY, ignoreType = true)
	// We need to clone (reference) container first, in order to have container not null when executing setP()
	@CloningStrategy(StrategyType.IGNORE)
	public CTBookmark getBookmark();

	@Setter(BOOKMARK_KEY)
	public void setBookmark(CTBookmark bookmark);

	public P cloneP();

	// TRICKY AREA
	// We override here the PAMELA definition of this property by declaring CloningStrategy as IGNORE
	// We do that because P is beeing cloned and the setting of new P value will cause creation of DocXTextRun
	// We definitely want to avoid double instanciation of DocXTextRun in a cloned paragraph !!!!
	@Override
	@CloningStrategy(StrategyType.IGNORE)
	public List<FlexoDocRun<DocXDocument, DocXTechnologyAdapter>> getRuns();

	/**
	 * This is the starting point for updating {@link DocXParagraph} with the paragraph provided from docx4j library<br>
	 * Take care that the supplied p is the object we should update with, but that {@link #getP()} is unsafe in this context, because return
	 * former value
	 */
	public void updateFromP(P p, DocXFactory factory);

	public DocXRun getRun(R r);

	public DocXFactory getFactory();

	public static abstract class DocXParagraphImpl extends FlexoDocParagraphImpl<DocXDocument, DocXTechnologyAdapter>
			implements DocXParagraph {

		private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger
				.getLogger(DocXParagraphImpl.class.getPackage().getName());

		private final Map<R, DocXRun> runs = new HashMap<>();

		// Factory used during initialization of DocXParagraph (either new or loaded document)
		protected DocXFactory _factory;

		public DocXParagraphImpl() {
			super();
		}

		@Override
		public P getDocXObject() {
			return getP();
		}

		@Override
		public void setP(P p) {

			// When called in cloning operation, container should NOT be null
			// That's why we use cloneAfterProperty feature
			// @CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneP()", cloneAfterProperty = CONTAINER_KEY)

			/*System.out.println("setP with " + p);
			System.out.println("getResourceData()=" + getResourceData());
			System.out.println("getContainer()=" + getContainer());
			System.out.println("getResourceData().getResource()=" + getResourceData().getResource());*/

			if ((p == null && getP() != null) || (p != null && !p.equals(getP()))) {
				if (p != null && getResourceData() != null && getResourceData().getResource() != null) {
					updateFromP(p, ((DocXDocumentResource) getResourceData().getResource()).getFactory());
				}
			}
		}

		/**
		 * This is the starting point for updating {@link DocXParagraph} with the paragraph provided from docx4j library<br>
		 * Take care that the supplied p is the object we should update with, but that {@link #getP()} is unsafe in this context, because
		 * return former value
		 */
		@Override
		public void updateFromP(P p, DocXFactory factory) {

			performSuperSetter(P_KEY, p);
			// Take care at the previous line, since there is a risk for the notification not to be triggered,
			// if value of P given by getP() returns the new value

			List<FlexoDocRun> runsToRemove = new ArrayList<>(getRuns());

			int currentIndex = 0;

			// System.out.println(DocXUtils.printContents(p, 0));
			for (Object o : p.getContent()) {
				if (o instanceof JAXBElement) {
					o = ((JAXBElement) o).getValue();
				}
				if (o instanceof R) {
					DocXRun run = runs.get(o);
					if (run == null) {
						// System.out.println("# Create new run for " + o);
						run = factory.makeNewDocXRun((R) o);
						// System.out.println("run=" + run);
						internallyInsertRunAtIndex(run, currentIndex);
					}
					else {
						// OK run was found
						if (getRuns().indexOf(run) != currentIndex) {
							// Paragraph was existing but is not at the right position
							internallyMoveRunToIndex(run, currentIndex);
							// System.out.println("# Move existing run to " + currentIndex + " for " + o);
						}
						else {
							// System.out.println("# Found existing run for " + o);
						}
						runsToRemove.remove(run);
					}
					// If run is null, do not increment currentIndex !!!
					if (run != null) {
						currentIndex++;
					}
				}
				else if (o instanceof CTBookmark) {
					CTBookmark bookmark = (CTBookmark) o;
					if (bookmark.getName() != null && bookmark.getName().startsWith(BOOKMARK_PREFIX)) {
						// System.out.println("Found CTBookmark : " + bookmark + " name=" + bookmark.getName() + " start="
						// + bookmark.getColFirst() + " end=" + bookmark.getColLast());
						setBookmark(bookmark);
						// System.out.println("identifier=" + getIdentifier());
						// System.out.println("strategy=" + getFactory().getIDStrategy());
					}
				}
			}

			for (FlexoDocRun<DocXDocument, DocXTechnologyAdapter> run : runsToRemove) {
				// System.out.println("# Remove run for " + e);
				internallyRemoveFromRuns(run);
			}

		}

		/*protected CTBookmark createCTBookmarkIdentifier(DocXFactory factory) {
			CTBookmark bookmark = new CTBookmark();
			bookmark.setName(BOOKMARK_PREFIX + factory.generateId());
			getP().getContent().add(0, bookmark);
			setBookmark(bookmark);
			setModified(true);
			return bookmark;
		}*/

		/**
		 * Generate a bookmark carring identifier of paragraph
		 * 
		 * @param p
		 * @param r
		 * @param name
		 * @param id
		 */
		protected CTBookmark createCTBookmarkIdentifier(DocXFactory docXFactory) {

			if (getP() == null) {
				return null;
			}

			ObjectFactory factory = Context.getWmlObjectFactory();
			BigInteger ID = BigInteger.valueOf(new Random().nextLong());

			// Add bookmark end first
			CTMarkupRange mr = factory.createCTMarkupRange();
			mr.setId(ID);
			JAXBElement<CTMarkupRange> bmEnd = factory.createBodyBookmarkEnd(mr);
			getP().getContent().add(getP().getContent().size(), bmEnd);

			// Next, bookmark start
			CTBookmark bm = factory.createCTBookmark();
			bm.setId(ID);
			bm.setName(BOOKMARK_PREFIX + docXFactory.generateId());
			JAXBElement<CTBookmark> bmStart = factory.createBodyBookmarkStart(bm);
			getP().getContent().add(0, bmStart);
			CTBookmark bookmark = bmStart.getValue();
			setBookmark(bookmark);
			setModified(true);

			docXFactory.someIdHaveBeenGeneratedAccordingToBookmarkManagementStrategy = true;

			return bookmark;
		}

		/**
		 * Surround the specified r in the specified p with a bookmark (with specified name and id)
		 * 
		 * @param p
		 * @param r
		 * @param name
		 * @param id
		 */
		public void bookmarkRun(R r, String name, int id) {

			// Find the index
			int index = getP().getContent().indexOf(r);

			if (index < 0) {
				System.out.println("P does not contain R!");
				return;
			}

			ObjectFactory factory = Context.getWmlObjectFactory();
			BigInteger ID = BigInteger.valueOf(id);

			// Add bookmark end first
			CTMarkupRange mr = factory.createCTMarkupRange();
			mr.setId(ID);
			JAXBElement<CTMarkupRange> bmEnd = factory.createBodyBookmarkEnd(mr);
			getP().getContent().add(index + 1, bmEnd); // from 2.7.0, use getContent()

			// Next, bookmark start
			CTBookmark bm = factory.createCTBookmark();
			bm.setId(ID);
			bm.setName(name);
			JAXBElement<CTBookmark> bmStart = factory.createBodyBookmarkStart(bm);
			getP().getContent().add(index, bmStart);

		}

		@Override
		public DocXFactory getFactory() {
			DocXFactory returned = null;
			if (getFlexoDocument() != null && getFlexoDocument().getFactory() != null) {
				returned = getFlexoDocument().getFactory();
			}
			if (returned == null) {
				return _factory;
			}
			return returned;
		}

		@Override
		public String getIdentifier() {
			if (getFactory() != null) {
				switch (getFactory().getIDStrategy()) {
					case ParaId:
						if (getP() != null) {
							return getP().getParaId();
						}
						return null;
					case Bookmark:
						if (getBookmark() == null) {
							createCTBookmarkIdentifier(getFactory());
						}
						if (getBookmark() != null) {
							return getBookmark().getName().substring(BOOKMARK_PREFIX.length());
						}
						return null;
					default:
						return null;
				}
			}
			else {
				logger.warning("Unreachable factory");
				return null;
			}
		}

		@Override
		public void setIdentifier(String identifier) {
			String oldIdentifier = getIdentifier();
			if (getFactory() != null) {
				switch (getFactory().getIDStrategy()) {
					case ParaId:
						if (getP() != null) {
							getP().setParaId(identifier);
						}
						break;
					case Bookmark:
						if (getBookmark() != null) {
							getBookmark().setName(BOOKMARK_PREFIX + identifier);
						}
						break;
				}
				// NPE Protection (when deleting elements)
				DocXDocumentImpl docxDoc = ((DocXDocumentImpl) getFlexoDocument());
				if (docxDoc != null) {
					docxDoc.reindexElement(this, oldIdentifier);
				}
			}
			else {
				logger.warning("Unreachable factory");
			}
		}

		@Override
		public String getRawText() {
			StringWriter sw = new StringWriter();
			try {
				TextUtils.extractText(getP(), sw);
			} catch (Exception e) {
				e.printStackTrace();
				return "<" + e.getClass().getSimpleName() + " message: " + e.getMessage() + ">";
			}
			return sw.toString();
		}

		@Override
		public void setRawText(String someText) {

			if (getRuns().size() == 0) {
				// Add run
				DocXTextRun run = getFlexoDocument().getFactory().makeTextRun(someText);
				addToRuns(run);
				return;
			}

			while (getRuns().size() > 1) {
				// Remove extra runs
				removeFromRuns(getRuns().get(getRuns().size() - 1));
			}

			if (getRuns().get(0) instanceof DocXTextRun) {
				((DocXTextRun) getRuns().get(0)).setText(someText);
			}

		}

		@Override
		public P cloneP() {

			if (getP() == null) {
				return null;
			}
			P copiedP = XmlUtils.deepCopy(getP());

			if (getFactory() != null) {
				// Translate ID
				String newId = getFactory().generateId();
				switch (getFactory().getIDStrategy()) {
					case ParaId:
						String oldId = getP().getParaId();
						copiedP.setParaId(newId);
						break;
					case Bookmark:
						for (Object o : copiedP.getContent()) {
							if (o instanceof JAXBElement) {
								o = ((JAXBElement) o).getValue();
							}
							if (o instanceof CTBookmark) {
								CTBookmark bookmark = (CTBookmark) o;
								if (bookmark.getName() != null && bookmark.getName().startsWith(BOOKMARK_PREFIX)) {
									String oldName = bookmark.getName();
									bookmark.setName(BOOKMARK_PREFIX + newId);
									BigInteger ID = BigInteger.valueOf(new Random().nextLong());
									bookmark.setId(ID);
									System.out.println("Translate CTBookmark ID for " + bookmark + " from name " + oldName + " to name="
											+ bookmark.getName());
									// setBookmark(bookmark);
								}
							}
						}
						break;
				}
			}

			// System.out.println("Paragraph [" + getRawTextPreview() + "] changed id from " + oldId + " to " + copiedP.getParaId());

			return copiedP;
		}

		@Override
		public void appendToWordprocessingMLPackage(ContentAccessor parent, int index) {

			/*System.out.println("appendToWordprocessingMLPackage for " + this);
				System.out.println("parent: " + parent);
				System.out.println("index: " + index);
				System.out.println("p=" + getP());*/

			parent.getContent().add(index, getP());
			getFlexoDocument().setIsModified();

		}

		/**
		 * Insert run to this {@link DocXParagraph} at supplied index (public API).<br>
		 * Element will be inserted to underlying {@link P} and {@link DocXParagraph} will be updated accordingly
		 */
		@Override
		public void insertRunAtIndex(FlexoDocRun<DocXDocument, DocXTechnologyAdapter> aRun, int index) {
			System.out.println("Add run " + aRun);
			P p = getP();
			if (aRun instanceof DocXRun) {
				R r = ((DocXRun) aRun).getR();
				p.getContent().add(index + 1, r);
				internallyInsertRunAtIndex(aRun, index);
			}
			else {
				logger.warning("Unexpected run: " + aRun);
			}
		}

		/**
		 * Internally used to update {@link DocXParagraph} object according to wrapped model in the context of run inserting (calling this
		 * assume that added run is already present in underlying {@link P})
		 * 
		 * @param addedElement
		 */
		private void internallyInsertRunAtIndex(FlexoDocRun<DocXDocument, DocXTechnologyAdapter> aRun, int index) {

			performSuperAdder(RUNS_KEY, aRun, index);
			internallyHandleRunAdding(aRun);
		}

		/**
		 * Internally handle run adding
		 * 
		 * @param aRun
		 */
		private void internallyHandleRunAdding(FlexoDocRun<DocXDocument, DocXTechnologyAdapter> aRun) {

			if (aRun instanceof DocXRun) {
				DocXRun run = (DocXRun) aRun;
				if (run.getR() != null) {
					runs.put(run.getR(), run);
				}
			}
		}

		/**
		 * Move run in this {@link DocXParagraph} at supplied index (public API).<br>
		 * Element will be moved inside underlying {@link P} and {@link DocXParagraph} will be updated accordingly
		 */
		@Override
		public void moveRunToIndex(FlexoDocRun<DocXDocument, DocXTechnologyAdapter> aRun, int index) {
			// TODO: do it in P !
			internallyMoveRunToIndex(aRun, index);
		}

		/**
		 * Internally used to update {@link DocXParagraph} object according to wrapped model in the context of run moving (calling this
		 * assume that moved run has already been moved in underlying {@link P})
		 * 
		 * @param addedElement
		 */
		private void internallyMoveRunToIndex(FlexoDocRun<DocXDocument, DocXTechnologyAdapter> aRun, int index) {
			List<FlexoDocRun<DocXDocument, DocXTechnologyAdapter>> runs = getRuns();
			runs.remove(aRun);
			runs.add(index, aRun);
		}

		/**
		 * Add run to this {@link DocXParagraph} (public API).<br>
		 * Element will be added to underlying {@link P} and {@link DocXParagraph} will be updated accordingly
		 */
		@Override
		public void addToRuns(FlexoDocRun<DocXDocument, DocXTechnologyAdapter> aRun) {

			/*if (isCreatedByCloning()) {
					// internallyAddToRuns(aRun);
					return;
				}*/

			P p = getP();
			if (aRun instanceof DocXRun) {
				R r = ((DocXRun) aRun).getR();
				p.getContent().add(r);
				internallyAddToRuns(aRun);
			}
			else {
				logger.warning("Unexpected run: " + aRun);
			}
		}

		/**
		 * Internally used to update {@link DocXParagraph} object according to wrapped model in the context of run adding (calling this
		 * assume that added run is already present in underlying {@link P})
		 * 
		 * @param addedRun
		 */
		private void internallyAddToRuns(FlexoDocRun<DocXDocument, DocXTechnologyAdapter> addedRun) {
			performSuperAdder(RUNS_KEY, addedRun);
			internallyHandleRunAdding(addedRun);
		}

		/**
		 * Remove run from this {@link DocXParagraph} (public API).<br>
		 * Element will be removed from underlying {@link P} and {@link DocXParagraph} will be updated accordingly
		 */
		@Override
		public void removeFromRuns(FlexoDocRun<DocXDocument, DocXTechnologyAdapter> aRun) {

			P p = getP();
			if (aRun instanceof DocXRun) {
				R r = ((DocXRun) aRun).getR();
				if (!DocXUtils.removeFromList(r, p.getContent())) {
					logger.warning("R item not present in P. Please investigate...");
				}
				internallyRemoveFromRuns(aRun);
			}
			else {
				logger.warning("Unexpected run: " + aRun);
			}
		}

		/**
		 * Internally used to update {@link DocXParagraph} object according to wrapped model in the context of run removing (calling this
		 * assume that removed run has already been removed from underlying {@link P})
		 * 
		 * @param removedRun
		 */
		private void internallyRemoveFromRuns(FlexoDocRun<DocXDocument, DocXTechnologyAdapter> removedRun) {
			if (removedRun instanceof DocXRun) {
				DocXRun run = (DocXRun) removedRun;
				if (run.getR() != null) {
					runs.remove(run.getR());
				}
			}
			performSuperRemover(RUNS_KEY, removedRun);
		}

		@Override
		public FlexoDocStyle<DocXDocument, DocXTechnologyAdapter> getStyle() {
			if (getP() != null && getP().getPPr() != null && getP().getPPr().getPStyle() != null && getFlexoDocument() != null) {
				String styleName = getP().getPPr().getPStyle().getVal();
				return getFlexoDocument().getStyleByIdentifier(styleName);
			}
			return null;
		}

		@Override
		public void setStyle(FlexoDocStyle<DocXDocument, DocXTechnologyAdapter> style) {
			if (getStyle() != style) {
				if (getP() != null) {
					PPr paragraphProperties = getP().getPPr();
					if (paragraphProperties == null) {
						ObjectFactory factory = Context.getWmlObjectFactory();
						paragraphProperties = factory.createPPr();
						getP().setPPr(paragraphProperties);
					}
					PStyle pStyle = paragraphProperties.getPStyle();
					if (pStyle == null) {
						ObjectFactory factory = Context.getWmlObjectFactory();
						pStyle = factory.createPPrBasePStyle();
						paragraphProperties.setPStyle(pStyle);
					}
					pStyle.setVal(style.getStyleId());
					if (getFlexoDocument() != null) {
						getFlexoDocument().invalidateRootElements();
						getFlexoDocument().notifyRootElementsChanged();
					}
				}
			}
		}

		@Override
		public DocXRun getRun(R r) {
			return runs.get(r);
		}

	}

}
