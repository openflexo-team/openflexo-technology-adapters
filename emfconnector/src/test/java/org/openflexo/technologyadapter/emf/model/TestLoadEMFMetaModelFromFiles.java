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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyFeatureAssociation;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.metamodel.EMFClassClass;
import org.openflexo.technologyadapter.emf.metamodel.EMFEnumClass;
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
public class TestLoadEMFMetaModelFromFiles extends OpenflexoTestCase {
	protected static final Logger logger = Logger.getLogger(TestLoadEMFMetaModelFromFiles.class.getPackage().getName());

	private static EMFTechnologyAdapter technologicalAdapter;

	private static FlexoResourceCenter<?> emfResourceCenter;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(EMFTechnologyAdapter.class);

		emfResourceCenter = serviceManager.getResourceCenterService().getFlexoResourceCenter("http://openflexo.org/emf-test");
		assertNotNull(emfResourceCenter);

		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(EMFTechnologyAdapter.class);
	}

	@Test
	@TestOrder(2)
	public void testConvertAllEMFMetaModel() {

		// Collection<EMFMetaModelResource> metaModelResources =
		// technologicalAdapter.getTechnologyContextManager().getAllMetaModelResources();

		for (EMFMetaModelResource mmResource : technologicalAdapter.getEMFMetaModelRepository(emfResourceCenter).getAllResources()) {

			System.out.println("\t Loading and Converting " + mmResource.getURI());
			long startTime = System.currentTimeMillis();

			System.out.println("mmResource=" + mmResource);
			/*
			 * try { mmResource.loadResourceData(null); } catch
			 * (FileNotFoundException e) { 
			 * e.printStackTrace(); } catch (ResourceLoadingCancelledException
			 * e) {  e.printStackTrace(); }
			 * catch (FlexoException e) { 
			 * e.printStackTrace(); }
			 */

			EMFMetaModel metamodel = mmResource.getMetaModelData();

			assertNotNull(metamodel);
			assertNull(metamodel.getRootConcept());

			long endTime = System.currentTimeMillis();

			System.out.println("\t\t MetaModel Conversion  took " + (endTime - startTime) + " milliseconds");

		}
	}

	@Test
	@TestOrder(3)
	public void testCity1MetaModel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		EMFMetaModelResource city1MMRes = technologicalAdapter.getEMFMetaModelRepository(emfResourceCenter)
				.getResource("http://www.thalesgroup.com/openflexo/emf/model/city1");
		assertNotNull(city1MMRes);

		EMFMetaModel city1MM = city1MMRes.loadResourceData(null);
		assertNotNull(city1MM);

		for (IFlexoOntologyClass<EMFTechnologyAdapter> emfClass : city1MM.getClasses()) {
			if (!emfClass.getName().contains("E")) {
				System.out.println("* " + emfClass + " uri=" + emfClass.getURI());
				for (IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter> fa : emfClass.getStructuralFeatureAssociations()) {
					System.out.println("    > " + fa);
				}
			}
		}

		EMFClassClass cityClass, houseClass, residentClass;
		EMFEnumClass houseTypeEnum;

		assertNotNull(cityClass = (EMFClassClass) city1MM.getClass("http://www.thalesgroup.com/openflexo/emf/model/city1/City"));
		assertNotNull(houseClass = (EMFClassClass) city1MM.getClass("http://www.thalesgroup.com/openflexo/emf/model/city1/House"));
		assertNotNull(residentClass = (EMFClassClass) city1MM.getClass("http://www.thalesgroup.com/openflexo/emf/model/city1/Resident"));
		assertNotNull(houseTypeEnum = (EMFEnumClass) city1MM.getClass("http://www.thalesgroup.com/openflexo/emf/model/city1/HouseType"));

		assertEquals(4, cityClass.getDeclaredFeatureAssociations().size());
		assertEquals(3, houseClass.getDeclaredFeatureAssociations().size());
		assertEquals(1, residentClass.getDeclaredFeatureAssociations().size());

	}

	@Test
	@TestOrder(4)
	public void testCity2MetaModel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		EMFMetaModelResource city2MMRes = technologicalAdapter.getEMFMetaModelRepository(emfResourceCenter)
				.getResource("http://www.thalesgroup.com/openflexo/emf/model/city2");
		assertNotNull(city2MMRes);

		EMFMetaModel city2MM = city2MMRes.loadResourceData(null);
		assertNotNull(city2MM);

		for (IFlexoOntologyClass<EMFTechnologyAdapter> emfClass : city2MM.getClasses()) {
			if (!emfClass.getName().contains("E")) {
				System.out.println("* " + emfClass + " uri=" + emfClass.getURI());
				for (IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter> fa : emfClass.getStructuralFeatureAssociations()) {
					System.out.println("    > " + fa);
				}
			}
		}

		EMFClassClass cityClass, houseClass, mayorClass, mansionClass, appartmentClass;

		assertNotNull(mayorClass = (EMFClassClass) city2MM.getClass("http://www.thalesgroup.com/openflexo/emf/model/city2/Mayor"));
		assertNotNull(houseClass = (EMFClassClass) city2MM.getClass("http://www.thalesgroup.com/openflexo/emf/model/city2/House"));
		assertNotNull(mansionClass = (EMFClassClass) city2MM.getClass("http://www.thalesgroup.com/openflexo/emf/model/city2/Mansion"));
		assertNotNull(
				appartmentClass = (EMFClassClass) city2MM.getClass("http://www.thalesgroup.com/openflexo/emf/model/city2/Appartment"));
		assertNotNull(cityClass = (EMFClassClass) city2MM.getClass("http://www.thalesgroup.com/openflexo/emf/model/city2/City"));

		assertEquals(1, mayorClass.getDeclaredFeatureAssociations().size());
		assertEquals(1, houseClass.getDeclaredFeatureAssociations().size());
		assertEquals(1, mansionClass.getDeclaredFeatureAssociations().size());
		assertEquals(1, appartmentClass.getDeclaredFeatureAssociations().size());
		assertEquals(3, cityClass.getDeclaredFeatureAssociations().size());

	}

	@Test
	@TestOrder(6)
	public void testArchiMetaModel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		EMFMetaModelResource archiMMRes = technologicalAdapter.getEMFMetaModelRepository(emfResourceCenter)
				.getResource("http://www.bolton.ac.uk/archimate");
		assertNotNull(archiMMRes);

		EMFMetaModel archiMM = archiMMRes.loadResourceData(null);
		assertNotNull(archiMM);

		for (IFlexoOntologyClass<EMFTechnologyAdapter> emfClass : archiMM.getClasses()) {
			System.out.println("* " + emfClass + " uri=" + emfClass.getURI());
			for (IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter> fa : emfClass.getStructuralFeatureAssociations()) {
				System.out.println("    > " + fa);
			}
		}
	}

	@Test
	@TestOrder(7)
	public void testParametersMetaModel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		EMFMetaModelResource parametersMMRes = technologicalAdapter.getEMFMetaModelRepository(emfResourceCenter)
				.getResource("http://www.thalesgroup.com/parameters/1.0");
		assertNotNull(parametersMMRes);

		EMFMetaModel parametersMM = parametersMMRes.loadResourceData(null);
		assertNotNull(parametersMM);

		for (IFlexoOntologyClass<EMFTechnologyAdapter> emfClass : parametersMM.getClasses()) {
			System.out.println("* " + emfClass + " uri=" + emfClass.getURI());
			for (IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter> fa : emfClass.getStructuralFeatureAssociations()) {
				System.out.println("    > " + fa);
			}
		}
	}

	@Test
	@TestOrder(8)
	public void testSysMLMetaModel() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		EMFMetaModelResource sysMLMMRes = technologicalAdapter.getEMFMetaModelRepository(emfResourceCenter)
				.getResource("http://www.eclipse.org/papyrus/0.7.0/SysML");
		assertNotNull(sysMLMMRes);

		EMFMetaModel sysMLMM = sysMLMMRes.loadResourceData(null);
		assertNotNull(sysMLMM);

		for (IFlexoOntologyClass<EMFTechnologyAdapter> emfClass : sysMLMM.getClasses()) {
			System.out.println("* " + emfClass + " uri=" + emfClass.getURI());
			for (IFlexoOntologyFeatureAssociation<EMFTechnologyAdapter> fa : emfClass.getStructuralFeatureAssociations()) {
				System.out.println("    > " + fa);
			}
		}
	}

}
