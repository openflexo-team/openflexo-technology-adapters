package org.openflexo.technologyadapter.xml;

import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.technologyadapter.FreeModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.xml.model.XMLModel;

public class FreeXMLModelSlotInstanceConfiguration extends FreeModelSlotInstanceConfiguration<XMLModel,FreeXMLModelSlot> {

	protected FreeXMLModelSlotInstanceConfiguration(FreeXMLModelSlot ms, CreateVirtualModelInstance action) {
		super(ms, action);
	}

}
