package org.openflexo.technologyadapter.owl.gui.widget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.openflexo.technologyadapter.owl.gui.FIBOWLOntologyBrowser;
import org.openflexo.technologyadapter.owl.gui.OWLOntologyBrowserModel;
import org.openflexo.technologyadapter.owl.model.OWL2URIDefinitions;
import org.openflexo.technologyadapter.owl.model.OWLClass;
import org.openflexo.technologyadapter.owl.model.OWLDataProperty;
import org.openflexo.technologyadapter.owl.model.OWLIndividual;
import org.openflexo.technologyadapter.owl.model.OWLObjectProperty;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OWLOntologyLibrary;
import org.openflexo.technologyadapter.owl.model.RDFSURIDefinitions;
import org.openflexo.technologyadapter.owl.model.RDFURIDefinitions;
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
public class TestFIBOWLOntologyBrowserOnSKOSOntology extends OpenflexoTestCaseWithGUI {

	private static GraphicalContextDelegate gcDelegate;

	private static OWLOntologyResource ontologyResource;
	private static FIBOWLOntologyBrowser browser;
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
		assertNotNull(concept = skosOntology.getClass(skosOntology.getURI() + "#" + "Concept"));
		assertNotNull(conceptScheme = skosOntology.getClass(skosOntology.getURI() + "#" + "ConceptScheme"));
		assertNotNull(list = skosOntology.getClass(RDFURIDefinitions.RDF_ONTOLOGY_URI + "#" + "List"));

