/**
 * 
 * Copyright (c) 2017, Openflexo
 * 
 * This file is part of Xmlconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.xml.fml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.foundation.test.fml.AbstractModelFactoryIntegrationTestCase;
import org.openflexo.logging.FlexoLogger;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.technologyadapter.xml.rm.XMLResourceFactory;
import org.openflexo.technologyadapter.xml.rm.XMLResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test instanciation of FMLModelFactory<br>
 * Here the model factory is instanciated with all FML and FML@RT technology adapters
 * 
 */
@RunWith(OrderedRunner.class)
public class TestCreateXMLResource extends AbstractModelFactoryIntegrationTestCase {

	private static final Logger logger = FlexoLogger.getLogger(TestCreateXMLResource.class.getPackage().getName());

	protected static DirectoryResourceCenter resourceCenter = null;
	protected static XMLTechnologyAdapter xmlTA = null;

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

		assertNotNull(taService.getTechnologyAdapter(XMLTechnologyAdapter.class));
	}

	/**
	 * Check the presence of {@link FMLTechnologyAdapter}, instanciate FMLModelFactory with this TA
	 */
	@Test
	@TestOrder(3)
	public void createNewTestRC() {
		log("createNewTestRC()");

		try {
			resourceCenter = makeNewDirectoryResourceCenter(serviceManager);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(resourceCenter);
	}

	/**
	 * Activate XML TA
	 */
	@Test
	@TestOrder(4)
	public void activateXMLTA() {
		log("activateXMLTA()");

		xmlTA = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(XMLTechnologyAdapter.class);
		if (!xmlTA.isActivated()) {
			xmlTA.activate();
		}

		assertTrue(xmlTA.isActivated());

	}

	/**
	 * Check if we can create a new XML resource
	 */
	@Test
	@TestOrder(5)
	public void createNewXMLResource() {
		log("createNewXMLResource()");

		XMLResource rsc = null;

		try {
			rsc = xmlTA.createResource(XMLResourceFactory.class, resourceCenter, "aTest.xml", "http://test.openflexo.org/aTest.xml",
					"model", ".xml", true);
		} catch (SaveResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ModelDefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertNotNull(rsc);

		rsc.setModified(true);
		try {
			rsc.save(null);
		} catch (SaveResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	@After
	public void tearDown() throws Exception {
		if (xmlTA != null) {
			xmlTA.disactivate();
		}
		if (resourceCenter != null) {
			resourceCenter.stop();
		}
		super.tearDown();
	}

}
