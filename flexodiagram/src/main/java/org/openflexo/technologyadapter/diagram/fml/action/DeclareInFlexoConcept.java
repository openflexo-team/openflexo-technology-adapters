/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Flexodiagram, a component of the software infrastructure 
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.FlexoBehaviour;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FMLRTModelSlot;
import org.openflexo.foundation.technologyadapter.FlexoMetaModel;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.technologyadapter.TypeAwareModelSlot;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

/**
 * This abstract class is an action that allows to create an flexo concept from a graphical representation(for instance a shape or
 * connector)
 * 
 * @author Sylvain, Vincent
 * 
 * @param <A>
 * @param <T1>
 */
public abstract class DeclareInFlexoConcept<A extends DeclareInFlexoConcept<A, T>, T extends DiagramElement<?>> extends
		FlexoAction<A, T, DiagramElement<?>> {

	private static final Logger logger = Logger.getLogger(DeclareInFlexoConcept.class.getPackage().getName());

	/**
	 * Stores the VirtualModel on which we are working<br>
	 * This {@link VirtualModel} must be set with external API.
	 */
	private VirtualModel virtualModel;

	private VirtualModelResource virtualModelResource;

	/**
	 * Stores the model slot which encodes the access to a {@link Diagram} conform to a {@link DiagramSpecification}, in the context of a
	 * {@link VirtualModel} (which is a context where a diagram is federated with other sources of informations)
	 */
	private TypedDiagramModelSlot diagramModelSlot;

	/**
	 * Stores the model slot used as source of information (data) in pattern proposal
	 */
	private ModelSlot<?> modelSlot;

	private FlexoConcept flexoConcept;

	private List<FMLRTModelSlot> virtualModelModelSlots = null;
	private List<TypeAwareModelSlot<?, ?>> typeAwareModelSlots = null;

	/**
	 * Constructor for this class
	 * 
	 * @param actionType
	 * @param focusedObject
	 * @param globalSelection
	 * @param editor
	 */
	DeclareInFlexoConcept(FlexoActionType<A, T, DiagramElement<?>> actionType, T focusedObject, Vector<DiagramElement<?>> globalSelection,
			FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
		// Get the set of model slots that are available from the current virtual model
		List<ModelSlot<?>> availableModelSlots = getModelSlots();
		if (availableModelSlots != null && availableModelSlots.size() > 0) {
			modelSlot = availableModelSlots.get(0);
		}
		// Get the set of internal elements inside the current focused object
		drawingObjectEntries = new Vector<DrawingObjectEntry>();
		int shapeIndex = 1;
		int connectorIndex = 1;

		List<? extends DiagramElement<?>> elements = (getFocusedObject() instanceof DiagramContainerElement ? ((DiagramContainerElement<?>) getFocusedObject())
				.getDescendants() : Collections.singletonList(getFocusedObject()));

		for (DiagramElement<?> o : elements) {
			if (o instanceof DiagramShape) {
				DiagramShape shape = (DiagramShape) o;
				String shapeRoleName = "shape" + (shapeIndex > 1 ? shapeIndex : "");
				drawingObjectEntries.add(new DrawingObjectEntry(shape, shapeRoleName));
				shapeIndex++;
			}
			if (o instanceof DiagramConnector) {
				DiagramConnector connector = (DiagramConnector) o;
				String connectorRoleName = "connector" + (connectorIndex > 1 ? connectorIndex : "");
				drawingObjectEntries.add(new DrawingObjectEntry(connector, connectorRoleName));
				connectorIndex++;
			}
		}
	}

	/**
	 * Return the VirtualModel on which we are working<br>
	 * This {@link VirtualModel} must be set with external API.
	 */
	public VirtualModel getVirtualModel() {
		if (virtualModel == null && getVirtualModelResource() != null) {
			return getVirtualModelResource().getVirtualModel();
		}
		return virtualModel;
	}

	/**
	 * Sets the VirtualModel on which we are working<br>
	 * This {@link VirtualModel} must be set with external API.
	 */
	public void setVirtualModel(VirtualModel virtualModel) {
		this.virtualModel = virtualModel;
	}

	public static enum DeclareInFlexoConceptChoices {
		CREATES_FLEXO_CONCEPT, CHOOSE_EXISTING_FLEXO_CONCEPT
	}

	public DeclareInFlexoConceptChoices primaryChoice = DeclareInFlexoConceptChoices.CREATES_FLEXO_CONCEPT;

	/**
	 * Return the model slot which encodes the access to a {@link Diagram} conform to a {@link DiagramSpecification}, in the context of a
	 * {@link VirtualModel} (which is a context where a diagram is federated with other sources of informations)
	 * 
	 * @return
	 */
	public TypedDiagramModelSlot getDiagramModelSlot() {
		return diagramModelSlot;
	}

	/**
	 * Sets the model slot which encodes the access to a {@link Diagram} conform to a {@link DiagramSpecification}, in the context of a
	 * {@link VirtualModel} (which is a context where a diagram is federated with other sources of informations)
	 * 
	 * @return
	 */
	public void setDiagramModelSlot(TypedDiagramModelSlot diagramModelSlot) {
		this.diagramModelSlot = diagramModelSlot;
	}

	@Override
	public abstract boolean isValid();

	public FlexoConcept getFlexoConcept() {
		return flexoConcept;
	}

	public void setFlexoConcept(FlexoConcept flexoConcept) {
		if (flexoConcept != this.flexoConcept) {
			this.flexoConcept = flexoConcept;
			resetFlexoRole();
		}
	}

	public abstract GraphicalElementRole<T, ?> getFlexoRole();

	public abstract List<? extends GraphicalElementRole<T, ?>> getAvailableFlexoRoles();

	public abstract void resetFlexoRole();

	public Vector<DrawingObjectEntry> drawingObjectEntries;

	public int getSelectedEntriesCount() {
		int returned = 0;
		for (DrawingObjectEntry e : drawingObjectEntries) {
			if (e.selectThis) {
				returned++;
			}
		}
		return returned;
	}

	public DrawingObjectEntry getEntry(DiagramElement<?> o) {
		for (DrawingObjectEntry e : drawingObjectEntries) {
			if (e.graphicalObject == o) {
				return e;
			}
		}
		return null;
	}

	public DiagramSpecification getDiagramSpecification() {
		return getFocusedObject().getDiagram().getDiagramSpecification();
	}

	/**
	 * Return the model slot used as source of information (data) in pattern proposal
	 * 
	 * @return
	 */
	public ModelSlot<?> getModelSlot() {
		return modelSlot;
	}

	/**
	 * Sets the model slot used as source of information (data) in pattern proposal
	 * 
	 * @return
	 */
	public void setModelSlot(ModelSlot<?> modelSlot) {
		this.modelSlot = modelSlot;
	}

	/**
	 * Return the list of all model slots declared in virtual model where this action is defined
	 * 
	 * @return
	 */
	public List<ModelSlot<?>> getModelSlots() {
		if (getVirtualModel() != null) {
			return getVirtualModel().getModelSlots();
		}
		return null;
	}

	/*private VirtualModelInstance retrieveCurrentVirtualModelInstance(){
		// Get the current Diagram
		Diagram currentDiagram = ((DiagramElement)getFocusedObject()).getDiagram();
		// Browse views resource
		if(getEditor() !=null && getEditor().getProject() !=null 
				&& getEditor().getProject().getViewLibrary()!=null 
				&& getEditor().getProject().getViewLibrary().getAllResources()!=null){
			for(ViewResource vr : getEditor().getProject().getViewLibrary().getAllResources()){
				if(vr.getView()!=null && vr.getView().getVirtualModelInstances()!=null){
					for(VirtualModelInstance vmi : vr.getView().getVirtualModelInstances()){
						for(ModelSlotInstance msi : vmi.getModelSlotInstances()){
							if(msi.getAccessedResourceData()!=null && msi.getAccessedResourceData().equals(currentDiagram)){
								return vmi;
							}
						}
					}
				}
				
			}
		}
		
		return null;
	}*/

	/**
	 * Return a virtual model adressed by a model slot
	 * 
	 * @return
	 */
	public VirtualModel getAdressedVirtualModel() {
		if (isVirtualModelModelSlot()) {
			FMLRTModelSlot virtualModelModelSlot = (FMLRTModelSlot) getModelSlot();
			return virtualModelModelSlot.getAddressedVirtualModel();
		}
		return null;
	}

	/**
	 * Return a virtual model adressed by a model slot
	 * 
	 * @return
	 */
	public FlexoMetaModel getAdressedFlexoMetaModel() {
		if (isTypeAwareModelSlot()) {
			TypeAwareModelSlot typeAwareModelSlot = (TypeAwareModelSlot) getModelSlot();
			if (typeAwareModelSlot != null && typeAwareModelSlot.getMetaModelResource() != null) {
				return typeAwareModelSlot.getMetaModelResource().getMetaModelData();
			}
		}
		return null;
	}

	public List<FMLRTModelSlot> getVirtualModelModelSlots() {
		if (virtualModelModelSlots == null) {
			virtualModelModelSlots = new ArrayList<FMLRTModelSlot>();
		}
		if (!virtualModelModelSlots.isEmpty()) {
			virtualModelModelSlots.clear();
		}
		if (getVirtualModel() != null) {
			for (ModelSlot<?> modelSlot : getVirtualModel().getModelSlots()) {
				if (modelSlot instanceof FMLRTModelSlot) {
					virtualModelModelSlots.add((FMLRTModelSlot) modelSlot);
				}
			}
		}
		return virtualModelModelSlots;
	}

	public List<TypeAwareModelSlot<?, ?>> getTypeAwareModelSlots() {
		if (typeAwareModelSlots == null) {
			typeAwareModelSlots = new ArrayList<TypeAwareModelSlot<?, ?>>();
		}
		if (!typeAwareModelSlots.isEmpty()) {
			typeAwareModelSlots.clear();
		}
		if (getVirtualModel() != null) {
			for (ModelSlot<?> modelSlot : getVirtualModel().getModelSlots()) {
				if (modelSlot instanceof TypeAwareModelSlot) {
					typeAwareModelSlots.add((TypeAwareModelSlot<?, ?>) modelSlot);
				}
			}
		}
		return typeAwareModelSlots;
	}

	public boolean isTypeAwareModelSlot() {
		if (getModelSlot() instanceof TypeAwareModelSlot) {
			return true;
		}
		return false;
	}

	public boolean isVirtualModelModelSlot() {
		if (getModelSlot() instanceof FMLRTModelSlot) {
			return true;
		}
		return false;
	}

	// TODO: i think that sometimes FlexoConcept is null !!!
	public FMLModelFactory getFactory() {
		if (getFlexoConcept() != null) {
			return getFlexoConcept().getFMLModelFactory();
		} else if (virtualModel != null) {
			return virtualModel.getFMLModelFactory();
		}
		return null;
	}

	private ArrayList<FlexoBehaviourConfiguration> flexoBehaviours;

	public void setFlexoBehaviours(ArrayList<FlexoBehaviourConfiguration> editionSchemes) {
		this.flexoBehaviours = editionSchemes;
	}

	public List<FlexoBehaviourConfiguration> getFlexoBehaviours() {
		if (flexoBehaviours == null) {
			flexoBehaviours = new ArrayList<FlexoBehaviourConfiguration>();

			initializeBehaviours();

			FlexoBehaviourConfiguration deleteShapeBehaviour = new FlexoBehaviourConfiguration(FlexoBehaviourChoice.DELETE_GR_ONLY);
			FlexoBehaviourConfiguration deleteShapeAndModelAllBehaviours = new FlexoBehaviourConfiguration(
					FlexoBehaviourChoice.DELETE_GR_AND_MODEL);

			flexoBehaviours.add(deleteShapeBehaviour);
			flexoBehaviours.add(deleteShapeAndModelAllBehaviours);
		}
		return flexoBehaviours;
	}

	public void updateEditionSchemesName(String name) {
		for (FlexoBehaviourConfiguration editionSchemeConfiguration : getFlexoBehaviours()) {
			if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DELETE_GR_ONLY) {
				editionSchemeConfiguration.getFlexoBehaviour().setName("deleteGR");
			}
			if (editionSchemeConfiguration.getType() == FlexoBehaviourChoice.DELETE_GR_AND_MODEL) {
				editionSchemeConfiguration.getFlexoBehaviour().setName("deleteGRandModel");
			}
			if (name != null) {
				editionSchemeConfiguration.getFlexoBehaviour().setName(editionSchemeConfiguration.getFlexoBehaviour().getName() + name);
			}
		}
		updateSpecialSchemeNames();
	}

	public void updateSpecialSchemeNames() {

	}

	public TypedDiagramModelSlot getTypedDiagramModelSlot() {
		if (getVirtualModel().getModelSlots(TypedDiagramModelSlot.class) != null
				&& getVirtualModel().getModelSlots(TypedDiagramModelSlot.class).size() > 0) {
			return getVirtualModel().getModelSlots(TypedDiagramModelSlot.class).get(0);
		} else {
			;
			DiagramTechnologyAdapter diagramTechnologyAdapter = getEditor().getServiceManager().getTechnologyAdapterService()
					.getTechnologyAdapter(DiagramTechnologyAdapter.class);
			TypedDiagramModelSlot typedDiagramModelSlot = diagramTechnologyAdapter.makeModelSlot(TypedDiagramModelSlot.class,
					getVirtualModel());
			typedDiagramModelSlot.setName("typedDiagramModelSlot");
			// ((TypeAwareModelSlot) newModelSlot).setMetaModelResource(mmRes);
			getVirtualModel().addToModelSlots(typedDiagramModelSlot);
			return typedDiagramModelSlot;
		}
	}

	public abstract void initializeBehaviours();

	/*public boolean editionSchemesNamedAreValid() {
		for (FlexoBehaviourConfiguration editionSchemeConfiguration : getFlexoBehaviours()) {
			if (editionSchemeConfiguration == null || editionSchemeConfiguration.getFlexoBehaviour() == null
					|| StringUtils.isEmpty(editionSchemeConfiguration.getFlexoBehaviour().getName()))
				return false;
		}
		return true;
	}*/

	public void addFlexoBehaviourConfigurationDeleteGROnly() {
		FlexoBehaviourConfiguration editionSchemeConfiguration = new FlexoBehaviourConfiguration(FlexoBehaviourChoice.DELETE_GR_ONLY);
		getFlexoBehaviours().add(editionSchemeConfiguration);
	}

	public void addFlexoBehaviourConfigurationDeleteGRAndModel() {
		FlexoBehaviourConfiguration editionSchemeConfiguration = new FlexoBehaviourConfiguration(FlexoBehaviourChoice.DELETE_GR_AND_MODEL);
		getFlexoBehaviours().add(editionSchemeConfiguration);
	}

	public void removeFlexoBehaviourConfiguration(FlexoBehaviourConfiguration editionSchemeConfiguration) {
		getFlexoBehaviours().remove(editionSchemeConfiguration);
	}

	public VirtualModelResource getVirtualModelResource() {
		return virtualModelResource;
	}

	public void setVirtualModelResource(VirtualModelResource virtualModelResource) {
		this.virtualModelResource = virtualModelResource;
		setVirtualModel(virtualModelResource.getVirtualModel());
	}

	public static enum FlexoBehaviourChoice {
		DELETE_GR_ONLY, DELETE_GR_AND_MODEL, DROP_AND_CREATE, DROP_AND_SELECT, LINK, CREATION
	}

	public class FlexoBehaviourConfiguration {

		private FlexoBehaviourChoice type;

		private boolean isValid;

		private FlexoBehaviour flexoBehaviour;

		public FlexoBehaviourConfiguration(FlexoBehaviourChoice type) {
			this.type = type;
			this.isValid = true;
			if (getFactory() != null) {
				if (type == FlexoBehaviourChoice.DELETE_GR_ONLY) {
					flexoBehaviour = getFactory().newDeletionScheme();
					flexoBehaviour.setName("defaultDeleteGROnly");
				}
				if (type == FlexoBehaviourChoice.DELETE_GR_AND_MODEL) {
					flexoBehaviour = getFactory().newDeletionScheme();
					flexoBehaviour.setName("defaultDeleteGRandModel");
				}
				if (type == FlexoBehaviourChoice.DROP_AND_CREATE) {
					flexoBehaviour = getFactory().newInstance(DropScheme.class);
					((DropScheme) flexoBehaviour).setTopTarget(true);
					flexoBehaviour.setName("defaultDropAndCreate");
				}
				if (type == FlexoBehaviourChoice.DROP_AND_SELECT) {
					flexoBehaviour = getFactory().newInstance(DropScheme.class);
					((DropScheme) flexoBehaviour).setTopTarget(true);
					flexoBehaviour.setName("defaultDropAndSelect");
				}
				if (type == FlexoBehaviourChoice.LINK) {
					flexoBehaviour = getFactory().newInstance(LinkScheme.class);
					flexoBehaviour.setName("defaultLink");
				}
			}
		}

		public FlexoBehaviourChoice getType() {
			return type;
		}

		public void setType(FlexoBehaviourChoice type) {
			this.type = type;
		}

		public FlexoBehaviour getFlexoBehaviour() {
			return flexoBehaviour;
		}

		public boolean isValid() {
			return isValid;
		}

		public void setValid(boolean isValid) {
			this.isValid = isValid;
		}

	}

	public class DrawingObjectEntry {
		private boolean selectThis;
		public DiagramElement<?> graphicalObject;
		public String flexoRoleName;

		public DrawingObjectEntry(DiagramElement<?> graphicalObject, String flexoRoleName) {
			super();
			this.graphicalObject = graphicalObject;
			this.flexoRoleName = flexoRoleName;
			this.selectThis = isMainEntry();
		}

		public boolean isMainEntry() {
			return graphicalObject == getFocusedObject();
		}

		public boolean getSelectThis() {
			if (isMainEntry()) {
				return true;
			}
			return selectThis;
		}

		public void setSelectThis(boolean aFlag) {
			if (!isMainEntry()) {
				selectThis = aFlag;
			}
		}

		public DrawingObjectEntry getParentEntry() {
			return getEntry(graphicalObject.getParent());
		}
	}

}
