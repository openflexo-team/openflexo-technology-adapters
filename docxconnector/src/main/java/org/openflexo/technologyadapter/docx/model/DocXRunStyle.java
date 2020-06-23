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

import org.docx4j.wml.RPrAbstract;
import org.openflexo.foundation.doc.FlexoDocStyle;
import org.openflexo.foundation.doc.FlexoRunStyle;
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
@ImplementationClass(DocXRunStyle.DocXRunStyleImpl.class)
@XMLElement
public interface DocXRunStyle extends DocXObject<RPrAbstract>, FlexoRunStyle<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = RPrAbstract.class)
	public static final String RPR_KEY = "rPr";

	@Getter(value = RPR_KEY, ignoreType = true)
	public RPrAbstract getRPr();

	@Setter(RPR_KEY)
	public void setRPr(RPrAbstract rPr);

	/**
	 * This is the starting point for updating {@link DocXRunStyle} with the rPr provided from docx4j library<br>
	 * Take care that the supplied rPr is the object we should update with, but that {@link #getRPr()} is unsafe in this context, because
	 * return former value
	 */
	public void updateFromRPr(RPrAbstract rPr, DocXFactory factory);

	public static abstract class DocXRunStyleImpl extends FlexoRunStyleImpl<DocXDocument, DocXTechnologyAdapter> implements DocXRunStyle {

		@Override
		public RPrAbstract getDocXObject() {
			return getRPr();
		}

		@Override
		public void setRPr(RPrAbstract rPr) {
			if ((rPr == null && getRPr() != null) || (rPr != null && !rPr.equals(getRPr()))) {
				if (rPr != null && getResourceData() != null && getResourceData().getResource() != null) {
					updateFromRPr(rPr, ((DocXDocumentResource) getResourceData().getResource()).getFactory());
				}
			}
		}

		/**
		 * This is the starting point for updating {@link DocXRunStyle} with the style provided from docx4j library<br>
		 * Take care that the supplied p is the object we should update with, but that {@link #getStyle()} is unsafe in this context,
		 * because return former value
		 */
		@Override
		public void updateFromRPr(RPrAbstract rPr, DocXFactory factory) {

			performSuperSetter(RPR_KEY, rPr);
			// Take care at the previous line, since there is a risk for the notification not to be triggered,
			// if value of RPrAbstract given by getRPr() returns the new value

			factory.extractStyleProperties(rPr, this);
		}

		@Override
		public String toString() {
			return getStringRepresentation();
		}
	}

}
