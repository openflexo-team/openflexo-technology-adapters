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

package org.openflexo.technologyadapter.docx.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * This test is intented to test blank .docx document creation features
 * 
 * @author sylvain
 *
 */
@RunWith(OrderedRunner.class)
public class TestCreateEmptyDocXDocument extends AbstractTestDocX {
	protected static final Logger logger = Logger.getLogger(TestCreateEmptyDocXDocument.class.getPackage().getName());

	private static DocXTechnologyAdapter technologicalAdapter;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager();
	}

	@Test
	@TestOrder(2)
	public void testEmptyDocXCreation() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class);

		DocXDocumentResource newDocResource = technologicalAdapter.createNewDocXDocumentResource(resourceCenter, "DocX",
				"TestBlankDocument.docx", true);
		DocXDocument newDocument = null;

		System.out.println("uri=" + newDocResource.getURI());
		System.out.println("newDocResource=" + newDocResource);

		assertNotNull(newDocResource);
		assertEquals("http://openflexo.org/test/TestResourceCenter/DocX/TestBlankDocument.docx", newDocResource.getURI());

		assertNotNull(newDocument = newDocResource.getResourceData(null));

		System.out.println(newDocument.debugStructuredContents());

		assertEquals(0, newDocument.getRootElements().size());

		newDocResource.save(null);

		assertFalse(newDocResource.isModified());
	}

}
