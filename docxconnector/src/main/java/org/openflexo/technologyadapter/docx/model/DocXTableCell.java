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
import org.docx4j.wml.Tc;
import org.openflexo.foundation.doc.FlexoTableCell;
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
 * Implementation of {@link FlexoTableCell} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXTableCell.DocXTableCellImpl.class)
@XMLElement
public interface DocXTableCell extends FlexoTableCell<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = Tc.class)
	public static final String TC_KEY = "tc";

	@Getter(value = TC_KEY, ignoreType = true)
	@CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneTc()")
	public Tc getTc();

	@Setter(TC_KEY)
	public void setTc(Tc tr);

	public Tc cloneTc();

	/**
	 * This is the starting point for updating {@link DocXTableCell} with the Tc provided from docx4j library<br>
	 * Take care that the supplied tc is the object we should update with, but that {@link #getTc()} is unsafe in this context, because
	 * return former value
	 */
	public void updateFromTc(Tc tc, DocXFactory factory);

	public static abstract class DocXTableCellImpl extends FlexoTableCellImpl<DocXDocument, DocXTechnologyAdapter> implements DocXTableCell {

		public DocXTableCellImpl() {
			super();
		}

		@Override
		public void setTc(Tc tc) {
			if ((tc == null && getTc() != null) || (tc != null && !tc.equals(getTc()))) {
				if (tc != null) {
					updateFromTc(
							tc,
							(getResourceData() != null && getResourceData().getResource() != null ? ((DocXDocumentResource) getResourceData()
									.getResource()).getFactory() : null));
				}
			}
		}

		/**
		 * This is the starting point for updating {@link DocXTableCell} with the Tc provided from docx4j library<br>
		 * Take care that the supplied p is the object we should update with, but that {@link #getTc()} is unsafe in this context, because
		 * return former value
		 */
		@Override
		public void updateFromTc(Tc tc, DocXFactory factory) {

			performSuperSetter(TC_KEY, tc);

			for (Object o : tc.getContent()) {
				if (o instanceof JAXBElement) {
					o = ((JAXBElement) o).getValue();
				}
				System.out.println("Hop, dans la cellule: " + o);
			}

		}

		@Override
		public DocXDocument getFlexoDocument() {
			if (getRow() != null) {
				return getRow().getFlexoDocument();
			}
			return null;
		}

		@Override
		public Tc cloneTc() {

			if (getTc() == null) {
				return null;
			}
			Tc copiedTc = XmlUtils.deepCopy(getTc());

			return copiedTc;
		}

		@Override
		public String toString() {
			return "DocXTableCell\n" + (getTc() != null ? DocXUtils.printContents(getTc(), 2) : "null");
		}
	}

}
