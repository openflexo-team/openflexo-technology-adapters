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

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.openflexo.antar.binding.BindingModel;
import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.validation.Validable;
import org.openflexo.foundation.view.FlexoConceptInstance;
import org.openflexo.foundation.viewpoint.AbstractActionScheme;
import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.foundation.viewpoint.FlexoConceptObject;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.model.annotations.Getter;
import org.openflexo.model.annotations.ImplementationClass;
import org.openflexo.model.annotations.ModelEntity;
import org.openflexo.model.annotations.PropertyIdentifier;
import org.openflexo.model.annotations.Setter;
import org.openflexo.model.annotations.XMLAttribute;
import org.openflexo.model.annotations.XMLElement;

@ModelEntity
@ImplementationClass(GraphicalElementAction.GraphicalElementActionImpl.class)
@XMLElement(xmlTag = "Action")
public interface GraphicalElementAction extends FlexoConceptObject {

	public static enum ActionMask {
		SingleClick, DoubleClick, ShiftClick, AltClick, CtrlClick, MetaClick;
	}

	@PropertyIdentifier(type = GraphicalElementRole.class)
	public static final String GRAPHICAL_ELEMENT_ROLE_KEY = "graphicalElementRole";
	@PropertyIdentifier(type = DataBinding.class)
	public static final String CONDITIONAL_KEY = "conditional";
	@PropertyIdentifier(type = ActionMask.class)
	public static final String ACTION_MASK_KEY = "actionMask";
	@PropertyIdentifier(type = AbstractActionScheme.class)
	public static final String ABSTRACT_ACTION_SCHEME_KEY = "abstractActionScheme";

	@Getter(value = GRAPHICAL_ELEMENT_ROLE_KEY, inverse = GraphicalElementRole.ACTIONS_KEY)
	public GraphicalElementRole<?, ?> getGraphicalElementRole();

	@Setter(GRAPHICAL_ELEMENT_ROLE_KEY)
	public void setGraphicalElementRole(GraphicalElementRole<?, ?> role);

	@Getter(value = ACTION_MASK_KEY)
	@XMLAttribute
	public ActionMask getActionMask();

	@Setter(ACTION_MASK_KEY)
	public void setActionMask(ActionMask actionMask);

	@Getter(value = ABSTRACT_ACTION_SCHEME_KEY)
	@XMLElement
	public AbstractActionScheme getAbstractActionScheme();

	@Setter(ABSTRACT_ACTION_SCHEME_KEY)
	public void setAbstractActionScheme(AbstractActionScheme abstractActionScheme);

	@Getter(value = CONDITIONAL_KEY)
	@XMLElement
	public DataBinding<Boolean> getConditional();

	@Setter(CONDITIONAL_KEY)
	public void setConditional(DataBinding<Boolean> conditional);

	public boolean evaluateCondition(FlexoConceptInstance flexoConceptInstance);

	public static abstract class GraphicalElementActionImpl extends FlexoConceptObjectImpl implements GraphicalElementAction {

		private ActionMask actionMask = ActionMask.DoubleClick;
		private AbstractActionScheme abstractActionScheme;

		// private GraphicalElementRole graphicalElementPatternRole;

		private DataBinding<Boolean> conditional;

		public GraphicalElementActionImpl() {
			super();
		}

		@Override
		public String getURI() {
			return null;
		}

		@Override
		public Collection<? extends Validable> getEmbeddedValidableObjects() {
			return null;
		}

		/*@Override
		public GraphicalElementRole getGraphicalElementPatternRole() {
			return graphicalElementPatternRole;
		}

		@Override
		public void setGraphicalElementPatternRole(GraphicalElementRole graphicalElementPatternRole) {
			this.graphicalElementPatternRole = graphicalElementPatternRole;
		}*/

		@Override
		public DataBinding<Boolean> getConditional() {
			if (conditional == null) {
				conditional = new DataBinding<Boolean>(this, Boolean.class, DataBinding.BindingDefinitionType.GET);
				conditional.setBindingName("conditional");
			}
			return conditional;
		}

		@Override
		public void setConditional(DataBinding<Boolean> conditional) {
			if (conditional != null) {
				conditional.setOwner(this);
				conditional.setDeclaredType(Boolean.class);
				conditional.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
				conditional.setBindingName("conditional");
			}
			this.conditional = conditional;
		}

		@Override
		public boolean evaluateCondition(FlexoConceptInstance flexoConceptInstance) {
			if (getConditional().isValid()) {
				try {
					return getConditional().getBindingValue(flexoConceptInstance);
				} catch (TypeMismatchException e) {
					e.printStackTrace();
				} catch (NullReferenceException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			return true;
		}

		@Override
		public FlexoConcept getFlexoConcept() {
			return getGraphicalElementRole() != null ? getGraphicalElementRole().getFlexoConcept() : null;
		}

		@Override
		public BindingModel getBindingModel() {
			return getFlexoConcept().getBindingModel();
		}

		@Override
		public VirtualModel getVirtualModel() {
			return getFlexoConcept().getVirtualModel();
		}

		@Override
		public AbstractActionScheme getAbstractActionScheme() {
			return abstractActionScheme;
		}

		@Override
		public void setAbstractActionScheme(AbstractActionScheme abstractActionScheme) {
			this.abstractActionScheme = abstractActionScheme;
		}

		@Override
		public ActionMask getActionMask() {
			return actionMask;
		}

		@Override
		public void setActionMask(ActionMask actionMask) {
			this.actionMask = actionMask;
		}

	}
}
