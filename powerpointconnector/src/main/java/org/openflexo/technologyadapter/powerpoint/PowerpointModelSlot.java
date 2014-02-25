package org.openflexo.technologyadapter.powerpoint;

import java.util.logging.Logger;

import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hslf.model.Slide;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.viewpoint.FlexoRole;
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
	}
}
