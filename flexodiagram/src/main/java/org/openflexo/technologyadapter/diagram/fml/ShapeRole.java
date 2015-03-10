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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.openflexo.connie.DataBinding;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.foundation.fml.FMLRepresentationContext;
import org.openflexo.foundation.fml.FMLRepresentationContext.FMLRepresentationOutput;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.model.annotations.CloningStrategy;
import org.openflexo.model.annotations.CloningStrategy.StrategyType;
import org.openflexo.model.annotations.DefineValidationRule;
import org.openflexo.model.annotations.Embedded;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.model.DiagramShape;

@ModelEntity
@ImplementationClass(ShapeRole.ShapeRoleImpl.class)
@XMLElement
@FML("ShapeRole")
public interface ShapeRole extends GraphicalElementRole<DiagramShape, ShapeGraphicalRepresentation> {

	@PropertyIdentifier(type = GraphicalRepresentation.class)
	public static final String GRAPHICAL_REPRESENTATION_KEY = "graphicalRepresentation";
	@PropertyIdentifier(type = ShapeRole.class)
	public static final String PARENT_SHAPE_PATTERN_ROLE_KEY = "parentShapeRole";

	@Getter(value = GRAPHICAL_REPRESENTATION_KEY)
	@CloningStrategy(StrategyType.CLONE)
	@Embedded
	@XMLElement
	public ShapeGraphicalRepresentation getGraphicalRepresentation();

	@Setter(GRAPHICAL_REPRESENTATION_KEY)
	public void setGraphicalRepresentation(ShapeGraphicalRepresentation graphicalRepresentation);

	@Getter(value = PARENT_SHAPE_PATTERN_ROLE_KEY)
	@XMLElement(context = "Parent")
	public ShapeRole getParentShapeRole();

	@Setter(PARENT_SHAPE_PATTERN_ROLE_KEY)
	public void setParentShapeRole(ShapeRole parentShapeRole);

	public boolean isContainedIn(ShapeRole container);

	public boolean getParentShapeAsDefinedInAction();

	public void setParentShapeAsDefinedInAction(boolean flag);

	/**
	 * Get the list of shape pattern roles that can be set as parent shape pattern role. This list contains all other shape pattern roles of
	 * current flexo concept which are not already in the containment subtree
	 * 
	 * @return
	 */
	public List<ShapeRole> getPossibleParentShapeRoles();

