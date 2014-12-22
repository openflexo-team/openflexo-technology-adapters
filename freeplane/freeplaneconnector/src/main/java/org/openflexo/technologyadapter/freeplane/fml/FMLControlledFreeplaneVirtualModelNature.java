package org.openflexo.technologyadapter.freeplane.fml;

import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.VirtualModelNature;
import org.openflexo.technologyadapter.freeplane.IFreeplaneModelSlot;

public class FMLControlledFreeplaneVirtualModelNature implements VirtualModelNature {

	public static final FMLControlledFreeplaneVirtualModelNature INSTANCE = new FMLControlledFreeplaneVirtualModelNature();

	private FMLControlledFreeplaneVirtualModelNature() {
	}

	@Override
	public boolean hasNature(final VirtualModel virtualModel) {
		return virtualModel.getModelSlots(IFreeplaneModelSlot.class).size() == 1;
	}

}
