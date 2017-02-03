/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Fml-technologyadapter-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.fib;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPointLibrary;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.action.CreateModelSlot;
import org.openflexo.foundation.fml.action.CreateTechnologyRole;
import org.openflexo.foundation.fml.rm.ViewPointResource;
import org.openflexo.foundation.fml.rm.ViewPointResourceFactory;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.gina.swing.utils.FIBJPanel;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.docx.AbstractTestDocXInspector;
import org.openflexo.technologyadapter.docx.DocXModelSlot;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.fml.DocXImageRole;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test StandardFlexoConceptView fib
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDocXImageRoleInspector extends AbstractTestDocXInspector {

	protected static final Logger logger = Logger.getLogger(TestDocXImageRoleInspector.class.getPackage().getName());

	private final String VIEWPOINT_NAME = "TestDocXImageRoleInspectorViewPoint";
	private final String VIEWPOINT_URI = "http://openflexo.org/test/TestDocXImageRoleInspectorViewPoint";
	private static final String DOCUMENT_VIRTUAL_MODEL_NAME = "DocumentVirtualModel";

	/*
	 * private static SwingGraphicalContextDelegate gcDelegate;
	 * 
	 * private static Resource fibResource;
	 */

	public static DocXTechnologyAdapter technologicalAdapter;
	public static DocXDocumentRepository repository;
	public static ViewPoint viewPoint;
	public static ViewPointResource viewPointResource;

	public static VirtualModel documentVirtualModel;
	public static DocXModelSlot docXModelSlot;
	public static DocXDocument templateDocument;

	public static DocXDocumentResource templateResource;

	@Test
	@TestOrder(1)
	public void testLoadWidget() {

		fibResource = ResourceLocator.locateResource("Inspectors/DocX/DocXImageRole.inspector");
		assertTrue(fibResource != null);
	}

	@Test
	@TestOrder(2)
	public void testValidateWidget() throws InterruptedException {

		validateFIB(fibResource);
	}

	private static DocXImageRole role;

	private static DirectoryResourceCenter newResourceCenter;

	@Test
	@TestOrder(3)
	public void loadConcepts()
			throws ResourceLoadingCancelledException, FlexoException, ModelDefinitionException, IOException {

		technologicalAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(DocXTechnologyAdapter.class);

		newResourceCenter = makeNewDirectoryResourceCenter(serviceManager);

		ViewPointLibrary vpLib = serviceManager.getViewPointLibrary();
		assertNotNull(vpLib);

		FMLTechnologyAdapter fmlTechnologyAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(FMLTechnologyAdapter.class);
		ViewPointResourceFactory factory = fmlTechnologyAdapter.getViewPointResourceFactory();

		viewPointResource = factory.makeViewPointResource(VIEWPOINT_NAME, VIEWPOINT_URI,
				fmlTechnologyAdapter.getGlobalRepository(newResourceCenter).getRootFolder(),
				fmlTechnologyAdapter.getTechnologyContextManager(), true);
		viewPoint = viewPointResource.getLoadedResourceData();
		// viewPoint = ViewPointImpl.newViewPoint(VIEWPOINT_NAME, VIEWPOINT_URI,
		// resourceCenter.getDirectory(),
		// serviceManager.getViewPointLibrary(), resourceCenter);
		// viewPointResource = (ViewPointResource) viewPoint.getResource();
		// assertTrue(viewPointResource.getDirectory().exists());
		assertTrue(viewPointResource.getDirectory() != null);
		assertTrue(viewPointResource.getFlexoIODelegate().exists());

		templateResource = getDocumentResource("DocumentWithSomeImages.docx");
		assertNotNull(templateDocument = templateResource.getResourceData(null));
		System.out.println(templateDocument.debugStructuredContents());
		assertEquals(19, templateDocument.getElements().size());

		// We create a VM
		VirtualModelResource newVMResource = factory.getVirtualModelResourceFactory().makeVirtualModelResource(
				DOCUMENT_VIRTUAL_MODEL_NAME, viewPoint.getViewPointResource(),
				fmlTechnologyAdapter.getTechnologyContextManager(), true);
		documentVirtualModel = newVMResource.getLoadedResourceData();
		// documentVirtualModel =
		// VirtualModelImpl.newVirtualModel("DocumentVirtualModel", viewPoint);
		assertTrue(ResourceLocator
				.retrieveResourceAsFile(((VirtualModelResource) documentVirtualModel.getResource()).getDirectory())
				.exists());
		assertTrue(((VirtualModelResource) documentVirtualModel.getResource()).getFlexoIODelegate().exists());

		// Then we create the docx model slot
		CreateModelSlot createDocumentModelSlot = CreateModelSlot.actionType.makeNewAction(documentVirtualModel, null,
				editor);
		createDocumentModelSlot.setTechnologyAdapter(technologicalAdapter);
		createDocumentModelSlot.setModelSlotClass(DocXModelSlot.class);
		createDocumentModelSlot.setModelSlotName("document");
		createDocumentModelSlot.doAction();
		assertTrue(createDocumentModelSlot.hasActionExecutionSucceeded());
		assertNotNull(docXModelSlot = (DocXModelSlot) createDocumentModelSlot.getNewModelSlot());
		docXModelSlot.setTemplateResource(templateResource);

		// We create a image role
		CreateTechnologyRole createImageRole = CreateTechnologyRole.actionType.makeNewAction(documentVirtualModel, null,
				editor);
		createImageRole.setRoleName("image");
		// createSectionRole.setModelSlot(docXModelSlot);
		createImageRole.setFlexoRoleClass(DocXImageRole.class);
		assertEquals(documentVirtualModel.getModelSlot("document"), createImageRole.getModelSlot());
		createImageRole.doAction();
		assertTrue(createImageRole.hasActionExecutionSucceeded());
		role = (DocXImageRole) createImageRole.getNewFlexoRole();

		assertNotNull(role);

		System.out.println(viewPoint.getFMLRepresentation());

	}

	@Test
	@TestOrder(4)
	public void testInstanciateWidget() {

		FIBJPanel<DocXImageRole> widget = instanciateFIB(fibResource, role, DocXImageRole.class);

		gcDelegate.addTab("DocXImageRole", widget.getController());
	}

	/*
	 * public static void initGUI() { gcDelegate = new
	 * SwingGraphicalContextDelegate(TestDocXImageRoleInspector.class.
	 * getSimpleName()); }
	 * 
	 * @AfterClass public static void waitGUI() { gcDelegate.waitGUI(); }
	 * 
	 * @Before public void setUp() { gcDelegate.setUp(); }
	 * 
	 * @Override
	 * 
	 * @After public void tearDown() throws Exception { gcDelegate.tearDown(); }
	 */

}
