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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.rm.XMLFileResource;
import org.openflexo.technologyadapter.xml.rm.XMLModelRepository;
import org.openflexo.technologyadapter.xml.rm.XMLResource;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestXMLResource extends OpenflexoTestCase {

	protected static final Logger logger = Logger.getLogger(TestXMLResource.class.getPackage().getName());

	private static XMLTechnologyAdapter xmlAdapter;
	private static XMLModelRepository<?> modelRepository;
	private static XSDMetaModelRepository<?> metaModelRepository;
	private static String baseUrl;

	/**
	 * Instanciate test ResourceCenter
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(1)
	public void test0LoadTestResourceCenter() throws IOException {
		log("test0LoadTestResourceCenter()");

		instanciateTestServiceManager(XMLTechnologyAdapter.class);

		FlexoResourceCenter<?> resourceCenter = serviceManager.getResourceCenterService()
				.getFlexoResourceCenter("http://openflexo.org/xml-test");

		xmlAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(XMLTechnologyAdapter.class);
		modelRepository = xmlAdapter.getXMLModelRepository(resourceCenter);
		metaModelRepository = xmlAdapter.getXSDMetaModelRepository(resourceCenter);
		baseUrl = resourceCenter.getDefaultBaseURI();
		assertNotNull(modelRepository);

		for (XMLFileResource r : modelRepository.getAllResources()) {
			System.out.println(" > " + r.getURI() + " mm=" + r.getMetaModelResource());
		}

		for (XSDMetaModelResource r : metaModelRepository.getAllResources()) {
			System.out.println(" >> " + r.getURI());
		}

		assertTrue(modelRepository.getAllResources().size() > 4);

	}

	@Test
	@TestOrder(2)
	public void test0LoadXMLResourcel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("test0LoadXMLResourcel()");

		assertNotNull(modelRepository);

		XMLResource modelRes = modelRepository.getResource(baseUrl + "/TestResourceCenter/XML/example_library_0.xml");

		assertNotNull(modelRes);
		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.getModelData());
		assertTrue(modelRes.isLoaded());

		// Helpers.dumpTypes(modelRes.getModel().getMetaModel());

		XMLType aType = modelRes.getModel().getMetaModel().getTypeFromURI(modelRes.getModel().getURI() + "/Metamodel#Library");
		assertNotNull(aType);

		Helpers.dumpIndividual(modelRes.getModelData().getRoot(), "");

	}

	@Test
	@TestOrder(3)
	public void test1LoadXMLResourcel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("test1LoadXMLResourcel()");

		assertNotNull(modelRepository);

		XMLResource modelRes = modelRepository.getResource(baseUrl + "/TestResourceCenter/XML/example_library_1.xml");
		assertNotNull(modelRes);

		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.getModelData());
		assertTrue(modelRes.isLoaded());

		// Helpers.dumpTypes(modelRes.getModel().getMetaModel());

		assertNotNull(modelRes.getModel().getMetaModel().getTypeFromURI("http://www.example.org/Library#Library"));

		Helpers.dumpIndividual(modelRes.getModelData().getRoot(), "");

	}

	@Test
	@TestOrder(4)
	public void test2LoadXMLResourcel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("test2LoadXMLResourcel()");

		assertNotNull(modelRepository);

		XMLResource modelRes = modelRepository.getResource(baseUrl + "/TestResourceCenter/XML/example_library_2.xml");
		assertNotNull(modelRes);
		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.getModelData());
		assertTrue(modelRes.isLoaded());

		// Helpers.dumpTypes(modelRes.getModel().getMetaModel());

		assertNotNull(modelRes.getModel().getMetaModel().getTypeFromURI("http://www.example.org/Library#Library"));

		Helpers.dumpIndividual(modelRes.getModelData().getRoot(), "");

	}

}
