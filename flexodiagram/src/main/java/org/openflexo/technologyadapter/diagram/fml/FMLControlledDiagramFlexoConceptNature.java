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

import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.FlexoConceptNature;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;

/**
 * Define the "controlled-diagram" nature of a {@link FlexoConcept}<br>
 * 
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramFlexoConceptNature implements FlexoConceptNature {

	public static FMLControlledDiagramFlexoConceptNature INSTANCE = new FMLControlledDiagramFlexoConceptNature();

	// Prevent external instantiation
	private FMLControlledDiagramFlexoConceptNature() {
	}

	/**
	 * Return boolean indicating if supplied {@link VirtualModel} might be interpreted as a FML-Controlled diagram
	 */
	@Override
	public boolean hasNature(FlexoConcept concept) {

		if (concept != null && concept.getVirtualModel() != null) {
			return concept.getVirtualModel().hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE);
		}
		return false;
	}

	public static TypedDiagramModelSlot getTypedDiagramModelSlot(FlexoConcept concept) {
		return INSTANCE._getTypedDiagramModelSlot(concept);
	}

	private TypedDiagramModelSlot _getTypedDiagramModelSlot(FlexoConcept concept) {
		return FMLControlledDiagramVirtualModelNature.getTypedDiagramModelSlot(concept.getVirtualModel());
	}
}
