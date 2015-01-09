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
package org.openflexo.technologyadapter.oslc.virtualmodel.action;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.editionaction.AssignableAction;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.oslc.OSLCCoreModelSlot;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProvider;

@FIBPanel("Fib/AddOSLCServiceProviderPanel.fib")
@ModelEntity
@ImplementationClass(AddOSLCServiceProvider.AddOSLCServiceProviderImpl.class)
@XMLElement
public interface AddOSLCServiceProvider extends AssignableAction<OSLCCoreModelSlot, OSLCServiceProvider> {

	public static abstract class AddOSLCServiceProviderImpl extends AssignableActionImpl<OSLCCoreModelSlot, OSLCServiceProvider> implements
			AddOSLCServiceProvider {

		private static final Logger logger = Logger.getLogger(AddOSLCServiceProvider.class.getPackage().getName());

		public AddOSLCServiceProviderImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return OSLCServiceProvider.class;
		}

		@Override
		public OSLCServiceProvider performAction(FlexoBehaviourAction action) {

			OSLCServiceProvider cdlActivity = null;

			FreeModelSlotInstance<OSLCResource, OSLCCoreModelSlot> modelSlotInstance = getModelSlotInstance(action);
			if (modelSlotInstance.getResourceData() != null) {

			}
			else {
				logger.warning("Model slot not correctly initialised : model is null");
				return null;
			}

			return cdlActivity;
		}

		@Override
		public FreeModelSlotInstance<OSLCResource, OSLCCoreModelSlot> getModelSlotInstance(FlexoBehaviourAction action) {
			return (FreeModelSlotInstance<OSLCResource, OSLCCoreModelSlot>) super.getModelSlotInstance(action);
		}

	}
}
