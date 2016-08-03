/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.owl.gui.widget;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.ResourceRepository;
import org.openflexo.gina.test.OpenflexoTestCaseWithGUI;
import org.openflexo.gina.test.SwingGraphicalContextDelegate;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.technologyadapter.owl.gui.FIBOWLOntologyEditor;
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
public class TestFIBOWLOntologyEditor extends OpenflexoTestCaseWithGUI {

	private static SwingGraphicalContextDelegate gcDelegate;

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

		initGUI();
	}

	@Test
	@TestOrder(1)
	public void instanciateWidgetOnRDFOntology() {

		OWLOntology rdfOntology = ontologyLibrary.getRDFOntology();

		FIBOWLOntologyEditor editor1 = new FIBOWLOntologyEditor(rdfOntology, null);

		gcDelegate.addTab("RDF", editor1.getFIBController());
	}

	@Test
	@TestOrder(2)
	public void instanciateWidgetOnRDFSOntology() {

		OWLOntology rdfsOntology = ontologyLibrary.getRDFSOntology();

		FIBOWLOntologyEditor editor2 = new FIBOWLOntologyEditor(rdfsOntology, null);

		gcDelegate.addTab("RDFS", editor2.getFIBController());
	}

	@Test
	@TestOrder(3)
	public void instanciateWidgetOnOWLOntology() {

		OWLOntology owlOntology = ontologyLibrary.getOWLOntology();

		FIBOWLOntologyEditor editor3 = new FIBOWLOntologyEditor(owlOntology, null);

		gcDelegate.addTab("OWL", editor3.getFIBController());
	}

	@Test
	@TestOrder(4)
	public void instanciateWidgetOnSKOSOntology() {

		OWLOntologyResource skosResource = ontologyRepository.getResource("http://www.w3.org/2004/02/skos/core");
		try {
			skosResource.loadResourceData(null);
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

		FIBOWLOntologyEditor editor4 = new FIBOWLOntologyEditor(skosResource.getLoadedResourceData(), null);

		gcDelegate.addTab("SKOS", editor4.getFIBController());
	}

	@Test
	@TestOrder(5)
	public void instanciateWidgetOnFlexoConceptOntology() {

		OWLOntologyResource flexoConceptResource = ontologyRepository.getResource(OWLOntologyLibrary.FLEXO_CONCEPT_ONTOLOGY_URI);
		try {
			flexoConceptResource.loadResourceData(null);
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

		FIBOWLOntologyEditor editor5 = new FIBOWLOntologyEditor(flexoConceptResource.getLoadedResourceData(), null);

		gcDelegate.addTab("FlexoConceptOntology", editor5.getFIBController());
	}

	@Test
	@TestOrder(6)
	public void instanciateWidgetOnSEPELOntology() {

		OWLOntologyResource sepelResource = ontologyRepository
				.getResource("http://www.thalesgroup.com/ViewPoints/sepel-ng/MappingSpecification.owl");
		try {
			sepelResource.loadResourceData(null);
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

		FIBOWLOntologyEditor editor6 = new FIBOWLOntologyEditor(sepelResource.getLoadedResourceData(), null);

		gcDelegate.addTab("SEPEL", editor6.getFIBController());
	}

	@Test
	@TestOrder(7)
	public void instanciateWidgetOnO5Ontology() {

		OWLOntologyResource sepelResource = ontologyRepository.getResource("http://www.openflexo.org/test/O5.owl");
		try {
			sepelResource.loadResourceData(null);
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

		FIBOWLOntologyEditor editor7 = new FIBOWLOntologyEditor(sepelResource.getLoadedResourceData(), null);

		gcDelegate.addTab("O5", editor7.getFIBController());
	}

	public static void initGUI() {
		gcDelegate = new SwingGraphicalContextDelegate(TestFIBOWLOntologyEditor.class.getSimpleName());
	}

	@AfterClass
	public static void waitGUI() {
		gcDelegate.waitGUI();
	}

	@Before
	public void setUp() {
		gcDelegate.setUp();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		gcDelegate.tearDown();
	}

}
