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

package org.openflexo.technologyadapter.docx;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.gina.test.OpenflexoFIBInspectorTestCase;
import org.openflexo.gina.test.OpenflexoTestCaseWithGUI;
import org.openflexo.gina.test.SwingGraphicalContextDelegate;
import org.openflexo.rm.Resource;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.IdentifierManagementStrategy;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;

/**
 * Test StandardFlexoConceptView fib
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public abstract class AbstractTestDocXInspector extends OpenflexoFIBInspectorTestCase {

	protected static final Logger logger = Logger.getLogger(AbstractTestDocXInspector.class.getPackage().getName());

	protected static SwingGraphicalContextDelegate gcDelegate;
	protected static Resource fibResource;
	protected static FlexoEditor editor;

	/**
	 * Instantiate a default {@link FlexoServiceManager} well suited for test purpose<br>
	 * FML and FML@RT technology adapters are activated in returned {@link FlexoServiceManager}, as well as technology adapters whose
	 * classes are supplied as varargs arguments
	 * 
	 * @param taClasses
	 * @return a newly created {@link FlexoServiceManager}
	 */
	protected static FlexoServiceManager instanciateTestServiceManagerForDocX(IdentifierManagementStrategy idStrategy) {
		serviceManager = instanciateTestServiceManager();
		DocXTechnologyAdapter docXTA = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class);
		docXTA.setDefaultIDStrategy(idStrategy);
		serviceManager.activateTechnologyAdapter(docXTA);
		return serviceManager;
	}

	protected DocXDocumentResource getDocumentResource(String documentName) {

		FlexoResourceCenter<?> resourceCenter = serviceManager.getResourceCenterService()
				.getFlexoResourceCenter("http://openflexo.org/docx-test");

		for (FlexoResourceCenter<?> rc : serviceManager.getResourceCenterService().getResourceCenters()) {
			System.out.println("> " + rc.getDefaultBaseURI());
		}

		System.out.println("resourceCenter=" + resourceCenter);

		String documentURI = resourceCenter.getDefaultBaseURI() + "/" + "TestResourceCenter" + "/" + documentName;
		System.out.println("Searching " + documentURI);

		DocXDocumentResource documentResource = (DocXDocumentResource) serviceManager.getResourceManager().getResource(documentURI, null,
				DocXDocument.class);

		if (documentResource == null) {
			logger.warning("Cannot find document resource " + documentURI);
			for (FlexoResource<?> r : serviceManager.getResourceManager().getRegisteredResources()) {
				System.out.println("> " + r.getURI());
			}
		}

		assertNotNull(documentResource);

		return documentResource;
	}

	/*
	 * protected DocXDocumentResource getDocumentResource(String documentName) {
	 * 
	 * String documentURI = resourceCenter.getDefaultBaseURI() +
	 * (resourceCenter.getDefaultBaseURI().endsWith("/") ? "" : "/") +
	 * "TestResourceCenter" + "/" + documentName;
	 * System.out.println("Searching " + documentURI);
	 * 
	 * DocXDocumentResource documentResource = (DocXDocumentResource)
	 * serviceManager.getResourceManager().getResource(documentURI, null,
	 * DocXDocument.class);
	 * 
	 * if (documentResource == null) { System.out.println("Cannot find: " +
	 * documentURI); for (FlexoResource r : resourceCenter.getAllResources()) {
	 * System.out.println(" > " + r.getURI()); } }
	 * assertNotNull(documentResource);
	 * 
	 * return documentResource;
	 * 
	 * }
	 */

	protected DocXDocument getDocument(String documentName) {

		DocXDocumentResource documentResource = getDocumentResource(documentName);
		assertNotNull(documentResource);

		DocXDocument document = null;
		try {
			document = documentResource.getResourceData(null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
		} catch (FlexoException e) {
			e.printStackTrace();
		}
		assertNotNull(document);
		assertNotNull(document.getWordprocessingMLPackage());

		return document;
	}

	@BeforeClass
	public static void setupClass() {
		instanciateTestServiceManagerForDocX(IdentifierManagementStrategy.ParaId);
		initGUI();
	}

	public static void initGUI() {
		gcDelegate = new SwingGraphicalContextDelegate("AbstractTestDocXInspector");

		// TODO: please check this: suspiscion of missing code after merge

		/*
		 * {
		 * 
		 * @Override public boolean handleException(Exception e) { //
		 * System.out.println( // "Handle exception ? isDisposed=" +
		 * isDisposed() + " exception=" + e + " stacktrace=" +
		 * e.getStackTrace().length); if (e instanceof NullPointerException &&
		 * ((NullPointerException) e).getStackTrace().length == 0) { // Handle
		 * unexpected exception occured in docx4all editor // We suspect issues
		 * with fonts // Temporary ignore those exceptions return false; }
		 * return super.handleException(e); } }
		 */
	}

	@AfterClass
	public static void waitGUI() {
		gcDelegate.waitGUI();
	}

	@Before
	public void setUp() {
		gcDelegate.setUp();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		OpenflexoTestCaseWithGUI.tearDownClass();
		gcDelegate.tearDown();
	}

}
