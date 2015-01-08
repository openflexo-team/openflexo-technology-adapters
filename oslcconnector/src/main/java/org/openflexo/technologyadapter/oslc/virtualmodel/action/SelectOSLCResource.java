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

@FIBPanel("Fib/SelectOSLCResourcePanel.fib")
@ModelEntity
@ImplementationClass(SelectOSLCResource.SelectOSLCResourceImpl.class)
@XMLElement
public interface SelectOSLCResource extends FetchRequest<OSLCCoreModelSlot, OSLCResource> {

	public static abstract class SelectOSLCResourceImpl extends FetchRequestImpl<OSLCCoreModelSlot, OSLCResource> implements
			SelectOSLCResource {

		private static final Logger logger = Logger.getLogger(SelectOSLCResource.class.getPackage().getName());

		public SelectOSLCResourceImpl() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public Type getFetchedType() {
			return OSLCResource.class;
		}

		@Override
		public List<OSLCResource> performAction(FlexoBehaviourAction action) {

			if (getModelSlotInstance(action) == null) {
				logger.warning("Could not access model slot instance. Abort.");
				return null;
			}
			if (getModelSlotInstance(action).getResourceData() == null) {
				logger.warning("Could not access model adressed by model slot instance. Abort.");
				return null;
			}

			OSLCResource cdlUnit = (OSLCResource) getModelSlotInstance(action).getAccessedResourceData();

			List<OSLCResource> selectedOSLCResources = new ArrayList<OSLCResource>();

			List<OSLCResource> returned = filterWithConditions(selectedOSLCResources, action);

			return returned;
		}
	}
}
