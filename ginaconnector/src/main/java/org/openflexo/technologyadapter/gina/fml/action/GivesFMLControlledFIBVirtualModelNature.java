/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Flexo-foundation, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.gina.fml.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.AbstractCreationScheme;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateModelSlot;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.task.Progress;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot.VariableAssignment;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBVirtualModelNature;
import org.openflexo.technologyadapter.gina.fml.editionaction.ConfigureGINAFIBComponent;
import org.openflexo.technologyadapter.gina.model.GINAFIBComponent;
import org.openflexo.technologyadapter.gina.model.action.CreateGINAFIBComponent;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResource;
import org.openflexo.toolbox.StringUtils;

/**
 * Gives {@link FMLControlledFIBVirtualModelNature} to this {@link VirtualModel}.<br>
 * 
 * More pragmatically, this action will add a new {@link FIBComponentModelSlot} connected to a {@link GINAFIBComponent} which is intented to
 * represent underlying {@link VirtualModel}
 * 
 * @author sylvain
 *
 */
public class GivesFMLControlledFIBVirtualModelNature extends FlexoAction<GivesFMLControlledFIBVirtualModelNature, FlexoConcept, FMLObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(GivesFMLControlledFIBVirtualModelNature.class.getPackage().getName());

	public static FlexoActionFactory<GivesFMLControlledFIBVirtualModelNature, FlexoConcept, FMLObject> actionType = new FlexoActionFactory<GivesFMLControlledFIBVirtualModelNature, FlexoConcept, FMLObject>(
			"gives_screen", FlexoActionFactory.defaultGroup, FlexoActionFactory.NORMAL_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public GivesFMLControlledFIBVirtualModelNature makeNewAction(FlexoConcept focusedObject, Vector<FMLObject> globalSelection,
				FlexoEditor editor) {
			return new GivesFMLControlledFIBVirtualModelNature(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoConcept object, Vector<FMLObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoConcept object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(GivesFMLControlledFIBVirtualModelNature.actionType, FlexoConcept.class);
	}

	private String fibModelSlotName = "ui";
	private FIBComponentChoice choice;

	public static enum FIBComponentChoice {
		CreateNewComponent, UseExistingComponent
	}

	private GINAFIBComponentResource templateResource;
	private RepositoryFolder<GINAFIBComponentResource, ?> repositoryFolder;
	private String newComponentName;

	private GivesFMLControlledFIBVirtualModelNature(FlexoConcept focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(GINATechnologyAdapter.class).getLocales();
		}
		return super.getLocales();
	}

	public GINATechnologyAdapter getGINATechnologyAdapter() {
		return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(GINATechnologyAdapter.class);
	}

	@Override
	protected void doAction(Object context) throws FlexoException {

		Progress.progress(getLocales().localizedForKey("gives_fml_controlled_component_nature"));

		if (getChoice() == FIBComponentChoice.CreateNewComponent) {
			CreateGINAFIBComponent createNewComponent = CreateGINAFIBComponent.actionType.makeNewEmbeddedAction(getRepositoryFolder(), null,
					this);
			createNewComponent.setComponentName(getNewComponentName());
			createNewComponent.doAction();
			setTemplateResource(createNewComponent.getNewComponentResource());
		}

		// Create model slot
		Progress.progress(getLocales().localizedForKey("create_model_slot") + " " + getFIBModelSlotName());
		CreateModelSlot action = CreateModelSlot.actionType.makeNewEmbeddedAction(getFocusedObject(), null, this);
		action.setModelSlotName(getFIBModelSlotName());
		action.setTechnologyAdapter(getGINATechnologyAdapter());
		action.setModelSlotClass(FIBComponentModelSlot.class);
		action.doAction();
		FIBComponentModelSlot uiModelSlot = (FIBComponentModelSlot) action.getNewModelSlot();
		uiModelSlot.setTemplateResource(getTemplateResource());
		VariableAssignment assign = uiModelSlot.createAssignment();
		assign.setVariable("data");
		assign.setVariableType(FlexoConceptInstanceType.getFlexoConceptInstanceType(getFocusedObject()));
		assign.setValue(new DataBinding<>("this"));

		if (getFocusedObject().getAbstractCreationSchemes().size() == 0) {
			// There is no creation scheme, create a default one
			CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewEmbeddedAction(getFocusedObject(), null,
					this);
			createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
			createCreationScheme.setFlexoBehaviourName("init");
			createCreationScheme.doAction();
			// CreationScheme initBehaviour = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();
		}

		// Update init behaviour(s) by appending ui=ConfigureGINAFIBComponent() to the end of all control graphs
		for (AbstractCreationScheme creationScheme : getFocusedObject().getAbstractCreationSchemes()) {
			CreateEditionAction configureModelSlotAction = CreateEditionAction.actionType
					.makeNewEmbeddedAction(creationScheme.getControlGraph(), null, this);
			configureModelSlotAction.setModelSlot(uiModelSlot);
			configureModelSlotAction.setEditionActionClass(ConfigureGINAFIBComponent.class);
			configureModelSlotAction.setAssignation(new DataBinding<>(getFIBModelSlotName()));
			configureModelSlotAction.doAction();

		}

	}

	public boolean isModelSlotNameValid() {
		if (StringUtils.isEmpty(getFIBModelSlotName())) {
			return false;
		}
		if (getFocusedObject().getModelSlot(getFIBModelSlotName()) != null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isValid() {
		if (!isModelSlotNameValid()) {
			return false;
		}
		return true;
	}

	public String getFIBModelSlotName() {
		return fibModelSlotName;
	}

	public void setFIBModelSlotName(String fibModelSlotName) {
		if ((fibModelSlotName == null && this.fibModelSlotName != null)
				|| (fibModelSlotName != null && !fibModelSlotName.equals(this.fibModelSlotName))) {
			String oldValue = this.fibModelSlotName;
			this.fibModelSlotName = fibModelSlotName;
			getPropertyChangeSupport().firePropertyChange("fibModelSlotName", oldValue, fibModelSlotName);
		}
	}

	public FIBComponentChoice getChoice() {
		return choice;
	}

	public void setChoice(FIBComponentChoice choice) {
		if ((choice == null && this.choice != null) || (choice != null && !choice.equals(this.choice))) {
			FIBComponentChoice oldValue = this.choice;
			this.choice = choice;
			getPropertyChangeSupport().firePropertyChange("choice", oldValue, choice);
		}
	}

	public GINAFIBComponentResource getTemplateResource() {
		return templateResource;
	}

	public void setTemplateResource(GINAFIBComponentResource templateResource) {
		if (templateResource != this.templateResource) {
			GINAFIBComponentResource oldValue = this.templateResource;
			this.templateResource = templateResource;
			getPropertyChangeSupport().firePropertyChange("templateResource", oldValue, templateResource);
		}
	}

	public String getNewComponentName() {
		return newComponentName;
	}

	public void setNewComponentName(String newComponentName) {
		if ((newComponentName == null && this.newComponentName != null)
				|| (newComponentName != null && !newComponentName.equals(this.newComponentName))) {
			String oldValue = this.newComponentName;
			this.newComponentName = newComponentName;
			getPropertyChangeSupport().firePropertyChange("newComponentName", oldValue, newComponentName);
		}
	}

	public RepositoryFolder<GINAFIBComponentResource, ?> getRepositoryFolder() {
		return repositoryFolder;
	}

	public void setRepositoryFolder(RepositoryFolder<GINAFIBComponentResource, ?> repositoryFolder) {
		if ((repositoryFolder == null && this.repositoryFolder != null)
				|| (repositoryFolder != null && !repositoryFolder.equals(this.repositoryFolder))) {
			RepositoryFolder<GINAFIBComponentResource, ?> oldValue = this.repositoryFolder;
			this.repositoryFolder = repositoryFolder;
			getPropertyChangeSupport().firePropertyChange("repositoryFolder", oldValue, repositoryFolder);
		}
	}

}
