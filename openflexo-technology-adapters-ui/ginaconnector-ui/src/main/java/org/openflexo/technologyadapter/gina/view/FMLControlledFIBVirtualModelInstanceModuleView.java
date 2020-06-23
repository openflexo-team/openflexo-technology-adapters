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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.openflexo.Flexo;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.resource.StreamIODelegate;
import org.openflexo.foundation.task.FlexoTask;
import org.openflexo.foundation.task.Progress;
import org.openflexo.gina.swing.editor.FIBEditor;
import org.openflexo.gina.swing.editor.controller.FIBEditorController;
import org.openflexo.gina.swing.editor.validation.ValidationPanel;
import org.openflexo.icon.IconLibrary;
import org.openflexo.icon.UtilsIconLibrary;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.pamela.validation.ValidationIssue;
import org.openflexo.selection.SelectionListener;
import org.openflexo.selection.SelectionManager;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot.VariableAssignment;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.controller.FMLControlledFIBController;
import org.openflexo.technologyadapter.gina.controller.GINAAdapterController;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.view.ModuleView;
import org.openflexo.view.SelectionSynchronizedFIBJPanel;
import org.openflexo.view.SelectionSynchronizedModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * A {@link ModuleView} suitable for {@link FMLRTVirtualModelInstance} that have the {@link FMLControlledFIBVirtualModelInstanceNature}<br>
 * Display a FIB view bound to {@link FMLRTVirtualModelInstance} evaluation context.<br>
 * This view allow to switch beeween normal and edit mode
 * 
 * @author sylvain
 *
 */
