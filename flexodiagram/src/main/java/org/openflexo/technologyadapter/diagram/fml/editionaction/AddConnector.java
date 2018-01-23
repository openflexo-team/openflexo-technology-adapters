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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.connectors.ConnectorSpecification.ConnectorType;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
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
import org.openflexo.technologyadapter.diagram.fml.ConnectorRole;
import org.openflexo.technologyadapter.diagram.fml.LinkScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.binding.LinkSchemeBindingModel;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElementImpl;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.technologyadapter.diagram.model.action.LinkSchemeAction;

/**
 * This edition primitive addresses the creation of a new connector linking two shapes in a diagram
 * 
 * @author sylvain
 * 
 */
@ModelEntity
@ImplementationClass(AddConnector.AddConnectorImpl.class)
@XMLElement
@FML("AddConnector")
public interface AddConnector extends AddDiagramElementAction<DiagramConnector> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String FROM_SHAPE_KEY = "fromShape";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String TO_SHAPE_KEY = "toShape";

	@Getter(value = FROM_SHAPE_KEY)
	@XMLAttribute
	public DataBinding<DiagramShape> getFromShape();

	@Setter(FROM_SHAPE_KEY)
	public void setFromShape(DataBinding<DiagramShape> fromShape);

	@Getter(value = TO_SHAPE_KEY)
	@XMLAttribute
	public DataBinding<DiagramShape> getToShape();

	@Setter(TO_SHAPE_KEY)
	public void setToShape(DataBinding<DiagramShape> toShape);

	@Override
	public ConnectorRole getAssignedFlexoProperty();

	public static abstract class AddConnectorImpl extends AddDiagramElementActionImpl<DiagramConnector> implements AddConnector {

		private static final Logger logger = Logger.getLogger(LinkSchemeAction.class.getPackage().getName());

		public AddConnectorImpl() {
			super();
		}

		/*@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			FMLRepresentationOutput out = new FMLRepresentationOutput(context);
			out.append(getClass().getSimpleName() + " conformTo ConnectorSpecification from "
					+ (getReceiver().isValid() ? getReceiver().toString() : "null") + " {" + StringUtils.LINE_SEPARATOR, context);
			out.append(getGraphicalElementSpecificationFMLRepresentation(context), context);
			out.append("}", context);
			return out.toString();
		}*/

		/*@Override
		public List<ConnectorRole> getAvailablePatternRoles() {
			if (getFlexoConcept() != null) {
				return getFlexoConcept().getPatternRoles(ConnectorRole.class);
			}
			return null;
		}*/

		@Override
		public ConnectorRole getAssignedFlexoProperty() {
			FlexoRole superFlexoRole = super.getAssignedFlexoProperty();
			if (superFlexoRole instanceof ConnectorRole) {
				return (ConnectorRole) superFlexoRole;
			}
			else if (superFlexoRole != null) {
				// logger.warning("Unexpected pattern property of type " + superPatternRole.getClass().getSimpleName());
				return null;
			}
			return null;
		}

		public DiagramShape getFromShape(RunTimeEvaluationContext evaluationContext) {
			if (evaluationContext instanceof FlexoBehaviourAction && getAssignedFlexoProperty() != null
					&& !getAssignedFlexoProperty().getStartShapeAsDefinedInAction()) {
				return ((FlexoBehaviourAction<?, ?, ?>) evaluationContext).getFlexoConceptInstance()
						.getFlexoActor(getAssignedFlexoProperty().getStartShapeRole());
			}
			else {
				try {
					return getFromShape().getBindingValue(evaluationContext);
				} catch (TypeMismatchException e) {
					e.printStackTrace();
				} catch (NullReferenceException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				return null;
			}
		}

		public DiagramShape getToShape(RunTimeEvaluationContext evaluationContext) {
			if (evaluationContext instanceof FlexoBehaviourAction && getAssignedFlexoProperty() != null
					&& !getAssignedFlexoProperty().getEndShapeAsDefinedInAction()) {
				return ((FlexoBehaviourAction<?, ?, ?>) evaluationContext).getFlexoConceptInstance()
						.getFlexoActor(getAssignedFlexoProperty().getEndShapeRole());
			}
			else {
				try {
					return getToShape().getBindingValue(evaluationContext);
				} catch (TypeMismatchException e) {
					e.printStackTrace();
				} catch (NullReferenceException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				return null;
			}
		}

		@Override
		public String toString() {
			return "AddConnector " + Integer.toHexString(hashCode()) + " patternRole=" + getAssignedFlexoProperty();
		}

		/*@Override
		public ConnectorRole getPatternRole() {
			try {
				return super.getPatternRole();
			} catch (ClassCastException e) {
				logger.warning("Unexpected pattern property type");
				setPatternRole(null);
				return null;
			}
		}*/

		// FIXME: if we remove this useless code, some FIB won't work (see FlexoConceptView.fib, inspect an AddIndividual)
		// Need to be fixed in KeyValueProperty.java
		/*@Override
		public void setPatternRole(ConnectorRole patternRole) {
			super.setPatternRole(patternRole);
		}*/

		private DataBinding<DiagramShape> fromShape;
		private DataBinding<DiagramShape> toShape;

		@Override
		public DataBinding<DiagramShape> getFromShape() {
			if (fromShape == null) {
				fromShape = new DataBinding<DiagramShape>(this, DiagramShape.class, BindingDefinitionType.GET);
				fromShape.setBindingName("fromShape");
			}
			return fromShape;
		}

		@Override
		public void setFromShape(DataBinding<DiagramShape> fromShape) {
			if (fromShape != null) {
				fromShape.setOwner(this);
				fromShape.setBindingName("fromShape");
				fromShape.setDeclaredType(DiagramShape.class);
				fromShape.setBindingDefinitionType(BindingDefinitionType.GET);
			}
			this.fromShape = fromShape;
			notifiedBindingChanged(this.fromShape);
		}

		@Override
		public DataBinding<DiagramShape> getToShape() {
			if (toShape == null) {
				toShape = new DataBinding<DiagramShape>(this, DiagramShape.class, BindingDefinitionType.GET);
				toShape.setBindingName("toShape");
			}
			return toShape;
		}

		@Override
		public void setToShape(DataBinding<DiagramShape> toShape) {
			if (toShape != null) {
				toShape.setOwner(this);
				toShape.setBindingName("toShape");
				toShape.setDeclaredType(DiagramShape.class);
				toShape.setBindingDefinitionType(BindingDefinitionType.GET);
			}
			this.toShape = toShape;
			notifiedBindingChanged(this.toShape);
		}

		@Override
		public Type getAssignableType() {
			return DiagramConnector.class;
		}

		/*@Override
		public boolean isAssignationRequired() {
			return true;
		}*/

		@Override
		public DiagramConnector execute(RunTimeEvaluationContext evaluationContext) {

			System.out.println("Creating diagram connector");

			DiagramShape fromShape = getFromShape(evaluationContext);
			DiagramShape toShape = getToShape(evaluationContext);

			System.out.println("From shape = " + fromShape);
			System.out.println("To shape = " + toShape);

			// NPE Protection
			if (fromShape != null && toShape != null) {

				Diagram diagram = fromShape.getDiagram();
				DiagramFactory factory = diagram.getDiagramFactory();

				DiagramConnector newConnector = factory.newInstance(DiagramConnector.class);
				newConnector.setStartShape(fromShape);
				newConnector.setEndShape(toShape);

				ConnectorGraphicalRepresentation grToUse = null;

				if (getAssignedFlexoProperty() != null) {

					// If no GR is defined for this shape, create a default one
					if (getAssignedFlexoProperty().getGraphicalRepresentation() == null) {
						System.out.println("No GR, creating ");
						grToUse = factory.makeConnectorGraphicalRepresentation(ConnectorType.LINE);
						System.out.println("Creating " + grToUse);
						// getAssignedFlexoProperty().setGraphicalRepresentation(grToUse);
					}
					else {
						grToUse = getAssignedFlexoProperty().getGraphicalRepresentation();
					}

					ConnectorGraphicalRepresentation newGR = factory.makeConnectorGraphicalRepresentation();
					newGR.setsWith(grToUse);
					newConnector.setGraphicalRepresentation(newGR);

					// Handle default ShapeSpecification when not set
					if (newGR.getConnectorSpecification() == null) {
						newGR.setConnectorSpecification(factory.makeConnector(ConnectorType.LINE));
					}
					// Handle default Foreground when not set
					if (newGR.getForeground() == null) {
						newGR.setForeground(factory.makeDefaultForegroundStyle());
					}
				}

				DiagramContainerElement<?> parent = DiagramElementImpl.getFirstCommonAncestor(fromShape, toShape);
				if (parent == null) {
					throw new IllegalArgumentException("No common ancestor");
				}

				System.out.println("Parent = " + parent);

				ConnectorRole fr = getAssignedFlexoProperty();
				if (fr != null) {
					if (fr.getGraphicalRepresentation() != null) {
						grToUse = getAssignedFlexoProperty().getGraphicalRepresentation();
					}
				}
				else {
					logger.warning("INVESTIGATE your ViewPoint, No FlexoRole defined for action " + this.getName());
				}

				System.out.println("Et hop, on ajoute le connecteur");

				parent.addToConnectors(newConnector);

				if (evaluationContext instanceof FlexoBehaviourAction) {
					((FlexoBehaviourAction<?, ?, ?>) evaluationContext).hasPerformedAction(this, newConnector);
				}

				// Register reference
				// newConnector.registerFlexoConceptReference(action.getFlexoConceptInstance());

				if (logger.isLoggable(Level.FINE)) {
					logger.fine("Added connector " + newConnector + " under " + parent);
				}

				// Well, not easy to understand here
				// The new connector has well be added to the diagram, and the drawing (which listen to the diagram) has well received the
				// event
				// The drawing is now up-to-date... but there is something wrong if we are in FML-controlled mode.
				// Since the connector has been added BEFORE the FlexoConceptInstance has been set, the drawing only knows about the
				// DiagamShape,
				// and not about an FMLControlledDiagramShape. That's why we need to notify again the new diagram element's parent, to be
				// sure that the Drawing can discover that the new shape is FML-controlled
				newConnector.getParent().getPropertyChangeSupport().firePropertyChange("invalidate", null, newConnector.getParent());

				return newConnector;
			}
			else {
				logger.warning("AddConnector failed due to null source or target shape");
				return null;
			}
		}

		/*@Override
		public void finalizePerformAction(FlexoBehaviourAction action, DiagramConnector newConnector) {
			super.finalizePerformAction(action, newConnector);
		
			// Well, not easy to understand here
			// The new connector has well be added to the diagram, and the drawing (which listen to the diagram) has well received the event
			// The drawing is now up-to-date... but there is something wrong if we are in FML-controlled mode.
			// Since the connector has been added BEFORE the FlexoConceptInstance has been set, the drawing only knows about the
			// DiagamShape,
			// and not about an FMLControlledDiagramShape. That's why we need to notify again the new diagram element's parent, to be
			// sure that the Drawing can discover that the new shape is FML-controlled
			newConnector.getParent().getPropertyChangeSupport().firePropertyChange("invalidate", null, newConnector.getParent());
		}*/

	}

	@DefineValidationRule
	public static class AddConnectorActionMustAdressAValidConnectorRole
			extends ValidationRule<AddConnectorActionMustAdressAValidConnectorRole, AddConnector> {
		public AddConnectorActionMustAdressAValidConnectorRole() {
			super(AddConnector.class, "add_connector_action_must_address_a_valid_connector_pattern_role");
		}

		@Override
		public ValidationIssue<AddConnectorActionMustAdressAValidConnectorRole, AddConnector> applyValidation(AddConnector action) {
			if (action.getAssignedFlexoProperty() == null && action.getOwner() instanceof AssignationAction) {
				Vector<FixProposal<AddConnectorActionMustAdressAValidConnectorRole, AddConnector>> v = new Vector<FixProposal<AddConnectorActionMustAdressAValidConnectorRole, AddConnector>>();
				for (ConnectorRole pr : action.getFlexoConcept().getDeclaredProperties(ConnectorRole.class)) {
					v.add(new SetsFlexoRole(pr));
				}
				return new ValidationError<AddConnectorActionMustAdressAValidConnectorRole, AddConnector>(this, action,
						"add_connector_action_does_not_address_a_valid_connector_flexo_role", v);
			}
			return null;
		}

		protected static class SetsFlexoRole extends FixProposal<AddConnectorActionMustAdressAValidConnectorRole, AddConnector> {

			private final ConnectorRole flexoRole;

			public SetsFlexoRole(ConnectorRole flexoRole) {
				super("assign_action_to_flexo_role_($flexoRole.flexoRoleName)");
				this.flexoRole = flexoRole;
			}

			public ConnectorRole getFlexoRole() {
				return flexoRole;
			}

			@Override
			protected void fixAction() {
				AddConnector action = getValidable();
				((AssignationAction) action.getOwner()).setAssignation(new DataBinding<Object>(flexoRole.getRoleName()));
			}

		}
	}

	@DefineValidationRule
	public static class AddConnectorActionMustHaveAValidStartingShape
			extends ValidationRule<AddConnectorActionMustHaveAValidStartingShape, AddConnector> {
		public AddConnectorActionMustHaveAValidStartingShape() {
			super(AddConnector.class, "add_connector_action_must_have_a_valid_starting_shape");
		}

		@Override
		public ValidationIssue<AddConnectorActionMustHaveAValidStartingShape, AddConnector> applyValidation(AddConnector action) {
			ConnectorRole pr = action.getAssignedFlexoProperty();
			DataBinding<DiagramShape> db = action.getFromShape();
			if (pr != null && pr.getStartShapeAsDefinedInAction() && !(db.isSet() && db.isValid())) {
				Vector<FixProposal<AddConnectorActionMustHaveAValidStartingShape, AddConnector>> v = new Vector<FixProposal<AddConnectorActionMustHaveAValidStartingShape, AddConnector>>();
				if (action.getRootOwner() instanceof LinkScheme) {
					FlexoConcept targetFlexoConcept = ((LinkScheme) action.getRootOwner()).getFromTargetFlexoConcept();
					if (targetFlexoConcept != null) {
						for (ShapeRole spr : action.getFlexoConcept().getDeclaredProperties(ShapeRole.class)) {
							v.add(new SetsStartingShapeToStartTargetShape(targetFlexoConcept, spr));
						}
					}
				}
				for (ShapeRole spr : action.getFlexoConcept().getDeclaredProperties(ShapeRole.class)) {
					v.add(new SetsStartingShapeToShape(spr));
				}
				return new ValidationError<AddConnectorActionMustHaveAValidStartingShape, AddConnector>(this, action,
						"add_connector_action_does_not_have_a_valid_starting_shape", v);
			}
			return null;
		}

		protected static class SetsStartingShapeToShape extends FixProposal<AddConnectorActionMustHaveAValidStartingShape, AddConnector> {

			private final ShapeRole patternRole;

			public SetsStartingShapeToShape(ShapeRole patternRole) {
				super("sets_starting_shape_to_($patternRole.patternRoleName)");
				this.patternRole = patternRole;
			}

			public ShapeRole getPatternRole() {
				return patternRole;
			}

			@Override
			protected void fixAction() {
				AddConnector action = getValidable();
				action.setFromShape(new DataBinding<DiagramShape>(patternRole.getRoleName()));
			}
		}

		protected static class SetsStartingShapeToStartTargetShape
				extends FixProposal<AddConnectorActionMustHaveAValidStartingShape, AddConnector> {

			private final FlexoConcept target;
			private final ShapeRole patternRole;

			public SetsStartingShapeToStartTargetShape(FlexoConcept target, ShapeRole patternRole) {
				super("sets_starting_shape_to_fromTarget.($patternRole.patternRoleName)");
				this.target = target;
				this.patternRole = patternRole;
			}

			public ShapeRole getPatternRole() {
				return patternRole;
			}

			public FlexoConcept getTarget() {
				return target;
			}

			@Override
			protected void fixAction() {
				AddConnector action = getValidable();
				action.setFromShape(new DataBinding<DiagramShape>(LinkSchemeBindingModel.FROM_TARGET + "." + patternRole.getRoleName()));
			}
		}

	}

	@DefineValidationRule
	public static class AddConnectorActionMustHaveAValidEndingShape
			extends ValidationRule<AddConnectorActionMustHaveAValidEndingShape, AddConnector> {
		public AddConnectorActionMustHaveAValidEndingShape() {
			super(AddConnector.class, "add_connector_action_must_have_a_valid_ending_shape");
		}

		@Override
		public ValidationIssue<AddConnectorActionMustHaveAValidEndingShape, AddConnector> applyValidation(AddConnector action) {
			ConnectorRole pr = action.getAssignedFlexoProperty();
			DataBinding<DiagramShape> shape = action.getToShape();
			if (pr != null && pr.getEndShapeAsDefinedInAction() && !(shape.isSet() && shape.isValid())) {
				Vector<FixProposal<AddConnectorActionMustHaveAValidEndingShape, AddConnector>> v = new Vector<FixProposal<AddConnectorActionMustHaveAValidEndingShape, AddConnector>>();
				if (action.getRootOwner() instanceof LinkScheme) {
					FlexoConcept targetFlexoConcept = ((LinkScheme) action.getRootOwner()).getToTargetFlexoConcept();
					if (targetFlexoConcept != null) {
						for (ShapeRole spr : action.getFlexoConcept().getDeclaredProperties(ShapeRole.class)) {
							v.add(new SetsEndingShapeToToTargetShape(targetFlexoConcept, spr));
						}
					}
				}
				for (ShapeRole spr : action.getFlexoConcept().getDeclaredProperties(ShapeRole.class)) {
					v.add(new SetsEndingShapeToShape(spr));
				}
				return new ValidationError<AddConnectorActionMustHaveAValidEndingShape, AddConnector>(this, action,
						"add_connector_action_does_not_have_a_valid_ending_shape", v);
			}
			return null;
		}

		protected static class SetsEndingShapeToShape extends FixProposal<AddConnectorActionMustHaveAValidEndingShape, AddConnector> {

			private final ShapeRole patternRole;

			public SetsEndingShapeToShape(ShapeRole patternRole) {
				super("sets_ending_shape_to_($patternRole.patternRoleName)");
				this.patternRole = patternRole;
			}

			public ShapeRole getPatternRole() {
				return patternRole;
			}

			@Override
			protected void fixAction() {
				AddConnector action = getValidable();
				action.setToShape(new DataBinding<DiagramShape>(patternRole.getRoleName()));
			}
		}

		protected static class SetsEndingShapeToToTargetShape
				extends FixProposal<AddConnectorActionMustHaveAValidEndingShape, AddConnector> {

			private final FlexoConcept target;
			private final ShapeRole patternRole;

			public SetsEndingShapeToToTargetShape(FlexoConcept target, ShapeRole patternRole) {
				super("sets_ending_shape_to_toTarget.($patternRole.patternRoleName)");
				this.target = target;
				this.patternRole = patternRole;
			}

			public ShapeRole getPatternRole() {
				return patternRole;
			}

			public FlexoConcept getTarget() {
				return target;
			}

			@Override
			protected void fixAction() {
				AddConnector action = getValidable();
				action.setToShape(new DataBinding<DiagramShape>(LinkSchemeBindingModel.TO_TARGET + "." + patternRole.getRoleName()));
			}
		}

	}
}
