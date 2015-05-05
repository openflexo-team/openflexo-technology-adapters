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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.logging.Logger;

import org.docx4j.model.styles.StyleTree.AugmentedStyle;
import org.docx4j.model.styles.Tree;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.OpenflexoProjectAtRunTimeTestCase;
import org.openflexo.foundation.doc.FlexoDocumentElement;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestLoadDocXDocuments extends OpenflexoProjectAtRunTimeTestCase {
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
	}

	private DocXDocument getDocument(String documentName) {
		String documentURI = resourceCenter.getDefaultBaseURI() + "TestResourceCenter" + File.separator + documentName;
		System.out.println("Searching " + documentURI);

		FlexoResource<DocXDocument> documentResource = serviceManager.getInformationSpace().getResource(documentURI, null,
				DocXDocument.class);
		assertNotNull(documentResource);

		DocXDocument document = documentResource.getLoadedResourceData();
		assertNotNull(document);
		assertNotNull(document.getWordprocessingMLPackage());

		return document;
	}

	@Test
	@TestOrder(4)
	public void testSimpleDocumentLoading() {

		DocXDocument simpleDocument = getDocument("SimpleDocument.docx");

		// System.out.println(documentWithImage.debugContents());

		System.out.println("Elements: " + simpleDocument.getElements().size());

		for (FlexoDocumentElement<?, ?> element : simpleDocument.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) element;
				System.out.println("* Paragraph "
						+ paragraph.getP().getParaId()
						+ " "
						+ paragraph.getP()
						+ " "
						+ (paragraph.getP().getPPr() != null && paragraph.getP().getPPr().getPStyle() != null ? "["
								+ paragraph.getP().getPPr().getPStyle().getVal() + "]" : "[no style]"));
			} else {
				System.out.println("* Element " + element);
			}
		}

		assertEquals(11, simpleDocument.getElements().size());

		DocXParagraph titleParagraph = (DocXParagraph) simpleDocument.getElements().get(0);

	}

	@Test
	@TestOrder(5)
	public void testStructuredDocumentLoading() {

		DocXDocument structuredDocument = getDocument("StructuredDocument.docx");

		// System.out.println(documentWithImage.debugContents());

		System.out.println("Elements: " + structuredDocument.getElements().size());

		for (FlexoDocumentElement<?, ?> element : structuredDocument.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph) element;
				System.out.println("* Paragraph " + paragraph.getP().getParaId() + " " + paragraph.getP() + " "
						+ (paragraph.getP().getPPr() != null ? "[" + paragraph.getP().getPPr().getPStyle().getVal() + "]" : "[no style]"));
			} else {
				System.out.println("* Element " + element);
			}
		}

		System.out.println("Tous les styles utilises: "
				+ structuredDocument.getWordprocessingMLPackage().getMainDocumentPart().getStylesInUse());

		System.out.println("Les styles: "
				+ structuredDocument.getWordprocessingMLPackage().getMainDocumentPart().getStyleTree().getParagraphStylesTree());

		Tree<AugmentedStyle> tree = structuredDocument.getWordprocessingMLPackage().getMainDocumentPart().getStyleTree()
				.getParagraphStylesTree();

		System.out.println("root="
				+ structuredDocument.getWordprocessingMLPackage().getMainDocumentPart().getStyleTree().getParagraphStylesTree()
						.getRootElement().data.getStyle());

	}
	/*@Test
	@TestOrder(6)
	public void testDocumentWithTableLoading() {

		DocXDocument documentWithTable = getDocument("DocumentWithTable.docx");

		// System.out.println(documentWithImage.debugContents());

		System.out.println("Elements: " + documentWithTable.getElements().size());

		for (FlexoDocumentElement<?, ?> element : documentWithTable.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph)element;
				System.out.println("* Paragraph " + paragraph.getP().getParaId() + " " + paragraph.getP() + " "
						+ (paragraph.getP().getPPr() != null ? "[" + paragraph.getP().getPPr().getPStyle().getVal() + "]" : "[no style]"));
			}
			else {
				System.out.println("* Element " + element);
			}
		}

	}

	@Test
	@TestOrder(7)
	public void testDocumentWithImageLoading() {

		DocXDocument documentWithImage = getDocument("DocumentWithImage.docx");

		// System.out.println(documentWithImage.debugContents());

		System.out.println("Elements: " + documentWithImage.getElements().size());

		for (FlexoDocumentElement<?, ?> element : documentWithImage.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph paragraph = (DocXParagraph)element;
				System.out.println("* Paragraph " + paragraph.getP().getParaId() + " " + paragraph.getP() + " "
						+ (paragraph.getP().getPPr() != null ? "[" + paragraph.getP().getPPr().getPStyle().getVal() + "]" : "[no style]"));
			}
			else {
				System.out.println("* Element " + element);
			}
		}

	}*/

}
