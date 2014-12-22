/*
  * (c) Copyright 2014-2015 Openflexo
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
package org.openflexo.technologyadapter.diagram.fml.binding;

import java.beans.PropertyChangeEvent;

import org.openflexo.antar.binding.BindingModel;
import org.openflexo.antar.binding.BindingVariable;
import org.openflexo.foundation.fml.binding.FlexoBehaviourBindingModel;
import org.openflexo.technologyadapter.diagram.fml.DiagramFlexoBehaviour;
import org.openflexo.technologyadapter.diagram.model.Diagram;

/**
 * This is the {@link BindingModel} exposed by a {@link DiagramFlexoBehaviour}<br>
 * 
 * @author sylvain
 * 
 */
public abstract class DiagramBehaviourBindingModel extends FlexoBehaviourBindingModel {

	public static final String TOP_LEVEL = "topLevel";

	private final BindingVariable diagramBindingVariable;

	public DiagramBehaviourBindingModel(DiagramFlexoBehaviour flexoBehaviour) {
		super(flexoBehaviour);

		diagramBindingVariable = new BindingVariable(TOP_LEVEL, Diagram.class);
		addToBindingVariables(diagramBindingVariable);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}

	public BindingVariable getDiagramBindingVariable() {
		return diagramBindingVariable;
	}
}
