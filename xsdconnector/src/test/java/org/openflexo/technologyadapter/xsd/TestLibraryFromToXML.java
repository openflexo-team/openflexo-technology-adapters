package org.openflexo.technologyadapter.xsd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.ApplicationContext;
import org.openflexo.TestApplicationContext;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.technologyadapter.xsd.metamodel.XSDMetaModel;
import org.openflexo.technologyadapter.xsd.metamodel.XSOntProperty;
import org.openflexo.technologyadapter.xsd.model.XMLXSDModel;
import org.openflexo.technologyadapter.xsd.model.XSOntIndividual;
import org.openflexo.technologyadapter.xsd.model.XSPropertyValue;
import org.openflexo.technologyadapter.xsd.rm.XMLXSDFileResource;
import org.openflexo.technologyadapter.xsd.rm.XMLXSDModelRepository;
import org.openflexo.technologyadapter.xsd.rm.XSDMetaModelRepository;
import org.openflexo.technologyadapter.xsd.rm.XSDMetaModelResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.xml.IXMLIndividual;

@RunWith(OrderedRunner.class)
public class TestLibraryFromToXML extends OpenflexoProjectAtRunTimeTestCase {

    private static final java.util.logging.Logger logger         = org.openflexo.logging.FlexoLogger.getLogger(TestLibraryFromToXML.class
                                                                         .getPackage().getName());

    private static final String                   LIBRARY_URI    = "http://www.example.org/Library#Library";
    private static final String                   BOOK_URI       = "http://www.example.org/Library#Book";
    private static final String                   BOOK_TITLE_URI = "http://www.example.org/Library/Book#title";
    private static final String                   LIB_NAME_URI   = "http://www.example.org/Library/LibraryType#name";
    private static final String                   LIB_BOOKS_URI  = "http://www.example.org/Library/LibraryType#books";

    private static ApplicationContext             testApplicationContext;
    private static XSDTechnologyAdapter           xsdAdapter;
    private static XSDMetaModelRepository         mmRepository;
    private static XMLXSDModelRepository          modelRepository;
    private static String                         baseUrl;

    private static final void dumpIndividual(IXMLIndividual<XSOntIndividual, XSOntProperty> indiv, String prefix) {

        System.out.println(prefix + "Indiv : " + indiv.getName() + "  ==> " + indiv.getUUID());

        for (XSOntProperty x : indiv.getAttributes()) {
            XSPropertyValue pv = ((XSOntIndividual) indiv).getPropertyValue(x);
            List<? extends Object> values = null;

            if (pv != null) {
                values = pv.getValues();
            }

            if (x.isSimpleAttribute()) {
                System.out.print(prefix + "   Data attr : " + x.getName());
                if (values != null) {
                    System.out.println("  =  " + values.toString());
                }
                else {
                    System.out.println("");
                }
            }
            else {
                System.out.println(prefix + "   Object attr : " + x.getName());
                if (values != null) {
                    for (Object o : values) {
                        XSOntIndividual child = (XSOntIndividual) o;
                        dumpIndividual(child, prefix + "      ");
                    }
                }

            }
        }

        System.out.println(prefix + "--- Dumping Children");
        for (IXMLIndividual<XSOntIndividual, XSOntProperty> x : indiv.getChildren()) {
            if (x != indiv) {
                dumpIndividual(x, prefix + "     ");
            }
            else {
                logger.info("NON MAIS NON!!!! CELA NE DOIT PAS ARRIVER");
            }
        }

        System.out.println("");
        System.out.flush();
    }

    /**
     * Instanciate test ResourceCenter
     * 
     * @throws IOException
     */
    @Test
    @TestOrder(1)
    public void test0LoadTestResourceCenter() throws IOException {
        log("test0LoadTestResourceCenter()");
        testApplicationContext = new TestApplicationContext();
        resourceCenter = (DirectoryResourceCenter) testApplicationContext.getResourceCenterService().getResourceCenters().get(0);
        xsdAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(XSDTechnologyAdapter.class);
        mmRepository = (XSDMetaModelRepository) resourceCenter.getRepository(XSDMetaModelRepository.class, xsdAdapter);
        modelRepository = (XMLXSDModelRepository) resourceCenter.getRepository(XMLXSDModelRepository.class, xsdAdapter);
        baseUrl = ((DirectoryResourceCenter) resourceCenter).getDirectory().getCanonicalPath();
        try {
            baseUrl = ((DirectoryResourceCenter) resourceCenter).getDirectory().toURI().toURL().toExternalForm();
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

        assertNotNull(mmRepository);
        assertNotNull(modelRepository);

        // baseUrl = baseUrl.replace('\\', '/');

        System.out.println("BaseDir: " + baseUrl);

        String resourceURI = baseUrl + "TestResourceCenter/XML/example_library_1.xml";
        System.out.println("ResourceURI: " + resourceURI);

        XSDMetaModelResource mmLibraryRes = mmRepository.getResource("http://www.example.org/Library");

        assertNotNull(mmLibraryRes);

        XMLXSDFileResource libraryRes = modelRepository.getResource(resourceURI);

        assertNotNull(libraryRes);

        XMLXSDModel mLib = libraryRes.getModel();

        libraryRes.setMetaModelResource(mmLibraryRes);
        mmLibraryRes.loadResourceData(null);
        libraryRes.loadResourceData(null);

        assertNotNull(mLib);
        assertTrue(mLib.getResource().isLoaded());

        dumpIndividual(mLib.getRoot(), "---");

    }

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
        XSDMetaModel mmLib = mmLibraryRes.getMetaModelData();

        assertNotNull(mmLib);
        assertTrue(mmLib.getResource().isLoaded());

        if (mmLib.getResource().isLoaded() == false) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Failed to load.");
            }
        }
        else {

            XMLXSDFileResource libRes = (XMLXSDFileResource) xsdAdapter.createNewXMLFile((FileSystemBasedResourceCenter) resourceCenter,
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
}
