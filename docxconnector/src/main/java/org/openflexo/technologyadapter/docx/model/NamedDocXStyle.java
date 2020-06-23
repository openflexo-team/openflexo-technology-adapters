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
import org.openflexo.foundation.doc.NamedDocStyle;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

/**
 * Implementation of {@link NamedDocStyle} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(NamedDocXStyle.NamedDocXStyleImpl.class)
@XMLElement
public interface NamedDocXStyle extends DocXObject<Style>, NamedDocStyle<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = NamedDocXStyle.class)
	public static final String PARENT_STYLE_KEY = "parentStyle";

	@PropertyIdentifier(type = Style.class)
	public static final String STYLE_KEY = "style";

	@Getter(value = PARENT_STYLE_KEY)
	public NamedDocXStyle getParentStyle();

	@Setter(PARENT_STYLE_KEY)
	public void setParentStyle(NamedDocXStyle parentStyle);

	@Getter(value = STYLE_KEY, ignoreType = true)
	public Style getStyle();

	@Setter(STYLE_KEY)
	public void setStyle(Style style);

	/**
	 * This is the starting point for updating {@link NamedDocXStyle} with the style provided from docx4j library<br>
	 * Take care that the supplied p is the object we should update with, but that {@link #getStyle()} is unsafe in this context, because
	 * return former value
	 */
	public void updateFromStyle(Style style, DocXFactory factory);

	public static abstract class NamedDocXStyleImpl extends NamedDocStyleImpl<DocXDocument, DocXTechnologyAdapter>
			implements NamedDocXStyle {

		public NamedDocXStyleImpl() {
			super();
		}

		@Override
		public Style getDocXObject() {
			return getStyle();
		}

		@Override
		public void setStyle(Style style) {
			if ((style == null && getStyle() != null) || (style != null && !style.equals(getStyle()))) {
				if (style != null && getResourceData() != null && getResourceData().getResource() != null) {
					updateFromStyle(style, ((DocXDocumentResource) getResourceData().getResource()).getFactory());
				}
			}
		}

		/**
		 * This is the starting point for updating {@link NamedDocXStyle} with the style provided from docx4j library<br>
		 * Take care that the supplied p is the object we should update with, but that {@link #getStyle()} is unsafe in this context,
		 * because return former value
		 */
		@Override
		public void updateFromStyle(Style style, DocXFactory factory) {

			performSuperSetter(STYLE_KEY, style);
			// Take care at the previous line, since there is a risk for the notification not to be triggered,
			// if value of P given by getStyle() returns the new value

			if (style != null && style.getPPr() != null) {
				DocXParagraphStyle pStyle = factory.makeParagraphStyle();
				factory.extractStyleProperties(style.getPPr(), pStyle);
				setParagraphStyle(pStyle);
			}

			if (style != null && style.getRPr() != null) {
				DocXRunStyle rStyle = factory.makeRunStyle();
				factory.extractStyleProperties(style.getRPr(), rStyle);
				setRunStyle(rStyle);
			}
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

		@Override
		public String getStringRepresentation() {
			return toString();
		}
	}

}
