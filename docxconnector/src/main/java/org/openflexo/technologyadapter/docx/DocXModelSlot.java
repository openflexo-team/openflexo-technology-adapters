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
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.technologyadapter.FreeModelSlot.FreeModelSlotImpl;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.fml.DocXFragmentRole;
import org.openflexo.technologyadapter.docx.fml.DocXParagraphRole;
import org.openflexo.technologyadapter.docx.fml.action.AddDocXParagraph;
import org.openflexo.technologyadapter.docx.model.DocXDocument;

/**
 * Implementation of the ModelSlot class for the DOCX technology adapter<br>
 * We expect here to connect an .docx document<br>
 * 
 * We might here supply a template document, which might be used as a "metamodel" to help manage connected document
 * 
 * @author sylvain
 * 
 */
@DeclareFlexoRoles({ DocXParagraphRole.class, DocXFragmentRole.class })
@DeclareEditionActions({ AddDocXParagraph.class })
@ModelEntity
@ImplementationClass(DocXModelSlot.DocXModelSlotImpl.class)
@XMLElement
public interface DocXModelSlot extends FreeModelSlot<DocXDocument> {

	@PropertyIdentifier(type = String.class)
	public static final String TEMPLATE_DOCUMENT_URI_KEY = "templateDocumentURI";
	@PropertyIdentifier(type = FlexoResource.class)
	public static final String TEMPLATE_RESOURCE_KEY = "templateResource";

	@Override
	public DocXTechnologyAdapter getModelSlotTechnologyAdapter();

	@Getter(value = TEMPLATE_DOCUMENT_URI_KEY)
	@XMLAttribute
	public String getTemplateDocumentURI();

	@Setter(TEMPLATE_DOCUMENT_URI_KEY)
	public void setTemplateDocumentURI(String templateDocumentURI);

	@Getter(TEMPLATE_RESOURCE_KEY)
	public FlexoResource<DocXDocument> getTemplateResource();

	@Setter(TEMPLATE_RESOURCE_KEY)
	public void setTemplateResource(FlexoResource<DocXDocument> templateResource);

	public static abstract class DocXModelSlotImpl extends FreeModelSlotImpl<DocXDocument> implements DocXModelSlot {

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
