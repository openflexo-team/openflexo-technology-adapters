package org.openflexo.technologyadapter.docx;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.openflexo.OpenflexoTestCaseWithGUI;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;

public abstract class AbstractTestDocX extends OpenflexoTestCaseWithGUI {
	protected static final Logger logger = Logger.getLogger(AbstractTestDocX.class.getPackage().getName());

	protected DocXDocumentResource getDocumentResource(String documentName) {

		String documentURI = resourceCenter.getDefaultBaseURI() + "TestResourceCenter" + File.separator + documentName;
		System.out.println("Searching " + documentURI);

		DocXDocumentResource documentResource = (DocXDocumentResource) serviceManager.getResourceManager().getResource(documentURI, null,
				DocXDocument.class);

		if (documentResource == null) {
			System.out.println("Cannot find: " + documentURI);
			for (FlexoResource r : resourceCenter.getAllResources()) {
				System.out.println(" > " + r.getURI());
			}
		}
		assertNotNull(documentResource);

		return documentResource;

	}

	protected DocXDocument getDocument(String documentName) {

		DocXDocumentResource documentResource = getDocumentResource(documentName);
		assertNotNull(documentResource);

		DocXDocument document = null;
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
		assertNotNull(document.getWordprocessingMLPackage());

		return document;
	}

}
