package org.openflexo.jgitTests;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.GitResourceCenter;
import org.openflexo.technologyadapter.powerpoint.model.PowerpointSlideshow;

public class ResourceLifeTest {
	private GitResourceCenter gitResourceCenter;

	private File pptFile;

	private FlexoResource<PowerpointSlideshow> resource;

	@Before
	public void createResource() {
		// pptFile = new File(gitResourceCenter.getDirectory().getParentFile(),"test.ppt");
	}

	@Test
	public void registerResourceInGitRepository() {

	}

	@Test
	public void updateResource() {

	}

	public void deleteResource() {

	}
}
