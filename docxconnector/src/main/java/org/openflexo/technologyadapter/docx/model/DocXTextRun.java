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

import javax.xml.bind.JAXBElement;

import org.docx4j.jaxb.Context;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;
import org.openflexo.foundation.doc.FlexoDocRun;
import org.openflexo.foundation.doc.FlexoTextRun;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;

/**
 * Implementation of {@link FlexoDocRun} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXTextRun.DocXTextRunImpl.class)
@XMLElement
public interface DocXTextRun extends FlexoTextRun<DocXDocument, DocXTechnologyAdapter>, DocXRun {

	public static abstract class DocXTextRunImpl extends DocXRunImpl implements DocXTextRun {

		private Text text;

		/**
		 * This is the starting point for updating {@link DocXTextRun} with the paragraph provided from docx4j library<br>
		 * Take care that the supplied p is the object we should update with, but that {@link #getP()} is unsafe in this context, because
		 * return former value
		 */
		@Override
		public void updateFromR(R r, DocXFactory factory) {

			super.updateFromR(r, factory);

			for (Object o : r.getContent()) {
				if (o instanceof JAXBElement) {
					o = ((JAXBElement<?>) o).getValue();
				}
				if (o instanceof Text) {
					text = (Text) o;
				}
			}

		}

		@Override
		public String getText() {
			/*StringWriter sw = new StringWriter();
			try {
				TextUtils.extractText(getR(), sw);
			} catch (Exception e) {
				e.printStackTrace();
				return "<" + e.getClass().getSimpleName() + " message: " + e.getMessage() + ">";
			}
			return sw.toString();*/
			if (text != null) {
				return text.getValue();
			}
			return null;
		}

		@Override
		public void setText(String someText) {
			if ((someText == null && getText() != null) || (someText != null && !someText.equals(getText()))) {
				String oldValue = getText();
				if (text != null) {
					text.setValue(someText);
				}
				else {
					text = Context.getWmlObjectFactory().createText();
					text.setValue(someText);
					text.setSpace("preserve");
					getR().getContent().add(text);
					text.setParent(getR());
				}
				getPropertyChangeSupport().firePropertyChange("text", oldValue, text);
				if (getFlexoDocument() != null) {
					getFlexoDocument().setIsModified();
				}
			}

		}

	}

}
