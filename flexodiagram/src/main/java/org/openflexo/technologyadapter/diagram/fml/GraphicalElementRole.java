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

package org.openflexo.technologyadapter.diagram.fml;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.connie.Bindable;
import org.openflexo.connie.BindingFactory;
import org.openflexo.connie.DataBinding;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.rt.AbstractVirtualModelInstanceModelFactory;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.ModelObjectActorReference;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.utils.FlexoObjectReference;
import org.openflexo.model.annotations.Adder;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.Getter.Cardinality;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Remover;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.FreeDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementAction.ActionMask;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;

@ModelEntity(isAbstract = true)
@ImplementationClass(GraphicalElementRole.GraphicalElementRoleImpl.class)
public abstract interface GraphicalElementRole<T extends DiagramElement<GR>, GR extends GraphicalRepresentation>
		extends FlexoRole<T>, Bindable {

	public static GraphicalFeature<String, GraphicalRepresentation> LABEL_FEATURE = new HackClassInit();

	public static class HackClassInit extends GraphicalFeature<String, GraphicalRepresentation> {

		public HackClassInit() {
			super("label", GraphicalRepresentation.TEXT);
		}

		@Override
		public String retrieveFromGraphicalRepresentation(GraphicalRepresentation gr) {
			return gr.getText();
		}

		@Override
		public void applyToGraphicalRepresentation(GraphicalRepresentation gr, String value) {
			gr.setText(value);
		}
	}

	public static GraphicalFeature<Boolean, GraphicalRepresentation> VISIBLE_FEATURE = new GraphicalFeature<Boolean, GraphicalRepresentation>(
			"visible", GraphicalRepresentation.IS_VISIBLE) {
		@Override
		public Boolean retrieveFromGraphicalRepresentation(GraphicalRepresentation gr) {
			return gr.getIsVisible();
		}

		@Override
		public void applyToGraphicalRepresentation(GraphicalRepresentation gr, Boolean value) {
			gr.setIsVisible(value);
		}
	};

	public static GraphicalFeature<Double, GraphicalRepresentation> TRANSPARENCY_FEATURE = new GraphicalFeature<Double, GraphicalRepresentation>(
			"transparency", GraphicalRepresentation.TRANSPARENCY) {
		@Override
		public Double retrieveFromGraphicalRepresentation(GraphicalRepresentation gr) {
			return gr.getTransparency();
		}

		@Override
		public void applyToGraphicalRepresentation(GraphicalRepresentation gr, Double value) {
			gr.setTransparency(value);
		}
	};

	public static GraphicalFeature<?, ?>[] AVAILABLE_FEATURES = { LABEL_FEATURE, VISIBLE_FEATURE, TRANSPARENCY_FEATURE };

	@PropertyIdentifier(type = String.class)
	public static final String EXAMPLE_LABEL_KEY = "exampleLabel";
	@PropertyIdentifier(type = Vector.class)
	public static final String ACTIONS_KEY = "actions";
	@PropertyIdentifier(type = Vector.class)
	public static final String DECLARED_GRSPECIFICATIONS_KEY = "declaredGRSpecifications";

	@PropertyIdentifier(type = DiagramElement.class)
	String METAMODEL_ELEMENT_KEY = "metamodelElement";
	@PropertyIdentifier(type = FlexoObjectReference.class)
	String METAMODEL_ELEMENT_REFERENCE_KEY = "metamodelElementReference";

	@Getter(value = EXAMPLE_LABEL_KEY)
	@XMLAttribute
	public String getExampleLabel();

	@Setter(EXAMPLE_LABEL_KEY)
	public void setExampleLabel(String exampleLabel);

	@Getter(value = ACTIONS_KEY, cardinality = Cardinality.LIST, inverse = GraphicalElementAction.GRAPHICAL_ELEMENT_ROLE_KEY)
	@XMLElement
	public List<GraphicalElementAction> getActions();

	@Setter(ACTIONS_KEY)
	public void setActions(List<GraphicalElementAction> actions);

	@Adder(ACTIONS_KEY)
	public void addToActions(GraphicalElementAction aAction);

	@Remover(ACTIONS_KEY)
	public void removeFromActions(GraphicalElementAction aAction);

	@Getter(value = DECLARED_GRSPECIFICATIONS_KEY, cardinality = Cardinality.LIST)
	@XMLElement
	public List<GraphicalElementSpecification<?, GR>> _getDeclaredGRSpecifications();

	@Setter(DECLARED_GRSPECIFICATIONS_KEY)
	public void _setDeclaredGRSpecifications(List<GraphicalElementSpecification<?, GR>> declaredGRSpecifications);

	@Adder(DECLARED_GRSPECIFICATIONS_KEY)
	public void _addToDeclaredGRSpecifications(GraphicalElementSpecification<?, GR> aDeclaredGRSpecification);

	@Remover(DECLARED_GRSPECIFICATIONS_KEY)
	public void _removeFromDeclaredGRSpecifications(GraphicalElementSpecification<?, GR> aDeclaredGRSpecification);

	@Getter(METAMODEL_ELEMENT_KEY)
	public T getMetamodelElement();

	@Setter(METAMODEL_ELEMENT_KEY)
	public void setMetamodelElement(T anElement);

	@Getter(value = METAMODEL_ELEMENT_REFERENCE_KEY, isStringConvertable = true)
	@XMLAttribute
	public FlexoObjectReference<T> getMetamodelElementReference();

	@Setter(METAMODEL_ELEMENT_REFERENCE_KEY)
	public void setMetamodelElementReference(FlexoObjectReference<T> anElementReference);

	public GR getGraphicalRepresentation();

	// Convenient method to access spec for label feature
	public DataBinding<String> getLabel();

	// Convenient method to access spec for label feature
	public void setLabel(DataBinding<String> label);

	// Convenient method to access read-only property for spec for label feature
	public boolean getReadOnlyLabel();

	// Convenient method to access read-only property for spec for label feature
	public void setReadOnlyLabel(boolean readOnlyLabel);

	public List<GraphicalElementSpecification<?, GR>> getGrSpecifications();

	public GraphicalElementSpecification<?, GR> getGraphicalElementSpecification(String featureName);

	public <T2> GraphicalElementSpecification<T2, GR> getGraphicalElementSpecification(GraphicalFeature<T2, ?> feature);

	public boolean containsShapes();

	public List<GraphicalElementAction> getActions(ActionMask mask);

	public List<ActionMask> getReferencedMasks();

	public GraphicalElementAction createAction();

	public GraphicalElementAction deleteAction(GraphicalElementAction anAction);

	@Deprecated
	public GR getDeprecatedGraphicalRepresentation();

	/**
	 * Called to configure using prototyping {@link DiagramElement} from metamodel
	 * 
	 * @param metaModelShape
	 */
	public void bindTo(T metaModelConcept);

	public static abstract class GraphicalElementRoleImpl<T extends DiagramElement<GR>, GR extends GraphicalRepresentation>
			extends FlexoRoleImpl<T> implements GraphicalElementRole<T, GR> {

		private static final Logger logger = Logger.getLogger(GraphicalElementRole.class.getPackage().getName());

		protected List<GraphicalElementSpecification<?, GR>> grSpecifications;

		private boolean defaultSpecificationsInitialized = false;

		private final List<GraphicalElementSpecification<?, GR>> pendingGRSpecs;

		public GraphicalElementRoleImpl() {
			super();
			pendingGRSpecs = new ArrayList<>();
			// Don't do it now: remember that this is forbidden to call from the constructor any method which has to be interpretated by
			// PAMELA
			// initDefaultSpecifications();
		}

		@Override
		public void finalizeDeserialization() {
			super.finalizeDeserialization();
			// Give a chance to GRSpecs to be well deserialized
			if (!defaultSpecificationsInitialized) {
				initDefaultSpecifications();
			}
		}

		protected void initDefaultSpecifications() {
			if (getFMLModelFactory() != null) {
				defaultSpecificationsInitialized = true;
				grSpecifications = new ArrayList<>();
				for (GraphicalFeature<?, ?> GF : AVAILABLE_FEATURES) {
					// logger.info("[COMMON:" + getRoleName() + "] Nouvelle GraphicalElementSpecification for " + GF);
					GraphicalElementSpecification newGraphicalElementSpecification = getFMLModelFactory()
							.newInstance(GraphicalElementSpecification.class);
					newGraphicalElementSpecification.setFlexoRole(this);
					newGraphicalElementSpecification.setFeature(GF);
					newGraphicalElementSpecification.setReadOnly(false);
					newGraphicalElementSpecification.setMandatory(true);
					grSpecifications.add(newGraphicalElementSpecification);
				}
			}

		}

		protected void handlePendingGRSpecs() {
			for (GraphicalElementSpecification<?, GR> grSpec : pendingGRSpecs) {
				registerGRSpecification(grSpec);
			}
		}

		public DiagramSpecification getDiagramSpecification() {
			if (getModelSlot() instanceof TypedDiagramModelSlot) {
				try {
					return ((TypedDiagramModelSlot) getModelSlot()).getMetaModelResource().getResourceData(null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ResourceLoadingCancelledException e) {
					e.printStackTrace();
				} catch (FlexoException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		/**
		 * Encodes the default cloning strategy
		 *
		 * @return
		 */
		@Override
		public RoleCloningStrategy defaultCloningStrategy() {
			return RoleCloningStrategy.Clone;
		}

		@Override
		public boolean defaultBehaviourIsToBeDeleted() {
			return true;
		}

		private FlexoObjectReference<T> metaModelElementReference;

		@Override
		public T getMetamodelElement() {
			if (metaModelElementReference != null) {
				return metaModelElementReference.getObject(true);
			}
			return null;
		}

		@Override
		public void setMetamodelElement(T anElement) {
			T old = (metaModelElementReference != null ? metaModelElementReference.getObject() : null);
			if (metaModelElementReference != null) {
				metaModelElementReference.setObject(anElement);
			}
			else {
				metaModelElementReference = new FlexoObjectReference<>(anElement);
			}
			getPropertyChangeSupport().firePropertyChange(METAMODEL_ELEMENT_KEY, old, anElement);
		}

		@Override
		public FlexoObjectReference<T> getMetamodelElementReference() {
			return metaModelElementReference;
		}

		@Override
		public void setMetamodelElementReference(FlexoObjectReference<T> anElementReference) {
			this.metaModelElementReference = anElementReference;
		}

		@Override
		public final GR getGraphicalRepresentation() {
			// System.out.println("Retrieve GR of " + this);
			// System.out.println("metaModelElementReference=" + getMetamodelElementReference());
			// System.out.println("metaModelElement=" + getMetamodelElement());
			if (getMetamodelElement() != null) {
				// System.out.println("return " + getMetamodelElement().getGraphicalRepresentation());
				return getMetamodelElement().getGraphicalRepresentation();
			}

			// System.out.println("J'arrive pas a obtenir de GR");
			// System.out.println("DS=" + getDiagramSpecification());
			// System.out.println("deprecatedGR=" + getDeprecatedGraphicalRepresentation());

			// Create the diagram element in the meta model
			if (getDiagramSpecification() != null) {
				Diagram exampleDiagram = getDiagramSpecification().getDefaultExampleDiagram();
				if (exampleDiagram == null) {
					exampleDiagram = getDiagramSpecification().createDefaultExampleDiagram(getServiceManager().getDefaultEditor());
				}
				T newMetamodelElement = makeDiagramElementInMetaModel(exampleDiagram, getDeprecatedGraphicalRepresentation());
				setMetamodelElement(newMetamodelElement);
				// System.out.println("newMetamodelElement=" + getMetamodelElement());
				return getMetamodelElement().getGraphicalRepresentation();
			}

			logger.warning("GraphicalElementRole not connected to any DiagramSpecification");
			return null;
		}

		public abstract T makeDiagramElementInMetaModel(Diagram exampleDiagram, GR graphicalRepresentation);

		// Convenient method to access spec for label feature
		@Override
		public DataBinding<String> getLabel() {
			if (getGraphicalElementSpecification(LABEL_FEATURE) != null) {
				return getGraphicalElementSpecification(LABEL_FEATURE).getValue();
			}
			return null;
		}

		// Convenient method to access spec for label feature
		@Override
		public void setLabel(DataBinding<String> label) {
			if (getGraphicalElementSpecification(LABEL_FEATURE) != null) {
				getGraphicalElementSpecification(LABEL_FEATURE).setValue(label);
			}
		}

		// Convenient method to access read-only property for spec for label feature
		@Override
		public boolean getReadOnlyLabel() {
			if (getGraphicalElementSpecification(LABEL_FEATURE) != null) {
				return getGraphicalElementSpecification(LABEL_FEATURE).getReadOnly();
			}
			return true;
		}

		// Convenient method to access read-only property for spec for label feature
		@Override
		public void setReadOnlyLabel(boolean readOnlyLabel) {
			if (getGraphicalElementSpecification(LABEL_FEATURE) != null) {
				getGraphicalElementSpecification(LABEL_FEATURE).setReadOnly(readOnlyLabel);
			}
		}

		@Override
		public BindingFactory getBindingFactory() {
			return getFlexoConcept().getInspector().getBindingFactory();
		}

		@Override
		public boolean containsShapes() {
			for (ShapeRole role : getFlexoConcept().getDeclaredProperties(ShapeRole.class)) {
				if (role.getParentShapeRole() == this) {
					return true;
				}
			}
			return false;
		}

		@Override
		public List<ActionMask> getReferencedMasks() {
			ArrayList<GraphicalElementAction.ActionMask> returned = new ArrayList<>();
			for (GraphicalElementAction a : getActions()) {
				if (!returned.contains(a.getActionMask())) {
					returned.add(a.getActionMask());
				}
			}
			return returned;
		}

		@Override
		public List<GraphicalElementAction> getActions(ActionMask mask) {
			ArrayList<GraphicalElementAction> returned = new ArrayList<>();
			for (GraphicalElementAction a : getActions()) {
				if (a.getActionMask() == mask) {
					returned.add(a);
				}
			}
			return returned;
		}

		@Override
		public GraphicalElementAction createAction() {
			GraphicalElementAction newAction = getFMLModelFactory().newInstance(GraphicalElementAction.class);
			addToActions(newAction);
			return newAction;
		}

		@Override
		public GraphicalElementAction deleteAction(GraphicalElementAction anAction) {
			removeFromActions(anAction);
			anAction.delete();
			return anAction;
		}

		@Override
		public List<GraphicalElementSpecification<?, GR>> getGrSpecifications() {

			if ((grSpecifications == null) && getFMLModelFactory() != null) {
				initDefaultSpecifications();
			}
			return grSpecifications;
		}

		@Override
		public GraphicalElementSpecification<?, GR> getGraphicalElementSpecification(String featureName) {
			if (getGrSpecifications() != null) {
				for (GraphicalElementSpecification<?, GR> spec : getGrSpecifications()) {
					if (spec.getFeatureName().equals(featureName)) {
						return spec;
					}
				}
			}
			return null;
		}

		@Override
		public <T2> GraphicalElementSpecification<T2, GR> getGraphicalElementSpecification(GraphicalFeature<T2, ?> feature) {
			if (feature != null) {
				return (GraphicalElementSpecification<T2, GR>) getGraphicalElementSpecification(feature.getName());
			}
			return null;
		}

		@Override
		public List<GraphicalElementSpecification<?, GR>> _getDeclaredGRSpecifications() {
			List<GraphicalElementSpecification<?, GR>> returned = new ArrayList<>();
			if (getGrSpecifications() != null) {
				for (GraphicalElementSpecification<?, ?> spec : getGrSpecifications()) {
					if (spec.getValue().isSet()) {
						returned.add((GraphicalElementSpecification<?, GR>) spec);
					}
				}
			}
			return returned;
		}

		@Override
		public void _setDeclaredGRSpecifications(List<GraphicalElementSpecification<?, GR>> someSpecs) {
			if (someSpecs != null) {
				for (GraphicalElementSpecification<?, GR> s : someSpecs) {
					_addToDeclaredGRSpecifications(s);
				}
			}
		}

		private <T2> void registerGRSpecification(GraphicalElementSpecification<T2, GR> aSpec) {
			GraphicalElementSpecification<T2, GR> existingSpec = (GraphicalElementSpecification<T2, GR>) getGraphicalElementSpecification(
					aSpec.getFeatureName());
			if (existingSpec == null) {
				logger.warning("Cannot find any GraphicalElementSpecification matching " + aSpec.getFeatureName() + ". Ignoring...");
			}
			else {
				existingSpec.setValue(aSpec.getValue());
				existingSpec.setReadOnly(aSpec.getReadOnly());
			}
		}

		@Override
		public void _addToDeclaredGRSpecifications(GraphicalElementSpecification<?, GR> aSpec) {
			if (aSpec != null) {
				if (getGraphicalElementSpecification(aSpec.getFeatureName()) == null) {
					// This might happen during deserialization
					// Factory is not accessible yet, and thus, GRSpecs not available yet
					// We store it in pendingGRSpecs for future use
					pendingGRSpecs.add(aSpec);
				}
				else {
					registerGRSpecification(aSpec);
				}
			}
		}

		@Override
		public void _removeFromDeclaredGRSpecifications(GraphicalElementSpecification<?, GR> aSpec) {
			GraphicalElementSpecification<?, ?> existingSpec = getGraphicalElementSpecification(aSpec.getFeatureName());
			if (existingSpec == null) {
				logger.warning("Cannot find any GraphicalElementSpecification matching " + aSpec.getFeatureName() + ". Ignoring...");
			}
			else {
				existingSpec.setValue(null);
			}
		}

		@Override
		public ModelObjectActorReference<T> makeActorReference(T object, FlexoConceptInstance fci) {
			AbstractVirtualModelInstanceModelFactory<?> factory = fci.getFactory();
			ModelObjectActorReference<T> returned = factory.newInstance(ModelObjectActorReference.class);
			returned.setFlexoRole(this);
			returned.setFlexoConceptInstance(fci);
			returned.setModellingElement(object);
			return returned;
		}

		@Override
		public ModelSlot<?> getModelSlot() {
			ModelSlot<?> returned = super.getModelSlot();
			if (returned == null) {
				if (getOwningVirtualModel() != null && getOwningVirtualModel().getModelSlots(TypedDiagramModelSlot.class).size() > 0) {
					return getOwningVirtualModel().getModelSlots(TypedDiagramModelSlot.class).get(0);
				}
				if (getOwningVirtualModel() != null && getOwningVirtualModel().getModelSlots(FreeDiagramModelSlot.class).size() > 0) {
					return getOwningVirtualModel().getModelSlots(FreeDiagramModelSlot.class).get(0);
				}
			}
			return returned;
		}

		@Override
		public Class<? extends TechnologyAdapter> getRoleTechnologyAdapterClass() {
			return DiagramTechnologyAdapter.class;
		}

	}
}
