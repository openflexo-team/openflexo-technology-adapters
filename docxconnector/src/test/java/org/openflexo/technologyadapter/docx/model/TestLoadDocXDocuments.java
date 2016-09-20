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
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestLoadDocXDocuments extends AbstractTestDocX {
	protected static final Logger logger = Logger.getLogger(TestLoadDocXDocuments.class.getPackage().getName());

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManagerForDocX(IdentifierManagementStrategy.ParaId);
	}

	@Test
	@TestOrder(2)
	public void testCreateProject() {
		_editor = createProject("TestProject");
		_project = _editor.getProject();
		System.out.println("Created project " + _project.getProjectDirectory());
		assertTrue(_project.getProjectDirectory().exists());
		assertTrue(_project.getProjectDataResource().getFlexoIODelegate().exists());
	}

	@Test
	@TestOrder(3)
	public void testDocXLoading() {
		DocXTechnologyAdapter technologicalAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(DocXTechnologyAdapter.class);

		for (FlexoResourceCenter<?> resourceCenter : serviceManager.getResourceCenterService().getResourceCenters()) {
			DocXDocumentRepository docXRepository = technologicalAdapter.getDocXDocumentRepository(resourceCenter);
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
		
		for (FlexoDocElement<?, ?> element : simpleDocument.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) element;
				System.out.println("* Paragraph " + paragraph.getP().getParaId() + " " + paragraph.getP() + " "
						+ (paragraph.getP().getPPr() != null && paragraph.getP().getPPr().getPStyle() != null
								? "[" + paragraph.getP().getPPr().getPStyle().getVal() + "]" : "[no style]"));
			} else {
				System.out.println("* Element " + element);
			}
		}*/

		assertEquals(12, simpleDocument.getElements().size());

		DocXParagraph titleParagraph = (DocXParagraph) simpleDocument.getElements().get(0);

	}

	@Test
	@TestOrder(5)
	public void testStructuredDocumentLoading() {

		DocXDocument structuredDocument = getDocument("StructuredDocument.docx");

		System.out.println("StructuredDocument.docx:\n" + structuredDocument.debugStructuredContents());

		assertEquals(13, structuredDocument.getElements().size());

		/*System.out.println("Elements: " + structuredDocument.getElements().size());
		
		
		for (FlexoDocElement<?, ?> element : structuredDocument.getElements()) {
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

		assertEquals(12, structuredDocument.getStyles().size());
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

		assertEquals("This is a paragraph with ", ((DocXTextRun) paragraph7.getRuns().get(0)).getText());
		assertEquals("a", ((DocXTextRun) paragraph7.getRuns().get(1)).getText());
		assertEquals(" ", ((DocXTextRun) paragraph7.getRuns().get(2)).getText());
		assertEquals("italic", ((DocXTextRun) paragraph7.getRuns().get(3)).getText());
		assertEquals(" word.", ((DocXTextRun) paragraph7.getRuns().get(4)).getText());
	}

	@Test
	@TestOrder(6)
	public void testDocumentWithTableLoading() {

		DocXDocument documentWithTable = getDocument("DocumentWithTable.docx");

		System.out.println("DocumentWithTable.docx:\n" + documentWithTable.debugStructuredContents());

		assertEquals(7, documentWithTable.getElements().size());

		DocXParagraph titleParagraph = (DocXParagraph) documentWithTable.getElements().get(0);
		DocXParagraph sectionParagraph = (DocXParagraph) documentWithTable.getElements().get(2);
		DocXParagraph p1 = (DocXParagraph) documentWithTable.getElements().get(3);
		DocXParagraph p2 = (DocXParagraph) documentWithTable.getElements().get(4);
		DocXTable table = (DocXTable) documentWithTable.getElements().get(5);
		DocXParagraph p3 = (DocXParagraph) documentWithTable.getElements().get(6);

		assertSameList(sectionParagraph.getChildrenElements(), p1, p2, table, p3);

		assertEquals(3, table.getTableRows().size());
		DocXTableRow header = (DocXTableRow) table.getTableRows().get(0);
		DocXTableRow row1 = (DocXTableRow) table.getTableRows().get(1);
		DocXTableRow row2 = (DocXTableRow) table.getTableRows().get(2);

		assertEquals(4, header.getTableCells().size());
		assertEquals(4, row1.getTableCells().size());
		assertEquals(4, row2.getTableCells().size());

		DocXTableCell cell11 = (DocXTableCell) header.getTableCells().get(0);
		DocXTableCell cell12 = (DocXTableCell) header.getTableCells().get(1);
		DocXTableCell cell13 = (DocXTableCell) header.getTableCells().get(2);
		DocXTableCell cell14 = (DocXTableCell) header.getTableCells().get(3);

		DocXTableCell cell21 = (DocXTableCell) row1.getTableCells().get(0);
		DocXTableCell cell22 = (DocXTableCell) row1.getTableCells().get(1);
		DocXTableCell cell23 = (DocXTableCell) row1.getTableCells().get(2);
		DocXTableCell cell24 = (DocXTableCell) row1.getTableCells().get(3);

		DocXTableCell cell31 = (DocXTableCell) row2.getTableCells().get(0);
		DocXTableCell cell32 = (DocXTableCell) row2.getTableCells().get(1);
		DocXTableCell cell33 = (DocXTableCell) row2.getTableCells().get(2);
		DocXTableCell cell34 = (DocXTableCell) row2.getTableCells().get(3);

		assertEquals(1, cell11.getParagraphs().size());
		assertEquals(1, cell12.getParagraphs().size());
		assertEquals(1, cell13.getParagraphs().size());
		assertEquals(1, cell14.getParagraphs().size());

		assertEquals(1, cell21.getParagraphs().size());
		assertEquals(1, cell22.getParagraphs().size());
		assertEquals(1, cell23.getParagraphs().size());
		assertEquals(1, cell24.getParagraphs().size());

		assertEquals(1, cell31.getParagraphs().size());
		assertEquals(1, cell32.getParagraphs().size());
		assertEquals(1, cell33.getParagraphs().size());
		assertEquals(1, cell34.getParagraphs().size());

		assertEquals("", ((DocXParagraph) cell11.getParagraphs().get(0)).getRawText());
		assertEquals("Column1", ((DocXParagraph) cell12.getParagraphs().get(0)).getRawText());
		assertEquals("Column2", ((DocXParagraph) cell13.getParagraphs().get(0)).getRawText());
		assertEquals("Column3", ((DocXParagraph) cell14.getParagraphs().get(0)).getRawText());

		assertEquals("Item1", ((DocXParagraph) cell21.getParagraphs().get(0)).getRawText());
		assertEquals("First item", ((DocXParagraph) cell22.getParagraphs().get(0)).getRawText());
		assertEquals("A description for the first item", ((DocXParagraph) cell23.getParagraphs().get(0)).getRawText());
		assertEquals("Data1", ((DocXParagraph) cell24.getParagraphs().get(0)).getRawText());

		assertEquals("Item2", ((DocXParagraph) cell31.getParagraphs().get(0)).getRawText());
		assertEquals("Second item", ((DocXParagraph) cell32.getParagraphs().get(0)).getRawText());
		assertEquals("A description for the second item", ((DocXParagraph) cell33.getParagraphs().get(0)).getRawText());
		assertEquals("Data2", ((DocXParagraph) cell34.getParagraphs().get(0)).getRawText());
	}

	@Test
	@TestOrder(7)
	public void testDocumentWithImageLoading() {

		DocXDocument documentWithImage = getDocument("DocumentWithImage.docx");

		System.out.println("DocumentWithImage.docx:\n" + documentWithImage.debugStructuredContents());

		/*System.out.println("Elements: " + documentWithImage.getElements().size());
		
		for (FlexoDocElement<?, ?> element : documentWithImage.getElements()) {
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
	@TestOrder(8)
	public void testExampleReportLoading() {

		DocXDocument exampleReport = getDocument("ExampleReport.docx");

		System.out.println("ExampleReport.docx:\n" + exampleReport.debugStructuredContents());

		assertEquals(exampleReport.getElements().size(),
				exampleReport.getWordprocessingMLPackage().getMainDocumentPart().getContent().size());

		/*System.out.println("Elements: " + documentWithImage.getElements().size());
		
		for (FlexoDocElement<?, ?> element : documentWithImage.getElements()) {
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
