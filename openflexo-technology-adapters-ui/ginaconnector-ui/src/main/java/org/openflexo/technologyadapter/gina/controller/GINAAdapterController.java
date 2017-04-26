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

package org.openflexo.technologyadapter.gina.controller;

import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.task.FlexoTask;
import org.openflexo.foundation.task.Progress;
import org.openflexo.foundation.technologyadapter.TechnologyObject;
import org.openflexo.gina.FIBLibrary.FIBLibraryImpl;
import org.openflexo.gina.swing.editor.FIBEditor;
import org.openflexo.gina.swing.editor.controller.FIBEditorController;
import org.openflexo.gina.swing.editor.inspector.FIBEditorInspectorController;
import org.openflexo.gina.swing.editor.palette.FIBEditorPalettes;
import org.openflexo.gina.swing.editor.widget.FIBLibraryBrowser;
import org.openflexo.gina.swing.utils.FIBEditorLoadingProgress;
import org.openflexo.gina.utils.InspectorGroup;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot.VariableAssignment;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.controller.action.CreateFMLControlledFIBVirtualModelInitializer;
import org.openflexo.technologyadapter.gina.controller.action.CreateGINAFIBComponentInitializer;
import org.openflexo.technologyadapter.gina.controller.action.GivesFMLControlledFIBVirtualModelNatureInitializer;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.technologyadapter.gina.view.GINAModuleView;
import org.openflexo.view.EmptyPanel;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.TechnologyAdapterController;
import org.openflexo.view.controller.model.FlexoPerspective;

public class GINAAdapterController extends TechnologyAdapterController<GINATechnologyAdapter> {

	static final Logger LOGGER = Logger.getLogger(GINAAdapterController.class.getPackage().getName());

	private FIBEditor editor;
	private FIBLibraryBrowser libraryBrowser;
	private FIBEditorPalettes palette;
	private InspectorGroup ginaInspectorGroup;
	private FIBEditorInspectorController inspectors;

	private FMLControlledFIBNaturePerspective fmlControlledFIBNaturePerspective;
	private FMLRTControlledFIBNaturePerspective fmlRTControlledFIBNaturePerspective;

	// private FIBInspectors inspectors;

	public GINAAdapterController() {
	}

	public FIBEditor getFIBEditor(boolean launchInTask) {
		if (editor == null && getTechnologyAdapter() != null && getTechnologyAdapter().getTechnologyContextManager() != null
				&& getTechnologyAdapter().getTechnologyContextManager().getFIBLibrary() != null) {
			editor = makeFIBEditor(launchInTask);

			/*editor = new FIBEditor(FIBLibraryImpl.createInstance()) {
				@Override
				public boolean activate(FIBEditorController editorController) {
					// centerPanel.add(controller.getEditorBrowser(), LayoutPosition.BOTTOM_LEFT.name());
					// centerPanel.revalidate();
					System.out.println("Activated " + editorController.getEditedComponent());
					return super.activate(editorController);
				}
			
				@Override
				public boolean disactivate(FIBEditorController editorController) {
					// centerPanel.add(null, LayoutPosition.BOTTOM_LEFT.name());
					// centerPanel.revalidate();
					System.out.println("Disactivated " + editorController.getEditedComponent());
					return super.disactivate(editorController);
				}
			};
			
			libraryBrowser = new FIBLibraryBrowser(editor.getFIBLibrary());
			palette = editor.makePalette();
			// inspectors = editor.makeInspectors();
			
			// centerPanel.add(libraryBrowser, LayoutPosition.TOP_LEFT.name());
			// centerPanel.add(editor.getMainPanel(), LayoutPosition.CENTER.name());
			// centerPanel.add(palette, LayoutPosition.TOP_RIGHT.name());
			// centerPanel.add(inspectors.getPanelGroup(), LayoutPosition.BOTTOM_RIGHT.name());
			
			JFIBInspectorController inspector = editor.makeInspector(FlexoFrame.getActiveFrame());
			inspector.setVisible(true);*/

		}
		return editor;
	}

	public FIBLibraryBrowser getLibraryBrowser() {
		return libraryBrowser;
	}

	public FIBEditorPalettes getPalette() {
		return palette;
	}

	/**
	 * Initialize inspectors for supplied module using supplied {@link FlexoController}
	 * 
	 * @param controller
	 */
	@Override
	protected void initializeInspectors(FlexoController controller) {

		ginaInspectorGroup = controller.loadInspectorGroup("Gina", getTechnologyAdapter().getLocales(),
				getFMLTechnologyAdapterInspectorGroup());
	}

	/**
	 * Return inspector group for this technology
	 * 
	 * @return
	 */
	@Override
	public InspectorGroup getTechnologyAdapterInspectorGroup() {
		return ginaInspectorGroup;
	}

	@Override
	public Class<GINATechnologyAdapter> getTechnologyAdapterClass() {
		return GINATechnologyAdapter.class;
	}

	@Override
	public void initializeActions(ControllerActionInitializer actionInitializer) {
		// actionInitializer.getController().getModuleInspectorController().loadDirectory(ResourceLocator.locateResource("Inspectors/GIN"));

		// FML edition
		new CreateFMLControlledFIBVirtualModelInitializer(actionInitializer);
		new GivesFMLControlledFIBVirtualModelNatureInitializer(actionInitializer);

		// GINA edition
		new CreateGINAFIBComponentInitializer(actionInitializer);

	}

	@Override
	public ImageIcon getTechnologyBigIcon() {
		return GINAIconLibrary.GINA_TECHNOLOGY_BIG_ICON;
	}

