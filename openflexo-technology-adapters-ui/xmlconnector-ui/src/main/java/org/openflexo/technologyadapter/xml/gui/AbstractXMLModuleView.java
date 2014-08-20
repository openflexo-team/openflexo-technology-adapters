package org.openflexo.technologyadapter.xml.gui;

import org.openflexo.rm.Resource;
import org.openflexo.technologyadapter.xml.metamodel.XMLObject;
import org.openflexo.view.ModuleView;
import org.openflexo.view.SelectionSynchronizedFIBView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Abstract ModuleView to represent an XML resource, which can be in a federation context or in a free editing context. Inherit from
 * JScrollPane, to contain XML View. 
 * 
 */
public abstract class AbstractXMLModuleView<T extends XMLObject> extends SelectionSynchronizedFIBView implements ModuleView<T> {

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
	protected AbstractXMLModuleView(FlexoController controller, T representedObject, FlexoPerspective perspective, Resource fib_file) {
		super(null, controller, fib_file);
		this.controller = controller;
		this.representedObject = representedObject;
		this.perspective = perspective;
		setDataObject(representedObject);
	}



	@Override
	public T getRepresentedObject() {
		return representedObject;
	}

	/**
	 * Remove ModuleView from controller. 
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
		// TODO
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
		// TODO
	}
	
	@Override
	public boolean isAutoscrolled() {
		return true;
	}
}
