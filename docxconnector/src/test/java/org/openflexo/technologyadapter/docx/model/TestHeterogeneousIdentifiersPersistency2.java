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

import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestHeterogeneousIdentifiersPersistency2 extends AbstractTestDocX {
	protected static final Logger logger = Logger.getLogger(TestHeterogeneousIdentifiersPersistency2.class.getPackage().getName());

	private static DocXDocument step0;
	private static DocXDocument step1;
	private static DocXDocument step2;
	private static DocXDocument step3;
	private static DocXDocument step4;

	@AfterClass
	public static void tearDownClass() {

		step0 = null;
		step1 = null;
		step2 = null;
		step3 = null;
		step4 = null;

		deleteProject();
		deleteTestResourceCenters();
		unloadServiceManager();
	}

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager(DocXTechnologyAdapter.class);
	}

	/*@Test
	@TestOrder(3)
	public void testDocXLoading() {
		DocXTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(
				DocXTechnologyAdapter.class);
	
		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			DocXDocumentRepository docXRepository = resourceCenter.getRepository(DocXDocumentRepository.class, technologicalAdapter);
			assertNotNull(docXRepository);
			Collection<DocXDocumentResource> documents = docXRepository.getAllResources();
			for (DocXDocumentResource docResource : documents) {
				try {
					docResource.loadResourceData(null);
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
				assertNotNull(docResource.getLoadedResourceData());
				System.out.println("URI of document: " + docResource.getURI());
			}
		}
	}*/

	@Test
	@TestOrder(4)
	public void testStep0() {

		step0 = getDocument("HeterogeneousDocumentEdition2/Step1-MSWord.docx");

		System.out.println("Step1-MSWord.docx:\n" + step0.debugStructuredContents());

		assertEquals(13, step0.getElements().size());

		// DocXParagraph titleParagraph = (DocXParagraph) simpleDocument.getElements().get(0);

	}

	// After modification of document.xml + rezip
	@Test
	@TestOrder(5)
	public void testStep1() {

		step1 = getDocument("HeterogeneousDocumentEdition2/Step1-alter-MSWord.docx");

		System.out.println("Step1-MSWord.docx:\n" + step1.debugStructuredContents());

		assertEquals(13, step1.getElements().size());

		assertEquals(step1.getElements().size(), step0.getElements().size());

		for (int i = 0; i < step0.getElements().size(); i++) {
			FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element1 = step0.getElements().get(i);
			FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element2 = step1.getElements().get(i);
			assertEquals(element1.getIdentifier(), element2.getIdentifier());
		}

	}

	// Same document after a SaveAs in Microsoft Word
	@Test
	@TestOrder(6)
	public void testStep2() {

		step2 = getDocument("HeterogeneousDocumentEdition2/Step2-alter-MSWord.docx");

		System.out.println("Step2-alter-MSWord.docx:\n" + step2.debugStructuredContents());

		assertEquals(13, step2.getElements().size());
		assertEquals(step1.getElements().size(), step2.getElements().size());

		for (int i = 0; i < step1.getElements().size(); i++) {
			FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element1 = step1.getElements().get(i);
			FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element2 = step2.getElements().get(i);
			assertEquals(element1.getIdentifier(), element2.getIdentifier());
		}

	}

	// Same document after a SaveAs in LibreOffice
	@Test
	@TestOrder(7)
	public void testStep3() {

		step3 = getDocument("HeterogeneousDocumentEdition2/Step2-alter-LibreOffice.docx");

		System.out.println("Step2-alter-LibreOffice.docx:\n" + step3.debugStructuredContents());

		assertEquals(13, step3.getElements().size());
		assertEquals(step1.getElements().size(), step3.getElements().size());

		for (int i = 0; i < step1.getElements().size(); i++) {
			FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element1 = step1.getElements().get(i);
			FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element2 = step2.getElements().get(i);
			FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element3 = step3.getElements().get(i);
			System.out.println("Step2-alter-LibreOffice.docx " + i + "  " + element1.getIdentifier() + " -- " + element2.getIdentifier()
					+ " -- " + element3.getIdentifier() + " -- ");
			assertEquals(element1.getIdentifier(), element2.getIdentifier());
			// does not work !!!
			// assertEquals(element1.getIdentifier(), element3.getIdentifier());
			// assertEquals(element2.getIdentifier(), element3.getIdentifier());
		}

	}

	// Same document after small modif + a SaveAs in Microsoft Word
	@Test
	@TestOrder(8)
	public void testStep4() {

		step4 = getDocument("HeterogeneousDocumentEdition2/Step3-alter-MSWord.docx");

		System.out.println("Step2-alter-MSWord.docx:\n" + step4.debugStructuredContents());

		assertEquals(13, step4.getElements().size());
		assertEquals(step1.getElements().size(), step4.getElements().size());

		for (int i = 0; i < step1.getElements().size(); i++) {
			FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element1 = step1.getElements().get(i);
			FlexoDocElement<DocXDocument, DocXTechnologyAdapter> element2 = step4.getElements().get(i);
			assertEquals(element1.getIdentifier(), element2.getIdentifier());
		}

	}
}
