/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Owlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.owl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.ResourceRepository;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLOntologyLibrary;
import org.openflexo.technologyadapter.owl.rm.OWLOntologyResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the structural and behavioural features of FIBOWLOntologyBrowser
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestSepelOntology extends OpenflexoTestCase {

	private static OWLTechnologyAdapter owlAdapter;
	private static OWLOntologyLibrary ontologyLibrary;
	private static ResourceRepository<OWLOntologyResource> ontologyRepository;

	@BeforeClass
	public static void setupClass() {
		Resource rsc = ResourceLocator.locateResource("/org.openflexo.owlconnector/TestResourceCenter");
		instanciateTestServiceManager(true, OWLTechnologyAdapter.class);
		owlAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(OWLTechnologyAdapter.class);
		ontologyLibrary = (OWLOntologyLibrary) serviceManager.getTechnologyAdapterService().getTechnologyContextManager(owlAdapter);
		List<ResourceRepository<?>> owlRepositories = serviceManager.getResourceManager().getAllRepositories(owlAdapter);
		ontologyRepository = (ResourceRepository<OWLOntologyResource>) owlRepositories.get(0);
		assertNotNull(ontologyRepository);
	}

	private static OWLOntology mappingSpecification;
	private static OWLOntology inputModel1;

	@Test
	@TestOrder(1)
	public void instanciateOntologies() {

		OWLOntologyResource mappingSpecificationResource = ontologyRepository
				.getResource("http://www.thalesgroup.com/ViewPoints/sepel-ng/MappingSpecification.owl");
		try {
			mappingSpecificationResource.loadResourceData(null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (FlexoException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		assertNotNull(mappingSpecification = mappingSpecificationResource.getLoadedResourceData());

		OWLOntologyResource inputModel1Resource = ontologyRepository
				.getResource("http://www.thalesgroup.com/ontologies/sepel-ng/SEPELInputModel1.owl");
		try {
			inputModel1Resource.loadResourceData(null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (FlexoException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		assertNotNull(inputModel1 = inputModel1Resource.getLoadedResourceData());

		assertTrue(mappingSpecification.getImportedOntologies().contains(inputModel1));

	}

	@Test
	@TestOrder(1)
	public void testRedefinition() {

		OWLClass rootClassForInputModel1InMS = mappingSpecification
				.getClass("http://www.thalesgroup.com/ontologies/sepel-ng/SEPELInputModel1.owl#RootClassForInputModel1");
		System.out.println("rootClassForInputModel1InMS=" + rootClassForInputModel1InMS);
		assertNotNull(rootClassForInputModel1InMS);

		OWLClass rootClassForInputModel1InIM1 = inputModel1
				.getClass("http://www.thalesgroup.com/ontologies/sepel-ng/SEPELInputModel1.owl#RootClassForInputModel1");
		System.out.println("rootClassForInputModel1InIM1=" + rootClassForInputModel1InIM1);
		assertNotNull(rootClassForInputModel1InIM1);

		assertSame(rootClassForInputModel1InMS.getOriginalDefinition(), rootClassForInputModel1InIM1);

		System.out.println("super classes for rootClassForInputModel1InIM1: " + rootClassForInputModel1InIM1.getSuperClasses());
		System.out.println("super classes for rootClassForInputModel1InMS: " + rootClassForInputModel1InMS.getSuperClasses());

		OWLClass etatInMS = mappingSpecification.getClass("http://www.thalesgroup.com/ontologies/sepel-ng/SEPELInputModel1.owl#Etat");
		System.out.println("etatInMS=" + etatInMS);
		assertNotNull(etatInMS);

		OWLClass etatInIM1 = inputModel1.getClass("http://www.thalesgroup.com/ontologies/sepel-ng/SEPELInputModel1.owl#Etat");
		System.out.println("etatInIM1=" + etatInIM1);
		assertNotNull(etatInIM1);

		assertSame(etatInMS.getOriginalDefinition(), etatInIM1);

		System.out.println("super classes of etatInMS = " + etatInMS.getSuperClasses());
		System.out.println("super classes of etatInIM1 = " + etatInIM1.getSuperClasses());

		assertEquals(2, etatInIM1.getSuperClasses().size());
		assertTrue(etatInIM1.getSuperClasses().contains(rootClassForInputModel1InIM1));
		assertTrue(etatInIM1.getSuperClasses().contains(inputModel1.getRootConcept()));

		assertEquals(2, etatInMS.getSuperClasses().size());
		assertTrue(etatInMS.getSuperClasses().contains(rootClassForInputModel1InMS));
		assertTrue(etatInMS.getSuperClasses().contains(mappingSpecification.getRootConcept()));

		OWLClass inputModelObject = mappingSpecification
				.getClass("http://www.thalesgroup.com/ontologies/sepel-ng/MappingSpecifications.owl#InputModelObject");
		System.out.println("inputModelObject=" + inputModelObject);
		assertNotNull(inputModelObject);

		boolean resultInIM1 = inputModelObject.isSuperConceptOf(etatInIM1);
		boolean resultInMS = inputModelObject.isSuperConceptOf(etatInMS);

		System.out.println("inputModelObject.isSuperConceptOf(etatInIM1)=" + resultInIM1);
		System.out.println("inputModelObject.isSuperConceptOf(etatInMS)=" + resultInMS);

		assertFalse(inputModelObject.isSuperConceptOf(etatInIM1));
		assertTrue(inputModelObject.isSuperConceptOf(etatInMS));

		assertEquals(38, mappingSpecification.getClasses().size());
		System.out.println("MS: classes=" + mappingSpecification.getClasses().size());
		for (OWLClass c : mappingSpecification.getClasses()) {
			System.out.println("> " + c);
		}

		assertEquals(15, inputModel1.getClasses().size());
		System.out.println("IM1: classes=" + inputModel1.getClasses().size());
		for (OWLClass c : inputModel1.getClasses()) {
			System.out.println("> " + c);
		}

		OWLObjectProperty directionInIM1 = inputModel1
				.getObjectProperty("http://www.thalesgroup.com/ontologies/sepel-ng/SEPELInputModel1.owl#direction");
		System.out.println("directionInIM1=" + directionInIM1);
		assertNotNull(directionInIM1);

		OWLObjectProperty directionInMS = mappingSpecification
				.getObjectProperty("http://www.thalesgroup.com/ontologies/sepel-ng/SEPELInputModel1.owl#direction");
		System.out.println("directionInMS=" + directionInMS);
		assertNotNull(directionInMS);

		assertNotSame(directionInIM1, directionInMS);

		assertSame(directionInMS.getOriginalDefinition(), directionInIM1);
	}
}
