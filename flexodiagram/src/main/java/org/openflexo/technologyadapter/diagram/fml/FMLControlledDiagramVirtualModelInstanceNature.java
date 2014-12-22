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

import org.openflexo.foundation.fmlrt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fmlrt.VirtualModelInstance;
import org.openflexo.foundation.fmlrt.VirtualModelInstanceNature;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;

/**
 * Define the "controlled-diagram" nature of a {@link VirtualModelInstance}<br>
 * 
 * A {@link FMLControlledDiagramVirtualModelInstanceNature} might be seen as an interpretation of a given {@link VirtualModelInstance}
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramVirtualModelInstanceNature implements VirtualModelInstanceNature {

	public static FMLControlledDiagramVirtualModelInstanceNature INSTANCE = new FMLControlledDiagramVirtualModelInstanceNature();

	// Prevent external instantiation
	private FMLControlledDiagramVirtualModelInstanceNature() {
	}

	/**
	 * Return boolean indicating if supplied {@link VirtualModelInstance} might be interpreted as a FML-Controlled diagram
	 */
	@Override
	public boolean hasNature(VirtualModelInstance virtualModelInstance) {

		// The corresponding VirtualModel should have FMLControlledDiagramVirtualModelNature
		if (!virtualModelInstance.getVirtualModel().hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE)) {
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

	public static TypeAwareModelSlotInstance<Diagram, DiagramSpecification, TypedDiagramModelSlot> getModelSlotInstance(
			VirtualModelInstance virtualModelInstance) {
		return INSTANCE._getModelSlotInstance(virtualModelInstance);

	}

	public static Diagram getDiagram(VirtualModelInstance virtualModelInstance) {
		return INSTANCE._getDiagram(virtualModelInstance);
	}

	private TypeAwareModelSlotInstance<Diagram, DiagramSpecification, TypedDiagramModelSlot> _getModelSlotInstance(
			VirtualModelInstance virtualModelInstance) {
		TypedDiagramModelSlot diagramMS = virtualModelInstance.getVirtualModel().getModelSlots(TypedDiagramModelSlot.class).get(0);

		return (TypeAwareModelSlotInstance<Diagram, DiagramSpecification, TypedDiagramModelSlot>) virtualModelInstance
				.getModelSlotInstance(diagramMS);

	}

	private Diagram _getDiagram(VirtualModelInstance virtualModelInstance) {
		return _getModelSlotInstance(virtualModelInstance).getAccessedResourceData();
	}

}
