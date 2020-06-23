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

import org.docx4j.wml.PPrBase;
import org.openflexo.foundation.doc.FlexoDocStyle;
import org.openflexo.foundation.doc.FlexoParagraphStyle;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

/**
 * Implementation of {@link FlexoDocStyle} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXParagraphStyle.DocXParagraphStyleImpl.class)
@XMLElement
public interface DocXParagraphStyle extends DocXObject<PPrBase>, FlexoParagraphStyle<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = PPrBase.class)
	public static final String PPR_KEY = "pPr";

	@Getter(value = PPR_KEY, ignoreType = true)
	public PPrBase getPPr();

	@Setter(PPR_KEY)
	public void setPPr(PPrBase pPr);

	/**
	 * This is the starting point for updating {@link DocXParagraphStyle} with the pPr provided from docx4j library<br>
	 * Take care that the supplied pPr is the object we should update with, but that {@link #getPPr()} is unsafe in this context, because
	 * return former value
	 */
	public void updateFromPPr(PPrBase pPr, DocXFactory factory);

	public static abstract class DocXParagraphStyleImpl extends FlexoParagraphStyleImpl<DocXDocument, DocXTechnologyAdapter>
			implements DocXParagraphStyle {

		@Override
		public PPrBase getDocXObject() {
			return getPPr();
		}

		@Override
		public void setPPr(PPrBase pPr) {
			if ((pPr == null && getPPr() != null) || (pPr != null && !pPr.equals(getPPr()))) {
				if (pPr != null && getResourceData() != null && getResourceData().getResource() != null) {
					updateFromPPr(pPr, ((DocXDocumentResource) getResourceData().getResource()).getFactory());
				}
			}
		}

		/**
		 * This is the starting point for updating {@link DocXParagraphStyle} with the style provided from docx4j library<br>
		 * Take care that the supplied p is the object we should update with, but that {@link #getStyle()} is unsafe in this context,
		 * because return former value
		 */
		@Override
		public void updateFromPPr(PPrBase pPr, DocXFactory factory) {

			performSuperSetter(PPR_KEY, pPr);
			// Take care at the previous line, since there is a risk for the notification not to be triggered,
			// if value of RPrAbstract given by getRPr() returns the new value

			factory.extractStyleProperties(pPr, this);
		}

		@Override
		public String toString() {
			return getStringRepresentation();
		}
	}

}
