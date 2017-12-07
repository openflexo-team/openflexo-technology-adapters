package org.openflexo.technologyadapter.excel;

/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Excelconnector, a component of the software infrastructure 
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

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.gina.test.OpenflexoTestCaseWithGUI;
import org.openflexo.gina.test.SwingGraphicalContextDelegate;
import org.openflexo.technologyadapter.excel.model.ExcelWorkbook;
import org.openflexo.technologyadapter.excel.rm.ExcelWorkbookResource;

public abstract class AbstractTestExcel extends OpenflexoTestCaseWithGUI {
	protected static final Logger logger = Logger.getLogger(AbstractTestExcel.class.getPackage().getName());

	protected static SwingGraphicalContextDelegate gcDelegate;

	protected ExcelWorkbookResource getExcelResource(String documentName) {

		FlexoResourceCenter<?> resourceCenter = serviceManager.getResourceCenterService()
				.getFlexoResourceCenter("http://www.openflexo.org/test/excel");

		for (FlexoResourceCenter<?> rc : serviceManager.getResourceCenterService().getResourceCenters()) {
			System.out.println("> " + rc.getDefaultBaseURI());
		}

		System.out.println("resourceCenter=" + resourceCenter);

		String documentURI = resourceCenter.getDefaultBaseURI() + "/" + "Excel" + "/" + documentName;
		System.out.println("Searching " + documentURI);

		ExcelWorkbookResource documentResource = (ExcelWorkbookResource) serviceManager.getResourceManager().getResource(documentURI, null,
				ExcelWorkbook.class);

		if (documentResource == null) {
			logger.warning("Cannot find document resource " + documentURI);
			for (FlexoResource<?> r : serviceManager.getResourceManager().getRegisteredResources()) {
				System.out.println("> " + r.getURI());
			}
		}

		assertNotNull(documentResource);

		return documentResource;

	}

	protected ExcelWorkbook getWorkbook(String documentName) {

		ExcelWorkbookResource documentResource = getExcelResource(documentName);
		assertNotNull(documentResource);

		ExcelWorkbook document = null;
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
		assertNotNull(document.getWorkbook());

		return document;
	}

	@BeforeClass
	public static void setupClass() {
		instanciateTestServiceManager(ExcelTechnologyAdapter.class);
		initGUI();
	}

	public static void initGUI() {
		gcDelegate = new SwingGraphicalContextDelegate("AbstractTestExcel");

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
