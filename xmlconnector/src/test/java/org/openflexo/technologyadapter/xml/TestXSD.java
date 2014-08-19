/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.technologyadapter.xml.rm.XMLModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestXSD extends OpenflexoProjectAtRunTimeTestCase {

	protected static final Logger         logger  = Logger.getLogger(TestXSD.class.getPackage().getName());

	private static XMLTechnologyAdapter   xmlAdapter;
	private static XSDMetaModelRepository mmRepository;
	private static XMLModelRepository  modelRepository;
	private static String                 baseUrl = null;

	/**
	 * Instanciate test ResourceCenter
	 */
	@Test
	@TestOrder(1)
	public void test0LoadTestResourceCenter() {

		instanciateTestServiceManager();

		log("test0LoadTestResourceCenter()");
		xmlAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(XMLTechnologyAdapter.class);
		try {
			baseUrl = resourceCenter.getDirectory().toURI().toURL().toExternalForm();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mmRepository = resourceCenter.getRepository(XSDMetaModelRepository.class, xmlAdapter);
		modelRepository = resourceCenter.getRepository(XMLModelRepository.class, xmlAdapter);
		assertNotNull(mmRepository);
		assertNotNull(modelRepository);
		assertEquals(3, mmRepository.getAllResources().size());
	}

	@Test
	@TestOrder(2)
	public void test1LibraryMetaModelPresentAndLoaded() {
		log("test1LibraryMetaModelPresentAndLoaded()");
		XSDMetaModelResource libraryRes = mmRepository.getResource("http://www.example.org/Library");
		assertNotNull(libraryRes);
		
		assertFalse(libraryRes.isLoaded());
		assertNotNull(libraryRes.getMetaModelData());
		assertTrue(libraryRes.isLoaded());

	}

	@Test
	@TestOrder(3)
	public void test2MavenMetaModelPresentAndLoaded() {
		log("test2MavenMetaModelPresentAndLoaded()");
		XSDMetaModelResource mavenRes = mmRepository.getResource("http://maven.apache.org/POM/4.0.0");
		assertNotNull(mavenRes);
		assertFalse(mavenRes.isLoaded());
		assertNotNull(mavenRes.getMetaModelData());
		assertTrue(mavenRes.isLoaded());

	}

}
