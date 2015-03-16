package org.openflexo.technologyadapter.oslc.model.io;

import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.openflexo.technologyadapter.oslc.model.core.OSLCResource;

public interface OSLCModelDedicatedConverter {

	public <AR extends AbstractResource> boolean handleAbstractResource(AR resource);

	public <AR extends AbstractResource, OR extends OSLCResource> OR convertAbstractResource(AR resource);

}
