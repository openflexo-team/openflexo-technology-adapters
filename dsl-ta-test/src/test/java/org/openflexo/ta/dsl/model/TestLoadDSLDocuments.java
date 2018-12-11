package org.openflexo.ta.dsl.model;
/**
 * 
 * Copyright (c) 2018, Openflexo
 * 
 * This file is part of OpenflexoTechnologyAdapter, a component of the software infrastructure 
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.ta.dsl.AbstractDSLTest;
import org.openflexo.ta.dsl.DSLTechnologyAdapter;
import org.openflexo.ta.dsl.rm.DSLResource;
import org.openflexo.ta.dsl.rm.DSLResourceRepository;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestLoadDSLDocuments extends AbstractDSLTest {
	protected static final Logger logger = Logger.getLogger(TestLoadDSLDocuments.class.getPackage().getName());

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(DSLTechnologyAdapter.class);
	}

	@Test
	@TestOrder(3)
	public void testDSLLoading() {
		DSLTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(DSLTechnologyAdapter.class);

		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			DSLResourceRepository<?> workbookRepository = technologicalAdapter.getDSLResourceRepository(resourceCenter);
			assertNotNull(workbookRepository);
			Collection<DSLResource> documents = workbookRepository.getAllResources();
			for (DSLResource docResource : documents) {
				try {
					docResource.loadResourceData();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (ResourceLoadingCancelledException e) {
					e.printStackTrace();
				} catch (FlexoException e) {
					e.printStackTrace();
				}
				assertNotNull(docResource.getLoadedResourceData());
				System.out.println("URI of document: " + docResource.getURI());
				System.out.println("ResourceData: " + docResource.getLoadedResourceData());
			}
		}
	}

	@Test
	@TestOrder(4)
	public void testDSLContents() {

		DSLSystem system = getDSLSystem("Test1.component");
		System.out.println("Test1.component:\n" + system);

		assertEquals(3, system.getComponents().size());
		assertEquals("ComponentA", system.getComponents().get(0).getName());
		assertEquals("ComponentB", system.getComponents().get(1).getName());
		assertEquals("ComponentC", system.getComponents().get(2).getName());
		assertEquals(4, system.getLinks().size());
		assertEquals("link1", system.getLinks().get(0).getName());
		assertEquals("link2", system.getLinks().get(1).getName());
		assertEquals("link3", system.getLinks().get(2).getName());
		assertEquals("link4", system.getLinks().get(3).getName());
	}
}
