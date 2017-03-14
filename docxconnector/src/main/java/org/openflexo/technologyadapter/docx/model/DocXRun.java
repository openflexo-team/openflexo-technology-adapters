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

import org.docx4j.TextUtils;
import org.docx4j.XmlUtils;
import org.docx4j.wml.R;
import org.docx4j.wml.RPrAbstract;
import org.openflexo.foundation.doc.FlexoDocRun;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Implementation;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

/**
 * Implementation of {@link FlexoDocRun} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity(isAbstract = true)
@ImplementationClass(DocXRun.DocXRunImpl.class)
public interface DocXRun extends FlexoDocRun<DocXDocument, DocXTechnologyAdapter>, DocXObject<R> {

	@PropertyIdentifier(type = R.class)
	public static final String R_KEY = "r";

	@Getter(value = R_KEY, ignoreType = true)
	@CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneR()")
	public R getR();

	@Setter(R_KEY)
	public void setR(R r);

	public R cloneR();

	/**
	 * This is the starting point for updating {@link DocXRun} with the run provided from docx4j library<br>
	 * Take care that the supplied r is the object we should update with, but that {@link #getR()} is unsafe in this context, because return
	 * former value
	 */
	public void updateFromR(R r, DocXFactory factory);

	@Implementation
	public static abstract class DocXRunImpl extends FlexoRunImpl<DocXDocument, DocXTechnologyAdapter> implements DocXRun {

		public DocXRunImpl() {
			super();
		}

		@Override
		public R getDocXObject() {
			return getR();
		}

		@Override
		public void setR(R r) {
			if ((r == null && getR() != null) || (r != null && !r.equals(getR()))) {
				if (r != null) {
					updateFromR(r, (getResourceData() != null && getResourceData().getResource() != null
							? ((DocXDocumentResource) getResourceData().getResource()).getFactory() : null));
				}
			}
		}

		/**
		 * This is the starting point for updating {@link DocXRun} with the paragraph provided from docx4j library<br>
		 * Take care that the supplied p is the object we should update with, but that {@link #getP()} is unsafe in this context, because
		 * return former value
		 */
		@Override
		public void updateFromR(R r, DocXFactory factory) {

			performSuperSetter(R_KEY, r);

			if (r != null) {
				RPrAbstract rpr = r.getRPr();
				if (rpr != null) {
					setRunStyle(factory.makeRunStyle(rpr));
				}
			}

		}

		public String getRawText() {
			StringWriter sw = new StringWriter();
			try {
				TextUtils.extractText(getR(), sw);
			} catch (Exception e) {
				e.printStackTrace();
				return "<" + e.getClass().getSimpleName() + " message: " + e.getMessage() + ">";
			}
			return sw.toString();
		}

		@Override
		public DocXDocument getFlexoDocument() {
			if (getParagraph() != null) {
				return getParagraph().getFlexoDocument();
			}
			return null;
		}

		@Override
		public R cloneR() {

			if (getR() == null) {
				return null;
			}
			R copiedR = XmlUtils.deepCopy(getR());

			return copiedR;
		}

		@Override
		public String toString() {
			return getImplementedInterface().getSimpleName() + "\n" + (getR() != null ? DocXUtils.printContents(getR(), 2) : "null");
		}
	}

}
