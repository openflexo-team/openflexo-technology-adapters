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

package org.openflexo.technologyadapter.docx;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.fml.DocXParagraphRole;
import org.openflexo.technologyadapter.docx.fml.action.AddDocXParagraph;
import org.openflexo.technologyadapter.docx.model.DocXDocument;

/**
 * Implementation of the ModelSlot class for the DOCX technology adapter<br>
 * We expect here to connect an .docx document
 * 
 * @author sylvain
 * 
 */
@DeclareFlexoRoles({ DocXParagraphRole.class })
@DeclareEditionActions({ AddDocXParagraph.class })
@ModelEntity
@ImplementationClass(DocXModelSlot.DocXModelSlotImpl.class)
@XMLElement
public interface DocXModelSlot extends FreeModelSlot<DocXDocument> {

	@Override
	public DocXTechnologyAdapter getModelSlotTechnologyAdapter();

	public static abstract class DocXModelSlotImpl extends FreeModelSlotImpl<DocXDocument>implements DocXModelSlot {

		private static final Logger logger = Logger.getLogger(DocXModelSlot.class.getPackage().getName());

		@Override
		public Class<DocXTechnologyAdapter> getTechnologyAdapterClass() {
			return DocXTechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public DocXModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new DocXModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (DocXParagraphRole.class.isAssignableFrom(patternRoleClass)) {
				return "document";
			}
			return null;
		}

		@Override
		public Type getType() {
			return DocXDocument.class;
		}

		@Override
		public DocXTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (DocXTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

	}
}
