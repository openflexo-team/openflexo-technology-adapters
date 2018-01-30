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

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.xml.metamodel.XMLComplexType;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XMLMetaModelImpl;
import org.openflexo.technologyadapter.xml.metamodel.XMLProperty;
import org.openflexo.technologyadapter.xml.metamodel.XMLSimpleType;
import org.openflexo.technologyadapter.xml.metamodel.XMLType;
import org.openflexo.technologyadapter.xml.model.XMLIndividual;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.model.XMLModelImpl;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestXMLModel extends OpenflexoTestCase {

	protected static final Logger logger = Logger.getLogger(TestXMLModel.class.getPackage().getName());

	private static final void dumpIndividual(XMLIndividual indiv, String prefix) {

		System.out.println(prefix + "Indiv : " + indiv.getName() + "  [" + indiv.getUUID() + "]");
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

		XMLModel model = MF.newInstance(XMLModel.class, metamodel);

		assertNotNull(model);

		model.setMetaModel(metamodel);

		XMLSimpleType ts = (XMLSimpleType) metamodel.createNewType(XMLMetaModel.STR_SIMPLETYPE_URI, "BASIC_STRING", true);

		XMLComplexType t = (XMLComplexType) metamodel.createNewType("http://www.openflexo.org/aTestModel#Fleumeu", "Fleumeu", false);
		t.createProperty("TOTO", ts);

		t = (XMLComplexType) metamodel.createNewType("http://www.openflexo.org/aTestModel#Flouk", "Flouk", false);
		t.createProperty("TOTO", ts);

		XMLIndividual xmind = model.addNewIndividual(metamodel.getTypeFromURI("http://www.openflexo.org/aTestModel#Fleumeu"));

		model.setRoot(xmind);

		xmind.addPropertyValue("TOTO", "Freumeuleu");

		XMLIndividual xmind2 = model.addNewIndividual(metamodel.getTypeFromURI("http://www.openflexo.org/aTestModel#Flouk"));

		xmind.addChild(xmind2);

		xmind2.addPropertyValue("TOTO", "Flagada");
		xmind2.addPropertyValue("TUTU", "Zogloubi");

		dumpIndividual(xmind, " -- ");
	}

}
