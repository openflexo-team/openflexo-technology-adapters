/**
 * 
 * Copyright (c) 2013-2014, Openflexo
 * Copyright (c) 2012, THALES SYSTEMES AEROPORTES - All Rights Reserved
 * Copyright (c) 2012-2012, AgileBirds
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyFeatureAssociation;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test EMF Meta-Model and model loading.
 * 
 * @author gbesancon
 * 
 */
@RunWith(OrderedRunner.class)
public class TestLoadEMFMetaModelFromClassPath extends OpenflexoTestCase {
	protected static final Logger logger = Logger.getLogger(TestLoadEMFMetaModelFromClassPath.class.getPackage().getName());

	private static EMFTechnologyAdapter technologicalAdapter;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(EMFTechnologyAdapter.class);

		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(EMFTechnologyAdapter.class);
	}

	@Test
	@TestOrder(2)
	public void testConvertAllEMFMetaModel() {

		Collection<EMFMetaModelResource> metaModelResources = technologicalAdapter.getTechnologyContextManager().getAllMetaModelResources();

		for (EMFMetaModelResource mmResource : metaModelResources) {

			System.out.println("\t Loading and Converting " + mmResource.getURI());
			long startTime = System.currentTimeMillis();

			EMFMetaModel metamodel = mmResource.getMetaModelData();

			assertNotNull(metamodel);
			assertNull(metamodel.getRootConcept());

			long endTime = System.currentTimeMillis();

			System.out.println("\t\t MetaModel Conversion  took " + (endTime - startTime) + " milliseconds");

		}
	}

	@Test
	@TestOrder(3)
	public void testECoreMetaModel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		// EMFMetaModelResource eCoreRes = technologicalAdapter.getEMFMetaModelRepository(resourceCenter)
		// .getResource("http://www.eclipse.org/emf/2002/Ecore");

		EMFMetaModelResource eCoreRes = technologicalAdapter.getTechnologyContextManager()
				.getMetaModelResourceByURI(EMFTechnologyAdapter.ECORE_MM_URI);
		assertNotNull(eCoreRes);

		EMFMetaModel eCoreMM = eCoreRes.loadResourceData(null);
		assertNotNull(eCoreMM);

		for (IFlexoOntologyClass<EMFTechnologyAdapter> emfClass : eCoreMM.getClasses()) {
			System.out.println("* " + emfClass + " uri=" + emfClass.getURI());
			for (IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter> fa : emfClass.getStructuralFeatureAssociations()) {
				System.out.println("    > " + fa);
			}
		}
	}

	@Test
	@TestOrder(4)
	public void testUMLMetaModel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		// EMFMetaModelResource eCoreRes = technologicalAdapter.getEMFMetaModelRepository(resourceCenter)
		// .getResource("http://www.eclipse.org/emf/2002/Ecore");

		EMFMetaModelResource eCoreRes = technologicalAdapter.getTechnologyContextManager()
				.getMetaModelResourceByURI(EMFTechnologyAdapter.UML_MM_URI);
		assertNotNull(eCoreRes);

		EMFMetaModel eCoreMM = eCoreRes.loadResourceData(null);
		assertNotNull(eCoreMM);

		for (IFlexoOntologyClass<EMFTechnologyAdapter> emfClass : eCoreMM.getClasses()) {
			System.out.println("* " + emfClass + " uri=" + emfClass.getURI());
			for (IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter> fa : emfClass.getStructuralFeatureAssociations()) {
				System.out.println("    > " + fa);
			}
		}
	}

}
