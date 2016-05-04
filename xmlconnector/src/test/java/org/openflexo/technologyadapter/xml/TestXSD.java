/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
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

	protected static final Logger logger = Logger.getLogger(TestXSD.class.getPackage().getName());

	private static XMLTechnologyAdapter xmlAdapter;
	private static XSDMetaModelRepository mmRepository;
	private static XMLModelRepository modelRepository;
	private static String baseUrl = null;

	/**
	 * Instanciate test ResourceCenter
	 */
	@Test
	@TestOrder(1)
	public void test0LoadTestResourceCenter() {

		instanciateTestServiceManager(XMLTechnologyAdapter.class);

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
