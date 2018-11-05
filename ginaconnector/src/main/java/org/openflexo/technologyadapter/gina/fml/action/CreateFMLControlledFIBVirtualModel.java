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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.Bindable;
import org.openflexo.connie.BindingFactory;
import org.openflexo.connie.BindingModel;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.type.PrimitiveType;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.TechnologySpecificFlexoAction;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.FlexoConceptInstanceRole;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.PrimitiveRole;
import org.openflexo.foundation.fml.PropertyCardinality;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModelInstanceType;
import org.openflexo.foundation.fml.action.AbstractCreateNatureSpecificVirtualModel;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateFlexoConceptInstanceRole;
import org.openflexo.foundation.fml.action.CreateModelSlot;
import org.openflexo.foundation.fml.action.CreatePrimitiveRole;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.editionaction.ExpressionAction;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.task.Progress;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot;
import org.openflexo.technologyadapter.gina.FIBComponentModelSlot.VariableAssignment;
import org.openflexo.technologyadapter.gina.GINATechnologyAdapter;
import org.openflexo.technologyadapter.gina.fml.editionaction.ConfigureGINAFIBComponent;
import org.openflexo.technologyadapter.gina.model.action.CreateGINAFIBComponent;
import org.openflexo.technologyadapter.gina.rm.GINAFIBComponentResource;
import org.openflexo.toolbox.PropertyChangedSupportDefaultImplementation;
import org.openflexo.toolbox.StringUtils;

/**
 * 
 * @author sylvain
 *
 */
