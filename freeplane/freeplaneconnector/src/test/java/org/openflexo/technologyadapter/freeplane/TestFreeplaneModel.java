package org.openflexo.technologyadapter.freeplane;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;

import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
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
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
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

	@Test
	public void validateConnectorToFreeplaneAPI() {
		final MapModel loadedMap = FreeplaneBasicAdapter.getInstance().loadMapFromFile(ResourceLocator.retrieveResourceAsFile(ResourceLocator.locateResource("TestResourceCenter/FPTest.mm")));
		Assert.assertEquals(FreeplaneBasicAdapter.getInstance().getMapName(), "FPTest");
		Assert.assertNotNull(FreeplaneBasicAdapter.getInstance().getIconToolbar());
		Assert.assertNotNull(FreeplaneBasicAdapter.getInstance().getMapView());
		final NodeModel nœudRacine = loadedMap.getRootNode();
		Assert.assertEquals(nœudRacine.getText(),"FreeplaneModel First node");
		Assert.assertEquals(nœudRacine.getChildCount(), 4);
		Assert.assertEquals(nœudRacine.getChildAt(2).getText(), "すごい");

		final FreeplaneMapImpl map = (FreeplaneMapImpl) this.factory.newInstance(IFreeplaneMap.class);
		map.setTechnologyAdapter(fpTA);
		map.setMapModel(loadedMap);
		for (IFreeplaneNode node : map.getRoot().getChildren()){
			if ("Tututus".equals(node.getNodeModel().getText())){
				Assert.assertEquals(node.getNodeAttributes().size(),1);
				Assert.assertEquals(node.getNodeAttributes().get(0).getName(),"key1");
				Assert.assertEquals(node.getNodeAttributes().get(0).getValue(),"nœud 1");
			}
			else if ("ue".equals(node.getNodeModel().getText())) {
				Assert.assertEquals(node.getNodeAttributes().size(), 1);
				Assert.assertEquals(node.getNodeAttributes().get(0).getName(), "key1");
				Assert.assertEquals(node.getNodeAttributes().get(0).getValue(), "nœud 2");
			}
		}
	}

}
