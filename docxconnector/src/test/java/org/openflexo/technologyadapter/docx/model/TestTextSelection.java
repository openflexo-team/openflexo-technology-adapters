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

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.doc.FlexoDocumentFragment.FragmentConsistencyException;
import org.openflexo.foundation.doc.FlexoRun;
import org.openflexo.foundation.doc.TextSelection;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentRepository;
import org.openflexo.technologyadapter.docx.rm.DocXDocumentResource;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;
import org.openflexo.toolbox.StringUtils;

@RunWith(OrderedRunner.class)
public class TestTextSelection extends AbstractTestDocX {
	protected static final Logger logger = Logger.getLogger(TestTextSelection.class.getPackage().getName());

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManager();
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

	public static DocXDocument libraryDocument;
	public static DocXFragment fragment;
	public static DocXParagraph titleParagraph;
	public static DocXParagraph authorParagraph;
	public static DocXParagraph editionParagraph;
	public static DocXParagraph typeParagraph;
	public static DocXParagraph descriptionParagraph;

	@Test
	@TestOrder(4)
	public void testExampleLibraryLoading() throws FragmentConsistencyException {

		libraryDocument = getDocument("ExampleLibrary.docx");

		System.out.println("ExampleLibrary.docx:\n" + libraryDocument.debugStructuredContents());

		titleParagraph = (DocXParagraph) libraryDocument.getElements().get(6);
		authorParagraph = (DocXParagraph) libraryDocument.getElements().get(7);
		editionParagraph = (DocXParagraph) libraryDocument.getElements().get(8);
		typeParagraph = (DocXParagraph) libraryDocument.getElements().get(9);
		descriptionParagraph = (DocXParagraph) libraryDocument.getElements().get(10);

		fragment = (DocXFragment) libraryDocument.getFactory().makeFragment(titleParagraph, descriptionParagraph);

		StringBuffer sb = new StringBuffer();
		for (DocXElement element : fragment.getElements()) {
			if (element instanceof DocXParagraph) {
				DocXParagraph para = (DocXParagraph) element;
				for (FlexoRun run : para.getRuns()) {
					sb.append("[" + ((DocXTextRun) run).getText() + "]");
				}
				sb.append("\n");
			}
		}

		System.out.println(sb.toString());

	}

	@Test
	@TestOrder(5)
	public void testTextSelection1() throws FragmentConsistencyException {

		TextSelection<DocXDocument, DocXTechnologyAdapter> selection = fragment.makeTextSelection(authorParagraph, 0, 0);
		assertEquals("Author", selection.getRawText());

	}

	@Test
	@TestOrder(6)
	public void testTextSelection2() throws FragmentConsistencyException {

		TextSelection<DocXDocument, DocXTechnologyAdapter> selection = fragment.makeTextSelection(titleParagraph, 0, 1);
		assertEquals("Les misérables", selection.getRawText());

	}

	@Test
	@TestOrder(7)
	public void testTextSelection3() throws FragmentConsistencyException {

		TextSelection<DocXDocument, DocXTechnologyAdapter> selection = fragment.makeTextSelection(titleParagraph, 0, typeParagraph, 1);
		assertEquals("Les misérables" + StringUtils.LINE_SEPARATOR + "Author: Victor Hugo" + StringUtils.LINE_SEPARATOR + "Edition: Dunod"
				+ StringUtils.LINE_SEPARATOR + "Type: Roman", selection.getRawText());

	}

	@Test
	@TestOrder(8)
	public void testTextSelection4() throws FragmentConsistencyException {

		TextSelection<DocXDocument, DocXTechnologyAdapter> selection = fragment.makeTextSelection(editionParagraph, 0, 2, editionParagraph,
				2, 4);
		assertEquals("ition: Duno", selection.getRawText());

	}

	@Test
	@TestOrder(9)
	public void testTextSelection5() throws FragmentConsistencyException {

		TextSelection<DocXDocument, DocXTechnologyAdapter> selection = fragment.makeTextSelection(editionParagraph, 0, 1, editionParagraph,
				0, 5);
		assertEquals("diti", selection.getRawText());

	}

	@Test
	@TestOrder(10)
	public void testTextSelection6() throws FragmentConsistencyException {

		TextSelection<DocXDocument, DocXTechnologyAdapter> selection = fragment.makeTextSelection(authorParagraph, 1, 2, 1, -1);
		assertEquals("Victor Hugo", selection.getRawText());

	}

	@Test
	@TestOrder(11)
	public void testTextSelection7() throws FragmentConsistencyException {

		TextSelection<DocXDocument, DocXTechnologyAdapter> selection = fragment.makeTextSelection(descriptionParagraph, 0, 4,
				descriptionParagraph, 4, 4);
		System.out.println(selection.getRawText());
		assertEquals(
				"Misérables est un roman de Victor Hugo paru en 1862 (la première partie est publiée le 30 mars à Bruxelles par les Éditions Lacroix, Verboeckhoven et Cie, et le 3 avril de la même année à Paris1). Dans",
				selection.getRawText());

	}

}
