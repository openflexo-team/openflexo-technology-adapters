package org.openflexo.technologyadapter.oslc.virtualmodel.action;

import java.lang.reflect.Type;
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
import org.openflexo.technologyadapter.oslc.model.rm.OSLCRequirementCollection;

@FIBPanel("Fib/SelectOSLCRequirementCollectionPanel.fib")
@ModelEntity
@ImplementationClass(SelectOSLCRequirementCollection.SelectOSLCRequirementCollectionImpl.class)
@XMLElement
public interface SelectOSLCRequirementCollection extends FetchRequest<OSLCCoreModelSlot, OSLCRequirementCollection> {

	public static abstract class SelectOSLCRequirementCollectionImpl extends FetchRequestImpl<OSLCCoreModelSlot, OSLCRequirementCollection>
			implements SelectOSLCRequirementCollection {

		private static final Logger logger = Logger.getLogger(SelectOSLCRequirementCollection.class.getPackage().getName());

		public SelectOSLCRequirementCollectionImpl() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public Type getFetchedType() {
			return OSLCRequirementCollection.class;
		}

		@Override
		public List<OSLCRequirementCollection> performAction(FlexoBehaviourAction action) {

			if (getModelSlotInstance(action) == null) {
				logger.warning("Could not access model slot instance. Abort.");
				return null;
			}
			if (getModelSlotInstance(action).getResourceData() == null) {
				logger.warning("Could not access model adressed by model slot instance. Abort.");
				return null;
			}

			OSLCResource cdlUnit = (OSLCResource) getModelSlotInstance(action).getAccessedResourceData();

			List<OSLCRequirementCollection> returned = filterWithConditions(null, action);

			return returned;
		}
	}
}
