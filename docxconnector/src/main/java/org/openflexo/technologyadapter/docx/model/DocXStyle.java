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

import org.docx4j.wml.Style;
import org.openflexo.foundation.doc.FlexoStyle;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

/**
 * Implementation of {@link FlexoStyle} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXStyle.DocXStyleImpl.class)
@XMLElement
public interface DocXStyle extends DocXObject, FlexoStyle<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = DocXStyle.class)
	public static final String PARENT_STYLE_KEY = "parentStyle";

	@PropertyIdentifier(type = Style.class)
	public static final String STYLE_KEY = "style";

	@Getter(value = PARENT_STYLE_KEY)
	public DocXStyle getParentStyle();

	@Setter(PARENT_STYLE_KEY)
	public void setParentStyle(DocXStyle parentStyle);

	@Getter(value = STYLE_KEY, ignoreType = true)
	public Style getStyle();

	@Setter(STYLE_KEY)
	public void setStyle(Style style);

	/**
	 * This is the starting point for updating {@link DocXStyle} with the style provided from docx4j library<br>
	 * Take care that the supplied p is the object we should update with, but that {@link #getStyle()} is unsafe in this context, because
	 * return former value
	 */
	public void updateFromStyle(Style style, DocXFactory factory);

	public static abstract class DocXStyleImpl extends FlexoStyleImpl<DocXDocument, DocXTechnologyAdapter>implements DocXStyle {

		public DocXStyleImpl() {
			super();
		}

		@Override
		public void setStyle(Style style) {
			if ((style == null && getStyle() != null) || (style != null && !style.equals(getStyle()))) {
				updateFromStyle(style, ((DocXDocumentResource) getResourceData().getResource()).getFactory());
			}
		}

		/**
		 * This is the starting point for updating {@link DocXStyle} with the style provided from docx4j library<br>
		 * Take care that the supplied p is the object we should update with, but that {@link #getStyle()} is unsafe in this context,
		 * because return former value
		 */
		@Override
		public void updateFromStyle(Style style, DocXFactory factory) {

			performSuperSetter(STYLE_KEY, style);
			// Take care at the previous line, since there is a risk for the notification not to be triggered,
			// if value of P given by getStyle() returns the new value

		}

		@Override
		public String getName() {
			if (getStyle() != null) {
				return getStyle().getName().getVal();
			}
			return null;
		}

		@Override
		public void setName(String name) {
			// TODO not implemented yet
		}

		@Override
		public String getStyleId() {
			if (getStyle() != null) {
				return getStyle().getStyleId();
			}
			return null;
		}

		@Override
		public void setStyleId(String styleId) {
			// TODO not implemented yet
		}

		@Override
		public String toString() {
			return getName() + (getParentStyle() != null ? " (based on " + getParentStyle().getName() + ")" : "");
		}
	}

}
