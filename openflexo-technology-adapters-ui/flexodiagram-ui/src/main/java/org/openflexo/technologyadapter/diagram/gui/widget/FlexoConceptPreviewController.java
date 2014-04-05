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
package org.openflexo.technologyadapter.diagram.gui.widget;

import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

import org.openflexo.foundation.viewpoint.FlexoConcept;
import org.openflexo.selection.SelectionManager;
import org.openflexo.selection.SelectionManagingDianaEditor;

public class FlexoConceptPreviewController extends SelectionManagingDianaEditor<FlexoConcept> {

	private static final Logger logger = Logger.getLogger(FlexoConceptPreviewController.class.getPackage().getName());

	// We share here instances of FlexoConceptPreviewRepresentation because they can be accessed from multiple
	// FlexoConceptPreviewComponent
	private static final Map<FlexoConcept, FlexoConceptPreviewRepresentation> flexoConceptPreviewRepresentations = new Hashtable<FlexoConcept, FlexoConceptPreviewRepresentation>();

	/**
	 * Obtain or build stored FlexoConceptPreviewRepresentation (they are all shared because they can be accessed from multiple
	 * FlexoConceptPreviewComponent)
	 * 
	 * @param flexoConcept
	 * @return
	 */
	private static final FlexoConceptPreviewRepresentation obtainFlexoConceptPreviewRepresentations(FlexoConcept flexoConcept) {
		FlexoConceptPreviewRepresentation returned = flexoConceptPreviewRepresentations.get(flexoConcept);
		if (returned == null) {
			returned = new FlexoConceptPreviewRepresentation(flexoConcept);
			flexoConceptPreviewRepresentations.put(flexoConcept, returned);
		}
		return returned;
	}

	public FlexoConceptPreviewController(FlexoConcept flexoConcept, SelectionManager sm) {
		super(obtainFlexoConceptPreviewRepresentations(flexoConcept), sm, flexoConcept.getVirtualModelFactory(), null);
	}

	@Override
	public void delete() {
		// Drawing is no more deleted since we keep all instances for sharing !!!!
		// getDrawing().delete();
		super.delete();
	}

	public FlexoConcept getFlexoConcept() {
		return getDrawing().getModel();
	}

}
