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
import org.openflexo.technologyadapter.oslc.OSLCRMModelSlot;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirementCollection;

@FIBPanel("Fib/AddCDLActivityPanel.fib")
@ModelEntity
@ImplementationClass(AddOSLCRequirementCollection.AddOSLCRequirementCollectionImpl.class)
@XMLElement
public interface AddOSLCRequirementCollection extends AssignableAction<OSLCRMModelSlot, OSLCRequirementCollection> {

	public static abstract class AddOSLCRequirementCollectionImpl extends AssignableActionImpl<OSLCRMModelSlot, OSLCRequirementCollection>
			implements AddOSLCRequirementCollection {

		private static final Logger logger = Logger.getLogger(AddOSLCRequirementCollection.class.getPackage().getName());

		public AddOSLCRequirementCollectionImpl() {
			super();
		}

		@Override
		public Type getAssignableType() {
			return OSLCRequirementCollection.class;
		}

		@Override
		public OSLCRequirementCollection performAction(FlexoBehaviourAction action) {

			OSLCRequirementCollection cdlActivity = null;

			FreeModelSlotInstance<OSLCResource, OSLCRMModelSlot> modelSlotInstance = getModelSlotInstance(action);
			if (modelSlotInstance.getResourceData() != null) {

			}
			else {
				logger.warning("Model slot not correctly initialised : model is null");
				return null;
			}

			return cdlActivity;
		}

		@Override
		public FreeModelSlotInstance<OSLCResource, OSLCRMModelSlot> getModelSlotInstance(FlexoBehaviourAction action) {
			return (FreeModelSlotInstance<OSLCResource, OSLCRMModelSlot>) super.getModelSlotInstance(action);
		}

	}
}
