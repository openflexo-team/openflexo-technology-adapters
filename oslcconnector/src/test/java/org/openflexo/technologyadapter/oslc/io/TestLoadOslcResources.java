package org.openflexo.technologyadapter.oslc.io;

import org.junit.Test;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.test.TestOrder;

public class TestLoadOslcResources extends OpenflexoTestCase {

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager();
	}

	/*@Test
	@TestOrder(2)
	public void testLoadOSLCResources() {
		log("testLoadOSLCResources()");
		OSLCTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(
				OSLCTechnologyAdapter.class);
	
		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			OSLCRepository repository = resourceCenter.getRepository(OSLCRepository.class, technologicalAdapter);
			assertNotNull(repository);
			Collection<OSLCResourceResource> resources = repository.getAllResources();
			for (OSLCResourceResource resource : resources) {
				try {
					resource.loadResourceData(null);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ResourceLoadingCancelledException e) {
					e.printStackTrace();
				} catch (FlexoException e) {
					e.printStackTrace();
				}
				assertNotNull(resource.getLoadedResourceData());
			}
		}
	}*/

}
