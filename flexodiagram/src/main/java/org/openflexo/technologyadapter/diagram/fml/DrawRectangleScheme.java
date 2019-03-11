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

import org.openflexo.foundation.fml.AbstractCreationScheme;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.annotations.FML;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLAttribute;
import org.openflexo.pamela.annotations.XMLElement;
import org.openflexo.technologyadapter.diagram.fml.binding.DrawRectangleBindingModel;
import org.openflexo.toolbox.StringUtils;

/**
 * A creation behaviour triggered while drawing a rectangle
 * 
 * @author sylvain
 *
 */
@FIBPanel("Fib/DrawRectangleSchemePanel.fib")
@ModelEntity
@ImplementationClass(DrawRectangleScheme.DrawRectangleSchemeImpl.class)
@XMLElement
@FML("DrawRectangleScheme")
public interface DrawRectangleScheme extends AbstractCreationScheme, DiagramFlexoBehaviour {

	@PropertyIdentifier(type = String.class)
	public static final String TARGET_KEY = "target";
	@PropertyIdentifier(type = String.class)
	public static final String CHILDREN_KEY = "children";
	@PropertyIdentifier(type = Boolean.class)
	public static final String SELECT_OBJECTS_KEY = "selectObjects";
	@PropertyIdentifier(type = FlexoConcept.class)
	public static final String TARGET_FLEXO_CONCEPT_KEY = "targetFlexoConcept";
	@PropertyIdentifier(type = FlexoConcept.class)
	public static final String CHILDREN_FLEXO_CONCEPT_KEY = "childrenFlexoConcept";

	public static final String TOP_TARGET_KEY = "topTarget";

	@Getter(value = TARGET_KEY)
	@XMLAttribute
	public String _getTarget();

	@Setter(TARGET_KEY)
	public void _setTarget(String target);

	@Getter(value = CHILDREN_KEY)
	@XMLAttribute
	public String _getChildren();

	@Setter(CHILDREN_KEY)
	public void _setChildren(String children);

	@Getter(value = SELECT_OBJECTS_KEY, defaultValue = "false")
	@XMLAttribute
	public boolean getSelectObjects();

	@Setter(SELECT_OBJECTS_KEY)
	public void setSelectObjects(boolean selectObjects);

	public boolean isTopTarget();

	public boolean getTopTarget();

	public void setTopTarget(boolean flag);

	public FlexoConcept getTargetFlexoConcept();

	public void setTargetFlexoConcept(FlexoConcept childrenFlexoConcept);

	public FlexoConcept getChildrenFlexoConcept();

	public void setChildrenFlexoConcept(FlexoConcept childrenFlexoConcept);

	public boolean isValidTarget(FlexoConcept aTarget);

	public static abstract class DrawRectangleSchemeImpl extends AbstractCreationSchemeImpl implements DrawRectangleScheme {

		private static final String TOP = "top";

		private String target = TOP;
		private FlexoConcept lastKnownTargetFlexoConcept;
		private FlexoConcept targetFlexoConcept;

		private String children = null;
		private FlexoConcept lastKnownChildrenFlexoConcept;
		private FlexoConcept childrenFlexoConcept;

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
				getPropertyChangeSupport().firePropertyChange(TOP_TARGET_KEY, !isTopTarget(), isTopTarget());
			}
		}

		@Override
		public FlexoConcept getTargetFlexoConcept() {
			if (isTopTarget()) {
				return null;
			}
			if (targetFlexoConcept != null) {
				return targetFlexoConcept;
			}

			if (StringUtils.isEmpty(_getTarget())) {
				return null;
			}
			if (getOwningVirtualModel() != null) {
				targetFlexoConcept = getOwningVirtualModel().getFlexoConcept(_getTarget());
				if (lastKnownTargetFlexoConcept != targetFlexoConcept) {
					FlexoConcept oldValue = lastKnownTargetFlexoConcept;
					lastKnownTargetFlexoConcept = targetFlexoConcept;
					getPropertyChangeSupport().firePropertyChange(TARGET_FLEXO_CONCEPT_KEY, oldValue, targetFlexoConcept);
				}
				return targetFlexoConcept;
			}
			return null;
		}

		@Override
		public void setTargetFlexoConcept(FlexoConcept aTargetFlexoConcept) {
			FlexoConcept oldTargetFlexoConcept = this.targetFlexoConcept;
			this.targetFlexoConcept = aTargetFlexoConcept;
			_setTarget(aTargetFlexoConcept != null ? aTargetFlexoConcept.getURI() : null);
			getPropertyChangeSupport().firePropertyChange(TARGET_FLEXO_CONCEPT_KEY, oldTargetFlexoConcept, aTargetFlexoConcept);
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
			}
			else {
				_setTarget("");
			}
		}

		@Override
		public String _getChildren() {
			return children;
		}

		@Override
		public void _setChildren(String children) {
			if (requireChange(this.children, children)) {
				FlexoConcept oldValue = getChildrenFlexoConcept();
				this.children = children;
				getPropertyChangeSupport().firePropertyChange(CHILDREN_FLEXO_CONCEPT_KEY, oldValue, getChildrenFlexoConcept());
			}
		}

		@Override
		public FlexoConcept getChildrenFlexoConcept() {
			if (StringUtils.isEmpty(_getChildren())) {
				return null;
			}
			if (childrenFlexoConcept != null) {
				return childrenFlexoConcept;
			}

			if (StringUtils.isEmpty(_getChildren())) {
				return null;
			}
			if (getOwningVirtualModel() != null) {
				childrenFlexoConcept = getOwningVirtualModel().getFlexoConcept(_getChildren());
				if (lastKnownChildrenFlexoConcept != childrenFlexoConcept) {
					FlexoConcept oldValue = lastKnownChildrenFlexoConcept;
					lastKnownChildrenFlexoConcept = childrenFlexoConcept;
					getPropertyChangeSupport().firePropertyChange(CHILDREN_FLEXO_CONCEPT_KEY, oldValue, childrenFlexoConcept);
				}
				return childrenFlexoConcept;
			}
			return null;
		}

		@Override
		public void setChildrenFlexoConcept(FlexoConcept aChildrenFlexoConcept) {
			/*if (targetFlexoConcept != null) {
				setTopTarget(false);
			}*/
			FlexoConcept oldChildrenFlexoConcept = this.childrenFlexoConcept;
			this.childrenFlexoConcept = aChildrenFlexoConcept;
			_setChildren(aChildrenFlexoConcept != null ? aChildrenFlexoConcept.getURI() : null);
			getPropertyChangeSupport().firePropertyChange(CHILDREN_FLEXO_CONCEPT_KEY, oldChildrenFlexoConcept, aChildrenFlexoConcept);
			// updateBindingModels();
		}

		@Override
		public boolean isValidTarget(FlexoConcept aTarget) {
			if (getTargetFlexoConcept() != null && getTargetFlexoConcept().isAssignableFrom(aTarget)) {
				return true;
			}
			return false;
		}

		@Override
		protected DrawRectangleBindingModel makeBindingModel() {
			return new DrawRectangleBindingModel(this);
		}

	}
}
