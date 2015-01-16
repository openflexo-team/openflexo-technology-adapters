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
import org.openflexo.foundation.fml.annotations.DeclareEditionActions;
import org.openflexo.foundation.fml.annotations.DeclareFetchRequests;
import org.openflexo.foundation.fml.annotations.DeclareFlexoRoles;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.ontology.IFlexoOntologyObject;
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.AddOSLCRequirement;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.AddOSLCRequirementCollection;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCRequirement;
import org.openflexo.technologyadapter.oslc.virtualmodel.action.SelectOSLCRequirementCollection;
import org.openflexo.technologyadapter.oslc.virtualmodel.rm.OSLCRequirementCollectionRole;
import org.openflexo.technologyadapter.oslc.virtualmodel.rm.OSLCRequirementRole;

/**
 * Implementation of the ModelSlot class for the OSLC technology adapter<br>
 * 
 * @author vleilde
 * 
 */
@ModelEntity
@ImplementationClass(OSLCRMModelSlot.OSLCRMModelSlotImpl.class)
@XMLElement
@DeclareFlexoRoles({ OSLCRequirementRole.class, OSLCRequirementCollectionRole.class })
@DeclareEditionActions({ AddOSLCRequirement.class, AddOSLCRequirementCollection.class })
@DeclareFetchRequests({ SelectOSLCRequirement.class, SelectOSLCRequirementCollection.class })
@FML("OSLCCoreModelSlot")
public interface OSLCRMModelSlot extends FreeModelSlot<OSLCResource> {

	// @Override
	// public OSLCTechnologyAdapter getTechnologyAdapter();

	public static abstract class OSLCRMModelSlotImpl extends FreeModelSlotImpl<OSLCResource> implements OSLCRMModelSlot {

		private static final Logger logger = Logger.getLogger(OSLCRMModelSlot.class.getPackage().getName());

		@Override
		public Class<OSLCTechnologyAdapter> getTechnologyAdapterClass() {
			return OSLCTechnologyAdapter.class;
		}

		/**
		 * Instanciate a new model slot instance configuration for this model slot
		 */
		@Override
		public OSLCRMModelSlotInstanceConfiguration createConfiguration(CreateVirtualModelInstance action) {
			return new OSLCRMModelSlotInstanceConfiguration(this, action);
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (OSLCRequirementRole.class.isAssignableFrom(patternRoleClass)) {
				return "requirement";
			} else if (OSLCRequirementCollectionRole.class.isAssignableFrom(patternRoleClass)) {
				return "requirementCollection";
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
