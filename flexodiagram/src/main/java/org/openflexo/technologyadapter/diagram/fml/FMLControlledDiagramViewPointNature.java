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

import java.util.ArrayList;
import java.util.List;

import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.ViewPointNature;
import org.openflexo.foundation.viewpoint.VirtualModel;

/**
 * Define the "controlled-diagram" nature of a {@link ViewPoint}<br>
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramViewPointNature implements ViewPointNature {

	public static FMLControlledDiagramViewPointNature INSTANCE = new FMLControlledDiagramViewPointNature();

	// Prevent external instantiation
	private FMLControlledDiagramViewPointNature() {
	}

	/**
	 * Return boolean indicating if supplied {@link VirtualModelInstance} might be interpreted as a FML-Controlled diagram
	 */
	@Override
	public boolean hasNature(ViewPoint viewPoint) {
		for (VirtualModel vm : viewPoint.getVirtualModels()) {
			if (vm.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE)) {
				return true;
			}
		}
		return false;
	}

	public static List<VirtualModel> getControlledDiagramVirtualModels(ViewPoint viewPoint) {
		return INSTANCE._getControlledDiagramVirtualModels(viewPoint);
	}

	private List<VirtualModel> _getControlledDiagramVirtualModels(ViewPoint viewPoint) {
		List<VirtualModel> returned = new ArrayList<VirtualModel>();
		for (VirtualModel vm : viewPoint.getVirtualModels()) {
			if (vm.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE)) {
				returned.add(vm);
			}
		}
		return returned;
	}

}
