package org.openflexo.technologyadapter.freeplane.view;

import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class FreeplaneModuleView extends AbstractFreeplaneModuleView<IFreeplaneMap> {

	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = 8443361431050803298L;

	public FreeplaneModuleView(final IFreeplaneMap map, final FlexoController controller, final FlexoPerspective perspective) {
		super(controller, map, perspective, FreeplaneBasicAdapter.getInstance().getMapView());
	}

}
