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

package org.openflexo.technologyadapter.diagram.fml.editionaction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.connie.type.TypeUtils;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.rt.RunTimeEvaluationContext;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.model.annotations.DefineValidationRule;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.model.validation.FixProposal;
import org.openflexo.model.validation.ValidationError;
import org.openflexo.model.validation.ValidationIssue;
import org.openflexo.model.validation.ValidationRule;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.GraphicalElementRole;
import org.openflexo.technologyadapter.diagram.fml.GraphicalFeature;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

@ModelEntity
@ImplementationClass(GraphicalAction.GraphicalActionImpl.class)
@XMLElement
@FML("GraphicalAction")
public interface GraphicalAction extends DiagramAction<TypedDiagramModelSlot, DiagramElement<?>> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String SUBJECT_KEY = "subject";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String VALUE_KEY = "value";
	@PropertyIdentifier(type = String.class)
	public static final String GRAPHICAL_FEATURE_NAME_KEY = "graphicalFeatureName";

	@Getter(value = SUBJECT_KEY)
	@XMLAttribute
	public DataBinding<DiagramElement<?>> getSubject();

	@Setter(SUBJECT_KEY)
	public void setSubject(DataBinding<DiagramElement<?>> subject);

	@Getter(value = VALUE_KEY)
	@XMLAttribute
	public DataBinding<Object> getValue();

	@Setter(VALUE_KEY)
	public void setValue(DataBinding<Object> value);

	@Getter(value = GRAPHICAL_FEATURE_NAME_KEY)
	@XMLAttribute(xmlTag = "feature")
	public String _getGraphicalFeatureName();

	@Setter(GRAPHICAL_FEATURE_NAME_KEY)
	public void _setGraphicalFeatureName(String graphicalFeatureName);

	public GraphicalFeature<?, ?> getGraphicalFeature();

	public void setGraphicalFeature(GraphicalFeature<?, ?> graphicalFeature);

	public List<GraphicalFeature<?, ?>> getAvailableGraphicalFeatures();

	public static abstract class GraphicalActionImpl extends
			TechnologySpecificActionDefiningReceiverImpl<TypedDiagramModelSlot, Diagram, DiagramElement<?>> implements GraphicalAction {

		private static final Logger logger = Logger.getLogger(GraphicalAction.class.getPackage().getName());

		private GraphicalFeature<?, ?> graphicalFeature = null;
		private DataBinding<Object> value;

		@Override
		public DiagramTechnologyAdapter getModelSlotTechnologyAdapter() {
			return (DiagramTechnologyAdapter) super.getModelSlotTechnologyAdapter();
		}

		public java.lang.reflect.Type getGraphicalFeatureType() {
			if (getGraphicalFeature() != null) {
				return getGraphicalFeature().getType();
			}
			return Object.class;
		}

		public Object getValue(FlexoBehaviourAction<?, ?, ?> action) {
			try {
				return getValue().getBindingValue(action);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public DataBinding<Object> getValue() {
			if (value == null) {
				value = new DataBinding<>(this, getGraphicalFeatureType(), BindingDefinitionType.GET);
				value.setBindingName("value");
			}
			return value;
		}

		@Override
		public void setValue(DataBinding<Object> value) {
			if (value != null) {
				value.setOwner(this);
				value.setBindingName("value");
				value.setDeclaredType(getGraphicalFeatureType());
				value.setBindingDefinitionType(BindingDefinitionType.GET);
			}
			this.value = value;
		}

		@Override
		public GraphicalFeature<?, ?> getGraphicalFeature() {
			if (graphicalFeature == null) {
				// System.out.println("Attempt to lookup " + _graphicalFeatureName + " from " + getAvailableGraphicalFeatures());
				if (_graphicalFeatureName != null && getAvailableGraphicalFeatures() != null) {
					for (GraphicalFeature<?, ?> GF : getAvailableGraphicalFeatures()) {
						if (GF.getName().equals(_graphicalFeatureName)) {
							return GF;
						}
					}
				}
			}
			return graphicalFeature;
		}

		@Override
		public void setGraphicalFeature(GraphicalFeature<?, ?> graphicalFeature) {
			this.graphicalFeature = graphicalFeature;
		}

		private List<GraphicalFeature<?, ?>> availableFeatures = null;

		@Override
		public List<GraphicalFeature<?, ?>> getAvailableGraphicalFeatures() {
			if (availableFeatures == null) {
				// System.out.println("On calcule la liste des features, subject=" + getSubject());
				if (getSubject().isSet() && getSubject().isValid()) {
					Class<?> accessedClass = TypeUtils.getBaseClass(getSubject().getAnalyzedType());
					// System.out.println("What about: " + accessedClass + " from " + getSubject().getAnalyzedType());
					if (DiagramElement.class.isAssignableFrom(accessedClass)) {
						availableFeatures = new ArrayList<>();
						for (GraphicalFeature<?, ?> GF : GraphicalElementRole.AVAILABLE_FEATURES) {
							availableFeatures.add(GF);
						}
						if (DiagramShape.class.isAssignableFrom(accessedClass)) {
							for (GraphicalFeature<?, ?> GF : ShapeRole.AVAILABLE_SHAPE_FEATURES) {
								availableFeatures.add(GF);
							}
						}
						if (DiagramConnector.class.isAssignableFrom(accessedClass)) {
							for (GraphicalFeature<?, ?> GF : ConnectorRole.AVAILABLE_CONNECTOR_FEATURES) {
								availableFeatures.add(GF);
							}
						}
					}
				}
			}
			return availableFeatures;
		}

		private String _graphicalFeatureName = null;

		@Override
		public String _getGraphicalFeatureName() {
			if (getGraphicalFeature() == null) {
				return _graphicalFeatureName;
			}
			return getGraphicalFeature().getName();
		}

		@Override
		public void _setGraphicalFeatureName(String featureName) {
			_graphicalFeatureName = featureName;
		}

		private DataBinding<DiagramElement<?>> subject;

		@Override
		public DataBinding<DiagramElement<?>> getSubject() {
			if (subject == null) {
				subject = new DataBinding<>(this, DiagramElement.class, DataBinding.BindingDefinitionType.GET);
				subject.setBindingName("subject");
			}
			return subject;
		}

		@Override
		public void setSubject(DataBinding<DiagramElement<?>> subject) {
			if (subject != null) {
				subject.setOwner(this);
				subject.setBindingName("subject");
				subject.setDeclaredType(DiagramElement.class);
				subject.setBindingDefinitionType(BindingDefinitionType.GET);
			}
			this.subject = subject;
		}

		public DiagramElement<?> getSubject(RunTimeEvaluationContext evaluationContext) {
			try {
				return getSubject().getBindingValue(evaluationContext);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void notifiedBindingChanged(DataBinding<?> binding) {
			super.notifiedBindingChanged(binding);
			if (binding == getSubject()) {
				availableFeatures = null;
				System.out.println("OK, on change les features disponibles, et on trouve: " + getAvailableGraphicalFeatures());
				getPropertyChangeSupport().firePropertyChange("availableGraphicalFeatures", null, getAvailableGraphicalFeatures());
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public DiagramElement<?> execute(RunTimeEvaluationContext evaluationContext) {
			logger.info("Perform graphical action " + evaluationContext);
			if (getGraphicalFeature() == null) {
				logger.warning("No graphical feature supplied, aborting");
				return null;
			}

			DiagramElement<?> graphicalElement = getSubject(evaluationContext);
			Object value = null;
			try {
				value = getValue().getBindingValue(evaluationContext);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Element is " + graphicalElement);
				logger.fine("Feature is " + getGraphicalFeature());
				logger.fine("Value is " + value);
			}

			Object castedValue = null;
			castedValue = TypeUtils.castTo(value, ((GraphicalFeature) getGraphicalFeature()).getType());

			((GraphicalFeature) getGraphicalFeature()).applyToGraphicalRepresentation(graphicalElement.getGraphicalRepresentation(),
					castedValue);
			return graphicalElement;
		}

		@Override
		public Type getAssignableType() {
			if (getSubject() != null && getSubject().isSet() && getSubject().isValid()) {
				return getSubject().getAnalyzedType();
			}
			return DiagramElement.class;
		}

		/*@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			FMLRepresentationOutput out = new FMLRepresentationOutput(context);
			out.append((getSubject() != null ? getSubject().toString() : "?") + "."
					+ (getGraphicalFeature() != null ? getGraphicalFeature().getName() : "?") + "="
					+ (getValue() != null ? getValue().toString() : "?"), context);
			return out.toString();
		}*/

		@Override
		public String getStringRepresentation() {
			return getHeaderContext() + (getSubject() != null ? getSubject().toString() : "?") + "."
					+ (getGraphicalFeature() != null ? getGraphicalFeature().getName() : "?") + "="
					+ (getValue() != null ? getValue().toString() : "?");
		}

	}

	@DefineValidationRule
	public static class GraphicalActionMustHaveASubject extends ValidationRule<GraphicalActionMustHaveASubject, GraphicalAction> {
		public GraphicalActionMustHaveASubject() {
			super(GraphicalAction.class, "graphical_action_must_have_a_subject");
		}

		@Override
		public ValidationIssue<GraphicalActionMustHaveASubject, GraphicalAction> applyValidation(GraphicalAction graphicalAction) {
			if (graphicalAction.getSubject().isSet() && graphicalAction.getSubject().isValid()) {
				return null;
			}
			else {
				Vector<FixProposal<GraphicalActionMustHaveASubject, GraphicalAction>> v = new Vector<>();
				for (ShapeRole pr : graphicalAction.getFlexoConcept().getDeclaredProperties(ShapeRole.class)) {
					v.add(new SetsFlexoRoleForSubject(pr));
				}
				for (ConnectorRole pr : graphicalAction.getFlexoConcept().getDeclaredProperties(ConnectorRole.class)) {
					v.add(new SetsFlexoRoleForSubject(pr));
				}
				return new ValidationError<>(this, graphicalAction, "graphical_action_has_no_valid_subject", v);
			}
		}

		protected static class SetsFlexoRoleForSubject extends FixProposal<GraphicalActionMustHaveASubject, GraphicalAction> {

			private final GraphicalElementRole<?, ?> flexoRole;

			public SetsFlexoRoleForSubject(GraphicalElementRole<?, ?> flexoRole) {
				super("set_subject_to_($flexoRole.flexoRoleName)");
				this.flexoRole = flexoRole;
			}

			public GraphicalElementRole<?, ?> getFlexoRole() {
				return flexoRole;
			}

			@Override
			protected void fixAction() {
				GraphicalAction graphicalAction = getValidable();
				graphicalAction.setSubject(new DataBinding<DiagramElement<?>>(flexoRole.getRoleName()));
			}

		}
	}

	@DefineValidationRule
	public static class GraphicalActionMustDefineAValue extends BindingIsRequiredAndMustBeValid<GraphicalAction> {
		public GraphicalActionMustDefineAValue() {
			super("'value'_binding_is_not_valid", GraphicalAction.class);
		}

		@Override
		public DataBinding<Object> getBinding(GraphicalAction object) {
			return object.getValue();
		}

	}

}
