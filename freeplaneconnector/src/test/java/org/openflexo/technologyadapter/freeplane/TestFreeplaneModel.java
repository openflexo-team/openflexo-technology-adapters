/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freeplane, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.freeplane;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.NodeModel;
import org.freeplane.main.application.FreeplaneBasicAdapter;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FileFlexoIODelegate;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneMap;
import org.openflexo.technologyadapter.freeplane.model.IFreeplaneNode;
import org.openflexo.technologyadapter.freeplane.model.impl.FreeplaneMapImpl;
import org.openflexo.technologyadapter.freeplane.rm.IFreeplaneResource;

public class TestFreeplaneModel extends OpenflexoTestCase {

	private static final Logger LOGGER = Logger.getLogger(TestFreeplaneModel.class.getPackage().getName());

	private static FreeplaneTechnologyAdapter fpTA;

	// private static FlexoServiceManager applicationContext;

	private ModelFactory factory;

	@BeforeClass
	public static void sotupBeforeClass() {
		serviceManager = instanciateTestServiceManager(FreeplaneTechnologyAdapter.class);
		fpTA = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(FreeplaneTechnologyAdapter.class);

		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			System.out.println("> rc: " + resourceCenter);
		}

		FlexoResourceCenter<?> resourceCenter = serviceManager.getResourceCenterService()
				.getFlexoResourceCenter("http://openflexo.org/freeplane-test");

		System.out.println("resourceCenter=" + resourceCenter);

		Assume.assumeNotNull(serviceManager, fpTA, resourceCenter);

		for (FlexoResource<?> r : resourceCenter.getAllResources(null)) {
			System.out.println(" > " + r);
		}

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
		assertNotNull(map);
		map.setTechnologyAdapter(fpTA);
		assertEquals(map.getTechnologyAdapter(), fpTA);
		final MapModel expected = new MapModel();
		expected.createNewRoot();
		map.setMapModel(expected);
		assertEquals(expected, map.getMapModel());
		assertNotNull(map.getRoot());
		assertEquals(Collections.emptyList(), map.getRoot().getChildren());
	}

	@Test
	public void validateConnectorToFreeplaneAPI() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		IFreeplaneResource fpResource = (IFreeplaneResource) serviceManager.getResourceManager()
				.getResource("http://openflexo.org/freeplane-test/TestResourceCenter/FPTest.mm");
		assertNotNull(fpResource);

		Assume.assumeTrue(fpResource.getFlexoIODelegate() instanceof FileFlexoIODelegate);

		IFreeplaneMap map = fpResource.getResourceData(null);

		final MapModel loadedMap = map.getMapModel();

		assertEquals(FreeplaneBasicAdapter.getInstance().getMapName(), "FPTest");
		assertNotNull(FreeplaneBasicAdapter.getInstance().getIconToolbar());
		assertNotNull(FreeplaneBasicAdapter.getInstance().getMapView());
		final NodeModel nœudRacine = loadedMap.getRootNode();
		assertEquals(nœudRacine.getText(), "FreeplaneModel First node");
		assertEquals(nœudRacine.getChildCount(), 4);
		assertEquals(nœudRacine.getChildAt(2).getText(), "すごい");

		/*
		 * final FreeplaneMapImpl map = (FreeplaneMapImpl)
		 * this.factory.newInstance(IFreeplaneMap.class);
		 * map.setTechnologyAdapter(fpTA); map.setMapModel(loadedMap);
		 */

		for (IFreeplaneNode node : map.getRoot().getChildren()) {
			if ("Tututus".equals(node.getNodeModel().getText())) {
				assertEquals(node.getNodeAttributes().size(), 1);
				assertEquals(node.getNodeAttributes().get(0).getName(), "key1");
				assertEquals(node.getNodeAttributes().get(0).getValue(), "nœud 1");
			}
			else if ("ue".equals(node.getNodeModel().getText())) {
				assertEquals(node.getNodeAttributes().size(), 1);
				assertEquals(node.getNodeAttributes().get(0).getName(), "key1");
				assertEquals(node.getNodeAttributes().get(0).getValue(), "nœud 2");
			}
		}
	}

}
