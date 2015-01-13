package org.openflexo.technologyadapter.powerpoint;

import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hslf.model.Slide;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.rt.ModelSlotInstance;
import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.model.annotations.Implementation;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;

@ModelEntity
public interface PowerpointModelSlot extends ModelSlot<PowerpointSlideshow> {

	@Implementation
	public abstract class PowerpointModelSlotImpl implements PowerpointModelSlot {

		private static final Logger logger = Logger.getLogger(PowerpointModelSlot.class.getPackage().getName());

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (Slide.class.isAssignableFrom(patternRoleClass)) {
				return "slide";
			} else if (AutoShape.class.isAssignableFrom(patternRoleClass)) {
				return "shape";
			}
			logger.warning("Unexpected pattern role: " + patternRoleClass.getName());
			return null;
		}

		@Override
		public String getPreciseType() {
			return "Powerpoint Slide Show";
		};

		@Override
		public Type getType() {
			return PowerpointSlideshow.class;
		}

		@Override
		public String getURIForObject(ModelSlotInstance<? extends ModelSlot<PowerpointSlideshow>, PowerpointSlideshow> msInstance, Object o) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object retrieveObjectWithURI(ModelSlotInstance<? extends ModelSlot<PowerpointSlideshow>, PowerpointSlideshow> msInstance,
				String objectURI) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ModelSlotInstanceConfiguration<? extends ModelSlot<PowerpointSlideshow>, PowerpointSlideshow> createConfiguration(
				CreateVirtualModelInstance action) {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
