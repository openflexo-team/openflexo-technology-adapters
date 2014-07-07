package org.openflexo.technologyadapter.freeplane;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.freeplane.features.map.MapModel;
import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.impl.FreeplaneMapImpl;

public class TestFreeplaneModel extends OpenflexoTestCase {

    private static final Logger LOGGER = Logger.getLogger(TestFreeplaneModel.class.getPackage().getName());

    private static FreeplaneTechnologyAdapter fpTA;

    private static FlexoServiceManager applicationContext;

    private ModelFactory factory;

    @BeforeClass
    public static void sotupBeforeClass() {
        applicationContext = instanciateTestServiceManager();
        fpTA = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(FreeplaneTechnologyAdapter.class);
        resourceCenter = (DirectoryResourceCenter) applicationContext.getResourceCenterService().getResourceCenters().get(0);
        Assume.assumeNotNull(applicationContext, fpTA, resourceCenter);
    }

    @Before
    public void setup() {
        try {
            this.factory = new ModelFactory(IFreeplaneMap.class);
        } catch (final ModelDefinitionException e) {
            final String msg = "Error while initializing Freeplane model resource";
            LOGGER.log(Level.SEVERE, msg, e);
        }
        FreeplaneBasicAdapter.getInstance();
    }

    @Override
    public void tearDown() {
        this.factory = null;
    }

    @Test
    public void validateModelDefinition() {
        final FreeplaneMapImpl map = (FreeplaneMapImpl) this.factory.newInstance(IFreeplaneMap.class);
        Assert.assertNotNull(map);
        map.setTechnologyAdapter(fpTA);
        Assert.assertEquals(map.getTechnologyAdapter(), fpTA);
        final MapModel expected = new MapModel();
        expected.createNewRoot();
        map.setMapModel(expected);
        Assert.assertEquals(expected, map.getMapModel());
        Assert.assertNotNull(map.getRoot());
        Assert.assertEquals(Collections.emptyList(), map.getRoot().getChildren());
    }

}
