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
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirement;

@FIBPanel("Fib/SelectOSLCRequirementPanel.fib")
@ModelEntity
@ImplementationClass(SelectOSLCRequirement.SelectOSLCRequirementImpl.class)
@XMLElement
public interface SelectOSLCRequirement extends FetchRequest<OSLCCoreModelSlot, OSLCRequirement> {

	public static abstract class SelectOSLCRequirementImpl extends FetchRequestImpl<OSLCCoreModelSlot, OSLCRequirement> implements
			SelectOSLCRequirement {

		private static final Logger logger = Logger.getLogger(SelectOSLCRequirement.class.getPackage().getName());

		public SelectOSLCRequirementImpl() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public Type getFetchedType() {
			return OSLCRequirement.class;
		}

		@Override
		public List<OSLCRequirement> performAction(FlexoBehaviourAction action) {

			if (getModelSlotInstance(action) == null) {
				logger.warning("Could not access model slot instance. Abort.");
				return null;
			}
			if (getModelSlotInstance(action).getResourceData() == null) {
				logger.warning("Could not access model adressed by model slot instance. Abort.");
				return null;
			}

			OSLCResource cdlUnit = (OSLCResource) getModelSlotInstance(action).getAccessedResourceData();

			List<OSLCRequirement> selectedOSLCRequirements = new ArrayList<OSLCRequirement>();

			List<OSLCRequirement> returned = filterWithConditions(selectedOSLCRequirements, action);

			return returned;
		}
	}
}
