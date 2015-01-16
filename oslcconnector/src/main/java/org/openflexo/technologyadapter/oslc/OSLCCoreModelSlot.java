/*
 * (c) Copyright 2013 Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of either : 
 * - GNU General Public License as published by
 * the Free Software Foundation version 3 of the License.
 * - EUPL v1.1 : European Union Public Licence
 * 
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License or EUPL for more details.
 *
 * You should have received a copy of the GNU General Public License or 
 * European Union Public Licence along with OpenFlexo. 
 * If not, see <http://www.gnu.org/licenses/>, or http://ec.europa.eu/idabc/eupl.html
 *
 */

package org.openflexo.technologyadapter.oslc;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareEditionAction;
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequest;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRole;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.ontology.IFlexoOntologyObject;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.AddOSLCResource;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.AddOSLCServiceProvider;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCResource;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCService;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCServiceProvider;
import org.openflexo.technologyadapter.oslc.virtualmodel.core.OSLCResourceRole;
import org.openflexo.technologyadapter.oslc.virtualmodel.core.OSLCServiceProviderRole;
import org.openflexo.technologyadapter.oslc.virtualmodel.core.OSLCServiceRole;

/**
 * Implementation of the ModelSlot class for the OSLC technology adapter<br>
 * 
 * @author vleilde
 * 
 */
@ModelEntity
@ImplementationClass(OSLCCoreModelSlot.CDLModelSlotImpl.class)
@XMLElement
@DeclareFlexoRoles({ // All pattern roles available through this model slot
@DeclareFlexoRole(FML = "OSLCResource", flexoRoleClass = OSLCResourceRole.class),
		@DeclareFlexoRole(FML = "OSLCServiceProvider", flexoRoleClass = OSLCServiceProviderRole.class),
		@DeclareFlexoRole(FML = "OSLCService", flexoRoleClass = OSLCServiceRole.class) })
@DeclareEditionActions({ // All edition actions available through this modelslot
@DeclareEditionAction(FML = "AddOSLCResource", editionActionClass = AddOSLCResource.class),
		@DeclareEditionAction(FML = "AddOSLCServiceProvider", editionActionClass = AddOSLCServiceProvider.class) })
@DeclareFetchRequests({ // All requests available through this model slot
		@DeclareFetchRequest(FML = "SelectOSLCResource", fetchRequestClass = SelectOSLCResource.class), // Sheet
		@DeclareFetchRequest(FML = "SelectOSLCServiceProvider", fetchRequestClass = SelectOSLCServiceProvider.class),
		@DeclareFetchRequest(FML = "SelectOSLCService", fetchRequestClass = SelectOSLCService.class) })
public interface OSLCCoreModelSlot extends FreeModelSlot<OSLCResource> {

	// @Override
	// public OSLCTechnologyAdapter getTechnologyAdapter();

	public static abstract class CDLModelSlotImpl extends FreeModelSlotImpl<OSLCResource> implements OSLCCoreModelSlot {

		private static final Logger logger = Logger.getLogger(OSLCCoreModelSlot.class.getPackage().getName());

		@Override
		public Class<OSLCTechnologyAdapter> getTechnologyAdapterClass() {
			return OSLCTechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public OSLCCoreModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new OSLCCoreModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (OSLCResourceRole.class.isAssignableFrom(patternRoleClass)) {
				return "OSLCResource";
			}
			return null;
		}

		@Override
		public String getURIForObject(FreeModelSlotInstance<OSLCResource, ? extends FreeModelSlot<OSLCResource>> msInstance, Object o) {
			if (o instanceof IFlexoOntologyObject) {
				return ((IFlexoOntologyObject) o).getURI();
			}
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(FreeModelSlotInstance<OSLCResource, ? extends FreeModelSlot<OSLCResource>> msInstance,
				String objectURI) {
			return msInstance.getResourceData().getObject(objectURI);
		}

		@Override
		public Type getType() {
			return OSLCResource.class;
		}

		/*@Override
		public OSLCTechnologyAdapter getTechnologyAdapter() {
			return (OSLCTechnologyAdapter) super.getTechnologyAdapter();
		}*/

	}
}
