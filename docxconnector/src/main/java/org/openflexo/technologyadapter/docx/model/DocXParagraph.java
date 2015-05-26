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
import org.docx4j.wml.P;
import org.openflexo.foundation.doc.FlexoParagraph;
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
public interface DocXParagraph extends DocXObject, FlexoParagraph<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = P.class)
	public static final String P_KEY = "p";

	@Getter(value = P_KEY, ignoreType = true)
	public P getP();

	@Setter(P_KEY)
	public void setP(P p);

	/**
	 * This is the starting point for updating {@link DocXParagraph} with the paragraph provided from docx4j library<br>
	 * Take care that the supplied p is the object we should update with, but that {@link #getP()} is unsafe in this context, because return
	 * former value
	 */
	public void updateFromP(P p, DocXFactory factory);

	public static abstract class DocXParagraphImpl extends FlexoParagraphImpl<DocXDocument, DocXTechnologyAdapter> implements DocXParagraph {

		public DocXParagraphImpl() {
			super();
		}

		@Override
		public void setP(P p) {
			if ((p == null && getP() != null) || (p != null && !p.equals(getP()))) {
				updateFromP(p, ((DocXDocumentResource) getResourceData().getResource()).getFactory());
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

	}

}
