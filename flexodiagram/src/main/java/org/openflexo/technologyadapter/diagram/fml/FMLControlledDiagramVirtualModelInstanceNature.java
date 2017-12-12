/**
 * 
 * Copyright (c) 2014, Openflexo
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

import java.awt.image.BufferedImage;

import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.TypeAwareModelSlotInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceNature;
import org.openflexo.foundation.nature.ScreenshotableNature;
import org.openflexo.technologyadapter.diagram.TypedDiagramModelSlot;
import org.openflexo.technologyadapter.diagram.metamodel.DiagramSpecification;
import org.openflexo.technologyadapter.diagram.model.Diagram;

/**
 * Define the "controlled-diagram" nature of a {@link FMLRTVirtualModelInstance}<br>
 * 
 * A {@link FMLControlledDiagramVirtualModelInstanceNature} might be seen as an interpretation of a given {@link FMLRTVirtualModelInstance}
 * 
 * @author sylvain
 * 
 */
public class FMLControlledDiagramVirtualModelInstanceNature
		implements VirtualModelInstanceNature, ScreenshotableNature<FMLRTVirtualModelInstance> {

	public static FMLControlledDiagramVirtualModelInstanceNature INSTANCE = new FMLControlledDiagramVirtualModelInstanceNature();

	// Prevent external instantiation
	private FMLControlledDiagramVirtualModelInstanceNature() {
	}

	/**
	 * Return boolean indicating if supplied {@link FMLRTVirtualModelInstance} might be interpreted as a FML-Controlled diagram
	 */
	@Override
	public boolean hasNature(FMLRTVirtualModelInstance virtualModelInstance) {

		// The corresponding VirtualModel should have FMLControlledDiagramVirtualModelNature
		VirtualModel virtualModel = virtualModelInstance.getVirtualModel();
		if (virtualModel == null || !virtualModel.hasNature(FMLControlledDiagramVirtualModelNature.INSTANCE)) {
			return false;
		}

		TypedDiagramModelSlot diagramMS = virtualModel.getModelSlots(TypedDiagramModelSlot.class).get(0);

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
			FMLRTVirtualModelInstance virtualModelInstance) {
		return INSTANCE._getModelSlotInstance(virtualModelInstance);

	}

	public static Diagram getDiagram(FMLRTVirtualModelInstance virtualModelInstance) {
		return INSTANCE._getDiagram(virtualModelInstance);
	}

	private TypeAwareModelSlotInstance<Diagram, DiagramSpecification, TypedDiagramModelSlot> _getModelSlotInstance(
			FMLRTVirtualModelInstance virtualModelInstance) {
		TypedDiagramModelSlot diagramMS = virtualModelInstance.getVirtualModel().getModelSlots(TypedDiagramModelSlot.class).get(0);

		return (TypeAwareModelSlotInstance<Diagram, DiagramSpecification, TypedDiagramModelSlot>) virtualModelInstance
				.getModelSlotInstance(diagramMS);

	}

	private Diagram _getDiagram(FMLRTVirtualModelInstance virtualModelInstance) {
		TypeAwareModelSlotInstance<Diagram, DiagramSpecification, TypedDiagramModelSlot> diagramModelSlotInstance = _getModelSlotInstance(
				virtualModelInstance);
		if (diagramModelSlotInstance != null) {
			return diagramModelSlotInstance.getAccessedResourceData();
		}
		return null;
	}

	@Override
	public BufferedImage getScreenshot(FMLRTVirtualModelInstance object) {
		System.out.println("Please perform the screenshot here !!!!!!!!!");
		return null;
	}
}
