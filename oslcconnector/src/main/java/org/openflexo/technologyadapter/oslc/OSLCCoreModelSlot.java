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
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;
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
@DeclareFlexoRoles({ OSLCResourceRole.class, OSLCServiceProviderRole.class, OSLCServiceRole.class })
@DeclareEditionActions({ AddOSLCResource.class, AddOSLCServiceProvider.class })
@DeclareFetchRequests({ SelectOSLCResource.class, SelectOSLCServiceProvider.class, SelectOSLCService.class })
@FML("OSLCCoreModelSlot")
public interface OSLCCoreModelSlot extends FreeModelSlot<OSLCServiceProviderCatalog> {

	// @Override
	// public OSLCTechnologyAdapter getTechnologyAdapter();

	public static abstract class CDLModelSlotImpl extends FreeModelSlotImpl<OSLCServiceProviderCatalog> implements OSLCCoreModelSlot {

		private static final Logger logger = Logger.getLogger(OSLCCoreModelSlot.class.getPackage().getName());

		@Override
		public Class<OSLCTechnologyAdapter> getTechnologyAdapterClass() {
			return OSLCTechnologyAdapter.class;
		}

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (OSLCResourceRole.class.isAssignableFrom(patternRoleClass)) {
				return "OSLCResource";
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
