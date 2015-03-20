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

import org.openflexo.connie.BindingModel;
import org.openflexo.connie.BindingVariable;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.shapes.ShapeSpecification.ShapeType;
import org.openflexo.fib.annotation.FIBPanel;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.fml.FMLRepresentationContext;
import org.openflexo.foundation.fml.FMLRepresentationContext.FMLRepresentationOutput;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.annotations.FMLProperty;
import org.openflexo.foundation.fml.editionaction.AssignationAction;
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
import org.openflexo.technologyadapter.diagram.fml.DropScheme;
import org.openflexo.technologyadapter.diagram.fml.ShapeRole;
import org.openflexo.technologyadapter.diagram.fml.binding.DiagramBehaviourBindingModel;
import org.openflexo.technologyadapter.diagram.fml.binding.DropSchemeBindingModel;
import org.openflexo.technologyadapter.diagram.model.Diagram;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.technologyadapter.diagram.model.DiagramElement;
import org.openflexo.technologyadapter.diagram.model.DiagramFactory;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.StringUtils;

/**
 * This edition primitive addresses the creation of a new shape in a diagram
 * 
 * @author sylvain
 * 
 */
@FIBPanel("Fib/AddShapePanel.fib")
@ModelEntity
@ImplementationClass(AddShape.AddShapeImpl.class)
@XMLElement
@FML("AddShape")
public interface AddShape extends AddDiagramElementAction<DiagramShape> {

	@PropertyIdentifier(type = DataBinding.class)
	public static final String CONTAINER_KEY = "container";
	@PropertyIdentifier(type = boolean.class)
	public static final String EXTEND_PARENT_BOUNDS_TO_HOST_THIS_SHAPE_KEY = "extendParentBoundsToHostThisShape";

	@Getter(value = CONTAINER_KEY)
	@XMLAttribute
	@FMLProperty(value = CONTAINER_KEY, required = true)
	public DataBinding<DiagramContainerElement<?>> getContainer();

	@Setter(CONTAINER_KEY)
	public void setContainer(DataBinding<DiagramContainerElement<?>> container);

	@Getter(value = EXTEND_PARENT_BOUNDS_TO_HOST_THIS_SHAPE_KEY, defaultValue = "false")
	@XMLAttribute
	@FMLProperty(value = EXTEND_PARENT_BOUNDS_TO_HOST_THIS_SHAPE_KEY, defaultValue = "false")
	public boolean getExtendParentBoundsToHostThisShape();

	@Setter(EXTEND_PARENT_BOUNDS_TO_HOST_THIS_SHAPE_KEY)
	public void setExtendParentBoundsToHostThisShape(boolean extendParentBoundsToHostThisShape);

	@Override
	public ShapeRole getAssignedFlexoProperty();

	public static abstract class AddShapeImpl extends AddDiagramElementActionImpl<DiagramShape> implements AddShape {

		private static final Logger logger = Logger.getLogger(AddShape.class.getPackage().getName());

		private boolean extendParentBoundsToHostThisShape = false;

		public AddShapeImpl() {
			super();
		}

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			FMLRepresentationOutput out = new FMLRepresentationOutput(context);
			/*if (getAssignation().isSet()) {
				out.append(getAssignation().toString() + " = (", context);
			}*/
			out.append(getClass().getSimpleName() + " conformTo ShapeSpecification from " + getModelSlot().getName() + " {"
					+ StringUtils.LINE_SEPARATOR, context);
			out.append(getGraphicalElementSpecificationFMLRepresentation(context), context);
			out.append("}", context);
			/*if (getAssignation().isSet()) {
				out.append(")", context);
			}*/
			return out.toString();
		}

