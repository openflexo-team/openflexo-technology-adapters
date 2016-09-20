/**
 * 
 * Copyright (c) 2015, Openflexo
 * 
 * This file is part of Emfconnector, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.emf.model;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.test.OpenflexoTestCase;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.EMFTechnologyContextManager;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelRepository;
import org.openflexo.technologyadapter.emf.rm.EMFModelRepository;
import org.openflexo.technologyadapter.emf.rm.EMFModelResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test UML Meta-Model and model loading.
 * 
 * @author xtof
 * 
 */
@RunWith(OrderedRunner.class)
public class TestLoadAllEMFModel extends OpenflexoTestCase {
	protected static final Logger logger = Logger.getLogger(TestLoadAllEMFModel.class.getPackage().getName());

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		log("test0InstantiateResourceCenter()");

		instanciateTestServiceManager(EMFTechnologyAdapter.class);
	}

	@Test
	@TestOrder(2)
	public void TestListAllMetaModels() {
		EMFTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(EMFTechnologyAdapter.class);

		EMFTechnologyContextManager ctxManager = technologicalAdapter.getTechnologyContextManager();

		System.out.println("ALL REGISTERED METAMODELS: ");

		for (String uri : ctxManager.getAllMetaModelURIs()) {
			System.out.println("\t " + uri);
		}

		System.out.println("ALL REGISTERED PROFILES: ");

		for (String uri : ctxManager.getAllProfileURIs()) {
			System.out.println("\t " + uri);
		}

	}

	@Test
	@TestOrder(3)
	public void TestLoadAllEMFModels() {
		EMFTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(EMFTechnologyAdapter.class);

		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			EMFMetaModelRepository<?> metaModelRepository = technologicalAdapter.getEMFMetaModelRepository(resourceCenter);
			assertNotNull(metaModelRepository);
			EMFModelRepository<?> modelRepository = technologicalAdapter.getEMFModelRepository(resourceCenter);
			Collection<EMFModelResource> modelResources = modelRepository.getAllResources();
			for (EMFModelResource modelResource : modelResources) {
				System.out.println("\t Loading " + modelResource.getURI());
				EMFModel model = modelResource.getModel();
				assertNotNull(model);
				assertNotNull(model.getMetaModel());
			}
		}
	}
}
