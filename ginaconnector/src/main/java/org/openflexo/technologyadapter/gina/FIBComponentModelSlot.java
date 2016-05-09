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

package org.openflexo.technologyadapter.gina;

import java.lang.reflect.Type;

import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.rt.AbstractVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.gina.fml.FIBComponentRole;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResource;
import org.openflexo.toolbox.StringUtils;

/**
 * A {@link ModelSlot} used to reference a {@link GINAFIBComponent}
 * 
 * @author Sylvain Guérin
 * 
 */
@DeclareFlexoRoles({ FIBComponentRole.class })
@DeclareEditionActions({})
@DeclareFetchRequests({})
@ModelEntity
@ImplementationClass(FIBComponentModelSlot.FIBComponentModelSlotImpl.class)
@XMLElement
public interface FIBComponentModelSlot extends FreeModelSlot<GINAFIBComponent> {

	@PropertyIdentifier(type = String.class)
	public static final String TEMPLATE_COMPONENT_URI_KEY = "templateComponentURI";
	@PropertyIdentifier(type = FlexoResource.class)
	public static final String TEMPLATE_RESOURCE_KEY = "templateResource";

	@Getter(value = TEMPLATE_COMPONENT_URI_KEY)
	@XMLAttribute
	public String getTemplateComponentURI();

	@Setter(TEMPLATE_COMPONENT_URI_KEY)
	public void setTemplateComponentURI(String templateComponentURI);

	@Getter(TEMPLATE_RESOURCE_KEY)
	public GINAFIBComponentResource getTemplateResource();

	@Setter(TEMPLATE_RESOURCE_KEY)
	public void setTemplateResource(GINAFIBComponentResource templateResource);

	@Override
	public GINATechnologyAdapter getModelSlotTechnologyAdapter();

	public static abstract class FIBComponentModelSlotImpl extends FreeModelSlotImpl<GINAFIBComponent>implements FIBComponentModelSlot {

		protected String templateComponentURI;
		private GINAFIBComponentResource templateResource;

		@Override
		public Class<GINATechnologyAdapter> getTechnologyAdapterClass() {
			return GINATechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public ModelSlotInstanceConfiguration<? extends FreeModelSlot<GINAFIBComponent>, GINAFIBComponent> createConfiguration(
				AbstractVirtualModelInstance<?, ?> virtualModelInstance, FlexoProject project) {
			return new FIBComponentModelSlotInstanceConfiguration(this, virtualModelInstance, project);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> flexoRoleClass) {
			if (FIBComponentRole.class.isAssignableFrom(flexoRoleClass)) {
				return "component";
			}
			return "";
		}

		@Override
		public Type getType() {
			return GINAFIBComponent.class;
		}

		@Override
		public GINATechnologyAdapter getModelSlotTechnologyAdapter() {
			return (GINATechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		@Override
		public String getTemplateComponentURI() {
			if (getTemplateResource() != null) {
				return getTemplateResource().getURI();
			}
			return templateComponentURI;
		}

		@Override
		public void setTemplateComponentURI(String templateComponentURI) {
			if ((templateComponentURI == null && this.templateComponentURI != null)
					|| (templateComponentURI != null && !templateComponentURI.equals(this.templateComponentURI))) {
				String oldValue = this.templateComponentURI;
				this.templateComponentURI = templateComponentURI;
				getPropertyChangeSupport().firePropertyChange("templateComponentURI", oldValue, templateComponentURI);
			}
		}

		@Override
		public GINAFIBComponentResource getTemplateResource() {
			if (templateResource == null && StringUtils.isNotEmpty(templateComponentURI)
					&& getServiceManager().getResourceManager() != null) {
				// System.out.println("Looking up " + templateDocumentURI);
				templateResource = (GINAFIBComponentResource) getServiceManager().getResourceManager().getResource(templateComponentURI,
						null);
				// System.out.println("templateResource = " + returned);
				// for (FlexoResource r : getServiceManager().getResourceManager().getRegisteredResources()) {
				// System.out.println("> " + r.getURI());
				// }
			}
			return templateResource;
		}

		@Override
		public void setTemplateResource(GINAFIBComponentResource templateResource) {
			if (templateResource != this.templateResource) {
				GINAFIBComponentResource oldValue = this.templateResource;
				this.templateResource = templateResource;
				getPropertyChangeSupport().firePropertyChange("templateResource", oldValue, templateResource);
			}
		}

	}
}
