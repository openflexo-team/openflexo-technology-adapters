package org.openflexo.technologyadapter.freeplane.view;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Abstract ModuleView to represent a Freeplane map, which can be in a federation context or in a free editing context. Inherit from
 * JScrollPane, to contain Freeplane MapView component. Can be source of NPE because they have unchecked runtime dependencies to a docking
 * framework unused in here.
 */
public abstract class AbstractFreeplaneModuleView<T extends FlexoObject> extends JScrollPane implements ModuleView<T> {

	protected final FlexoController controller;

	protected final T representedObject;

	protected final FlexoPerspective perspective;

	/**
	 * Initialize needed attribute. All are final, no implicit call of super should be done.
	 *
	 * @param controller
	 * @param representedObject
	 * @param perspective
	 */
	protected AbstractFreeplaneModuleView(FlexoController controller, T representedObject, FlexoPerspective perspective) {
		super();
		this.controller = controller;
		this.representedObject = representedObject;
		this.perspective = perspective;
	}

	/**
	 * Initialize needed attribute. All are final, no implicit call of super should be done. Add a viewportView to ScrollPane.
	 *
	 * @param controller
	 * @param representedObject
	 * @param perspective
	 * @param viewportView
	 */
	public AbstractFreeplaneModuleView(FlexoController controller, T representedObject, FlexoPerspective perspective,
			JComponent viewportView) {
		this(controller, representedObject, perspective);
		this.setViewportView(viewportView);
	}

	@Override
	public T getRepresentedObject() {
		return representedObject;
	}

	/**
	 * Remove ModuleView from controller. A Freeplane action may be needed but nothing is done at the moment.
	 */
	@Override
	public void deleteModuleView() {
		this.controller.removeModuleView(this);
	}

	/**
	 * @return perspective given during construction of ModuleView.
	 */
	@Override
	public FlexoPerspective getPerspective() {
		return this.perspective;
	}

	/**
	 * Update right view to have Freeplane icon scrollbar.
	 */
	@Override
	public void willShow() {
		this.displayIconToolBar(this.controller, this.perspective);
	}

	/**
	 * Nothing done on this ModuleView
	 */
	@Override
	public void willHide() {
		// Nothing to implement
	}

	@Override
	public void show(FlexoController flexoController, FlexoPerspective flexoPerspective) {
		this.displayIconToolBar(flexoController, flexoPerspective);
	}

	/*
	* Needed Update on swap from FMLControlled to Free ModuleView, so refactor to call it in show and will show.
	*/
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