public class CreateFMLControlledFIBVirtualModel extends AbstractCreateNatureSpecificVirtualModel<CreateFMLControlledFIBVirtualModel>
		implements TechnologySpecificFlexoAction<GINATechnologyAdapter> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateFMLControlledFIBVirtualModel.class.getPackage().getName());

	public static FlexoActionFactory<CreateFMLControlledFIBVirtualModel, FlexoObject, FMLObject> actionType = new FlexoActionFactory<CreateFMLControlledFIBVirtualModel, FlexoObject, FMLObject>(
			"create_screen_virtual_model", FlexoActionFactory.newVirtualModelMenu, FlexoActionFactory.defaultGroup,
			FlexoActionFactory.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFMLControlledFIBVirtualModel makeNewAction(FlexoObject focusedObject, Vector<FMLObject> globalSelection,
				FlexoEditor editor) {
			return new CreateFMLControlledFIBVirtualModel(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoObject object, Vector<FMLObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoObject object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFMLControlledFIBVirtualModel.actionType, VirtualModel.class);
		FlexoObjectImpl.addActionForClass(CreateFMLControlledFIBVirtualModel.actionType, RepositoryFolder.class);
	}

	// Unused private String newVirtualModelName;
	// Unused private String newVirtualModelDescription;
	private VirtualModel newVirtualModel;

	private String fibModelSlotName = "ui";
	private FIBComponentChoice choice;

	public static enum FIBComponentChoice {
		CreateNewComponent, UseExistingComponent
	}

	private GINAFIBComponentResource templateResource;
	private RepositoryFolder<GINAFIBComponentResource, ?> repositoryFolder;
	private String newComponentName;

	private CreateFMLControlledFIBVirtualModel(FlexoObject focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
		apiEntries = new ArrayList<>();
	}

	@Override
	public GINATechnologyAdapter getTechnologyAdapter() {
		return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(GINATechnologyAdapter.class);
	}

	@Override
	public Class<GINATechnologyAdapter> getTechnologyAdapterClass() {
		return GINATechnologyAdapter.class;
	}

	private PrimitiveRole<?> createPrimitiveRole(VirtualModel vm, String roleName, PrimitiveType primitiveType) {
		CreatePrimitiveRole createStringRole = CreatePrimitiveRole.actionType.makeNewEmbeddedAction(newVirtualModel, null, this);
		createStringRole.setRoleName(roleName);
		createStringRole.setPrimitiveType(primitiveType);
		createStringRole.setCardinality(PropertyCardinality.ZeroOne);
		createStringRole.doAction();
		return createStringRole.getNewFlexoRole();
	}

	@Override
	protected void doAction(Object context) throws FlexoException {

		Progress.progress(getLocales().localizedForKey("create_virtual_model"));

		Progress.progress(getLocales().localizedForKey("create_virtual_model"));

		try {
			VirtualModelResource vmResource = makeVirtualModelResource();
			newVirtualModel = vmResource.getLoadedResourceData();
			newVirtualModel.setDescription(getNewVirtualModelDescription());
		} catch (SaveResourceException e) {
			throw new SaveResourceException(null);
		} catch (ModelDefinitionException e) {
			throw new FlexoException(e);
		}

		performSetParentConcepts();
		performCreateProperties();
		performCreateBehaviours();
		performCreateInspectors();
		performPostProcessings();

		if (getChoice() == FIBComponentChoice.CreateNewComponent) {
			CreateGINAFIBComponent createNewComponent = CreateGINAFIBComponent.actionType.makeNewEmbeddedAction(getRepositoryFolder(), null,
					this);
			createNewComponent.setComponentName(getNewComponentName());
			createNewComponent.doAction();
			setTemplateResource(createNewComponent.getNewComponentResource());
		}

		// Create required roles
		for (GINAFIBComponentAPIEntry apiEntry : getAPIEntries()) {
			if (apiEntry.isAPI() && apiEntry.getType() != null) {
				FlexoRole<?> newRole = null;
				if (apiEntry.getType().equals(String.class)) {
					newRole = createPrimitiveRole(newVirtualModel, apiEntry.getName(), PrimitiveType.String);
				}
				else if (apiEntry.getType().equals(Boolean.class) || apiEntry.getType().equals(Boolean.TYPE)) {
					newRole = createPrimitiveRole(newVirtualModel, apiEntry.getName(), PrimitiveType.Boolean);
				}
				else if (apiEntry.getType().equals(Integer.class) || apiEntry.getType().equals(Integer.TYPE)) {
					newRole = createPrimitiveRole(newVirtualModel, apiEntry.getName(), PrimitiveType.Integer);
				}
				else if (apiEntry.getType().equals(Float.class) || apiEntry.getType().equals(Float.TYPE)) {
					newRole = createPrimitiveRole(newVirtualModel, apiEntry.getName(), PrimitiveType.Float);
				}
				else if (apiEntry.getType().equals(Double.class) || apiEntry.getType().equals(Double.TYPE)) {
					newRole = createPrimitiveRole(newVirtualModel, apiEntry.getName(), PrimitiveType.Double);
				}
				else if (apiEntry.getType() instanceof VirtualModelInstanceType) {
					CreateFlexoConceptInstanceRole createViewRole = CreateFlexoConceptInstanceRole.actionType
							.makeNewEmbeddedAction(newVirtualModel, null, this);
					createViewRole.setRoleName(apiEntry.getName());
					createViewRole.setFlexoConceptInstanceType(((VirtualModelInstanceType) apiEntry.getType()).getVirtualModel());
					createViewRole.setCardinality(PropertyCardinality.ZeroOne);
					createViewRole.doAction();
					newRole = createViewRole.getNewFlexoRole();
					((FlexoConceptInstanceRole) newRole).setVirtualModelInstance(new DataBinding<VirtualModelInstance<?, ?>>("view"));
				}
				else if (apiEntry.getType() instanceof FlexoConceptInstanceType) {
					CreateFlexoConceptInstanceRole createViewRole = CreateFlexoConceptInstanceRole.actionType
							.makeNewEmbeddedAction(newVirtualModel, null, this);
					createViewRole.setRoleName(apiEntry.getName());
					createViewRole.setFlexoConceptInstanceType(((FlexoConceptInstanceType) apiEntry.getType()).getFlexoConcept());
					createViewRole.setCardinality(PropertyCardinality.ZeroOne);
					createViewRole.doAction();
					newRole = createViewRole.getNewFlexoRole();
					((FlexoConceptInstanceRole) newRole)
							.setVirtualModelInstance(new DataBinding<VirtualModelInstance<?, ?>>("virtualModelInstance"));
				}
			}
		}

		// Create model slot
		Progress.progress(getLocales().localizedForKey("create_model_slot") + " " + getFIBModelSlotName());
		CreateModelSlot action = CreateModelSlot.actionType.makeNewEmbeddedAction(getNewVirtualModel(), null, this);
		action.setModelSlotName(getFIBModelSlotName());
		action.setTechnologyAdapter(getTechnologyAdapter());
		action.setModelSlotClass(FIBComponentModelSlot.class);
		action.doAction();
		FIBComponentModelSlot uiModelSlot = (FIBComponentModelSlot) action.getNewModelSlot();
		uiModelSlot.setTemplateResource(getTemplateResource());
		for (GINAFIBComponentAPIEntry apiEntry : getAPIEntries()) {
			if (apiEntry.getType() != null) {
				VariableAssignment assign = uiModelSlot.createAssignment();
				assign.setVariable(apiEntry.getName());
				assign.setVariableType(apiEntry.getType());
				if (apiEntry.isAPI()) {
					assign.setValue(new DataBinding<>(apiEntry.getName()));
				}
				else {
					assign.setValue(new DataBinding<>(apiEntry.getValue().toString()));
				}
			}
		}

		// Create init behaviour
		CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewEmbeddedAction(newVirtualModel, null, this);
		createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
		createCreationScheme.setFlexoBehaviourName("init");
		createCreationScheme.doAction();
		CreationScheme initBehaviour = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();
		for (GINAFIBComponentAPIEntry apiEntry : getAPIEntries()) {
			if (apiEntry.getType() != null) {
				if (apiEntry.isAPI()) {
					// Unused FlexoBehaviourParameter newParameter =
					createParameter(initBehaviour, apiEntry.getName(), apiEntry.getType());
				}
			}
		}

		CreateEditionAction configureModelSlotAction = CreateEditionAction.actionType.makeNewEmbeddedAction(initBehaviour.getControlGraph(),
				null, this);
		configureModelSlotAction.setModelSlot(uiModelSlot);
		configureModelSlotAction.setEditionActionClass(ConfigureGINAFIBComponent.class);
		configureModelSlotAction.setAssignation(new DataBinding<>(getFIBModelSlotName()));
		configureModelSlotAction.doAction();

		for (GINAFIBComponentAPIEntry apiEntry : getAPIEntries()) {
			if (apiEntry.getType() != null) {
				if (apiEntry.isAPI()) {
					CreateEditionAction createAssignAction = CreateEditionAction.actionType
							.makeNewEmbeddedAction(initBehaviour.getControlGraph(), null, this);
					createAssignAction.setModelSlot(uiModelSlot);
					createAssignAction.setEditionActionClass(ExpressionAction.class);
					createAssignAction.setAssignation(new DataBinding<>(apiEntry.getName()));
					createAssignAction.doAction();
					AssignationAction<?> assignAction = (AssignationAction<?>) createAssignAction.getNewEditionAction();
					((ExpressionAction<?>) assignAction.getAssignableAction())
							.setExpression(new DataBinding<>("parameters." + apiEntry.getName()));
					assignAction.setName("assign");
				}
			}
		}

		newVirtualModel.getPropertyChangeSupport().firePropertyChange("name", null, newVirtualModel.getName());
		newVirtualModel.getResource().getPropertyChangeSupport().firePropertyChange("name", null, newVirtualModel.getName());
	}

	@Override
	public VirtualModel getNewVirtualModel() {
		return newVirtualModel;
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

	private final List<GINAFIBComponentAPIEntry> apiEntries;

	public List<GINAFIBComponentAPIEntry> getAPIEntries() {
		return apiEntries;
	}

	public GINAFIBComponentAPIEntry newAPIEntry() {
		GINAFIBComponentAPIEntry returned = new GINAFIBComponentAPIEntry();
		returned.setName("data" + (getAPIEntries().size() > 0 ? getAPIEntries().size() + 1 : ""));
		apiEntries.add(returned);
		getPropertyChangeSupport().firePropertyChange("APIEntries", null, returned);
		return returned;
	}

	public void deleteAPIEntry(GINAFIBComponentAPIEntry apiEntryToDelete) {
		apiEntries.remove(apiEntryToDelete);
		apiEntryToDelete.delete();
		getPropertyChangeSupport().firePropertyChange("APIEntries", apiEntryToDelete, null);
	}

	public class GINAFIBComponentAPIEntry extends PropertyChangedSupportDefaultImplementation implements Bindable {

		private String name;
		private Type type;
		private boolean isAPI;
		private DataBinding<?> value;

		public void delete() {
			name = null;
			type = null;
			value = null;
			getPropertyChangeSupport().firePropertyChange("deleted", false, true);
		}

		@Override
		public String getDeletedProperty() {
			return "deleted";
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			if ((name == null && this.name != null) || (name != null && !name.equals(this.name))) {
				String oldValue = this.name;
				this.name = name;
				getPropertyChangeSupport().firePropertyChange("name", oldValue, name);
			}
		}

		public Type getType() {
			return type;
		}

		public void setType(Type type) {
			if ((type == null && this.type != null) || (type != null && !type.equals(this.type))) {
				Type oldValue = this.type;
				this.type = type;
				getPropertyChangeSupport().firePropertyChange("type", oldValue, type);
			}
		}

		public boolean isAPI() {
			return isAPI;
		}

		public void setIsAPI(boolean isAPI) {
			if (isAPI != this.isAPI) {
				this.isAPI = isAPI;
				getPropertyChangeSupport().firePropertyChange("isAPI", !isAPI, isAPI);
			}
		}

		public DataBinding<?> getValue() {
			if (value == null) {
				value = new DataBinding<>(this, Object.class, DataBinding.BindingDefinitionType.GET);
				value.setBindingName("value");
			}
			return value;
		}

		public void setValue(DataBinding<?> value) {
			if (value != null) {
				this.value = new DataBinding<>(value.toString(), this, Object.class, DataBinding.BindingDefinitionType.GET);
				value.setBindingName("value");
			}
		}

		public String getConfigurationErrorMessage() {

			if (StringUtils.isEmpty(getName())) {
				return getLocales().localizedForKey("please_supply_valid_entry_name");
			}
			if (getType() == null) {
				return getLocales().localizedForKey("no_type_defined_for") + " " + getName();
			}

			return null;
		}

		public String getConfigurationWarningMessage() {
			if (!isAPI() && !getValue().isValid()) {
				return getLocales().localizedForKey("no_value_defined_for_non_API_entry") + " " + getName();
			}
			return null;

		}

		@Override
		public BindingFactory getBindingFactory() {
			if (getFocusedObject() instanceof VirtualModel) {
				return ((VirtualModel) getFocusedObject()).getBindingFactory();
			}
			return null;
		}

		@Override
		public BindingModel getBindingModel() {
			if (getFocusedObject() instanceof VirtualModel) {
				return ((VirtualModel) getFocusedObject()).getBindingModel();
			}
			return null;
		}

		@Override
		public void notifiedBindingChanged(DataBinding<?> dataBinding) {
			getPropertyChangeSupport().firePropertyChange(dataBinding.getBindingName(), null, dataBinding);
		}

		@Override
		public void notifiedBindingDecoded(DataBinding<?> dataBinding) {
			getPropertyChangeSupport().firePropertyChange(dataBinding.getBindingName(), null, dataBinding);
		}
	}

}