		public DiagramContainerElement<?> getContainer(FlexoBehaviourAction action) {
			if (getAssignedFlexoProperty() != null && !getAssignedFlexoProperty().getParentShapeAsDefinedInAction()) {
				FlexoObject returned = action.getFlexoConceptInstance().getFlexoActor(getAssignedFlexoProperty().getParentShapeRole());
				return action.getFlexoConceptInstance().getFlexoActor(getAssignedFlexoProperty().getParentShapeRole());
			} else {
				BindingModel bm = getContainer().getOwner().getBindingModel();
				for (int i = 0; i < bm.getBindingVariablesCount(); i++) {
					BindingVariable bv = bm.getBindingVariableAt(i);
				}
				try {
					if (getContainer().getBindingValue(action) != null) {
						return getContainer().getBindingValue(action);
					} else {
						// In case the toplevel is not specified set o the diagram top level.
						return (DiagramContainerElement<?>) getModelSlotInstance(action).getAccessedResourceData();
					}

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
		public ShapeRole getAssignedFlexoProperty() {
			FlexoRole superFlexoRole = super.getAssignedFlexoProperty();
			if (superFlexoRole instanceof ShapeRole) {
				return (ShapeRole) superFlexoRole;
			} else if (superFlexoRole != null) {
				// logger.warning("Unexpected pattern property of type " + superPatternRole.getClass().getSimpleName());
				return null;
			}
			return null;
		}

		private DataBinding<DiagramContainerElement<?>> container;

		@Override
		public DataBinding<DiagramContainerElement<?>> getContainer() {
			if (container == null) {
				container = new DataBinding<DiagramContainerElement<?>>(this, DiagramContainerElement.class, BindingDefinitionType.GET);
				container.setBindingName("container");
			}
			return container;
		}

		@Override
		public void setContainer(DataBinding<DiagramContainerElement<?>> container) {
			if (container != null) {
				container.setOwner(this);
				container.setBindingName("container");
				container.setDeclaredType(DiagramContainerElement.class);
				container.setBindingDefinitionType(BindingDefinitionType.GET);
			}
			this.container = container;
			notifiedBindingChanged(this.container);
		}

		@Override
		public boolean getExtendParentBoundsToHostThisShape() {
			return extendParentBoundsToHostThisShape;
		}

		@Override
		public void setExtendParentBoundsToHostThisShape(boolean extendParentBoundsToHostThisShape) {
			this.extendParentBoundsToHostThisShape = extendParentBoundsToHostThisShape;
		}

		@Override
		public Type getAssignableType() {
			return DiagramShape.class;
		}

		/*@Override
		public boolean isAssignationRequired() {
			return true;
		}*/

		@Override
		public DiagramShape execute(FlexoBehaviourAction action) {
			DiagramContainerElement<?> container = getContainer(action);
			Diagram diagram = container.getDiagram();

			DiagramFactory factory = diagram.getDiagramFactory();
			DiagramShape newShape = factory.newInstance(DiagramShape.class);

			ShapeGraphicalRepresentation grToUse = null;

			// If an overriden graphical representation is defined, use it
			/*if (action.getOverridingGraphicalRepresentation(getPatternRole()) != null) {
				grToUse = action.getOverridingGraphicalRepresentation(getPatternRole());
			} else*/if (getAssignedFlexoProperty().getGraphicalRepresentation() != null) {
				grToUse = getAssignedFlexoProperty().getGraphicalRepresentation();
			}

			ShapeGraphicalRepresentation newGR = factory.makeShapeGraphicalRepresentation();
			newGR.setsWith(grToUse);
			newShape.setGraphicalRepresentation(newGR);

			// Handle default ShapeSpecification when not set
			if (newGR.getShapeSpecification() == null) {
				newGR.setShapeSpecification(factory.makeShape(ShapeType.RECTANGLE));
				newGR.setWidth(50);
				newGR.setHeight(40);
			}
			// Handle default Foreground when not set
			if (newGR.getForeground() == null) {
				newGR.setForeground(factory.makeDefaultForegroundStyle());
			}
			// Handle default Background when not set
			if (newGR.getBackground() == null) {
				newGR.setBackground(factory.makeDefaultBackgroundStyle());
			}
			// Handle default Border when not set
			if (newGR.getBorder() == null) {
				newGR.setBorder(factory.makeShapeBorder());
			}

			// Register reference
			// newShape.registerFlexoConceptReference(action.getFlexoConceptInstance());

			if (container == null) {
				logger.warning("When adding shape, cannot find container for action " + getAssignedFlexoProperty() + " container="
						+ getContainer(action) + " container=" + getContainer());
				return null;
			}

			container.addToShapes(newShape);

			action.hasPerformedAction(this, newShape);

			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Added shape " + newShape + " under " + container);
			}

			// Well, not easy to understand here
			// The new shape has well be added to the diagram, and the drawing (which listen to the diagram) has well received the event
			// The drawing is now up-to-date... but there is something wrong if we are in FML-controlled mode.
			// Since the shape has been added BEFORE the FlexoConceptInstance has been set, the drawing only knows about the DiagamShape,
			// and not about an FMLControlledDiagramShape. That's why we need to notify again the new diagram element's parent, to be
			// sure that the Drawing can discover that the new shape is FML-controlled
			newShape.getParent().getPropertyChangeSupport().firePropertyChange(DiagramElement.INVALIDATE, null, newShape.getParent());

			System.out.println("Added shape " + newShape);
			return newShape;
		}
		/*@Override
		public void finalizePerformAction(FlexoBehaviourAction action, DiagramShape newShape) {
			super.finalizePerformAction(action, newShape);

			// Well, not easy to understand here
			// The new shape has well be added to the diagram, and the drawing (which listen to the diagram) has well received the event
			// The drawing is now up-to-date... but there is something wrong if we are in FML-controlled mode.
			// Since the shape has been added BEFORE the FlexoConceptInstance has been set, the drawing only knows about the DiagamShape,
			// and not about an FMLControlledDiagramShape. That's why we need to notify again the new diagram element's parent, to be
			// sure that the Drawing can discover that the new shape is FML-controlled
			newShape.getParent().getPropertyChangeSupport().firePropertyChange(DiagramElement.INVALIDATE, null, newShape.getParent());
		}*/
	}

	@DefineValidationRule
	public static class AddShapeActionMustAdressAValidShapeRole extends ValidationRule<AddShapeActionMustAdressAValidShapeRole, AddShape> {
		public AddShapeActionMustAdressAValidShapeRole() {
			super(AddShape.class, "add_shape_action_must_address_a_valid_shape_pattern_role");
		}

		@Override
		public ValidationIssue<AddShapeActionMustAdressAValidShapeRole, AddShape> applyValidation(AddShape action) {
			if (action.getAssignedFlexoProperty() == null && action.getOwner() instanceof AssignationAction) {
				Vector<FixProposal<AddShapeActionMustAdressAValidShapeRole, AddShape>> v = new Vector<FixProposal<AddShapeActionMustAdressAValidShapeRole, AddShape>>();
				for (ShapeRole pr : action.getFlexoConcept().getDeclaredProperties(ShapeRole.class)) {
					v.add(new SetsFlexoRole(pr));
				}
				return new ValidationError<AddShapeActionMustAdressAValidShapeRole, AddShape>(this, action,
						"add_shape_action_does_not_address_a_valid_shape_flexo_role", v);
			}
			return null;
		}

		protected static class SetsFlexoRole extends FixProposal<AddShapeActionMustAdressAValidShapeRole, AddShape> {

			private final ShapeRole flexoRole;

			public SetsFlexoRole(ShapeRole flexoRole) {
				super("assign_action_to_flexo_role_($flexoRole.flexoRoleName)");
				this.flexoRole = flexoRole;
			}

			public ShapeRole getFlexoRole() {
				return flexoRole;
			}

			@Override
			protected void fixAction() {
				AddShape action = getValidable();
				((AssignationAction) action.getOwner()).setAssignation(new DataBinding<Object>(flexoRole.getRoleName()));
			}

		}
	}

	@DefineValidationRule
	public static class AddShapeActionMustHaveAValidContainer extends ValidationRule<AddShapeActionMustHaveAValidContainer, AddShape> {
		public AddShapeActionMustHaveAValidContainer() {
			super(AddShape.class, "add_shape_action_must_have_a_valid_container");
		}

		@Override
		public ValidationIssue<AddShapeActionMustHaveAValidContainer, AddShape> applyValidation(AddShape action) {
			if (action.getAssignedFlexoProperty() != null && action.getAssignedFlexoProperty().getParentShapeAsDefinedInAction()
					&& !(action.getContainer().isSet() && action.getContainer().isValid())) {
				Vector<FixProposal<AddShapeActionMustHaveAValidContainer, AddShape>> v = new Vector<FixProposal<AddShapeActionMustHaveAValidContainer, AddShape>>();
				if (action.getRootOwner() instanceof DropScheme) {
					FlexoConcept targetFlexoConcept = ((DropScheme) action.getRootOwner()).getTargetFlexoConcept();
					if (targetFlexoConcept != null) {
						for (ShapeRole pr : action.getFlexoConcept().getDeclaredProperties(ShapeRole.class)) {
							v.add(new SetsContainerToTargetShape(targetFlexoConcept, pr));
						}
					}
				}
				v.add(new SetsContainerToTopLevel());
				for (ShapeRole pr : action.getFlexoConcept().getDeclaredProperties(ShapeRole.class)) {
					v.add(new SetsContainerToShape(pr));
				}
				String details;
				if (action.getContainer().isSet()) {
					details = "Invalid container: " + action.getContainer() + " reason: " + action.getContainer().invalidBindingReason();
				} else {
					details = "Container not set";
				}

				return new ValidationError<AddShapeActionMustHaveAValidContainer, AddShape>(this, action,
						"add_shape_action_does_not_have_a_valid_container", v);
			}
			return null;
		}

		protected static class SetsContainerToTopLevel extends FixProposal<AddShapeActionMustHaveAValidContainer, AddShape> {

			public SetsContainerToTopLevel() {
				super("sets_container_to_'top_level'");
			}

			@Override
			protected void fixAction() {
				AddShape action = getValidable();
				action.setContainer(new DataBinding<DiagramContainerElement<?>>(DiagramBehaviourBindingModel.TOP_LEVEL));
			}

		}

		protected static class SetsContainerToShape extends FixProposal<AddShapeActionMustHaveAValidContainer, AddShape> {

			private final ShapeRole shapeRole;

			public SetsContainerToShape(ShapeRole shapeRole) {
				super("sets_container_to_'($shapeRole.roleName)'");
				this.shapeRole = shapeRole;
			}

			public ShapeRole getShapeRole() {
				return shapeRole;
			}

			@Override
			protected void fixAction() {
				AddShape action = getValidable();
				action.setContainer(new DataBinding<DiagramContainerElement<?>>(shapeRole.getRoleName()));
			}
		}

		protected static class SetsContainerToTargetShape extends FixProposal<AddShapeActionMustHaveAValidContainer, AddShape> {

			private final FlexoConcept target;
			private final ShapeRole patternRole;

			public SetsContainerToTargetShape(FlexoConcept target, ShapeRole patternRole) {
				super("sets_container_to_target.($patternRole.patternRoleName)");
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
				AddShape action = getValidable();
				action.setContainer(new DataBinding<DiagramContainerElement<?>>(DropSchemeBindingModel.TARGET + "."
						+ patternRole.getRoleName()));
			}
		}

	}
}
