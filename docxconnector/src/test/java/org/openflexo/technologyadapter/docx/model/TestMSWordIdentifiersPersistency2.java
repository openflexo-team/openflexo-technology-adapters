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

import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestMSWordIdentifiersPersistency2 extends AbstractTestDocX {
	protected static final Logger logger = Logger.getLogger(TestMSWordIdentifiersPersistency2.class.getPackage().getName());

	// TODO This test is incomplete

	@AfterClass
	public static void tearDownClass() {

		AbstractTestDocX.tearDownClass();
	}

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManagerForDocX(IdentifierManagementStrategy.ParaId);
	}
	/*
		@Test
		@TestOrder(3)
		public void testDocXLoading() {
			DocXTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(
					DocXTechnologyAdapter.class);
	
			for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
				DocXDocumentRepository docXRepository = resourceCenter.getRepository(DocXDocumentRepository.class, technologicalAdapter);
				assertNotNull(docXRepository);
				Collection<DocXDocumentResource> documents = docXRepository.getAllResources();
				for (DocXDocumentResource docResource : documents) {
					System.out.println("> Found " + docResource);
					try {
						docResource.loadResourceData(null);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (ResourceLoadingCancelledException e) {
						e.printStackTrace();
					} catch (FlexoException e) {
						e.printStackTrace();
					}
					assertNotNull(docResource.getLoadedResourceData());
					System.out.println("URI of document: " + docResource.getURI());
				}
			}
		}
	
		private static DocXDocument step1;
		private static DocXDocument step2;
	
		@Test
		@TestOrder(4)
		public void testStep1() {
	
			step1 = getDocument("TestActivityReport/ActivityReport1.docx");
	
			System.out.println("ActivityReport1.docx:\n" + step1.debugStructuredContents());
	
			assertEquals(113, step1.getElements().size());
	
			// DocXParagraph titleParagraph = (DocXParagraph) simpleDocument.getElements().get(0);
	
		}
	
		// Same document after a SaveAs in Microsoft Word
		@Test
		@TestOrder(5)
		public void testStep2() {
	
			step2 = getDocument("TestActivityReport/ActivityReport2.docx");
	
			System.out.println("ActivityReport2.docx:\n" + step2.debugStructuredContents());
	
			assertEquals(113, step2.getElements().size());
			assertEquals(step1.getElements().size(), step2.getElements().size());
	
			for (int i = 0; i < step1.getElements().size(); i++) {
				FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element1 = step1.getElements().get(i);
				FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element2 = step2.getElements().get(i);
				// This doesn't work: LibreOffice does not persist paraId !!!!
				// assertEquals(element1.getIdentifier(), element2.getIdentifier());
			}
	
		}*/

}
