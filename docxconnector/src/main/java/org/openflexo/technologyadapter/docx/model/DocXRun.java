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

import org.docx4j.XmlUtils;
import org.docx4j.jaxb.Context;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;
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
 * Implementation of {@link FlexoRun} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXRun.DocXRunImpl.class)
@XMLElement
public interface DocXRun extends FlexoRun<DocXDocument, DocXTechnologyAdapter> {

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

	public static abstract class DocXRunImpl extends FlexoRunImpl<DocXDocument, DocXTechnologyAdapter>implements DocXRun {

		private Text text;

		public DocXRunImpl() {
			super();
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

			for (Object o : r.getContent()) {
				if (o instanceof JAXBElement) {
					o = ((JAXBElement) o).getValue();
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
			return "DocXRun\n" + (getR() != null ? DocXUtils.printContents(getR(), 2) : "null");
		}
	}

}
