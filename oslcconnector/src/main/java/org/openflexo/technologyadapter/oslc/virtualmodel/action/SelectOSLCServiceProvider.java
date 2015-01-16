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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.FetchRequest;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.oslc.OSLCCoreModelSlot;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProvider;

@FIBPanel("Fib/SelectOSLCServiceProviderPanel.fib")
@ModelEntity
@ImplementationClass(SelectOSLCServiceProvider.SelectOSLCServiceProviderImpl.class)
@XMLElement
@FML("SelectOSLCServiceProvider")
public interface SelectOSLCServiceProvider extends FetchRequest<OSLCCoreModelSlot, OSLCServiceProvider> {

	public static abstract class SelectOSLCServiceProviderImpl extends FetchRequestImpl<OSLCCoreModelSlot, OSLCServiceProvider> implements
			SelectOSLCServiceProvider {

		private static final Logger logger = Logger.getLogger(SelectOSLCServiceProvider.class.getPackage().getName());

		public SelectOSLCServiceProviderImpl() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public Type getFetchedType() {
			return OSLCServiceProvider.class;
		}

		@Override
		public List<OSLCServiceProvider> execute(FlexoBehaviourAction action) {

			if (getModelSlotInstance(action) == null) {
				logger.warning("Could not access model slot instance. Abort.");
				return null;
			}
			if (getModelSlotInstance(action).getResourceData() == null) {
				logger.warning("Could not access model adressed by model slot instance. Abort.");
				return null;
			}

			OSLCResource cdlUnit = (OSLCResource) getModelSlotInstance(action).getAccessedResourceData();

			List<OSLCServiceProvider> selectedOSLCServiceProviders = new ArrayList<OSLCServiceProvider>();

			List<OSLCServiceProvider> returned = filterWithConditions(selectedOSLCServiceProviders, action);

			return returned;
		}
	}
}
