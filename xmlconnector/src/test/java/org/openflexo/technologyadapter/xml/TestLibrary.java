package org.openflexo.technologyadapter.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.ApplicationContext;
import org.openflexo.TestApplicationContext;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.technologyadapter.xml.metamodel.XSDMetaModel;
import org.openflexo.technologyadapter.xml.metamodel.XSOntClass;
import org.openflexo.technologyadapter.xml.metamodel.XSOntDataProperty;
import org.openflexo.technologyadapter.xml.metamodel.XSOntObjectProperty;
import org.openflexo.technologyadapter.xml.model.AbstractXSOntObject;
import org.openflexo.technologyadapter.xml.model.XMLTechnologyContextManager;
import org.openflexo.technologyadapter.xml.model.XSOntology;
import org.openflexo.technologyadapter.xml.rm.XMLXSDModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelRepository;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResource;
import org.openflexo.technologyadapter.xml.rm.XSDMetaModelResourceImpl;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestLibrary extends OpenflexoProjectAtRunTimeTestCase {

    private static final String                FILE_NAME = "library";

    protected static final Logger              logger    = Logger.getLogger(TestLibrary.class.getPackage().getName());

    private static XMLTechnologyAdapter        xmlAdapter;
    private static XMLTechnologyContextManager xmlContextManager;
    private static FlexoResourceCenter<?>      resourceCenter;
    private static XSDMetaModelRepository      mmRepository;
    private static XMLXSDModelRepository       modelRepository;
    private static ApplicationContext          testApplicationContext;

    public static void xsoObject(AbstractXSOntObject obj, StringBuffer buffer) {
        buffer.append(obj.getClass().getSimpleName());
        buffer.append(" Name: ").append(obj.getName());
        buffer.append(" URI: ").append(obj.getURI()).append("\n");
    }

    public static void generalInfos(XSOntology lib, StringBuffer buffer) {
        buffer.append("* General infos *\n");
        buffer.append("Name: ").append(lib.getName()).append("\n");
        buffer.append("URI: ").append(lib.getURI()).append("\n");
        buffer.append("\n");
    }

    public static void classListing(XSOntology lib, StringBuffer buffer) {
        assertNotNull(lib.getClasses());
        assertFalse(lib.getClasses().isEmpty());
        buffer.append("Classes\n");
        for (XSOntClass xsoClass : lib.getClasses()) {
            xsoObject(xsoClass, buffer);
        }
        buffer.append("\n");
    }

    public static void dataPropertyListing(XSOntology lib, StringBuffer buffer) {
        assertNotNull(lib.getDataProperties());
        assertFalse(lib.getDataProperties().isEmpty());
        buffer.append("Data properties\n");
        for (XSOntDataProperty xsoDP : ((XSDMetaModel) lib).getDataProperties()) {
            xsoObject(xsoDP, buffer);
        }
        buffer.append("\n");
    }

    public static void objectPropertyListing(XSOntology lib, StringBuffer buffer) {
        assertNotNull(lib.getObjectProperties());
        assertFalse(lib.getObjectProperties().isEmpty());
        buffer.append("Object properties\n");
        for (XSOntObjectProperty xsoOP : lib.getObjectProperties()) {
            xsoObject(xsoOP, buffer);
        }
        buffer.append("\n");
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
        resourceCenter = testApplicationContext.getResourceCenterService().getResourceCenters().get(0);
        xmlAdapter = testApplicationContext.getTechnologyAdapterService().getTechnologyAdapter(XMLTechnologyAdapter.class);
        xmlContextManager = (XMLTechnologyContextManager) xmlAdapter.getTechnologyContextManager();
        mmRepository = resourceCenter.getRepository(XSDMetaModelRepository.class, xmlAdapter);
        modelRepository = resourceCenter.getRepository(XMLXSDModelRepository.class, xmlAdapter);
        ((DirectoryResourceCenter) resourceCenter).getDirectory().getCanonicalPath();
        assertNotNull(modelRepository);
        assertNotNull(mmRepository);
        assertEquals(3, mmRepository.getAllResources().size());
        assertTrue(modelRepository.getAllResources().size() > 2);
    }

    @Test
    @TestOrder(2)
    public void test1Library() {
        StringBuffer buffer = new StringBuffer();
        XSDMetaModel lib = null;
        XSDMetaModelResource libRes = null;
        testApplicationContext = new TestApplicationContext();

        try {
            libRes = resourceCenter.getRepository(XSDMetaModelRepository.class, xmlAdapter).getResource("http://www.example.org/Library");
            lib = libRes.getResourceData(null);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertNotNull(lib);
        ((XSDMetaModelResourceImpl) lib.getResource()).loadWhenUnloaded();
        assertTrue(lib.getResource().isLoaded());
        if (lib.getResource().isLoaded() == false) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("Failed to load.");
                fail();
            }
        }
        else {
            generalInfos(lib, buffer);
            classListing(lib, buffer);
            dataPropertyListing(lib, buffer);
            objectPropertyListing(lib, buffer);

            if (logger.isLoggable(Level.INFO)) {
                logger.info(buffer.toString());
            }
        }
    }
}
