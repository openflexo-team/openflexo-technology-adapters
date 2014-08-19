package org.openflexo.technologyadapter.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.xml.model.XMLModel;
import org.openflexo.technologyadapter.xml.rm.XMLFileResource;
import org.openflexo.technologyadapter.xml.rm.XMLModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelRepository;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestLibraryFromToXML extends OpenflexoProjectAtRunTimeTestCase {

	private static final java.util.logging.Logger logger         = org.openflexo.logging.FlexoLogger.getLogger(TestLibraryFromToXML.class
			.getPackage().getName());

	private static final String                   LIBRARY_URI    = "http://www.example.org/Library#Library";
	private static final String                   BOOK_URI       = "http://www.example.org/Library#Book";
	private static final String                   BOOK_TITLE_URI = "http://www.example.org/Library/Book#title";
	private static final String                   LIB_NAME_URI   = "http://www.example.org/Library/LibraryType#name";
	private static final String                   LIB_BOOKS_URI  = "http://www.example.org/Library/LibraryType#books";

	private static XMLTechnologyAdapter           xmlAdapter;
	private static XSDMetaModelRepository         mmRepository;
	private static XMLModelRepository          modelRepository;
	private static String                         baseUrl;

	
	/**
	 * Instanciate test ResourceCenter
	 * 
	 * @throws IOException
	 */
	@Test
	@TestOrder(1)
	public void test0LoadTestResourceCenter() throws IOException {
		instanciateTestServiceManager();

		log("test0LoadTestResourceCenter()");
		xmlAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(XMLTechnologyAdapter.class);
		mmRepository = resourceCenter.getRepository(XSDMetaModelRepository.class, xmlAdapter);
		modelRepository = resourceCenter.getRepository(XMLModelRepository.class, xmlAdapter);
		baseUrl = resourceCenter.getDirectory().getCanonicalPath();
		try {
			baseUrl = resourceCenter.getDirectory().toURI().toURL().toExternalForm();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertNotNull(modelRepository);
		assertNotNull(mmRepository);
		assertEquals(3, mmRepository.getAllResources().size());
		assertTrue(modelRepository.getAllResources().size() > 2);
	}

	@Test
	@TestOrder(2)
	public void test0LibraryFromXML() throws ParserConfigurationException, TransformerException, FileNotFoundException,
	ResourceLoadingCancelledException, FlexoException {

		log("test0LibraryFromXML()");

//		assertNotNull(mmRepository);
		assertNotNull(modelRepository);

		// baseUrl = baseUrl.replace('\\', '/');

		System.out.println("BaseDir: " + baseUrl);

		String resourceURI = baseUrl + "TestResourceCenter/XML/example_library_1.xml";
		System.out.println("ResourceURI: " + resourceURI);


   	    XMLFileResource libraryRes = (XMLFileResource) modelRepository.getResource(resourceURI);

		assertNotNull(libraryRes);

		XMLModel mLib = libraryRes.getModel();

		libraryRes.loadResourceData(null);

		assertNotNull(mLib);
		assertTrue(mLib.getResource().isLoaded());

		Helpers.dumpIndividual(mLib.getRoot(), "---");

	}
/*
	@Test
	@TestOrder(3)
	public void test1LibraryToXML() throws ParserConfigurationException, TransformerException, FileNotFoundException,
	ResourceLoadingCancelledException, FlexoException {

		log("test1LibraryToXML()");

		assertNotNull(mmRepository);
		assertNotNull(modelRepository);

		XSDMetaModelResource mmLibraryRes = mmRepository.getResource("http://www.example.org/Library");

		if (!mmLibraryRes.isLoaded()) {
			mmLibraryRes.loadResourceData(null);
		}
		XMLMetaModel mmLib = mmLibraryRes.getMetaModelData();

		assertNotNull(mmLib);
		assertTrue(mmLib.getResource().isLoaded());

		if (mmLib.getResource().isLoaded() == false) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Failed to load.");
			}
		}
		else {

			XMLXSDFileResource libRes = (XMLXSDFileResource) xmlAdapter.createNewXMLFile(resourceCenter,
					"TestResourceCenter/GenXML", "library.xml", mmLibraryRes);

			XMLXSDModel lib = libRes.getModel();

			// TODO : this wont work anymore as we suppressed name's
			// significance for XSOntIndividual
			XSOntIndividual library = lib.createOntologyIndividual(mmLib.getClass(LIBRARY_URI));
			lib.setRoot(library);
			library.addToPropertyValue(mmLib.getProperty(LIB_NAME_URI), "My Library");
			XSOntIndividual book1 = lib.createOntologyIndividual(mmLib.getClass(BOOK_URI));
			book1.addToPropertyValue(mmLib.getProperty(BOOK_TITLE_URI), "My First Book");
			XSOntIndividual book2 = lib.createOntologyIndividual(mmLib.getClass(BOOK_URI));
			book2.addToPropertyValue(mmLib.getProperty(BOOK_TITLE_URI), "My Second Book");
			library.addToPropertyValue(mmLib.getProperty(LIB_BOOKS_URI), book1);
			library.addToPropertyValue(mmLib.getProperty(LIB_BOOKS_URI), book2);

			try {
				lib.save();
			} catch (SaveResourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	*/
	
}
