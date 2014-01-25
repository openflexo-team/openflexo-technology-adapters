package org.openflexo.technologyadapter.diagram.controller;

import javax.swing.ImageIcon;

import org.openflexo.fge.swing.control.SwingToolFactory;
import org.openflexo.fge.swing.control.tools.JDianaInspectors;
import org.openflexo.fge.swing.control.tools.JDianaScaleSelector;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.foundation.viewpoint.DeleteAction;
import org.openflexo.foundation.viewpoint.EditionAction;
import org.openflexo.foundation.viewpoint.EditionPatternInstancePatternRole;
import org.openflexo.foundation.viewpoint.EditionScheme;
import org.openflexo.foundation.viewpoint.PatternRole;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.icon.VEIconLibrary;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.action.AddDiagramPaletteElementInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramPaletteInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramSpecificationInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateExampleDiagramInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeclareConnectorInEditionPatternInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeclareShapeInEditionPatternInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramPaletteElementInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramPaletteInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramSpecificationInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteExampleDiagramElementsInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteExampleDiagramInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.PushToPaletteInitializer;
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
import org.openflexo.toolbox.FileResource;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class DiagramTechnologyAdapterController extends TechnologyAdapterController<DiagramTechnologyAdapter> {

	private SwingToolFactory toolFactory;

	private JDianaInspectors inspectors;
	private JDianaScaleSelector scaleSelector;

	@Override
	public Class<DiagramTechnologyAdapter> getTechnologyAdapterClass() {
		return DiagramTechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {

		toolFactory = new SwingToolFactory(actionInitializer.getController().getFlexoFrame());

		scaleSelector = toolFactory.makeDianaScaleSelector(null);
		inspectors = toolFactory.makeDianaInspectors();

		inspectors.getForegroundStyleInspector().setLocation(1000, 100);
		inspectors.getTextStyleInspector().setLocation(1000, 300);
		inspectors.getShadowStyleInspector().setLocation(1000, 400);
		inspectors.getBackgroundStyleInspector().setLocation(1000, 500);
		inspectors.getShapeInspector().setLocation(1000, 600);
		inspectors.getConnectorInspector().setLocation(1000, 700);
		inspectors.getLocationSizeInspector().setLocation(1000, 50);

		actionInitializer.getController().getModuleInspectorController().loadDirectory(new FileResource("Inspectors/OWL"));

		// ExampleDiagram edition
		new CreateDiagramSpecificationInitializer(actionInitializer);
		new DeleteDiagramSpecificationInitializer(actionInitializer);
		new CreateExampleDiagramInitializer(actionInitializer);
		new DeleteExampleDiagramInitializer(actionInitializer);
		new PushToPaletteInitializer(actionInitializer);
		new DeclareShapeInEditionPatternInitializer(actionInitializer);
		new DeclareConnectorInEditionPatternInitializer(actionInitializer);
		new DeleteExampleDiagramElementsInitializer(actionInitializer);

		// DiagramPalette edition
		new CreateDiagramPaletteInitializer(actionInitializer);
		new DeleteDiagramPaletteInitializer(actionInitializer);
		new AddDiagramPaletteElementInitializer(actionInitializer);
		new DeleteDiagramPaletteElementInitializer(actionInitializer);
	}

	public SwingToolFactory getToolFactory() {
		return toolFactory;
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
		return VEIconLibrary.VIEW_ICON;
	}

	/**
	 * Return icon representing a model of underlying technology
	 * 
	 * @return
	 */
	@Override
	public ImageIcon getMetaModelIcon() {
		return VEIconLibrary.VIEW_ICON;
	}

	/**
	 * Return icon representing supplied ontology object
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject> objectClass) {
		return null;
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
		} else if (EditionPatternInstancePatternRole.class.isAssignableFrom(patternRoleClass)) {
			return VEIconLibrary.EDITION_PATTERN_INSTANCE_ICON;
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
	public boolean hasModuleViewForObject(TechnologyObject object) {
		// TODO not applicable
		return false;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject object) {
		return object.toString();
	}

	@Override
	public <T extends FlexoObject> ModuleView<T> createModuleViewForObject(T object, FlexoController controller,
			FlexoPerspective perspective) {
		// TODO not applicable
		return new EmptyPanel<T>(controller, perspective, object);
	}

}
