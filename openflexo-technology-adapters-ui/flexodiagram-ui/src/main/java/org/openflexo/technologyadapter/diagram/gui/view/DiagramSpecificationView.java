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
package org.openflexo.technologyadapter.diagram.gui.view;

import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.view.FIBModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * This is the module view representing a {@link DiagramSpecification}<br>
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class DiagramSpecificationView extends FIBModuleView<FlexoConcept> {

	public DiagramSpecificationView(FlexoConcept flexoConcept, FlexoController controller) {
		super(flexoConcept, controller, DiagramCst.DIAGRAM_SPECIFICATION_VIEW_FIB);
	}

	@Override
	public FlexoPerspective getPerspective() {
		return getFlexoController().getCurrentPerspective();
	}

}