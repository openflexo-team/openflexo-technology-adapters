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

import org.openflexo.foundation.view.TypeAwareModelSlotInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.view.VirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;

/**
 * Define the "controlled-diagram" nature of a {@link VirtualModelInstance}<br>
 * 
 * A {@link ControlledDiagramInstanceNature} might be seen as an interpretation of a given {@link VirtualModelInstance}
 * 
 * @author sylvain
 * 
 * @param <E>
 *            type of introspected concept
 */
public class ControlledDiagramInstanceNature implements VirtualModelInstanceNature {

	public static ControlledDiagramInstanceNature INSTANCE = new ControlledDiagramInstanceNature();

	// Prevent external instantiation
	private ControlledDiagramInstanceNature() {
	}

	/**
	 * Return boolean indicating if supplied {@link VirtualModelInstance} might be interpreted as a FML-Controlled diagram
	 */
	@Override
	public boolean hasNature(VirtualModelInstance virtualModelInstance) {

		// The corresponding VirtualModel should have ControlledDiagramNature
		if (ControlledDiagramNature.INSTANCE.hasNature(virtualModelInstance.getVirtualModel())) {
			return false;
		}

		TypedDiagramModelSlot diagramMS = virtualModelInstance.getVirtualModel().getModelSlots(TypedDiagramModelSlot.class).get(0);

		TypeAwareModelSlotInstance<Diagram, DiagramSpecification, TypedDiagramModelSlot> msInstance = (TypeAwareModelSlotInstance<Diagram, DiagramSpecification, TypedDiagramModelSlot>) virtualModelInstance
				.getModelSlotInstance(diagramMS);

		if (msInstance == null) {
			return false;
		}

		if (msInstance.getAccessedResourceData() == null) {
			return false;
		}

		return true;
	}
}
