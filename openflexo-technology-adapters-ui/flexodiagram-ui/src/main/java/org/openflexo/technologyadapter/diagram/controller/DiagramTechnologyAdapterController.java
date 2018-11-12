/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.technologyadapter.diagram.controller;

import javax.swing.ImageIcon;

import org.openflexo.diana.swing.control.SwingToolFactory;
import org.openflexo.diana.swing.control.tools.JDianaDialogInspectors;
import org.openflexo.diana.swing.control.tools.JDianaInspectors;
import org.openflexo.diana.swing.control.tools.JDianaScaleSelector;
import org.openflexo.foundation.fml.FlexoBehaviour;
import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.editionaction.DeleteAction;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.utils.InspectorGroup;
import org.openflexo.icon.FMLRTIconLibrary;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.pamela.undo.CompoundEdit;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.action.AddConnectorInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.AddShapeInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramFromPPTSlideInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramPaletteElementInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramPaletteInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateDiagramSpecificationInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateExampleDiagramFromPPTSlideInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateExampleDiagramInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateFMLControlledDiagramFlexoConceptInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateFMLControlledDiagramPaletteElementInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateFMLControlledDiagramVirtualModelInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateFMLControlledDiagramVirtualModelInstanceInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElementInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreateFMLDiagramPaletteElementBindingInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreatePaletteElementFromFlexoConceptInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.CreatePaletteElementFromShapeInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeclareConnectorInFlexoConceptInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeclareShapeInFlexoConceptInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.DeleteDiagramElementsAndFlexoConceptInstancesInitializer;
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
import org.openflexo.technologyadapter.diagram.controller.action.ExportFMLControlledDiagramToImageInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.LinkSchemeActionInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.OpenFMLControlledDiagramVirtualModelInstanceInitializer;
import org.openflexo.technologyadapter.diagram.controller.action.ResetGraphicalRepresentationInitializer;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramEditor;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FreeDiagramModuleView;
import org.openflexo.technologyadapter.diagram.controller.paletteeditor.DiagramPaletteEditor;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.DiagramNavigationScheme;
import org.openflexo.technologyadapter.diagram.fml.DiagramRole;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.FMLDiagramPaletteElementBinding;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddConnector;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.fml.editionaction.CreateDiagram;
import org.openflexo.technologyadapter.diagram.fml.editionaction.GraphicalAction;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.diagram.gui.view.DiagramSpecificationView;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramPalette;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
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
	private JDianaDialogInspectors dialogInspectors;
	private JDianaScaleSelector scaleSelector;

	@Override
	public Class<DiagramTechnologyAdapter> getTechnologyAdapterClass() {
		return DiagramTechnologyAdapter.class;
	}

	@Override
	public void activate() {
		super.activate();
		getServiceManager().getScreenshotService().registerDelegate(new FMLControlledDiagramScreenshotServiceDelegate());
	}

	@Override
	protected void initializeInspectors(FlexoController controller) {

		swingToolFactory = new SwingToolFactory(controller.getFlexoFrame());

		scaleSelector = swingToolFactory.makeDianaScaleSelector(null);
		dialogInspectors = swingToolFactory.makeDianaDialogInspectors();
		inspectors = swingToolFactory.makeDianaInspectors();
		// inspectors.getPanelGroup().setPreferredSize(new Dimension(800, 800));
		// inspectors.getPanelGroup().setMinimumSize(new Dimension(500, 500));

		dialogInspectors.getForegroundStyleInspector().setLocation(1000, 100);
		dialogInspectors.getTextPropertiesInspector().setLocation(1000, 300);
		dialogInspectors.getShadowStyleInspector().setLocation(1000, 400);
		dialogInspectors.getBackgroundStyleInspector().setLocation(1000, 500);
		dialogInspectors.getShapeInspector().setLocation(1000, 600);
		dialogInspectors.getConnectorInspector().setLocation(1000, 700);
		dialogInspectors.getLocationSizeInspector().setLocation(1000, 50);

		diagramInspectorGroup = controller.loadInspectorGroup("Diagram", getTechnologyAdapter().getLocales(),
				getFMLTechnologyAdapterInspectorGroup());
	}

	private InspectorGroup diagramInspectorGroup;

	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return diagramInspectorGroup;
	}

	@Override
	protected void initializeActions(ControllerActionInitializer actionInitializer) {

		WindowMenu viewMenu = actionInitializer.getController().getMenuBar().getWindowMenu();
		viewMenu.addSeparator();

		WindowMenuItem foregroundInspectorItem = viewMenu.new WindowMenuItem(getLocales().localizedForKey("foreground_inspector"),
				dialogInspectors.getForegroundStyleInspector());
		WindowMenuItem backgroundInspectorItem = viewMenu.new WindowMenuItem(getLocales().localizedForKey("background_inspector"),
				dialogInspectors.getBackgroundStyleInspector());
		WindowMenuItem textInspectorItem = viewMenu.new WindowMenuItem(getLocales().localizedForKey("text_inspector"),
				dialogInspectors.getTextPropertiesInspector());
		WindowMenuItem shapeInspectorItem = viewMenu.new WindowMenuItem(getLocales().localizedForKey("shape_inspector"),
				dialogInspectors.getShapeInspector());
		WindowMenuItem connectorInspectorItem = viewMenu.new WindowMenuItem(getLocales().localizedForKey("connector_inspector"),
				dialogInspectors.getConnectorInspector());
		WindowMenuItem shadowInspectorItem = viewMenu.new WindowMenuItem(getLocales().localizedForKey("shadow_inspector"),
				dialogInspectors.getShadowStyleInspector());
		WindowMenuItem locationSizeInspectorItem = viewMenu.new WindowMenuItem(getLocales().localizedForKey("location_size_inspector"),
				dialogInspectors.getLocationSizeInspector());

		viewMenu.add(foregroundInspectorItem);
		viewMenu.add(backgroundInspectorItem);
		viewMenu.add(textInspectorItem);
		viewMenu.add(shapeInspectorItem);
		viewMenu.add(connectorInspectorItem);
		viewMenu.add(shadowInspectorItem);
		viewMenu.add(locationSizeInspectorItem);

		// Set the screenshot builders
		getTechnologyAdapter().setScreenshotBuilder(new DiagramScreenshotBuilder());
		getTechnologyAdapter().setDiagramPaletteScreenshotBuilder(new DiagramPaletteScreenshotBuilder());
		getTechnologyAdapter().setDiagramShapeScreenshotBuilder(new DiagramShapeScreenshotBuilder());
		getTechnologyAdapter().setFMLControlledDiagramScreenshotBuilder(new FMLControlledDiagramScreenshotBuilder());

		// Add paste handlers
		diagramElementPasteHandler = new DiagramElementPasteHandler(actionInitializer.getController().getSelectionManager());
		actionInitializer.getEditingContext().registerPasteHandler(diagramElementPasteHandler);

		// Diagram edition
		new CreateDiagramSpecificationInitializer(actionInitializer);
		new DeleteDiagramSpecificationInitializer(actionInitializer);
		new CreateExampleDiagramInitializer(actionInitializer);
		new DeleteExampleDiagramInitializer(actionInitializer);
		new CreatePaletteElementFromShapeInitializer(actionInitializer);
		new DeleteExampleDiagramElementsInitializer(actionInitializer);
		new CreateDiagramFromPPTSlideInitializer(actionInitializer);
		new CreateExampleDiagramFromPPTSlideInitializer(actionInitializer);

		// DiagramPalette edition
		new CreateDiagramPaletteInitializer(actionInitializer);
		new DeleteDiagramPaletteInitializer(actionInitializer);
		new CreateDiagramPaletteElementInitializer(actionInitializer);
		new DeleteDiagramPaletteElementInitializer(actionInitializer);

		new CreateFMLControlledDiagramVirtualModelInitializer(actionInitializer);

		new CreateFMLControlledDiagramVirtualModelInstanceInitializer(actionInitializer);
		new OpenFMLControlledDiagramVirtualModelInstanceInitializer(actionInitializer);
		new CreateDiagramInitializer(actionInitializer);
		new DeleteDiagramInitializer(actionInitializer);
		new AddShapeInitializer(actionInitializer);
		new AddConnectorInitializer(actionInitializer);
		new DeleteDiagramElementsInitializer(actionInitializer);
		new DropSchemeActionInitializer(actionInitializer);
		new LinkSchemeActionInitializer(actionInitializer);
		new ResetGraphicalRepresentationInitializer(actionInitializer);
		new CreatePaletteElementFromFlexoConceptInitializer(actionInitializer);
		new CreatePaletteElementFromShapeInitializer(actionInitializer);
		new ExportDiagramToImageInitializer(actionInitializer);
		new ExportFMLControlledDiagramToImageInitializer(actionInitializer);
		new DeleteDiagramElementsAndFlexoConceptInstancesInitializer(actionInitializer);

	}

	@Override
	public void initializeAdvancedActions(ControllerActionInitializer actionInitializer) {
		new DeclareShapeInFlexoConceptInitializer(actionInitializer);
		new DeclareConnectorInFlexoConceptInitializer(actionInitializer);
		new CreateFMLDiagramPaletteElementBindingInitializer(actionInitializer);
		new CreateFMLDiagramPaletteElementBindingFromDiagramPaletteElementInitializer(actionInitializer);
		new CreateFMLControlledDiagramPaletteElementInitializer(actionInitializer);

		// Overrides CreateFlexoConceptInitializer by providing palette element creation
		new CreateFMLControlledDiagramFlexoConceptInitializer(actionInitializer);

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
		return DiagramIconLibrary.DIAGRAM_SPECIFICATION_ICON;
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
	public ImageIcon getIconForTechnologyObject(Class<? extends TechnologyObject<?>> objectClass) {
		if (Diagram.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.DIAGRAM_ICON;
		}
		else if (DiagramShape.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.SHAPE_ICON;
		}
		else if (DiagramConnector.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.CONNECTOR_ICON;
		}
		else if (DiagramSpecification.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.DIAGRAM_SPECIFICATION_ICON;
		}
		else if (DiagramPalette.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.DIAGRAM_PALETTE_ICON;
		}
		else if (FMLDiagramPaletteElementBinding.class.isAssignableFrom(objectClass)) {
			return DiagramIconLibrary.FML_PALETTE_ELEMENT_BINDING_ICON_16X16;
		}
		return IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_ICON, IconLibrary.QUESTION);
	}

	/**
	 * Return icon representing supplied pattern property
	 * 
	 * @param object
	 * @return
	 */
	@Override
	public ImageIcon getIconForFlexoRole(Class<? extends FlexoRole<?>> patternRoleClass) {
		if (DiagramRole.class.isAssignableFrom(patternRoleClass)) {
			return DiagramIconLibrary.DIAGRAM_ICON;
		}
		else if (ShapeRole.class.isAssignableFrom(patternRoleClass)) {
			return DiagramIconLibrary.SHAPE_ICON;
		}
		else if (ConnectorRole.class.isAssignableFrom(patternRoleClass)) {
			return DiagramIconLibrary.CONNECTOR_ICON;
		}
		else if (FlexoConceptInstanceRole.class.isAssignableFrom(patternRoleClass)) {
			return FMLRTIconLibrary.FLEXO_CONCEPT_INSTANCE_ICON;
		}
		return null;
	}

	@Override
	public ImageIcon getIconForEditionAction(Class<? extends EditionAction> editionActionClass) {
		if (CreateDiagram.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DiagramIconLibrary.DIAGRAM_ICON, IconLibrary.DUPLICATE);
		}
		else if (AddShape.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DiagramIconLibrary.SHAPE_ICON, IconLibrary.DUPLICATE);
		}
		else if (AddConnector.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DiagramIconLibrary.CONNECTOR_ICON, IconLibrary.DUPLICATE);
		}
		else if (GraphicalAction.class.isAssignableFrom(editionActionClass)) {
			return IconFactory.getImageIcon(DiagramIconLibrary.GRAPHICAL_ACTION_ICON);
		}
		else if (DeleteAction.class.isAssignableFrom(editionActionClass)) {
			return FMLRTIconLibrary.DELETE_ICON;
		}
		return super.getIconForEditionAction(editionActionClass);
	}

	@Override
	public ImageIcon getIconForFlexoBehaviour(Class<? extends FlexoBehaviour> flexoBehaviourClass) {
		if (DropScheme.class.isAssignableFrom(flexoBehaviourClass)) {
			return DiagramIconLibrary.DROP_SCHEME_ICON;
		}
		else if (LinkScheme.class.isAssignableFrom(flexoBehaviourClass)) {
			return DiagramIconLibrary.LINK_SCHEME_ICON;
		}
		else if (DiagramNavigationScheme.class.isAssignableFrom(flexoBehaviourClass)) {
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

		if (object instanceof DiagramSpecification) {
			// DiagramSpecificationView diagramSpecificationView = new DiagramSpecificationView(object, controller);
			return new DiagramSpecificationView((DiagramSpecification) object, controller);
		}

		// TODO not applicable
		return new EmptyPanel<>(controller, perspective, object);
	}

}
