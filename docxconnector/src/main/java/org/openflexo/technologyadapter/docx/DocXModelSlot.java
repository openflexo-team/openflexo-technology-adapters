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
import org.openflexo.foundation.doc.fml.ImageActorReference;
import org.openflexo.foundation.doc.fml.ParagraphActorReference;
import org.openflexo.foundation.doc.fml.TableActorReference;
import org.openflexo.foundation.doc.fml.TextSelectionActorReference;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareActorReferences;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.task.FlexoTask;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.docx.fml.DocXFragmentRole;
import org.openflexo.technologyadapter.docx.fml.DocXImageRole;
import org.openflexo.technologyadapter.docx.fml.DocXParagraphRole;
import org.openflexo.technologyadapter.docx.fml.DocXTableRole;
import org.openflexo.technologyadapter.docx.fml.DocXTextSelectionRole;
import org.openflexo.technologyadapter.docx.fml.editionaction.AddDocXFragment;
import org.openflexo.technologyadapter.docx.fml.editionaction.AddDocXParagraph;
import org.openflexo.technologyadapter.docx.fml.editionaction.ApplyTextBindings;
import org.openflexo.technologyadapter.docx.fml.editionaction.CreateEmptyDocXResource;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXDocument;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXImage;
import org.openflexo.technologyadapter.docx.fml.editionaction.GenerateDocXTable;
import org.openflexo.technologyadapter.docx.fml.editionaction.ReinjectFromDocXTable;
import org.openflexo.technologyadapter.docx.fml.editionaction.ReinjectTextBindings;
import org.openflexo.technologyadapter.docx.fml.editionaction.SelectGeneratedDocXFragment;
import org.openflexo.technologyadapter.docx.fml.editionaction.SelectGeneratedDocXImage;
import org.openflexo.technologyadapter.docx.fml.editionaction.SelectGeneratedDocXTable;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.IdentifierManagementStrategy;
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
@DeclareFlexoRoles({ DocXParagraphRole.class, DocXTableRole.class, DocXFragmentRole.class, DocXImageRole.class,
		DocXTextSelectionRole.class })
@DeclareEditionActions({ GenerateDocXDocument.class, AddDocXFragment.class, AddDocXParagraph.class, ApplyTextBindings.class,
		ReinjectTextBindings.class, SelectGeneratedDocXFragment.class, GenerateDocXTable.class, ReinjectFromDocXTable.class,
		SelectGeneratedDocXTable.class, GenerateDocXImage.class, SelectGeneratedDocXImage.class, CreateEmptyDocXResource.class })
@DeclareActorReferences({ FragmentActorReference.class, TableActorReference.class, ParagraphActorReference.class, ImageActorReference.class,
		TextSelectionActorReference.class })
@ModelEntity
@ImplementationClass(DocXModelSlot.DocXModelSlotImpl.class)
@XMLElement
public interface DocXModelSlot extends FlexoDocumentModelSlot<DocXDocument> {

	@PropertyIdentifier(type = FlexoResource.class)
	public static final String TEMPLATE_RESOURCE_KEY = "templateResource";

	@PropertyIdentifier(type = IdentifierManagementStrategy.class)
	public static final String ID_STRATEGY_KEY = "idStrategy";

	@Override
	@Getter(TEMPLATE_RESOURCE_KEY)
	public DocXDocumentResource getTemplateResource();

	@Setter(TEMPLATE_RESOURCE_KEY)
	public void setTemplateResource(DocXDocumentResource templateResource);

	@Getter(ID_STRATEGY_KEY)
	@XMLAttribute
	public IdentifierManagementStrategy getIdStrategy();

	@Setter(ID_STRATEGY_KEY)
	public void setIdStrategy(IdentifierManagementStrategy idStrat);

	// Implem
	public static abstract class DocXModelSlotImpl extends FlexoDocumentModelSlotImpl<DocXDocument> implements DocXModelSlot {

		private static final Logger logger = Logger.getLogger(DocXModelSlot.class.getPackage().getName());

		@Override
		public Class<DocXTechnologyAdapter> getTechnologyAdapterClass() {
			return DocXTechnologyAdapter.class;
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

		private DocXDocumentResource templateResource;

		@Override
		public DocXDocumentResource getTemplateResource() {

			if (templateResource == null && StringUtils.isNotEmpty(templateDocumentURI)
					&& getServiceManager().getResourceManager() != null) {

				FlexoTask activateTA = getServiceManager().activateTechnologyAdapter(getModelSlotTechnologyAdapter());
				if (activateTA != null) {
					getServiceManager().getTaskManager().waitTask(activateTA);
				}

				// System.out.println("Looking up " + templateDocumentURI);
				templateResource = (DocXDocumentResource) getServiceManager().getResourceManager().getResource(templateDocumentURI);
				// System.out.println("templateResource = " + templateResource);
				// for (FlexoResourceCenter<?> rc : getServiceManager().getResourceCenterService().getResourceCenters()) {
				// System.out.println("* rc=" + rc);
				// for (FlexoResource r : rc.getAllResources(null)) {
				// System.out.println(" >> " + r);
				// }
				// }
				// for (FlexoResource r : getServiceManager().getResourceManager().getRegisteredResources()) {
				// System.out.println("> " + r.getURI());
				// }
			}

			return templateResource;
		}

		@Override
		public void setTemplateResource(DocXDocumentResource templateResource) {
			if (templateResource != this.templateResource) {
				DocXDocumentResource oldValue = this.templateResource;
				this.templateResource = templateResource;
				getPropertyChangeSupport().firePropertyChange("templateResource", oldValue, templateResource);
			}
		}

		/*@Override
		public TechnologyAdapterResource<DocXDocument, ?> createProjectSpecificEmptyResource(VirtualModelInstance<?, ?> view,
				String filename, String modelUri) {
		
			return getModelSlotTechnologyAdapter().createNewDocXDocumentResource(view.getResourceCenter(), filename, true, getIdStrategy());
		}
		
		@Override
		public TechnologyAdapterResource<DocXDocument, ?> createSharedEmptyResource(FlexoResourceCenter<?> resourceCenter,
				String relativePath, String filename, String modelUri) {
			if (resourceCenter instanceof FileSystemBasedResourceCenter) {
				return getModelSlotTechnologyAdapter().createNewDocXDocumentResource((FileSystemBasedResourceCenter) resourceCenter,
						relativePath, filename, false, getIdStrategy());
			}
			// TODO
			logger.warning("Could not create docx in this kind of ResourceCenter");
			return null;
		}*/

		@Override
		// returns default value to paraId when none specified, because it is less intrusive (templage management)
		public IdentifierManagementStrategy getIdStrategy() {
			IdentifierManagementStrategy val = (IdentifierManagementStrategy) this.performSuperGetter(ID_STRATEGY_KEY);
			if (val == null) {
				val = IdentifierManagementStrategy.ParaId;
			}
			return val;
		}
	}
}
