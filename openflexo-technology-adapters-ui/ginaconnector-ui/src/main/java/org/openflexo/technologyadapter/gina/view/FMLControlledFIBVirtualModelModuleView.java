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

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.openflexo.foundation.fml.AbstractVirtualModel;
import org.openflexo.gina.swing.editor.FIBEditor;
import org.openflexo.gina.swing.editor.controller.FIBEditorController;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.controller.GINAAdapterController;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelNature;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * A {@link ModuleView} suitable for {@link AbstractVirtualModel} that have the {@link FMLControlledFIBVirtualModelInstanceNature}<br>
 * Display an editable FIB view in {@link FIBEditor}
 * 
 * @author sylvain
 *
 */
@SuppressWarnings("serial")
public class FMLControlledFIBVirtualModelModuleView extends JPanel implements ModuleView<AbstractVirtualModel<?>> {

	private final FlexoController controller;
	private final AbstractVirtualModel<?> representedObject;
	private final FlexoPerspective perspective;
	private FIBEditorController editorController;
	private GINAFIBComponent component;
	private FIBComponentModelSlot modelSlot;

	/**
	 * Initialize needed attribute. All are final.
	 *
	 * @param controller
	 *            The flexo controller
	 * @param representedObject
	 *            GINModel object that will be represented
	 * @param perspective
	 */
	public FMLControlledFIBVirtualModelModuleView(AbstractVirtualModel<?> representedObject, FlexoController controller,
			FlexoPerspective perspective) {
		super(new BorderLayout());
		this.controller = controller;
		this.representedObject = representedObject;
		this.perspective = perspective;
		modelSlot = FMLControlledFIBVirtualModelNature.getFIBComponentModelSlot(representedObject);
		component = FMLControlledFIBVirtualModelNature.getFIBComponent(representedObject);

		component.bindTo(representedObject, modelSlot);

		// TODO: if we set flag to true, a dead-lock happen here.
		// Please investigate to find an elegant solution
		editorController = getFIBEditor(false).openFIBComponent(
				component.getResource().getIODelegate().getSerializationArtefactAsResource(), component.getComponent(), null,
				controller.getFlexoFrame());

		add(editorController.getEditorPanel(), BorderLayout.CENTER);
	}

	public GINAAdapterController getAdapterController() {
		GINATechnologyAdapter ta = controller.getApplicationContext().getTechnologyAdapterService()
				.getTechnologyAdapter(GINATechnologyAdapter.class);
		return controller.getApplicationContext().getTechnologyAdapterControllerService().getTechnologyAdapterController(ta);
	}

	public FIBEditor getFIBEditor(boolean launchInTask) {
		if (getAdapterController() != null) {
			return getAdapterController().getFIBEditor(launchInTask);
		}
		return null;
	}

	@Override
	public void show(FlexoController flexoController, FlexoPerspective flexoPerspective) {

		component.bindTo(representedObject, modelSlot);

		// Sets palette view of editor to be the top right view
		flexoPerspective.setTopRightView(getFIBEditor(false).getPalettes());
		flexoPerspective.setBottomLeftView(editorController.getEditorBrowser());

		getFIBEditor(false).activate(editorController);

		perspective.setBottomRightView(getFIBEditor(false).getInspectors().getPanelGroup());

		controller.getControllerModel().setRightViewVisible(true);

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

	@Override
	public void willHide() {
		setVisible(false);
	}

	@Override
	public void willShow() {
		setVisible(true);
	}

	@Override
	public AbstractVirtualModel<?> getRepresentedObject() {
		return representedObject;
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
