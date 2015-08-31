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
import org.docx4j.wml.Tr;
import org.openflexo.foundation.doc.FlexoTableRow;
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
 * Implementation of {@link FlexoTableRow} for {@link DocXTechnologyAdapter}
 * 
 * @author sylvain
 *
 */
@ModelEntity
@ImplementationClass(DocXTableRow.DocXTableRowImpl.class)
@XMLElement
public interface DocXTableRow extends FlexoTableRow<DocXDocument, DocXTechnologyAdapter> {

	@PropertyIdentifier(type = Tr.class)
	public static final String TR_KEY = "tr";

	@Getter(value = TR_KEY, ignoreType = true)
	@CloningStrategy(value = StrategyType.CUSTOM_CLONE, factory = "cloneTr()")
	public Tr getTr();

	@Setter(TR_KEY)
	public void setTr(Tr tr);

	public Tr cloneTr();

	/**
	 * This is the starting point for updating {@link DocXTableRow} with the Tr provided from docx4j library<br>
	 * Take care that the supplied tr is the object we should update with, but that {@link #getTr()} is unsafe in this context, because
	 * return former value
	 */
	public void updateFromTr(Tr tr, DocXFactory factory);

	public static abstract class DocXTableRowImpl extends FlexoTableRowImpl<DocXDocument, DocXTechnologyAdapter>implements DocXTableRow {

		public DocXTableRowImpl() {
			super();
		}

		@Override
		public void setTr(Tr tr) {
			if ((tr == null && getTr() != null) || (tr != null && !tr.equals(getTr()))) {
				if (tr != null) {
					updateFromTr(tr, (getResourceData() != null && getResourceData().getResource() != null
							? ((DocXDocumentResource) getResourceData().getResource()).getFactory() : null));
				}
			}
		}

		/**
		 * This is the starting point for updating {@link DocXTableRow} with the Tr provided from docx4j library<br>
		 * Take care that the supplied p is the object we should update with, but that {@link #getTr()} is unsafe in this context, because
		 * return former value
		 */
		@Override
		public void updateFromTr(Tr tr, DocXFactory factory) {

			performSuperSetter(TR_KEY, tr);

			for (Object o : tr.getContent()) {
				if (o instanceof JAXBElement) {
					o = ((JAXBElement) o).getValue();
				}
				if (o instanceof Tc) {
					System.out.println("Hop, une cellule: " + ((Tc) o).toString());
				}
			}

		}

		@Override
		public DocXDocument getFlexoDocument() {
			if (getTable() != null) {
				return getTable().getFlexoDocument();
			}
			return null;
		}

		@Override
		public Tr cloneTr() {

			if (getTr() == null) {
				return null;
			}
			Tr copiedTr = XmlUtils.deepCopy(getTr());

			return copiedTr;
		}

		@Override
		public String toString() {
			return "DocXTableRow\n" + (getTr() != null ? DocXUtils.printContents(getTr(), 2) : "null");
		}
	}

}