		assertNotNull(altLabel = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "altLabel"));
		assertNotNull(hiddenLabel = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "hiddenLabel"));
		assertNotNull(prefLabel = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "prefLabel"));
		assertNotNull(hasTopConcept = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "hasTopConcept"));
		assertNotNull(inScheme = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "inScheme"));
		assertNotNull(topConceptOf = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "topConceptOf"));
		assertNotNull(member = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "member"));
		assertNotNull(memberList = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "memberList"));

		assertNotNull(note = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "note"));
		assertNotNull(changeNote = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "changeNote"));
		assertNotNull(definition = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "definition"));
		assertNotNull(editorialNote = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "editorialNote"));
		assertNotNull(example = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "example"));
		assertNotNull(historyNote = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "historyNote"));
		assertNotNull(scopeNote = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "scopeNote"));

		assertNotNull(semanticRelation = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "semanticRelation"));
		assertNotNull(broaderTransitive = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "broaderTransitive"));
		assertNotNull(broader = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "broader"));
		assertNotNull(broadMatch = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "broadMatch"));
		assertNotNull(mappingRelation = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "mappingRelation"));
		assertNotNull(closeMatch = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "closeMatch"));
		assertNotNull(exactMatch = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "exactMatch"));
		assertNotNull(narrowMatch = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "narrowMatch"));
		assertNotNull(relatedMatch = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "relatedMatch"));
		assertNotNull(narrowerTransitive = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "narrowerTransitive"));
		assertNotNull(narrower = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "narrower"));
		assertNotNull(related = skosOntology.getObjectProperty(skosOntology.getURI() + "#" + "related"));

		assertNotNull(label = owlOntology.getDataProperty(RDFSURIDefinitions.RDFS_ONTOLOGY_URI + "#" + "label"));
		assertNotNull(notation = skosOntology.getDataProperty(skosOntology.getURI() + "#" + "notation"));

		assertNotNull(coreIndividual = skosOntology.getIndividual(skosOntology.getURI()));

		assertNotNull(resource = owlOntology.getClass(RDFSURIDefinitions.RDFS_ONTOLOGY_URI + "#" + "Resource"));
		assertNotNull(namedIndividual = owlOntology.getClass(OWL2URIDefinitions.OWL_ONTOLOGY_URI + "#" + "NamedIndividual"));
		assertNotNull(nothing = owlOntology.getClass(OWL2URIDefinitions.OWL_ONTOLOGY_URI + "#" + "Nothing"));

		assertNotNull(topObjectProperty = owlOntology.getObjectProperty(OWL2URIDefinitions.OWL_ONTOLOGY_URI + "#" + "topObjectProperty"));
		assertNotNull(bottomObjectProperty = owlOntology.getObjectProperty(OWL2URIDefinitions.OWL_ONTOLOGY_URI + "#"
				+ "bottomObjectProperty"));
		assertNotNull(bottomDataProperty = owlOntology.getDataProperty(OWL2URIDefinitions.OWL_ONTOLOGY_URI + "#" + "bottomDataProperty"));
		assertNotNull(topDataProperty = owlOntology.getDataProperty(OWL2URIDefinitions.OWL_ONTOLOGY_URI + "#" + "topDataProperty"));

		assertNotNull(differentFrom = owlOntology.getObjectProperty(OWL2URIDefinitions.OWL_ONTOLOGY_URI + "#" + "differentFrom"));
		assertNotNull(sameAs = owlOntology.getObjectProperty(OWL2URIDefinitions.OWL_ONTOLOGY_URI + "#" + "sameAs"));

	}

	private static OWLOntology skosOntology, owlOntology;
	private static OWLClass thing, collection, concept, conceptScheme, list;
	private static OWLObjectProperty altLabel, hiddenLabel, prefLabel, hasTopConcept, inScheme, topConceptOf, member, memberList;
	private static OWLObjectProperty note, changeNote, definition, editorialNote, example, historyNote, scopeNote;
	private static OWLObjectProperty semanticRelation, broaderTransitive, broader, broadMatch, mappingRelation, closeMatch, exactMatch,
			narrowMatch, relatedMatch, narrowerTransitive, narrower, related;
	private static OWLDataProperty label, notation;
	private static OWLIndividual coreIndividual;
	private static OWLClass resource, namedIndividual, nothing;
	private static OWLObjectProperty topObjectProperty, bottomObjectProperty;
	private static OWLDataProperty bottomDataProperty, topDataProperty;
	private static OWLObjectProperty differentFrom, sameAs;

	@Test
	@TestOrder(2)
	public void test2InstanciateWidget() {

		browser = new FIBOWLOntologyBrowser(ontologyResource.getLoadedResourceData());

		gcDelegate.addTab("FIBOntologyBrowser", browser.getController());
	}

	@Test
	@TestOrder(3)
	public void checkInitialStructure() {

		OWLOntologyBrowserModel obm = browser.getModel();

		assertEquals(4, obm.getRoots().size());
		assertEquals(obm.getRoots().get(0), thing);
		assertEquals(obm.getRoots().get(1), inScheme);
		assertEquals(obm.getRoots().get(2), note);
		assertEquals(obm.getRoots().get(3), notation);

		assertEquals(8, obm.getChildren(thing).size());
		assertSameList(obm.getChildren(thing), collection, concept, conceptScheme, list, hiddenLabel, altLabel, prefLabel, coreIndividual);

		assertEquals(6, obm.getChildren(note).size());
		assertSameList(obm.getChildren(note), changeNote, definition, editorialNote, example, historyNote, scopeNote);

		assertEquals(1, obm.getChildren(conceptScheme).size());
		assertSameList(obm.getChildren(conceptScheme), hasTopConcept);

		assertEquals(2, obm.getChildren(concept).size());
		assertSameList(obm.getChildren(concept), topConceptOf, semanticRelation);

		assertEquals(4, obm.getChildren(semanticRelation).size());
		assertSameList(obm.getChildren(semanticRelation), broaderTransitive, mappingRelation, narrowerTransitive, related);

		assertEquals(1, obm.getChildren(broaderTransitive).size());
		assertSameList(obm.getChildren(broaderTransitive), broader);

		assertEquals(1, obm.getChildren(broader).size());
		assertSameList(obm.getChildren(broader), broadMatch);

		assertEquals(4, obm.getChildren(mappingRelation).size());
		assertSameList(obm.getChildren(mappingRelation), broadMatch, closeMatch, narrowMatch, relatedMatch);

		assertEquals(1, obm.getChildren(closeMatch).size());
		assertSameList(obm.getChildren(closeMatch), exactMatch);

		assertEquals(1, obm.getChildren(narrowerTransitive).size());
		assertSameList(obm.getChildren(narrowerTransitive), narrower);

		assertEquals(1, obm.getChildren(narrower).size());
		assertSameList(obm.getChildren(narrower), narrowMatch);

		assertEquals(1, obm.getChildren(related).size());
		assertSameList(obm.getChildren(related), relatedMatch);

	}

	@Test
	@TestOrder(4)
	public void showOWLRDFConceptAndCheckStructure() {

		browser.setShowOWLAndRDFConcepts(true);

		OWLOntologyBrowserModel obm = browser.getModel();

		assertEquals(4, obm.getRoots().size());
		assertEquals(obm.getRoots().get(0), thing);
		assertEquals(obm.getRoots().get(1), inScheme);
		assertEquals(obm.getRoots().get(2), note);
		assertEquals(obm.getRoots().get(3), notation);

		assertEquals(8, obm.getChildren(thing).size());
		assertSameList(obm.getChildren(thing), resource, coreIndividual, bottomObjectProperty, differentFrom, topObjectProperty, sameAs,
				bottomDataProperty, topDataProperty);

		assertEquals(26, obm.getChildren(resource).size());
		assertTrue(obm.getChildren(resource).contains(collection));
		assertTrue(obm.getChildren(resource).contains(concept));
		assertTrue(obm.getChildren(resource).contains(conceptScheme));
		assertTrue(obm.getChildren(resource).contains(namedIndividual));
		assertTrue(obm.getChildren(resource).contains(nothing));

		assertEquals(6, obm.getChildren(note).size());
		assertSameList(obm.getChildren(note), changeNote, definition, editorialNote, example, historyNote, scopeNote);

		assertEquals(1, obm.getChildren(conceptScheme).size());
		assertSameList(obm.getChildren(conceptScheme), hasTopConcept);

		assertEquals(2, obm.getChildren(concept).size());
		assertSameList(obm.getChildren(concept), topConceptOf, semanticRelation);

		assertEquals(4, obm.getChildren(semanticRelation).size());
		assertSameList(obm.getChildren(semanticRelation), broaderTransitive, mappingRelation, narrowerTransitive, related);

		assertEquals(1, obm.getChildren(broaderTransitive).size());
		assertSameList(obm.getChildren(broaderTransitive), broader);

		assertEquals(1, obm.getChildren(broader).size());
		assertSameList(obm.getChildren(broader), broadMatch);

		assertEquals(4, obm.getChildren(mappingRelation).size());
		assertSameList(obm.getChildren(mappingRelation), broadMatch, closeMatch, narrowMatch, relatedMatch);

		assertEquals(1, obm.getChildren(closeMatch).size());
		assertSameList(obm.getChildren(closeMatch), exactMatch);

		assertEquals(1, obm.getChildren(narrowerTransitive).size());
		assertSameList(obm.getChildren(narrowerTransitive), narrower);

		assertEquals(1, obm.getChildren(narrower).size());
		assertSameList(obm.getChildren(narrower), narrowMatch);

		assertEquals(1, obm.getChildren(related).size());
		assertSameList(obm.getChildren(related), relatedMatch);

	}

	@Test
	@TestOrder(5)
	public void setStrictModeAndCheckStructure() {

		browser.setShowOWLAndRDFConcepts(false);
		browser.setStrictMode(true);

		OWLOntologyBrowserModel obm = browser.getModel();

		assertEquals(4, obm.getRoots().size());
		assertSameList(obm.getRoots(), thing, inScheme, note, notation);

		assertEquals(8, obm.getChildren(thing).size());
		assertSameList(obm.getChildren(thing), collection, concept, conceptScheme, list, hiddenLabel, altLabel, prefLabel, coreIndividual);

		assertEquals(6, obm.getChildren(note).size());
		assertSameList(obm.getChildren(note), changeNote, definition, editorialNote, example, historyNote, scopeNote);

		assertEquals(1, obm.getChildren(conceptScheme).size());
		assertSameList(obm.getChildren(conceptScheme), hasTopConcept);

		assertEquals(2, obm.getChildren(concept).size());
		assertSameList(obm.getChildren(concept), topConceptOf, semanticRelation);

		assertEquals(4, obm.getChildren(semanticRelation).size());
		assertSameList(obm.getChildren(semanticRelation), broaderTransitive, mappingRelation, narrowerTransitive, related);

		assertEquals(1, obm.getChildren(broaderTransitive).size());
		assertSameList(obm.getChildren(broaderTransitive), broader);

		assertEquals(1, obm.getChildren(broader).size());
		assertSameList(obm.getChildren(broader), broadMatch);

		assertEquals(4, obm.getChildren(mappingRelation).size());
		assertSameList(obm.getChildren(mappingRelation), broadMatch, closeMatch, narrowMatch, relatedMatch);

		assertEquals(1, obm.getChildren(closeMatch).size());
		assertSameList(obm.getChildren(closeMatch), exactMatch);

		assertEquals(1, obm.getChildren(narrowerTransitive).size());
		assertSameList(obm.getChildren(narrowerTransitive), narrower);

		assertEquals(1, obm.getChildren(narrower).size());
		assertSameList(obm.getChildren(narrower), narrowMatch);

		assertEquals(1, obm.getChildren(related).size());
		assertSameList(obm.getChildren(related), relatedMatch);

	}

	@Test
	@TestOrder(6)
	public void hideObjectPropertiesAndCheckStructure() {

		browser.setShowObjectProperties(false);

		OWLOntologyBrowserModel obm = browser.getModel();

		assertEquals(3, obm.getRoots().size());
		assertSameList(obm.getRoots(), thing, note, notation);

		assertEquals(8, obm.getChildren(thing).size());
		assertSameList(obm.getChildren(thing), collection, concept, conceptScheme, list, hiddenLabel, altLabel, prefLabel, coreIndividual);

		assertEquals(6, obm.getChildren(note).size());
		assertSameList(obm.getChildren(note), changeNote, definition, editorialNote, example, historyNote, scopeNote);

		assertNull(obm.getChildren(conceptScheme));

		assertNull(obm.getChildren(concept));

		browser.setShowObjectProperties(true);
	}

	@Test
	@TestOrder(7)
	public void hideDataPropertiesAndCheckStructure() {

		browser.setShowDataProperties(false);

		OWLOntologyBrowserModel obm = browser.getModel();

		assertEquals(3, obm.getRoots().size());
		assertSameList(obm.getRoots(), thing, inScheme, note);

		assertEquals(8, obm.getChildren(thing).size());
		assertSameList(obm.getChildren(thing), collection, concept, conceptScheme, list, hiddenLabel, altLabel, prefLabel, coreIndividual);

		assertEquals(6, obm.getChildren(note).size());
		assertSameList(obm.getChildren(note), changeNote, definition, editorialNote, example, historyNote, scopeNote);

		assertEquals(1, obm.getChildren(conceptScheme).size());
		assertSameList(obm.getChildren(conceptScheme), hasTopConcept);

		assertEquals(2, obm.getChildren(concept).size());
		assertSameList(obm.getChildren(concept), topConceptOf, semanticRelation);

		assertEquals(4, obm.getChildren(semanticRelation).size());
		assertSameList(obm.getChildren(semanticRelation), broaderTransitive, mappingRelation, narrowerTransitive, related);

		assertEquals(1, obm.getChildren(broaderTransitive).size());
		assertSameList(obm.getChildren(broaderTransitive), broader);

		assertEquals(1, obm.getChildren(broader).size());
		assertSameList(obm.getChildren(broader), broadMatch);

		assertEquals(4, obm.getChildren(mappingRelation).size());
		assertSameList(obm.getChildren(mappingRelation), broadMatch, closeMatch, narrowMatch, relatedMatch);

		assertEquals(1, obm.getChildren(closeMatch).size());
		assertSameList(obm.getChildren(closeMatch), exactMatch);

		assertEquals(1, obm.getChildren(narrowerTransitive).size());
		assertSameList(obm.getChildren(narrowerTransitive), narrower);

		assertEquals(1, obm.getChildren(narrower).size());
		assertSameList(obm.getChildren(narrower), narrowMatch);

		assertEquals(1, obm.getChildren(related).size());
		assertSameList(obm.getChildren(related), relatedMatch);

		browser.setShowDataProperties(true);
	}

	@Test
	@TestOrder(8)
	public void hideAnnotationPropertiesAndCheckStructure() {

		browser.setShowAnnotationProperties(false);
		browser.setShowObjectProperties(false);

		OWLOntologyBrowserModel obm = browser.getModel();

		assertEquals(2, obm.getRoots().size());
		assertSameList(obm.getRoots(), thing, notation);

		assertEquals(8, obm.getChildren(thing).size());
		assertSameList(obm.getChildren(thing), collection, concept, conceptScheme, list, coreIndividual, hiddenLabel, altLabel, prefLabel);

		assertNull(obm.getChildren(conceptScheme));

		assertNull(obm.getChildren(concept));

		browser.setShowAnnotationProperties(true);
		browser.setShowObjectProperties(true);
	}

	@Test
	@TestOrder(9)
	public void doNotStorePropertiesAndCheckStructure() {

		browser.setDisplayPropertiesInClasses(false);

		OWLOntologyBrowserModel obm = browser.getModel();

		assertEquals(11, obm.getRoots().size());
		assertSameList(obm.getRoots(), thing, altLabel, hasTopConcept, hiddenLabel, inScheme, member, memberList, note, prefLabel,
				semanticRelation, notation);

		assertEquals(5, obm.getChildren(thing).size());
		assertSameList(obm.getChildren(thing), collection, concept, conceptScheme, list, coreIndividual);

		assertEquals(6, obm.getChildren(note).size());
		assertSameList(obm.getChildren(note), changeNote, definition, editorialNote, example, historyNote, scopeNote);

		assertNull(obm.getChildren(conceptScheme));

		assertNull(obm.getChildren(concept));

		assertEquals(4, obm.getChildren(semanticRelation).size());
		assertSameList(obm.getChildren(semanticRelation), broaderTransitive, mappingRelation, narrowerTransitive, related);

		assertEquals(1, obm.getChildren(broaderTransitive).size());
		assertSameList(obm.getChildren(broaderTransitive), broader);

		assertEquals(1, obm.getChildren(broader).size());
		assertSameList(obm.getChildren(broader), broadMatch);

		assertEquals(4, obm.getChildren(mappingRelation).size());
		assertSameList(obm.getChildren(mappingRelation), broadMatch, closeMatch, narrowMatch, relatedMatch);

		assertEquals(1, obm.getChildren(closeMatch).size());
		assertSameList(obm.getChildren(closeMatch), exactMatch);

		assertEquals(1, obm.getChildren(narrowerTransitive).size());
		assertSameList(obm.getChildren(narrowerTransitive), narrower);

		assertEquals(1, obm.getChildren(narrower).size());
		assertSameList(obm.getChildren(narrower), narrowMatch);

		assertEquals(1, obm.getChildren(related).size());
		assertSameList(obm.getChildren(related), relatedMatch);

		assertEquals(1, obm.getChildren(inScheme).size());
		assertSameList(obm.getChildren(inScheme), topConceptOf);

		browser.setDisplayPropertiesInClasses(true);

	}

	@Test
	@TestOrder(10)
	public void setNonHierarchicAndCheckStructure() {

		browser.setStrictMode(false);
		browser.setHierarchicalMode(false);

		OWLOntologyBrowserModel obm = browser.getModel();

		assertEquals(1, obm.getRoots().size());
		assertSameList(obm.getRoots(), skosOntology);

		assertEquals(4, obm.getChildren(skosOntology).size());
		assertSameList(obm.getChildren(skosOntology), thing, inScheme, note, notation);

		assertEquals(8, obm.getChildren(thing).size());
		assertSameList(obm.getChildren(thing), collection, concept, conceptScheme, list, hiddenLabel, altLabel, prefLabel, coreIndividual);

		assertEquals(6, obm.getChildren(note).size());
		assertSameList(obm.getChildren(note), changeNote, definition, editorialNote, example, historyNote, scopeNote);

		assertEquals(1, obm.getChildren(conceptScheme).size());
		assertSameList(obm.getChildren(conceptScheme), hasTopConcept);

		assertEquals(2, obm.getChildren(concept).size());
		assertSameList(obm.getChildren(concept), topConceptOf, semanticRelation);

		assertEquals(4, obm.getChildren(semanticRelation).size());
		assertSameList(obm.getChildren(semanticRelation), broaderTransitive, mappingRelation, narrowerTransitive, related);

		assertEquals(1, obm.getChildren(broaderTransitive).size());
		assertSameList(obm.getChildren(broaderTransitive), broader);

		assertEquals(1, obm.getChildren(broader).size());
		assertSameList(obm.getChildren(broader), broadMatch);

		assertEquals(4, obm.getChildren(mappingRelation).size());
		assertSameList(obm.getChildren(mappingRelation), broadMatch, closeMatch, narrowMatch, relatedMatch);

		assertEquals(1, obm.getChildren(closeMatch).size());
		assertSameList(obm.getChildren(closeMatch), exactMatch);

		assertEquals(1, obm.getChildren(narrowerTransitive).size());
		assertSameList(obm.getChildren(narrowerTransitive), narrower);

		assertEquals(1, obm.getChildren(narrower).size());
		assertSameList(obm.getChildren(narrower), narrowMatch);

		assertEquals(1, obm.getChildren(related).size());
		assertSameList(obm.getChildren(related), relatedMatch);

	}

	public static void initGUI() {
		gcDelegate = new GraphicalContextDelegate(TestFIBOWLOntologyBrowserOnSKOSOntology.class.getSimpleName());
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