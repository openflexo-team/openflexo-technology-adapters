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
import static org.junit.Assert.assertNotSame;

import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.doc.FlexoDocFragment.FragmentConsistencyException;
import org.openflexo.foundation.fml.FMLModelFactory;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

@RunWith(OrderedRunner.class)
public class TestDocXFragmentConverter extends AbstractTestDocX {
	protected static final Logger logger = Logger.getLogger(TestDocXFragmentConverter.class.getPackage().getName());

	private static DocXDocument document;
	private static DocXFragment initialFragment;

	@AfterClass
	public static void tearDownClass() {

		unloadAndDelete(document);
		document = null;
		initialFragment = null;

		AbstractTestDocX.tearDownClass();
	}

	@Test
	@TestOrder(1)
	public void testInitializeServiceManager() throws Exception {
		instanciateTestServiceManagerForDocX(IdentifierManagementStrategy.ParaId);
	}

	@Test
	@TestOrder(2)
	public void testDocumentLoading() {

		document = getDocument("StructuredDocument.docx");

		System.out.println("Document:\n" + document.debugStructuredContents());

		assertEquals(13, document.getElements().size());

		assertEquals(12, document.getStyles().size());

	}

	@Test
	@TestOrder(3)
	public void testFragmentSerialisation() throws FragmentConsistencyException {

		DocXParagraph startParagraph = (DocXParagraph) document.getElements().get(7);
		DocXParagraph endParagraph = (DocXParagraph) document.getElements().get(11);

		System.out.println("start=" + startParagraph.getRawText());
		System.out.println("end=" + endParagraph.getRawText());

		initialFragment = (DocXFragment) document.getFactory().makeFragment(startParagraph, endParagraph);

		DocXFragmentConverter converter = new DocXFragmentConverter();

		System.out.println("fragment: " + converter.convertToString(initialFragment));

		assertEquals("http://openflexo.org/docx-test/TestResourceCenter/StructuredDocument.docx:6DC2CAFC:3146A934",
				converter.convertToString(initialFragment));
	}

	@Test
	@TestOrder(4)
	public void testFragmentDeserialisation() throws FragmentConsistencyException, ModelDefinitionException {

		FMLModelFactory factory = new FMLModelFactory(null, serviceManager);

		DocXFragmentConverter converter = new DocXFragmentConverter();

		String stringValue = "http://openflexo.org/docx-test/TestResourceCenter/StructuredDocument.docx:6DC2CAFC:3146A934";
		DocXFragment fragment = converter.convertFromString(stringValue, factory);

		System.out.println("fragment=" + fragment);

		assertNotNull(fragment);
		assertNotSame(fragment, initialFragment);
		assertEquals(fragment, initialFragment);

	}
}
