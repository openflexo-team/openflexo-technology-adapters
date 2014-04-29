package org.openflexo.technologyadapter.diagram;

import java.util.logging.Logger;

import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.model.annotations.Implementation;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.DiagramRole;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.Diagram;

/**
 * Implemented by all ModelSlot class for the Openflexo built-in diagram technology adapter<br>
 * 
 * @author sylvain
 * 
 */

@ModelEntity(isAbstract = true)
public interface DiagramModelSlot extends ModelSlot<Diagram> {

	@Implementation
	public abstract class DiagramModelSlotImpl implements DiagramModelSlot {

		private static final Logger logger = Logger.getLogger(DiagramModelSlot.class.getPackage().getName());

		@Override
		public <PR extends FlexoRole<?>> String defaultFlexoRoleName(Class<PR> patternRoleClass) {
			if (DiagramRole.class.isAssignableFrom(patternRoleClass)) {
				return "diagram";
			} else if (ShapeRole.class.isAssignableFrom(patternRoleClass)) {
				return "shape";
			} else if (ConnectorRole.class.isAssignableFrom(patternRoleClass)) {
				return "connector";
			}
			logger.warning("Unexpected pattern role: " + patternRoleClass.getName());
			return null;
		}
		
	}
}
