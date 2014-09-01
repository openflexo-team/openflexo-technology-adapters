package org.openflexo.technologyadapter.freeplane.fml;

import org.openflexo.foundation.view.FreeModelSlotInstance;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.view.VirtualModelInstanceNature;
import org.openflexo.technologyadapter.freeplane.IFreeplaneModelSlot;
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

	private FreeModelSlotInstance<IFreeplaneMap, IFreeplaneModelSlot> getModelSlotInstance(final VirtualModelInstance vmi) {
		final IFreeplaneModelSlot modelSlot = vmi.getVirtualModel().getModelSlots(IFreeplaneModelSlot.class).get(0);
		return (FreeModelSlotInstance<IFreeplaneMap, IFreeplaneModelSlot>) vmi.getModelSlotInstance(modelSlot);
	}

}