	public static abstract class ShapeRoleImpl extends GraphicalElementRoleImpl<DiagramShape, ShapeGraphicalRepresentation> implements
			ShapeRole {

		private static final Logger logger = Logger.getLogger(ShapeRole.class.getPackage().getName());

		private ShapeRole parentShapeRole;

		// private List<ShapeRole> _possibleParentPatternRole;

		public ShapeRoleImpl() {
			super();
		}

		@Override
		protected void initDefaultSpecifications() {
			super.initDefaultSpecifications();
			if (getFMLModelFactory() != null) {
				for (GraphicalFeature<?, ?> GF : AVAILABLE_FEATURES) {
					GraphicalElementSpecification newGraphicalElementSpecification = getFMLModelFactory().newInstance(
							GraphicalElementSpecification.class);
					newGraphicalElementSpecification.setPatternRole(this);
					newGraphicalElementSpecification.setFeature(GF);
					newGraphicalElementSpecification.setReadOnly(false);
					newGraphicalElementSpecification.setMandatory(true);
					grSpecifications.add(newGraphicalElementSpecification);
				}
			}
		}

		@Override
		public String getFMLRepresentation(FMLRepresentationContext context) {
			FMLRepresentationOutput out = new FMLRepresentationOutput(context);
			out.append("FlexoRole " + getName() + " as ShapeSpecification from " + getOwningVirtualModel().getName() + ";", context);
			return out.toString();
		}

		@Override
		public String getStringRepresentation() {
			return FlexoLocalization.localizedForKey("shape");
		}

		public void tryToFindAGR() {
			if (getGraphicalRepresentation() == null && getModelSlot() instanceof TypedDiagramModelSlot) {
				// Try to find one somewhere
				TypedDiagramModelSlot ms = (TypedDiagramModelSlot) getModelSlot();
				for (FMLDiagramPaletteElementBinding binding : ms.getPaletteElementBindings()) {
					if (binding.getBoundFlexoConcept() == getFlexoConcept()) {
						setGraphicalRepresentation(binding.getPaletteElement().getGraphicalRepresentation());
					}
				}
			}
		}

		@Override
		public Type getType() {
			return DiagramShape.class;
		}

		private boolean detectLoopInParentShapePatternRoleDefinition() {
			List<ShapeRole> list = new ArrayList<ShapeRole>();
			ShapeRole current = this;
			while (!list.contains(current) && current != null) {
				list.add(current);
				current = current.getParentShapeRole();
			}
			if (current != null) {
				return true;
			}
			return false;
		}

		@Override
		public ShapeRole getParentShapeRole() {
			return parentShapeRole;
		}

		@Override
		public void setParentShapeRole(ShapeRole parentShapeRole) {
			if (parentShapeRole != this.parentShapeRole) {
				ShapeRole oldParentShapeRole = this.parentShapeRole;
				logger.info(">>>> setParentShapePatternRole() with " + parentShapeRole);
				this.parentShapeRole = parentShapeRole;
				if (detectLoopInParentShapePatternRoleDefinition()) {
					logger.warning("Detecting a loop in parent shape pattern role definition. Resetting parent shape pattern role");
					this.parentShapeRole = null;
				}
				// setChanged();
				// notifyObservers();
				getPropertyChangeSupport().firePropertyChange(PARENT_SHAPE_PATTERN_ROLE_KEY, oldParentShapeRole, parentShapeRole);
				if (getFlexoConcept() != null) {
					getFlexoConcept().getPropertyChangeSupport().firePropertyChange(PARENT_SHAPE_PATTERN_ROLE_KEY, oldParentShapeRole,
							parentShapeRole);
				}
			}
		}

		@Override
		public boolean getParentShapeAsDefinedInAction() {
			return getParentShapeRole() == null;
		}

		@Override
		public void setParentShapeAsDefinedInAction(boolean flag) {
			// System.out.println(">>>> setParentShapeAsDefinedInAction() with " + flag);
			List<ShapeRole> possibleParentPatternRole = getPossibleParentShapeRoles();
			if (!flag) {
				if (possibleParentPatternRole.size() > 0) {
					setParentShapeRole(possibleParentPatternRole.get(0));
				}
			} else {
				// System.out.println("setParentShapePatternRole with null");
				setParentShapeRole(null);
				// flag = true;
			}
		}

		@Override
		public boolean isContainedIn(ShapeRole container) {
			if (container == this) {
				return true;
			}
			if (getParentShapeRole() != null) {
				return getParentShapeRole().isContainedIn(container);
			}
			return false;
		}

		/**
		 * Get the list of shape pattern roles that can be set as parent shape pattern role. This list contains all other shape pattern
		 * roles of current flexo concept which are not already in the containment subtree
		 * 
		 * @return
		 */
		@Override
		public List<ShapeRole> getPossibleParentShapeRoles() {
			List<ShapeRole> returned = new ArrayList<ShapeRole>();
			if (getFlexoConcept() != null) {
				List<ShapeRole> shapesPatternRoles = getFlexoConcept().getFlexoRoles(ShapeRole.class);
				for (ShapeRole shapeRole : shapesPatternRoles) {
					if (!shapeRole.isContainedIn(this)) {
						returned.add(shapeRole);
					}
				}
			}
			return returned;
		}

		/*@Override
		public boolean isEmbeddedIn(ShapeRole aPR) {
			if (getParentShapePatternRole() != null) {
				if (getParentShapePatternRole() == aPR) {
					return true;
				} else {
					return getParentShapePatternRole().isEmbeddedIn(aPR);
				}
			}
			return false;
		}*/

		public static GraphicalFeature<Double, ShapeGraphicalRepresentation> POS_X_FEATURE = new GraphicalFeature<Double, ShapeGraphicalRepresentation>(
				"x", ShapeGraphicalRepresentation.X) {
			@Override
			public Double retrieveFromGraphicalRepresentation(ShapeGraphicalRepresentation gr) {
				return gr.getX();
			}

			@Override
			public void applyToGraphicalRepresentation(ShapeGraphicalRepresentation gr, Double value) {
				gr.setX(value.doubleValue());
			}
		};

		public static GraphicalFeature<Double, ShapeGraphicalRepresentation> POS_Y_FEATURE = new GraphicalFeature<Double, ShapeGraphicalRepresentation>(
				"y", ShapeGraphicalRepresentation.Y) {
			@Override
			public Double retrieveFromGraphicalRepresentation(ShapeGraphicalRepresentation gr) {
				return gr.getY();
			}

			@Override
			public void applyToGraphicalRepresentation(ShapeGraphicalRepresentation gr, Double value) {
				gr.setY(value.doubleValue());
			}
		};

		public static GraphicalFeature<Double, ShapeGraphicalRepresentation> WIDTH_FEATURE = new GraphicalFeature<Double, ShapeGraphicalRepresentation>(
				"width", ShapeGraphicalRepresentation.WIDTH) {
			@Override
			public Double retrieveFromGraphicalRepresentation(ShapeGraphicalRepresentation gr) {
				return gr.getWidth();
			}

			@Override
			public void applyToGraphicalRepresentation(ShapeGraphicalRepresentation gr, Double value) {
				gr.setWidth(value.doubleValue());
			}
		};

		public static GraphicalFeature<Double, ShapeGraphicalRepresentation> HEIGHT_FEATURE = new GraphicalFeature<Double, ShapeGraphicalRepresentation>(
				"height", ShapeGraphicalRepresentation.HEIGHT) {
			@Override
			public Double retrieveFromGraphicalRepresentation(ShapeGraphicalRepresentation gr) {
				return gr.getHeight();
			}

			@Override
			public void applyToGraphicalRepresentation(ShapeGraphicalRepresentation gr, Double value) {
				gr.setHeight(value.doubleValue());
			}
		};

		public static GraphicalFeature<Double, ShapeGraphicalRepresentation> RELATIVE_TEXT_X_FEATURE = new GraphicalFeature<Double, ShapeGraphicalRepresentation>(
				"relativeTextX", ShapeGraphicalRepresentation.RELATIVE_TEXT_X) {
			@Override
			public Double retrieveFromGraphicalRepresentation(ShapeGraphicalRepresentation gr) {
				return gr.getRelativeTextX();
			}

			@Override
			public void applyToGraphicalRepresentation(ShapeGraphicalRepresentation gr, Double value) {
				gr.setRelativeTextX(value.doubleValue());
			}
		};

		public static GraphicalFeature<Double, ShapeGraphicalRepresentation> RELATIVE_TEXT_Y_FEATURE = new GraphicalFeature<Double, ShapeGraphicalRepresentation>(
				"relativeTextY", ShapeGraphicalRepresentation.RELATIVE_TEXT_Y) {
			@Override
			public Double retrieveFromGraphicalRepresentation(ShapeGraphicalRepresentation gr) {
				return gr.getRelativeTextY();
			}

			@Override
			public void applyToGraphicalRepresentation(ShapeGraphicalRepresentation gr, Double value) {
				gr.setRelativeTextY(value.doubleValue());
			}
		};

		public static GraphicalFeature<Double, ShapeGraphicalRepresentation> ABSOLUTE_TEXT_X_FEATURE = new GraphicalFeature<Double, ShapeGraphicalRepresentation>(
				"absoluteTextX", ShapeGraphicalRepresentation.ABSOLUTE_TEXT_X) {
			@Override
			public Double retrieveFromGraphicalRepresentation(ShapeGraphicalRepresentation gr) {
				return gr.getAbsoluteTextX();
			}

			@Override
			public void applyToGraphicalRepresentation(ShapeGraphicalRepresentation gr, Double value) {
				gr.setAbsoluteTextX(value.doubleValue());
			}
		};

		public static GraphicalFeature<Double, ShapeGraphicalRepresentation> ABSOLUTE_TEXT_Y_FEATURE = new GraphicalFeature<Double, ShapeGraphicalRepresentation>(
				"absoluteTextY", ShapeGraphicalRepresentation.ABSOLUTE_TEXT_Y) {
			@Override
			public Double retrieveFromGraphicalRepresentation(ShapeGraphicalRepresentation gr) {
				return gr.getAbsoluteTextY();
			}

			@Override
			public void applyToGraphicalRepresentation(ShapeGraphicalRepresentation gr, Double value) {
				gr.setAbsoluteTextY(value.doubleValue());
			}
		};

		public static GraphicalFeature<?, ?>[] AVAILABLE_FEATURES = { POS_X_FEATURE, POS_Y_FEATURE, WIDTH_FEATURE, HEIGHT_FEATURE,
				RELATIVE_TEXT_X_FEATURE, RELATIVE_TEXT_Y_FEATURE, ABSOLUTE_TEXT_X_FEATURE, ABSOLUTE_TEXT_Y_FEATURE };

	}

	@DefineValidationRule
	public static class LabelBindingdMustBeValid extends BindingMustBeValid<ShapeRole> {
		public LabelBindingdMustBeValid() {
			super("'label'_binding_must_be_valid", ShapeRole.class);
		}

		@Override
		public DataBinding<String> getBinding(ShapeRole object) {
			return object.getLabel();
		}

	}

}
