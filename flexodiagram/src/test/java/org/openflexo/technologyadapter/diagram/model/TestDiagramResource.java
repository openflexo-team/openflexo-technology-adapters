package org.openflexo.technologyadapter.diagram.model;

import java.io.File;

import org.junit.Test;
import org.openflexo.foundation.TestFlexoServiceManager;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.rm.FileResourceImpl;
import org.openflexo.rm.FileSystemResourceLocatorImpl;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;

/**
 * Test ProjectDataResource in Openflexo ResourceManager
 * 
 * @author sylvain
 * 
 */
public class TestDiagramResource {

	/**
	 * Test the diagram resource
	 */
	@Test
	public void testDiagramResource() {

		final FileSystemResourceLocatorImpl fsrl = new FileSystemResourceLocatorImpl();
		fsrl.appendToDirectories(System.getProperty("user.dir"));
		ResourceLocator.appendDelegate(fsrl);

		// try {
		TestFlexoServiceManager applicationContext = new TestFlexoServiceManager(((FileResourceImpl) ResourceLocator.locateResource("src/test/resources")).getFile());
		DiagramTechnologyAdapter technologicalAdapter = applicationContext.getTechnologyAdapterService().getTechnologyAdapter(
				DiagramTechnologyAdapter.class);

		FlexoResourceCenter<?> resourceCenter = applicationContext.getResourceCenterService().getResourceCenters().get(0);
		System.out.println ("RESOURCE CENTER: " + resourceCenter.toString());
		
		// }
	}
}
