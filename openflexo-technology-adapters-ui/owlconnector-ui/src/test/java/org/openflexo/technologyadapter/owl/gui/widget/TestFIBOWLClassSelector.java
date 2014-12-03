package org.openflexo.technologyadapter.owl.gui.widget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.OpenflexoTestCaseWithGUI;
import org.openflexo.fib.testutils.GraphicalContextDelegate;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.ResourceRepository;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.technologyadapter.owl.gui.FIBOWLClassSelector;
import org.openflexo.technologyadapter.owl.gui.OWLOntologyBrowserModel;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLOntologyLibrary;
import org.openflexo.technologyadapter.owl.model.RDFURIDefinitions;
import org.openflexo.technologyadapter.owl.rm.OWLOntologyResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the structural and behavioural features of FIBOWLPropertySelector
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestFIBOWLClassSelector extends OpenflexoTestCaseWithGUI {

	private static GraphicalContextDelegate gcDelegate;

	private static OWLOntologyResource ontologyResource;

	private static FIBOWLClassSelector selector;

	private static OWLOntology skosOntology, owlOntology;
	private static OWLClass thing, collection, orderedCollection, concept, conceptScheme, list;

	private static OWLTechnologyAdapter owlAdapter;
	private static OWLOntologyLibrary ontologyLibrary;

	@BeforeClass
	public static void setupClass() {
		Resource rsc = ResourceLocator.locateResource("/org.openflexo.owlconnector/TestResourceCenter");
		instanciateTestServiceManager(true);
		owlAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(OWLTechnologyAdapter.class);
		ontologyLibrary = (OWLOntologyLibrary) serviceManager.getTechnologyAdapterService().getTechnologyContextManager(owlAdapter);
		initGUI();
	}

	@Test
	@TestOrder(1)
	public void test1RetrieveOntology() {

		OWLTechnologyAdapter owlTA = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(OWLTechnologyAdapter.class);

		assertNotNull(owlTA);

		List<ResourceRepository<?>> owlRepositories = serviceManager.getInformationSpace().getAllRepositories(owlTA);

		ResourceRepository<OWLOntologyResource> ontologyRepository = (ResourceRepository<OWLOntologyResource>) owlRepositories.get(0);

		assertNotNull(ontologyRepository);

		// ontologyResource = ontologyRepository.getResource("http://www.agilebirds.com/openflexo/ViewPoints/BasicOntology.owl");
		ontologyResource = ontologyRepository.getResource("http://www.w3.org/2004/02/skos/core");

		assertNotNull(ontologyResource);

		System.out.println("Found ontology resource " + ontologyResource);

		System.out.println("Try to load ontology resource " + ontologyResource);

		try {
			ontologyResource.loadResourceData(null);
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

		skosOntology = ontologyResource.getLoadedResourceData();
		assertNotNull(skosOntology);

		owlOntology = ontologyLibrary.getOWLOntology();
		assertNotNull(owlOntology);
		assertTrue(skosOntology.getImportedOntologies().contains(owlOntology));

		thing = skosOntology.getRootConcept();
		assertNotNull(thing);
		OWLClass thingInOWL = owlOntology.getRootConcept();
		assertSame(thing.getOriginalDefinition(), thingInOWL);

		assertNotNull(collection = skosOntology.getClass(skosOntology.getURI() + "#" + "Collection"));
		assertNotNull(orderedCollection = skosOntology.getClass(skosOntology.getURI() + "#" + "OrderedCollection"));
		assertNotNull(concept = skosOntology.getClass(skosOntology.getURI() + "#" + "Concept"));
		assertNotNull(conceptScheme = skosOntology.getClass(skosOntology.getURI() + "#" + "ConceptScheme"));
		assertNotNull(list = skosOntology.getClass(RDFURIDefinitions.RDF_ONTOLOGY_URI + "#" + "List"));
	}

	@Test
	@TestOrder(2)
	public void test2InstanciateWidget() {

		selector = new FIBOWLClassSelector(null);
		selector.setContext(ontologyResource.getLoadedResourceData());
		selector.setStrictMode(false);
		selector.getCustomPanel();

		assertNotNull(selector.getSelectorPanel().getController());

		gcDelegate.addTab("FIBOWLClassSelector", selector.getSelectorPanel().getController());
	}

	@Test
	@TestOrder(3)
	public void checkInitialStructure() {

		OWLOntologyBrowserModel obm = selector.getModel();

		assertEquals(1, obm.getRoots().size());
		assertEquals(obm.getRoots().get(0), thing);

		assertEquals(4, obm.getChildren(thing).size());
		assertSameList(obm.getChildren(thing), collection, concept, conceptScheme, list);

	}

	@Test
	@TestOrder(4)
	public void selectRootClass() {

		// selector.setRootClassURI("http://www.w3.org/2004/02/skos/core#Collection");
		selector.setRootClass(collection);

		OWLOntologyBrowserModel obm = selector.getModel();

		assertEquals(1, obm.getRoots().size());
		assertEquals(obm.getRoots().get(0), collection);

		assertEquals(1, obm.getChildren(collection).size());
		assertSameList(obm.getChildren(collection), orderedCollection);
	}

	public static void initGUI() {
		gcDelegate = new GraphicalContextDelegate(TestFIBOWLClassSelector.class.getSimpleName());
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
