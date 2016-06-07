/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Openflexo-technology-adapters-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.gui.widget;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.doc.FlexoDocFragment.FragmentConsistencyException;
import org.openflexo.technologyadapter.docx.AbstractTestDocX;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.docx.model.DocXDrawingRun;
import org.openflexo.technologyadapter.docx.model.DocXParagraph;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test the structural and behavioural features of FIBDocXFragmentSelector when a table is involved
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestFIBDocXImageSelector extends AbstractTestDocX {

	// private static SwingGraphicalContextDelegate gcDelegate;

	private static FIBDocXImageSelector selector;

	@Test
	@TestOrder(2)
	public void test2InstanciateWidget() throws FragmentConsistencyException {

		DocXDocument documentWithImages = getDocument("DocumentWithSomeImages.docx");
		assertNotNull(documentWithImages);

		DocXDrawingRun drawingRun = (DocXDrawingRun) ((DocXParagraph) documentWithImages.getElements().get(4)).getRuns().get(0);

		System.out.println("Document: " + documentWithImages);
		System.out.println(documentWithImages.debugStructuredContents());

		assertNotNull(drawingRun);
		assertNotNull(drawingRun.getFlexoDocument());

		selector = new FIBDocXImageSelector(drawingRun);
		selector.setServiceManager(serviceManager);
		selector.setDocument(documentWithImages);
		selector.getCustomPanel();

		assertNotNull(selector.getSelectorPanel().getController());

		gcDelegate.addTab("FIBDocXImageSelector", selector.getSelectorPanel().getController());
	}

	/*public static void initGUI() {
		gcDelegate = new SwingGraphicalContextDelegate(TestFIBDocXImageSelector.class.getSimpleName());
	}
	
	@AfterClass
	public static void waitGUI() {
		gcDelegate.waitGUI();
	}
	
	@Before
	public void setUp() {
		gcDelegate.setUp();
	}
	
	@Override
	@After
	public void tearDown() throws Exception {
		gcDelegate.tearDown();
	}*/

}
