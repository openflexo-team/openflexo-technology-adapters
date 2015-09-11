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

import org.openflexo.foundation.doc.fml.FlexoDocumentModelSlot;
import org.openflexo.foundation.doc.fml.FragmentActorReference;
import org.openflexo.foundation.doc.fml.TableActorReference;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareActorReferences;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterResource;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.fml.DocXFragmentRole;
import org.openflexo.technologyadapter.docx.fml.DocXParagraphRole;
import org.openflexo.technologyadapter.docx.fml.DocXTableRole;
import org.openflexo.technologyadapter.docx.fml.editionaction.AddDocXFragment;
import org.openflexo.technologyadapter.docx.fml.editionaction.AddDocXParagraph;
import org.openflexo.technologyadapter.docx.fml.editionaction.ApplyTextBindings;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXDocument;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXTable;
import org.openflexo.technologyadapter.docx.fml.editionaction.ReinjectFromDocXTable;
import org.openflexo.technologyadapter.docx.fml.editionaction.ReinjectTextBindings;
import org.openflexo.technologyadapter.docx.fml.editionaction.SelectGeneratedDocXFragment;
import org.openflexo.technologyadapter.docx.fml.editionaction.SelectGeneratedDocXTable;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.toolbox.StringUtils;

/**
 * Implementation of the ModelSlot class for the DOCX technology adapter<br>
 * We expect here to connect an .docx document<br>
 * 
 * We might here supply a template document, which might be used as a "metamodel" to help manage connected document
 * 
 * @author sylvain
 * 
 */
@DeclareFlexoRoles({ DocXParagraphRole.class, DocXTableRole.class, DocXFragmentRole.class })
@DeclareEditionActions({ GenerateDocXDocument.class, AddDocXFragment.class, AddDocXParagraph.class, ApplyTextBindings.class,
		ReinjectTextBindings.class, SelectGeneratedDocXFragment.class, GenerateDocXTable.class, ReinjectFromDocXTable.class,
		SelectGeneratedDocXTable.class })
@DeclareActorReferences({ FragmentActorReference.class, TableActorReference.class })
@ModelEntity
@ImplementationClass(DocXModelSlot.DocXModelSlotImpl.class)
@XMLElement
public interface DocXModelSlot extends FlexoDocumentModelSlot<DocXDocument> {

	@Override
	@Getter(TEMPLATE_RESOURCE_KEY)
	public DocXDocumentResource getTemplateResource();

	@Setter(TEMPLATE_RESOURCE_KEY)
	public void setTemplateResource(DocXDocumentResource templateResource);

	// Implem
	public static abstract class DocXModelSlotImpl extends FlexoDocumentModelSlotImpl<DocXDocument>implements DocXModelSlot {

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

		@Override
		public DocXDocumentResource getTemplateResource() {
			DocXDocumentResource returned = (DocXDocumentResource) performSuperGetter(TEMPLATE_RESOURCE_KEY);
			if (returned == null && StringUtils.isNotEmpty(templateDocumentURI) && getServiceManager().getResourceManager() != null) {
				returned = (DocXDocumentResource) getServiceManager().getResourceManager().getResource(templateDocumentURI, null);
			}
			return returned;
		}

		@Override
		public TechnologyAdapterResource<DocXDocument, ?> createProjectSpecificEmptyResource(View view, String filename, String modelUri) {

			return getModelSlotTechnologyAdapter().createNewDocXDocumentResource(view.getProject(), filename, false);
		}

		@Override
		public TechnologyAdapterResource<DocXDocument, ?> createSharedEmptyResource(FlexoResourceCenter<?> resourceCenter,
				String relativePath, String filename, String modelUri) {
			if (resourceCenter instanceof FileSystemBasedResourceCenter) {
				return getModelSlotTechnologyAdapter().createNewDocXDocumentResource((FileSystemBasedResourceCenter) resourceCenter,
						relativePath, filename, false);
			}
			// TODO
			logger.warning("Could not create docx in this kind of ResourceCenter");
			return null;
		}
	}
}
