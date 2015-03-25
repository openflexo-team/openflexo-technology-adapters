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

import org.openflexo.connie.DataBinding;
import org.openflexo.fib.annotation.FIBPanel;
import org.openflexo.foundation.fml.AbstractCreationScheme;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.foundation.fml.editionaction.EditionAction;
import org.openflexo.foundation.fml.editionaction.TechnologySpecificAction;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.fml.binding.LinkSchemeBindingModel;
import org.openflexo.technologyadapter.diagram.fml.editionaction.AddConnector;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;
import org.openflexo.toolbox.StringUtils;

@FIBPanel("Fib/LinkSchemePanel.fib")
@ModelEntity
@ImplementationClass(LinkScheme.LinkSchemeImpl.class)
@XMLElement
@FML("LinkScheme")
public interface LinkScheme extends AbstractCreationScheme, DiagramFlexoBehaviour {

	@PropertyIdentifier(type = String.class)
	public static final String FROM_TARGET_KEY = "fromTarget";
	@PropertyIdentifier(type = String.class)
	public static final String TO_TARGET_KEY = "toTarget";
	@PropertyIdentifier(type = boolean.class)
	public static final String IS_AVAILABLE_WITH_FLOATING_PALETTE_KEY = "isAvailableWithFloatingPalette";
	@PropertyIdentifier(type = boolean.class)
	public static final String NORTH_DIRECTION_SUPPORTED_KEY = "northDirectionSupported";
	@PropertyIdentifier(type = boolean.class)
	public static final String EAST_DIRECTION_SUPPORTED_KEY = "eastDirectionSupported";
	@PropertyIdentifier(type = boolean.class)
	public static final String SOUTH_DIRECTION_SUPPORTED_KEY = "southDirectionSupported";
	@PropertyIdentifier(type = boolean.class)
	public static final String WEST_DIRECTION_SUPPORTED_KEY = "westDirectionSupported";

	@PropertyIdentifier(type = FlexoConcept.class)
	public static final String FROM_TARGET_FLEXO_CONCEPT_KEY = "fromTargetFlexoConcept";
	@PropertyIdentifier(type = FlexoConcept.class)
	public static final String TO_TARGET_FLEXO_CONCEPT_KEY = "toTargetFlexoConcept";

	@Getter(value = FROM_TARGET_KEY)
	@XMLAttribute
	public String _getFromTarget();

	@Setter(FROM_TARGET_KEY)
	public void _setFromTarget(String fromTarget);

	@Getter(value = TO_TARGET_KEY)
	@XMLAttribute
	public String _getToTarget();

	@Setter(TO_TARGET_KEY)
	public void _setToTarget(String toTarget);

	@Getter(value = IS_AVAILABLE_WITH_FLOATING_PALETTE_KEY, defaultValue = "false")
	@XMLAttribute
	public boolean getIsAvailableWithFloatingPalette();

	@Setter(IS_AVAILABLE_WITH_FLOATING_PALETTE_KEY)
	public void setIsAvailableWithFloatingPalette(boolean isAvailableWithFloatingPalette);

	@Getter(value = NORTH_DIRECTION_SUPPORTED_KEY, defaultValue = "false")
	@XMLAttribute
	public boolean getNorthDirectionSupported();

	@Setter(NORTH_DIRECTION_SUPPORTED_KEY)
	public void setNorthDirectionSupported(boolean northDirectionSupported);

	@Getter(value = EAST_DIRECTION_SUPPORTED_KEY, defaultValue = "false")
	@XMLAttribute
	public boolean getEastDirectionSupported();

	@Setter(EAST_DIRECTION_SUPPORTED_KEY)
	public void setEastDirectionSupported(boolean eastDirectionSupported);

	@Getter(value = SOUTH_DIRECTION_SUPPORTED_KEY, defaultValue = "false")
	@XMLAttribute
	public boolean getSouthDirectionSupported();

	@Setter(SOUTH_DIRECTION_SUPPORTED_KEY)
	public void setSouthDirectionSupported(boolean southDirectionSupported);

	@Getter(value = WEST_DIRECTION_SUPPORTED_KEY, defaultValue = "false")
	@XMLAttribute
	public boolean getWestDirectionSupported();

	@Setter(WEST_DIRECTION_SUPPORTED_KEY)
	public void setWestDirectionSupported(boolean westDirectionSupported);

	public FlexoConcept getFromTargetFlexoConcept();

	public void setFromTargetFlexoConcept(FlexoConcept targetFlexoConcept);

	public FlexoConcept getToTargetFlexoConcept();

	public void setToTargetFlexoConcept(FlexoConcept targetFlexoConcept);

