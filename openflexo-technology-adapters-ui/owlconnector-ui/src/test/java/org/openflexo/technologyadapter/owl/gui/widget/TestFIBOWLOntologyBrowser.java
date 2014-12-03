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
import org.openflexo.OpenflexoTestCaseWithGUI;
import org.openflexo.fib.testutils.GraphicalContextDelegate;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.ResourceRepository;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.owl.OWLTechnologyAdapter;
import org.openflexo.technologyadapter.owl.gui.FIBOWLOntologyBrowser;
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
public class TestFIBOWLOntologyBrowser extends OpenflexoTestCaseWithGUI {

	private static GraphicalContextDelegate gcDelegate;

	private static OWLOntologyResource ontologyResource;

	@BeforeClass
	public static void setupClass() {
		Resource rsc = ResourceLocator.locateResource("/org.openflexo.owlconnector/TestResourceCenter");
		instanciateTestServiceManager(true);
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

		assertNotNull(ontologyResource.getLoadedResourceData());
	}

	@Test
	@TestOrder(2)
	public void test2InstanciateWidget() {

		FIBOWLOntologyBrowser browser = new FIBOWLOntologyBrowser(ontologyResource.getLoadedResourceData());

		gcDelegate.addTab("FIBOntologyBrowser", browser.getController());
	}

	public static void initGUI() {
		gcDelegate = new GraphicalContextDelegate(TestFIBOWLOntologyBrowser.class.getSimpleName());
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
