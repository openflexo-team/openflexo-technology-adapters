package org.openflexo.technologyadapter.freeplane.view;

import org.freeplane.features.mode.Controller;
import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.technologyadapter.freeplane.fml.FMLControlledFreeplaneVirtualModelInstanceNature;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class FMLControlledFreeplaneModuleView extends AbstractFreeplaneModuleView<VirtualModelInstance> {

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 814998081156747502L;

	public FMLControlledFreeplaneModuleView(final FlexoController controller, final VirtualModelInstance vmi,
			final FlexoPerspective perspective) {
		super(controller, vmi, perspective);
		final IFreeplaneMap map = FMLControlledFreeplaneVirtualModelInstanceNature.getMap(this.representedObject);
		// Things could haven't initialized
		FreeplaneBasicAdapter.getInstance();
		Controller.getCurrentController().getMapViewManager().newMapView(map.getMapModel(), Controller.getCurrentModeController());
		this.setViewportView(FreeplaneBasicAdapter.getInstance().getMapView());
	}

}
