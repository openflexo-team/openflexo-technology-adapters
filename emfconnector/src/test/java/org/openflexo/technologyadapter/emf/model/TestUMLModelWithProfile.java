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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.logging.Logger;

import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.foundation.fml.action.CreateEditionAction;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.emf.EMFTechnologyAdapter;
import org.openflexo.technologyadapter.emf.EMFTechnologyContextManager;
import org.openflexo.technologyadapter.emf.fml.editionaction.AddEMFObjectIndividual;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelRepository;
import org.openflexo.technologyadapter.emf.rm.EMFMetaModelResource;
import org.openflexo.technologyadapter.emf.rm.EMFModelRepository;
import org.openflexo.technologyadapter.emf.rm.EMFModelResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test UML Meta-Model and model loading. and Testing a Model with a Profile (SysML)
 * 
 * @author xtof
 * 
 */
@RunWith(OrderedRunner.class)
public class TestUMLModelWithProfile extends OpenflexoProjectAtRunTimeTestCase {
	protected static final Logger logger = Logger.getLogger(TestUMLModelWithProfile.class.getPackage().getName());

	private static FlexoEditor editor;
	private static FlexoProject project;
	private static EMFTechnologyAdapter technologicalAdapter;
	private static EMFTechnologyContextManager ctxManager;
	private static EMFMetaModelResource umlMetaModelResource;
	private static EMFMetaModel umlMetaModel;
	
	private static String UML_MODEL_NAME = "testingProfiles.uml";

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		log("test0InstantiateResourceCenter()");

		instanciateTestServiceManager();

		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(
				EMFTechnologyAdapter.class);
		assertNotNull(technologicalAdapter);
		
		ctxManager = technologicalAdapter.getTechnologyContextManager();
		assertNotNull(ctxManager);
	}

	@Test
	@TestOrder(2)
	public void testCreateProject() {
		editor = createProject("TestProject");
		project = editor.getProject();
		System.out.println("Created project " + project.getProjectDirectory());
		assertTrue(project.getProjectDirectory().exists());
		assertTrue(project.getProjectDataResource().getFlexoIODelegate().exists());
	}




	@Test
	@TestOrder(3)
	public void TestLoadUMLMetaModel() {
		

		umlMetaModelResource = ctxManager.getMetaModelResourceByURI(EMFTechnologyAdapter.UML_MM_URI);
		
		assertNotNull(umlMetaModelResource);
		
		umlMetaModel = umlMetaModelResource.getMetaModelData();

		assertNotNull(umlMetaModel);
		
		
	}

	@Test
	@TestOrder(4)
	public void TestLoadSysMLMetaModell() {


		EMFMetaModelResource sysmlMetaModelResource = ctxManager.getProfileResourceByURI("http://www.eclipse.org/papyrus/0.7.0/SysML");
		
		assertNotNull(sysmlMetaModelResource);
		
		EMFMetaModel umlMetaModel = sysmlMetaModelResource.getMetaModelData();

		assertNotNull(umlMetaModel);
	}

	@Test
	@TestOrder(5)
	public void createUMLModel() {
		RepositoryFolder<FlexoResource<?>> folder = project.createNewFolder("Models");
		File umlFile = new File (folder.getFile(),UML_MODEL_NAME);
		assertNotNull(umlFile);
		EMFModelResource umlModelResource = technologicalAdapter.createNewEMFModel(umlFile, umlFile.getAbsolutePath(), umlMetaModelResource);
		assertNotNull(umlModelResource);
		EMFModel umlModel = umlModelResource.getModel();
		assertNotNull(umlModel);
		assertTrue(umlModel.getMetaModel() == umlMetaModel);
		
		UMLResource umlEResource = (UMLResource) umlModel.getEMFResource();
		assertNotNull(umlEResource);

		
	}
	
/*	
	protected EMFObjectIndividual addEMFObjectIndividual(EMFModelResource emfModelResource, String classURI, FMLModelFactory factory) {

		EMFObjectIndividual result = null;

		CreateEditionAction createEditionAction1 = CreateEditionAction.actionType.makeNewAction(creationScheme.getControlGraph(), null,
				editor);
		// createEditionAction1.actionChoice = CreateEditionActionChoice.ModelSlotSpecificAction;
		createEditionAction1.setEditionActionClass(AddEMFObjectIndividual.class);
		createEditionAction1.setModelSlot(newModelSlot);
		createEditionAction1.doAction();

		AddEMFObjectIndividual addObject = (AddEMFObjectIndividual) createEditionAction1.getNewEditionAction();

		try {
			addObject.setOntologyClass(umlMetaModelResource.getResourceData(null).getClass(classURI));
			// addObject.setEMFClassURI(classURI);
			result = addObject.execute(creationSchemeCreationAction);
			// addObject.finalizePerformAction(creationSchemeCreationAction, result);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlexoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
*/
	
	
	
}