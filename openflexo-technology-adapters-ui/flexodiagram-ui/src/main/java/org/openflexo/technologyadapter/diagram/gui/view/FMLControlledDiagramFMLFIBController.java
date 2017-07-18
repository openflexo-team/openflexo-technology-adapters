/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Flexo-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.diagram.gui.view;

import java.util.logging.Logger;

import org.openflexo.fml.controller.FMLFIBController;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.view.GinaViewFactory;
import org.openflexo.gina.view.widget.FIBCustomWidget;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.fml.action.CreateDiagramPalette;
import org.openflexo.technologyadapter.diagram.fml.action.CreateExampleDiagram;
import org.openflexo.technologyadapter.diagram.gui.widget.DiagramEditorComponent;
import org.openflexo.technologyadapter.diagram.gui.widget.PaletteEditorComponent;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.view.controller.TechnologyAdapterControllerService;

/**
 * Represents the controller of a FIBInspector (FIBComponent) in the context of FML
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramFMLFIBController extends FMLFIBController {

	private static final Logger logger = FlexoLogger.getLogger(FMLControlledDiagramFMLFIBController.class.getPackage().getName());

	private Diagram selectedDiagram;
	private DiagramPalette selectedPalette;
	private FMLControlledDiagramVirtualModelView moduleView;

	public FMLControlledDiagramFMLFIBController(FIBComponent component, GinaViewFactory<?> viewFactory) {
		super(component, viewFactory);
	}

	public DiagramSpecification getDiagramSpecification(VirtualModel virtualModel) {
		TypedDiagramModelSlot typedDiagramModelSlot = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(virtualModel);
		if (typedDiagramModelSlot != null) {
			return typedDiagramModelSlot.getDiagramSpecification();
		}
		return null;
	}

	/**
	 * Return selected example diagram, null if no diagram selected (or a palette for example)
	 * 
	 * @return
	 */
	public Diagram getSelectedDiagram() {
		return selectedDiagram;
	}

	/**
	 * Called to select a new example diagram
	 * 
	 * @param selectedDiagram
	 */
	public void setSelectedDiagram(Diagram selectedDiagram) {

		if ((selectedDiagram == null && this.selectedDiagram != null)
				|| (selectedDiagram != null && !selectedDiagram.equals(this.selectedDiagram))) {
			boolean notifySelectedPaletteChange = false;
			if (selectedDiagram != null && selectedPalette != null) {
				selectedPalette = null;
				notifySelectedPaletteChange = true;
			}
			Diagram oldDiagram = this.selectedDiagram;
			this.selectedDiagram = selectedDiagram;
			getPropertyChangeSupport().firePropertyChange("selectedDiagram", oldDiagram, selectedDiagram);
			if (notifySelectedPaletteChange) {
				getPropertyChangeSupport().firePropertyChange("selectedPalette", true, false);
			}
			updateModuleViewTooling();
		}
	}

	/**
	 * Return selected palette diagram
	 * 
	 * @return
	 */
	public DiagramPalette getSelectedPalette() {
		return selectedPalette;
	}

	/**
	 * Called to select a new palette diagram
	 * 
	 * @param selectedDiagram
	 */
	public void setSelectedPalette(DiagramPalette selectedPalette) {

		if ((selectedPalette == null && this.selectedPalette != null)
				|| (selectedPalette != null && !selectedPalette.equals(this.selectedPalette))) {

			boolean notifySelectedDiagramChange = false;
			if (selectedPalette != null && selectedDiagram != null) {
				selectedDiagram = null;
				notifySelectedDiagramChange = true;
			}
			DiagramPalette oldPalette = this.selectedPalette;
			this.selectedPalette = selectedPalette;
			getPropertyChangeSupport().firePropertyChange("selectedPalette", oldPalette, selectedPalette);
			if (notifySelectedDiagramChange) {
				getPropertyChangeSupport().firePropertyChange("selectedDiagram", true, false);
			}
			updateModuleViewTooling();

		}
	}

	/**
	 * Called to update module view tooling (when an example diagram or a palette is selected)
	 */
	protected void updateModuleViewTooling() {

		System.out.println("updateModuleViewTooling()");
		System.out.println("selectedDiagram=" + selectedDiagram);
		System.out.println("getDiagramEditorComponent()=" + getDiagramEditorComponent());

		if (selectedDiagram != null && getModuleView() != null && getDiagramEditorComponent() != null) {

			// We "tell" diagram editor component that the diagram has changed
			// This component will then display the "right" diagram
			getDiagramEditorComponent().setFlexoController(getFlexoController());
			getDiagramEditorComponent().setEditedObject(selectedDiagram);

			System.out.println("getDiagramEditorComponent().getDiagramEditor()=" + getDiagramEditorComponent().getDiagramEditor());
			getModuleView().getPerspective().setTopRightView(getDiagramEditorComponent().getDiagramEditor().getPaletteView());

			// getDiagramEditorComponent().getDiagramEditor().getCommonPalette()
			// .attachToEditor(getDiagramEditorComponent().getDiagramEditor());

			// We also set inspectors, and attach them to new new editor
			getModuleView().getPerspective().setBottomRightView(getDiagramTechnologyAdapterController().getInspectors().getPanelGroup());
			getDiagramTechnologyAdapterController().getInspectors().attachToEditor(getDiagramEditorComponent().getDiagramEditor());

			getModuleView().revalidate();
			getModuleView().repaint();
		}

		if (selectedPalette != null && getModuleView() != null && getPaletteEditorComponent() != null) {

			// We "tell" diagram editor component that the diagram has changed
			// This component will then display the "right" diagram
			getPaletteEditorComponent().setFlexoController(getFlexoController());
			getPaletteEditorComponent().setEditedObject(selectedPalette);

			// We set new palette view
			getModuleView().getPerspective().setTopRightView(getPaletteEditorComponent().getPaletteEditor().getPaletteView());

			// getDiagramEditorComponent().getDiagramEditor().getCommonPalette()
			// .attachToEditor(getDiagramEditorComponent().getDiagramEditor());

			// We also set inspectors, and attach them to new new editor
			getModuleView().getPerspective().setBottomRightView(getDiagramTechnologyAdapterController().getInspectors().getPanelGroup());
			getDiagramTechnologyAdapterController().getInspectors().attachToEditor(getPaletteEditorComponent().getPaletteEditor());

			getModuleView().revalidate();
			getModuleView().repaint();
		}
	}

	public DiagramTechnologyAdapterController getDiagramTechnologyAdapterController() {
		if (getFlexoController() != null) {
			TechnologyAdapterControllerService tacService = getFlexoController().getApplicationContext()
					.getTechnologyAdapterControllerService();
			return tacService.getTechnologyAdapterController(DiagramTechnologyAdapterController.class);
		}
		return null;
	}

	public FIBCustomWidget<?, DiagramEditorComponent, Diagram> getDiagramEditorWidget() {
		return (FIBCustomWidget<?, DiagramEditorComponent, Diagram>) viewForComponent("DiagramEditorComponent");
	}

	public FIBCustomWidget<?, PaletteEditorComponent, DiagramPalette> getPaletteEditorWidget() {
		return (FIBCustomWidget<?, PaletteEditorComponent, DiagramPalette>) viewForComponent("PaletteEditorComponent");
	}

	public DiagramEditorComponent getDiagramEditorComponent() {
		FIBCustomWidget<?, DiagramEditorComponent, Diagram> diagramEditorWidget = getDiagramEditorWidget();
		if (diagramEditorWidget != null) {
			return diagramEditorWidget.getCustomComponent();
		}
		return null;
	}

	public PaletteEditorComponent getPaletteEditorComponent() {
		FIBCustomWidget<?, PaletteEditorComponent, DiagramPalette> paletteEditorWidget = getPaletteEditorWidget();
		if (paletteEditorWidget != null) {
			return paletteEditorWidget.getCustomComponent();
		}
		return null;
	}

	public FMLControlledDiagramVirtualModelView getModuleView() {
		return moduleView;
	}

	public void setModuleView(FMLControlledDiagramVirtualModelView moduleView) {
		this.moduleView = moduleView;
	}

	@Override
	public void singleClick(Object object) {
		System.out.println("singleClick with " + object);
		super.singleClick(object);
	}

	@Override
	public void doubleClick(Object object) {
		System.out.println("doubleClick with " + object);
		if (object instanceof Diagram) {
			setSelectedDiagram((Diagram) object);
		}
		else if (object instanceof DiagramPalette) {
			setSelectedPalette((DiagramPalette) object);
		}
		else {
			super.doubleClick(object);
		}
	}

	public void createNewExampleDiagram(DiagramSpecification diagramSpecification) {
		CreateExampleDiagram action = CreateExampleDiagram.actionType.makeNewAction(diagramSpecification, null, getEditor());
		action.doAction();
	}

	public void createNewPalette(DiagramSpecification diagramSpecification) {
		CreateDiagramPalette action = CreateDiagramPalette.actionType.makeNewAction(diagramSpecification, null, getEditor());
		action.doAction();
	}

	/*public void addCustomProperty(FlexoObject object) {
		if (object instanceof InnerResourceData) {
			System.out.println("Creating property for object " + object);
			AddFlexoProperty action = AddFlexoProperty.actionType.makeNewAction(object, null, getEditor());
			action.doAction();
		}
	}
	
	public void removeCustomProperty(FlexoProperty property) {
		System.out.println("Deleting property " + property + " for object " + property.getOwner());
		property.delete();
	}*/

}
