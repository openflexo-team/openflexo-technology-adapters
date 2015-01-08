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

@FIBPanel("Fib/AddOSLCResourcePanel.fib")
@ModelEntity
@ImplementationClass(AddOSLCResource.AddOSLCResourceImpl.class)
@XMLElement
public interface AddOSLCResource extends AssignableAction<OSLCCoreModelSlot, OSLCResource> {

	public static abstract class AddOSLCResourceImpl extends AssignableActionImpl<OSLCCoreModelSlot, OSLCResource> implements
			AddOSLCResource {

		private static final Logger logger = Logger.getLogger(AddOSLCResource.class.getPackage().getName());

		public AddOSLCResourceImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return OSLCResource.class;
		}

		@Override
		public OSLCResource performAction(FlexoBehaviourAction action) {

			OSLCResource cdlActivity = null;

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