@SuppressWarnings("serial")
public class FMLControlledFIBVirtualModelInstanceModuleView extends JPanel
		implements SelectionSynchronizedModuleView<VirtualModelInstance<?, ?>> {

	protected static final Logger logger = FlexoLogger
			.getLogger(FMLControlledFIBVirtualModelInstanceModuleView.class.getPackage().getName());

	private final FlexoController controller;
	private final FlexoPerspective perspective;
	private final VirtualModelInstance<?, ?> virtualModelInstance;

	private FIBEditorController editorController;
	private GINAFIBComponent component;
	private FreeModelSlotInstance<GINAFIBComponent, FIBComponentModelSlot> modelSlotInstance;
	private SelectionSynchronizedFIBJPanel<?> componentView;
	private ValidationPanel validationPanel;

	private LocalizedDelegate locales;

	private JPanel buttonsPanel;
	private JPanel bottomPanel;

	public FMLControlledFIBVirtualModelInstanceModuleView(VirtualModelInstance<?, ?> representedObject, FlexoController controller,
			FlexoPerspective perspective, LocalizedDelegate locales) {
		super(new BorderLayout());

		this.locales = locales;
		this.controller = controller;
		this.perspective = perspective;
		this.virtualModelInstance = representedObject;

		modelSlotInstance = FMLControlledFIBVirtualModelInstanceNature.getModelSlotInstance(representedObject);
		component = FMLControlledFIBVirtualModelInstanceNature.getGINAFIBComponent(representedObject);

		if (component != null && component.getComponent() != null && component.getComponent().getModelFactory() != null) {
			System.out.println(component.getComponent().getModelFactory().stringRepresentation(component.getComponent()));
		}

		if (getRepresentedObject() != null && component != null) {
			component.bindTo(getRepresentedObject().getVirtualModel(),
					(modelSlotInstance != null ? modelSlotInstance.getModelSlot() : null));
		}

		if (component != null) {
			component.getComponent()
					.setCustomTypeEditorProvider(controller.getApplicationContext().getTechnologyAdapterControllerService());

			if (component.getComponent().getControllerClass() != null
					&& !FMLControlledFIBController.class.isAssignableFrom(component.getComponent().getControllerClass())) {
				// If declared controller class is not a subclass of FMLControlledFIBController, force it
				component.getComponent().setControllerClass(FMLControlledFIBController.class);
			}

			componentView = new SelectionSynchronizedFIBJPanel<Object>(component.getComponent(), null, locales) {
				@Override
				public void delete() {
				}

				@Override
				public Class<Object> getRepresentedType() {
					return Object.class;
				}
			};

			add(componentView, BorderLayout.CENTER);

			buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

			localizeButton = new JButton(locales.localizedForKey("localize"), UtilsIconLibrary.UK_FLAG);
			localizeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (isEditMode()) {
						getFIBEditor(false).localizeFIB(editorController.getEditedComponent(), getFlexoController().getFlexoFrame());
					}
				}
			});
			buttonsPanel.add(localizeButton);

			/*localizedItem = new JMenuItem(FIBEditor.EDITOR_LOCALIZATION.localizedForKey("localized_editor"));
			localizedItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (FIBEditorMenuBar.this.fibEditor.localizedEditor == null) {
						FIBEditorMenuBar.this.fibEditor.localizedEditor = new LocalizedEditor(frame, "localized_editor",
								FIBEditor.EDITOR_LOCALIZATION, FIBEditor.EDITOR_LOCALIZATION, true, false);
					}
					FIBEditorMenuBar.this.fibEditor.localizedEditor.setVisible(true);
				}
			});*/

			editButton = new JButton(locales.localizedForKey("edit"), IconLibrary.EDIT_ICON);
			editButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!isEditMode()) {
						switchToEditMode();
					}
				}
			});
			buttonsPanel.add(editButton);

			doneButton = new JButton(locales.localizedForKey("done"), IconLibrary.VALID_ICON);
			doneButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (isEditMode()) {
						switchToNormalMode();
					}
				}
			});
			buttonsPanel.add(doneButton);

			saveButton = new JButton(locales.localizedForKey("save"), IconLibrary.SAVE_ICON);
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (isEditMode()) {
						try {
							saveEditedComponent();
						} catch (SaveResourceException e1) {
							e1.printStackTrace();
						}
					}
				}
			});
			buttonsPanel.add(saveButton);

			localizeButton.setVisible(false);
			doneButton.setVisible(false);
			saveButton.setVisible(false);

			bottomPanel = new JPanel(new BorderLayout());

			if (Flexo.isDev) {
				bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
			}

			add(bottomPanel, BorderLayout.SOUTH);
		}
		else {
			logger.warning("Unable to create module view as component is null for: " + representedObject);
		}
	}

	private JButton localizeButton;
	private JButton editButton;
	private JButton doneButton;
	private JButton saveButton;
	private boolean editMode = false;

	public boolean isEditMode() {
		return editMode;
	}

	public void switchToEditMode() {
		FlexoTask switchToEditMode = new FlexoTask("opening_fib_editor") {
			@Override
			public void performTask() throws InterruptedException {
				setExpectedProgressSteps(50);
				_switchToEditMode();
			}
		};
		getFlexoController().getApplicationContext().getTaskManager().scheduleExecution(switchToEditMode);
	}

	public boolean _switchToEditMode() {

		System.out.println("switchToEditMode, editMode=" + editMode + " thread=" + Thread.currentThread());

		if (editMode) {
			return false;
		}

		editMode = true;

		Progress.progress(locales.localizedForKey("switching_to_edit_mode"));

		if (editorController == null) {
			Progress.progress(locales.localizedForKey("loading_fib_editor"));
			editorController = getFIBEditor(false).openFIBComponent(
					component.getResource().getIODelegate().getSerializationArtefactAsResource(), component.getComponent(),
					virtualModelInstance, controller.getFlexoFrame());
		}

		Progress.progress(locales.localizedForKey("initialize_fib_editor"));

		remove(componentView);
		add(editorController.getEditorPanel(), BorderLayout.CENTER);

		validationPanel = new ValidationPanel(editorController, getFIBEditor(false).getFIBLibrary(), FIBEditor.EDITOR_LOCALIZATION) {
			@Override
			protected void performSelect(ValidationIssue<?, ?> validationIssue) {
				System.out.println("Tiens, faudrait selectionner " + validationIssue);
			}
		};
		bottomPanel.add(validationPanel, BorderLayout.CENTER);
		// validationPanel.setEditedObject(object);
		// validationPanel.getController().setDataObject(editorController.getFIBComponent(), true);

		// We force the update of the view
		validationPanel.getController().getRootView().update();

		System.out.println("Hop, on cree le validationPanel avec " + editorController.getFIBComponent());

		editButton.setVisible(false);
		localizeButton.setVisible(true);
		doneButton.setVisible(true);
		saveButton.setVisible(true);

		updateAsEditMode();

		revalidate();
		repaint();

		return true;
	}

	public boolean switchToNormalMode() {

		System.out.println("switchToNormalMode, editMode=" + editMode);

		if (!editMode) {
			return false;
		}

		editMode = false;

		if (validationPanel != null) {
			validationPanel.delete();
		}

		if (editorController != null) {
			remove(editorController.getEditorPanel());
		}

		add(componentView, BorderLayout.CENTER);

		bottomPanel.removeAll();
		if (Flexo.isDev) {
			bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
		}

		editButton.setVisible(true);
		localizeButton.setVisible(false);
		doneButton.setVisible(false);
		saveButton.setVisible(false);

		updateAsNormalMode();

		revalidate();
		repaint();

		return true;
	}

	public boolean saveEditedComponent() throws SaveResourceException {

		if (!editMode) {
			return false;
		}

		if (component.getResource().getIODelegate() instanceof StreamIODelegate) {
			((StreamIODelegate<?>) component.getResource().getIODelegate()).setSaveToSourceResource(true);
		}
		component.getResource().save();

		return true;
	}

	public FlexoController getFlexoController() {
		return controller;
	}

	public GINAAdapterController getAdapterController() {
		GINATechnologyAdapter ta = getFlexoController().getApplicationContext().getTechnologyAdapterService()
				.getTechnologyAdapter(GINATechnologyAdapter.class);
		return getFlexoController().getApplicationContext().getTechnologyAdapterControllerService().getTechnologyAdapterController(ta);
	}

	public FIBEditor getFIBEditor(boolean launchInTask) {
		if (getAdapterController() != null) {
			return getAdapterController().getFIBEditor(launchInTask);
		}
		return null;
	}

	private void updateAsEditMode() {
		((FMLControlledFIBController) editorController.getController()).setFlexoController(getFlexoController());
		for (VariableAssignment variableAssignment : modelSlotInstance.getModelSlot().getAssignments()) {
			try {
				Object value = variableAssignment.getValue().getBindingValue(getRepresentedObject());
				// System.out.println("> Variable " + variableAssignment.getVariable() + " value=" + value);
				editorController.getController().setVariableValue(variableAssignment.getVariable(), value);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		setToolingForEditMode();
		// getFIBEditor(false).getInspector().setVisible(true);
	}

	protected void updateAsNormalMode() {
		if (componentView != null && componentView.getController() != null) {
			((FMLControlledFIBController) componentView.getController()).setFlexoController(getFlexoController());
			if (modelSlotInstance != null && modelSlotInstance.getModelSlot() != null) {
				for (VariableAssignment variableAssignment : modelSlotInstance.getModelSlot().getAssignments()) {
					try {
						Object value = variableAssignment.getValue().getBindingValue(getRepresentedObject());
						// System.out.println("> Variable " + variableAssignment.getVariable() + " value=" + value);
						componentView.getController().setVariableValue(variableAssignment.getVariable(), value);
					} catch (TypeMismatchException e) {
						e.printStackTrace();
					} catch (NullReferenceException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}
		setToolingForNormalMode();
	}

	protected void setToolingForNormalMode() {
		perspective.setTopRightView(null);
		perspective.setBottomLeftView(null);
		controller.getControllerModel().setRightViewVisible(false);
	}

	protected void setToolingForEditMode() {
		// Sets palette view of editor to be the top right view
		perspective.setTopRightView(getFIBEditor(false).getPalettes());
		perspective.setBottomRightView(getFIBEditor(false).getInspectors().getPanelGroup());
		perspective.setBottomLeftView(editorController.getEditorBrowser());

		controller.getControllerModel().setRightViewVisible(true);

		getFIBEditor(false).activate(editorController);
	}

	@Override
	public void show(FlexoController flexoController, FlexoPerspective flexoPerspective) {

		if (isEditMode()) {
			updateAsEditMode();
		}
		else {
			updateAsNormalMode();
		}
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

	@Override
	public void willHide() {
		setVisible(false);
	}

	@Override
	public void willShow() {
		setVisible(true);
	}

	public GINAFIBComponent getGINAFIBComponent() {
		return component;
	}

	public SelectionSynchronizedFIBJPanel<?> getComponentView() {
		return componentView;
	}

	@Override
	public boolean isAutoscrolled() {
		// If you want to handle scrollable by yourself instead of letting Openflexo doing it, change return to true.
		return false;
	}

	@Override
	public VirtualModelInstance<?, ?> getRepresentedObject() {
		return virtualModelInstance;
	}

	@Override
	public List<SelectionListener> getSelectionListeners() {
		return Arrays.asList((SelectionListener) this);
	}

	/**
	 * Adds specified object to selection
	 * 
	 * @param object
	 */
	@Override
	public void fireObjectSelected(FlexoObject object) {
		if (ignoreFiredSelectionEvents) {
			return;
		}
		// logger.info("SELECTED: "+object);

		componentView.getController().objectAddedToSelection(getRelevantObject(object));
	}

	/**
	 * Removes specified object from selection
	 * 
	 * @param object
	 */
	@Override
	public void fireObjectDeselected(FlexoObject object) {
		if (ignoreFiredSelectionEvents) {
			return;
		}
		// logger.info("DESELECTED: "+object);
		componentView.getController().objectRemovedFromSelection(getRelevantObject(object));
	}

	/**
	 * Clear selection
	 */
	@Override
	public void fireResetSelection() {
		if (ignoreFiredSelectionEvents) {
			return;
		}
		// logger.info("RESET SELECTION");
		componentView.getController().selectionCleared();
	}

	/**
	 * Notify that the selection manager is performing a multiple selection
	 */
	@Override
	public void fireBeginMultipleSelection() {
		if (ignoreFiredSelectionEvents) {
			return;
		}
	}

	/**
	 * Notify that the selection manager has finished to perform a multiple selection
	 */
	@Override
	public void fireEndMultipleSelection() {
		if (ignoreFiredSelectionEvents) {
			return;
		}
	}

	public SelectionManager getSelectionManager() {
		if (getFlexoController() != null) {
			return getFlexoController().getSelectionManager();
		}
		return null;
	}

	/*	@Override
		public void selectionChanged(List<Object> selection) {
			if (selection == null) {
				return;
			}
			Vector<FlexoObject> newSelection = new Vector<>();
			for (Object o : selection) {
				if (o instanceof FlexoObject) {
					newSelection.add(getRelevantObject((FlexoObject) o));
				}
			}
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("FlexoFIBView now impose new selection : " + newSelection);
			}
			if (getSelectionManager() != null) {
				ignoreFiredSelectionEvents = true;
				getSelectionManager().setSelectedObjects(newSelection);
				ignoreFiredSelectionEvents = false;
			}
		}*/

	private boolean ignoreFiredSelectionEvents = false;

	/**
	 * We manage here an indirection with resources: resource data is used instead of resource if resource is loaded
	 * 
	 * @param object
	 * @return
	 */
	private static FlexoObject getRelevantObject(FlexoObject object) {
		if (object instanceof FlexoResource<?> && ((FlexoResource<?>) object).isLoaded()) {
			try {
				return (FlexoObject) ((FlexoResource<?>) object).getResourceData();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ResourceLoadingCancelledException e) {
				e.printStackTrace();
			} catch (FlexoException e) {
				e.printStackTrace();
			}
		}
		return object;
	}

}
