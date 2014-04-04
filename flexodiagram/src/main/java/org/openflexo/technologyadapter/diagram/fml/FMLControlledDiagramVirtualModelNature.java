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

import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.foundation.viewpoint.VirtualModelNature;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;

/**
 * Define the "controlled-diagram" nature of a {@link VirtualModel}<br>
 * 
 * A {@link FMLControlledDiagramVirtualModelNature} might be seen as an interpretation of a given {@link VirtualModelInstance}
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramVirtualModelNature implements VirtualModelNature {

	public static FMLControlledDiagramVirtualModelNature INSTANCE = new FMLControlledDiagramVirtualModelNature();

	// Prevent external instantiation
	private FMLControlledDiagramVirtualModelNature() {
	}

	/**
	 * Return boolean indicating if supplied {@link VirtualModel} might be interpreted as a FML-Controlled diagram
	 */
	@Override
	public boolean hasNature(VirtualModel virtualModel) {

		// VirtualModel should have one and only one TypedDiagramModelSlot
		if (virtualModel.getModelSlots(TypedDiagramModelSlot.class).size() != 1) {
			return false;
		}

		TypedDiagramModelSlot diagramMS = virtualModel.getModelSlots(TypedDiagramModelSlot.class).get(0);

		// The unique TypedDiagramModelSlot should have one MetaModel (a DiagramSpecification)
		if (diagramMS.getMetaModelResource() == null) {
			return false;
		}

		return true;
	}

	public static TypedDiagramModelSlot getTypedDiagramModelSlot(VirtualModel virtualModel) {
		return INSTANCE._getTypedDiagramModelSlot(virtualModel);
	}

	private TypedDiagramModelSlot _getTypedDiagramModelSlot(VirtualModel virtualModel) {
		if (virtualModel != null && virtualModel.getModelSlots(TypedDiagramModelSlot.class).size() == 1) {
			return virtualModel.getModelSlots(TypedDiagramModelSlot.class).get(0);
		}
		return null;
	}
}
