/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012-2012, AgileBirds
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModelImpl;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.rm.XMLFileResource;
import org.openflexo.technologyadapter.xml.rm.XMLFileResourceImpl;
import org.openflexo.technologyadapter.xml.rm.XMLModelRepository;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestXML extends OpenflexoProjectAtRunTimeTestCase {

	protected static final Logger logger = Logger.getLogger(TestXML.class.getPackage().getName());

	private static XMLTechnologyAdapter xmlAdapter;
	private static XMLModelRepository modelRepository;
	private static String baseUrl;

	/**
	 * Instanciate test ResourceCenter
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(1)
	public void test0LoadTestResourceCenter() throws IOException {
		instanciateTestServiceManager(XMLTechnologyAdapter.class);

		log("test0LoadTestResourceCenter()");
		xmlAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(XMLTechnologyAdapter.class);
		modelRepository = resourceCenter.getRepository(XMLModelRepository.class, xmlAdapter);
		baseUrl = resourceCenter.getDirectory().toURI().toURL().toExternalForm();
		assertNotNull(modelRepository);
		assertTrue(modelRepository.getAllResources().size() > 3);
	}

	@Test
	@TestOrder(2)
	public void test0LoadFileAndDump() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("test1LoadFileAndDump()");

		assertNotNull(modelRepository);

		XMLFileResource modelRes = modelRepository.getResource(baseUrl + "TestResourceCenter/XML/example_library_0.xml");
		assertNotNull(modelRes);
		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.getModelData());
		assertNotNull(modelRes.loadResourceData(null));
		assertTrue(modelRes.isLoaded());

		// dumpTypes(modelRes.getModel());

		assertNotNull(modelRes.getModel().getMetaModel().getTypeFromURI(modelRes.getModel().getURI() + "/Metamodel#Library"));

		Helpers.dumpIndividual(modelRes.getModelData().getRoot(), "");

	}

	@Test
	@TestOrder(3)
	public void test1LoadFileAndDump() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("test1LoadFileAndDump()");

		assertNotNull(modelRepository);

		XMLFileResource modelRes = modelRepository.getResource(baseUrl + "TestResourceCenter/XML/example_library_1.xml");
		assertNotNull(modelRes);
		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.getModelData());
		assertNotNull(modelRes.loadResourceData(null));
		assertTrue(modelRes.isLoaded());

		// dumpTypes(modelRes.getModel());

		assertNotNull(modelRes.getModel().getMetaModel().getTypeFromURI("http://www.example.org/Library#Library"));

		Helpers.dumpIndividual(modelRes.getModelData().getRoot(), "");

	}

	@Test
	@TestOrder(4)
	public void test2LoadFileAndDump() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("test2LoadFileAndDump()");

		assertNotNull(modelRepository);

		XMLFileResource modelRes = modelRepository.getResource(baseUrl + "TestResourceCenter/XML/example_library_2.xml");
		assertNotNull(modelRes);
		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.getModelData());
		assertNotNull(modelRes.loadResourceData(null));
		assertTrue(modelRes.isLoaded());

		// dumpTypes(modelRes.getModel());

		assertNotNull(modelRes.getModel().getMetaModel().getTypeFromURI("http://www.example.org/Library#Library"));

		// dumpIndividual(modelRes.getModelData().getRoot(), "");
	}

	@Test
	@TestOrder(5)
	public void test3LoadFileAndDump() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("test3LoadFileAndDump()");

		assertNotNull(modelRepository);

		XMLFileResource modelRes = modelRepository.getResource(baseUrl + "TestResourceCenter/XML/example_library_3.xml");
		assertNotNull(modelRes);
		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.getModelData());
		assertNotNull(modelRes.loadResourceData(null));
		assertTrue(modelRes.isLoaded());

		assertNotNull(modelRes.getModel().getMetaModel().getTypeFromURI("http://www.example.org/Library#Library"));

		// Helpers.dumpTypes(modelRes.getModel());
		Helpers.dumpIndividual(modelRes.getModelData().getRoot(), "");
	}

	@Test
	@TestOrder(6)
	public void test1CreateNewFile() throws Exception {

		log("test1CreateNewFile()");

		assertNotNull(modelRepository);

		String fileUUID = UUID.randomUUID().toString();
		URI fileURI = new URI(baseUrl + "TestResourceCenter/GenXML/example_File_" + fileUUID + ".xml");

		File xmlFile = new File(fileURI);

		XMLFileResource modelRes = XMLFileResourceImpl.makeXMLFileResource(xmlFile,
				(XMLTechnologyContextManager) xmlAdapter.getTechnologyContextManager(), modelRepository.getResourceCenter());

		XMLModel aModel = modelRes.getModel();
		aModel.setNamespace("http://montest.com", "tst");

		// creating an empty MetaModel for this file and
		XMLMetaModel aMetamodel = XMLMetaModelImpl.createEmptyMetaModel("http://montest.com");
		Object blobType = aMetamodel.createNewType("http://montest.com#Blob", "Blob", false);
		aModel.setMetaModel(aMetamodel);

		XMLType aType = aMetamodel.createNewType("http://zutalors.com", "Blib", false);

		// TODO Manage several namespaces in same file!!
		// aType = new XMLType("http://zutalors.com", "Blib", "pt:Blib", aModel);
		// aModel.addType(aType);

		XMLIndividual rootIndividual = aModel.addNewIndividual(aModel.getMetaModel().getTypeFromURI("http://montest.com#Blob"));
		aModel.setRoot(rootIndividual);

		XMLIndividual anIndividual = aModel.addNewIndividual(aType);
		anIndividual.addPropertyValue("name", "Mon velo court");
		rootIndividual.addChild(anIndividual);

		anIndividual = aModel.addNewIndividual(aType);
		anIndividual.addPropertyValue("name", "Pan");
		anIndividual.addPropertyValue("ID", "17");
		rootIndividual.addChild(anIndividual);

		assertNotNull(anIndividual);

		Helpers.dumpTypes(aMetamodel);
		Helpers.dumpIndividual(modelRes.getModel().getRoot(), "");

		modelRes.save(null);

	}
}