	public boolean isValidTarget(FlexoConcept actualFromTarget, FlexoConcept actualToTarget);

	public static abstract class LinkSchemeImpl extends AbstractCreationSchemeImpl implements LinkScheme {

		private String fromTarget;
		private String toTarget;

		private boolean northDirectionSupported = true;
		private boolean eastDirectionSupported = true;
		private boolean southDirectionSupported = true;
		private boolean westDirectionSupported = true;

		private boolean isAvailableWithFloatingPalette = true;

		private FlexoConcept lastKnownFromTargetFlexoConcept = null;
		private FlexoConcept lastKnownToTargetFlexoConcept = null;

		public LinkSchemeImpl() {
			super();
		}

		@Override
		public String _getFromTarget() {
			return fromTarget;
		}

		@Override
		public void _setFromTarget(String fromTarget) {
			if (requireChange(this.fromTarget, fromTarget)) {
				FlexoConcept oldValue = lastKnownFromTargetFlexoConcept;
				this.fromTarget = fromTarget;
				getPropertyChangeSupport().firePropertyChange(FROM_TARGET_FLEXO_CONCEPT_KEY, oldValue, getFromTargetFlexoConcept());
			}
		}

		@Override
		public String _getToTarget() {
			return toTarget;
		}

		@Override
		public void _setToTarget(String toTarget) {
			if (requireChange(this.toTarget, toTarget)) {
				FlexoConcept oldValue = lastKnownToTargetFlexoConcept;
				this.toTarget = toTarget;
				getPropertyChangeSupport().firePropertyChange(TO_TARGET_FLEXO_CONCEPT_KEY, oldValue, getToTargetFlexoConcept());
			}
		}

		@Override
		public FlexoConcept getFromTargetFlexoConcept() {

			FlexoConcept returned = null;

			if (!StringUtils.isEmpty(_getFromTarget()) && getOwningVirtualModel() != null) {
				returned = getOwningVirtualModel().getFlexoConcept(_getFromTarget());
			}
			if (lastKnownFromTargetFlexoConcept != returned) {
				FlexoConcept oldValue = lastKnownFromTargetFlexoConcept;
				lastKnownFromTargetFlexoConcept = returned;
				getPropertyChangeSupport().firePropertyChange(FROM_TARGET_FLEXO_CONCEPT_KEY, oldValue, returned);
			}
			return returned;
		}

		@Override
		public void setFromTargetFlexoConcept(FlexoConcept targetFlexoConcept) {
			_setFromTarget(targetFlexoConcept != null ? targetFlexoConcept.getURI() : null);
		}

		@Override
		public FlexoConcept getToTargetFlexoConcept() {

			FlexoConcept returned = null;
			if (!StringUtils.isEmpty(_getToTarget()) && getOwningVirtualModel() != null) {
				returned = getOwningVirtualModel().getFlexoConcept(_getToTarget());
			}
			if (lastKnownToTargetFlexoConcept != returned) {
				FlexoConcept oldValue = lastKnownToTargetFlexoConcept;
				lastKnownToTargetFlexoConcept = returned;
				getPropertyChangeSupport().firePropertyChange(TO_TARGET_FLEXO_CONCEPT_KEY, oldValue, returned);
			}
			return returned;
		}

		@Override
		public void setToTargetFlexoConcept(FlexoConcept targetFlexoConcept) {
			_setToTarget(targetFlexoConcept != null ? targetFlexoConcept.getURI() : null);
			// updateBindingModels();
		}

		@Override
		public boolean isValidTarget(FlexoConcept actualFromTarget, FlexoConcept actualToTarget) {
			// TODO: improved this so that we can take into account adressed models restrictions. See also
			// LinkScheme.isValidTarget on branch 1.5.1
			return getFromTargetFlexoConcept().isAssignableFrom(actualFromTarget)
					&& getToTargetFlexoConcept().isAssignableFrom(actualToTarget);
		}

		/*private void appendFromTargetBindingVariable(BindingModel bindingModel) {
			if (getFromTargetFlexoConcept() != null) {
				bindingModel.addToBindingVariables(new BindingVariable(LinkSchemeBindingModel.FROM_TARGET, FlexoConceptInstanceType
						.getFlexoConceptInstanceType(getFromTargetFlexoConcept())));
			}
		}

		private void appendToTargetBindingVariable(BindingModel bindingModel) {
			if (getToTargetFlexoConcept() != null) {
				bindingModel.addToBindingVariables(new BindingVariable(LinkSchemeBindingModel.TO_TARGET, FlexoConceptInstanceType
						.getFlexoConceptInstanceType(getToTargetFlexoConcept())));
			}
		}*/

