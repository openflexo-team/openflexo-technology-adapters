/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Fml-technologyadapter-ui, a component of the software infrastructure 
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

package org.openflexo.technologyadapter.docx.fib;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.ViewPoint;
import org.openflexo.foundation.fml.ViewPointLibrary;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.gina.swing.utils.FIBJPanel;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.docx.AbstractTestDocXInspector;
import org.openflexo.technologyadapter.docx.fml.DocXFragmentRole;
import org.openflexo.test.OrderedRunner;
import org.openflexo.test.TestOrder;

/**
 * Test StandardFlexoConceptView fib
 * 
 * @author sylvain
 * 
 */
@RunWith(OrderedRunner.class)
public class TestDocXFragmentRoleInspector extends AbstractTestDocXInspector {

	/*
	 * private static SwingGraphicalContextDelegate gcDelegate;
	 * 
	 * private static Resource fibResource;
	 * 
	 * static FlexoEditor editor;
	 */

	@Test
	@TestOrder(1)
	public void testLoadWidget() {

		fibResource = ResourceLocator.locateResource("Inspectors/DocX/DocXFragmentRole.inspector");
		assertTrue(fibResource != null);
	}

	@Test
	@TestOrder(2)
	public void testValidateWidget() throws InterruptedException {

		validateFIB(fibResource);
	}

	private static DocXFragmentRole role;

	@Test
	@TestOrder(3)
	public void loadConcepts() {

		ViewPointLibrary vpLib = serviceManager.getViewPointLibrary();
		assertNotNull(vpLib);
		ViewPoint viewPoint = vpLib.getViewPoint("http://openflexo.org/docx-test/TestLibraryViewPoint");
		assertNotNull(viewPoint);
		VirtualModel virtualModel = viewPoint.getVirtualModelNamed("DocumentVirtualModel");
		assertNotNull(virtualModel);
		FlexoConcept bookDescriptionSection = virtualModel.getFlexoConcept("BookDescriptionSection");
		assertNotNull(bookDescriptionSection);

		System.out.println(virtualModel.getFMLRepresentation());

		role = (DocXFragmentRole) bookDescriptionSection.getAccessibleProperty("section");
		System.out.println("role=" + role);
		assertNotNull(role);

		System.out.println("TextBindings=" + role.getTextBindings());

		System.out.println("fragment=" + role.getFragment());

		System.out.println(role.getFragment().getFlexoDocument().debugStructuredContents());
	}

	@Test
	@TestOrder(4)
	public void testInstanciateWidget() {

		System.out.println("On instancie le widget");
		System.out.println("role=" + role);
		System.out.println("fragment" + role.getFragment());

		FIBJPanel<DocXFragmentRole> widget = instanciateFIB(fibResource, role, DocXFragmentRole.class);

		gcDelegate.addTab("DocXFragmentRole", widget.getController());
	}

	/*
	 * public static void initGUI() { gcDelegate = new
	 * SwingGraphicalContextDelegate(TestDocXFragmentRoleInspector.class.
	 * getSimpleName()); }
	 * 
	 * @AfterClass public static void waitGUI() { gcDelegate.waitGUI(); }
	 * 
	 * @Before public void setUp() { gcDelegate.setUp(); }
	 * 
	 * @Override
	 * 
	 * @After public void tearDown() throws Exception { gcDelegate.tearDown(); }
	 */

}
