/*
 * (c) Copyright 2013- Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.technologyadapter.gina.view;

import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.gina.swing.editor.controller.FIBEditorController;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.controller.GINAAdapterController;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.view.FIBModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

@SuppressWarnings("serial")
public class FMLControlledFIBVirtualModelInstanceModuleView extends FIBModuleView<VirtualModelInstance> {

	private final FlexoPerspective perspective;
	private FIBEditorController editorController;
	private GINAFIBComponent component;
	private FreeModelSlotInstance<GINAFIBComponent, FIBComponentModelSlot> modelSlotInstance;

	public FMLControlledFIBVirtualModelInstanceModuleView(VirtualModelInstance representedObject, FlexoController controller,
			FlexoPerspective perspective) {
		super(representedObject, controller,
				FMLControlledFIBVirtualModelInstanceNature.getGINAFIBComponent(representedObject).getComponent(), true);
		this.perspective = perspective;
		modelSlotInstance = FMLControlledFIBVirtualModelInstanceNature.getModelSlotInstance(representedObject);
		component = FMLControlledFIBVirtualModelInstanceNature.getGINAFIBComponent(representedObject);
	}

	@Override
	public void initializeFIBComponent() {
		super.initializeFIBComponent();
		component.bindTo(getRepresentedObject().getVirtualModel(), modelSlotInstance.getModelSlot());
	}

	public GINAAdapterController getAdapterController() {
		GINATechnologyAdapter ta = getFlexoController().getApplicationContext().getTechnologyAdapterService()
				.getTechnologyAdapter(GINATechnologyAdapter.class);
		return getFlexoController().getApplicationContext().getTechnologyAdapterControllerService().getTechnologyAdapterController(ta);
	}

	@Override
	public void show(FlexoController flexoController, FlexoPerspective flexoPerspective) {

		// If you want to add right and left panels to your module view, do it here. Un comment following code with your component.
		// SwingUtilities.invokeLater(new Runnable() {
		// @Override
		// public void run() {
		// perspective.setTopRightView(customJComponent);
		// controller.getControllerModel().setRightViewVisible(true);
		// }
		// });

		// Sets palette view of editor to be the top right view

		// getDiagramTechnologyAdapterController(controller).getInspectors().attachToEditor(getEditor());
		// getDiagramTechnologyAdapterController(controller).getDialogInspectors().attachToEditor(getEditor());
		// getDiagramTechnologyAdapterController(controller).getScaleSelector().attachToEditor(getEditor());

		// perspective.setBottomRightView(getDiagramTechnologyAdapterController(controller).getInspectors().getPanelGroup());

		/*SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Force right view to be visible
				controller.getControllerModel().setRightViewVisible(true);
			}
		});*/

		// controller.getControllerModel().setRightViewVisible(true);

	}

	/**
	 * Remove ModuleView from controller.
	 */
	@Override
	public void deleteModuleView() {
		getFlexoController().removeModuleView(this);
	}

	/**
	 * @return perspective given during construction of ModuleView.
	 */
	@Override
	public FlexoPerspective getPerspective() {
		return this.perspective;
	}

	/**
	 * Nothing done on this ModuleView
	 */
	@Override
	public void willShow() {
		// Nothing to implement by default, empty body
	}

	/**
	 * Nothing done on this ModuleView
	 */
	@Override
	public void willHide() {
		// Nothing to implement by default, empty body
	}

	public GINAFIBComponent getGINAFIBComponent() {
		return component;
	}

	@Override
	public boolean isAutoscrolled() {
		// If you want to handle scrollable by yourself instead of letting Openflexo doing it, change return to true.
		return false;
	}
}
