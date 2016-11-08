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

package org.openflexo.technologyadapter.diagram.fml.action;

import java.lang.reflect.Type;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.fge.DrawingGraphicalRepresentation;
import org.openflexo.fge.FGEModelFactory;
import org.openflexo.fge.FGEModelFactoryImpl;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.CheckboxParameter;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FMLObject;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.FlexoBehaviour;
import org.openflexo.foundation.fml.FlexoBehaviourParameter;
import org.openflexo.foundation.fml.FlexoConceptInstanceParameter;
import org.openflexo.foundation.fml.FlexoConceptInstanceType;
import org.openflexo.foundation.fml.FloatParameter;
import org.openflexo.foundation.fml.TextFieldParameter;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.AbstractCreateVirtualModel;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviour;
import org.openflexo.foundation.fml.action.CreateFlexoBehaviourParameter;
import org.openflexo.foundation.fml.action.CreateModelSlot;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rm.VirtualModelResourceFactory;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.task.Progress;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.editionaction.CreateDiagram;
import org.openflexo.technologyadapter.diagram.rm.DiagramSpecificationResource;
import org.openflexo.toolbox.StringUtils;

public class CreateFMLControlledDiagramVirtualModel
		extends AbstractCreateVirtualModel<CreateFMLControlledDiagramVirtualModel, ViewPoint, FMLObject> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateFMLControlledDiagramVirtualModel.class.getPackage().getName());

	public static FlexoActionType<CreateFMLControlledDiagramVirtualModel, ViewPoint, FMLObject> actionType = new FlexoActionType<CreateFMLControlledDiagramVirtualModel, ViewPoint, FMLObject>(
			"create_diagram_virtual_model", FlexoActionType.newVirtualModelMenu, FlexoActionType.defaultGroup,
			FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateFMLControlledDiagramVirtualModel makeNewAction(ViewPoint focusedObject, Vector<FMLObject> globalSelection,
				FlexoEditor editor) {
			return new CreateFMLControlledDiagramVirtualModel(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(ViewPoint object, Vector<FMLObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(ViewPoint object, Vector<FMLObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(CreateFMLControlledDiagramVirtualModel.actionType, ViewPoint.class);
	}

	private String newVirtualModelName;
	private String newVirtualModelDescription;
	private VirtualModel newVirtualModel;

	private String diagramModelSlotName = "diagram";
	private DiagramSpecificationChoice choice;

	public static enum DiagramSpecificationChoice {
		CreateNewDiagramSpecification, UseExistingDiagramSpecification
	}

	private DiagramSpecificationResource diagramSpecificationResource;
	private RepositoryFolder<DiagramSpecificationResource, ?> repositoryFolder;
	private String newDiagramSpecificationName;
	private String newDiagramSpecificationURI;

	CreateFMLControlledDiagramVirtualModel(ViewPoint focusedObject, Vector<FMLObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() != null) {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class).getLocales();
		}
		return super.getLocales();
	}

	public DiagramTechnologyAdapter getDiagramTechnologyAdapter() {
		return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
	}

	private FlexoBehaviourParameter createParameter(FlexoBehaviour behaviour, String parameterName, Type parameterType) {
		CreateFlexoBehaviourParameter createParameter = CreateFlexoBehaviourParameter.actionType.makeNewEmbeddedAction(behaviour, null,
				this);
		createParameter.setParameterName(parameterName);

		if (parameterType.equals(String.class)) {
			createParameter.setFlexoBehaviourParameterClass(TextFieldParameter.class);
		}
		else if (parameterType.equals(Boolean.class) || parameterType.equals(Boolean.TYPE)) {
			createParameter.setFlexoBehaviourParameterClass(TextFieldParameter.class);
		}
		else if (parameterType.equals(Integer.class) || parameterType.equals(Integer.TYPE)) {
			createParameter.setFlexoBehaviourParameterClass(CheckboxParameter.class);
		}
		else if (parameterType.equals(Float.class) || parameterType.equals(Float.TYPE)) {
			createParameter.setFlexoBehaviourParameterClass(FloatParameter.class);
		}
		else if (parameterType.equals(Double.class) || parameterType.equals(Double.TYPE)) {
			createParameter.setFlexoBehaviourParameterClass(FloatParameter.class);
		}
		else if (parameterType instanceof FlexoConceptInstanceType) {
			createParameter.setFlexoBehaviourParameterClass(FlexoConceptInstanceParameter.class);
		}
		createParameter.doAction();
		FlexoBehaviourParameter returned = createParameter.getNewParameter();
		if (parameterType instanceof FlexoConceptInstanceType) {
			((FlexoConceptInstanceParameter) returned).setFlexoConceptType(((FlexoConceptInstanceType) parameterType).getFlexoConcept());
			((FlexoConceptInstanceParameter) returned)
					.setVirtualModelInstance(new DataBinding<VirtualModelInstance>("virtualModelInstance"));
		}
		return returned;
	}

	protected DrawingGraphicalRepresentation makePaletteGraphicalRepresentation() throws ModelDefinitionException {
		FGEModelFactory factory = new FGEModelFactoryImpl();
		DrawingGraphicalRepresentation gr = factory.makeDrawingGraphicalRepresentation();
		gr.setDrawWorkingArea(true);
		gr.setWidth(260);
		gr.setHeight(300);
		gr.setIsVisible(true);
		return gr;
	}

	@Override
	protected void doAction(Object context) throws FlexoException {

		Progress.progress(getLocales().localizedForKey("create_virtual_model"));

		FMLTechnologyAdapter fmlTechnologyAdapter = getServiceManager().getTechnologyAdapterService()
				.getTechnologyAdapter(FMLTechnologyAdapter.class);
		VirtualModelResourceFactory factory = fmlTechnologyAdapter.getViewPointResourceFactory().getVirtualModelResourceFactory();

		try {
			VirtualModelResource vmResource = factory.makeVirtualModelResource(getNewVirtualModelName(),
					(ViewPointResource) getFocusedObject().getResource(), fmlTechnologyAdapter.getTechnologyContextManager(), true);
			newVirtualModel = vmResource.getLoadedResourceData();
			newVirtualModel.setDescription(newVirtualModelDescription);
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

		// newVirtualModel = VirtualModelImpl.newVirtualModel(newVirtualModelName, getFocusedObject());
		// newVirtualModel.setDescription(newVirtualModelDescription);

		if (getChoice() == DiagramSpecificationChoice.CreateNewDiagramSpecification) {
			CreateDiagramSpecification createDiagramSpecification = CreateDiagramSpecification.actionType
					.makeNewEmbeddedAction(getRepositoryFolder(), null, this);
			createDiagramSpecification.setNewDiagramSpecificationName(getNewDiagramSpecificationName());
			createDiagramSpecification.setNewDiagramSpecificationURI(getNewDiagramSpecificationURI());
			createDiagramSpecification.doAction();
			setDiagramSpecificationResource(createDiagramSpecification.getNewDiagramSpecification().getResource());

			CreateExampleDiagram createExampleDiagram = CreateExampleDiagram.actionType
					.makeNewEmbeddedAction(createDiagramSpecification.getNewDiagramSpecification(), null, this);
			createExampleDiagram.setNewDiagramName("ExampleDiagram");
			createExampleDiagram.setNewDiagramTitle("Example diagram");
			createExampleDiagram.doAction();

			CreateDiagramPalette createDiagramPalette = CreateDiagramPalette.actionType
					.makeNewEmbeddedAction(createDiagramSpecification.getNewDiagramSpecification(), null, this);
			createDiagramPalette.setNewPaletteName("Palette");

			try {
				createDiagramPalette.setGraphicalRepresentation(makePaletteGraphicalRepresentation());
			} catch (ModelDefinitionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			createDiagramPalette.doAction();
		}

		// Create model slot
		Progress.progress(getLocales().localizedForKey("create_model_slot") + " " + getDiagramModelSlotName());
		CreateModelSlot action = CreateModelSlot.actionType.makeNewEmbeddedAction(getNewVirtualModel(), null, this);
		action.setModelSlotName(getDiagramModelSlotName());
		action.setTechnologyAdapter(getDiagramTechnologyAdapter());
		action.setModelSlotClass(TypedDiagramModelSlot.class);
		action.setMmRes(getDiagramSpecificationResource());
		action.doAction();
		TypedDiagramModelSlot uiModelSlot = (TypedDiagramModelSlot) action.getNewModelSlot();
		// uiModelSlot.setMetaModelResource(getDiagramSpecificationResource());

		// Create init behaviour
		CreateFlexoBehaviour createCreationScheme = CreateFlexoBehaviour.actionType.makeNewEmbeddedAction(newVirtualModel, null, this);
		createCreationScheme.setFlexoBehaviourClass(CreationScheme.class);
		createCreationScheme.setFlexoBehaviourName("init");
		createCreationScheme.doAction();
		CreationScheme initBehaviour = (CreationScheme) createCreationScheme.getNewFlexoBehaviour();

		CreateEditionAction configureModelSlotAction = CreateEditionAction.actionType.makeNewEmbeddedAction(initBehaviour.getControlGraph(),
				null, this);
		configureModelSlotAction.setModelSlot(uiModelSlot);
		configureModelSlotAction.setEditionActionClass(CreateDiagram.class);
		configureModelSlotAction.setAssignation(new DataBinding<Object>(getDiagramModelSlotName()));
		configureModelSlotAction.doAction();

		CreateDiagram createDiagramAction = (CreateDiagram) ((AssignationAction<?>) configureModelSlotAction.getNewEditionAction())
				.getAssignableAction();
		createDiagramAction.setDiagramSpecificationResource(getDiagramSpecificationResource());
		createDiagramAction.setDiagramName(new DataBinding<String>("'diagram'"));
		createDiagramAction.setResourceCenter(new DataBinding<FlexoResourceCenter<?>>("project"));
		createDiagramAction.setRelativePath("/Diagrams");

		newVirtualModel.getPropertyChangeSupport().firePropertyChange("name", null, newVirtualModel.getName());
		newVirtualModel.getResource().getPropertyChangeSupport().firePropertyChange("name", null, newVirtualModel.getName());
	}

	public boolean isNewVirtualModelNameValid() {
		if (StringUtils.isEmpty(newVirtualModelName)) {
			return false;
		}
		if (getFocusedObject().getVirtualModelNamed(newVirtualModelName) != null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isValid() {
		if (!isNewVirtualModelNameValid()) {
			return false;
		}
		return true;
	}

	@Override
	public VirtualModel getNewVirtualModel() {
		return newVirtualModel;
	}

	public String getNewVirtualModelName() {
		return newVirtualModelName;
	}

	public void setNewVirtualModelName(String newVirtualModelName) {
		this.newVirtualModelName = newVirtualModelName;
		getPropertyChangeSupport().firePropertyChange("newVirtualModelName", null, newVirtualModelName);

	}

	public String getNewVirtualModelDescription() {
		return newVirtualModelDescription;
	}

	public void setNewVirtualModelDescription(String newVirtualModelDescription) {
		this.newVirtualModelDescription = newVirtualModelDescription;
		getPropertyChangeSupport().firePropertyChange("newVirtualModelDescription", null, newVirtualModelDescription);
	}

	@Override
	public int getExpectedProgressSteps() {
		return 15;
	}

	public String getDiagramModelSlotName() {
		return diagramModelSlotName;
	}

	public void setDiagramModelSlotName(String diagramModelSlotName) {
		if ((diagramModelSlotName == null && this.diagramModelSlotName != null)
				|| (diagramModelSlotName != null && !diagramModelSlotName.equals(this.diagramModelSlotName))) {
			String oldValue = this.diagramModelSlotName;
			this.diagramModelSlotName = diagramModelSlotName;
			getPropertyChangeSupport().firePropertyChange("diagramModelSlotName", oldValue, diagramModelSlotName);
		}
	}

	public DiagramSpecificationChoice getChoice() {
		return choice;
	}

	public void setChoice(DiagramSpecificationChoice choice) {
		if ((choice == null && this.choice != null) || (choice != null && !choice.equals(this.choice))) {
			DiagramSpecificationChoice oldValue = this.choice;
			this.choice = choice;
			getPropertyChangeSupport().firePropertyChange("choice", oldValue, choice);
		}
	}

	public DiagramSpecificationResource getDiagramSpecificationResource() {
		return diagramSpecificationResource;
	}

	public void setDiagramSpecificationResource(DiagramSpecificationResource diagramSpecificationResource) {
		if (diagramSpecificationResource != this.diagramSpecificationResource) {
			DiagramSpecificationResource oldValue = this.diagramSpecificationResource;
			this.diagramSpecificationResource = diagramSpecificationResource;
			getPropertyChangeSupport().firePropertyChange("diagramSpecificationResource", oldValue, diagramSpecificationResource);
		}
	}

	public String getNewDiagramSpecificationURI() {
		return newDiagramSpecificationURI;
	}

	public void setNewDiagramSpecificationURI(String diagramSpecificationURI) {
		if ((diagramSpecificationURI == null && this.newDiagramSpecificationURI != null)
				|| (diagramSpecificationURI != null && !diagramSpecificationURI.equals(this.newDiagramSpecificationURI))) {
			String oldValue = this.newDiagramSpecificationURI;
			this.newDiagramSpecificationURI = diagramSpecificationURI;
			getPropertyChangeSupport().firePropertyChange("newDiagramSpecificationURI", oldValue, diagramSpecificationURI);
		}
	}

	public String getNewDiagramSpecificationName() {
		return newDiagramSpecificationName;
	}

	public void setNewDiagramSpecificationName(String newComponentName) {
		if ((newComponentName == null && this.newDiagramSpecificationName != null)
				|| (newComponentName != null && !newComponentName.equals(this.newDiagramSpecificationName))) {
			String oldValue = this.newDiagramSpecificationName;
			this.newDiagramSpecificationName = newComponentName;
			getPropertyChangeSupport().firePropertyChange("newDiagramSpecificationName", oldValue, newComponentName);
		}
	}

	public RepositoryFolder<DiagramSpecificationResource, ?> getRepositoryFolder() {
		return repositoryFolder;
	}

	public void setRepositoryFolder(RepositoryFolder<DiagramSpecificationResource, ?> repositoryFolder) {
		if ((repositoryFolder == null && this.repositoryFolder != null)
				|| (repositoryFolder != null && !repositoryFolder.equals(this.repositoryFolder))) {
			RepositoryFolder<DiagramSpecificationResource, ?> oldValue = this.repositoryFolder;
			this.repositoryFolder = repositoryFolder;
			getPropertyChangeSupport().firePropertyChange("repositoryFolder", oldValue, repositoryFolder);
		}
	}

}
