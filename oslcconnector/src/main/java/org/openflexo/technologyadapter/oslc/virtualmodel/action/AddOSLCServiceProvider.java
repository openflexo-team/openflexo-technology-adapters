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

package org.openflexo.technologyadapter.oslc.virtualmodel.action;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.openflexo.fib.annotation.FIBPanel;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.oslc.OSLCCoreModelSlot;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProvider;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;

@FIBPanel("Fib/AddOSLCServiceProviderPanel.fib")
@ModelEntity
@ImplementationClass(AddOSLCServiceProvider.AddOSLCServiceProviderImpl.class)
@XMLElement
@FML("AddOSLCServiceProvider")
public interface AddOSLCServiceProvider extends OSLCCoreAction<OSLCServiceProvider> {

	public static abstract class AddOSLCServiceProviderImpl extends TechnologySpecificActionImpl<OSLCCoreModelSlot, OSLCServiceProvider>
			implements AddOSLCServiceProvider {

		private static final Logger logger = Logger.getLogger(AddOSLCServiceProvider.class.getPackage().getName());

		public AddOSLCServiceProviderImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return OSLCServiceProvider.class;
		}

		@Override
		public OSLCServiceProvider execute(RunTimeEvaluationContext evaluationContext) {

			OSLCServiceProvider cdlActivity = null;

			FreeModelSlotInstance<OSLCServiceProviderCatalog, OSLCCoreModelSlot> modelSlotInstance = getModelSlotInstance(evaluationContext);
			if (modelSlotInstance.getResourceData() != null) {

			} else {
				logger.warning("Model slot not correctly initialised : model is null");
				return null;
			}

			return cdlActivity;
		}

		@Override
		public FreeModelSlotInstance<OSLCServiceProviderCatalog, OSLCCoreModelSlot> getModelSlotInstance(
				RunTimeEvaluationContext evaluationContext) {
			return (FreeModelSlotInstance<OSLCServiceProviderCatalog, OSLCCoreModelSlot>) super.getModelSlotInstance(evaluationContext);
		}

	}
}
