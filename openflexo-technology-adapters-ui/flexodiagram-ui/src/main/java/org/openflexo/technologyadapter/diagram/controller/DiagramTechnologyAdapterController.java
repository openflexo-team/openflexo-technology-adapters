/*
 * (c) Copyright 2013-2014 Openflexo
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
package org.openflexo.technologyadapter.diagram.controller;

import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

import org.openflexo.fge.swing.control.SwingToolFactory;
import org.openflexo.fge.swing.control.tools.JDianaDialogInspectors;
import org.openflexo.fge.swing.control.tools.JDianaInspectors;
import org.openflexo.fge.swing.control.tools.JDianaScaleSelector;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.view.VirtualModelInstanceNature;
import org.openflexo.foundation.viewpoint.FlexoBehaviour;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.FlexoConceptInstanceRole;
import org.openflexo.foundation.viewpoint.FlexoConceptNature;
import org.openflexo.foundation.viewpoint.FlexoRole;
import org.openflexo.foundation.viewpoint.editionaction.DeleteAction;
import org.openflexo.foundation.viewpoint.editionaction.EditionAction;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.icon.VEIconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.model.undo.CompoundEdit;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.action.AddConnectorInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.AddDiagramPaletteElementInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.AddShapeInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateControlledDiagramVirtualModelInstanceInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramFromPPTSlideInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramPaletteInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramSpecificationInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateExampleDiagramFromPPTSlideInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateExampleDiagramInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElementInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateFMLDiagramPaletteElementBindingInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeclareConnectorInFlexoConceptInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeclareShapeInFlexoConceptInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramElementsInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramPaletteElementInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramPaletteInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramSpecificationInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteExampleDiagramElementsInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteExampleDiagramInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DiagramElementPasteHandler;
import org.openflexo.technologyadapter.diagram.controller.action.DropSchemeActionInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.ExportDiagramToImageInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.LinkSchemeActionInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.OpenControlledDiagramVirtualModelInstanceInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.PushToPaletteInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.ResetGraphicalRepresentationInitializer;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramModuleView;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramModuleView;
import org.openflexo.technologyadapter.diagram.controller.paletteeditor.DiagramPaletteEditor;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.DiagramNavigationScheme;
import org.openflexo.technologyadapter.diagram.fml.DiagramRole;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramFlexoConceptNature;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddConnector;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddDiagram;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.fml.editionaction.GraphicalAction;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.gui.view.DiagramFlexoConceptView;
import org.openflexo.technologyadapter.diagram.gui.view.DiagramSpecificationView;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.DeclareVirtualModelInstanceNature;
import org.openflexo.view.controller.DeclareVirtualModelInstanceNatures;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.view.menu.WindowMenu;
import org.openflexo.view.menu.WindowMenu.WindowMenuItem;

@DeclareVirtualModelInstanceNatures({ // Natures declaration
@DeclareVirtualModelInstanceNature(nature = FMLControlledDiagramVirtualModelInstanceNature.class) })
public class DiagramTechnologyAdapterController extends TechnologyAdapterController<DiagramTechnologyAdapter> {

	private SwingToolFactory swingToolFactory;

	private JDianaInspectors inspectors;
	private JDianaDialogInspectors dialogInspectors;
	private JDianaScaleSelector scaleSelector;

	@Override
	public Class<DiagramTechnologyAdapter> getTechnologyAdapterClass() {
		return DiagramTechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {

		swingToolFactory = new SwingToolFactory(actionInitializer.getController().getFlexoFrame());

		scaleSelector = swingToolFactory.makeDianaScaleSelector(null);
		dialogInspectors = swingToolFactory.makeDianaDialogInspectors();
		inspectors = swingToolFactory.makeDianaInspectors();

		dialogInspectors.getForegroundStyleInspector().setLocation(1000, 100);
		dialogInspectors.getTextStyleInspector().setLocation(1000, 300);
		dialogInspectors.getShadowStyleInspector().setLocation(1000, 400);
		dialogInspectors.getBackgroundStyleInspector().setLocation(1000, 500);
		dialogInspectors.getShapeInspector().setLocation(1000, 600);
		dialogInspectors.getConnectorInspector().setLocation(1000, 700);
		dialogInspectors.getLocationSizeInspector().setLocation(1000, 50);

		actionInitializer.getController().getModuleInspectorController()
				.loadDirectory(ResourceLocator.locateResource("Inspectors/Diagram"));

		WindowMenu viewMenu = actionInitializer.getController().getMenuBar().getWindowMenu();
		viewMenu.addSeparator();

		WindowMenuItem foregroundInspectorItem = viewMenu.new WindowMenuItem(FlexoLocalization.localizedForKey("foreground_inspector"),
				dialogInspectors.getForegroundStyleInspector());
		WindowMenuItem backgroundInspectorItem = viewMenu.new WindowMenuItem(FlexoLocalization.localizedForKey("background_inspector"),
				dialogInspectors.getBackgroundStyleInspector());
		WindowMenuItem textInspectorItem = viewMenu.new WindowMenuItem(FlexoLocalization.localizedForKey("text_inspector"),
				dialogInspectors.getTextStyleInspector());
		WindowMenuItem shapeInspectorItem = viewMenu.new WindowMenuItem(FlexoLocalization.localizedForKey("shape_inspector"),
				dialogInspectors.getShapeInspector());
		WindowMenuItem connectorInspectorItem = viewMenu.new WindowMenuItem(FlexoLocalization.localizedForKey("connector_inspector"),
				dialogInspectors.getConnectorInspector());
		WindowMenuItem shadowInspectorItem = viewMenu.new WindowMenuItem(FlexoLocalization.localizedForKey("shadow_inspector"),
				dialogInspectors.getShadowStyleInspector());
		WindowMenuItem locationSizeInspectorItem = viewMenu.new WindowMenuItem(
				FlexoLocalization.localizedForKey("location_size_inspector"), dialogInspectors.getLocationSizeInspector());

		viewMenu.add(foregroundInspectorItem);
		viewMenu.add(backgroundInspectorItem);
		viewMenu.add(textInspectorItem);
		viewMenu.add(shapeInspectorItem);
		viewMenu.add(connectorInspectorItem);
		viewMenu.add(shadowInspectorItem);
		viewMenu.add(locationSizeInspectorItem);

		// Diagram edition
		new CreateDiagramSpecificationInitializer(actionInitializer);
		new DeleteDiagramSpecificationInitializer(actionInitializer);
		new CreateExampleDiagramInitializer(actionInitializer);
		new DeleteExampleDiagramInitializer(actionInitializer);
		new PushToPaletteInitializer(actionInitializer);
		new DeclareShapeInFlexoConceptInitializer(actionInitializer);
		new DeclareConnectorInFlexoConceptInitializer(actionInitializer);
		new DeleteExampleDiagramElementsInitializer(actionInitializer);
		new CreateDiagramFromPPTSlideInitializer(actionInitializer);
		new CreateExampleDiagramFromPPTSlideInitializer(actionInitializer);

		// DiagramPalette edition
		new CreateDiagramPaletteInitializer(actionInitializer);
		new DeleteDiagramPaletteInitializer(actionInitializer);
		new AddDiagramPaletteElementInitializer(actionInitializer);
		new DeleteDiagramPaletteElementInitializer(actionInitializer);

		new CreateControlledDiagramVirtualModelInstanceInitializer(actionInitializer);
		new OpenControlledDiagramVirtualModelInstanceInitializer(actionInitializer);
		new CreateDiagramInitializer(actionInitializer);
		new DeleteDiagramInitializer(actionInitializer);
		new AddShapeInitializer(actionInitializer);
		new AddConnectorInitializer(actionInitializer);
		new DeleteDiagramElementsInitializer(actionInitializer);
		new DropSchemeActionInitializer(actionInitializer);
		new LinkSchemeActionInitializer(actionInitializer);
		new ResetGraphicalRepresentationInitializer(actionInitializer);
		new DeclareShapeInFlexoConceptInitializer(actionInitializer);
		new DeclareConnectorInFlexoConceptInitializer(actionInitializer);
		new PushToPaletteInitializer(actionInitializer);
		new ExportDiagramToImageInitializer(actionInitializer);
		new CreateFMLDiagramPaletteElementBindingInitializer(actionInitializer);
		new CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElementInitializer(actionInitializer);

		// Set the screenshot builders
		getTechnologyAdapter().setScreenshotBuilder(new DiagramScreenshotBuilder());
		getTechnologyAdapter().setDiagramShapeScreenshotBuilder(new DiagramShapeScreenshotBuilder());

		// Add paste handlers
		diagramElementPasteHandler = new DiagramElementPasteHandler(actionInitializer.getController().getSelectionManager());
		actionInitializer.getEditingContext().registerPasteHandler(diagramElementPasteHandler);

	}

	private DiagramElementPasteHandler diagramElementPasteHandler;

	public SwingToolFactory getToolFactory() {
		return swingToolFactory;
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return DiagramIconLibrary.DIAGRAM_MEDIUM_ICON;
	}

	/**
	 * Return icon representing underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getTechnologyIcon() {
		return DiagramIconLibrary.DIAGRAM_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getModelIcon() {
		return DiagramIconLibrary.DIAGRAM_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getMetaModelIcon() {
		return DiagramIconLibrary.DIAGRAM_ICON;
	}

	public JDianaDialogInspectors getDialogInspectors() {
		return dialogInspectors;
	}

	public JDianaInspectors getInspectors() {
		return inspectors;
	}

	public JDianaScaleSelector getScaleSelector() {
		return scaleSelector;
	}

	/**
	 * Return icon representing supplied ontology object
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<DiagramTechnologyAdapter>> objectClass) {
		if (Diagram.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.DIAGRAM_ICON;
		} else if (DiagramShape.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.SHAPE_ICON;
		} else if (DiagramConnector.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.CONNECTOR_ICON;
		} else if (DiagramSpecification.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.DIAGRAM_SPECIFICATION_ICON;
		} else if (FMLDiagramPaletteElementBinding.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.FML_PALETTE_ELEMENT_BINDING_ICON_16X16;
		}
		return IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_ICON, IconLibrary.QUESTION);
	}

	/**
	 * Return icon representing supplied pattern role
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForPatternRole(Class<? extends FlexoRole<?>> patternRoleClass) {
		if (DiagramRole.class.isAssignableFrom(patternRoleClass)) {
			return DiagramIconLibrary.DIAGRAM_ICON;
		} else if (ShapeRole.class.isAssignableFrom(patternRoleClass)) {
			return DiagramIconLibrary.SHAPE_ICON;
		} else if (ConnectorRole.class.isAssignableFrom(patternRoleClass)) {
			return DiagramIconLibrary.CONNECTOR_ICON;
		} else if (FlexoConceptInstanceRole.class.isAssignableFrom(patternRoleClass)) {
			return VEIconLibrary.FLEXO_CONCEPT_INSTANCE_ICON;
		}
		return null;
	}

	@Override
	public ImageIcon getIconForEditionAction(Class<? extends EditionAction<?, ?>> editionActionClass) {
		if (AddDiagram.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_ICON, IconLibrary.DUPLICATE);
		} else if (AddShape.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DiagramIconLibrary.SHAPE_ICON, IconLibrary.DUPLICATE);
		} else if (AddConnector.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DiagramIconLibrary.CONNECTOR_ICON, IconLibrary.DUPLICATE);
		} else if (GraphicalAction.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DiagramIconLibrary.GRAPHICAL_ACTION_ICON);
		} else if (DeleteAction.class.isAssignableFrom(editionActionClass)) {
			return VEIconLibrary.DELETE_ICON;
		}
		return super.getIconForEditionAction(editionActionClass);
	}

	@Override
	public ImageIcon getIconForFlexoBehaviour(Class<? extends FlexoBehaviour> flexoBehaviourClass) {
		if (DropScheme.class.isAssignableFrom(flexoBehaviourClass)) {
			return DiagramIconLibrary.DROP_SCHEME_ICON;
		} else if (LinkScheme.class.isAssignableFrom(flexoBehaviourClass)) {
			return DiagramIconLibrary.LINK_SCHEME_ICON;
		} else if (DiagramNavigationScheme.class.isAssignableFrom(flexoBehaviourClass)) {
			return DiagramIconLibrary.NAVIGATION_SCHEME_ICON;
		}
		return super.getIconForFlexoBehaviour(flexoBehaviourClass);
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject<DiagramTechnologyAdapter> object, FlexoController controller) {
		return object instanceof Diagram || object instanceof DiagramPalette || object instanceof DiagramSpecification;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject<DiagramTechnologyAdapter> object, FlexoController controller) {

		if (object instanceof Diagram) {
			return ((Diagram) object).getName();
		}
		return object.toString();
	}

	@Override
	public ModuleView<?> createModuleViewForObject(TechnologyObject<DiagramTechnologyAdapter> object, FlexoController controller,
			FlexoPerspective perspective) {

		if (object instanceof Diagram) {
			// Initialization of Diagram representation may rise PAMELA edits
			// The goal is here to embed all those edits in a special edit record
			// Which is to be discarded as undoable action at the end of this initialization
			CompoundEdit edit = null;
			if (controller.getEditor().getUndoManager() != null) {
				edit = controller.getEditor().getUndoManager().startRecording("Initialize diagram");
			}
			FreeDiagramEditor editor = new FreeDiagramEditor((Diagram) object, false, controller, swingToolFactory);
			if (edit != null) {
				controller.getEditor().getUndoManager().stopRecording(edit);
				// Make this edit not-undoable
				edit.die();
			}
			return new FreeDiagramModuleView(editor, perspective);
		}

		if (object instanceof DiagramPalette) {
			DiagramPaletteEditor editor = new DiagramPaletteEditor((DiagramPalette) object, false, controller, swingToolFactory);
			// return new DiagramPaletteModuleView(editor, perspective);
			return editor.getModuleView();
		}

		// TODO: handle DiagramSpecification
		if (object instanceof DiagramSpecification) {
			// DiagramSpecificationView diagramSpecificationView = new DiagramSpecificationView(object, controller);
			return new DiagramSpecificationView((DiagramSpecification) object, controller);
		}

		// TODO: handle FlexoConcept where many PR are parts of a diagram

		// TODO: handle VirtualModel where one or many MS are diagram MS

		/*if (object instanceof ViewPoint) {
			return new ViewPointView((ViewPoint) object, controller, perspective);
		}
		if (object instanceof FlexoConcept) {
			FlexoConcept ep = (FlexoConcept) object;
			if (ep instanceof VirtualModel) {
				// if (ep instanceof DiagramSpecification) {
				// return new DiagramSpecificationView(ep, (VPMController) controller);
				// } else {
				return new VirtualModelView(ep, controller, perspective);
				// }
			} else {
				// if (ep.getVirtualModel() instanceof DiagramSpecification) {
				// return new DiagramFlexoConceptView(ep, (VPMController) controller);
				// } else {
				return new DiagramFlexoConceptView(ep, controller, perspective);
				// }
			}

		*/

		// TODO not applicable
		return new EmptyPanel<TechnologyObject<DiagramTechnologyAdapter>>(controller, perspective, object);
	}

	/*@Override
	public void notifyModuleViewDisplayed(ModuleView<?> moduleView, final FlexoController controller, FlexoPerspective perspective) {

		super.notifyModuleViewDisplayed(moduleView, controller, perspective);
		if (moduleView instanceof FreeDiagramModuleView) {
			// Sets palette view of editor to be the top right view
			perspective.setTopRightView(((FreeDiagramModuleView) moduleView).getEditor().getPaletteView());
			// perspective.setHeader(((FreeDiagramModuleView) moduleView).getEditor().getS());

			inspectors.attachToEditor(((FreeDiagramModuleView) moduleView).getEditor());

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// Force right view to be visible
					controller.getControllerModel().setRightViewVisible(true);
				}
			});
		} else if (moduleView instanceof FMLControlledDiagramModuleView) {
			// Sets palette view of editor to be the top right view
			perspective.setTopRightView(((FMLControlledDiagramModuleView) moduleView).getEditor().getPaletteView());
			// perspective.setHeader(((FreeDiagramModuleView) moduleView).getEditor().getS());

			inspectors.attachToEditor(((FMLControlledDiagramModuleView) moduleView).getEditor());

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// Force right view to be visible
					controller.getControllerModel().setRightViewVisible(true);
				}
			});
		}
	}*/

	@Override
	public List<? extends VirtualModelInstanceNature> getSpecificVirtualModelInstanceNatures(VirtualModelInstance vmInstance) {
		if (vmInstance.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
			return Collections.singletonList(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE);
		}

		return Collections.emptyList();
	}

	@Override
	public ModuleView<VirtualModelInstance> createVirtualModelInstanceModuleViewForSpecificNature(VirtualModelInstance vmInstance,
			VirtualModelInstanceNature nature, FlexoController controller, FlexoPerspective perspective) {
		if (vmInstance.hasNature(nature) && nature == FMLControlledDiagramVirtualModelInstanceNature.INSTANCE) {
			System.out.println("----------> Hop, je recree un editor");
			FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor(vmInstance, false, controller, swingToolFactory);
			return new FMLControlledDiagramModuleView(editor, perspective);
		}
		return null;
	}

	@Override
	public List<? extends FlexoConceptNature> getSpecificFlexoConceptNatures(FlexoConcept concept) {
		if (concept.hasNature(FMLControlledDiagramFlexoConceptNature.INSTANCE)) {
			return Collections.singletonList(FMLControlledDiagramFlexoConceptNature.INSTANCE);
		}

		return Collections.emptyList();
	}

	@Override
	public ModuleView<FlexoConcept> createFlexoConceptModuleViewForSpecificNature(FlexoConcept concept, FlexoConceptNature nature,
			FlexoController controller, FlexoPerspective perspective) {
		if (concept.hasNature(nature) && nature == FMLControlledDiagramFlexoConceptNature.INSTANCE) {
			return new DiagramFlexoConceptView(concept, controller, perspective);
		}
		return null;
	}

}
