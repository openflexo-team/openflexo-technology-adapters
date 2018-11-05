/*
 * (c) Copyright 2013- Openflexo
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

package org.openflexo.technologyadapter.pdf;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.pdf.model.PDFDocument;

/**
 * Implementation of the ModelSlot class for the DOCX technology adapter<br>
 * We expect here to connect an .docx document<br>
 * 
 * We might here supply a template document, which might be used as a "metamodel" to help manage connected document
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(PDFModelSlot.PDFModelSlotImpl.class)
@XMLElement
public interface PDFModelSlot extends FreeModelSlot<PDFDocument> {

	// Implem
	public static abstract class PDFModelSlotImpl extends FreeModelSlotImpl<PDFDocument> implements PDFModelSlot {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(PDFModelSlot.class.getPackage().getName());

		@Override
		public Class<PDFTechnologyAdapter> getTechnologyAdapterClass() {
			return PDFTechnologyAdapter.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			return null;
		}

		@Override
		public Type getType() {
			return PDFDocument.class;
		}

		@Override
		public PDFTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (PDFTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}
	}
}
