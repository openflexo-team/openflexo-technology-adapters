package org.openflexo.technologyadapter.powerpoint;

import java.util.List;
import java.util.logging.Logger;

import org.openflexo.foundation.fml.rt.action.CreateVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.ModelSlotInstanceConfiguration;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.technologyadapter.FreeModelSlotInstanceConfiguration;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;
import org.openflexo.technologyadapter.powerpoint.rm.PowerpointSlideshowResource;

public class BasicPowerpointModelSlotInstanceConfiguration extends
		FreeModelSlotInstanceConfiguration<PowerpointSlideshow, BasicPowerpointModelSlot> {

	private static final Logger logger = Logger.getLogger(ModelSlotInstanceConfiguration.class.getPackage().getName());

	protected List<ModelSlotInstanceConfigurationOption> options;

	protected FlexoResourceCenter<?> resourceCenter;
	protected PowerpointSlideshowResource modelResource;

	protected BasicPowerpointModelSlotInstanceConfiguration(BasicPowerpointModelSlot ms, CreateVirtualModelInstance action) {
		super(ms, action);
		setResourceUri(getAction().getFocusedObject().getProject().getURI() + "/Models/myPowerpointModel");
		setRelativePath("/");
		setFilename("myPowerpointResource.ppt");
	}

}
