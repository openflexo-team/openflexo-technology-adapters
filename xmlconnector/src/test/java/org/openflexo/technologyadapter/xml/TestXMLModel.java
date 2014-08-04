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

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModelImpl;
import org.openflexo.technologyadapter.xml.model.XMLAttribute;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLModelImpl;
import org.openflexo.technologyadapter.xml.model.XMLType;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.xml.IXMLIndividual;

@RunWith(OrderedRunner.class)
public class TestXMLModel extends OpenflexoTestCase {

	protected static final Logger         logger = Logger.getLogger(TestXMLModel.class.getPackage().getName());

	private static final void dumpIndividual(IXMLIndividual<XMLIndividual, XMLAttribute> indiv, String prefix) {

		System.out.println(prefix + "Indiv : " +  indiv.getName() + "  [" + indiv.getUUID() + "]");
		for (XMLAttribute a : indiv.getAttributes()) {
			System.out.println(prefix + "    * attr: " + a.getName() + " = " + a.getValue().toString());
		}
		for (IXMLIndividual<XMLIndividual, XMLAttribute> x : indiv.getChildren())
			dumpIndividual(x, prefix + "    ");
		System.out.flush();
	}

	private static final void dumpTypes(XMLModel model) {
		for (XMLType t : model.getMetaModel().getTypes()) {
			System.out.println("Inferred Type: " + t.getName() + " -> " + t.getFullyQualifiedName() + "[" + t.getURI() + "]");
			System.out.flush();
		}

	}


	@Test
	@TestOrder(1)
	public void test0createXMLModel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		ModelFactory MF = null;
		ModelFactory MMF = null;
		MF = XMLModelImpl.getModelFactory();
		//new ModelFactory(XMLModel.class);
		MMF = XMLMetaModelImpl.getModelFactory();
		//new ModelFactory(XMLMetaModel.class);
		assertNotNull(MF);
		assertNotNull(MMF);
		
		XMLMetaModel metamodel = MMF.newInstance(XMLMetaModel.class);


		metamodel.setURI("http://www.openflexo.org/aTestModel");
		
		assertNotNull(metamodel);
		

		XMLModel model = MF.newInstance(XMLModel.class,metamodel);

		assertNotNull(model);

		

		model.setMetaModel(metamodel);

		metamodel.createNewType("http://www.openflexo.org/aTestModel#Fleumeu", "Fleumeu");
		metamodel.createNewType("http://www.openflexo.org/aTestModel#Flouk", "Flouk");

		XMLIndividual xmind = (XMLIndividual) model.addNewIndividual(metamodel.getTypeFromURI("http://www.openflexo.org/aTestModel#Fleumeu"));
		xmind.setName("Ploum");
		
		model.setRoot(xmind);

		xmind.createAttribute("TOTO", String.class, "Freumeuleu");

		XMLIndividual xmind2 = (XMLIndividual) model.addNewIndividual(metamodel.getTypeFromURI("http://www.openflexo.org/aTestModel#Flouk"));
		xmind2.setName("Pouet");

		xmind.addChild(xmind2);
		
		xmind2.createAttribute("TOTO", String.class, "Flagada");
		
		dumpIndividual(xmind," -- ");
	}


}
