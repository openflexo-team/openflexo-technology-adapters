package org.openflexo.technologyadapter.diagram.controller;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import org.openflexo.fge.swing.control.SwingToolFactory;
import org.openflexo.fge.swing.control.tools.JDianaInspectors;
import org.openflexo.fge.swing.control.tools.JDianaScaleSelector;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.viewpoint.FlexoConceptInstancePatternRole;
import org.openflexo.foundation.viewpoint.EditionScheme;
import org.openflexo.foundation.viewpoint.PatternRole;
import org.openflexo.foundation.viewpoint.editionaction.DeleteAction;
import org.openflexo.foundation.viewpoint.editionaction.EditionAction;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.icon.VEIconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.action.AddConnectorInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.AddDiagramPaletteElementInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.AddShapeInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramPaletteInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramSpecificationInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateExampleDiagramInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeclareConnectorInFlexoConceptInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeclareShapeInFlexoConceptInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramElementsInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramPaletteElementInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramPaletteInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramSpecificationInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteExampleDiagramElementsInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteExampleDiagramInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DropSchemeActionInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.ExportDiagramToImageInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.LinkSchemeActionInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.PushToPaletteInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.ResetGraphicalRepresentationInitializer;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.DiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.DiagramModuleView;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.paletteeditor.DiagramPaletteEditor;
import org.openflexo.technologyadapter.diagram.controller.paletteeditor.DiagramPaletteModuleView;
import org.openflexo.technologyadapter.diagram.fml.ConnectorPatternRole;
import org.openflexo.technologyadapter.diagram.fml.DiagramPatternRole;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapePatternRole;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddConnector;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddDiagram;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.fml.editionaction.GraphicalAction;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.FileResource;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.view.menu.WindowMenu;
import org.openflexo.view.menu.WindowMenu.WindowMenuItem;

public class DiagramTechnologyAdapterController extends TechnologyAdapterController<DiagramTechnologyAdapter> {

	private SwingToolFactory swingToolFactory;

	private JDianaInspectors inspectors;
	private JDianaScaleSelector scaleSelector;

