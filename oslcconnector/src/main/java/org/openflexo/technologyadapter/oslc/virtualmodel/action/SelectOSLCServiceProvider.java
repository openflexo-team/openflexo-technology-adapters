package org.openflexo.technologyadapter.oslc.virtualmodel.action;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.annotations.FIBPanel;
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
		public List<OSLCServiceProvider> performAction(FlexoBehaviourAction action) {

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
