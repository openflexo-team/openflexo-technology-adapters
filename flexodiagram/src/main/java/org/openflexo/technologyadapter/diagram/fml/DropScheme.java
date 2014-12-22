/*
 * (c) Copyright 2010-2011 AgileBirds
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
package org.openflexo.technologyadapter.diagram.fml;

import java.util.List;

import org.openflexo.antar.binding.DataBinding;
import org.openflexo.foundation.fml.AbstractCreationScheme;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoRole;
import org.openflexo.foundation.fml.annotations.FIBPanel;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.fml.binding.DiagramBehaviourBindingModel;
import org.openflexo.technologyadapter.diagram.fml.binding.DropSchemeBindingModel;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddShape;
import org.openflexo.technologyadapter.diagram.model.DiagramContainerElement;
import org.openflexo.toolbox.StringUtils;

@FIBPanel("Fib/DropSchemePanel.fib")
@ModelEntity
@ImplementationClass(DropScheme.DropSchemeImpl.class)
@XMLElement
public interface DropScheme extends AbstractCreationScheme, DiagramFlexoBehaviour {

	@PropertyIdentifier(type = String.class)
	public static final String TARGET_KEY = "target";
	@PropertyIdentifier(type = ShapeRole.class)
	public static final String TARGET_SHAPE_ROLE_KEY = "targetShapeRole";
	@PropertyIdentifier(type = FlexoConcept.class)
	public static final String TARGET_FLEXO_CONCEPT_KEY = "targetFlexoConcept";

	@Getter(value = TARGET_KEY)
	@XMLAttribute
	public String _getTarget();

	@Setter(TARGET_KEY)
	public void _setTarget(String target);

	@Getter(value = TARGET_SHAPE_ROLE_KEY)
	@XMLElement
	public ShapeRole getTargetShapeRole();

	@Setter(TARGET_SHAPE_ROLE_KEY)
	public void setTargetShapeRole(ShapeRole targetShapeRole);

	public boolean isTopTarget();

	public boolean getTopTarget();

	public void setTopTarget(boolean flag);

	public FlexoConcept getTargetFlexoConcept();

	public void setTargetFlexoConcept(FlexoConcept targetFlexoConcept);

	public boolean isValidTarget(FlexoConcept aTarget, FlexoRole contextRole);

	public List<ShapeRole> getAvailableTargetShapeRoles();

	public boolean targetHasMultipleRoles();

	public static abstract class DropSchemeImpl extends AbstractCreationSchemeImpl implements DropScheme {

		private static final String TOP = "top";

		private String target = TOP;
		private ShapeRole targetPatternRole;
		private FlexoConcept lastKnownTargetFlexoConcept;

		public DropSchemeImpl() {
			super();
		}

		@Override
		public String _getTarget() {
			return target;
		}

		@Override
		public void _setTarget(String target) {
			if (requireChange(this.target, target)) {
				FlexoConcept oldValue = getTargetFlexoConcept();
				this.target = target;
				getPropertyChangeSupport().firePropertyChange(TARGET_FLEXO_CONCEPT_KEY, oldValue, getTargetFlexoConcept());
			}
		}

		@Override
		public FlexoConcept getTargetFlexoConcept() {
			if (StringUtils.isEmpty(_getTarget())) {
				return null;
			}
			if (isTopTarget()) {
				return null;
			}
			if (getVirtualModel() != null) {
				FlexoConcept returned = getVirtualModel().getFlexoConcept(_getTarget());
				if (lastKnownTargetFlexoConcept != returned) {
					FlexoConcept oldValue = lastKnownTargetFlexoConcept;
					lastKnownTargetFlexoConcept = returned;
					getPropertyChangeSupport().firePropertyChange(TARGET_FLEXO_CONCEPT_KEY, oldValue, returned);
				}
				return returned;
			}
			return null;
		}

		@Override
		public void setTargetFlexoConcept(FlexoConcept targetFlexoConcept) {
			/*if (targetFlexoConcept != null) {
				setTopTarget(false);
			}*/
			_setTarget(targetFlexoConcept != null ? targetFlexoConcept.getURI() : null);
			// updateBindingModels();
		}

		@Override
		public boolean isTopTarget() {
			return getTopTarget();
		}

		@Override
		public boolean getTopTarget() {
			if (StringUtils.isEmpty(_getTarget())) {
				return false;
			}
			return _getTarget().equalsIgnoreCase(TOP);
		}

		@Override
		public void setTopTarget(boolean flag) {
			if (flag) {
				_setTarget(TOP);
			} else {
				_setTarget("");
			}
		}

		@Override
		public boolean targetHasMultipleRoles() {
			return getTargetFlexoConcept() != null && getAvailableTargetShapeRoles().size() > 1;
		}

		@Override
		public List<ShapeRole> getAvailableTargetShapeRoles() {
			if (getTargetFlexoConcept() != null) {
				return getTargetFlexoConcept().getFlexoRoles(ShapeRole.class);
			}
			return null;
		}

		@Override
		public ShapeRole getTargetShapeRole() {
			return targetPatternRole;
		}

		@Override
		public void setTargetShapeRole(ShapeRole targetPatternRole) {
			this.targetPatternRole = targetPatternRole;
		}

		@Override
		public boolean isValidTarget(FlexoConcept aTarget, FlexoRole contextRole) {
			if (getTargetFlexoConcept() != null && getTargetFlexoConcept().isAssignableFrom(aTarget)) {
				if (targetHasMultipleRoles()) {
					// TODO make proper implementation when role inheritance will be in use !!!
					return getTargetShapeRole() == null || getTargetShapeRole().getRoleName().equals(contextRole.getRoleName());
				} else {
					return true;
				}
			}
			return false;
		}

		@Override
		protected DropSchemeBindingModel makeBindingModel() {
			return new DropSchemeBindingModel(this);
		}

		/*@Override
		protected void appendContextualBindingVariables(BindingModel bindingModel) {
			super.appendContextualBindingVariables(bindingModel);
			// bindingModelNeedToBeRecomputed = false;
			bindingModel.addToBindingVariables(new BindingVariable(DiagramFlexoBehaviour.TOP_LEVEL, Diagram.class));
			if (getTargetFlexoConcept() != null) {
				bindingModel.addToBindingVariables(new BindingVariable(DiagramFlexoBehaviour.TARGET, FlexoConceptInstanceType
						.getFlexoConceptInstanceType(getTargetFlexoConcept())));
			} else if (_getTarget() != null && !_getTarget().equals("top")) {
				// logger.warning("Cannot find flexo concept " + _getTarget() + " !!!!!!!!!!!!!!");
				// bindingModelNeedToBeRecomputed = true;
				}
		}*/

		// private boolean bindingModelNeedToBeRecomputed = false;
		// private boolean isUpdatingBindingModel = false;

		/*@Override
		public BindingModel getBindingModel() {
			if (bindingModelNeedToBeRecomputed && !isUpdatingBindingModel) {
				isUpdatingBindingModel = true;
				bindingModelNeedToBeRecomputed = false;
				updateBindingModels();
				isUpdatingBindingModel = false;
			}
			return super.getBindingModel();
		}*/

		/*@Override
		protected void rebuildActionsBindingModel() {
			if (!bindingModelNeedToBeRecomputed) {
				super.rebuildActionsBindingModel();
			}
		}*/

		/**
		 * Overrides {@link #createAction(Class, ModelSlot)} by providing default value for top level container
		 * 
		 * @return newly created {@link EditionAction}
		 */
		@Override
		public <A extends EditionAction<?, ?>> A createAction(Class<A> actionClass, ModelSlot<?> modelSlot) {
			A newAction = super.createAction(actionClass, modelSlot);
			if (newAction instanceof AddShape) {
				if (isTopTarget()) {
					((AddShape) newAction)
							.setContainer(new DataBinding<DiagramContainerElement<?>>(DiagramBehaviourBindingModel.TOP_LEVEL));
				}
			}
			return newAction;
		}

		/*@Override
		public DiagramTechnologyAdapter getTechnologyAdapter() {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
		}*/
	}
}
