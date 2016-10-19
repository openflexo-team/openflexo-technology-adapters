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

import java.awt.Component;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.openflexo.fge.swing.view.JDrawingView;
import org.openflexo.fml.controller.FMLFIBController;
import org.openflexo.foundation.fml.AbstractVirtualModel;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.view.GinaViewFactory;
import org.openflexo.gina.view.widget.FIBCustomWidget;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.AbstractDiagramPalette;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelNature;
import org.openflexo.technologyadapter.diagram.gui.widget.DiagramEditorComponent;
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
	private FMLControlledDiagramVirtualModelView moduleView;

	public FMLControlledDiagramFMLFIBController(FIBComponent component, GinaViewFactory<?> viewFactory) {
		super(component, viewFactory);
	}

	public DiagramSpecification getDiagramSpecification(AbstractVirtualModel<?> virtualModel) {
		TypedDiagramModelSlot typedDiagramModelSlot = FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(virtualModel);
		if (typedDiagramModelSlot != null) {
			return typedDiagramModelSlot.getDiagramSpecification();
		}
		return null;
	}

	public Diagram getSelectedDiagram() {
		return selectedDiagram;
	}

	public void setSelectedDiagram(Diagram selectedDiagram) {

		System.out.println("Hop, on selectionne le diagramme: " + selectedDiagram);

		if ((selectedDiagram == null && this.selectedDiagram != null)
				|| (selectedDiagram != null && !selectedDiagram.equals(this.selectedDiagram))) {
			Diagram oldValue = this.selectedDiagram;

			System.out.println("On editait le diagramme " + oldValue);
			System.out.println("DiagramEditor=" + getDiagramEditorComponent().getDiagramEditor());

			this.selectedDiagram = selectedDiagram;
			// getPropertyChangeSupport().firePropertyChange("selectedDiagram", oldValue, selectedDiagram);

			System.out.println("On edite maintenant le diagramme " + selectedDiagram);
			System.out.println("DiagramEditor=" + getDiagramEditorComponent().getDiagramEditor());

			System.out.println("PaletteView was: " + getModuleView().getPerspective().getMiddleRightView());
			if (getModuleView().getPerspective().getMiddleRightView() instanceof JTabbedPane) {
				JTabbedPane tabbedPane = (JTabbedPane) getModuleView().getPerspective().getMiddleRightView();
				for (int i = 0; i < tabbedPane.getComponentCount(); i++) {
					Component tab = tabbedPane.getComponentAt(i);
					if (tab instanceof JScrollPane) {
						Component c = ((JScrollPane) tab).getViewport().getView();
						System.out.println(" >> " + c + " of " + (c != null ? c.getClass() : "<null>"));
						if (c instanceof JDrawingView) {
							JDrawingView dv = (JDrawingView) c;
							if (dv.getDrawable() instanceof AbstractDiagramPalette) {
								AbstractDiagramPalette palette = (AbstractDiagramPalette) dv.getDrawable();
								System.out.println("palette: " + palette + " editor=" + palette.getEditor());
							}
						}
					}
				}
			}

			if (selectedDiagram != null && getModuleView() != null && getDiagramEditorComponent() != null) {
				// getModuleView().getPerspective().setTopRightView(new JLabel("TopRight"));
				// getModuleView().getPerspective().setMiddleRightView(new JLabel("MiddleRight"));
				System.out.println("perspective=" + getModuleView().getPerspective());
				System.out.println("diagramEditorWidget=" + getDiagramEditorWidget());
				System.out.println("getDiagramEditorComponent=" + getDiagramEditorComponent());
				System.out.println("getDiagramTechnologyAdapterController=" + getDiagramTechnologyAdapterController());

				getDiagramEditorComponent().setEditedObject(selectedDiagram);

				/*if (oldValue != null) {
					getModuleView().getPerspective().setMiddleRightView(new JLabel("Un autre diagramme"));
				}
				else {*/
				// getModuleView().getPerspective().setMiddleRightView(new JLabel("Premier diagramme"));
				getModuleView().getPerspective().setMiddleRightView(getDiagramEditorComponent().getDiagramEditor().getPaletteView());
				// }
				// getDiagramEditorComponent().getDiagramEditor().getPaletteView().setPreferredSize(new Dimension(200, 400));
				System.out.println("PaletteView is now: " + getModuleView().getPerspective().getMiddleRightView());
				if (getModuleView().getPerspective().getMiddleRightView() instanceof JTabbedPane) {
					JTabbedPane tabbedPane = (JTabbedPane) getModuleView().getPerspective().getMiddleRightView();
					for (int i = 0; i < tabbedPane.getComponentCount(); i++) {
						Component tab = tabbedPane.getComponentAt(i);
						if (tab instanceof JScrollPane) {
							Component c = ((JScrollPane) tab).getViewport().getView();
							System.out.println(" >> " + c + " of " + (c != null ? c.getClass() : "<null>"));
							if (c instanceof JDrawingView) {
								JDrawingView dv = (JDrawingView) c;
								if (dv.getDrawable() instanceof AbstractDiagramPalette) {
									AbstractDiagramPalette palette = (AbstractDiagramPalette) dv.getDrawable();
									System.out.println("palette: " + palette + " editor=" + palette.getEditor());
								}
							}
						}
					}
				}

				getDiagramEditorComponent().getDiagramEditor().getCommonPalette()
						.attachToEditor(getDiagramEditorComponent().getDiagramEditor());

				getModuleView().getPerspective()
						.setBottomRightView(getDiagramTechnologyAdapterController().getInspectors().getPanelGroup());
				getDiagramTechnologyAdapterController().getInspectors().attachToEditor(getDiagramEditorComponent().getDiagramEditor());
				System.out.println("donc:");
				System.out.println("top right: " + getModuleView().getPerspective().getTopRightView());
				System.out.println("middle right: " + getModuleView().getPerspective().getMiddleRightView());
				System.out.println("bottom right: " + getModuleView().getPerspective().getBottomRightView());
				getModuleView().revalidate();
				getModuleView().repaint();
			}

		}

		// Sets palette view of editor to be the top right view
		// perspective.setTopRightView(previewComponent);

		// getDiagramTechnologyAdapterController(controller).getInspectors().attachToEditor(previewComponent.getPreviewController());
		// getDiagramTechnologyAdapterController(controller).getDialogInspectors().attachToEditor(previewComponent.getPreviewController());

		// perspective.setBottomRightView(getDiagramTechnologyAdapterController(controller).getInspectors().getPanelGroup());

		// controller.getControllerModel().setRightViewVisible(true);

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

	public DiagramEditorComponent getDiagramEditorComponent() {
		FIBCustomWidget<?, DiagramEditorComponent, Diagram> diagramEditorWidget = getDiagramEditorWidget();
		if (diagramEditorWidget != null) {
			return diagramEditorWidget.getCustomComponent();
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
		else {
			super.doubleClick(object);
		}
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