	@Override
	public ImageIcon getTechnologyIcon() {
		return GINAIconLibrary.GINA_TECHNOLOGY_ICON;
	}

	@Override
	public ImageIcon getModelIcon() {
		return GINAIconLibrary.FIB_COMPONENT_ICON;
	}

	@Override
	public ImageIcon getMetaModelIcon() {
		return GINAIconLibrary.FIB_COMPONENT_ICON;
	}

	@Override
	public ImageIcon getIconForTechnologyObject(final Class<? extends TechnologyObject<?>> objectClass) {
		if (VariableAssignment.class.isAssignableFrom(objectClass)) {
			return GINAIconLibrary.VARIABLE_ASSIGNMENT_ICON;
		}
		return GINAIconLibrary.GINA_TECHNOLOGY_ICON;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(final TechnologyObject<GINATechnologyAdapter> object, final FlexoController controller,
			final FlexoPerspective perspective) {
		// TODO Auto-generated method stub : update your moduleView code to have something represented
		if (object instanceof GINAFIBComponent) {
			return new GINAModuleView((GINAFIBComponent) object, controller, perspective);
		}
		return new EmptyPanel<>(controller, perspective, object);
	}

	@Override
	public ImageIcon getIconForFlexoRole(Class<? extends FlexoRole<?>> flexoRoleClass) {
		return GINAIconLibrary.GINA_TECHNOLOGY_ICON;
	}

	@Override
	public String getWindowTitleforObject(TechnologyObject<GINATechnologyAdapter> object, FlexoController controller) {
		if (object instanceof GINAFIBComponent && ((GINAFIBComponent) object).getResource() != null) {
			return ((GINAFIBComponent) object).getResource().getName();
		}
		return "";
	}

	@Override
	public boolean hasModuleViewForObject(TechnologyObject<GINATechnologyAdapter> obj, FlexoController controller) {
		return obj instanceof GINAFIBComponent;
	}

	@Override
	public void installFMLNatureSpecificPerspectives(FlexoController controller) {
		super.installFMLNatureSpecificPerspectives(controller);
		controller.addToPerspectives(fmlControlledFIBNaturePerspective = new FMLControlledFIBNaturePerspective(controller));
	}

	public FMLControlledFIBNaturePerspective getFMLControlledFIBNaturePerspective() {
		return fmlControlledFIBNaturePerspective;
	}

	@Override
	public void installFMLRTNatureSpecificPerspectives(FlexoController controller) {
		super.installFMLRTNatureSpecificPerspectives(controller);
		controller.addToPerspectives(fmlRTControlledFIBNaturePerspective = new FMLRTControlledFIBNaturePerspective(controller));
	}

	public FMLRTControlledFIBNaturePerspective getFMLRTControlledFIBNaturePerspective() {
		return fmlRTControlledFIBNaturePerspective;
	}

	public class LoadFIBEditor extends FlexoTask {

		public LoadFIBEditor() {
			super(getLocales().localizedForKey("loading_fib_editor"));
		}

		@Override
		public void performTask() throws InterruptedException {
			setExpectedProgressSteps(10);
			editor = _makeFIBEditor();
		}

	}

	private FIBEditor makeFIBEditor(boolean launchInTask) {

		if (launchInTask && getServiceManager() != null && getServiceManager().getTaskManager() != null) {
			LoadFIBEditor task = new LoadFIBEditor();
			getServiceManager().getTaskManager().scheduleExecution(task);
			getServiceManager().getTaskManager().waitTask(task);
		}
		else {
			editor = _makeFIBEditor();
		}
		return editor;
	}

	private FIBEditor _makeFIBEditor() {
		if (getTechnologyAdapter() != null && getTechnologyAdapter().getTechnologyContextManager() != null
				&& getTechnologyAdapter().getTechnologyContextManager().getFIBLibrary() != null) {

			FIBEditorLoadingProgress progress = new FIBEditorLoadingProgress() {
				@Override
				public void progress(String stepName) {
					Progress.progress(stepName);
				}
			};

			FIBEditor editor = new FIBEditor(FIBLibraryImpl.createInstance(getTechnologyAdapter().getTechnologyAdapterService()),
					progress) {
				@Override
				public boolean activate(FIBEditorController editorController) {
					// centerPanel.add(controller.getEditorBrowser(), LayoutPosition.BOTTOM_LEFT.name());
					// centerPanel.revalidate();
					System.out.println("Activated " + editorController.getEditedComponent());
					return super.activate(editorController);
				}

				@Override
				public boolean disactivate(FIBEditorController editorController) {
					// centerPanel.add(null, LayoutPosition.BOTTOM_LEFT.name());
					// centerPanel.revalidate();
					System.out.println("Disactivated " + editorController.getEditedComponent());
					return super.disactivate(editorController);
				}
			};

			libraryBrowser = new FIBLibraryBrowser(editor.getFIBLibrary());
			palette = editor.makePalette();
			inspectors = editor.makeInspectors();

			// centerPanel.add(libraryBrowser, LayoutPosition.TOP_LEFT.name());
			// centerPanel.add(editor.getMainPanel(), LayoutPosition.CENTER.name());
			// centerPanel.add(palette, LayoutPosition.TOP_RIGHT.name());
			// centerPanel.add(inspectors.getPanelGroup(), LayoutPosition.BOTTOM_RIGHT.name());

			// JFIBDialogInspectorController inspector = editor.makeInspector(FlexoFrame.getActiveFrame());
			// inspector.setVisible(true);

			return editor;
		}
		return null;
	}

	public FIBEditorInspectorController getInspectors() {
		return inspectors;
	}

}
