package org.openflexo.technologyadapter.oslc.io;

import org.junit.Test;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.test.TestOrder;

public class TestFlexoOslcClient extends OpenflexoTestCase {

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager();
	}

	@Test
	@TestOrder(2)
	public void testOslcRestClientGetResource() {
		log("testOslcRestClientGetResource()");
	}

	@Test
	@TestOrder(3)
	public void testOslcRestClientGetResourceoAuth() {
		log("testOslcRestClientGetResourceoAuth()");
	}

	@Test
	@TestOrder(4)
	public void testOslcRestClientServiceProviders() {
		log("testOslcRestClientServiceProviders()");
	}

	@Test
	@TestOrder(5)
	public void testOslcRestClientQueryCapabilities() {
		log("testOslcRestClientQueryCapabilities()");
	}

	@Test
	@TestOrder(6)
	public void testOslcRestClientCreationFactories() {
		log("testOslcRestClientCreationFactories()");
	}

}
