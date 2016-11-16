/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.technologyadapter.diagram.controller.diagrameditor;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterControllerService;
import org.openflexo.view.controller.model.FlexoPerspective;

@SuppressWarnings("serial")
public class FMLControlledDiagramModuleView extends JPanel implements ModuleView<VirtualModelInstance>, PropertyChangeListener {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(FMLControlledDiagramModuleView.class.getPackage().getName());

	private final FMLControlledDiagramEditor editor;
	private final FlexoPerspective perspective;

	private final JPanel bottomPanel;

	public FMLControlledDiagramModuleView(FMLControlledDiagramEditor editor, FlexoPerspective perspective) {
		super();
		setLayout(new BorderLayout());
		this.editor = editor;
		this.perspective = perspective;
		add(editor.getToolsPanel(), BorderLayout.NORTH);
		add(new JScrollPane(editor.getDrawingView()), BorderLayout.CENTER);

		bottomPanel = new JPanel(new BorderLayout());

		bottomPanel.add(editor.getFlexoController().makeInfoLabel(), BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		editor.getFlexoController().setInfoMessage("Controlled diagramming - CTRL-drag to draw edges", false);

		validate();

		getRepresentedObject().getPropertyChangeSupport().addPropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
	}

	public FMLControlledDiagramEditor getEditor() {
		return editor;
	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}

	@Override
	public void deleteModuleView() {
		getRepresentedObject().getPropertyChangeSupport().removePropertyChangeListener(getRepresentedObject().getDeletedProperty(), this);
		getEditor().getFlexoController().removeModuleView(this);
		getEditor().delete();
	}

	@Override
	public VirtualModelInstance getRepresentedObject() {
		return editor.getVirtualModelInstance();
	}

	@Override
	public boolean isAutoscrolled() {
		return true;
	}

	@Override
	public void willHide() {

		System.out.println("FMLControlledDiagramModuleView WILL HIDE !!!!!!");

		getEditor().getFlexoController().getEditingContext().unregisterPasteHandler(getEditor().getPasteHandler());

		bottomPanel.remove(getDiagramTechnologyAdapterController(getEditor().getFlexoController()).getScaleSelector().getComponent());

	}

	@Override
	public void willShow() {

		System.out.println("FMLControlledDiagramModuleView WILL SHOW !!!!!!");

		// Sets palette view of editor to be the top right view
		perspective.setTopRightView(getEditor().getPaletteView());
		// perspective.setHeader(((FreeDiagramModuleView) moduleView).getEditor().getS());

		DiagramTechnologyAdapterController diagramTAController = getDiagramTechnologyAdapterController(getEditor().getFlexoController());

		diagramTAController.getInspectors().attachToEditor(getEditor());
		diagramTAController.getDialogInspectors().attachToEditor(getEditor());
		diagramTAController.getScaleSelector().attachToEditor(getEditor());

		perspective.setBottomRightView(
				getDiagramTechnologyAdapterController(getEditor().getFlexoController()).getInspectors().getPanelGroup());

		/*SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Force right view to be visible
				controller.getControllerModel().setRightViewVisible(true);
			}
		});*/

		getEditor().getFlexoController().getControllerModel().setRightViewVisible(true);

		getEditor().getFlexoController().getEditingContext().registerPasteHandler(getEditor().getPasteHandler());

		bottomPanel.add(getDiagramTechnologyAdapterController(getEditor().getFlexoController()).getScaleSelector().getComponent(),
				BorderLayout.EAST);

		getPerspective().focusOnObject(getRepresentedObject());
	}

	public DiagramTechnologyAdapterController getDiagramTechnologyAdapterController(FlexoController controller) {
		TechnologyAdapterControllerService tacService = controller.getApplicationContext().getTechnologyAdapterControllerService();
		return tacService.getTechnologyAdapterController(DiagramTechnologyAdapterController.class);
	}

	@Override
	public void show(final FlexoController controller, FlexoPerspective perspective) {

		// Sets palette view of editor to be the top right view
		/*perspective.setTopRightView(getEditor().getPaletteView());
		// perspective.setHeader(((FreeDiagramModuleView) moduleView).getEditor().getS());
		
		getDiagramTechnologyAdapterController(controller).getInspectors().attachToEditor(getEditor());
		getDiagramTechnologyAdapterController(controller).getDialogInspectors().attachToEditor(getEditor());
		getDiagramTechnologyAdapterController(controller).getScaleSelector().attachToEditor(getEditor());
		
		perspective.setBottomRightView(getDiagramTechnologyAdapterController(controller).getInspectors().getPanelGroup());
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Force right view to be visible
				controller.getControllerModel().setRightViewVisible(true);
			}
		});
		
		controller.getControllerModel().setRightViewVisible(true);*/
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == getRepresentedObject() && evt.getPropertyName().equals(getRepresentedObject().getDeletedProperty())) {
			deleteModuleView();
		}
	}

}
