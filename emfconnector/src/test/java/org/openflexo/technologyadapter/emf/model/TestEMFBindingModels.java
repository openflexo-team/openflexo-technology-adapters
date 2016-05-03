/**
 * 
 * Copyright (c) 2015-2015, Openflexo
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

import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.OpenflexoTestCase;
import org.openflexo.technologyadapter.emf.EMFModelSlot;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.fml.binding.EMFBindingFactory;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;
import org.openflexo.technologyadapter.emf.rm.EMFModelResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test Class for EMF BindingModels Checks
 * 
 * @author xtof
 * 
 */
@RunWith(OrderedRunner.class)
public class TestEMFBindingModels extends OpenflexoTestCase {

	protected static final Logger logger = Logger.getLogger(TestEMFBindingModels.class.getPackage().getName());

	private static String cityOne_MM_URI = "http://www.thalesgroup.com/openflexo/emf/model/city1";

	static EMFBindingFactory bindingFactory = null;
	static EMFTechnologyAdapter technologicalAdapter;
	static EMFModelSlot newModelSlot = null;
	static EMFModelResource emfModelResource = null;
	static EMFMetaModelResource emfMetaModelResource = null;
	static EMFObjectIndividual individual = null;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() {

		instanciateTestServiceManager(EMFTechnologyAdapter.class);

		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(EMFTechnologyAdapter.class);

		assertNotNull(technologicalAdapter);
		assertNotNull(technologicalAdapter.getTechnologyContextManager());

		bindingFactory = new EMFBindingFactory();
	}

	@Test
	@TestOrder(2)
	public void testEMFMetaModelBindingModel() {

		EMFMetaModelResource metaModelResource = technologicalAdapter.getTechnologyContextManager()
				.getMetaModelResourceByURI(cityOne_MM_URI);

		assertNotNull(metaModelResource);

		EMFMetaModel metaModel = metaModelResource.getMetaModelData();

		assertNotNull(metaModel);

		// TODO Write tests

	}

	/*
	@Test
	@TestOrder(3)
	public void testEMFModelBindingModel() {
	
		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			/*
		EMFMetaModelRepository metaModelRepository = resourceCenter.getRepository(EMFMetaModelRepository.class, technologicalAdapter);
		assertNotNull(metaModelRepository);
	
		EMFModelRepository modelRepository = resourceCenter.getRepository(EMFModelRepository.class, technologicalAdapter);
		Collection<EMFModelResource> modelResources = modelRepository.getAllResources();
		for (EMFModelResource modelResource : modelResources) {
			System.out.println("\t Loading " + modelResource.getURI());
			EMFModel model = modelResource.getModel();
			assertNotNull(model);
			assertNotNull(model.getMetaModel());
	
	
		}
		}
	}
	
	@Test
	@TestOrder(4)
	public void testEMFIndividualBindingModel() {
	
	}
	 */
}
