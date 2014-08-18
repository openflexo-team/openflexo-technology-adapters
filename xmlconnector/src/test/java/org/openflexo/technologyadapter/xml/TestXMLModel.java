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
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLModelImpl;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestXMLModel extends OpenflexoTestCase {

	protected static final Logger         logger = Logger.getLogger(TestXMLModel.class.getPackage().getName());

	private static final void dumpIndividual(XMLIndividual indiv, String prefix) {

		System.out.println(prefix + "Indiv : " +  indiv.getName() + "  [" + indiv.getUUID() + "]");
		for (XMLProperty a : indiv.getType().getProperties()) {
			System.out.println(prefix + "    * attr: " + a.getName() + " = " + indiv.getPropertyStringValue(a));
		}
		for (XMLIndividual x : indiv.getChildren())
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
		MMF = XMLMetaModelImpl.getModelFactory();

		assertNotNull(MMF);
		assertNotNull(MF);
		
		XMLMetaModel metamodel = MMF.newInstance(XMLMetaModel.class);
		metamodel.setReadOnly(false);


		metamodel.setURI("http://www.openflexo.org/aTestModel");
		
		assertNotNull(metamodel);
		

		XMLModel model = MF.newInstance(XMLModel.class,metamodel);

		assertNotNull(model);

		
		model.setMetaModel(metamodel);

		XMLType t = (XMLType) metamodel.createNewType("http://www.openflexo.org/aTestModel#Fleumeu", "Fleumeu");
		t.createProperty("TOTO", String.class);
		
		t = (XMLType) metamodel.createNewType("http://www.openflexo.org/aTestModel#Flouk", "Flouk");
		t.createProperty("TOTO", String.class);
		

		XMLIndividual xmind = (XMLIndividual) model.addNewIndividual(metamodel.getTypeFromURI("http://www.openflexo.org/aTestModel#Fleumeu"));
		
		model.setRoot(xmind);

		xmind.addPropertyValue("TOTO", "Freumeuleu");

		XMLIndividual xmind2 = (XMLIndividual) model.addNewIndividual(metamodel.getTypeFromURI("http://www.openflexo.org/aTestModel#Flouk"));

		xmind.addChild(xmind2);
		
		xmind2.addPropertyValue("TOTO", "Flagada");
		xmind2.addPropertyValue("TUTU", "Zogloubi");
		
		dumpIndividual(xmind," -- ");
	}


}