		@Override
		protected LinkSchemeBindingModel makeBindingModel() {
			return new LinkSchemeBindingModel(this);
		}

		/*@Override
		protected void appendContextualBindingVariables(BindingModel bindingModel) {
			super.appendContextualBindingVariables(bindingModel);
			// bindingModelNeedToBeRecomputed = false;
			bindingModel.addToBindingVariables(new BindingVariable(DiagramFlexoBehaviour.TOP_LEVEL, Diagram.class));
			if (getFromTargetFlexoConcept() != null) {
				bindingModel.addToBindingVariables(new BindingVariable(DiagramFlexoBehaviour.FROM_TARGET, FlexoConceptInstanceType
						.getFlexoConceptInstanceType(getFromTargetFlexoConcept())));
			} else if (_getFromTarget() != null && !StringUtils.isEmpty(_getFromTarget())) {
				// bindingModelNeedToBeRecomputed = true;
			}
			if (getToTargetFlexoConcept() != null) {
				bindingModel.addToBindingVariables(new BindingVariable(DiagramFlexoBehaviour.TO_TARGET, FlexoConceptInstanceType
						.getFlexoConceptInstanceType(getToTargetFlexoConcept())));
			} else if (_getToTarget() != null && !StringUtils.isEmpty(_getToTarget())) {
				//bindingModelNeedToBeRecomputed = true;
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
		}

		@Override
		protected void rebuildActionsBindingModel() {
			if (!bindingModelNeedToBeRecomputed) {
				super.rebuildActionsBindingModel();
			}
		}*/

		/**
		 * Overrides {@link #createAction(Class, ModelSlot)} by providing default value for from and to targets
		 * 
		 * @return newly created {@link EditionAction}
		 */
		@Override
		public <A extends TechnologySpecificAction<?, ?>> A createAction(Class<A> actionClass, ModelSlot<?> modelSlot) {
			A returned = super.createAction(actionClass, modelSlot);
			if (returned instanceof AddConnector) {
				AddConnector newAction = (AddConnector) returned;
				FlexoConcept fromFlexoConcept = this.getFromTargetFlexoConcept();
				if (fromFlexoConcept != null) {
					ShapeRole fromShapeRole = getDefaultShapeRole(fromFlexoConcept);
					if (fromShapeRole != null) {
						newAction.setFromShape(new DataBinding<DiagramShape>("fromTarget." + fromShapeRole.getName()));
					}
				}
				FlexoConcept toFlexoConcept = this.getToTargetFlexoConcept();
				if (toFlexoConcept != null) {
					ShapeRole toShapeRole = getDefaultShapeRole(toFlexoConcept);
					if (toShapeRole != null) {
						newAction.setToShape(new DataBinding<DiagramShape>("toTarget." + toShapeRole.getName()));
					}
				}
			}
			return returned;
		}

		private ShapeRole getDefaultShapeRole(FlexoConcept ep) {
			if (ep.getDeclaredProperties(ShapeRole.class).size() > 0) {
				return ep.getDeclaredProperties(ShapeRole.class).get(0);
			}
			return null;
		}

		@Override
		public boolean getIsAvailableWithFloatingPalette() {
			return isAvailableWithFloatingPalette;
		}

		@Override
		public void setIsAvailableWithFloatingPalette(boolean isAvailableWithFloatingPalette) {
			this.isAvailableWithFloatingPalette = isAvailableWithFloatingPalette;
		}

		@Override
		public boolean getNorthDirectionSupported() {
			return northDirectionSupported;
		}

		@Override
		public void setNorthDirectionSupported(boolean northDirectionSupported) {
			this.northDirectionSupported = northDirectionSupported;
		}

		@Override
		public boolean getEastDirectionSupported() {
			return eastDirectionSupported;
		}

		@Override
		public void setEastDirectionSupported(boolean eastDirectionSupported) {
			this.eastDirectionSupported = eastDirectionSupported;
		}

		@Override
		public boolean getSouthDirectionSupported() {
			return southDirectionSupported;
		}

		@Override
		public void setSouthDirectionSupported(boolean southDirectionSupported) {
			this.southDirectionSupported = southDirectionSupported;
		}

		@Override
		public boolean getWestDirectionSupported() {
			return westDirectionSupported;
		}

		@Override
		public void setWestDirectionSupported(boolean westDirectionSupported) {
			this.westDirectionSupported = westDirectionSupported;
		}

		/*@Override
		public DiagramTechnologyAdapter getTechnologyAdapter() {
			return getServiceManager().getTechnologyAdapterService().getTechnologyAdapter(DiagramTechnologyAdapter.class);
		}*/

	}
}
