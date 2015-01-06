package org.openflexo.technologyadapter.freeplane.fml;

import org.openflexo.foundation.fml.rt.FreeModelSlotInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstanceNature;
import org.openflexo.technologyadapter.freeplane.FreeplaneModelSlot;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;

public class FMLControlledFreeplaneVirtualModelInstanceNature implements VirtualModelInstanceNature {

	public static final FMLControlledFreeplaneVirtualModelInstanceNature INSTANCE = new FMLControlledFreeplaneVirtualModelInstanceNature();

	@Override
	public boolean hasNature(final VirtualModelInstance concept) {
		if (concept != null && concept.getVirtualModel() != null) {
			return concept.getVirtualModel().hasNature(FMLControlledFreeplaneVirtualModelNature.INSTANCE);
		}
		return false;
	}

	public static IFreeplaneMap getMap(final VirtualModelInstance vmi) {
		return INSTANCE.getModelSlotInstance(vmi).getAccessedResourceData();
	}

	private FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot> getModelSlotInstance(final VirtualModelInstance vmi) {
		final FreeplaneModelSlot modelSlot = vmi.getVirtualModel().getModelSlots(FreeplaneModelSlot.class).get(0);
		return (FreeModelSlotInstance<IFreeplaneMap, FreeplaneModelSlot>) vmi.getModelSlotInstance(modelSlot);
	}

}
