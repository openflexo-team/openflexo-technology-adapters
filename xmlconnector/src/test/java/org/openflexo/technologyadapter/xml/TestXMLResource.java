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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.rm.XMLModelRepository;
import org.openflexo.technologyadapter.xml.rm.XMLResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestXMLResource extends OpenflexoTestCase {

	protected static final Logger         logger = Logger.getLogger(TestXMLResource.class.getPackage().getName());

	private static XMLTechnologyAdapter   xmlAdapter;
	private static XMLModelRepository     modelRepository;
	private static String                 baseUrl;

	private static final void dumpIndividual(XMLIndividual indiv, String prefix) {

		System.out.println(prefix + "Indiv : " +  indiv.getName() + "  [" + indiv.getUUID() + "]");
		for (XMLProperty a : indiv.getProperties()) {
			System.out.println(prefix + "    * attr: " + a.getName() + " = " + a.getValue().toString());
		}
		for (XMLIndividual x : indiv.getChildren())
			dumpIndividual(x, prefix + "    ");
		System.out.flush();
	}

	private static final void dumpTypes(XMLMetaModel metamodel) {
		for (XMLType t : metamodel.getTypes()) {
			String prefix = new String();
			if ( t.isAbstract() ) prefix = "*";
			else prefix = "-";
			if (t.getSuperType() != null){
				System.out.println("Metamodel Type: "+prefix + t.getName() + " :: " + t.getSuperType().getName() );
			}
			else {
				System.out.println("Metamodel Type: "+prefix + t.getName() );

			}
			for ( XMLProperty x : t.getProperties()) {
				System.out.println("     --- " + x.getName() + "  :: " + x.getType().getName());
			}
			System.out.flush();
		}

	}



	/**
	 * Instanciate test ResourceCenter
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(1)
	public void test0LoadTestResourceCenter() throws IOException {
		instanciateTestServiceManager();

		log("test0LoadTestResourceCenter()");
		xmlAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(XMLTechnologyAdapter.class);
		modelRepository = resourceCenter.getRepository(XMLModelRepository.class, xmlAdapter);
		baseUrl = resourceCenter.getDirectory().toURI().toURL().toExternalForm();
		assertNotNull(modelRepository);
		assertTrue(modelRepository.getAllResources().size() > 6);
	}
	
	@Test
	@TestOrder(2)
	public void test0LoadXMLResourcel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("test0LoadXMLResourcel()");

		assertNotNull(modelRepository);

		XMLResource modelRes = modelRepository.getResource(baseUrl + "TestResourceCenter/XML/example_library_0.xml");
		assertNotNull(modelRes);
		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.getModelData());
		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.loadResourceData(null));
		assertTrue(modelRes.isLoaded());

		dumpTypes(modelRes.getModel().getMetaModel());

		assertNotNull(modelRes.getModel().getMetaModel().getTypeFromURI(modelRes.getModel().getURI() + "/Metamodel#Library"));

		System.out.println("\n\n");
		
		dumpIndividual(modelRes.getModelData().getRoot(), "");

	}


	@Test
	@TestOrder(3)
	public void test1LoadXMLResourcel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("test1LoadXMLResourcel()");

		assertNotNull(modelRepository);

		XMLResource modelRes = modelRepository.getResource(baseUrl + "TestResourceCenter/XML/example_library_1.xml");
		assertNotNull(modelRes);
		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.getModelData());
		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.loadResourceData(null));
		assertTrue(modelRes.isLoaded());

	//	dumpTypes(modelRes.getModel().getMetaModel());

		assertNotNull(modelRes.getModel().getMetaModel().getTypeFromURI("http://www.example.org/Library#Library"));

//		dumpIndividual(modelRes.getModelData().getRoot(), "");

	}

	@Test
	@TestOrder(4)
	public void test2LoadXMLResourcel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("test2LoadXMLResourcel()");

		assertNotNull(modelRepository);

		XMLResource modelRes = modelRepository.getResource(baseUrl + "TestResourceCenter/XML/example_library_2.xml");
		assertNotNull(modelRes);
		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.getModelData());
		assertFalse(modelRes.isLoaded());
		assertNotNull(modelRes.loadResourceData(null));
		assertTrue(modelRes.isLoaded());

		dumpTypes(modelRes.getModel().getMetaModel());

		
		assertNotNull(modelRes.getModel().getMetaModel().getTypeFromURI("http://www.example.org/Library#Library"));

		System.out.println("\n\n");
		
		dumpIndividual(modelRes.getModelData().getRoot(), "");

	}


}
