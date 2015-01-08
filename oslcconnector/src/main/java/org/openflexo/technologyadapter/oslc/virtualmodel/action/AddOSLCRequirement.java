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
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirement;

@FIBPanel("Fib/AddOSLCRequirementPanel.fib")
@ModelEntity
@ImplementationClass(AddOSLCRequirement.AddOSLCRequirementImpl.class)
@XMLElement
public interface AddOSLCRequirement extends AssignableAction<OSLCCoreModelSlot, OSLCRequirement> {

	public static abstract class AddOSLCRequirementImpl extends AssignableActionImpl<OSLCCoreModelSlot, OSLCRequirement> implements
			AddOSLCRequirement {

		private static final Logger logger = Logger.getLogger(AddOSLCRequirement.class.getPackage().getName());

		public AddOSLCRequirementImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return OSLCRequirement.class;
		}

		@Override
		public OSLCRequirement performAction(FlexoBehaviourAction action) {

			OSLCRequirement cdlActivity = null;

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
