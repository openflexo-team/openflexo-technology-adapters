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

package org.openflexo.technologyadapter.emf.gui.browser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.OpenflexoTestCaseWithGUI;
import org.openflexo.components.widget.OntologyBrowserModel;
import org.openflexo.fib.testutils.GraphicalContextDelegate;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.gui.EMFModelView;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelRepository;
import org.openflexo.technologyadapter.emf.rm.EMFModelRepository;
import org.openflexo.technologyadapter.emf.rm.EMFModelResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test Class for OntologyBrowser on an UMLModel
 * 
 * @author  xtof
 * 
 */
@RunWith(OrderedRunner.class)
public class TestUMLOntologyBrowerModel extends OpenflexoTestCaseWithGUI {
	protected static final Logger logger = Logger.getLogger(TestUMLOntologyBrowerModel.class.getPackage().getName());
	
	static EMFTechnologyAdapter technologicalAdapter;
	static EMFModelResource umlModelResource = null;
	static EMFModel umlModel = null;


	private static GraphicalContextDelegate gcDelegate;
	
	static String umlModelResourceRelativeURI = "/TestResourceCenter/EMF/Model/uml/test1.uml";
	

	@BeforeClass
	public static void setupClass() {
		instanciateTestServiceManager(true);

		instanciateTestServiceManager(true);
		
		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(EMFTechnologyAdapter.class);
		

		initGUI();
	}


	@Test
	@TestOrder(1)
	public void TestLoadUMLEMFModel() {
		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			
			EMFMetaModelRepository metaModelRepository = resourceCenter.getRepository(EMFMetaModelRepository.class, technologicalAdapter);
			assertNotNull(metaModelRepository);

			EMFModelRepository modelRepository = resourceCenter.getRepository(EMFModelRepository.class, technologicalAdapter);
			assertNotNull(modelRepository);
			
			System.out.println("Loading " + ((FileSystemBasedResourceCenter) resourceCenter).getRootDirectory().getAbsolutePath() +  umlModelResourceRelativeURI);
			
			umlModelResource = modelRepository.getResource("file:" +((FileSystemBasedResourceCenter) resourceCenter).getRootDirectory().getAbsolutePath() +  umlModelResourceRelativeURI);
			
			assertNotNull(umlModelResource);
			
			umlModel = umlModelResource.getModel();
			assertNotNull(umlModel);
			assertNotNull(umlModel.getMetaModel());
			assertEquals(umlModel.getMetaModel().getURI(),technologicalAdapter.UML_MM_URI );
		}
	}


	@Test
	@TestOrder(3)
	public void TestCreateOntologyBrowser() {
	
		long startTime = System.currentTimeMillis();

		OntologyBrowserModel<EMFTechnologyAdapter> obm = new OntologyBrowserModel<EMFTechnologyAdapter>(umlModel);

		long endTime = System.currentTimeMillis();

		System.out.println("\t\t Building OntologyBrowser took " + (endTime - startTime) + " milliseconds");

		obm.setStrictMode(true);
		obm.setHierarchicalMode(false);

		startTime = System.currentTimeMillis();

		obm.recomputeStructure();
		
		endTime = System.currentTimeMillis();

		System.out.println("\t\t Recomputing OntologyBrowser took  " + (endTime - startTime) + " milliseconds");

	}


	@Test
	@TestOrder(4)
	public void TestCreateEMFModelView() {
		long previousDate, currentDate;
		
		logger.info("TestCreateEMFModelView");

		previousDate = System.currentTimeMillis();

		EMFModelView modelView = new EMFModelView(umlModel,null,null);
		currentDate = System.currentTimeMillis();
		System.out.println (" initial creation of view took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		gcDelegate.addTab("umlView", modelView.getFIBController());

		previousDate = System.currentTimeMillis();

		modelView.setShowIndividuals(false);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowIndividuals took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.setShowClasses(false);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowClasses took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.setShowDataProperties(false);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowDataProperties took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.setShowObjectProperties(false);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowObjectProperties took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.setShowAnnotationProperties(false);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowAnnotationProperties took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.update();
		currentDate = System.currentTimeMillis();
		System.out.println (" update took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.setShowClasses(true);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowClasses took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.setShowDataProperties(true);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowDataProperties took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.setShowObjectProperties(true);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowObjectProperties took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.setShowAnnotationProperties(true);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowAnnotationProperties took: " + (currentDate-previousDate));
		previousDate=currentDate;

		modelView.setShowIndividuals(true);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowIndividuals took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		
		modelView.update();
		currentDate = System.currentTimeMillis();
		System.out.println (" update took: " + (currentDate-previousDate));
		previousDate=currentDate;
		

		modelView.setShowIndividuals(false);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowIndividuals took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.setShowClasses(false);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowClasses took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.setShowDataProperties(false);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowDataProperties took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.setShowObjectProperties(false);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowObjectProperties took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.setShowAnnotationProperties(false);
		currentDate = System.currentTimeMillis();
		System.out.println (" setShowAnnotationProperties took: " + (currentDate-previousDate));
		previousDate=currentDate;
		
		modelView.update();
		currentDate = System.currentTimeMillis();
		System.out.println (" update took: " + (currentDate-previousDate));
		previousDate=currentDate;
	}
	

	public static void initGUI() {
		gcDelegate = new GraphicalContextDelegate(TestUMLOntologyBrowerModel.class.getSimpleName());
	}

	@AfterClass
	public static void waitGUI() {
		gcDelegate.waitGUI();
	}

	@Before
	public void setUp() {
		gcDelegate.setUp();
	}

	@Override
	@After
	public void tearDown() throws Exception {
		gcDelegate.tearDown();
	}
}
