package org.openflexo.technologyadapter.freeplane.view;

import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

import javax.swing.*;

/**
 * Created by eloubout on 30/07/14.
 */
public abstract class AbstractFreeplaneModuleView<T extends FlexoObject> extends JScrollPane implements ModuleView<T> {

	protected final FlexoController controller;

	protected final T representedObject;

	protected final FlexoPerspective perspective;

	protected AbstractFreeplaneModuleView(FlexoController controller, T representedObject, FlexoPerspective perspective) {
		super();
		this.controller = controller;
		this.representedObject = representedObject;
		this.perspective = perspective;
	}

	public AbstractFreeplaneModuleView(FlexoController controller, T representedObject, FlexoPerspective perspective,
			JComponent viewportView) {
		this(controller, representedObject, perspective);
		this.setViewportView(viewportView);
	}

	@Override
	public T getRepresentedObject() {
		return representedObject;
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
	public void willShow() {
		this.displayIconToolBar(this.controller, this.perspective);
	}

	@Override
	public void willHide() {
		// Nothing to implement
	}

	@Override
	public void show(FlexoController flexoController, FlexoPerspective flexoPerspective) {
		this.displayIconToolBar(flexoController, flexoPerspective);
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
	public boolean isAutoscrolled() {
		return true;
	}
}
