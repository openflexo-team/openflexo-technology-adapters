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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.technologyadapter.pdf.PDFTechnologyAdapter;
import org.openflexo.technologyadapter.pdf.rm.PDFDocumentRepository;
import org.openflexo.technologyadapter.pdf.rm.PDFDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestLoadPDFDocuments extends AbstractTestPDF {
	protected static final Logger logger = Logger.getLogger(TestLoadPDFDocuments.class.getPackage().getName());

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManagerForPDF();
	}

	/*@Test
	@TestOrder(2)
	public void testCreateProject() {
		_editor = createProject("TestProject");
		_project = _editor.getProject();
		System.out.println("Created project " + _project.getProjectDirectory());
		assertTrue(_project.getProjectDirectory().exists());
		assertTrue(_project.getProjectDataResource().getFlexoIODelegate().exists());
	}*/

	@Test
	@TestOrder(3)
	public void testPDFLoading() {
		PDFTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(PDFTechnologyAdapter.class);

		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			PDFDocumentRepository docXRepository = resourceCenter.getRepository(PDFDocumentRepository.class, technologicalAdapter);
			assertNotNull(docXRepository);
			Collection<PDFDocumentResource> documents = docXRepository.getAllResources();
			for (PDFDocumentResource docResource : documents) {
				/*try {
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
				System.out.println("URI of document: " + docResource.getURI());*/
				System.out.println("> docResource=" + docResource + " uri=" + docResource.getURI());
			}
		}
	}

	private void testDocumentLoading(String pdfName, int expectedTextBoxesNb, int expectedImageBoxesNb) {

		PDFDocument doc = getDocument("PDF/" + pdfName);
		System.out.println(pdfName + doc);

		assertEquals(1, doc.getPages().size());
		PDFDocumentPage p = doc.getPages().get(0);

		/*for (TextBox tb : p.getTextBoxes()) {
			System.out.println("* tb=" + tb.toString());
		}*/
		assertEquals(expectedTextBoxesNb, p.getTextBoxes().size());

		assertEquals(expectedImageBoxesNb, p.getImageBoxes().size());

		doc.getResource().unloadResourceData(false);
	}

	@Test
	@TestOrder(4)
	public void testDocumentLoading1() {
		testDocumentLoading("EH200142_SHOCK_3L.pdf", 10, 1);
	}

	@Test
	@TestOrder(5)
	public void testDocumentLoading2() {
		testDocumentLoading("00213972L-LangetteMiniFlotteur.pdf", 1, 0);
	}

	@Test
	@TestOrder(6)
	public void testDocumentLoading3() {
		testDocumentLoading("00213972LT_box_BlueTech_FR.pdf", 299, 0);
	}

	@Test
	@TestOrder(7)
	public void testDocumentLoading4() {
		testDocumentLoading("00213972M-Manchon-mini_flotteur_BlueTech.pdf", 163, 0);
	}

	@Test
	@TestOrder(8)
	public void testDocumentLoading5() {
		testDocumentLoading("00213972P_box_BlueTech_debord_FR.pdf", 26, 0);
	}

	@Test
	@TestOrder(9)
	public void testDocumentLoading6() {
		testDocumentLoading("00213972S-secuMiniFlotteur.pdf", 37, 0);
	}

	@Test
	@TestOrder(10)
	public void testDocumentLoading7() {
		testDocumentLoading("00219957M-Manchon-mini_flotteurBr_BlueTech.pdf", 171, 0);
	}

	@Test
	@TestOrder(11)
	public void testDocumentLoading8() {
		testDocumentLoading("00219957P-boxBr_BlueTech_debord_FR.pdf", 20, 0);
	}

	@Test
	@TestOrder(12)
	public void testDocumentLoading9() {
		testDocumentLoading("00219957S-secu-mini_flotteurBr-BlueTechFR.pdf", 165, 0);
	}

	@Test
	@TestOrder(13)
	public void testDocumentLoading10() {
		testDocumentLoading("00219957T-boxBr_BlueTech_FR.pdf", 306, 0);
	}

}
