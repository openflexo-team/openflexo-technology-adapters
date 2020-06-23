/**
 * 
 * Copyright (c) 2015, Openflexo
 * 
 * This file is part of Oslcconnector, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
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
import org.openflexo.foundation.technologyadapter.FreeModelSlot;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;
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
@FML("OSLCRMModelSlot")
public interface OSLCRMModelSlot extends FreeModelSlot<OSLCServiceProviderCatalog> {

	// @Override
	// public OSLCTechnologyAdapter getTechnologyAdapter();

	public static abstract class OSLCRMModelSlotImpl extends FreeModelSlotImpl<OSLCServiceProviderCatalog> implements OSLCRMModelSlot {

		@SuppressWarnings("unused")
		private static final Logger logger = Logger.getLogger(OSLCRMModelSlot.class.getPackage().getName());

		@Override
		public Class<OSLCTechnologyAdapter> getTechnologyAdapterClass() {
			return OSLCTechnologyAdapter.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (OSLCRequirementRole.class.isAssignableFrom(patternRoleClass)) {
				return "requirement";
			}
			else if (OSLCRequirementCollectionRole.class.isAssignableFrom(patternRoleClass)) {
				return "requirementCollection";
			}
			return null;
		}

		@Override
		public String getURIForObject(OSLCServiceProviderCatalog resourceData, Object o) {
			// TODO
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(OSLCServiceProviderCatalog resourceData, String objectURI) {
			// TODO
			return null;
		}

		@Override
		public Type getType() {
			return OSLCResource.class;
		}

		@Override
		public OSLCTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (OSLCTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

	}
}
