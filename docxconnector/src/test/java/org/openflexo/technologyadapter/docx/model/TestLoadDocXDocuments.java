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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestLoadDocXDocuments extends AbstractTestDocX {
	protected static final Logger logger = Logger.getLogger(TestLoadDocXDocuments.class.getPackage().getName());

	private static FlexoEditor editor;
	private static FlexoProject project;

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager();
	}

	@Test
	@TestOrder(2)
	public void testCreateProject() {
		editor = createProject("TestProject");
		project = editor.getProject();
		System.out.println("Created project " + project.getProjectDirectory());
		assertTrue(project.getProjectDirectory().exists());
		assertTrue(project.getProjectDataResource().getFlexoIODelegate().exists());
	}

	@Test
	@TestOrder(3)
	public void testDocXLoading() {
		DocXTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(DocXTechnologyAdapter.class);

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
	}

	@Test
	@TestOrder(4)
	public void testSimpleDocumentLoading() {

		DocXDocument simpleDocument = getDocument("SimpleDocument.docx");

		System.out.println("SimpleDocument.docx:\n" + simpleDocument.debugStructuredContents());

		/*System.out.println("Elements: " + simpleDocument.getElements().size());
		
		for (FlexoDocumentElement<?, ?> element : simpleDocument.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) element;
				System.out.println("* Paragraph " + paragraph.getP().getParaId() + " " + paragraph.getP() + " "
						+ (paragraph.getP().getPPr() != null && paragraph.getP().getPPr().getPStyle() != null
								? "[" + paragraph.getP().getPPr().getPStyle().getVal() + "]" : "[no style]"));
			} else {
				System.out.println("* Element " + element);
			}
		}*/

		assertEquals(11, simpleDocument.getElements().size());

		DocXParagraph titleParagraph = (DocXParagraph) simpleDocument.getElements().get(0);

	}

	@Test
	@TestOrder(5)
	public void testStructuredDocumentLoading() {

		DocXDocument structuredDocument = getDocument("StructuredDocument.docx");

		System.out.println("StructuredDocument.docx:\n" + structuredDocument.debugStructuredContents());

		assertEquals(13, structuredDocument.getElements().size());

		/*System.out.println("Elements: " + structuredDocument.getElements().size());
		
		
		for (FlexoDocumentElement<?, ?> element : structuredDocument.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) element;
				System.out.println("* Paragraph " + paragraph.getP().getParaId() + " " + paragraph.getP() + " "
						+ (paragraph.getP().getPPr() != null ? "[" + paragraph.getP().getPPr().getPStyle().getVal() + "]" : "[no style]"));
			} else {
				System.out.println("* Element " + element);
			}
		}*/
		// System.out.println("contents:\n" + structuredDocument.debugContents());
		// System.out.println("structure:\n" + structuredDocument.debugStructuredContents());
		// System.out.println("Used styles: " + structuredDocument.getStyles());

		assertEquals(11, structuredDocument.getStyles().size());
		DocXStyle docDefaults = (DocXStyle) structuredDocument.getStyleByName("DocDefaults");
		assertNotNull(docDefaults);
		assertNull(docDefaults.getParentStyle());
		DocXStyle normal = (DocXStyle) structuredDocument.getStyleByName("Normal");
		assertNotNull(normal);
		assertSame(docDefaults, normal.getParentStyle());
		DocXStyle title = (DocXStyle) structuredDocument.getStyleByName("Title");
		assertNotNull(title);
		assertSame(normal, title.getParentStyle());
		DocXStyle heading1 = (DocXStyle) structuredDocument.getStyleByName("heading 1");
		assertNotNull(heading1);
		assertSame(normal, heading1.getParentStyle());
		DocXStyle heading2 = (DocXStyle) structuredDocument.getStyleByName("heading 2");
		assertNotNull(heading2);
		assertSame(normal, heading2.getParentStyle());

		DocXParagraph titleParagraph = (DocXParagraph) structuredDocument.getElements().get(0);
		assertSame(title, titleParagraph.getStyle());

		DocXParagraph section1Paragraph = (DocXParagraph) structuredDocument.getElements().get(1);
		assertSame(heading1, section1Paragraph.getStyle());

		DocXParagraph paragraph1 = (DocXParagraph) structuredDocument.getElements().get(2);
		assertNull(paragraph1.getStyle());

		DocXParagraph subSection1Paragraph = (DocXParagraph) structuredDocument.getElements().get(3);
		assertSame(heading2, subSection1Paragraph.getStyle());

		DocXParagraph paragraph2 = (DocXParagraph) structuredDocument.getElements().get(4);
		assertNull(paragraph2.getStyle());

		DocXParagraph subSection2Paragraph = (DocXParagraph) structuredDocument.getElements().get(5);
		assertSame(heading2, subSection2Paragraph.getStyle());

		DocXParagraph paragraph3 = (DocXParagraph) structuredDocument.getElements().get(6);
		assertNull(paragraph3.getStyle());

		DocXParagraph section2Paragraph = (DocXParagraph) structuredDocument.getElements().get(7);
		assertSame(heading1, section2Paragraph.getStyle());

		DocXParagraph paragraph4 = (DocXParagraph) structuredDocument.getElements().get(8);
		assertNull(paragraph4.getStyle());
		DocXParagraph paragraph5 = (DocXParagraph) structuredDocument.getElements().get(9);
		assertNull(paragraph5.getStyle());
		DocXParagraph paragraph6 = (DocXParagraph) structuredDocument.getElements().get(10);
		assertNull(paragraph6.getStyle());
		DocXParagraph paragraph7 = (DocXParagraph) structuredDocument.getElements().get(11);
		assertNull(paragraph7.getStyle());
		DocXParagraph paragraph8 = (DocXParagraph) structuredDocument.getElements().get(12);
		assertNull(paragraph8.getStyle());

		assertSameList(structuredDocument.getRootElements(), titleParagraph, section1Paragraph, section2Paragraph);

		assertSameList(structuredDocument.getRootElements(), titleParagraph, section1Paragraph, section2Paragraph);
		assertSameList(section1Paragraph.getChildrenElements(), paragraph1, subSection1Paragraph, subSection2Paragraph);
		assertSameList(subSection1Paragraph.getChildrenElements(), paragraph2);
		assertSameList(subSection2Paragraph.getChildrenElements(), paragraph3);
		assertSameList(section2Paragraph.getChildrenElements(), paragraph4, paragraph5, paragraph6, paragraph7, paragraph8);

		assertEquals(5, paragraph7.getRuns().size());

		assertEquals("This is a paragraph with ", paragraph7.getRuns().get(0).getText());
		assertEquals("a", paragraph7.getRuns().get(1).getText());
		assertEquals(" ", paragraph7.getRuns().get(2).getText());
		assertEquals("italic", paragraph7.getRuns().get(3).getText());
		assertEquals(" word.", paragraph7.getRuns().get(4).getText());
	}

	@Test
	@TestOrder(6)
	public void testDocumentWithTableLoading() {

		DocXDocument documentWithTable = getDocument("DocumentWithTable.docx");

		System.out.println("DocumentWithTable.docx:\n" + documentWithTable.debugStructuredContents());

		/*System.out.println("Elements: " + documentWithTable.getElements().size());
		
		for (FlexoDocumentElement<?, ?> element : documentWithTable.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) element;
				System.out.println("* Paragraph " + paragraph.getP().getParaId() + " " + paragraph.getP() + " "
						+ (paragraph.getP().getPPr() != null ? "[" + paragraph.getP().getPPr().getPStyle().getVal() + "]" : "[no style]"));
			} else {
				System.out.println("* Element " + element);
			}
		}*/

	}

	@Test
	@TestOrder(7)
	public void testDocumentWithImageLoading() {

		DocXDocument documentWithImage = getDocument("DocumentWithImage.docx");

		System.out.println("DocumentWithImage.docx:\n" + documentWithImage.debugStructuredContents());

		/*System.out.println("Elements: " + documentWithImage.getElements().size());
		
		for (FlexoDocumentElement<?, ?> element : documentWithImage.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) element;
				System.out.println("* Paragraph " + paragraph.getP().getParaId() + " " + paragraph.getP() + " "
						+ (paragraph.getP().getPPr() != null ? "[" + paragraph.getP().getPPr().getPStyle().getVal() + "]" : "[no style]"));
			} else {
				System.out.println("* Element " + element);
			}
		}*/

	}

}
