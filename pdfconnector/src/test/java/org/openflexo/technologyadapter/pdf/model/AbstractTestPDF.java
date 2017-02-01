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

package org.openflexo.technologyadapter.pdf.model;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoServiceManager;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.test.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.technologyadapter.pdf.PDFTechnologyAdapter;
import org.openflexo.technologyadapter.pdf.rm.PDFDocumentResource;

public abstract class AbstractTestPDF extends OpenflexoProjectAtRunTimeTestCase {
	protected static final Logger logger = Logger.getLogger(AbstractTestPDF.class.getPackage().getName());

	/**
	 * Instantiate a default {@link FlexoServiceManager} well suited for PDF
	 * test purpose<br>
	 * 
	 * @param taClasses
	 * @return a newly created {@link FlexoServiceManager}
	 */
	protected static FlexoServiceManager instanciateTestServiceManagerForPDF() {
		serviceManager = instanciateTestServiceManager();
		PDFTechnologyAdapter pdfTA = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(PDFTechnologyAdapter.class);
		serviceManager.activateTechnologyAdapter(pdfTA);
		return serviceManager;
	}

	protected PDFDocumentResource getDocumentResource(String documentName) {

		FlexoResourceCenter<?> resourceCenter = serviceManager.getResourceCenterService()
				.getFlexoResourceCenter("http://openflexo.org/pdf-test");

		/*
		 * for (FlexoResourceCenter<?> rc :
		 * serviceManager.getResourceCenterService().getResourceCenters()) {
		 * System.out.println("> " + rc.getDefaultBaseURI()); }
		 */

		System.out.println("resourceCenter=" + resourceCenter);

		String documentURI = resourceCenter.getDefaultBaseURI() + "/" + "TestResourceCenter" + "/" + "PDF" + "/"
				+ documentName;
		System.out.println("Searching " + documentURI);

		PDFDocumentResource documentResource = (PDFDocumentResource) serviceManager.getResourceManager()
				.getResource(documentURI, null, PDFDocument.class);

		if (documentResource == null) {
			logger.warning("Cannot find document resource " + documentURI);
			for (FlexoResource<?> r : serviceManager.getResourceManager().getRegisteredResources()) {
				System.out.println("> " + r.getURI());
			}
		}

		assertNotNull(documentResource);

		return documentResource;

	}

	protected PDFDocument getDocument(String documentName) {

		PDFDocumentResource documentResource = getDocumentResource(documentName);
		assertNotNull(documentResource);

		PDFDocument document = null;
		try {
			document = documentResource.getResourceData(null);
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
		assertNotNull(document);
		assertNotNull(document.getPDDocument());

		return document;
	}

}
