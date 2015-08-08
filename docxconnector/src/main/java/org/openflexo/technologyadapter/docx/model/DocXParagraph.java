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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.docx4j.TextUtils;
import org.docx4j.XmlUtils;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.openflexo.foundation.doc.FlexoParagraph;
import org.openflexo.foundation.doc.FlexoRun;
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
 * Implementation of {@link FlexoParagraph} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXParagraph.DocXParagraphImpl.class)
@XMLElement
public interface DocXParagraph extends DocXElement, FlexoParagraph<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = P.class)
	public static final String P_KEY = "p";

	@Getter(value = P_KEY, ignoreType = true)
	@CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneP()")
	public P getP();

	@Setter(P_KEY)
	public void setP(P p);

	public P cloneP();

	/**
	 * This is the starting point for updating {@link DocXParagraph} with the paragraph provided from docx4j library<br>
	 * Take care that the supplied p is the object we should update with, but that {@link #getP()} is unsafe in this context, because return
	 * former value
	 */
	public void updateFromP(P p, DocXFactory factory);

	public static abstract class DocXParagraphImpl extends FlexoParagraphImpl<DocXDocument, DocXTechnologyAdapter>implements DocXParagraph {

		private final Map<R, DocXRun> runs = new HashMap<R, DocXRun>();

		public DocXParagraphImpl() {
			super();
		}

		@Override
		public void setP(P p) {
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

			List<FlexoRun> runsToRemove = new ArrayList<FlexoRun>(getRuns());

			int currentIndex = 0;

			System.out.println("On regarde dans le paragraph");
			for (Object o : p.getContent()) {
				System.out.println("* dans le paragraph, y'a un " + o + " of " + o.getClass());
				if (o instanceof JAXBElement) {
					o = ((JAXBElement) o).getValue();
				}
				if (o instanceof R) {
					DocXRun run = runs.get(o);
					if (run == null) {
						// System.out.println("# Create new run for " + o);
						run = factory.makeNewDocXRun((R) o);
						insertRunAtIndex(run, currentIndex);
					} else {
						// OK run was found
						if (getRuns().indexOf(run) != currentIndex) {
							// Paragraph was existing but is not at the right position
							moveRunToIndex(run, currentIndex);
						} else {
							// System.out.println("# Found existing paragraph for " + o);
						}
						runsToRemove.remove(run);
					}
					currentIndex++;
				}
			}

			for (FlexoRun<DocXDocument, DocXTechnologyAdapter> run : runsToRemove) {
				// System.out.println("# Remove run for " + e);
				removeFromRuns(run);
			}

		}

		@Override
		public String getIdentifier() {
			if (getP() != null) {
				return getP().getParaId();
			}
			return null;
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

		/*@Override
		public ContentAccessor getParent() {
			return getFlexoDocument().getWordprocessingMLPackage().getMainDocumentPart();
		}*/

		@Override
		public P cloneP() {

			if (getP() == null) {
				return null;
			}
			P copiedP = XmlUtils.deepCopy(getP());

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

		@Override
		public void insertRunAtIndex(FlexoRun<DocXDocument, DocXTechnologyAdapter> aRun, int index) {
			performSuperAdder(RUNS_KEY, aRun, index);
			internallyHandleRunAdding(aRun);
		}

		private void internallyHandleRunAdding(FlexoRun<DocXDocument, DocXTechnologyAdapter> aRun) {
			if (aRun instanceof DocXRun) {
				DocXRun run = (DocXRun) aRun;
				if (run.getR() != null) {
					runs.put(run.getR(), run);
				}
			}
		}

		@Override
		public void moveRunToIndex(FlexoRun<DocXDocument, DocXTechnologyAdapter> aRun, int index) {
			List<FlexoRun<DocXDocument, DocXTechnologyAdapter>> runs = getRuns();
			runs.remove(aRun);
			runs.add(index, aRun);
		}

		@Override
		public void addToRuns(FlexoRun<DocXDocument, DocXTechnologyAdapter> aRun) {
			performSuperAdder(RUNS_KEY, aRun);
			internallyHandleRunAdding(aRun);
		}

		@Override
		public void removeFromRuns(FlexoRun<DocXDocument, DocXTechnologyAdapter> aRun) {
			if (aRun instanceof DocXRun) {
				DocXRun run = (DocXRun) aRun;
				if (run.getR() != null) {
					runs.remove(run.getR());
				}
			}
			performSuperRemover(RUNS_KEY, aRun);
		}

	}

}
