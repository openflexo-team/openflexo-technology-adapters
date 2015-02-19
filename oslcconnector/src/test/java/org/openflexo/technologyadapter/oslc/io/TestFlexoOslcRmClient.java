package org.openflexo.technologyadapter.oslc.io;

import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.ResourceShape;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.technologyadapter.oslc.model.core.OSLCServiceProviderCatalog;
import org.openflexo.technologyadapter.oslc.rm.OSLCResourceResource;

public class TestFlexoOslcRmClient extends OpenflexoTestCase {

	private static OSLCResourceResource jazzResource;
	private static CreationFactory creationFactory;
	private static QueryCapability queryCapability;
	private static ResourceShape resourceShape;
	private static OSLCServiceProviderCatalog catalog;

	/*@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager();
		OSLCTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(
				OSLCTechnologyAdapter.class);
		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			OSLCRepository repository = resourceCenter.getRepository(OSLCRepository.class, technologicalAdapter);
			assertNotNull(repository);
			Collection<OSLCResourceResource> resources = repository.getAllResources();
			for (OSLCResourceResource resource : resources) {
				if (resource.getURI().endsWith("jazz.oslc")) {
					jazzResource = resource;
					catalog = (OSLCServiceProviderCatalog) resource.loadResourceData(null);
				}
			}
		}
		assertNotNull(jazzResource);
		assertNotNull(catalog);
	}*/

	/*@Test
	@TestOrder(2)
	public void testOslcRestClientRetrieveElements() {
		log("testOslcRestClientCreateRequirement()");

		try {
			Requirement requirement = new Requirement();
			requirement.setTitle("Test");
			assertNotNull(requirement);

			for (OSLCServiceProvider sp : catalog.getOSLCServiceProviders()) {
				for (OSLCService service : sp.getOSLCServices()) {
					if (service.getOSLCService().getDomain().toString().equals(OSLCConstants.OSLC_RM_V2)) {
						creationFactory = service.getOSLCCreationFactories().get(0).getOSLCCreationFactory();
						assertNotNull(creationFactory);
						queryCapability = service.getOSLCQueryCapabilities().get(0).getOSLCQueryCapability();
						assertNotNull(queryCapability);
						resourceShape = jazzResource.getConverter().getOslcClient()
								.getResourceShape(creationFactory, OslcMediaType.APPLICATION_RDF_XML, OSLCConstants.RM_REQUIREMENT_TYPE);
						assertNotNull(resourceShape);
					}
				}
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	/*@Test
	@TestOrder(2)
	public void testOslcRestClientCreateRequirement() {

		log("testOslcRestClientCreateRequirement()");
		try {
			Requirement requirement;
			requirement = new Requirement();
			requirement.setTitle("Test");
			assertNotNull(requirement);
			requirement.setInstanceShape(resourceShape.getAbout());
			jazzResource.getConverter().getOslcClient().create(creationFactory, requirement, OslcMediaType.APPLICATION_RDF_XML);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	/*@Test
	@TestOrder(6)
	public void testOslcRestClientRetrieveRequirement() {
		log("testOslcRestClientCreateRequirement()");
	}*/

	/*@Test
	@TestOrder(6)
	public void testOslcRestClientRemoveRequirement() {

		log("testOslcRestClientCreateRequirement()");

	}*/

}
