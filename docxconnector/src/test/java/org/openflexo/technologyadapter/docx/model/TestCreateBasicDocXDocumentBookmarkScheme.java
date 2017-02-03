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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.resource.SaveResourceException;
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
public class TestCreateBasicDocXDocumentBookmarkScheme extends AbstractTestDocX {
	protected static final Logger logger = Logger
			.getLogger(TestCreateBasicDocXDocumentBookmarkScheme.class.getPackage().getName());

	private static DocXTechnologyAdapter technologicalAdapter;

	private static DocXDocumentResource newDocResource = null;
	private static DocXDocument newDocument = null;

	private static DirectoryResourceCenter newResourceCenter;

	@AfterClass
	public static void tearDownClass() {

		unloadAndDelete(newDocument);
		newDocument = null;
		technologicalAdapter = null;
		newDocResource = null;

		AbstractTestDocX.tearDownClass();
	}

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManagerForDocX(IdentifierManagementStrategy.Bookmark);

		newResourceCenter = makeNewDirectoryResourceCenter();
		assertNotNull(newResourceCenter);

	}

	@Test
	@TestOrder(2)
	public void testEmptyDocXCreation()
			throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("testEmptyDocXCreation");

		technologicalAdapter = serviceManager.getTechnologyAdapterService()
				.getTechnologyAdapter(DocXTechnologyAdapter.class);

		newDocResource = technologicalAdapter.createNewDocXDocumentResource(newResourceCenter, "DocX",
				"TestBasicDocument.docx", true, technologicalAdapter.getDefaultIDStrategy());
		newDocument = null;

		System.out.println("uri=" + newDocResource.getURI());
		System.out.println("newDocResource=" + newDocResource);

		assertNotNull(newDocResource);
		assertEquals("http://openflexo.org/test/TestResourceCenter/DocX/TestBasicDocument.docx",
				newDocResource.getURI());

		assertNotNull(newDocument = newDocResource.getResourceData(null));

		System.out.println("Styles=" + newDocument.getStyles());

		assertEquals(23, newDocument.getStyles().size());

		DocXStyle docDefaultsStyle = (DocXStyle) newDocument.getStyleByIdentifier("DocDefaults");
		DocXStyle normalStyle = (DocXStyle) newDocument.getStyleByIdentifier("Normal");
		DocXStyle heading1 = (DocXStyle) newDocument.getStyleByIdentifier("Heading1");

		// System.out.println("docDefaultsStyle=" + docDefaultsStyle + " of " +
		// docDefaultsStyle.getClass());
		// System.out.println("normalStyle=" + normalStyle + " of " +
		// normalStyle.getClass());
		// System.out.println("heading1=" + heading1 + " of " +
		// heading1.getClass());

		assertEquals(4, newDocument.getStructuringStyles().size());
		assertFalse(docDefaultsStyle.isLevelled());
		assertFalse(normalStyle.isLevelled());
		assertTrue(heading1.isLevelled());

		// System.out.println("style=" + normalStyle.getStyle());
		// System.out.println("based on=" +
		// normalStyle.getStyle().getBasedOn());

		// TODO Check why it fails with gradle
		// assertEquals(docDefaultsStyle, normalStyle.getParentStyle());

		assertEquals(normalStyle, heading1.getParentStyle());

		System.out.println(newDocument.debugStructuredContents());

		assertEquals(0, newDocument.getRootElements().size());

		newDocResource.save(null);

		assertFalse(newDocResource.isModified());
	}

	@Test
	@TestOrder(3)
	public void testUseSomeStyles() {

		log("testUseSomeStyles");

		DocXStyle normalWebStyle;

		System.out.println("Styles=" + newDocument.getStyles());
		System.out.println("Known styles=" + newDocument.getKnownStyleIds());
		assertEquals(23, newDocument.getStyles().size());

		assertNotNull(newDocument.getFactory());

		assertNotNull(normalWebStyle = newDocument.activateStyle("NormalWeb"));

		assertEquals(24, newDocument.getStyles().size());
		assertTrue(newDocument.getStyles().contains(normalWebStyle));

		System.out.println("newDocument.getStructuringStyles()=" + newDocument.getStructuringStyles());

		assertEquals(4, newDocument.getStructuringStyles().size());

		// newDocument.getWordprocessingMLPackage().getMainDocumentPart().addStyledParagraphOfText("Title",
		// "Hello Word!");
		// newDocument.getWordprocessingMLPackage().getMainDocumentPart().addStyledParagraphOfText("Subtitle",
		// "This is a subtitle!");

	}

	@Test
	@TestOrder(4)
	public void testAddSomeParagraphs() throws SaveResourceException {

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

	}

	/**
	 */
	@Test
	@TestOrder(10)
	public void testReloadDocument() throws FileNotFoundException, ResourceLoadingCancelledException, FlexoException {

		log("testReloadDocument()");

		DocXDocument documentBeforeReload = newDocument;
		DocXDocumentResource documentResourceBeforeReload = newDocResource;
		assertNotNull(documentBeforeReload);

		instanciateTestServiceManager(DocXTechnologyAdapter.class);

		serviceManager.getResourceCenterService()
				.addToResourceCenters(newResourceCenter = new DirectoryResourceCenter(testResourceCenterDirectory,
						serviceManager.getResourceCenterService()));
		newResourceCenter.performDirectoryWatchingNow();

		assertNotNull(newDocResource = (DocXDocumentResource) serviceManager.getResourceManager()
				.getResource("http://openflexo.org/test/TestResourceCenter/DocX/TestBasicDocument.docx"));

		newDocument = newDocResource.getDocument();
		assertNotSame(documentBeforeReload, newDocument);

		assertEquals(5, newDocument.getElements().size());
		assertEquals(1, newDocument.getRootElements().size());

		DocXStyle normalStyle = (DocXStyle) newDocument.getStyleByIdentifier("Normal");
		DocXStyle heading1 = (DocXStyle) newDocument.getStyleByIdentifier("Heading1");
		DocXStyle heading2 = (DocXStyle) newDocument.getStyleByIdentifier("Heading2");

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

	}

	/**
	 */
	@Test
	@TestOrder(11)
	public void testChangeStyle() {

		log("testChangeStyle()");

		System.out.println(newDocument.debugStructuredContents());

		DocXStyle normalStyle = (DocXStyle) newDocument.getStyleByIdentifier("Normal");
		DocXStyle heading1 = (DocXStyle) newDocument.getStyleByIdentifier("Heading1");
		DocXStyle heading2 = (DocXStyle) newDocument.getStyleByIdentifier("Heading2");

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

		text1.setStyle(heading2);
		text2.setStyle(heading1);

		System.out.println(newDocument.debugStructuredContents());

		assertEquals(5, newDocument.getElements().size());
		assertEquals(2, newDocument.getRootElements().size());
		assertEquals(3, title.getChildrenElements().size());

	}

}
