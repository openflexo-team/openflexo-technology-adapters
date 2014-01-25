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

import org.openflexo.foundation.viewpoint.EditionPattern;
import org.openflexo.technologyadapter.diagram.controller.DiagramCst;
import org.openflexo.view.FIBModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * This is the module view representing a diagram EditionPattern (an EditionPattern which is part of a DiagramSpecification)<br>
 * 
 * @author sguerin
 * 
 */
@SuppressWarnings("serial")
public class DiagramEditionPatternView extends FIBModuleView<EditionPattern> {

	public DiagramEditionPatternView(EditionPattern editionPattern, FlexoController controller) {
		super(editionPattern, controller, DiagramCst.DIAGRAM_EDITION_PATTERN_VIEW_FIB);
	}

	@Override
	public FlexoPerspective getPerspective() {
		return getFlexoController().getCurrentPerspective();
	}

}
