/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.technologyadapter.oslc.fml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.fml.AbstractModelFactoryIntegrationTestCase;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.technologyadapter.oslc.OSLCTechnologyAdapter;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestOslcTechnologyAdapterIntegration extends AbstractModelFactoryIntegrationTestCase {

	private static final Logger logger = FlexoLogger.getLogger(TestOslcTechnologyAdapterIntegration.class.getPackage().getName());

	/**
	 * Instanciate test ServiceManager
	 */
	@Test
	@TestOrder(1)
	public void initializeServiceManager() {
		log("initializeServiceManager()");
		instanciateTestServiceManager();

		assertNotNull(serviceManager.getService(FlexoResourceCenterService.class));
		assertNotNull(serviceManager.getService(TechnologyAdapterService.class));

		TechnologyAdapterService taService = serviceManager.getTechnologyAdapterService();
		assertEquals(taService, serviceManager.getService(TechnologyAdapterService.class));

		assertNotNull(taService.getTechnologyAdapter(OSLCTechnologyAdapter.class));
	}

	/**
	 * Check the presence of {@link FMLTechnologyAdapter}, instanciate FMLModelFactory with this TA
	 */
	@Test
	@TestOrder(2)
	public void checkOSLCTechnologyAdapter() {
		log("checkOSLCTechnologyAdapter()");

		testVirtualModelModelFactoryWithTechnologyAdapter(serviceManager.getTechnologyAdapterService().getTechnologyAdapter(
				OSLCTechnologyAdapter.class));
	}

}
