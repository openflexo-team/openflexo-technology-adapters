/**
 * 
 * Copyright (c) 2013-2017, Openflexo
 * Copyright (c) 2011-2012, AgileBirds
 * 
 * This file is part of Integration-tests, a component of the software infrastructure 
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.action.AddRepositoryFolder;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.foundation.fml.rt.View;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateViewInFolder;
import org.openflexo.foundation.fml.rt.rm.ViewResource;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.foundation.test.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.technologyadapter.xml.XMLTechnologyAdapter;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestBasicWorkbookXMLView extends OpenflexoProjectAtRunTimeTestCase {

	public static FlexoProject project;
	private static FlexoEditor editor;
	private static ViewPoint viewPoint;
	private static VirtualModel virtualModel;
	private static RepositoryFolder<ViewResource, ?> viewFolder;
	private static View view;

	private static String VP_URI = "http://www.openflexo.org/XMLTestVP";
	private static String VM_URI = "http://www.openflexo.org/XMLTestVP/XMLWorkbookTest";

	private ViewPoint loadViewPoint(String viewPointURI) {

		log("Testing ViewPoint loading: " + viewPointURI);

		ViewPointResource vpRes = (ViewPointResource) serviceManager.getResourceManager().getResource(viewPointURI);

		assertNotNull(vpRes);
		assertFalse(vpRes.isLoaded());

		ViewPoint vp = vpRes.getViewPoint();
		assertTrue(vpRes.isLoaded());

		return vp;

	}

	/**
	 * Instantiate test resource center
	 */
	@Test
	@TestOrder(1)
	public void test0InstantiateResourceCenter() {

		log("test0InstantiateResourceCenter()");

		// We are connected directely to the resource center embedded in a jar in the classpath
		// We use the ResourceCenter deployed in integration-tests-rc
		instanciateTestServiceManager(FMLTechnologyAdapter.class, FMLRTTechnologyAdapter.class, XMLTechnologyAdapter.class);

	}

	@Test
	@TestOrder(2)
	public void test1CreateProject() {
		editor = createProject("TestCreateView");
		project = editor.getProject();

		assertNotNull(project.getViewLibrary());
	}

	@Test
	@TestOrder(3)
	public void test2LoadViewPoint() {
		viewPoint = loadViewPoint(VP_URI);
		assertNotNull(viewPoint);
		System.out.println("Found view point in " + ((ViewPointResource) viewPoint.getResource()).getIODelegate().toString());
	}

	@Test
	@TestOrder(4)
	public void test3CreateViewFolder() {
		AddRepositoryFolder addRepositoryFolder = AddRepositoryFolder.actionType.makeNewAction(project.getViewLibrary().getRootFolder(),
				null, editor);
		addRepositoryFolder.setNewFolderName("NewViewFolder");
		addRepositoryFolder.doAction();
		assertTrue(addRepositoryFolder.hasActionExecutionSucceeded());
		viewFolder = addRepositoryFolder.getNewFolder();
		assertTrue(((File) viewFolder.getSerializationArtefact()).exists());
	}

	@Test
	@TestOrder(5)
	public void test4CreateView() {
		CreateViewInFolder addView = CreateViewInFolder.actionType.makeNewAction(viewFolder, null, editor);
		addView.setNewViewName("TestNewView");
		addView.setNewViewTitle("A nice title for a new view");
		addView.setViewpointResource((ViewPointResource) viewPoint.getResource());
		addView.doAction();
		assertTrue(addView.hasActionExecutionSucceeded());
		View newView = addView.getNewView();
		System.out.println("New view " + newView + " created in " + ((ViewResource) newView.getResource()).getIODelegate().toString());
		assertNotNull(newView);
		assertEquals(addView.getNewViewName(), newView.getName());
		assertEquals(addView.getNewViewTitle(), newView.getTitle());
		assertEquals(addView.getViewpointResource().getViewPoint(), viewPoint);
		assertTrue(((ViewResource) newView.getResource()).getIODelegate().exists());
	}

	@Test
	@TestOrder(6)
	public void test5ReloadProject() {
		editor = reloadProject(project.getProjectDirectory());
		project = editor.getProject();
		assertNotNull(project.getViewLibrary());
		assertEquals(1, project.getViewLibrary().getRootFolder().getChildren().size());
		viewFolder = project.getViewLibrary().getRootFolder().getChildren().get(0);
		assertEquals(1, viewFolder.getResources().size());
		ViewResource viewRes = viewFolder.getResources().get(0);
		assertEquals(viewRes, project.getViewLibrary().getResource(viewRes.getURI()));
		assertNotNull(viewRes);
		assertFalse(viewRes.isLoaded());
		view = viewRes.getView();
		assertTrue(viewRes.isLoaded());
		assertNotNull(view);
		assertEquals(project, ((ViewResource) view.getResource()).getResourceCenter());
	}

	@Test
	@TestOrder(7)
	public void test6LoadVirtualModel() {

		assertEquals(1, viewPoint.getVirtualModels(true).size());
		virtualModel = viewPoint.getVirtualModels(true).get(0);
		assertNotNull(virtualModel);
		assertEquals(virtualModel.getURI(), VM_URI);
	}

	@Test
	@TestOrder(8)
	public void test7CreateVirtualModelInstace() {

		log("testCreateVirtualModelInstance()");

		System.out.println("newView=" + view);
		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType.makeNewAction(view, null, editor);
		action.setNewVirtualModelInstanceName("MyVirtualModelInstance");
		action.setNewVirtualModelInstanceTitle("Test creation of a new VirtualModelInstance");
		action.setVirtualModel(virtualModel);
		CreationScheme creationScheme = virtualModel.getCreationSchemes().get(0); // First Creation Scheme
		action.setCreationScheme(creationScheme);
		action.doAction();
		assertTrue(action.hasActionExecutionSucceeded());
		VirtualModelInstance newVirtualModelInstance = action.getNewVirtualModelInstance();
		assertNotNull(newVirtualModelInstance);
		assertNotNull(newVirtualModelInstance.getResource());

		assertEquals(virtualModel, newVirtualModelInstance.getFlexoConcept());
		assertEquals(virtualModel, newVirtualModelInstance.getVirtualModel());

		// Save All Attempt:
		for (FlexoResource<?> r : serviceManager.getResourceManager().getUnsavedResources()) {
			try {
				System.out.println("SAVING: " + r.getURI());
				r.save(null);
			} catch (SaveResourceException e) {
				System.out.println("Unable to save: " + r.getURI());
				e.printStackTrace();
			}
		}
	}

}