	@Override
	public Class<DiagramTechnologyAdapter> getTechnologyAdapterClass() {
		return DiagramTechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {

		swingToolFactory = new SwingToolFactory(actionInitializer.getController().getFlexoFrame());

		scaleSelector = swingToolFactory.makeDianaScaleSelector(null);
		inspectors = swingToolFactory.makeDianaInspectors();

		inspectors.getForegroundStyleInspector().setLocation(1000, 100);
		inspectors.getTextStyleInspector().setLocation(1000, 300);
		inspectors.getShadowStyleInspector().setLocation(1000, 400);
		inspectors.getBackgroundStyleInspector().setLocation(1000, 500);
		inspectors.getShapeInspector().setLocation(1000, 600);
		inspectors.getConnectorInspector().setLocation(1000, 700);
		inspectors.getLocationSizeInspector().setLocation(1000, 50);

		actionInitializer.getController().getModuleInspectorController().loadDirectory(new FileResource("Inspectors/Diagram"));

		WindowMenu viewMenu = actionInitializer.getController().getMenuBar().getWindowMenu();
		viewMenu.addSeparator();

		WindowMenuItem foregroundInspectorItem = viewMenu.new WindowMenuItem(FlexoLocalization.localizedForKey("foreground_inspector"),
				inspectors.getForegroundStyleInspector());
		WindowMenuItem backgroundInspectorItem = viewMenu.new WindowMenuItem(FlexoLocalization.localizedForKey("background_inspector"),
				inspectors.getBackgroundStyleInspector());
		WindowMenuItem textInspectorItem = viewMenu.new WindowMenuItem(FlexoLocalization.localizedForKey("text_inspector"),
				inspectors.getTextStyleInspector());
		WindowMenuItem shapeInspectorItem = viewMenu.new WindowMenuItem(FlexoLocalization.localizedForKey("shape_inspector"),
				inspectors.getShapeInspector());
		WindowMenuItem connectorInspectorItem = viewMenu.new WindowMenuItem(FlexoLocalization.localizedForKey("connector_inspector"),
				inspectors.getConnectorInspector());
		WindowMenuItem shadowInspectorItem = viewMenu.new WindowMenuItem(FlexoLocalization.localizedForKey("shadow_inspector"),
				inspectors.getShadowStyleInspector());
		WindowMenuItem locationSizeInspectorItem = viewMenu.new WindowMenuItem(
				FlexoLocalization.localizedForKey("location_size_inspector"), inspectors.getLocationSizeInspector());

		viewMenu.add(foregroundInspectorItem);
		viewMenu.add(backgroundInspectorItem);
		viewMenu.add(textInspectorItem);
		viewMenu.add(shapeInspectorItem);
		viewMenu.add(connectorInspectorItem);
		viewMenu.add(shadowInspectorItem);
		viewMenu.add(locationSizeInspectorItem);

		// ExampleDiagram edition
		new CreateDiagramSpecificationInitializer(actionInitializer);
		new DeleteDiagramSpecificationInitializer(actionInitializer);
		new CreateExampleDiagramInitializer(actionInitializer);
		new DeleteExampleDiagramInitializer(actionInitializer);
		new PushToPaletteInitializer(actionInitializer);
		new DeclareShapeInFlexoConceptInitializer(actionInitializer);
		new DeclareConnectorInFlexoConceptInitializer(actionInitializer);
		new DeleteExampleDiagramElementsInitializer(actionInitializer);

		// DiagramPalette edition
		new CreateDiagramPaletteInitializer(actionInitializer);
		new DeleteDiagramPaletteInitializer(actionInitializer);
		new AddDiagramPaletteElementInitializer(actionInitializer);
		new DeleteDiagramPaletteElementInitializer(actionInitializer);

		// Diagram perspective
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

	}

	public SwingToolFactory getToolFactory() {
		return swingToolFactory;
	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		// TODO
		return DiagramIconLibrary.DIAGRAM_ICON;
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

	/**
	 * Return icon representing supplied ontology object
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject> objectClass) {
		if (Diagram.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.DIAGRAM_ICON;
		} else if (DiagramShape.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.SHAPE_ICON;
		} else if (DiagramConnector.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.CONNECTOR_ICON;
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
	public ImageIcon getIconForPatternRole(Class<? extends PatternRole<?>> patternRoleClass) {
		if (DiagramPatternRole.class.isAssignableFrom(patternRoleClass)) {
			return DiagramIconLibrary.DIAGRAM_ICON;
		} else if (ShapePatternRole.class.isAssignableFrom(patternRoleClass)) {
			return DiagramIconLibrary.SHAPE_ICON;
		} else if (ConnectorPatternRole.class.isAssignableFrom(patternRoleClass)) {
			return DiagramIconLibrary.CONNECTOR_ICON;
		} else if (FlexoConceptInstancePatternRole.class.isAssignableFrom(patternRoleClass)) {
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
	public ImageIcon getIconForEditionScheme(Class<? extends EditionScheme> editionSchemeClass) {
		if (DropScheme.class.isAssignableFrom(editionSchemeClass)) {
			return DiagramIconLibrary.DROP_SCHEME_ICON;
		} else if (LinkScheme.class.isAssignableFrom(editionSchemeClass)) {
			return DiagramIconLibrary.LINK_SCHEME_ICON;
		}
		return super.getIconForEditionScheme(editionSchemeClass);
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject<DiagramTechnologyAdapter> object, FlexoController controller) {
		return object instanceof Diagram || object instanceof DiagramPalette;
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
			DiagramEditor editor = new FreeDiagramEditor((Diagram) object, false, controller, swingToolFactory);
			return new DiagramModuleView(editor, perspective);
		}

		if (object instanceof DiagramPalette) {
			DiagramPaletteEditor editor = new DiagramPaletteEditor((DiagramPalette) object, false, controller, swingToolFactory);
			return new DiagramPaletteModuleView(editor, perspective);
		}

		// TODO: handle DiagramSpecification

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
				return new StandardFlexoConceptView(ep, controller, perspective);
				// }
			}

		*/

		// TODO not applicable
		return new EmptyPanel<TechnologyObject<DiagramTechnologyAdapter>>(controller, perspective, object);
	}

	@Override
	public void notifyModuleViewDisplayed(ModuleView<?> moduleView, final FlexoController controller, FlexoPerspective perspective) {

		System.out.println("Ici, moduleView = " + moduleView + " of " + moduleView.getClass());
		super.notifyModuleViewDisplayed(moduleView, controller, perspective);
		if (moduleView instanceof DiagramModuleView) {
			System.out.println("Ici2");
			// Sets palette view of editor to be the top right view
			System.out.println("Ici3, paletteView=" + ((DiagramModuleView) moduleView).getEditor().getPaletteView());
			perspective.setTopRightView(((DiagramModuleView) moduleView).getEditor().getPaletteView());
			// perspective.setHeader(((DiagramModuleView) moduleView).getEditor().getS());

			inspectors.attachToEditor(((DiagramModuleView) moduleView).getEditor());

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					// Force right view to be visible
					controller.getControllerModel().setRightViewVisible(true);
				}
			});
		}
	}

}
