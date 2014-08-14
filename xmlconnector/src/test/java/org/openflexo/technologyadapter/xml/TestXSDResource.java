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
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.model.XMLAttribute;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLType;
import org.openflexo.technologyadapter.xml.rm.XMLModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.xml.IXMLIndividual;

@RunWith(OrderedRunner.class)
public class TestXSDResource extends OpenflexoTestCase {

	protected static final Logger         logger = Logger.getLogger(TestXSDResource.class.getPackage().getName());

	private static XMLTechnologyAdapter   xmlAdapter;
	private static XMLModelRepository     modelRepository;
	private static XSDMetaModelRepository     mmRepository;
	private static String                 baseUrl;
	

	private static final void dumpIndividual(IXMLIndividual<XMLIndividual, XMLAttribute> indiv, String prefix) {

		System.out.println(prefix + "Indiv : " +  indiv.getName() + "  [" + indiv.getUUID() + "]");
		for (XMLAttribute a : indiv.getAttributes()) {
			System.out.println(prefix + "    * attr: " + a.getName() + " = " + a.getValue().toString());
		}
		for (IXMLIndividual<XMLIndividual, XMLAttribute> x : indiv.getChildren())
			dumpIndividual(x, prefix + "    ");
		System.out.flush();
	}

	private static final void dumpTypes(XMLMetaModel metamodel) {
		for (XMLType t : metamodel.getTypes()) {
			System.out.println("Parsed Type: " + t.getName() + " -> " + t.getFullyQualifiedName() + "[" + t.getURI() + "]");
			for ( XMLAttribute x : t.getAttributes()) {
				System.out.println("     --- " + x.getName());
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
		mmRepository = resourceCenter.getRepository(XSDMetaModelRepository.class, xmlAdapter);
		baseUrl = resourceCenter.getDirectory().toURI().toURL().toExternalForm();
		assertNotNull(modelRepository);
		assertTrue(modelRepository.getAllResources().size() > 6);
		assertNotNull(mmRepository);
		assertTrue(mmRepository.getAllResources().size()  > 2);
		/*
			Found an XSD with uri: http://www.example.org/Library(library.xsd)
			Found an XSD with uri: http://maven.apache.org/POM/4.0.0(maven-v4_0_0.xsd)
			Found an XSD with uri: http://www.taskcoach.org/TSK_XSD(taskcoach.xsd)
		 */
	}

	/**
	 * Load and dump the types found in Library MM
	 * @throws FlexoException 
	 * @throws ResourceLoadingCancelledException 
	 * @throws FileNotFoundException 
	 * 
	 */

	@Test
	@TestOrder(2)
	public void test1LoadLibraryMetamodel () throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		
		
		XSDMetaModelResource mmRes = mmRepository.getResource("http://www.example.org/Library");
		
		assertNotNull(mmRes);
		assertFalse(mmRes.isLoaded());
		if (!mmRes.isLoaded()){
			mmRes.loadResourceData(null);
		}
		assertTrue(mmRes.isLoaded());
		
		dumpTypes(mmRes.getMetaModelData());
		
	}
	
	
}
