package org.openflexo.technologyadapter.freeplane.view;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.freeplane.features.mode.Controller;
import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.technologyadapter.freeplane.fml.FMLControlledFreeplaneVirtualModelInstanceNature;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class FMLControlledFreeplaneModuleView extends JScrollPane implements ModuleView<VirtualModelInstance> {

	/** Generated UID */
	private static final long serialVersionUID = 814998081156747502L;

	private final FlexoController controller;

	private final VirtualModelInstance vmi;

	private final FlexoPerspective perspective;

	public FMLControlledFreeplaneModuleView(final FlexoController controller, final VirtualModelInstance vmi,
			final FlexoPerspective perspective) {
		super();
		this.vmi = vmi;
		this.controller = controller;
		this.perspective = perspective;
		final IFreeplaneMap map = FMLControlledFreeplaneVirtualModelInstanceNature.getMap(this.vmi);
		// Things could haven't initialized
		FreeplaneBasicAdapter.getInstance();
		Controller.getCurrentController().getMapViewManager().newMapView(map.getMapModel(), Controller.getCurrentModeController());
		this.setViewportView(FreeplaneBasicAdapter.getInstance().getMapView());
	}

	@Override
	public void deleteModuleView() {
		this.controller.removeModuleView(this);
	}

	@Override
	public FlexoPerspective getPerspective() {
		return this.perspective;
	}

	@Override
	public VirtualModelInstance getRepresentedObject() {
		return this.vmi;
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}

	@Override
	public void show(final FlexoController controller, final FlexoPerspective perspective) {
		displayIconToolBar(controller, perspective);
	}

	private void displayIconToolBar(final FlexoController controller, final FlexoPerspective perspective) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				perspective.setTopRightView(FreeplaneBasicAdapter.getInstance().getIconToolbar());
				controller.getControllerModel().setRightViewVisible(true);
			}
		});
	}

	@Override
	public void willHide() {
		// Nothing to implement
	}

	@Override
	public void willShow() {
		displayIconToolBar(this.controller, this.perspective);
	}

}
