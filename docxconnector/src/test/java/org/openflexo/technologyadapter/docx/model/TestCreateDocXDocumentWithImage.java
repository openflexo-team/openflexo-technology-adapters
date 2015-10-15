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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.junit.AfterClass;
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
public class TestCreateDocXDocumentWithImage extends AbstractTestDocX {
	protected static final Logger logger = Logger.getLogger(TestCreateDocXDocumentWithImage.class.getPackage().getName());

	private static DocXTechnologyAdapter technologicalAdapter;

	private static DocXDocument newDocument = null;
	private static DocXDocumentResource newDocResource;

	@AfterClass
	public static void tearDownClass() {

		technologicalAdapter = null;
		newDocument = null;
		newDocResource = null;

		deleteProject();
		deleteTestResourceCenters();
		unloadServiceManager();
	}

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager();
	}

	@Test
	@TestOrder(2)
	public void testEmptyDocXCreation() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {
		technologicalAdapter = serviceManager.getTechnologyAdapterService().getTechnologyAdapter(DocXTechnologyAdapter.class);

		newDocResource = technologicalAdapter.createNewDocXDocumentResource(resourceCenter, "DocX", "TestDocumentWithTable.docx", true);

		System.out.println("uri=" + newDocResource.getURI());
		System.out.println("newDocResource=" + newDocResource);

		assertNotNull(newDocResource);
		assertEquals("http://openflexo.org/test/TestResourceCenter/DocX/TestDocumentWithTable.docx", newDocResource.getURI());

		assertNotNull(newDocument = newDocResource.getResourceData(null));

		System.out.println(newDocument.debugStructuredContents());

		assertEquals(0, newDocument.getRootElements().size());

		newDocResource.save(null);

		assertFalse(newDocResource.isModified());
	}

	@Test
	@TestOrder(3)
	public void testAddSomeParagraphs() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("testAddSomeParagraphs");

		DocXStyle normalStyle = (DocXStyle) newDocument.getStyleByIdentifier("Normal");
		DocXStyle heading1 = (DocXStyle) newDocument.getStyleByIdentifier("Heading1");
		DocXStyle heading2 = (DocXStyle) newDocument.getStyleByIdentifier("Heading2");

		newDocument.addStyledParagraphOfText(heading1, "Title of document");
		newDocument.addStyledParagraphOfText(heading2, "Subtitle1");
		newDocument.addStyledParagraphOfText(normalStyle, "This is a paragraph");
		newDocument.addStyledParagraphOfText(heading2, "Subtitle2");
		newDocument.addStyledParagraphOfText(normalStyle, "This is an other paragraph");

		System.out.println(newDocument.debugStructuredContents());

		assertEquals(5, newDocument.getElements().size());
		assertEquals(1, newDocument.getRootElements().size());

		DocXParagraph title = (DocXParagraph) newDocument.getElements().get(0);
		DocXParagraph subtitle1 = (DocXParagraph) newDocument.getElements().get(1);
		DocXParagraph text1 = (DocXParagraph) newDocument.getElements().get(2);
		DocXParagraph subtitle2 = (DocXParagraph) newDocument.getElements().get(3);
		DocXParagraph text2 = (DocXParagraph) newDocument.getElements().get(4);

		assertEquals("Title of document", title.getRawText());
		assertEquals(heading1, title.getStyle());
		assertEquals("Subtitle1", subtitle1.getRawText());
		assertEquals(heading2, subtitle1.getStyle());
		assertEquals("This is a paragraph", text1.getRawText());
		assertEquals(normalStyle, text1.getStyle());
		assertEquals("Subtitle2", subtitle2.getRawText());
		assertEquals(heading2, subtitle2.getStyle());
		assertEquals("This is an other paragraph", text2.getRawText());
		assertEquals(normalStyle, text2.getStyle());

		assertTrue(newDocResource.isModified());
		newDocResource.save(null);
		assertFalse(newDocResource.isModified());

		System.out.println(newDocument.debugStructuredContents());

	}

	@Test
	@TestOrder(4)
	public void testAddImage() throws Exception {
		log("testAddImage");

		File imageFile = new File(resourceCenter.getDirectory(), "TestResourceCenter/Images/CarteDeVoeuxOF2015.png");
		assertTrue(imageFile.exists());

		DocXParagraph text2 = (DocXParagraph) newDocument.getElements().get(4);

		text2.addToRuns(newDocument.getFactory().makeDrawingRun(imageFile));

		System.out.println(newDocument.debugStructuredContents());

		newDocResource.save(null);

	}

}
