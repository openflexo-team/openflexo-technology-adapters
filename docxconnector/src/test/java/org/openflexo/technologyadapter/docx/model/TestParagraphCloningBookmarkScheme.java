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
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.doc.FlexoDocElement;
import org.openflexo.foundation.resource.SaveResourceException;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestParagraphCloningBookmarkScheme extends AbstractTestDocX {
	protected static final Logger logger = Logger.getLogger(TestParagraphCloningBookmarkScheme.class.getPackage().getName());

	private static DocXDocument simpleDocumentWithBookmarks;
	private static DocXParagraph titleParagraph;
	private static DocXParagraph firstParagraph;
	private static DocXParagraph lastParagraph;

	@AfterClass
	public static void tearDownClass() {

		unloadAndDelete(simpleDocumentWithBookmarks);
		simpleDocumentWithBookmarks = null;
		titleParagraph = null;
		firstParagraph = null;
		lastParagraph = null;

		AbstractTestDocX.tearDownClass();
	}

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManagerForDocX(IdentifierManagementStrategy.Bookmark);
	}

	@Test
	@TestOrder(2)
	public void testCreateProject() {
		_editor = createStandaloneProject("TestProject");
		_project = (FlexoProject<File>) _editor.getProject();
		System.out.println("Created project " + _project.getProjectDirectory());
		assertTrue(_project.getProjectDirectory().exists());
	}

	@Test
	@TestOrder(3)
	public void testSimpleDocumentWithBookmarksLoading() throws SaveResourceException {

		simpleDocumentWithBookmarks = getDocument("SimpleDocumentWithBookmarks.docx");

		System.out.println("SimpleDocumentWithBookmarks.docx:\n" + simpleDocumentWithBookmarks.debugStructuredContents());

		System.out.println("Elements: " + simpleDocumentWithBookmarks.getElements().size());

		for (FlexoDocElement<?, ?> element : simpleDocumentWithBookmarks.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) element;
				System.out.println("* Paragraph " + paragraph.getP().getParaId() + " " + paragraph.getP() + " "
						+ (paragraph.getP().getPPr() != null && paragraph.getP().getPPr().getPStyle() != null
								? "[" + paragraph.getP().getPPr().getPStyle().getVal() + "]" : "[no style]"));
			}
			else {
				System.out.println("* Element " + element);
			}
		}

		assertEquals(14, simpleDocumentWithBookmarks.getElements().size());

		titleParagraph = (DocXParagraph) simpleDocumentWithBookmarks.getElements().get(0);
		firstParagraph = (DocXParagraph) simpleDocumentWithBookmarks.getElements().get(2);
		lastParagraph = (DocXParagraph) simpleDocumentWithBookmarks.getElements().get(12);

		// Ids present in document
		assertEquals("19DABA72", firstParagraph.getIdentifier());
		assertEquals("77576D62", lastParagraph.getIdentifier());

		// New generated id
		assertNotNull(titleParagraph.getIdentifier());

		/*
		 * if (simpleDocumentWithBookmarks.isModified()) {
		 * assertTrue(simpleDocumentWithBookmarks.isModified());
		 * simpleDocumentWithBookmarks.getResource().save(null);
		 * assertFalse(simpleDocumentWithBookmarks.isModified()); }
		 */

	}

	/**
	 * @throws SaveResourceException
	 */
	@Test
	@TestOrder(4)
	public void testCloneParagraph() throws SaveResourceException {

		log("testCloneParagraph()");

		DocXParagraph clonedParagraph = (DocXParagraph) firstParagraph.cloneObject();

		System.out.println("text=" + firstParagraph.getRawText());

		assertNotNull(clonedParagraph);
		assertEquals(firstParagraph.getRawText(), clonedParagraph.getRawText());

		System.out.println("Identifier of cloned paragraph=" + clonedParagraph.getIdentifier());
		assertNotNull(clonedParagraph.getIdentifier());
		assertFalse(firstParagraph.getIdentifier().equals(clonedParagraph.getIdentifier()));

		simpleDocumentWithBookmarks.addToElements(clonedParagraph);

		System.out.println("SimpleDocumentWithBookmarks.docx:\n" + simpleDocumentWithBookmarks.debugStructuredContents());

		assertEquals(firstParagraph, simpleDocumentWithBookmarks.getElementWithIdentifier(firstParagraph.getIdentifier()));
		assertEquals(clonedParagraph, simpleDocumentWithBookmarks.getElementWithIdentifier(clonedParagraph.getIdentifier()));

		assertTrue(simpleDocumentWithBookmarks.isModified());

		// simpleDocumentWithBookmarks.getResource().save(null);
		// assertFalse(simpleDocumentWithBookmarks.isModified());

	}

}
