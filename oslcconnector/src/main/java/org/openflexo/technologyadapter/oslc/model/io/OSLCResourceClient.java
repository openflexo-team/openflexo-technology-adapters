package org.openflexo.technologyadapter.oslc.model.io;

import org.eclipse.lyo.oslc4j.core.model.AbstractResource;

public class OSLCResourceClient extends OSLCClient<AbstractResource> {

	public OSLCResourceClient(String uri) {
		super(AbstractResource.class, uri);
	}

	@Override
	public AbstractResource getResource() {
		final AbstractResource artifact = null;
		return artifact;
	}

	@Override
	protected String getResourceType() {
		return "";
	}
}
